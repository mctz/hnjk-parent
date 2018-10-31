package com.hnjk.core.support.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.template.FreemarkBuildHelper;
import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 提供一个基于<code>JavaMail</code>邮件发送服务. <p>
 * 邮件主机的信息需要在<code>applicationContext-email.xml</code>中配置.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： Jul 6, 2009 10:27:46 PM
 * @see 
 * @version 1.0
 */
@Service("emailService")
public class MailServiceImpl implements IMailService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**邮件发送者*/
	private final static String EMAIL_FROM = "no-reply@greenhome.com";
	
	private final static String EMAIL_FOOTER = "";
	
	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender mailSender;
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/* (non-Javadoc)
	 * @see com.gdcn.core.email.IMailService#sendTextMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendTextMail(String from, String to, String subject, String content) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(content);

		try {			
			mailSender.send(msg);
			logger.info("本邮件已发送至{}", StringUtils.arrayToCommaDelimitedString(msg.getTo()));
		} catch (MailException e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.gdcn.core.email.IMailService#sendRichMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendRichMail(String toUserName, String to, String cc, String subject, String context, String attachPath) {
		MimeMessage msg = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(to);
			helper.setFrom(EMAIL_FROM);
			if(!ExStringUtils.isEmpty(cc)){
				helper.setCc(cc);
			}
			helper.setSubject(subject);
			//根据模板构建邮件内容
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", toUserName);
			map.put("content",context);
			map.put("footer", EMAIL_FOOTER);
			String content = FreemarkBuildHelper.buildDefaultTemplate("mail.ftl",map);
			helper.setText(content,true);
			//如果有附件，则添加附件
			if(!ExStringUtils.isEmpty(attachPath)){
				addAttachment(attachPath,helper);
			}
			
			mailSender.send(msg);
			logger.info("HTML版邮件已发送至{}" ,to);
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (MailException e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	
	/**
	 * 添加邮件附件
	 * @param attachPath 附件路径，为服务器上的全路径
	 * @param helper
	 * @throws MessagingException
	 */
	private void addAttachment(String attachPath,MimeMessageHelper helper) throws MessagingException {
		try {
			File file = new File(attachPath);
			if(!file.exists()){
				throw new ServiceException("文件不存在！");
			}
			helper.addAttachment("mailAttachment.txt", file);		
		} catch (Exception e) {
			logger.error("构造邮件失败,附件文件不存在", e);
			throw new MessagingException("附件文件不存在");
		}
	}
	
}
