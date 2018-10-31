package com.hnjk.extend.plugin.excel.config;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


/**
 * Excel导入导出配置参数实体. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： Jul 15, 2009 11:52:49 AM
 * @see 
 * @version 1.0
 */
public class ConfigParam implements java.io.Serializable{

	private static final long serialVersionUID = 6514704092133762012L;

	transient private ExcelConfigInfo excelConfig = null;//配置xml
	 
	 private File excelFile = null;//excel 文件
	 
	 private Map valueMap = null;//属性

	 transient private OutputStream excelOutputStream = null;//excel输出流
	 
	 private List modelList;//转换完成的模型列表	
	 
	 private List<MergeCellsParam> mergeCellsParams;//需要合并的单元格坐标
	 
	public OutputStream getExcelOutputStream() {
		return excelOutputStream;
	}

	public void setExcelOutputStream(OutputStream excelOutputStream) {
		this.excelOutputStream = excelOutputStream;
	}

	public List getModelList() {
		return modelList;
	}

	public void setModelList(List modelList) {
		this.modelList = modelList;
	}

	public ExcelConfigInfo getExcelConfig() {
		return excelConfig;
	}

	public void setExcelConfig(ExcelConfigInfo excelConfig) {
		this.excelConfig = excelConfig;
	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public Map getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map valueMap) {
		this.valueMap = valueMap;
	}

	/**
	 * @return the mergeCellsParams
	 */
	public List<MergeCellsParam> getMergeCellsParams() {
		return mergeCellsParams;
	}

	/**
	 * @param mergeCellsParams the mergeCellsParams to set
	 */
	public void setMergeCellsParams(List<MergeCellsParam> mergeCellsParams) {
		this.mergeCellsParams = mergeCellsParams;
	}
	 
	 
	 
	 
}
