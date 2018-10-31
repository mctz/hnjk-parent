<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自定义导出条件</title>
<script type="text/javascript">
function exportCustomExcel(){
	var unit 		= $("#exbranchSchool").val();
	var grade 		= $("#exgrade").val();
	var studystatus = $("#exstuStatus").val();
	var classic 	= $("#exclassic").val();
	var major 		= $("#exmajor").val();
	if(""==grade){
		alertMsg.warn("请选择一个年级.");
		return false;
	}
    var	url="${baseUrl}/edu3/roll/graduationQualif/exportCustomExcel.html"
    	+"?unit="+unit
    	+"&grade="+grade
    	+"&studystatus="+studystatus
    	+"&classic="+classic
    	+"&major="+major
    	;

    $('#frame_exportCostomInfos').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportCostomInfos";
	iframe.src = url;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
	
}

function _select_all_field(obj,checkbox){
	$(checkbox+" INPUT[type='checkbox']").each(function(ind){
		$(this).attr("checked",$(obj).attr("checked"));
	});
}
function _checkExportCustomExcelForm(obj){
	var exgrade  = $('#exportCustomExcelForm #exgrade').val();
	if(exgrade ==''){
		alertMsg.warn('请选择导出的年级!');
		return false;
	}
	
}
</script>
</head>
<body>

	<div class="page">
		<form id="exportCustomExcelForm" method="post"
			action="${baseUrl}/edu3/roll/graduationQualif/exportCustomExcel.html"
			class="pageForm" onsubmit="return _checkExportCustomExcelForm(this);">
			<div class="pageHeader">
				<div id="graduateCustomExport" class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete tabindex="1"
								name="exbranchSchool" id="exbranchSchool" defaultValue=""
								displayType="code" style="width:120px" /></li>
						<li><label>年级：</label>
						<gh:selectModel name="exgrade" id="exgrade" bindValue="resourceid"
								displayValue="gradeName" value=""
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" style="width:120px" /><font
							color="red">*</font></li>
						<li><label>学籍状态：</label>
						<gh:select name="exstuStatus" id="exstuStatus" value=""
								dictionaryCode="CodeStudentStatus" style="width:125px"
								filtrationStr="11,16,21,22,25" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>层次：</label>
						<gh:selectModel name="exclassic" id="exclassic"
								bindValue="resourceid" displayValue="classicName" value=""
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li><label>专业：</label>
						<gh:selectModel id="exmajor" name="exmajor" bindValue="resourceid"
								displayValue="majorName" value=""
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
					</ul>
				</div>
			</div>
			<table>
				<tr>
					<td colspan="4"><strong>需要导出的字段(<input
							type="checkbox" id="_select_all_fields" value="y"
							onclick="_select_all_field('#_select_all_fields','#_student_export_fields')" />全选)
					</strong></td>
			</table>


			<div class="pageContent">
				<table id="_student_export_fields" class="list">
					</tr>
					<tr>
						<td colspan="4"><strong>学籍信息</strong></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="major" value="on" />专业</td>
						<td><input type="checkbox" name="grade" value="on" />年级</td>
						<td><input type="checkbox" name="unit" value="on" />教学站</td>
						<td><input type="checkbox" name="classic" value="on" />层次</td>
						<td><input type="checkbox" name="studentNo" value="on" />学生号</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="examNo" value="on" />考生号</td>
						<td><input type="checkbox" name="studyAttr" value="on" />进修性质</td>
						<td><input type="checkbox" name="studyForm" value="on" />学习方式</td>
						<td><input type="checkbox" name="inSchoolType" value="on" />就读方式</td>
						<td><input type="checkbox" name="studyType" value="on" />学习类别</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="enterAttr" value="on" />入学性质</td>
						<td><input type="checkbox" name="inSchoolStatus" value="on" />在学状态</td>
						<td><input type="checkbox" name="studyStatus" value="on" />学籍状态</td>
						<td><input type="checkbox" name="isBookGD" value="on" />是否预约毕业论文</td>
						<td><input type="checkbox" name="isApplyGraduation"
							value="on" />是否申请学位</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="accountStatus" value="on" />学生账号状态</td>
						<td><input type="checkbox" name="isBookStudy" value="on" />学生预约状态</td>
						<td><input type="checkbox" name="teachingType" value="on" />教学模式</td>
						<td><input type="checkbox" name="scoreTotal" value="on" />已修总学分</td>
						<td><input type="checkbox" name="scoreNecTotal" value="on" />已修必修课总学分</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="scoreLimit" value="on" />已完成限选课门数</td>
						<td><input type="checkbox" name="enterSchool" value="on" />入学资格审核</td>
						<td><input type="checkbox" name="degree" value="on" />学位状态</td>
					</tr>
					<tr>
						<td><strong>学生基本信息</strong></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="name" value="on" />姓名</td>
						<td><input type="checkbox" name="nameUsed" value="on" />曾用名</td>
						<td><input type="checkbox" name="namePY" value="on" />姓名拼音</td>
						<td><input type="checkbox" name="gender" value="on" />性别</td>
						<td><input type="checkbox" name="certType" value="on" />证件类型</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="certNum" value="on" />证件号码</td>
						<td><input type="checkbox" name="contactAddress" value="on" />联系地址</td>
						<td><input type="checkbox" name="contactZipcode" value="on" />联系邮编</td>
						<td><input type="checkbox" name="contactPhone" value="on" />联系电话</td>
						<td><input type="checkbox" name="mobile" value="on" />移动电话</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="email" value="on" />邮件</td>
						<td><input type="checkbox" name="homePage" value="on" />个人主页</td>
						<td><input type="checkbox" name="height" value="on" />身高</td>
						<td><input type="checkbox" name="bloodType" value="on" />血型</td>
						<td><input type="checkbox" name="bornDay" value="on" />出生日期</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="bornAddress" value="on" />出生地</td>
						<td><input type="checkbox" name="country" value="on" />国籍</td>
						<td><input type="checkbox" name="homePlace" value="on" />籍贯</td>
						<td><input type="checkbox" name="gaqCode" value="on" />港澳侨代码</td>
						<td><input type="checkbox" name="nation" value="on" />民族</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="health" value="on" />身体健康状态</td>
						<td><input type="checkbox" name="marriage" value="on" />婚姻状况</td>
						<td><input type="checkbox" name="politics" value="on" />政治面貌</td>
						<td><input type="checkbox" name="faith" value="on" />宗教信仰</td>
						<td><input type="checkbox" name="residenceKind" value="on" />户口性质</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="residence" value="on" />户口所在地</td>
						<td><input type="checkbox" name="currenAddress" value="on" />现住址</td>
						<td><input type="checkbox" name="homeAddress" value="on" />住址</td>
						<td><input type="checkbox" name="homezipCode" value="on" />住址邮编</td>
						<td><input type="checkbox" name="homePhone" value="on" />家庭电话</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="officeName" value="on" />公司名称</td>
						<td><input type="checkbox" name="officePhone" value="on" />公司电话</td>
						<td><input type="checkbox" name="title" value="on" />职务职称</td>
						<td><input type="checkbox" name="specialization" value="on" />特长</td>
						<td><input type="checkbox" name="industryType" value="on" />行业类别</td>
					</tr>
					<tr>
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
</html>