package com.hnjk.security.sso.client;


/**
 * 用于web验证的票据.
 * <code>WebTicket</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-26 上午10:55:30
 * @see 
 * @version 1.0
 */
public class WebTicket implements java.io.Serializable {

	private static final long serialVersionUID = -7358859683077306203L;

	public final static String ASYN_MODE = "asyn";//同步模式
	public final static String NOASYN_MODE = "noasyn";//异步模式

	private String username;//用户名
	
	private String unitCode;//组织代码
	
	private Long id = System.currentTimeMillis();//ID

	private String forwardUrl = "";
	
	private String asynMode = ASYN_MODE;//是否为异步模式，使用异步模式，只返回验证结果，而不跳转页面.

	
	public WebTicket(){}
	
	public WebTicket(String username,String unitCode){
		this.username = username;
		this.unitCode = unitCode;	
	}
	
	public WebTicket(String username,String unitCode,String forwardUrl){
		this.username = username;
		this.unitCode = unitCode;
		this.forwardUrl = forwardUrl;
	}
	
	public WebTicket(String username,String unitCode,String forwardUrl,String asynMode){
		this.username = username;
		this.unitCode = unitCode;
		this.forwardUrl = forwardUrl;
		this.asynMode = asynMode;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return (null != username && !"".equals(username)) ? username : "";
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return (null != unitCode && !"".equals(unitCode)) ? unitCode : "";
	}

	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the forwardUrl
	 */
	public String getForwardUrl() {
		return forwardUrl;
	}

	/**
	 * @param forwardUrl the forwardUrl to set
	 */
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	/**
	 * @return the asynMode
	 */
	public String getAsynMode() {
		return asynMode;
	}

	/**
	 * @param asynMode the asynMode to set
	 */
	public void setAsynMode(String asynMode) {
		this.asynMode = asynMode;
	}
	

	@Override
	public String toString() {		
		return getUsername()+"$"+getUnitCode()+"$"+getId()+"$"+getForwardUrl()+"$"+getAsynMode();
	}

	
	
}
