<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络阅卷课程列表</title>
<script type="text/javascript">
	//阅卷试题列表
	function listExamPaperCorrect(){
		if(isCheckOnlyone('resourceid','#exampapercorrectcourseBody')){
			var rObj = $("#exampapercorrectcourseBody input[@name='resourceid']:checked");
			var url = "${baseUrl}/edu3/teaching/exampapercorrect/list.html?courseId="+rObj.val()+"&examSubId="+rObj.attr('rel');
			navTab.openTab('RES_TEACHING_EXAMPAPERCORRECT_LIST', url, '阅卷试题列表');
		}
	}
	//网络阅卷机考成绩查看
	function listOnlineExamResults(){
		if(isCheckOnlyone('resourceid','#exampapercorrectcourseBody')){
			var rObj = $("#exampapercorrectcourseBody input[@name='resourceid']:checked");
			var url = "${baseUrl}/edu3/teaching/exampapercorrect/examresults/list.html?courseId="+rObj.val()+"&examSubId="+rObj.attr('rel');
			navTab.openTab('RES_TEACHING_EXAMPAPERCORRECT_EXAMRESULTS', url, '网络阅卷机考成绩');
		}
	}
	//提交成绩
	function submitOnlineExamResults(){
		var examSubId = $("#examPaperCorrect_course_examSubId").val();
		pageBarHandle("您确定要提交这些课程的机考成绩吗？","${baseUrl}/edu3/teaching/exampapercorrect/examresults/submit.html?examSubId="+examSubId,"#exampapercorrectcourseBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exampapercorrect/course/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="examPaperCorrect_course_examSubId" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								value="${condition['examSubId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:52%;" condition="batchType='exam'"
								orderBy="yearInfo.firstYear desc,term desc,resourceid" /></li>
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
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_exampapercorrectcourse"
							onclick="checkboxAll('#check_all_exampapercorrectcourse','resourceid','#exampapercorrectcourseBody')" /></th>
						<th width="50%">考试批次</th>
						<th width="45%">考试课程</th>
					</tr>
				</thead>
				<tbody id="exampapercorrectcourseBody">
					<c:forEach items="${examCourseList.result}" var="c" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${c.courseid }" rel="${c.examsubid }" autocomplete="off" /></td>
							<td>${c.batchName }</td>
							<td>${c.courseName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examCourseList}"
				goPageUrl="${baseUrl }/edu3/teaching/exampapercorrect/course/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>