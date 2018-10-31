<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已分配座位列表</title>


</head>
<body>
	<div class="page">

		<div class="pageContent" defH="150">

			<table class="table" layouth="40" width="100%">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="10%">姓名</th>
						<th width="8%">教学站</th>
						<th width="8%">学号</th>
						<%--<th width="8%">年级</th> --%>
						<th width="15%">专业</th>
						<th width="16%">考试课程</th>
						<th width="20%">考试时间</th>
						<th width="10%">试室</th>
						<th width="8%">座号</th>
					</tr>
				</thead>

				<tbody id="assignedSeatListBody">
					<c:forEach items="${examResultList}" var="examResult"
						varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${examResult.studentInfo.studentName }</td>
							<td>${examResult.studentInfo.branchSchool.unitShortName}</td>
							<td>${examResult.studentInfo.studyNo}</td>
							<%--  <td >${examResult.studentInfo.grade.gradeName}</td>--%>
							<td>${examResult.studentInfo.major.majorName}</td>
							<td>${examResult.course.courseName}</td>
							<td><fmt:formatDate
									value="${examResult.examInfo.examStartTime}"
									pattern="yyyy-MM-dd HH:mm" /> - <fmt:formatDate
									value="${examResult.examInfo.examEndTime}" pattern="HH:mm" />
							</td>
							<td>${examResult.examroom.examroomName}</td>
							<td>${examResult.examSeatNum}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>

	</div>
</body>
</html>