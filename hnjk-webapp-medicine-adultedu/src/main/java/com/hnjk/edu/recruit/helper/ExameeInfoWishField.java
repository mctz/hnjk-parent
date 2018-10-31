package com.hnjk.edu.recruit.helper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
/**
 * 
 * <code>ExameeInfoWishField</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-9-6 下午05:46:21
 * @see 
 * @version 1.0
 */
public class ExameeInfoWishField {
	private List<ExameeInfoField> exameeInfoWishList = new ArrayList<ExameeInfoField>();//成绩字段列表
	
	@XmlElement(name = "ExameeInfoField")
	public List<ExameeInfoField> getExameeInfoWishFieldList() {
		return exameeInfoWishList;
	}	
	public void setExameeInfoWishFieldList(List<ExameeInfoField> exameeInfoWishList) {
		this.exameeInfoWishList = exameeInfoWishList;
	}	
}
