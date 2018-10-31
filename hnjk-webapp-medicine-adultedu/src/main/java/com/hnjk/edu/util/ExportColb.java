package com.hnjk.edu.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 18, 2016 2:27:41 PM 
 * 
 */
public class ExportColb {
	private static Connection con = null;
	private static int lenStr = 2000;
	
//	private StringBuffer guidSQL_insert = new StringBuffer("insert into edu_lear_courseguid (RESOURCEID, SYLLABUSTREEID, TYPE, CONTENT, FILLINMAN, FILLINMANID, FILLINDATE, VERSION, ISDELETED) values (");
//	private StringBuffer overviewSQL_insert = new StringBuffer("insert into edu_lear_overview (RESOURCEID, COURSEID, TYPE, CONTENT, FILLINMAN, FILLINMANID, FILLINDATE, VERSION, ISDELETED) values (");
//	private StringBuffer examSQL_insert = new StringBuffer("insert into edu_lear_exams (RESOURCEID, EXAMTYPE, DIFFICULT, REQUIREMENT, COURSEID, KEYWORDS, QUESTION, ANSWER, PARSER, FILLINDATE, FILLINMAN, FILLINMANID, VERSION, ISDELETED, ISENROLEXAM, COURSENAME, SHOWORDER, PARENTID, EXAMNODETYPE, EXAMFORM, MODIFYMAN, MODIFYDATE, ANSWEROPTIONNUM, ISONLINEANSWER) values (");
	
	/**
	 *1、结构树
	 * select * from edu_teach_syllabustree st where st.courseid in ('000125b','000072');
	 * 2、select * from edu_lear_mate m where m.isdeleted=0 and m.syllabustreeid in (select st.resourceid from edu_teach_syllabustree st where st.courseid in ('000125b','000072'));
	 * 
	 * 
	 * 
	 */
	public ExportColb(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			con = DriverManager.getConnection("jdbc:oracle:thin:@125.88.254.178:67:orcl", "xy_gdxy_gzdx", "beta12345678");
			con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.247:1521:orcl", "xy_gdy_beta", "beta12345678");
			con.setAutoCommit(false);
		} catch(Exception e){}
	}
	//获取key
	public List<String> getKey(Map<String, String> map,Object value) {
		String o=null;
		List<String> all=new ArrayList<String>();    //建一个数组用来存放符合条件的KEY值
		Set set=map.entrySet();
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Map.Entry entry=(Map.Entry)it.next();
			if(entry.getValue().equals(value)) {
				o=entry.getKey().toString();
				all.add(o);
			}
		}
		if (all.size() == 0 && value!=null) {
			all.add(value.toString());
		}
		return all;
	}
	
	public void creatSql(String type, String[] courseIds,Map<String, String> courseidMap, String filePath) throws Exception {
		
        File f = new File(filePath);  
        if (!f.exists()) {  
            f.createNewFile();// 不存在则创建  
        }  
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));  
            
		String sel_sql = "";
		if ("course".equals(type)) {
			Reader inStream=null;
			sel_sql= "select * from edu_base_course c where c.resourceid in(?) order by resourceid ";

			sel_sql = String.format(sel_sql.replace("?","%s"),ExStringUtils.addSymbol(courseIds,"'","'"));
			PreparedStatement  ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				List<String> courseidList = getKey(courseidMap, rs.getString("RESOURCEID"));
				boolean rep = courseidList.size()>1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer courseSQL_insert = new StringBuffer("insert into edu_base_course (RESOURCEID, COURSECODE, COURSENAME, COURSEENNAME, COURSESHORTNAME, CHSINTRODUCTION, ENINTRODUCTION, STATUS, ISUNITEEXAM, STOPTIME, ");
					courseSQL_insert.append("EXAMTYPE, EXAMCODE, MEMO, VERSION, ISDELETED, ISDEGREEUNITEXAM, ISPRACTICE, PLANOUTSTUDYHOUR, PLANOUTCREDITHOUR, HASRESOURCE, UNITEEXAMTEMPLATESORDER, ISQUALITYRESOURCE, COVER, COURSETYPE, TOPICNUM) values (");

					courseSQL_insert.append("'"+courseidList.get(index)+"', ");
					courseSQL_insert.append("'"+rs.getString("COURSECODE")+(rep==true?index:"")+"', ");
					courseSQL_insert.append("'"+rs.getString("COURSENAME")+"', ");
					courseSQL_insert.append("'"+rs.getString("COURSEENNAME")+"', ");
					courseSQL_insert.append("'"+rs.getString("COURSESHORTNAME")+"', ");
					courseSQL_insert.append("'"+rs.getString("CHSINTRODUCTION")+"', ");
					courseSQL_insert.append("'"+rs.getString("ENINTRODUCTION")+"', ");
					courseSQL_insert.append("'"+rs.getInt("STATUS")+"', ");
					courseSQL_insert.append("'"+rs.getString("ISUNITEEXAM")+"', ");
					//Date fd = rs.getDate("FILLINDATE");
					//courseSQL_insert.append("to_date('"+ExDateUtils.formatDateStr(fd, "dd-MM-yyyy HH:mm:ss")+"', 'dd-mm-yyyy hh24:mi:ss'), ");
					courseSQL_insert.append("NULL, ");
					courseSQL_insert.append("'"+rs.getInt("EXAMTYPE")+"', ");
					courseSQL_insert.append("'"+rs.getString("EXAMCODE")+"', ");
					courseSQL_insert.append("'"+rs.getString("MEMO")+"', ");
					courseSQL_insert.append("'"+rs.getInt("VERSION")+"', ");
					courseSQL_insert.append("'"+rs.getInt("ISDELETED")+"', ");
					courseSQL_insert.append("'"+rs.getString("ISDEGREEUNITEXAM")+"', ");
					courseSQL_insert.append("'"+rs.getString("ISPRACTICE")+"', ");
					courseSQL_insert.append("'"+rs.getInt("PLANOUTSTUDYHOUR")+"', ");
					courseSQL_insert.append("'"+rs.getInt("PLANOUTCREDITHOUR")+"', ");
					courseSQL_insert.append("'"+rs.getString("HASRESOURCE")+"', ");
					courseSQL_insert.append("'"+rs.getInt("UNITEEXAMTEMPLATESORDER")+"', ");
					courseSQL_insert.append("'"+rs.getString("ISQUALITYRESOURCE")+"', ");
					courseSQL_insert.append("'"+rs.getString("COVER")+"', ");
					courseSQL_insert.append("'"+rs.getString("COURSETYPE")+"', ");
					courseSQL_insert.append(rs.getInt("TOPICNUM")+");\r\n ");

					bw.write(courseSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}
		} else if ("syllabustree".equals(type)) {
			Reader inStream=null;
			sel_sql= "select * from edu_teach_syllabustree sbt where sbt.courseid in(?) order by sbt.nodelevel ";

			sel_sql = String.format(sel_sql.replace("?","%s"),ExStringUtils.addSymbol(courseIds,"'","'"));
			PreparedStatement  ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID"));
				boolean rep = courseidList.size()>1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer treeSQL_insert = new StringBuffer("insert into edu_teach_syllabustree (RESOURCEID, COURSEID, NODENAME, PARENTID, ISCHILD, NODELEVEL, NODETYPE, NODECONTENT, PROVIDESTYDYHOUR, MEMO, VERSION, ISDELETED, SHOWORDER) values (");

					treeSQL_insert.append("'"+rs.getString("RESOURCEID")+(rep==true?index:"")+"', ");
					treeSQL_insert.append("'"+courseidList.get(index)+"', ");
					treeSQL_insert.append("'"+rs.getString("NODENAME")+"', ");
					treeSQL_insert.append("'" + rs.getString("PARENTID") + (rep == true ? index : "") + "', ");
					treeSQL_insert.append("'"+rs.getString("ISCHILD")+"', ");
					treeSQL_insert.append("'"+rs.getInt("NODELEVEL")+"', ");
					treeSQL_insert.append("'"+rs.getString("NODETYPE")+"', ");
					treeSQL_insert.append("'"+rs.getString("NODECONTENT")+"', ");
					treeSQL_insert.append("'"+rs.getInt("PROVIDESTYDYHOUR")+"', ");
					treeSQL_insert.append("'"+rs.getString("MEMO")+"', ");
					treeSQL_insert.append("'"+rs.getInt("VERSION")+"', ");
					treeSQL_insert.append("'"+rs.getInt("ISDELETED")+"', ");
					treeSQL_insert.append(rs.getInt("SHOWORDER")+");\r\n ");

					bw.write(treeSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}
		} else if ("mate".equals(type)) {
			Reader inStream=null;
			sel_sql = "select m.*,sbt.COURSEID as COURSEID_M from edu_lear_mate m join edu_teach_syllabustree sbt on sbt.RESOURCEID = m.SYLLABUSTREEID where sbt.COURSEID in(?)";
			//todo 如果需要导出课程所有类型的资源，需要注释此行！！！
			sel_sql += " and m.MATETYPE in('0','1','2','3','9','11','12','13')";
			sel_sql = String.format(sel_sql.replace("?","%s"),ExStringUtils.addSymbol(courseIds,"'","'"));
			PreparedStatement  ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID_M"));
				boolean rep = courseidList.size()>1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer mateSQL_insert = new StringBuffer("insert into edu_lear_mate (RESOURCEID, SYLLABUSTREEID, MATENAME, MATETYPE, VERSION, ISDELETED, MATEURL, ISPUBLISHED, MATEEDUNETURL, SHOWORDER, CHANNELTYPE, COURSEID, CAPTUREDATE, TOTALTIME) values (");

					mateSQL_insert.append("'"+rs.getString("RESOURCEID")+(rep==true?index:"")+"', ");
					mateSQL_insert.append("'"+rs.getString("SYLLABUSTREEID")+(rep==true?index:"")+"', ");
					mateSQL_insert.append("'"+rs.getString("MATENAME")+"', ");
					mateSQL_insert.append("'"+rs.getString("MATETYPE")+"', ");
					mateSQL_insert.append("'"+rs.getInt("VERSION")+"', ");
					mateSQL_insert.append("'"+rs.getInt("ISDELETED")+"', ");
					mateSQL_insert.append("'"+rs.getString("MATEURL")+"', ");
					mateSQL_insert.append("'"+rs.getString("ISPUBLISHED")+"', ");
					mateSQL_insert.append("'"+rs.getString("MATEEDUNETURL")+"', ");
					mateSQL_insert.append("'"+rs.getInt("SHOWORDER")+"', ");
					mateSQL_insert.append("'"+rs.getString("CHANNELTYPE")+"', ");
					mateSQL_insert.append("'"+courseidList.get(index)+"', ");
					if (ExStringUtils.isNotBlank(rs.getString("CAPTUREDATE"))) {
						Date fd = rs.getDate("FILLINDATE");
						mateSQL_insert.append("to_date('" + ExDateUtils.formatDateStr(fd, "dd-MM-yyyy HH:mm:ss") + "', 'dd-mm-yyyy hh24:mi:ss'), ");
					} else {
						mateSQL_insert.append("NULL, ");
					}
					mateSQL_insert.append(rs.getInt("TOTALTIME")+");\r\n");

					bw.write(mateSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}
		} else if ("courseexam".equals(type)) {
			Reader inStream=null;
			sel_sql = "select ce.*,sbt.COURSEID from edu_lear_courseexam ce join edu_teach_syllabustree sbt on sbt.RESOURCEID=ce.SYLLABUSTREEID where sbt.COURSEID in(?)";
			sel_sql = String.format(sel_sql.replace("?","%s"),ExStringUtils.addSymbol(courseIds,"'","'"));
			PreparedStatement  ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID"));
				boolean rep = courseidList.size()>1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer mateSQL_insert = new StringBuffer("insert into edu_lear_courseexam (RESOURCEID, SYLLABUSTREEID, SCORE, VERSION, ISDELETED, SHOWORDER, REFERSYLLABUSTREEIDS, REFERSYLLABUSTREENAMES, EXAMID, ISPUBLISHED, AUDITMAN, AUDITMANID, AUDITDATE, MODIFYMAN, MODIFYDATE) values (");

					mateSQL_insert.append("'"+rs.getString("RESOURCEID")+(rep==true?index:"")+"', ");
					mateSQL_insert.append("'"+rs.getString("SYLLABUSTREEID")+(rep==true?index:"")+"', ");
					mateSQL_insert.append("'"+rs.getInt("SCORE")+"', ");
					mateSQL_insert.append("'"+rs.getInt("VERSION")+"', ");
					mateSQL_insert.append("'"+rs.getInt("ISDELETED")+"', ");
					mateSQL_insert.append("'"+rs.getInt("SHOWORDER")+"', ");
					mateSQL_insert.append("'"+rs.getString("REFERSYLLABUSTREEIDS")+"', ");
					mateSQL_insert.append("'"+rs.getString("REFERSYLLABUSTREENAMES")+"', ");
					mateSQL_insert.append("'"+rs.getString("EXAMID") + (rep==true?index:"")+"', ");
					mateSQL_insert.append("'"+rs.getString("ISPUBLISHED")+"', ");

					mateSQL_insert.append("NULL,NULL,NULL,NULL,NULL);\r\n");
					bw.write(mateSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}
		} else if ("guid".equals(type)) {
			Reader inStream = null;
			sel_sql = "select st.courseid,cg.* from edu_lear_courseguid cg join edu_teach_syllabustree st on cg.syllabustreeid=st.resourceid where cg.isdeleted=0 and st.isdeleted=0 and st.courseid in(?) order by st.courseid ";

			sel_sql = String.format(sel_sql.replace("?", "%s"), ExStringUtils.addSymbol(courseIds, "'", "'"));
			PreparedStatement ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID"));
				boolean rep = courseidList.size() > 1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer guidSQL_insert = new StringBuffer("insert into edu_lear_courseguid (RESOURCEID, SYLLABUSTREEID, TYPE, CONTENT, FILLINMAN, FILLINMANID, FILLINDATE, VERSION, ISDELETED) values (");
					Clob content = rs.getClob("CONTENT");
					inStream = content.getCharacterStream();
					char[] c = new char[(int) content.length()];
					inStream.read(c);
					String contentStr = new String(c);
					inStream.close();
					int times = 0;
					if (contentStr.length() % lenStr == 0) {
						times = contentStr.length() / lenStr;
					} else {
						times = (contentStr.length() / lenStr) + 1;
					}
					guidSQL_insert.append("'" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "', ");
					guidSQL_insert.append("'" + rs.getString("SYLLABUSTREEID") + (rep == true ? index : "") + "', ");
					guidSQL_insert.append("'" + rs.getString("TYPE") + "', ");
					guidSQL_insert.append("'" + (times <= 1 ? contentStr.substring(0, contentStr.length()) : contentStr.substring(0, lenStr)).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "', ");
					guidSQL_insert.append("'" + rs.getString("FILLINMAN") + "', ");
					guidSQL_insert.append("NULL,NULL, ");
					//Date fd = rs.getDate("FILLINDATE");
					//guidSQL_insert.append("to_date('" + ExDateUtils.formatDateStr(fd, "dd-MM-yyyy HH:mm:ss") + "', 'dd-mm-yyyy hh24:mi:ss'), ");
					guidSQL_insert.append(rs.getInt("VERSION") + ", ");
					guidSQL_insert.append(rs.getInt("ISDELETED") + ");\r\n");
					for (int i = 2; i <= times; i++) {
						int len = lenStr * i;
						if (i == times) {
							len = contentStr.length();
						}
						guidSQL_insert.append("update edu_lear_courseguid set CONTENT=CONTENT||'" + contentStr.substring(lenStr * (i - 1), len).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "' where RESOURCEID='" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "';\r\n");
					}
					bw.write(guidSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}

		} else if ("overview".equals(type)) {
			Reader inStream = null;
			sel_sql = "select ov.* from edu_lear_overview ov where ov.isdeleted=0 and ov.courseid in (?)";
			sel_sql = String.format(sel_sql.replace("?", "%s"), ExStringUtils.addSymbol(courseIds, "'", "'"));
			PreparedStatement ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID"));
				boolean rep = courseidList.size() > 1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer overviewSQL_insert = new StringBuffer("insert into edu_lear_overview (RESOURCEID, COURSEID, TYPE, CONTENT, FILLINMAN, FILLINMANID, FILLINDATE, VERSION, ISDELETED) values (");
					Clob content = rs.getClob("CONTENT");
					inStream = content.getCharacterStream();
					char[] c = new char[(int) content.length()];
					inStream.read(c);
					String contentStr = new String(c);
					inStream.close();

					int times = 0;
					if (contentStr.length() % lenStr == 0) {
						times = contentStr.length() / lenStr;
					} else {
						times = (contentStr.length() / lenStr) + 1;
					}

					overviewSQL_insert.append("'" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "', ");
					overviewSQL_insert.append("'" + courseidList.get(index) + "', ");
					overviewSQL_insert.append("'" + rs.getString("TYPE") + "', ");
					overviewSQL_insert.append("'" + (times <= 1 ? contentStr.substring(0, contentStr.length()) : contentStr.substring(0, lenStr)).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "', ");
					overviewSQL_insert.append("'" + rs.getString("FILLINMAN") + "', ");
					overviewSQL_insert.append("NULL,NULL, ");
					//Date fd = rs.getDate("FILLINDATE");
					//overviewSQL_insert.append("to_date('" + ExDateUtils.formatDateStr(fd, "dd-MM-yyyy HH:mm:ss") + "', 'dd-mm-yyyy hh24:mi:ss'), ");
					overviewSQL_insert.append(rs.getInt("VERSION") + ", ");
					overviewSQL_insert.append(rs.getInt("ISDELETED") + ");\r\n");
					for (int i = 2; i <= times; i++) {
						int len = lenStr * i;
						if (i == times) {
							len = contentStr.length();
						}
						overviewSQL_insert.append("update edu_lear_overview set CONTENT=CONTENT||'" + contentStr.substring(lenStr * (i - 1), len).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "' where RESOURCEID='" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "';\r\n");
					}
					bw.write(overviewSQL_insert.toString().replaceAll("'null'", "null"));
				}
			}
		} else if ("exam".equals(type)) {
			Reader inStream = null;
			sel_sql = "select e.* from edu_lear_exams e where e.isdeleted=0 and e.courseid in (?)";
			sel_sql = String.format(sel_sql.replace("?", "%s"), ExStringUtils.addSymbol(courseIds, "'", "'"));
			PreparedStatement ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				List<String> courseidList = getKey(courseidMap, rs.getString("COURSEID"));
				boolean rep = courseidList.size() > 1;
				for (int index = 0; index < courseidList.size(); index++) {
					StringBuffer examSQL_insert = new StringBuffer("insert into edu_lear_exams (RESOURCEID, EXAMTYPE, DIFFICULT, REQUIREMENT, COURSEID, KEYWORDS, QUESTION, ANSWER, PARSER, FILLINDATE, FILLINMAN, FILLINMANID, VERSION, ISDELETED, ISENROLEXAM, COURSENAME, SHOWORDER, PARENTID, EXAMNODETYPE, EXAMFORM, MODIFYMAN, MODIFYDATE, ANSWEROPTIONNUM, ISONLINEANSWER) values (");
					Clob question = rs.getClob("QUESTION");
					inStream = question.getCharacterStream();
					char[] questionc = new char[(int) question.length()];
					inStream.read(questionc);
					String questionStr = new String(questionc);
					inStream.close();
					int times = 0;
					if (questionStr.length() % lenStr == 0) {
						times = questionStr.length() / lenStr;
					} else {
						times = (questionStr.length() / lenStr) + 1;
					}
					Clob answer = rs.getClob("ANSWER");
					String answerStr = "";
					if (answer != null) {
						inStream = answer.getCharacterStream();
						char[] answerc = new char[(int) answer.length()];
						inStream.read(answerc);
						answerStr = new String(answerc);
						answerStr = answerStr.replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "");
						inStream.close();
					}
					Clob parser = rs.getClob("PARSER");
					String parserStr = "";
					if (parser != null) {
						inStream = parser.getCharacterStream();
						char[] parserc = new char[(int) parser.length()];
						inStream.read(parserc);
						parserStr = new String(parserc);
						parserStr = parserStr.replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "");
						inStream.close();
					}
					examSQL_insert.append("'" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "', ");
					examSQL_insert.append("'" + rs.getString("EXAMTYPE") + "', ");
					examSQL_insert.append("'" + rs.getString("DIFFICULT") + "', ");
					examSQL_insert.append("'" + rs.getString("REQUIREMENT") + "', ");
					examSQL_insert.append("'" + courseidList.get(index) + "', ");
					examSQL_insert.append("'" + rs.getString("KEYWORDS") + "', ");
					examSQL_insert.append("'" + (times <= 1 ? questionStr.substring(0, questionStr.length()) : questionStr.substring(0, lenStr)).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "', ");
					examSQL_insert.append("'" + answerStr + "', ");
					examSQL_insert.append("'" + parserStr + "', ");
					Date fd = rs.getDate("FILLINDATE");
					examSQL_insert.append("to_date('" + ExDateUtils.formatDateStr(fd, "dd-MM-yyyy HH:mm:ss") + "', 'dd-mm-yyyy hh24:mi:ss'), ");
					examSQL_insert.append("'" + rs.getString("FILLINMAN") + "', ");
					examSQL_insert.append("NULL, ");
					examSQL_insert.append(rs.getInt("VERSION") + ", ");
					examSQL_insert.append(rs.getInt("ISDELETED") + ",");
					examSQL_insert.append("'" + rs.getString("ISENROLEXAM") + "', ");
					examSQL_insert.append("'" + rs.getString("COURSENAME") + "', ");
					examSQL_insert.append(rs.getInt("SHOWORDER") + ",");
					examSQL_insert.append("'" + rs.getString("PARENTID") + (rep == true ? index : "") + "', ");

					examSQL_insert.append("'" + rs.getString("EXAMNODETYPE") + "', ");
					examSQL_insert.append("'" + rs.getString("EXAMFORM") + "', ");
					examSQL_insert.append("'" + rs.getString("MODIFYMAN") + "', ");
					Date mfd = rs.getDate("MODIFYDATE");
					if (mfd == null) {
						mfd = new Date();
					}
					examSQL_insert.append("to_date('" + ExDateUtils.formatDateStr(mfd, "dd-MM-yyyy HH:mm:ss") + "', 'dd-mm-yyyy hh24:mi:ss'), ");
					examSQL_insert.append(rs.getInt("ANSWEROPTIONNUM") + ",");
					examSQL_insert.append("'" + rs.getString("ISONLINEANSWER") + "');\r\n ");
					for (int i = 2; i <= times; i++) {
						int len = lenStr * i;
						if (i == times) {
							len = questionStr.length();
						}
						examSQL_insert.append("update edu_lear_exams set QUESTION=QUESTION||'" + questionStr.substring(lenStr * (i - 1), len).replaceAll("'", "' || chr(39) || '").replaceAll("&", "' || chr(38) || '").replaceAll("&", "' || chr(34) || '").replaceAll("&", "' || chr(10) || '").replaceAll("&", "' || chr(13) || '").replaceAll("\r\n", "").replaceAll("\n", "") + "' where RESOURCEID='" + rs.getString("RESOURCEID") + (rep == true ? index : "") + "';\r\n");
					}
					bw.write(examSQL_insert.toString().replaceAll("'null'", "null"));
					inStream.close();
				}
			}
		}
		bw.close();  
		System.out.println("已生成 "+type+"sql文件！");
	}
	
	 public static void main(String[] args)  {
		 ExportColb ec = new ExportColb();
		 //查询有资源的课程
		 //大学英语
		 //String[] courseIds = {"4a4092b150ccb9cf0150d1cf1b483be6"};

		 //广东医视频课件资源（m3u8）导出
		 String[] courseIds = {"000039", "000062", "000072", "000096", "000111", "000125", "000125b", "000125c", "000125d"};

		 //courseidMap:课程id对照表   key  : 目标表（需要导入课程资源的数据库）课程id
		 // 						 value: 查询表（需要导出课程资源的数据库）课程id，默认使用查询表课程id
		 Map<String, String> courseidMap = new HashMap<String, String>();
		 //courseidMap.put("0001", "4a4092b150ccb9cf0150d1cf1b483be6");

		 try {
		 	 //edu_base_course
			 ec.creatSql("course", courseIds,courseidMap,"D:\\01_course.sql");

			 //edu_lear_exams
			 ec.creatSql("exam", courseIds,courseidMap,"D:\\02_exams_clob.sql");
			 //edu_lear_overview
			 ec.creatSql("overview", courseIds,courseidMap,"D:\\02_overview_clob.sql");
			 //edu_teach_syllabustree
			 ec.creatSql("syllabustree", courseIds,courseidMap,"D:\\02_syllabustree.sql");

			 //edu_lear_mate  如果需要导出所有类型的课件资源，请注释资源类型查询条件！
			 ec.creatSql("mate", courseIds,courseidMap,"D:\\03_mate.sql");
			 //edu_lear_courseexam
			 ec.creatSql("courseexam", courseIds,courseidMap,"D:\\03_courseexam.sql");
			 //edu_lear_courseguid
			 ec.creatSql("guid", courseIds,courseidMap,"D:\\03_guid_clob.sql");

			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
