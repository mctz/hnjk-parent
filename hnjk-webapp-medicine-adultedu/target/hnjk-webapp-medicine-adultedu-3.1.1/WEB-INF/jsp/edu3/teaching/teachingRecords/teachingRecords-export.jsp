<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<style type="text/css">
th {
	border: none;
	border-color: white;
}
</style>
	<table class="table" layouth="110" width="100%" border="1">
		<thead>
			<tr>
				<th colspan="8" align="center"
					style="height: 50px; font-size: small;">${school } <br>
				<label>（&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;月&nbsp;~&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;月）</label>
					<br>
				<br>
				<label style="width: 20%">课程名称：${courseName }</label>&nbsp;&nbsp;&nbsp;
					<label style="width: 20%">上课年级：${gradeName }</label>&nbsp;&nbsp;&nbsp;
					<label style="width: 20%">专业：${majorName }</label>&nbsp;&nbsp;&nbsp;
					<label style="width: 20%">地点：${classroom }</label>
				</th>
			</tr>
			<%-- <tr>
				<th colspan="2">课程名称：${courseName }</th>
				<th colspan="2">上课年级：${gradeName }</th>
				<th colspan="2">专业：${majorName }</th>
				<th colspan="2">地点：${classroom }</th>
			</tr> --%>
			<tr>
				<th width="8%" style="text-align: center; vertical-align: middle;">周次</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">日期</th>
				<th width="25%" style="text-align: center; vertical-align: middle;">理论教学内容</th>
				<th width="8%" style="text-align: center; vertical-align: middle;">学时</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">教学手段</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">授课教师</th>
				<th width="10%" style="text-align: center; vertical-align: middle;">职称</th>
				<th width="15%" style="text-align: center; vertical-align: middle;">联系电话</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="tr" varStatus="vs">
				<tr>
					<td style="text-align: center; vertical-align: middle;">${tr.week }</td>
					<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
							value="${tr.timeperiod }" pattern="yyyy年MM月" /></td>
					<td style="text-align: center; vertical-align: middle;">${tr.contents }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.planCourse.stydyHour }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.teachType }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.teacher.cnName }</td>
					<td style="text-align: center; vertical-align: middle;">${tr.teacher.titleOfTechnical }</td>
					<td style="text-align: center; vertical-align: middle;"><c:choose>
							<c:when test="${not empty tr.teacher.mobile }"> ${tr.teacher.mobile }</c:when>
							<c:otherwise>${tr.teacher.officeTel }</c:otherwise>
						</c:choose></td>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align: center; vertical-align: middle;">备 注</td>
				<td colspan="7"></td>
			</tr>
			<tr style="border: 0px;">
				<td colspan="5" style="vertical-align: middle; border: 0px;">&nbsp;&nbsp;授课教师：
				</td>
				<td colspan="3"
					style="text-align: center; vertical-align: middle; border: 0px;">
					教研室（教学科）主任：<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;日
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>