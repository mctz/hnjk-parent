<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.recordScoreTeacher {
	cursor: pointer;
}
</style>
</head>
<body>
	<table class="table" layouth="190" border="1">
		<thead>
			<tr>
				<th width="16%">班级</th>
				<th width="5%">年级</th>
				<th width="12%">专业</th>
				<th width="4%">层次</th>
				<th width="6%">学习方式</th>
				<th width="12%">教学站</th>
				<th width="10%">课程</th>
				<th width="8%">上课学期</th>
				<th width="6%">排课状态</th>
				<th width="8%">任课老师</th>
				<th width="6%">登分老師</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${coursePage}" var="c" varStatus="vs">
				<tr>
					<td>${c.classesname }</td>
					<td>${c.gradeName }</td>
					<td>${c.majorName }</td>
					<td>${c.classicName }</td>
					<td>${ghfn:dictCode2Val('CodeTeachingType',c.teachingType) }</td>
					<td>${c.unitName }</td>
					<td>${c.courseName }</td>
					<td>${ghfn:dictCode2Val('CodeCourseTermType',c.courseterm) }</td>
					<td><c:choose>
							<c:when test="${c.status eq 0 }">未排课</c:when>
							<c:otherwise>已排课</c:otherwise>
						</c:choose></td>
					<td>${c.lecturerName }</td>
					<td>${c.teacherName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>