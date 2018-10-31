<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/examresult/correct/print.html?resourceids=${resourceids }&brSchoolId=${brSchoolId }&gradeId=${gradeId }&classicId=${classicId }&teachingType=${teachingType }&majorId=${majorId }&teachType=${teachType }&examSubId=${examSubId }&classId=${classId }&courseId=${courseId }&studyNo=${studyNo }&studentName=${studentName }" />
</body>
</html>