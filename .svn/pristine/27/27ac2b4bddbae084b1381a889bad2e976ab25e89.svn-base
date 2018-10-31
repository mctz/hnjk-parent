package com.hnjk.edu.teaching.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.basedata.service.IExamroomService;
import com.hnjk.edu.teaching.model.ExamDistribu;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamDistribuService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IMergeExamRoomService;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Service("examDistribuService")
@Transactional
public class ExamDistribuServiceImpl extends BaseServiceImpl<ExamDistribu> implements IExamDistribuService {
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("examroomService")
	private IExamroomService examroomService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("mrgeExamRoomService")
	private IMergeExamRoomService mrgeExamRoomService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	/**
	 * 分配座位
	 * @param examInfos      考试课程ID串(以逗号分隔)
	 * @param examRoomIds    考场课室ID串(以逗号分隔)
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试批次ID
	 * @param branchSchool   学习中心ID
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public  Map<String,Object> assignSeat(String examInfos, String examRoomIds, String examSubId, String branchSchool, boolean reset) throws ServiceException {
		
		Map<String,Object> resultMap           = new HashMap<String, Object>();
		Map<String,ExamResults> examResultsMap = new HashMap<String, ExamResults>();
		boolean isAssignSuccess                = false;
		ExamInfo info                          = null;  //要分配座位的考试课程之一,用于获取课程所在的时间段      
		String   msg 			               = isAllowAssignSeat(examSubId);
		String   msg2                          = checkExamRoomSize(examRoomIds);
		StringBuffer mgrgeExamBrschoolIds      = new StringBuffer();
		//检查是否在允许的时间段安排座位
		if (ExStringUtils.isNotEmpty(msg)) {
			resultMap = setResultMapMsg(resultMap, msg);
			resultMap.put("isAssignSuccess",isAssignSuccess);
			return resultMap;
		}
		//检查选择的试室是否设定了双隔位容量
		if (ExStringUtils.isNotEmpty(msg2)) {
			resultMap = setResultMapMsg(resultMap, msg2);
			resultMap.put("isAssignSuccess",isAssignSuccess);
			return resultMap;
		}
		//----------------------------------------------------获取学习中心的考场合并记录----------------------------------------------------
		User user        = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
		}
		List<String> mergeExamBrschool  = mrgeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examSubId);
		if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {

			for (String id :mergeExamBrschool) {
				mgrgeExamBrschoolIds.append(",'"+id+"'");
			}
			mgrgeExamBrschoolIds.append(",'"+branchSchool+"'");
		}
		//----------------------------------------------------获取学习中心的考场合并记录----------------------------------------------------
		
		//----------------------------------------------------获取要分配座位的成绩记录----------------------------------------------------
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 and results.checkStatus='-1' ");
		hql.append(" and results.examroom is null and results.examSeatNum is null   ");
		
		//考试科目
		if (examInfos.split(",").length>1) {
			StringBuffer ids  = new StringBuffer();
			for (String id :examInfos.split(",")) {
				if(null==info ){
					info 		  = examInfoService.get(id);
				}
				ids.append(",'"+id+"'");
			}
			//resultMap.put("examInfos",ids.substring(1));
			hql.append(" and results.examInfo.resourceid in("+ids.substring(1)+") ");  
		}else {
			info 		  = examInfoService.get(examInfos);
			resultMap.put("examInfo",examInfos);
			hql.append(" and results.examInfo.resourceid =:examInfo ");  
		}
		//合并考场记录
		if (ExStringUtils.isNotEmpty(mgrgeExamBrschoolIds.toString())) {
			hql.append(" and results.studentInfo.branchSchool.resourceid in("+mgrgeExamBrschoolIds.substring(1)+")");
		}else {
			resultMap.put("branchSchool", branchSchool);
			hql.append(" and results.studentInfo.branchSchool.resourceid =:branchSchool");
		}
		hql.append(" order by cast( results.studentInfo.studyNo as long ) desc");
		//要进行分配座位的学生考试成绩记录列表
		List<ExamResults> examResultsList      = new ArrayList<ExamResults>();
		if (resultMap.isEmpty()) {
			examResultsList                    = examResultsService.findByHql(hql.toString());
		}else {
			examResultsList                    = examResultsService.findByHql(hql.toString(), resultMap);
		}
		
		//----------------------------------------------------获取要分配座位的成绩记录----------------------------------------------------
		Map<String,List<ExamResults> > infoRlsMap = new HashMap<String, List<ExamResults>>();
		for (ExamResults  results:examResultsList){
			 examResultsMap.put(results.getResourceid(),results);
		}

		
		for (String id:examInfos.split(",")) {
			List<ExamResults> rlsList  = new ArrayList<ExamResults>();
			for (ExamResults  results:examResultsList) {
				if (results.getExamInfo().getResourceid().equals(id)) {
					rlsList.add(results);
				}
			}
			infoRlsMap.put(id, rlsList);
		}
		
		//要进行分配座位的考场课室列表
		List<Examroom>       examRoomList	   = examroomService.findExamRoomListByIds(examRoomIds);
		//开始分配座位
		synchronized (ExamDistribuServiceImpl.class) {
			int index = 0;
			List<String> noFreeSeatRoomlist    = new ArrayList<String>();//没有空闲座位的考场
			
			for (String id:examInfos.split(",")) { //科目
				List<ExamResults> rlsList  	   = infoRlsMap.get(id);
				int seatIdext                  = rlsList.size()-1;
				for(Examroom room:examRoomList){ //试室
						//获取空闲座位列表
						String[] freeSeat     		   = getRoomFreeSeats(examSubId,room,info,reset);
						boolean  haveFreeSeat 		   = false;
						for (int i = 0; i < freeSeat.length; i++) {
							if (freeSeat[i] != null) {
								haveFreeSeat = true;
								break;
							}
						}
						if (!haveFreeSeat) {
							noFreeSeatRoomlist.add(room.getExamroomName());
						}
						for (int j = 0; j < freeSeat.length; j++) {
							
							String seatNum = freeSeat[j];//待分配的座位号
							if (seatNum == null) {
								continue;
							}
							if(index == examResultsList.size()){//全部分配完立即跳出
								msg  = examResultsList.size() + "个考试预约座位安排成功!!";
								isAssignSuccess = true;
								resultMap       = setResultMapMsg(resultMap, msg);
								resultMap.put("isAssignSuccess", isAssignSuccess);
								return resultMap;
							}
							
							ExamResults assignResults=rlsList.get(seatIdext);
							assignResults.setExamroom(room);
							assignResults.setExamSeatNum(seatNum);
							
							ExamDistribu distribu = new ExamDistribu();
										 distribu.setExamResults(assignResults);
										 distribu.setExamSeatNum(seatNum);
										 
							examResultsService.saveOrUpdate(assignResults);
							this.exGeneralHibernateDao.save(distribu);
	
							examResultsMap.remove(assignResults.getResourceid());
							
							index++;
							if (seatIdext>0) {//当前科目如果还有未安排的座位，则递减 
								seatIdext -- ;
							}else if (seatIdext==0) {//当前科目没有未安排的座位，则跳出 
								break;
							}
						}
						if (seatIdext==0) {
							break;
						}
				}
				
			}	
			if (noFreeSeatRoomlist.size()>0) {
				msg ="试室：";
				for (String noFreeSeatRoomName:noFreeSeatRoomlist) {
					msg += noFreeSeatRoomName +",";
				}
				msg += "没有空闲的座位!!";
				isAssignSuccess = false;
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}
			if (examResultsMap.size()>0) {
				msg = examResultsMap.size()+"个学生分配座位失败!";
				isAssignSuccess = true;
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}else {
				isAssignSuccess = true;
				msg = "座位分配成功!!";
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}
			
		}
		return resultMap;
	}
	/**
	 * 检查是否允许分配座位
	 * @param examSubId
	 * @return
	 */
	public String isAllowAssignSeat(String examSubId)throws ServiceException{
		ExamSub examSub = examSubService.get(examSubId); 
		String  msg     = "";
		if (null!=examSub){
			Date curDate = new Date();
			//分配座位应在预约批次的 预约信息添加截止时间之后 和 成绩录入开始时间之前
			if (!(curDate.getTime()>=examSub.getExamsubEndTime().getTime()) && 
				!(curDate.getTime()<=examSub.getExaminputStartTime().getTime()) ) {
				
				String examsubEndTime     =  DateUtils.getFormatDate(examSub.getExamsubEndTime(), "yyyy-MM-dd");
				String examinputStartTime =  DateUtils.getFormatDate(examSub.getExaminputStartTime(), "yyyy-MM-dd");
									  msg =  "请在此时间范围内分配座位:";
									  msg += examsubEndTime+" 至 "+examinputStartTime;
				return msg;
			}
		}else {
			msg="请选择一个考试批次！";
			return  msg;
		}
		return msg;
	}
	/**
	 * 检查要分配座位的试室是否设置双隔位容量
	 * @param examRoomIds
	 * @return
	 */
	public String checkExamRoomSize(String examRoomIds)throws ServiceException{
		if (ExStringUtils.isNotEmpty(examRoomIds)) {
			for (String id:examRoomIds.split(",")) {
				Examroom room  = examroomService.get(id);
				if (null==room.getDoubleSeatNum()) {
					return "您所选择的试室未设定双隔位容量!";
				}
			}
		}else {
			return "请选择要安排座位的试室!";
		}
		
		return "";
	}
	/**
	 * 获取考场的空闲座位列表
	 * @param examSubId
	 * @param examRoomId
	 * @param courseId
	 * @return
	 */
	public String[] getRoomFreeSeats(String examSubId,Examroom room,ExamInfo info,boolean reset){
		
		int totalSeatNum  = room.getDoubleSeatNum().intValue();
		String[] freeSeat = new String[totalSeatNum];
		
		for(int i=0;i<totalSeatNum;i++){
			freeSeat[i]   = String.valueOf(i+1);
		}
		List<ExamDistribu> list = getRoomAssignedList(examSubId,room.getResourceid(),info);
		for(ExamDistribu distribu : list){
			int usedSeatNum         = Integer.parseInt(distribu.getExamSeatNum());
			freeSeat[usedSeatNum-1] = null;
		}
		
		if (reset) {//当同一个考场按排多门课程座位号需要从1开始
			freeSeat = new String[totalSeatNum-list.size()];
			for(int i=0;i<freeSeat.length;i++){
				freeSeat[i]   = String.valueOf(i+1);
			}
		}
		
		return freeSeat;
	}
	
	/**
	 * 获得已分配座位的记录
	 * @param examSubId
	 * @param examRoomId
	 * @param courseId
	 * @return
	 */
	public List<ExamDistribu> getRoomAssignedList(String examSubId,String examRoomId,ExamInfo info){
		
		StringBuffer hql 	   = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("examsubId", examSubId);
		map.put("examRoomId", examRoomId);
		map.put("examStartTime", info.getExamStartTime());
		map.put("examEndTime", info.getExamEndTime());
		hql.append("from ExamDistribu distribu where distribu.isDeleted = 0");
		hql.append(" and distribu.examResults.examsubId =:examsubId");
		hql.append(" and distribu.examResults.examroom.resourceid=:examRoomId");
		hql.append(" and distribu.examResults.examInfo.examStartTime=:examStartTime");
		hql.append(" and distribu.examResults.examInfo.examEndTime=:examEndTime");
		
		return (List<ExamDistribu>) this.exGeneralHibernateDao.findByHql(hql.toString(),map);
	}
	//设置返回的resultMap的消息数组
	public Map<String,Object> setResultMapMsg(Map <String,Object> resultMap,String msg){
	
	   if (null==resultMap) {
		   resultMap = new HashMap<String, Object>();
	   }
	   
	    List<String> msgList =  (List<String>) resultMap.get("msg"); 

        if (null==msgList) {
        	msgList = new ArrayList<String>() ;
        	msgList.add(msg);
			resultMap.put("msg",msgList);
		}else {
			msgList.add(msg);
			resultMap.put("msg",msgList);
		}
        
		return resultMap;
	}
	//清空考场
	@Override
	public boolean clearnSeat(String examResultIds) {
		
		if (ExStringUtils.isNotEmpty(examResultIds)) {
			
			
			String sql1 = "delete from EDU_TEACH_EXAMDISTRIBU dis where dis.EXAMRESULTSID in("+examResultIds+")";
			String sql2 = "update EDU_TEACH_EXAMRESULTS result set result.CLASSROOMID=null , result.EXAMSEATNUM=null where result.RESOURCEID in("+examResultIds+")";
			
			try {
				
				int result1=baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql1,null);
				int result2=baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql2,null);
				
				return true;
			} catch (Exception e) {
				logger.error("清空考场service方法出错：{}",e.fillInStackTrace());
				return false;
			}
		}
		return false;
	}
	/**
	 * 生成JasperPrint对象
	 */
	@Override
	public JasperPrint doJasperPrint(File jasperPrintModelFlie, Map<String, Object> param, Connection conn) throws Exception {
		JasperPrint jasperPrint = null;
		try {
			Map<String,Object> map  = new HashMap<String, Object>();
			StringBuffer hql        = new StringBuffer();
			StringBuffer sql        = new StringBuffer();
			StringBuffer courseIds  = new StringBuffer();
			String examSubID        = param.get("examSubID").toString();
			String examRoomID       = param.get("examRoomID").toString();
			String branchSchool     = param.get("branchSchool").toString();
			map.put("isDeleted",0);
			map.put("examSubID", examSubID);
			map.put("examRoomID", examRoomID);
		
			OrgUnit unit            = null;
			
			hql.append(" select distinct(result.examInfo) from ExamResults result ");
	        hql.append("  where result.isDeleted=0") ;
	        hql.append("    and result.examroom.resourceid=?");
	        hql.append("    and result.examsubId=?");
	        //获取试室下的所有考试课程
	        List courseList         = examResultsService.findByHql(hql.toString(), new String[]{examRoomID,examSubID} );
			for (int i = 0; i < courseList.size(); i++) {
				ExamInfo info        = (ExamInfo) courseList.get(i);
				courseIds.append(",'"+info.getResourceid()+"'");
			}
			if (courseIds.length()>0) {
				map.put("examinfoid", courseIds.substring(1));
			}
			
			sql.append(" select rs.examinfoid,count(0) counts from EDU_TEACH_EXAMRESULTS rs where rs.isdeleted =:isDeleted  ");
			if(map.containsKey("examSubID")){
				sql.append(" and rs.examsubid=:examSubID ");
			}
			if(map.containsKey("examinfoid")){
				sql.append(" and rs.examinfoid in("+map.get("examinfoid")+") ");
			}
			if(map.containsKey("examRoomID")){
				sql.append(" and rs.classroomid =:examRoomID ");
			}
			sql.append(" group by rs.examinfoid ");
			//获取试室下的所有考试课程的考试人数
			List<Map<String,Object>> courseCount=baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), map);
			map.clear();
			for (Map<String,Object> m : courseCount) {
				map.put(m.get("examinfoid").toString(), ((BigDecimal)m.get("counts")).longValue());
			}			 
			if(ExStringUtils.isNotEmpty(branchSchool)){
				unit                = orgUnitService.load(branchSchool);
			}
			
			for (int i = 0; i < courseList.size(); i++) {
				
				ExamInfo info        = (ExamInfo) courseList.get(i);
				StringBuffer c_n     = new StringBuffer();
				param.put("examInfoID", info.getResourceid());

				//Integer  count       = examResultsService.findExamResultCount(param);
				Long  count    		 = null==map.get(info.getResourceid())?0:(Long)map.get(info.getResourceid());
				String examDate      = ExDateUtils.formatDateStr(info.getExamStartTime(),"yyyy-MM-dd");
				String examStartTime = ExDateUtils.formatDateStr(info.getExamStartTime(), "HH:mm"); 
				String examEndTime   = ExDateUtils.formatDateStr(info.getExamEndTime(), "HH:mm");
				String examCourseCode= ExStringUtils.isEmpty(info.getExamCourseCode())?"":info.getExamCourseCode()+"--";
				c_n.append("《"+examCourseCode+info.getCourse().getCourseName()+"》 ");
				if (ExStringUtils.isNotBlank(info.getIsMachineExam())&&Constants.BOOLEAN_YES.equals(info.getIsMachineExam())) {
					c_n.append(" /机考");
				}else{
					c_n.append(" /笔试");
				}
				if (Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(info.getIsMachineExam()))) {
					c_n.append(" /闭卷");
				}
				if (null!=info.getCourse().getExamType()&&!Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(info.getIsMachineExam()))) {
					c_n.append(" /"+JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType", info.getCourse().getExamType().toString()));
				}
				if (null!=unit) {
					c_n.append(" /"+unit.getUnitCode()+"--"+unit.getUnitShortName());
				}
				param.put("examCount", count.toString());
				param.put("courseName",c_n.toString());
				param.put("examDate", examDate);
				param.put("examTime", examStartTime+"-"+examEndTime);
				if (count.intValue()==0) {
					continue;
				}
				if(conn.isClosed()){
					conn            = this.getConn();
				}
				
				if (i==0) {
					       //jasperPrint = JasperFillManager.fillReport(jasperPrintModelFlie.getPath(), param, conn); // 填充报表
					jasperPrint = printReport(jasperPrintModelFlie.getPath(), param, conn,false);
				}else {
					//JasperPrint jasperPage = JasperFillManager.fillReport(jasperPrintModelFlie.getPath(), param, conn); // 填充报表
					JasperPrint jasperPage = printReport(jasperPrintModelFlie.getPath(), param, conn,false);
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;
				}
			}
		} catch (Exception e) {
			logger.error("打印试室座位表生成报表出错：{}"+e.fillInStackTrace());
			e.printStackTrace();
		}finally{
			if(null!=conn){
				conn.close();
			}
		}
		
		
		return jasperPrint;
	}
	
	/**
	 * 按课程分配座位
	 * @param examInfos      考试课程ID串(以逗号分隔)
	 * @param examRoomIds    考场课室ID串(以逗号分隔)
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试批次ID
	 * @param branchSchool   学习中心ID
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public  Map<String,Object> assignSeatByCourse(String results, String examRoomIds, String examSubId, String branchSchool, boolean reset) throws ServiceException {
		
		Map<String,Object> resultMap           = new HashMap<String, Object>();
		Map<String,ExamResults> examResultsMap = new HashMap<String, ExamResults>();
		boolean isAssignSuccess                = false;
		ExamInfo info                          = null;  //要分配座位的考试课程之一,用于获取课程所在的时间段      
		String   msg 			               = isAllowAssignSeat(examSubId);
		String   msg2                          = checkExamRoomSize(examRoomIds);
		StringBuffer mgrgeExamBrschoolIds      = new StringBuffer();
		//检查是否在允许的时间段安排座位
		if (ExStringUtils.isNotEmpty(msg)) {
			resultMap = setResultMapMsg(resultMap, msg);
			resultMap.put("isAssignSuccess",isAssignSuccess);
			return resultMap;
		}
		//检查选择的试室是否设定了双隔位容量
		if (ExStringUtils.isNotEmpty(msg2)) {
			resultMap = setResultMapMsg(resultMap, msg2);
			resultMap.put("isAssignSuccess",isAssignSuccess);
			return resultMap;
		}
		//----------------------------------------------------获取学习中心的考场合并记录----------------------------------------------------
		User user        = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
		}
		List<String> mergeExamBrschool  = mrgeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examSubId);
		if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {

			for (String id :mergeExamBrschool) {
				mgrgeExamBrschoolIds.append(",'"+id+"'");
			}
			mgrgeExamBrschoolIds.append(",'"+branchSchool+"'");
		}
		//----------------------------------------------------获取学习中心的考场合并记录----------------------------------------------------
		
		//----------------------------------------------------获取要分配座位的成绩记录----------------------------------------------------
//		StringBuffer hql = new StringBuffer();
//		hql.append(" from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 and results.checkStatus='-1' ");
//		hql.append(" and results.examroom is null and results.examSeatNum is null   ");
//		
//		//考试科目
//		if (examInfos.split(",").length>1) {
//			StringBuffer ids  = new StringBuffer();
//			for (String id :examInfos.split(",")) {
//				if(null==info ){
//					info 		  = examInfoService.get(id);
//				}
//				ids.append(",'"+id+"'");
//			}
//			//resultMap.put("examInfos",ids.substring(1));
//			hql.append(" and results.examInfo.resourceid in("+ids.substring(1)+") ");  
//		}else {
//			info 		  = examInfoService.get(examInfos);
//			resultMap.put("examInfo",examInfos);
//			hql.append(" and results.examInfo.resourceid =:examInfo ");  
//		}
//		//合并考场记录
//		if (ExStringUtils.isNotEmpty(mgrgeExamBrschoolIds.toString())) {
//			hql.append(" and results.studentInfo.branchSchool.resourceid in("+mgrgeExamBrschoolIds.substring(1)+")");
//		}else {
//			resultMap.put("branchSchool", branchSchool);
//			hql.append(" and results.studentInfo.branchSchool.resourceid =:branchSchool");
//		}
//		hql.append(" order by cast( results.studentInfo.studyNo as long ) desc");
		//要进行分配座位的学生考试成绩记录列表
		List<ExamResults> examResultsList      = new ArrayList<ExamResults>();
		String[] resultArr = results.split(",");
		for(int j = resultArr.length;j>0;j--){
			String resultId = resultArr[j-1];
			examResultsList.add(examResultsService.get(resultId));
		}
		
		
		//----------------------------------------------------获取要分配座位的成绩记录----------------------------------------------------
		
		for (ExamResults  result:examResultsList){
			 examResultsMap.put(result.getResourceid(),result);
		}

		
		
		
		//要进行分配座位的考场课室列表
		List<Examroom>       examRoomList	   = examroomService.findExamRoomListByIds(examRoomIds);
		//开始分配座位
		synchronized (ExamDistribuServiceImpl.class) {
			int index = 0;
			List<String> noFreeSeatRoomlist    = new ArrayList<String>();//没有空闲座位的考场
			
//			for (String id:examInfos.split(",")) { //科目
//				List<ExamResults> rlsList  	   = infoRlsMap.get(id);
				int seatIdext                  = examResultsList.size()-1;
				for(Examroom room:examRoomList){ //试室
						
					for(int a= 0;a<examResultsList.size();a++){
						ExamResults result = examResultsList.get(seatIdext);
						//获取空闲座位列表
						String[] freeSeat     		   = getRoomFreeSeats(examSubId,room,result.getExamInfo(),reset);
						boolean  haveFreeSeat 		   = false;
						//未安排座位的座位号
						int seat = 0;
						for (int i = 0; i <freeSeat.length ; i++) {
							if (freeSeat[i] != null) {
								haveFreeSeat = true;
								seat = i;
								break;
							}
						}
						if(a==freeSeat.length){
							break;
						}
						if (!haveFreeSeat) {
							noFreeSeatRoomlist.add(room.getExamroomName());
						}
						String seatNum = freeSeat[seat];//待分配的座位号
						if (seatNum == null) {
							continue;
						}
						if(index == examResultsList.size()){//全部分配完立即跳出
							msg  = examResultsList.size() + "个考试预约座位安排成功!!";
							isAssignSuccess = true;
							resultMap       = setResultMapMsg(resultMap, msg);
							resultMap.put("isAssignSuccess", isAssignSuccess);
							return resultMap;
						}
						
						ExamResults assignResults=examResultsList.get(seatIdext);
						assignResults.setExamroom(room);
						assignResults.setExamSeatNum(seatNum);
						
						ExamDistribu distribu = new ExamDistribu();
									 distribu.setExamResults(assignResults);
									 distribu.setExamSeatNum(seatNum);
									 
						examResultsService.saveOrUpdate(assignResults);
						this.exGeneralHibernateDao.save(distribu);

						examResultsMap.remove(assignResults.getResourceid());
						
						index++;
						if (seatIdext>0) {//当前科目如果还有未安排的座位，则递减 
							seatIdext -- ;
						}else if (seatIdext==0) {//当前科目没有未安排的座位，则跳出 
							break;
						}
						
					}
					
//					//获取空闲座位列表
//						String[] freeSeat     		   = getRoomFreeSeats(examSubId,room,info,reset);
//						boolean  haveFreeSeat 		   = false;
//						for (int i = 0; i < freeSeat.length; i++) {
//							if (freeSeat[i] != null) {
//								haveFreeSeat = true;
//								break;
//							}
//						}
//						if (!haveFreeSeat) {
//							noFreeSeatRoomlist.add(room.getExamroomName());
//						}
//						for (int j = 0; j < freeSeat.length; j++) {
//							
//							String seatNum = freeSeat[j];//待分配的座位号
//							if (seatNum == null) {
//								continue;
//							}
//							if(index == examResultsList.size()){//全部分配完立即跳出
//								msg  = examResultsList.size() + "个考试预约座位安排成功!!";
//								isAssignSuccess = true;
//								resultMap       = setResultMapMsg(resultMap, msg);
//								resultMap.put("isAssignSuccess", isAssignSuccess);
//								return resultMap;
//							}
//							
//							ExamResults assignResults=examResultsList.get(seatIdext);
//							assignResults.setExamroom(room);
//							assignResults.setExamSeatNum(seatNum);
//							
//							ExamDistribu distribu = new ExamDistribu();
//										 distribu.setExamResults(assignResults);
//										 distribu.setExamSeatNum(seatNum);
//										 
//							examResultsService.saveOrUpdate(assignResults);
//							this.exGeneralHibernateDao.save(distribu);
//	
//							examResultsMap.remove(assignResults.getResourceid());
//							
//							index++;
//							if (seatIdext>0) {//当前科目如果还有未安排的座位，则递减 
//								seatIdext -- ;
//							}else if (seatIdext==0) {//当前科目没有未安排的座位，则跳出 
//								break;
//							}
//						}
						if (seatIdext==0) {
							break;
						}
				}
				
			
			if (noFreeSeatRoomlist.size()>0) {
				msg ="试室：";
				for (String noFreeSeatRoomName:noFreeSeatRoomlist) {
					msg += noFreeSeatRoomName +",";
				}
				msg += "没有空闲的座位!!";
				isAssignSuccess = false;
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}
			if (examResultsMap.size()>0) {
				msg = examResultsMap.size()+"个学生分配座位失败!";
				isAssignSuccess = true;
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}else {
				isAssignSuccess = true;
				msg = "座位分配成功!!";
				resultMap = setResultMapMsg(resultMap, msg);
				resultMap.put("isAssignSuccess", isAssignSuccess);
			}
			
		}
		return resultMap;
	}
	
}
