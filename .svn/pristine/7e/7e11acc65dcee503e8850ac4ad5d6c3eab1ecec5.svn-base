<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看已发布毕业论文成绩</title>
<script type="text/javascript">
	function modifyThesisExamResultsAudit(){	
		if(isCheckOnlyone('resourceid','#publishedThesisExamResultsForAuditBody')){
			var url = "${baseUrl}/edu3/teaching/examresult/thesis/correct/edit.html";
			navTab.openTab('RES_TEACHING_RESULT_THESIS_CORRECT_EDIT', url+'?resourceid='+$("#publishedThesisExamResultsForAuditBody input[@name='resourceid']:checked").val(), '更正毕业论文成绩');
		}
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examresult/thesis/correct/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>论文批次：</label> <gh:selectModel
								id="publishedThesisExamResultsForAudit_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='thesis'"
								orderBy="examinputStartTime desc" /> <span style="color: red">*</span>
						</li>
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
			<gh:resAuth parentCode="RES_TEACHING_RESULT_THESIS_CORRECT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_publishedThesisExamResultsForAudit"
							onclick="checkboxAll('#check_all_publishedThesisExamResultsForAudit','resourceid','#publishedThesisExamResultsForAuditBody')" /></th>
						<th width="25%">论文批次</th>
						<th width="15%">学号</th>
						<th width="10%">姓名</th>
						<th width="15%">初评成绩</th>
						<th width="15%">答辩成绩</th>
						<th width="15%">终评成绩</th>
					</tr>
				</thead>
				<tbody id="publishedThesisExamResultsForAuditBody">
					<c:forEach items="${examResultsList.result}" var="r" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${r.resourceid }" autocomplete="off" /></td>
							<td>${r.batchName }</td>
							<td>${r.studyNo}</td>
							<td>${r.studentName }</td>
							<td><fmt:formatNumber value="${r.firstScore }"
									pattern="###.#" /></td>
							<td><fmt:formatNumber value="${r.secondScore }"
									pattern="###.#" /></td>
							<td>${ghfn:dictCode2Val('CodeScoreChar',r.integratedScore) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsList}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/thesis/correct/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>