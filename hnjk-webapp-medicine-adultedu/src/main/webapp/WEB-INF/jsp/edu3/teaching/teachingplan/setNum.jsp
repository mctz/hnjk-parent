<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingplan/saveElectiveNum.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="teachingPlanIds"
					value="${teachingPlanIds }" />
				<table class="form" layoutH="50">
					<tr>
						<td width="20%">选修课修读门数：</td>
						<td width="35%"><input type="text" name="num"
							style="width: 60%" value="" class="digits" /></td>
					</tr>
				</table>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>

</html>