package com.hnjk.edu.recruit.helper;

import java.util.Comparator;

import com.hnjk.edu.recruit.model.BranchSchoolMajor;

/**
 * 校外学习中心招生专业排序<code>ComparatorBranchSchoolMajor</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午02:39:43
 * @see 
 * @version 1.0
*/
public class ComparatorBranchSchoolMajor implements Comparator<BranchSchoolMajor>{

	@Override
	public int compare(BranchSchoolMajor o1, BranchSchoolMajor o2) {
		int flag=o1.getRecruitMajor().getTeachingType().compareTo(o2.getRecruitMajor().getTeachingType());
		if (0==flag) {
			return o1.getRecruitMajor().getClassic().getResourceid().compareTo(o2.getRecruitMajor().getClassic().getResourceid());
		}else {
			return flag;
		}
	
	}

}
