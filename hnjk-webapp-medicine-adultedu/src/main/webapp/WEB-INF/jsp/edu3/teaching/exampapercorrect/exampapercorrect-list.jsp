<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批阅试题列表</title>
<style type="text/css">
#exampapercorrectBody td div {
	line-height: 170%;
	height: auto;
	white-space: normal;
}

#exampapercorrectBody td {
	word-wrap: break-word;
}

#exampapercorrectBody p {
	width: 100%;
}

#exampapercorrectBody div img {
	vertical-align: middle;
}

#exampapercorrectBody td.middle {
	vertical-align: middle;
}
</style>
<script type="text/javascript">
	function _examPaperCorrectNavTabSearch(form){
		if($("#examPaperCorrect_examSubId").val()==""){
			alertMsg.warn("请选择一个考试批次和课程");
			return false;
		}
		return navTabSearch(form);
	}
	//阅卷评分
	function correctExamPapers(){
		if(isCheckOnlyone('resourceid','#exampapercorrectBody')){
			var rObj = $("#exampapercorrectBody input[@name='resourceid']:checked");
			var url = "${baseUrl}/edu3/teaching/exampapercorrect/correct.html?examId="+rObj.val()+"&examSubId="+rObj.attr('rel')+"&isFinishCorrect="+rObj.attr('ref');
			navTab.openTab('RES_TEACHING_EXAMPAPERCORRECT_ANSWER', url, '阅卷评分');
		}
	}
	//批量置零分
	function batchzeroExamPapers(){
		var examSubId = $("#examPaperCorrect_examSubId").val();
		pageBarHandle("您确定要把这些试题未作答的答卷置零分吗？","${baseUrl}/edu3/teaching/exampapercorrect/zero.html?examSubId="+examSubId,"#exampapercorrectBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return _examPaperCorrectNavTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exampapercorrect/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="examPaperCorrect_examSubId" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								value="${condition['examSubId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:52%;" condition="batchType='exam'"
								orderBy="yearInfo.firstYear desc,term desc,resourceid" /></li>
						<li><label>考试课程：</label> <select
							id="examPaperCorrect_courseId" name="courseId"
							style="width: 52%;">
								<option value=""></option>
								<c:forEach items="${courseList }" var="course">
									<option value="${course.resourceid }"
										<c:if test="${course.resourceid eq condition['courseId'] }">selected="selected"</c:if>>${course.coursecode}-${course.courseName }</option>
								</c:forEach>
						</select> <script type="text/javascript">
						$(document).ready(function(){
							$("#examPaperCorrect_courseId").flexselect();
					    });
					</script></li>
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
				pageType="sublist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_exampapercorrect"
							onclick="checkboxAll('#check_all_exampapercorrect','resourceid','#exampapercorrectBody')" /></th>
						<th width="10%">课程</th>
						<th width="45%">考试试题</th>
						<th width="10%">使用次数</th>
						<th width="10%">未作答数</th>
						<th width="10%">未批阅数</th>
						<th width="10%">已批阅数</th>
					</tr>
				</thead>
				<tbody id="exampapercorrectBody">
					<c:forEach items="${examList.result}" var="exam" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.examid }" rel="${exam.examsubid }"
								ref="${exam.uncorrectNum eq 0 ? 'Y' : 'N' }" autocomplete="off" /></td>
							<td>${exam.courseName }</td>
							<td>
								<div style="font-weight: bold;">
									<c:choose>
										<c:when test="${not empty exam.examNodeType }">${ghfn:dictCode2Val('CodeExamNodeType',exam.examNodeType) }</c:when>
										<c:otherwise>${ghfn:dictCode2Val('CodeExamType',exam.examType) }</c:otherwise>
									</c:choose>
								</div>
								<div>${exam.question }</div>
								<div>
									<b>参考答案：</b>${exam.answer }</div>
							</td>
							<td class="middle">${exam.answerNum }</td>
							<td class="middle">${exam.noAnswerNum }</td>
							<td class="middle">${exam.uncorrectNum }</td>
							<td class="middle">${exam.correctNum }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examList}"
				goPageUrl="${baseUrl }/edu3/teaching/exampapercorrect/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>