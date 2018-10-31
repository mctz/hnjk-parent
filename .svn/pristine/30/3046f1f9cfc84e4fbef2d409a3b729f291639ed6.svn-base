<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
<script type="text/javascript">
 	$(document).ready(function(){
		var errorMsg = "${msg}";
		if(null!=errorMsg && ""!=errorMsg){
			$("#examResultsReviewPrintViewBodyForFaceCourse").html("");
			alertMsg.warn(errorMsg);
			$.pdialog.closeCurrent();
		}
	});
  </script>
</head>

<body id="examResultsReviewPrintViewBodyForFaceCourse">
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/result/examresultsreviewForFaceCourse-print.html?examSubId=${condition['examSubId'] }&examInfoId=${condition['examInfoId'] }&branchSchool=${condition['branchSchool'] }&teachType=${condition['teachType'] }&gradeid=${condition['gradeid'] }&major=${condition['major'] }&classic=${condition['classic'] }&courseId=${condition['courseId'] }&printType=${condition['printType'] }&classesid=${condition['classesid'] }" />
</body>
</html>