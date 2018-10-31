package com.hnjk.edu.learning.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;
/**
 * 网上学习时间设置管理
 * <code>LearningTimeSettingController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-6 下午03:37:41
 * @see 
 * @version 1.0
 */
@Controller
public class LearningTimeSettingController extends BaseSupportController {

	private static final long serialVersionUID = 7484174547555751900L;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService; 
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;  
	
	protected static Logger logger = LoggerFactory.getLogger(BaseSupportController.class);
	
	/**
	 * 网上学习时间列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/learningtimesetting/list.html")
	public String listLearningTimeSetting(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("yearInfo.firstYear desc,term");
		objPage.setOrder(Page.DESC);		
		
		String yearInfoId = request.getParameter("yearInfoId");	
		String term = request.getParameter("term");	
		
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotBlank(term)) {
			condition.put("term", term);
		}
		
		Page page = learningTimeSettingService.findLearningTimeSettingByCondition(condition, objPage);		
		model.addAttribute("learningTimeSettingList", page);
		model.addAttribute("condition", condition);
		return "/edu3/learning/learningtimesetting/learningtimesetting-list";
	}
	
	/**
	 * 网上学习时间列表_触发自动计算平时分
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/learningtimesetting/autoCalucate.html")
	public String autoCalucate(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("yearInfo.firstYear desc,term");
		objPage.setOrder(Page.DESC);		
//		String str = usualResultsService.timingCalculateAndSubmitUsualResults();
		
//		String message = learningTimeSettingService.timingCalculateAndSubmitUsualResults();
		String message = timingCalculateAndSubmitUsualResults();
		System.out.println(message);
		
		String yearInfoId = request.getParameter("yearInfoId");	
		String term = request.getParameter("term");	
		
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotBlank(term)) {
			condition.put("term", term);
		}
		
		Page page = learningTimeSettingService.findLearningTimeSettingByCondition(condition, objPage);		
		model.addAttribute("learningTimeSettingList", page);
		model.addAttribute("condition", condition);
		return "/edu3/learning/learningtimesetting/learningtimesetting-list";
	}
	
	
	/**
	 * 定时计算并提交平时成绩（在学习时间结束后）
	 * 注：计算网络教育类课程的成绩
	 * @return
	 */
	public String timingCalculateAndSubmitUsualResults()  {
		
		LearningTimeSetting  setting 		    = null;
		Date curTime                            = ExDateUtils.getCurrentDateTime();
		Date cachTime                           = null;
		StringBuffer returnStr       		    = new StringBuffer("");
		int counts                     	    	= 0 ;
		try {
			cachTime                            = ExDateUtils.parseDate(CacheAppManager.getSysConfigurationByCode("usualResultsCalculateTime").getParamValue(),ExDateUtils.PATTREN_DATE);
		} catch (Exception e1) {
			cachTime                            = curTime;
		}
		try {
			curTime                             = ExDateUtils.formatDate(curTime,ExDateUtils.PATTREN_DATE);//当前系统日期
			
			String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();

			if (null!=yearTerm) {
				String[] ARRYyterm = yearTerm.split("\\.");
				List<LearningTimeSetting> sets  = learningTimeSettingService.findLearningTimeSettingListByCondition(ARRYyterm[0],ARRYyterm[1]);
				
				
				if(null!=sets&&!sets.isEmpty()) {
					setting = sets.get(0);
				}
			}	
			//学习时间结束第二天凌晨计算平时分,或者是全局参数中设置的时间
			if ((null!=setting&&setting.getEndTime()!=null&&
				 curTime.getTime()  == ExDateUtils.formatDate(ExDateUtils.addDays(setting.getEndTime(),1),ExDateUtils.PATTREN_DATE).getTime())||
				 cachTime.getTime() == curTime.getTime() ) {
				String yearInfoId = setting.getYearInfo().getResourceid();
				String term = setting.getTerm();
				List<Map<String,Object>> cids   = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select distinct i.courseid courseid,count(stp.resourceid) counts from edu_learn_stuplan stp inner join edu_teach_examinfo i on stp.examinfoid = i.resourceid where stp.isdeleted = ? and stp.orderexamyear = ? and stp.orderexamterm = ? and i.isdeleted=0 group by i.courseid order by counts desc", new Object []{0,yearInfoId,term});
				long startTime = System.nanoTime();
				for (Map<String,Object> map : cids) {
					try{
						counts +=usualResultsService.saveAllUsualResultsInt(yearInfoId,term,map.get("courseid").toString());
					}catch (Exception e) {
						returnStr.append(e.getMessage());
						continue;
					}
					logger.warn("完成计算课程"+map.get("courseid").toString());
					logger.warn("已计算"+counts+"条记录！");
				}
				// ... the code being measured ...
				long estimatedTime = System.nanoTime() - startTime;
				returnStr.append("计算并提交平时成绩成功，共"+counts+"条记录！");
				logger.warn("计算并提交平时成绩成功，共"+counts+"条记录！");
			}else {
				returnStr.append("未设置学习时间,或者当前时间跟学习结束时间不是同一天！");
			}
		} catch (Exception e) {
			String msg 					  		 = "定时计算并提交平时成绩出错：{}"+e.fillInStackTrace();
			List<User> adminUsers         		 = userService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.like("cnName","系统管理员"));
			sysMsgService.sendMsg(adminUsers.get(0).getResourceid(),adminUsers.get(0).getCnName(),adminUsers.get(0).getOrgUnit(),
								  "tips","<font color='red'>定时计算并提交平时成绩出错</font>",msg,"ROLE_ADMINISTRATOR","role","","timingCalculateAndSubmitUsualResults");
			logger.debug(msg);
			returnStr.append(msg);
			e.printStackTrace();
			throw new ServiceException(msg);
		}
		return returnStr.toString();
	}
	

	/**
	 * 新增编辑学习时间
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/learningtimesetting/input.html")
	public String editLearningTimeSetting(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{		
		LearningTimeSetting learningTimeSetting = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			learningTimeSetting = learningTimeSettingService.get(resourceid);
		}else{ //----------------------------------------新增
			learningTimeSetting = new LearningTimeSetting();
		}	
		model.addAttribute("learningTimeSetting", learningTimeSetting);
		return "/edu3/learning/learningtimesetting/learningtimesetting-form";
	}

	/**
	 * 保存网上学习时间
	 * @param yearInfoId
	 * @param learningTimeSetting
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/learningtimesetting/save.html")
	public void saveLearningTimeSetting(String yearInfoId,LearningTimeSetting learningTimeSetting,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {			
			if(ExStringUtils.isNotEmpty(yearInfoId) && ExStringUtils.isNotBlank(learningTimeSetting.getTerm())){
				YearInfo yearInfo = yearInfoService.get(yearInfoId);
				learningTimeSetting.setYearInfo(yearInfo);
			
				if(ExStringUtils.isBlank(learningTimeSetting.getResourceid())){
					LearningTimeSetting setting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, learningTimeSetting.getTerm());
					if(setting!=null){
						throw new WebException(yearInfo.getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", learningTimeSetting.getTerm())+"已经设置了网上学习时间");
					}
				}				
				if(ExStringUtils.isNotBlank(learningTimeSetting.getResourceid())){ //--------------------更新				
					LearningTimeSetting p_setting = learningTimeSettingService.get(learningTimeSetting.getResourceid());
					ExBeanUtils.copyProperties(p_setting, learningTimeSetting);
					learningTimeSettingService.update(p_setting);
				}else{ //-------------------------------------------------------------------保存				
					learningTimeSettingService.save(learningTimeSetting);
				}				
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_TEACHING_LEARNINGTIMESETTING_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/learningtimesetting/input.html?resourceid="+learningTimeSetting.getResourceid());
			} else {
				map.put("statusCode", 300);
				map.put("message", "请选择年度和学期!");
			}
		}catch (Exception e) {
			logger.error("设置网上学习时间出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除网上学习时间
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/learningtimesetting/remove.html")
	public void removeLearningTimeSetting(String resourceid,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				learningTimeSettingService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");		
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
