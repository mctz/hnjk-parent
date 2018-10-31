package com.hnjk.core.foundation.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.rao.configuration.json.JsonBinder;

/**
 * 提供一个Java对象转换Json数据格式，或从Json对象转换为Java数据类型(目前只提供到javabean和List)的工具类. <p>
 * Json数据格式介绍：<br/>
	按照最简单的形式，可以用下面这样的 JSON 表示名称/值对：<br/>
	<code>{ "firstName": "Brett" }</code> <br/>
	这个示例非常基本，而且实际上比等效的纯文本名称/值对占用更多的空间：<br/>
	<code>firstName=Brett</code><br/>
	但是，当将多个名称/值对串在一起时，JSON 就会体现出它的价值了。首先，可以创建包含多个名称/值对的记录，比如：<br/>
	<code>{ "firstName": "Brett", "lastName":"McLaughlin", "email": "brett@newInstance.com" }</code><br/>
	从语法方面来看，这与名称/值对相比并没有很大的优势，但是在这种情况下 JSON 更容易使用，而且可读性更好。
	例如，它明确地表示以上三个值都是同一记录的一部分；花括号使这些值有了某种联系。<br/>
	<p>
	JSON值的数组<br/>
	当需要表示一组值时，JSON 不但能够提高可读性，而且可以减少复杂性。例如，假设您希望表示一个人名列表。在 XML 中，
	需要许多开始标记和结束标记；如果使用典型的名称/值对（就像在本系列前面文章中看到的那种名称/值对），那么必须建立一种专有的数据格式，
	或者将键名称修改为 person1-firstName 这样的形式。<br/>
	如果使用 JSON，就只需将多个带花括号的记录分组在一起：<br/>
	<code>
	{ "people": [
	  { "firstName": "Brett", "lastName":"McLaughlin", "email": "brett@newInstance.com" },
	  { "firstName": "Jason", "lastName":"Hunter", "email": "jason@servlets.com" },
	  { "firstName": "Elliotte", "lastName":"Harold", "email": "elharo@macfaq.com" }
	]}
 	</code>
 	<br/>
	这不难理解。在这个示例中，只有一个名为 people 的变量，值是包含三个条目的数组，每个条目是一个人的记录，其中包含名、姓和电子邮件地址。
	上面的示例演示如何用括号将记录组合成一个值。当然，可以使用相同的语法表示多个值（每个值包含多个记录）：<br/>
	<code>
	{ "programmers": [
	  { "firstName": "Brett", "lastName":"McLaughlin", "email": "brett@newInstance.com" },
	  { "firstName": "Jason", "lastName":"Hunter", "email": "jason@servlets.com" },
	  { "firstName": "Elliotte", "lastName":"Harold", "email": "elharo@macfaq.com" }
	 ],
	"authors": [
	  { "firstName": "Isaac", "lastName": "Asimov", "genre": "science fiction" },
	  { "firstName": "Tad", "lastName": "Williams", "genre": "fantasy" },
	  { "firstName": "Frank", "lastName": "Peretti", "genre": "christian fiction" }
	 ],
	"musicians": [
	  { "firstName": "Eric", "lastName": "Clapton", "instrument": "guitar" },
	  { "firstName": "Sergei", "lastName": "Rachmaninoff", "instrument": "piano" }
	 ]
	}	 
	</code>

 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-3下午04:03:07
 * @modify: 
 * @主要功能：
 * @version 1.0
 */
public class JsonUtils {

	protected static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	/**
	 * List to json
	 * @param list
	 * @return
	 */
	public static String listToJson(List<?> list) {
		return binder.toJson(list);
	}

	/**
	 * map to json
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<?, ?> map) {
		return binder.toJson(map);
	}
	
	/**
	 * obj to json
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj) {
		return binder.toJson(obj);
	}
	
	/**
	 * boolean to json
	 * @param bool
	 * @return
	 */
	public static String booleanToJson(Boolean bool){
		return binder.toJson(bool);
	}
	
	/**
	 * jost to string list
	 * @param listString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> jsonToStringList(String listString){
		List<String> liststr = null;
		try {
			liststr =  binder.getMapper().readValue(listString, List.class);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return liststr;
	}
	
	/**
	 * json to list obj
	 * @param beanStr
	 * @return
	 */
	public static List<Object> jsonToBean(String beanStr){
		List<Object> listObj = null;
		try {
			listObj =  binder.getMapper().readValue(beanStr, new TypeReference<List<Object>>(){
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return listObj;
	}
	
	/**
	 * json to map
	 * @param mapStr
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String mapStr){
		return binder.fromJson(mapStr, HashMap.class);
	}
	
	/**
	 * json to bean
	 * @param <T>
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonToBean(String jsonStr,Class<T> clazz){
		return binder.fromJson(jsonStr, clazz);
	}
}
