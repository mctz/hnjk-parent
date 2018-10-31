<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>重新登录</title>
</head>
<body>
	<h2 class="contentTitle">您的账号登录超时，被退出，请重新登录</h2>
	<div class="page">
		<form method="post" action="${baseUrl}/j_spring_security_check"
			class="pageForm" id="userForm"
			onsubmit="return validateCallback(this);">
			<input type="hidden" name="byAjax" value="ajax" />
			<table class="form">
				<tr>
				<tr>
					<td style="width: 24%">用户名：</td>
					<td style="width: 66%"><input type="text" name="j_username"
						size="40" id="j_username" value="${username }" class="required" /></td>
					</td>
				</tr>
				<tr>
					<td style="width: 24%">密码:</td>
					<td style="width: 66%"><input type="password"
						name="j_password" size="40" id="j_password" class="required" />
					</td>
				</tr>
				<tr>
					<td>验证码：</td>
					<td><input type='text' size='4' maxlength="4"
						name="j_checkcode" class="required" id="j_checkcode"
						onfocus="this.select();"
						onkeydown="if(event.keyCode==0xD){login(); return false;}" /> <img
						src="${baseUrl}/imageCaptcha" id="checkCodeImg"
						style="margin-bottom: -6px; padding-left: 6px"
						onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
						title="看不清，点击换一张"> <a href="#"
						onclick="javascript:document.getElementById('checkCodeImg').src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()">换一张</a></td>
				</tr>
				<tr>
					<td style="width: 24%">入口:</td>
					<td style="width: 66%"><input type="radio" name="fromNet"
						value="pub" checked="checked" />公众网 &nbsp;&nbsp;<input
						type="radio" name="fromNet" value="edu" />教育网</td>
				</tr>
			</table>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit" style="cursor: pointer;">登录</button>
							</div>
						</div></li>
					<li><div class="button">
							<div class="buttonContent">
								<button type="button" class="close"
									onclick="navTab.closeCurrentTab();" style="cursor: pointer;">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>
