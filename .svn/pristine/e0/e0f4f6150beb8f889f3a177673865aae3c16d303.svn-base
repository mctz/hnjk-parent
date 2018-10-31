package com.hnjk.edu.teaching.util;
import java.math.BigDecimal;
import java.util.Arrays;

import com.hnjk.core.foundation.utils.ExStringUtils;

public class Test {
	    /**
	     * 根据ip端开始ip，和子网掩码长度（如23表示255.255.127.0），获取ip端结束ip
	     * @param ipStart
	     * @param mask
	     * @return
	     */
	    public static String getIpEnd(String ipStart, int mask) {
	        if(ipStart == null || "".equals(ipStart) || mask < 0) {
	            return null;
	        }
	        StringBuilder ipEnd = new StringBuilder();
	        int quotient = mask / 8;
	        int remainder = mask % 8;
	        String[] ipArray = ipStart.split("\\.");
	        for(int i = 0; i < quotient; i++) {
	            ipEnd.append(ipArray[i]).append(".");
	        }
	        if(quotient == 4) {
	            return ipEnd.substring(0, ipEnd.length()-1);
	        }
	        int num = 4 - quotient;
	        if(remainder != 0) {
	            byte[] ip_binary_array = convertIpUniteToByteArray(Integer.valueOf(ipArray[quotient]));
	            byte[] byteArray = new byte[8];
	            for(int j = 0; j < remainder; j++) {
	                byteArray[j] = ip_binary_array[j];
	            }
	            for(int j = 8; j > remainder; j--) {
	                byteArray[j-1] = (byte)1;
	            }
	            int ipEnd_quotient = convertByteArrayToIpUnite(byteArray);
	            ipEnd.append(ipEnd_quotient).append(".");
	             
	        }
	        if(remainder != 0 && num > 1) {
	            for(int j = 0; j < num-1; j++) {
	                ipEnd.append("255").append(".");
	            }
	        }else if(remainder == 0 && num > 0){
	            for(int j = 0; j < num; j++) {
	                ipEnd.append("255").append(".");
	            }
	        }
	        return ipEnd.substring(0, ipEnd.length()-1);
	    }
	     
	    /**
	     * 将ip位转换为8字节无符号byte数组
	     * @param number
	     * @return
	     */
	    public static byte[] convertIpUniteToByteArray(int number) {
	        if(number < 0 || number > 255) {
	            return null;
	        }
	        byte[] byteArray = new byte[8];
	        Arrays.fill(byteArray, (byte)0);
	        int index = 7;
	        int quotient = number / 2;
	        int remainder = number % 2;
	        while(number != 0){
	            byteArray[index--] = (byte)remainder;
	            number = quotient;
	            quotient = number / 2;
	            remainder = number % 2;
	        }
	        return byteArray;
	    }
	     
	    /**
	     * 将无符号byte数组转换成ip位 
	     * @param byteArray
	     * @return
	     */
	    public static Integer convertByteArrayToIpUnite(byte[] byteArray) {
	        if(byteArray == null) {
	            return null;
	        }
	        Integer num = 0;
	        for(int i = 0; i < byteArray.length; i++) {
	            num += byteArray[i] * (int)(Math.pow(2, (8-i-1)));
	        }
	        return num;
	    }
	     
	    /**
	     * 将ip字符串转换成long类型（二进制方式）
	     * @param ipStr
	     * @return
	     */
	    public static Long convertIpToLong(String ipStr) {
	        if(ipStr == null || "".equals(ipStr)){
	            return null;
	        }
	        Long ipLong = 0L;
	        String[] ipArray = ipStr.split("\\.");
	        if(ipArray.length != 4) {
	            return null;
	        }
	        for(int i = 0; i < ipArray.length; i++) {
	            byte[] ipByteArray = convertIpUniteToByteArray(Integer.valueOf(ipArray[i]));
	            for(int j = 0; j < ipByteArray.length; j++) {
	                ipLong += ipByteArray[j] * (long)Math.pow(2, (((4-i)*8) - j - 1));
	            }
	        }
	        return ipLong;
	    }
	    
	    public static String encode(String text) {
	    	 char c;
	    	 StringBuffer n = new StringBuffer();
	    	 for (int i = 0; i < text.length(); i++) {
		    	 c = text.charAt(i);
		    	 switch (c) {
			    	 case '&':{
				    	n.append("&amp;");
				    	break;
			    	 }
			    	 case '<':{
				    	n.append("&lt;");
				    	break;
			    	}
			    	 case '>':{
				    	n.append("&gt;");
				    	break;
			    	 }
			    	 case '"':{
				    	n.append("&quot;");
				    	break;
			    	 }
			    	 case '\'':{
				    	n.append("&apos;");
				    	break;
		    		 }
			    	 default:{
				    	n.append(c);
				    	break;
			    	 }
		    	}
	    	}
	    	 return new String(n);
		}
	     
	    public static void main(String[] args) {
//	    	String majorName="国际经济与贸易(日资贸易)[广州]";
//	    	majorName = majorName.replaceAll("\\[.*?\\]", "");
//	    	System.out.println(majorName);
//	        String ipEnd = getIpEnd("1.0.2.0", 23);
//	        System.out.println(ipEnd);
//	        Long ipLong = convertIpToLong(ipEnd);
//	        System.out.println(ipLong);
//	    	Calendar calendar = Calendar.getInstance();
//			int year = calendar.get(Calendar.YEAR);
//			int month = calendar.get(Calendar.MONTH);
//			System.out.println(month);
	    	
	    	/*List<TeachingPlanCourseStatus> aa = new ArrayList<TeachingPlanCourseStatus>();
	    	for(int i=0;i<2;i++){
	    		TeachingPlanCourseStatus courseStatus = new TeachingPlanCourseStatus();
				courseStatus.setOpenStatus("Y"); //开课状态
				courseStatus.setIsOpen("Y");//是否开课
				courseStatus.setTerm(i+"");//设置学期
				courseStatus.setCheckStatus("openY");//开课审核通过
				aa.add(courseStatus);
				courseStatus.setSchoolIds(i+"");
				courseStatus.setSchoolName(i+"学校");
	    	}
	    	
	    	for(TeachingPlanCourseStatus tpcs:aa){
	    		System.out.println(tpcs.getSchoolIds()+"_"+tpcs.getSchoolName()+"_"+tpcs.getCheckStatus());
	    	}*/
	    	try {
	    		/*List<String> aa = new ArrayList<String>();
	    		aa.add("999");
	    		if(ExCollectionUtils.isNotEmpty(aa)){
	    			System.out.println(aa+"----");
	    		}else {
	    			System.out.println("===========");
	    		}*/
//	    		
	    		/*BigDecimal decimal = new BigDecimal("100");
	    		decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
	    		System.out.println(decimal);
	    		System.out.println(decimal.toPlainString());
	    		System.out.println(decimal.toEngineeringString());
	    		System.out.println(decimal.toPlainString().replace(".", ""));
	    		System.out.println(ExStringUtils.suppleFront('0',decimal.toPlainString().replace(".", ""), 12));*/
	    		/*String ss = "000000";
	    		System.out.println(ss.length());
	    		char[] cc = ss.toCharArray();
	    		System.out.println(cc.length);
	    		System.out.println("----------------");
	    		char[] c = new char[]{'0','0','0','0','0','0'};
	    		System.out.println(c.length);
	    		String str = new String(c);
	    		System.out.println(str);
	    		System.out.println(str.length());*/
//	    		Calendar today = Calendar.getInstance();
//	    		System.out.println(today.get(Calendar.MONTH));
	    		/*Object o = null;
	    		if(ExStringUtils.isEmpty((String)o)){
	    			System.out.println("---------");
	    		}*/
	    	/*	String str = "select * from (aaa group by)";
	    		System.out.println(str.lastIndexOf(" group bu"));
	    		System.out.println(StringUtils.substringAfterLast(str, "group by"));*/
	    		
	    		/*List<String> aa = new ArrayList<String>();
	    		aa.add("11");
	    		aa.add("22");
	    		List<String> bb = new ArrayList<String>();
	    		bb.add("22");
	    		bb.add("33");
	    		System.out.println(aa.retainAll(bb));
	    		System.out.println(aa);*/
	    		
	    		/*List<String> batchClassIds = new ArrayList<String>(Arrays.asList("".split(",")));
	    		List<String> aa = new ArrayList<String>();
	    		aa.add("11");
	    		aa.add("22");
	    		batchClassIds.retainAll(aa);
	    		System.out.println(batchClassIds);*/
	    		/*List<String> aa = null;
	    		if(ExCollectionUtils.isNotEmpty(aa)){
	    			System.out.println("====");
	    		}*/
//	    		System.out.println(Tools.getMax(new int[]{5,4,3}));
//	    		System.out.println(Tools.getRowNum("▲毛泽东思想和中国特色社会主义理论体系概论/100", 30));
	    		/*String aa = "000000001005";
	    		String bb = aa.substring(aa.length()-2);
	    		String cc = aa.substring(0, aa.length()-2);
	    		System.out.println(bb+"---"+cc);
	    		System.out.println(Double.valueOf(Integer.parseInt(cc)+"."+bb));*/
	    		System.out.println("1".equals(ExStringUtils.trimToEmpty(null)));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
