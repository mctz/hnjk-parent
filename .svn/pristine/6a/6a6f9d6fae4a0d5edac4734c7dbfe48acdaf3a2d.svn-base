package com.hnjk.edu.recruit.helper;

import java.util.regex.Pattern;

/**
 * 把15位的身份证号升级成18位 <code>UpdateIDNumber</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-5-12 下午03:51:08
 * @see
 * @version 1.0
 */
public class UpdateIDNumber {
	final int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	private int[] ai = new int[18];

	private final static Pattern PARTTERN_CARD_NO = Pattern
			.compile("\\d{15}|\\d{17}[0-9X]");
	private final static Pattern PARTTERN_DATE = Pattern
			.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)");
	// 1-17位相乘因子数组
	private final static int[] FACTOR = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
			10, 5, 8, 4, 2 };
	// 18位随机码数组
	private final static char[] RANDOM = "10X98765432".toCharArray();

	public static boolean validate(String idCardNo) {
	      // 对身份证号进行长度等简单判断
	      if (idCardNo == null || !PARTTERN_CARD_NO.matcher(idCardNo).matches()) {
	         return false;
	      }
	      int len = idCardNo.length();
	      // 一代身份证
	      if (len == 15) {
	         return PARTTERN_DATE.matcher("19" + idCardNo.substring(6, 12)).matches();
	      }
	      // 二代身份证
	      if (len == 18 && PARTTERN_DATE.matcher(idCardNo.substring(6, 14)).matches()) {
	         // 判断随机码是否相等
	         return calculateRandom(idCardNo) == idCardNo.charAt(17);
	      } else {
	         return false;
	      }

	   }

	   /**
	    * 计算最后一位随机码
	    * 
	    * @param idCardNo
	    * @return
	    */
	   private static char calculateRandom(String idCardNo) {
	      // 计算1-17位与相应因子乘积之和
	      int total = 0;
	      for (int i = 0; i < 17; i++) {
	         total += Character.getNumericValue(idCardNo.charAt(i)) * FACTOR[i];
	      }
	      // 判断随机码是否相等
	      return RANDOM[total % 11];
	   }

	   /**
	    * 计算最后一位随机码
	    * 
	    * @param idCardNo
	    * @return
	    */
	   private char calculateRandom(char[] idCardNo) {
	      // 计算1-17位与相应因子乘积之和
	      int total = 0;
	      for (int i = 0; i < 17; i++) {
	         total += Character.getNumericValue(idCardNo[i]) * FACTOR[i];
	      }
	      // 判断随机码是否相等
	      return RANDOM[total % 11];
	   }
	
	// 验证15位18位
	public boolean Verify(String idcard) {
		if (idcard.length() == 15) {
			idcard = uptoeighteen(idcard);
		}
		if (idcard.length() != 18) {
			return false;
		}
		String verify = idcard.substring(17, 18);
		if (verify.equals(getVerify(idcard))) {
			return true;
		}
		return false;
	}// 得到最后地位校验码

	public String getVerify(String eightcardid) {
		int remaining = 0;
		if (eightcardid.length() == 18) {
			eightcardid = eightcardid.substring(0, 17);
		}
		if (eightcardid.length() == 17) {
			int sum = 0;
			for (int i = 0; i < 17; i++) {
				String k = eightcardid.substring(i, i + 1);
				ai[i] = Integer.parseInt(k);
			}
			for (int i = 0; i < 17; i++) {
				sum = sum + wi[i] * (ai[i]);
			}
			remaining = sum % 11;
		}
		return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
	} // 15转18位

	public String uptoeighteen(String fifteencardid) {
		String eightcardid = fifteencardid.substring(0, 6);
		eightcardid = eightcardid + "19";
		eightcardid = eightcardid + fifteencardid.substring(6, 15);
		eightcardid = eightcardid + getVerify(eightcardid);
		return eightcardid;
	}
}
