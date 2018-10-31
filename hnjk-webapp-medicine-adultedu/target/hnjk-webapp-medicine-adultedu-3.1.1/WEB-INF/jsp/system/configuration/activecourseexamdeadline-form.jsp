<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>当前学期随堂练习截止时间设置</title>
</head>
<body>
	<h2 class="contentTitle">当前学期随堂练习截止时间设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/configuration/activecourseexam/deadline/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${sysConfiguration.resourceid }" /> <input type="hidden"
					name="paramCode" value="${sysConfiguration.paramCode }" /> <input
					type="hidden" name="paramName"
					value="${sysConfiguration.paramName }" /> <input type="hidden"
					name="memo" value="${sysConfiguration.memo }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>截止时间:</td>
							<td><input type='text' name='paramValue' style="width: 40%"
								value="${sysConfiguration.paramValue }" class='required'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
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