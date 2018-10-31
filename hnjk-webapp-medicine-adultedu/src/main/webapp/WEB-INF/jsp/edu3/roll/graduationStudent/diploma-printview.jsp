<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/roll/graduation/diploma/print.html?resourceid=${resourceid}&branchSchool=${branchSchool }&grade=${grade}&classic=${classic }&teachingtype=${teachingtype }&major=${major }&classes=${classes }&studyNo=${studyNo }&name=${name }&degreeStatus=${degreeStatus}&diplomaType=${diplomaType }" />
</body>
</html>