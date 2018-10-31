<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>是否沿用或累积平时成绩管理</title>
<script type="text/javascript">
	function changeStudentIsRedoCourseExam(){
		pageBarHandle("您确定要重新累积平时成绩吗？","${baseUrl}/edu3/learning/studentlearnplan/redocourseexam/change.html","#studentlearnplan_redocourseexamBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/learning/studentlearnplan/redocourseexam/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="studentlearnplan_redocourseexam_courseId"
								displayType="code" value="${condition['courseId']}"
								style="width:120px"></gh:courseAutocomplete></li>
						<li><label>累积类型：</label> <select name="isRedoCourseExam"
							style="width: 120px;">
								<option value="N"
									<c:if test="${condition['isRedoCourseExam'] eq 'N' }">selected="selected"</c:if>>沿用</option>
								<option value="Y"
									<c:if test="${condition['isRedoCourseExam'] eq 'Y' }">selected="selected"</c:if>>重新累积</option>
						</select></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">请输入学生学号或姓名进行查询</span></li>
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
			<c:choose>
				<c:when test="${condition['isRedoCourseExam'] eq 'N' }">
					<gh:resAuth parentCode="RES_LEARNING_STUDENT_REDOCOURSEEXAM"
						pageType="nlist"></gh:resAuth>
				</c:when>
				<c:otherwise>
					<gh:resAuth parentCode="RES_LEARNING_STUDENT_REDOCOURSEEXAM"
						pageType="ylist"></gh:resAuth>
				</c:otherwise>
			</c:choose>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_studentlearnplan_redocourseexam"
							onclick="checkboxAll('#check_all_studentlearnplan_redocourseexam','resourceid','#studentlearnplan_redocourseexamBody')" /></th>
						<th width="25%">学号</th>
						<th width="25%">姓名</th>
						<th width="25%">课程</th>
						<th width="20%">累积类型</th>
					</tr>
				</thead>
				<tbody id="studentlearnplan_redocourseexamBody">
					<c:forEach items="${studentLearnPlanPage.result}" var="plan"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.resourceid}" autocomplete="off" /></td>
							<td>${plan.studentInfo.studyNo }</td>
							<td>${plan.studentInfo.studentName }</td>
							<td>${plan.teachingPlanCourse.course.courseName}</td>
							<td><c:choose>
									<c:when test="${plan.isRedoCourseExam eq 'N' }">沿用平时成绩</c:when>
									<c:when test="${plan.isRedoCourseExam eq 'Y' }">重新累积平时成绩</c:when>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentLearnPlanPage}"
				goPageUrl="${baseUrl}/edu3/learning/studentlearnplan/redocourseexam/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>