package com.hnjk.platform.system.service.jdbcimpl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IStandbyMenuService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织单位服务实现. <code>OrgUnitServiceImpl</code><p>;
 *
 * @version 1.0
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-4 上午09:58:47
 * @see
 */
@Service("standbyMenuService")
@Transactional
public class StandbyMenuServiceImpl extends BaseServiceImpl<Object> implements IStandbyMenuService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持

	private StringBuffer returnMsg = new StringBuffer();
	private Map<String, Object> validateMap;

	private int modNum = 4;//一次添加的学生人数

	@Override
	public Map<String, Object> importBivariateExamResult(String filePath) {
		// TODO Auto-generated method stub
		Map<String, Object> mapXls = new HashMap<String, Object>();
		try {
			if (ExStringUtils.isNotEmpty(filePath)) {
				InputStream is = new FileInputStream(filePath);
				String postfix = ExStringUtils.getPostfix(filePath);
				if (ExStringUtils.isNotEmpty(postfix)) {
					if ("xls".equals(postfix)) {
						mapXls = readExcel(new HSSFWorkbook(is));
					} else if ("xlsx".equals(postfix)) {
						mapXls = readExcel(new XSSFWorkbook(is));
					}
				}
				is.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("导入成绩文件不存在", e);
			mapXls.put("message", "导入失败");
		} catch (IOException e) {
			logger.error("读取成绩文件出错", e);
			mapXls.put("message", "导入失败");
		} catch (Exception e) {
			logger.error("解析成绩文件出错", e);
			mapXls.put("message", "导入失败");
		}
		return mapXls;
	}

	/**
	 * Read the Excel 2007-2010
	 *
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private Map<String, Object> readExcel(XSSFWorkbook workbook) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		int totalCount = 0; //总条数
		int successCount = 0;// 成功的条数
		int startRow = 0;    //第一条数据所在行数
		int endRow = 0;        //结束行
		int endCol = 0;        //结束列
		int scoreCol = 0;    //分数开始列
		returnMsg.setLength(0);
		StringBuffer sqlBuffer = new StringBuffer("insert all ");
		do {
			for (Sheet sheet : workbook) {
				startRow = 0;
				List<String> courseCodeList = new ArrayList<String>();
				List<String> courseNameList = new ArrayList<String>();
				List<String> courseTermList = new ArrayList<String>();
				List<String> courseCreditHourList = new ArrayList<String>();
				Row firstRow = sheet.getRow(0);
				if (firstRow == null) {
					returnMsg.append("<font color='red'></font>读取的文件内容为空！");
					continue;
				}
				//分数开始列,数据结束列
				endCol = sheet.getRow(0).getLastCellNum() - 1;
				for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
					Cell cell = sheet.getRow(0).getCell(i);
					if (ExStringUtils.isBlank(ExStringUtils.toString(sheet.getRow(0).getCell(i))) || ExStringUtils.containsCharacter(ExStringUtils.toString(sheet.getRow(0).getCell(i)))) {
						courseCodeList.add(ExStringUtils.toString(sheet.getRow(0).getCell(i)) + i);
					} else {
						courseCodeList.add(ExStringUtils.toString(sheet.getRow(0).getCell(i)));
					}
					//courseCodeList.add(getValue(sheet.getRow(0).getCell(i)));
					courseNameList.add(ExStringUtils.toString(sheet.getRow(1).getCell(i)));
					courseTermList.add(ExStringUtils.removeEnd(ExStringUtils.toString(sheet.getRow(2).getCell(i)), ".0"));
					courseCreditHourList.add(ExStringUtils.removeEnd(ExStringUtils.toString(sheet.getRow(3).getCell(i)), ".0"));
					if (ExStringUtils.toString(cell).contains("课程") && scoreCol == 0) {
						scoreCol = i + 1;
					}
				}
				//如果第一列单元格（序号）值为1，则为开始行（2-7行）
				String startRowName = "";
				for (int i = 1; i < 7; i++) {
					Row xssfRow = sheet.getRow(i);
					if (xssfRow != null) {
						Cell cell = xssfRow.getCell(0);
						if (!"序号".equals(ExStringUtils.toString(cell)) && "序号".equals(startRowName)) {
							startRow = i;
							break;
						}
						startRowName = ExStringUtils.toString(cell);
					}
				}
				//结束行
				endRow = sheet.getLastRowNum();
				for (int i = sheet.getLastRowNum(); i >= startRow; i++) {
					Row xssfRow = sheet.getRow(i);
					if (xssfRow != null && ExStringUtils.isNotBlank(ExStringUtils.toString(xssfRow.getCell(1)))) {
						endRow = i;
						break;
					}
				}
				for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
					Row xssfRow = sheet.getRow(rowNum);
					if (xssfRow != null && ExStringUtils.isNotBlank(ExStringUtils.toString(xssfRow.getCell(1)))) {
						Cell order = xssfRow.getCell(0);
						Cell studyNo = xssfRow.getCell(1);
						Cell studentName = xssfRow.getCell(2);
						String orderStr = ExStringUtils.toString(order);
						String studyNoStr = ExStringUtils.toString(studyNo);
						String studentNameStr = ExStringUtils.toString(studentName);
						StringBuffer stuScore = new StringBuffer();
						//一个学生的所有课程成绩
						for (int colNum = scoreCol; colNum <= endCol; colNum++) {
							Cell examResultsCell = xssfRow.getCell(colNum);
							String examResults = ExStringUtils.mathRound(ExStringUtils.toString(examResultsCell));
							stuScore.append(" into edu_teach_examresults(resourceid,studentid,courseid,integratedscore,onlinescore,memo,isoutplancourse,coursescoretype,examabnormity,isdelayexam,examcount,checkstatus,version,isdeleted,examtype,ismakeupexam,plancourseteachtype)");
							stuScore.append(" values('" + studyNoStr + "_" + courseCodeList.get(colNum) + "',");
							stuScore.append("(select si.resourceid from edu_roll_studentinfo si where si.studyno='" + studyNoStr + "'),");
							stuScore.append("(select c.resourceid from edu_base_course c where c.coursecode='" + courseCodeList.get(colNum) + "'),");
							stuScore.append("'" + examResults + "','" + examResults + "',");
							stuScore.append("'" + courseTermList.get(colNum) + courseCreditHourList.get(colNum) + "_" + courseNameList.get(colNum) + "',");
							stuScore.append("'N','11','0','N',1,'4',0,0,0,'N','facestudy')");
							totalCount++;
							if (examResults.startsWith("转")) {//如果是异动前成绩表，则跳过
								stuScore.setLength(0);
								totalCount = totalCount - 1 - colNum + scoreCol;
								break;
							}
						}
						sqlBuffer.append(stuScore);
					}
					if (rowNum % modNum == 0 && sqlBuffer.toString().contains("values")) {
						try {
							sqlBuffer.append(" select 1 from dual ");
							successCount += jdbcDao.getBaseJdbcTemplate().executeForMap(sqlBuffer.toString(), new HashMap<String, Object>());
							sqlBuffer = new StringBuffer("insert all ");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("---------------------------" + sheet.getSheetName() + ":");
							System.out.println(sqlBuffer.toString());
							sqlBuffer = new StringBuffer("insert all ");
							break;
						}
					}
				}
			}
			try {
				if (sqlBuffer.toString().contains("values")) {
					sqlBuffer.append(" select 1 from dual ");
					successCount += jdbcDao.getBaseJdbcTemplate().executeForMap(sqlBuffer.toString(), new HashMap<String, Object>());
				}
				//删除不在系统中的学生成绩
				//jdbcDao.getBaseJdbcTemplate().executeForMap("delete from edu_teach_examresults er where er.studentid is null", new HashMap<String, Object>());
				//删除没有成绩的学生成绩
				//jdbcDao.getBaseJdbcTemplate().executeForMap("delete from edu_teach_examresults er where er.studentid not in(select er.studentid from edu_teach_examresults er where er.onlinescore is not null group by er.studentid)", new HashMap<String, Object>());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("------------------------" + workbook.getSheetAt(workbook.getNumberOfSheets() - 1) + ":");
				System.out.println(sqlBuffer.toString());
			}
		} while (false);
		map.put("totalCount", totalCount);
		map.put("successCount", successCount);
		map.put("message", returnMsg.toString());
		return map;
	}

	/**
	 * Read the Excel 1997-2003
	 *
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private Map<String, Object> readExcel(HSSFWorkbook workbook) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		int totalCount = 0; //总条数
		int successCount = 0;// 成功的条数
		int startRow = 0;    //第一条数据所在行数
		int endRow = 0;        //结束行
		int endCol = 0;        //结束列
		int scoreCol = 0;    //分数开始列
		successCount = 0;
		returnMsg.setLength(0);
		StringBuffer sqlBuffer = new StringBuffer("insert all ");
		do {
			for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
				startRow = 0;
				List<String> courseCodeList = new ArrayList<String>();
				List<String> courseNameList = new ArrayList<String>();
				List<String> courseTermList = new ArrayList<String>();
				List<String> courseCreditHourList = new ArrayList<String>();
				HSSFSheet sheet = workbook.getSheetAt(numSheet);
				HSSFRow firstRow = sheet.getRow(0);
				if (firstRow == null) {
					returnMsg.append("<font color='red'></font>读取的文件内容为空！");
					continue;
				}
				//分数开始列,数据结束列
				endCol = sheet.getRow(0).getLastCellNum() - 1;
				for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
					HSSFCell cell = sheet.getRow(0).getCell(i);
					if (ExStringUtils.isBlank(ExStringUtils.toString(sheet.getRow(0).getCell(i))) || ExStringUtils.containsCharacter(ExStringUtils.toString(sheet.getRow(0).getCell(i)))) {
						courseCodeList.add(ExStringUtils.toString(sheet.getRow(0).getCell(i)) + i);
					} else {
						courseCodeList.add(ExStringUtils.toString(sheet.getRow(0).getCell(i)));
					}
					//courseCodeList.add(getValue(sheet.getRow(0).getCell(i)));
					courseNameList.add(ExStringUtils.toString(sheet.getRow(1).getCell(i)));
					courseTermList.add(ExStringUtils.removeEnd(ExStringUtils.toString(sheet.getRow(2).getCell(i)), ".0"));
					courseCreditHourList.add(ExStringUtils.removeEnd(ExStringUtils.toString(sheet.getRow(3).getCell(i)), ".0"));
					if (ExStringUtils.toString(cell).contains("课程") && scoreCol == 0) {
						scoreCol = i + 1;
					}
				}
				//如果第一列单元格（序号）值为1，则为开始行（2-7行范围类查询）
				String startRowName = "";
				for (int i = 1; i < 7; i++) {
					HSSFRow hssfRow = sheet.getRow(i);
					if (hssfRow != null) {
						HSSFCell cell = hssfRow.getCell(0);
						if (!"序号".equals(ExStringUtils.toString(cell)) && "序号".equals(startRowName)) {
							startRow = i;
							break;
						}
						startRowName = ExStringUtils.toString(cell);
					}
				}
				//结束行
				endRow = sheet.getLastRowNum();
				for (int i = sheet.getLastRowNum(); i >= startRow; i++) {
					HSSFRow xssfRow = sheet.getRow(i);
					if (xssfRow != null && ExStringUtils.isNotBlank(ExStringUtils.toString(xssfRow.getCell(1)))) {
						endRow = i;
						break;
					}
				}
				for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
					HSSFRow hssfRow = sheet.getRow(rowNum);
					if (hssfRow != null && ExStringUtils.isNotBlank(ExStringUtils.toString(hssfRow.getCell(1)))) {
						HSSFCell order = hssfRow.getCell(0);
						HSSFCell studyNo = hssfRow.getCell(1);
						HSSFCell studentName = hssfRow.getCell(2);
						String orderStr = ExStringUtils.toString(order);
						String studyNoStr = ExStringUtils.toString(studyNo);
						String studentNameStr = ExStringUtils.toString(studentName);
						StringBuffer stuScore = new StringBuffer();
						//一个学生的所有课程成绩
						for (int colNum = scoreCol; colNum <= endCol; colNum++) {
							HSSFCell examResultsCell = hssfRow.getCell(colNum);
							String examResults = ExStringUtils.mathRound(ExStringUtils.toString(examResultsCell));
							stuScore.append(" into edu_teach_examresults(resourceid,studentid,courseid,integratedscore,onlinescore,memo,isoutplancourse,coursescoretype,examabnormity,isdelayexam,examcount,checkstatus,version,isdeleted,examtype,ismakeupexam,plancourseteachtype)");
							stuScore.append(" values('" + studyNoStr + "_" + courseCodeList.get(colNum) + "',");
							stuScore.append("(select si.resourceid from edu_roll_studentinfo si where si.studyno='" + studyNoStr + "'),");
							stuScore.append("(select c.resourceid from edu_base_course c where c.coursecode='" + courseCodeList.get(colNum) + "'),");
							stuScore.append("'" + examResults + "','" + examResults + "',");
							stuScore.append("'" + courseTermList.get(colNum) + courseCreditHourList.get(colNum) + "_" + courseNameList.get(colNum) + "',");
							stuScore.append("'N','11','0','N',1,'4',0,0,0,'N','facestudy')");
							totalCount++;
							if (examResults.startsWith("转")) {//如果是异动前成绩表，则跳过
								stuScore.setLength(0);
								totalCount = totalCount - 1 - colNum + scoreCol;
								break;
							}
						}
						sqlBuffer.append(stuScore);
					}
					if (rowNum % modNum == 0 && sqlBuffer.toString().contains("values")) {
						try {
							sqlBuffer.append(" select 1 from dual ");
							successCount += jdbcDao.getBaseJdbcTemplate().executeForMap(sqlBuffer.toString(), new HashMap<String, Object>());
							sqlBuffer = new StringBuffer("insert all ");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("---------------------------" + sheet.getSheetName() + ":");
							System.out.println(sqlBuffer.toString());
							sqlBuffer = new StringBuffer("insert all ");
							break;
						}
					}
				}
			}
			try {
				if (sqlBuffer.toString().contains("values")) {
					sqlBuffer.append(" select 1 from dual ");
					successCount += jdbcDao.getBaseJdbcTemplate().executeForMap(sqlBuffer.toString(), new HashMap<String, Object>());
				}
				//删除不在系统中的学生成绩
				//jdbcDao.getBaseJdbcTemplate().executeForMap("delete from edu_teach_examresults er where er.studentid is null", new HashMap<String, Object>());
				//删除没有成绩的学生成绩
				//jdbcDao.getBaseJdbcTemplate().executeForMap("delete from edu_teach_examresults er where er.studentid not in(select er.studentid from edu_teach_examresults er where er.onlinescore is not null group by er.studentid)", new HashMap<String, Object>());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("------------------------" + workbook.getSheetAt(workbook.getNumberOfSheets() - 1) + ":");
				System.out.println(sqlBuffer.toString());
			}
		} while (false);
		map.put("totalCount", totalCount);
		map.put("successCount", successCount);
		map.put("message", returnMsg.toString());
		return map;
	}

	@Override
	public Map<String, Object> importSql(String filePath) {
		int insertCount = 0;//insert 条数
		int updateCount = 0;//update 条数
		int deleteCount = 0;//delete 条数
		String sql = "";
		Connection conn = null;
		PreparedStatement st = null;
		List<String> tableList = new ArrayList<String>();//数据库表
		List<String> sqlList = new ArrayList<String>();//执行sql
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回结果
		try {
			List<String> operateList = new ArrayList<String>();//允许执行的操作
			List<Dictionary> dictionaries = CacheAppManager.getChildren("CodeSqlOperateType");
			for (Dictionary dictionary : dictionaries) {
				operateList.add(dictionary.getDictValue());
			}
			InputStreamReader read = new InputStreamReader(new FileInputStream(filePath), "gbk");
			BufferedReader reader = new BufferedReader(read);
			String tempString = "";
			StringBuilder sqlSb = new StringBuilder();
			//如果按照字节读取少数中文会乱码
			while ((tempString = reader.readLine()) != null) {
				sqlSb.append(tempString).append("\r\n");
			}
			reader.close();
			// Windows 下换行是 \r\n, Linux 下是 \n
			String[] sqlArr = sqlSb.toString().split("(;\\s*\\r\\n)|(;\\s*\\n)");
			StringBuilder _sql = new StringBuilder();
			for (int i = 0; i < sqlArr.length; i++) { //以[';'+换行] 进行分割，获取每条执行sql
				_sql.setLength(0);
				sql = sqlArr[i].trim();
				String[] lineStr = sql.split("\\r\\n");
				for (String string : lineStr) {//每条sql按行读取，去掉注释
					if (string.startsWith("--")) {
						string = "";
					} else {
						string = string.replaceFirst("[)]\\s*;\\s*--.*", ");");
					}
					_sql.append(" ").append(string);
				}
				sql = _sql.toString().trim();
				if (!operateList.contains(sql.split(" ")[0].toLowerCase())) {
					throw new ServiceException("没有权限执行'" + sql.split(" ")[0] + "'操作！");
				}
				if (i == sqlArr.length - 1 && sql.endsWith(";")) {//最后一条sql去掉";"
					sql = sql.substring(0, sql.length() - 1);
				}
				sqlList.add(sql);
			}
			conn = getConn();
			conn.setAutoCommit(false);//通知数据库开启事务(start transaction)
			for (int i = 0; i < sqlList.size(); i++) {
				int count = 0;
				int tableName_index = 2;
				sql = sqlList.get(i);
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				if (ExStringUtils.startsWithIgnoreCase(sql, "insert")) {
					//从数据库查询是否有这条记录
					StringBuilder contentCol = new StringBuilder();
					StringBuilder sqlBuffer = new StringBuilder("select * from " + sql.replace("(", " (").split(" ")[tableName_index] + " where 1=1");
					if (!sql.contains("select") && !sql.contains("sys_guid()")) {
						String[] sqlArr2 = null;
						if (sql.contains(" values")) {
							sqlArr2 = sql.substring(sql.indexOf("("), sql.length()).split("\\s*values\\s*");
						} else if (sql.contains(" VALUES")) {
							sqlArr2 = sql.substring(sql.indexOf("("), sql.length()).split("\\s*VALUES\\s*");
						}
						String[] colNames = sqlArr2[0].substring(1, sqlArr2[0].length() - 1).split(",");
						String[] colValues = sqlArr2[1].substring(1, sqlArr2[1].length() - 1).split(",");
						if (colNames.length < colValues.length) {
							StringBuilder valueBuilder = new StringBuilder();
							String[] _colValues = new String[colNames.length];
							int j = 0;
							boolean date = false;
							boolean content = false;
							for (String value : colValues) {
								valueBuilder.append(value.trim());
								//过滤 to_date('','')函数 分割
								if (!content && (ExStringUtils.startsWithIgnoreCase(value.trim(), "to_date"))) {
									date = true;
									//过滤 '1,2,3'内容 分割    小概率事件：||','
								} else if (!date && value.trim().startsWith("'") && !value.trim().endsWith("'")) {
									content = true;
								} else if (!date && value.trim().endsWith("'")) {
									content = false;
								} else {
									date = false;
								}
								if (date || content) {
									valueBuilder.append(",");
									continue;
								}
								_colValues[j++] = valueBuilder.toString();
								valueBuilder.setLength(0);
							}
							colValues = _colValues;
						}
						boolean resourceid = false;
						String appendString = "";
						for (int j = 0; j < colNames.length; j++) {
							String name = colNames[j].trim();
							String value = colValues[j].trim();
							appendString = " and " + name + "=" + value;

							if (ExStringUtils.isBlank(value) || "null".equals(value)) {
								appendString = appendString.replace("=", " is ");
							}
							if ("CONTENT".equalsIgnoreCase(name) || resourceid) {
								contentCol.append(appendString);
								continue;
							} else if ("resourceid".equalsIgnoreCase(name)) {
								resourceid = true;
							}
							sqlBuffer.append(appendString);
						}
						result = jdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuffer.toString(), null);
					}
					if (result != null && result.size() > 0) {//如果已经存在记录，则更改为update语句
						//for (Map<String, Object> map2 : result) {
						sql = sqlBuffer.append(contentCol).toString().replace("select * from", "update").replace("where 1=1 and", "set")
								.replace(" and ", ",").replace(" is ", "=");
						sql += " where resourceid='" + result.get(0).get("resourceid") + "'";
						sqlList.add(i + 1, sql);
						//}
					} else {
						st = conn.prepareStatement(sql);
						count = st.executeUpdate();
						insertCount += count;
					}
				} else if (ExStringUtils.startsWithIgnoreCase(sql, "update")) {
					tableName_index = 1;
					st = conn.prepareStatement(sql);
					count = st.executeUpdate();
					updateCount += count;
				} else if (ExStringUtils.startsWithIgnoreCase(sql, "delete")) {
					st = conn.prepareStatement(sql);
					count = st.executeUpdate();
					deleteCount += count;
				} else {//alter
					tableName_index = 1;
					st = conn.prepareStatement(sql);
					st.execute();
					logger.info("第" + i + "条sql：" + sql);
				}
				if (st != null) {
					st.close();
				}

				if (count > 0 || sql.startsWith("alter")) {
					String tableName = sql.replace("(", " (").split(" ")[tableName_index];
					if (!tableList.contains(tableName)) {
						tableList.add(tableName);
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			insertCount = 0;
			updateCount = 0;
			deleteCount = 0;
			returnMap.put("message", e);
			logger.info(sql);
			e.printStackTrace();
		} finally {
			returnMap.put("insertCount", insertCount);
			returnMap.put("updateCount", updateCount);
			returnMap.put("deleteCount", deleteCount);
			returnMap.put("tableNames", ExStringUtils.addSymbol(tableList, "[", "]"));
		}
		return returnMap;
	}
}
