<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业数据自定义导出条件</title>
<style type="text/css">
	td{text-align: left}
</style>
<script type="text/javascript">

function _select_all_field(obj,checkbox){
	$(checkbox+" INPUT[type='checkbox']").each(function(ind){
		$(this).attr("checked",$(obj).attr("checked"));
	});
}
function _select_all_fieldbyClass(obj,checkbox){
	$(checkbox+" INPUT[type='checkbox']").each(function(ind){
		if("gd"==$(this).attr("class"))
		$(this).attr("checked",$(obj).attr("checked"));
	});
}
function _checkExportCustomExcelForm(obj){
	/*
	var exggrade  = $('#exportCustomExcelForm #exggrade').val();
	if(exggrade ==''){
		alertMsg.warn('请选择导出的年级!');
		return false;
	}
	*/
	
}
</script>
</head>
<body>

	<div class="page">
		<form id="exportCustomExcelForm" method="post"
			action="${baseUrl}/edu3/roll/graduationStudent/exportCustomExcel.html"
			class="pageForm" onsubmit="return _checkExportCustomExcelForm(this);">
			<input type="hidden" name="exgbranchSchool" id="exgbranchSchool"
				value="${exgbranchSchool }" /> <input type="hidden" name="exggrade"
				id="exggrade" value="${exggrade}" /> <input type="hidden"
				name="exggraduateDate" id="exggraduateDate"
				value="${exggraduateDate}" /> <input type="hidden" name="exgclassic"
				id="exgclassic" value="${exgclassic}" /> <input type="hidden"
				name="exgmajor" id="exgmajor" value="${exgmajor}" /> <input
				type="hidden" name="confirmGraduateDateb" id="confirmGraduateDateb"
				value="${confirmGraduateDateb}" /> <input type="hidden"
				name="confirmGraduateDatee" id="confirmGraduateDatee"
				value="${confirmGraduateDatee}" /> <input type="hidden"
				name="degreeStatus" id="degreeStatus" value="${degreeStatus}" /> <input
				type="hidden" name="exgraduationType" id="exgbtype"
				value="${graduationType}" /> <input type="hidden"
				name="exteachingType" id="extctype" value="${teachingType}" /> <input
				type="hidden" name="exclasses" id="exclasses" value="${classes}" />
				 <input type="hidden" name="degreeApplyStatus" id="degreeApplyStatus" value="${degreeApplyStatus}" />
			<%--<div class="pageHeader">
			<div id="graduateCustomExport" class="searchBar">
				<ul class="searchContent">
					<li>
					<label>教学站：</label>
							<gh:brSchoolAutocomplete tabindex="1" name="exgbranchSchool" id="exgbranchSchool" defaultValue="" displayType="code" style="width:120px"/>
				</li>
				<li><label>年级：</label><gh:selectModel name="exggrade" id="exggrade" bindValue="resourceid" displayValue="gradeName" value=""
									modelClass="com.hnjk.edu.basedata.model.Grade" orderBy="gradeName desc" style="width:120px" /><font color="red">*</font></li>
				<li><label>学籍状态：</label><gh:select name="exggraduateDate" id="exggraduateDate" value="" dictionaryCode="CodeStudentStatus" style="width:125px" filtrationStr="11,16,21,22,24"/></li>					
				</ul>
				<ul class="searchContent">
					<li><label>层次：</label><gh:selectModel name="exgclassic" id="exgclassic"  bindValue="resourceid" displayValue="classicName" value=""
							modelClass="com.hnjk.edu.basedata.model.Classic" style="width:120px"   /></li>
					<li><label>专业：</label><gh:selectModel id="exgmajor" name="exgmajor" bindValue="resourceid" displayValue="majorName" value=""
							modelClass="com.hnjk.edu.basedata.model.Major" style="width:120px"/></li>
				</ul>
			</div>
		</div> --%>

			<table>
				<tr>
					<td colspan="4" style="margin-left: 10px;"><strong>需要导出的字段(<input
							type="checkbox" id="_select_all_fields" value="y"
							onclick="_select_all_field('#_select_all_fields','#_student_export_fields')" />全选)
					</strong></td>
				</tr>
			</table>


			<div class="pageContent">
				<table id="_student_export_fields" class="list">
					<tr>
						<td colspan="6" style="text-align: center;"><strong>毕业数据信息(<input
								type="checkbox" id="_select_graduateDate_fields" value="y"
								onclick="_select_all_fieldbyClass('#_select_graduateDate_fields','#_student_export_fields')" />全选)
						</strong></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="unit" value="on" class="gd" /><font
							color="red">教学站</font></td>
						<td><input type="checkbox" name="yxmc" value="on" class="gd" /><font
							color="red">院校名称</font></td>
						<td><input type="checkbox" name="yxdm" value="on" class="gd" /><font
							color="red">院校代码</font></td>
						<td><input type="checkbox" name="examCertificateNo"
							value="on" class="gd" /><font color="red">准考证号</font></td>
						<td><input type="checkbox" name="examNo" value="on"
							class="gd" /><font color="red">考生号</font></td>
						<td><input type="checkbox" name="classic" value="on"
							class="gd" /><font color="red">层次</font></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="name" value="on" class="gd" /><font
							color="red">姓名</font></td>
						<td><input type="checkbox" name="studentNo" value="on"
							class="gd" /><font color="red">学生号</font></td>
						<td><input type="checkbox" name="bornDay" value="on"
							class="gd" /><font color="red">出生日期</font></td>
						<td><input type="checkbox" name="homePlace" value="on"
							class="gd" /><font color="red">籍贯</font></td>
						<td><input type="checkbox" name="politics" value="on"
							class="gd" /><font color="red">政治面貌</font></td>
						<td><input type="checkbox" name="gender" value="on"
							class="gd" /><font color="red">性别</font></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="nation" value="on"
							class="gd" /><font color="red">民族</font></td>
						<td><input type="checkbox" name="majorName" value="on"
							class="gd" /><font color="red">专业名称</font></td>
						<td><input type="checkbox" name="majorCode" value="on"
							class="gd" /><font color="red">专业代码</font></td>
						<td><input type="checkbox" name="classes" value="on"
							class="gd" /><font color="red">班级名称</font></td>
						<td><input type="checkbox" name="entranceDate" value="on"
							class="gd" /><font color="red">入学日期</font></td>
						<td><input type="checkbox" name="eduYear" value="on"
							class="gd" /><font color="red">学制</font></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="teachingType" value="on"
							class="gd" /><font color="red">学习模式</font></td>
						<td><input type="checkbox" name="certNum" value="on"
							class="gd" /><font color="red">证件号码</font></td>
						<td><input type="checkbox" name="graduateDate" value="on"
							class="gd" /><font color="red">毕业日期</font></td>
						<td><input type="checkbox" name="diplomaNum" value="on"
							class="gd" /><font color="red">毕业证号</font></td>
						<td><input type="checkbox" name="degreeNum" value="on"
							class="gd" /><font color="red">学位证号</font></td>
						<td><input type="checkbox" name="totalPoint" value="0"
							class="gd" /><font color="red">成人高考分数</font></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="bjyjl" value="on" class="gd" /><font
							color="red">毕结业结论</font></td>
						<td><input type="checkbox" name="xzxm" value="on" class="gd" /><font
							color="red">校长姓名</font></td>
						<td><input type="checkbox" name="bz_sh" value="on" class="gd" /><font
							color="red">备注</font>
						<!-- <input type="checkbox" name="fxzy" value="on"  /><font color="red" >继续教育学院</font>--></td>
					</tr>

					<tr>
						<td colspan="6" style="text-align: center;"><strong>其他学籍信息</strong></td>
					</tr>
					<tr>
						<!-- <td><input type="checkbox" name="major"  value="on"/>专业</td> -->
						<td><input type="checkbox" name="grade" value="on" />年级</td>
						<td><input type="checkbox" name="studyAttr" value="on" />进修性质</td>
						<td><input type="checkbox" name="studyForm" value="on" />学习方式</td>
						<td><input type="checkbox" name="inSchoolType" value="on" />就读方式</td>
						<td><input type="checkbox" name="studyType" value="on" />学习类别</td>
						<td><input type="checkbox" name="enterAttr" value="on" />入学性质</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="inSchoolStatus" value="on" />在学状态</td>
						<td><input type="checkbox" name="studyStatus" value="on" />学籍状态</td>
						<td><input type="checkbox" name="isBookGD" value="on" />是否预约毕业论文</td>
						<td><input type="checkbox" name="isApplyGraduation"
							value="on" />是否申请学位</td>
						<td><input type="checkbox" name="accountStatus" value="on" />学生账号状态</td>
						<td><input type="checkbox" name="isBookStudy" value="on" />学生预约状态</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="scoreTotal" value="on" />已修总学分</td>
						<td><input type="checkbox" name="scoreNecTotal" value="on" />已修必修课总学分</td>
						<td><input type="checkbox" name="scoreLimit" value="on" />已完成限选课门数</td>
						<td><input type="checkbox" name="enterSchool" value="on" />入学资格审核</td>
						<td><input type="checkbox" name="degree" value="on" />学位状态</td>
						<td><input type="checkbox" name="degreeName" value="on" />学位名称</td>
					</tr>
					<tr>
						<td colspan="6" style="text-align: center;"><strong>其他学生基本信息</strong></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="nameUsed" value="on" />曾用名</td>
						<td><input type="checkbox" name="namePY" value="on" />姓名拼音</td>
						<td><input type="checkbox" name="certType" value="on" />证件类型</td>
						<td><input type="checkbox" name="contactAddress" value="on" />联系地址</td>
						<td><input type="checkbox" name="contactZipcode" value="on" />联系邮编</td>
						<td><input type="checkbox" name="contactPhone" value="on" />联系电话</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="mobile" value="on" />移动电话</td>
						<td><input type="checkbox" name="email" value="on" />邮件</td>
						<td><input type="checkbox" name="homePage" value="on" />个人主页</td>
						<td><input type="checkbox" name="height" value="on" />身高</td>
						<td><input type="checkbox" name="bloodType" value="on" />血型</td>
						<td><input type="checkbox" name="bornAddress" value="on" />出生地</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="country" value="on" />国籍</td>
						<td><input type="checkbox" name="gaqCode" value="on" />港澳侨代码</td>
						<td><input type="checkbox" name="health" value="on" />身体健康状态</td>
						<td><input type="checkbox" name="marriage" value="on" />婚姻状况</td>
						<td><input type="checkbox" name="faith" value="on" />宗教信仰</td>
						<td><input type="checkbox" name="residenceKind" value="on" />户口性质</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="residence" value="on" />户口所在地</td>
						<td><input type="checkbox" name="currenAddress" value="on" />现住址</td>
						<td><input type="checkbox" name="homeAddress" value="on" />住址</td>
						<td><input type="checkbox" name="homezipCode" value="on" />住址邮编</td>
						<td><input type="checkbox" name="homePhone" value="on" />家庭电话</td>
						<td><input type="checkbox" name="officeName" value="on" />公司名称</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="officePhone" value="on" />公司电话</td>
						<td><input type="checkbox" name="title" value="on" />职务职称</td>
						<td><input type="checkbox" name="specialization" value="on" />特长</td>
						<td><input type="checkbox" name="industryType" value="on" />行业类别</td>
						<td><input type="checkbox" name="workStatus" value="on" />职业状态</td>
					</tr>
				</table>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">导出</button>
								</div>
							</div></li>
					</ul>
				</div>
			</div>
		</form>
	</div>
</body>
</html>