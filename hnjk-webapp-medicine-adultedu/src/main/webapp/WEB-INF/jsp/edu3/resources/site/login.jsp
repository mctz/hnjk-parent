<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- <c:if
		test="${not empty course.resourceid }">${course.courseName } - </c:if>
	登录
</title>
<gh:loadCom components="jquery" />
<style type="text/css">
/* 登录 */
#loginArea {
	border: 1px solid #ece6e6;
	vertical-align: middle;
	line-height: 30px;
	color: #91887f;
	margin: 0 auto;
	background: #fcfcfc;
	font-size: 14px;
}

#loginArea H2 {
	FONT-FAMILY: "微软雅黑";
	FONT-WEIGHT: lighter;
	FONT-SIZE: 16px;
	COLOR: #3f3f3f;
	padding: 5px 10px 5px 10px;
	margin: 0;
	font-weight: bold;
	display: block;
	background: #f3f5f7;
	text-align: left;
}

#loginArea label {
	margin: 8px 0 0 20px;
}

#loginArea label input {
	BORDER: #f3f5f7 1px solid;
}

#loginArea .input1 {
	width: 169px;
	height: 26px;
	line-height: 25px;
	padding: 0 5px;
	margin: 8px 0;
}

#loginArea A {
	COLOR: #339;
	TEXT-DECORATION: none
}

#loginArea A:hover {
	TEXT-DECORATION: underline
}

#loginArea .button {
	TEXT-ALIGN: center;
	PADDING-BOTTOM: 0.55em;
	MARGIN: 0px 2px;
	OUTLINE-STYLE: none;
	OUTLINE-COLOR: invert;
	PADDING-LEFT: 2em;
	OUTLINE-WIDTH: medium;
	PADDING-RIGHT: 2em;
	ZOOM: 1;
	DISPLAY: inline-block;
	FONT: 14px/100% Arial, Helvetica, sans-serif;
	VERTICAL-ALIGN: baseline;
	CURSOR: pointer;
	TEXT-DECORATION: none;
	PADDING-TOP: 0.5em;
	text-shadow: 0 1px 1px rgba(0, 0, 0, .3);
	-webkit-border-radius: .5em;
	-moz-border-radius: .5em;
	border-radius: .5em;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .2)
}

#loginArea .button:hover {
	TEXT-DECORATION: none
}

#loginArea .button:active {
	POSITION: relative;
	TOP: 1px
}

#loginArea .white {
	BORDER-BOTTOM: #b7b7b7 1px solid;
	FILTER: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffff',
		endColorstr='#ededed');
	BORDER-LEFT: #b7b7b7 1px solid;
	BACKGROUND: #fff;
	COLOR: #606060;
	BORDER-TOP: #b7b7b7 1px solid;
	BORDER-RIGHT: #b7b7b7 1px solid
}

#loginArea .white:hover {
	FILTER: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffff',
		endColorstr='#dcdcdc');
	BACKGROUND: #ededed
}

#loginArea .white:active {
	FILTER: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed',
		endColorstr='#ffffff');
	COLOR: #999
}
</style>
</head>
<body>
	<div id="loginArea">
		<c:choose>
			<c:when test="${param.error == true }">
				<h2 style="color: red">${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}</h2>
			</c:when>
			<c:otherwise>
				<h2>请登录</h2>
			</c:otherwise>
		</c:choose>
		<form id="loginform" action="${baseUrl}/j_spring_security_check"
			method="post">
			<input type="hidden" name="spring-security-redirect"
				value="/resource/redirct.html?courseId=${course.resourceid }" /> <input
				type="hidden" name="authenticationFailureUrl"
				value="/resource/login.html?courseId=${course.resourceid }&error=true" />
			<input type="hidden" name="defaultTargetUrl"
				value="/resource/redirct.html?courseId=${course.resourceid }" /> <label>用户名：<input
				name="j_username" class="input1" type="text" /></label><br /> <label>密
				码：<input name="j_password" class="input1" type="password" />
			</label>
			<c:if test="${loginNum >3 }">
				<br />
				<label>验证码：<input type='text' size='4' maxlength="4"
					name="j_checkcode" class="sk_login_textstyle" id="j_checkcode"
					onfocus="this.select();"
					onkeydown="if(event.keyCode==0xD){login(); return false;}" /> <img
					src="${baseUrl}/imageCaptcha" id="checkCodeImg"
					style="margin-bottom: -6px; padding-left: 6px"
					onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
					title="看不清，点击换一张"> <a href="#"
					onclick="javascript:document.getElementById('checkCodeImg').src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()">换一张</a></label>
			</c:if>
			<p align="center">
				<input class="button white" type="submit" value="登　录" />
			</p>
		</form>
	</div>
</body>
</html>
