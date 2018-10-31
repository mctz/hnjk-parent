<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置</title>
</head>
<body>
	<div class="page">
		<form method="post"
			action="${baseUrl }/edu3/framework/user/savesetting.html?act=unitSetting"
			class="pageForm" id="orgUnitForm"
			onsubmit="return validateCallback(this);">
			<input type="hidden" name="resourceid" id="resourceid"
				value="${userid }">
			<table class="form">
				<tr>
					<td style="width: 24%">负责人:</td>
					<td style="width: 66%"><input type="text" name="principal"
						class="required" size="40" value="${unit.principal }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">联系人:</td>
					<td style="width: 66%"><input type="text" name="linkman"
						class="required" size="40" value="${unit.linkman }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">联系电话:</td>
					<td style="width: 66%"><input type="text" name="contectCall"
						class="required" size="40" value="${unit.contectCall }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">所属城市:</td>
					<td style="width: 66%"><input type="text" name="localCity"
						size="40" value="${unit.localCity }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">邮编:</td>
					<td style="width: 66%"><input type="text" name="zipcode"
						size="40" value="${unit.zipcode }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">电子邮件:</td>
					<td style="width: 66%"><input type="text" name="email"
						size="40" value="${unit.email }" /></td>
				</tr>
				<tr>
					<td style="width: 24%">地址:</td>
					<td style="width: 66%"><input type="text" name="address"
						class="required" size="40" value="${unit.address }" /></td>
				</tr>
			</table>
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
</body>
</html>