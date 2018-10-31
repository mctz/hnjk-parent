<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户信息管理</title>
<script type="text/javascript">
$(document).ready(function(){
});

function showResArticleView(articleId){
	var url = "${baseUrl }/edu3/portal/manage/article/resourcesArticle-view.html";
	$.pdialog.open(url+'?resourceid='+articleId, 'RES_PORTAL_RESOURCES_DOWNLOAD_VIEW', '查看资源', {width: 800, height: 600});
}

function downloadArticleZip(articleId){
	var url ="${baseUrl }/portal/site/article/download.html?articleId="+articleId;
	 downloadFileByIframe(url, "downloadArticleZip");
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/manage/article/resourcesArticle-list.html"
				method="post">
				<div class="searchBar">
					<input type="hidden" name="channelId" id="channelId"
						value="${condition['channelId'] }" />
					<ul class="searchContent">
						<li style="width: 360px;">
							<label>资料标题：</label>
							<input type="text" name="title" style="width: 240px;" value="${condition['title']}" />
						</li>
						<li>
							<label>上传者：</label>
							<input type="text" name="fillinMan" value="${condition['fillinMan']}" />
						</li>
					</ul>
					<ul class="searchContent">
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_PORTAL_RESOURCES_DOWNLOAD"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="135">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id=check_all_resArticle
							onclick="checkboxAll('#check_all_resArticle','resourceid','#resArticleBody')" /></th>
						<th width="5%">所属栏目</th>
						<th width="10%">所属单位</th>
						<th width="20%">资料标题</th>
						<th width="10%">更新时间</th>
						<th width="5%">文件大小</th>
						<th width="10%">上传者</th>
						<th width="5%">资料类型</th>
						<!-- <th width="40%">资料内容</th> -->
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody id="resArticleBody">
					<c:forEach items="${articleList.result}" var="article"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${article.resourceid }" autocomplete="off" /></td>
							<td>${article.channel.channelName}</td>
							<td>${article.orgUnit.unitName }</td>
							<td><a href="#"
								onclick="showResArticleView('${article.resourceid}')">${article.title}</a></td>
							<td><fmt:formatDate value="${article.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td><c:choose>
									<c:when test="${article.fileSize==0}"></c:when>
									<c:when test="${article.fileSize<=1024}">${article.fileSize} byte</c:when>
									<c:when test="${article.fileSize<=1024*1024}">
										<fmt:formatNumber type="number" value="${article.fileSize/1024}" pattern="0.00" maxFractionDigits="2"/> KB</c:when>
									<c:when test="${article.fileSize<=1024*1024*1024}">
										<fmt:formatNumber type="number" value="${article.fileSize/1024/1024}" pattern="0.00" maxFractionDigits="2"/> M</c:when>
									<c:otherwise><fmt:formatNumber type="number" value="${article.fileSize/1024/1024/1024}" pattern="0.00" maxFractionDigits="2"/> GB</c:otherwise>
								</c:choose>
							</td>
							<td>${article.fillinMan }</td>
							<td>${article.artitype }</td>
							<%-- <td>${article.content }</td> --%>
							<td><a href="#" style="color: green;"
								onclick="showResArticleView('${article.resourceid}')">查看</a>
								&nbsp;&nbsp;<a href="#" style="color: blue"
								onclick="downloadArticleZip('${article.resourceid}')">下载</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${articleList}"
				goPageUrl="${baseUrl }/edu3/portal/manage/article/resourcesArticle-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>