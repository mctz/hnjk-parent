package com.hnjk.edu.recruit.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 考生信息导入配置(字段检查条件及映射字段).
 * <code>ExameeInfoConfig</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-9-6 下午05:31:34
 * @see 
 * @version 1.0
 */
@XmlRootElement(name="ExameeInfoConfig")
public class ExameeInfoConfig {
	private List<ExameeInfoField> fieldList = new ArrayList<ExameeInfoField>();//字段集合
	private ExameeInfoScoreFileld exameeInfoScoreField;//成绩字段
	private ExameeInfoWishField exameeInfoWishField;//志愿信息字段
		
	@XmlElement(name = "ExameeInfoField")
	public List<ExameeInfoField> getFieldList() {
		return fieldList;
	}	
	public void setFieldList(List<ExameeInfoField> fieldList) {
		this.fieldList = fieldList;
	}
	@XmlElement(name = "ExameeInfoScoreFileld")
	public ExameeInfoScoreFileld getExameeInfoScoreField() {
		return exameeInfoScoreField;
	}	
	public void setExameeInfoScoreField(ExameeInfoScoreFileld exameeInfoScoreField) {
		this.exameeInfoScoreField = exameeInfoScoreField;
	}
	@XmlElement(name = "ExameeInfoWishFileld")
	public ExameeInfoWishField getExameeInfoWishField() {
		return exameeInfoWishField;
	}
	public void setExameeInfoWishField(ExameeInfoWishField exameeInfoWishField) {
		this.exameeInfoWishField = exameeInfoWishField;
	}

	/**
	 * 获取导入字段配置
	 * @return
	 */
	public Map<String, ExameeInfoField> getFieldListToMap(){
		Map<String, ExameeInfoField> map = new LinkedHashMap<String, ExameeInfoField>();
		for (ExameeInfoField field : getFieldList()) {
			map.put(field.getName(), field);
		}
		return map;
	}
	/**
	 * 获取成绩字段配置
	 * @return
	 */
	public Map<String, ExameeInfoField> getExameeInfoScoreFieldMap(){
		Map<String, ExameeInfoField> map = new LinkedHashMap<String, ExameeInfoField>();
		for (ExameeInfoField field : exameeInfoScoreField.getExameeInfoScoreList()) {
			map.put(field.getName(), field);
		}
		return map;
	}
	/**
	 * 获取志愿字段配置
	 * @return
	 */
	public Map<String, ExameeInfoField> getExameeInfoWishFieldMap(){
		Map<String, ExameeInfoField> map = new LinkedHashMap<String, ExameeInfoField>();
		for (ExameeInfoField field : exameeInfoWishField.getExameeInfoWishFieldList()) {
			map.put(field.getName(), field);
		}
		return map;
	}
	public Map<String, ExameeInfoField> getAllFieldListToMap(){
		Map<String, ExameeInfoField> map = new LinkedHashMap<String, ExameeInfoField>();
		map.putAll(getFieldListToMap());
		map.putAll(getExameeInfoScoreFieldMap());
		map.putAll(getExameeInfoWishFieldMap());
		return map;
	}
}
