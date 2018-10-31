package com.hnjk.edu.teaching.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.vo.NoExamApplyVo;

/**
 * 学生免考申请表.
 * <code>INoExamApplyService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-12-6 上午11:47:57
 * @see 
 * @version 1.0
 */
public interface INoExamApplyService extends IBaseService<NoExamApply>{
	/**
	 * 根据条件查询NoExamApply，返回Page
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findNoExamApplyByCondition(Map<String, Object> condition, Page objPage)throws ServiceException;
	/**
	 * 根据条件查询NoExamApply，返回List
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<NoExamApply> findNoExamApplyByCondition(Map<String, Object> condition)throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids);
	
	/**
	 * 审核
	 * @param ids
	 * @param userid
	 * @param cnName
	 */
	void batchAudit(String[] ids, String userid, String cnName);
	/**
	 * 审核
	 * @param ids
	 * @param userid
	 * @param cnName
	 */
	void audit(String resourceid, String userid, String cnName);
	
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	void batchCancel(String[] ids, String resourceid, String cnName);
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	void cancel(String resourceid, String resourceid2, String cnName);

	/**
	 * 审核
	 * @param ids
	 * @param userid
	 * @param cnName
	 */
	void batchAudit(String[] ids, String userid, String cnName,StringBuffer message);
	/**
	 * 审核
	 * @param ids
	 * @param userid
	 * @param cnName
	 */
	void audit(String resourceid, String userid, String cnName,StringBuffer message,Date checkTime);
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	void batchCancel(String[] ids, String resourceid, String cnName,StringBuffer message);
	/**
	 * 取消审核
	 * @param ids
	 * @param resourceid
	 * @param cnName
	 */
	void cancel(String resourceid, String resourceid2, String cnName,StringBuffer message,Date checkTime);
	/**
	 * 学习中心替学生申请免修免考-保存/更新
	 * @param map
	 * @param courseTypes     
	 * @param noExamIds
	 * @param courseIds
	 * @param memos
	 * @param other_memos
	 * @param unScores
	 * @param scores
	 * @param studentId
	 * @throws Exception
	 */
	void noExamAppSaveByBrSchool(Map<String, Object> map,String[] courseTypes, String[] noExamIds, String[] courseIds,
								 String[] memos,String [] other_memos, String[] unScores,String [] scores, String studentId)throws Exception;
	
	/**
	 * 免修免考导入
	 * @param datas
	 * @param returnMap
	 * @return
	 * @throws Exception 
	 */
	Map<String,Object> noExamApplyImport(List<NoExamApplyVo> datas,Map<String,Object> returnMap) throws Exception;
}
