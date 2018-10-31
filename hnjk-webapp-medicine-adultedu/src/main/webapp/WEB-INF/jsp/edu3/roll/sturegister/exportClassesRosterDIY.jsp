<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出花名册-自定义导出条件</title>
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
		<form id="exportClassesRosterDIYform" method="post"
			action="${baseUrl}/edu3/register/studentinfo/exportClassesRoster.html"
			class="pageForm" onsubmit="return _checkExportCustomExcelForm(this);">
			<input type="hidden" name="branchSchool" id="branchSchool"
				value="${branchSchool }" /> <input type="hidden" name="grade"
				id="grade" value="${grade}" /> <input type="hidden" name="classes"
				id="classes" value="${classes}" /> <input type="hidden"
				name="classic" id="classic" value="${classic}" /> <input
				type="hidden" name="major" id="major" value="${major}" /> <input
				type="hidden" name="flag" id="flag" value="${flag }" />
			<div class="pageHeader">
				<div id="graduateCustomExport" class="searchBar">
					<ul class="searchContent">
						<li style="width: 220px;"><font color="#183152">教学站：${branchSchool_txt }</font></li>
						<li style="width: 220px;"><font color="#183152">专业：${major_txt }</font></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 220px;"><font color="#183152">年级：${grade_txt }</font></li>
						<li style="width: 220px;"><font color="#183152">层次：${classic_txt }</font></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 220px;"><font color="#183152">班级：${classes_txt }</font></li>
					</ul>
				</div>
			</div>
			<table>
				<!-- 
		<tr>
			<td colspan="4"><strong>需要导出的字段(<input type="checkbox" id="_select_all_fields" value="y" onclick="_select_all_field('#_select_all_fields','#_student_export_fields')"/>全选)</strong></td>
		</tr>
		 -->
			</table>


			<div class="pageContent">
				<table id="_student_export_fields" class="list">
					<tr>
						<td colspan="6"><strong>基础信息(<input type="checkbox"
								id="_select_graduateDate_fields" value="y"
								onclick="_select_all_fieldbyClass('#_select_graduateDate_fields','#_student_export_fields')" />全选)
						</strong></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="examNo" value="on"
							class="gd" /><font color="red">考生号</font></td>
						<td><input type="checkbox" name="studentNo" value="on"
							class="gd" /><font color="red">学生号</font></td>
						<td><input type="checkbox" name="name" value="on" class="gd" /><font
							color="red">姓名</font></td>
						<td><input type="checkbox" name="contactZipcode" value="on"
							class="gd" /><font color="red">邮政编号</font></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="gender" value="on"
							class="gd" /><font color="red">性别</font></td>
						<td><input type="checkbox" name="certNum" value="on"
							class="gd" /><font color="red">身份证号</font></td>
						<td><input type="checkbox" name="mobile" value="on"
							class="gd" /><font color="red">手机号码</font></td>
						<td><input type="checkbox" name="contactAddress" value="on"
							class="gd" /><font color="red">联系地址</font></td>
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