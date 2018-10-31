<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色管理</title>
</head>
<body>
	<script type="text/javascript">
	//新增
	function addRole(){
		navTab.openTab('_blank', '${baseUrl}/edu3/system/authoriza/role/input.html', '新增角色');
	}
	
	//修改
	function modifyRole(){
		var url = "${baseUrl}/edu3/system/authoriza/role/input.html";
		if(isCheckOnlyone('resourceid','#roleBody')){
			navTab.openTab('RES_AUTHORIZA_ROLE_EDIT', url+'?resourceid='+$("#roleBody input[@name='resourceid']:checked").val(), '编辑角色');
		}			
	}
		
	//删除
	function deleteRole(){	
		pageBarHandle("注意：删除角色将删除角色下的所有用户！<br/>您确定要删除这些记录吗？","${baseUrl}/edu3/system/authoriza/role/delete.html","#roleBody");
	}
</script>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/authoriza/role/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>角色编码：</label><input type="text" name="roleCode"
							value="${condition['roleCode']}" /></li>
						<li><label>角色名称：</label><input type="text" name="roleName"
							value="${condition['roleName']}" /></li>
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
			<gh:resAuth parentCode="RES_AUTHORIZA_ROLE" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_role"
							onclick="checkboxAll('#check_all_role','resourceid','#roleBody')" /></th>
						<th width="20%">角色编码</th>
						<th width="20%">角色名称</th>
						<th width="35%">角色描述</th>
					</tr>
				</thead>
				<tbody id="roleBody">
					<c:forEach items="${roleList.result}" var="role" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${role.resourceid }" autocomplete="off" /></td>
							<td>${role.roleCode }</td>
							<td>${role.roleName }</td>
							<td>${role.roleDescript }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${roleList}"
				goPageUrl="${baseUrl }/edu3/system/authoriza/role/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>