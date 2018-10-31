<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看课程公告</title>
<style>
.pageContent p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
}
</style>
<script type="text/javascript">
	//附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent" layoutH="25">
			<h2>
				发布人: ${courseNotice.fillinMan}, 发布时间:
				<fmt:formatDate value="${courseNotice.fillinDate }"
					pattern="yyyy-MM-dd HH:mm:ss" />
			</h2>
			<div style="width: 99%; line-height: 170%;">${courseNotice.noticeContent }</div>
			<br />
			<c:if test="${not empty courseNotice.attachs}">
				<div style="font-weight: bold;">附件：</div>
				<ul>
					<c:forEach items="${courseNotice.attachs}" var="attach"
						varStatus="vs">
						<li id="${attach.resourceid }" style="list-style:"><img
							style="cursor: pointer; height: 10px;"
							src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />
							&nbsp;&nbsp; <a
							onclick="downloadAttachFile('${attach.resourceid }')" href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
						</li>
					</c:forEach>
				</ul>
				<br />
			</c:if>
		</div>
	</div>
</body>
</html>