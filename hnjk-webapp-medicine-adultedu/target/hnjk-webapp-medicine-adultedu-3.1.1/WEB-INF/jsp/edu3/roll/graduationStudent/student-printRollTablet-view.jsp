<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印学籍卡预览</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){

	});
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/schoolRollTable/printOrExport.html?flag=${flag}&studentIds=${studentIds}&unitId=${unitId }&classicId=${classicId }&majorId=${majorId }&gradeId=${gradeId }&classesId=${classesId }&matriculateNoticeNo=${matriculateNoticeNo }&name=${name }&rollCard=${rollCard }&certNum=${certNum }&stuStatus=${stuStatus }&entranceFlag=${entranceFlag }" />
</body>
</html>