<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户管理-子页面</title>
<gh:loadCom components="treeView" />
<script type="text/javascript">

	 function addUser(){
	 	var url = "${baseUrl}/edu3/system/org/user/input.html";
	 	var unitId = $('#userSearchForm #unitId').val();
	 	if(unitId == ""){
	 		alertMsg.warn("请选择左边的某个单位！");
	 		return false;
	 	}
	 	navTab.openTab('_blank', url+'?unitId='+unitId, '新增用户');
	 }
		
 	function modifyUser(){
			var url = "${baseUrl}/edu3/system/org/user/input.html";
			if(isCheckOnlyone('resourceid','#userBody')){
		 	navTab.openTab('_blank', url+'?resourceid='+$("#userBody input[@name='resourceid']:checked").val(), '编辑用户');
		}			
	}
	
	//删除
	function deleteUser(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/org/user/delete.html","#userBody");
	}
	
	
	//启用账号
	function setUserEnable(){
		pageBarHandle("您确定要激活这些用户账户吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=Y&fwPage=0","#userBody");
	}
	
	//禁用账户
	function setUserDisenable(){
		pageBarHandle("您确定要禁用这些用户账户吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=N&fwPage=0","#userBody");
	}
	
	
	//切换用户
	function switchSystemUser(){
		if(isCheckOnlyone('resourceid','#userBody')){
			var obj = $("#userBody input[@name='resourceid']:checked");
			var username = obj.attr("title");
			switchSecurityTargetUser(username);
		}
		
	}
	function resetUserPassword(){
		pageBarHandle("您确定要重置这些用户的密码为<font color='red'>默认密码</font>吗？","${baseUrl}/edu3/framework/system/org/user/resetUserPassword.html?fwPage=0","#userBody");
	}
	
</script>

</head>
<body>
	<div class="pageHeader">
		<!-- 使用TREE | RIGHT 模式的页面，校验分会函数使用localAreaSearch('局部显示DIV ID',this) -->
		<form id="userSearchForm"
			onsubmit="return localAreaSearch('_userListContent',this);"
			action="${baseUrl }/edu3/system/org/user/list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>用户名：</label><input type="text" name="username"
						id="username" value="${condition['userName'] }" /></li>
					<li><label>用户中文名：</label> </label><input type="text" name="cnName"
						id="cnName" value="${condition['cnName'] }" /> <input
						type="hidden" name="unitId" id="unitId"
						value="${condition['unitId'] }" /></li>
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
		<gh:resAuth parentCode="RES_ORG_USER" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all_user"
						onclick="checkboxAll('#check_all_user','resourceid','#userBody')" /></th>
					<th width="15%">用户登录名</th>
					<th width="15%">用户中文名</th>
					<th width="15%">所属角色</th>
					<th width="30%">所在单位</th>
					<th width="10%">用户状态</th>
				</tr>
			</thead>
			<tbody id="userBody">
				<c:forEach items="${userList.result}" var="user" varStatus="vs">
					<tr>
						<td><c:if test="${user.resourceid ne cureentUser }">
								<input type="checkbox" name="resourceid"
									value="${user.resourceid }" title="${user.username }"
									autocomplete="off" />
							</c:if></td>
						<td>${user.username }</td>
						<td>${user.cnName }</td>
						<td><c:if test="${not empty user.roles }">
								<c:forEach items="${user.roles }" var="role">
		            		${role.roleName } ,
		            	</c:forEach>
							</c:if></td>
						<td>${user.orgUnit.unitName }</td>
						<td><c:choose>
								<c:when test="${user.isDeleted == 1 }">
									<font color='red'><s>删除</s></font>
								</c:when>
								<c:when test="${!user.enabled }">
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

		<gh:page page="${userList}"
			goPageUrl="${baseUrl }/edu3/system/org/user/list.html?isSubPage=y"
			condition="${condition }" pageType="sys" targetType="localArea"
			localArea="_userListContent" />
	</div>

</body>
</html>