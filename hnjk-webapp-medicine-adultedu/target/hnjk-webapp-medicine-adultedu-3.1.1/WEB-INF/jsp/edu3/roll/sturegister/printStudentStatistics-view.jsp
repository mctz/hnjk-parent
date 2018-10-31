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
		reportUrl="${baseUrl}/edu3/roll/studentinfo/studentStatistics-view.html" />
</body>
</html>