<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/register/studentinfo/exameeinfo/print.html?studentId=${studentId }&unitId=${unitId}&classicId=${classicId}&majorId=${majorId}&stuGrade=${stuGrade}&classesId=${classesId}&matriculateNoticeNo=${matriculateNoticeNo}&name=${name}&rollCard=${rollCard}&certNum=${certNum}&stuStatus=${stuStatus}&entranceFlag=${entranceFlag}&from=${from}" />
</body>
</html>