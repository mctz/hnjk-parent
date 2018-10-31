<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑年级</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/grade/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${grade.resourceid }" />
				<input type="hidden" name="isBookingExame"
					value="${grade.isBookingExame }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">年级名称:</td>
							<td width="35%"><input type="text" name="gradeName"
								style="width: 50%" value="${grade.gradeName }" class="required" /></td>
							<td width="15%">年度名称:</td>
							<td width="35%"><gh:selectModel name="yearInfoId"
									bindValue="resourceid" displayValue="yearName"
									condition="firstYear<=${year+3},firstYear>=${startYear+3}"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${grade.yearInfo.resourceid}" orderBy="firstYear desc"
									style="width:52%" classCss="required" /></td>
						</tr>
						<tr>
							<td>学期:</td>
							<td><gh:select name="term" value="${grade.term}"
									dictionaryCode="CodeTerm" style="width:52%" classCss="required" /></td>
							<td>是否当前默认年级:</td>
							<td><input type="radio" name="isDefaultGrade" value="N"
								<c:if test="${grade.isDefaultGrade eq 'N' or empty grade.isDefaultGrade }">checked="checked"</c:if> />否
								<input type="radio" name="isDefaultGrade" value="Y"
								<c:if test="${grade.isDefaultGrade eq 'Y' }">checked="checked"</c:if> />是
							</td>
						</tr>
						<tr>
							<td>默认入学日期：</td>
							<td><input type="text" id="indate" name="indate"
								class="required"
								value='<fmt:formatDate value="${grade.indate  }" pattern="yyyy-MM-dd"/>'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
							<td>默认毕业日期：</td>
							<td><input type="text" id="grade_graduateDate"
								name="graduateDate"
								value='<fmt:formatDate value="${grade.graduateDate  }" pattern="yyyy-MM-dd"/>'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><input type="text" name="memo"
								style="width: 50%" value="${grade.memo }" /></td>
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