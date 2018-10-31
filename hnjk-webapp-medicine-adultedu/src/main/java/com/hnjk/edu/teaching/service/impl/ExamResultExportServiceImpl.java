package com.hnjk.edu.teaching.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.rao.configuration.xml.JaxbUtil;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.jaxb.entity.AchievementCourseTip;
import com.hnjk.edu.teaching.jaxb.entity.AchievementItem;
import com.hnjk.edu.teaching.jaxb.entity.AchievementList;
import com.hnjk.edu.teaching.jaxb.entity.AchievementTip;
import com.hnjk.edu.teaching.jaxb.entity.CourseAchievementList;
import com.hnjk.edu.teaching.jaxb.entity.OperateInfo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamResultsLog;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsExportService;
import com.hnjk.edu.teaching.service.IExamResultsLogService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.User;


/**
 * 处理成绩导入导出ServiceImpl
 * @author luof
 *
 */
@Service("examResultsExportService")
@Transactional
public class ExamResultExportServiceImpl extends BaseServiceImpl<ExamResults> implements IExamResultsExportService{
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatepapersorderservice;
	
	@Autowired
	@Qualifier("examResultsLogService")
	private IExamResultsLogService examResultsLogService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;

	/**
	 * 文件状态
	 */
	public static enum GenFile {
		FAILED,  // 失败
		SUCCESS, // 成功
		EMPTY    // 空
	}
	
	/**
	 * 将成绩生成成绩压缩流
	 * 
	 * @param examSubId
	 *            批次Id
	 * @param user
	 *            操作者
	 * @param checkstatuscodes
	 *            成绩的审核状态
	 * @param zos
	 *            成绩压缩流
	 * @throws Exception
	 */
	@Override
	public GenFile generateZipStreamFromAchievement(ExamSub examSub, String user, String[] checkstatuscodes, ZipOutputStream zos)throws Exception {
		
		ZipEntry listEntry = new ZipEntry(Constants.ACHIEVEMENTLIST_FILENAME);
		zos.putNextEntry(listEntry);
		try {
			return generateFileFromAchievement(examSub, user,checkstatuscodes, zos);
		} finally {
			zos.closeEntry();
		}
	}
	/**
	 * 将成绩生成成绩流
	 * 
	 * @param examSubId
	 *            批次Id
	 * @param user
	 *            操作者
	 * @param checkstatuscodes
	 *            成绩的审核状态
	 * @param os
	 *            成绩流
	 * @throws Exception
	 */
	private GenFile generateFileFromAchievement(ExamSub examSub, String user,
			   String[] checkstatuscodes, OutputStream os) throws Exception {
		
		AchievementTip tip = buildAchievementTip(user, examSub,checkstatuscodes);
		AchievementList list = buildAchievementList(examSub, tip,checkstatuscodes);
		
		if (list.getLists().size() == 0) {
			return GenFile.EMPTY;
		}
		boolean success = true;
		try {
			zipGenerateOutput(os, list);
		} catch (Exception e) {
			success = false;
		}
		return (success ? GenFile.SUCCESS : GenFile.FAILED);
	}
	/**
	 * 建立成绩摘要信息
	 * 
	 * @param user
	 *            操作者信息
	 * @param guid
	 *            批次guid
	 * @param checkstatuss
	 *            操作状态信息
	 * @return AchievementTip
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private AchievementTip buildAchievementTip(String user, ExamSub examSub,String[] checkstatuss) throws Exception {

		String isgraduate = "";
		if (examSub.getGradendDate() != null) {
			isgraduate	  = "1";
		} else {
			isgraduate 	  = "0";
		}

		OperateInfo info  = new OperateInfo(user,examSub.getResourceid(),examSub.getBatchName(),isgraduate);
		String 		 hql  = "select result.course.resourceid,count(result.resourceid) ";
					 hql += "from ExamResults result where result.isDeleted=0";
					 hql += " and result.examsubId=? ";
			   for (int i = 0; i < checkstatuss.length; i++) {
				   	 hql += " and result.checkStatus='"+checkstatuss[i]+"'";
			   }
			  
			   		 hql += "group by result.course.resourceid";
			   
		List list 		  = examResultsService.findByHql(hql, examSub.getResourceid());

		Map<String, ExamInfo> examInfoMap = findExamInfoMap(examSub.getResourceid());

		Collection<AchievementCourseTip> coursetips = new ArrayList<AchievementCourseTip>();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			String courseguid = (String) objs[0];
			
			ExamInfo examInfo = examInfoMap.get(courseguid);
			
			AchievementCourseTip coursetip = new AchievementCourseTip(examInfo.getCourse().getResourceid(),
																	  examInfo.getCourse().getCourseName(),
																	  0L,Integer.valueOf(Integer.parseInt(objs[1].toString())),"2","学时", 
																	  ExDateUtils.formatDateStr(examInfo.getExamStartTime(), "yyyy-MM-dd HH:mm:ss"),
																	  ExDateUtils.formatDateStr( examInfo.getExamEndTime(),"yyyy-MM-dd HH:mm:ss"));
			coursetips.add(coursetip);
			coursetip = null;
			examInfo = null;
			objs = null;
		}

		return new AchievementTip(info, coursetips);
	}
	
	/**
	 * 建立成绩列表信息
	 * 
	 * @param guid
	 *            批次guid
	 * @param tip
	 *            成绩摘要信息
	 * @param checkstatuss
	 *            操作状态信息
	 * @return AchievementList
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked" })
	private AchievementList buildAchievementList(ExamSub examSub,AchievementTip tip, String[] checkstatuss) throws Exception {
		
		
		OperateInfo info = tip.getOperateinfo();
		Map<String, List<AchievementItem>> itemMap = new HashMap();
		
		String hql = "from ExamResults result where result.isDeleted=0 and result.examsubId=? ";
		for (int i = 0; i < checkstatuss.length; i++) {
			hql+=" and result.checkStatus='"+checkstatuss[i]+"'";
		}
			   
		List<ExamResults> examResultList = examResultsService.findByHql(hql, examSub.getResourceid());

		for (ExamResults  examResult : examResultList) {

			AchievementItem item = makeAchievementItem(examResult);
			// 如果是毕业批次则添加导师信息
			if ("1".equals(info.getIsgraduate())) {
				String graduatePapersOrderHQL = " from GraduatePapersOrder graduate where graduate.isDeleted=0 ";
				      graduatePapersOrderHQL += " and graduate.examSub.resourceid=?";
				List mentors = graduatepapersorderservice.findByHql(hql, "");
				if (mentors.size() > 0) {
					GraduatePapersOrder graduate =(GraduatePapersOrder) mentors.get(0);
					item.setMentorguid(graduate.getTeacher().getResourceid());
					item.setMentor(graduate.getGuidTeacherName());
				}
			}

			List<AchievementItem> items = itemMap.get(item.getCourseguid());
			if (items == null) {
				items = new ArrayList();
				itemMap.put(item.getCourseguid(), items);
			}
			items.add(item);
		}
		 
		Collection<CourseAchievementList> list = new ArrayList<CourseAchievementList>();
		for (AchievementCourseTip coursetip : tip.getCoursetips()) {

			List<AchievementItem> items = itemMap.get(coursetip.getCourseguid());
			if (items != null) {
				list.add(new CourseAchievementList(coursetip, items));
			}
		}
		return new AchievementList(info, list);
	}
	private void zipGenerateOutput(OutputStream os, AchievementList list)throws Exception {
		JaxbUtil util = new JaxbUtil(AchievementList.class);
		util.marshal(list, os);
	}
	/**
	 * 过滤成绩信息
	 * 
	 * @param info
	 * @return PureStudentexaminfo
	 */
	private AchievementItem makeAchievementItem(ExamResults results) {
		AchievementItem item = new AchievementItem();
		
		item.setCourseexecstuguid(results.getResourceid());                                   //学生成绩序号
		item.setMajornamechs(results.getStudentInfo().getMajor().getMajorName());             // 专业名称
		item.setGradename(results.getStudentInfo().getGrade().getGradeName());                // 年级名称
		item.setBranchname(results.getStudentInfo().getBranchSchool().getUnitName());         // 分教点名称
		item.setClassicname(results.getStudentInfo().getClassic().getClassicName());          // 层次名称
		item.setStudentid(results.getStudentInfo().getStudyNo());							  // 学号
		item.setName(results.getStudentInfo().getStudentName());							  // 名字
		item.setLearningstyle(JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle",results.getStudentInfo().getLearningStyle()));// 学习方式
		item.setStudentguid(results.getStudentInfo().getResourceid());																    // 学生序号
		item.setCourseguid(results.getCourse().getResourceid());													                    // 原始考试课程序号
		item.setCoursenamechs(results.getCourse().getCourseName());													                    // 原始考试课程中文名称
		item.setIsoutplancourse(results.getIsOutplancourse());													                        // 是否本专业外课程
		item.setCoursescorestyle(JstlCustomFunction.dictionaryCode2Value("CodeCourseScoreStyle",results.getCourseScoreType()));         // 考试成绩类型
		item.setCoursescorestylecode(results.getCourseScoreType());													                    // 考试成绩类型代码
		item.setInputman1(results.getFillinMan());													                                    // 成绩输入人员
		item.setExamabnormity(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity",results.getExamAbnormity()));                 // 考试异常情况
		item.setExamabnormitycode(results.getExamAbnormity());													                        // 考试异常情况代码
		item.setIsdelayexam(results.getIsDelayExam());													                                // 是否缓考
		item.setBatchname(results.getExamInfo().getExamSub().getBatchName());													        // 考试批次名称
		item.setExamsubscribemanageguid(results.getExamInfo().getExamSub().getResourceid());							   // 考试预约管理guid
		item.setYearname(results.getExamInfo().getExamSub().getYearInfo().getYearName());								   // 预约批次年度名称
		item.setTermname(JstlCustomFunction.dictionaryCode2Value("CodeTerm",results.getExamInfo().getExamSub().getTerm()));// 学期名称
		item.setClassroomname(null==results.getExamroom()?"":results.getExamroom().getExamroomName());					   // 考场名称
		if (null!=results.getExamSeatNum()) {
			item.setExamseat(Long.parseLong(results.getExamSeatNum()));					   // 座位
		}
		item.setExamcount(results.getExamCount());																		   // 选考次数
		item.setCheckstatus(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus",results.getCheckStatus()));// 审核状态
		item.setCheckstatuscode(results.getCheckStatus());							 			    // 审核状态代码
		item.setModifyflag(results.getModifyFlag());								 				// 审核后修改标记
		item.setMemo(results.getCheckNotes()); 										  				//备注
		item.setScore( null==results.getTempintegratedScore()?"":results.getTempintegratedScore()); // 综合成绩
		item.setScore1(null==results.getTempwrittenScore()?"":results.getTempwrittenScore()); 	    // 卷面成绩
		item.setScore2(null==results.getTempusuallyScore()?"":results.getTempusuallyScore()); 		// 平时成绩
		
		return item;
	}
	/**
	 * 写操作日志
	 * 
	 * @param batchguid
	 *            操作批次
	 * @param username
	 *            用户名
	 * @param codes
	 *            操作的
	 * @throws Exception
	 */
	@Override
	public String writeOperateLog(ExamSub examSub, User user, String fileDIR, String filename, String operateType) throws ServiceException {
		
		Date curDate         = new Date();
		String attId         = "";
	
		try {
			
			Attachs attachs  = new Attachs();
			attachs.setAttName(examSub.getBatchName()+"成绩列表.zip");
			attachs.setAttType("zip");
			attachs.setFillinName(user.getUsername());
			attachs.setFillinNameId(user.getResourceid());
			attachs.setFormType(operateType);
			attachs.setFormId(examSub.getResourceid());
			attachs.setSerPath(fileDIR+File.separator + filename);
			attachs.setSerName(filename);
			attachs.setUploadTime(curDate);
			attachsService.save(attachs);
			
			ExamResultsLog log = new ExamResultsLog();
			log.setOptionType(operateType);
			log.setAttachId(attachs.getResourceid());
			log.setExamSubId(examSub.getResourceid());
			log.setFillinDate(curDate);
			log.setFillinMan(user.getUsername());
			log.setFillinManId(user.getResourceid());
			examResultsLogService.save(log);
			
			attId            = attachs.getResourceid();
		} catch (Exception e) {
			logger.error("生成成绩列表XML日志记录出错：{}",e.fillInStackTrace());
			return attId;
		}
		
	
		return attId;
	}
	@SuppressWarnings("unchecked")
	private Map<String, ExamInfo> findExamInfoMap(String examSubId) throws Exception {
		String hql ="from ExamInfo info where info.isDeleted=0 and info.examSub.resourceid=?";
		List<ExamInfo> infos = examInfoService.findByHql(hql,examSubId);
		Map<String, ExamInfo> courseMap = new HashMap<String, ExamInfo>();
		for (ExamInfo info : infos) {
			courseMap.put(info.getCourse().getResourceid(), info);
		}
		return courseMap;
	}
}
