<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板管理</title>
<script type="text/javascript">

</script>
<style>
#nostyle td {
	height: 8px
}
</style>
</head>
<body>
	<h2 class="contentTitle">编辑模板</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/template/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name=filename value="${filename }" />
						<tr>
							<td style="width: 12%">外部链接:</td>
							<td style="width: 38%"><input type="text" name="channelHref"
								size="50" value="${channel.channelHref}" class="required" /></td>
							<td style="width: 12%">排序:</td>
							<td style="width: 38%"><input type="text" name="showOrder"
								value="${channel.showOrder }" size="6" /> <span
								style="padding: 4px; color: red">数字越小越靠前</span></td>
						</tr>

					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<!-- 隐藏域 -->
									<input type="hidden" name="fillinMan"
										value="${channel.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${channel.fillinManId }" /> <input
										type="hidden" name="fillinDate"
										value=" <fmt:formatDate value="${channel.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
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