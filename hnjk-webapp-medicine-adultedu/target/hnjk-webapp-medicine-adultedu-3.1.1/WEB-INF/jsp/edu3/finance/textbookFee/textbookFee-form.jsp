<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年教材费标准设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑年教材费标准</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/textbookFee/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${textbookFee.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="10%">年度:</td>
							<td>
								<gh:selectModel name="yearId" bindValue="resourceid" displayValue="yearName"  value="${textbookFee.yearInfo.resourceid }" modelClass="com.hnjk.edu.basedata.model.YearInfo" orderBy="firstYear desc" classCss="required" style="width:30%" />
							</td>
						</tr>
						<tr>
							<td>专业:</td>
							<td>
								<gh:majorAutocomplete name="majorId"  defaultValue="${textbookFee.major.resourceid }"   id="textbookFee_form_majorId"  tabindex="-1" displayType="code" orderBy="majorCode" style="width: 30%" classCss="required"></gh:majorAutocomplete>
							</td>
						</tr>
						<tr>
							<td>金额:</td>
							<td >
								<input type="text" name="money" style="width: 10%" value="${textbookFee.money }" class="required number"/>
							</td>
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