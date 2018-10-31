<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>温馨提示</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="45">
				<thead>
					<tr>
						<th width="15%">消息类型</th>
						<th width="35%">标题</th>
						<th width="25%">发送人</th>
						<th width="25%">发送时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${messagePage.result}" var="msgReceiverUser"
						varStatus="s">
						<tr>
							<td>${ghfn:dictCode2Val('CodeMsgType',msgReceiverUser.messageReceiver.message.msgType) }
							</td>
							<td
								style="font-weight: ${(not msgReceiverUser.messageReceiver.message.readed)?'bold':'normal' }">
								<a target="dialog"
								href="${baseUrl }/edu3/framework/message/show.html?msgId=${msgReceiverUser.messageReceiver.message.resourceid }&type=inbox"
								title="${msgReceiverUser.messageReceiver.message.msgTitle }"
								width="800" height="600" rel="interactive_view_message">${msgReceiverUser.messageReceiver.message.msgTitle }</a>
							</td>
							<td>${msgReceiverUser.messageReceiver.message.senderName }</td>
							<td><fmt:formatDate
									value="${msgReceiverUser.messageReceiver.message.sendTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${messagePage}"
				goPageUrl="${baseUrl }/edu3/learning/interactive/message/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>