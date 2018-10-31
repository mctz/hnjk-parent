<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程论坛回复帖管理</title>
<style type="text/css">
#bbsTopic_content p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
	line-height: 1.2em;
}
</style>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'replyContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=replyContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	
		      afterCreate : function(id) {
				KE.util.focus(id);
			}          
	  		});	
	  		
    	});    	
    	
	</script>
</head>
<body>
	<h2 class="contentTitle">回复帖</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsSectionForm" method="post"
				action="${baseUrl}/edu3/metares/topicreply/bbsreply/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsReply.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>所属帖子:</td>
							<td colspan="3"><input type="text" style="width: 500px"
								value="${bbsReply.bbsTopic.title }" readonly="readonly" /> <input
								type="hidden" name="bbsTopicId"
								value="${bbsReply.bbsTopic.resourceid }" /></td>
						</tr>
						<tr>
							<td>帖子内容:</td>
							<td colspan="3">
								<div id="bbsTopic_content" style="width: 700px;">${bbsReply.bbsTopic.content }</div>
							</td>
						</tr>
						<tr>
							<td>回复内容:</td>
							<td colspan="3"><textarea id="replyContent"
									name="replyContent" rows="5" cols=""
									style="width: 700px; height: 400px; visibility: hidden;">${bbsReply.replyContent }</textarea>
							</td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3"><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${bbsReply.attachs}"
									fileExt="zip|rar" /> <font color="green">(单个文件上传大小不能大于10M)</font>
							</td>
						</tr>
						<c:if
							test="${bbsReply.bbsTopic.bbsSection.sectionCode eq sectioncode }">
							<tr>
								<td>是否标记为FAQ:</td>
								<td colspan="3"><input type="checkbox" name="tags"
									value="FAQ"
									<c:if test="${fn:contains(bbsReply.bbsTopic.tags,'FAQ') }">checked="checked"</c:if> />标记为FAQ
								</td>
							</tr>
						</c:if>
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
 	 function loadEditor(){
	     KE.create('replyContent'); 		    
     } 	        
     window.setTimeout(loadEditor,1000);
</script>
</body>
</html>