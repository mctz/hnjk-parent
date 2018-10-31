package com.hnjk.extend.plugin.excel.config;

/**
 * excel组件全局配置. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午02:17:16
 * @see 
 * @version 1.0
 */
public class ConfigConstant {
	/*
	 * model 的查找编号  id
	 */
	public static final String MODEL_ID = "id";
	
	/*
	 * 从Excel 到 Model 转换的目标 javabean class
	 */
	public static final String MODEL_CLASS="class";
	
	/*
	 * excel 列中对应到javabean中的 javabean属性名称
	 */
	public static final String PROPERTY_NAME = "name";
	
	/*
	 * excel中的标标题中的列数 (  取值的时候可以用javabean属性和对应的列数取值)
	 */
	public static final String PROPERTY_CLOUMN="column";
	
	/*
	 * excel 列中对应到javabean中的 excel列标题名称  (取值的时候,可以用 javabean属性和Excel中的标题对应取值)
	 */
	public static final String PROPERTY_EXCEL_TITLE_NAME = "excelTitleName";
	
	/*
	 * excel 列是否可以为空。　值　Y：可以为空　N：不可以为空
	 */
	public static final String PROPERTY_ISNULL="isNull";
	
	/*
	 * Model 写入到Excel时，每列的宽度。
	 */
	public static final String PROPERTY_COLUMN_WIDTH="columnWidth";
	/*
	 * excel中的列数据要被转换的数据类型
	 */
	public static final String PROPERTY_DATA_TYPE = "dataType";
	
	/*
	 * excel中列数据转换时的pattern
	 */
	public static final String PROPERTY_DATA_PATTERN = "pattern";
	
	/*
	 * excel中的数据最大长度
	 */
	public static final String PROPERTY_MAX_LENGTH="maxLength";
	
	/*
	 * 在excel中没有列数据,需要系统对javabean属性中的某一个值设置一个动态传入的值(指所有JavaBean的这个属性值，都是统一传入的固定值).设置些属性,必须设置默认值
	 * 
	 * 他的值设置为 fixity = "yes" ,默认为 no.
	 */
	public static final String PROPERTY_FIXITY="fixity";
	
	/*
	 * 在导入excel时,有些值在存入系统时,使用的值需要转换
	 * 如:一个保管期限的下拉列表中有{永久(Y),长期(C),短期(D)}
	 * 系统中存入的只能是 Y,C,D之类的值,导入Excel的值却是永久,长期,短期这类的值,需要转换.
	 * 转换方式:取到Excel中的具体值,加上保管期限前辍,从传入的Map中取值.如 取值长期  C = Map.get("bgqx长期"); 
	 * 
	 */
	public static final String PROPERTY_CODE_TABLE_NAME="codeTableName";
	/*
	 * 如果值为空,设置的默认值
	 */
	public static final String PROPERTY_DEFAULT="default";


}
