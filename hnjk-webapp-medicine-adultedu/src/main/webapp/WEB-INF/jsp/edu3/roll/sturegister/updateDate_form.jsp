<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<script type="text/javascript">
</script>
<div class="page">
	<div class="pageContent">
		<form id="updateEnrollDateForm" method="post"
			  action="${baseUrl}/edu3/register/studentinfo/updateEnrollDate.html"
			  onsubmit="return validateCallback(this);" class="pageForm">
			<div class="pageFormContent" layouth="80">
				<table class="form">
					<input type="hidden" name="studentids" value="${studentids }">
					<tr>
						<td width="40%">年级：</td>
						<td width="60%">
							<gh:selectModel id="enrollDate_form_gradeid"
											name="gradeid" bindValue="resourceid"
											displayValue="gradeName"
											orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
											modelClass="com.hnjk.edu.basedata.model.Grade"
											value="${classes.grade.resourceid}" classCss="required" /> <span
								style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>学号前缀：</td>
						<td>
							<input type="text" id="enrollDate_form_studyNoPerfix" name="studyNoPerfix"
								   style="width: 200px;" value="${studyNoPerfix }"
								   class="required" />
						</td>
					</tr>
					<tr>
						<td width="20%">入学日期:</td>
						<td width="30%"><input
								value="<fmt:formatDate value='${enrollDate}' pattern='yyyy-MM-dd' />"
								type="text" name="enrollDate" id="enrollDate_form_enrollDate"
								class="required"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
					<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close" href="#close">取消</button>
						</div>
					</div></li>
				</ul>
			</div>
		</form>
	</div>
</div>
</body>
</html>