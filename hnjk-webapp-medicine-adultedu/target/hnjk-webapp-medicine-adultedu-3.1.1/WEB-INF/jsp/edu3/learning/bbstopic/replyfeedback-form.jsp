<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>回复学生反馈</title>
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
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'refeedbackContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=refeedbackContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	 
		       afterCreate : function(id) {
				KE.util.focus(id);
			}           
	  		});
	  		
    	});
    	
		function myFormCallbackFeedback1(json){		
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){				
				if ("closeCurrent" == json.callbackType) {
					setTimeout(function(){navTab.closeCurrentTab();}, 100);
				} 
				navTab.reload(json.reloadUrl,{},json.navTabId);
			}
		}
	</script>
<style>
#nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<h2 class="contentTitle">回复学生反馈: ${bbsTopic.title }</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsSectionForm" method="post"
				action="${baseUrl}/edu3/teacher/feedback/save.html" class="pageForm"
				onsubmit="return validateCallback(this,myFormCallbackFeedback1);">
				<input type="hidden" name="resourceid"
					value="${bbsTopic.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tbody>
							<tr>
								<td width="20%">
									<div style="color: #006699;">${bbsTopic.fillinMan }
										(反馈人)<br />
										<fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
										<br />
									</div>
								</td>
								<td>
									<div style="line-height: 26px;">${bbsTopic.content }</div>
								</td>
							</tr>
							<c:forEach items="${bbsTopic.bbsReplys }" var="bbsReply"
								varStatus="vs">
								<tr>
									<td style="line-height: 26px;"><div
											style="color: #006699;">${bbsReply.replyMan }<br />
											<fmt:formatDate value="${bbsReply.replyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
											<br />
										</div></td>
									<td>
										<div align="justify">${bbsReply.replyContent }</div>
									</td>
								</tr>
							</c:forEach>
							<tr id="nostyle">
								<td>回复:</td>
								<td colspan="3"><textarea id="refeedbackContent"
										name="replyContent" rows="5" cols=""
										style="width: 700px; height: 400px; visibility: hidden;"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
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
			</form>
		</div>
	</div>
	<script type="text/javascript">	 
 	 function loadEditorFeedback1(){
	     KE.create('refeedbackContent'); 		    
     } 	        
     window.setTimeout(loadEditorFeedback1,1000);
</script>
</body>
</html>