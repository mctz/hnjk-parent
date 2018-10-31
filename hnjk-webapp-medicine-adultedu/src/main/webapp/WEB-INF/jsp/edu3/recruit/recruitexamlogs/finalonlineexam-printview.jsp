<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/exam/examPrint/finalonlineexam-print.html?examSubId=${examSubId }&courseId=${courseId }" />
</body>
</html>