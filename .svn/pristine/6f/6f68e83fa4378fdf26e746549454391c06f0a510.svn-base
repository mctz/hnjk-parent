<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生范围管理</title>
<script type="text/javascript">
	//新增
	function addRecruitmentScope(){
		navTab.openTab('navTab', '${baseUrl}/edu3/enrollment/booking/recruitmentScope/edit.html', '新增招生范围');
	}
	
	//修改
	function modifyRecruitmentScope(){
		var url = "${baseUrl}/edu3/enrollment/booking/recruitmentScope/edit.html";
		if(isCheckOnlyone('resourceid','#recruitmentScopeBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#recruitmentScopeBody input[@name='resourceid']:checked").val(), '编辑招生范围');
		}			
	}
		
	//删除
	function deleteRecruitmentScope(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/enrollment/booking/recruitmentScope/delete.html","#recruitmentScopeBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/enrollment/booking/recruitmentScope/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li>
							<label>教学点：</label>
							<gh:brSchoolAutocomplete name="unitId"  tabindex="2"
										id="recruitmentScope_list_unitId" defaultValue="${condition['unitId']}" displayType="code" style="width:60%;" />
						</li>
						<li>
							<label>服务地区：</label>
							<gh:select name="areaScope" value="${condition['areaScope']}"
									dictionaryCode="CodeRecruitmentScope" style="width:55%"  size="150"/>
						</li>
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
			<gh:resAuth parentCode="RES_ENROLLMENT_BOOKING_RECRUITMENTSCOPE" pageType="list"></gh:resAuth>
			<table id="recruitmentScopeTab" class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">
							<input type="checkbox" name="checkall"  id="check_all_recruitmentScope"
								onclick="checkboxAll('#check_all_recruitmentScope','resourceid','#recruitmentScopeBody')" />
						</th>
						<th width="50%">教学点</th>
						<th width="45%">服务地区</th>
					</tr>
				</thead>
				<tbody id="recruitmentScopeBody">
					<c:forEach items="${scopeList.result}" var="scope" varStatus="vs">
						<tr>
							<td>
								<input type="checkbox" name="resourceid" value="${scope.resourceid }" autocomplete="off" />
							</td>
							<td>${scope.unit.unitName }</td>
							<td>${ghfn:dictCode2Val('CodeRecruitmentScope',scope.areaScope) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${scopeList}" goPageUrl="${baseUrl }/edu3/enrollment/booking/recruitmentScope/list.html" pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>