package com.hnjk.edu.teaching.helper;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

import com.hnjk.edu.teaching.vo.ExamSeatAssignExamCourseVo;
/**
 * 座位按排查询列表排序-试室排序<code>ComparatorExamSetting</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-05-21 上午12:01:43
 * @see 
 * @version 1.0
*/
public class ComparatorExamSeatAssigendForExamRoom implements Comparator<Map<String,Object>>{

	@Override
	public int compare(Map<String,Object> o1, Map<String,Object> o2) {
		
		Long doubleseatnum_o1 = null==o1.get("doubleseatnum")? 0L :((BigDecimal)o1.get("doubleseatnum")).longValue();
		Long assigned_o1 	  = null==o1.get("assigned")? 0L :((BigDecimal)o1.get("assigned")).longValue();
		Long freeSeatNum_o1   = doubleseatnum_o1-assigned_o1;
		
		Long doubleseatnum_o2 = null==o2.get("doubleseatnum")? 0L :((BigDecimal)o2.get("doubleseatnum")).longValue();
		Long assigned_o2 	  = null==o2.get("assigned")? 0L :((BigDecimal)o2.get("assigned")).longValue();
		Long freeSeatNum_o2   = doubleseatnum_o2-assigned_o2;
		
		return freeSeatNum_o2.compareTo(freeSeatNum_o1);
	}

}
