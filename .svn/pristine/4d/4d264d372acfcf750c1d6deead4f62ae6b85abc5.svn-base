package com.hnjk.edu.resources.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.edu.resources.vo.SyllabusVo;

/**
 * 精品课程服务接口.
 * <code>ICourseResourceService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-28 下午12:35:18
 * @see 
 * @version 1.0
 */
public interface ICourseResourceService {
	/**
	 * 课程学习材料知识节点
	 * @param courseId
	 * @param mateType 0-阅读，1-视频，2-习题
	 * @return
	 * @throws ServiceException
	 */
	List<SyllabusVo> findSyllabusVoForCourseResource(String courseId, String resType) throws ServiceException;
	/**
	 * 学生学习记录统计
	 * @param courseId
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	List<SyllabusVo> findSyllabusVoForLearningStat(String courseId, String studentId) throws ServiceException;
	/**
	 * 检索
	 * @param courseId
	 * @param keyword
	 * @return
	 * @throws ServiceException
	 */
	List<SyllabusVo> findSyllabusVoForSearch(String courseId, String keyword) throws ServiceException;
}
