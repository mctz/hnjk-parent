<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷题目设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑题目</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/quality/evaluation/questionBank/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${questionBank.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%">序号</td>
							<td style="width: 38%"><input type="text" name="showOrder" style="width: 50%"
								value="${questionBank.showOrder }" class="required"/></td>
							<td style="width: 12%">调查指标</td>
							<td style="width: 38%"><gh:select name="questionTarget" value="${questionBank.questionTarget}"
						dictionaryCode="CodeQuestionBankQuestion" style="width:52%" classCss="required" /></td> 
						</tr>
						<tr>
							
							<td style="width: 12%">课程类型</td>
							<td style="width: 38%"><gh:select name="courseType" value="${questionBank.courseType}"
									dictionaryCode="CodeQuestionBankCourseType" style="width:52%" classCss="required" /></td>
							<td style="width: 12%">分值</td>
							<td style="width: 38%"><input type="text" name="score" style="width: 50%"
								value="${questionBank.score }" class="required"/></td>
						</tr>
						
						<tr>
							<td style="width: 12%">指标内涵</td>
							<td colspan="3"><textarea name="question"
									style="height: 30px; width: 80%">${questionBank.question }</textarea></td>
						</tr>
						<tr>
							<td style="width: 12%">备注</td>
							<td style="width: 38%"><input type="text" name="memo" style="width: 50%"
								value="${questionBank.memo }" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>