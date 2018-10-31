<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍异动附件查看</title>
</head>
<body>
	<table class="table">
		<ul>
			<c:forEach items="${attachList}" var="attach">
				<li id="${attach.resourceid}"><img
					src="${ baseUrl}/jscript/jquery.uploadify/images/attach.png"
					style="cursor: pointer; height: 10px" /> <a href="#"
					onclick="downloadAttachFile('${attach.resourceid}')">
						${attach.attName}</a></li>
			</c:forEach>
		</ul>
	</table>

</body>
</html>