<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷库</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="56">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="30%">课程</th>
						<th width="30%">试卷名称</th>
						<th width="15%">试卷类型</th>
						<th width="15%">考试时长(单位:分钟)</th>
					</tr>
				</thead>
				<tbody id="final_courseExamPapersBody">
					<c:forEach items="${courseExamPapersPage.result}" var="p"
						varStatus="vs">
						<tr>
							<td><input type="radio" name="resourceid"
								value="${p.resourceid }" rel="${p.paperName }"
								autocomplete="off" onclick="setFinalCourseExamPapers(this);"
								<c:if test="${p.resourceid eq condition['vid'] }">checked="checked"</c:if> /></td>
							<td>${p.course.courseName }</td>
							<td>${p.paperName }</td>
							<td>${ghfn:dictCode2Val('CodePaperType',p.paperType) }</td>
							<td>${p.paperTime }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamPapersPage}"
				goPageUrl="${baseUrl }/edu3/framework/finalexaminfo/courseexampapers/list.html"
				pageType="sys" pageNumShown="5" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
function setFinalCourseExamPapers(obj){
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
