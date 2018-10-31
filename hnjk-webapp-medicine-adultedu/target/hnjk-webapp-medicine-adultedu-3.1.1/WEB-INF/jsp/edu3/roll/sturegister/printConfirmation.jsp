<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/register/studentinfo/print-confirmation.html?stus=${stus}" />

</body>
</html>