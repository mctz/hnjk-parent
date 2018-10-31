package com.hnjk.edu.recruit.helper;

import java.util.Comparator;

import com.hnjk.edu.recruit.model.RecruitMajor;
/**
 * 招生专业排序<code>ComparatorBranchSchoolMajor</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午02:39:43
 * @see 
 * @version 1.0
*/
public class ComparatorRecruitMajor implements Comparator<RecruitMajor>{

	@Override
	public int compare(RecruitMajor o1, RecruitMajor o2) {
		int flag=o1.getMajor().getMajorName().compareTo(o2.getMajor().getMajorName());
		if (0==flag) {
			return o1.getClassic().getResourceid().compareTo(o2.getClassic().getResourceid());
		}else {
			return flag;
		}
	
	}

}
