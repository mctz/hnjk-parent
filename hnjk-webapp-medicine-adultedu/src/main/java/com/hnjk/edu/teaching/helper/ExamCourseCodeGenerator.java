package com.hnjk.edu.teaching.helper;

/**
 * 考试课程编号生成器<code>ComparatorBranchSchoolMajor</code><p>
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-9-15 下午03:21:43
 * @see 
 * @version 1.0
*/
public class ExamCourseCodeGenerator {
	
	private static final ExamCourseCodeGenerator instance = new ExamCourseCodeGenerator();
	
	private ExamCourseCodeGenerator(){};
	
	public static ExamCourseCodeGenerator getInstance(){
		return instance;
	}
	//生成课程编号的前缀 A、B、C、D、E、F......
	public String [] genCodePrefix(int length){
		int ascii   = 65; //字母A的ASCII码
		String [] s = new String[length];
		for (int i  = 0; i < length; i++) {
			char c  = (char)ascii;
			s[i]    = String.valueOf(c) ;
			++ascii;
		}
		return s;
	}
	/**
	 * 生成考试课程编号
	 * @param prefix  编号的前缀
	 * @param suffix  编号的流水号
	 * @param length  生成编号的位数 如:prefix=A,suffix=1,length=3,生成结果为:A001
	 * @return
	 */
	public String genExamCourseCode(String prefix,String suffix,int length){
        int dif = length - suffix.length();
        if (dif < 0){
            return null;
        }
        char[] difChars = new char[dif];
        for (int i = 0; i < difChars.length; i++){
            difChars[i] = '0';
        }
        return (prefix+new String(difChars) + suffix);
	}
	public static void  main(String[] args){
		 ExamCourseCodeGenerator instance = ExamCourseCodeGenerator.getInstance();
		 String [] s = instance.genCodePrefix(3);
		 System.out.println(s.toString());
		 System.out.println(instance.genExamCourseCode(s[0], "33", 3));
	}
}

