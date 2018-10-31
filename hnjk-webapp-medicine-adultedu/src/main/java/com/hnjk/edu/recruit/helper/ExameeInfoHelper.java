package com.hnjk.edu.recruit.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.xml.JaxbUtil;
import com.hnjk.extend.plugin.dbf.DBFReader;
import lombok.Cleanup;

public class ExameeInfoHelper {
	/**dbf数据检查条件配置文件*/
	public static final String CONFIGNAME = "dbf/exameeinfo-config.xml";
	/**
	 * 读取dbf数据，转为map
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> dbfToListMap(String filePath) throws ServiceException {
		return dbfToListMap(filePath, "GBK");
	}
	/**
	 * 以指定编码读取dbf数据，转为map
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> dbfToListMap(String filePath, String charsetName) throws ServiceException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {			
			@Cleanup InputStream fis = new FileInputStream(filePath);
			DBFReader reader = new DBFReader(fis);
			if(ExStringUtils.isNotBlank(charsetName)){//设置读取编码
				reader.setCharactersetName(charsetName);
			}	
			Object[] rowValues;
			Map<String, Object> map = null;//把array转为map
			while ((rowValues = reader.nextRecord()) != null) {
				map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < rowValues.length; i++) {
					Object value = rowValues[i];
					map.put(reader.getField(i).getName().toUpperCase(), (value != null && value instanceof String)?ExStringUtils.trimToEmpty(value.toString()):value);					
				}
				list.add(map);
			}
		} catch (Exception e) {
			throw new ServiceException("读取dbf文件失败.",e.fillInStackTrace());
		}		
		return list;
	}	
	/**
	 * 导入数据检查配置
	 * @return
	 */
	public static ExameeInfoConfig getExameeInfoConfig(){
		try {			
			@Cleanup InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIGNAME);
			JaxbUtil u = new JaxbUtil(ExameeInfoConfig.class);
			ExameeInfoConfig ei = (ExameeInfoConfig) u.unmarshal(in);
			return ei;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("数据检查条件和转换条件没有配置",e);
		}		
	}
}
