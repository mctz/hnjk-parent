package com.hnjk.edu.teaching.util;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.edu.basedata.model.Grade;

/**
 * 
 * <code>学生预约学习的工具类</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-07-16  23:20:06
 * @see 
 * @version 1.0
 */
public class StudentCourseOrderUtil {
	
	private final static String TERM_1 = "秋";

	private final static String TERM_2 = "春";
	
	/**
	 * 获取传入年级的年份
	 * @param grade
	 * @return
	 */
	public static Long getGradeYear(Grade grade){
		Long year    = 1000L;
		try {
			String term = null==grade?"1":grade.getTerm();
			if ("2".equals(term)) {
				year = grade.getYearInfo().getFirstYear()+1;
			}else {
				year = grade.getYearInfo().getFirstYear();
			}
		} catch (Exception e) {
			e.fillInStackTrace();
			year	 = 1000L;
		}
		
		return year;
	}
	
	/**
	 * 算出学生在当前年级中可以预约哪个学期的课程
	 * 
	 * @param defaultGrade      默认年级的
	 * @param defaultGrade      学生所属年级   
	 * @return
	 */
	public static String getAllowOrderCourseTerm(Grade defaultGrade,Grade studentGrade){
		
		String allowTerm    	= "1";
		int balance         	= 1; 
		
		try {
			Long defaultYear    = getGradeYear(defaultGrade); //默认年级的 年份
			Long stuGradeYear   = getGradeYear(studentGrade); //学生所属年级的年份
		
			String defaultTerm  = defaultGrade.getTerm();     //默认年级的学期
			String stuGradeTerm = studentGrade.getTerm();     //学生所属年级的学期
			
			//如果默认年级跟学生年级的学期相同，则允许的学期是 （默认年级年份-学生年级年份）* 2 + 1
			if (stuGradeTerm.equals(defaultTerm)) {
				balance         = 1;
			}
			//如果默认年级的学期为1，学生年级的学期为2，则允许的学期是 （默认年级年份-学生年级年份）* 2 + 2
			if ("1".equals(defaultTerm)&&"2".equals(stuGradeTerm)) {
				balance         = 2;
			}
			//如果默认年级的学期为2，学生年级的学期为1，则允许的学期是 （默认年级年份-学生年级年份）* 2 + 0
			if ("2".equals(defaultTerm)&&"1".equals(stuGradeTerm)) {
				balance         = 0;
			}
			
			allowTerm           = String.valueOf((defaultYear-stuGradeYear)*2+balance);

		} catch (Exception e) {
			e.fillInStackTrace();
		}
		
		return allowTerm;
	}
	/**
	 * 获取当前年级减去term学期的年级名称
	 * @param minusTerm  当前年级的前N个学期
	 * @return
	 */
	public static String getSomeTermsAgoGradeName(int minusTerm){
		
		long currentYear     = ExDateUtils.getCurrentYear(); //系统时间当前年份
		long currentMonth    = ExDateUtils.getCurrentMonth();//系统时间当前月份
		String gradeTerm     = currentMonth>7?TERM_1:TERM_2; //返回的年级所属的学期
		String gradeName     = "";                           //返回的年级名称
		int cutNum  		 = 0;
		
		try {
			
			if(minusTerm<1) {
				gradeName    = String.valueOf(currentYear)+gradeTerm;
			}else if(minusTerm == 1 && TERM_1.equals(gradeTerm)) {
				gradeName    = String.valueOf(currentYear)+TERM_2;
			}else if(minusTerm == 1 && TERM_2.equals(gradeTerm)){
				gradeName    = String.valueOf(currentYear-minusTerm)+TERM_1;
			}else if(minusTerm%2>0){
				cutNum  	 = minusTerm%2;
				gradeName    = String.valueOf(currentYear-cutNum)+gradeTerm;
			}else{
				cutNum   	 = minusTerm/2-1;
				gradeTerm    = currentMonth>7?TERM_2:TERM_1;
				gradeName    = String.valueOf(currentYear-cutNum)+gradeTerm;
			}
			
		} catch (Exception e) {
			gradeName        = String.valueOf(currentYear)+gradeTerm;
		}
		
		return gradeName;
	}
	/**
	 * 获取默认年级减去term学期的年级名称
	 * @param curGrade	      默认年级
	 * @param minusTerm   当前年级的前N个学期
	 * @return
	 */
	public static String getSomeTermsAgoGradeNameByCurGrade(Grade curGrade,int minusTerm){
		long currentYear     = "1".equals(curGrade.getTerm()) ?curGrade.getYearInfo().getFirstYear():(curGrade.getYearInfo().getFirstYear()+1);
		String gradeTerm     = "1".equals(curGrade.getTerm()) ?TERM_1:TERM_2; //返回的年级所属的学期
		String gradeName     = "";                           //返回的年级名称
		int cutNum  		 = 0;
		/**
		 *  
			3/2:1
			4/2:2
			5/2:2
			6/2:3
			
			3%2:1
			4%2:0
			5%2:1
			6%2:0
		 */
		try {
			
			if(minusTerm<1) {
				gradeName    = String.valueOf(currentYear)+gradeTerm;
			}else if(minusTerm == 1 && TERM_1.equals(gradeTerm)) {
				gradeName    = String.valueOf(currentYear)+TERM_2;
			}else if(minusTerm == 1 && TERM_2.equals(gradeTerm)){
				gradeName    = String.valueOf(currentYear-minusTerm)+TERM_1;
			}else if(minusTerm%2>0){
				cutNum  	 = minusTerm%2;
				gradeName    = String.valueOf(currentYear-cutNum)+gradeTerm;
			}else{
				if("1".equals(curGrade.getTerm())){
					cutNum=minusTerm/2-1;
				}else{
					cutNum=minusTerm/2;
				}
				gradeName    = String.valueOf(currentYear-cutNum)+gradeTerm;
			}
			
		} catch (Exception e) {
			gradeName        = String.valueOf(currentYear)+gradeTerm;
		}
		
		return gradeName;
	}
}
