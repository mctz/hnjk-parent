<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常用下载模板</title>
	<link rel="stylesheet" href="${baseUrl }/themes/bootstrap/bootstrap.min.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${baseUrl }/themes/bootstrap/toastr.min.css" rel="stylesheet"/>
	<script src="${baseUrl }/jscript/jquery-2.1.1.js" type="text/javascript"></script>
	<script src="${baseUrl }/jscript/bootstrap/bootstrap.min.js" type="text/javascript"></script>
	<script src="${baseUrl }/jscript/bootstrap/toastr.min.js" type="text/javascript"></script>

<style type="text/css">

.table th, .table td { 
text-align: center;
vertical-align: middle!important;
}

</style>
<script type="text/javascript">
function downloadArticleZip(articleId){
	var url ="${baseUrl }/portal/site/article/download.html?articleId="+articleId;
	 _downloadFileByIframe(url, "downloadArticleZip");
}

function _downloadFileByIframe(url,iframeId){
	$('#'+iframeId).remove();
	var iframe = document.createElement("iframe");
	iframe.id = iframeId;
	iframe.src = url;
	iframe.style.display = "none";	
//	var bgbar = $("#background,#progressBar").show();//显示:数据加载中，请稍等...
//	if (iframe.attachEvent){
//	    iframe.attachEvent("onload", function(){//加载完毕 IE
//	        bgbar.hide();
//	    });
//	} else {
//		iframe.addEventListener("load",function(){
//	        bgbar.hide();
//	    },false);
//	    iframe.onload = function(){
//	    	bgbar.hide();
//	    };
//	}
	
	document.body.appendChild(iframe);
	
}
</script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading" align="center">
			<h2>
				常用下载
			</h2>
		</div>
		<div class="table-responsive">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th style="width: 50%;">名称</th>
						<th style="width: 10%;">类型</th>
						<th style="width: 10%;">文件大小</th>
						<th style="width: 10%;">上传日期</th>
						<th style="width: 10%;">上传者</th>
						<th style="width: 10%;">操作</th>
					</tr>
				</thead>
				<tbody id="picCarouselTbody">
					<c:forEach items="${articleList.result}" var="article"
						varStatus="vs">
						<tr>
							<td>${article.title}</td>
							<td>${article.artitype}</td>
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
							<td>
							<c:choose>
							<c:when test="${empty article.updateDate }"><fmt:formatDate value="${article.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise><fmt:formatDate value="${article.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></c:otherwise>
							</c:choose>
							</td>
							<td>${article.fillinMan }</td>							
							<td><a href="#" style="color: blue"
								onclick="downloadArticleZip('${article.resourceid}')">下载</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>