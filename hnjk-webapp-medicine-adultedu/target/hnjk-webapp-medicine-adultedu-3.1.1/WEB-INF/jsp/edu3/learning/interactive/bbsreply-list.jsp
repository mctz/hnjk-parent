<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>${bbsTopic.title }</title>
<style>
.pageFormContent p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
}
</style>
</head>
<body>
	<h2 class="contentTitle" style="border-bottom: medium;">
		<div>
			问题：${bbsTopic.title }<span style="color: #006699; float: right;">提问时间：<fmt:formatDate
					value="${bbsTopic.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" />
				&nbsp;&nbsp; 提问人：${bbsTopic.fillinMan }
			</span>
		</div>
	</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layouth="72">
				<table class="form">
					<thead>
						<tr>
							<td width="20%"><div style="color: #006699;">${bbsTopic.fillinMan }
									(提问人)<br />
									<fmt:formatDate value="${bbsTopic.fillinDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
									<br />
								</div></td>
							<td>
								<div style="line-height: 26px;">${bbsTopic.content }</div>
							</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${bbsReplyListPage.result }" var="bbsReply"
							varStatus="vs">
							<tr
								<c:if test="${fn:contains(teacherIds,bbsReply.bbsUserInfo.sysUser.resourceid) }">style="border: 2px solid #FF0000;"</c:if>>
								<td style="line-height: 26px;"><div style="color: #006699;">${bbsReply.replyMan }<br />
										<fmt:formatDate value="${bbsReply.replyDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
										<br />
									</div></td>
								<td>
									<div align="justify">${bbsReply.replyContent }</div> <c:if
										test="${not empty bbsReply.attachs}">
										<div
											style="margin-left: 5px 0; border: 1px solid #E6E6E2; margin-bottom: 6px; padding: 0.5em;">
											<div style="font-weight: bold;">附件：</div>
											<c:forEach items="${bbsReply.attachs}" var="attach"
												varStatus="vs">
												<li id="${attach.resourceid }"><img
													style="cursor: pointer; height: 10px;"
													src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />
													&nbsp;&nbsp; <a
													onclick="downloadAttachFile('${attach.resourceid }')"
													href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
											</c:forEach>
										</div>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<gh:page page="${bbsReplyListPage}"
				goPageUrl="${baseUrl}/edu3/learning/interactive/bbsreply/list.html"
				targetType="dialog" condition="${condition }" pageType="sys" />
		</div>
	</div>
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
</body>
</html>
