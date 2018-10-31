<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教材信息</title>
</head>
<body>
	<h2 class="contentTitle" style="border-bottom: medium;">${edumanager.cnName}</h2>
	<div class="page">
		<div class="pageFormContent" layoutH="25">
			<table class="form">
				<tbody>
					<tr>
						<th width="15%">课程名称</th>
						<th width="20%">教材名称</th>
						<th width="20%">书号</th>
						<th width="15%">出版社</th>
						<th width="15%">主编</th>
						<th width="15%">单价</th>
					</tr>
					<c:forEach items="${textBooks}" var="tb" varStatus="vs">
					<tr>
						<td >${tb.course.courseName }</td>
						<td >${tb.bookName }</td>
						<td >${tb.bookSerial }</td>
						<td >${tb.press }</td>
						<td >${tb.editor}</td>
						<td >${tb.price }</td>
					</tr>
					</c:forEach>
					

				</tbody>
			</table>
		</div>
	</div>

</body>
</html>