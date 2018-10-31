<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>免修免考参数设定</title>
<script type="text/javascript">

	//免修、免考认定老师设置
	function applyUserConfig(flag){
		var titleInfo = flag=="noExam"?"免修免考认定老师设置":"免修免考认定老师设置";
		var url = "${baseUrl}/edu3/teaching/noexamapply/config-edit.html?flag="+flag+"&operatingFlag=userConfig";
		navTab.openTab('RES_TEACHING_RESULT_NOEXAM_CONFIG_SUB', url, titleInfo);
	}
	
	//设置免修、免考申请时间
	function applyTimeConfig(flag){
		var titleInfo = flag=="noExam"?"设置免修免考申请时间":"设置免修免考申请时间";
		var url = "${baseUrl}/edu3/teaching/noexamapply/config-edit.html?flag="+flag+"&operatingFlag=timeConfig";
		navTab.openTab('RES_TEACHING_RESULT_NOEXAM_CONFIG_SUB', url, titleInfo);
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<c:choose>
				<c:when test="${flag eq 'noExam' }">
					<gh:resAuth parentCode="RES_TEACHING_RESULT_NOEXAM_CONFIG"
						pageType="list"></gh:resAuth>
				</c:when>
				<c:when test="${flag eq 'noStudy' }">
					<gh:resAuth parentCode="RES_TEACHING_ESTAB_NOSUDYAPPLY_CONFIG"
						pageType="list"></gh:resAuth>
				</c:when>
			</c:choose>

			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="20%">参数名</th>
						<th width="80%">参数值</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${dictName1}</td>
						<td>${dictValue1}</td>
					</tr>
					<tr>
						<td>${dictName2}</td>
						<td>${dictValue2 }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>