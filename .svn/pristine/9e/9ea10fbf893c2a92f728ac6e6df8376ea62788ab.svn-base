package  com.hnjk.core.extend.plugin.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;

/**
 * 测试导入导出的工具.
 * <code>StudentUtils</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 上午09:37:27
 * @see 
 * @version 1.0
 */
public class StudentUtils {
	
	/*Excel数据文件存放在数据库上的路径 一般通过用户上传指定到地方*/
	public final  static File excelfile = new File("D:\\temp\\import_student.xls");
	private static IImportExcelService test = (IImportExcelService)SpringContextHolder.getBean("importExcelService");
	
	public static List getStudentFormList(){
		Map codeMap = new HashMap();
		codeMap.put("sex男", "M");
		codeMap.put("sex女", "F");
		
		test.initParmas(excelfile,	"importStudentInfo", codeMap);
		
		List modelList = test.getModelList();
		return modelList;
	}
	
	public static List getStudentMapList(){
		Map codeMap = new HashMap();
		codeMap.put("sex男", "M");
		codeMap.put("sex女", "F");
		test.initParmas(excelfile,	"importStudentInfo", codeMap);
		test.getExcelToModel().setSheet(1);
		List modelList = test.getModelList();
		return modelList;
	}
	
	public static List getScoreMapList(){
		Map codeMap = new HashMap();
		codeMap.put("sex男", "M");
		codeMap.put("sex女", "F");
		test.initParmas(excelfile,	"importStudentInfo", codeMap);
		test.getExcelToModel().setSheet(4);
		List modelList = test.getModelList();
		return modelList;
	}
}
