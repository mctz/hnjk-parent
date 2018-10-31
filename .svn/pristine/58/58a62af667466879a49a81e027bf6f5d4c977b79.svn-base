package com.hnjk.edu.util;

import com.hnjk.core.foundation.utils.AesUtils;
import com.hnjk.core.foundation.utils.Base64;

public class Test {

	static {
//		System.out.println(System.getProperty("java.library.path"));
//		System.loadLibrary("dll/XgdProxy");
	}
	
	public native  int PosProxyInit2();
	public native  int PosProxyDoTrade2(int handle, String strin, String strout);
	public native  int PosProxyUninit2(int handle);
	
	/*public void test(){
		System.out.println("-------");
	}*/
	
	public static void main(String[] args) {
		String aa = "d0ea7c6cc2842ba04c536602ec16b5bf836e045d2bdd6ebd6977b4a57969e4d996fd89dae9762a4029981b84ed37d4bcb4861f617f87d25d8a804e4d80e35a4e6ecef2bd1c50f66298fb97fbd9083f9a8e5f23fd2efd2dd9854953913644f63bf038a2dd6c05476c132f33f08292f62bab9b61b085cdaf6c34b2056c163b12e63e1e2857f4aaefa4f589258b72bbdcb60bfb74f7a05c8a72d2f8a7bf61af1268";
		Test test = new Test();
//		test.test();
		System.out.println(test.appDecrypt(aa));
	}
	
	private String appDecrypt(String result){
		String str;
		byte[] data = AesUtils.hexStr2Bytes(result);
		data = AesUtils.decrypt(data);
		try {
			str = new String(Base64.decode(data),"UTF-8");
		} catch (Exception e) {
			return "";
		}
		return str;
	}
}

