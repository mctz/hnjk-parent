<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>版本设置列表</title>
<script type="text/javascript">
	//新增
	function versionAdd(){
		navTab.openTab('navTab', '${baseUrl}/system/version/edit.html', '版本管理');
	}
	
	//修改
	function versionModify(){
		var url = "${baseUrl}/system/version/edit.html";
		if(isCheckOnlyone('resourceid','#versionBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#versionBody input[@name='resourceid']:checked").val(), '版本管理');
		}			
	}
		
	//删除
	function versionDelete(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/system/version/delete.html","#versionBody");
	}
	
	function versionList(){
		var $_form = $("#validVersionNo");
		if(!$_form.valid()){
			alertMsg.warn("版本号只能为整数！")
			return false;
		}
		return navTabSearch($_form);
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return versionList();"
				action="${baseUrl }/system/version/list.html" method="post"
				id="validVersionNo">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>版本号：</label> <input type="text" name="versionNo"
							style="width: 52%" value="${condition['versionNo'] }"
							class="digits" /></li>
						<li><label>版本号名称：</label> <input type="text"
							name="versionName" style="width: 52%"
							value="${condition['versionName'] }" /></li>
						<li><label>是否发布：</label> <select name="isPublish"
							style="width: 55%;">
								<option value="">请选择</option>
								<option value="0"
									${(condition['isPublish'] eq 'N')?'selected':'' }>未发布</option>
								<option value="1"
									${(condition['isPublish'] eq 'Y')?'selected':'' }>已发布</option>
						</select></li>
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
			<gh:resAuth parentCode="RES_SYS_VERSION" pageType="list"></gh:resAuth>
			<table class="table" layouth="135">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_version"
							onclick="checkboxAll('#check_all_version','resourceid','#versionBody')" /></th>
						<th width="5%">版本号</th>
						<th width="5%">名称</th>
						<th width="10%">上个版本URL</th>
						<th width="10%">本版本URL</th>
						<th width="10%">后端系统URL</th>
						<th width="5%">强制更新</th>
						<th width="5%">是否发布</th>
						<th width="10%">操作人</th>
						<th width="10%">更新时间</th>
						<th width="10%">创建时间</th>
						<th width="17%">版本说明</th>
					</tr>
				</thead>
				<tbody id="versionBody">
					<c:forEach items="${versionList.result}" var="ver" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${ver.resourceid }" autocomplete="off" /></td>
							<td>${ver.versionNo }</td>
							<td>${ver.versionName }</td>
							<td>${ver.preAppUrl }</td>
							<td>${ver.appUrl }</td>
							<td>${ver.backendUrl }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',ver.isForcedUpdate) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',ver.isPublish) }</td>
							<td>${ver.operatorName }</td>
							<td><fmt:formatDate value="${ver.updateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${ver.createDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ver.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${versionList}"
				goPageUrl="${baseUrl }/system/version/list.html" pageType="sys"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>