<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交流区设置</title>
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
      id : 'content',     
      items : [
				'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons',  'link'],

	      afterCreate : function(id) {
				KE.util.focus(id);
			}	      
	});
	
	$("#uploadify_gMsgFile").uploadify({
        'script'         : "${baseUrl}/edu3/filemanage/upload.html?storePath=users,${storeDir},gruates", //上传URL
        'auto'           : true, //自动上传
        'multi'          : false, //多文件上传
        'fileDesc'       : '支持格式:zip/rar',  //限制文件上传格式描述
        'fileExt'        : '*.ZIP;*.RAR;', //限制文件上传的类型,必须有fileDesc这个性质
        'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
            $("#gMsgFile").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
			$("#hideGMsgFile").append("<input type='hidden' id='uploadfileid' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
		},
		onError: function(event, queueID, fileObj) {  //上传失败回调函数
			alert("文件:" + fileObj.name + "上传失败"); 
			$('#uploadify_gMsgFile').uploadifyClearQueue(); //清空上传队列
		}
    });
});

function _gonextsetp(id){
	alertMsg.confirm("确定要进入下一环节？", {
		okCall: function(){
			$.post("${baseUrl}/edu3/teaching/graduateMsg/nextTache.html",{resourceid:id,fromPage:'edit'}, navTabAjaxDone, "json");				
		}
	});   		
}

	//附件下载
function downloadAttachFile(attid){
	$('#frameForDownload').remove();
	var elemIF = document.createElement("iframe");
	elemIF.id = "frameForDownload"; //创建id
	elemIF.src = "${baseUrl}/edu3/framework/filemanage/download.html?id="+attid;
	elemIF.style.display = "none";
	document.body.appendChild(elemIF); 
}
	   
//附件删除
function deleteAttachFile(attid){
	alertMsg.confirm("确定要删除这个附件？", {
		okCall: function(){
			var url = "${baseUrl}/edu3/framework/filemanage/delete.html"; 
			$.get(url,{fileid:attid},function(data){
				$("li[id='"+attid+"']").remove();
				$("#uploadfileid[value='"+attid+"']").remove();
			});					
		}
	});   		
	
	
}

//修改留言
function _modifyGraduateMsg(id){
	var url = "${baseUrl}/edu3/teaching/graduateMsg/edit.html";
	navTab.openTab('RES_TEACHING_THESIS_MSG_VIEW', url+'?resourceid='+$('#_grauateMsgForm #paperOrderId').val()+'&msgid='+id, '毕业论文交流区');
}

//删除留言
function _deleteGraduateMsg(id){
	alertMsg.confirm("确定要删除留言？", {
		okCall: function(){
			var paperOrderId = $('#_grauateMsgForm #paperOrderId').val();
			$.post("${baseUrl}/edu3/teaching/graduateMsg/delete.html",{resourceid:id,paperOrderId:paperOrderId}, navTabAjaxDone, "json");				
		}
	});   		
}

</script>
	<h2 class="contentTitle">毕业论文交流区 -
		${graduate.graduatePapersOrder.course.courseName } -
		${ghfn:dictCode2Val('CodeCurrentTache',graduate.currentTache ) }</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduateMsg/save.html"
				id="_grauateMsgForm" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" id="msgId" name="resourceid"
					value="${graduate.resourceid }" /> <input type="hidden"
					name="fillinManId" value="${graduate.fillinManId }" /> <input
					type="hidden" name="fillinMan" value="${graduate.fillinMan }" /> <input
					type="hidden" name="currentTache" value="${graduate.currentTache }" />
				<input type="hidden" name="sendTime"
					value="<fmt:formatDate value="${graduate.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<input type="hidden" id="paperOrderId" name="paperOrderId"
					value="${graduate.graduatePapersOrder.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs"
						<c:if test="${not empty tabIndex }">currentIndex=${ tabIndex}</c:if>
						style="width: 98%;">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>开题报告</span></a></li>
									<li class="#"><a href="#"><span>初稿</span></a></li>
									<li class="#"><a href="#"><span>定稿</span></a></li>
								</ul>
							</div>
						</div>

						<div class="tabsContent" style="height: 100%;">
							<!-- 1 -->
							<div>
								<font color="red"><b>开题报告 截止到:<fmt:formatDate
											value="${graduate.graduatePapersOrder.examSub.gradendDate.syllabusEndDate }"
											pattern="yyyy-MM-dd" /></b></font>
								<table class="list" width="100%">
									<c:forEach items="${ListMsg}" var="m1" varStatus="vs">
										<c:if test="${m1.currentTache eq '1'}">
											<tr>
												<td width="20%">${m1.fillinMan}<br />发表于：<fmt:formatDate
														value="${m1.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td width="80%">
													<div class="list_content" id="content_${m1.resourceid }">${m1.content }</div>
													<c:if test="${not empty m1.attachs}">
														<div class="list_attachs">
															<c:forEach items="${m1.attachs }" var="f1" varStatus="vs">
																<li id="${f1.resourceid }"><img
																	style="cursor: pointer; height: 10px;"
																	src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
																	<a onclick="downloadAttachFile('${f1.resourceid }')"
																	href="#">${f1.attName }&nbsp;</a>&nbsp;&nbsp; <c:if
																		test="${userId eq m1.fillinManId}">
																		<img style="cursor: pointer; height: 10px;"
																			onclick="deleteAttachFile('${f1.resourceid }');"
																			src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
																	</c:if></li>
															</c:forEach>
													</c:if>
													</div> <c:if test="${userId eq m1.fillinManId}">
														<div id="_opration">
															<span class="tips"><a href="##"
																onclick="_modifyGraduateMsg('${m1.resourceid }')">修改</a></span>&nbsp;&nbsp;
															<span class="tips"><a href="##"
																onclick="_deleteGraduateMsg('${m1.resourceid }')">删除</a></span>
														</div>
													</c:if>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</table>
							</div>
							<!-- 2 -->
							<div>
								<font color="red"><b>初稿 截止到:<fmt:formatDate
											value="${graduate.graduatePapersOrder.examSub.gradendDate.firstDraftEndDate }"
											pattern="yyyy-MM-dd" /></b></font>
								<table class="list" width="100%">
									<c:forEach items="${ListMsg}" var="m2" varStatus="vs">
										<c:if test="${m2.currentTache eq '2'}">
											<tr>
												<td width="20%">${m2.fillinMan}<br />发表于：<fmt:formatDate
														value="${m2.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td width="80%">
													<div class="list_content" id="content_${m2.resourceid }">${m2.content }</div>
													<c:if test="${not empty m2.attachs}">
														<div class="list_attachs">
															<c:forEach items="${m2.attachs }" var="f2" varStatus="vs">
																<li id="${f2.resourceid }"><img
																	style="cursor: pointer; height: 10px;"
																	src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
																	<a onclick="downloadAttachFile('${f2.resourceid }')"
																	href="#">${f2.attName }&nbsp;</a>&nbsp;&nbsp; <c:if
																		test="${userId eq m2.fillinManId}">
																		<img style="cursor: pointer; height: 10px;"
																			onclick="deleteAttachFile('${f2.resourceid }');"
																			src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
																	</c:if></li>
															</c:forEach>
													</c:if>
													</div> <c:if test="${userId eq m2.fillinManId}">
														<div id="_opration">
															<span class="tips"><a href="##"
																onclick="_modifyGraduateMsg('${m2.resourceid }')">修改</a></span>&nbsp;&nbsp;
															<span class="tips"><a href="##"
																onclick="_deleteGraduateMsg('${m2.resourceid }')">删除</a></span>
														</div>
													</c:if>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</table>
							</div>
							<!-- 3 -->
							<div>
								<font color="red"><b>定稿 截止到:<fmt:formatDate
											value="${graduate.graduatePapersOrder.examSub.gradendDate.secondDraftEndDate }"
											pattern="yyyy-MM-dd" /></b></font>
								<table class="list" width="100%">
									<c:forEach items="${ListMsg}" var="m3" varStatus="vs">
										<c:if test="${m3.currentTache eq '3'}">
											<tr>
												<td width="20%">${m3.fillinMan}<br />发表于：<fmt:formatDate
														value="${m3.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td width="80%">
													<div class="list_content" id="content_${m3.resourceid }">${m3.content }</div>
													<c:if test="${not empty m3.attachs}">
														<div class="list_attachs">
															<c:forEach items="${m3.attachs }" var="f3" varStatus="vs">
																<li id="${f3.resourceid }"><img
																	style="cursor: pointer; height: 10px;"
																	src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
																	<a onclick="downloadAttachFile('${f3.resourceid }')"
																	href="#">${f3.attName }&nbsp;</a>&nbsp;&nbsp; <c:if
																		test="${userId eq m3.fillinManId}">
																		<img style="cursor: pointer; height: 10px;"
																			onclick="deleteAttachFile('${f3.resourceid }');"
																			src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
																	</c:if></li>
															</c:forEach>
													</c:if>
													</div> <c:if test="${userId eq m3.fillinManId}">
														<div id="_opration">
															<span class="tips"><a href="##"
																onclick="_modifyGraduateMsg('${m3.resourceid }')">修改</a></span>&nbsp;&nbsp;
															<span class="tips"><a href="##"
																onclick="_deleteGraduateMsg('${m3.resourceid }')">删除</a></span>
														</div>
													</c:if>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</table>
							</div>

						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
					<div style="width: 98%;">
						<table class="form" style="width: 99%;">
							<tr id="nostyle">
								<td width="15%">交流内容:</td>
								<td><textarea name="content" id="content"
										style="width: 80%; height: 200px; visibility: hidden;"
										class="required">${graduate.content }</textarea></td>
							</tr>
							<tr>
								<td>附件:</td>
								<td><span class="tips">*单个文件上传大小不能大于10M</span><br /> <input
									type="file" name="uploadify_gMsgFile" id="uploadify_gMsgFile" />
									<div id="gMsgFile">
										<c:forEach items="${filelist}" var="attach" varStatus="vs">
											<li id="${attach.resourceid }"><img
												style="cursor: pointer; height: 10px;"
												src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;<a
												onclick="downloadAttachFile('${attach.resourceid }')"
												href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;<img
												style="cursor: pointer; height: 10px;"
												onclick="deleteAttachFile('${attach.resourceid }');"
												src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
											</li>
										</c:forEach>
									</div>
									<div id="hideGMsgFile" style="display: none"></div></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<c:if test="${graduate.currentTache != '3' }">
							<security:authorize
								ifAnyGranted="ROLE_TEACHER,ROLE_TEACHER_DUTY,ROLE_TEACHER_COACH">
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="button"
												onclick="_gonextsetp('${graduate.graduatePapersOrder.resourceid }')">进入下一环节</button>
										</div>
									</div></li>
							</security:authorize>
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
</body>
<script type="text/javascript">
	function createKE(){
		KE.create('content');
	}
	setTimeout(createKE,1000);

</script>
</html>