package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamSub;

/**
 * 
 * <code>考试/毕业论文批次预约Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-23 上午10:01:51
 * @see 
 * @version 1.0
 */
public interface IExamSubService  extends IBaseService<ExamSub>{
	
	Page findExamSubByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] ids)  throws ServiceException;
	void batchClose(String[] ids);

	void relationChildMethod(ExamSub examSub, HttpServletRequest request) throws ParseException;
	
	/**
	 * 同一时间段是否只有一个批次开放
	 */
	public boolean isUnique(ExamSub es) throws ServiceException;
	
	/**
	 * 根据条件查询 考试/毕业论文批次预约表
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 * @author：luof 
	 */
	@Override
	public List<ExamSub> findByCondition(Map <String,Object> paramMap) throws ServiceException;
	/**
	 * 查询当前年度的考试/毕业论文批次预约 对象
	 * @return
	 * @throws ServiceException
	 * @author：luof 
	 */
	public List<ExamSub> findExamSubByYear()throws ServiceException;
	/**
	 * 查询开放的考试/毕业论文批次预约 对象
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamSub> findOpenedExamSub(String subType)throws ServiceException;

	/**
	 * 毕业论文
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findGraduateThesisByCondition(Map<String, Object> condition, Page objPage);
	
	/**
	 * 改变状态
	 * @param resourceid
	 */
	void changeStates(String resourceid, String states);
	
	/**
	 * 检查考试批次是否被成绩关联
	 * @param resourceid
	 * @return
	 * @throws ServiceException
	 */
	public boolean isLinkedByExamResults(String resourceid)throws ServiceException;
	
	public List<Map<String,Object>> getExamSubByGradeId(String gradeId);
	
	/**
	 * 根据考试类型、年度和学期查询考试批次
	 * 
	 * @param examType
	 * @param nextYear
	 * @param nextTerm
	 * @return
	 */
	 ExamSub findExamSubBycondition(String examType, Long nextYear, String nextTerm);
}
