<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
	 	//alert("111111111")
	});
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/result/personalReportCard-print.html?flag=${flag }&branchSchoolId=${branchSchoolId}&gradeId=${gradeId}&classicId=${classicId}&teachingType=${teachingType}&majorId=${majorId}&studentIds=${studentId}&printDate=${printDate }&studyTime=${studyTime }&printExam=true&classes=${classesid }&courseid=${courseid }&terms=${terms }&degreeUnitExam=${degreeUnitExam }&passExam=${passExam}&printPage=${printPage }" />
</body>
</html>