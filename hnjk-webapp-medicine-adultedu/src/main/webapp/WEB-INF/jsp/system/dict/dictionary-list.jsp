<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>字典管理</title>
<script type="text/javascript">
	//新增
	function addDict(){
		navTab.openTab('navTab', '${baseUrl}/edu3/system/dictionary/edit.html', '新增字典');
	}
	
	//修改
	function modifyDict(){
		var url = "${baseUrl}/edu3/system/dictionary/edit.html";
		if(isCheckOnlyone('resourceid','#dictBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#dictBody input[@name='resourceid']:checked").val(), '编辑字典');
		}			
	}
		
	//删除
	function deleteDict(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/dictionary/delete.html","#dictBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/dictionary/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>所属模块：</label><input type="text" name="module"
							value="${condition['module']}" /></li>
						<li><label>字典名：</label><input type="text" name="dictName"
							value="${condition['dictName']}" style="width: 130px;"/></li>
						<li style="width: 300px;"><label>字典编码：</label><input type="text" name="dictCode"
							value="${condition['dictCode']}" style="width: 200px;" /></li>
						
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
			<gh:resAuth parentCode="RES_SYS_DICTIONARY" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_dict"
							onclick="checkboxAll('#check_all_dict','resourceid','#dictBody')" /></th>
						<th width="35%">所属模块</th>
						<th width="35%">字典编码</th>
						<th width="25%">字典名</th>
					</tr>
				</thead>
				<tbody id="dictBody">
					<c:forEach items="${dictList.result}" var="dictionary"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${dictionary.resourceid }" autocomplete="off" /></td>
							<td>${dictionary.module }</td>
							<td>${dictionary.dictCode }</td>
							<td>${dictionary.dictName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${dictList}"
				goPageUrl="${baseUrl }/edu3/system/dictionary/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
</html>