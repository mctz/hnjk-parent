<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<table class="table" layouth="110" width="100%" border="1">
		<thead>
			<tr>
				<th colspan="8" style="height: 50px; font-size: medium;">${school }</th>
			</tr>
			<tr>
				<th width="10%" style="text-align: center; vertical-align: middle;">教学点</th>
				<th width="5%" style="text-align: center; vertical-align: middle;">年级</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">专业</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">课程</th>
				<th width="5%" style="text-align: center; vertical-align: middle;">学时</th>
				<th width="5%" style="text-align: center; vertical-align: middle;">教学手段</th>
				<th width="5%" style="text-align: center; vertical-align: middle;">周次</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">日期</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">授课教师</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">职称</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">联系电话</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">地点</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">理论教学内容</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="tr" varStatus="vs">
				<tr>
					<td style="text-align: center; vertical-align: middle;">${tr.unit.unitName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.grade.gradeName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.major.majorName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.planCourse.course.courseName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.planCourse.stydyHour }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.teachType }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.week }</td>
					<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
							value="${tr.timeperiod }" pattern="yyyy年MM月" /></td>
					<td style="text-align: center; vertical-align: middle;">${tr.teacher.cnName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.teacher.titleOfTechnical }</td>
					<td style="text-align: center; vertical-align: middle;"><c:choose>
							<c:when test="${not empty tr.teacher.mobile }"> ${tr.teacher.mobile }</c:when>
							<c:otherwise>${tr.teacher.officeTel }</c:otherwise>
						</c:choose></td>
					<td style="text-align: center; vertical-align: middle;">${tr.classroom }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.contents }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>