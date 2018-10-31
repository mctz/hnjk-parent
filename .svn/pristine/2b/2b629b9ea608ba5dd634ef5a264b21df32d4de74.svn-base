<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间设置列表</title>
<script type="text/javascript">
	//新增
	function addSensitiveWord(){
		navTab.openTab('navTab', '${baseUrl}/edu3/system/sensitiveWord/edit.html', '敏感词管理');
	}
	
	//修改
	function modifySensitiveWord(){
		var url = "${baseUrl}/edu3/system/sensitiveWord/edit.html";
		if(isCheckOnlyone('resourceid','#sensitiveWordBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#sensitiveWordBody input[@name='resourceid']:checked").val(), '敏感词管理');
		}			
	}
		
	//删除
	function deleteSensitiveWord(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/sensitiveWord/delete.html","#sensitiveWordBody");
	}
	
	// 导入敏感词
	function importSensitiveWord(){
		var url = "${baseUrl}/edu3/system/sensitiveWord/import.html";		
		$.pdialog.open(url, 'RES_SYS_SENSITIVEWORD_IMPORT', '导入敏感词', {width: 600, height: 400});
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/sensitiveWord/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>敏感词：</label> <input type="text" name="word"
							style="width: 52%" value="${condition['word'] }" /></li>
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
			<gh:resAuth parentCode="RES_SYS_SENSITIVEWORD" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_sensitiveWord"
							onclick="checkboxAll('#check_all_sensitiveWord','resourceid','#sensitiveWordBody')" /></th>
						<th width="55%">敏感词</th>
						<th width="20%">创建时间</th>
						<th width="20%">更新时间</th>
					</tr>
				</thead>
				<tbody id="sensitiveWordBody">
					<c:forEach items="${sensitiveWordList.result}" var="sensitiveWord"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${sensitiveWord.resourceid }" autocomplete="off" /></td>
							<td>${sensitiveWord.word }</td>
							<td><fmt:formatDate value="${sensitiveWord.createDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${sensitiveWord.updateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${sensitiveWordList}"
				goPageUrl="${baseUrl }/edu3/system/sensitiveWord/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>