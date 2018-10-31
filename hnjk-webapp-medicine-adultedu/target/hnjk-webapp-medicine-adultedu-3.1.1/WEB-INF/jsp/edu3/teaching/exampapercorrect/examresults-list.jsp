<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线作答成绩列表</title>
<script type="text/javascript">
function recorrectExamPaper(){
	if(isCheckOnlyone('resourceid','#onlineexamResultslistBody')){
		var rObj = $("#onlineexamResultslistBody input[@name='resourceid']:checked");
		var url = "${baseUrl}/edu3/teaching/exampapercorrect/recorrect.html?examResultId="+rObj.val();
		navTab.openTab('RES_TEACHING_EXAMPAPERCORRECT_RECORRECT', url, '重新批阅');
	}
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exampapercorrect/examresults/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="onlineExamResults_submit_examSubId" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='exam'"
								orderBy="examinputStartTime desc" /> <span style="color: red">*</span>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="onlineExamResults_submit_courseId"
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
			<gh:resAuth parentCode="RES_TEACHING_EXAMPAPERCORRECT"
				pageType="tlist"></gh:resAuth>
			<table class="table" layouth="161" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_onlineexamResultslist"
							onclick="checkboxAll('#check_all_onlineexamResultslist','resourceid','#onlineexamResultslistBody')" /></th>
						<th width="10%">考试批次</th>
						<th width="10%">课程</th>
						<th width="10%">学号</th>
						<th width="10%">姓名</th>
						<th width="15%">卷面成绩</th>
						<th width="15%">客观题成绩</th>
						<th width="15%">主观题成绩</th>
						<th width="10%">成绩状态</th>
					</tr>
				</thead>
				<tbody id="onlineexamResultslistBody">
					<c:forEach items="${examResultsList.result}" var="r" varStatus="vs">
						<tr>
							<td><c:choose>
									<c:when test="${r.checkStatus gt 0 }">
										<input type="hidden" name="resourceid"
											value="${r.resourceid }" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="resourceid"
											value="${r.resourceid }" autocomplete="off" />
									</c:otherwise>
								</c:choose></td>
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
							<td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',r.checkStatus)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsList}"
				goPageUrl="${baseUrl }/edu3/teaching/exampapercorrect/examresults/list.html"
				targetType="navTab" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>