package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.edu.roll.vo.GraduationInfoExportVo;
import com.hnjk.edu.roll.vo.StudentInfoVo;

public interface IStudentGDAuditJDBCService {
	/**
	 * 查询毕业审核结果
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getGraduateAuditResults(Map <String,Object> condition);
	/**
	 * 查询学位审核结果
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getDegreeAuditResults(Map<String, Object> condition);
	/**
	 * 获得所有存在学位毕业审核记录的学生
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getStudentIdInAudit(Map<String, Object> condition);
	/**
	 * 
	 * @param condition
	 * @return
	 */
	public List<GraduationInfoExportVo> getGraduateAuditResults_new(Map <String,Object> condition);
}
