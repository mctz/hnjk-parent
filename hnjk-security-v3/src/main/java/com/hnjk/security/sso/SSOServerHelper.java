package com.hnjk.security.sso;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.security.sso.client.SSOServiceException;
import com.hnjk.security.sso.client.SSOUtils;
import com.hnjk.security.sso.client.WebTicket;
import lombok.Cleanup;


/**
 * 服务器端解析URL参数
 * <code>SSOServerService</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-26 下午03:06:09
 * @see 
 * @version 1.0
 */
public class SSOServerHelper {

	public static WebTicket parseTicket(String ticket) throws SSOServiceException{		
		
		ticket = deCodeDigitalSignature(ticket);
		
		String[] tickets = ticket.split("\\$");
		
		WebTicket t = new WebTicket();
		t.setUsername(tickets[0]);
		t.setUnitCode(tickets[1]);
		try{
			if(null != tickets[3]) {
				t.setForwardUrl(tickets[3]);
			}
			if(null != tickets[4]) {
				t.setAsynMode(tickets[4]);
			}
			
		}catch (Exception e) {
		//nothing
		}
		
		return t;
	}
	
	private static String deCodeDigitalSignature(String str) throws SSOServiceException{		
		try {			
			 @Cleanup FileInputStream fis2 = new FileInputStream(ConfigPropertyUtil.getInstance().getProperty("web.security.sso.keystore.path"));
			 KeyStore ks = KeyStore.getInstance("JKS");         // 加载证书库
			 char[] kspwd = ConfigPropertyUtil.getInstance().getProperty("web.security.sso.keystore.keypass").toCharArray();          // 证书库密码
			 char[] keypwd = ConfigPropertyUtil.getInstance().getProperty("web.security.sso.keystore.pass").toCharArray();          // 证书密码
			 ks.load(fis2, kspwd);              // 加载证书
			 KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)ks.getEntry(ConfigPropertyUtil.getInstance().getProperty("web.security.sso.keystore.alias"), new PasswordProtection(keypwd));
			 PrivateKey pk2 = privateKeyEntry.getPrivateKey();
			 //PrivateKey pk2 = (PrivateKey)ks.getKey("tomcat1", keypwd);     // 获取证书私钥
			 Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			 c2.init(Cipher.DECRYPT_MODE, pk2);
			 //解决解密128字节限制
			  StringBuilder sb = new StringBuilder();
			  byte[] decodeByteArray = SSOUtils.decodeForUrl(str);
			  for (int i = 0; i < decodeByteArray.length; i += 128) {//分成多次解密
			   byte[] doFinal = c2.doFinal(SSOUtils.subarray(decodeByteArray, i, i + 128));
			   sb.append(new String(doFinal));
			  }
			// byte[] msg2 = c2.doFinal();   
			return sb.toString();			
		} catch (Exception e) {
			throw new SSOServiceException("证书解密出错："+e.fillInStackTrace());
		}
		
	}	
}
