<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网上考试卷面成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresults/writtenscore/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="OnlineExamResults_examSub" name="examSub"
								bindValue="resourceid" displayValue="batchName"
								style="width:120px"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSub']}" condition="batchType='exam'"
								orderBy='yearInfo.firstYear desc,term desc' /> <span
							style="color: red;">*</span></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="OnlineExamResults_courseId" isFilterTeacher="Y"
								value="${condition['courseId']}"></gh:courseAutocomplete></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name'] }" /></li>
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
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
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_onlineexamresult"
							onclick="checkboxAll('#check_all_onlineexamresult','resourceid','#check_all_onlineexamresultBody')" /></th>
						<th width="15%">考试批次</th>
						<th width="15%">课程</th>
						<th width="10%">姓名</th>
						<th width="15%">学号</th>
						<th width="10%">卷面成绩</th>
					</tr>
				</thead>
				<tbody id="check_all_onlineexamresultBody">
					<c:forEach items="${examResultsPage.result}" var="examResult"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examResult.resourceid }" autocomplete="off" /></td>
							<td>${examResult.examInfo.examSub.batchName }</td>
							<td>${examResult.course.courseName }</td>
							<td>${examResult.studentInfo.studentName }</td>
							<td>${examResult.studentInfo.studyNo }</td>
							<td>${examResult.writtenScore }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsPage}"
				goPageUrl="${baseUrl }/edu3/teaching/examresults/writtenscore/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>