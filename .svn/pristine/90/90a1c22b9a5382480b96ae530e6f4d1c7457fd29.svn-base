package com.hnjk.core.foundation.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES加解密工具
 * @author Zik
 *
 */
public class DesEncryptAndDecryptUtils {

	private final static String DES = "DES";
	
	/**
     * 根据键值进行加密
     * @param data 待加密字符串
     * @param key 加密私钥，长度不能够小于8位
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] encryptByte = encrypt(data.getBytes(), key.getBytes());
        String encryptStr = new BASE64Encoder().encode(encryptByte);
        return encryptStr;
    }
 
    /**
     * 根据键值进行解密
     * @param data 待解密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException, Exception {
        if (data == null){
        	return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bufByte = decoder.decodeBuffer(data);
        byte[] decryptByte = decrypt(bufByte,key.getBytes());
        return new String(decryptByte);
    }
 
    /**
     * 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
    	Cipher cipher = initCipher(data, key, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }
     
     
    /**
     * 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
    	Cipher cipher = initCipher(data, key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }
    
    /**
     *  初始化Cipher对象
     * @param data 
     * @param key 加密键byte数组
     * @param cipherValue
     * @return
     * @throws Exception
     */
    private static Cipher initCipher(byte[] data, byte[] key,int cipherValue) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        
        // Cipher对象实际完成加密或解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(cipherValue, securekey, sr);
        
        return cipher;
    }
    
    public static void main(String[] args) throws Exception{
    	String data = "123456";
    	String key = "free!@#$%";
    	String encryData = DesEncryptAndDecryptUtils.encrypt(data, key);
    	System.out.println("--加密--"+encryData);
    	String decryptData = DesEncryptAndDecryptUtils.decrypt(encryData,  key);
    	System.out.println("--解密--"+decryptData);
	}
}
