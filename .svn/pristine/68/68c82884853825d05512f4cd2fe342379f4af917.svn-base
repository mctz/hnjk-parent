<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>随堂练习预览</title>
<style>
.pageFormContent p {
	float: left;
	display: block;
	width: 100%;
	height: 150%;
	margin: 0;
	padding: 5px 0;
	position: relative;
}
</style>
</head>
<body>
	<h2 class="contentTitle">随堂练习预览</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<table class="form" style="background-color: #FFF;">
					<tr>
						<td>
							<div style="line-height: 150%;">${activeCourseExam.showOrder }.&nbsp;${activeCourseExam.question }&nbsp;(${activeCourseExam.score }分)</div>
							<div>
								<c:forEach items="${activeCourseExam.answers }" var="answer">
									<div style="line-height: 150%;">&nbsp;&nbsp;&nbsp;&nbsp;${answer.showOrder}.&nbsp;&nbsp;${answer.answer}</div>
								</c:forEach>
							</div>
						</td>
					</tr>
					<tr>
						<td><div style="line-height: 150%;">正确答案：${activeCourseExam.correctAnswer }</div></td>
					</tr>
					<tr>
						<td>问题解析：
							<div style="line-height: 150%;">${activeCourseExam.questionParse }</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="button">
							<div class="buttonContent">
								<button type="button" class="close">关闭</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>