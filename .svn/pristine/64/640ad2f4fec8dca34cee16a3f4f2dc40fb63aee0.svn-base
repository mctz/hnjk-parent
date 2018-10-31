package com.hnjk.edu.roll.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;


/**
 * <code>IGraduationQualifService</code><p>
 * 毕业资格列表
 * @author：广东学苑教育发展有限公司
 * @since： 2011-3-22 下午10:02:52
 * @see 
 * @version 1.0
 */
public interface IGraduationQualifService extends IBaseService<StudentInfo> {

	//StringBuffer  queryGraduationStr();
	//int[] getStudenScore(String stuId,String plandId);
	
	Page findGraduateionQualifByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	public String getGradeToMajor(String gradeId,String majorId,String id,String name,String click)throws ServiceException; //年级-专业 级联
	public String getGradeToMajor1(String gradeId,String majorId,String id,String name,String click,String classic)throws ServiceException; //年级-层次-专业 级联
	
	public String getGradeToMajorToClasses(String gradeId,String majorId,String classesId,String brschoolId,String id,String name)throws ServiceException; //年级-专业-班级-教学站
	public String getGradeToMajorToClasses1(String gradeId,String majorId,String classesId,String brschoolId,String id,String name,String classic)throws ServiceException; //年级-专业-层次-班级
	public String getMajors(String gradeId,String majorId,String classic);
	public String getClasses(String gradeId,String majorId,String classesId,String brschoolId,String classic);
	//学院2016修改
	public String getGradeToMajorToCourse(String examsub,String unit,String course,String isMk);

}
