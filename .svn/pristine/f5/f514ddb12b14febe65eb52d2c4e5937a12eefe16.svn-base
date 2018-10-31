<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国家专业设置</title>
<script type="text/javascript">
	//新建
	function addNationMajor(){
		navTab.openTab('RES_BASEDATA_MANAGER_NATIONMAJOR', '${baseUrl}/edu3/sysmanager/nationmajor/input.html', '新增国家专业');
	}
	
	//修改
	function modifyNationMajor(){
		var url = "${baseUrl}/edu3/sysmanager/nationmajor/input.html";
		if(isCheckOnlyone('resourceid','#nationMajorBody')){
			navTab.openTab('RES_BASEDATA_MANAGER_NATIONMAJOR', url+'?resourceid='+$("#nationMajorBody input[@name='resourceid']:checked").val(), '编辑国家专业');
		}			
	}
		
	//删除
	function removeNationMajor(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/nationmajor/remove.html","#nationMajorBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/nationmajor/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>高职高专代码</label><input type="text"
							name="nationMajorCode" value="${condition['nationMajorCode']}"
							style="margin-left: 10px;" /></li>
						<li><label>专业名称</label><input type="text"
							name="nationMajorName" value="${condition['nationMajorName']}" />
						</li>
						<li><label>层次</label>
						<gh:selectModel name="classicid" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classicid']}"
								style="width:150px;height:22px;" /></li>

					</ul>
					<ul style="margin-top: 10px;">
						<li><label>科类</label>
						<gh:select name="nationMajorType"
								value="${condition['nationMajorType'] }"
								dictionaryCode="CodeMajorClass" isSubOptionText="value-code"
								style="height:22px;" /></li>
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
			<gh:resAuth parentCode="RES_BASEDATA_MAJOR" pageType="sublist"></gh:resAuth>
			<table class="table" layouth="170">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_nationmajor"
							onclick="checkboxAll('#check_all_nationmajor','resourceid','#nationMajorBody')" /></th>
						<th width="10%">专业大类</th>
						<th width="10%">专业类别</th>
						<th width="10%">高职高专代码</th>
						<th width="15%">专业名称</th>
						<th width="10%">科类</th>
						<th width="5%">科类代码</th>
						<th width="5%">层次</th>
						<th width="10%">专业方向</th>
						<th width="20%">备注</th>
					</tr>
				</thead>
				<tbody id="nationMajorBody">
					<c:forEach items="${nationMajorList.result}" var="nationMajor"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${nationMajor.resourceid }" autocomplete="off" /></td>
							<td>${ghfn:dictCode2Val('nationmajorParentCatolog',nationMajor.parentCatalog) }</td>
							<td>${ghfn:dictCode2Val('nationmajorChildCatolog', nationMajor.childCatalog) }</td>
							<td>${nationMajor.nationMajorCode }</td>
							<td>${nationMajor.nationMajorName }</td>
							<td>${ghfn:dictCode2Val('CodeMajorClass',nationMajor.nationMajorType) }</td>
							<td>${nationMajor.nationMajorType}</td>
							<td>${nationMajor.classic.classicName }</td>
							<td>${nationMajor.nationMajorDict }</td>
							<td>${nationMajor.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${nationMajorList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/nationmajor/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>