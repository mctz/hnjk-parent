<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的课表</title>
<style type="text/css">
#studentCourseTimeTable_table td {
	border-bottom: 1px solid #EDEDED;
}

#studentCourseTimeTable_table .trbg {
	background-color: #FFF;
}
</style>
<script type="text/javascript">
function exportStudentTimetable(){
	var url = "${baseUrl}/edu3/student/study/mycoursetimetable/export.html?"+$("#studentTimetableForm").serialize();
	downloadFileByIframe(url,"studentTimetableIframe");
}
$(function (){
	_w_table_rowspan("#studentCourseTimeTable_table",1);
});
</script>
</head>
<body>
	<c:if test="${from ne 'main' }">
		<div class="page">
			<div class="pageHeader">
				<form id="studentTimetableForm"
					onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/student/study/mycoursetimetable/list.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>上课学期：</label> <gh:select
									id="studenttimetable_term" name="term"
									value="${condition['term']}"
									dictionaryCode="CodeCourseTermType" style="width:55%"
									filtrationStr="${yearList }" /></li>
						</ul>
						<div class="subBar">
							<ul>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">查 询</button>
										</div>
									</div></li>
							</ul>
						</div>
					</div>
				</form>
			</div>
			<gh:resAuth parentCode="RES_STUDENT_MYCOURSETIMETABLE"
				pageType="list"></gh:resAuth>
			<div class="pageContent">
				<div layoutH="97">
	</c:if>
	<table class="list" width="100%" id="studentCourseTimeTable_table">
		<thead>
			<tr>
				<th width="16%" style="text-align: center;" colspan="2">日期</th>
				<c:forEach items="${dictList}" var="dict" varStatus="vs">
					<th width="12%" style="text-align: center;">${dict.dictName }</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${timePeriodList}" var="p">
				<tr>
					<td width="6%" style="text-align: center;">
						${ghfn:dictCode2Val('CodeCourseTimePeriod',p.timePeriod) }</td>
					<td width="10%" style="text-align: center;">${p.timeName }<br />
						<fmt:formatDate value="${p.startTime }" pattern="HH:mm" />-<fmt:formatDate
							value="${p.endTime }" pattern="HH:mm" />
					</td>
					<c:forEach items="${dictList}" var="dict" varStatus="vs">
						<td style="text-align: center;"><c:forEach
								items="${timetableMap[p.resourceid][dict.dictValue]}" var="t">
								<b>${t.course.courseName}</b>
								<br />
				            	 (${t.classroom.classroomName })<br />
				            	 (${t.teacherName })<br />
				            	 (${t.teachDate })<br />
							</c:forEach></td>
					</c:forEach>
				</tr>
			</c:forEach>
			<c:if test="${not empty memo }">
				<tr>
					<td style="text-align: center;">调课信息</td>
					<td colspan="8">${memo }</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<c:if test="${from ne 'main' }">
		</div>
		</div>
		</div>
</body>
</html>
</c:if>