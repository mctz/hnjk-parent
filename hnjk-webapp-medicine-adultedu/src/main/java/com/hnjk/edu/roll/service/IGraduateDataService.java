package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.StudentMakeupList;

/**
 * 毕业生数据表
 * <code>IGraduateDataService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:07:17
 * @see 
 * @version 1.0
 */
public interface IGraduateDataService extends IBaseService<GraduateData>{

	Page findGraduateDataByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] ids);
	
	boolean isExist(String stuNum);
	
	List<GraduateData> findByHql(Map<String,Object> condition);
	
	GraduateData findByHql(StudentInfo studentInfo);
	
	StudentMakeupList findByHql1(StudentInfo studentInfo);

	StudentMakeupList findByHql2(String studentid);
	
	/**
	 * 根据学生ID获取毕业数据
	 * @param studentId
	 * @return
	 */
	GraduateData findByStudentId(String studentId);
	
	/**
	 * 设置学位申请状态
	 * 
	 * @param resourceids
	 * @param degreeApplyStatus
	 */
	void setDegreeApplyStatus(String resourceids,String degreeApplyStatus) throws Exception;
	
	/**
	 * 批量修改毕业生的学位申请状态
	 * @param graduateDataIds
	 * @return
	 */
	int batchUpdateApplyStatus(String graduateDataIds) throws Exception;
}
