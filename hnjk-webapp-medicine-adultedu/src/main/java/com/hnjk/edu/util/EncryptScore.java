package com.hnjk.edu.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

public class EncryptScore {

	private static Connection con = null;
	public EncryptScore(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.247:1521:orcl", "xy_stdx_beta_20170630", "beta12345678");
			con.setAutoCommit(false);
		} catch(Exception e){}
	}
	
	private void creatSql(String filePath) throws Exception{
		  File f = new File(filePath);  
	        if (!f.exists()) {  
	            f.createNewFile();// 不存在则创建  
	        }  
	        BufferedWriter bw = new BufferedWriter(new FileWriter(f));  
	            
			String sel_sql = "";
			Reader inStream=null;
			/*sel_sql= "select er.resourceid,er.studentid,f_decrypt_score(er.integratedscore,er.studentid) decryptscore,er.usuallyscore,er.writtenscore,er.integratedscore,er.onlinescore from edu_teach_examresults er "+
					" join edu_roll_studentinfo si on si.resourceid=er.studentid"+
					" join edu_teach_guiplan gp on gp.planid=si.teachplanid"+
					" left join edu_teach_coursestatus cs on cs.isopen='Y' and cs.term='2016_02' and cs.guiplanid=gp.resourceid and cs.schoolids=si.branchschoolid"+
					" left join edu_teach_plancourse pc on pc.courseid=er.courseid and pc.resourceid=cs.plancourseid"+
					" join edu_base_course c on c.resourceid=pc.courseid and c.coursename='经济数学'"+
					" where cs.isdeleted=0 and er.isdeleted=0 order by decryptscore";*/
			/*sel_sql = "select us.resourceid,us.studentid,si.studyno,f_decrypt_score(us.exerciseResults,us.studentid) decryptscore from edu_teach_usualresults us "+
					"join edu_roll_studentinfo si on si.resourceid=us.studentid "+
					"join edu_teach_guiplan gp on gp.planid=si.teachplanid "+
					"left join edu_teach_coursestatus cs on cs.isopen='Y' and cs.term='2016_02' and cs.guiplanid=gp.resourceid and cs.schoolids=si.branchschoolid "+
					"left join edu_teach_plancourse pc on pc.courseid=us.courseid and pc.resourceid=cs.plancourseid "+
					"join edu_base_course c on c.resourceid=pc.courseid and c.coursename='经济数学' "+
					"where cs.isdeleted=0 and us.isdeleted=0 order by decryptscore ";*/
			sel_sql = "select er.resourceid,er.studentid,'95' integratedscore from edu_teach_examresults er where er.onlinescore='195' and er.studentid is not null";
			PreparedStatement  ps = con.prepareStatement(sel_sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				//在线课程
				//StringBuffer updateSql = new StringBuffer("update edu_teach_usualresults er set ");
				//updateSql.append("er.exerciseResults='"+getEncryptScore(rs.getString("studentid"),"100")+"'");
				//非在线课程
				StringBuffer updateSql = new StringBuffer("update edu_teach_examresults er set ");
			    //updateSql.append("er.usuallyscore='"+getEncryptScore(rs.getString("studentid"),"100")+"'");
			    //updateSql.append(",er.writtenscore='"+getEncryptScore(rs.getString("studentid"),"100")+"'");
			    updateSql.append("er.integratedscore='"+getEncryptScore(rs.getString("studentid"),rs.getString("integratedscore"))+"'");
			    updateSql.append(" where er.resourceid='"+rs.getString("resourceid")+"';\r\n");
			    
			    bw.write(updateSql.toString().replaceAll("'null'", "null"));
			}
			bw.close();  
			System.out.println("已生成sql文件！");
	}
	
	private String getEncryptScore(String studentid,String score) {
		return ScoreEncryptionDecryptionUtil.getInstance().encrypt(studentid, score);
	}
	
	public static void main(String[] args)  {
		EncryptScore encryptScore = new EncryptScore();
		try {
			encryptScore.creatSql("D:\\encryptScore.sql");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
