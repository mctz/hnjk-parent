package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.NationMajor;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.INationMajorService;
import com.hnjk.edu.recruit.helper.ComparatorBranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.BranchShoolNewMajor;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IBranchShoolNewMajorService;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;
import com.opensymphony.workflow.spi.WfAgent;

/**
 * 
 * <code>RecruitMajorManageController</code><p>
 * 招生专业申报.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-9-9 上午10:08:30
 * @see 
 * @version 1.0
 */
@Controller
public class RecruitMajorManageController extends BaseSupportController{

	private static final long serialVersionUID = 7975380378695874456L;

	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;
	
	@Autowired
	@Qualifier("brschMajorSettingService")
	private IBrschMajorSettingService brschMajorSettingService;
	
	
	@Autowired
	@Qualifier("nationMajorService")
	private INationMajorService nationMajorService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("branchShoolNewMajorService")
	private IBranchShoolNewMajorService branchShoolNewMajorService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
//	@Autowired
//	@Qualifier("examSubjectSetService")
//	private IExamSubjectSetService examSubjectSetService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 学习中心招生专业列表
	 * @param request
	 * @param objPage
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschoolmajor-list.html")
	public String listBranchSchoolMajor(HttpServletRequest request,Page objPage,  HttpServletResponse response,ModelMap model)throws WebException{
		
		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("plan.isAudited");
		
		Map<String,Object> condition 	  = new HashMap<String,Object>();
		List<Map<String,Object>> wfIdList = new ArrayList<Map<String,Object>>();
		List<String> roleId          	  = new ArrayList<String>();
		String recruitplanName 		 	  = ExStringUtils.trimToEmpty(request.getParameter("recruitplanName"));
		String recruitplanId 		 	  = ExStringUtils.trimToEmpty(request.getParameter("recruitplanId"));
		String teachingType    		 	  = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String branchSchool    		 	  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String isAudited             	  = ExStringUtils.trimToEmpty(request.getParameter("isAudited"));
		String fillinMan			 	  = ExStringUtils.trimToEmpty(request.getParameter("fillinMan"));
		String fillinDate			 	  = ExStringUtils.trimToEmpty(request.getParameter("fillinDate"));
		String selectFlag            	  = ExStringUtils.trimToEmpty(request.getParameter("selectFlag"));

		boolean isAdmin              	  = false;
		String curUserId             	  = "";
		String wfid                       = "";
		
		User user 					 	  = SpringSecurityHelper.getCurrentUser();
		curUserId                    	  = user.getResourceid();
		
		for(Role r:user.getRoles()){
			roleId.add(r.getResourceid());
			if (r.getRoleCode().indexOf(CacheAppManager.getSysConfigurationByCode("sysuser.role.admin").getParamValue())>-1) {
				isAdmin                   = true;
			}
		}
		if (ExStringUtils.isEmpty(selectFlag)) {
			selectFlag                    = "DB";
		}
		
		if (!isAdmin&&!"ALL".equals(selectFlag)&&!"BACK".equals(selectFlag)) {
			try {
				wfIdList                  = getCurrentUserWorkFlowerId(selectFlag,curUserId,roleId);
			} catch (Exception e) {
				logger.error("学习中心专业申报管理-获取当前用户的待办、已办出错:{}"+e.fillInStackTrace());
			}
		}else if ("BACK".equals(selectFlag)) {
			condition.put("tabIndex", 1); //用于用户点击查看退回流程功能时进入审核界面默认显示流程信息TAB页
			try {
				wfIdList                  = getCurrentUserBackFlowerId(curUserId);
			} catch (Exception e) {
				logger.error("学习中心专业申报管理-获取当前用户退回的流程出错:{}"+e.fillInStackTrace());
			}
		}
		

		if (!wfIdList.isEmpty()) {
			for (Map<String,Object> map:wfIdList) {
				wfid += ",'"+map.get("ID")+"'";
			}
		}
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//学习中心
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("isBranchSchool", "Y");
		}
		condition.put("selectFlag", selectFlag);
		if(ExStringUtils.isNotEmpty(recruitplanName)) {
			condition.put("recruitplanName", recruitplanName);
		}
		if(ExStringUtils.isNotEmpty(recruitplanId)) {
			condition.put("recruitplanId", recruitplanId);
		}
		if(ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(isAudited)) {
			condition.put("isAudited", isAudited);
		}
		if(ExStringUtils.isNotEmpty(fillinMan)) {
			condition.put("fillinMan", fillinMan);
		}
		if(ExStringUtils.isNotEmpty(fillinDate)) {
			condition.put("fillinDate", fillinDate);
		}
		if(ExStringUtils.isNotEmpty(wfid)) {
			condition.put("wfid", wfid.substring(1));
		}
		
		Page page 		  			 = branchSchoolPlanService.getBrsPlanListByCondition(objPage, condition);

		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/recruit/recruitmanage/branchschoolmajor-list";
	}
	/**
	 * 根据查询范围获取当前用户的流程实例
	 * @param selectFlag
	 * @return
	 * @throws Exception 
	 */
	private List<Map<String,Object>> getCurrentUserWorkFlowerId(String selectFlag,String userId,List<String> roleId) throws Exception{
		
		StringBuffer sql = new StringBuffer();
	
		if ("DB".equals(selectFlag)) {
			
			sql.append(" select distinct( e.id) from os_currentstep s,OS_WFENTRY e where s.entry_id = e.id and e.state = '1'");
		    sql.append(" and e.name = 'brsrecruitplan' and (s.owner  like  '%"+userId+"%'");
		    for (String rid :roleId) {
		    	sql.append(" or s.owner like '%"+rid+"%'  ");
			}
		    sql.append(" )");
		    sql.append(" order by e.id");
		    
		}else if ("YB".equals(selectFlag)) { 
			sql.append(" select distinct( e.id)  from os_historystep hs,OS_WFENTRY e where hs.entry_id = e.id and e.name = 'brsrecruitplan'");
			sql.append("  and hs.caller like '%"+userId+"%' order by e.id ");
		}
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), new HashMap<String, Object>());
	}
	/**
	 * 获取当前用户退回的流程实例ID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> getCurrentUserBackFlowerId(String userId)throws Exception{
		String sql = "select  distinct(t.entry_id) as ID from os_historystep t where t.action_id in('3','5' ,'7') and t.caller like '%"+userId+"%'";
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, new HashMap<String, Object>());
	}
	/**
	 * 招生专业申报表单
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschoolmajor-form.html")
	public String branchSchoolMajorForm(HttpServletRequest request, ModelMap model) throws WebException {
		
		String resourceid 			  = request.getParameter("resourceid");					// 获取表单ID
		String wf_id 	  			  = ExStringUtils.trimToEmpty(request.getParameter("wf_id"));	// 获取流程实例ID
		String APP_FROM   			  = ExStringUtils.trimToEmpty(request.getParameter("APP_FROM"));
		String tabIndex               = ExStringUtils.trimToEmpty(request.getParameter("tabIndex"));
		User user 					  = SpringSecurityHelper.getCurrentUser();
		String userUnitId             = user.getOrgUnit().getResourceid();
		String userSchoolType         = user.getOrgUnit().getSchoolType();
	 	List <RecruitPlan> list 	  = new ArrayList<RecruitPlan>();
		List <RecruitPlan> applyPlan  = new ArrayList<RecruitPlan>();


		BranchSchoolPlan brSchoolPlan = null;
		String isBrschool             = "N";
		if (ExStringUtils.isNotEmpty(tabIndex)) {
			model.put("tabIndex", tabIndex);
		}
		if(null!=APP_FROM && StringUtils.isNotEmpty(wf_id)) {
			brSchoolPlan 			  = branchSchoolPlanService.findUniqueByProperty("wfid", Long.parseLong(wf_id));
		}else{
			brSchoolPlan 			  = StringUtils.isNotEmpty(resourceid)?branchSchoolPlanService.get(resourceid): new BranchSchoolPlan();
		}
		if (null!=brSchoolPlan.getBranchShoolNewMajor() && !brSchoolPlan.getBranchShoolNewMajor().isEmpty()) {
			String brMajorHQL         = "from "+Attachs.class.getSimpleName()+" att where att.isDeleted=0 and  att.formType='brSchoolAppNewMajor' and att.formId=?";
			List<Attachs> attachsList = attachsService.findByHql(brMajorHQL, brSchoolPlan.getResourceid());
			brSchoolPlan.setAttachs(attachsList);
		}
		try {
			list                	  = recruitPlanService.getPublishedPlanList("forManager");
		} catch (Exception e) {
			logger.error("招生专业申报表单-获取开放的招生批次出错：{}",e.fillInStackTrace());
		}
		
		for (int i = 0; i < list.size(); i++) {
			
			RecruitPlan plan 		  = list.get(i);
			String planTeachingType   = plan.getTeachingType();
			boolean isAllow           = false;
			
			//招生批次学习模式不为空，指定了学习中心，且当前在指定范围内，允许申报
			if (ExStringUtils.isNotEmpty(planTeachingType) & ExStringUtils.isNotEmpty(plan.getBrSchoolIds())) {
				String brSchoolIds    = plan.getBrSchoolIds();
				if (brSchoolIds.indexOf(userUnitId)>=0){
					applyPlan.add(plan);
				}
			
			//招生批次学习模式不为空，没有指定学习中心，则当前用户具有的办学模式在批次中，允许申报
			}else if (ExStringUtils.isNotEmpty(planTeachingType) & !ExStringUtils.isNotEmpty(plan.getBrSchoolIds())) {
				
				if (ExStringUtils.isNotEmpty(userSchoolType)) {
					
				  String[] userSchoolTypes = userSchoolType.split(",");
				  
				  for (int j = 0; j < userSchoolTypes.length; j++) {
					String type			   = userSchoolTypes[j];
					if (planTeachingType.indexOf(type)>=0){      
						isAllow            = true;
						break;
					}
				  }
				}else {
					isAllow            	   = false;
				}
				
				if (isAllow) {
					applyPlan.add(plan);
				}
			//招生批次学习模式为空，没有指定学习中心，默认对所有学习中心开放	
			}else {
				applyPlan.add(plan);
			}
		}
		
		for (Role r:user.getRoles()) {
			if ("ROLE_RECRUIT_LEADER".equals(r.getRoleCode())) {
				model.addAttribute("isRecruitLeader","Y");
			}
		}
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(null==user?"":user.getOrgUnit().getUnitType())){//学习中心
			 isBrschool        		  = "Y";
			 model.addAttribute("unitID", user.getOrgUnit().getResourceid());
			 model.addAttribute("isAllowNewMajor", user.getOrgUnit().getIsAllowNewMajor());
		}else{
			 isBrschool       		  = "N";
		}
		/*List<BranchSchoolMajor> brLi  = new ArrayList<BranchSchoolMajor>(brSchoolPlan.getBranchSchoolMajor());
		ComparatorBranchSchoolMajor crm    = new  ComparatorBranchSchoolMajor();
		if (null!=brSchoolPlan.getBranchSchoolMajor()) {
			Collections.sort(brLi, crm);
		}*/
		model.addAttribute("isBrschool", isBrschool); 
		model.addAttribute("brSchoolPlan", brSchoolPlan);
		//model.addAttribute("branchSchoolMajorList",brLi);
		model.addAttribute("recruitPlanList", applyPlan);
		model.addAttribute("currentUser",user);
		try {
			 if(ExStringUtils.isNotBlank(brSchoolPlan.getResourceid()) && ExCollectionUtils.isNotEmpty(brSchoolPlan.getBranchShoolNewMajor())){
				 String sql = "select t.*,t.basemajorid baseMajor,c.classicname,m.majorname from edu_recruit_brnewmajor t join edu_base_classic c on c.resourceid=t.classicid join edu_base_major m on m.resourceid=t.basemajorid where t.isdeleted=0 and t.brschplanid=? order by t.teachingtype,t.resourceid";
				 List<BranchShoolNewMajor> newMajorList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, new Object[]{brSchoolPlan.getResourceid()}, BranchShoolNewMajor.class);
				model.addAttribute("newMajorList",newMajorList);
			 }			
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		
		return "/edu3/recruit/recruitmanage/branchschoolmajor-form";
	}
	
	/**
	 * 保存招生专业申报信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschoolmajor-save.html")
	public void saveRecruitMajor(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String,Object> param            = new HashMap<String, Object>();
		Map<String, Object> map 		    = new HashMap<String, Object>();
		Date currentDate                    = new Date();
		User user 						    = SpringSecurityHelper.getCurrentUser();
		
		//--------------------申报招生批次中没有的专业的参数--------------------
		String[] uploadNewMajorFileFileIds  = request.getParameterValues("uploadNewMajorFileFileId"); //上件的附件ID
		
		String[] baseMajors       		    = request.getParameterValues("baseMajor");			     		   //基础专业库专业ID
		String[] teachingTypes_baseMajor    = request.getParameterValues("teachingType_baseMajor");            //办学模式 ---从基础专业库中申报新专业
		String[] classicIds_baseMajor       = request.getParameterValues("classic_baseMajor");				   //层次           ---从基础专业库中申报新专业                     
		String[] dicrects_baseMajor         = request.getParameterValues("dicrect_baseMajor");				   //专业方向 ---从基础专业库中申报新专业
		String[] address_baseMajor 		    = request.getParameterValues("address_baseMajor");                 //办学地址 ---从基础专业库中申报新专业
		String[] scopes_baseMajor    	    = request.getParameterValues("scope_baseMajor");                   //招生范围 ---从基础专业库中申报新专业
		String[] shapes_baseMajor           = request.getParameterValues("shape_baseMajor");                   //形式	 ---从基础专业库中申报新专业
		String[] memos_baseMajor            = request.getParameterValues("memo_baseMajor");                    //备注           ---从基础专业库中申报新专业
		String[] limitNums_baseMajor        = request.getParameterValues("limitNum_baseMajor");                //指标数      ---从基础专业库中申报新专业
		String[] lowerNums_baseMajor        = request.getParameterValues("lowerNum_baseMajor");                //下限数      ---从基础专业库中申报新专业
		String[] studyperiod_baseMajor      = request.getParameterValues("studyperiod_baseMajor");   //学制	
		String[] majorDescript_baseMajor      = request.getParameterValues("majorDescript_baseMajor");   //专业介绍
		
		String[] nationMajors    		    = request.getParameterValues("nationMajor");			  	       //国家专业库专业ID
		String[] nationmajorParentCatolog   = request.getParameterValues("nationmajorParentCatolog"); 		   //专业大类
		String[] nationmajorChildCatolog    = request.getParameterValues("nationmajorChildCatolog");	 	   //专业小类
		String[] teachingTypes_nationMajor  = request.getParameterValues("teachingType_nationMajor");          //办学模式---从国家专业库中申报新专业
		String[] classicIds_nationMajor     = request.getParameterValues("classic_nationMajor");			   //层次           ---从国家专业库中申报新专业                     
		String[] dicrects_nationMajor       = request.getParameterValues("dicrect_nationMajor");			   //专业方向 ---从国家专业库中申报新专业
		String[] address_nationMajor 	    = request.getParameterValues("address_nationMajor");               //办学地址 ---从国家专业库中申报新专业
		String[] scopes_nationMajor    	    = request.getParameterValues("scope_nationMajor");                 //招生范围 ---从国家专业库中申报新专业
		String[] shapes_nationMajor         = request.getParameterValues("shape_nationMajor");                 //形式           ---从国家专业库中申报新专业
		String[] memos_nationMajor          = request.getParameterValues("memo_nationMajor");                  //备注           ---从国家专业库中申报新专业
		String[] limitNums_nationMajor      = request.getParameterValues("limitNum_nationMajor");              //指标数      ---从国家专业库中申报新专业
		String[] lowerNums_nationMajor      = request.getParameterValues("lowerNum_nationMajor");              //下限数      ---从国家专业库中申报新专业
		//--------------------申报招生批次中没有的专业的参数--------------------
		
		//--------------------申报招生批次中的专业的参数--------------------
		String brSchoolplanId 			    = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String recruitplanId  			    = ExStringUtils.trimToEmpty(request.getParameter("recruitplanId"));
		String wf_id            		    = ExStringUtils.trimToEmpty(request.getParameter("wf_id"));
		String[] majorIds 				    = request.getParameterValues("majorId"); 
		String[] recruitMajors 		        = request.getParameterValues("recruitMajor"); 
		String[] tuitionFees 			    = request.getParameterValues("tuitionFee"); 
		String[] limitNums 				    = request.getParameterValues("limitNum");
		String[] lowerNums 				    = request.getParameterValues("lowerNum"); 
		//--------------------申报招生批次中的专业的参数--------------------
		
		param.put("uploadNewMajorFileFileIds", uploadNewMajorFileFileIds);
		
		param.put("baseMajors", baseMajors);
		param.put("teachingTypes_baseMajor", teachingTypes_baseMajor);
		param.put("classicIds_baseMajor", classicIds_baseMajor);
		param.put("dicrects_baseMajor", dicrects_baseMajor);
		param.put("address_baseMajor", address_baseMajor);
		param.put("scopes_baseMajor", scopes_baseMajor);
		param.put("shapes_baseMajor", shapes_baseMajor);
		param.put("memos_baseMajor", memos_baseMajor);
		param.put("limitNums_baseMajor", limitNums_baseMajor);
		param.put("lowerNums_baseMajor", lowerNums_baseMajor);
		param.put("studyperiod_baseMajor", studyperiod_baseMajor);
		param.put("majorDescript_baseMajor", majorDescript_baseMajor);
		
		param.put("nationMajors", nationMajors);
		param.put("nationmajorParentCatolog", nationmajorParentCatolog);
		param.put("nationmajorChildCatolog", nationmajorChildCatolog);
		param.put("teachingTypes_nationMajor", teachingTypes_nationMajor);
		param.put("classicIds_nationMajor", classicIds_nationMajor);
		param.put("dicrects_nationMajor", dicrects_nationMajor);
		param.put("address_nationMajor", address_nationMajor);
		param.put("scopes_nationMajor", scopes_nationMajor);
		param.put("shapes_nationMajor", shapes_nationMajor);
		param.put("memos_nationMajor", memos_nationMajor);
		param.put("limitNums_nationMajor", limitNums_nationMajor);
		param.put("lowerNums_nationMajor", lowerNums_nationMajor);
		
		
		try {
			RecruitPlan recruitPlan 	= recruitPlanService.get(recruitplanId);
			Date brSchoolApplyCloseTime = recruitPlan.getBrSchoolApplyCloseTime();
			
			//检查学习中心是否已超过签约时间、申报结束时间
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				
				//申报时间检查
				if (null==brSchoolApplyCloseTime) {
					throw new WebException("您所申报的批次，学习中心申报结束时间未设置，请与招生办联系！");
				}else if(currentDate.getTime()>ExDateUtils.addDays(brSchoolApplyCloseTime,1).getTime()){
					throw new WebException("您所申报的批次，申报工作已结束！");
				}
				
				//签约时间检查
				OrgUnit unit            = user.getOrgUnit();
				Date startDate          = unit.getStartDate();
				Date endDate            = unit.getEndDate();
				if (null==startDate ||null==endDate) {
					throw new WebException("签约时间未设置,请签约后再进行申报！");
				}
				if (null!=startDate && null!=endDate && !(currentDate.getTime()>=startDate.getTime() && currentDate.getTime()<=endDate.getTime())) {
					throw new WebException("贵单位的签约已失效,请重新签约！");
				}
				
				/*//当提交申请时检查申报的批次是否已有申报记录，如果有申报记录则不允许申报，编辑审核时不检查
				if (ExStringUtils.isEmpty(brSchoolplanId)) {
					String brSchoolPlanHQL 			 = "from "+BranchSchoolPlan.class.getSimpleName()+
													   " br where br.isDeleted=0 and br.recruitplan.resourceid=? and br.branchSchool.resourceid=?";
					List<BranchSchoolPlan> oldBrPlan = branchSchoolPlanService.findByHql(brSchoolPlanHQL, recruitplanId,user.getOrgUnit().getResourceid());
					if (null!=oldBrPlan && !oldBrPlan.isEmpty()) {
						throw new WebException("你已申报该批次！");
					}
				}*/
			}
			
			
			//申报流程处理方法
			BranchSchoolPlan brSchoolPlan			  = branchSchoolPlanService.appRecruitPlanFlowerStart(request,param, map, user, uploadNewMajorFileFileIds, 
																	  			              baseMajors,nationMajors, brSchoolplanId, wf_id, majorIds,
																	  			              recruitMajors, limitNums, lowerNums, recruitPlan);
			//重新加载URL
			String reloadUrl                          = request.getContextPath() +"/edu3/recruit/recruitmanage/branchschoolmajor-form.html?resourceid=";
			//重新加载的流程
			BranchSchoolPlan nextBrPlan	              = getNextBranchSchoolPlanAuditDB(user);
			
			//如果当前用户还有待审核的批次则随机加载一条，否则加载当前记录
			if (null!=nextBrPlan) {
				reloadUrl                            += nextBrPlan.getResourceid()+"&wf_id="+nextBrPlan.getWfid();
			}else {
				reloadUrl                            += brSchoolPlan.getResourceid()+"&wf_id="+brSchoolPlan.getWfid();
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("reloadUrl", reloadUrl);
					
					
		} catch (Exception e) {
			logger.error("保存招生专业申报信息出错:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 用户进行审核操作时，提交后页面中载入待办中的另一条记录（如果当前用户是招生办用户）
	 * @param user
	 * @param roleId
	 * @return
	 */
	private BranchSchoolPlan getNextBranchSchoolPlanAuditDB(User user){
		
		String wfid  		    = "";
		BranchSchoolPlan bp     = null;
		List<String> roleId     = new ArrayList<String>();
		boolean  isAllowNext    = false;
		try {
			
			for(Role r:user.getRoles()){
				roleId.add(r.getResourceid());
				if ("ROLE_RECRUIT_LEADER".equals(r.getRoleCode()) ||
						"ROLE_RECRUIT_DIRECTOR".equals(r.getRoleCode()) ||
						"ROLE_RECRUIT_MANAGER".equals(r.getRoleCode())) {
					isAllowNext = true;
				}
			}
			
			List<Map<String, Object>> wfIdList = getCurrentUserWorkFlowerId("DB",user.getResourceid(),roleId);
			
			if (null!=wfIdList && !wfIdList.isEmpty()) {
				wfid            = wfIdList.get(0).get("ID").toString();
			}
			
			if (ExStringUtils.isNotEmpty(wfid) && isAllowNext) {
				bp = branchSchoolPlanService.findUniqueByProperty("wfid", Long.valueOf(wfid));
			}
		} catch (Exception e) {
			logger.error("用户进行审核操作时，提交后获取下一条审核流程出错：{}"+e.fillInStackTrace());
		}
		
		return bp;
	}
	 
	
	/**
	 * 申报新专业-表单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschool-newmajor-form.html")
	public String addNewMajorForm(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		String teachingType = ExStringUtils.trimToEmpty(request.getParameter("teachingType1"));            //办学模式
		String baseMajor1  = ExStringUtils.trimToEmpty(request.getParameter("baseMajor1"));			    //基础专业
		String limitNum1   = ExStringUtils.trimToEmpty(request.getParameter("limitNum1"));              //人数
		String studyperiod1    = ExStringUtils.trimToEmpty(request.getParameter("studyperiod1"));			//学制		
		String dicrect      = ExStringUtils.trimToEmpty(request.getParameter("dicrect1"));					//专业方向
		String address 		= ExStringUtils.trimToEmpty(request.getParameter("address1"));                 //办学地址
		String scope    	= ExStringUtils.trimToEmpty(request.getParameter("scope1"));                   //招生范围
		String newMajorId        = ExStringUtils.trimToEmpty(request.getParameter("newMajorId"));          //已申报学习中心专业d
		String memo         = ExStringUtils.trimToEmpty(request.getParameter("memo1"));                    //备注
		String majorDescript1 = ExStringUtils.trimToEmpty(request.getParameter("majorDescript1"));         //专业介绍
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));//新增或编辑
		User user 			= SpringSecurityHelper.getCurrentUser();
		
//		if (ExStringUtils.isNotEmpty(nationMajor)&&!"undefined".equals(nationMajor)) {
//			NationMajor m 		= nationMajorService.load(nationMajor);
//			model.addAttribute("nationMajorName", m.getNationMajorName());
//		}
		//model.addAttribute("majorClassName",JstlCustomFunction.dictionaryCode2Value("CodeMajorClass", majorClass));
		model.addAttribute("currentUser1", user);
		model.addAttribute("teachingType1", teachingType);
		model.addAttribute("baseMajor1", baseMajor1);
		model.addAttribute("limitNum1", limitNum1);
		model.addAttribute("studyperiod1", studyperiod1);
		model.addAttribute("dicrect1", dicrect);
		model.addAttribute("address1", address);
		model.addAttribute("scope1", scope);
		model.addAttribute("newMajorId", newMajorId);
		model.addAttribute("memo1", memo);
		model.addAttribute("majorDescript1", majorDescript1);
		model.addAttribute("from", from);
		
		List<Major> majorList = majorService.findByHql("from "+Major.class.getSimpleName()+" where isDeleted=? order by majorCode", 0);
		model.addAttribute("majorList", majorList);
		return "/edu3/recruit/recruitmanage/branchschool-newmajor-form";
	}

	/**
	 * 取回
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschoolmajor-getback.html")
	public void getBack(HttpServletRequest request,HttpServletResponse response) throws WebException {
		String resourceid = request.getParameter("resourceid");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BranchSchoolPlan brSchoolPlan =new BranchSchoolPlan();
			if(StringUtils.isNotEmpty(resourceid)){
				brSchoolPlan = branchSchoolPlanService.get(resourceid);
			}
			//取回
			 WfAgent.getBack(request);
			 
			 map.put("statusCode", 200);
			 map.put("message", "取回成功！");
			 map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/recruitmanage/branchschoolmajor-form.html?resourceid="+brSchoolPlan.getResourceid()
						 +"&wf_id="+brSchoolPlan.getWfid());
			} catch (Exception e) {
					e.printStackTrace();
					logger.error("取回招生专业申报信息出错:"+e.fillInStackTrace());
					map.put("statusCode", 300);
					map.put("message", "取回失败！");
				}
			renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除
	 * @param resourceid
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/branchschoolmajor-delete.html")
	public void delRecruitMajor(String resourceid,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
                recruitMajorService.batchDelete(resourceid.split(","));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			}
		} catch (Exception e) {
			logger.error("删除招生专业出错:",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 招生账户列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/brschoolaccount-list.html")
	public String listRecruitAccount(HttpServletRequest request, Page objPage, ModelMap model)throws WebException {
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			objPage.setOrderBy("username");
			objPage.setOrder(Page.ASC);
			
			Map<String, Object> condition = new HashMap<String, Object>();
			String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String account = ExStringUtils.trimToEmpty(request.getParameter("account"));
			String userCnName = ExStringUtils.trimToEmpty(request.getParameter("cnName"));
			
			condition.put("isDeleted",0);
			if(StringUtils.isNotEmpty(unitId)){
				condition.put("unitId", unitId);
				condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"), ""));
			}
			if(StringUtils.isNotEmpty(account))	{
				condition.put("account", account);
			}
			if(StringUtils.isNotEmpty(userCnName))	{
				condition.put("cnName", userCnName);
			}
			if(StringUtils.isNotEmpty(branchSchool))	{
				condition.put("unitId", branchSchool);
			}
			
			//单位类型为：校外学习中心
			condition.put("unitType", ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType"));
			condition.put("userType", CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());
			condition.put("teacherType", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.rolecode").getParamValue());//只查找招生人员角色
			
			Page userList = edumanagerService.findEdumanagerByCondition(condition,objPage);
			model.addAttribute("userList", userList);
			model.addAttribute("condition", condition);
			
			return "/edu3/recruit/recruitmanage/brschoolaccount-list-sub";
		}else{
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));
			try {
				List<OrgUnit> orgList = orgUnitService.findOrgTree(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue());
				String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue(),defaultCheckedValue));
				model.addAttribute("unitTree", jsonString);
			} catch (Exception e) {
				logger.error("输出招生单位账号-组织树出错："+e.fillInStackTrace());
			}
			return "/edu3/recruit/recruitmanage/brschoolaccount-list";
		}
		
	}
	
	/**
	 * 招生账户管理
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/recruit/recruitmanage/brschoolaccount-form.html")
	public String addRecruitAccount(String resourceid,HttpServletRequest request,ModelMap model)throws WebException{
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));	
		
		
		//查询校外学习中心角色
		//String sql = "select * from hnjk_sys_roles t start with t.parentid is null and t.rolecode = '"+CacheAppManager.getSysConfigurationByCode("orgunit.brschool.rolecode").getParamValue()+"' connect by prior t.resourceid=t.parentid";
		//List<Role> roleList = userService.findByStartWith(sql,Role.class);
		
		List<Role> roleList = roleService.findByHql("from "+Role.class.getSimpleName()+ " where isDeleted = ? and roleCode = ?", 0,CacheAppManager.getSysConfigurationByCode("orgunit.brschool.rolecode").getParamValue());
		if(StringUtils.isNotEmpty(resourceid)){			
			User user = userService.get(resourceid);
			model.addAttribute("user", user);
			model.addAttribute("unitId", user.getOrgUnit().getResourceid());
		}else{
			User user = new Edumanager();
			user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());			
			model.addAttribute("user", user);
			model.addAttribute("unitId", unitId);
		}
		//List<OrgUnit> schoollist = orgUnitService.findByCriteria(Restrictions.eq("unitType", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue()),
		 //                   	Restrictions.eq("isDeleted", 0));
		
		
		model.addAttribute("roleList", roleList);
		//model.addAttribute("schoollist", schoollist);
		
		return "/edu3/recruit/recruitmanage/brschoolaccount-form";
	}
	
	/**
	 * 保存招生账户
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/brschoolaccount-save.html")
	public void saveAccount(HttpServletRequest request,HttpServletResponse response,Edumanager user) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String msg = "保存成功！";
		int status = 200;
		User user1 = null;
		try {
			String roleId = ExStringUtils.trimToEmpty(request.getParameter("roleId"));
			Role role = null;
			if(ExStringUtils.isNotEmpty(roleId)){
				role = roleService.get(roleId);				
			}
			
			if(ExStringUtils.isNotBlank(user.getResourceid())){//编辑
				User persistUser = userService.get(user.getResourceid());
				if(!persistUser.getPassword().equals(user.getPassword())) {
					user.setPassword(BaseSecurityCodeUtils.getMD5(user.getPassword()));
				}
				
				
				if(ExStringUtils.isNotBlank(user.getUnitId())){
					OrgUnit orgUnit = orgUnitService.load(user.getUnitId());
					user.setOrgUnit(orgUnit);
					orgUnit.getUser().add(persistUser);
				}
				user.getRoles().add(role);
				ExBeanUtils.copyProperties(persistUser, user);
				userService.update(persistUser);
			}else {
				user1 = userService.findUniqueByProperty("username", user.getUsername());
				if(null != user1){
					msg = "用户账号已存在！<br/>请使用恢复功能.";					
					user.setResourceid(user1.getResourceid());
					status = 202;
				}else{
					if(ExStringUtils.isNotBlank(user.getUnitId())){
						OrgUnit orgUnit = orgUnitService.load(user.getUnitId());
						user.setOrgUnit(orgUnit);
						orgUnit.getUser().add(user);
					}
						user.getRoles().add(role);
						userService.save(user);
				}
			
			}
			map.put("statusCode", status);
			map.put("message", msg);
			map.put("reloadUrl", request.getContextPath() 
				+"/edu3/recruit/recruitmanage/brschoolaccount-form.html?resourceid="+user.getResourceid());
		} catch (Exception e) {
			logger.error("保存招生账户错误:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}


	/**
	 * 查看学习中心允许的招生的专业列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/brahchSchool-limit-major.html")
	public String viewBrahchSchoolLimitMajor(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchoolId 	     = ExStringUtils.trimToEmpty(request.getParameter("brahchSchoolId"));
		condition.put("brschoolId", branchSchoolId);
		condition.put("isOpened", "Y");
		List<BrschMajorSetting> list = brschMajorSettingService.findBranchSchoolLimitMajorList(condition);
		model.addAttribute("list", list);
		
		return "/edu3/recruit/recruitmanage/branchschool-limit-major-list";
	}
	

	/**
	 *  获取国家专业库的专业信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/get-nationmajort.html")
	public void getNationMajorList(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		List<JsonModel> modelList    = new ArrayList<JsonModel>();
		
		/*
		版本一
		String classicId     	     = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorType  			 = ExStringUtils.trimToEmpty(request.getParameter("majorType"));
		String flag      			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String exceptid              = ExStringUtils.trimToEmpty(request.getParameter("exceptid"));
		String exId                  = "";
		
		if (ExStringUtils.isNotEmpty(classicId)) condition.put("classicid",classicId);
		if (ExStringUtils.isNotEmpty(majorType)) condition.put("nationMajorType",majorType);
		if (ExStringUtils.isNotEmpty(exceptid)) {
			String [] exceptids      = exceptid.split(",");
			if (null!=exceptids && exceptids.length>0) {
				for (int i = 0; i < exceptids.length; i++) {
					exId += ",'"+exceptids[i]+"'";
				}
			}
		}
		if (ExStringUtils.isNotEmpty(exId))      condition.put("exceptid", exId.substring(1));

		List<NationMajor> majorList = nationMajorService.findNationMajorByCondition(condition);
		
		if ("queryType".equals(flag)) {
			condition.clear();
			for (int i = 0; i < majorList.size(); i++) {
				NationMajor major   = majorList.get(i);
				JsonModel model     = new JsonModel() ;
				
				model.setKey(major.getNationMajorType());
				model.setValue(JstlCustomFunction.dictionaryCode2Value("CodeMajorClass", major.getNationMajorType()));
				
				condition.put(major.getNationMajorType(),model);
			}
			for (String key:condition.keySet()) {
				JsonModel model = (JsonModel) condition.get(key);
				modelList.add(model);
			}
		}else if ("queryMajor".equals(flag)) {
			for (int i = 0; i < majorList.size(); i++) {
				NationMajor major   = majorList.get(i);
				JsonModel model     = new JsonModel() ;
				
				model.setKey(major.getResourceid());
				model.setValue(major.getNationMajorName());
				
				modelList.add(model);
			}
		}*/
		
		
		//版本二
		String nationmajorParentCatolog = ExStringUtils.trimToEmpty(request.getParameter("nationmajorParentCatolog"));
		String nationmajorChildCatolog  = ExStringUtils.trimToEmpty(request.getParameter("nationmajorChildCatolog"));
		String flag      			    = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String exceptid                 = ExStringUtils.trimToEmpty(request.getParameter("exceptid"));
		String exId                     = "";
		
		if (ExStringUtils.isNotEmpty(nationmajorParentCatolog)) {
			condition.put("nationmajorParentCatolog",nationmajorParentCatolog);
		}
		if (ExStringUtils.isNotEmpty(nationmajorChildCatolog)) {
			condition.put("nationmajorChildCatolog",nationmajorChildCatolog);
		}
		if (ExStringUtils.isNotEmpty(exceptid)) {
			String [] exceptids      = exceptid.split(",");
			if (null!=exceptids && exceptids.length>0) {
				for (int i = 0; i < exceptids.length; i++) {
					exId += ",'"+exceptids[i]+"'";
				}
			}
		}
		if (ExStringUtils.isNotEmpty(exId)) {
			condition.put("exceptid", exId.substring(1));
		}
		
		List<NationMajor> majorList = nationMajorService.findNationMajorByCondition(condition);
		
		if ("queryType".equals(flag)) {
			condition.clear();
			for (int i = 0; i < majorList.size(); i++) {
				NationMajor major   = majorList.get(i);
				JsonModel model     = new JsonModel() ;
				
				model.setKey(major.getChildCatalog());
				model.setValue(JstlCustomFunction.dictionaryCode2Value("nationmajorChildCatolog", major.getChildCatalog()));
				
				condition.put(major.getChildCatalog(),model);
			}
			for (String key:condition.keySet()) {
				JsonModel model = (JsonModel) condition.get(key);
				modelList.add(model);
			}
		}else if ("queryMajor".equals(flag)) {
			
			for (int i = 0; i < majorList.size(); i++) {
				NationMajor major   = majorList.get(i);
				JsonModel model     = new JsonModel() ;
				
				model.setKey(major.getResourceid());
				model.setValue(major.getNationMajorName());
				
				modelList.add(model);
			}
		}
		
		renderJson(response,JsonUtils.listToJson(modelList));
 	}
	/**
	 * 查看设置学习中心申报的新专业列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/brSchoolnewmajor-list.html")
	public String queryBrschoolNewMajorList(HttpServletRequest request,ModelMap model){
	
		Map<String,Object> condition = new HashMap<String, Object>();
		List<BranchSchoolPlan>   brpl= new ArrayList<BranchSchoolPlan>();
		List<BranchShoolNewMajor>brnm= new ArrayList<BranchShoolNewMajor>();
		String recruitPlanId  		 = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		String brSchoolId     		 = ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String isIntoRecruitPlan     = ExStringUtils.trimToEmpty(request.getParameter("isIntoRecruitPlan"));
		
		//List<ExamSubjectSet> setList = examSubjectSetService.findByCriteria(Restrictions.eq("isDeleted", 0));
		if (ExStringUtils.isNotEmpty(recruitPlanId)) {
			condition.put("recruitPlanId", recruitPlanId);
		}
		if (ExStringUtils.isNotEmpty(brSchoolId)) {
			condition.put("brSchoolId", brSchoolId);
		}
		
		
		
		if (ExStringUtils.isNotEmpty(recruitPlanId) || ExStringUtils.isNotEmpty(brSchoolId)) {
			brpl.addAll(branchSchoolPlanService.getBranchSchoolPlanList(condition));
		}
		//默认查询未转入招生批次的新专业
		if (ExStringUtils.isEmpty(isIntoRecruitPlan)) {
			condition.put("isIntoRecruitPlan",Constants.BOOLEAN_NO);
		}else if(!"A".equals(isIntoRecruitPlan)) {
			condition.put("isIntoRecruitPlan",isIntoRecruitPlan);
		}
		
		condition.put("isPass", Constants.BOOLEAN_YES);
		condition.put("orderBy", "newMajor.classic.classicCode,newMajor.teachingType,newMajor.baseMajor,newMajor.resourceid");
		for (BranchSchoolPlan bp:brpl) {
			condition.put("branchSchoolPlan", bp.getResourceid());
			brnm.addAll(branchShoolNewMajorService.findBranchShoolNewMajorByCondition(condition));
		}
		//model.addAttribute("examSubjectSet",setList);
		model.addAttribute("condition",condition);
		model.addAttribute("brSchoolNewMajorList",brnm);
		
		return "/edu3/recruit/recruitmanage/branchschool-newmajor-list";
	}
	/**
	 * 将所选的新专业转入招生批次，设置为招生专业
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/brSchoolnewmajor.html")
	public void inToRecruitPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		Map<String,Object> map     = new HashMap<String, Object>();
		String recruitPlanId       = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));    //招生批次ID
		String brSchNewMajorId     = ExStringUtils.trimToEmpty(request.getParameter("brSchNewMajorId"));  //申报的新专业ID
		String limitNum    		   = ExStringUtils.trimToEmpty(request.getParameter("limitNum"));         //设置为招生专业的指标数
		String lowerNum     	   = ExStringUtils.trimToEmpty(request.getParameter("lowerNum"));         //设置为招生专业的下限数
		String studyperiod     	   = ExStringUtils.trimToEmpty(request.getParameter("studyperiod"));      //设置为招生专业的学制
		String examSubjectSet      = ExStringUtils.trimToEmpty(request.getParameter("examSubjectSet"));   //设置为招生专业的考试科目
		String memo     		   = ExStringUtils.trimToEmpty(request.getParameter("memo"));             //设置为招生专业的备注
		
		
		String[] brSchNewMajorIds  = null;						  //申报的新专业ID
		String[] limitNums     	   = null;						  //设置为招生专业的指标数
		String[] lowerNums     	   = null;						  //设置为招生专业的下限数
		String[] studyperiods  	   = null;						  //设置为招生专业的学制
		//String[] examSubjectSets   = null;					      //设置为招生专业的考试科目
		String[] memos             = null;						  //设置为招生专业的备注
		
		if (ExStringUtils.isNotEmpty(brSchNewMajorId) && brSchNewMajorId.length()>0) {
			brSchNewMajorIds       = brSchNewMajorId.split(",");
		}
		if (ExStringUtils.isNotEmpty(limitNum) && limitNum.length()>0) {
			limitNums       	   = limitNum.split(",");
		}
		if (ExStringUtils.isNotEmpty(lowerNum) && lowerNum.length()>0) {
			lowerNums       	   = lowerNum.split(",");
		}
		if (ExStringUtils.isNotEmpty(studyperiod) && studyperiod.length()>0) {
			studyperiods      	   = studyperiod.split(",");
		}
		//if (ExStringUtils.isNotEmpty(examSubjectSet) && examSubjectSet.length()>0) {
		//	examSubjectSets        = examSubjectSet.split(",");
		//}
		if (ExStringUtils.isNotEmpty(memo) && memo.length()>0) {
			memos       		   = memo.split(",");
		}
		
		//当提交转入请求的招生批次ID、新专业ID、考试科目、指标数、下限数、学制都不为空时才进行转入操作
		if (null!=brSchNewMajorIds && brSchNewMajorIds.length>0&&null!=limitNums && limitNums.length>0 &&
			null!=lowerNums && lowerNums.length>0 &&null!=studyperiods&&studyperiods.length>0&&ExStringUtils.isNotEmpty(recruitPlanId)) {
			
			RecruitPlan recruitPlan = recruitPlanService.get(recruitPlanId);
			User user               = SpringSecurityHelper.getCurrentUser();
			Date curDate			= new Date ();
			
			map.put("user", user);
			map.put("curDate", curDate);
			map.put("recruitPlan",recruitPlan);
			
			try {
				
				for (int i = 0; i < brSchNewMajorIds.length; i++) {
					
					BranchShoolNewMajor brNewMajor = branchShoolNewMajorService.load(brSchNewMajorIds[i]);        //申报的新专业
					BranchSchoolPlan   brSchPlan   = branchSchoolPlanService.get(brNewMajor.getBranchSchoolPlan().getResourceid()); //申报新专业关联的校外学习中心批次
					Classic classic                = brNewMajor.getClassic();									  //申报新专业关联的层次
					OrgUnit uint                   = brNewMajor.getBranchSchoolPlan().getBranchSchool();	      //申报新专业关联的校外学习中心
					String isIntoRecruitPlan       = brNewMajor.getIsIntoRecruitPlan();							  //申报的新专业是否已转为对应批次的招生专业
					String teachingTypeStr 		   = brNewMajor.getTeachingType();								  //申报新专业的办学模式	
					Long brSchLimitNumL            = null==brNewMajor.getLimitNum()?0L:brNewMajor.getLimitNum();  //申报新专业的招生指标数
					Long brSchLowerNumL            = null==brNewMajor.getLowerNum()?0L:brNewMajor.getLowerNum();  //申报新专业的招生下限数
					String [] teachingTypes        = null;														  //申报新专业的办学模式
					Major major 				   = null;														  //申报新专业关联的基础专业
					
					//String e_t          		   = examSubjectSets[i];										  //设置为招生专业的考试科目
					String li_Num                  = limitNums[i];												  //设置为招生专业的指标数
					String lo_Num                  = lowerNums[i];												  //设置为招生专业的下限数
					String stpd                    = studyperiods[i];											  //设置为招生专业的学制
					String m_o                     = memos[i];													  //设置为招生专业的备注
					
					if (!"Y".equals(brSchPlan.getIsAudited())) {
						throw new WebException(brSchPlan.getBranchSchool().getUnitName()+"申报新专业流程未通过!");
					}
					
					//申报的新专业来至国家专业库
					if (ExStringUtils.isNotEmpty(brNewMajor.getMajorName())) {
						major         =  majorService.findByCriteria(Restrictions.eq("nationMajor.resourceid", brNewMajor.getMajorName())).get(0);
					}
					//申报的新专业来至基础专业库
					if (ExStringUtils.isNotEmpty(brNewMajor.getBaseMajor())) {
						major 		  =  majorService.findByCriteria(Restrictions.eq("resourceid", brNewMajor.getBaseMajor())).get(0);
					}
					
					//如果 是否已转入招生批次标识为Y，则代表这个新专业已加入招生专业
					if (null!=isIntoRecruitPlan && "Y".equals(isIntoRecruitPlan)) {
						continue;
					}
					
					//如果申报的新专业关联的招生批次跟所选的招生批不是同一个批次则跳出
					if (!recruitPlanId.equals(brSchPlan.getRecruitplan().getResourceid())) {
						continue;
					}
					
					//如果申报的新专业对应的基础专业为空则跳过
					if (null==major) {
						continue;
					}
					
					if (ExStringUtils.isNotEmpty(teachingTypeStr)) {
						teachingTypes = teachingTypeStr.split(",");
					}
					
					//如果申报的新专业模块为空则跳过，因为招生专业的办学模式是必需的
					if (null!=teachingTypes && teachingTypes.length>0) {
						
						map.put("uint",uint);
						map.put("memo",m_o);
						map.put("lowerNum",lo_Num);
						map.put("limitNum", li_Num);
						map.put("studyperiod",stpd);
						map.put("baseMajor", major);
						map.put("baseClassic", classic);
						//map.put("examSubjectSet", e_t);
						map.put("brSchPlan",brSchPlan);
						map.put("teachingTypes", teachingTypes);
						map.put("brSchLimitNumL",brSchLimitNumL);
						map.put("brSchLowerNumL",brSchLowerNumL);
						
						recruitMajorService.branSchoolNewMajorIntoRecruitMajor(map);
						
						//如果转入成功则将校外学习中心申报的新专业设置为已转入招生批次
						brNewMajor.setIsIntoRecruitPlan(Constants.BOOLEAN_YES);
						branchShoolNewMajorService.update(brNewMajor);
					}else {
						continue;
					}
				}
			    //recruitPlanService.reloadLazy(recruitPlan);
				map.clear();
				map.put("statusCode", 200);
				map.put("message", "转入成功！");
				map.put("callbackType", "forward");
				map.put("forward", request.getContextPath()+"/edu3/recruit/recruitmanage/brSchoolnewmajor-list.html?recruitPlanId="+recruitPlanId);
				
			} catch (Exception e) {
				logger.error("转入出错:{}"+e.fillInStackTrace());
				map.clear();
				map.put("statusCode", 300);
				map.put("message", "转入失败:<br/>"+e.getMessage());
			}
		}else {
			map.clear();
			map.put("statusCode", 300);
			map.put("message", "转入失败:<br/>招生批次、要转入的新专业、指标数、下限数、学制不允许为空!");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	       
}
