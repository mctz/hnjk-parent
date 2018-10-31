<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息管理</title>
<style>
.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
		jQuery(document).ready(function(){		
			if($('.leftMsgTree')){//如果有左侧树，则赋值为自适应高度		
				$('.leftMsgTree').height($("#container .tabsPageContent").height()-5);
			}
			$('#${type}').css({"font-weight":"bold"});	
		})
		
		function msgList(type){		
			navTab.reload("${baseUrl}/edu3/portal/message/list.html?fromtype=${condition['fromtype']}&type="+type);						
		}
		//新增消息
		function addMessage(){
			var type = $("#message_type").val();
			navTab.openTab('RES_PERSON_SYSMSG_INPUT', "${baseUrl}/edu3/portal/message/input.html?fromtype=${condition['fromtype']}&type="+type, "新增消息");
		}
		
		//编辑消息
		function modifyMessage(){	
			var type = '${type}';	
			if(type=='inbox'){
				alertMsg.warn("不可编辑接收到的消息！");
			} else {
				var url = "${baseUrl}/edu3/portal/message/input.html";
				if(isCheckOnlyone('resourceid','#messagesBody')){
					navTab.openTab('RES_PERSON_SYSMSG_INPUT', url+"?fromtype=${condition['fromtype']}&resourceid="+$("#messagesBody input[@name='resourceid']:checked").val()+"&type=${type}", '编辑消息');
				}	
			}			
		}
		
		//导出消息
		function exportMessage(){
			var type = $("#message_type").val();
			var url = "${baseUrl}/edu3/portal/message/list.html?flag=export&fromtype=${condition['fromtype']}&type="+type;
			downloadFileByIframe(url,'RES_PERSON_SYSMSG_EXPORT',"post");
		}
		//删除消息
		function removeMessage(){	
			pageBarHandle("您确定要删除这些消息吗？","${baseUrl}/edu3/portal/message/remove.html?fromtype=${condition['fromtype']}&type=${type}","#messagesBody");
		}
		
		//撤销消息
		function revokeMessage(){	
			pageBarHandle("您确定要撤销这些消息吗？","${baseUrl}/edu3/portal/message/revoke.html?fromtype=${condition['fromtype']}&type=${type}","#messagesBody");
		}
	</script>
</head>
<body>
	<div id="leftMsgBody" style="float: left; width: 11%">
		<div class="leftMsgTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<a class="panelBar type" id="inbox"
				href="javascript:msgList('inbox')">收件箱</a> <a class="panelBar type"
				id="draftbox" href="javascript:msgList('draftbox')">草稿箱</a> <a
				class="panelBar type" id="sendbox"
				href="javascript:msgList('sendbox')">发件箱</a>
		</div>
	</div>

	<div class="page" style="float: left; width: 89%">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/message/list.html" method="post"
				id="resForm">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>标题：</label><input type="text" name="msgTitle"
							value="${condition['msgTitle']}" style="width: 50%" /> <input
							type="hidden" id="message_type" name="type" value="${type}" /> <input
							type="hidden" id="message_info_from" name="fromtype"
							value="${condition['fromtype']}" /></li>
						<li><label>类型：</label>
						<gh:select dictionaryCode="CodeMsgType" name="msgType"
								value="${condition['msgType']}" style="width: 52%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>发送时间：</label><input type="text" name="startDate"
							value="${condition['startDate']}" class="Wdate" id="msgStartDate"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'msgEndDate\')}',dateFmt:'yyyy-MM-dd'})"
							style="width: 50%" /></li>
						<li><label>至：</label><input type="text" name="endDate"
							value="${condition['endDate']}" class="Wdate" id="msgEndDate"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'msgStartDate\')}',dateFmt:'yyyy-MM-dd'})"
							style="width: 50%" /></li>
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
			<gh:resAuth parentCode="RES_PERSON_SYSMSG" pageType="${type },list" />
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_messages"
							onclick="checkboxAll('#check_all_messages','resourceid','#messagesBody')" /></th>
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
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="messagesBody">
					<c:choose>
						<c:when test="${type=='inbox' }">
							<c:forEach items="${messageReceiverUserPage.result}" var="mru"
								varStatus="s">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${mru.messageReceiver.message.resourceid }"
										autocomplete="off" /></td>
									<td>${ghfn:dictCode2Val('CodeMsgType',mru.messageReceiver.message.msgType) }
									</td>
									<td>${mru.messageReceiver.message.msgTitle }</td>
									<td>${mru.messageReceiver.message.senderName }</td>
									<td><fmt:formatDate
											value="${mru.messageReceiver.message.sendTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td style="${mru.readStatus=='unRead'?'red':''}">${ghfn:dictCode2Val('CodeReadStatus',mru.readStatus) }</td>
									<td>${ghfn:dictCode2Val('yesOrNo',mru.messageReceiver.message.isReply) }</td>
									<td><a target="dialog"
										href="${baseUrl }/edu3/framework/message/show.html?msgId=${mru.messageReceiver.message.resourceid }&type=${type}"
										title="查看消息" width="800" height="600" rel="view_message">查看</a>
										&nbsp;&nbsp; <c:if
											test="${(type eq 'inbox')and(mru.messageReceiver.message.isReply eq 'Y') }">
											<a target="navTab"
												href="${baseUrl }/edu3/portal/message/input.html?parentId=${mru.messageReceiver.message.resourceid }"
												rel="RES_PERSON_SYSMSG_INPUT" title="回复消息">回复</a>
										</c:if></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach items="${messagePage.result}" var="message"
								varStatus="s">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${message.resourceid }" autocomplete="off" /></td>
									<td>${ghfn:dictCode2Val('CodeMsgType',message.msgType) }</td>
									<td>${message.msgTitle }</td>
									<td>${message.senderName }</td>
									<td><fmt:formatDate value="${message.sendTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${ghfn:dictCode2Val('yesOrNo',message.isReply) }</td>
									<td><a target="dialog"
										href="${baseUrl }/edu3/framework/message/show.html?msgId=${message.resourceid }&type=${type}"
										title="查看消息" width="800" height="600" rel="view_message">查看</a>
										&nbsp;&nbsp; <c:if
											test="${(type eq 'inbox')and(message.isReply eq 'Y') }">
											<a target="navTab"
												href="${baseUrl }/edu3/portal/message/input.html?parentId=${message.resourceid }"
												rel="RES_PERSON_SYSMSG_INPUT" title="回复消息">回复</a>
										</c:if></td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<c:choose>
				<c:when test="${type=='inbox' }">
					<gh:page page="${messageReceiverUserPage}"
						goPageUrl="${baseUrl }/edu3/portal/message/list.html?type=${type}"
						condition="${condition }" pageType="sys" />
				</c:when>
				<c:otherwise>
					<gh:page page="${messagePage}"
						goPageUrl="${baseUrl }/edu3/portal/message/list.html?type=${type}"
						condition="${condition }" pageType="sys" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>