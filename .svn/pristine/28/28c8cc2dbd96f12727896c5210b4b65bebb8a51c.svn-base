package com.hnjk.core.exception;

/**
 *  <code>ServiceException</code>业务层异常.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-1-4 下午04:01:58
 * @see 
 * @version 1.0
 */
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = -4009582694957479032L;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable t) {
		super(t);
		setStackTrace(t.getStackTrace());
	}

	public ServiceException(String msg, Throwable t) {
		super(msg, t);
		setStackTrace(t.getStackTrace());
	}
}
