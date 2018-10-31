package com.hnjk.core.foundation.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
 * <code>Pinyin4jHelper</code><p>
 * 汉字转换拼音辅助.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-7-28 下午08:11:11
 * @see 
 * @version 1.0
 */
public class Pinyin4jHelper {

	public static String getPingYin(String src) {
        char[] t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写
       // t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);//大写
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//WITH_TONE_NUMBER//第几声
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        try {
            for (int i = 0; i <t1.length; i++) {
            //判断是否为汉字字符函数
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0]+"";
                } else {
                    t4 += java.lang.Character.toString(t1[i])+" ";
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
           
        }
        return t4;
    }
	
	  public static void main(String[] args){
	        System.out.println("全拼："+getPingYin("深圳"));
	   }
}
