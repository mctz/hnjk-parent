<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业设置列表</title>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">

			<table class="table" layouth="38" id="_recruitMajorSetting">
				<thead>
					<tr>
						<th width="20%">办学模式</th>
						<th width="20%">层次</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody id="recruitMajorSettingBody">
					<c:forEach items="${teachingType}" var="type" varStatus="vs">
						<c:forEach items="${classic }" var="cls">
							<tr>
								<td class="center">${type.dictName}</td>
								<td align="left">${cls.classicName}</td>
								<td align="left"><a
									href="${baseUrl }/edu3/recruit/recruitmajorsetting/recruitmajorsetting-form.html?teachingTypeValue=${type.dictValue}&teachingTypeName=${type.dictName}&classicId=${cls.resourceid}&classicName=${cls.classicName}"
									target="navTab"
									rel="RES_RECRUIT_MANAGE_RECRUITMAJORSETTING_ADD" title="设置专业">设置专业</a></td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">
$(document).ready(function(){
	_w_table_rowspan("#_recruitMajorSetting",1);
});
</script>
</body>
</html>
