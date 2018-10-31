<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程作业</title>
<script type="text/javascript">
	//批改作业入口,进入学生作业列表
	function listStudentExercise(exerciseBatchId){
		var url = "${baseUrl}/edu3/teaching/exercisecheck/studentexercise/list.html";
		navTab.openTab('navTab', url+'?exerciseBatchId='+exerciseBatchId, '批改该次学生作业');
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exercisecheck/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名称：</label><input name="courseName"
							style="width: 120px" value="${condition['courseName']}" /></li>
						<li><label>批次名称：</label><input type="text" name="colName"
							value="${condition['colName'] }" /></li>
						<li><label>作业类型：</label>
						<gh:select name="colType" value="${condition['colType']}"
								dictionaryCode="CodeLearningColType" /></li>
						<li><label>作业状态：</label>
						<gh:select name="status" value="${condition['status']}"
								dictionaryCode="CodeExerciseBatchStatus" /></li>
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
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="8%">所属课程</th>
						<th width="8%">批次名称</th>
						<th width="7%">作业类型</th>
						<th width="5%">年度</th>
						<th width="5%">学期</th>
						<th width="8%">作业开始日期</th>
						<th width="8%">作业截止日期</th>
						<th width="7%">作业状态</th>
						<th width="8%">记分分形式</th>
						<th width="8%">已提交人数</th>
						<th width="8%">平均分</th>
						<th width="10%">&nbsp;</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="exerciseCheckBody">
					<c:forEach items="${exerciseBatchListPage.result }"
						var="exerciseBatch" varStatus="vs">
						<tr>
							<td>${exerciseBatch.course.courseName }</td>
							<td>${exerciseBatch.colName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningColType',exerciseBatch.colType) }</td>
							<td>${exerciseBatch.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',exerciseBatch.term) }</td>
							<td><fmt:formatDate value="${exerciseBatch.startDate }"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${exerciseBatch.endDate }"
									pattern="yyyy-MM-dd" /></td>
							<td>${ghfn:dictCode2Val('CodeExerciseBatchStatus',exerciseBatch.status) }</td>
							<td>${ghfn:dictCode2Val('CodeExerciseBatchScoringType',exerciseBatch.scoringType) }</td>
							<td>${studentFinishedCount[exerciseBatch.resourceid].num }</td>
							<td><fmt:formatNumber pattern="###.#"
									value="${studentFinishedCount[exerciseBatch.resourceid].result }" /></td>
							<td><a href="javascript:;"
								onclick="listStudentExercise('${exerciseBatch.resourceid}');">学生作业列表</a></td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/exercise/student/view.html?exerciseBatchId=${exerciseBatch.resourceid }','viewStudentExerciseFinished','查看学生提交情况',{width:600,height:400});">查看提交情况</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${exerciseBatchListPage}"
				goPageUrl="${baseUrl}/edu3/teaching/exercisecheck/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>