package com.hnjk.security.sso.client;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.crypto.Cipher;


/**
 * 客户端用来构造ticket的加密方法.
 * <code>SSOClientService</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-26 上午11:00:25
 * @see 
 * @version 1.0
 */
public class SSOClientHelper {	
	
	public static String createTicket(WebTicket webTicket) throws SSOServiceException{		
		return enCodeDigitalSignature(webTicket.toString());
	}
	
	public static String createTicket(String username,String unitcode,String id,String forwardurl)throws SSOServiceException{
		String param = username+"$"+unitcode+"$"+id+"$"+forwardurl;
		try {
			return enCodeDigitalSignature(new String(param.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
		
	private static String enCodeDigitalSignature(String str) throws SSOServiceException{
		
		try{
			byte[] msg = str.getBytes();
			
			CertificateFactory cff = CertificateFactory.getInstance("X.509");	
			InputStream in = SSOClientHelper.class.getResourceAsStream("/com/hnjk/security/sso/client/hnjk-sso-client.cert");
			
			Certificate cf = cff.generateCertificate(in);
			PublicKey pk1 = cf.getPublicKey();           // 得到证书文件携带的公钥
			Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");      // 定义算法：RSA
			c1.init(Cipher.ENCRYPT_MODE, pk1);  
			//解决 加密117字节限制
			byte[] encodedByteArray = new byte[] {};
			for (int i = 0; i < msg.length; i += 100) {//拆成多次加密
			   byte[] subarray = SSOUtils.subarray(msg, i, i + 100);
			   byte[] doFinal = c1.doFinal(subarray);
			   encodedByteArray = SSOUtils.addAll(encodedByteArray, doFinal);
			 }
					
			return SSOUtils.encodeForUrl(encodedByteArray);
		}catch (Exception e) {
			throw new SSOServiceException("证书加密出错："+e.fillInStackTrace());			
		}		  
		 
		
	}
}
