<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
	});
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/teaching/teachingrecords/print.html?ids=${ids }&brSchoolId=${brSchoolId }&gradeId=${gradeId }&majorId=${majorId }&courseId=${courseId }&teacherName=${teacherName }" />
</body>
</html>