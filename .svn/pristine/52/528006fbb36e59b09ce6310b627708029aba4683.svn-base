<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生限制管理</title>
<script type="text/javascript">
	
	//修改
	function modify(){
		var url = "${baseUrl}/edu3/recruit/recruitmanage/recruitlimit-form.html";
		if(isCheckOnlyone('resourceid','#slimitBody')){
			navTab.openTab('limit', url+'?resourceid='+$("#slimitBody input[@name='resourceid']:checked").val(), '招生限制表单');
		}			
	}
	//查看教学站招生专业
	function viewBranchSchoolLimitMajor(brschoolId,brschoolName){
		var url = "${baseUrl}/edu3/recruit/recruitmanage/brahchSchool-limit-major.html?brahchSchoolId="+brschoolId;
		//navTab.openTab('brschoolLimitMajorList'+brschoolId, url,brschoolName+'-允许的招生专业列表');
		$.pdialog.open(url,'brschoolLimitMajorList'+brschoolId,brschoolName+'-允许的招生专业列表',{width:800, height:600});	
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/recruitlimit-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label><input type="text" name="unitName"
							value="${condition['unitName']}" /></li>

						<li><label>是否允许申请新专业：</label> <select name="isAllowNewMajor">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isAllowNewMajor' ] eq 'Y'}"> selected="selected" </c:if>>是</option>
								<option value="N"
									<c:if test="${condition['isAllowNewMajor' ] eq 'N'}"> selected="selected" </c:if>>否</option>
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
			<div class="panelBar">
				<ul class="toolBar">
					<gh:resAuth parentCode="RES_RECRUIT_MANAGE_LIMIT" pageType="list"></gh:resAuth>
				</ul>
			</div>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_slimit"
							onclick="checkboxAll('#check_all_slimit','resourceid','#slimitBody')" /></th>
						<th width="20%">教学站编号</th>
						<th width="20%">教学站名称</th>
						<th width="20%">简称</th>
						<th width="10%">允许招生指总标数</th>
						<th width="15%">允许申请新专业</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="slimitBody">
					<c:forEach items="${page.result}" var="school" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${school.resourceid}" autocomplete="off" /></td>
							<td>${school.unitCode }</td>
							<td>${school.unitName }</td>
							<td>${school.unitShortName }</td>
							<td>${school.limitMajorNum }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',school.isAllowNewMajor) }</td>
							<td><a href="javascript:void(0)"
								onclick="viewBranchSchoolLimitMajor('${school.resourceid}','${school.unitName }');">查看招生专业</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitmanage/recruitlimit-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
