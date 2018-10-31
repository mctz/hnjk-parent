<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生批次表单</title>
<script type="text/javascript">
 </script>
</head>
<body>
<body>
	<h2 class="contentTitle">查看招生批次信息</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td style="width: 12%">招生批次名称:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							name="recruitPlanname" size="40" value="${plan.recruitPlanname }"
							class="required" /></td>
						<td style="width: 12%">发布日期:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							name="publishDate" size="40" value="${plan.publishDate }"
							class="required date1" /></td>
					</tr>
					<tr>
						<td style="width: 12%">开始日期:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							id="d1" name="startDate" size="40" value="${plan.startDate }"
							class="required date1" /></td>
						<td style="width: 12%">截止日期:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							id="d2" name="endDate" size="40" value="${plan.endDate }"
							class="required date1" /></td>
					</tr>
					<tr>
						<td style="width: 12%">录取查询开放时间:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							id="d3" name="enrollStartTime" size="40"
							value="${plan.enrollStartTime }" class="required" /></td>
						<td style="width: 12%">录取查询截止时间:</td>
						<td style="width: 38%"><input readonly="readonly" type="text"
							id="d4" name="enrollEndTime" size="40"
							value="${plan.enrollEndTime }" class="required" /></td>
					</tr>
					<tr>
						<td style="width: 12%">年度:</td>
						<td style="width: 38%"><gh:selectModel name="yearInfo"
								bindValue="resourceid" displayValue="yearName" disabled="true"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${plan.yearInfo.resourceid}" classCss="required" /></td>
						<td style="width: 12%">学期:</td>
						<td style="width: 38%"><gh:select name="term"
								dictionaryCode="CodeTerm" value="${plan.term }" disabled="true"
								classCss="required" /></td>
					</tr>
					<tr>
						<td style="width: 12%">是否特批:</td>
						<td style="width: 38%"><input type="radio" name="isSpecial"
							value="Y" classCss="required"
							<c:if test="${plan.isSpecial eq 'Y'}"> checked="checked" </c:if> />是&nbsp;&nbsp;&nbsp;
							<input type="radio" name="isSpecial" value="N"
							classCss="required"
							<c:if test="${plan.isSpecial eq 'N' || plan.isSpecial ==null  || plan.isSpecial eq '' }"> checked="checked" </c:if> />否
						</td>
						<td style="width: 12%">批次状态:</td>
						<td style="width: 38%"><input type="radio" name="isPublished"
							value="Y"
							<c:if test="${plan.isPublished eq 'Y' }" >  checked="checked" </c:if> />发布&nbsp;&nbsp;&nbsp;
							<input type="radio" name="isPublished" value="N"
							<c:if test="${plan.isPublished eq 'N' || plan.isPublished eq '' || plan.isPublished ==null}"> checked="checked" </c:if> />关闭
						</td>
					</tr>
					<tr id="teachingTypeTR">
						<td style="width: 12%">办学模式:</td>
						<td style="width: 38%" colspan="3"><gh:checkBox
								name="teachingType" dictionaryCode="CodeTeachingType"
								value="${plan.teachingType}" /></td>
					</tr>

					<tr>
						<td style="width: 12%">网上报名注意事项:</td>
						<td><textarea readonly="readonly" name="webregMemo" cols="60"
								rows="5">${plan.webregMemo }</textarea></td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>

</html>