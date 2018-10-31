<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程参考资料</title>
</head>
<body>
	<h2 class="contentTitle">${title}</h2>
	<table style="width: 98%; margin: 0pt 1%;">
		<c:forEach items="${referenceTypes }" var="t" varStatus="vs">
			<tr>
				<td colspan="2">
					<h5>${ghfn:dictCode2Val('CodeReferenceType',t)}</h5>
				</td>
			</tr>
			<c:forEach items="${courseReferences }" var="courseRef"
				varStatus="vs">
				<c:if test="${courseRef.referenceType eq t }">
					<tr>
						<td width="50%">
							<div style="line-height: 170%; font-size: 13px">
								<a href="${courseRef.url }" target="_blank">${courseRef.referenceName }</a>
							</div>
						</td>
						<td width="50%">
							<div style="line-height: 170%; font-size: 13px">
								${courseRef.url }</div>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</c:forEach>
	</table>
</body>
</html>
