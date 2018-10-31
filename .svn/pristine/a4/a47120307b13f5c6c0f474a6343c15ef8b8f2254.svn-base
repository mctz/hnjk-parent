<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>监巡考人员管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty examStaff.resourceid)?'新增':'编辑' }监巡考人员</h2>
	<div class="page">
		<div class="pageContent">
			<form id="examStaff_form" method="post"
				action="${baseUrl}/edu3/teaching/examstaff/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${examStaff.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">姓名:</td>
							<td colspan="3"><input type="text"
								value="${examStaff.name }" name="name" class="required" /> <input
								type="hidden" id="examStaff_form_sysUserId" name="sysUserId"
								value="${examStaff.sysUser.resourceid }" /> <a class="button"
								href="javascript:;" onclick="selectSysUser();"><span>选择用户</span></a>
							</td>
						</tr>
						<tr>
							<td width="20%">所属单位:</td>
							<td width="30%"><gh:brSchoolAutocomplete name="orgUnitId"
									tabindex="2" id="examStaff_form_orgUnitId"
									defaultValue="${examStaff.orgUnit.resourceid }" scope="all"></gh:brSchoolAutocomplete>
							</td>
							<td width="20%">身份证号:</td>
							<td width="30%"><input type="text"
								value="${examStaff.idcardNum }" name="idcardNum"
								class="required idnumber" /></td>
						</tr>
						<tr>
							<td>联系电话:</td>
							<td><input type="text" value="${examStaff.telelphone }"
								name="telelphone" class="required" /></td>
							<td>邮件:</td>
							<td><input type="text" value="${examStaff.email }"
								name="email" class="required email" /></td>
						</tr>
						<tr>
							<td>性别:</td>
							<td><gh:select dictionaryCode="CodeSex"
									id="examStaff_form_gender" name="gender"
									value="${examStaff.gender }" /></td>
							<td>学历:</td>
							<td><gh:select dictionaryCode="CodeEducation"
									id="examStaff_form_education" name="education"
									value="${examStaff.education }" /></td>
						</tr>
						<tr>
							<td>出身年月:</td>
							<td><input type="text" name="bornDay"
								value="<fmt:formatDate value='${examStaff.bornDay }' pattern='yyyy-MM-dd'/>"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
							<td>健康状况:</td>
							<td><gh:select dictionaryCode="CodeHealth"
									id="examStaff_form_health" name="health"
									value="${examStaff.health }" /></td>
						</tr>
						<tr>
							<td>是否有过监考:</td>
							<td><gh:select dictionaryCode="yesOrNo"
									id="examStaff_form_hasExamstaff" name="hasExamstaff"
									value="${examStaff.hasExamstaff }" /></td>
							<td>工作级别:</td>
							<td><gh:select dictionaryCode="CodeWorkLevel"
									id="examStaff_form_workLevel" name="workLevel"
									value="${examStaff.workLevel }" /></td>
						</tr>
						<tr>
							<td>备注</td>
							<td colspan="3"><textarea name='memo' cols='1' rows='3'
									style='width: 75%;'>${examstaff.memo }</textarea></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		function selectSysUser(){
			var url ="${baseUrl }/edu3/framework/examstaff/sysuser/rel.html?formid=examStaff_form&sysUserId="+$("#examStaff_form_sysUserId").val();
	    	$.pdialog.open(url,'chooseExamStaffSysUser','选择用户',{mask:true,height:600,width:800});
		}
	</script>
</body>
</html>