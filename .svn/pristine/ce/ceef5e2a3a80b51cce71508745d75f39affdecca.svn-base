package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.vo.GraduatePapersOrderVo;

/**
 * 毕业生论文预约信息表.
 * <code>IGraduatePapersOrderService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午03:11:10
 * @see 
 * @version 1.0
 */
public interface IGraduatePapersOrderService extends IBaseService<GraduatePapersOrder>{

	//更新或插入预约记录
	void saveOrUpdateGraduatePaperOrder(GraduatePapersOrder graduatePapersOrder) throws ServiceException;
	
	Page findGraduateByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	void batchCascadeDelete(String[] ids) throws ServiceException;

	void audit(String resourceid, String userid, String cnName,int status) throws ServiceException;

	void batchAudit(String[] ids, String userid, String cnName ,int status) throws ServiceException;

	/**
	 * 单个处理进入下一环节
	 * @param resourceid
	 */
	void next(String resourceid) throws ServiceException;

	/**
	 * 批量处理进入下一环节
	 * @param ids
	 */
	void batchCascadeNext(String[] ids) throws ServiceException;
	
	/**
	 * 统计毕业论文预约统计
	 * @param objPage
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page countGradePaperOrderByCondition(Page objPage,Map<String, Object> condition) throws ServiceException;
	/**
	 * 保存毕业论文录入成绩
	 * @param request
	 * @throws ServiceException
	 */
	void saveOrUpdateGraduateResults(HttpServletRequest request) throws ServiceException;
	/**
	 * 审核毕业论文成绩
	 * @param ids
	 * @throws ServiceException
	 */
	void auditThesisResults(String[] ids, String teachType)throws ServiceException;
	
	List<GraduatePapersOrder> findGraduateByCondition(Map<String, Object> condition) throws ServiceException;
	
	List<GraduatePapersOrderVo> findGraduatePapersOrderVoByBatchId(String batchId) throws ServiceException;
	/**
	 * A+B模式毕业论文成绩录入列表
	 * @return
	 * @throws ServiceException
	 */
	Page findThesisExamResultsVoByCondition(Page objPage, Map<String, Object> condition) throws ServiceException;
	/**
	 * 保存A+B模式毕业论文成绩
	 */
	void saveOrUpdateNetsideStudyThesisResults(HttpServletRequest request) throws ServiceException;
}
