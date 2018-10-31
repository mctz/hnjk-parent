package com.hnjk.platform.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.PictureCarousel;
import com.hnjk.platform.system.service.IPictureCarouselService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 图片轮播Controller
 */
@Controller
public class PictureCarouselController extends BaseSupportController {

	private static final long serialVersionUID = -8435399123768472714L;

	@Qualifier("pictureCarouselService")
	@Autowired
	private IPictureCarouselService pictureCarouselService;
	
	
	/**
	 * 获取图片轮播列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/pictureCarousel/list.html")
	public String listPictureCarousel(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
	
		objPage.setOrderBy("showOrder");
		objPage.setOrder("asc");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String picName = request.getParameter("picName");
		if(ExStringUtils.isNotEmpty(picName)){
			condition.put("picName", picName);
		}
		
		String isShow = request.getParameter("isShow");
		if(ExStringUtils.isNotEmpty(isShow)){
			condition.put("isShow", isShow);
		}
		
		Page page = pictureCarouselService.findByCondition(condition, objPage);
		model.addAttribute("pictureCarouselList",page);
		model.addAttribute("condition", condition);
		
		return "/system/pictureCarousel/pictureCarousel-list";
	}
	
	/**
	 * 新增/编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/pictureCarousel/edit.html")
	public String editPictureCarousel(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		PictureCarousel pictureCarousel = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			pictureCarousel = pictureCarouselService.get(resourceid);	
		}else{ //----------------------------------------新增
			pictureCarousel = new PictureCarousel();			
		}
		model.addAttribute("pictureCarousel", pictureCarousel);
		model.addAttribute("storePath", "psictureCarousel");
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		/*//本地测试
		model.addAttribute("rootUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/cover/");
		*/
		return "/system/pictureCarousel/pictureCarousel-form";
	}
	
	/**
	 * 保存/更新图片轮播
	 * @param setTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/pictureCarousel/save.html")
	public void savePictureCarousel(PictureCarousel pictureCarousel,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		Date today = new Date();
		try {
			do{
				if(ExStringUtils.isEmpty(ExStringUtils.trimToEmpty(pictureCarousel.getPicName()))){
					statusCode = 300;
					message = "图片名称不能为空！";
					continue;
				} 
				User user = SpringSecurityHelper.getCurrentUser();
				if(user == null){
					statusCode = 300;
					message = "登录用户不存在,请重新登录";
					continue;
				} 
				
				pictureCarousel.setPicName(ExStringUtils.trim(pictureCarousel.getPicName()));
				PictureCarousel _pictureCarousel = null;
				if(ExStringUtils.isNotBlank(pictureCarousel.getResourceid())){ //  更新
					_pictureCarousel = pictureCarouselService.get(pictureCarousel.getResourceid());	
					pictureCarousel.setCreateDate(_pictureCarousel.getCreateDate());
				}else{ // 保存
					_pictureCarousel = new PictureCarousel();
					pictureCarousel.setCreateDate(today);
				}		
				if(!ExStringUtils.trimToEmpty(_pictureCarousel.getPicName()).equals(pictureCarousel.getPicName())){
					PictureCarousel hasPicName = pictureCarouselService.getByPicName(pictureCarousel.getPicName());
					if(hasPicName != null){
						statusCode = 300;
						message = "该图片名称已经存在！";
						continue;
					}
				}
				pictureCarousel.setOperatprId(user.getResourceid());
				ExBeanUtils.copyProperties(_pictureCarousel, pictureCarousel);
				_pictureCarousel.setUpdateDate(today);
				pictureCarouselService.saveOrUpdate(_pictureCarousel);
				
				//map.put("navTabId", "RES_SYS_PICTURECAROUSEL_UPDATE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/system/pictureCarousel/edit.html?resourceid="+_pictureCarousel.getResourceid());
			} while(false);
		}catch (Exception e) {
			logger.error("保存轮播图片出错",e);
			statusCode = 300;
			message = "保存失败！";
		}finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批量删除图片轮播
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/pictureCarousel/delete.html")
	public void deletePictureCarousel(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceids = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceids)){
				if(resourceids.split(",").length >1){//批量删除					
					pictureCarouselService.batchCascadeDelete(resourceids.split(","));
				}else{//单个删除
					pictureCarouselService.delete(resourceids);
				}
				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/system/pictureCarousel/list.html");
			}
		} catch (Exception e) {
			logger.error("删除图片轮播出错",e);
			map.put("statusCode", 300);
			map.put("message", "删除失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
}
