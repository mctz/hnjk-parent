package com.hnjk.edu.recruit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.NationMajor;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.INationMajorService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.BranchShoolNewMajor;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitMajorSetting;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IBranchShoolNewMajorService;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitMajorSettingService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WfAgent;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;


/**
 * 校外学习中心招生批次服务实现<code>BranchSchoolPlanServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-11 下午01:45:45
 * @see 
 * @version 1.0
*/
@Transactional
@Service("branchSchoolPlanService")
public class BranchSchoolPlanServiceImpl extends BaseServiceImpl<BranchSchoolPlan>
    implements IBranchSchoolPlanService{
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("nationMajorService")
	INationMajorService nationMajorService;
	
	@Autowired
	@Qualifier("attachsService")
	IAttachsService attachsService;
	
	@Autowired
	@Qualifier("branchShoolNewMajorService")
	IBranchShoolNewMajorService branchShoolNewMajorService;
	
	
	@Autowired
	@Qualifier("majorService")
	IMajorService majorService;
	
	@Autowired
	@Qualifier("brschMajorSettingService")
	private IBrschMajorSettingService brschMajorSettingService;
	
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("recruitMajorSettingService")
	private IRecruitMajorSettingService recruitMajorSettingService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	/**
     * 根据条件查找学习中心招生批次
     */
	@Override
	@Transactional(readOnly=true)
	public BranchSchoolPlan getBranchSchoolPlanByCondition(Map<String, Object> condition) throws ServiceException {
			StringBuffer hql = new StringBuffer(" from "+BranchSchoolPlan.class.getSimpleName()+" plan where plan.isDeleted ='0' ");
			//  学习中心
			if (condition.containsKey("branchSchool")) {
			     hql.append(" and plan.branchSchool.resourceid =:branchSchool");
			}	
			// 招生批次
			if (condition.containsKey("recruitPlan")) {
				hql.append(" and plan.recruitplanId =:recruitPlan ");
			}
			List<BranchSchoolPlan> list = findByHql( hql.toString(),condition);
			return list.size()>0 ? list.get(0) : null;

	}
	/**
     * 根据条件查找学习中心招生批次列表
     */
	@Override
	@Transactional(readOnly=true)
	public Page getBrsPlanListByCondition(Page page,Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer(" from "+BranchSchoolPlan.class.getSimpleName()+" plan where plan.isDeleted ='0' ");
		//  学习中心
		if (condition.containsKey("branchSchool")) {
		     hql.append(" and plan.branchSchool.resourceid =:branchSchool");
		}	
		// 招生批次名称
		if (condition.containsKey("recruitplanName")) {
			hql.append(" and plan.recruitplanName like '%"+condition.get("recruitplanName")+"%' ");
		}
		// 招生批次ID或年度ID
		if (condition.containsKey("recruitplanId")) {
			hql.append(" and (plan.recruitplan.resourceid=:recruitplanId or plan.recruitplan.yearInfo.resourceid=:recruitplanId))");
		}
		//办学模式
		if (condition.containsKey("teachingType")) {
			hql.append(" and plan.teachingType=:teachingType");
		}
		//审核状态
		if (condition.containsKey("isAudited")) {
			hql.append(" and plan.isAudited=:isAudited");
		}
		//提交人
		if (condition.containsKey("fillinMan")) {
			hql.append(" and plan.fillinMan like '%"+condition.get("fillinMan")+"%' ");
		}
		//提交时间
		if (condition.containsKey("fillinDate")) {
			hql.append(" and plan.fillinDate <= to_date('"+condition.get("fillinDate")+"','yyyy-MM-dd')");
		}
		//流程ID
		if (condition.containsKey("wfid")) {
			hql.append(" and to_char(plan.wfid) in("+condition.get("wfid")+")");
		}
		//排序
		if (ExStringUtils.isNotEmpty(page.getOrderBy()) && ExStringUtils.isNotEmpty(page.getOrder())) {
			hql.append(" order by "+page.getOrderBy() + " "+page.getOrder());
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), condition);
	}
	/**
     * 获取审核通过的校外学习中心招生批次
     */
	@Override
	@Transactional(readOnly=true)
	public List getAuditedBrsPlanList() throws ServiceException {
		StringBuffer hql = new StringBuffer("from "+BranchSchoolPlan.class.getSimpleName()+" plan where plan.isDeleted = ? ");
		hql.append(" and plan.isAudited=?");
		return exGeneralHibernateDao.findByHql(hql.toString(),0,Constants.BOOLEAN_YES);
	}
	/**
     * 根据条件查找学习中心招生批次
     */
	@Override
	@Transactional(readOnly=true)
	public List<BranchSchoolPlan> getBranchSchoolPlanList(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer(" from "+BranchSchoolPlan.class.getSimpleName()+" plan where plan.isDeleted =:isDeleted and plan.isAudited=:isAudited");
		condition.put("isDeleted", 0);
		condition.put("isAudited", Constants.BOOLEAN_YES);
		//招生批次或批次年度
		if (condition.containsKey("recruitPlanId")) {
		     hql.append(" and (plan.recruitplan.resourceid =:recruitPlanId or plan.recruitplan.yearInfo.resourceid=:recruitPlanId)");
		}	
		//年级
		if (condition.containsKey("grade")) {
			 hql.append(" and plan.recruitplan.grade.resourceid=:grade");
		}
		if (condition.containsKey("brSchoolId")) {
			hql.append(" and plan.branchSchool.resourceid=:brSchoolId");
		}
		hql.append(" order by plan.branchSchool.unitCode asc");

		return findByHql(hql.toString(),condition);
	}
	
	/**
     * 获取给定校外学习中心审核通过的招生批次
     */
	@Override
	@Transactional(readOnly=true)
	public List<BranchSchoolPlan> getAuditedBrsPlanListForBranSchool(Map<String,Object> condition) throws ServiceException {
		Map<String,Object> values = new HashMap<String, Object>();
		StringBuffer hql		  = new StringBuffer(" from "+BranchSchoolPlan.class.getSimpleName()+
													 " plan where plan.isDeleted =:isDeleted and plan.isAudited= :isAudited");
		values.put("isDeleted", 0);
		values.put("isAudited", Constants.BOOLEAN_YES);
		//招生批次		
		if (condition.containsKey("recruitPlanIds")) {
		     hql.append(" and plan.recruitplan.resourceid in("+condition.get("recruitPlanIds")+")");
		}	
		//所属学习中心
		if (condition.containsKey("brSchool")) {
			 hql.append(" and plan.branchSchool.resourceid=:brSchool");
			 values.put("brSchool", condition.get("brSchool"));
		}
		//年级
		if (condition.containsKey("grade")) {
			 hql.append(" and plan.recruitplan.grade.resourceid=:grade");
			 values.put("grade", condition.get("grade"));
		}
		
		List<BranchSchoolPlan> list =  findByHql(hql.toString(),values);
		return list;
	}
	
	
	 /**
     *  校外学习中心申报专业流程-入口
     * @param request                      HttpServletRequest对象
     * @param param						        申报新专业时用于保存参数的MAP
     * @param map                          提供HQL查询时的参数传递MAP 
     * @param user                         当前用户 
     * @param uploadNewMajorFileFileIds    申报新专业时上传的附件ID
     * @param baseMajors                   从基础专业库中申报新专业的ID
     * @param nationMajors                 从国家专业库中申报新专业的ID
     * @param brSchoolplanId               校外学习中心招生批次ID
     * @param wf_id                        流程实例ID
     * @param majorIds                     申报招生批次中的招生专业ID-标识符
     * @param recruitMajors                申报招生批次中的招生专业ID
     * @param limitNums                    申报招生批次中的招生专业的指标数
     * @param lowerNums					        申报招生批次中的招生专业的下限数
     * @param recruitPlan                  校外学习中心要申报专业的招生批次
     * @return                             BranchSchoolPlan---校外学习中心招生批次 
     * @throws Exception
     */
	@Override
	public BranchSchoolPlan appRecruitPlanFlowerStart(HttpServletRequest request, Map<String, Object> param,Map<String, Object> map, User user,
													  String[] uploadNewMajorFileFileIds, String[] baseMajors,String[] nationMajors, String brSchoolplanId,
													  String wf_id,String[] majorIds, String[] recruitMajors, String[] limitNums,
													  String[] lowerNums, RecruitPlan recruitPlan) throws Exception {
	
		Workflow wf 				  = WfAgent.getWorkflow(request);
	
		BranchSchoolPlan brSchoolPlan = null;
		//审核
		if(StringUtils.isNotEmpty(brSchoolplanId)){
			
			brSchoolPlan 	    	  = this.get(brSchoolplanId);
			if("1".equalsIgnoreCase(request.getParameter("CM"))) {	// 提交的标志
				
				request.setAttribute("isAudited", "Y");
				WfAgent.doAction(request);
						
				long wfstate = wf.getEntryState(brSchoolPlan.getWfid());

				//如果提交归档则状态变成审核通过
				if(WorkflowEntry.COMPLETED == (new Long(wfstate).intValue())){
						
					brSchoolPlan.setIsAudited("Y");
					
					//招生领导个性审批专业---部分专业审核通过
					recruitLeaderCustomAudit(request, brSchoolPlan,wf);
					
					Set<BranchShoolNewMajor> newmajors =  brSchoolPlan.getBranchShoolNewMajor();
					//如果申报了新专业，基础专业库中没有申报的专业则按申报的专业属性新增一个,并给学习中心招生限制表加入当前专业
					if (null!=newmajors && !newmajors.isEmpty()) {
						appRecruitPlanFlowerEnd(brSchoolPlan);
					}
					
					//审批通过，加入招生批次
					addToRecruitMajor(brSchoolPlan);
				}
			}
		//申报	
		}else{
			brSchoolPlan = new BranchSchoolPlan();
			brSchoolPlan.setWfid(WfAgent.initialize(request));
			brSchoolPlan.setBranchSchool(user.getOrgUnit());
			brSchoolPlan.setFillinMan(user.getCnName());
			brSchoolPlan.setFillinManId(user.getResourceid());
			brSchoolPlan.setFillinDate(ExDateUtils.getCurrentDateTime());
			brSchoolPlan.setIsAudited("W");//待审核
		}
		//只有学习中心才可以编辑表单域
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			
			String recruitplanName  = recruitPlan.getRecruitPlanname();
			recruitplanName		    = recruitplanName+ "-"+ user.getOrgUnit().getUnitName();		
			
			brSchoolPlan.setRecruitplan(recruitPlan);
			brSchoolPlan.setRecruitplanName(recruitplanName);
			brSchoolPlan.setTeachingType(recruitPlan.getTeachingType());
			
			brSchoolPlan.getBranchSchoolMajor().clear();
			if(majorIds!=null && majorIds.length>0){
				//申报招生批次专业
				appRecruitPlanAppBrMajor(majorIds,recruitMajors, limitNums, lowerNums, brSchoolPlan);
			}
			//生成文号
			brSchoolPlan.setDocumentCode("ZS"+user.getOrgUnit().getUnitCode()+recruitPlan.getGrade().getGradeName()+recruitPlan.getGrade().getTerm());
			//审核流程时如果附件有更改，则重新关联附件
			if (null!=uploadNewMajorFileFileIds && uploadNewMajorFileFileIds.length>0&&ExStringUtils.isNotEmpty(brSchoolplanId)){
				
		    	List<Attachs> list  = new ArrayList<Attachs>();
		    
		    	for (int i = 0; i < uploadNewMajorFileFileIds.length; i++) {
		    		Attachs attachs 	=  attachsService.get(uploadNewMajorFileFileIds[i]);
		    		if (null!=attachs) {
		    			attachs.setFormId(brSchoolplanId);
				    	attachsService.saveOrUpdate(attachs);
				    	list.add(attachs);
					}
			    	
				}
		    	brSchoolPlan.setAttachs(list);
			}
		}
		
		this.saveOrUpdate(brSchoolPlan);
		brSchoolplanId   = brSchoolPlan.getResourceid();
		
		//获取当前流程步骤,如果流程步骤小于等于3，在申报流程中表示流程还在学习中心
		List<HibernateCurrentStep> cureentSetesCurrentSteps = wf.getCurrentSteps(new Long(wf_id).longValue());
		int step = 10000;
		if (null !=cureentSetesCurrentSteps && cureentSetesCurrentSteps.size() > 0) {
			HibernateCurrentStep   currentStep   = cureentSetesCurrentSteps.get(0);
			step								 = currentStep.getStepId();
		}
		//流程刚开始或在学习中心则可以修改或增加申报新专业	
		if ("0".equals(wf_id) || step <= 3) { 
			ExGeneralHibernateDao dao 			 = branchShoolNewMajorService.getExGeneralHibernateDao();
			String clearHQL 					 = " delete from "+BranchShoolNewMajor.class.getSimpleName()+
												   " m where m.isDeleted=0 and m.branchSchoolPlan.resourceid=:clearBranchSchoolPlanId";
			map.put("clearBranchSchoolPlanId", brSchoolplanId);
			dao.executeHQL(clearHQL, map);
			
			//当选择了申报新专业并且申报专业不为空时			
			if ( (null != nationMajors && nationMajors.length > 0) || (null != baseMajors && baseMajors.length>0)) {
				appRecruitPlanFlawerAddNewMajor(brSchoolPlan, param);
			}
		}
		
		return brSchoolPlan;
	}
	/**
	 * 校外学习中心申报专业流程-招生领导个性化审批专业
	 * @param request
	 * @param brSchoolPlan
	 */
	public void recruitLeaderCustomAudit(HttpServletRequest request,BranchSchoolPlan brSchoolPlan,Workflow wf) {
		//招生领导审核通过的招生专业
		String [] passMajorIds      		         = request.getParameterValues("checkId");
		//招生领导审核通过的新专业
		String [] passNewMajors     			     = request.getParameterValues("flag");
		
		//学习中心申报的招生专业
		Set<BranchSchoolMajor> branchSchoolMajor     = brSchoolPlan.getBranchSchoolMajor();
		//学习中心申报的新专业
		Set<BranchShoolNewMajor> branchShoolNewMajor = brSchoolPlan.getBranchShoolNewMajor();
		
		//招生领导审核不通过的招生专业
		List <String> unPassMajorName                = new ArrayList<String>();
		Date curDate                                 = new Date();
		//当前用户
		User user                                    = SpringSecurityHelper.getCurrentUser();
		//设置招生专业的审核状态
		if (null!=passMajorIds &&passMajorIds.length>0) {
			for (BranchSchoolMajor brm : branchSchoolMajor) {
				boolean isInclude = false;
				for (String id : passMajorIds) {
					if (brm.getResourceid().equals(id)) {
						isInclude = true;
						break;
					}
				}
				if (isInclude) {
					brm.setIsPassed("Y");
				}else {
					brm.setIsPassed("N");
					unPassMajorName.add(brm.getRecruitMajor().getRecruitMajorName());
				}
			}
		}else {
			for (BranchSchoolMajor brm : branchSchoolMajor) {
				brm.setIsPassed("N");
			}
		}
		//设置新专业的审核状态
		if (null!=passNewMajors &&passNewMajors.length>0) {
			for (BranchShoolNewMajor brn:branchShoolNewMajor) {
				boolean isInclude = false;
				for (String id:passNewMajors) {
					if(brn.getResourceid().equals(id)){
						isInclude = true;
						break;
					}
				}
				if (isInclude) {
					brn.setIsPassed("Y");
				}else {
					brn.setIsPassed("N");
					if(ExStringUtils.isNotEmpty(brn.getBaseMajor())){
						unPassMajorName.add(majorService.get(brn.getBaseMajor()).getMajorName());
					}
					if(ExStringUtils.isNotEmpty(brn.getMajorName())){
						unPassMajorName.add(nationMajorService.get(brn.getMajorName()).getNationMajorName());
					}
				}
			}
		}else {
			for (BranchShoolNewMajor brn:branchShoolNewMajor) {
				brn.setIsPassed("N");
			}
		}
		brSchoolPlan.setBranchSchoolMajor(branchSchoolMajor);
		brSchoolPlan.setBranchShoolNewMajor(branchShoolNewMajor);
		this.update(brSchoolPlan);
		
		if (null!=unPassMajorName && unPassMajorName.size()>0) {
			
			try {
			
				OrgUnit unit        = orgUnitService.get( wf.getEntry(brSchoolPlan.getWfid()).getUnitId());
				String userHQL	    = " from "+User.class.getSimpleName()+" u where u.isDeleted=0 and u.enabled="+true+" and u.unitId='"+unit.getResourceid()+"' " ;
				List<User> userList = userService.findByHql(userHQL);
				
				StringBuffer majorNameStr = new StringBuffer(unit.getUnitName()+"您好，您申报"+brSchoolPlan.getRecruitplan().getRecruitPlanname()+"招生专业审核结果如下:</br>");
				for (String majorName:unPassMajorName) {
					majorNameStr.append("<font color='red'>"+majorName+"</font>---审核不通过</br>");
				}
				Message msg = new Message();
				msg.setMsgType("tips");
				msg.setMsgTitle(brSchoolPlan.getRecruitplan().getRecruitPlanname()+",部分专业审核不通过!");
				msg.setContent(majorNameStr.toString());
				msg.setSendTime(curDate);
				msg.setSender(user);
				msg.setSenderName("招生办");
				msg.setOrgUnit(user.getOrgUnit());
				MessageReceiver receiver = new MessageReceiver();
				receiver.setReceiveType("user");
				receiver.setMessage(msg);
				if (null!=userList && userList.size()>0) {
					StringBuffer userNames = new StringBuffer();
					for (User u:userList) { 
						for(Role role :u.getRoles()){
							if(role.getRoleCode().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.rolecode").getParamValue())>-1){
								/*MessageSender messageSender = new MessageSender();
								messageSender.setMessage(msg);
								messageSender.setSender("招生办");
								messageSender.setSenderId(user.getResourceid());
								messageSender.setOrgUnit(user.getOrgUnit());*/
								userNames.append(u.getUsername()+",");
								
//								sysMsgService.saveMessage_old(messageSender, receiver,null);
								break;
							}
						}						
						
					}
					receiver.setUserNames(userNames.toString());
					sysMsgService.saveMessage(msg, receiver,null);
				}
			} catch (WorkflowException e) {
				logger.error("招生领导部分专业审核不通过，发送消息出错："+e.fillInStackTrace());
			}
			
		}
	}
	/**
	 * 校外学习中心申报专业流程-申报招生批次专业

	 */
	@Override
	public void appRecruitPlanAppBrMajor(String[] majorIds, String[] recruitMajors, String[] limitNums, String[] lowerNums , BranchSchoolPlan brSchoolPlan)throws Exception{
		for(int i=0;i<majorIds.length;i++){

		    BranchSchoolMajor brSchoolMajor = new BranchSchoolMajor();

			RecruitMajor recruitMajor 		= recruitMajorService.load(recruitMajors[i]);
			brSchoolMajor.setRecruitMajor(recruitMajor);
			//brSchoolMajor.setTuitionFee(Double.valueOf(tuitionFees[i]));
			brSchoolMajor.setLimitNum(Long.valueOf(limitNums[i]));
			brSchoolMajor.setLowerNum(Long.valueOf(lowerNums[i]));
			brSchoolMajor.setBranchSchoolPlan(brSchoolPlan);			

			brSchoolPlan.getBranchSchoolMajor().add(brSchoolMajor);
		}
	}
	/**
	 * 校外学习中心申报专业流程-申报新专业
	 */
	@Override
	public void appRecruitPlanFlawerAddNewMajor(BranchSchoolPlan brSchoolPlan,Map<String,Object> param) throws Exception{
		
		
		String[] uploadNewMajorFileFileIds = (String[])param.get("uploadNewMajorFileFileIds"); 			   //上件的附件ID
		String[] nationMajors    		   = (String[])param.get("nationMajors");			   			   //国家专业库专业ID
		String[] nationmajorParentCatolog  = (String[])param.get("nationmajorParentCatolog");			   //国家专业库中专业的大类分类
		String[] nationmajorChildCatolog   = (String[])param.get("nationmajorChildCatolog");			   //国家专业库中专业的小类分类
		String[] teachingTypes_nationMajor = (String[])param.get("teachingTypes_nationMajor");             //办学模式		---从国家专业库中申报新专业
		String[] classicIds_nationMajor    = (String[])param.get("classicIds_nationMajor");				   //层次			---从国家专业库中申报新专业
		String[] dicrects_nationMajor      = (String[])param.get("dicrects_nationMajor");				   //专业方向		---从国家专业库中申报新专业
		String[] address_nationMajor 	   = (String[])param.get("address_nationMajor");                   //办学地址		---从国家专业库中申报新专业
		String[] scopes_nationMajor    	   = (String[])param.get("scopes_nationMajor");                    //招生范围		---从国家专业库中申报新专业
		String[] shapes_nationMajor        = (String[])param.get("shapes_nationMajor");                    //形式			---从国家专业库中申报新专业
		String[] memos_nationMajor         = (String[])param.get("memos_nationMajor");                     //备注			---从国家专业库中申报新专业
		String[] limitNums_nationMajor 	   = (String[])param.get("limitNums_nationMajor");				   //指标数     	    ---从国家专业库中申报新专业
		String[] lowerNums_nationMajor     = (String[])param.get("lowerNums_nationMajor");                 //下限数      		---从国家专业库中申报新专业
		
		String[] baseMajors            	   = (String[])param.get("baseMajors");				   		     //基础专业库专业ID
		String[] teachingTypes_baseMajor   = (String[])param.get("teachingTypes_baseMajor");             //办学模式			---从基础专业库中申报新专业
		//String[] classicIds_baseMajor      = (String[])param.get("classicIds_baseMajor");				 //层次				---从基础专业库中申报新专业
		String[] dicrects_baseMajor        = (String[])param.get("dicrects_baseMajor");				     //专业方向			---从基础专业库中申报新专业
		String[] address_baseMajor 		   = (String[])param.get("address_baseMajor");                   //办学地址			---从基础专业库中申报新专业
		String[] scopes_baseMajor    	   = (String[])param.get("scopes_baseMajor");                    //招生范围			---从基础专业库中申报新专业
		String[] shapes_baseMajor          = (String[])param.get("shapes_baseMajor");                    //形式				---从基础专业库中申报新专业
		String[] memos_baseMajor           = (String[])param.get("memos_baseMajor");                     //备注				---从基础专业库中申报新专业
		String[] limitNums_baseMajor 	   = (String[])param.get("limitNums_baseMajor");				 //指标数      		    ---从基础专业库中申报新专业
		//String[] lowerNums_baseMajor       = (String[])param.get("lowerNums_baseMajor");                 //下限数    			---从基础专业库中申报新专业
        String[] studyperiod_baseMajor     = (String[])param.get("studyperiod_baseMajor");               //学制
        String[] majorDescript_baseMajor   = (String[])param.get("majorDescript_baseMajor");
		
		//从基础专业库中申报
		if (null!=baseMajors && baseMajors.length>0) {
			
			for (int i = 0; i < baseMajors.length; i++) {
				
				//String c_id = classicIds_baseMajor[i];
				String t_p  = teachingTypes_baseMajor[i];
				String d_t  = dicrects_baseMajor[i];
				String a_d  = address_baseMajor[i];
				String s_p  = scopes_baseMajor[i];
				String s_s  = shapes_baseMajor[i];
				String m_o  = memos_baseMajor[i];
				String b_m  = baseMajors[i];
				String l_i  = null==limitNums_baseMajor[i]?"0":limitNums_baseMajor[i];
				//String l_o  = null==lowerNums_baseMajor[i]?"0":lowerNums_baseMajor[i];
				l_i         = ""==l_i?"0":l_i;
			//	l_o         = ""==l_o?"0":l_o;				
				
				//Classic classic             = classicService.load(c_id);    
				Major baseMajor = majorService.get(b_m);
				BranchShoolNewMajor major   = new BranchShoolNewMajor();
				major.setBaseMajor(b_m);
				major.setAddress(a_d);
				major.setTeachingType(t_p);
				major.setDicrect(d_t);
				major.setScope(s_p);
				major.setShape(s_s);
				major.setMemo(m_o);
			    major.setBranchSchoolPlan(brSchoolPlan);
			    major.setClassic(baseMajor.getNationMajor().getClassic());
			    major.setLimitNum(Long.valueOf(l_i));
			  //  major.setLowerNum(Long.valueOf(l_o));
			    major.setStudyperiod(Double.valueOf(ExStringUtils.defaultIfEmpty(studyperiod_baseMajor[i], "0")));
			    major.setMajorDescript(majorDescript_baseMajor[i]);
			    
			    branchShoolNewMajorService.save(major);
			    brSchoolPlan.getBranchShoolNewMajor().add(major);
			}
		}
		//从国家专业库中申报
		if (null!=nationMajors && nationMajors.length>0) {
			for (int j = 0; j < nationMajors.length; j++) {
				
				String c_id = classicIds_nationMajor[j];
				String t_p  = teachingTypes_nationMajor[j];
				String n_m  = nationMajors[j];
				String d_t  = dicrects_nationMajor[j];
				String a_d  = address_nationMajor[j];
				String s_p  = scopes_nationMajor[j];
				String s_s  = shapes_nationMajor[j];
				String m_o  = memos_nationMajor[j];
				String p_c  = null==nationmajorParentCatolog[j]?"":nationmajorParentCatolog[j];
				String c_c  = null==nationmajorChildCatolog[j]?"":nationmajorChildCatolog[j];
				String l_i  = null==limitNums_nationMajor[j]?"0":limitNums_nationMajor[j];
				String l_o  = null==lowerNums_nationMajor[j]?"0":lowerNums_nationMajor[j];
				l_i         = ""==l_i?"0":l_i;
				l_o         = ""==l_o?"0":l_o;
				
				Classic classic             = classicService.load(c_id);                   
				BranchShoolNewMajor major   = new BranchShoolNewMajor();
				major.setAddress(a_d);
				major.setTeachingType(t_p);
				major.setDicrect(d_t);
				major.setScope(s_p);
				major.setShape(s_s);
				major.setMemo(m_o);
			    major.setBranchSchoolPlan(brSchoolPlan);
			    major.setClassic(classic);
			    major.setMajorName(n_m);
				major.setParentCatalog(p_c);
				major.setChildCatalog(c_c);
				major.setLimitNum(Long.valueOf(l_i));
				major.setLowerNum(Long.valueOf(l_o));
			    
			    branchShoolNewMajorService.save(major);
			}
		
		}
		//上传的附件
	    if (null!=uploadNewMajorFileFileIds && uploadNewMajorFileFileIds.length>0){
	    	
	    	List<Attachs> list  = new ArrayList<Attachs>();
	    
	    	for (int i = 0; i < uploadNewMajorFileFileIds.length; i++) {
	    		Attachs attachs 	=  attachsService.get(uploadNewMajorFileFileIds[i]);
		    	attachs.setFormId(brSchoolPlan.getResourceid());
		    	attachsService.saveOrUpdate(attachs);
		    	list.add(attachs);
			}
	    	brSchoolPlan.setAttachs(list);
	    	this.saveOrUpdate(brSchoolPlan);
		}
		
	}
	/**
	 * 校外学习中心申报专业流程-流程通过时将申报的新专业转成基础专业、校外学习中心招生专业限制数据、招生专业设置表的数据
	 */
	@Override
	public void appRecruitPlanFlowerEnd(BranchSchoolPlan brSchoolPlan)throws Exception {
		//检查是否存在相同的校外学习中心限制数据
		String brsSetHQL = " from "+BrschMajorSetting.class.getSimpleName() +" s where s.isDeleted=0 and s.isOpened='Y' "+
	       				   " and s.brSchool.resourceid=? and s.major.resourceid=? and s.classic.resourceid=? and s.schoolType=? ";
		
		//检查是否存在相同的招生专业设置数据
		String rmSetHQL  = " from "+RecruitMajorSetting.class.getSimpleName()+" rms where rms.isDeleted=0 " +
						   " and rms.major.resourceid=? and rms.classic.resourceid=? and rms.teachingType=? ";
		
		//检查是否存在相同的基础专业数据
		String majorHQL  = " from "+Major.class.getSimpleName()+" major where major.isDeleted=0 and  major.nationMajor.resourceid=? ";
		
		Set<BranchShoolNewMajor> newmajors = brSchoolPlan.getBranchShoolNewMajor();
		for (BranchShoolNewMajor brMajor:newmajors) {
			
			//如果申报的新专业审核通过
			if (Constants.BOOLEAN_YES.equals(brMajor.getIsPassed())) {
				//申报专业的办学模式
				String appNewMajorType   = brMajor.getTeachingType();
				String [] types          = null;
				if (ExStringUtils.isNotEmpty(appNewMajorType)) {
					types 				 = appNewMajorType.split(",");
				}
				
				NationMajor nationMajor  = null;
				List<Major> majorList	 = new ArrayList<Major>();
				//当申报的新专业是从国家专业库中取得的时候
				if (ExStringUtils.isNotEmpty(brMajor.getMajorName())) {
					nationMajor			 = nationMajorService.get(brMajor.getMajorName());
					majorList            = majorService.findByHql(majorHQL,nationMajor.getResourceid());
				}
				//当申报的新专业是从基础专业库中取得的时候
				if (ExStringUtils.isNotEmpty(brMajor.getBaseMajor())) {
					majorList.add(majorService.get(brMajor.getBaseMajor()));
				}
				
				Major major = new Major();
				if (null==majorList || majorList.size()<=0 ) {
					//版本二   国家专业库没有科类
					//major.setMajorClass(brMajor.getMajorClass());
					major.setMajorCode(nationMajor.getNationMajorCode());
					major.setMajorIntroduce(brMajor.getMemo());
					major.setMajorName(nationMajor.getNationMajorName());
					major.setMajorNationCode(nationMajor.getNationMajorCode());
					major.setNationMajor(nationMajor);
					
					majorService.save(major);
				}else {
					major = majorList.get(0);
				}
				
				if (null!=types) {
			
					for (int i = 0; i < types.length; i++) {
						
						List<BrschMajorSetting> brsList = brschMajorSettingService.findByHql(brsSetHQL,brSchoolPlan.getBranchSchool().getResourceid(),major.getResourceid(),brMajor.getClassic().getResourceid(),types[i]);
						if (brsList.isEmpty()) {
							//生成校外学习中心招生专业限制数据
							BrschMajorSetting setting = new BrschMajorSetting();
							setting.setBrSchool(brSchoolPlan.getBranchSchool());
							setting.setClassic(brMajor.getClassic());
							setting.setMajor(major);
							setting.setLimitNum(0);
							setting.setSchoolType(types[i]);
							setting.setMemo("由学习中心自己申报，通过后产生...");
							brschMajorSettingService.save(setting);
						}
						List<RecruitMajorSetting> rmsList = recruitMajorSettingService.findByHql(rmSetHQL, major.getResourceid(),brMajor.getClassic().getResourceid(),types[i]);
						if (rmsList.isEmpty()) {
							//增加招生专业设置表的数据
							RecruitMajorSetting rmSetting = new RecruitMajorSetting();
							rmSetting.setMajor(major);
							rmSetting.setClassic(brMajor.getClassic());
							rmSetting.setTeachingType(types[i]);
							
							recruitMajorSettingService.save(rmSetting);
						}
						
					}
				}
			}
		}
	}
	
	@Override
	public ExGeneralHibernateDao getExGeneralHibernateDao(){
		return this.exGeneralHibernateDao;
	}
	 
    /**
     * 查询给定ID的招生批次是否已经有通过审核的申报记录
     * @param planId  招生批次ID，可以用传单个，也可以传以逗号分隔的多个ID
     * @return
     * @throws Exception
     */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> isBrSchoolAPPAuditPlan(String planId) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();

		if (ExStringUtils.isNotEmpty(planId)) {
			
			String[] ids = planId.split(",");
			String inIds = "";
			StringBuffer hql   = new StringBuffer(" from "+BranchSchoolPlan.class.getSimpleName());
			hql.append(" plan where plan.isDeleted ='0' and plan.recruitplan.resourceid =?");
			hql.append(" and plan.isAudited = 'Y' ");
			
			for (int i = 0; i < ids.length; i++) {
				
				List<BranchSchoolPlan> list =  findByHql(hql.toString(),ids[i]);
				
				if ( null!=list && !list.isEmpty()) {
					resultMap.put(ids[i],false);
				}else{
					resultMap.put(ids[i],true);
				}
			}
		}else {
			return null;
		}
		
		return resultMap;
	}
	/**
     * 查询学习中心某一年度申报通过的批次
     * @param unitId
     * @param preYear
     * @return
     * @throws Exception
     */
	@Override
	public List<BranchSchoolPlan> findPreAuditBranchSchoolPlanList(String unitId, Long year){
		String hql = " from "+BranchSchoolPlan.class.getSimpleName() + " plan where plan.isDeleted=0 and plan.isAudited='Y' "
				   + " and plan.recruitplan.yearInfo.firstYear=? and plan.branchSchool.resourceid=?";
		return (List<BranchSchoolPlan>) exGeneralHibernateDao.findByHql(hql, year,unitId);
	}
	
	public void addToRecruitMajor(BranchSchoolPlan brSchoolPlan)throws Exception {
		Map<String,Object> map     = new HashMap<String, Object>();
		map.put("user", SpringSecurityHelper.getCurrentUser());
		map.put("curDate", new Date());
		map.put("recruitPlan",brSchoolPlan.getRecruitplan());
		for (BranchShoolNewMajor newMajor : brSchoolPlan.getBranchShoolNewMajor()) {			
			map.put("uint",brSchoolPlan.getBranchSchool());
			map.put("baseClassic", newMajor.getClassic());
			map.put("memo",newMajor.getMemo());
			map.put("lowerNum",newMajor.getLowerNum()!=null?newMajor.getLowerNum().toString():"0");
			map.put("limitNum", newMajor.getLimitNum()!=null?newMajor.getLimitNum().toString():"0");
			map.put("studyperiod",newMajor.getStudyperiod()!=null?newMajor.getStudyperiod().toString():"0");
			map.put("baseMajor", majorService.get(newMajor.getBaseMajor()));
			map.put("brSchPlan",brSchoolPlan);
			map.put("teachingTypes", new String[]{newMajor.getTeachingType()});
			map.put("brSchLimitNumL",newMajor.getLimitNum());
			map.put("brSchLowerNumL",newMajor.getLowerNum());
			
			recruitMajorService.branSchoolNewMajorIntoRecruitMajor(map);
		}
		
	}
	
}
