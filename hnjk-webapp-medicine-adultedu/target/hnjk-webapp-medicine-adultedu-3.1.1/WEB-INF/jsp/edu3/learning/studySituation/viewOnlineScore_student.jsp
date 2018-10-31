<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看网上学习成绩</title>
</head>
<body>
	<div class="page">
		<c:choose>
			<c:when test="${ !empty noOpenCourse }">
				<div class="pageHeader" style="height: 346px">
					<h1 style="color: red; font-weight: bolder; margin-top: 15px">${noOpenCourse }</h1>
				</div>
			</c:when>
			<c:otherwise>
				<div class="pageHeader">
					<h1>成绩比重：</h1>
					<div class="searchBar">
						<ul class="searchContent" style="height: 35px; margin-top: 10px;">
							<li style="height: 30px;"><span style="color: #183152;">随堂问答比例：</span>${empty usualResultsRule.askQuestionResultPer?0:usualResultsRule.askQuestionResultPer  }%</li>
							<li style="height: 30px;"><span style="color: #183152;">随堂练习比例：</span>${empty usualResultsRule.courseExamResultPer?0:usualResultsRule.courseExamResultPer }%</li>
						</ul>
						<ul class="searchContent" style="height: 35px;">
							<li style="height: 30px;"><span style="color: #183152;">课后作业比例：</span>${empty usualResultsRule.exerciseResultPer?0:usualResultsRule.exerciseResultPer }%</li>
						</ul>
					</div>
				</div>
				<div class="pageHeader">
					<h1>学习成绩：</h1>
					<div class="searchBar">
						<ul class="searchContent" style="height: 35px; margin-top: 10px;">
							<li style="height: 30px;"><span style="color: #183152;">随堂问答分数：</span>${usualResults.askQuestionResults }</li>
							<li style="height: 30px;"><span style="color: #183152;">随堂练习分数：</span>${usualResults.courseExamResults }</li>
						</ul>
						<ul class="searchContent" style="height: 180px">
							<li style="height: 30px;"><span style="color: #183152;">课后作业分数：</span>${usualResults.exerciseResults }</li>
							<li style="height: 30px;"><span style="color: #183152;">综合分数：</span>${usualResults.usualResults }</li>
						</ul>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>