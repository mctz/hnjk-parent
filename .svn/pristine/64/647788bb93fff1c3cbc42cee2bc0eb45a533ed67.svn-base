<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>403 - 缺少权限</title>
</head>
<style>
div.error {
	width: 460px;
	border: 1px solid red;
	background-color: yellow;
	text-align: center;
}
-->
</style>
<body>
	<div align="center">
		<h2>您没有权限访问</h2>
		<div class="error">
			${requestScope['SPRING_SECURITY_403_EXCEPTION'].message} <br>
			<div style="text-align: left">
				可能的原因：<br /> 1)您没有被授权;<br /> 2)您的权限已过期;<br /> 3)您的权限没有被分配或被回收;<br />
				请联系系统管理员.
			</div>
		</div>
		<a href="${baseUrl }/j_spring_security_logout">注销</a> <a
			href="javascript:history.go(-1)">返回</a>
	</div>
</body>
</html>