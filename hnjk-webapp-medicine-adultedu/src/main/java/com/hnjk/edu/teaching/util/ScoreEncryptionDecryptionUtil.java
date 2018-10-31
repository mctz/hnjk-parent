package com.hnjk.edu.teaching.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.utils.ExStringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * <code>学生成绩加密解密工具类</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-8 上午11:32:56
 * @see 
 * @version 1.0
 */
public class ScoreEncryptionDecryptionUtil {
	
	private static final String ALGORITHM = "DES";
	private static ScoreEncryptionDecryptionUtil instance;
	private Logger logger= LoggerFactory.getLogger(getClass());
	
	private ScoreEncryptionDecryptionUtil(){
		
	}
	public static ScoreEncryptionDecryptionUtil getInstance() {
		
		if (null==instance) {
			instance = new ScoreEncryptionDecryptionUtil();
		}
		return instance;
	}
	/**
	 * 成绩解密
	 * @param studentInfoId      学籍ID
	 * @param scoreStr           密文
	 * @return
	 * @throws Exception 
	 */
	public String decrypt(String studentInfoId,String scoreStr) {
		
		try {
			
			if (validate(scoreStr)){
		        return "";
		    }
			
			String keystr       = studentInfoId;
	        Cipher c            = generateCipher(Cipher.DECRYPT_MODE, keystr);
	        BASE64Decoder d     = new BASE64Decoder();
	        byte[] encrypt_byte = c.doFinal(d.decodeBuffer(scoreStr));
	       
	        return new String(encrypt_byte, "GBK");
	        
		} catch (Exception e) {
			logger.error("成绩解密出错,学籍ID："+studentInfoId+"{"+scoreStr+"}",e.fillInStackTrace());
			return "";
		}
		
	}
	/**
	 * 成绩加密
	 * @param studentInfoId      学籍ID
	 * @param scoreStr           明文
	 * @return
	 * @throws Exception 
	 */
	public String encrypt(String studentInfoId,String scoreStr) {
		
		try {
			if (validate(scoreStr)){
	            return scoreStr;
	        }        
			
	        String keystr       = studentInfoId;
	        Cipher c            = generateCipher(Cipher.ENCRYPT_MODE, keystr);
	        byte[] encrypt_byte = c.doFinal(scoreStr.getBytes());
	        BASE64Encoder e     = new BASE64Encoder();
	        String encrypt_str  = e.encode(encrypt_byte);
	        
	        return encrypt_str;
	        
		} catch (Exception e) {
			
			logger.error("成绩加密出错,学籍ID："+studentInfoId+"{}",e.fillInStackTrace());
			return "";
		}
	}
	
	/**
	 * 验证成绩是否为空
	 * @param scoreStr
	 * @return
	 */
	private boolean validate(String scoreStr){
		return ExStringUtils.isBlank(scoreStr);
	}
	
	/**
	 * 生成为加密和解密提供密码功能的Cipher类
	 * @param encrypt_mode       Cipher模式 加密 解密 密钥包装 解包
	 * @param keyStr             Key 生成密钥的字符串
	 * @return
	 * @throws Exception
	 */
	private Cipher generateCipher(int encrypt_mode, String keyStr) throws Exception{
		Key deskey    = generateSecretKey(keyStr);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(encrypt_mode, deskey);
        return cipher;
	}
	
	/**
	 * 生成密钥
	 * @param keyStr  生成密钥的字符串
	 * @return
	 * @throws Exception
	 */
	private Key generateSecretKey(String keyStr) throws Exception{
		
        Key key        = null;
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        key            = keyFactory.generateSecret(dks);
        return key;
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().encrypt("4a4092b15946a4630159aa2cfac87f09", "101"));
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().encrypt("4a4092b15946a4630159aa2cfac87f09", "60"));
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().encrypt("4a4092b15946a4630159aa2cfac87f09", "60.0"));
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().encrypt("4a4092b15946a4630159aa2cfac87f09", "0"));
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().encrypt("4a4092b15946a4630159aa2cfac87f09", "11111"));
		System.out.println(ScoreEncryptionDecryptionUtil.getInstance().decrypt("4a4092b15946a4630159aa2cfac87f09", "3PL54JDfEng="));
	}
}
