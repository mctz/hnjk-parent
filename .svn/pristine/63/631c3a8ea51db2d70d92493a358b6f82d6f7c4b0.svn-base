package com.hnjk.core.foundation.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.util.*;
import java.util.Map.Entry;

import com.hnjk.core.beans.ColumnInfo;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * 常用Bean工具集. <p>
 * 该类继承apache BeanUtils，可以自行扩展.<p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-25下午05:07:27
 * @see org.apache.commons.beanutils.BeanUtils
 * @version 1.0
 */
public class ExBeanUtils extends BeanUtils{

	protected static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	
	/**
	 * 获取对象属性.
	 * 强制获取，无须通过get方法.
	 * @param object 对象
	 * @param fieldName 属性
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);		

		Object result = null;
		if (null != field && !field.isAccessible()) {
			field.setAccessible(true);
			try {
				result = field.get(object);
			} catch (IllegalAccessException e) {
				logger.error("不可能抛出的异常{}", e.getMessage());
			}
		}
		
		return result;
	}
	
	/**
	 * 设置对象属性值.
	 * 强制设置，无须通过set方法.
	 * @param object
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);
		if (null != field && !field.isAccessible()) {
			field.setAccessible(true);
		}
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * @param object
	 * @param fieldName
	 * @return
	 * @see #getDeclaredField(Class clazz, String fieldName)
	 * @throws NoSuchFieldException
	 */
	public static Field getDeclaredField(Object object, String fieldName) throws NoSuchFieldException {
		Assert.notNull(object);	
		return getDeclaredField(object.getClass(), fieldName);
	}
	
	/**
	 * 获取实体属性的注解
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Annotation[] getAnnotation(Object object, String fieldName) throws NoSuchFieldException {
		return getDeclaredField(object.getClass(), fieldName).getAnnotations();
	}

	/**
	 * 循环向上转型,获取类的DeclaredField.
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(fieldName);
		if("serialVersionUID".equals(fieldName)){//suid排除
			return null;
		}
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + fieldName);
	}

	/**
	 * 判断实体属性是否存在
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field hasDeclaredField(Class clazz, String fieldName) {
		Assert.hasText(fieldName);
		while (clazz != Object.class && clazz!=null) {
			if (fieldName.contains(".")) {
				String childFieldName = ExStringUtils.removeStartString(fieldName, ".");
				Field childField = null;
				try {
					childField = hasDeclaredField(clazz.getDeclaredField(ExStringUtils.substringByRegex(fieldName, ".", true)).getType(),childFieldName);
				} catch (NoSuchFieldException e) {
					//e.printStackTrace();
					return null;
				}
				if (childField!=null) {return  childField;}
			} else {
				try {
					if (clazz.getDeclaredField(fieldName) != null) {return clazz.getDeclaredField(fieldName);}
				} catch (NoSuchFieldException e) {
					//e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	/**
	 * 对象间属性的copy
	 * @author Terry,政企软件中心
	 * @since 2009-9-22,下午03:22:11
	 * @param dest 目标对象
	 * @param orig 源对象
	 */
	public static synchronized void copyProperties(Object dest, Object orig){
		try {
			//PropertyUtils.copyProperties(dest, orig);
			copyBeans(orig,dest);
		} catch (Exception e1) {			
			logger.error("属性拷贝时抛出的异常:{}", e1.getMessage());
		} 
	}
	
	public static Object copyBeans(Object oldObj, Object newObj,String[][] propertyArray) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {  
		newObj = copyBeans(oldObj, newObj);  
		 for (int i = 0; i < propertyArray.length; i++) {  
		   for (int t = 0; t < propertyArray[i].length; t++) {  
			 if (propertyArray[i].length < 2){
				 return newObj;}
			 if (propertyArray[i][t] == null){
				 return newObj;}
		   }
			   
			 try {  
				 Object indate = PropertyUtils.getNestedProperty(oldObj,propertyArray[i][0]);  
				 if (indate instanceof java.util.Date) {  
					 indate = new java.sql.Date(((java.util.Date) indate).getTime());  
				 }  
				 	PropertyUtils.setNestedProperty(newObj, propertyArray[i][1],indate);  
				 } catch (IllegalAccessException e) {  
					 e.printStackTrace();  
				 } catch (InvocationTargetException e) {  
					 e.printStackTrace();  
				 } catch (NoSuchMethodException e) {  
					 e.printStackTrace();  
				 }  
			 }  
			 return newObj;  
	}  
	/**
	 * 给定属性集(排除\指定)，从旧的Bean,copy数据到新的Bean中,返回新的Bean  
	 * 
	 * @param oldObj     旧的Bean 
	 * @param newObj     新的Bean
	 * @param isExclude  是否排除
	 * @param propertys  给定的属性集
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object copyBeans(Object oldObj, Object newObj,boolean isExclude,String...propertys)throws IllegalAccessException, InvocationTargetException, NoSuchMethodException { 
		
		Assert.notNull(oldObj,"属性复制的原对象不能为空!");
		Assert.notNull(newObj,"属性复制的新对象不能为空!");
		Assert.notNull(isExclude,"isExclude不能为空!");
		Assert.notNull(propertys,"请给定要复制或排除的属性!");
		Assert.notEmpty(propertys,"要复制或排除的属性不能为空!");
		
		Map<String,String> dataMap  = new HashMap<String, String>();
		Map<String,String> proMap   = new HashMap<String, String>();
		
		PropertyDescriptor[] oldPds = PropertyUtils .getPropertyDescriptors(oldObj);  
		PropertyDescriptor[] newPds = PropertyUtils .getPropertyDescriptors(newObj);  
		
		//1.将给定的属性集放入MAP
		for (int i = 0; i < propertys.length; i++){
			dataMap.put(propertys[i], propertys[i]);
		}

		//2.获得要Copy的属性名
		for (int i = 0; i < oldPds.length; i++) {
			PropertyDescriptor oldPd = oldPds[i];  
			if(isExclude&&dataMap.containsKey(oldPd.getDisplayName())) {
				continue;//排除给定的属性集
			}
			if(isExclude==false&&(!dataMap.containsKey(oldPd.getDisplayName()))) {
				continue;//只复制给定属性的值
			}
			for(int j = 0; j < newPds.length; j++) {  
			    PropertyDescriptor newPd = newPds[j];
				if(null == newPd.getWriteMethod()) {
					continue;
				}
				if ((oldPd.getDisplayName().equals(newPd.getDisplayName())) && (!"class".equals(newPd.getDisplayName()))) {
					proMap.put(oldPd.getDisplayName(),oldPd.getDisplayName());
					break;  
				}  
			}  
		}
		//3.Copy属性值
		for(String key : proMap.keySet()) {
			
			if (key == null) {
				return newObj;
			}
			
			Object indate = PropertyUtils.getNestedProperty(oldObj,key);			 // 处理日期  
			if(indate instanceof java.util.Date) {
				indate = new java.sql.Date(((Date) indate).getTime());
			}
			if(indate instanceof Collection && null != indate){
			   Collection c = (Collection)indate;
			   if(c.isEmpty() || c.size()<1) {
				   continue;
			   }
			}
			PropertyUtils.setNestedProperty(newObj,key,indate);
		}
		
		return newObj;
	}
	
	/** 
	* 由旧的Bean,copy数据到新的Bean中,返回新的Bean 
	*  
	* @param oldObj 旧的Bean 
	* @param newObj 新的Bean 
	* @return Object 返回新的JavaBean 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	*/  
	public static Object copyBeans(Object oldObj, Object newObj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {  
		 PropertyDescriptor[] oldPds = PropertyUtils .getPropertyDescriptors(oldObj);  
		 PropertyDescriptor[] newPds = PropertyUtils .getPropertyDescriptors(newObj);  
		 String[] propertys = new String[250];  
		 int ss = 0;  
		 for (int i = 0; i < oldPds.length; i++) {  
			 PropertyDescriptor oldPd = oldPds[i];  
			 for (int t = 0; t < newPds.length; t++) {  
				 PropertyDescriptor newPd = newPds[t];
				 if(null == newPd.getWriteMethod()) {
					 continue;
				 }
				 if ((oldPd.getDisplayName().equals(newPd.getDisplayName())) && (!"class".equals(newPd.getDisplayName()))) {
					 propertys[ss] = oldPd.getDisplayName(); 					
					 ss = ss + 1;  
					 break;  
				 }  
			 }  
		 }  
		   
		 for (int i = 0; i < propertys.length; i++) {  
			 if (propertys[i] == null) {
				 return newObj;
			 }
			 	 
				 // 处理日期  
				 Object indate = PropertyUtils.getNestedProperty(oldObj,propertys[i]);		
				
				 //if(indate instanceof IBaseModel) continue;
				 if (indate instanceof java.util.Date) {  
					 indate = new java.sql.Date(((java.util.Date) indate).getTime());  
				 }  
								 
				 //if(indate instanceof IBaseModel){
					
				 //}else	
				 //if(indate instanceof Collection){
					
				 //}else{
					// if(null != indate){
						 if(indate instanceof Collection){
							 if(null != indate){
								 Collection c = (Collection)indate;
									if(c.isEmpty() || c.size()<1){
										continue;
									}
							 }
							
						 }
						 PropertyUtils.setNestedProperty(newObj, propertys[i], indate);
					// }
					
				 //}
				  				 
				  
		 }  
		 return newObj;  
	 } 
	
	/**
	 * 将bean 转换为map
	 * @param <T>
	 * @param t
	 * @return
	 */
	 public static <T> Map<String,Object> convertBeanToMap(T t){  
	     Map<String,Object> map = new HashMap<String, Object>();  
	          
	     Class cls=t.getClass();  
	     Field[] fieldlist = cls.getDeclaredFields();  
	     for(int i=0;i<fieldlist.length;i++){ //遍历bean属性 
	          Field f=fieldlist[i];  
	          String name=f.getName();  
	          
	          Class<?> cs=f.getType();  
	          try {  
	              String methodNme = changeStrToGetMethodNames(name);  
	              Method meth = null;  
	              Method[] mds = cls.getDeclaredMethods();  
	                for(int j=0;j<mds.length;j++){  
	                    if(mds[j].getName().equals(methodNme)){  
	                         meth = cls.getMethod(methodNme);  //使用get方法获取值
	                         Object rtcs= meth.invoke(t, null);  
	                         map.put(name, rtcs);  
	                        break;  
	                    }  
	                }  
	            if(meth==null){  
	                logger.debug("在"+t.getClass().getName()+"中没有找到"+methodNme+"方法");  
	            }  
	                  
	            } catch (SecurityException e) {  
	              
	            } catch (NoSuchMethodException e) {  
	              
	            } catch (IllegalArgumentException e) {  
	               
	            } catch (IllegalAccessException e) {  
	               
	            } catch (InvocationTargetException e) {  
	              
	            }  
	        }  
	        return map;  
	 }  
	 
	 //转换属性为get方法
	 private static String changeStrToGetMethodNames(String str){  
	    if(ExStringUtils.isBlank(str)) {
			return null;
		}
	       char firstChar=str.toCharArray()[0];  
	       String upstr=String.valueOf(firstChar).toUpperCase();  
	       return "get"+upstr+str.substring(1);  
	 }  
	 
	 /**
	  * 根据是否为空判断两个对象是否异或
	  * @param a
	  * @param b
	  * @return
	  */
	 public static boolean xorForNull(Object a,Object b) {
		boolean bool = true;
		if(a==null && b==null){
			bool = false;
		}
		if (a!=null && b!=null) {
			bool = false;
		}
		return  bool;
	}

	 /**
	  * 排序（一列转多列）
	  * @param col 列数
	  * @param row 行数（打印时每页能够显示的行数）
	  * @param list 数据
	  * @return *转换后的list可能存在新创建的对象,封装数据时需要判空
	  */
	public static List<Map<String, Object>> transformMultipleColumns4Map(int col,int row,List<Map<String, Object>> list) {
		List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		try {
			if(list!=null && list.size()>0){
				//如果有余数，则补全
				int num = col-list.size()%col;
				for(;num<col;num++){
					list.add(getNewMap(list.get(0)));
				}
				int size = list.size();//总大小
				int pageNum = getPageNum(row,col,size);//页数
				int pageSize = pageNum>1?row*col:size;//每页大小
				for(int n=1;n<=pageNum;n++){//遍历每一页
					int toIndex = n*pageSize;//当前页结束位置
					toIndex = toIndex>size?size:toIndex;
					List<Map<String, Object>> list2 = list.subList((n-1)*pageSize, toIndex);
					int currentPageSize = list2.size();
					List<Map<String, Object>> tempList2 = new ArrayList<Map<String,Object>>();
					for(int i=1;i<=list2.size();i++){//当前页排序
						Map<String, Object> map = list2.get(i-1);
						int mod = i%col;
						if (mod==0) {//余数为0
							map = list2.get((i+currentPageSize*(col-1))/col-1);
						}else {//余数不为0
							map = list2.get((i+currentPageSize*(mod-1)+col-mod)/col-1);
						}
						tempList2.add(map);
					}
					tempList.addAll(tempList2);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tempList;
	}
	
	/**
	 * 获取新创建的Map对象
	 * @param map
	 * @return key相同，value为null
	 */
	public static Map<String, Object> getNewMap(Map<String, Object> map) {
		Map<String, Object> temp = new HashMap<String, Object>();
		for (String key: map.keySet()) {
			temp.put(key, null);
		}
		return temp;
	}
	
	/**
	 * 获取新创建的Map对象
	 * @param map
	 * @return key相同，value为null
	 */
	public static Map<String, String> getNewMap4String(Map<String, String> map) {
		Map<String, String> temp = new HashMap<String, String>();
		for (String key: map.keySet()) {
			temp.put(key, null);
		}
		return temp;
	}

	/**
	 * 获取页数
	 * @param row
	 * @param column
	 * @param size
	 * @return
	 */
	private static int getPageNum(int row,int column,int size){
		int pageNum=1;
		int divisor = size/(row*column);
		int remainder=size%(row*column);
		if(divisor>0){
			pageNum=pageNum+divisor;
			if(remainder==0){
				pageNum--;
			}
		}
		return pageNum;
	}

	/**
	 * mapList排序
	 * @param mapList
	 * @param key 根据键值排序
	 */
	public static void sortMaps(List<Map<String, Object>> mapList, final String... key) {
		// TODO Auto-generated method stub
		Collections.sort(mapList, new Comparator<Map<String, Object>>(){
			@Override
			public int compare(Map<String, Object> o1,Map<String, Object> o2) {
				int compareResult = 0;
				for(int i=0;i<key.length;i++){
					if(o1.containsKey(key[i]) && o2.containsKey(key[i]) && o1.get(key[i])!=null && o2.get(key[i])!=null){
						compareResult = o1.get(key[i]).toString().compareTo(o2.get(key[i]).toString());
					}else if (o1.get(key[i])!=null){
						compareResult = -1;
					}else if (o2.get(key[i])!=null) {
						compareResult = 1;
					}
					if(compareResult!=0) {
						break;
					}
				}
				return compareResult;
			}
		});
	}

	/**
	 * 判断是否都不为空
	 * 只要有一个为空则返回false
	 * @param object
	 * @return
	 */
	public static boolean isNotNullOfAll(Object... object) {
		// TODO Auto-generated method stub
		for (Object o : object) {
			if(o==null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否都为空
	 * 只要有一个不为空则返回false
	 * @param object
	 * @return
	 */
	public static boolean isNullOfAll(Object... object) {
		// TODO Auto-generated method stub
		for (Object o : object) {
			if(o!=null){
				return false;
			}
		}
		return true;
	}

	public static List<String> getEmptyList(int size) {
		List<String> result = new ArrayList<String>();
		for(int i=0;i<size;i++){
			result.add("");
		}
		return result;
	}

	/**
	 * 将查询结果List<Map>封装成单个Map对象，其中一列作为Key，其它列以Map形式存放到Value
	 * <br>如果字段名包含'key'(该列不重复)，则该字段的值作为Map的key；否则第一个字段为Map的key
	 * <br>示例：[{key=1, col_name=col_value}] &nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;{1={col_name=col_value}}
	 * @param mapList
	 * @return
	 */
	public static Map<String, Map<String, Object>> convertMapsToMap(List<Map<String, Object>> mapList) {
		Map<String, Map<String, Object>> returnMap = new HashMap<String, Map<String, Object>>();
		if(mapList!=null && mapList.size()>0){
			String columnKey = "";
			if(mapList.get(0).containsKey("key")){
				columnKey = "key";
			}else {
				for (Entry<String, Object> map : mapList.get(0).entrySet()) {
					columnKey = map.getKey();
					break;
				}
			}
			
			for (Map<String, Object> map : mapList) {
				String key = map.get(columnKey)==null?"":map.get(columnKey).toString();
				map.remove(columnKey);
				returnMap.put(key, map);
			}
		}
		return returnMap;
	}

	/**
	 * 将查询结果List<Map>封装成单个Map对象，其中一列作为Key，另外一列作为Value
	 * <br>如果字段名包含'key'(该列不重复)，则该字段的值作为Map的key；否则第一个字段为Map的key
	 * <br>如果字段名包含'value'(该列不重复)，则该字段的值作为Map的value
	 * <br>示例：[{key=1, value=col_value}] &nbsp;&nbsp;&nbsp;->&nbsp;&nbsp;&nbsp;{1=col_value}
	 * @param mapList
	 * @return
	 */
	public static Map<String, Object> convertMapsToMap2(List<Map<String, Object>> mapList) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(mapList!=null && mapList.size()>0){
			String columnKey = "";
			String columnValue = "";
			if(mapList.get(0).containsKey("key")){
				columnKey = "key";
			}else {
				for (Entry<String, Object> map : mapList.get(0).entrySet()) {
					if (!"value".equals(map.getKey())) {
						columnKey = map.getKey();
						break;
					}
				}
			}
			if (mapList.get(0).containsKey("value")) {
				columnValue = "value";
			} else {
				for (Entry<String, Object> map : mapList.get(0).entrySet()) {
					if (!map.getKey().equals(columnKey)) {
						columnValue = map.getKey();
						break;
					}
				}
			}

			for (Map<String, Object> map : mapList) {
				String key = map.get(columnKey)==null?"":map.get(columnKey).toString();
				String value = map.get(columnValue)==null?"":map.get(columnValue).toString();
				returnMap.put(key, value);
			}
		}
		return returnMap;
	}


	/**
	 * 获取所有属性
	 * @param clazz
	 */
	public static List<Field> getDeclaredFieldList(Class clazz) {
		List<Field> fieldList = new ArrayList<Field>();
		while (clazz != Object.class) {
			fieldList.addAll(Arrays.asList(clazz .getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fieldList;
	}

	/**
	 * 判断集合中是否包含某个注解类型
	 * @param objectList
	 * @param annotationArray
	 * @return
	 */
	public static boolean isContainsAnnotation(List<Class> objectList, List<Annotation> annotationArray) {
		for (Annotation an : annotationArray) {
			if (objectList.contains(an.annotationType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断集合中是否包含某个类型的类
	 * @param objectList
	 * @param classArray
	 * @return
	 */
	public static boolean isContainsObj(List<Class> objectList, Class<?>... classArray) {
		for (Class aClass : classArray) {
			if (objectList.contains(aClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从属性的注解中获取数据库中对应的列名称
	 * @param field
	 * @return
	 */
	public static String getValueByAnnotationName(Field field) {
		String result = field.getName();
		List<Annotation> annotationList = Arrays.asList(field.getDeclaredAnnotations());
		if (annotationList.contains(Column.class)) {
			result = field.getAnnotation(Column.class).name();
		} else if (annotationList.contains(JoinColumn.class)) {
			result = field.getAnnotation(JoinColumn.class).name();
		} else if (annotationList.contains(OneToMany.class)) {
			result = "";
		}
		return result;
	}

	/**
	 * 判断是否包含Transient注解
	 * @param annotationList
	 * @return
	 */
	public static boolean isContainsTransient(List<Annotation> annotationList) {
		for (Annotation annotation : annotationList) {
			if (annotation.annotationType().equals(Transient.class)) {
				return true;
			}
		}
		return false;
	}

	public static JasperPrint convertListToJasperPrint(List<JasperPrint> jasperPrints) {
		int i = 0;
		JasperPrint jasperPrint = new JasperPrint();
		for (JasperPrint print : jasperPrints) {
			if(i==0){
				jasperPrint = print;
				i++;
			}else{
				List<JRBasePrintPage> pages = (List<JRBasePrintPage>)print.getPages();
				for (JRBasePrintPage page : pages) {
					jasperPrint.addPage(page);
				}
			}
		}
		return  jasperPrint;
	}

	public static boolean isContainsAnnotation(List<Annotation> annotationList, Class columnClass) {
		for (Annotation annotation : annotationList) {
			if (annotation.annotationType().equals(columnClass)) {
				return true;
			}
		}
		return false;
	}

	public static List getValueForKeyMap(Map<Map, List> mapCache, Map<String, Object> condition) {
		for(Map.Entry entry:mapCache.entrySet()){
			Map<String,Object> keyMap = (Map<String, Object>) entry.getKey();
			if (isEqualsForMap(keyMap, condition)) {
				return (List) entry.getValue();
			}
		}
		return null;
	}

	/**
	 * 判断两个Map中的Key和Value是否全部相同
	 * @param map1
	 * @param map2
	 * @return
	 */
	private static boolean isEqualsForMap(Map<String, Object> map1, Map<String, Object> map2) {
		for(Map.Entry entry:map1.entrySet()){
			String m1value = entry.getValue() == null?"":entry.getValue().toString();
			if (!map2.containsKey(entry.getKey())) {
				return false;
			}
			String m2value = map2.get(entry.getKey()).toString();
			if (!m1value.equals(m2value)) {
				return false;
			}
		}
		return true;
	}
}
