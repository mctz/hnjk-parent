<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>更正成绩复审</title>
<script type="text/javascript">
	function publishAuditExamResults(){
		pageBarHandle("您确定要更正这些成绩并审核发布吗？","${baseUrl}/edu3/teaching/examresult/correct/audit/publish.html","#audit_examResultsAuditBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examresult/correct/audit/list.html"
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
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examresultsaudit_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>更正人：</label> <input type="text" name="changedMan"
							value="${condition['changedMan'] }" /></li>
						<li><label>复查人：</label> <input type="text" name="auditMan"
							value="${condition['auditMan'] }" /></li>
						<li><label>更正开始时间：</label> <input type="text" id="startTime"
							name="startTime" style="width: 50%"
							value="${condition['startTime']}"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" />
						</li>
						<li><label>更正结束时间：</label> <input type="text" id="endTime"
							name="endTime" style="width: 50%" value="${condition['endTime']}"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" />
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
			<gh:resAuth parentCode="RES_TEACHING_RESULT_CORRECT_AUDIT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="4%" style="text-align: center;"><input
							type="checkbox" name="checkall"
							id="check_all_audit_examResultsAudit"
							onclick="checkboxAll('#check_all_audit_examResultsAudit','resourceid','#audit_examResultsAuditBody')" /></th>
						<th width="8%" style="text-align: center;">课程</th>
						<th width="8%" style="text-align: center;">学号</th>
						<th width="8%" style="text-align: center;">姓名</th>
						<th width="8%" style="text-align: center;">更正前平时成绩</th>
						<th width="8%" style="text-align: center;">更正前卷面成绩</th>
						<th width="8%" style="text-align: center;">更正前综合成绩</th>
						<th width="8%" style="text-align: center;">更正后平时成绩</th>
						<th width="8%" style="text-align: center;">更正后卷面成绩</th>
						<th width="8%" style="text-align: center;">更正后综合成绩</th>
						<th width="8%" style="text-align: center;">更正人/时间</th>
						<th width="8%" style="text-align: center;">复查人/时间</th>
						<th width="8%" style="text-align: center;">备注</th>
					</tr>
				</thead>
				<tbody id="audit_examResultsAuditBody">
					<c:forEach items="${examResultsAuditList.result}" var="audit"
						varStatus="vs">
						<tr>
							<td style="text-align: center;"><input type="checkbox"
								name="resourceid" value="${audit.resourceid }"
								autocomplete="off" /></td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.examResults.course.courseName }">${audit.examResults.course.courseName }</td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.examResults.studentInfo.studyNo}">${audit.examResults.studentInfo.studyNo}</td>
							<td style="text-align: center; vertical-align: middle;"
								title="${audit.examResults.studentInfo.studentName}">${audit.examResults.studentInfo.studentName}</td>
							<td
								style="text-align: center; vertical-align: middle; color: blue">${audit.beforeUsuallyScore }</td>
							<td
								style="text-align: center; vertical-align: middle; color: blue">
								<c:choose>
									<c:when test="${audit.beforeExamAbnormity eq '0'}"> ${audit.beforeWrittenScore }</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.beforeExamAbnormity) }</c:otherwise>
								</c:choose>
							</td>
							<td
								style="text-align: center; vertical-align: middle; color: blue">
								<c:choose>
									<c:when test="${audit.beforeExamAbnormity eq '0'}"> ${audit.beforeIntegratedScore }</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.beforeExamAbnormity) }</c:otherwise>
								</c:choose>
							</td>
							<td
								style="text-align: center; vertical-align: middle; color: red">${audit.changedUsuallyScore }</td>
							<td
								style="text-align: center; vertical-align: middle; color: red">
								<c:choose>
									<c:when test="${audit.changedExamAbnormity eq '0'}"> ${audit.changedWrittenScore }</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.changedExamAbnormity) }</c:otherwise>
								</c:choose>
							</td>
							<td
								style="text-align: center; vertical-align: middle; color: red">
								<c:choose>
									<c:when test="${audit.changedExamAbnormity eq '0'}"> ${audit.changedIntegratedScore }</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.changedExamAbnormity) }</c:otherwise>
								</c:choose>
							</td>
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
				goPageUrl="${baseUrl }/edu3/teaching/examresult/correct/audit/list.html"
				targetType="navTab" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>