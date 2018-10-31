<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${mateName }</title>
</head>
<body>
	<div class="page">
		<h2 class="contentTitle">${mateName }</h2>
		<div class="pageContent" style="margin-left: 10px;" layouth="60">
			<c:choose>
				<%-- 如果是视频就播放 --%>
				<c:when
					test="${(mateType eq '2') or (mateType eq '3') or (mateType eq '4')}">
					<embed width='382' height='330' hidden='no' autostart='true'
						src='${mateUrl }'>
				</c:when>
				<c:otherwise>
					<iframe src="${mateUrl }" width="100%" height="100%"></iframe>
				</c:otherwise>
			</c:choose>
		</div>

	</div>
</body>
</html>