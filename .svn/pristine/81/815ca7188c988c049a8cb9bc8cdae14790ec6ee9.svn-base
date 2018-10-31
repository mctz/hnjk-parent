<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>文章管理</title>
</head>
<body>
	<script type="text/javascript">	
	//新增文章
	function addArticle(){
		var url = "${baseUrl}/edu3/portal/manage/article/input.html";
		var channelId = $('#articleSearchForm #channelId').val();
	 	if(channelId == ""){
	 		alertMsg.warn("请选择左边的某个栏目！");
	 		return false;
	 	}
		navTab.openTab('_blank', url+'?channelId='+channelId, '新增资料');
	}
	
	//修改文章
	function modifyArticle(){
		var url = "${baseUrl}/edu3/portal/manage/article/input.html";
		if(isCheckOnlyone('resourceid','#articleBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#articleBody input[@name='resourceid']:checked").val(), '编辑资料');
		}			
	}
		
	//删除
	function deleteArticle(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/portal/manage/article/remove.html","#articleBody");
	}
		
	//审核发布
	function auditArticle(type){
		var msg = "审核发布";
		if(type == "nopass"){
			msg = "取消审核发布";
		}	
		pageBarHandle("您确定要"+msg+"这些记录吗？","${baseUrl}/edu3/portal/manage/article/audit.html?auditType="+type,"#articleBody");
	}

	//预览
 function previewArticle(){
		if(isCheckOnlyone('resourceid','#articleBody')){
			var res = $("#articleBody input[@name='resourceid']:checked").val();		              
			window.open('${baseUrl}/portal/site/article/show.html?id='+res,'newwindow');
		}
	}

</script>

	<div class="pageHeader">
		<form id="articleSearchForm"
			onsubmit="return localAreaSearch('_articleListContent',this);"
			action="${baseUrl }/edu3/portal/manage/article/list.html?isSubPage=y"
			method="post">
			<div class="searchBar">
				<ul class="searchContent">
					<li style="width: 360px;">
						<label>标题：</label>
						<input type="text" name="title" value="${condition['title']}" style="width: 240px;" />
						<input type="hidden" name="channelId" id="channelId" value="${condition['channelId'] }" />
					</li>
					<%--
				<li>			
				<input type="checkbox" name="isPhotoNews" value="Y"/> 图片新闻 
				<input type="checkbox" name="isDraft" value="Y"/> 草稿
				</li>	
				 --%>
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
		<gh:resAuth parentCode="RES_PORTAL_ARTICLE" pageType="list"></gh:resAuth>
		<table class="table" layouth="138">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall"
						id="check_all"
						onclick="checkboxAll('#check_all','resourceid','#articleBody')" /></th>
					<th width="35%">资料标题</th>
					<th width="10%">所属栏目</th>
					<th width="15%">更新时间</th>
					<th width="10%">文件大小</th>
					<th width="10%">上传者</th>
					<!-- <th width="5%">文件类型</th> -->
					<!-- <th width="10%">是否草稿</th> -->
					<th width="10%">审核状态</th>
				</tr>
			</thead>
			<tbody id="articleBody">
				<c:forEach items="${articleList.result}" var="article"
					varStatus="vs">
					<tr>
						<td><input type="checkbox" name="resourceid"
							value="${article.resourceid }" autocomplete="off" /></td>
						<td>${article.title }</td>
						<td>${article.channel.channelName }</td>
						<td><fmt:formatDate value="${article.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><c:choose>
								<c:when test="${article.fileSize==0 || empty article.fileSize}"></c:when>
								<c:when test="${article.fileSize<=1024}">${article.fileSize} byte</c:when>
								<c:when test="${article.fileSize<=1024*1024}">
									<fmt:formatNumber type="number" value="${article.fileSize/1024}" pattern="0.00" maxFractionDigits="2"/> KB</c:when>
								<c:when test="${article.fileSize<=1024*1024*1024}">
									<fmt:formatNumber type="number" value="${article.fileSize/1024/1024}" pattern="0.00" maxFractionDigits="2"/> M</c:when>
								<c:otherwise><fmt:formatNumber type="number" value="${article.fileSize/1024/1024/1024}" pattern="0.00" maxFractionDigits="2"/> GB</c:otherwise>
							</c:choose>
						</td>
						<td>${article.fillinMan }</td>
						<%-- <td>${article.artitype }</td> --%>
						<%-- <td><c:if test="${article.isDraft eq 'Y' }">是</c:if>
							<c:if test="${article.isDraft eq 'N' }">否</c:if></td> --%>
						<td><c:if test="${article.auditStatus == 0 }">待审核</c:if>
							<c:if test="${article.auditStatus == 1 }">审核通过</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<gh:page page="${articleList}"
			goPageUrl="${baseUrl }/edu3/portal/manage/article/list.html?isSubPage=y"
			pageType="sys" condition="${condition }" targetType="localArea"
			localArea="_articleListContent" />
	</div>

</body>
</html>