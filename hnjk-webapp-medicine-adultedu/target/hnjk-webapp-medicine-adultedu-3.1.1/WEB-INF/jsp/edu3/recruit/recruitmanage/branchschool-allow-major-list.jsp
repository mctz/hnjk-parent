<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学站允许招生专业列表</title>
<script type="text/javascript">	
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="20">
				<thead>
					<tr>
						<th width="20%">教学站</th>
						<th width="10%">层次</th>
						<th width="20%">专业</th>
						<th width="10%">指标数</th>
						<th width="10%">是否启用</th>
						<th width="10%">办学模式</th>
						<th width="20%">备注</th>
					</tr>
				</thead>
				<tbody id="allowMajorSettingBody">
					<c:forEach items="${brschMajorSetting }" var="setting"
						varStatus="vs">
						<tr>
							<td>${setting.brSchool.unitName }</td>
							<td>${setting.classic.classicName }</td>
							<td>${setting.major.majorName }</td>
							<td>${setting.limitNum }</td>
							<td><c:choose>
									<c:when test="${ setting.isOpened eq 'Y'}">启用</c:when>
									<c:otherwise>未启用</c:otherwise>
								</c:choose></td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',setting.schoolType)}</td>
							<td>${setting.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
