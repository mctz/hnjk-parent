<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>层次设置</title>
<script type="text/javascript">
	//新增
	function addClassic(){
		navTab.openTab('navTab', '${baseUrl}/edu3/sysmanager/classic/edit.html', '新增层次');
	}
	
	//修改
	function modifyClassic(){
		var url = "${baseUrl}/edu3/sysmanager/classic/edit.html";
		if(isCheckOnlyone('resourceid','#classBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#classBody input[@name='resourceid']:checked").val(), '编辑层次');
		}			
	}
		
	//删除
	function deleteClassic(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/classic/delete.html","#classBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/classic/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>层次名称：</label><input type="text" name="classicName"
							value="${condition['classicName']}" /></li>
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
			<gh:resAuth parentCode="RES_BASEDATA_CLASSIC" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_class"
							onclick="checkboxAll('#check_all_class','resourceid','#classBody')" /></th>
						<th width="15%">层次代码</th>
						<th width="15%">层次名称</th>
						<th width="15%">层次英文名</th>
						<th width="15%">起点</th>
						<th width="15%">终点</th>
						<th width="20%">简称</th>
					</tr>
				</thead>
				<tbody id="classBody">
					<c:forEach items="${classicList.result}" var="classic"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${classic.resourceid }" autocomplete="off" /></td>
							<td>${classic.classicCode }</td>
							<td>${classic.classicName }</td>
							<td>${classic.classicEnName }</td>
							<td>${classic.startPoint }</td>
							<td>${classic.endPoint }</td>
							<td>${classic.shortName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${classicList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/classic/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>