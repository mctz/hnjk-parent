package com.hnjk.edu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;

public class NewExcelUtil4Score {
	
	private static Connection con = null;

	public static final String exportFilepath = "E:\\score.txt";

	public static final String flag = "（本）";

	public static final String flag1 = "（专）";

	public static final int startRow = 3;// 开始行

	public static final int startcol = 5;// 开始列

	public static final String[] title = { "studyno", "courseName", "term",
			"score", "year", "studentName", "fileName", "sheetName" };

	public NewExcelUtil4Score() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.248:1521:orcl",
					"XY_GDWY_BETA", "beta12345678");
			con.setAutoCommit(false);
		} catch (Exception e) {
		}
	}

	public static List<Map<String, Object>> readExcel(String filepath) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String[] tmpY = filepath.split("\\\\");
		String fileName = "";
		String year1 = "";
		if (tmpY.length == 4) {
			year1 = tmpY[3].contains("16") ? "2016"
					: tmpY[3].contains("17") ? "2017" : "2016";
			fileName = tmpY[3];
		} else {
			year1 = tmpY[2].contains("16") ? "2016"
					: tmpY[2].contains("17") ? "2017" : "2016";
			fileName = tmpY[2];
		}
		// System.out
		// .println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
		// + year1);
		do {
			Workbook wb = null;
			try {
				@Cleanup InputStream inputStream = new FileInputStream(filepath);
				wb = WorkbookFactory.create(inputStream);
				int sheetCount = wb.getNumberOfSheets();
				if (sheetCount == 0) {
					System.out.println("文件表格为空！");
					break;
				}
				// System.out.println("共有："+sheetCount+" 个sheet");
				int studenttotal = 0;
				for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {

					Sheet sheet = (Sheet) wb.getSheetAt(sheetIndex);
					// System.out.println("sheet name:"+sheet.getSheetName());
					Row courseNameRow = sheet.getRow(2);// 课程名称行
					Row termRow = sheet.getRow(1);// 课程名称行
					int studentCount = 0;
					for (int rowIndex = startRow; rowIndex < sheet
							.getLastRowNum() + 1; rowIndex++) {
						Row row = sheet.getRow(rowIndex);
						if (row == null) {
							break;
						}
						if (row.getCell(1) == null
								|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) {
							break;
						}
						studentCount++;
						int endcol = row.getLastCellNum();

						for (int colIndex = startcol; colIndex <= endcol; colIndex++) {
							Map<String, Object> map = new HashMap<String, Object>();
							Cell cell = row.getCell(colIndex);
							// if(cell==null||cell.getCellType()==Cell.CELL_TYPE_BLANK){
							// break;//遇到有空格，就直接退出这一行的循环
							// }
							if (cell != null
									&& cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								// cell.setCellType(Cell.CELL_TYPE_STRING);
								String cellValue = "";
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:// 数字类型
									cellValue = String.format("%.0f",
											cell.getNumericCellValue());
									break;
								case Cell.CELL_TYPE_STRING:
									cellValue = verifyScorce(cell);
									break;
								default:
									break;
								}
								if (ExStringUtils.isNotBlank(cellValue)) {// 值不为空才添加记录
									row.getCell(1).setCellType(
											Cell.CELL_TYPE_STRING);
									// 学号列
									String no = row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING ? row
											.getCell(1).getStringCellValue()
											: String.valueOf(row.getCell(1)
													.getNumericCellValue());
									String studentName = row.getCell(3)
											.getStringCellValue();// 姓名列
									no = verifyStudyNo(no, studentName);
									Cell tmpCell = termRow.getCell(colIndex);
									String tmp = "";
									if (tmpCell == null) {

									} else {
										tmp = tmpCell.getCellType() == Cell.CELL_TYPE_BLANK ? ""
												: tmpCell.getStringCellValue();
									}
									// String
									// tmp=tmpCell==null||tmpCell.getCellType()==Cell.CELL_TYPE_BLANK?"":tmpCell.getStringCellValue();
									for (int k = 1; k < 7; k++) {
										if (tmp == null || tmp == "") {
											tmp = termRow.getCell(colIndex - k) == null ? ""
													: termRow.getCell(
															colIndex - k)
															.getCellType() == Cell.CELL_TYPE_BLANK ? ""
															: termRow
																	.getCell(
																			colIndex
																					- k)
																	.getStringCellValue();
										} else {
											break;
										}
									}
									String courseName = courseNameRow.getCell(
											colIndex).getStringCellValue();
									courseName = verifyCourseName(courseName,
											tmp);
									if (courseName != "" && no.length() >= 10) {
										map.put(title[0], no);// 学号列
										map.put(title[1], courseName);// 课程名称

										map.put(title[3], cellValue);// 成绩
										String year = convertYear(year1, tmp);
										map.put(title[4], year);
										String term = convertTerm(year1, tmp);
										map.put(title[2], term);// 学期
										map.put(title[5], studentName);
										map.put(title[6], fileName);
										map.put(title[7], sheet.getSheetName());
										resultList.add(map);
									}
								}

							}
						}
					}
					// System.out.println("第"+(sheetIndex+1)+"个sheet有 "+studentCount+" 个学生");
					studenttotal = studenttotal + studentCount;
				}
				// System.out.println("当前excel 共有"+studenttotal+" 个学生");
				inputStream.close();
				wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (false);

		return resultList;
	}

	/**
	 * @param no
	 * @param studentName
	 * @return
	 */
	private static String verifyStudyNo(String no, String studentName) {
		if ("118460117012026".equals(no) && "罗彩仪".equals(studentName)) {
			no = "118460116012026";
		}
		if ("118460117012026".equals(no) && "张会芳".equals(studentName)) {
			no = "118460117012026";
		}
		if ("118460216012136".equals(no) && "罗婉君".equals(studentName)) {
			no = "118460216012136";
		}
		if ("118460216012136".equals(no) && "苏海萍".equals(studentName)) {
			no = "118460216006010";
		}
		if ("118460317004002".equals(no) && "谭倩雯".equals(studentName)) {
			no = "118460317104007";
		}
		if ("118460317004002".equals(no) && "徐梓君".equals(studentName)) {
			no = "118460317004002";
		}

		if ("118460317004007".equals(no) && "邱坤科".equals(studentName)) {
			no = "118460317104005";
		}
		if ("118460317004007".equals(no) && "翁晓倩".equals(studentName)) {
			no = "118460317004007";
		}
		if ("118460317004017".equals(no) && "黄婉婷".equals(studentName)) {
			no = "118460317104001";
		}
		if ("118460317004017".equals(no) && "王浩伦".equals(studentName)) {
			no = "118460317004017";
		}

		if ("118460417006001".equals(no) && "贺美蓉".equals(studentName)) {
			no = "118461517101001";
		}
		if ("118460417006001".equals(no) && "吴景君".equals(studentName)) {
			no = "118460417006001";
		}
		if ("118461116004051".equals(no) && "蔡静君".equals(studentName)) {
			no = "118461116004051";
		}
		// 没找到
		if ("118461116004051".equals(no) && "梁婉婷".equals(studentName)) {
			no = "11846216118";
		}

		if ("118461116105014".equals(no) && "贺美蓉".equals(studentName)) {
			no = "118461517101001";
		}
		if ("118461116105014".equals(no) && "陈龙颜".equals(studentName)) {
			no = "118461116105044";
		}
		if ("118460417006001".equals(no) && "陈燕珊".equals(studentName)) {
			no = "118461116105014";
		}
		if ("118461217004035".equals(no) && "有芬芬".equals(studentName)) {
			no = "118461217004035";
		}
		if ("118461217004035".equals(no) && "于丽红".equals(studentName)) {
			no = "118460317104009";
		}
		if ("118461217004046".equals(no) && "王春风".equals(studentName)) {
			no = "118460317104002";
		}
		if ("118461217004046".equals(no) && "严玉兰".equals(studentName)) {
			no = "118461217004046";
		}
		if ("118461217004047".equals(no) && "黎燕芬".equals(studentName)) {
			no = "118460317104006";
		}
		if ("118461217004047".equals(no) && "王嘉莉".equals(studentName)) {
			no = "118461217004047";
		}
		//
		if ("118461716012016".equals(no) && "陆李妮".equals(studentName)) {
			no = "118461716012016";
		}
		if ("118461716012016".equals(no) && "王海雯".equals(studentName)) {
			no = "118461716012059";
		}
		return no;
	}

	private static String convertYear(String year, String term) {
		int _year = Integer.valueOf(year);
		int _term = Integer.valueOf(convertTerm(term));
		if (_term % 2 == 1) {// 1 3 5
			if (_term / 2 == 0) {

			} else if (_term / 2 == 1) {
				_year++;
			} else {
				_year++;
				_year++;
			}
		} else {// 2 4 6
			if (_term / 2 == 1) {

			} else if (_term / 2 == 2) {
				_year++;
			} else {
				_year++;
				_year++;
			}
		}
		return String.valueOf(_year);
	}

	private static String convertTerm(String year, String term) {
		int _term = Integer.valueOf(convertTerm(term));
		if (_term % 2 == 1) {// 1 3 5
			_term = 1;
		} else {// 2 4 6
			_term = 2;
		}
		return String.valueOf(_term);
	}

	/**
	 * @param cell
	 * @return
	 */
	private static String verifyCourseName(String courseName, String term) {
		try {
			courseName = courseName.trim().replaceAll("\\(", "（")
					.replaceAll("\\)", "）").replaceAll(" ", "")
					.replaceAll("　", "");
			if (courseName.startsWith("课程名称")) {
				courseName = "";
			}
			// 查询数据库，共有16门
			// 学期对应 6门
			if (courseName.startsWith("学位外语")) {
				courseName = "学位外语" + convertTerm(term);
			}
			if (courseName.startsWith("英语口语")) {
				courseName = "英语口语" + convertTerm(term);
			}
			if (courseName.startsWith("英语视听")) {
				courseName = "英语视听" + convertTerm(term);
			}
			if (courseName.startsWith("高级英语")) {
				courseName = "高级英语" + convertTerm(term);
			}
			if (courseName.startsWith("商务英语交际")) {
				courseName = "商务英语交际" + convertTerm(term);
			}
			if (courseName.startsWith("英语（") || courseName.endsWith("英语")) {// 注意带上“（”作为区别
				courseName = "英语" + convertTerm(term);
			}

			// 学期 除2
			if (courseName.startsWith("商务英语写作")) {
				courseName = "商务英语写作" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务综合英语（二）")) {
				courseName = "商务综合英语（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务综合英语（一）")) {
				courseName = "商务综合英语（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("英语泛读")) {
				courseName = "英语泛读" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("基础商务英语（二）")) {
				courseName = "基础商务英语（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("基础商务英语（一）")) {
				courseName = "基础商务英语（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务英语听说（一）")) {
				courseName = "商务英语听说（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务英语听说（二）")) {
				courseName = "商务英语听说（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			// 学期 -1 2门
			if (courseName.startsWith("商务英语阅读")) {
				courseName = "商务英语阅读" + (convertTerm(term) - 1);
			}
			if (courseName.startsWith("英语听力")) {
				courseName = "英语听力" + (convertTerm(term) - 1);
			}

			// 课程名称纠错
			if ("基础乐理".equals(courseName)) {
				courseName = "基本乐理";
			}
			if ("国际贸易与操作实务".equals(courseName)) {
				courseName = "国际贸易操作实务";
			}
			if ("英美文学".equals(courseName)) {
				courseName = "英美文学选读";
			}
			if (courseName.startsWith("基础商务英语1")) {
				courseName = "基础商务英语（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("基础商务英语2")) {
				courseName = "基础商务英语（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("基础商务英语1")) {
				courseName = "基础商务英语（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("基础商务英语2")) {
				courseName = "基础商务英语（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务英语听说1")) {
				courseName = "商务英语听说（一）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("商务英语听说2")) {
				courseName = "商务英语听说（二）" + (convertTerm(term) % 2 == 0 ? 2 : 1);
			}
			if (courseName.startsWith("物理管理基础")) {
				courseName = "物流管理基础";
			}
			if ("济学".equals(courseName)) {
				courseName = "";
			}
			if ("济学".equals(courseName)) {
				courseName = "";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courseName;
	}

	private static int convertTerm(String term) {
		int tmp = 1;
		term = term.trim();
		try {
			if ("第一学期".equals(term)) {
				tmp = 1;
			}
			if ("第二学期".equals(term)) {
				tmp = 2;
			}
			if ("第三学期".equals(term)) {
				tmp = 3;
			}
			if ("第四学期".equals(term)) {
				tmp = 4;
			}
			if ("第五学期".equals(term)) {
				tmp = 5;
			}
			if ("第六学期".equals(term)) {
				tmp = 6;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}

	private static String verifyScorce(Cell cell) {
		String cellValue = "";
		try {
			cellValue = cell.getStringCellValue().trim().replaceAll(" （", "（")
					.replaceAll("\\(", "（").replaceAll("\\)", "）")
					.replaceAll(" ", "").replaceAll(" ", "");
			if (cellValue.startsWith("课程名称")) {
				cellValue = "";
			}
			if (cellValue.startsWith("备注")) {
				cellValue = "";
			}
			if (cellValue.startsWith("自动")) {
				cellValue = "";
			}
			if (cellValue.startsWith("未")) {
				cellValue = "";
			}
			if (cellValue.endsWith("退")) {
				cellValue = "";
			}
			if ("退学".equals(cellValue)) {
				cellValue = "";
			}
			if (cellValue.startsWith("转")) {
				cellValue = "";
			}
			if ("成绩".equals(cellValue)) {
				cellValue = "";
			}
			if ("学籍".equals(cellValue)) {
				cellValue = "";
			}
			if ("学点".equals(cellValue)) {
				cellValue = "";
			}
			if ("放弃".equals(cellValue)) {
				cellValue = "";
			}
			if ("学".equals(cellValue)) {
				cellValue = "";
			}
			if ("没来上课".equals(cellValue)) {
				cellValue = "0";
			}
			if ("风东".equals(cellValue)) {
				cellValue = "0";
			}
			if ("不读".equals(cellValue)) {
				cellValue = "";
			}
			if ("2016.1转教学点".equals(cellValue)) {
				cellValue = "";
			}
			if ("160904".equals(cellValue)) {
				cellValue = "";
			}
			if (cellValue.startsWith("休")) {// 休学的学生，不录入成绩
				cellValue = "";
			}
			if (cellValue.startsWith("待")) {// 休学的学生，不录入成绩
				cellValue = "";
			}
			if (cellValue.endsWith("复学")) {// 休学的学生，不录入成绩
				cellValue = "";
			}
			if (cellValue.startsWith("校本部")) {// 休学的学生，不录入成绩
				cellValue = "";
			}
			if (cellValue.startsWith("第2学期")) {// 休学的学生，不录入成绩
				cellValue = "";
			}
			if (cellValue.contains(".")) {// 小数点4舍5入
				String[] tmp = cellValue.split("\\.");
				if (Integer.valueOf(tmp[1]) >= 5) {
					cellValue = String.valueOf((Integer.valueOf(tmp[0]) + 1));
				} else {
					cellValue = tmp[0];
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cellValue;
	}

	public static List<String> createSql(String studyNostr) {
		// String[] strs = (String[]) studyNos.toArray(new String[0]);
		List<String> returnList = new ArrayList<String>();
		String sql = "select s.resourceid,s.studyno,cl.classicname from edu_roll_studentinfo s join edu_base_classic cl on cl.resourceid = s.classicid where studyno in ("
				+ studyNostr + ")";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			// oracle 驱动版本低，无法使用这个方式
			// Array array = ps.getConnection().createArrayOf("VARCHAR",
			// studyNos.toArray());
			// ps.setArray(1, array);
			// ps.setString(1, "118462417007001");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				returnList.add(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	public static List<String> findRepeats() {
		// String[] strs = (String[]) studyNos.toArray(new String[0]);
		List<String> returnList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.studyno from tmp_score t where t.studyno in ");
		sb.append(" (Select t1.studyno from (select studyno,studentname  ");
		sb.append(" from tmp_score group by studyno,studentname) t1      ");
		sb.append("  Group by t1.studyno Having count(t1.studentname)>1) ");
		sb.append(" group by t.studyno                                   ");
		sb.append(" order by t.studyno                                   ");
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sb.toString());
			// oracle 驱动版本低，无法使用这个方式
			// Array array = ps.getConnection().createArrayOf("VARCHAR",
			// studyNos.toArray());
			// ps.setArray(1, array);
			// ps.setString(1, "118462417007001");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				returnList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}
	/**
	 * 原始成绩
	 * @param list
	 * @return
	 */
	public static boolean insertExamResults(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		// 注意不要带 ;
		sb.append("truncate table tmp_score");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" insert into tmp_score (resourceid,filename,sheetname,studyno,studentname,coursename,yearinfo,term,score) ");
		sb1.append(" values (?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				ps1.setString(1, String.valueOf(index));
				ps1.setString(2, (String) map.get("fileName"));
				ps1.setString(3, (String) map.get("sheetName"));
				ps1.setString(4, (String) map.get("studyno"));
				ps1.setString(5, (String) map.get("studentName"));
				ps1.setString(6, (String) map.get("courseName"));
				ps1.setString(7, (String) map.get("year"));
				ps1.setString(8, (String) map.get("term"));
				ps1.setString(9, (String) map.get("score"));

				ps1.addBatch();
				index++;
			}
			ps1.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	/**
	 * 正考成绩
	 * @param list
	 * @return
	 */
	public static boolean insertExamResultsZ(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		// 注意不要带 ;
		sb.append("truncate table tmp_score_z");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" insert into tmp_score_z (resourceid,filename,sheetname,studyno,studentname,coursename,yearinfo,term,score) ");
		sb1.append(" values (?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				ps1.setString(1, (String) map.get("resourceid"));
				ps1.setString(2, (String) map.get("fileName"));
				ps1.setString(3, (String) map.get("sheetName"));
				ps1.setString(4, (String) map.get("studyNo"));
				ps1.setString(5, (String) map.get("studentName"));
				ps1.setString(6, (String) map.get("courseName"));
				ps1.setString(7, (String) map.get("year"));
				ps1.setString(8, (String) map.get("term"));
				ps1.setString(9, (String) map.get("score"));

				ps1.addBatch();
				index++;
			}
			ps1.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	/**
	 * 补考成绩
	 * @param list
	 * @return
	 */
	public static boolean insertExamResultsY(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		// 注意不要带 ;
		sb.append("truncate table tmp_score_y");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" insert into tmp_score_y (resourceid,filename,sheetname,studyno,studentname,coursename,yearinfo,term,score) ");
		sb1.append(" values (?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				ps1.setString(1, String.valueOf(index));
				ps1.setString(2, (String) map.get("fileName"));
				ps1.setString(3, (String) map.get("sheetName"));
				ps1.setString(4, (String) map.get("studyNo"));
				ps1.setString(5, (String) map.get("studentName"));
				ps1.setString(6, (String) map.get("courseName"));
				ps1.setString(7, (String) map.get("year"));
				ps1.setString(8, (String) map.get("term"));
				ps1.setString(9, (String) map.get("score"));

				ps1.addBatch();
				index++;
			}
			ps1.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	/**
	 * 免考
	 * @param list
	 * @return
	 */
	public static boolean insertExamResultsN(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		// 注意不要带 ;
		sb.append("truncate table tmp_score_n");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" insert into tmp_score_n (resourceid,filename,sheetname,studyno,studentname,coursename,yearinfo,term,score) ");
		sb1.append(" values (?,?,?,?,?,?,?,?,?)");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				ps1.setString(1, String.valueOf(index));
				ps1.setString(2, (String) map.get("fileName"));
				ps1.setString(3, (String) map.get("sheetName"));
				ps1.setString(4, (String) map.get("studyno"));
				ps1.setString(5, (String) map.get("studentName"));
				ps1.setString(6, (String) map.get("courseName"));
				ps1.setString(7, (String) map.get("year"));
				ps1.setString(8, (String) map.get("term"));
				ps1.setString(9, (String) map.get("score"));

				ps1.addBatch();
				index++;
			}
			ps1.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	/**
	 * 补考名单
	 * @param list
	 * @return
	 */
	public static boolean insertExamResultsM(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		// 注意不要带 ;
		sb.append("truncate table tmp_score_m");
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" insert into tmp_score_m (resourceid,studyno,coursename,yearinfo,term,score) ");
		sb1.append(" values (?,?,?,?,?,?)");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				ps1.setString(1, String.valueOf(index));
				ps1.setString(2, (String) map.get("studyNo"));
				ps1.setString(3, (String) map.get("courseName"));
				ps1.setString(4, (String) map.get("year"));
				ps1.setString(5, (String) map.get("term"));
				ps1.setString(6, (String) map.get("score"));
				//ps1.setString(10, String.valueOf((Integer) map.get("type")));
				ps1.addBatch();
				index++;
			}
			ps1.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	private static String createExamInfoSql(List<Map<String, Object>> list,
			String examType) {

		HashSet<Map<String, Object>> set = new HashSet<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : list) {
			String studyno = (String) map.get("studyno");
			String year = (String) map.get("year");
			String courseName = (String) map.get("courseName");
			String term = String.valueOf(map.get("term"));

			if (!"N".equalsIgnoreCase(examType)) {
				if ("2".equals(term)) {// 第二学期的，学期加1
					year = String.valueOf((Integer.valueOf(year) + 1));
					term = "1";
				} else {
					term = "2";
				}
			}
			// 考试信息表
			Map<String, Object> mapExamInfo = new HashMap<String, Object>();
			mapExamInfo.put("courseName", courseName);
			mapExamInfo.put("term", String.valueOf(map.get("term")));
			mapExamInfo.put("year", String.valueOf(map.get("year")));
			if (set.add(mapExamInfo)) {// 去重
				sb.append(" insert into edu_teach_examinfo (RESOURCEID, EXAMSUBID, COURSEID, ISOUTPLANCOURSE, MEMO, ");
				sb.append(" VERSION, ISDELETED,  EXAMCOURSETYPE,  ISMACHINEEXAM, ISSHOWSCORE, STUDYSCOREPER, ISABNORMITYEND, ");
				sb.append("ISMIXTURE,  COURSESCORETYPE, FACESTUDYSCOREPER, FACESTUDYSCOREPER2,TEACHTYPE) ");
				sb.append(" select sys_guid(),es.resourceid,c.resourceid,0,'后台导入',0,0,1,'N','N',60,'N','N','11',60,40,1 ");
				sb.append(" from edu_teach_examsub es,edu_base_year y ,edu_roll_studentinfo s, edu_base_classic cl,edu_base_course c ");
				sb.append(" where y.resourceid = es.yearid and y.firstyear = "
						+ year + " and es.term ='" + term
						+ "' AND ES.EXAMTYPE = '" + examType
						+ "' and  cl.resourceid = s.classicid ");
				sb.append(" and substr(c.coursecode,3,1)=cl.classiccode and c.coursename = '"
						+ courseName
						+ "' and s.studyno = '"
						+ studyno
						+ "'; \r\n ");
			}

		}
		// 强行修正
		List<Map<String, Object>> manual = new ArrayList<Map<String, Object>>();
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("year", 2017);
		m1.put("term", 2);
		m1.put("classicCode", "2");
		m1.put("courseName", "基础会计");
		manual.add(m1);
		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("year", 2016);
		m2.put("term", 2);
		m2.put("classicCode", "2");
		m2.put("courseName", "行政管理学");
		manual.add(m2);
		Map<String, Object> m3 = new HashMap<String, Object>();
		m3.put("year", 2019);
		m3.put("term", 1);
		m3.put("classicCode", "2");
		m3.put("courseName", "公共关系");
		manual.add(m3);
		Map<String, Object> m4 = new HashMap<String, Object>();
		m4.put("year", 2018);
		m4.put("term", 1);
		m4.put("classicCode", "2");
		m4.put("courseName", "电子商务概论");
		manual.add(m4);
		Map<String, Object> m5 = new HashMap<String, Object>();
		m5.put("year", 2019);
		m5.put("term", 2);
		m5.put("classicCode", "2");
		m5.put("courseName", "公共关系");
		manual.add(m3);
		for (Map<String, Object> map : manual) {
			sb.append(" insert into edu_teach_examinfo (RESOURCEID, EXAMSUBID, COURSEID, ISOUTPLANCOURSE, MEMO, ");
			sb.append(" VERSION, ISDELETED,  EXAMCOURSETYPE,  ISMACHINEEXAM, ISSHOWSCORE, STUDYSCOREPER, ISABNORMITYEND, ");
			sb.append("ISMIXTURE,  COURSESCORETYPE, FACESTUDYSCOREPER, FACESTUDYSCOREPER2,TEACHTYPE) ");
			sb.append(" select sys_guid(),es.resourceid,c.resourceid,0,'后台导入',0,0,1,'N','N',60,'N','N','11',60,40,1 ");
			sb.append(" from edu_teach_examsub es,edu_base_year y , edu_base_course c ");
			sb.append(" where y.resourceid = es.yearid and y.firstyear = "
					+ map.get("year") + " and es.term ='" + map.get("term")
					+ "' AND ES.EXAMTYPE = '" + examType + "'");
			sb.append(" and substr(c.coursecode,3,1)='"
					+ map.get("classicCode") + "' and c.coursename='"
					+ map.get("courseName") + "'; \r\n ");
		}

		return sb.toString();
	}

	private static String createExamSql(List<Map<String, Object>> list,
			String examType) {
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : list) {
			String studyno = (String) map.get("studyno");
			String year = (String) map.get("year");
			String courseName = (String) map.get("courseName");
			String term = String.valueOf(map.get("term"));
			String score = (String) map.get("score");
			String ab = "0";
			String tmpScore = " f_encrypt_score('" + score + "',s.resourceid) ";
			if ("缺考".equals(score)) {
				ab = "2";
				tmpScore = null;
			}
			if ("缓考".equals(score)) {
				ab = "5";
				tmpScore = null;
			}
			String isMakeup = "N";
			if (!"N".equalsIgnoreCase(examType)) {
				if ("2".equals(term)) {// 第二学期的，学期加1
					year = String.valueOf((Integer.valueOf(year) + 1));
					term = "1";
				} else {
					term = "2";
				}
				isMakeup = "Y";
			}
			String courseStr = " and c.coursename='" + courseName + "' ";

			if ("基础会计学".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='基础会计')";
			}
			if ("公共关系与商务礼仪".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='公共关系')";
			}
			if ("市场营销学".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='市场营销')";
			}
			if ("跨境电子商务".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='电子商务概论')";
			}
			sb.append("insert into edu_teach_examresults (RESOURCEID, STUDENTID, COURSEID, MAJORCOURSEID, EXAMINFOID,");
			sb.append("ISOUTPLANCOURSE, COURSESCORETYPE, WRITTENSCORE, USUALLYSCORE, INTEGRATEDSCORE,");
			sb.append("EXAMABNORMITY, ISDELAYEXAM, EXAMCOUNT, CHECKSTATUS, VERSION, ISDELETED, EXAMSUBID,");
			sb.append("EXAMTYPE,coursetype, ISMAKEUPEXAM,ismachineexam,  PLANCOURSETEACHTYPE)");
			sb.append("select sys_guid(),s.resourceid,c.resourceid,pc.resourceid,ef.resourceid,");
			sb.append("'N', '11', " + tmpScore + ", " + tmpScore + ", "
					+ tmpScore + ",");
			sb.append(ab + ",'N', 1, '4',0,0, es.resourceid,1,'11','"
					+ isMakeup + "','N','facestudy'");
			sb.append(" from edu_teach_examsub es,edu_base_year y ,edu_roll_studentinfo s, edu_base_classic cl,edu_base_course c, ");
			sb.append(" edu_teach_examinfo ef,edu_teach_plan p,edu_teach_plancourse pc ");
			sb.append(" where y.resourceid = es.yearid and y.firstyear = '"
					+ year + "' and es.term = '" + term + "' ");
			sb.append(" AND ES.EXAMTYPE = '"
					+ examType
					+ "' and cl.resourceid = s.classicid  and substr(c.coursecode, 3, 1) = cl.classiccode ");
			sb.append(courseStr + " and s.studyno = '" + studyno + "'  ");
			sb.append(" and ef.examsubid = es.resourceid and ef.teachtype = '1' ");
			sb.append(" and ef.courseid = c.resourceid and s.teachplanid = pc.planid and pc.courseid = c.resourceid ");
			sb.append(" group by s.resourceid,es.resourceid,c.resourceid,ef.resourceid,pc.resourceid;\r\n");
		}

		return sb.toString();
	}

	public static boolean CIExamSql(List<Map<String, Object>> list,
			String examType) {
		StringBuffer sb = new StringBuffer();
		// 注意不要带 ;
		// sb.append("truncate table edu_teach_examresults\r\n");
		//1、正考时，清空表中的成绩数据
		sb.append("delete from edu_teach_examresults e where e.resourceid is not null");
		StringBuffer sb1 = new StringBuffer();
		sb1.append("insert into edu_teach_examresults (RESOURCEID, STUDENTID, COURSEID, MAJORCOURSEID, EXAMINFOID,");
		sb1.append("ISOUTPLANCOURSE, COURSESCORETYPE, WRITTENSCORE, USUALLYSCORE, INTEGRATEDSCORE,");
		sb1.append("EXAMABNORMITY, ISDELAYEXAM, EXAMCOUNT, CHECKSTATUS, VERSION, ISDELETED, EXAMSUBID,");
		sb1.append("EXAMTYPE,coursetype, ISMAKEUPEXAM,ismachineexam,  PLANCOURSETEACHTYPE)");
		sb1.append("select sys_guid(),s.resourceid,c.resourceid,pc.resourceid,ef.resourceid,");
		sb1.append("'N', '11', f_encrypt_score(?,s.resourceid), f_encrypt_score(?,s.resourceid), f_encrypt_score(?,s.resourceid) ,");
		sb1.append("?,'N', 1, '4',0,0, es.resourceid,1,'11',?,'N','facestudy'");
		sb1.append(" from edu_teach_examsub es,edu_base_year y ,edu_roll_studentinfo s, edu_base_classic cl,edu_base_course c, ");
		sb1.append(" edu_teach_examinfo ef,edu_teach_plan p,edu_teach_plancourse pc ");
		sb1.append(" where y.resourceid = es.yearid and y.firstyear = ? and es.term =?");
		sb1.append(" AND ES.EXAMTYPE = ? and cl.resourceid = s.classicid  and substr(c.coursecode, 3, 1) = cl.classiccode ");
		// sb1.append("? and s.studyno = ? ");
		sb1.append("and (coursename=? or coursename=?) and s.studyno = ? ");
		sb1.append(" and ef.examsubid = es.resourceid and ef.teachtype = '1' ");
		sb1.append(" and ef.courseid = c.resourceid and s.teachplanid = pc.planid and pc.courseid = c.resourceid ");
		sb1.append(" group by s.resourceid,es.resourceid,c.resourceid,ef.resourceid,pc.resourceid\r\n");
		PreparedStatement ps, ps1;
		boolean isSuccess = true;

		try {
			ps = con.prepareStatement(sb.toString());
			if("N".equalsIgnoreCase(examType)){
				ps.executeUpdate();
			}
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb1.toString());
			for (Map<String, Object> map : list) {
				String studyno = (String) map.get("studyNo");
				String year = (String) map.get("year");
				String courseName = (String) map.get("courseName");
				String term = String.valueOf(map.get("term"));
				String score = (String) map.get("score");
				String ab = "0";
				if ("缺考".equals(score) || "缺".equals(score)) {
					ab = "2";
					score = null;
				} else if ("缓考".equals(score)) {
					ab = "5";
					score = null;
				}
				String isMakeup = "N";
				if (!"N".equalsIgnoreCase(examType)) {
					if ("2".equals(term)) {// 第二学期的，学期加1
						year = String.valueOf((Integer.valueOf(year) + 1));
						term = "1";
					} else {
						term = "2";
					}
					isMakeup = "Y";
				}
				String courseName1 = courseName;
				if ("基础会计学".equals(courseName)) {
					courseName1 = "基础会计";
				}
				if ("公共关系与商务礼仪".equals(courseName)) {
					courseName1 = "公共关系";
				}
				if ("市场营销学".equals(courseName)) {
					courseName1 = "市场营销";
				}
				if ("跨境电子商务".equals(courseName)) {
					courseName1 = "电子商务概论";
				}
				ps1.setString(1, score);
				ps1.setString(2, score);
				ps1.setString(3, score);
				ps1.setInt(4, Integer.valueOf(ab));
				ps1.setString(5, isMakeup);
				ps1.setInt(6, Integer.valueOf(year));
				ps1.setInt(7, Integer.valueOf(term));
				ps1.setString(8, examType);
				ps1.setString(9, courseName);
				ps1.setString(10, courseName1);
				ps1.setString(11, studyno);
				ps1.addBatch();
			}
			System.out.println("      ############批量执行成绩导入:"+examType+"##############");
			//2、导入成绩数据
			int[] arrays=ps1.executeBatch();
//			for (int i = 0; i < arrays.length; i++) {
//				if (arrays[i] != (-2)) {
//					System.out.println("第（" + (i + 1) + "） 行数据插入错误");
//				}
//			}
			con.commit();
			System.out.println("      ############执行完毕:"+examType+"##############");
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static String createMLSql(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : list) {
			int examType = (Integer) map.get("type");
			String studyno = (String) map.get("studyno");
			String courseName = (String) map.get("courseName");
			String year = (String) map.get("year");
			String term = String.valueOf(map.get("term"));
			String year1 = year;
			String term1 = term;
			String examTypeStr1 = "N";
			String examTypeStr2 = "Y";
			if (examType == 1) {// 一补
				if ("2".equals(term)) {// 第二学期的，学期加1
					year1 = String.valueOf((Integer.valueOf(year) + 1));
					term1 = "1";
				} else {
					term = "2";
				}
			} else {
				examTypeStr1 = "Y";
				examTypeStr2 = "T";
				year1 = String.valueOf((Integer.valueOf(year) + 2));
				term1 = "2";
			}
			String courseStr = " and c.coursename='" + courseName + "' ";

			if ("基础会计学".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='基础会计')";
			}
			if ("公共关系与商务礼仪".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='公共关系')";
			}
			if ("市场营销学".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='市场营销')";
			}
			if ("跨境电子商务".equals(courseName)) {
				courseStr = " and (c.coursename='" + courseName
						+ "' or c.coursename='电子商务概论')";
			}
			sb.append(" insert into edu_teach_makeuplist (RESOURCEID, STUDENTID, COURSEID, VERSION, ISDELETED,");
			sb.append("  RESULTSID, PLANSOURCEID, NEXTEXAMSUBID, ISPASS,ISMACHINEEXAM) ");
			sb.append(" select sys_guid(),s.resourceid,c.resourceid,0,0,");
			sb.append(" er.resourceid,pc.resourceid,es1.resourceid,'N','N' ");
			sb.append(" from edu_teach_examsub es,edu_base_year y ,edu_roll_studentinfo s, edu_base_classic cl,");
			sb.append(" edu_base_course c,edu_teach_plan p,edu_teach_plancourse pc,");
			sb.append(" edu_teach_examresults er ,edu_teach_examsub es1,edu_base_year y1  ");
			sb.append("  where y.resourceid = es.yearid and y.firstyear = "
					+ year + " and es.term = '" + term + "'");
			sb.append("  AND ES.EXAMTYPE = '" + examTypeStr1
					+ "' and cl.resourceid = s.classicid  and ");
			sb.append("  substr(c.coursecode, 3, 1) = cl.classiccode "
					+ courseStr);
			sb.append("  and s.studyno = '" + studyno
					+ "' ");
			sb.append("   and s.teachplanid = pc.planid");
			sb.append("  and pc.courseid = c.resourceid and es1.examtype = '"
					+ examTypeStr2 + "' and es1.yearid = y1.resourceid");
			sb.append("  and y1.firstyear = " + year1 + " and es1.term = '"
					+ term1 + "' and er.majorcourseid = pc.resourceid");
			sb.append("  and er.studentid = s.resourceid and er.ismakeupexam = '"+examTypeStr1+"' ");
			sb.append("  group by s.resourceid,es.resourceid,c.resourceid,");
			sb.append("  pc.resourceid,es1.resourceid,er.resourceid;\r\n");
		}
		return sb.toString();
	}

	private static void CIMLSql(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" delete from edu_teach_makeuplist where resourceid is not null");
		sb.append(" insert into edu_teach_makeuplist (RESOURCEID, STUDENTID, COURSEID, VERSION, ISDELETED,");
		sb.append("  RESULTSID, PLANSOURCEID, NEXTEXAMSUBID, ISPASS,ISMACHINEEXAM) ");
		sb.append(" select sys_guid(),s.resourceid,c.resourceid,0,0,");
		sb.append(" er.resourceid,pc.resourceid,es1.resourceid,'N','N' ");
		sb.append(" from edu_teach_examsub es,edu_base_year y ,edu_roll_studentinfo s, edu_base_classic cl,");
		sb.append(" edu_base_course c,edu_teach_plan p,edu_teach_plancourse pc,");
		sb.append(" edu_teach_examresults er ,edu_teach_examsub es1,edu_base_year y1  ");
		sb.append("  where y.resourceid = es.yearid and y.firstyear = ? and es.term = ?");
		sb.append("  AND ES.EXAMTYPE = ? and cl.resourceid = s.classicid  and ");
		sb.append("  substr(c.coursecode, 3, 1) = cl.classiccode and (courseName=? or coursename=?) ");
		sb.append("  and s.studyno = ? ");
		sb.append("  and s.teachplanid = pc.planid");
		sb.append("  and pc.courseid = c.resourceid and es1.examtype = ? and es1.yearid = y1.resourceid");
		sb.append("  and y1.firstyear =? and es1.term = ? and er.majorcourseid = pc.resourceid");
		sb.append("  and er.studentid = s.resourceid and er.ismakeupexam = ? ");
		sb.append("  group by s.resourceid,es.resourceid,c.resourceid,");
		sb.append("  pc.resourceid,es1.resourceid,er.resourceid ");
		PreparedStatement ps,ps1;
		try {
			ps = con.prepareStatement(sb1.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb.toString());
			for (Map<String, Object> map : list) {
				String studyno = (String) map.get("studyNo");
				String courseName = (String) map.get("courseName");
				int year =  Integer.valueOf((String) map.get("year"));
				String term = String.valueOf(map.get("term"));
				int year1 = Integer.valueOf(String.valueOf(map.get("gradeName")).substring(0,4));
				String term1 = term;
				String examTypeStr1 = "N";
				String examTypeStr2 = "Y";
				examTypeStr1 = "Y";
				examTypeStr2 = "T";
				if("1".equals(term)){
					term = "2";
					
				}else{
					term="1";
					year=year+1;
				}
				
				year1 = year1+2;
				term1 = "2";
				String courseName1 = courseName;
				if ("基础会计学".equals(courseName)) {
					courseName1 = "基础会计";
				}
				if ("公共关系与商务礼仪".equals(courseName)) {
					courseName1 = "公共关系";
				}
				if ("市场营销学".equals(courseName)) {
					courseName1 = "市场营销";
				}
				if ("跨境电子商务".equals(courseName)) {
					courseName1 = "电子商务概论";
				}
				ps1.setInt(1, year);
				ps1.setString(2, term);
				ps1.setString(3, examTypeStr1);
				ps1.setString(4, courseName);
				ps1.setString(5, courseName1);
				ps1.setString(6, studyno);
				ps1.setString(7, examTypeStr2);
				ps1.setInt(8, year1);
				ps1.setString(9, term1);
				ps1.setString(10, examTypeStr1);
				ps1.addBatch();
			}
			
			System.out.println("      ############批量执行补考名单##############");
			int[] arrays = ps1.executeBatch();
			
			con.commit();
			System.out.println("      ############执行完毕##############");
			con.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void CINoexamSql(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb1.append("delete from edu_teach_noexam e where e.resourceid is not null");
		sb.append(" insert into edu_teach_noexam (RESOURCEID, STUDENTID, COURSEID,  COURSESCORETYPE,  SUBJECTTIME, ");
		sb.append(" CHECKSTATUS, CHECKMAN, CHECKTIME, MEMO,   UNSCORE, VERSION, ISDELETED, CHECKMANID) ");
		sb.append(" select sys_guid(), s.resourceid, c.resourceid, '11',  to_date('20-12-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),");
		sb.append(" '1', '超级管理员',  to_date('21-12-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), '无', '1', 0, '0',");
		sb.append(" 'ff80808107e784fa0107e9234c78012e' from edu_roll_studentinfo s ,edu_base_course c,edu_base_classic cl ");
		sb.append(" where s.classicid = cl.resourceid and substr(c.coursecode, 3, 1)=cl.classiccode ");
		sb.append(" and c.coursename = ?and s.studyno = ?");
		sb.append(" group by s.resourceid, c.resourceid");
		PreparedStatement ps,ps1 = null;

		try {
			ps = con.prepareStatement(sb1.toString());
			ps.executeUpdate();
			con.setAutoCommit(false);
			ps1 = con.prepareStatement(sb.toString());
			for (Map<String, Object> map : list) {
				String studyno = (String) map.get("studyNo");
				String courseName = (String) map.get("courseName");
				ps1.setString(1, courseName);
				ps1.setString(2, studyno);
				ps1.addBatch();
			}
			System.out.println("       ##########执行免考成绩导入#############");
			int[] arrays = ps1.executeBatch();

			for (int i = 0; i < arrays.length; i++) {
				if (arrays[i] != (-2)) {

					System.out.println("第（" + (i + 1) + "） 行数据插入错误");
				}
			}
			con.commit();
			System.out.println("       ##########执行免考成绩导入完毕#############");
			con.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String createNoExamSql(List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : list) {
			String studyno = (String) map.get("studyno");
			String courseName = (String) map.get("courseName");
			sb.append(" insert into edu_teach_noexam (RESOURCEID, STUDENTID, COURSEID,  COURSESCORETYPE,  SUBJECTTIME, ");
			sb.append(" CHECKSTATUS, CHECKMAN, CHECKTIME, MEMO,   UNSCORE, VERSION, ISDELETED, CHECKMANID) ");
			sb.append(" select sys_guid(), s.resourceid, c.resourceid, '11',  to_date('20-12-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),");
			sb.append(" '1', '超级管理员',  to_date('21-12-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), '无', '1', 0, '0',");
			sb.append(" 'ff80808107e784fa0107e9234c78012e' from edu_roll_studentinfo s ,edu_base_course c,edu_base_classic cl ");
			sb.append(" where s.classicid = cl.resourceid and substr(c.coursecode, 3, 1)=cl.classiccode ");
			sb.append(" and c.coursename = '" + courseName
					+ "' and s.studyno = '" + studyno + "'");
			sb.append(" group by s.resourceid, c.resourceid; \r\n");
		}

		return sb.toString();
	}

	public static List<String> findCourseSql(String courseNames) {
		List<String> returnList = new ArrayList<String>();
		String sql = "select distinct coursecode,resourceid,''''||courseName||'''' from edu_base_course where courseName in ("
				+ courseNames + ")";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				returnList.add(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	/**
	 * @param listAll1
	 * @param successNos
	 */
	private static void export2Excel(List<Map<String, Object>> listAll1,
			List<String> successNos) {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();

		int scoreCount = 0;
		for (int i = 1; i < listAll1.size() + 1; i++) {
			// System.out.println(listAll.get(i));
			// 进行学号过滤
			if (successNos.contains(listAll1.get(i - 1).get("studyno")
					.toString())) {
				scoreCount++;
				XSSFRow row = sheet.createRow(scoreCount);
				for (int j = 0; j < 4; j++) {
					XSSFCell cell = row.createCell(j);
					switch (j) {
					case 0:
						cell.setCellValue(listAll1.get(i - 1).get("studyno")
								.toString());
						break;
					case 1:
						cell.setCellValue(listAll1.get(i - 1).get("term")
								.toString());
						break;
					case 2:
						cell.setCellValue(listAll1.get(i - 1).get("score")
								.toString());
						break;
					default:
						cell.setCellValue(listAll1.get(i - 1).get("courseName")
								.toString());
						break;
					}
				}
			}
		}
		System.out.println("过滤学号后，共有：" + scoreCount + "条成绩记录");
		FileOutputStream fileOut;
		String filePath = "E:\\SCORE.xlsx";

		try {
			fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param listAll
	 * @param successNos
	 * @param listZ
	 * @param listY
	 * @param listN
	 * @param listM
	 * @throws NumberFormatException
	 */
	private static void getLists(List<Map<String, Object>> listAll,
			List<Map<String, Object>> listZ,
			List<Map<String, Object>> listY, List<Map<String, Object>> listN,
			List<Map<String, Object>> listM) throws NumberFormatException {
		for (int i = 0; i < listAll.size(); i++) {
//			if (successNos.contains(listAll.get(i).get("studyno").toString())) {
				String score = listAll.get(i).get("score").toString();
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> map2 = new HashMap<String, Object>();
				Map<String, Object> map3 = new HashMap<String, Object>();
				map.putAll(listAll.get(i));
				map2.putAll(listAll.get(i));
				map3.putAll(listAll.get(i));
				if (score.contains("/")) {
					map.put("score", score.split("/")[0]);
					listZ.add(map);
					map2.put("score", score.split("/")[1]);
					listY.add(map2);
					continue;
				}
				if (score.contains("、")) {
					map.put("score", score.split("、")[0]);
					listZ.add(map);
					map2.put("score",
							score.split("、")[1].replaceAll("（补考）", ""));
					listY.add(map2);
					continue;
				}
				if ("免考".equals(score)) {
					listN.add(map);
					continue;
				}
				if (score.startsWith("60（") || score.startsWith("60补")
						|| score.startsWith("补60")) {
					map.put("score", "0");
					listZ.add(map);
					map2.put("score", "60");
					listY.add(map2);
					continue;
				}
				if ("60（补）".equals(score)) {
					map.put("score", "0");
					listZ.add(map);
					map2.put("score", "60");
					listY.add(map2);
					continue;
				}
				if ("缺考".equals(score) || "缺".equals(score)) {
					listZ.add(map);
					map2.put("score", "0");
//					listY.add(map2);
					map3.put("type", 2);// type 类型 二补
					listM.add(map3);
					continue;
				}
				if (score.startsWith("缓")) {
					map.put("score", "缓考");
					listZ.add(map);
//					map2.put("type", 1);// type 类型 一补
//					listM.add(map2);
					continue;
				}
				if (Integer.valueOf(score) < 60) {
					listZ.add(map);
//					map2.put("score", "0");
//					listY.add(map2);
					map3.put("type", 2);// type 类型 二补
					listM.add(map3);
					continue;
				}
				if (Integer.valueOf(score) >= 60) {
					listZ.add(map);
					continue;
				}
//			}
		}
	}

	/**
	 * @param parentPath
	 * @return
	 */
	private static List<String> getAllfilePaths(String parentPath) {
		File file = new File(parentPath);// 获取某个目录下的所有文件名
		String[] test;
		test = file.list();
		List<String> filepaths = new ArrayList<String>();
		// 遍历文件夹
		for (int i = 0; i < test.length; i++) {
			if (test[i].endsWith(".xls") || test[i].endsWith(".xlsx")) {
				filepaths.add(parentPath + test[i]);
			} else {
				File file1 = new File(parentPath + test[i] + "\\");
				String[] test1;
				test1 = file1.list();
				if (test1 != null) {
					for (int j = 0; j < test1.length; j++) {
						filepaths.add(parentPath + test[i] + "\\" + test1[j]);
					}
				}

			}

		}
		return filepaths;
	}

	public static void main(String[] avgs) throws Exception {
		System.out.println("程序开始执行"+ExDateUtils.getCurrentDateTimeStr());
		String parentPath = "E:\\需导入学苑教务系统16级17级成绩汇总\\";
		NewExcelUtil4Score eu = new NewExcelUtil4Score();
		List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
		// 读取excel
		List<String> filepaths = getAllfilePaths(parentPath);
		for (int k = 0; k < filepaths.size(); k++) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list = readExcel(filepaths.get(k));
			listAll.addAll(list);
		}
		//插入原始成绩
		insertExamResults(listAll);
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		//去掉重复的学号、课程
		updateMemo();
		allList=selectNoRepeat();
		// 正考的list
		List<Map<String, Object>> listZ = new ArrayList<Map<String, Object>>();
		// 一补的list
		List<Map<String, Object>> listY = new ArrayList<Map<String, Object>>();
		// 免考的list
		List<Map<String, Object>> listN = new ArrayList<Map<String, Object>>();
		// 补考名单的list
		List<Map<String, Object>> listM = new ArrayList<Map<String, Object>>();

		getLists(allList, listZ, listY, listN, listM);
		//把数据临时表中
		insertExamResultsZ(listZ);
		insertExamResultsY(listY);
		insertExamResultsN(listN);
		
		
		System.out.println("正考成绩表数据共："+listZ.size()+" 条");
		System.out.println("补考成绩表数据共："+listY.size()+" 条");
		System.out.println("免考成绩表数据共："+listN.size()+" 条");
		System.out.println("补考名单表数据共："+listM.size()+" 条");
		// 1、考试信息表		
		System.out
				.println("######################开始批量生成并执行SQL######################"+ExDateUtils.getCurrentDateTimeStr());
		long st1 = System.currentTimeMillis();
		//正考考试信息表
		insertExamInfoZ();
		//正考
		CIExamSql(listZ, "N");
		//免考
		CINoexamSql(listN);
		//正考插入不成功名单
		updateMemoZ();
		//补考考试信息
		insertExamInfoY();
		//补考
		CIExamSql(listY, "Y");
		//补考插入不成功名单
		updateMemoY();
		List<Map<String, Object>> listY0 = new ArrayList<Map<String,Object>>();
		listY0=selectList();
		//补考名单
		insertExamResultsM(listY0);
		//添加补考成绩为0
		CIExamSql(listY0, "Y");
		//进入二补名单
		CIMLSql(listY0);
		long et2 = System.currentTimeMillis();
		System.out
				.println("---------------------------执行完毕----------------------------"+ExDateUtils.getCurrentDateTimeStr());
		System.out.println("执行 完毕！共花去（ " + (et2 - st1)
				+ "） 毫秒");

		con.close();
//		String examInfoSql = createExamInfoSql(listZ, "N");
//		examInfoSql += createExamInfoSql(listY, "Y");
//		String examInfoPath = "E:\\examInfoSql.txt";
//		FileUtils.createFile(examInfoPath, examInfoSql, "GB2312");
//		// 2、正考成绩
//		String examSql = createExamSql(listZ, "N");
//		String examPath = "E:\\examSql.txt";
//		FileUtils.createFile(examPath, examSql, "GB2312");
//		// 3、免考成绩
//		String noExamSql = createNoExamSql(listN);
//		String noExamPath = "E:\\noExamSql.txt";
//		FileUtils.createFile(noExamPath, noExamSql, "GB2312");
//		// 4、一补成绩
//		String YexamSql = createExamSql(listY, "Y");
//		String YexamPath = "E:\\YexamSql.txt";
//		FileUtils.createFile(YexamPath, YexamSql, "GB2312");
//		// 5、补考名单
//		String mlSql = createMLSql(listM);
//		String mlPath = "E:\\mlSql.txt";
//		FileUtils.createFile(mlPath, mlSql, "GB2312");
		// export2Excel(listAll1, successNos);
		System.out
				.println("#########################  all done!  ############################");
	}

	private static List<Map<String,Object>> selectList() {
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from (                                                                                                     "); 
		sb.append(" select g.gradename,s.studyno,pc.resourceid,y.firstyear,es.term,c.coursecode,c.coursename,                           ");
		sb.append(" f_decrypt_score( e.integratedscore,e.studentid),e.examabnormity,ne.studentid,                                       ");
		sb.append(" f_decrypt_score(e2.integratedscore,e2.studentid) makeup,'0' from edu_teach_examresults e                            ");
		sb.append(" join edu_roll_studentinfo s on s.resourceid = e.studentid                                                           ");
		sb.append(" join edu_teach_plancourse pc on pc.resourceid = e.majorcourseid                                                     ");
		sb.append(" join edu_base_course c on c.resourceid = pc.courseid                                                                ");
		sb.append(" join edu_base_grade g on g.resourceid = s.gradeid                                                                   ");
		sb.append(" join edu_teach_examsub es on es.resourceid = e.examsubid                                                            ");
		sb.append(" join edu_base_year y on y.resourceid = es.yearid                                                                    ");
		sb.append(" left join edu_teach_noexam ne on ne.studentid = s.resourceid and ne.courseid =c.resourceid                          ");
		sb.append(" left join edu_teach_examresults e2 on e2.studentid = e.studentid                                                    ");
		sb.append(" and e2.majorcourseid = e.majorcourseid and e2.ismakeupexam = 'Y'                                                    ");
		sb.append(" where to_number(f_decrypt_score( e.integratedscore,e.studentid))<60 or e.examabnormity != 0 and e.ismakeupexam = 'N'");
		sb.append(" ) where  makeup is null or studentid is not null order by gradename                                                 ");
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("gradeName", rs.getString(1));
				map.put("studyNo", rs.getString(2));
				
				map.put("courseName", rs.getString(7));
				map.put("year", rs.getString(4));
				map.put("term", rs.getString(5));
				map.put("score", rs.getString(12));
				returnList.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnList;
	}

	private static void updateMemoY() {
		StringBuffer sb = new StringBuffer();
		sb.append(" update tmp_score_z tz set tz.memo = '未找到学生的课程' where tz.resourceid in (                                 ");
		sb.append(" select resourceid from (                                                                                        ");
		sb.append(" select t.resourceid,t.studyno,t.coursename,t.score,t.yearinfo,t.term,tmp.studyno tmpstudyno from tmp_score_y t  ");
		sb.append(" left join (                                                                                                     ");
		sb.append(" select s.studyno,c.coursename,f_decrypt_score(e.integratedscore,e.studentid) score,y.firstyear,es.term          ");
		sb.append(" from edu_teach_examresults e                                                                                    ");
		sb.append(" join edu_roll_studentinfo s on s.resourceid = e.studentid                                                       ");
		sb.append(" join edu_base_course c on c.resourceid = e.courseid                                                             ");
		sb.append(" join edu_teach_examsub es on es.resourceid = e.examsubid                                                        ");
		sb.append(" join edu_base_year y on y.resourceid = es.yearid                                                                ");
		sb.append(" where e.ismakeupexam = 'Y'                                                                                      ");
		sb.append(" ) tmp on tmp.studyno = t.studyno and tmp.coursename = t.coursename and tmp.firstyear                            ");
		sb.append(" =decode(t.term,1, t.yearinfo, t.yearinfo+1)  and tmp.term = decode(t.term,2,1,2)                                ");
		sb.append(" ) where tmpstudyno is null)                                                                                     ");
		PreparedStatement ps2;
		try {
			ps2 = con.prepareStatement(sb.toString());
			ps2.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	private static void updateMemoZ() {
		StringBuffer sb = new StringBuffer();
		sb.append(" update tmp_score_z tz set tz.memo = '未找到学生的课程' where tz.resourceid in (                                         "); 
		sb.append(" select resourceid from (                                                                                                ");
		sb.append(" select t.resourceid,t.studyno,t.coursename,t.score,t.yearinfo,t.term,tmp.studyno tmpstudyno from tmp_score_z t          ");
		sb.append(" left join (                                                                                                             ");
		sb.append(" select s.studyno,c.coursename,f_decrypt_score(e.integratedscore,e.studentid) score,y.firstyear,es.term                  ");
		sb.append(" from edu_teach_examresults e                                                                                            ");
		sb.append(" join edu_roll_studentinfo s on s.resourceid = e.studentid                                                               ");
		sb.append(" join edu_base_course c on c.resourceid = e.courseid                                                                     ");
		sb.append(" join edu_teach_examsub es on es.resourceid = e.examsubid                                                                ");
		sb.append(" join edu_base_year y on y.resourceid = es.yearid                                                                        ");
		sb.append(" ) tmp on tmp.studyno = t.studyno and tmp.coursename = t.coursename and tmp.firstyear = t.yearinfo and tmp.term = t.term ");
		sb.append(" ) where tmpstudyno is null)                                                                                             ");
		PreparedStatement ps2;
		try {
			ps2 = con.prepareStatement(sb.toString());
			ps2.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static void insertExamInfoZ() {
		StringBuffer sb = new StringBuffer();
		//1、查询正常在学、不是未分配教学点的学生的课程，通过group by 去重复
		sb.append(" select coursecode,coursename,classicname,yearinfo,term from (                                           ");
		sb.append(" select c.coursecode,t.*,cl.classicname,s.resourceid sr,c.resourceid cr from tmp_score t                 ");
		sb.append(" left join edu_roll_studentinfo s on s.studyno = t.studyno and                                           ");
		sb.append(" s.studentstatus = '11' and s.branchschoolid !='4a40f0a044289986014429e4d12b0001'                        ");
		sb.append(" left join edu_base_classic cl on cl.resourceid = s.classicid                                            ");
		sb.append(" left join edu_base_course c on c.coursename = t.coursename and substr(c.coursecode,3,1) = cl.classiccode");
		sb.append(" ) tmp where tmp.sr is not null and tmp.cr is not null                                                   ");
		sb.append(" group by coursecode,coursename,classicname,yearinfo,term                                                ");
		sb.append(" order by yearinfo,term                                                                                  ");
		PreparedStatement ps;
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		try {
			ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("courseCode", rs.getString(1));
				map.put("yearInfo", rs.getString(4));
				map.put("term", rs.getString(5));
				returnList.add(map);
			}
			//2、删除表中原来的数据
			StringBuffer sb2 = new StringBuffer();
			sb2.append(" delete from edu_teach_examinfo where resourceid is not null ");
			PreparedStatement ps2;
			ps2 = con.prepareStatement(sb2.toString());
			ps2.executeUpdate();
			//3、将查询到的信息，导入到考试信息表  examinfo
			StringBuffer sb1 = new StringBuffer();
			sb1.append(" insert into edu_teach_examinfo (RESOURCEID, EXAMSUBID, COURSEID, ISOUTPLANCOURSE, MEMO,  VERSION, ISDELETED,  EXAMCOURSETYPE,            ");     
			sb1.append(" ISMACHINEEXAM, ISSHOWSCORE, STUDYSCOREPER, ISABNORMITYEND, ISMIXTURE,  COURSESCORETYPE, FACESTUDYSCOREPER, FACESTUDYSCOREPER2,TEACHTYPE) "); 
			sb1.append(" select sys_guid(),es.resourceid,c.resourceid,0,'后台导入',0,0,1,'N','N',60,'N','N','11',60,40,1  from edu_teach_examsub es,              ");
			sb1.append(" edu_base_year y , edu_base_course c  where y.resourceid = es.yearid and y.firstyear = ? and es.term =? AND ES.EXAMTYPE = 'N'        ");
			sb1.append(" and c.coursecode = ?                                                                                                             ");
			
			PreparedStatement ps1;
			ps1 = con.prepareStatement(sb1.toString());
			for(Map<String,Object> map:returnList){
				ps1.setInt(1, Integer.valueOf((String)map.get("yearInfo")));
				ps1.setString(2, (String)map.get("term"));
				ps1.setString(3, (String)map.get("courseCode"));
				ps1.addBatch();
			}
			con.setAutoCommit(false);
			ps1.executeBatch();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void insertExamInfoY() {
		StringBuffer sb = new StringBuffer();
		//1、查询正常在学、不是未分配教学点的学生的课程，通过group by 去重复
		sb.append(" select coursecode,coursename,classicname,yearinfo,term from (                                           ");
		sb.append(" select c.coursecode,t.*,cl.classicname,s.resourceid sr,c.resourceid cr from tmp_score_z t                 ");
		sb.append(" left join edu_roll_studentinfo s on s.studyno = t.studyno and                                           ");
		sb.append(" s.studentstatus = '11' and s.branchschoolid !='4a40f0a044289986014429e4d12b0001'                        ");
		sb.append(" left join edu_base_classic cl on cl.resourceid = s.classicid                                            ");
		sb.append(" left join edu_base_course c on c.coursename = t.coursename and substr(c.coursecode,3,1) = cl.classiccode");
		sb.append(" ) tmp where tmp.sr is not null and tmp.cr is not null                                                   ");
		sb.append(" group by coursecode,coursename,classicname,yearinfo,term                                                ");
		sb.append(" order by yearinfo,term                                                                                  ");
		PreparedStatement ps;
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		try {
			ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String, Object>();
				String term =rs.getString(5);
				String year =rs.getString(4);
				if ("2".equals(term)) {// 第二学期的，学期加1
					year = String.valueOf((Integer.valueOf(year) + 1));
					term = "1";
				} else {
					term = "2";
				}
				map.put("courseCode", rs.getString(1));
				map.put("yearInfo", year);
				map.put("term", term);
				returnList.add(map);
			}
			StringBuffer sb2 = new StringBuffer();
			sb2.append(" delete from edu_teach_examinfo where memo='一补'");
			PreparedStatement ps2;
			ps2 = con.prepareStatement(sb2.toString());
			ps2.executeUpdate();
			//3、将查询到的信息，导入到考试信息表  examinfo
			StringBuffer sb1 = new StringBuffer();
			sb1.append(" insert into edu_teach_examinfo (RESOURCEID, EXAMSUBID, COURSEID, ISOUTPLANCOURSE, MEMO,  VERSION, ISDELETED,  EXAMCOURSETYPE,            ");     
			sb1.append(" ISMACHINEEXAM, ISSHOWSCORE, STUDYSCOREPER, ISABNORMITYEND, ISMIXTURE,  COURSESCORETYPE, FACESTUDYSCOREPER, FACESTUDYSCOREPER2,TEACHTYPE) "); 
			sb1.append(" select sys_guid(),es.resourceid,c.resourceid,0,'一补',0,0,1,'N','N',60,'N','N','11',60,40,1  from edu_teach_examsub es,              ");
			sb1.append(" edu_base_year y , edu_base_course c  where y.resourceid = es.yearid and y.firstyear = ? and es.term =? AND ES.EXAMTYPE = 'Y'        ");
			sb1.append(" and c.coursecode = ?                                                                                                             ");
			
			PreparedStatement ps1;
			ps1 = con.prepareStatement(sb1.toString());
			for(Map<String,Object> map:returnList){
				ps1.setInt(1, Integer.valueOf((String)map.get("yearInfo")));
				ps1.setString(2, (String)map.get("term"));
				ps1.setString(3, (String)map.get("courseCode"));
				ps1.addBatch();
			}
			con.setAutoCommit(false);
			ps1.executeBatch();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 更新导入不正常的数据的备注信息
	 */
	private static void updateMemo() {
		StringBuffer sb = new StringBuffer();
		sb.append(" update tmp_score tz set tz.memo = '学号或课程不存在' where tz.studyno in (                              ");
		sb.append(" select studyno from (                                                                                   ");
		sb.append(" select c.coursecode,t.*,cl.classicname,s.resourceid sr,c.resourceid cr from tmp_score t                 ");
		sb.append(" left join edu_roll_studentinfo s on s.studyno = t.studyno and                                           ");
		sb.append(" s.studentstatus = '11' and s.branchschoolid !='4a40f0a044289986014429e4d12b0001'                        ");
		sb.append(" left join edu_base_classic cl on cl.resourceid = s.classicid                                            ");
		sb.append(" left join edu_base_course c on c.coursename = t.coursename and substr(c.coursecode,3,1) = cl.classiccode");
		sb.append(" ) tmp where tmp.sr is null or tmp.cr is null    )                                                       ");
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement(sb.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static List<Map<String,Object>> selectNoRepeat() {
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tz3.* from tmp_score tz3 join (                                                 "); 
		sb.append(" select tz2.studyno,tz2.studentname,tz2.coursename from (                               ");
		sb.append(" select tz.studyno,tz.studentname,tz.coursename,count(tz.resourceid) counts             ");
		sb.append("  from tmp_score tz group by tz.studyno,tz.studentname,tz.coursename                    ");
		sb.append(" ) tz2 where tz2.counts=1                                                               ");
		sb.append(" ) tz4 on tz4.studyno = tz3.studyno and tz3.studentname = tz4.studentname               ");
		sb.append(" and tz4.coursename= tz3.coursename and tz3.studyno not in(                             ");
		sb.append(" select t3.studyno from (                                                               ");
		sb.append(" select t2.studyno,count(t2.studentname) counts2 from (                                 ");
		sb.append(" select t1.studyno,t1.studentname  from tmp_score t1 group by t1.studyno,t1.studentname ");
		sb.append(" ) t2 group by t2.studyno) t3 where t3.counts2>1)                                       ");
		sb.append("where tz3.memo is null                                       ");
		sb.append(" order by to_number(tz3.resourceid),tz3.studyno,tz3.studentname,tz3.coursename          ");
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("resourceid", rs.getString(1));
				map.put("fileName", rs.getString(2));
				map.put("sheetName", rs.getString(3));
				map.put("studyNo", rs.getString(4));
				map.put("studentName", rs.getString(5));
				map.put("courseName", rs.getString(6));
				map.put("year", rs.getString(7));
				map.put("term", rs.getString(8));
				map.put("score", rs.getString(9));
				returnList.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}
}
