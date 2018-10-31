package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.vo.ExportCourseAndTeacherVo;
import com.hnjk.edu.teaching.vo.FailExamStudentVo;
import com.hnjk.edu.teaching.vo.NonexaminationExportVo;
import com.hnjk.edu.teaching.vo.NonexaminationExportVoForGZDX;

/**
 * 考试打印
 * @author luof
 * 
 */
public interface IExamPrintService extends IBaseService<ExamSub>{

	/**
	 * 获取所选考试年批次有学生预约考试的学习中心
	 * @param examSublId
	 * @return
	 */
	public List<Object> getExamSubUint(String examSublId) throws ServiceException;
	
	/**
	 * 打印准考证列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamCardList(Map<String, Object> condition, Page objPage)throws ServiceException;
	
	/**
	 * 按班级导出补考名单
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<FailExamStudentVo> nonexamExportByClasses(Map<String, Object> condition) throws ServiceException;
	/**
	 * 广大导出补考名单功能
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<NonexaminationExportVoForGZDX> nonexamExportByClassesForGZDX(Map<String, Object> condition) throws ServiceException;
	/**
	 * 导出教学计划课程表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ExportCourseAndTeacherVo> exportCourseAndTeacher(Map<String,Object> condition) throws ServiceException;
}
