<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷题库</title>
</head>
<script type="text/javascript">


	//新增
	function addQuestionnaireBatch() {
		navTab.openTab('navTab',
				'${baseUrl}/edu3/teaching/quality/evaluation/questionnaireBatch/edit.html', '新增教评批次');
	}

	//修改
	function editQuestionnaireBatch() {
		var url = "${baseUrl}/edu3/teaching/quality/evaluation/questionnaireBatch/edit.html";
		if (isCheckOnlyone('resourceid', '#QuestionnaireBatchBody')) {
			navTab.openTab('_blank',url+ '?resourceid='+ $("#QuestionnaireBatchBody input[@name='resourceid']:checked").val(), '编辑教评批次');
		}
	}

	//删除
	function deleteQuestionnaireBatch() {
		pageBarHandle("您确定要删除这些记录吗？",
				"${baseUrl}/edu3/teaching/quality/evaluation/questionnaireBatch/delete.html",
				"#QuestionnaireBatchBody");
	}
	function publishQuestionnaireBatch() {
		pageBarHandle("您确定要发布吗？发布后将生成相应年度、学期的调查，且不可撤销发布",
				"${baseUrl}/edu3/teaching/quality/evaluation/questionnaireBatch/publish.html",
				"#QuestionnaireBatchBody");
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/quality/evaluation/questionnaireBatch/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel
								id="questionnaireBatch_yearInfoId" name="yearId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" orderBy="yearName desc"
								style="width:125px" /></li>
						<li><label>学期：</label> <gh:select name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:125px" /></li>
						<li><label>是否发布：</label> <gh:select name="isPublish"
								dictionaryCode="yesOrNo" value="${condition['isPublish']}"
								style="width:125px" /></li>

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
			<gh:resAuth parentCode="RES_TEACHING_QUALITY_EVALUATION_QUESTIONNAIRE_BATCH" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_evaluation_question"
<!-- 							onclick="checkboxAll('#check_all_feemajor','resourceid','#feemajorBody')"  -->
							</th>
						<th width="15%">年度</th>
						<th width="10%">学期</th>
						<th width="10%">是否发布</th>
						<th width="15%">面授开始时间</th>
						<th width="15%">面授结束时间</th>
						<th width="15%">网络开始时间</th>
						<th width="15%">网络结束时间</th>							
					</tr>
				</thead>
				<tbody id="QuestionnaireBatchBody">
					<c:forEach items="${page.result}" var="qb"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${qb.resourceid }" autocomplete="off" /></td>
							<td>${qb.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',qb.term) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',qb.isPublish) }</td>
							<td><fmt:formatDate value="${qb.faceStartTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${qb.faceEndTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${qb.netStartTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${qb.netEndTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/quality/evaluation/questionnaireBatch/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>