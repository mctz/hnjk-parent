package com.hnjk.core.foundation;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.UUID;

import javax.crypto.Cipher;

import junit.framework.Assert;

import lombok.Cleanup;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;

/**
 * 数字签名测试.
 * @author hzg
 *
 */
public class DigitalSignatureTest {

	@Test
	public void testUUID() {      
	    UUID uuid = UUID.randomUUID();      
	    System.out.println(uuid.toString().length());      
	 }      
	
	//RSA数字签名
	@Test
	public void testRSASignature() throws Exception{
		
		 String before = "asdf";  
	     byte[] plainText = before.getBytes("ISO_8859-1");  
	  
	        //形成RSA公钥对  
	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  
	        keyGen.initialize(1024);  
	        KeyPair key = keyGen.generateKeyPair();  
	  
	        //使用私钥签名**********************************************************  
	        Signature sig = Signature.getInstance("SHA1WithRSA");  
	        sig.initSign(key.getPrivate());//sig对象得到私钥  
	        //签名对象得到原始数据  
	        sig.update(plainText);//sig对象得到原始数据(现实中用的是原始数据的摘要，摘要的是单向的，即摘要算法后无法解密)  
	        byte[] signature = sig.sign();//sig对象用私钥对原始数据进行签名，签名后得到签名signature  
	        System.out.println(sig.getProvider().getInfo());  
	        //String after1 = new String(signature,"ISO_8859-1");  
	        System.out.println("\n用私钥签名后:"+BaseSecurityCodeUtils.base64Encode(signature));  
	  
	        //使用公钥验证**********************************************************  
	        sig.initVerify(key.getPublic());//sig对象得到公钥  
	        //签名对象得到原始信息  
	        sig.update(plainText);//sig对象得到原始数据(现实中是摘要)  
	        System.out.println("--->"+new String(plainText));
	        try {  
	            if (sig.verify(signature)) {//sig对象用公钥解密签名signature得到原始数据(即摘要)，一致则true  
	                System.out.println("签名验证正确！！");  
	            } else {  
	                System.out.println("签名验证失败！！");  
	            }  
	        } catch (SignatureException e) {  
	            System.out.println("签名验证失败！！");  
	        }  
	}
	
	//test
	@Test
	public void testCertificateCode() throws Exception{
		StringBuffer testStrBf = new StringBuffer();
		testStrBf
		.append("https://www.google.com.hk/").append("#q=Data+must+not+be+longer+than+117+bytes&hl=zh-CN")
		.append("&newwindow=1&safe=strict&prmd=imvns&ei=viMWUOTsJs24iAfU6IHoCA&start=0")
		.append("&sa=N&bav=on.2,or.r_gc.r_pw.r_cp.,cf.osb&fp=56ab6d5f2f4aaf31&biw=1173&bih=614");
		
		byte[] msg =  testStrBf.toString().getBytes("UTF8");
		String enMsg = certificateEncode(msg);
		
		Assert.assertEquals(new String(msg), certificateDecode(enMsg));
				
	}
	
	
	@Test
	public void testDecode() throws Exception{
		String string = "UAu-oX6AD1-x-uISg7IzYX-HkDoULg4CVH2V7QEKf5Gxy9ykTC2Ck-x-e2BOqdy-uaoPmXyq9aD-O0ilBIfNsYDc48rT30dKHTHXtXIFXXJgaJFqpPqHpEuveDBCHtdV-x-DPnEMv8oWXzu5Lm5fgSn1I7Um7dKCbiiyUyWXXQxce9Q4U";
		System.out.println(certificateDecode(string));
	}
	
	@Test
	public void testEncode() throws Exception{
		System.out.println(certificateEncode("test".getBytes()));
	}
	
	//数字证书加密	
	private String certificateEncode(byte[] msg) throws Exception{
		
		  //byte[] msg = "As most of you probably".getBytes("UTF8");     // 待加解密的消息
		  
		  // 用证书的公钥加密
		  CertificateFactory cff = CertificateFactory.getInstance("X.509");
		  @Cleanup FileInputStream fis1 = new FileInputStream("D:\\Marco\\deploy\\tomcat-hnjk\\conf\\myserver.cert"); // 证书文件
		  Certificate cf = cff.generateCertificate(fis1);
		  PublicKey pk1 = cf.getPublicKey();           // 得到证书文件携带的公钥
		  Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");      // 定义算法：RSA
		  c1.init(Cipher.ENCRYPT_MODE, pk1);
		  byte[] encodedByteArray = new byte[] {};
		  for (int i = 0; i < msg.length; i += 117) {
		   byte[] subarray = ArrayUtils.subarray(msg, i, i + 117);
		   byte[] doFinal = c1.doFinal(subarray);
		   encodedByteArray = ArrayUtils.addAll(encodedByteArray, doFinal);
		  }
		  
		 // byte[] msg1 = c1.doFinal(msg);            // 加密后的数据
		  
		  return BaseSecurityCodeUtils.base64Encode(encodedByteArray);	
		  
	}
	
	//数字证书解密
	private String certificateDecode(String enMsg) throws Exception{
		// 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
		 //String enMsg = "iFPuKJGONrSmTn/8k4oq3/VbgyN/x4GCnpqAfodm9uzYTdSZNgRpf+VaTkGMY1FWxxFU36iTeQQY9rSPG+CE7/7m475k1Xa5Kltn0xXAKVOyCdsY/KCWyZVTRq/d+mDiMdeFMyJr9lecC1+a+80pJK3PKgJVVHyUWmNkxwiJEXQ=";
		  @Cleanup FileInputStream fis2 = new FileInputStream("D:\\Marco\\deploy\\tomcat-hnjk\\conf\\keystore-file");
		  KeyStore ks = KeyStore.getInstance("JKS");         // 加载证书库
		  char[] kspwd = "888888".toCharArray();          // 证书库密码
		  char[] keypwd = "888888".toCharArray();          // 证书密码
		  ks.load(fis2, kspwd);              // 加载证书
		  KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)ks.getEntry("tomcat1", new PasswordProtection(keypwd));
		  PrivateKey pk2 = privateKeyEntry.getPrivateKey();
		  //PrivateKey pk2 = (PrivateKey)ks.getKey("tomcat1", keypwd);     // 获取证书私钥
		  Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		  c2.init(Cipher.DECRYPT_MODE, pk2);
		 
		  StringBuilder sb = new StringBuilder();
		  byte[] decodeByteArray = BaseSecurityCodeUtils.base64Decode(enMsg);
		  for (int i = 0; i < decodeByteArray.length; i += 128) {
		   byte[] doFinal = c2.doFinal(ArrayUtils.subarray(decodeByteArray, i, i + 128));
		   sb.append(new String(doFinal));
		  }
		  return sb.toString();
		 // byte[] msg2 = c2.doFinal();            // 解密后的数据
		  // 打印解密字符串 -
		///return new String(msg2,"UTF8");        // 将解密数据转为字符串
		 
		  
	}
	
	
	@Test
	public void testStr()throws Exception{
		String ss = "/opt/hnjk/datas/test/file/ttttt.cvs";
		System.out.println(ss.substring(0,ss.lastIndexOf(".")));
	}
	
	@Test
	public void testCertLength() throws Exception{
		
	}
}
