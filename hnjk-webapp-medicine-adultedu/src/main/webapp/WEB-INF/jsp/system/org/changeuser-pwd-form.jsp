<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置</title>
</head>
<body>
	<div class="page">

		<form method="post"
			action="${baseUrl }/edu3/framework/user/savesetting.html?act=changepwd"
			class="pageForm" id="userForm"
			onsubmit="return validateCallback(this);">
			<input type="hidden" name="resourceid" id="resourceid"
				value="${userid }">
			<table class="form">
				<tr>
					<td style="width: 24%">旧密码:</td>
					<td style="width: 66%"><input type="password"
						name="oldPassword" id="oldPassword" class="required" size="40"
						value="" /></td>
				</tr>
				<tr>
					<td style="width: 24%">新密码:</td>
					<td style="width: 66%"><input type="password"
						name="newPassword" id="newPassword" class="required" size="40"
						value="" /></td>
				</tr>
				<tr>
					<td style="width: 24%">确认新密码:</td>
					<td style="width: 66%"><input type="password"
						name="newPassword1" size="40" class="required" value=""
						equalTo="#newPassword" /></td>
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
								<button type="button" class="close" onclick="closeDialog();">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		function closeDialog(){
			$.pdialog.closeCurrent(); 
		}
	</script>

</body>
</html>