<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单科成绩列表</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${msg}";
		if(""!=msg){
			alertMsg.warn(msg);
		}
	});
</script>
	<div class="page">
		<div class="pageHeader">
			<table width="100%">
				<tr height="30px">
					<td style="color: #183152; width: 10%"><strong> 考试批次:</strong></td>
					<td>${examSub.batchName }</td>
					<td style="color: #183152; width: 10%"><strong>考试课程:</strong></td>
					<td>${examInfo.course.courseName}</td>

				</tr>
				<tr height="30px">
					<td style="color: #183152; width: 10%"><strong>考查方式：</strong>
					</td>
					<td>${ghfn:dictCode2Val('CodeCourseExamType',examInfo.course.examType)}</td>
					<td style="color: #183152; width: 10%"><strong>考试日期：</strong></td>
					<td><fmt:formatDate value="${examInfo.examStartTime }"
							pattern="yyyy-MM-dd HH:mm" /> -<fmt:formatDate
							value="${examInfo.examEndTime }" pattern="HH:mm" /></td>
				</tr>
			</table>
		</div>
		<div class="pageContent">
			<table class="table" width="100%" layouth="120">
				<thead>
					<tr>
						<th width="4%">序号</th>
						<th width="16%">教学中心</th>
						<th width="16%">专业</th>
						<th width="8%">学号</th>
						<th width="8%">姓名</th>
						<th width="8%">卷面成绩</th>
						<th width="8%">平时成绩</th>
						<th width="8%">综合成绩</th>
						<th width="8%">成绩状态</th>
						<th width="8%">选考次数</th>
						<th width="8%">备注</th>
					</tr>
				</thead>
				<tbody id="courseExamResultsViewBody">
					<c:forEach items="${objPage.result}" var="examResults"
						varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${examResults.studentInfo.branchSchool.unitName }</td>
							<td>${examResults.studentInfo.major.majorName}</td>
							<td>${examResults.studentInfo.studyNo}</td>
							<td>${examResults.studentInfo.studentName}</td>
							<td>${examResults.writtenScore}</td>
							<td>${examResults.usuallyScore}</td>
							<td>${examResults.integratedScore}</td>
							<td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
							<td>${examResults.examCount}</td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/course-examresults-view.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>