<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课调整</title>
<style type="text/css">
#historyCourseTimetableBody td div {
	line-height: 170%;
	height: auto;
	white-space: normal;
}
</style>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingplancoursetimetable/history.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>班级：</label> <gh:selectModel
								id="history_timetable_classes" name="classesid"
								bindValue="resourceid" displayValue="classname"
								condition="brSchool.resourceid='${condition.brSchoolid}'"
								modelClass="com.hnjk.edu.roll.model.Classes"
								value="${condition['classesid']}"
								orderBy="grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid"
								style="width: 120px" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseid"
								id="history_timetable_courseid" tabindex="1" displayType="code"
								style="width:120px;" value="${condition['courseid'] }"></gh:courseAutocomplete>
						</li>
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
		<div class="pageContent">
			<table class="table" width="100%" layoutH="138">
				<thead>
					<tr>
						<th width="25%">班级</th>
						<th width="15%">课程</th>
						<th width="40%">调整内容</th>
						<th width="10%">调整用户</th>
						<th width="10%">调整时间</th>
					</tr>
				</thead>
				<tbody id="historyCourseTimetableBody">
					<c:forEach items="${historyPage.result}" var="h" varStatus="vs">
						<tr>
							<td>${h.classname }</td>
							<td>${h.courseName }</td>
							<td>
								<%-- 
			        	<b>调整前：</b>${h.beforevalue } <br/><b>调整后：</b> ${h.aftervalue } --%>
								${h.aftervalue }
							</td>
							<td>${h.operatorman }</td>
							<td><fmt:formatDate value="${h.operatortime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${historyPage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingplancoursetimetable/history.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>