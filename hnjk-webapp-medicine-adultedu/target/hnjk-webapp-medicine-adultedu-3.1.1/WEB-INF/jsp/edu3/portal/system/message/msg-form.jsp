<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<style>
#nostyle td {
	height: 4px
}
</style>

<body>
	<script type="text/javascript">	
	$(document).ready(function(){	
		KE.init({//初始化编辑器
	      id : 'messagecontent',
	      skinsPath:'${baseUrl}/jscript/editor/skins/',	     
	      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=messagecontent&storePath=users,${storeDir},images',       
	      allowFileManager:true,
	      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
	      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f
	      afterCreate : function(id) {
				KE.util.focus(id);
			}	      
  		}); 			
	});
	
	
		function msgTypeChange(msgType){
			$("#isReplySpan").css("display",(msgType=='usermsg')?"block":"none");
			$("#isReplySpan select[name='isReply']").val((msgType=='usermsg')?"Y":"N");
			$("#toTimeSpan").css("display",(msgType=='tips')?"block":"none");
		}
		
		function sendModeChange(sendMode){
			$("#sendTimeSpan").css("display",(sendMode=='1')?"block":"none");
			$("#sendTimeSpan input[name='sendTime']").val("");
		}
		function receiveTypeChange(type){
			if(type==""){type="user";}
			$("#receiveTd div").hide();
			$("#"+type+"Div").show();		
		}
</script>
	<h2 class="contentTitle">
		<c:choose>
			<c:when test="${not empty message.parent }">回复</c:when>
			<c:otherwise>${(empty message.resourceid)?'新增':'编辑' }</c:otherwise>
		</c:choose>
		消息
	</h2>
	<div class="page">
		<div class="pageContent">
			<form action="${baseUrl }/edu3/portal/message/saveMsg.html"
				class="pageForm" method="post"
				onsubmit="return validateCallback(this);">
				<input type=hidden name="resourceid" value="${message.resourceid }" />
				<input type=hidden name="parentId"
					value="${message.parent.resourceid }" /> <input type=hidden
					id="msgForm_isDraft" name="isDraft" value="${message.isDraft }" />
				<input type=hidden name="fromtype" value="${fromtype }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>${(empty message.parent)?'消息':'回复' }标题:</td>
							<td colspan="3"><input type=text name="msgTitle"
								value="${message.msgTitle }<c:if test='${not empty message.parent }'>Re:${message.parent.msgTitle }</c:if>"
								style="width: 70%" class="required"></td>
						</tr>
						<tr>
							<td>接收类型</td>
							<td colspan="3"><c:choose>
									<c:when test="${(isBrschool eq true) or (not empty fromtype) }">
										<gh:select name="receiveType"
											onchange="receiveTypeChange(this.value);"
											value="${messageReceiver.receiveType}"
											dictionaryCode="CodeReceiveType" disabled="disabled"
											style="width: 120px;" />
										<input type="hidden" name="receiveType"
											value="${messageReceiver.receiveType}" />
									</c:when>
									<c:otherwise>
										<gh:select name="receiveType"
											onchange="receiveTypeChange(this.value);"
											value="${messageReceiver.receiveType}"
											dictionaryCode="CodeReceiveType" style="width: 120px;" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td>接收者:</td>
							<td colspan="3" id="receiveTd">
								<div id="userDiv"
									style="display: ${(messageReceiver.receiveType eq 'user')?'block':'none'}">
									<input type="hidden" id="messageUserNames" name="userNames"
										value="${messageReceiver.userNames}" /> <input type="text"
										id="messageUserCnNames" readonly="true"
										value="${messageReceiver.userCnNames}" style="width: 80%;" />&nbsp;&nbsp;
									<c:choose>
										<c:when test="${not empty fromtype }">
											<a href="#" class="button"
												onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/student.html?usersN=messageUserNames&namesN=messageUserCnNames','selector','选择学生',{mask:true,width:800,height:500});"><span>选择学生</span></a>
										</c:when>
										<c:otherwise>
											<a href="#" class="button"
												onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/user.html?usersN=messageUserNames&namesN=messageUserCnNames','selector','选择用户',{mask:true,width:800,height:500});"><span>选择用户</span></a>
										</c:otherwise>
									</c:choose>
								</div>
								<div id="roleDiv"
									style="display: ${(messageReceiver.receiveType eq 'role')?'block':'none'}">
									<input type="hidden" id="messageRoleCodes" name="roleCodes"
										value="${messageReceiver.roleCodes}" /> <input type="text"
										id="messageRoleNames" readonly="true"
										value="${messageReceiver.roleNames}" style="width: 80%;" />&nbsp;&nbsp;
									<a href="#" class="button"
										onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/role.html?codesN=messageRoleCodes&namesN=messageRoleNames','selector','选择角色',{mask:true,width:700,height:500});"><span>选择角色</span></a>
								</div>
								<div id="orgDiv"
									style="display: ${(messageReceiver.receiveType eq 'org')?'block':'none'}">
									<input type="hidden" id="messageOrgUnitCodes"
										name="orgUnitCodes" value="${messageReceiver.orgUnitCodes}" />
									<input type="text" id="messageOrgUnitNames" readonly="true"
										value="${messageReceiver.orgUnitNames}" style="width: 80%;" />&nbsp;&nbsp;
									<a href="#" class="button"
										onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/org.html?codesN=messageOrgUnitCodes&namesN=messageOrgUnitNames&checkedCodes='+$('#messageOrgUnitCodes').val(),'selector','选择组织',{mask:true,width:700,height:500});"><span>选择组织</span></a>
								</div>
								<div id="gradeDiv"
									style="display: ${(messageReceiver.receiveType eq 'grade')?'block':'none'}">
									<input type="hidden" id="grades" name="grades"
										value="${messageReceiver.grades}" /> <input type="text"
										id="gradeNames" readonly="true"
										value="${messageReceiver.gradeNames}" style="width: 80%;" />&nbsp;&nbsp;
									<a href="#" class="button"
										onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/grade.html?codesN=grades&namesN=gradeNames&checkedCodes='+$('#grades').val(),'selector','选择年级',{mask:true,width:700,height:500});"><span>选择年级</span></a>
								</div>
								<div id="classesDiv"
									style="display: ${(messageReceiver.receiveType eq 'classes')?'block':'none'}">
									<input type="hidden" id="classes" name="classes"
										value="${messageReceiver.classes}" /> <input type="text"
										id="classesNames" readonly="true"
										value="${messageReceiver.classesNames}" style="width: 80%;" />&nbsp;&nbsp;
									<a href="#" class="button"
										onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/classes.html?codesN=classes&namesN=classesNames&checkedCodes='+$('#classes').val(),'selector','选择班级',{mask:true,width:700,height:500});"><span>选择班级</span></a>
								</div>
							</td>
						</tr>
						<tr>
							<td width="10%">消息类型:</td>
							<td width="40%">
								<ul>
									<li style="float: left;"><gh:select name="msgType"
											onchange="msgTypeChange(this.value);"
											value="${message.msgType}" dictionaryCode="CodeMsgType"
											disabled="${(not empty message.parent)?'disabled':'' }"
											classCss="required" /> <span style="color: red">*</span> <c:if
											test="${not empty message.parent }">
											<input type="hidden" name="msgType"
												value="${message.msgType}" />
										</c:if></li>
									<li style="float: left;"><span id="isReplySpan"
										style="display: ${(message.msgType eq 'usermsg')?'block':'none'};">
											<label style="width: 100px;">是否允许回复：</label>
										<gh:select name="isReply" value="${message.isReply}"
												dictionaryCode="yesOrNo" />
									</span></li>
									<li style="float: left;"><span id="toTimeSpan"
										style="display: ${(message.msgType eq 'tips')?'block':'none'};">
											<label style="width: 100px;">倒计时提醒:</label> <input type=text
											name="toTime"
											value="<fmt:formatDate value='${message.toTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
											class="Wdate"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-{%d}'})">
									</span></li>
								</ul>
							</td>
							<td width="10%">发送类型:</td>
							<td width="40%"><select name="sendMode"
								onchange="sendModeChange(this.value);">
									<option value="0">即时发送</option>
									<option value="1" ${(not empty sendMode)?'selected':''}>定时发送</option>
							</select> <span id="sendTimeSpan"
								style="display: ${(not empty sendMode)?'block':'none'};">
									&nbsp;&nbsp;<label style="width: 100px;">发送时间：</label><input
									type=text name="sendTime"
									value="<fmt:formatDate value='${message.sendTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
									class="Wdate"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-{%d}'})">
							</span></td>
						</tr>
						<tr id="nostyle">
							<td>${(empty message.parent)?'消息':'回复' }内容:</td>
							<td colspan="3"><textarea id="messagecontent" name="content"
									style="width: 99%; height: 250px;" class="required">${message.content }</textarea></td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3"><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${message.attachs}" />
								<font color="green">(单个文件上传大小不能大于10M)</font></td>
						</tr>
						<%-- 
					<tr>
						<td width="10%">附加发送选项:</td>
						<td width="90%" colspan="3">
							<!-- <input type=checkbox name="sendType" value="portal" ${fn:containsIgnoreCase(message.sendType,'portal')?'checked':'' }/>发送到门户&nbsp;&nbsp;-->
							<input type=checkbox name="sendType" value="email" ${fn:containsIgnoreCase(message.sendType,'email')?'checked':'' }/>发送到邮箱&nbsp;&nbsp;
							<input type=checkbox name="sendType" value="sms" ${fn:containsIgnoreCase(message.sendType,'sms')?'checked':'' }/>发送到手机
						</td>							
					</tr>
					--%>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<c:if
							test="${(empty message.resourceid)or(type eq 'draftbox')or(message.isDraft eq 'Y')or(type eq 'sendbox') }">
							<c:if
								test="${(type eq 'sendbox')or(type eq 'draftbox')or(message.isDraft eq 'Y') }">
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit"
												onclick="javascript:$('#msgForm_isDraft').val('Y');return true;">${((type eq 'draftbox')or(message.isDraft eq 'Y'))?'保存':'保存为草稿' }</button>
										</div>
									</div></li>
							</c:if>
							<c:if
								test="${(empty message.resourceid)or((type eq 'draftbox')and(message.isDraft ne 'Y'))or(message.isDraft eq 'Y') }">
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit"
												onclick="$('#msgForm_isDraft').val('N');navTab.closeCurrentTab();">${(empty message.parent)?'发送':'回复' }</button>
										</div>
									</div></li>
							</c:if>
						</c:if>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
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
	KE.create('messagecontent');
}
setTimeout(createKE,1000); 

function checkValidateForm(){
	var receiveType = $("#receiveType");
	if(receiveType.val()==""){
		alertMsg.warn("请指定消息接收类型！");
		return false;
	}
	var receiver = $("#"+receiveType.val()+"Div input[readonly='true']");	
	if(receiver.val()==""){
		alertMsg.warn("请指定消息接收者！");
		return false;
	}	
	$('input[name=isDraft]').val('N');
	return true;
}
//-->
</script>
</body>