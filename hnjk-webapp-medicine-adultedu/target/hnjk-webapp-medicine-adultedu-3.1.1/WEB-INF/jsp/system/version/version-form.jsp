<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>app版本管理</title>
</head>
<body>
	<h2 class="contentTitle">${version.resourceid!=null?'编辑':'添加' }app版本</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="${baseUrl}/system/version/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${version.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>版本号:</td>
							<td><input type="text" name="versionNo" style="width: 50%"
								value="${version.versionNo }" class="required digits" /></td>
							<td>版本号名称:</td>
							<td><input type="text" name="versionName" style="width: 50%"
								value="${version.versionName }" class="required" /></td>
						</tr>
						<tr>
							<td>上个版本URL:</td>
							<td><input type="text" name="preAppUrl" style="width: 50%"
								value="${version.preAppUrl }" class="required" /></td>

							<td>最新版本URL:</td>
							<td><input type="text" name="appUrl" style="width: 50%"
								value="${version.appUrl }" class="required" /></td>
						</tr>
						<tr>
							<td>后端系统URL:</td>
							<td><input type="text" name="backendUrl" style="width: 50%"
								value="${version.backendUrl }" class="required" /></td>

							<td>是否强制更新:</td>
							<td><gh:select dictionaryCode="yesOrNo"
									name="isForcedUpdate"
									value="${empty version.isForcedUpdate?'N':version.isForcedUpdate}"
									style="width:50%;" classCss="required" /></td>

						</tr>
						<tr>
							<td>是否发布:</td>
							<td colspan="3"><gh:select dictionaryCode="yesOrNo"
									name="isPublish"
									value="${empty version.isPublish?'Y':version.isPublish}"
									style="width:20%;" classCss="required" /></td>
						</tr>
						<tr>
							<td>版本说明:</td>
							<td colspan="3"><textarea name="memo" style="width: 50%"
									rows="5">${version.memo }</textarea></td>
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