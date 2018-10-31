<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>敏感词管理</title>
</head>
<body>
	<h2 class="contentTitle">${sensitiveWord.resourceid!=null?'编辑':'添加' }敏感词</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/sensitiveWord/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${sensitiveWord.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>敏感词:</td>
							<td colspan="3"><textarea name="word" style="width: 50%"
									rows="3">${sensitiveWord.word }</textarea></td>
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