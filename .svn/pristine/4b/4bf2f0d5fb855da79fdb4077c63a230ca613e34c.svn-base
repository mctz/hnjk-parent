<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩审核操作记录</title>
<script type="text/javascript">

</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table">
				<thead>
					<tr>
						<th>操作人</th>
						<th>操作时间</th>
						<th>操作内容</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${result}" var="record" varStatus="vs">
						<tr>
							<td>${record.opeman }</td>
							<td>${record.opedate }</td>
							<td>${record.opetype }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>