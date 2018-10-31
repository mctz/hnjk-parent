package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.EnrollmentBookInfo;
import com.hnjk.edu.recruit.vo.EnrollmentBookInfoVO;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:13:19 
 * 
 */
public interface IEnrollmentBookInfoService extends IBaseService<EnrollmentBookInfo> {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 是否允许操作
	 * @param yearInfoId
	 * @return
	 */
	Map<String, Object> isAllowOperate(String yearInfoId);
	
	/**
	 * 获取某个年级所有的招生报读学生的身份证号
	 * @param gradeId
	 * @return
	 */
	List<String> findCertNumByGradeId(String gradeId) throws Exception;
	
	/**
	 * 根据身份证号判断该学生是否属于该教学点的招生范围
	 * @param certNum
	 * @param scopeList
	 * @return
	 */
	boolean isInScope(String certNum, List<String> scopeList);
	
	/**
	 * 处理导入招生预约报读信息
	 * 
	 * @param modelList
	 * @param gradeId
	 * @return
	 */
	Map<String, Object> handleEnrollmentBookInfoImport(List<EnrollmentBookInfoVO> modelList,String gradeId);
	
	/**
	 * 需要导出的信息
	 * 
	 * @param condition
	 * @return
	 */
	List<EnrollmentBookInfoVO> findVoByCondition(Map<String, Object> condition)  throws Exception;
}
