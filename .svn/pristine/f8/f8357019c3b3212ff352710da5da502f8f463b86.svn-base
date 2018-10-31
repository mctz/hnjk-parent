package com.hnjk.edu.recruit.model;


/**
 * 导出新生库中导出所使用的属性  bean
 * <code>ExportRecruitPlan</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-4-14 下午03:33:46
 * @see 
 * @version 1.0
 */
public class ExportRecruitPlan {
	

	//表一.试点高校网络教育招生计划备案表（招生办）.xls导出所使用的属性
		 private String recruitPlanname;//招生计名（招生计划批次）
		 private String recruitTime;	//计划招生时间
		 private String classicName;	//层次名称
		 private String majorNationCode;//专业代码	
		 private String  majorName;		//专业名称
		 private String issetMajor;				//校内普通本专科是否开设该专业
		 private String limitNum;		//计划招生人数
		 private String studentOrigin;	//表一为生源地区  表二为生源地区
		 private String totalFee;		//收费标准         (元/学分)
		 private String schoolmasterName;//校长 
	//表二.试点高校网络教育实际录取情况表（学籍办）.xls导出所使用的属性	
		 private String cout;//录取人数
	//表三.试点高校网络教育录取学生基本情况表（学籍办）.xls 暂时未做	
		 
	//表四.试点高校本年度计划招生的校外学习中心备案表（招生办）.xls导出所使用的属性	
		 private String schoolCode;		//学习中心编码
		 private String schoolName;		//校外学习中心全称
		 private String district;//district
		 private String address;		//联系地址
		 private String relyOnUnitName; //依托单位名称
		 private String relyOnUnitQuale;//依托单位性质
		 private String principal;//负责人
		 private String linkman;//联系人
		 //姓名  字段等同于principal  和  linkman
		 private String contectCall;//联系电话
		 private String email;//电子信箱
		 private String checkDate;//审批通过时间
		 private String documentCode;//批文号
		 private String isRecruitStudents;//是否当批次可招生
	//报表：通过入学资格审查的学生统计
		 private String enrolleeCode;//考生号
		 private String name;//学生姓名
		 private String gender;//性别
		 private String bornDay;//生日
		 private String certNum;//身份证号
		 private String politics;//政治面貌
		 private String nation;//民族
		//专业代码	上面有该属性
		//专业名称       上面有该属性
		//层次名称       上面有该属性
		public  ExportRecruitPlan(){}
		public ExportRecruitPlan(String schoolCode){
			this.schoolCode=schoolCode;
		}
		
		 //表一，表二用
		 public ExportRecruitPlan(String recruitPlanname,String recruitTime,String classicName,String majorNationCode,String  majorName,String limitNum,String totalFee,String issetMajor,String studentOrigin){
				this.recruitPlanname=recruitPlanname;
				this.recruitTime=recruitTime;
				this.classicName=classicName;
				this.majorNationCode=majorNationCode;
				this.majorName=majorName;
				this.limitNum=limitNum;
				this.totalFee=totalFee;
				this.issetMajor=issetMajor;
				this.studentOrigin=studentOrigin;
			}
		 public String getRecruitTime() {
			//[1]计划招生时间:2008年春季的招生必须填写成'200803';2008年秋季的招生必须填写成'200809'
			String stu="";
			if(recruitPlanname!=null&&recruitPlanname.indexOf("春")>-1){
				stu=recruitPlanname.substring(0, 4)+"03";
			}
			else if(recruitPlanname!=null&&recruitPlanname.indexOf("秋")>-1){
				stu=recruitPlanname.substring(0, 4)+"09";
			}
			else{
				stu=recruitPlanname;//“/记录行结束/” 为数据行结束标志，请勿删除
			}
			
			return stu;
		 }

		 public void setRecruitTime(String recruitTime) {
			this.recruitTime = recruitTime;
		}

		

		public String getIssetMajor() {
			String stu="是";
			if("/记录行结束/".equals(recruitPlanname)){
				stu="";
			}
			//默认为是
			return stu;
		}


		public void setIssetMajor(String issetMajor) {
			this.issetMajor = issetMajor;
		}

		public String getRecruitPlanname() {
			return recruitPlanname;
		}

		public void setRecruitPlanname(String recruitPlanname) {
			this.recruitPlanname = recruitPlanname;
		}

		public String getClassicName() {
			return classicName;
		}

		public void setClassicName(String classicName) {
			this.classicName = classicName;
		}

		public String getMajorNationCode() {
			if(majorNationCode==null||"".equals(majorNationCode)){//目录外
				majorNationCode="目录外";
			}
			if("/记录行结束/".equals(recruitPlanname)){
				majorNationCode="";
			}
			return majorNationCode;
		}

		public void setMajorNationCode(String majorNationCode) {
			this.majorNationCode = majorNationCode;
		}

		public String getMajorName() {
			return majorName;
		}

		public void setMajorName(String majorName) {
			this.majorName = majorName;
		}

		public String getLimitNum() {
			return limitNum;
		}

		public void setLimitNum(String limitNum) {
			this.limitNum = limitNum;
		}

		public String getTotalFee() {
			return totalFee;
		}

		public void setTotalFee(String totalFee) {
			this.totalFee = totalFee;
		}
		public String getStudentOrigin() {//这里需要进行城市转换成省份的过程 返回城市所在省份
			return studentOrigin;
		}

		public void setStudentOrigin(String studentOrigin) {
			this.studentOrigin = studentOrigin;
		}
	
		 public String getDistrict() {
				return district;
			}

			public void setDistrict(String district) {
				this.district = district;
			}

			public String getSchoolCode() {
				return schoolCode;
			
			}

			public void setSchoolCode(String schoolCode) {
				this.schoolCode = schoolCode;
			}

			public String getSchoolName() {
				return schoolName;
			}

			public void setSchoolName(String schoolName) {
				this.schoolName = schoolName;
			}

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}
			//无数据返回空字符串
			public String getRelyOnUnitName() {
				return "";
			}

			public void setRelyOnUnitName(String relyOnUnitName) {
				this.relyOnUnitName = relyOnUnitName;
			}
			//无数据返回空字符串
			public String getRelyOnUnitQuale() {
				return "";
			}

			public void setRelyOnUnitQuale(String relyOnUnitQuale) {
				this.relyOnUnitQuale = relyOnUnitQuale;
			}

			public String getPrincipal() {
				return principal;
			}

			public void setPrincipal(String principal) {
				this.principal = principal;
			}

			public String getLinkman() {
				return linkman;
			}

			public void setLinkman(String linkman) {
				this.linkman = linkman;
			}

			public String getContectCall() {
				return contectCall;
			}

			public void setContectCall(String contectCall) {
				this.contectCall = contectCall;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			public String getCheckDate() {
				return checkDate;
			}

			public void setCheckDate(String checkDate) {
				this.checkDate = checkDate;
			}

			public String getDocumentCode() {
				return documentCode;
			}

			public void setDocumentCode(String documentCode) {
				this.documentCode = documentCode;
			}
			
			//无数据返回1
			public String getIsRecruitStudents() {
				if("/记录行结束/".equals(schoolCode)){
					isRecruitStudents="";
				}else{
					isRecruitStudents="1";
				}
				return isRecruitStudents;
			}

			public void setIsRecruitStudents(String isRecruitStudents) {
				this.isRecruitStudents = isRecruitStudents;
			}

			public String getCout() {
				return cout;
			}

			public void setCout(String cout) {
				this.cout = cout;
			}
			public String getSchoolmasterName() {
				return schoolmasterName;
			}
			public void setSchoolmasterName(String schoolmasterName) {
				this.schoolmasterName = schoolmasterName;
			}
			public String getEnrolleeCode() {
				return enrolleeCode;
			}
			public void setEnrolleeCode(String enrolleeCode) {
				this.enrolleeCode = enrolleeCode;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getGender() {
				return gender;
			}
			public void setGender(String gender) {
				this.gender = gender;
			}
			public String getBornDay() {
				return bornDay;
			}
			public void setBornDay(String bornDay) {
				this.bornDay = bornDay;
			}
			public String getCertNum() {
				return certNum;
			}
			public void setCertNum(String certNum) {
				this.certNum = certNum;
			}
			public String getPolitics() {
				return politics;
			}
			public void setPolitics(String politics) {
				this.politics = politics;
			}
			public String getNation() {
				return nation;
			}
			public void setNation(String nation) {
				this.nation = nation;
			}
			
}
