<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生平时分积分规则管理</title>
</head>
<body>
	<h2 class="contentTitle">学生平时分积分基本规则</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/usualresultsrule/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="type" value="init" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"></label>
							<gh:selectModel id="usualresultsrule_initform_yearInfoId"
									name="yearInfoId" bindValue="resourceid"
									displayValue="yearName" style="width:130px"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${usualResultsRule.yearInfo.resourceid}"
									orderBy="firstYear desc" classCss="required" /> <span
								style="color: red;">*</span></td>
							<td width="20%">学期:</td>
							<td><gh:select id="usualresultsrule_initform_term"
									name="term" value="${usualResultsRule.term}"
									dictionaryCode="CodeTerm" style="width:50%;"
									classCss="required" /> <span style="color: red;">*</span></td>
						</tr>
						<tr>
							<td width="20%">优秀帖满分个数:</td>
							<td width="30%"><input name="bbsBestTopicNum"
								value="${usualResultsRule.bbsBestTopicNum }" size="5"
								style="text-align: right;" class="required digits" /></td>
							<td>随堂练习满分正确率:</td>
							<td><input name="courseExamCorrectPer"
								value="${usualResultsRule.courseExamCorrectPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							
						</tr>
						<tr><td colspan="4"><div style="color: green; padding: 8px;">说明：下列各项比例总和应为100%！</div></td></tr>
						<tr>
							<td>随堂问答分比例:</td>
							<td><input name="askQuestionResultPer"
								value="${usualResultsRule.askQuestionResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td>随堂练习分比例:</td>
							<td><input name="courseExamResultPer"
								value="${usualResultsRule.courseExamResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
						</tr>
						<tr>
							<td>作业练习分比例:</td>
							<td><input name="exerciseResultPer"
								value="${usualResultsRule.exerciseResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td width="20%"></td>
							<td width="30%"></td>
						</tr>
						<c:if test="${defaultRule eq 'N'}">
						<tr>
							<td>网络辅导分比例:</td>
							<td width="30%"><input name="bbsResultPer"
								value="${usualResultsRule.bbsResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td>同步自测分比例:</td>
							<td><input name="selftestResultPer"
								value="${usualResultsRule.selftestResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
						</tr>
						<tr>
							<td>实践及其他分比例:</td>
							<td><input name="otherResultPer"
								value="${usualResultsRule.otherResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td>面授考勤分比例:</td>
							<td><input name="faceResultPer"
								value="${usualResultsRule.faceResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
						</tr>
						</c:if>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" rows="5" cols=""
									style="width: 70%">${usualResultsRule.memo }</textarea></td>
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
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>