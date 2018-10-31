<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>进入课程论坛</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/framework/bbs/entry.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名称：</label> ${bbsentryCourseSelect }</li>
					</ul>
					<div class="subBar">
						<ul>
							<div class="buttonActive" style="float: right">
								<div class="buttonContent">
									<button type="submit">查 询</button>
								</div>
							</div>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="115">
				<thead>
					<tr>
						<th width="20%">课程名称</th>
						<th width="25%">论坛名称</th>
						<th width="25%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="coursebbs">
					<c:forEach items="${courseListPage.result}" var="course"
						varStatus="vs">
						<tr>
							<td><a
								href="${baseUrl }/edu3/teaching/teachingcourse/view.html?courseId=${course.resourceid }"
								target="dialog" mask="true" width="700" height="400"
								title="${course.courseName }">${course.courseName }</a></td>
							<td>${course.courseName}课程论坛</td>
							<td><c:choose>
									<c:when test="${course.isQualityResource eq 'Y'}">
										<a href="javascript:;"
											onclick="window.open('${baseUrl }/edu3/course/bbs/index.html?courseId=${course.resourceid }','course_bbs');return false;"
											style="color: blue;">进入课程论坛</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:;"
											onclick="window.open('${baseUrl }/edu3/learning/bbs/index.html?courseId=${course.resourceid }','course_bbs');return false;"
											style="color: blue;">进入课程论坛</a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseListPage}"
				goPageUrl="${baseUrl }/edu3/framework/bbs/entry.html" pageType="sys"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>