<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">

</style>
</head>
<body>
	<div class="pageContent">
		<label style="line-height: normal;font-size: larger">${fn:replace(fn:replace(fn:replace(message.content,"green","red"),"<br/>","") , ",", "<br/>")}</label>
	</div>
</body>
</html>