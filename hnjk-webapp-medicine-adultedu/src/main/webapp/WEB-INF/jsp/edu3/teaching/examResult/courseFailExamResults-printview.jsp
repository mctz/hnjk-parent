<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/result/failexamresults-print.html?examSubId=${examSubId }&plansourceid=${teachingPlanCourseId }&gradeid=${gradeid }&flag=${flag }&operatingType=${operatingType }&classesid=${classesId}&branchSchool=${branchSchool}&type=${type}&form=${form}&majorid=${majorId}&courseid=${courseId}&unitid=${branchSchool}&coursestatusid=${coursestatusid }&isShowUnitCode=${isShowUnitCode }&totalNum=${totalNum }" />
</body>
</html>