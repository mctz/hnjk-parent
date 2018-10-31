<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<table class="table" border="1">
		<thead>
			<tr>
				<th width="15%">消息类型</th>
				<th width="22%">标题</th>
				<c:choose>
					<c:when test="${type=='inbox' }">
						<th width="15%">发送人</th>
						<th width="15%">发送时间</th>
						<th width="10%">阅读情况</th>
					</c:when>
					<c:otherwise>
						<th width="20%">发送人</th>
						<th width="20%">发送时间</th>
					</c:otherwise>
				</c:choose>
				<th width="8%">是否回复</th>
				<!-- <th width="10%">操作</th> -->
			</tr>
		</thead>
		<tbody id="messagesBody">
			<c:choose>
				<c:when test="${type=='inbox' }">
					<c:forEach items="${messageReceiverUserPage.result}" var="mru"
						varStatus="s">
						<tr>
							<td>${ghfn:dictCode2Val('CodeMsgType',mru.messageReceiver.message.msgType) }
							</td>
							<td>${mru.messageReceiver.message.msgTitle }</td>
							<td>${mru.messageReceiver.message.senderName }</td>
							<td><fmt:formatDate
									value="${mru.messageReceiver.message.sendTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td style="${mru.readStatus=='unRead'?'red':''}">${ghfn:dictCode2Val('CodeReadStatus',mru.readStatus) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',mru.messageReceiver.message.isReply) }</td>
							<%-- <td><a target="dialog"
								href="${baseUrl }/edu3/framework/message/show.html?msgId=${mru.messageReceiver.message.resourceid }&type=${type}"
								title="查看消息" width="800" height="600" rel="view_message">查看</a>
								&nbsp;&nbsp; <c:if
									test="${(type eq 'inbox')and(mru.messageReceiver.message.isReply eq 'Y') }">
									<a target="navTab"
										href="${baseUrl }/edu3/portal/message/input.html?parentId=${mru.messageReceiver.message.resourceid }"
										rel="RES_PERSON_SYSMSG_INPUT" title="回复消息">回复</a>
								</c:if></td> --%>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${messagePage.result}" var="message"
						varStatus="s">
						<tr>
							<td>${ghfn:dictCode2Val('CodeMsgType',message.msgType) }</td>
							<td>${message.msgTitle }</td>
							<td>${message.senderName }</td>
							<td><fmt:formatDate value="${message.sendTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('yesOrNo',message.isReply) }</td>
							<%-- <td><a target="dialog"
								href="${baseUrl }/edu3/framework/message/show.html?msgId=${message.resourceid }&type=${type}"
								title="查看消息" width="800" height="600" rel="view_message">查看</a>
								&nbsp;&nbsp; <c:if
									test="${(type eq 'inbox')and(message.isReply eq 'Y') }">
									<a target="navTab"
										href="${baseUrl }/edu3/portal/message/input.html?parentId=${message.resourceid }"
										rel="RES_PERSON_SYSMSG_INPUT" title="回复消息">回复</a>
								</c:if></td> --%>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</body>