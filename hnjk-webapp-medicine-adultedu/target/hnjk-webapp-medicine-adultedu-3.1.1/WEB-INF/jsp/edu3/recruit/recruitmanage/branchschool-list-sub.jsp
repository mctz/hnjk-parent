<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>

<script type="text/javascript">
	
	function addRecruitOrgUnit(){
	 	var url = "${baseUrl}/edu3/recruit/recruitmanage/orgunit/input.html";
	 	var unitId = $('#branchschool_unitForm #unitId').val();
	 	if(unitId == ""){
	 		alertMsg.warn("请选择左边的某个单位！");
	 		return false;
	 	}
	 	navTab.openTab('RES_RECRUIT_MANAGE_ORGUNIT_EDIT', url+'?unitId='+unitId, '新增单位');
 	}
 	
 	function modifyRecruitOrgUnit(){
			var url = "${baseUrl}/edu3/recruit/recruitmanage/orgunit/input.html";
			if(isCheckOnlyone('resourceid','#branchschoolBody')){
		 	navTab.openTab('RES_RECRUIT_MANAGE_ORGUNIT_EDIT', url+'?resourceid='+$("#branchschoolBody input[@name='resourceid']:checked").val(), '编辑单位');
		}			
	}
	
	//删除
	function deleteRecruitOrgUnit(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/recruit/recruitmanage/orgunit/delete.html","#branchschoolBody");
	}
		
	function viewBranchSchoolLimitMajor(brschoolId,brschoolName){
		var url = "${baseUrl}/edu3/recruit/recruitmanage/brahchSchool-limit-major.html?brahchSchoolId="+brschoolId;		
		$.pdialog.open(url,'brschoolLimitMajorList'+brschoolId,brschoolName+'-允许的招生专业列表',{width:800, height:600});	
	}
</script>
</head>
<body>


	<div class="pageHeader">
		<form id="branchschool_unitForm"
			onsubmit="return localAreaSearch('_branchschoolListContent',this);"
			action="${baseUrl }/edu3/recruit/recruitmanage/orgunit/list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li style="width: 360px;"><label>单位名称：</label><input type="text" name="unitName"
						id="unitName" value="${condition['unitName'] }" style="width: 240px;"/> <input
						type="hidden" id="unitId" name="unitId" 
						value="${condition['unitId'] }"></li>
					<li><label style="text-align: left; width: 110px;">教学站办学模式：</label>
						<gh:select name="schoolType" dictionaryCode="CodeTeachingType"
							value="${condition['schoolType']}" /></li>
					<li><label style="text-align: left; width: 50px;">状态：</label>
						<gh:select name="status" dictionaryCode="CodeOrgUnitStatus"
							value="${condition['status']}" /></li>
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
		<gh:resAuth parentCode="RES_RECRUIT_MANAGE_ORGUNIT" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all_branchschool"
						onclick="checkboxAll('#check_all_branchschool','resourceid','#branchschoolBody')" /></th>
					<th width="15%">组织单位名称</th>
					<th width="14%">组织单位简称</th>
					<th width="10%">组织单位编码</th>
					<th width="15%">组织单位描述</th>
					<th width="8%">状态</th>
					<th width="8%">是否机考</th>
					<th width="10%"></th>

				</tr>
			</thead>
			<tbody id="branchschoolBody">
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
						<td>${ghfn:dictCode2Val('CodeOrgUnitStatus',orgU.status ) }</td>
						<td><c:choose>
								<c:when test="${orgU.isMachineExam eq 'Y' }">是</c:when>
								<c:otherwise>否</c:otherwise>
							</c:choose></td>
						<td><a href="javascript:void(0)"
							onclick="viewBranchSchoolLimitMajor('${orgU.resourceid}','${orgU.unitName }');">查看招生专业</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<gh:page page="${orgUnitList}"
			goPageUrl="${baseUrl }/edu3/recruit/recruitmanage/orgunit/list.html?isSubPage=y"
			condition="${condition }" pageType="sys" targetType="localArea"
			localArea="_branchschoolListContent" />
	</div>

</body>
</html>