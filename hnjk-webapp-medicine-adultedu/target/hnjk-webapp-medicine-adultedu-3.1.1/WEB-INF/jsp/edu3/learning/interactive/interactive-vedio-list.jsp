<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>互动视频</title>
</head>
<body>
	<c:choose>
		<c:when test="${success eq 'Y' }">
			<iframe id='interactive_vedio_list' width='100%' height='650px'
				frameborder='0' scrolling="no" src='${interactiveVideoUrl }'></iframe>
		</c:when>
		<c:otherwise>
			${errorMsg }
		</c:otherwise>
	</c:choose>
</body>
</html>
