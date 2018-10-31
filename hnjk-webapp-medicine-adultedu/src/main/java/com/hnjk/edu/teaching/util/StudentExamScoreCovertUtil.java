package com.hnjk.edu.teaching.util;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;

public class StudentExamScoreCovertUtil {

	/**
	 * 成绩类型转换成百分制
	 * @param code
	 * @param score
	 * @return
	 */
	public static String  convertScore(String code, String score){
		
		String score_100  = null;
		
		if (code == null || score == null) {
			return "0";
		}
		
		if (code.equals(Constants.COURSE_SCORE_TYPE_TWO)){
			
			if ("2201".equals(score) || "1".equals(score)){
				score_100 = "50";
			}else if ("2212".equals(score) || "2".equals(score)){
				score_100 = "75";
			}
			
		}else if (code.equals(Constants.COURSE_SCORE_TYPE_THREE)) {
			
			if("2301".equals(score) || "1".equals(score)){
				score_100 = "50";
			}else if ("2312".equals(score) || "2".equals(score)){
				score_100 = "75";
			}else if ("2313".equals(score) || "3".equals(score)){
				score_100 = "95";
			}
			
		}else if (code.equals(Constants.COURSE_SCORE_TYPE_FOUR)) {
			
			if("2401".equals(score) || "1".equals(score)){
				score_100 = "50";
			}else if ("2412".equals(score) || "2".equals(score)){
				score_100 = "75";
			}else if ("2413".equals(score) || "3".equals(score)){
				score_100 = "85";
			}else if ("2414".equals(score) || "4".equals(score)){
				score_100 = "95";
			}
			
		}else if (code.equals(Constants.COURSE_SCORE_TYPE_FIVE)){
			
			if("2501".equals(score) || "1".equals(score)){
				score_100 = "50";
			}else if ("2512".equals(score) || "2".equals(score)){
				score_100 = "65";
			}else if ("2513".equals(score) || "3".equals(score)){
				score_100 = "75";
			}else if ("2514".equals(score) || "4".equals(score)){
				score_100 = "85";
			}else if ("2515".equals(score) || "5".equals(score)){
				score_100 = "95";
			}
			
		}else{
			score_100     = score;
		}
		
		return score_100;
	}
	
	/**
	 * 判断一个成绩是否通过
	 * @param code
	 * @param score
	 * @return
	 */
	public static String isPassScore(String code, double score){
		if (ExStringUtils.isBlank(code)) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_TWO) && score < 75) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_THREE)&& score < 60) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_FOUR)&& score < 60) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_FIVE) && score < 65) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_ONEHUNHRED) && score < 60) {
			return "N";
		}
		return "Y";
	}
}
