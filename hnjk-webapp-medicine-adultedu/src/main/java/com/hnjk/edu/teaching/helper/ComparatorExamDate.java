package com.hnjk.edu.teaching.helper;

import java.util.Comparator;
import java.util.Date;

import com.hnjk.edu.teaching.model.ExamInfo;
/**
 * 考试时间排序<code>ComparatorExamSetting</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-11-11 上午11:01:43
 * @see 
 * @version 1.0
*/
public class ComparatorExamDate implements Comparator<Date>{

	@Override
	public int compare(Date o1, Date o2) {

		int flag=o1.compareTo(o2);
		
		return flag;
	}

}
