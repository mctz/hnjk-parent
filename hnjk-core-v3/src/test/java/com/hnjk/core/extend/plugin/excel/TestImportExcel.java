package com.hnjk.core.extend.plugin.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;

/**
 * 测试导入Excel.
 * <code>TestImportExcel</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 上午09:55:51
 * @see 
 * @version 1.0
 */
public class TestImportExcel  {
	
	private static Map<String,Object> codeMap = new HashMap<String,Object>();//定义字典转换Map
	
	private static IImportExcelService exportExcel;//注入Excel导入服务
	
	//@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//替换别名:codeTableName_value(代码_值,替换的值)
		codeMap.put("sex_男", "M");//替换Excel表格中 sex列 男->M
		codeMap.put("sex_女", "F");
		exportExcel = (IImportExcelService)SpringContextHolder.getBean("importExcelService");
		//初始化配置参数
		exportExcel.initParmas(StudentUtils.excelfile,"importStudentInfo", codeMap);
		
	}
	
	//@Test
	public void testReadConfig_Form() {	//测试导入	
		List modelList = exportExcel.getModelList();//获取转换的model list
		//遍历
		for (int i = 0; i < modelList.size(); i++) {
			Object obj = modelList.get(i);
			System.out.println("--" + obj.getClass().getSimpleName());
			System.out.println("--" + ToStringBuilder.reflectionToString(obj));
		}		
	}

	
}
