<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>访问出错</title>
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
		<h3>访问出错</h3>
		<div class="error">
			<div style="text-align: left">${errormsg}</div>
		</div>
		<a href="${baseUrl }/edu3/framework/index.html">返回首页</a>
	</div>
</body>
</html>