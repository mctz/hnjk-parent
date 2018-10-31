<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>任课老师课表</title>
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
	<div class="page">
		<div class="pageHeader">
			<form id="studentTimetableForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/student/study/mycoursetimetable/teacher-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>上课学期：</label> <gh:select
								id="studenttimetable_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeCourseTermType"
								style="width:55%" filtrationStr="${yearList }" /></li>
						<%--<label>班级</label>
				<gh:selectModel id="nonExam_classesid" name="classesId" bindValue="resourceid" displayValue="classname" style="width:350px" 
										modelClass="com.hnjk.edu.roll.model.Classes" orderBy="classname desc" value="${condition['classesId']}" 
										condition=" isDeleted=0  and classesMasterId='${condition['classesMasterId'] }'" />--%>
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
		<gh:resAuth parentCode="RES_STUDENT_MYCOURSETIMETABLE" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<div layoutH="97">
				<table class="list" width="100%" id="studentCourseTimeTable_table">
					<thead>
						<tr>
							<th width="3%">序号</th>
							<th width="5%">班级名称</th>
							<th width="3%">层次</th>
							<th width="4%">学习形式</th>
							<th width="4%">学生人数</th>
							<th width="5%">课程名称</th>
							<th width="4%">计划学时</th>
							<th width="4%">考核类型</th>
							<th width="4%">年级</th>
							<th width="5%">专业</th>
							<th width="4%">教学站</th>
							<th width="4%">上课学期</th>
							<c:forEach items="${dictList}" var="dict" varStatus="vs">
								<th width="7%" style="text-align: center;">${dict.dictName }</th>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${coursePage.result}" var="p">
							<c:if test="${p.stucount>0 }">
								<tr>
									<td>${p.index }</td>
									<td title="${p.classesname }">${p.classesname }</td>
									<td>${p.classicName }</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',p.teachingType) }</td>
									<td>${p.stucount }</td>
									<td>${p.courseName }</td>
									<td>${p.stydyHour }</td>
									<td>${ghfn:dictCode2Val('CodeExamClassType',p.examClassType) }</td>
									<td>${p.gradeName }</td>
									<td>${p.majorName }</td>
									<td>${p.unitName }</td>
									<td>${ghfn:dictCode2Val('CodeCourseTermType',p.courseterm) }</td>
									<c:forEach items="${dictList}" var="dict" varStatus="vs">
										<td style="text-align: center;"><c:forEach
												items="${p.timetable[dict.dictValue]}" var="t">
				            	 (${t.unitTimePeriod.courseTimeName })<br />
				            	 (${t.teachDate })<br />
				            	 (${t.classroom.classroomName })<br />
											</c:forEach></td>
									</c:forEach>
								</tr>
							</c:if>
						</c:forEach>
						<c:if test="${not empty memo }">
							<tr>
								<td style="text-align: center;">调课信息</td>
								<td colspan="8">${memo }</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
