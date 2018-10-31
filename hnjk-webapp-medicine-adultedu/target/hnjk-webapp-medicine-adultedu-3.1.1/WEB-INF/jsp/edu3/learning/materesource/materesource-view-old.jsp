<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程素材资源预览</title>
</head>
<body>
	<div class="page">
		<h2 class="contentTitle">材料类型为${ghfn:dictCode2Val('CodeMateType',mateType)}</h2>
		<div class="pageContent" style="margin-left: 10px;" layouth="60">
			<c:choose>
				<%-- 如果是视频就播放 --%>
				<c:when
					test="${(mateType eq '2') or (mateType eq '3') or (mateType eq '4')}">
					<embed width='382' height='330' hidden='no' autostart='true'
						src='${mateUrl }'>
				</c:when>
				<c:when
					test="${mateType eq '6' or mateType eq '7' or mateType eq '8'}">
					<iframe src="${mateUrl }" width="100%" height="100%"></iframe>
				</c:when>
				<%-- 如果是其他就下载 --%>
				<c:otherwise>
					<a href="${mateUrl }" class="button"><span>测试下载</span></a>
				</c:otherwise>
			</c:choose>
		</div>

	</div>
</body>
</html>