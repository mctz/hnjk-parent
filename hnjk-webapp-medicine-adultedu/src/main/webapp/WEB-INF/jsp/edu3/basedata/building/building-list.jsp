<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学楼管理</title>
<script type="text/javascript">
	//新增
	function addBuilding(){
		navTab.openTab('RES_BASEDATA_BUILDING_INPUT', '${baseUrl}/edu3/sysmanager/building/input.html', '新增教学楼');
	}
	
	//修改
	function modifyBuilding(){
		var url = "${baseUrl}/edu3/sysmanager/building/input.html";
		if(isCheckOnlyone('resourceid','#buildingBody')){
			navTab.openTab('RES_BASEDATA_BUILDING_INPUT', url+'?resourceid='+$("#buildingBody input[@name='resourceid']:checked").val(), '编辑教学楼');
		}			
	}
		
	//删除
	function removeBuilding(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/building/remove.html","#buildingBody");
	}
	//课室管理
	function listClassroom(){
		var url = "${baseUrl}/edu3/sysmanager/classroom/list.html";
		if(isCheckOnlyone('resourceid','#buildingBody')){
			navTab.openTab('RES_BASEDATA_CLASSROOM_MANAGER', url+'?buildingId='+$("#buildingBody input[@name='resourceid']:checked").val(), '教室管理');
		}	
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/building/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${not brschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchoolId" tabindex="1" id="building_brSchoolId"
									defaultValue="${condition['branchSchoolId']}" /></li>
						</c:if>
						<li><label>教学楼名称 ：</label><input type="text"
							name="buildingName" value="${condition['buildingName']}" /></li>
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
			<gh:resAuth parentCode="RES_BASEDATA_BUILDING" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_building"
							onclick="checkboxAll('#check_all_building','resourceid','#buildingBody')" /></th>
						<th width="20%">所属教学站</th>
						<th width="20%">教学楼名称</th>
						<th width="10%">最高楼层</th>
						<th width="10%">每层最大单元数</th>
						<th width="10%">电话</th>
						<th width="25%">备注</th>
					</tr>
				</thead>
				<tbody id="buildingBody">
					<c:forEach items="${buildingPage.result}" var="building"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${building.resourceid }" autocomplete="off" /></td>
							<td align="left">${building.branchSchool.unitName }</td>
							<td align="left">${building.buildingName }</td>
							<td align="left">${building.maxLayers }</td>
							<td align="left">${building.maxUnits }</td>
							<td align="left">${building.phone }</td>
							<td align="left">${building.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${buildingPage}"
				goPageUrl="${baseUrl }/edu3/sysmanager/building/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>