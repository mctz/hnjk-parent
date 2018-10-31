
/**
 * <code>StuPerpayfeeServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:18:38
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.finance.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseJdbcTemplate;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StuPerpayfee;
import com.hnjk.edu.finance.service.IStuPerpayfeeService;
import com.hnjk.edu.roll.model.StudentFactFee;
import com.hnjk.platform.system.service.IDictionaryService;

/**
 * <code>StuPerpayfeeServiceImpl</code><p>
 * 学生预交费用表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:18:38
 * @see 
 * @version 1.0
 */
@Transactional
@Service("stuperpayfeeservice")
public class StuPerpayfeeServiceImpl extends BaseServiceImpl<StuPerpayfee> implements IStuPerpayfeeService {

	@Autowired
	@Qualifier("yxtDataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findFeeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(128);
		sb.append(" from "+StuPerpayfee.class.getSimpleName()+" where 1=1 ");
		sb.append(" and isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("grade")){
			sb.append(" and studentInfo.grade.resourceid = :grade ");
			values.put("grade", condition.get("grade").toString().trim());
		}
		if(condition.containsKey("branchSchool")){
			sb.append(" and studentInfo.branchSchool.resourceid = :branchSchool ");
			values.put("branchSchool", condition.get("branchSchool").toString().trim());
		}
		if(condition.containsKey("major")){
			sb.append( " and studentInfo.major.resourceid = :major ");
			values.put("major", condition.get("major").toString().trim());
		}
		if(condition.containsKey("classic")){
			sb.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("classic").toString().trim());
		}
		sb.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, sb.toString(), values);
	}

	
	
	@Transactional(readOnly=true)
	@Override
	public Page findFactFeeByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(256);
		sb.append(" from "+StudentFactFee.class.getSimpleName()+" where isDeleted =:isDeleted");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("grade")){//缴费年度
			sb.append(" and chargeyear = :grade");
			values.put("grade", Integer.parseInt(condition.get("grade").toString()));
		}
		
		if(condition.containsKey("branchSchool")){//所属学校中心
			sb.append(" and studentInfo.branchSchool.resourceid = :branchSchool");
			values.put("branchSchool", condition.get("branchSchool").toString());
		}
		if(condition.containsKey("studyNo")){//学号
			sb.append(" and studentInfo.studyNo = :studyNo");
			values.put("studyNo", condition.get("studyNo").toString());
		}
		if(condition.containsKey("studentName")){//学生姓名
			sb.append(" and studentInfo.studentName like :studentName");
			values.put("studentName", "%"+condition.get("studentName").toString()+"%");
		}
		if(condition.containsKey("stuInfoId")){//学籍id
			sb.append(" and studentInfo.resourceid = :stuInfoId");
			values.put("stuInfoId",condition.get("stuInfoId").toString());
		}
		if(condition.containsKey("status")){//缴费状态
			if("0".equals(condition.get("status"))){
				sb.append(" and (facepayFee + derateFee) >=  recpayFee ");//正常
			} else {
				sb.append(" and (facepayFee + derateFee) <  recpayFee ");//欠费
			}
		}
		sb.append(" order by studentInfo.branchSchool.unitCode,chargeyear desc");
		return exGeneralHibernateDao.findByHql(objPage, sb.toString(), values);
	}




	@Override
	public void saveCollection(List<StuPerpayfee> stuFeeList) throws ServiceException{
		exGeneralHibernateDao.saveOrUpdateCollection(stuFeeList);
	}
	
	@Override
	@Transactional(readOnly=true)
	public  List findByHql(Map<String,Object> values) throws ServiceException{
		StringBuffer buffer=new StringBuffer();
		
		buffer.append("");
		buffer.append(" select stu.resourceid,stu.studyno,nvl(fee.totalfee,0) as totalfee,nvl( tail.payedFee,0)as payedFee,nvl(tail.payableFee,0)as payableFee from edu_roll_studentinfo stu ")
			  .append(" left join edu_roll_feerule fee on fee.branchschoolid=stu.branchschoolid and fee.majorid=stu.majorid and fee.classicid=stu.classicid ")
			  .append(" left join (")
			  .append(" select studentInfoId, sum(payedFee) as payedFee,sum(payableFee) as payableFee from edu_roll_stupaydetail group by studentInfoId ")
			  .append(" ) tail on tail.studentInfoId=stu.resourceid where 1=1 ");
		if(ExStringUtils.isNotEmpty(values.get("resourceid").toString().trim())){
			buffer.append(" and stu.resourceid='"+values.get("resourceid").toString().trim()).append("'");
		}		
	
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.SQLQuery query = session.createSQLQuery(buffer.toString()) ;		
		
		return query.list();
	}


	@Override
	public void synchronStuFee() throws ServiceException {	
		
		snychronYXTFactFee();
		
	}
	
	private void snychronYXTFactFee() throws ServiceException{
		Calendar cal = Calendar.getInstance();
		long starttime = System.currentTimeMillis();
		Map<String ,Object> param = new HashMap<String, Object>();
		param.put("chargeyear", new Long(ExDateUtils.getCurrentYear()).intValue());
				
		try {			
			//清空今年年度的缴费记录;
			exGeneralHibernateDao.executeHQL("delete from "+StudentFactFee.class.getSimpleName()+ " where chargeyear = :chargeyear", param);
			
			BaseJdbcTemplate jdbcTemplate = new BaseJdbcTemplate(dataSource);		
			@SuppressWarnings("unchecked")
			List<StudentFactFee> feeList = jdbcTemplate.findList("select studentid,recpayfee,factpayfee,chargeyear,deratefee from v_fistudentfee where chargeyear = :chargeyear", StudentFactFee.class, param);
			//查询出今年的缴费记录，并插入到数据库中
			
			List<Object[]> params = new ArrayList<Object[]>();
			
			if(null != feeList){
				GUIDUtils.init();
				String sql = "select resourceid from EDU_ROLL_STUDENTINFO where studyNo = :studyNo";
				for(StudentFactFee fee : feeList){
					List<Object> objList = new ArrayList<Object>();
					objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
					objList.add(fee.getStudentid());//studentid	
					param.clear();
					param.put("studyNo", fee.getStudentid());					
					List<String> studentInfoId = jdbcDao.getBaseJdbcTemplate().findList(sql, String.class, param);	
					if(null != studentInfoId && studentInfoId.size()>0){
						objList.add(studentInfoId.get(0));//studentinfoid
					}
					
					objList.add(fee.getRecpayFee());//recpayfee
					objList.add(fee.getFacepayFee());//factpayfee
					objList.add(fee.getChargeyear());//chargeyear
					objList.add(fee.getDerateFee());//deratefee
					objList.add(0L);
					objList.add(0);			
					
					params.add(objList.toArray());
				}
			}
			
			jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into EDU_ROLL_FISTUDENTFEE (RESOURCEID,STUDENTID,STUDENTINFOID,RECPAYFEE,FACTPAYFEE,CHARGEYEAR,DERATEFEE,VERSION,ISDELETED) " +
					" values (?,?,?,?,?,?,?,?,?)", params);
			long endtime = System.currentTimeMillis();
			cal.setTimeInMillis(endtime - starttime);
			logger.info("同步学费...ok("+cal.get(Calendar.MINUTE)+"分"+cal.get(Calendar.SECOND)+"秒"+cal.get(Calendar.MILLISECOND)+"毫秒)");
		} catch (Exception e) {
			throw new ServiceException("同步学费出错：{}"+e.fillInStackTrace());
		}
		
	}
	
	/*
	private void snychronYXTFee() throws ServiceException{
		Calendar cal = Calendar.getInstance();
		logger.info("同步学费begin...");
		try {
			BaseJdbcTemplate jdbcTemplate = new BaseJdbcTemplate(dataSource);
			//找出最近同步的記錄
			List list = findByHql("select nvl(max(gatheringDate),sysdate) from "+StuPerpayfee.class.getSimpleName()+" where isDeleted=0", new Object[0]); 
			if(!list.isEmpty()){
				Date time = (Timestamp) list.get(0);
				cal.setTime(time);					
				List<BankWaterView> list2 = new ArrayList<BankWaterView>();
				Map<String, Object> pamarMap = new HashMap<String, Object>();
				pamarMap.put("CollegeID", "033");
				pamarMap.put("StudentID", 14);
				if(ExDateUtils.formatDate(time, ExDateUtils.PATTREN_DATE).getTime()==ExDateUtils.formatDate(new Date(), ExDateUtils.PATTREN_DATE).getTime()){					
					list2 = jdbcTemplate.findList("select StudentID,BankSerialNo,BankName,RequestWaID,ChargeYear,TuitionFee,OccurAmt,TradeDate,TradeTime,RushFlag from V_BankWater " +
							"where CollegeID!= :CollegeID and datalength(StudentID)> :StudentID order by TradeDate", 
							BankWaterView.class,
							pamarMap);
				}else{
					pamarMap.put("TradeDate", ExDateUtils.formatDateStr(time, "yyyyMMdd"));
					list2 = jdbcTemplate.findList("select StudentID,BankSerialNo,BankName,RequestWaID,ChargeYear,TuitionFee,OccurAmt,TradeDate,TradeTime,RushFlag from V_BankWater " +
							"where CollegeID!= :CollegeID and datalength(StudentID)> :StudentID and TradeDate>= :TradeDate order by TradeDate", 
							BankWaterView.class,
							pamarMap);
				}
				logger.info("查询到"+list2.size()+"条数据未同步...");
				List<StuPerpayfee> stuFeeList = new ArrayList<StuPerpayfee>();
				//查詢學生繳費明細
				List<StuPerpayfee> stuPerpayfees = findByHql("from "+StuPerpayfee.class.getSimpleName()+" where isDeleted = ? and payedFee is null order by payFeeScale", 0);
				if(null != stuPerpayfees && stuPerpayfees.size()>0){
					for(StuPerpayfee stuPerpayfee : stuPerpayfees){//遍历需要同步学费的学生
						if(!list2.isEmpty()){
							for(BankWaterView bankWaterView : list2){//遍历银校通学生
								if(stuPerpayfee.getStudyNo().equals(bankWaterView.getStudentID())){
									stuPerpayfee.setPayedFee(Double.parseDouble(bankWaterView.getOccurAmt())); //已缴费用金额
									stuPerpayfee.setGatheringDate(ExDateUtils.parseDate(bankWaterView.getTradeTime(), "yyyyMMddHHmmss")); //缴费日期
									stuPerpayfee.setPayedNo(bankWaterView.getBankSerialNo()); //缴费单号
									stuFeeList.add(stuPerpayfee);
									list2.remove(bankWaterView);
									break;
								}
								
							}
						}
						
					}
				}
			}
		} catch (Exception e) {				
			throw new ServiceException("同步学费出错:"+e.fillInStackTrace());
		}
	}
	*/

	@Override
	public List<StudentFactFee> findStudentFactFeeByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+StudentFactFee.class.getSimpleName()+" where isDeleted =:isDeleted");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("chargeyear")){//缴费年度
			hql.append(" and chargeyear = :chargeyear");
			values.put("chargeyear", condition.get("chargeyear"));
		}		
		if(condition.containsKey("branchSchool")){//所属学校中心
			hql.append(" and studentInfo.branchSchool.resourceid = :branchSchool");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("studyNo")){//学号
			hql.append(" and studentInfo.studyNo = :studyNo");
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("studentName")){//学生姓名
			hql.append(" and studentInfo.studentName like :studentName");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("stuInfoId")){//学籍id
			hql.append(" and studentInfo.resourceid = :stuInfoId");
			values.put("stuInfoId",condition.get("stuInfoId"));
		}
		if(condition.containsKey("orderby")){//排序
			hql.append(" order by "+condition.get("orderby"));
		}		
		return (List<StudentFactFee>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}
}
