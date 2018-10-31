package com.hnjk.edu.teaching.helper;

import java.util.Comparator;

import com.hnjk.edu.teaching.model.ExamSetting;
/**
 * 考试计划设置表排序<code>ComparatorExamSetting</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-9-15 下午03:21:43
 * @see 
 * @version 1.0
*/
public class ComparatorExamSetting implements Comparator<ExamSetting>{

	@Override
	public int compare(ExamSetting o1, ExamSetting o2) {
		int flag=o1.getExamDate().compareTo(o2.getExamDate());
		if (0==flag) {
			return o1.getTimeSegment().compareTo(o2.getTimeSegment());
		}else {
			return flag;
		}
	}
}