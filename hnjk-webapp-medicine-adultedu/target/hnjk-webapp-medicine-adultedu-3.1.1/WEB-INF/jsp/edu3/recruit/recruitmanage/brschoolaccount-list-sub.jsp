<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生账号列表</title>

<script type="text/javascript">
	
 function setAvailable(){
	 pageBarHandle("您确定要将选择的账户激活吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=Y&fwPage=1","#brsaccountBody");
	}

 function setDisable(){
     pageBarHandle("您确定要将选择的账户停用吗？","${baseUrl}/edu3/framework/system/org/user/updateuserstatus.html?statusCode=N&fwPage=1","#brsaccountBody");
	}
	
  function add(){
     	var url = "${baseUrl}/edu3/recruit/recruitmanage/brschoolaccount-form.html";
	 	var unitId = $('#braccoutForm #unitId').val();	 
	 	if(unitId == ""){
	 		alertMsg.warn("请选择左边的某个单位！");
	 		return false;
	 	}
		navTab.openTab('RES_RECRUIT_MANAGE_ACCOUNT_EDIT', url+'?unitId='+unitId, '新增招生账户');
	}
		
 function modify(){
		var url = "${baseUrl}/edu3/recruit/recruitmanage/brschoolaccount-form.html";
		if(isCheckOnlyone('resourceid','#brsaccountBody')){
			navTab.openTab('RES_RECRUIT_MANAGE_ACCOUNT_EDIT', url+'?resourceid='+$("#brsaccountBody input[@name='resourceid']:checked").val(), '修改招生账户');
		}			
	}


</script>
</head>
<body>

	<div class="pageHeader">
		<form id="braccoutForm"
			onsubmit="return localAreaSearch('_brschoolAccoutListContent',this);"
			action="${baseUrl }/edu3/recruit/recruitmanage/brschoolaccount-list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>单位名称：</label>
					<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
							id="brschoolaccount_brSchoolName"
							defaultValue="${condition['unitId']}" /></li>
					<li><label>招生账号：</label><input type="text" name="account"
						value="${condition['account']}" /> <input type="hidden"
						id="unitId" name="unitId" value="${condition['unitId'] }" /></li>
					<!--402881a02bc74aaf012bc7    -->
					<li><label>用户名：</label><input type="text" name="cnName"
						value="${condition['cnName']}" /></li>
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
		<div class="panelBar">
			<ul class="toolBar">
				<gh:resAuth parentCode="RES_RECRUIT_MANAGE_ACCOUNT" pageType="list"></gh:resAuth>
			</ul>
		</div>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all_brsaccount"
						onclick="checkboxAll('#check_all_brsaccount','resourceid','#brsaccountBody')" /></th>
					<th width="25%">教学站</th>
					<th width="20%">招生账号</th>
					<th width="20%">用户名</th>
					<th width="20%">发文号</th>
					<th width="10%">状态</th>
				</tr>
			</thead>
			<tbody id="brsaccountBody">
				<c:forEach items="${userList.result}" var="user" varStatus="vs">
					<tr>
						<td><input type="checkbox" name="resourceid"
							value="${user.resourceid }"
							<c:if test="${user.isDeleted == 1 }"> disabled</c:if>
							autocomplete="off" /></td>
						<td>${user.orgUnit.unitName}</td>
						<td>${user.username}</td>
						<td>${user.cnName}</td>
						<td>${user.documentcode}</td>
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
			goPageUrl="${baseUrl }/edu3/recruit/recruitmanage/brschoolaccount-list.html?isSubPage=y"
			pageType="sys" condition="${condition}" targetType="localArea"
			localArea="_brschoolAccoutListContent" />
	</div>

</body>
</html>
