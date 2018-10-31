package com.hnjk.edu.recruit.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 生成学号
 * 规则：
 * 	安徽医科大的学号规则是：

	学号编制规则
	1 2 3 4 5 6 7 8 9
	
	年份 培养层次 专业 顺序号
	1-2为入学年份后两位，3为层次代码，4-5为专业编号，6-9为流水号。
	
	层次编码如下：
	层次 编号
	专升本 1
	高起专 2
	
	<pre>
		因为生成学生学生号要用到入学年份，而入学日期在数据库学生表里并没有只是bean里面虚拟的字段，规则是:
		studentinfo.getGrade().getTerm().equals("1") ? studentinfo.getGrade().getYearInfo().getFirstYear()+"-03-01"
						:String.valueOf(studentinfo.getGrade().getYearInfo().getFirstYear()+1)+"-03-01"
		
	</pre>
	找到 是否有入学时间，层次，专业相同的学生的学号，如果有则新学号是其最大的学号+1，如果没则是第一个
	RecruitJDBCServiceImpl类里的相关方法：
		getStudentInfoByQuery(Map<String,object>())：根据入学时间，层次，专业查找最大的学号
		checkStudentInfoByNo(String no):查找学号是否存在
 * @author wwq
 *
 */

public class MakeStudentNoUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(MakeStudentNoUtil.class);
	private String 			message 	= 		"生成学号失败！"; //信息
	private int 			issuccess	= 		300;			 //操作是否成功  300 失败   | 200 成功
	private StudentInfo		stu			= 		null;			 //学生对象		
	
	//构造器
	public MakeStudentNoUtil(StudentInfo studentinfo){
		this.stu = studentinfo;
	}
	
	/**
	 * 产生学生学号
	 * @param no 学号（查看入学时间，层次，专业下的最大的学号）
	 * @return
	 */
	public Map<String,Object> getStudentNo(String no){
		Map<String,Object> map = new HashMap<String, Object>();
		String 			stuno       =       "";				 //学号
		try {
			//产生学号
			if( null == stu ){
				message = "生成学号失败，学生信息为空！";
			}else{						
				if( null == no || "".equals(no.trim()) || no.length() != 9){//当没有 入学时间，层次，专业相同的学生信息时  按规则生成学生学号，否则获取最大的那个学生信学号递增生成新学号
					stuno 		= 	getOne_to_two()+getTree()+getFour_to_Five()+"0001";							
				}else{					
					stuno 		=  (Long.parseLong(no)+1L)+"";
				}
			}
		} catch (Exception e) {
			logger.error("生成学号出错："+e.fillInStackTrace());
			message = "生成学号出错："+e.fillInStackTrace();
			e.fillInStackTrace();
		}
		
		if( !"".equals(stuno.toString().trim()) && null != stuno ){
			issuccess 	= 	200;
			message 	=   "生成学生号成功！";
			map.put("stuno", stuno);
		}
		
		//装载信息
		map.put("message", message);
		map.put("issuccess", issuccess);	
		return map;
	}
	
	//学号第1-2位数 为入学年份后两位
	private String getOne_to_two(){
		String _n1_2="";
		try {
			_n1_2 = "1".equals(stu.getGrade().getTerm())
			?
			stu.getGrade().getYearInfo().getFirstYear().toString().substring(
					stu.getGrade().getYearInfo().getFirstYear().toString().length()-2,stu.getGrade().getYearInfo().getFirstYear().toString().length()
				)
			:
			String.valueOf(stu.getGrade().getYearInfo().getFirstYear()+1).substring(
					String.valueOf(stu.getGrade().getYearInfo().getFirstYear()+1).length()-2,String.valueOf(stu.getGrade().getYearInfo().getFirstYear()+1).length()
				);
		} catch (Exception e) {
			logger.error("生成学号第1-2位数出错："+e.fillInStackTrace());
			message = "生成学号第1-2位数出错："+e.fillInStackTrace();
			e.fillInStackTrace();
		}
		return _n1_2;				
	}
	
	/*
	 *  学号第3位数 3为层次代码， 层次编码如下：
	 * 	层次 编号
	 * 	专升本 1
	 * 	高起专 2
	 * */
	private String getTree(){
		String _n3 = "";
		try {
		   _n3 = "专升本".equals(stu.getClassic().getShortName().toString().trim())?"1":"2";
		}catch (Exception e) {
			logger.error("生成学号第3位数出错："+e.fillInStackTrace());
			message = "生成学号第3位数出错："+e.fillInStackTrace();
			e.fillInStackTrace();
		}
		return _n3;
	}
	
	//学号第4-5位数 4-5为专业编号，
	private String getFour_to_Five(){
		String _n4_5 = "";
		try {
			_n4_5 = stu.getMajor().getMajorCode();
		} catch (Exception e) {
			logger.error("生成学号第4-5位数出错："+e.fillInStackTrace());
			message = "生成学号第4-5位数出错："+e.fillInStackTrace();
			e.fillInStackTrace();
		}
		return _n4_5;
	}
}
