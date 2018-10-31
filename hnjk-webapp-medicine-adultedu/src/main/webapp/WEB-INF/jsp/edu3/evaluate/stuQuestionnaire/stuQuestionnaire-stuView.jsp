<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷列表</title>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
		<br>
		<span><h2>提示：如果学生的分数为最高或最低的10%，则是否有效一栏显示为“否”；<c:if test="${isTeacher }">您的账号为任课老师账号，问卷详情已匿名，你只能查看学生的评分及评语</c:if></h2></span>
		<br>	
		</div>
		<div class="pageContent">
			<table class="table" layouth="110">
				<thead>
					<tr>
					<c:choose>
					<c:when test="${isTeacher }">
					<th width="5%">序号</th>
					<th width="5%">评分</th>
					<th width="5%">是否有效</th>
					<th width="85%">评价</th>
					</c:when>
					<c:otherwise>
						<th width="5%">序号</th>
						<th width="15%">学号</th>
						<th width="10%">姓名</th>
						<th width="5%">评分</th>
						<th width="5%">是否有效</th>
						<th width="60%">评价</th>
					</c:otherwise>
					</c:choose>
					
						
												
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.result}" var="qn" varStatus="vs">
						<tr>
						<c:choose>
						<c:when test="${isTeacher }">
						<td>${vs.index+1 }</td>
						<td>${qn.totalscore}</td>
						<td>${qn.isUse}</td>
						<td>${qn.commentlabel}</td>
						</c:when>
						<c:otherwise>
						<td>${vs.index+1 }</td>
							<td>${qn.studyno}</td>
							<td>${qn.studentname}</td>
							<td>${qn.totalscore}</td>
							<td>${qn.isUse}</td>
							<td>${qn.commentlabel}</td>
						</c:otherwise>
						</c:choose>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/stuList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>