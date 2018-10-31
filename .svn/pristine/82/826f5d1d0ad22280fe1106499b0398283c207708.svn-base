<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>在线帮助文章管理</title>
</head>
<body>
	<script type="text/javascript">	
	//新增文章
	function addHelpArticle(){
		var url = "${baseUrl}/edu3/portal/manage/helper/article/input.html?method=add";
		var channelId = $('#helpArticleSearchForm #channelId').val();
	 	if(channelId == ""){
	 		alertMsg.warn("请选择左边的某个栏目！");
	 		return false;
	 	}
		navTab.openTab('_blank', url+'&channelId='+channelId, '新增帮助文章');
	}
	
	//修改文章
	function modifyHelpArticle(){
		var url = "${baseUrl}/edu3/portal/manage/helper/article/input.html?method=edit";
		if(isCheckOnlyone('resourceid','#helpArticleBody')){
			navTab.openTab('_blank', url+'&resourceid='+$("#helpArticleBody input[@name='resourceid']:checked").val(), '编辑帮助文章');
		}			
	}
		
	//删除
	function deleteHelpArticle(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/portal/manage/helper/article/remove.html","#helpArticleBody");
	}
		
	//审核发布
	function auditHelpArticle(type){
		var msg = "审核发布";
		if(type == "nopublish"){
			msg = "取消审核发布";
		}	
		pageBarHandle("您确定要"+msg+"这些记录吗？","${baseUrl}/edu3/portal/manage/helper/article/publish.html?isPublish="+type,"#helpArticleBody");
	}

	//预览
 function previewHelpArticle(){
		if(isCheckOnlyone('resourceid','#helpArticleBody')){
			var res = $("#helpArticleBody input[@name='resourceid']:checked").val();		              
			window.open('${baseUrl}/portal/help/detail.html?id='+res,'newwindow');
		}
	}

</script>

	<div class="pageHeader">
		<form id="helpArticleSearchForm"
			onsubmit="return localAreaSearch('_helpArticleListContent',this);"
			action="${baseUrl }/edu3/portal/manage/helper/article/list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>文章标题：</label><input type="text" name="title"
						value="${condition['title']}" /> <input type="hidden"
						name="channelId" id="channelId" value="${condition['channelId'] }" />
					</li>
					<li><label>发布状态：</label><select name="isPublish">
							<option value="">请选择</option>
							<option value="N"
								<c:if test="${condition['isPublish']=='N'}">selected="selected"</c:if>>否</option>
							<option value="Y"
								<c:if test="${condition['isPublish']=='Y'}">selected="selected"</c:if>>是</option>
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
		<gh:resAuth parentCode="RES_HELP_ARTICLE" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="helpActicleCheck_all"
						onclick="checkboxAll('#helpActicleCheck_all','resourceid','#helpArticleBody')" /></th>
					<th width="45%">文章标题</th>
					<th width="15%">栏目</th>
					<th width="15%">撰稿人</th>
					<th width="8%">是否发布</th>
					<th width="6%">已解决</th>
					<th width="6%">未解决</th>
				</tr>
			</thead>
			<tbody id="helpArticleBody">
				<c:forEach items="${helpArticleList.result}" var="helpArticle"
					varStatus="vs">
					<tr>
						<td><input type="checkbox" name="resourceid"
							value="${helpArticle.resourceid }" autocomplete="off" /></td>
						<td>${helpArticle.title }</td>
						<td>${helpArticle.channel.channelName }</td>
						<td>${helpArticle.fillinMan }</td>
						<td><c:if test="${helpArticle.isPublish eq 'Y' }">是</c:if>
							<c:if test="${helpArticle.isPublish eq 'N' }">否</c:if></td>
						<td>${helpArticle.resolved}</td>
						<td>${helpArticle.unresolved}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<gh:page page="${helpArticleList}"
			goPageUrl="${baseUrl }/edu3/portal/manage/helper/article/list.html?isSubPage=y"
			pageType="sys" condition="${condition }" targetType="localArea"
			localArea="_helpArticleListContent" />
	</div>

</body>
</html>