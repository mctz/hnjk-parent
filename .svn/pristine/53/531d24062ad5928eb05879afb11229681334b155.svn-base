package com.hnjk.edu.finance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.edu.finance.service.IFeeCommissionInfoService;
import com.hnjk.edu.finance.vo.TransferSchoolFeeVo;

/** 
 * 学费提成接口实现
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 22, 2016 4:29:38 PM 
 * 
 */
@Service("feeCommissionInfoService")
@Transactional
public class FeeCommissionInfoServiceImpl extends BaseSupportJdbcDao implements IFeeCommissionInfoService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;
	
	/**
	 * 查询学费提成明细列表-广东医版本
	 * @param condition
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findFeeCommissionInfoByJDBC(Map<String, Object> condition) {
		StringBuffer sql     = new StringBuffer(1024);
		List<String> values = new ArrayList<String>();
		String operatedate = (String)condition.get("operatedate");
		String year = operatedate.substring(0, 4);
		String month = operatedate.substring(operatedate.lastIndexOf("-")+1);
		//查看不了实收的数据,因为日期问题
		String beginDate = ((int)(Integer.parseInt(year)-1))+"-12";;
		String endDate = year+"-";		
		if((Integer.valueOf(month)+1)<10){
			endDate += "0";
		}
		endDate += (Integer.valueOf(month)+1);
		// 查询sql
		sql.append("select u.resourceid unitid,u.unitname,u.unitshortname,cc.classicname,g.gradename,so.studentNum,feeStandard.creditfee,u.royaltyrate,nvl(paidup.spdamount,0)-nvl(retfee.returnfee,0) spdamount,nvl(unpaid.stunum,0) paynum, nvl(retfee.returnfee,0) returnfee ");
		sql.append(" from");
		sql.append(" (select so1.branchschoolid,so1.gradeid,so1.classicid,nvl(count(so1.resourceid),0) studentNum");
		sql.append(" from edu_roll_studentinfo so1,edu_base_grade g1,edu_base_year y");
		sql.append(" where so1.isdeleted=0 ");
		sql.append(" and so1.gradeid=g1.resourceid ");
		sql.append(" and g1.yearid=y.resourceid ");
		sql.append(" and y.isdeleted=0 ");
		sql.append(" and g1.isdeleted=0");
		sql.append(" and so1.studentstatus in('11','21','25')");//学籍状态
		sql.append(" and y.firstyear>="+(Integer.parseInt(year)-2));
		sql.append(" group by so1.branchschoolid,so1.gradeid,so1.classicid) so");
		sql.append(" inner join edu_base_grade g on g.isdeleted=0 and g.resourceid=so.gradeid");
		sql.append(" inner join edu_base_classic cc on cc.isdeleted=0 and cc.resourceid=so.classicid ");
		sql.append(" inner join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=so.branchschoolid ");
		//学费标准
		sql.append(" left join (select py.gradeid,py.paymenttype,pyd.creditfee from edu_fee_payyeardetails pyd,edu_fee_payyear py");
		sql.append(" where pyd.payyearid=py.resourceid and pyd.isdeleted=0 and py.isdeleted=0 group by py.gradeid,py.paymenttype,pyd.creditfee) feeStandard");
		sql.append(" on feeStandard.paymenttype=cc.classiccode and feeStandard.Gradeid=g.resourceid");
		//实收学费金额
		sql.append(" left join (select so.branchschoolid,so.gradeid,so.classicid,nvl(sum(spd.payamount),0) spdamount  from edu_fee_studengpaymentdetails spd,");
		sql.append(" edu_roll_studentinfo so");
		sql.append(" where spd.isdeleted=0 ");
		sql.append(" and so.resourceid=spd.studentinfoid ");
//		sql.append(" and so.isdeleted=0 and so.studentstatus in('11','25')");
		sql.append(" and so.isdeleted=0 ");
		sql.append(" and spd.chargingItems='tuition'");
		sql.append(" and spd.operatedate >= to_date('"+beginDate+"','yyyy-MM')");
		sql.append(" and spd.operatedate < to_date('"+endDate+"','yyyy-MM')");
		sql.append(" group by so.branchschoolid,so.gradeid,so.classicid ");
		sql.append(" ) paidup on paidup.branchschoolid=u.resourceid and paidup.gradeid=g.resourceid and paidup.classicid=cc.resourceid ");
		// 退费的学费
		sql.append(" left join (select sio.branchschoolid,sio.gradeid,sio.classicid,nvl(sum(rp.returnpremiumfee),0) returnfee from edu_fee_returnpremium rp, ");
		sql.append(" edu_roll_studentinfo sio ");
//		sql.append(" where rp.studentinfoid=sio.resourceid and sio.studentstatus in('11','21','25')");
		sql.append(" where rp.studentinfoid=sio.resourceid");
		sql.append(" and sio.isdeleted=0 and rp.isdeleted=0 ");
		sql.append(" and rp.chargingItems='tuition' and rp.processType='returnPremium'");
		sql.append(" and rp.createdate>= to_date('"+beginDate+"','yyyy-MM')");
		sql.append(" and rp.createdate< to_date('"+endDate+"','yyyy-MM')");
		sql.append(" group by sio.branchschoolid,sio.gradeid,sio.classicid) retfee  ");
		sql.append(" on retfee.branchschoolid=u.resourceid and retfee.gradeid=g.resourceid and retfee.classicid=cc.resourceid  ");
		//已缴学费人数
		sql.append(" left join (select stu.branchschoolid,stu.gradeid,stu.classicid,count(stu.resourceid) stunum from ");
		sql.append(" (select spdnum.studentinfoid,nvl(sum(spdnum.payamount),0) payamount from edu_fee_studengpaymentdetails spdnum ");
		sql.append(" where spdnum.isdeleted=0 ");
		//+ subStr
		sql.append(" and spdnum.chargingItems='tuition'");
		sql.append(" and spdnum.operatedate >= to_date('"+beginDate+"','yyyy-MM')");
		sql.append(" and spdnum.operatedate < to_date('"+endDate+"','yyyy-MM')");
		sql.append(" group by spdnum.studentinfoid ) spdstu, ");
		sql.append(" edu_roll_studentinfo stu ");
		sql.append(" where stu.resourceid=spdstu.studentinfoid ");
		sql.append(" and stu.isdeleted=0 and stu.studentstatus in('11','21','25')");
//		sql.append(" and stu.isdeleted=0 ");
		sql.append(" and to_number(spdstu.payamount) > 0");
		sql.append(" group by stu.branchschoolid,stu.gradeid,stu.classicid");
		sql.append(" ) unpaid on unpaid.branchschoolid=u.resourceid and unpaid.gradeid=g.resourceid and unpaid.classicid=cc.resourceid ");
		sql.append(" where 1=1");
			
		if(condition.containsKey("brSchool")){
			sql.append(" and u.resourceid =?");
			values.add(condition.get("brSchool").toString());
		}
		sql.append(" order by u.unitcode,cc.classiccode,g.gradename");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = jdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), values.toArray());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询学费提成明细列表-广外版本
	 * @param condition
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findFeeCommissionExt(Map<String, Object> condition) {
		StringBuffer sql     = new StringBuffer(1024);
		List<String> values = new ArrayList<String>();
		String beginDate = ExStringUtils.toString(condition.get("beginDate")) +"  00:00:00";
		String endDate = ExStringUtils.toString(condition.get("endDate"))+"  23:59:59";

		sql.append("select u.resourceid unitid,u.unitname,u.unitshortname,u.royaltyrate, nvl(retfee.returnfee,0) returnfee,u.islocal ");
		sql.append(",paidup.spdamount_tuition+nvl(pa.payamount,0)-nvl(retfee.returnfee,0) spdamount_tuition,u.royaltyrate2,u.reserveratio ");
		sql.append(",paidup.royaltyRatePay+nvl(pa.notForeignLngPa,0)-nvl(retfee.notForeignLngRetfee,0) royaltyRatePay");
		sql.append(",paidup.royaltyRate2Pay+nvl(pa.foreignLngPa,0)-nvl(retfee.foreignLngRetfee,0) royaltyRate2Pay");
		sql.append(",rtamounts.firstReturnPay,rtamounts.secondReturnPay,nvl(pa.payamount,0) payamount");
		sql.append(" from hnjk_sys_unit u ");
		//实收学费金额
		sql.append(" left join (select so.branchschoolid,nvl(sum(spd.payamount),0) spdamount_tuition  ");
		//外语类和非外语类实收学费金额（不包括教材费等）
		sql.append(",sum(decode(m.isForeignLng,'Y',spd.payamount,0)) royaltyRate2Pay");
		sql.append(",sum(decode(m.isForeignLng,'Y',0,spd.payamount)) royaltyRatePay");
		sql.append(" from edu_fee_studengpaymentdetails spd join edu_roll_studentinfo so on so.isdeleted=0 and so.resourceid=spd.studentinfoid ");
		sql.append(" join edu_base_major m on m.resourceid=so.majorid");
		sql.append(" where spd.isdeleted=0 ");
//		sql.append(" and so.isdeleted=0 and so.studentstatus in('11','25')");
		sql.append(" and spd.chargingItems='tuition'");
		sql.append(" and spd.operatedate >= to_date('"+beginDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" and spd.operatedate <= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" group by so.branchschoolid ");
		sql.append(" ) paidup on paidup.branchschoolid=u.resourceid ");
		// 退费的金额
		sql.append(" left join (select sio.branchschoolid,nvl(sum(rp.returnpremiumfee),0) returnfee ");
		sql.append(" ,sum(decode(m.isForeignLng,'Y',rp.returnpremiumfee,0)) foreignLngRetfee ");
		sql.append(" ,sum(decode(m.isForeignLng,'Y',0,rp.returnpremiumfee)) notForeignLngRetfee ");
		sql.append("  from edu_fee_returnpremium rp ");
		sql.append(" inner join edu_roll_studentinfo sio on sio.isdeleted=0 and rp.studentinfoid=sio.resourceid ");
		sql.append(" inner join edu_base_major m on m.resourceid=sio.majorid ");
		sql.append(" where rp.isdeleted=0 ");
		sql.append(" and rp.chargingItems='tuition' and rp.processType='returnPremium'");
		sql.append(" and rp.createdate>= to_date('"+beginDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" and rp.createdate<= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" group by sio.branchschoolid) retfee  ");
		sql.append(" on retfee.branchschoolid=u.resourceid ");
		// 补交的金额
		sql.append(" left join (select sio.branchschoolid,nvl(sum(rp.payamount),0) payamount ");
		sql.append(" ,sum(decode(m.isForeignLng,'Y',rp.payamount,0)) foreignLngPa");
		sql.append(" ,sum(decode(m.isForeignLng,'Y',0,rp.payamount)) notForeignLngPa ");
		sql.append("  from edu_fee_returnpremium rp ");
		sql.append(" inner join edu_roll_studentinfo sio on sio.isdeleted=0 and rp.studentinfoid=sio.resourceid ");
		sql.append(" inner join edu_base_major m on m.resourceid=sio.majorid ");
		sql.append(" where rp.isdeleted=0 ");
		sql.append(" and rp.chargingItems='tuition' and rp.processType='afterPayment'");
		sql.append(" and rp.createdate>= to_date('"+beginDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" and rp.createdate<= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" group by sio.branchschoolid) pa  ");
		sql.append(" on pa.branchschoolid=u.resourceid ");
		//已反学费金额
		sql.append(" left join (select rm.unitid,sum(decode(rm.count,1,rm.amounts,0)) firstReturnPay,sum(decode(rm.count,2,rm.amounts,0)) secondReturnPay ");
		sql.append(" from edu_fee_returnamounts rm where rm.isdeleted=0 ");
		sql.append(" and rm.operatedate>= to_date('"+beginDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" and rm.operatedate<= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss')");
		sql.append(" group by rm.unitid) rtamounts on rtamounts.unitid=u.resourceid");
		sql.append(" where 1=1");
			
		if(condition.containsKey("brSchool")){
			sql.append(" and u.resourceid =?");
			values.add(condition.get("brSchool").toString());
		}
		sql.append(" and u.unitname!='未分配'");
		sql.append(" order by u.unitcode");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = jdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), values.toArray());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Page findNotPayStudentFee(Map<String, Object> condition,Page objPage) {
		StringBuffer sql     = new StringBuffer();
		List<String> values = new ArrayList<String>();
		getNotPayQuerySql(sql,values,condition);
		Page page = null;
		try {
			page = jdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public List<Map<String, Object>> findNotPayStudentFee(Map<String, Object> condition) {
		StringBuffer sql     = new StringBuffer();
		List<String> values = new ArrayList<String>();
		getNotPayQuerySql(sql,values,condition);
		List<Map<String, Object>> list = null;
		try {
			list = jdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void getNotPayQuerySql(StringBuffer sql, List<String> values,Map<String, Object> condition) {
		String operatedate = condition.get("operatedate").toString();
		String year = operatedate.substring(0, 4);
		String month = operatedate.substring(operatedate.lastIndexOf("-")+1);
		String beginDate = ((int)(Integer.parseInt(year)-1))+"-12";;
		String endDate = year+"-"+(Integer.valueOf(month)+1);	
		//教学点，学号，年级，班级，层次，学习形式，缴费标准
		sql.append(" select u.unitname,si.studyno,g.gradename,cl.classesname,ci.classicname,si.teachingtype,feeStandard.creditfee");
		sql.append(" from edu_roll_studentinfo si ");
		sql.append(" join hnjk_sys_unit u on u.resourceid=si.branchschoolid");
		sql.append(" join edu_base_grade g on g.resourceid=si.gradeid and g.isdeleted=0");
		sql.append(" join edu_base_year y on y.resourceid = g.yearid and y.isdeleted = 0");
		sql.append(" join edu_base_classic ci on ci.resourceid=si.classicid");
		sql.append(" left join edu_roll_classes cl on cl.resourceid=si.classesid");
		//学费标准
		sql.append(" left join (select py.gradeid,py.paymenttype,pyd.creditfee from edu_fee_payyeardetails pyd,edu_fee_payyear py");
		sql.append(" where pyd.payyearid=py.resourceid and pyd.isdeleted=0 and py.isdeleted=0 group by py.gradeid,py.paymenttype,pyd.creditfee) feeStandard");
		sql.append(" on feeStandard.paymenttype=ci.classiccode and feeStandard.Gradeid=g.resourceid");
		sql.append(" where si.isdeleted=0 and si.studentstatus in('11','21','25')");
		sql.append(" and y.firstyear >= '"+(Integer.parseInt(year)-2)+"' and si.resourceid not in (");
		//已缴学生
		sql.append(" select stu.resourceid from (select spdnum.studentinfoid,nvl(sum(spdnum.payamount),0) payamount from edu_fee_studengpaymentdetails spdnum"); 
		sql.append(" where spdnum.isdeleted=0 and spdnum.operatedate >= to_date('"+beginDate+"', 'yyyy-MM') and spdnum.operatedate < to_date('"+endDate+"', 'yyyy-MM')");
		sql.append(" group by spdnum.studentinfoid ) spdstu join edu_roll_studentinfo stu on stu.resourceid = spdstu.studentinfoid");
		sql.append(" and stu.resourceid=spdstu.studentinfoid and stu.studentstatus in('11','21','25') and stu.isdeleted=0 and to_number(spdstu.payamount) > 0");
		sql.append(" join edu_base_grade g on g.resourceid = stu.gradeid and g.isdeleted = 0 join edu_base_year y on y.resourceid = g.yearid and y.isdeleted = 0 and y.firstyear>='"+(Integer.parseInt(year)-2)+"'");
		if(condition.containsKey("brSchool")){
			sql.append(" and stu.branchschoolid =?");
			values.add(condition.get("brSchool").toString());
		}
		sql.append(" )");
		
		if(condition.containsKey("stuids")){
			sql.append(" and si.studyno in("+condition.get("stuids")+")");
		}
		if(condition.containsKey("brSchool")){
			sql.append(" and u.resourceid =?");
			values.add(condition.get("brSchool").toString());
		}
		if(condition.containsKey("unitName")){
			sql.append(" and u.unitName =?");
			values.add(condition.get("unitName").toString());
		}
		sql.append(" order by unitname,gradename,classicname,classesname");
	}

	/**
	 * 查询转教学点学费分成信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param separateDate 格式如：07-01
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TransferSchoolFeeVo> findChangeUnitFeeInfo(String beginDate, String endDate, String separateDate) throws Exception {
		StringBuffer sql = new StringBuffer(1024);
		sql.append("select sci.changebeforebrschoolid||'_'||nvl(m.isforeignlng,'N') beforeSchoolId,sci.changebrschoolid||'_'||nvl(m.isforeignlng,'N') schoolId ");
		sql.append(",(nvl(nf.facepayfee,0)+nvl(nf.payamount,0)-nvl(nf.returnpremiumfee,0))*0.5 money from edu_roll_stuchangeinfo sci ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and y.firstyear=to_number(to_char(sci.applicationdate,'yyyy')) ");
		sql.append("inner join edu_fee_annualfees nf on nf.isdeleted=0 and nf.yearid=y.resourceid and nf.studentid=sci.studentid ");
		sql.append("inner join edu_base_major m on m.isdeleted=0 and m.resourceid=sci.changemajorid ");
		sql.append("where sci.isdeleted=0 and sci.changetype in ('81','82') and sci.finalauditstatus='Y' ");
		sql.append("and to_date(to_char(sci.applicationdate,'MM-dd'),'MM-dd')>to_date('"+separateDate+"','MM-dd') ");
		sql.append("and sci.changebeforebrschoolid!=sci.changebrschoolid ");
		sql.append("and sci.applicationdate<=to_date('"+beginDate+"','yyyy-MM-dd') ");
		sql.append("and sci.applicationdate>=to_date('"+endDate+"','yyyy-MM-dd')");
		
		return jdbcDao.getBaseJdbcTemplate().findList(sql.toString(), null, TransferSchoolFeeVo.class);
	}
}
