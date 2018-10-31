<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
<script type="text/javascript">
 	$(document).ready(function(){
		var errorMsg = "${msg}";

		if(null!=errorMsg && ""!=errorMsg){
			$("#examResultsReviewPrintViewBody").html("");
			alertMsg.warn(errorMsg);
			$.pdialog.closeCurrent();
		}
	});
  </script>
</head>

<body id="examResultsReviewPrintViewBody">
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/result/examresultsreview-print.html?examResultsStatus=${examResultsStatus }&examSubId=${examSubId }&examInfoId=${examInfoId }&printType=${printType }&branchSchool=${branchSchool }" />
</body>
</html>