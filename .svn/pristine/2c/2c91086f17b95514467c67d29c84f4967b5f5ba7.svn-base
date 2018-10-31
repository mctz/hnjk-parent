package com.hnjk.edu.teaching.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.basedata.service.IExamroomService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamPaperBag;
import com.hnjk.edu.teaching.model.ExamPaperBagDetails;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamPaperBagService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 
 * <code>试卷袋标签ExamPaperBagServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-27 下午03:09:59
 * @see 
 * @version 1.0
 */
@Service("examPaperBagService")
public class ExamPaperBagServiceImpl extends BaseServiceImpl<ExamPaperBag> implements IExamPaperBagService{

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("examroomService")
	private IExamroomService examroomService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	/**
	 * 根据条件查询试卷袋标签,返回分页对象
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findExamPaperBagByCondition(Page page,Map<String, Object> condition) throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+ExamPaperBag.class.getSimpleName()+" bag where bag.isDeleted=0 ");
		
		if (condition.containsKey("examSub")) {
			hql.append(" and bag.examInfo.examSub.resourceid=:examSub");
			param.put("examSub", condition.get("examSub"));
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and bag.examInfo.course.resourceid=:courseId");
			param.put("courseId", condition.get("courseId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and bag.unit.resourceid=:branchSchool");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		if (condition.containsKey("isMachineExam") ) {
			hql.append(" and bag.examInfo.isMachineExam=:isMachineExam");
			param.put("isMachineExam", condition.get("isMachineExam"));
		}
		if (ExStringUtils.isNotEmpty(page.getOrderBy())) {
			hql.append(" order by "+page.getOrderBy());
		}else {
			hql.append(" order by bag.examInfo.examCourseCode ");
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), param);
	}
	
	/**
	 * 根据条件统计试卷袋标签总袋数
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Object> findExamPaperBagCountByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select sum(bag.bagNum)  from "+ExamPaperBag.class.getSimpleName()+" bag where bag.isDeleted=0 ");
		
		if (condition.containsKey("examSub")) {
			hql.append(" and bag.examInfo.examSub.resourceid=:examSub");
			param.put("examSub", condition.get("examSub"));
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and bag.examInfo.course.resourceid=:courseId");
			param.put("courseId", condition.get("courseId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and bag.unit.resourceid=:branchSchool");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		if (condition.containsKey("isMachineExam") ) {
			hql.append(" and bag.examInfo.isMachineExam=:isMachineExam");
			param.put("isMachineExam", condition.get("isMachineExam"));
		}
		return (List<Object>) exGeneralHibernateDao.findByHql(hql.toString(), param);
	}
	/**
	 * 生成试卷袋标签数据
	 * @param condition
	 * @throws ServiceException
	 */
	@Override
	public void genExamPagerBag(Map<String, Object> condition)throws ServiceException {
		
		//ExamSub examSub                 		  	= examSubService.get(condition.get("examSubId").toString());
		Double  backupCoefficient         			= Double.parseDouble(condition.get("backupCoefficient").toString());
		
		Map<String,Integer> courseBagNumMap         = new HashMap<String, Integer>(); //课程总包数            
		Map<String,Integer> courseBagIndexMap       = new HashMap<String, Integer>(); //课程总包数的当前顺序MAP
		
		Map<String,Integer> unitCourseBagNumMap     = new HashMap<String, Integer>(); //学习中心 课程总包数  
		Map<String,Integer> unitCourseBagIndesMap   = new HashMap<String, Integer>(); //学习中心 课程总包数  的当前顺序MAP 
		Map<String,Integer> unitCouresOrderNumMap   = new HashMap<String, Integer>(); //学习中心 课程预约数  
		Map<String,Integer> unitCouresPaperNumMap   = new HashMap<String, Integer>(); //学习中心 课程试卷份数
		
		condition.put("statDirection","forProgram"); 					
		delExistsExamPaperBagDetails(condition.get("examSubId").toString());									    //清除当前批次试卷袋标签的子表记录
		delExistsExamPaperBag(condition.get("examSubId").toString());                                               //清除当前批次试卷袋标签的主记录
		Map<String,ExamPaperBag> existsPaperBag     = new HashMap<String, ExamPaperBag>();                          //已生成的学习中心试卷袋标签的记录

		List<Map<String, Object>> courseUnitStatList= teachingJDBCService.findCourseExaminationNum(condition);      //同一考试课程各学习中心的考试人数
		if(null==courseUnitStatList || courseUnitStatList.isEmpty()){
			throw new ServiceException("还没有预约考试记录！");
		}
		//------------------------------------------------1.生成课程试卷的总包数-----------------------------------------------
		BigDecimal divisor   						= new BigDecimal("40");
		/**
		 * 试卷袋标签要根据最终安排试室来定，以试室为单位，如果某试室安排人数大于40，则需要以安排人数除40并将余数单独成包
		 * 
		 * 如果未安排试室，试卷袋标签则按预约人数*试卷备份系数除40，余数单独成包
		 */
		for (Map<String, Object> map:courseUnitStatList) {
			
			String examInfoId        = map.get("EXAMINFOID").toString();   
			String unitId            = map.get("UNITID").toString();
			Integer orderNum         = Integer.parseInt(map.get("TOTALNUM").toString());     
			Double paperCount        = BigDecimalUtil.round(BigDecimalUtil.mul(orderNum, backupCoefficient),0);           				//学习中心课程的总试卷份数(加入备份数)
			int coursePaperBagNum    = 0;  //课程总包数
			int unitCoursPaperBagNum = 0;  //学习中心课程总包数  
			
			int courseBagNum         = null==courseBagNumMap.get(examInfoId)?0:courseBagNumMap.get(examInfoId);           			     //课程总包数
			int unitCourseBagNum     = null==unitCourseBagNumMap.get(examInfoId+unitId)?0:unitCourseBagNumMap.get(examInfoId+unitId);    //学习中心 课程总包数
			int unitCourseOrderNum   = null==unitCouresOrderNumMap.get(examInfoId+unitId)?0:unitCouresOrderNumMap.get(examInfoId+unitId);//学习中心 课程预约数
			int unitCoursePaperNum   = null==unitCouresPaperNumMap.get(examInfoId+unitId)?0:unitCouresPaperNumMap.get(examInfoId+unitId);//学习中心 课程试卷份数
			
			//试卷数小于等于40，则课程的包数为1
			if (paperCount<=40) {
				coursePaperBagNum    = courseBagNum+1;
				unitCoursPaperBagNum = unitCourseBagNum+1;
			//试卷数大于40，则课程的包数为:试卷总数除40
			}else {
				BigDecimal p_c 		 = new BigDecimal(paperCount.toString());
				int bagNum     		 = (p_c.divide(divisor,0,BigDecimal.ROUND_UP)).intValue();             //当前试室的试卷袋数  (四舍五入模式 余数大于0 就四舍五入) 
				
				coursePaperBagNum    = courseBagNum+bagNum;                                                //课程总包数                     = 各学习中心该课程的包数之和
				unitCoursPaperBagNum = unitCourseBagNum+bagNum;                                            //学习中心课程总包数  = 学习中心该课程的包数之和 (不同试室) 
			}
			
			
			courseBagNumMap.put(examInfoId, coursePaperBagNum);
			unitCourseBagNumMap.put(examInfoId+unitId, unitCoursPaperBagNum);
			unitCouresOrderNumMap.put(examInfoId+unitId, unitCourseOrderNum+orderNum);
			unitCouresPaperNumMap.put(examInfoId+unitId,unitCoursePaperNum +paperCount.intValue());
		}
		//------------------------------------------------1.生成课程试卷的总包数------------------------------------------------
		
		//------------------------------------------------2.生成学习中心课程试卷的包数------------------------------------------------
		for (Map<String, Object> map:courseUnitStatList) {
			
			String examInfoId     	   = map.get("EXAMINFOID").toString();   									      //考试科目
			String unitId         	   = map.get("UNITID").toString();        					 				      //学习中心ID
			String examRoomId    	   = null==map.get("EXAMROOMID")?"":map.get("EXAMROOMID").toString();             //试室ID
			
			ExamInfo info         	   = examInfoService.load(examInfoId);
			OrgUnit unit         	   = orgUnitService.load(unitId);	
			Examroom room         	   = null;
			if (ExStringUtils.isNotEmpty(examRoomId)) {
				room              	   = examroomService.get(examRoomId);
			}
			
			Integer totalBagIndex 	   = courseBagIndexMap.get(examInfoId);                                           //课程试卷的总包数序号
			totalBagIndex         	   = null==totalBagIndex?1:totalBagIndex;                                         //序号从1开始
			totalBagIndex         	   = 0==totalBagIndex?1:totalBagIndex;                                            //序号从1开始
			
			
			Integer unitCourseBagIndex = unitCourseBagIndesMap.get(examInfoId+unitId);                                //学习中心该课程试卷的总包数序号
			unitCourseBagIndex         = null==unitCourseBagIndex?1:unitCourseBagIndex;                               //序号从1开始
			unitCourseBagIndex         = 0==unitCourseBagIndex?1:unitCourseBagIndex;                                  //序号从1开始
			
			
			Integer courseBagNum  	   = courseBagNumMap.get(examInfoId);                         				       //课程试卷的总包数
			Integer unitCourseBagNum   = unitCourseBagNumMap.get(examInfoId+unitId);                                   //学习中心课程试卷的总包数
			Integer unitCourseOrderNum = unitCouresOrderNumMap.get(examInfoId+unitId);                                 //学习中心课程的预约人数
			Integer unitCoursePaperNum = unitCouresPaperNumMap.get(examInfoId+unitId);							       //学习中心课程试卷的份数
			
			Integer roomExamNum        = Integer.parseInt(map.get("TOTALNUM").toString());     				           //当前试室考试人数
			Double roomExamPaperCount  = BigDecimalUtil.round(BigDecimalUtil.mul(roomExamNum, backupCoefficient),0);   //当前试室总试卷份数(加入备份数)
			
			
			ExamPaperBag bag     	   = null;
			int    roomBagNum	       = 0;
			//试卷数小于等于40，则课程的包数为1
			if (roomExamPaperCount<=40) {
				roomBagNum   		   = 1;
			//试卷数大于40，则课程的包数为:试卷总数除40
			}else {
				BigDecimal u_c    	   = new BigDecimal(roomExamPaperCount.toString());
				roomBagNum   		   =  u_c.divide(divisor, 0,BigDecimal.ROUND_UP).intValue();    //四舍五入模式 余数大于0 就四舍五入 
			}
			
			//如果当前批次已存在，则修改原来的记录
			if (existsPaperBag.containsKey(unitId+examInfoId)) {
				bag               	   = existsPaperBag.get(unitId+examInfoId);
			//如果当前批次不存在，则新增一条记录	
			}else {
				bag               	   = new ExamPaperBag();
				bag.setBagNum(unitCourseBagNum);
				bag.setExamInfo(info);
				bag.setUnit(unit);
				bag.setOrderNum(unitCourseOrderNum);
				bag.setPaperNum(unitCoursePaperNum);
			}
			//当前试室的试卷包数小于2
			if (roomBagNum<2) {
				
				ExamPaperBagDetails details = new ExamPaperBagDetails();
				details.setBagIndex(unitCourseBagIndex);
				details.setExamPaperBag(bag);
				details.setExamRoom(room);
				details.setExamNum(roomExamNum);
				details.setPaperNum(roomExamPaperCount.intValue());
				details.setTotalBagIndex(totalBagIndex);
				details.setTotalBagNum(courseBagNum);
				
				bag.getExamPaperBags().add(details);
				totalBagIndex +=1;
				courseBagIndexMap.put(examInfoId, totalBagIndex);
				
			//当前试室的试卷包数大于等于2	每40份试卷一袋,应考人数为40; 试卷份数除40的余数单独成包,应考人数为当前试室总预约人数减去已分配试卷袋人数
			}else {
				
					while (roomExamPaperCount>40) {
						
						ExamPaperBagDetails details = new ExamPaperBagDetails();

						roomExamPaperCount 		    = roomExamPaperCount - 40;
						if(roomExamNum>=40){
							roomExamNum             = roomExamNum - 40;
							details.setExamNum(40);
						}else {
							details.setExamNum(roomExamNum.intValue());
							roomExamNum             = roomExamNum - roomExamNum;
						}
						details.setPaperNum(40);
						
						details.setBagIndex(unitCourseBagIndex);
						details.setExamPaperBag(bag);
						details.setExamRoom(room);

						details.setTotalBagIndex(totalBagIndex);
						details.setTotalBagNum(courseBagNum);
						
						bag.getExamPaperBags().add(details);
						
						totalBagIndex +=1;
						unitCourseBagIndex +=1;
					}
					
					if (roomExamPaperCount>0) {
						
						ExamPaperBagDetails details = new ExamPaperBagDetails();
						details.setBagIndex(unitCourseBagIndex);
						details.setExamPaperBag(bag);
						details.setExamRoom(room);
						
						details.setExamNum(roomExamNum>0?roomExamNum:0);
						details.setPaperNum(roomExamPaperCount.intValue());

						details.setTotalBagIndex(totalBagIndex);
						details.setTotalBagNum(courseBagNum);
						
						bag.getExamPaperBags().add(details);
						
						totalBagIndex +=1;
						unitCourseBagIndex +=1;
					}
			}
			
			courseBagIndexMap.put(examInfoId, totalBagIndex);
			unitCourseBagIndesMap.put(examInfoId+unitId, unitCourseBagIndex);
			
			this.saveOrUpdate(bag);
			
			existsPaperBag.put(unitId+examInfoId, bag);
		}
		
		//------------------------------------------------2.生成学习中心课程试卷的包数------------------------------------------------
	}
	/**
	 * 查询指定批次下已生成的试卷袋标签,以MAP形式返回已生成试卷袋标签记录的学习中心ID+考试科目ID
	 * @param examSubId
	 * @return
	 */
	private Map<String,ExamPaperBag> findExistsExamPaperBag(String examSubId){
		Map<String,ExamPaperBag> map = new HashMap<String, ExamPaperBag>();
		StringBuffer hql 	   		 = new StringBuffer();
		hql.append(" from "+ExamPaperBag.class.getSimpleName()+" bag where bag.isDeleted=0 and bag.examInfo.examSub.resourceid=?");

		
		try {
			
			List<ExamPaperBag> list = (List<ExamPaperBag>) exGeneralHibernateDao.findByHql(hql.toString(), examSubId);
			if(null!=list && list.size()>0){
				for (ExamPaperBag bag:list) {
					map.put(bag.getUnit().getResourceid()+bag.getExamInfo().getResourceid(),bag);
				}
			}
		} catch (Exception e) {
			logger.error("获取已生成的试卷袋标签出错：{}"+e.getMessage());
			return map;
		}
		return map;
	}
	/**
	 * 清除选定批次已生成的试卷袋标签主表
	 * @param examSubId
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void delExistsExamPaperBag(String examSubId){
		
		
		
		Map<String,Object> param = new HashMap<String , Object>();
		param.put("examSubId", examSubId);

		
		StringBuffer delStat	 = new StringBuffer();
		
		delStat.append(" delete from edu_teach_exampaperbagstat bgs where bgs.examinfoid in( ");
		delStat.append("   select i.resourceid from  edu_teach_examinfo i where i.examsubid=:examSubId ) ");
		
		try {
			
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(delStat.toString(), param);   //删除主表
		} catch (Exception e) {
			logger.error("清除选定批次已生成的试卷袋标签出错：{}"+e.fillInStackTrace());
		}
	
	}
	/**
	 * 清除选定批次已生成的试卷袋标签子表
	 * @param examSubId
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void delExistsExamPaperBagDetails(String examSubId){
		
		
		
		Map<String,Object> param = new HashMap<String , Object>();
		param.put("examSubId", examSubId);
		StringBuffer delDetails	 = new StringBuffer();
		delDetails.append(" delete from edu_teach_exampaperbagdetails bgd ");
		delDetails.append(" where bgd.exampaperbagstatid in ( ");
		delDetails.append("     select bgs.resourceid  from edu_teach_exampaperbagstat bgs, edu_teach_examinfo i where bgs.examinfoid = i.resourceid ");
		delDetails.append("    and i.examsubid = :examSubId) ");

		try {
			
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(delDetails.toString(), param);//删除子表
		
		} catch (Exception e) {
			logger.error("清除选定批次已生成的试卷袋标签出错：{}"+e.fillInStackTrace());
		}
	
	}
}
