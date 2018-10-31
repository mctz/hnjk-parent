package com.hnjk.core.exception;
/**
 * 
 * <code>SetFirstTermCourseForStuRegException</code>
 * 注册学籍时预约第一学期课程，为了保持两个业务的事务独立开而设置的一个异常类<p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-31 上午09:34:30
 * @see 
 * @version 1.0
 */
public class SetFirstTermCourseForStuRegException extends RuntimeException{

	private static final long serialVersionUID = 1418113092686210941L;
	
	public SetFirstTermCourseForStuRegException(String msg){
		super(msg);
	}
	public SetFirstTermCourseForStuRegException(Throwable t){
		super(t);
		setStackTrace(t.getStackTrace());
	}
	public SetFirstTermCourseForStuRegException(String msg,Throwable t){
		super(msg,t);
		setStackTrace(t.getStackTrace());
	}
}
