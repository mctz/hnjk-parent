package com.hnjk.core.foundation.utils;


import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AesUtils {    
	public static final int ENCRYPT = 0;
	public static final int DECRYPT = 1;

    // iv同C语言中iv
    private static byte[] ivBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

    // keyBytes同C语言中key
    private static byte[] keyBytes = new byte[]{(byte) 0xf4, 0x3d, (byte) 0xeb,
            0x10, 0x15, (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73,
            (byte) 0xae, (byte) 0xf0, (byte) 0x85, 0x7d, 0x77,
            0x1f, (byte) 0x81, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d,
            (byte) 0x98, 0x10, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf,
            0x60};

    /**
     * 加密
     * 
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return
     */
    public static byte[] encrypt(byte[] content) {
        return docrypt(content, keyBytes, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * 
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content) {
        return docrypt(content, keyBytes, Cipher.DECRYPT_MODE);
    }

    public static byte[] docrypt(byte[] content, byte[] keyBytes, int mode) {
        try {
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(keyBytes));
            // SecretKey secretKey = kgen.generateKey();
            // byte[] enCodeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES"); // keyBytes32个字节，256位，
                                                                    // 与C语言中的key一致
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(mode, key, iv);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


	/**
	 * 将16进制转换为二进制(服务端)
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStr2Bytes(String hexStr) {
		if (hexStr.length() < 1) {
            return null;
        }
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
    public static String bytes2HexStr(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if (((int) data[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
                buf.append("0");
            }
            buf.append(Long.toHexString((int) data[i] & 0xff)); /* 转换16进制,下方法同 */
        }
        return buf.toString();
    }

    private static BASE64Encoder encoder = new BASE64Encoder();  
    private static BASE64Decoder decoder = new BASE64Decoder();  
    /** 
     * BASE64 编码 
     *  
     * @param s 
     * @return 
     */  
    public static String encodeBufferBase64(byte[] buff)  
    {  
        return buff == null?null:encoder.encodeBuffer(buff).trim();  
    }  
      
      
    /** 
     * BASE64解码 
     *  
     * @param s 
     * @return 
     */  
    public static byte[] decodeBufferBase64(String s)  
    {  
        try  
        {  
            return s == null ? null : decoder.decodeBuffer(s);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }   
        return null;  
    }  
      
  
    /** 
     * base64编码 
     *  
     * @param bytes 
     *            字符数组 
     * @return 
     * @throws IOException 
     */  
    public static String encodeBytes(byte[] bytes) throws IOException  
    {  
        return new BASE64Encoder().encode(bytes).replace("\n", "").replace("\r", "");  
    }  
  
    /** 
     * base64解码 
     *  
     * @param bytes 
     *            字符数组 
     * @return 
     * @throws IOException 
     */  
    public static String decodeBytes(byte[] bytes) throws IOException  
    {  
        return new String(new BASE64Decoder().decodeBuffer(new String(bytes)));  
    }  
    
    /**
     * 使用AES加密（UTF-8）
     * @param str
     * @param key
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * 解密AES数据（UTF-8）
     * @param str
     * @param key
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = new BASE64Decoder().decodeBuffer(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }
 
}
