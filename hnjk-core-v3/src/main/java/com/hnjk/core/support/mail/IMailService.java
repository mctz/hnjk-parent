package com.hnjk.core.support.mail;

/**
 * 基于JavaMail的邮件发送接口. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： Jul 7, 2009 11:54:41 AM
 * @see 
 * @version 1.0
 */
public interface IMailService {

	/**
	 * 发送简易文本邮件
	 * @param from
	 * @param to 
	 * @param subject
	 * @param content
	 */
	public abstract void sendTextMail(String from, String to, String subject,String content);

	/**
	 * 发送MIME邮件
	 * @param toUserName 发送对象
	 * @param to
	 * @param cc
	 * @param subject
	 * @param context
	 * @param attachPath
	 */
	public abstract void sendRichMail(String toUserName, String to, String cc,String subject, String context, String attachPath);

}