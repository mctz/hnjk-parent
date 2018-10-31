package com.hnjk.edu.finance.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;

/**
 * 学生缴费明细接口的实现
 * <code>StudentPaymentDetailsServiceImpl</code><p>
 * 
 * @author：  zik, 广东学苑教育发展有限公司
 * @since： 2015-11-16 16:36
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentPaymentDetailsService")
public class StudentPaymentDetailsServiceImpl extends BaseServiceImpl<StudentPaymentDetails>implements IStudentPaymentDetailsService {
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 根据条件获取学生缴费明细列表-page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findStudentPaymentDetailsByCondition(Page objPage,Map<String, Object> condition) throws ServiceException {
		Page page = null;
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			StringBuffer hql = findPaymentDetailsByContionHql(condition,param);
			page = findByHql(objPage, hql.toString(), param);
//			Map<String, Object> payCondition = new HashMap<String, Object>();
		} catch (Exception e) {
			logger.error("根据条件获取学生缴费明细列表-page出错", e);
		}
		return page;
	}
	
	/**
	 * 根据条件获取学生缴费明细列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentPaymentDetails> findStudentPaymentDetailsByCondition(Map<String, Object> condition) throws ServiceException {
		List<StudentPaymentDetails> stuPaymentDetailList = new ArrayList<StudentPaymentDetails>();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			StringBuffer hql = findPaymentDetailsByContionHql(condition,param);
//			hql.append(" order by spd.studentInfo.studyNo,spd.operateDate desc ");
			stuPaymentDetailList = findByHql(hql.toString(), param);
		} catch (Exception e) {
			logger.error("根据条件获取学生缴费明细列表出错", e);
		}
		return stuPaymentDetailList;
	}

	/**
	 * 根据条件获取学生缴费明细列表HQL
	 * @param condition
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private StringBuffer findPaymentDetailsByContionHql(Map<String, Object> condition,Map<String, Object> param) throws UnsupportedEncodingException {
		StringBuffer hql = new StringBuffer();
		hql.append("from "+StudentPaymentDetails.class.getSimpleName() +" spd where spd.isDeleted=:isDeleted ");
		param.put("isDeleted", 0);
		
		if(condition.containsKey("detailIds")){//勾选id
			String[] ids = ((String)condition.get("detailIds")).split(",");
			StringBuffer detailId = new StringBuffer();
			for(String id : ids){
				detailId.append("'"+id+"',");
			}
			detailId.deleteCharAt(detailId.length()-1);
			hql.append(" and spd.resourceid in "+"("+detailId.toString()+")");
		}else{
			if(condition.containsKey("studentInfoId")){// 学籍ID
				hql.append(" and spd.studentInfo.resourceid=:studentInfoId");
				param.put("studentInfoId", condition.get("studentInfoId"));
			}
			if(condition.containsKey("payType")){// 缴费类型
				hql.append(" and spd.payType=:payType");
				param.put("payType", condition.get("payType"));
			}
			if(condition.containsKey("operatorId")){// 操作人ID
				hql.append(" and spd.operatorId=:operatorId");
				param.put("operatorId", condition.get("operatorId"));
			}
			if(condition.containsKey("brSchool")){//学习中心
				hql.append(" and spd.studentInfo.branchSchool.resourceid=:brSchool ");
				param.put("brSchool", condition.get("brSchool"));
			}
			if(condition.containsKey("gradeid")){//年级
				hql.append(" and spd.studentInfo.grade.resourceid=:gradeid ");
				param.put("gradeid", condition.get("gradeid"));
			}else if(condition.containsKey("gradeId")){
				hql.append(" and spd.studentInfo.grade.resourceid=:gradeid ");
				param.put("gradeid", condition.get("gradeId"));
			}
			if(condition.containsKey("majorid")){//专业
				hql.append(" and spd.studentInfo.major.resourceid=:majorid ");
				param.put("majorid", condition.get("majorid"));
			}else if (condition.containsKey("majorId")) {
				hql.append(" and spd.studentInfo.major.resourceid=:majorid ");
				param.put("majorid", condition.get("majorId"));
			}
			if(condition.containsKey("classicid")){//层次
				hql.append(" and spd.studentInfo.classic.resourceid=:classicid ");
				param.put("classicid", condition.get("classicid"));
			}else if(condition.containsKey("classicId")){
				hql.append(" and spd.studentInfo.classic.resourceid=:classicid ");
				param.put("classicid", condition.get("classicId"));
			}
			if(condition.containsKey("name")){//学生姓名
				hql.append(" and spd.studentInfo.studentName like :name ");
				param.put("name", "%"+condition.get("name")+"%");
			}
			if(condition.containsKey("studyNo")){//学号
				hql.append(" and spd.studentInfo.studyNo =:studyNo ");
				param.put("studyNo", condition.get("studyNo"));
			}		

			if(condition.containsKey("studentStatus")){//学籍状态
				hql.append(" and spd.studentInfo.studentStatus=:studentStatus ");
				param.put("studentStatus", condition.get("studentStatus"));
			}
			if(condition.containsKey("classesId")){//班级ID
				hql.append(" and spd.studentInfo.classes.resourceid=:classesId ");
				param.put("classesId", condition.get("classesId"));
			}
			if(condition.containsKey("payDate")){//缴费日期
				hql.append(" and to_char(spd.operateDate,'yyyy-mm-dd')=:operateDate ");
				param.put("operateDate", condition.get("payDate"));
			}
			if(condition.containsKey("beginDate")){//缴费开始日期
				hql.append(" and to_char(spd.operateDate,'yyyy-mm-dd')>=:beginDate ");
				param.put("beginDate", condition.get("beginDate"));
			}
			if(condition.containsKey("endDate")){//缴费结束日期
				hql.append(" and to_char(spd.operateDate,'yyyy-mm-dd')<=:endDate ");
				param.put("endDate", condition.get("endDate"));
			}
			if(condition.containsKey("receiptNumber_begin") && StringUtils.isNumeric((String) condition.get("receiptNumber_begin"))){//票据号开始
				hql.append(" and to_number(spd.receiptNumber)>=:receiptNumber_begin ");
				param.put("receiptNumber_begin", condition.get("receiptNumber_begin"));
			}
			if(condition.containsKey("receiptNumber_end") && StringUtils.isNumeric((String) condition.get("receiptNumber_end"))){//票据号结束
				hql.append(" and to_number(spd.receiptNumber)<=:receiptNumber_end ");
				param.put("receiptNumber_end", condition.get("receiptNumber_end"));
			}
			
			if(condition.containsKey("examCertificateNo")){//准考证号
				hql.append(" and spd.studentInfo.examCertificateNo=:examCertificateNo ");
				param.put("examCertificateNo", condition.get("examCertificateNo"));
			}
			if(condition.containsKey("paymentMethod")){//付款方式
				hql.append(" and spd.paymentMethod=:paymentMethod ");
				param.put("paymentMethod", condition.get("paymentMethod"));
			}
			if(condition.containsKey("beginPrintDate")){//打印开始日期
				hql.append(" and to_char(spd.printDate,'yyyy-mm-dd')>=:beginPrintDate ");
				param.put("beginPrintDate", condition.get("beginPrintDate"));
			}
			if(condition.containsKey("endPrintDate")){//打印结束日期
				hql.append(" and to_char(spd.printDate,'yyyy-mm-dd')<=:endPrintDate ");
				param.put("endPrintDate", condition.get("endPrintDate"));
			}
			if(condition.containsKey("drawer")){//开票人	
				hql.append(" and spd.drawer like :drawer ");
				param.put("drawer", "%"+URLDecoder.decode((String) condition.get("drawer"),"UTF-8")+"%");
			}
			if(condition.containsKey("year")){//学年	
				hql.append(" and spd.year=:year ");
				param.put("year",condition.get("year"));
			}
			if(condition.containsKey("chargingItems")){// 收费项
				hql.append(" and spd.chargingItems=:chargingItems ");
				param.put("chargingItems",condition.get("chargingItems"));
			}
			if(condition.containsKey("isInvoicing")){// 是否开单位发票
				hql.append(" and spd.isInvoicing=:isInvoicing ");
				param.put("isInvoicing",condition.get("isInvoicing"));
			}
		}
			
		if(condition.containsKey("print")){//打印专用
			hql.append(" and spd.receiptNumber is null");
		}
		if(condition.containsKey("isPrint")){//是否打印
			if("Y".equals(condition.get("isPrint"))){
				hql.append(" and spd.receiptNumber is not null");
			}else if("N".equals(condition.get("isPrint"))){
				hql.append(" and spd.receiptNumber is null");
			}		
		}
		if(condition.containsKey("operateType")){
			if("export".equals(condition.get("operateType"))){
				hql.append(" order by spd.receiptNumber asc");
			}else{
				hql.append(" order by spd.studentInfo.branchSchool.unitCode,spd.studentInfo.classic.classicCode,spd.studentInfo.major.majorCode,spd.studentInfo.studyNo asc");
			}
		}else{
			hql.append(" order by spd.studentInfo.branchSchool.unitCode,spd.studentInfo.classic.classicCode,spd.studentInfo.major.majorCode,spd.studentInfo.studyNo asc");
		}
		
		return hql;
	}

	/**
	 * 根据条件获取收费汇总表
	 */
	@Override
	public List<Map<String, Object>> findChargeSummayForMap(Map<String, Object> condition) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> summarytList = null;
		sql.append("select d.dictname paymentmethod,ci.classicname,");
		if(condition.containsKey("chargeBack") && "Y".equals(condition.get("chargeBack"))){//退费汇总表
			/*if(condition.get("currentFee")!=null){
				sql.append("nvl(sum(:currentFee-spd.paidamount),0) paidamount,");
				sql.append("count(si.resourceid) paidcount,");
				sql.append("count(distinct si.resourceid) stucount,");
				param.put("currentFee", condition.get("currentFee"));
			}else {
				sql.append("sum(0) paidamount,");
				sql.append("sum(0) paidcount,");
				sql.append("sum(0) stucount,");
			}*/
		}else {//收费汇总表
			sql.append("nvl(sum(spd.payamount),0) paidamount,");
			sql.append("count(si.resourceid) paidcount,");
			sql.append("count(distinct si.resourceid) stucount,");
		}
		sql.append(" min(to_number(spd.receiptnumber)) minnumber,max(to_number(spd.receiptnumber)) maxnumber,");
		sql.append(" min(spd.operatedate) mindate,max(spd.operatedate) maxdate");
		sql.append(" from edu_fee_studengpaymentdetails spd ");
		sql.append(" join edu_roll_studentinfo si on si.resourceid=spd.studentinfoid");
		sql.append(" join edu_base_classic ci on ci.resourceid=si.classicid");
		sql.append(" left join hnjk_sys_dict d on d.dictcode like 'CodePaymentMethod_%' and d.dictvalue=spd.paymentMethod");
		sql.append(" where spd.isdeleted=0");
		if(condition.containsKey("chargeBack") && "Y".equals(condition.get("chargeBack")) && condition.get("currentFee")!=null){
			sql.append(" and spd.shouldpayamount !="+condition.get("currentFee") );
		}
		if(condition.containsKey("brSchool")){
			sql.append(" and si.branchschoolid=:brSchool");
			param.put("brSchool", condition.get("brSchool"));
		}
		if(condition.containsKey("majorid")){
			sql.append(" and si.majorid=:majorid");
			param.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){
			sql.append(" and si.classicid=:classicid");
		}
		if(condition.containsKey("gradeid")){
			sql.append(" and si.gradeid=:gradeid");
			param.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("studentStatus")){//学籍状态
			sql.append(" and si.studentStatus=:studentStatus ");
			param.put("studentStatus", condition.get("studentStatus"));
		}
		if(condition.containsKey("payType")){// 缴费类型
			sql.append(" and spd.payType=:payType");
			param.put("payType", condition.get("payType"));
		}
		if(condition.containsKey("paymentMethod")){//付款方式
			sql.append(" and spd.paymentMethod=:paymentMethod ");
			param.put("paymentMethod", condition.get("paymentMethod"));
		}
		if(condition.containsKey("beginDate")){
			sql.append(" and to_char(spd.operateDate,'yyyy-mm-dd')>=:beginDate");
			param.put("beginDate", condition.get("beginDate"));
		}
		if(condition.containsKey("endDate")){
			sql.append(" and to_char(spd.operateDate,'yyyy-mm-dd')<=:endDate");
			param.put("endDate", condition.get("endDate"));
		}
		if(condition.containsKey("receiptNumber_begin") && StringUtils.isNumeric((String) condition.get("receiptNumber_begin"))){//票据号开始
			sql.append(" and to_number(spd.receiptNumber)>=:receiptNumber_begin ");
			param.put("receiptNumber_begin", condition.get("receiptNumber_begin"));
		}
		if(condition.containsKey("receiptNumber_end") && StringUtils.isNumeric((String) condition.get("receiptNumber_end"))){//票据号结束
			sql.append(" and to_number(spd.receiptNumber)<=:receiptNumber_end ");
			param.put("receiptNumber_end", condition.get("receiptNumber_end"));
		}
		if(condition.containsKey("chargingItems")){// 收费项
			sql.append(" and spd.chargingItems=:chargingItems ");
			param.put("chargingItems", condition.get("chargingItems"));
		}
		sql.append(" group by spd.paymentMethod,ci.classiccode,ci.classicname,d.dictcode,d.dictname order by spd.paymentMethod,ci.classiccode ");
		try {
			summarytList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summarytList;
	}
}
