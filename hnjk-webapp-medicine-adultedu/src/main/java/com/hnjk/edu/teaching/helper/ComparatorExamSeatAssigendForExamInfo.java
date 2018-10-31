package com.hnjk.edu.teaching.helper;

import java.util.Comparator;

import com.hnjk.edu.teaching.vo.ExamSeatAssignExamCourseVo;
/**
 * 座位按排查询列表排序-考试课程排序<code>ComparatorExamSetting</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-05-21 上午12:01:43
 * @see 
 * @version 1.0
*/
public class ComparatorExamSeatAssigendForExamInfo implements Comparator<ExamSeatAssignExamCourseVo>{

	@Override
	public int compare(ExamSeatAssignExamCourseVo o1, ExamSeatAssignExamCourseVo o2) {
		Long a1 = o1.getTOTALNUM()-o1.getASSIGEND();
		Long a2 = o2.getTOTALNUM()-o2.getASSIGEND();

		return a2.compareTo(a1);
	}

}
