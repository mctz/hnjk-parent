package com.hnjk.edu.finance.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddPaymentUtil {
	
	

	public static String createSignString(String[] values)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		StringBuffer buf = new StringBuffer(512);
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (value == null) {
				continue;
			}
			buf.append(value);
		}
		byte[] bufTemp = buf.toString().getBytes("UTF-8");
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(bufTemp);
		byte[] md5Result = mdTemp.digest();
		return byte2String(md5Result);
	}

	public static String byte2String(byte[] buf) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			if ((buf[i] & 0xff) < 0x10) {
				result.append('0');
			}
			result.append((Integer.toHexString(buf[i] & 0xff)));
		}
		return result.toString();
	}
	public static void main(String[] args){
		try {
//			addPayment();
//			System.out.println(Integer.valueOf("00"));
//			http://[校园支付通域名]/pay/dealPay.html?pwd=[支付码]
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
