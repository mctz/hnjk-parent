package com.hnjk.edu.portal.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.model.HelpArticle;
import com.hnjk.edu.portal.model.HelpChannel;
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.edu.portal.service.IHelperArticleService;
import com.hnjk.edu.portal.service.IHelperChannelService;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.extend.taglib.tree.ZTreeNode;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 *  <code>ArticleController</code>文章管理控制器.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午07:00:57
 * @see 
 * @version 1.0
 */
@Controller
public class ArticleController extends FileUploadAndDownloadSupportController {
	
	private static final long serialVersionUID = 9095675867493724642L;
	
	@Autowired
	@Qualifier("articleService")
	private IArticleService articleService;
	
	@Autowired
	@Qualifier("channelService")
	private IChannelService channelService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("helperChannelService")
	private IHelperChannelService helperChannelService;
	
	@Autowired
	@Qualifier("helpArticleService")
	private IHelperArticleService helperArticleService;
	
	
	/**
	 * 获取资源-文章列表
	 * @param objPage
	 * @param title
	 * @param tags
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/portal/manage/article/resourcesArticle-list.html"})
	public String resourceArticleList(Page objPage, String title, ModelMap model,HttpServletRequest request) throws WebException{		
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);
		String fillinMan = request.getParameter("fillinMan");
		String channelId = request.getParameter("channelId");
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(title)) {
			condition.put("title", title);
		}
		if(ExStringUtils.isNotEmpty(fillinMan)) {
			condition.put("fillinMan", fillinMan);
		}
		if(ExStringUtils.isNotEmpty(channelId) && !"0".equals(channelId)) {
			condition.put("channelId", channelId);
		}
		//if(ExStringUtils.isNotEmpty(isPhotoNews))							condition.put("isPhotoNews", isPhotoNews);
		//if(ExStringUtils.isNotEmpty(isDraft))								condition.put("isDraft", isDraft);
		
//		if(cureentUser.getOrgUnit().getUnitType().equals("brSchool")){
//			condition.put("unitId", cureentUser.getOrgUnit().getResourceid());
//		}
		model.addAttribute("condition", condition);
		//只搜索出已经发布的信息
		condition.put("auditStatus", 1);		
//		List<Channel> allChanList = channelService.findChannelByParentAndchild();//查找所有栏目列表
//		model.addAttribute("allChanList", allChanList);
		Page page = articleService.findArticleByCondition(condition, objPage);
		model.addAttribute("articleList", page);
		return "/edu3/portal/system/article/resourcesArticle-list";
		
	}
	
	@RequestMapping(value={"/simpleDownloads.html"})
	public String resourceArticleListOutside(Page objPage, ModelMap model,HttpServletRequest request) throws WebException{		
		
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);
		Map<String, Object> condition = new HashMap<String, Object>();
		model.addAttribute("condition", condition);
		//只搜索出已经发布的信息
		condition.put("auditStatus", 1);		
		Page page = articleService.findArticleByCondition(condition, objPage);
		model.addAttribute("articleList", page);
		return "/simpleDownloads";		
	}
	
	/**
	 * 查看资源-文章页面
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/article/resourcesArticle-view.html")
	public String resourceArticleView(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{			
		String channelId = ExStringUtils.trimToEmpty(request.getParameter("channelId"));
		Channel channel = null;
		if(ExStringUtils.isNotBlank(resourceid)){
			Article article = articleService.get(resourceid);
			//如果附件不为空，则载入附件
			List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", resourceid);
			model.addAttribute("attachList", attachList);
			model.addAttribute("article", article);
			channel = article.getChannel();
		}else {
			Article article = new Article();
			if(ExStringUtils.isNotEmpty(channelId)){
				channel = channelService.get(channelId);
				article.setChannel(channel);
			}
			article.setSource("本站");
			model.addAttribute("article", article);
		}
		if(channel.getCourse()==null){//门户栏目
			List<Channel> allChanList = channelService.findChannelByParentAndchild();
			model.addAttribute("channelList", allChanList);
		} else {//课程栏目
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("courseId", channel.getCourse().getResourceid());
			List<Channel> allChanList = channelService.findCourseChannelByCondition(condition);
			model.addAttribute("channelList", allChanList);
		}
		//添加允许上传的文件类型和个数
		model.addAttribute("aceptFiletype", getAcepttype());
		model.addAttribute("aceptFilemax", getMaxfile());			
		model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置附件上传自动创建目录
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
		model.addAttribute("curUser",SpringSecurityHelper.getCurrentUser());
		return "/edu3/portal/system/article/resourcesArticle-view";
	}
	/**
	 * 文章列表
	 * @param objPage
	 * @param title
	 * @param tags
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/article/list.html")
	public String articleList(Page objPage, String title,String channelId,String isPhotoNews,String isDraft, ModelMap model,HttpServletRequest request) throws WebException{		
		objPage.setOrderBy("updateDate");
		objPage.setOrder(Page.DESC);
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			objPage.setOrderBy("fillinDate");
			objPage.setOrder(Page.DESC);
			
			Map<String, Object> condition = new HashMap<String, Object>();
			if(ExStringUtils.isNotEmpty(title)) {
				condition.put("title", title);
			}
			if(ExStringUtils.isNotEmpty(channelId) && !"0".equals(channelId)) {
				condition.put("channelId", channelId);
			}
			if(ExStringUtils.isNotEmpty(isPhotoNews)) {
				condition.put("isPhotoNews", isPhotoNews);
			}
			if(ExStringUtils.isNotEmpty(isDraft)) {
				condition.put("isDraft", isDraft);
			}
			
			if("brSchool".equals(cureentUser.getOrgUnit().getUnitType())){
				condition.put("unitId", cureentUser.getOrgUnit().getResourceid());
			}
			
			model.addAttribute("condition", condition);
			
			List<Channel> allChanList = channelService.findChannelByParentAndchild();//查找所有栏目列表
			model.addAttribute("allChanList", allChanList);
			Page page = articleService.findArticleByCondition(condition, objPage);
			model.addAttribute("articleList", page);
			
			return "/edu3/portal/system/article/article-list-sub";
		}else{
			String courseId = request.getParameter("courseId");//课程栏目课程id
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));
			if(ExStringUtils.isBlank(courseId)){//门户栏目			
				
				String parentCode = Channel.CHANNEL_ROOT;
//				if(cureentUser.getOrgUnit().getUnitType().
//						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员，则显示校外学习中心的栏目
//					parentCode = CacheAppManager.getSysConfigurationByCode("portal.brschool.chanel.root").getParamValue();
//				}
				
				try {
					List<Channel> list = channelService.findChannelTree(parentCode);
//					Map<String,Object> condition = new HashMap<String,Object>();
//					condition.put("parentId", "0");
//					condition.put("chanelIds", "'0','402880e528719d98012871a7c9b30007'");
//					List<Channel> list = channelService.findCourseChannelByCondition(condition);
					String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(list,parentCode,defaultCheckedValue));
					model.addAttribute("channelTree", jsonString);
				} catch (Exception e) {
					logger.error("输出文章列表-栏目树出错： "+e.fillInStackTrace());
				}
			} else {//课程栏目
				try {
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("courseId", courseId);
					List<Channel> list = channelService.findCourseChannelByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(list)){
						for (Channel channel : list) {
							if(channel.getParent()!=null){
								channel.setParentId(channel.getParent().getResourceid());
							}
						}
						String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(list,list.get(0).getResourceid(),defaultCheckedValue));
						model.addAttribute("channelTree", jsonString);
						model.addAttribute("courseId", courseId);
						model.addAttribute("channelId", defaultCheckedValue);
					}					
				} catch (Exception e) {
					logger.error("输出文章列表-栏目树出错： "+e.fillInStackTrace());
				}
			}				
			return "/edu3/portal/system/article/article-list";
		}
		
	}
	
	/**
	 * 编辑文章页面
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/article/input.html")
	public String addArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{			
		String channelId = ExStringUtils.trimToEmpty(request.getParameter("channelId"));
		Channel channel = null;
		if(ExStringUtils.isNotBlank(resourceid)){
			Article article = articleService.get(resourceid);
			//如果附件不为空，则载入附件
			List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", resourceid);
			model.addAttribute("attachList", attachList);
			model.addAttribute("article", article);
			channel = article.getChannel();
		}else {
			Article article = new Article();
			if(ExStringUtils.isNotEmpty(channelId)){
				channel = channelService.get(channelId);
				article.setChannel(channel);
			}
			article.setSource("本站");
			model.addAttribute("article", article);
		}		
		//List<Channel> channelList = channelService.findByCriteria(Restrictions.eq("channelStatus", 0),Restrictions.eq("isDeleted", 0));//文章栏目列表
		if(channel.getCourse()==null){//门户栏目
			List<Channel> allChanList = channelService.findChannelByParentAndchild();
			model.addAttribute("channelList", allChanList);
		} else {//课程栏目
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("courseId", channel.getCourse().getResourceid());
			List<Channel> allChanList = channelService.findCourseChannelByCondition(condition);
			model.addAttribute("channelList", allChanList);
		}
		//添加允许上传的文件类型和个数
		model.addAttribute("aceptFiletype", getAcepttype());
		model.addAttribute("aceptFilemax", getMaxfile());			
		model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置附件上传自动创建目录
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
		
		return "/edu3/portal/system/article/article-form";
	}
	
	/**
	 * 保存文章
	 * @param atrticle
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/portal/manage/article/save.html")
	public void saveArticle(Article atrticle,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		String channelId = ExStringUtils.defaultIfEmpty(request.getParameter("channelId"), "");//栏目ID	
		String orgUnitId =ExStringUtils.defaultIfEmpty(request.getParameter("orgUnitId"), "");//组织ID
		//获取附件
		String[] attachIds = request.getParameterValues("attachId");
		//if(null != attachIds && attachIds.length>0){
		//	atrticle.setIsAttach(Constants.BOOLEAN_YES);
		//}
		
		if(ExStringUtils.isNotEmpty(atrticle.getImgPath())) {
			atrticle.setIsPhotoNews(Constants.BOOLEAN_YES);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();//获取当前用户
		Map<String,Object> map = new HashMap<String, Object>();		
		
		try {	
			//设置栏目关联
			if(!ExStringUtils.isEmpty(channelId)){
				Channel channel = channelService.get(channelId);
				atrticle.setChannel(channel);
			}
			//设置所属单位
			if(!ExStringUtils.isEmpty(orgUnitId)){
				OrgUnit orgUnit = orgUnitService.get(orgUnitId);
				atrticle.setOrgUnit(orgUnit);
			}
		
			if(ExStringUtils.isNotEmpty(atrticle.getResourceid())){//更新
				Article destArticle = articleService.get(atrticle.getResourceid());
				ExBeanUtils.copyProperties(destArticle,atrticle);
				destArticle.setUpdateDate(new Date());
				articleService.updateArticle(destArticle, attachIds);//保存文章
			}else{//新增
				atrticle.setFillinManId(user.getResourceid());
				atrticle.setFillinMan(user.getCnName());
				atrticle.setOrgUnit(user.getOrgUnit());
				atrticle.setFillinDate(new Date());
				articleService.addArticle(atrticle,attachIds);
			}			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_PORTAL_ARTICLE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/portal/manage/article/input.html?resourceid="+atrticle.getResourceid());
	
		//	rendResponseStr = "{statusCode:200,message:'操作成功！',"	+ "reloadUrl:'" + request.getContextPath()+ "/edu3/portal/manage/article/input.html?resourceid="+ atrticle.getResourceid() + "'};";
		} catch (Exception e) {			
			logger.error("保存文章失败：{}",e.fillInStackTrace());
			//rendResponseStr = "{statusCode:300,message:'操作失败！'};";
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		//StringBuffer html = new StringBuffer();
		//html.append("<script>");
		//html.append("var response = "+rendResponseStr);
		//html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		//html.append("</script>");
		//renderHtml(response, html.toString());
		//return Constants.REQUEST_REDIRECT+"/edu3/portal/manage/article/input.html?resourceid="+atrticle.getResourceid();
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核发布文章
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/article/audit.html")
	public void auditArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();		
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				String type = ExStringUtils.defaultIfEmpty(request.getParameter("auditType"), "");				
				if(resourceid.split("\\,").length >1){
					List<Article> list = new ArrayList<Article>();
					for (int i = 0; i < resourceid.split("\\,").length; i++) {
						Article destArticle = articleService.get(resourceid.split("\\,")[i]);
						if("pass".equals(type)){
							destArticle.setAuditStatus(Article.AUDIT_STATUS_PASS);
						}else if("nopass".equals(type)){
							destArticle.setAuditStatus(Article.AUDIT_STATUS_NOTPASS);
						}						
						destArticle.setAuditDate(new Date());
						User user = SpringSecurityHelper.getCurrentUser();
						destArticle.setAuditManId(user.getResourceid());
						destArticle.setAuditMan(user.getCnName());
						list.add(destArticle);
					}					
					articleService.batchSaveOrUpdate(list);	
				}else{
					Article destArticle = articleService.get(resourceid);
					if("pass".equals(type)){
						destArticle.setAuditStatus(Article.AUDIT_STATUS_PASS);
					}else if("nopass".equals(type)){
						destArticle.setAuditStatus(Article.AUDIT_STATUS_NOTPASS);
					}
					destArticle.setAuditDate(new Date());
					User user = SpringSecurityHelper.getCurrentUser();
					destArticle.setAuditManId(user.getResourceid());
					destArticle.setAuditMan(user.getCnName());
					articleService.update(destArticle);
				}
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				//局部刷新(只有树节点+子页面时，才配置这个参数)
				map.put("localArea", "_articleListContent");
				//map.put("forward", request.getContextPath()+"/edu3/portal/manage/article/list.html");
			}
		} catch (Exception e) {
			logger.error("更新文章状态出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核发布出错<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/article/remove.html")
	public void removeArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					articleService.batchDelete(resourceid.split("\\,"));
				}else{//单个删除
					articleService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				//map.put("forward", request.getContextPath()+"/edu3/portal/manage/article/list.html");
				map.put("localArea", "_articleListContent");
			}
		} catch (Exception e) {
			logger.error("删除文章出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 图片上传
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/portal/manage/article/upload.html")
	public void uploadFile(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String result = "";
		try {
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"portal"+File.separator+"images"+File.separator+ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置上传路径
			setAcepttype("jpg|gif|png");
			List<AttachInfo> list = doUploadFile(request, response,null);
			AttachInfo attachInfo = list.get(0);
			//缓存在服务器上的物理路径
			String distSerAttachPath =  System.getProperty("xy.root") +"images"+File.separator+"cache"
								+File.separator+"portal"+File.separator+"articles"+File.separator+ExDateUtils.DATE_PATH_FORMAT.format(new Date());
			File distSerAttachPathFile = new File(distSerAttachPath);
			if(!distSerAttachPathFile.exists()){//如果目标文件不存在，就创建以日期命名的图片目录
				distSerAttachPathFile.mkdirs();
			}
			//上传到服务器上的源文件
			String sourceAttachPath = attachInfo.getSerPath()+File.separator+attachInfo.getSerName();
			//缓存到web缓存目录下
			FileUtils.copyFile(sourceAttachPath, distSerAttachPath+File.separator+attachInfo.getSerName());
			
			//缓存图片URL路径
		    String imgUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/images/cache/portal/articles/"+ExDateUtils.DATE_PATH_FORMAT.format(new Date())+"/"+attachInfo.getSerName();
			 result ="<script type=\"text/javascript\">parent.KE.plugin['image'].insert('content','"+ imgUrl + "','" 
			 + attachInfo.getAttName() + "','"+ 240+ "','" + 180 + "','" + 0+ "','" + "left" +"');" +
           		"alert('图片上传成功！');" +"</script>";

		} catch (Exception e) {
			result = "<script type=\"text/javascript\">图片上传失败，原因：\n"+e.getMessage()+"</script>";
			logger.error("上传文件失败：{}",e.fillInStackTrace());
		}
		logger.info(result);
		renderHtml(response, result);
	}
	
		
	/**
	 * 浏览服务器上的图片
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/portal/manage/article/filemanager.html")
	public void fileManager(HttpServletRequest request,HttpServletResponse response) throws WebException{		     
        // 根目录路径，可以指定绝对路径，比如 /var/www/attached/
		
        String root_path = System.getProperty("xy.root") + "images/";      
        String root_url = request.getContextPath() + "/images/";        
       
        //根据path参数，设置各路径和URL（除根目录之外所有子目录名称）   
        String path = request.getParameter("path");
        //    当前路径
        String current_path = "";
        //    当前URL
        String current_url = "";
        //    当前子路径
        String current_dir_path = "";
        //    上级路径
        String moveup_dir_path = "";

        if (path == null || "".equals(path)) {
            current_path = root_path;
            current_url = root_url;
            current_dir_path = "";
            moveup_dir_path = "";
        } else {
            current_path = root_path + path;
            current_url = root_url + path;
            current_dir_path = path;
            //    去掉当前目录之后即为上级目录路径
            moveup_dir_path = current_dir_path.substring(0, getUpIndex(current_dir_path));
        }

        //    排序形式，name or size or type
        String order = request.getParameter("order");
        order = (order == null) ? "name" : order.toLowerCase();
        
        //    目录不存在或不是目录
        File file = new File(current_path);
        if (!file.exists()) {
        	renderHtml(response, "目录或文件不存在...");           
            return;
        }

        //遍历目录取得文件信息       
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        File dir = new File(current_path);
		File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            Map<String, Object> file_list = new HashMap<String, Object>();
            File f_file = files[i];
            String filename = f_file.getName();
           
            //    是否是目录
            boolean is_dir = false;

            if (f_file.getPath().equals(root_path) || f_file.isDirectory()) {
                is_dir = true;
            }

            long filesize = 0;
            //    是否是图片
            boolean is_photo = false,
            //    如果是目录，目录下是否有图片
            has_file = true;
            String filetype = "";
            String[] file_ext = filename.trim().split("\\.");
            if (file_ext.length > 1) {
                filesize = f_file.length();
                is_photo = getExtType(file_ext[1]);
                has_file = false;
                filetype = file_ext[1];
            }

            file_list.put("is_dir", is_dir);        //是否文件夹
            file_list.put("has_file", has_file);    //文件夹是否包含文件
            file_list.put("filesize", filesize);    //文件大小
            file_list.put("is_photo", is_photo);    //是否图片
            file_list.put("filetype", filetype);    //文件类别，用扩展名判断
            file_list.put("filename", filename);     //文件名，包含扩展名      
            file_list.put("datetime", f_file.lastModified()); //文件最后一次访问时间   
           
            list.add(file_list);
        }
       
        //    利用比较器进行排序
        Collections.sort(list,getComparator(order));
       
        //对排序后的List进行构建json字符串
        StringBuffer file_list_str = new StringBuffer();
        file_list_str.append("[");
               
        for (int i=0; i < list.size(); i++){
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            System.out.println(list.size()+"//"+i+"=="+map);
            file_list_str.append("{");                   
            file_list_str.append("\"is_dir\":").append(Boolean.parseBoolean(map.get("is_dir").toString())).append(",");
            file_list_str.append("\"has_file\":").append(Boolean.parseBoolean(map.get("has_file").toString())).append(",");
            file_list_str.append("\"filesize\":\"").append(Long.parseLong(map.get("filesize").toString())).append("\",");
            file_list_str.append("\"is_photo\":").append(Boolean.parseBoolean(map.get("is_photo").toString())).append(",");
            file_list_str.append("\"filetype\":\"").append(map.get("filetype").toString()).append("\",");
             file_list_str.append("\"filename\":\"").append(map.get("filename").toString()).append("\",");            
            SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");           
            file_list_str.append("\"datetime\":\"").append(dateFormat.format(new Date(Long.parseLong(map.get("datetime").toString())))).append("\"}");
            if (i < files.length - 1) {
                file_list_str.append(",");
            }
        }
        file_list_str.append("]");
       
        StringBuffer json_str = new StringBuffer();
        //相对于根目录的上一级目录
        json_str.append("{\"moveup_dir_path\":\"" + moveup_dir_path).append("\",");
        //相对于根目录的当前目录
        json_str.append("\"current_dir_path\":\"" + current_dir_path).append("\",");
        //当前目录的URL
        json_str.append("\"current_url\":\"" + current_url).append("\",");
        //文件数
        json_str.append("\"total_count\":\"" + list.size()).append("\",");
        //文件列表数组
        json_str.append("\"file_list\":").append(file_list_str).append("}");
            
        renderText(response, json_str.toString());
	}
	
	/**
	 * 判断文件是否为图片
	 * @param file_suffix
	 * @return
	 */
	private boolean getExtType(String file_suffix) {
        String[] ext_arr = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
        boolean ext_type = false;
        for (int i = 0; i < ext_arr.length; i++) {
            if (file_suffix != null && file_suffix.equals(ext_arr[i])) {
                ext_type = true;
                break;
            }
        }
        return ext_type;
    }

   /**
    * 对文件进行排序
    * @param order
    * @return
    */
    private java.util.Comparator getComparator(final String order) {
        return new java.util.Comparator() {
            //    Override the method compare
            @Override
			@SuppressWarnings("unchecked")
			public int compare(Object a1, Object b1) {
                Map<String, Object> a = (Map<String, Object>) a1;
                Map<String, Object> b = (Map<String, Object>) b1;

                boolean a_dir = Boolean.parseBoolean(a.get("is_dir").toString());
                boolean b_dir = Boolean.parseBoolean(b.get("is_dir").toString());

                if (a_dir && !b_dir) {
                    return -1;
                } else if (!a_dir && b_dir) {
                    return 1;
                } else {
                    if ("size".equals(order)) {
                        long a_size = Long.parseLong(a.get("filesize").toString());
                        long b_size = Long.parseLong(b.get("filesize").toString());

                        if (a_size > b_size) {
                            return 1;
                        } else if (a_size < b_size) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else if ("type".equals(order)) {
                        return a.get("filetype").toString().compareTo(
                                b.get("filetype").toString());
                    } else {
                        return a.get("filename").toString().compareTo(
                                b.get("filename").toString());
                    }
                }
         
             }   
        };
    }

    /**
     * 获取上级目录的位置
     * @param current_dir_path
     * @return
     */
    private int getUpIndex(String current_dir_path) {
        int dex = current_dir_path.length()-1;
        int j=0;
        for (int i = dex; i >= 0; i--) {
            if (current_dir_path.charAt(i)=='/') {
				j++;
			}
            if (j == 2){
                dex = i+1;
                break;
            }           
        }
        if (j==1){
            dex = 0;
        }
        return dex;
    }

    
    
	/**
	 * 在线帮助文章列表
	 * @param objPage
	 * @param title
	 * @param tags
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/article/list.html")
	public String helpArticleList(Page objPage,String isPublish, String title,String channelId, ModelMap model,HttpServletRequest request) throws WebException{		
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			objPage.setOrderBy("fillinDate");
			objPage.setOrder(Page.DESC);
			
			Map<String, Object> condition = new HashMap<String, Object>();
			if(ExStringUtils.isNotEmpty(title))	{								
				condition.put("title", title);//标题
			}
			String rootId = request.getParameter("rootId");
			if(ExStringUtils.isNotEmpty(channelId) && !channelId.equals(rootId)){	
				condition.put("channelId", channelId);//所述栏目id
			}
			if(ExStringUtils.isNotEmpty(isPublish)){									
				condition.put("isPublish", isPublish);//标题	
			}
			model.addAttribute("condition", condition);
			List<HelpChannel> allChanList = helperChannelService.findHelpChannelByParentAndchild();//查找所有栏目列表
			model.addAttribute("allChanList", allChanList);
			objPage.setOrderBy("showOrder");
			objPage.setOrder("asc");
			Page page = helperArticleService.findHelpArticleByCondition(condition, objPage);
			
			model.addAttribute("helpArticleList", page);
			
			return "/edu3/portal/system/help/article-sublist";
		}else{
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("helpChannelName", "系统根栏目");
			List<HelpChannel> listHelpChannel = helperChannelService.findHelpChannelByParentAndchild(condition);
			if(listHelpChannel.size()==0){
				//没有设置系统的根栏目
			}else{
				String parentCode = listHelpChannel.get(0).getResourceid();//得到根栏目id
				try {
					List<HelpChannel> list = helperChannelService.findHelpChannelTree(parentCode);//这个是包含了根栏目的
					//处理在线帮助列表的显示顺序
					list=sortTree(list);
					String jsonString = JsonUtils.objectToJson(buildTreeById(list,parentCode,defaultCheckedValue));
					 
					model.addAttribute("rootId",parentCode);
					model.addAttribute("helpChannelTree", jsonString);
				} catch (Exception e) {
					logger.error("输出文章列表-栏目树出错： "+e.fillInStackTrace());
				}
			}
			return "/edu3/portal/system/help/article-list";
		}
		
	}
	/**
	 * 整理顺序之后的List<HelpChannel>的元素，失去了@transient的属性parentId,
	 * 从而，getParentNodeId就取不到parentId的值
	 * 目前把ZTreeNode的静态方法提出来，改成适合整理后List的方法。
	 * @param treeList
	 * @param rootNodeCode
	 * @param checkedValues
	 * @return
	 */
	public ZTreeNode buildTreeById(List<HelpChannel> treeList,String rootNodeCode,String checkedValues){
		ZTreeNode zTreeNode = null;		
		ZTreeNode zTreeNode1 = null;	
		Map<String,ZTreeNode> map = new HashMap<String, ZTreeNode>();
		for(HelpChannel treeNode:treeList){	
			if(treeNode.getNodeCode().equals(rootNodeCode)){				
				zTreeNode =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeId(),treeNode.getParentNodeId(),true,false,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
				map.put(treeNode.getNodeId(), zTreeNode);
			}else {
				String pid = null;
				if(null!=map.get(pid=treeNode.getParent().getResourceid())){
					ZTreeNode parentOrg = map.get(pid);
					zTreeNode1 =  new ZTreeNode(treeNode.getNodeName(),treeNode.getNodeId(),pid,true,false,treeNode.getNodeLevel(),new LinkedHashSet<ZTreeNode>());
					map.put(treeNode.getNodeId(), zTreeNode1);
					parentOrg.getNodes().add(zTreeNode1);				
				}
			}	
		}
		map = null;
		zTreeNode1 = null;
		return zTreeNode;
	}
	/**
	 * 按照每个层级按照showOrder值由小到大的先后顺序排列，整理查询出来的List<HelpChannel>
	 * 使用start with connect by prior的sql只能实现按照层级分层的List<HelpChannel>，不能实现showOrder的作用
	 * @param originalTree
	 * @return
	 */
	private List<HelpChannel> sortTree(List<HelpChannel> originalTree){
		List<HelpChannel> desTree = new ArrayList<HelpChannel>(0);
		//获得最大的栏目层级
		int maxChannelLevel = 0;
		for (HelpChannel helpChannel : originalTree) {
			if(maxChannelLevel<helpChannel.getChannelLevel()){
				maxChannelLevel = helpChannel.getChannelLevel();
			}
		}
		//从系统根栏目开始，从1级到n级，每一级按照showOrder排列，得到一个符合顺序的临时表
		List<HelpChannel> tmpList = new ArrayList<HelpChannel>(0);
		for (int i = 0; i <=maxChannelLevel; i++) {
			List<HelpChannel> tmpListinLoop = new ArrayList<HelpChannel>(0);
			tmpListinLoop=helperChannelService.findByHql(" from "+HelpChannel.class.getName()+" where channelLevel=? and isDeleted = 0  order by showOrder", i);
			for (HelpChannel helpChannel : tmpListinLoop) {
				tmpList.add(helpChannel);
			}
		}
		HelpChannel rootChannel = null;
		if(tmpList.size()>0){
			rootChannel = tmpList.get(0);
		}
		//tmpList的第一个节点为根节点，把根节点添加到最终的list中(这个地方跟ChannelController中的sortTree不同，因为在栏目管理中不需要见到系统根栏目，而在文章管理时，因为需要显示栏目的树形结构，需要系统根栏目)
		desTree.add(rootChannel);
		//得到树状遍历排序的list
		desTree = getChild(tmpList, desTree, rootChannel);
		return desTree; 
	}
	/**
	 * 递归方法：查找node的子节点
	 * @param list
	 * @param result
	 * @param node
	 * @return
	 */
	private List<HelpChannel> getChild(List<HelpChannel> list,List<HelpChannel> result,HelpChannel node){
		for (HelpChannel helpChannel : list) {
			if(null!=helpChannel.getParent()){
				if(node.getResourceid().equals(helpChannel.getParent().getResourceid())){
					result.add(helpChannel);
					getChild(list, result, helpChannel);
				}
			}
		}
		return result;
	}
	/**
	 * 在线帮助文档删除
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/article/remove.html")
	public void removeHelpArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					helperArticleService.batchDelete(resourceid.split("\\,"));
				}else{//单个删除
					helperArticleService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("localArea", "_helpArticleListContent");
			}
		} catch (Exception e) {
			logger.error("删除在线帮助文档文章出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 编辑在线帮助文章页面
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/article/input.html")
	public String addHelpArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{			
		String channelId = request.getParameter("channelId");
		String method = ExStringUtils.trimToEmpty(request.getParameter("method"));
		if(ExStringUtils.isNotBlank(resourceid)){//修改
			HelpArticle helpArticle = helperArticleService.findUniqueByProperty("resourceid", resourceid);
			//如果附件不为空，则载入附件
			List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", resourceid);
			model.addAttribute("attachList", attachList);
			model.addAttribute("helpArticle", helpArticle);
		}else {//新增
			HelpArticle helpArticle = new HelpArticle();
			if(ExStringUtils.isNotEmpty(channelId)){
				HelpChannel helpChannel = helperChannelService.get(channelId);
				helpArticle.setChannel(helpChannel);
			}
			model.addAttribute("helpArticle", helpArticle);
		}		
		List<HelpChannel> allChanList = helperChannelService.getAll();
		allChanList = sortTree(allChanList);
		//添加允许上传的文件类型和个数
		model.addAttribute("aceptFiletype", getAcepttype());
		model.addAttribute("aceptFilemax", getMaxfile());		
		model.addAttribute("allChanList", allChanList);
		model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置附件上传自动创建目录
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
		model.addAttribute("method",method);
		return "/edu3/portal/system/help/article-form";
	}
	
	/**
	 * 保存在线帮助文档
	 * @param atrticle
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/portal/manage/helper/article/save.html")
	public void saveHelpArticle(HelpArticle helpAtrticle,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		String method = ExStringUtils.trimToEmpty(request.getParameter("method"));
		String showOrder = "".equals(ExStringUtils.trimToEmpty(request.getParameter("showOrder")))?"0":ExStringUtils.trimToEmpty(request.getParameter("showOrder"));
		String param = null;
		String channelId = ExStringUtils.defaultIfEmpty(request.getParameter("channelId"), "");//栏目ID	
		//获取附件
		String[] attachIds = request.getParameterValues("attachId");
		
		User user = SpringSecurityHelper.getCurrentUser();//获取当前用户
		Map<String,Object> map = new HashMap<String, Object>();		
		
		try {	
			//设置栏目关联
			if(!ExStringUtils.isEmpty(channelId)){
				HelpChannel helpChannel = helperChannelService.get(channelId);
				helpAtrticle.setChannel(helpChannel);
			}
		
			if(ExStringUtils.isNotEmpty(helpAtrticle.getResourceid())){//更新
				HelpArticle destArticle = helperArticleService.get(helpAtrticle.getResourceid());
				//可更新的字段包括：内容 标题 关键字 填写人 填写人id 是否发布 以及解决状况
				destArticle.setContent(helpAtrticle.getContent());
				destArticle.setTags(helpAtrticle.getTags());
				destArticle.setTitle(helpAtrticle.getTitle());
				destArticle.setFillinManId(user.getResourceid());
				destArticle.setFillinMan(user.getCnName());
				destArticle.setFillinDate(new Date());
				destArticle.setIsPublish(helpAtrticle.getIsPublish());
				destArticle.setShowOrder(Integer.valueOf(showOrder));
				helperArticleService.updateHelpArticle(destArticle, attachIds);//保存文章
				
			}else{//新增
				helpAtrticle.setIsPublish(Constants.BOOLEAN_NO);
				//helpAtrticle.setUserFacebook(-1);
				helpAtrticle.setFillinManId(user.getResourceid());
				helpAtrticle.setFillinMan(user.getCnName());
				helpAtrticle.setFillinDate(new Date());
				helpAtrticle.setResolved(0);
				helpAtrticle.setUnresolved(0);
				helpAtrticle.setShowOrder(Integer.valueOf(showOrder));
				helperArticleService.addHelpArticle(helpAtrticle,attachIds);
			}			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_HELP_ARTICLE");
			if(("add").equals(method)){
				param="/edu3/portal/manage/helper/article/input.html?channelId="+channelId+"&method="+method;
			}else if(("edit").equals(method)){
				param="/edu3/portal/manage/helper/article/input.html?channelId="+channelId+"&resourceid="+helpAtrticle.getResourceid()+"&method="+method;
			}
			map.put("reloadUrl", request.getContextPath() +param);
		} catch (Exception e) {			
			logger.error("保存文章失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核发布文章
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/article/publish.html")
	public void auditHelpArticle(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();		
		try {
			if(!ExStringUtils.isEmpty(resourceid)){		
				String isPublish = ExStringUtils.defaultIfEmpty(request.getParameter("isPublish"), "");				
				if(resourceid.split("\\,").length >1){//批量审核
					List<HelpArticle> list = new ArrayList<HelpArticle>();
					for (int i = 0; i < resourceid.split("\\,").length; i++) {
						HelpArticle destArticle = helperArticleService.get(resourceid.split("\\,")[i]);
						if("publish".equals(isPublish)){
							destArticle.setIsPublish(Constants.BOOLEAN_YES);
						}else if("nopublish".equals(isPublish)){
							destArticle.setIsPublish(Constants.BOOLEAN_NO);
						}						
						destArticle.setFillinDate(new Date());
						User user = SpringSecurityHelper.getCurrentUser();
						destArticle.setFillinManId(user.getResourceid());
						destArticle.setFillinMan(user.getCnName());
						list.add(destArticle);
					}					
					helperArticleService.batchSaveOrUpdate(list);	
				}else{//单个审核
					HelpArticle destArticle = helperArticleService.get(resourceid);
					if("publish".equals(isPublish)){
						destArticle.setIsPublish(Constants.BOOLEAN_YES);
					}else if("nopublish".equals(isPublish)){
						destArticle.setIsPublish(Constants.BOOLEAN_NO);
					}	
					destArticle.setFillinDate(new Date());
					User user = SpringSecurityHelper.getCurrentUser();
					destArticle.setFillinManId(user.getResourceid());
					destArticle.setFillinMan(user.getCnName());
					helperArticleService.update(destArticle);
				}
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("localArea", "_helpArticleListContent");
				
			}
		} catch (Exception e) {
			logger.error("更新文章状态出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核发布出错<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 更新文章的问题解决度（更改已解决和未解决的数目）
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/manage/helper/article/resovle/update/ajax.html")
	public void changeResolve(HttpServletRequest request ,HttpServletResponse response) throws WebException{
		String userfacebook = request.getParameter("userfacebook");
		String articleResourceId = request.getParameter("articleResourceId");
		HelpArticle helpArticle = helperArticleService.findByHql(" from "+HelpArticle.class.getName()+" where resourceid=?", articleResourceId).get(0);
		Integer resolved = helpArticle.getResolved();
		Integer unResolved = helpArticle.getUnresolved();
		if ("-1".equals(userfacebook)) {
			unResolved++;
			helpArticle.setUnresolved(unResolved);
		}else{
			resolved++;
			helpArticle.setResolved(resolved);
		}
		helperArticleService.update(helpArticle);
	}
	
	
	
}
