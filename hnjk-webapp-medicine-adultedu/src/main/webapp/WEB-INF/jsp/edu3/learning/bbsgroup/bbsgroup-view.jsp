<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习小组查看</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<table width="98%">
					<tr>
						<td width="20%">课程：</td>
						<td>${bbsGroup.course.courseName }</td>
					</tr>
					<tr>
						<td width="20%">组名：</td>
						<td>${bbsGroup.groupName }</td>
					</tr>
					<tr>
						<td width="20%">组描述：</td>
						<td>${bbsGroup.groupDescript }</td>
					</tr>
				</table>
			</div>
		</div>

		<div class="">
			<table class="table" layouth="97">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="10%">组名</th>
						<th width="10%">课程</th>
						<th width="15%">姓名</th>
						<th width="15%">学号</th>
						<th width="10%">年级</th>
						<th width="10%">培养层次</th>
						<th width="10%">专业</th>
						<th width="15%">教学站</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${bbsGroup.groupUsers }" var="bbsGroupUsers"
						varStatus="vs">
						<tr>
							<td>&nbsp;</td>
							<td>${bbsGroupUsers.bbsGroup.groupName }</td>
							<td>${bbsGroupUsers.bbsGroup.course.courseName}</td>
							<td>${bbsGroupUsers.studentInfo.studentName}</td>
							<td>${bbsGroupUsers.studentInfo.studyNo}</td>
							<td>${bbsGroupUsers.studentInfo.grade.gradeName}</td>
							<td>${bbsGroupUsers.studentInfo.classic.classicName}</td>
							<td>${bbsGroupUsers.studentInfo.major.majorName}</td>
							<td>${bbsGroupUsers.studentInfo.branchSchool}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>