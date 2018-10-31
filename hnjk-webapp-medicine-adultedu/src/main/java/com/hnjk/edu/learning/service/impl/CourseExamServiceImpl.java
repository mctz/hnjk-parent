package com.hnjk.edu.learning.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.CharSetUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.ZipUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.learning.util.CourseExamUtils;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 题库试题管理服务接口实现.
 * <code>CourseExamServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-29 上午11:12:36
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseExamService")
public class CourseExamServiceImpl extends BaseServiceImpl<CourseExam> implements ICourseExamService {

	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseExamByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = getCourseExamHqlbyCondition(condition, values);
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	private StringBuffer getCourseExamHqlbyCondition(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(" from "+CourseExam.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);		
		if(!(condition.containsKey("filterChildExam") && Constants.BOOLEAN_NO.equals(condition.get("filterChildExam")))){//不查询子题
			hql.append(" and c.parent is null ");
		} 
		
		if(condition.containsKey("isEnrolExam")){//是否入学考试
			hql.append(" and c.isEnrolExam =:isEnrolExam ");
			values.put("isEnrolExam", condition.get("isEnrolExam"));
		}
		if(condition.containsKey("courseName")){//入学考试课程
			hql.append(" and c.courseName =:courseName ");
			values.put("courseName", condition.get("courseName"));
		}
		if(condition.containsKey("courseId")){//课程
			hql.append(" and c.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("examType")){//题型
			hql.append(" and c.examType =:examType ");
			values.put("examType", condition.get("examType"));
		}	
		if(condition.containsKey("notInExamType")){
			hql.append(" and c.examType not in "+condition.get("notInExamType")+" ");
		}
		if(condition.containsKey("examform")){//考试形式
			if("unit_exam".equals(condition.get("examform"))){
				hql.append(" and (c.examform =:examform or c.examform is null) ");
			} else {
				hql.append(" and c.examform =:examform ");
			}			
			values.put("examform", condition.get("examform"));
		}	
		if(condition.containsKey("examforms")){
			hql.append(" and c.examform in (:examforms) ");
			values.put("examforms", condition.get("examforms"));
		}
		if(condition.containsKey("existCourseExamIds")){
			hql.append(" and c.resourceid not in("+condition.get("existCourseExamIds")+") ");
		}
		if(condition.containsKey("examNodeType")){//类别
			hql.append(" and c.examNodeType =:examNodeType ");
			values.put("examNodeType", condition.get("examNodeType"));
		}	
		if(condition.containsKey("difficult")){//难度
			hql.append(" and c.difficult =:difficult ");
			values.put("difficult", condition.get("difficult"));
		}
		if(condition.containsKey("showOrder")){//序号
			hql.append(" and c.showOrder =:showOrder ");
			values.put("showOrder", Integer.parseInt(condition.get("showOrder").toString()));
		}
		if(condition.containsKey("requirement")){//考试要求度
			hql.append(" and c.requirement =:requirement ");
			values.put("requirement", condition.get("requirement"));
		}
		if(condition.containsKey("question")){
			hql.append(" and c.question like :question ");
			values.put("question", "%"+condition.get("question")+"%");
		}
		if(condition.containsKey("keywords")){//关键字
			hql.append(" and (lower(c.keywords) like :keywords or lower(c.question) like :keywords) ");
			values.put("keywords", "%"+condition.get("keywords").toString().toLowerCase()+"%");
		}
		//if(condition.containsKey("teacherId")){//课程老师
		//	hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.course.resourceid and (t.teacherId like :teacherId or t.assistantIds like :teacherId ) ) ");
		//	values.put("teacherId", "%"+condition.get("teacherId")+"%");//老师
		//}
		//if(condition.containsKey("syllabusId")){
		//	hql.append(" and not exists (from "+ActiveCourseExam.class.getSimpleName()+" a where a.isDeleted=0 and a.syllabus.resourceid=:syllabusId and a.courseExam.resourceid=c.resourceid ) ");
		//	values.put("syllabusId", condition.get("syllabusId"));
		//}
		if(condition.containsKey("paperId")){
			hql.append(" and not exists (from "+CourseExamPaperDetails.class.getSimpleName()+" d where d.isDeleted=0 and d.courseExam.resourceid=c.resourceid and d.courseExamPapers.resourceid=:paperId ) ");
			values.put("paperId", condition.get("paperId"));
		}
		return hql;
	}

	@Override
	@Transactional(readOnly=true)
	public List<CourseExam> findCourseExamByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = getCourseExamHqlbyCondition(condition, values);	
		if(condition.containsKey("orderby")){
			hql.append(" order by "+condition.get("orderby"));
		} else {
			hql.append(" order by c.examNodeType,c.examType ");
		}		
		return findByHql(hql.toString(),values);
	}

	@Override
	public void saveOrUpdate(CourseExam courseExam) throws ServiceException {
		exGeneralHibernateDao.saveOrUpdate(courseExam);
	}

	@Override
	public void batchDeleteCourseExam(String[] ids,String isEnrolExam)  throws ServiceException {
		if(ids!=null && ids.length>0){
			for (int i = 0; i < ids.length; i++) {
				deleteCourseExam(ids[i],isEnrolExam);				
			}
		}
	}
	public void deleteCourseExam(String resourceid,String isEnrolExam) throws ServiceException {		
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = " select * from edu_lear_exams t where not exists ( ";
		if(Constants.BOOLEAN_NO.equals(isEnrolExam)){//非入学考试试题，查询随堂练习和作业
			sql += " select c.resourceid from edu_lear_courseexam c where c.isdeleted=0 and c.examid=t.resourceid ";
			sql += " union all ";
			sql += " select e.resourceid from edu_lear_exercise e where e.isdeleted=0 and e.examid=t.resourceid ";
			sql += " union all ";
		}		
		sql += " select d.resourceid from edu_lear_expaperdetails d where d.isdeleted=0 and d.examid=t.resourceid ";
		sql += " ) and t.isdeleted=0 and t.resourceid=:resourceid";
		SQLQuery query = session.createSQLQuery(sql).addEntity(CourseExam.class);
		query.setParameter("resourceid",resourceid);
		CourseExam courseExam = (CourseExam)query.uniqueResult();//查出的是没被引用的试题
		if(courseExam==null){
			throw new ServiceException("试题使用中，不能删除！");
		} else {
			//delete(courseExam);//删除没被引用的试题
			User user = SpringSecurityHelper.getCurrentUser();
			courseExam.setIsDeleted(1);
			courseExam.setModifyMan(user.getCnName());
			courseExam.setModifyDate(new Date());
		}		
	}
	/*
	 * isEnrolExam=是否入学考试试题
	 * examform=考试题库(入学考试、随堂练习、在线考试、期末考试)
	 * course，courseName=课程
	 * text=导入试题文本
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.ICourseExamService#parseAndImportCourseExam(java.lang.String, java.lang.String, com.hnjk.edu.basedata.model.Course, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CourseExam> parseAndImportCourseExam(String isEnrolExam,String examform, Course course, String courseName, String text) throws ServiceException {
		List<CourseExam> list = new ArrayList<CourseExam>();
		User user = SpringSecurityHelper.getCurrentUser();
		Map<String, String> examNodeTypeMap = getExamNodeTypeDictValueByMap();//试题类型字典值
		//◎题型：单选题[xxx]                                --题型[类型值或名称]
		//影响度是指电子商务对国民经济和（   ）产生的影响       --题干
		//A．社会发展                 B．企业发展    
		//C．市场                     D．人民生活
		//【 A 】                                           --答案
		String[] texts1 = text.split(CourseExam.EXAMTYPEREGEX1);//划分不同题型题目
		for (String str : texts1) {
			str = str.trim();
			if(ExStringUtils.isNotBlank(str)){
				String examType = getExamType(str);//获取题型
				
				if(Constants.BOOLEAN_YES.equals(isEnrolExam)){
					if(!(CourseExam.SINGLESELECTION.equals(examType)||CourseExam.MUTILPCHIOCE.equals(examType)||CourseExam.CHECKING.equals(examType)||CourseExam.COMPREHENSION.equals(examType))){
						throw new WebException("入学考试题目只能为选择题或判断题或材料题！");
					}
				}
				if(ExStringUtils.isNotBlank(examType)){					
					str = ExStringUtils.trimToEmpty(str.substring(3));//题目列表文本					
					if(CourseExam.COMPREHENSION.equals(examType)){//材料题 
						String examNodeType = getExamNodeType(str,examNodeTypeMap);//获取类型[examNodeType]
						if(ExStringUtils.isNotBlank(examNodeType)){
							str = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(str, "]"));
						}		
						String isOnlineAnswer = Constants.BOOLEAN_NO;//是否在线做题
						if(ExStringUtils.startsWithIgnoreCase(str, "[在线做题]")){
							isOnlineAnswer = Constants.BOOLEAN_YES;
							str = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(str, "[在线做题]"));
						}
						if(str.startsWith("T题型")){//材料题为空，不处理
							continue;
						}
						int index = str.indexOf("T题型");
						if(index>0){							
							String paragraph = ExStringUtils.trimToEmpty(str.substring(0,index-1));	//组合体文章段落					
							String subtext = str.substring(index);
							String[] texts2 = subtext.split(CourseExam.EXAMTYPEREGEX2);//划分子问题题型
							
							CourseExam parentCourseExam = new CourseExam(isEnrolExam,course,courseName,examType,paragraph,null,new Date(),user.getCnName(),user.getResourceid(),examNodeType,examform);
							parentCourseExam.setIsOnlineAnswer(isOnlineAnswer);
							list.add(parentCourseExam);
							for (String s : texts2) {
								s = s.trim();
								if(ExStringUtils.isNotBlank(s)){
									String subExamType = getExamType(s);//获取题型									
									if(Constants.BOOLEAN_YES.equals(isEnrolExam)){
										if(!(CourseExam.SINGLESELECTION.equals(subExamType)||CourseExam.MUTILPCHIOCE.equals(subExamType)||CourseExam.CHECKING.equals(subExamType))){
											throw new WebException("入学考试材料题题目只能为选择题或判断题！");
										}
									}
									if(ExStringUtils.isNotBlank(subExamType)){
										s = ExStringUtils.trimToEmpty(s.substring(3));
										textExamToModel(isEnrolExam,examform, course, courseName, user, subExamType, parentCourseExam, s,list, examNodeTypeMap); 
									}
								}
							}
						} else {
							throw new WebException("材料题格式不正确，请确保格式正确:<br/> "+str);
						}						
					} else {//非材料题
						textExamToModel(isEnrolExam,examform, course, courseName, user, examType, null, str,list,examNodeTypeMap);					
					}					
				} else {//没办法区分题型
					throw new WebException("题型格式不正确，请确保格式正确:<br/> "+str);
				}
			}
		}
		/*排序*/
		int showOrder = 0;
		Date currentDate = new Date();
		for (CourseExam courseExam : list) {
			courseExam.setShowOrder(++showOrder);
			courseExam.setFillinDate(currentDate);
			for (CourseExam child : courseExam.getChilds()) {
				child.setShowOrder(++showOrder);
				child.setFillinDate(currentDate);
			}
		}
		return list;
	}

	/**
	 * 把题目文本转为model
	 * @param isEnrolExam 是否入学考
	 * @param examform 试题考试形式
	 * @param course 课程
	 * @param courseName 入学考课程
	 * @param user 用户名
	 * @param examType 题型
	 * @param parentCourseExam 材料题父试题
	 * @param s 题目列表文本
	 * @param list 已处理试题列表
	 * @param examNodeTypeMap
	 */
	private void textExamToModel(String isEnrolExam,String examform, Course course,
			String courseName, User user, String examType,
			CourseExam parentCourseExam, String s,
			List<CourseExam> list,Map<String, String> examNodeTypeMap) {
		String examNodeType = getExamNodeType(s,examNodeTypeMap);//获取类型
		if(ExStringUtils.isNotBlank(examNodeType)){
			s = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(s, "]"));
		} else {
			if(parentCourseExam!=null){//材料题
				examNodeType = parentCourseExam.getExamNodeType();
			}
		}
		String isOnlineAnswer = Constants.BOOLEAN_NO;
		if(ExStringUtils.startsWithIgnoreCase(s, "[在线做题]")){
			isOnlineAnswer = Constants.BOOLEAN_YES;
			s = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(s, "[在线做题]"));
		}
		//影响度是指电子商务对国民经济和（   ）产生的影响       --题干
		//A．社会发展                 B．企业发展    
		//C．市场                     D．人民生活
		//【A】                                             --答案
		String[] exams = s.split(CourseExam.EXAMREGEX);//题目列表
		if(exams!=null && exams.length>0){
			for (String exam : exams) {
				if(ExStringUtils.isNotBlank(exam)){
					String[] qas = exam.split(CourseExam.QUESTIONANSWEREGEX);//分割问题与答案
					if(qas!=null && qas.length==2){
						String answer = trimExamAnswer(ExStringUtils.trimToEmpty(qas[1]),examType);
						if(answer==null){
							throw new WebException("<b>"+JstlCustomFunction.dictionaryCode2Value("CodeExamType", examType)+"答案错误</b>: <br/>"+(exam.replace("\r\n", "<br/>")).replace("\n", "<br/>"));
						}
						if(CourseExam.ESSAYS.equals(examType)){//论述题
							answer = replaceToHtml(answer);
						}
						CourseExam courseExam = new CourseExam(isEnrolExam,course,courseName,examType,
								replaceToHtml(ExStringUtils.trimToEmpty(qas[0])),answer,
								new Date(),user.getCnName(),user.getResourceid(),examNodeType,examform);	
						if(parentCourseExam!=null){//材料题
							courseExam.setParent(parentCourseExam);
							courseExam.setIsOnlineAnswer(parentCourseExam.getIsOnlineAnswer());
							parentCourseExam.getChilds().add(courseExam);
						} else {
							courseExam.setIsOnlineAnswer(isOnlineAnswer);//设置是否在线做题
							list.add(courseExam);
						}
					} else {//问题与答案无法区分
						throw new WebException("答案格式不正确，请确保格式正确！ <br/>"+s.substring(s.indexOf(exam)));
					}					
				} 								
			}
		}
	}	
	//获取试题类型
//	private String getExamNodeType(String str){		
//		String examNodeType = ExStringUtils.trimToEmpty(ExStringUtils.substringBetween(str, "(", ")"));
//		if(ExStringUtils.isNotBlank(examNodeType)){
//			List<Dictionary> dicts = CacheAppManager.getChildren("CodeExamNodeType");
//			for (Dictionary dict : dicts) {
//				if(examNodeType.equals(dict.getDictValue())){
//					return dict.getDictValue();
//				}
//			}
//		}
//		return null;
//	}	
	/**
	 * 获取试题类型
	 * @param str 试题文本
	 * @param examNodeTypeMap 试题类型Map：key=类型值或类型名，value=类型字典值
	 * @return
	 */
	private String getExamNodeType(String str, Map<String, String> examNodeTypeMap){
		String examNodeType = ExStringUtils.trimToEmpty(ExStringUtils.substringBetween(str, "[", "]"));//类型值或中文名称
		if(ExStringUtils.isNotBlank(examNodeType) && examNodeTypeMap.containsKey(examNodeType)){//值或名称包含其一，返回类型字典值
			return examNodeTypeMap.get(examNodeType);
		}
		return null;
	}
	/**
	 * 试题类型名称和值映射Map
	 * @return
	 * @throws ServiceException
	 */
	private Map<String, String> getExamNodeTypeDictValueByMap() throws ServiceException {
		Map<String,String> valueMap = new HashMap<String,String>();		
		List<Dictionary> dictList = CacheAppManager.getChildren("CodeExamNodeType");
		for (Dictionary dict : dictList) {
			valueMap.put(dict.getDictName(), dict.getDictValue());
			valueMap.put(dict.getDictValue(), dict.getDictValue());
		}	
		return valueMap;
	}
	/**
	 * 转换为合法答案
	 * @param answer
	 * @param examType
	 * @return
	 */
	private String trimExamAnswer(String answer,String examType) {		
		if(CourseExam.MUTILPCHIOCE.equals(examType) || CourseExam.SINGLESELECTION.equals(examType)){//单选或多选
			answer = CharSetUtils.keep(ExStringUtils.full2HalfChange(answer), new String[]{"a-z","A-Z"});//只保留字母
			if(CourseExam.SINGLESELECTION.equals(examType) && answer.length()!=1){//单选题答案只有一位字母
				return null;
			}
		} else if(CourseExam.CHECKING.equals(examType)){//判断题
			answer = CharSetUtils.keep(ExStringUtils.full2HalfChange(answer), new String[]{"T","F","对","错","√","×"});
			if(answer.length()!=1){
				return null;
			}
		}
		return answer;
	}
	
	/**
	 * 获取题型
	 * @param str
	 * @return
	 */
	private String getExamType(String str) {
		if(str.startsWith("单选题")){
			return CourseExam.SINGLESELECTION;	
		} else if(str.startsWith("多选题")){
			return CourseExam.MUTILPCHIOCE;	
		} else if(str.startsWith("判断题")){
			return CourseExam.CHECKING;	
		} else if(str.startsWith("填空题")){
			return CourseExam.COMPLETION;	
		} else if(str.startsWith("论述题")){
			return CourseExam.ESSAYS;	
		} else if(str.startsWith("材料题")){
			return CourseExam.COMPREHENSION;
		}
		return "";
	}
	
	/**
	 * 把换行和空格替换成html格式
	 * @param str
	 * @return
	 */
	private String replaceToHtml(String str) {
		if(str.indexOf("<img")>-1 || str.indexOf("<table")>-1){
			return str.replaceAll("[\n\r]+", "<br/>");
		}
		return str.replaceAll("[\n\r]+", "<br/>").replaceAll("[ 　]{2,}", "&nbsp; ");
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getCourseExamTypeAndCount(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " select new map(examNodeType as examNodeType,examType as examType,count(resourceid) as examcount) from "+CourseExam.class.getSimpleName()+" where parent is null ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseId")){//课程
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}		
		if(condition.containsKey("examType")){//题型
			hql += " and examType =:examType ";
			values.put("examType", condition.get("examType"));
		}
		if(condition.containsKey("notInExamType")){
			hql += " and examType not in "+condition.get("notInExamType")+" ";
		}
		if(condition.containsKey("isEnrolExam")){//是否入学考试
			hql += " and isEnrolExam =:isEnrolExam ";
			values.put("isEnrolExam", condition.get("isEnrolExam"));
		}
		if(condition.containsKey("courseName")){//入学考试课程
			hql += " and courseName =:courseName ";
			values.put("courseName", condition.get("courseName"));
		}
		if(condition.containsKey("examform")){//考试形式
			if("unit_exam".equals(condition.get("examform"))){
				hql += " and (examform =:examform or examform is null) ";
			} else {
				hql += " and examform =:examform ";
			}			
			values.put("examform", condition.get("examform"));
		}	
		if(condition.containsKey("existCourseExamIds")){
			hql += " and resourceid not in("+condition.get("existCourseExamIds")+") ";
		}
		hql += " group by examNodeType,examType ";
		hql += " order by examNodeType,examType ";
		return (List<Map<String, Object>>) exGeneralHibernateDao.findByHql(hql, values);
	}
	/*
	 * 导入随堂练习习题
	 */
	@Override
	public int parseAndImportActiveCourseExam(String text) throws ServiceException {
		List<ActiveCourseExam> activeCourseExamList = new ArrayList<ActiveCourseExam>(0);//随堂练习列表
		List<CourseExam> courseExamList = new ArrayList<CourseExam>(0);//试题列表
		Map<String, String> examNodeTypeMap = getExamNodeTypeDictValueByMap();//试题类型字典值
		//#知识点:第一章(id:402881b935e0ff9d0135e11f12370002)
		//◎题型:单选题(YYCHYF)
		//question
		//A.answer1 B.answer2 C.answer3 D.answer4	
		//【A】
		String[] syllabusTexts = text.split("\\s*#知识点(：|:)");//划分章节
		for (String syllabusText : syllabusTexts) {
			if(ExStringUtils.isNotBlank(syllabusText) && syllabusText.indexOf("(id:")>0){ //存在随堂练习知识点id			
				String syllabusId = ExStringUtils.substringBetween(syllabusText,"(id:", ")");			
				Syllabus syllabus = null;
				if(ExStringUtils.isNotBlank(syllabusId)){
					syllabus = syllabusService.get(ExStringUtils.trimToEmpty(syllabusId));
				}
				if(syllabus==null){
					throw new WebException("知识点格式不正确，请确保格式正确:<br/>"+replaceToHtml(syllabusText));
				}
				syllabusText = ExStringUtils.substringAfter(syllabusText, "(id:"+syllabusId+")");
				String[] examTyteTtexts = syllabusText.split(CourseExam.EXAMTYPEREGEX1);//划分章节下的题型				
				User user = SpringSecurityHelper.getCurrentUser();
				for (String examTypeText : examTyteTtexts) {
					examTypeText = ExStringUtils.trimToEmpty(examTypeText);
					if(ExStringUtils.isNotBlank(examTypeText)){
						String examType = getExamType(examTypeText);//提取题型
						if(ExStringUtils.isBlank(examType)||CourseExam.ESSAYS.equals(examType)){
							throw new WebException("随堂练习只能为选择或判断题或填空题或材料题:<br/>"+replaceToHtml(examTypeText));
						}						
						if(CourseExam.COMPREHENSION.equals(examType)){//材料题
							examTypeText = ExStringUtils.trimToEmpty(examTypeText.substring(3));//题目列表文本
							String examNodeType = getExamNodeType(examTypeText,examNodeTypeMap);//获取类型
							if(ExStringUtils.isNotBlank(examNodeType)){
								examTypeText = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(examTypeText, "]"));
							}
							if(ExStringUtils.startsWith(examTypeText, "T题型")){//材料题为空
								continue;
							}
							int index = examTypeText.indexOf("T题型");
							if(index>0){							
								String paragraph = ExStringUtils.trimToEmpty(examTypeText.substring(0,index-1));							
								String subtext = examTypeText.substring(index);
								String[] subExamTexts = subtext.split(CourseExam.EXAMTYPEREGEX2);//划分子问题题型
								
								CourseExam parentCourseExam = new CourseExam(Constants.BOOLEAN_NO,syllabus.getCourse(),null,examType,paragraph,null,new Date(),user.getCnName(),user.getResourceid(),examNodeType,"unit_exam");
								courseExamList.add(parentCourseExam);//材料题题干
								
								ActiveCourseExam activeexam = new ActiveCourseExam();
								activeexam.setCourseExam(parentCourseExam);
								activeexam.setSyllabus(syllabus);
								activeCourseExamList.add(activeexam);
								
								for (String subExamText : subExamTexts) {
									subExamText = ExStringUtils.trimToEmpty(subExamText);
									if(ExStringUtils.isNotBlank(subExamText)){
										String subExamType = getExamType(subExamText);//获取子题型									
										if(ExStringUtils.isBlank(subExamType)||Integer.valueOf(subExamType)>4){
											throw new WebException("材料题子题只能为选择或判断题或填空题:<br/>"+replaceToHtml(subExamText));
										}
										addActiveCourseExam(activeCourseExamList,courseExamList,syllabus,user,subExamText,subExamType,parentCourseExam,examNodeTypeMap);
									}
								}
							} else {
								throw new WebException("材料题格式不正确，请确保格式正确:<br/> "+examTypeText);
							}	
						} else {
							addActiveCourseExam(activeCourseExamList,courseExamList,syllabus,user,examTypeText,examType,null,examNodeTypeMap);
						}						
					}
				}	
			} 
		}
		//调整随堂练习和试题序号
		int showOrder = 1;
		Date currentDate = new Date();
		Syllabus tempSyllabus = null;		
		for (ActiveCourseExam exam : activeCourseExamList) {			
			if(!exam.getSyllabus().equals(tempSyllabus)){
				tempSyllabus = exam.getSyllabus();
				showOrder = activeCourseExamService.getNextShowOrder(tempSyllabus.getResourceid());
			}
			if(CourseExam.COMPREHENSION.equals(exam.getCourseExam().getExamType())){//材料题大题序号与第一小题同
				exam.setShowOrder(showOrder);
			} else {
				exam.setShowOrder(showOrder++);
			}			
		}
		showOrder = 0;
		for (CourseExam courseExam : courseExamList) {
			courseExam.setShowOrder(++showOrder);
			courseExam.setFillinDate(currentDate);
			for (CourseExam child : courseExam.getChilds()) {
				child.setShowOrder(++showOrder);
				child.setFillinDate(currentDate);
			}
		}
		activeCourseExamService.saveImportAcitveCoruseExam(activeCourseExamList, courseExamList);
		return activeCourseExamList.size();
//		return 0;
	}
	

	/**
	 * 把题目列表转为试题model
	 * @param activeCourseExamList
	 * @param courseExamList
	 * @param syllabus
	 * @param user
	 * @param examText
	 * @param examType
	 * @param parentCourseExam
	 * @throws ServiceException
	 */
	private void addActiveCourseExam(List<ActiveCourseExam> activeCourseExamList,List<CourseExam> courseExamList, Syllabus syllabus, User user, String examText, String examType,CourseExam parentCourseExam,Map<String, String> examNodeTypeMap) throws ServiceException{
		examText = ExStringUtils.trimToEmpty(examText.substring(3));
		String examNodeType = getExamNodeType(examText,examNodeTypeMap);//获取类型
		if(ExStringUtils.isNotBlank(examNodeType)){
			examText = ExStringUtils.trimToEmpty(ExStringUtils.substringAfter(examText, "]"));
		}	
		String[] exams = examText.split(CourseExam.EXAMREGEX);		
		if(exams!=null && exams.length>0){
			for (String exam : exams) {
				if(ExStringUtils.isNotBlank(exam)){					
					String[] qas = exam.split(CourseExam.QUESTIONANSWEREGEX);//分离问题与答案
					if(qas!=null && qas.length==2){						
						CourseExam courseExam = new CourseExam();
						courseExam.setExamType(examType);
						courseExam.setExamNodeType(examNodeType);
						courseExam.setIsEnrolExam(Constants.BOOLEAN_NO);
						courseExam.setCourse(syllabus.getCourse());
						String answer = trimExamAnswer(ExStringUtils.trimToEmpty(qas[1]),examType);
						if(answer==null){
							throw new WebException("<b>"+JstlCustomFunction.dictionaryCode2Value("CodeExamType", examType)+"答案错误</b>: <br/>"+replaceToHtml(exam));
						}
						courseExam.setAnswer(answer);
						courseExam.setQuestion(replaceToHtml(ExStringUtils.trimToEmpty(qas[0])));
						courseExam.setFillinDate(new Date());
						courseExam.setFillinMan(user.getCnName());
						courseExam.setFillinManId(user.getResourceid());
						courseExam.setExamform("unit_exam");//随堂练习
						courseExam.setParent(parentCourseExam);
						if(CourseExam.SINGLESELECTION.equals(examType)){
							courseExam.setAnswerOptionNum(4);
						}
						if(parentCourseExam!=null){//材料题
							parentCourseExam.getChilds().add(courseExam);							
						} else {
							courseExamList.add(courseExam);
						}						
						
						ActiveCourseExam activeexam = new ActiveCourseExam();
						activeexam.setCourseExam(courseExam);
						activeexam.setSyllabus(syllabus);						
						
						activeCourseExamList.add(activeexam);
					} else {
						throw new WebException("答案格式不正确，请确保格式正确: <br/>"+replaceToHtml(exam));
					}
				} 								
			}
		} 
	}
	
	/**
	 * 解压并处理html文件获取文本，并替换图片路
	 * @param zipFilePath
	 * @param picPatch
	 * @return
	 * @throws IOException
	 */
	public static String getTextFromZipOfHtml(String zipFilePath,String picPatch) throws IOException {
		String text = "";
		String unzipDir = ExStringUtils.substringBeforeLast(zipFilePath, ".zip");//解压路径
		ZipUtils.unZip(unzipDir, zipFilePath);//解压html压缩包
		
		File htmlFile = null;//html文件
		File picFile = null;//图片文件夹
		for (File file : new File(unzipDir).listFiles()) {
			if(file.isDirectory() && ExStringUtils.endsWithIgnoreCase(file.getName(), ".files")){
				picFile = file;
			}
			if(file.isFile() && (ExStringUtils.endsWithIgnoreCase(file.getName(), ".html") || ExStringUtils.endsWithIgnoreCase(file.getName(), ".htm"))){
				htmlFile = file;
			}
		}
		if(htmlFile!=null){			
			String html = new String(FileUtils.readFile(htmlFile),"gb2312");	
			//保留图片、表格、下划线、加粗
			String[] filterTags = {"img","table","thead","th","tr","td","u","b"};
			text = CourseExamUtils.trimHtml2Txt(html,filterTags);
			if(picFile!=null){						
				FileUtils.copyFolder(picFile.getAbsolutePath(), Constants.EDU3_DATAS_LOCALROOTPATH+picPatch);//把zip解压的图片文件复制到指定的图片目录
				String oldPre = picFile.getName()+"/";
				String pre = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()+picPatch.replace(File.separator, "/")+"/";
				text = text.replace(oldPre, pre);				
			}
		}
		return text;
	}
}
