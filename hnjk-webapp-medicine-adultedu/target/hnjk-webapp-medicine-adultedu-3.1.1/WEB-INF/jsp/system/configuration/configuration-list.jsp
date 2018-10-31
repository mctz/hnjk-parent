<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>全局参数配置管理</title>
<script type="text/javascript">
	//新增
	function addConfiguration(){
		navTab.openTab('navTab', '${baseUrl}/edu3/system/configuration/input.html', '新增全局参数');
	}
	
	//修改
	function modifyConfiguration(){
		var url = "${baseUrl}/edu3/system/configuration/input.html";
		if(isCheckOnlyone('resourceid','#configurationBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#configurationBody input[@name='resourceid']:checked").val(), '编辑字典');
		}			
	}
		
	//删除
	function deleteConfiguration(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/configuration/delete.html","#configurationBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/configuration/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 300px;"><label>参数名称：</label><input type="text" name="paramName"
							value="${condition['paramName']}" style="width: 180px;" /></li>
						<li style="width: 300px;"><label>参数编码：</label><input type="text" name="paramCode"
							value="${condition['paramCode']}" style="width: 180px;"/></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SYS_CONFIGURATION" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_dict"
							onclick="checkboxAll('#check_all_dict','resourceid','#configurationBody')" /></th>
						<th width="20%">参数名称</th>
						<th width="25%">参数编码</th>
						<th width="25%">参数值</th>
						<th width="25%">备注</th>
					</tr>
				</thead>
				<tbody id="configurationBody">
					<c:forEach items="${configList.result}" var="config" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${config.resourceid }" autocomplete="off" /></td>
							<td>${config.paramName }</td>
							<td>${config.paramCode }</td>
							<td>${config.paramValue }</td>
							<td>${config.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${configList}"
				goPageUrl="${baseUrl }/edu3/system/configuration/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
</html>