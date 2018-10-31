<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约情况列表</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="96">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="15%">年度</th>
						<th width="15%">学期</th>
						<th width="15%">课程</th>
						<th width="10%">姓名</th>
						<th width="10%">性别</th>
						<th width="15%">专业</th>
						<th width="15%">学习中心</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${listStudents.result}" var="plan" varStatus="vs">
						<tr>
							<td>&nbsp;</td>
							<td>${plan.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',plan.term) }</td>
							<td>${plan.teachingPlanCourse.course.courseName}</td>
							<td>${plan.studentInfo.studentName}</td>
							<td>${ghfn:dictCode2Val('CodeSex',plan.studentInfo.studentBaseInfo.gender)}</td>
							<td>${plan.studentInfo.major.majorName }</td>
							<td>${plan.studentInfo.branchSchool }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${listStudents}"
				goPageUrl="${baseUrl }/edu3/framework/teaching/studentlearnplan/list.html"
				pageType="sys" targetType="dialog" pageNumShown="5"
				condition="${condition}" />
		</div>
	</div>
</body>
</html>