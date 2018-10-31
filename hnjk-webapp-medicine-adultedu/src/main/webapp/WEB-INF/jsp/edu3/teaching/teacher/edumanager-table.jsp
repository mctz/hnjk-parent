<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师管理</title>
<script type="text/javascript">
	
</script>
</head>
<body>

	<table class="table"  border="1">
		<thead>
			<tr>
				<th width="11%">教师编号</th>
				<th width="11%">姓名</th>
				<th width="11%">登录帐号</th>
				<th width="20%">所属单位</th>
				<th width="6%">性别</th>
				<th width="10%">职称</th>
				<th width="11%">手机</th>
				<th width="18">是否班主任</th>
				<th width="8%">账号状态</th>
			</tr>
		</thead>
		<tbody id="tedumanagerBody">
			<c:forEach items="${edumanagerListPage.result}" var="edumanager"
				varStatus="vs">
				<tr>
					<td>${edumanager.teacherCode }</td>
					<td><a
						href="${baseUrl }/edu3/framework/edumanager/view.html?userId=${edumanager.resourceid }"
						target="dialog" title="${edumanager.cnName }" width="700"
						height="500" rel="edumanager_view">${edumanager.cnName }</a></td>
					<td>${edumanager.username }</td>

					<td>${edumanager.orgUnit.unitName }</td>

					<td>${ghfn:dictCode2Val('CodeSex',edumanager.gender ) }</td>
					<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',edumanager.titleOfTechnical ) }</td>
					<td>${edumanager.mobile }</td>
					<td><c:choose>
							<c:when test="${edumanager.isMaster == 'Y' }">是</c:when>
							<c:when test="${edumanager.isMaster == 'N' }">否</c:when>
						</c:choose></td>
					<td><c:choose>
							<c:when test="${edumanager.isDeleted == 1 }">
								<font color='red'><s>删除</s></font>
							</c:when>
							<c:when test="${!edumanager.enabled }">
								<font color='red'>禁用</font>
							</c:when>
							<c:otherwise>
								<font color='green'>正常</font>
							</c:otherwise>
						</c:choose></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>


</body>
