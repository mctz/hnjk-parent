<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试记录管理</title>
</head>
<body>
<body>
	<h2 class="contentTitle">修改考试考试时间</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitexamlogs/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<input type="hidden" name="resourceid"
						value="${recruitExamLogs.resourceid }" />
					<table class="form">
						<tr>
							<td style="width: 10%">教学站:</td>
							<td style="width: 40%">${recruitExamLogs.brSchool }</td>
							<td style="width: 10%">考试场次:</td>
							<td style="width: 40%">${recruitExamLogs.recruitExamPlan }</td>
						</tr>
						<tr>
							<td>考场:</td>
							<td>${recruitExamLogs.examroom }</td>
							<td>考试科目:</td>
							<td>${ghfn:dictCode2Val('CodeEntranceExam',recruitExamLogs.courseName) }</td>
						</tr>
						<tr>
							<td>姓名:</td>
							<td>${recruitExamLogs.enrolleeInfo.studentBaseInfo.name }</td>
							<td>准考证号:</td>
							<td>${recruitExamLogs.enrolleeInfo.examCertificateNo }</td>
						</tr>
						<tr>
							<td>考试开始时间:</td>
							<td><input type="text" id="recruitExamLogs_startTime"
								name="startTime"
								value="<fmt:formatDate value='${recruitExamLogs.startTime}' pattern='yyyy-MM-dd HH:mm:ss' />"
								style="width: 60%"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="required" /></td>
							<td>考试结束时间:</td>
							<td><input type="text" id="recruitExamLogs_endTime"
								name="endTime"
								value="<fmt:formatDate value='${recruitExamLogs.endTime}' pattern='yyyy-MM-dd HH:mm:ss' />"
								style="width: 60%"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="required" /></td>
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
</body>
</html>