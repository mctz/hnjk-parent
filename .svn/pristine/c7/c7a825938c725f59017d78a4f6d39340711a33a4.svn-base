package com.hnjk.edu.teaching.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;

/**
 * 教学计划(套餐)表
 * <code>ITeachingPlanService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-24 下午03:42:43
 * @see 
 * @version 1.0
 */
public interface ITeachingPlanService  extends IBaseService<TeachingPlan>{

	Page findTeachingPlanByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	List<TeachingPlan> findTeachingPlanByCondition(Map<String, Object> condition) throws ServiceException;
	
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 获取下一版本号
	 * @param majorId
	 * @param classicId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextVersionNum(String majorId, String classicId) throws ServiceException;;
	/**
	 * 查找最新发布的教学计划
	 * @return
	 * @throws ServiceException
	 */
	List<TeachingPlan> findLatestTeachingPlan(Map<String, Object> condition) throws ServiceException;
	/**
	 * 获取教学计划关联的专业
	 * @param condition
	 * @return
	 */
	
	public List<Map<String,Object>> getUnitTeachingPlanMajor(Map<String,Object> condition) throws ServiceException;
	
	/**
	 * 按班级打印成绩表 自定义表头
	 * @param studentInfos
	 * @return
	 * @throws Exception
	 */
	LinkedList<String> printResultsByClassesTitleListMap(List<StudentInfo> studentInfos,String terms,String degreeUnitExam) throws  Exception;
	List<StudentExamResultsVo> printResultsByClassesListMap(List<StudentInfo> studentInfos, List<String> tempList, String degreeUnitExam) throws  Exception ;

	String constructOptions(List<TeachingPlan> planList, String defaultValue);

	/**
	 * 新：按班级打印成绩表 自定义表头
	 * @param examResultsList
	 * @param terms
	 * @param degreeUnitExam
	 * @return
	 */
	LinkedList<String> printResultsByClassesTitleListMap_new(List<StudentExamResultsVo> examResultsList, String terms, String degreeUnitExam) throws  Exception;

	/**
	 * 将成绩vo转为map(一个VO对应一个成绩，一个Map对应一个学生所有成绩)
	 * @param examResultsList
	 * @param mapDict
	 * @param degreeUnitExam
	 * @return
	 * @throws Exception
	 */
	List<LinkedHashMap<String,Object>> getStuExamMapByExamVOList(List<StudentExamResultsVo> examResultsList,Map<String,Integer> mapDict,String degreeUnitExam,String hk,String schoolCode) throws  Exception;

	/**
	 * 取出字典的顺序
	 * @param dictionaryCode
	 * @return
	 */
	Map<String,Integer> getDictIndext(String dictionaryCode);
}
