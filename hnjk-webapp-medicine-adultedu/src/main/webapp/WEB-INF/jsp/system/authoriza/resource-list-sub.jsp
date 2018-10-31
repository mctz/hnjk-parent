<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色权限管理</title>

<script type="text/javascript">

	
	function addRes(){
	 	var url = "${baseUrl}/edu3/system/authoriza/resource/input.html";
	 	var resId = $('#resForm #resId').val();
	 	if(resId == ""){
	 		alertMsg.warn("请选择左边的某个资源！");
	 		return false;
	 	}
	 	navTab.openTab('_blank', url+'?resId='+resId, '新增资源');
	 }
		
 	function modifyRes(){
		var url = "${baseUrl}/edu3/system/authoriza/resource/input.html";
		if(isCheckOnlyone('resourceid','#resBody')){
		 	navTab.openTab('_blank', url+'?resourceid='+$("#resBody input[@name='resourceid']:checked").val(), '编辑资源');
		}			
	}
	
	//删除
	function deleteRes(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/authoriza/resource/delete.html","#resBody");
	}
	
	//重载缓存
	function recacheResource(){
		alertMsg.confirm("执行重载缓存会消耗服务器性能，确定要执行吗?", {
			okCall: function(){//执行                
				$.post("${baseUrl}/edu3/system/authoriza/resource/recache.html","", navTabAjaxDone, "json");
			}
	});			
	}

</script>
</head>
<body>

	<div class="pageHeader">
		<form onsubmit="return localAreaSearch('_resourceListContent',this);"
			action="${baseUrl }/edu3/system/authoriza/resource/list.html?isSubPage=y"
			method="post" id="resForm">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>资源编码：</label><input type="text" name="resourceCode"
						value="${condition['resourceCode']}" /></li>
					<li><label>资源名称：</label><input type="text" name="resourceName"
						value="${condition['resourceName']}" /> <input type="hidden"
						name="resId" id="resId" value="${condition['resId'] }" /></li>
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
		<gh:resAuth parentCode="RES_AUTHORIZA_RESOURCE" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all_res"
						onclick="checkboxAll('#check_all_res','resourceid','#resBody')" /></th>
					<th width="20%">资源编码</th>
					<th width="10%">资源名称</th>
					<th width="10%">第一人称资源名</th>
					<th width="10%">资源类型</th>
					<th width="35%">资源授权路径</th>
					<th width="5%">排序号</th>
				</tr>
			</thead>
			<tbody id="resBody">
				<c:forEach items="${resList.result}" var="res" varStatus="vs">
					<tr>
						<td><c:if test="${res.resourceLevel != 0 }">
								<input type="checkbox" name="resourceid"
									value="${res.resourceid }" autocomplete="off" />
							</c:if></td>
						<td>${res.resourceCode }</td>
						<td>${res.resourceName }</td>
						<td>${res.firstPersonName }</td>
						<td>${ghfn:dictCode2Val('resourceType',res.resourceType) }</td>
						<td>${res.resourcePath }</td>
						<td>${res.showOrder }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<gh:page page="${resList}"
			goPageUrl="${baseUrl }/edu3/system/authoriza/resource/list.html?isSubPage=y"
			condition="${condition }" pageType="sys" targetType="localArea"
			localArea="_resourceListContent" />
	</div>

</body>
</html>