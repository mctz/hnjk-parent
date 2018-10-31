package com.hnjk.security.sso.client;


/**
 * 单点异常.
 * <code>SSOServiceException</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-26 上午11:05:48
 * @see 
 * @version 1.0
 */
public class SSOServiceException extends RuntimeException {

	private static final long serialVersionUID = 8540997001723151304L;

	public SSOServiceException(String msg){
		super(msg);
	}
	
	public SSOServiceException(String msg,Throwable throwable){
		super(msg,throwable);
	}
}
