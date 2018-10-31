<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生限制管理</title>
<script type="text/javascript">	
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="15%">办学模式</th>
						<th width="20%">层次</th>
						<th width="35%">专业</th>
						<th width="10%">指标数</th>
						<th width="10%">是否启用</th>
						<th width="10%">备注</th>
					</tr>
				</thead>
				<tbody id="majorSettingBody">
					<c:forEach items="${list }" var="setting" varStatus="vs">
						<tr>
							<td>
								${ghfn:dictCode2Val('CodeTeachingType',setting.schoolType ) }</td>
							<td>${setting.classic.classicName }</td>
							<td>${setting.major.majorName }</td>
							<td>${setting.limitNum }</td>
							<td><c:choose>
									<c:when test="${ setting.isOpened eq 'Y'}">启用</c:when>
									<c:otherwise>未启用</c:otherwise>
								</c:choose></td>
							<td>${setting.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
