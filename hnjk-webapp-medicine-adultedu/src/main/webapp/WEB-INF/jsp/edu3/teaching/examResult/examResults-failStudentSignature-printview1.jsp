<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<%--   <gh:printView reportUrl="${baseUrl}/edu3/teaching/result/printFailExamSignature1.html?yearId=${condition['yearId'] }&branchSchool=${condition['branchSchool'] }&examType=${condition['examType'] }&classic=${condition['classic'] }&term=${condition['term'] }&gradeId=${condition['gradeId'] }&classesId=${condition['classesId'] }&major=${condition['major'] }&branchschoolid=${condition['branchschoolid'] }&examSubId=${condition['examSubId'] }&courseId=${condition['courseId'] }&failResultStatus=${condition['failResultStatus'] }&type=${condition['type'] }&teachplanids=${condition['teachplanids'] }&courseids=${condition['courseids'] }&gradeids=${condition['gradeids'] }&majorids=${condition['majorids'] }&unitids=${condition['unitids'] }&classesids=${condition['classesids'] }"/> --%>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/result/printFailExamSignature1.html?yearId=${yearId }&branchSchool=${branchSchool }&examType=${examType }&classic=${classic }&term=${term }&gradeId=${gradeId }&classesId=${classesId }&major=${major }&branchschoolid=${branchschoolid }&examSubId=${examSubId }&courseId=${courseId }&failResultStatus=${failResultStatus }&type=${type }&courseids=${courseids }&classesids=${classesids}&plansourceids=${plansourceids }" />
</body>
</html>