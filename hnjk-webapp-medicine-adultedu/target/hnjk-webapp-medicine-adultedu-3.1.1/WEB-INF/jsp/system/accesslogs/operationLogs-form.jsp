<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统操作日志管理</title>
</head>
<body>
	<table style="width: 100%;height: 100%;">
		<tr>
			<td><textarea style="width: 95%;height: 95%">${logs.operationContent }</textarea></td>
		</tr>
	</table>
</body>
</html>