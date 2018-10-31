<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看预约统计</title>
</head>
<body>
	<h2 class="contentTitle">查看预约统计</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduatePapers/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">毕业论文批次:</td>
							<td width="30%">${stuList[0].examSub.batchName }&nbsp;</td>
							<td width="20%">教学站:</td>
							<td width="30%">${stuList[0].studentInfo.branchSchool }&nbsp;</td>
						</tr>
						<tr>
							<td>专业:</td>
							<td>${stuList[0].studentInfo.major }&nbsp;</td>
							<td>层次:</td>
							<td>${stuList[0].studentInfo.classic }&nbsp;</td>
						</tr>
						<!--
				<tr>
					<td>导师:</td>
					<td colspan="3">${stuList[0].guidTeacherName }&nbsp;</td>
				</tr>	
				  -->
					</table>
					<table class="list" width="100%">
						<tr>
							<th width="10%">指导老师</th>
							<th width="10%">学生学号</th>
							<th width="15%">学生姓名</th>
							<th width="10%">年级</th>
							<th width="20%">专业</th>
							<th width="20%">层次</th>
							<th width="15%">论文环节</th>
						</tr>
						<c:forEach items="${stuList }" var="stu" varStatus="vs">
							<tr>
								<td>${stu.teacher.cnName }</td>
								<td>${stu.studentInfo.studyNo }</td>
								<td>${stu.studentInfo.studentName}</td>
								<td>${stu.studentInfo.grade }</td>
								<td>${stu.studentInfo.major }</td>
								<td>${stu.studentInfo.classic}</td>
								<td>${ghfn:dictCode2Val('CodeCurrentTache',stu.currentTache ) }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>