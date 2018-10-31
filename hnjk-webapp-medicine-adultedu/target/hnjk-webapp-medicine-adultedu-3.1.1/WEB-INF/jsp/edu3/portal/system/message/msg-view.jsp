<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<body>
	<style>
p {
	line-height: 170%;
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
	<h2 class="contentTitle">${messageReceiver.message.msgTitle }</h2>
	<div style="margin-bottom: 8px">
		来 自：${messageReceiver.message.senderName } <span
			style="padding-left: 12px">发送时间:<fmt:formatDate
				value="${messageReceiver.message.sendTime }"
				pattern="yyyy-MM-dd HH:mm:ss" /></span>
		<c:if test="${not empty messageReceiver }">
			<br />
			<span>接收人： <c:choose>
					<c:when test="${messageReceiver.receiveType eq 'user' }">
						<c:if test="${not empty messageReceiver.userCnNames }">${fn:substring(messageReceiver.userCnNames,0,fn:length(messageReceiver.userCnNames)-1) }</c:if>
					</c:when>
					<c:when test="${messageReceiver.receiveType eq 'org' }">${messageReceiver.orgUnitNames }</c:when>
					<c:when test="${messageReceiver.receiveType eq 'grade' }">${messageReceiver.gradeNames }</c:when>
					<c:when test="${messageReceiver.receiveType eq 'classes' }">${messageReceiver.classesNames }</c:when>
					<c:otherwise>${messageReceiver.roleNames }</c:otherwise>
				</c:choose>
			</span>
		</c:if>
	</div>
	<div class="page">
		<div class="pageContent">
			<div class="tabs" eventType="click">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li><a href="javascript:void(0)"><span>消息内容</span></a></li>
							<c:if test="${type ne 'inbox' }">
								<li><a href="javascript:void(1)"><span>阅读情况</span></a></li>
							</c:if>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<div>
						<div layoutH="148" style="background-color: #fff;">
							<div style="width: 99%; line-height: 170%;">${messageReceiver.message.content }</div>
							<c:if test="${not empty messageReceiver.message.attachs}">
								<div
									style="margin-top: 5px; margin-left: 5px; border: 1px solid #E6E6E2; margin-bottom: 6px; padding: 0.5em;">
									<div>附件：</div>
									<c:forEach items="${messageReceiver.message.attachs}"
										var="attach" varStatus="vs">
										<li id="${attach.resourceid }"><img
											style="cursor: pointer; height: 10px;"
											src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />
											&nbsp;&nbsp; <a
											onclick="downloadAttachFile('${attach.resourceid }')"
											href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
									</c:forEach>
								</div>
							</c:if>
						</div>
					</div>
					<c:if test="${type ne 'inbox' }">
						<div>
							<table class="table" layoutH="148">
								<thead>
									<tr>
										<th>序号</th>
										<th>阅读人</th>
										<th>阅读状态</th>
										<th>阅读时间</th>
										<th>消息状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${messageStats.result}" var="messageStat"
										varStatus="s">
										<tr>
											<td>${s.index+1 }</td>
											<td>${messageStat.userCNName }</td>
											<td>${ghfn:dictCode2Val("CodeReadStatus",messageStat.readStatus)}</td>
											<td><fmt:formatDate value="${messageStat.readTime }"
													pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td>${ghfn:dictCode2Val("CodeMsgStatus",messageStat.msgStatus)}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<gh:page page="${messageStats}"
								goPageUrl="${baseUrl}/edu3/framework/message/show.html"
								targetType="dialog" pageNumShown="5" condition="${condition }"
								pageType="sys" />
						</div>
					</c:if>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>

			<div class="formBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type=submit class="close">关闭</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</div>
</body>
