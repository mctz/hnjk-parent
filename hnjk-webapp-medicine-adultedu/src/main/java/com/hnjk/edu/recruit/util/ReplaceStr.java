package com.hnjk.edu.recruit.util;

import com.hnjk.core.foundation.utils.ExStringUtils;

public class ReplaceStr {

	
	public static String replace(String[] strArrayL,String[] strArrayR,String rpstr){
		String rtStr = rpstr;
		if(ExStringUtils.isNotBlank(rpstr) && null != strArrayL && strArrayL.length > 0 && strArrayR.length == strArrayL.length){
			String[] rp = new String[strArrayL.length];
			for(int i = 0; i<strArrayL.length; i++){
				rp[i] = "\\"+strArrayL[i]+".*"+"\\"+strArrayR[i];
			}
			if(null != rp && rp.length > 0){
				for(int t = 0 ; t < rp.length; t++){
					rtStr = rtStr.replaceAll(rp[t], "");
				}
			}
		}
		return rtStr;
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "aaaadsf(我要的字符串）kasdjf";
		String r = str.replaceAll("\\(.*\\）", "");
		System.out.println(r);
	}
}
