<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学考试成绩录入人设定</title>
<script type="text/javascript">

	//设定录入人
	function entranceExamScoreInputEdit(){
		var url = "${baseUrl}/edu3/system/configuration/entranceExamScoreInput-edit.html";
		if(isCheckOnlyone('resourceid','#entranceExamScoreInpuConfigurationBody')){
			navTab.openTab('RES_MATRICALATE_EXAMSCORE_INPUT_CONFIG_EDIT', url+'?resourceid='+$("#entranceExamScoreInpuConfigurationBody input[@name='resourceid']:checked").val(), '设定录入人');
		}			
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_MATRICALATE_EXAMSCORE_MODIFYLIST"
				pageType="subList"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_entranceexamscore_dict"
							onclick="checkboxAll('#check_all_entranceexamscore_dict','resourceid','#entranceExamScoreInpuConfigurationBody')" /></th>
						<th width="20%">名称</th>
						<th width="25%">录入人</th>
						<th width="25%">备注</th>
					</tr>
				</thead>
				<tbody id="entranceExamScoreInpuConfigurationBody">
					<c:forEach items="${configList.result}" var="config" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${config.resourceid }" autocomplete="off" /></td>
							<td>${config.paramName }</td>
							<td>${ghfn:ids2Names('user',config.paramValue) }</td>
							<td>${config.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>