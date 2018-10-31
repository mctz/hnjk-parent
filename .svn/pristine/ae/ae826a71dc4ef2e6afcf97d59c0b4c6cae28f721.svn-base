<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>友情链接管理</title>
<script type="text/javascript">	
	//新增连接
	function addLink(){
		navTab.openTab('_blank', '${baseUrl}/edu3/portal/manage/link/input.html', '新增友情链接');
	}
	
	//修改链接
	function modifyLink(){
		var url = "${baseUrl}/edu3/portal/manage/link/input.html";
		if(isCheckOnlyone('resourceid','#linkBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("input[@name='resourceid']:checked").val(), '编辑友情链接');
		}			
	}
	
	//删除
	function deleteLink(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/portal/manage/link/remove.html","#linkBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/manage/link/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>链接名称：</label> <input type="text" name="linkName"
							value="${condition['linkName'] }" /></li>
						<li><label>链接类型：</label> <gh:select name="linkPosition"
								value="${condition['linkPosition'] }"
								dictionaryCode="linkPosition" classCss="required"
								style="width:33%" /></li>
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
			<gh:resAuth parentCode="RES_PORTAL_LINK" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all"
							onclick="checkboxAll('#check_all','resourceid','#linkBody')" /></th>
						<th width="30%">链接名称</th>
						<th width="25%">链接地址</th>
						<th width="15%">链接说明</th>
						<th width="15%">链接类型</th>
						<th width="10%">链接状态</th>
					</tr>
				</thead>
				<tbody id="linkBody">
					<c:forEach items="${linkList.result}" var="link" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${link.resourceid }" autocomplete="off" /></td>
							<td>${link.linkName }</td>
							<td><a href="${link.linkHref }" target="${link.openType }">${link.linkHref }</a></td>
							<td>${link.linkDescript }</td>
							<td>${ghfn:dictCode2Val('linkPosition',link.linkPosition) }</td>
							<td>${link.status==0 ? "正常":"停用"}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${linkList}"
				goPageUrl="${baseUrl }/edu3/portal/manage/link/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>

</body>
</html>