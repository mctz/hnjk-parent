package com.hnjk.extend.plugin.excel.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.hnjk.extend.plugin.excel.file.ExcelToModel;

/**
 * excel导入服务接口。 <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午06:42:51
 * @see 
 * @version 1.0
 */
public interface IImportExcelService {

	/**
	 * 初始化参数
	 * @param fileName 生成excel的目标文件
	 * @param modelName 模型名 -对应配置文件中 model id="xxx"
	 * @param valueMap 值集合 - 数据集合
	 */
	public abstract void initParmas(File fileName, String modelName,Map valueMap);

	/**
	 * 取得从Excel转换到Model的　List.
	 */
	public abstract List getModelList();

	/**
	 * 验证Xml文件配制的Excel标题在　Excel文件中是否在
	 */
	public abstract boolean isValidateExcelFormat();

	/**
	 * 取得Xml文件中配制的对应Excel文件中的标题
	 */
	public abstract Map getConfigTitle();

	/**
	 * 取得从Excel转换到Model的并且验证通过的 List.
	 */
	public abstract List getSucessModelList();

	/**
	 * 取得从Excel转换到Model的并且验证未通过的 List.
	 */
	public abstract List getErrorModelList();

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的记录
	 * @param  excelFile File
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getExcelFile(File excelFile, List modelList);

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的验证通过的记录
	 * @param  excelFile File
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getSucessExcelFile(File excelFile, List modelList);

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的验证未通过的记录
	 * @param  excelFile File
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getErrorExcelFile(File excelFile, List modelList);

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的记录
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getExcelFile(List modelList);

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的验证通过的记录
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getSucessExcelFile(List modelList);

	/**
	 * 把转换之后的modelList中验证未通过的消息，回写到Excel文件中，并返回全部导入时文件的验证未通过的记录
	 * @param  modelList List
	 * @return File
	 */
	public abstract File getErrorExcelFile(List modelList);
	
	public ExcelToModel getExcelToModel() ;
	
	public void setExcelToModel(ExcelToModel excelToModel);
	
	public List getModelListWithoutConfig();
}