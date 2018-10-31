<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习查看</title>
<style>
#activecourseexam_all_view_table.list div {
	line-height: 170%;
}

#activecourseexam_all_view_table.list td {
	word-wrap: break-word
}

#activecourseexam_all_view_table.list p {
	width: 100%;
	height: auto;
}

#activecourseexam_all_view_table.list img {
	vertical-align: middle;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/view.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> ${activecourseexamallviewCourse }</li>
						<li><label>知识节点：</label> <select
							id="activecourseexamview_syllabusId" name="syllabusId"
							style="width: 125px;">
								<option value="">请选择</option>
								<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
									<option value="${syb.resourceid }"
										<c:if test="${syb.resourceid eq condition['syllabusId']}"> selected </c:if>>
										<c:forEach var="seconds" begin="1" end="${syb.syllabusLevel}"
											step="1">&nbsp;&nbsp;</c:forEach> ${syb.syllabusName }
									</option>
								</c:forEach>
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
		<div class="pageContent" layouth="96">
			<table id="activecourseexam_all_view_table" class="list"
				style="width: 100%; table-layout: fixed;">
				<thead>
					<tr>
						<th width="10%"></th>
						<th>试题内容</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${activeCourseExamList.result }" var="exam"
						varStatus="vs">
						<c:if
							test="${syllabusExam[exam.syllabus.resourceid] eq exam.resourceid }">
							<tr>
								<td colspan="2">
									<div style="font-weight: bold;">&nbsp;&nbsp;
										${exam.syllabus.syllabusName }</div>
								</td>
							</tr>
						</c:if>
						<tr>
							<td><c:choose>
									<c:when test="${exam.courseExam.examType eq '6' }">
										<span style="font-weight: bold;">${ghfn:dictCode2Val('CodeExamType',exam.courseExam.examType) }</span>
									</c:when>
									<c:otherwise>
										<span style="font-weight: bold;">${exam.showOrder }.
											(${ghfn:dictCode2Val('CodeExamType',exam.courseExam.examType) })</span>
									</c:otherwise>
								</c:choose></td>
							<td>
								<div>${exam.courseExam.question }</div>
								<div tyle="font-weight: bold;">答案：${exam.courseExam.answer }</div>
							</td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</div>
		<gh:page page="${activeCourseExamList}"
			goPageUrl="${baseUrl}/edu3/metares/exercise/activeexercise/view.html"
			targetType="navTab" condition="${condition }" pageType="sys" />
	</div>
</body>
</html>