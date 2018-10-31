<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷复查列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exampapercorrect/review/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="onlineExamResults_review_examSubId" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='exam'"
								orderBy="examinputStartTime desc" /> <span style="color: red">*</span>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="onlineExamResults_review_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
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
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_onlineexamResultsreviewlist"
							onclick="checkboxAll('#check_all_onlineexamResultsreviewlist','resourceid','#onlineexamResultsreviewlistBody')" /></th>
						<th width="10%">考试批次</th>
						<th width="10%">课程</th>
						<th width="10%">学号</th>
						<th width="10%">姓名</th>
						<th width="15%">卷面成绩</th>
						<th width="15%">客观题成绩</th>
						<th width="15%">主观题成绩</th>
						<th width="10%">复查试卷</th>
					</tr>
				</thead>
				<tbody id="onlineexamResultsreviewlistBody">
					<c:forEach items="${examResultsList.result}" var="r" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${r.resourceid }" autocomplete="off" /></td>
							<td>${r.batchName }</td>
							<td>${r.courseName }</td>
							<td>${r.studyNo }</td>
							<td>${r.studentName }</td>
							<td><fmt:formatNumber value="${r.writtenScore }"
									pattern="###.#" /></td>
							<td><fmt:formatNumber value="${r.writtenMachineScore }"
									pattern="###.#" /></td>
							<td><fmt:formatNumber
									value="${r.writtenOnlineHandworkScore }" pattern="###.#" /></td>
							<td><a
								href="${baseUrl}/edu3/teaching/exampapercorrect/review.html?examResultId=${r.resourceid}"
								target="dialog" width="800" height="600"
								rel="RES_TEACHING_EXAMPAPERCORRECT_REVIEWLIST" title="复查试卷">复查试卷</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsList}"
				goPageUrl="${baseUrl }/edu3/teaching/exampapercorrect/review/list.html"
				targetType="navTab" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>