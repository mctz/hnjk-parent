<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程模拟试题</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layoutH="50">
				<thead>
					<tr>
						<th>模拟试题列表</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty courseMockTests }">
						<tr>
							<td>暂时还没有模拟试题......</td>
						</tr>
					</c:if>
					<c:forEach items="${courseMockTests }" var="courseMockTest"
						varStatus="vs">
						<tr>
							<td><a href="${courseMockTest.mateUrl }" target="_blank">${courseMockTest.mocktestName }</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
