<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷题库</title>
</head>
<script type="text/javascript">


	//新增
	function addQuestionBank() {
		navTab.openTab('navTab',
				'${baseUrl}/edu3/teaching/quality/evaluation/questionBank/edit.html', '新增问卷题目');
	}

	//修改
	function editQuestionBank() {
		var url = "${baseUrl}/edu3/teaching/quality/evaluation/questionBank/edit.html";
		if (isCheckOnlyone('resourceid', '#QuestionBankBody')) {
			navTab.openTab('_blank',url+ '?resourceid='+ $("#QuestionBankBody input[@name='resourceid']:checked").val(), '编辑问卷题目');
		}
	}

	//删除
	function deleteQuestionBank() {
		pageBarHandle("您确定要删除这些记录吗？",
				"${baseUrl}/edu3/teaching/quality/evaluation/questionBank/delete.html",
				"#QuestionBankBody");
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/quality/evaluation/questionBank/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>问卷题目类型：</label><input type="text" name="courseType"
							value="${condition['courseType']}" /></li>
						
					</ul>
					<br>
					<li><font color="red" >当前面授类课程问卷总分为：${faceSum }</font></li>
					
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
			<gh:resAuth parentCode="RES_TEACHING_QUALITY_EVALUATION_QUESTIONBANK" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_evaluation_question"
<!-- 							onclick="checkboxAll('#check_all_feemajor','resourceid','#feemajorBody')"  -->
							</th>
						<th width="3%">序号</th>
						<th width="10%">调查指标</th>
						<th width="40%">指标内涵</th>
						<th width="10%">分值</th>
						<th width="10%">问卷题目类型</th>
						<th width="15%">备注</th>						
					</tr>
				</thead>
				<tbody id="QuestionBankBody">
					<c:forEach items="${questionPage.result}" var="qs"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${qs.resourceid }" autocomplete="off" /></td>
							<td>${qs.showOrder }</td>
							<td>${ghfn:dictCode2Val('CodeQuestionBankQuestion',qs.questionTarget) }</td>
							<td>${qs.question }</td>
							<td>${qs.score }</td>
							<td>${ghfn:dictCode2Val('CodeQuestionBankCourseType',qs.courseType) }</td>
							<td>${qs.memo }</td>
<%-- 							<td>${ghfn:dictCode2Val('CodeQuestionBankUserType',qs.userType) }</td> --%>
<%-- 							<td>${ghfn:dictCode2Val('CodeQuestionBankCourseType',qs.courseType) }</td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${questionPage}"
				goPageUrl="${baseUrl }/edu3/teaching/quality/evaluation/questionBank/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>