package com.hnjk.edu.finance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.Refundback;
import com.hnjk.edu.finance.model.ReturnPremium;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IRefundbackService;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.vo.RefundbackVO;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午4:38:35 
 * 
 */
@Transactional
@Service("refundbackService")
public class RefundbackServiceImpl extends BaseServiceImpl<Refundback> implements IRefundbackService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("returnPremiumService")
	private IReturnPremiumService returnPremiumService;
	
	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;	
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>(5);
		StringBuffer hql = findByConditionHql(condition, values);
		
		hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	/**
	 * 查询列表HQL
	 * @param condition
	 * @param values
	 * @return
	 */
	private StringBuffer findByConditionHql(Map<String, Object> condition,Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(500);
		hql.append(" from "+Refundback.class.getSimpleName()+" where isDeleted=0 ");
		if(condition.containsKey("brSchool")){
			hql.append(" and studentInfo.branchSchool.resourceid=:brSchool ");
			values.put("brSchool", condition.get("brSchool"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and studentInfo.major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and studentInfo.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			hql.append(" and studentInfo.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("studentName")){
			hql.append(" and studentInfo.studentName=:studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and studyNo=:studyNo ");
			values.put("studyNo",condition.get("studyNo"));
		}
		if(condition.containsKey("yearId")){
			hql.append(" and yearInfo.resourceid=:yearId ");
			values.put("yearId",condition.get("yearId"));
		}
		if(condition.containsKey("chargingItems")){
			hql.append(" and chargingItems=:chargingItems ");
			values.put("chargingItems",condition.get("chargingItems"));
		}
		if(condition.containsKey("processType")){
			hql.append(" and processType=:processType ");
			values.put("processType",condition.get("processType"));
		}
		if(condition.containsKey("processStatus")){
			hql.append(" and processStatus=:processStatus ");
			values.put("processStatus",condition.get("processStatus"));
		}
		if(condition.containsKey("changeType")){
			hql.append(" and changeInfo.changeType=:changeType ");
			values.put("changeType",condition.get("changeType"));
		}
		if(condition.containsKey("beginDate")){
			hql.append(" and to_char(createDate,'yyyy-mm-dd')>=:beginDate ");
			values.put("beginDate",condition.get("beginDate"));
		}
		if(condition.containsKey("endDate")){
			hql.append(" and to_char(createDate,'yyyy-mm-dd')<=:endDate ");
			values.put("endDate",condition.get("endDate"));
		}
		
		return hql;
	}
	
	
	/**
	 * 批量删除(只能删除未处理状态)
	 * @param resourceids
	 */
	@Override
	public void batchCascadeDelete(String resourceids) throws ServiceException {
		try {
			Map<String, Object> params = new HashMap<String, Object>(5);
			params.put("id",CollectionUtils.arrayToList(resourceids.split(",")) );
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap("update EDU_FEE_REFUNDBACK set isdeleted=1 where processstatus='pending' and resourceid in(:id) ", params);
		} catch (Exception e) {
			logger.error("删除未处理状态的记录出错", e);
		}			
	}
	
	/**
	 * 根据条件获取预退费补交订单信息列表
	 * @param condition
	 * @return
	 */
	@Override
	public List<Refundback>findRefundbackByCondition(Map<String, Object> condition) {
		Map<String,Object> values =  new HashMap<String, Object>(5);
		StringBuffer hql = findByConditionHql(condition, values);
		
		return findByHql(hql.toString(), values);
	}

	/**
	 * 批量处理或回退预退费补交订单
	 * @param type
	 * @param resourceids
	 * @return
	 */
	/**
	 * @param type
	 * @param resourceids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> handleOrRollbackRefundback(String type, String resourceids) {
		Map<String, Object> returnMap = new HashMap<String, Object>(150);
		int statusCode = 200;
		String message = null;
		try {
			do {
				if (ExStringUtils.isBlank(resourceids) || ExStringUtils.isBlank(type)) {
					statusCode = 300;
					message = "设置失败";
					continue;
				}
				if(!("handled".equals(type) || "rollback".equals(type))){
					statusCode = 300;
					message = "操作类型不存在";
					continue;
				}
				// 根据id获取需要处理的预退费补交订单信息
				List<Refundback> refundbackList = findEntitysByIds(Refundback.class, CollectionUtils.arrayToList(resourceids.split(",")));
				if(ExCollectionUtils.isEmpty(refundbackList)){
					statusCode = 300;
					message = "没有需要操作的数据";
					continue;
				}
				List<AnnualFees> updateAnnualFeesList = new ArrayList<AnnualFees>(100);
				List<StudentPayment> updatePaymentList = new ArrayList<StudentPayment>(100);
				List<ReturnPremium> updateRpList = new ArrayList<ReturnPremium>(100);
				List<Refundback> updateRefundbackList = new ArrayList<Refundback>(100);
				String annualFeeHql = "from AnnualFees where isDeleted=0 and studentInfo.resourceid=? and yearInfo.resourceid=? and chargingItems=?";
				String RpHql = "from ReturnPremium where isDeleted=0 and orderNo=?";
				StudentInfo studentInfo = null;
				YearInfo yearInfo = null;
				String chargingItems =  null;
				String processType = null;
				Double moneyRp = 0d;
				Double moneyPa = 0d;
				Map<String,Object> condition = new HashMap<String, Object>();
				for(Refundback r : refundbackList){
					studentInfo = r.getStudentInfo();
					yearInfo = r.getYearInfo();
					chargingItems = r.getChargingItems();
					processType = r.getProcessType();
					if("handled".equals(type)){
						// 已处理的跳过
						if(r.getProcessStatus().equals(Refundback.PROCESSSTATUS_HANDLED)){
							continue;
						}
						// 原年度应缴金额
						Double recpayFee_r = 0d;
						// 现年度应缴金额
						Double recpayFee_o = 0d;
						// 处理年度缴费信息，如果是学费，还要处理学生缴费标准
						AnnualFees annualFees = annualFeesService.findUnique(annualFeeHql, studentInfo.getResourceid(),yearInfo.getResourceid(),chargingItems);
						recpayFee_r = annualFees.getRecpayFee();
						// 处理类型
						Double money = r.getMoney();
						if("returnPremium".equals(processType)){// 退费
							moneyRp = r.getMoney();
							recpayFee_o = recpayFee_r - moneyRp;
							annualFees.setReturnPremiumFee(moneyRp+(annualFees.getReturnPremiumFee()==null?0d:annualFees.getReturnPremiumFee()));
							// 应缴金额
							annualFees.setRecpayFee(recpayFee_o);
						}else if("afterPayment".equals(processType)){// 补交
							moneyPa = r.getMoney();
							recpayFee_o = recpayFee_r + moneyPa;
							annualFees.setPayAmount(moneyPa+(annualFees.getPayAmount()==null?0d:annualFees.getPayAmount()));
							// 应缴金额
							annualFees.setRecpayFee(recpayFee_o);
						}
						// 需要更新的年度缴费信息
						updateAnnualFeesList.add(annualFees);
						
						// 收费项
						if("tuition".equals(chargingItems)){// 学费
							condition.clear();
							condition.put("studyNo", r.getStudyNo());
							List<StudentPayment> paymentList = studentPaymentService.findStudentPaymentByCondition(condition);
							if(paymentList!=null&&paymentList.size()>0){
								//设置总的缴费标准信息
								StudentPayment studentPayment = paymentList.get(0);
								studentPayment.setReturnPremiumFee(moneyRp+(studentPayment.getReturnPremiumFee()!=null?studentPayment.getReturnPremiumFee():0.0));
								studentPayment.setPayAmount(moneyPa+(studentPayment.getPayAmount()!=null?studentPayment.getPayAmount():0d));
								studentPayment.setRecpayFee(studentPayment.getRecpayFee()-recpayFee_r+recpayFee_o);
								if(studentPayment.getFacepayFee()!=0){
									if((studentPayment.getFacepayFee()+studentPayment.getPayAmount())>=studentPayment.getRecpayFee()){
										studentPayment.setChargeStatus("1");
									}else{
										studentPayment.setChargeStatus("-1");
									}
								}
								// 需要更新的学生总的缴费信息
								updatePaymentList.add(studentPayment);
							}
						}
						
						// 生成退费或补交订单(即退费详细记录)
						ReturnPremium rp = returnPremiumService.createReturnPremium(money, annualFees.getFacepayFee(), r.getPaymentMethod(), 
								recpayFee_o, studentInfo, yearInfo, 1, processType,r.getOrderNo(),chargingItems);
						
						updateRpList.add(rp);
						// 修改预退费补交订单状态
						r.setProcessStatus(Refundback.PROCESSSTATUS_HANDLED);
						// 需要更新预退费补交缴费订单信息
						updateRefundbackList.add(r);
						
					} else if ("rollback".equals(type)){
						// 未处理的跳过
						if(r.getProcessStatus().equals(Refundback.PROCESSSTATUS_PENDING)){
							continue;
						}
						// 处理年度缴费信息，如果是学费，还要处理学生缴费标准
						// 退费补交订单
						ReturnPremium rp = returnPremiumService.findUnique(RpHql, r.getOrderNo());
						Double rollbackMoney = 0d;
						// 年度缴费信息
						AnnualFees annualFees = annualFeesService.findUnique(annualFeeHql, studentInfo.getResourceid(),yearInfo.getResourceid(),chargingItems);
						// 处理类型
						if("returnPremium".equals(processType)){// 退费
							rollbackMoney = rp.getReturnPremiumFee();
							annualFees.setRecpayFee(annualFees.getRecpayFee()+rollbackMoney);
							annualFees.setReturnPremiumFee(annualFees.getReturnPremiumFee()-rollbackMoney);
						}else if("afterPayment".equals(processType)){// 补交
							rollbackMoney = rp.getPayAmount();
							annualFees.setRecpayFee(annualFees.getRecpayFee()-rollbackMoney);
							annualFees.setPayAmount(annualFees.getPayAmount()-rollbackMoney);
						}
						// 需要更新的年度缴费信息
						updateAnnualFeesList.add(annualFees);
						
						// 学费
						if("tuition".equals(chargingItems)){// 学费
							condition.clear();
							condition.put("studyNo", r.getStudyNo());
							List<StudentPayment> paymentList = studentPaymentService.findStudentPaymentByCondition(condition);
							if(paymentList!=null&&paymentList.size()>0){
								//设置总的缴费标准信息
								StudentPayment sp = paymentList.get(0);
								// 处理类型
								if("returnPremium".equals(processType)){// 退费
									sp.setRecpayFee(sp.getRecpayFee()+rollbackMoney);
									sp.setReturnPremiumFee(sp.getReturnPremiumFee()-rollbackMoney);
								}else if("afterPayment".equals(processType)){// 补交
									sp.setRecpayFee(sp.getRecpayFee()-rollbackMoney);
									sp.setPayAmount(sp.getPayAmount()-rollbackMoney);
								}
								// 需要更新的学生总的缴费信息
								updatePaymentList.add(sp);
							}
						}
						
						// 删除退费或补交订单
						rp.setIsDeleted(1);
						updateRpList.add(rp);
						
						// 修改预退费补交订单状态
						r.setProcessStatus(Refundback.PROCESSSTATUS_PENDING);
						// 需要更新预退费补交缴费订单信息
						updateRefundbackList.add(r);
					} 
				}
				
				// 保存
				if(ExCollectionUtils.isNotEmpty(updateAnnualFeesList)) {
					annualFeesService.batchSaveOrUpdate(updateAnnualFeesList);
				}
				if(ExCollectionUtils.isNotEmpty(updatePaymentList)) {
					studentPaymentService.batchSaveOrUpdate(updatePaymentList);
				}
				if(ExCollectionUtils.isNotEmpty(updateRpList)) {
					returnPremiumService.batchSaveOrUpdate(updateRpList);
				}
				if(ExCollectionUtils.isNotEmpty(updateRefundbackList)) {
					batchSaveOrUpdate(updateRefundbackList);
				}
				
			} while (false);
		} catch (ServiceException e) {
			logger.error("批量处理或回退预退费补交订单出错", e);
			statusCode = 300;
			message = "处理失败";
		} finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message);
		}
		
		return returnMap;
	}

	/**
	 * 根据条件获取预退费补交订单信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RefundbackVO> findRefundbackInfoByCondition(Map<String, Object> condition) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(20);
		StringBuffer sql = new StringBuffer(1024);
		sql.append("select rownum showOrder,u.unitname,rb.studyno,so.studentname,sci.changetype,rb.money,sci.bankname accountName,");
		sql.append("sci.bankaddress openingBank,sci.cardno bankAccount,to_char(sci.auditdate,'yyyy-mm-dd') auditingDate,y.yearname,");
		sql.append("rb.chargingitems,rb.processtype,rb.processstatus from edu_fee_refundback rb ");
		sql.append("inner join edu_roll_studentinfo so on so.isdeleted=0 and rb.studentinfoid=so.resourceid  ");
		sql.append("inner join edu_roll_stuchangeinfo sci on sci.isdeleted=0 and sci.resourceid=rb.stuchangeid  ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and y.resourceid=rb.yearid  ");
		sql.append("inner join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=so.branchschoolid  ");
		sql.append("where rb.isdeleted=0  ");
		
		if(condition.containsKey("brSchoolId")){
			sql.append("and so.branchschoolid=:brSchoolId ");
			params.put("brSchoolId", condition.get("brSchoolId"));
		}
		if(condition.containsKey("majorId")){
			sql.append("and so.majorid=:majorId ");
			params.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("gradeId")){
			sql.append("and so.gradeid=:gradeId ");
			params.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			sql.append("and so.classicid=:classicId ");
			params.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("yearId")){
			sql.append("and rb.yearid=:yearId ");
			params.put("yearId", condition.get("yearId"));
		}
		if(condition.containsKey("studyNo")){
			sql.append("and rb.studyno=:studyNo ");
			params.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("studentName")){
			sql.append("and so.studentname like :studentName ");
			params.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("chargingItems")){
			sql.append("and rb.chargingitems=:chargingItems ");
			params.put("chargingItems", condition.get("chargingItems"));
		}
		if(condition.containsKey("processType")){
			sql.append("and rb.processtype=:processType ");
			params.put("processType", condition.get("processType"));
		}
		if(condition.containsKey("processStatus")){
			sql.append("and rb.processstatus=:processStatus ");
			params.put("processStatus", condition.get("processStatus"));
		}
		if(condition.containsKey("changeType")){
			sql.append("and sci.changetype=:changeType ");
			params.put("changeType", condition.get("changeType"));
		}
		if(condition.containsKey("beginDate")){
			sql.append("and rb.createdate>=:beginDate ");
			params.put("beginDate", condition.get("beginDate"));
		}
		if(condition.containsKey("endDate")){
			sql.append("and rb.createdate<=:endDate ");
			params.put("endDate", condition.get("endDate"));
		}
		if(condition.containsKey("refundbackIds")){
			sql.append("and rb.resourceid in(:refundbackIds )");
			params.put("endDate", condition.get("endDate"));
		}
		
		sql.append("order by rownum,rb.createdate,rb.studyno  ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), RefundbackVO.class, params);
	}

	/**
	 * 退补教材费
	 * 
	 * @param stuChangeId
	 * @param processType
	 * @param money
	 * @return
	 */
	@Override
	public Map<String, Object> refundSuppleTextbookFee(String stuChangeId, String processType, Double money) {
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功";
		try {
			do{
				String chargingItems = "textbookFee";
				// 获取当前年份
				String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
				YearInfo year = yearInfoService.getByFirstYear(Long.valueOf(currentYear));
				// 检查该条异动申请记录是否已生成预退费补交教材费订单，有则提示，没有则新增
				String rbsql = "from Refundback where isDeleted=0 and yearInfo.resourceid=? and chargingItems=? and changeInfo.resourceid=?";
				Refundback rb = findUnique(rbsql, year.getResourceid(),chargingItems,stuChangeId);
				if(rb != null){
					statusCode = 300;
					message = "该学籍异动已生成预退费补交教材费订单！";
					continue;
				}
				StuChangeInfo changeInfo = stuChangeInfoService.get(stuChangeId);
				StudentInfo studentInfo = changeInfo.getStudentInfo();
				// 年度缴费信息
				String annualFeeHql = "from AnnualFees where isDeleted=0 and studentInfo.resourceid=? and yearInfo.resourceid=? and chargingItems=?";
				AnnualFees annualFees = annualFeesService.findUnique(annualFeeHql, studentInfo.getResourceid(),year.getResourceid(),"textbookFee");
				if(annualFees == null){
					statusCode = 300;
					message = "请先联系管理员生成年教材费标准！";
					continue;
				}
				// 如果是退费处理类型，则需判断退费金额是否小于等于已缴金额
				Double facePayFee = annualFees.getFacepayFee()-(annualFees.getReturnPremiumFee()==null?0d:annualFees.getReturnPremiumFee());
				if("returnPremium".equals(processType) && money.compareTo(facePayFee)>0){
					statusCode = 300;
					message = "退费金额不能大于已缴金额，请检查一下！";
					continue;
				}
				// 创建预退费补交教材费订单
				createRefundback(changeInfo, chargingItems, studentInfo, processType, year, money);
				
			}while(false);
		} catch (Exception e) {
			logger.error("退补教材费出错", e);
			statusCode = 300;
			message = "操作失败";
		}finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		return map;
	}
	
	/**
	 * 创建预退费补交订单
	 * @param changeInfo
	 * @param chargingItems
	 * @param studentInfo
	 * @param processType
	 * @param year
	 * @param money
	 */
	@Override
	public void createRefundback(StuChangeInfo changeInfo,String chargingItems,
			StudentInfo studentInfo,String processType,YearInfo year,Double money) {
		Refundback refundback = new Refundback();
	    Date d = new Date();
	    refundback.setChangeInfo(changeInfo);
	    refundback.setChargingItems(chargingItems);
	    refundback.setCreateDate(d);
	    refundback.setMoney(money);
	    refundback.setOrderNo(studentInfo.getStudyNo()+d.getTime());
	    refundback.setPaymentMethod("5");
	    refundback.setProcessStatus(Refundback.PROCESSSTATUS_PENDING);
	    refundback.setProcessType(processType);
	    refundback.setStudentInfo(studentInfo);
	    refundback.setStudyNo(studentInfo.getStudyNo());
	    refundback.setYearInfo(year);
		// 保存
	    saveOrUpdate(refundback);
	}

	/**
	 * 编辑退补订单信息
	 * 
	 * @param resourceid
	 * @param money
	 * @return
	 */
	@Override
	public Map<String, Object> editRefundback(String resourceid,Double money) {
		Map<String, Object> returnMap = new HashMap<String, Object>(10);
		int statusCode =200;
		String message = null;
		try {
			do{
				Refundback refundback = get(resourceid);
				// 获取年度缴费标准
				AnnualFees annualFees = annualFeesService.findUniqueByCondition(refundback.getStudentInfo().getResourceid(),refundback.getYearInfo().getResourceid(),"textbookFee");
				// 如果是退费，判断修改金额是否大于已缴金额
				Double facePayFee = annualFees.getFacepayFee()-(annualFees.getReturnPremiumFee()==null?0d:annualFees.getReturnPremiumFee());
				if("returnPremium".equals(refundback.getProcessType()) && money.compareTo(facePayFee)>0){
					statusCode = 300;
					message = "退费金额不能大于已缴金额，请检查一下！";
					continue;
				}
				refundback.setMoney(money);
				// 保存
				saveOrUpdate(refundback);
			}while(false);
		} catch (ServiceException e) {
			logger.error("", e);
			statusCode = 300;
			message = "编辑退补订单信息失败！";
		} finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message);
		}
		
		return returnMap;
		
	}
}
