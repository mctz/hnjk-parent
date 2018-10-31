package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
/**
 * 入学考试日志记录服务接口
 * <code>IRecruitExamLogsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-19 下午05:14:20
 * @see 
 * @version 1.0
 */
public interface IRecruitExamLogsService extends IBaseService<RecruitExamLogs> {
	/**
	 * 分页查找入学考试日志记录
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findRecruitExamLogsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException; 
	
	List<RecruitExamLogs> findRecruitExamLogsByCondition(Map<String, Object> condition) throws ServiceException; 
	/**
	 * 强制交卷
	 * @param res
	 * @throws ServiceException
	 */
	//void handPapers(String[] ids,String from) throws ServiceException; 
	/**
	 * 清除交卷状态，继续考试
	 * @param ids
	 * @param from
	 * @throws ServiceException
	 */
	void continueExam(String[] ids,String from) throws ServiceException; 
	/**
	 * 重考
	 * @param ids
	 * @throws ServiceException
	 */
	void reExam(String[] ids) throws ServiceException; 
	/**
	 * 网考重考
	 * @param ids
	 * @throws ServiceException
	 */
	void reOnlineExam(String[] ids) throws ServiceException;
	/**
	 * 重新登录
	 * @param ids
	 * @throws ServiceException
	 */
	void reloginExam(String[] ids) throws ServiceException;
	
	int joinCourseExamPaperDetails(String examSubId,String examInfoId) throws ServiceException;
	/**
	 * 入学考试名单导出
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> findRecruitExamLogsByConditionForExport(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 期末机考强制交卷
	 * @param ids
	 * @throws ServiceException
	 */
	void handFinalExamPapers(String[] ids) throws ServiceException;
}
