<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>留言列表</title>
<style>
#nostyle td {
	height: 4px
}

span {
	line-height: 170%;
}

.list_content {
	font-size: 10pt;
	text-align: left;
	line-height: 170%;
}

.list_content p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
}

.list_attachs {
	margin: 6px
}

.list tr {
	background: #fff
}
</style>
</head>
<body>
	<script type="text/javascript">
$(function() {
	KE.init({//初始化编辑器
      id : 'graduatemsgcontent',     
      items : [
				'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons',  'link'],

	      afterCreate : function(id) {
				KE.util.focus(id);
			}	      
	});
});
function downloadAttachFile(attid){
	downloadFileByIframe("${baseUrl}/edu3/framework/filemanage/download.html?id="+attid,"frameForDownload");
}
//修改留言
function _modifyGraduateMsg_group(id,pid){
	var url = "${baseUrl}/edu3/teaching/graduatemsg/group/input.html";
	navTab.openTab('RES_TEACHING_THESIS_MOOT_ADD', url+'?resourceid='+id+"&parentId="+pid, '毕业论文交流区');
}

//删除留言
function _deleteGraduateMsg_group(id,pid){
	alertMsg.confirm("确定要删除留言？", {
		okCall: function(){
			$.post("${baseUrl}/edu3/teaching/graduatemsg/group/remove.html",{resourceid:id,parentId:pid}, navTabAjaxDone, "json");				
		}
	});   		
}
</script>
	<div class="page">
		<div class="pageContent" layouth="0">
			<h2 class="contentTitle">${parentMsg.title}</h2>
			<table class="list" width="98%">
				<tbody>
					<c:if test="${not empty parentMsg.resourceid }">
						<tr>
							<td width="20%">${parentMsg.fillinMan}<br /> <c:choose>
									<c:when test="${parentMsg.isStudent eq 'Y' }">
										<c:if
											test="${not empty parentMsg.graduatePapersOrder.studentInfo.studentBaseInfo.mobile }">
					联系电话：${parentMsg.graduatePapersOrder.studentInfo.studentBaseInfo.mobile }<br />
										</c:if>
										<c:if
											test="${not empty parentMsg.graduatePapersOrder.studentInfo.studentBaseInfo.email }">
					邮箱：${parentMsg.graduatePapersOrder.studentInfo.studentBaseInfo.email }<br />
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if
											test="${not empty parentMsg.graduateMentor.edumanager.officeTel }">
					联系电话：${parentMsg.graduateMentor.edumanager.officeTel}<br />
										</c:if>
										<c:if
											test="${not empty parentMsg.graduateMentor.edumanager.email }">
					邮箱：${parentMsg.graduateMentor.edumanager.email }<br />
										</c:if>
									</c:otherwise>
								</c:choose> 发表于：<fmt:formatDate value="${parentMsg.sendTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td width="80%">
								<div class="list_content"
									id="graduatemsgcontent_${parentMsg.resourceid }">${parentMsg.content }</div>
								<c:if test="${not empty parentMsg.attachs}">
									<div class="list_attachs">
										<c:forEach items="${parentMsg.attachs }" var="attach"
											varStatus="vs">
											<li id="${attach.resourceid }"><img
												style="cursor: pointer; height: 10px;"
												src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
												<a onclick="downloadAttachFile('${attach.resourceid }')"
												href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
										</c:forEach>
								</c:if>
								</div> <c:if
									test="${parentMsg.isStudent eq 'N' and userId eq parentMsg.fillinManId}">
									<div>
										<span class="tips"><a href="javascript:;"
											onclick="_modifyGraduateMsg_group('${parentMsg.resourceid }','${parentMsg.resourceid }')">修改</a></span>&nbsp;&nbsp;
										<span class="tips"><a href="javascript:;"
											onclick="_deleteGraduateMsg_group('${parentMsg.resourceid }','')">删除</a></span>
									</div>
								</c:if>
							</td>
						</tr>
					</c:if>
					<c:forEach items="${msgList.result}" var="msg" varStatus="vs">
						<tr>
							<td width="20%">${msg.fillinMan}<br /> <c:choose>
									<c:when test="${msg.isStudent eq 'Y' }">
										<c:if
											test="${not empty msg.graduatePapersOrder.studentInfo.studentBaseInfo.mobile }">
						联系电话：${msg.graduatePapersOrder.studentInfo.studentBaseInfo.mobile }<br />
										</c:if>
										<c:if
											test="${not empty msg.graduatePapersOrder.studentInfo.studentBaseInfo.email }">
						邮箱：${msg.graduatePapersOrder.studentInfo.studentBaseInfo.email }<br />
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if
											test="${not empty msg.graduateMentor.edumanager.officeTel }">
						联系电话：${msg.graduateMentor.edumanager.officeTel}<br />
										</c:if>
										<c:if test="${not empty msg.graduateMentor.edumanager.email }">
						邮箱：${msg.graduateMentor.edumanager.email }<br />
										</c:if>
									</c:otherwise>
								</c:choose> 发表于：<fmt:formatDate value="${msg.sendTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td width="80%">
								<div class="list_content"
									id="graduatemsgcontent_${msg.resourceid }">${msg.content }</div>
								<c:if test="${not empty msg.attachs}">
									<div class="list_attachs">
										<c:forEach items="${msg.attachs }" var="attach" varStatus="vs">
											<li id="${attach.resourceid }"><img
												style="cursor: pointer; height: 10px;"
												src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
												<a onclick="downloadAttachFile('${attach.resourceid }')"
												href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
										</c:forEach>
								</c:if>
								</div> <c:if
									test="${msg.isStudent eq 'N' and userId eq msg.fillinManId}">
									<div>
										<span class="tips"><a href="javascript:;"
											onclick="_modifyGraduateMsg_group('${msg.resourceid }','${msg.parent.resourceid }')">修改</a></span>&nbsp;&nbsp;
										<span class="tips"><a href="javascript:;"
											onclick="_deleteGraduateMsg_group('${msg.resourceid }','${msg.parent.resourceid }')">删除</a></span>
									</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${msgList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduatemsg/group/input.html"
				pageType="sys" condition="${condition}" />

			<form method="post"
				action="${baseUrl}/edu3/teaching/graduatemsg/group/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduateMsg.resourceid }" /> <input type="hidden"
					name="parentId" value="${graduateMsg.parent.resourceid }" />
				<table class="form">
					<c:if
						test="${not isStudent and empty graduateMsg.parent.resourceid }">
						<tr>
							<td width="15%">论文批次:</td>
							<td><select name="batchId" size="1">
									<option value="">请选择...</option>
									<c:forEach items="${ examSubList}" var="examSub">
										<option value="${examSub.resourceid }"
											<c:if test="${examSub.resourceid eq  graduateMsg.graduateMentor.examSub.resourceid}">selected="selected"</c:if>>${examSub.batchName }</option>
									</c:forEach>
							</select><span style="color: red;">*</span></td>
						</tr>
					</c:if>
					<c:if test="${empty graduateMsg.parent.resourceid }">
						<tr>
							<td width="15%">标题:</td>
							<td><input type="text" name="title"
								value="${graduateMsg.title}" style="width: 80%;"
								class="required" /></td>
						</tr>
					</c:if>
					<tr id="nostyle">
						<td width="15%">交流内容:</td>
						<td><textarea name="content" id="graduatemsgcontent"
								style="width: 80%; height: 200px; visibility: hidden;"
								class="required">${graduateMsg.content }</textarea></td>
					</tr>
					<tr>
						<td>附件:</td>
						<td><span class="tips">*单个文件上传大小不能大于10M</span><br /> <gh:upload
								hiddenInputName="uploadfileid" uploadType="attach"
								baseStorePath="users,${storeDir},gruates" fileExt="zip|rar"
								attachList="${graduateMsg.attachs }"></gh:upload></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>
							<div class="formBar">
								<ul>
									<li><div class="buttonActive">
											<div class="buttonContent">
												<button type="submit">提交</button>
											</div>
										</div></li>
									<li><div class="button">
											<div class="buttonContent">
												<button type="button" class="close"
													onclick="navTab.closeCurrentTab();">取消</button>
											</div>
										</div></li>
								</ul>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	function createGraduateMsgKE(){
		KE.create('graduatemsgcontent');
	}
	setTimeout(createGraduateMsgKE,1000);

</script>
</html>