package com.hnjk.core.beans;

import lombok.Data;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Function : 字段属性信息，用于生成HQL查询语句
 * <p>Author : msl
 * <p>Date   : 2018-07-20
 * <p>Description :ColumnInfo
 */
@Data
public class ColumnInfo extends Object implements Serializable {

	/**
	 * 输入框参数名称，用于保存原始名称
	 */
	private String inputName;

	/**
	 * 实体属性名称
	 */
	private String propertyName;

	/**
	 * 实体属性对应的表名称
	 */
	private String propertyTableName;

	/**
	 * 详细属性名称，用于生成Hql查询
	 */
	private String propertyNameForHql;

	/**
	 * 数据表字段名称
	 */
	private String columnName;

	/**
	 * 属性类型
	 */
	private Class propertyType;

	/**
	 * 目标实体类属性类型，一对多关系映射时使用
	 */
	private Class targetEntityType;

	/**
	 * 查询条件中的参数名称
	 */
	private String paramKey;

	/**
	 * 查询条件中的参数值
	 */
	private Object paramValue;

	/**
	 * 属性注解类型集合
	 */
	private List<Annotation> annotationList;

	/**
	 * 返回实体类类型List&lt;Object&gt; 、Object 都返回 Object
	 * @return
	 */
	public Class getEntityType() {
		if (targetEntityType == null) {
			try {
				if ("List".equals(propertyType.getSimpleName()) || "Set".equals(propertyType.getSimpleName())) {
					return propertyType.getField(propertyName).getGenericType().getClass();
				} else {
					return propertyType;
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		} else {
			return targetEntityType;
		}
		return null;
	}

}
