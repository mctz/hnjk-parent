<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生平时分积分规则管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty usualResultsRule.resourceid)?'新增':'编辑' }学生平时分积分规则</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/usualresultsrule/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${usualResultsRule.resourceid }" /> <input type="hidden"
					name="isUsed" value="${usualResultsRule.isUsed }" /> <input
					type="hidden" name="versionNum"
					value="${usualResultsRule.versionNum }" /> <input type="hidden"
					name="fillinDate"
					value="<fmt:formatDate value='${usualResultsRule.fillinDate }' pattern='yyyy-MM-dd'/>" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"></label>
							<gh:selectModel id="usualresultsruleform_yearInfoId"
									name="yearInfoId" bindValue="resourceid"
									displayValue="yearName" style="width:130px"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${usualResultsRule.yearInfo.resourceid}"
									orderBy="firstYear desc" classCss="required" /> <span
								style="color: red;">*</span></td>
							<td width="20%">学期:</td>
							<td><gh:select id="usualresultsruleform_term" name="term"
									value="${usualResultsRule.term}" dictionaryCode="CodeTerm"
									style="width:50%;" classCss="required" /> <span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td width="20%">课程:</td>
							<td colspan="3"><gh:courseAutocomplete name="courseId"
									tabindex="1" id="usualresultsruleform_courseId"
									style="width:50%;"
									value="${usualResultsRule.course.resourceid}"
									displayType="code" classCss="required" isFilterTeacher="Y" />
							</td>
						</tr>
						<tr>
							<td width="20%">随堂问答分比例:</td>
							<td width="30%"><input name="askQuestionResultPer"
								value="${usualResultsRule.askQuestionResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td width="20%">优秀帖满分个数:</td>
							<td width="30%"><input name="bbsBestTopicNum"
								value="${usualResultsRule.bbsBestTopicNum }" size="5" 
								style="text-align: right;" class="required digits" /></td>
						</tr>
						<tr>
							<td>随堂练习分比例:</td>
							<td><input name="courseExamResultPer"
								value="${usualResultsRule.courseExamResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>作业练习分比例:</td>
							<td><input name="exerciseResultPer"
								value="${usualResultsRule.exerciseResultPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
							
							<td>随堂练习满分正确率:</td>
							<td><input name="courseExamCorrectPer"
								value="${usualResultsRule.courseExamCorrectPer }" size="5"
								style="text-align: right;" class="required digits" maxlength="3" />%</td>
						</tr>
						<c:if test="${defaultRule ne 'N'}">
							<input type="hidden" name="bbsResultPer" value="0" />
							<input type="hidden" name="selftestResultPer" value="0" />
							<input type="hidden" name="otherResultPer" value="0" />
							<input type="hidden" name="faceResultPer" value="0" />
						</c:if>
						<c:if test="${defaultRule eq 'N'}">
						<tr>
							<td>网络辅导分比例:</td>
							<td><input name="bbsResultPer"
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
					<div style="color: green; padding: 8px;">说明：七项比例总和应为100%！</div>
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