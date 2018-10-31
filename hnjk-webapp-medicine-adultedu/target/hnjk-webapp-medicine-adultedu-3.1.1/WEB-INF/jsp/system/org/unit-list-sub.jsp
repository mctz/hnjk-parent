<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>

<script type="text/javascript">

	
	function addUnit(){
	 	var url = "${baseUrl}/edu3/system/orgunit/input.html";
	 	var unitId = $('#unitForm #unitId').val();
	 	if(unitId == ""){
	 		alertMsg.warn("请选择左边的某个单位！");
	 		return false;
	 	}
	 	navTab.openTab('_blank', url+'?unitId='+unitId, '新增单位');
 	}
 	
 	function modifyUnit(){
			var url = "${baseUrl}/edu3/system/orgunit/input.html";
			if(isCheckOnlyone('resourceid','#unitBody')){
		 	navTab.openTab('_blank', url+'?resourceid='+$("#unitBody input[@name='resourceid']:checked").val(), '编辑单位');
		}			
	}
	
	//删除
	function deleteUnit(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/orgunit/delete.html","#unitBody");
	}
	
	
</script>
</head>
<body>

	<div class="pageHeader">
		<form id="unitForm"
			onsubmit="return localAreaSearch('_orgUnitListContent',this);"
			action="${baseUrl }/edu3/system/org/unit/list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li style="width: 480px;"><label>单位名称：</label><input type="text" name="unitName"
						id="unitName" value="${condition['unitName'] }" style="width: 360px;"/>
						<input type="hidden" id="unitId" name="unitId" value="${condition['unitId'] }"></li>
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
		<gh:resAuth parentCode="RES_ORG_UNIT" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all_unit"
						onclick="checkboxAll('#check_all_unit','resourceid','#unitBody')" /></th>
					<th width="20%">组织单位名称</th>
					<th width="20%">组织单位简称</th>
					<th width="20%">组织单位编码</th>
					<th width="25%">组织单位描述</th>
				</tr>
			</thead>
			<tbody id="unitBody">
				<c:forEach items="${orgUnitList.result}" var="orgU" varStatus="vs">
					<tr>
						<td><c:if test="${orgU.unitLevel != 0 }">
								<input type="checkbox" name="resourceid"
									value="${orgU.resourceid }" autocomplete="off" />
							</c:if></td>
						<td>${orgU.unitName }</td>
						<td>${orgU.unitShortName }</td>
						<td>${orgU.unitCode }</td>
						<td>${orgU.unitDescript }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<gh:page page="${orgUnitList}"
			goPageUrl="${baseUrl }/edu3/system/org/unit/list.html?isSubPage=y"
			condition="${condition }" pageType="sys" targetType="localArea"
			localArea="_orgUnitListContent" />
	</div>

</body>
</html>