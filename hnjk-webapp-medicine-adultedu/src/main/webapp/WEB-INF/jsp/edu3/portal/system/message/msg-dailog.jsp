<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<style>
#nostyle td {
	height: 6px
}
</style>

<body>
	<script type="text/javascript">	
	$(document).ready(function(){	
		KE.init({//初始化编辑器
	      id : 'messagecontentdialog',
	      skinsPath:'${baseUrl}/jscript/editor/skins/',	     
	      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=messagecontentdialog&storePath=users,${storeDir},images',       
	      allowFileManager:true,
	      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
	      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f
	      afterCreate : function(id) {
				KE.util.focus(id);
			}	      
  		}); 		
		
	});
	
</script>
	<h2 class="contentTitle">发送消息</h2>
	<div class="page">
		<div class="pageContent">
			<form
				action="${baseUrl }/edu3/portal/message/saveMsg.html?from=dailog"
				class="pageForm" method="post"
				onsubmit="return validateCallback(this,dialogAjaxDone);">
				<input type=hidden name="resourceid" value="${message.resourceid }" />
				<input type=hidden name="parentId"
					value="${message.parent.resourceid }" /> <input type="hidden"
					name="receiveType" value="user" /> <input type="hidden"
					name="msgType" value="${message.msgType }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="10%">标题:</td>
							<td><input type=text name="msgTitle"
								value="${message.msgTitle }<c:if test='${not empty message.parent }'>Re:${message.parent.msgTitle }</c:if>"
								style="width: 70%" class="required"></td>
						</tr>
						<tr>
							<td width="10%">接收者:</td>
							<td>
								<div id="userDivDialog">
									<input type="hidden" id="messageUserNamesD" name="userNames"
										value="${messageReceiver.userNames}" /> <input type="text"
										id="messageUserCnNamesD" readonly="true"
										value="${messageReceiver.userCnNames}" style="width: 80%;" />&nbsp;&nbsp;
									<c:if test="${not empty unitId }">
										<a href="#" class="button"
											onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/user.html?usersN=messageUserNamesD&namesN=messageUserCnNamesD&sendto=unit&unitId=${unitId }&userType=${userType }','selectorUsers','选择用户',{mask:true,width:750,height:500});"><span>选择用户</span></a>
									</c:if>
								</div>
							</td>
						</tr>
						<tr>
							<td width="10%">内容:</td>
							<td id="nostyle"><textarea id="messagecontentdialog"
									name="content" style="width: 99%; height: 250px;"
									class="required">${message.content }</textarea></td>
						</tr>
						<tr>
							<td>附件:</td>
							<td><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${message.attachs}"
									fileExt="zip|rar" /> <font color="green">(单个文件上传大小不能大于10M)</font><br />
								<%--
							<input type="file" name="uploadify" id="uploadify_msg_dialog"/>
							<div id="uploadifyQueue_msg_dialog" class="uploadifyQueue">
								<c:forEach items="${message.attachs}" var="attach" varStatus="vs">								
									<li id="${attach.resourceid }">
										<img style="cursor: pointer; height: 10px;" src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
										<a onclick="downloadAttachFile('${attach.resourceid }')" href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
										<img style="cursor: pointer; height: 10px;" onclick="deleteAttachFile('${attach.resourceid }');" src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
									</li>
								</c:forEach>
							</div>		
							<div id="hideFileId_msg_dialog" style="display:none">
								<c:forEach items="${message.attachs}" var="attach" varStatus="vs">	
									<input type="hidden" value="${attach.resourceid }" name="uploadfileid" id="uploadfileid_msg_dialog">
								</c:forEach>							
							</div>
							 --%></td>
						</tr>

					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit" onclick="return checkValidateForm();">发送</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
<!--
function createKE(){
	KE.create('messagecontentdialog');
}
setTimeout(createKE,1000); 
function checkValidateForm(){
	var receiver = $("#userDivDialog input[readonly='true']");	
	if(receiver.val()==""){
		alertMsg.warn("请指定消息接收者！");
		return false;
	}	
	return true;
}
//-->
</script>
</body>