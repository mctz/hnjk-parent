<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更正毕业论文成绩复审</title>
<script type="text/javascript">
	function publishThesisExamResultsAudit (){
		pageBarHandle("您确定要更正这些成绩并审核发布吗？","${baseUrl}/edu3/teaching/examresult/thesis/correct/audit/publish.html","#audit_thesis_examResultsAuditBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresult/thesis/correct/audit/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>审核状态：</label> <select name="auditStatus"
							style="width: 50%;">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['auditStatus'] eq '0' }">selected="selected"</c:if>>待审核</option>
								<option value="1"
									<c:if test="${condition['auditStatus'] eq '1' }">selected="selected"</c:if>>审核通过</option>
						</select></li>
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
			<gh:resAuth parentCode="RES_TEACHING_RESULT_THESIS_CORRECT_AUDIT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="4%" style="text-align: center;"><input
							type="checkbox" name="checkall"
							id="check_all_audit_thesis_examResultsAudit"
							onclick="checkboxAll('#check_all_audit_thesis_examResultsAudit','resourceid','#audit_thesis_examResultsAuditBody')" /></th>
						<th width="8%" style="text-align: center;">学号</th>
						<th width="8%" style="text-align: center;">姓名</th>
						<th width="9%" style="text-align: center;">更正前初评成绩</th>
						<th width="9%" style="text-align: center;">更正前答辩成绩</th>
						<th width="9%" style="text-align: center;">更正前终评成绩</th>
						<th width="9%" style="text-align: center;">更正后初评成绩</th>
						<th width="9%" style="text-align: center;">更正后答辩成绩</th>
						<th width="9%" style="text-align: center;">更正后终评成绩</th>
						<th width="9%" style="text-align: center;">更正人/时间</th>
						<th width="9%" style="text-align: center;">复查人/时间</th>
						<th width="8%" style="text-align: center;">备注</th>
					</tr>
				</thead>
				<tbody id="audit_thesis_examResultsAuditBody">
					<c:forEach items="${examResultsAuditList.result}" var="audit"
						varStatus="vs">
						<tr>
							<td style="text-align: center;"><input type="checkbox"
								name="resourceid" value="${audit.resourceid }"
								autocomplete="off" /></td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.examResults.studentInfo.studyNo}">${audit.examResults.studentInfo.studyNo}</td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.examResults.studentInfo.studentName}">${audit.examResults.studentInfo.studentName}</td>
							<td
								style="text-align: center; vertical-align: middle; color: blue"><fmt:formatNumber
									value="${audit.beforeFirstScore }" pattern="###.#" /></td>
							<td
								style="text-align: center; vertical-align: middle; color: blue"><fmt:formatNumber
									value="${audit.beforeSecondScore }" pattern="###.#" /></td>
							<td
								style="text-align: center; vertical-align: middle; color: blue">${ghfn:dictCode2Val('CodeScoreChar',audit.beforeIntegratedScore) }</td>
							<td
								style="text-align: center; vertical-align: middle; color: red"><fmt:formatNumber
									value="${audit.changedFirstScore }" pattern="###.#" /></td>
							<td
								style="text-align: center; vertical-align: middle; color: red"><fmt:formatNumber
									value="${audit.changedSecondScore }" pattern="###.#" /></td>
							<td
								style="text-align: center; vertical-align: middle; color: red">${ghfn:dictCode2Val('CodeScoreChar',audit.changedIntegratedScore) }</td>
							<td style="text-align: center; vertical-align: middle;"
								title="(${audit.changedMan})<fmt:formatDate value="${audit.changedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>">(${audit.changedMan})<fmt:formatDate
									value="${audit.changedDate}" pattern="yyyy-MM-dd" />
							</td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${not empty audit.auditMan and not empty audit.auditDate}">(${audit.auditMan})${audit.auditDate}</c:if>
							</td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.memo}">${audit.memo}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsAuditList}"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/thesis/correct/audit/list.html"
				targetType="navTab" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>