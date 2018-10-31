<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
	 	//alert("111111111")
	});
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/framework/recruit/enroll/printExamExcusedList.html?recruitPlan=${recruitPlan}&major=${major}&classic=${classic}&teachingType=${teachingType}&enrollType=${enrollType}&certNum=${certNum}&enrolleeinfo=${enrolleeinfo}" />

</body>
</html>