package com.hnjk.core.foundation.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
 * 数字转换为汉语中人民币的大写
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 18, 2016 9:15:43 AM 
 * 
 */
public class NumberToCNUtils {
	
	//	汉语中数字大写
	private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	//	 汉语中货币单位大写，这样的设计类似于占位符
	private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
	
	//	 特殊字符：整
	private static final String CN_FULL = "整";
	
	//	特殊字符：负
	private static final String CN_NEGATIVE = "负";

	//	金额的精度，默认值为2
	private static final int MONEY_PRECISION = 2;

	//	 特殊字符：零元整
	private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
	
	//  汉语中货币单位大写，用于收据打印
	private static final String[] CN_UPPER_MONETRAY_UNIT_2 = { "分", "角", "元", "拾", "佰", "仟", "万" };

	/**
	* 把输入的金额转换为汉语中人民币的大写
	* 
	* @param numberOfMoney 输入的金额
	* @return 对应的汉语大写
	*/
	public static String number2CNMonetaryUnit(BigDecimal numberOfMoney) {
		StringBuffer sb = new StringBuffer();
		// -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
		// positive.
		int signum = numberOfMoney.signum();
		// 零元整的情况
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}
		//这里会进行金额的四舍五入
		long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
		// 得到小数点后两位值
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
		if (scale <= 0) {
			 numIndex = 2;
			 number = number / 100;
			 getZero = true;
		}
		if ((scale > 0) && (scale % 10 <= 0)) {
			 numIndex = 1;
			 number = number / 10;
			 getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			 if (number <= 0) {
			     break;
			 }
			 // 每次获取到最后一个数
			 numUnit = (int) (number % 10);
			 if (numUnit > 0) {
			     if ((numIndex == 9) && (zeroSize >= 3)) {
				 sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
			     }
			     if ((numIndex == 13) && (zeroSize >= 3)) {
				 sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
			     }
			     sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
			     sb.insert(0, CN_UPPER_NUMBER[numUnit]);
			     getZero = false;
			     zeroSize = 0;
			 } else {
			     ++zeroSize;
			     if (!(getZero)) {
				 sb.insert(0, CN_UPPER_NUMBER[numUnit]);
			     }
			     if (numIndex == 2) {
				 if (number > 0) {
				     sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				 }
			     } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
				 sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
			     }
			     getZero = true;
			 }
				 // 让number每次都去掉最后一个数
				 number = number / 10;
				 ++numIndex;
		}
		// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (scale <= 0) {
			sb.append(CN_FULL);
		}
		return sb.toString();
	}
	
	/**
	 * 获取对应人民币单位的大写数字
	 * 
	 * @param moneyNum
	 * @param monetaryUnit 不能为空，并且在["分", "角", "元", "拾", "佰", "仟", "万"]里面
	 * @return
	 */
	public static String getCNByMonetaryUnit(BigDecimal moneyNum, String monetaryUnit) {
		Long upperNumber = 0L;
		String result = "";
		do{
			if(ExStringUtils.isEmpty(monetaryUnit) || !Arrays.asList(CN_UPPER_MONETRAY_UNIT_2).contains(monetaryUnit)){
				result = "参数有误！";
				continue;
			}
			if(moneyNum != null){
				//这里会进行金额的四舍五入
				long number = moneyNum.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
				if("分".equals(monetaryUnit)){
					upperNumber = number%10;
				}else if("角".equals(monetaryUnit)){
					number = number/10;
					upperNumber = number%10;
				}else if("元".equals(monetaryUnit)){
					number = number/100;
					upperNumber = number%10;
				}else if("拾".equals(monetaryUnit)){
					number = number/1000;
					upperNumber = number%10;
				}else if("佰".equals(monetaryUnit)){
					number = number/10000;
					upperNumber = number%10;
				}else if("仟".equals(monetaryUnit)){
					number = number/100000;
					upperNumber = number%10;
				}else if("万".equals(monetaryUnit)){
					number = number/1000000;
					upperNumber = number%10;
				}
			}
			result = CN_UPPER_NUMBER[upperNumber.intValue()];
		} while(false);
		
		return result;
	}

	/**
	 * 获取人民币的大写数字
	 * 
	 * @param moneyNum
	 * @return
	 */
	public static String getCN(BigDecimal moneyNum) {
		Long upperNumber = 0L;
		StringBuffer result = new StringBuffer("");
		do{
			if(moneyNum != null && moneyNum.compareTo(BigDecimal.ZERO)!=0){
				//这里会进行金额的四舍五入
				long number = moneyNum.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
				// 分
				upperNumber = number%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 角
				upperNumber = (number/10)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 元
				upperNumber = (number/100)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 拾
				upperNumber = (number/1000)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 佰
				upperNumber = (number/10000)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 仟
				upperNumber = (number/100000)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
				// 万
				upperNumber = (number/1000000)%10;
				result.insert(0, CN_UPPER_NUMBER[upperNumber.intValue()]);
			}
		} while(false);
		
		return result.toString();
	}
	
	public static void main(String[] args) {
		double money = 10000.15;
		BigDecimal numberOfMoney = new BigDecimal(money);
		System.out.println(getCN(numberOfMoney));
		/*String s = NumberToCNUtils.number2CNMonetaryUnit(numberOfMoney);
		System.out.println("你输入的金额为：【"+ money +"】   #--# [" +s.toString()+"]");*/
		
	/*	System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "分"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "角"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "元"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "拾"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "佰"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "仟"));
		System.out.println(NumberToCNUtils.getCNByMonetaryUnit(numberOfMoney, "万"));*/
	}
}
