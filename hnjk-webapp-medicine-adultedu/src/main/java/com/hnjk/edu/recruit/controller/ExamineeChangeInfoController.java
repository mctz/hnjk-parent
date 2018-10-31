package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.ExamineeChangeInfo;
import com.hnjk.edu.recruit.service.IExamineeChangeInfoService;
import com.hnjk.edu.recruit.vo.ExamineeChangeInfoVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

import edu.emory.mathcs.backport.java.util.Arrays;


/**
 * 考生异动信息Controller<p>
 * 
 * @author：zik  广东学苑教育发展有限公司
 * @since： 2015-12-31 下午15:30:22
 * @see
 * @version 1.0
 */
@Controller
public class ExamineeChangeInfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -840589902981667977L;
	
	@Autowired
	@Qualifier("examineeChangeInfoService")
	private IExamineeChangeInfoService examineeChangeInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;

	/**
	 * 获取转点记录列表
	 * 
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/examineeChangeInfo/list.html")
	public String listExamineeChangeInfo(HttpServletRequest request,HttpServletResponse response,Page objPage, ModelMap model) throws WebException {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			// 处理参数
			String recruitPlanId = request.getParameter("recruitPlanId");
			String rolloutSchool = request.getParameter("rolloutSchool");
			String rollinSchool = request.getParameter("rollinSchool");
			String classic = request.getParameter("classic");
			String major = request.getParameter("major");
			String  examineeName= request.getParameter("examineeName");
			String  examCertificateNo= request.getParameter("examCertificateNo");
			String enrolleeCode = request.getParameter("enrolleeCode");
			String rollType = request.getParameter("rollType");
			if(ExStringUtils.isEmpty(rollType)){
				rollType = "rollinOrOut";
			}
			
			User currentUser = SpringSecurityHelper.getCurrentUser();
			SysConfiguration unitTypeConfig = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype");
			boolean isBrschool = false;
			if(currentUser.getOrgUnit().getUnitType().equals(unitTypeConfig.getParamValue())){
				isBrschool = true;
				if("rollout".equals(rollType)){
					rolloutSchool = currentUser.getOrgUnit().getResourceid();
				} else if("rollin".equals(rollType)){
					rollinSchool = currentUser.getOrgUnit().getResourceid();
				} else {
					rolloutSchool = currentUser.getOrgUnit().getResourceid();
					rollinSchool = currentUser.getOrgUnit().getResourceid();
				}
				condition.put("rollType", rollType);
			}
			
			if(ExStringUtils.isNotEmpty(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotEmpty(rolloutSchool)){
				condition.put("rolloutSchool", rolloutSchool);
			}
			if(ExStringUtils.isNotEmpty(rollinSchool)){
				condition.put("rollinSchool", rollinSchool);
			}
			if(ExStringUtils.isNotEmpty(classic)){
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(major)){
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(examineeName)){
				condition.put("examineeName", examineeName);
			}
			if(ExStringUtils.isNotEmpty(enrolleeCode)){
				condition.put("enrolleeCode", enrolleeCode);
			}
			if(ExStringUtils.isNotEmpty(examCertificateNo)){
				condition.put("examCertificateNo", examCertificateNo);
			}
			
			// 根据条件获取转点记录列表
			Page examineeChangeInfoPage = examineeChangeInfoService.findByCondition(condition, objPage);
			
			model.addAttribute("condition", condition);
			model.addAttribute("isBrschool", isBrschool);
			model.addAttribute("examineeChangeInfoPage", examineeChangeInfoPage);
		} catch (Exception e) {
			logger.error(" 获取转点记录列表出错", e);
		}
		return "/edu3/recruit/examineeChangeInfo/examineeChangeInfo-list";
	}
	
	/**
	 * 转点记录打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/examineeChangeInfo/printView.html")
	public String printViewExamineeChangeInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "");
		String examineeChangeInfoIds = ExStringUtils.defaultIfEmpty(request.getParameter("examineeChangeInfoIds"), "");
		String recruitPlanId = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlanId"), "");
		String classic = ExStringUtils.defaultIfEmpty(request.getParameter("classic"), "");
		String major = ExStringUtils.defaultIfEmpty(request.getParameter("major"), "");
		String examineeName = ExStringUtils.defaultIfEmpty(request.getParameter("examineeName"), "");
		String enrolleeCode = ExStringUtils.defaultIfEmpty(request.getParameter("enrolleeCode"), "");
		String rollType = ExStringUtils.defaultIfEmpty(request.getParameter("rollType"), "");
		
		model.addAttribute("type", type);
		model.addAttribute("rollType", rollType);
		model.addAttribute("examineeChangeInfoIds", examineeChangeInfoIds);
		model.addAttribute("recruitPlanId", recruitPlanId);
		model.addAttribute("classic", classic);
		model.addAttribute("major", major);
		model.addAttribute("examineeName", examineeName);
		model.addAttribute("enrolleeCode", enrolleeCode);
		return "/edu3/recruit/examineeChangeInfo/examineeChangeInfo-printView";
	}
	
	/**
	 * 打印转点记录
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/examineeChangeInfo/print.html")
	public void printExamineeChangeInfo(HttpServletRequest request, HttpServletResponse response) throws WebException {
		String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "");
		String examineeChangeInfoIds = ExStringUtils.defaultIfEmpty(request.getParameter("examineeChangeInfoIds"), "");
		String recruitPlanId = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlanId"), "");
		String classic = ExStringUtils.defaultIfEmpty(request.getParameter("classic"), "");
		String major = ExStringUtils.defaultIfEmpty(request.getParameter("major"), "");
		String examineeName = ExStringUtils.defaultIfEmpty(request.getParameter("examineeName"), "");
		String enrolleeCode = ExStringUtils.defaultIfEmpty(request.getParameter("enrolleeCode"), "");
		String rollType = ExStringUtils.defaultIfEmpty(request.getParameter("rollType"), "rollinOrOut");
		
		JasperPrint jasperPrint = null;
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			User currentUser = SpringSecurityHelper.getCurrentUser();
			OrgUnit unit = currentUser.getOrgUnit();
			if(ExStringUtils.isNotEmpty(type) && "check".equals(type)){
				condition.put("examineeChangeInfoIds", Arrays.asList(examineeChangeInfoIds.split(",")));
			} else {// 按查询条件打印
				String rolloutSchool = "";
				String rollinSchool = "";
				if("rollout".equals(rollType)){
					rolloutSchool = unit.getResourceid();
				} else if("rollin".equals(rollType)){
					rollinSchool = unit.getResourceid();
				} else {
					rolloutSchool = unit.getResourceid();
					rollinSchool = unit.getResourceid();
				}
				condition.put("rollType", rollType);
				if(ExStringUtils.isNotEmpty(recruitPlanId)){
					condition.put("recruitPlanId", recruitPlanId);
				}
				if(ExStringUtils.isNotEmpty(rolloutSchool)){
					condition.put("rolloutSchool", rolloutSchool);
				}
				if(ExStringUtils.isNotEmpty(rollinSchool)){
					condition.put("rollinSchool", rollinSchool);
				}
				if(ExStringUtils.isNotEmpty(classic)){
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotEmpty(major)){
					condition.put("major", major);
				}
				if(ExStringUtils.isNotEmpty(examineeName)){
					condition.put("examineeName", examineeName);
				}
				if(ExStringUtils.isNotEmpty(enrolleeCode)){
					condition.put("enrolleeCode", enrolleeCode);
				}
			}
			List<ExamineeChangeInfo> examineeChangeInfoList = examineeChangeInfoService.findByCondition(condition);
			List<ExamineeChangeInfoVo> list = new ArrayList<ExamineeChangeInfoVo>();
			if(ExCollectionUtils.isNotEmpty(examineeChangeInfoList)){
				int orderNum = 0;
				for(ExamineeChangeInfo examineeChangeInfo : examineeChangeInfoList){
					ExamineeChangeInfoVo examineeChangeInfoVo = new ExamineeChangeInfoVo();
					orderNum++;
					examineeChangeInfoVo.setOrderNum(orderNum+"");
					examineeChangeInfoVo.setRecruitPlan(examineeChangeInfo.getRecruitPlan().getRecruitPlanname());
					examineeChangeInfoVo.setKsh(examineeChangeInfo.getEnrolleeInfo().getEnrolleeCode());
					examineeChangeInfoVo.setExamineeName(examineeChangeInfo.getExamineeName());
					examineeChangeInfoVo.setRollinSchool(examineeChangeInfo.getRollinSchool().getUnitName());
					examineeChangeInfoVo.setRolloutSchool(examineeChangeInfo.getRolloutSchool().getUnitName());
					examineeChangeInfoVo.setClassicName(examineeChangeInfo.getClassic().getClassicName());
					examineeChangeInfoVo.setMajorName(examineeChangeInfo.getMajor().getMajorName());
					examineeChangeInfoVo.setPhone(examineeChangeInfo.getEnrolleeInfo().getStudentBaseInfo().getContactPhone());
					examineeChangeInfoVo.setMemo(examineeChangeInfo.getEnrolleeInfo().getMemo());
					list.add(examineeChangeInfoVo);
				}
			}
			// 填充报表
			Map<String, Object> param = new HashMap<String, Object>();
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			param.put("title", schoolName+schoolConnectName);
			param.put("brschool", unit.getUnitName());
			param.put("logoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
			if(ExCollectionUtils.isNotEmpty(list)){
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						File.separator+"recruit"+File.separator+"examineeChangeInfo.jasper"),"utf-8");
				jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印转点记录出错", e);
		}
	}

}
