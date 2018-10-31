<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板管理</title>
<script type="text/javascript">
	//修改
	function modifyTemplate(){
		var url = "${baseUrl}/edu3/portal/manage/template/input.html";
		if(isCheckOnlyone('filename','#templateBody')){
			navTab.openTab('_blank', url+'?filename='+$("input[@name='filename']:checked").val(), '编辑模板');
		}			
	}

	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/manage/template/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>模板名称：</label> <input type="text"
							name="templatename" /></li>
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
			<gh:resAuth parentCode="RES_PORTAL_TEMPLATE" pageType="list"></gh:resAuth>
			<table class="table" layouth="142">
				<thead>
					<tr>
						<th style="width: 5%;"><input type="checkbox" name="checkall"
							id="check_all"
							onclick="checkboxAll('#check_all','filename','#templateBody')" /></th>
						<th style="width: 40%;">模板名称</th>
						<th style="width: 15%;">模板大小</th>
						<th style="width: 25%;">最后修改时间</th>
					</tr>
				</thead>
				<tbody id="templateBody">
					<c:forEach items="${fileList}" var="template" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="filename"
								value="${template.attName }" autocomplete="off" /></td>
							<td>${template.attName }</td>
							<td>${ghfn:formatFileSize(template.attSize) }</td>
							<td><fmt:formatDate value="${template.uploadTime }"
									pattern="yyyy-MM-dd HH:mm" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="panelBar">
				<div class="pages">
					<span>共找到 ${fn:length(fileList)} 条记录</span>
				</div>
				<div class="pagination" targetType="navTab"
					totalCount="${fn:length(fileList)}" condition="${condition }"
					numPerPage="20" pageNumShown="10" currentPage="1"></div>
			</div>
		</div>
	</div>


</body>
</html>