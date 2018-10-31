<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/plancourseArrange/studentAttendance-print.html?resIds=${resIds }&classesIds=${classesIds }&unitIds=${unitIds }&courseIds=${courseIds }&pcIds=${pcIds }&terms=${terms}" />
</body>
</html>