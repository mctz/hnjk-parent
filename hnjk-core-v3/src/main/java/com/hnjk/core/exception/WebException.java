package com.hnjk.core.exception;

/**
 *  <code>WebException</code>抛出web层异常.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-1-4 下午04:02:56
 * @see 
 * @version 1.0
 */
public class WebException extends RuntimeException{

	private static final long serialVersionUID = 7634736969218333842L;

	public WebException(String msg) {
		super(msg);
	}

	public WebException(Throwable t) {
		super(t);
		setStackTrace(t.getStackTrace());
	}

	public WebException(String msg, Throwable t) {
		super(msg, t);
		setStackTrace(t.getStackTrace());
	}

}
