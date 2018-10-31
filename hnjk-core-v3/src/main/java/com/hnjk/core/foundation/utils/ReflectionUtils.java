package com.hnjk.core.foundation.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @description 反射类
 * @author zhoufeng
 * @datetime 2009-6-15上午10:11:03
 */
public class ReflectionUtils {

	/**
	 * 得到某个对象的公共属性
	 * @param obj 对象
	 * @param fieldName 字段
	 * @return 该属性对象
	 * @throws Exception
	 */
	public static Object getProperty(Object obj, String fieldName) throws Exception {
		Class ownerClass = obj.getClass();
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(obj);
		return property;
	}

	/**
	 * 得到某个对象的公共属性
	 * @param className 类名
	 * @param fieldName 属性名
	 * @return 该属性对象
	 * @throws Exception
	 */
	public static Object getProperty(String className, String fieldName) throws Exception {
		Class ownerClass = Class.forName(className);
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(ownerClass);
		return property;
	}

	/**
	 * 得到某类的静态公共属性
	 * @param obj 对象
	 * @param fieldName 属性名
	 * @return 该属性对象
	 * @throws Exception
	 */
	public static Object getStaticProperty(Object obj, String fieldName) throws Exception {
		Class ownerClass = obj.getClass();
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(ownerClass);
		return property;
	}

	/**
	 * 得到某类的静态公共属性
	 * @param className 类名
	 * @param fieldName 属性名
	 * @return 该属性对象
	 * @throws Exception
	 */
	public static Object getStaticProperty(String className, String fieldName) throws Exception {
		Class ownerClass = Class.forName(className);
		Field field = ownerClass.getField(fieldName);
		Object property = field.get(ownerClass);
		return property;
	}

	/**
	 * 执行某对象方法
	 * @param obj 对象
	 * @param methodName 方法名
	 * @param args 方法参数
	 * @return 方法返回值
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object obj, String methodName, Object[] args) throws Exception {
		Class ownerClass = obj.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass); // 第一个参数为调用的方法名。第二个为方法的返回值；类型
		return method.invoke(obj, args); // 第一个参数表示要调用的对象，后者为传给这个方法的参数���������Ĳ���
	}

	/**
	 * 执行某对象get方法
	 * @param obj 对象
	 * @param fieldName 属性名
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeGetMethod(Object obj, String fieldName) throws Exception {
		Class ownerClass = obj.getClass();
		StringBuilder getMethod = new StringBuilder("get");
		getMethod.append(fieldName.substring(0, 1).toUpperCase());
		getMethod.append(fieldName.substring(1));
		Method method = ownerClass.getDeclaredMethod(getMethod.toString(), new Class[] {});
		return method.invoke(obj);
	}

	/**
	 * 执行某对象set方法
	 * @param obj 对象
	 * @param fieldName 属性名
	 * @param args 参数
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeSetMethod(Object obj, String fieldName, Object args) throws Exception {
		Class ownerClass = obj.getClass();
		StringBuilder getMethod = new StringBuilder("set");
		getMethod.append(fieldName.substring(0, 1).toUpperCase());
		getMethod.append(fieldName.substring(1));
		Method method = ownerClass.getDeclaredMethod(getMethod.toString(), args.getClass());
		return method.invoke(obj, args);
	}

	/**
	 * 执行某类的静态方法
	 * @param className 类名
	 * @param methodName 方法名
	 * @param args 参数数组
	 * @return 执行方法返回的结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
		Class ownerClass = Class.forName(className);
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(null, args);
	}

	/**
	 * 新建实例
	 * @param className 类名
	 * @param args 构造函数的参数
	 * @return 新建的实例
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object newInstance(String className, Object[] args) throws Exception {
		Class newoneClass = Class.forName(className);
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Constructor cons = newoneClass.getConstructor(argsClass);
		return cons.newInstance(args);
	}
	
	@SuppressWarnings("unchecked")
	public static Object newInstance(String className) throws Exception {
		Class newoneClass = Class.forName(className);
		Class[] classes = new Class[0];
		Constructor cons = newoneClass.getConstructor(classes);
		return cons.newInstance(new Object[0]);
	}

	/**
	 * 是不是某个类的实例
	 * @param obj 实例
	 * @param cls 类
	 * @return 如果 obj 是此类的实例，则返回 true
	 */
	public static boolean isInstance(Object obj, Class cls) {
		return cls.isInstance(obj);
	}

	/**
	 * 得到数组中的某个元素
	 * @param array 数组
	 * @param index 索引
	 * @return 返回指定数组对象中索引组件的值
	 */
	public static Object getByArray(Object array, int index) {
		return Array.get(array, index);
	}
		
	
	public static void main(String[] args) {
		try {
			Object obj = ReflectionUtils.newInstance("com.hnjk.core.foundation.utils.ExBeanUtils");
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
