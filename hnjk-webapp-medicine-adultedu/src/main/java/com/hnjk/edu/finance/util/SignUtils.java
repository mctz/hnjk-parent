package com.hnjk.edu.finance.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.CollectionUtils;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.MD5CryptorUtils;

/** 
 * 加密签名工具类
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年1月19日 上午11:14:59 
 * 
 */
public class SignUtils {

	/** 
     * 使用 Map按key进行排序 
     * @param map 
     * @return 
     */  
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {  
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());  
        sortMap.putAll(map);  
        return sortMap;  
    }  
    
    /**
     * <p>
	 * 对于queryMap中的每个键值对按照键的字母顺序升序排序，得到排序后的请求字符串qs
	 * 其中value需要使用utf-8格式进行URLEncode
	 * </p>
	 * @param queryMap
	 * @return
     * @throws UnsupportedEncodingException 
	 */
	public static String createQueryString(Map<String, Object> queryMap) throws UnsupportedEncodingException {
		queryMap = sortMapByKey(queryMap);
		StringBuffer qs = new StringBuffer("");
		if(!CollectionUtils.isEmpty(queryMap)){
			String val = "";
			for(Map.Entry<String, Object> entry : queryMap.entrySet()){
				val = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
				qs.append("&").append(entry.getKey()).append("=").append(val);
			}
		}
		String qsStr = qs.toString();
		if(ExStringUtils.isNotEmpty(qsStr)){
			qsStr = qsStr.substring(1);
		}
		return qsStr;
	}
	
	/**
	 * 将参数进行MD5加密
	 * @param queryMap
	 * @return
	 */
	public static String signByMD5(Map<String, Object> queryMap) {
		String queryParam = null;
		try {
			queryParam = createQueryString(queryMap);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MD5CryptorUtils.getMD5ofStr(queryParam);
	}
}

/**
 * 字符串比较器
 * @author Zik
 *
 */
class MapKeyComparator implements Comparator<String>{

	@Override
	public int compare(String str1, String str2) {
		
		return str1.compareTo(str2);
	}
}
