<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷成卷规则</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="56">
				<thead>
					<tr>
						<th width="6%"></th>
						<th width="15%">规则名称</th>
						<th width="15%">考试时长(分钟)</th>
						<th width="16%">试题类型</th>
						<th width="16%">试题题型</th>
						<th width="16%">试题数</th>
						<th width="16%">试题分数</th>
					</tr>
				</thead>
				<tbody id="final_courseExamRulesBody">
					<c:forEach items="${courseExamRulesPage.result}" var="examRule"
						varStatus="vs">
						<tr>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }"><input
								type="radio" name="resourceid" value="${examRule.resourceid }"
								rel="${examRule.courseExamRulesName }" autocomplete="off"
								onclick="setFinalCourseExamRules(this);"
								<c:if test="${examRule.resourceid eq condition['vid'] }">checked="checked"</c:if> /></td>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }">${examRule.courseExamRulesName }<c:if
									test="${not empty examRule.paperSourse }">(题库来源：${ghfn:dictCode2Val('CodeExamform',examRule.paperSourse) })</c:if></td>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }">${examRule.examTimeLong }</td>
						</tr>
						<c:forEach items="${examRule.courseExamRulesDetails }"
							var="detail">
							<tr>
								<td>${ghfn:dictCode2Val('CodeExamNodeType',detail.examNodeType) }</td>
								<td>${ghfn:dictCode2Val('CodeExamType',detail.examType) }</td>
								<td>${detail.examNum }</td>
								<td>${detail.examValue }</td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamRulesPage}"
				goPageUrl="${baseUrl }/edu3/framework/finalexaminfo/courseexamrule/list.html"
				pageType="sys" pageNumShown="5" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
function setFinalCourseExamRules(obj){
	var $obj = $(obj);
	if($obj.attr("checked")){
		$("#${condition['ids']}").val($obj.val());
		$("#${condition['names']}").val($obj.attr("rel"));
		setTimeout(function(){$.pdialog.closeCurrent();}, 100);
	}
}
</script>
</body>
</html>
