<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生反馈</title>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'feedbackContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=feedbackContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	 
		       afterCreate : function(id) {
				KE.util.focus(id);
			}           
	  		});
	  		
    	});
    	
		function myFormCallbackFeedback(json){		
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){				
				if(json.from == "interactive"){
					setTimeout(function(){$.pdialog.closeCurrent();}, 100);
				} else {
					setTimeout(function(){navTab.closeTab("RES_STUDENT_FEEDBACK_ADD");}, 100);
				}
				//navTab.reload(json.reloadUrl,{},json.navTabId);
				navTab.openTab(json.navTabId,json.reloadUrl,"问题反馈");
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
	<h2 class="contentTitle">学生反馈</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsSectionForm" method="post"
				action="${baseUrl}/edu3/student/feedback/save.html" class="pageForm"
				onsubmit="return validateCallback(this,myFormCallbackFeedback);">
				<input type="hidden" name="resourceid"
					value="${bbsTopic.resourceid }" /> <input type="hidden"
					name="topicType" value="6" /> <input type="hidden" name="courseId"
					value="${bbsTopic.course.resourceid }" /> <input type="hidden"
					name="from" value="${from }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">标题:</td>
							<td colspan="3"><input type="text" name="title"
								value="${bbsTopic.title }" class="required" style="width: 60%" />
							</td>
						</tr>
						<tr>
							<td>反馈类型</td>
							<td colspan="3"><gh:select dictionaryCode="CodeFacebookType"
									value="${bbsTopic.facebookType }" classCss="required"
									name="facebookType" style="width:125px;" /> <span
								style="color: red;">* (如果是课件学习方面的问题，请告知具体课程名称)</span></td>
						</tr>
						<tr>
							<td>反馈对象</td>
							<td colspan="3"><gh:select
									dictionaryCode="CodeFacebookTargetType"
									value="${bbsTopic.tags }" classCss="required"
									name="facebookTargetType" style="width:125px;" /> <span
								style="color: red;">* </span></td>
						</tr>
						<tr id="nostyle">
							<td>内容:</td>
							<td colspan="3"><textarea id="feedbackContent"
									name="content" rows="5" cols=""
									style="width: 600px; height: 300px; visibility: hidden;"
									class="required">${bbsTopic.content }</textarea></td>
						</tr>
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
 	 function loadEditorFeedback(){
	     KE.create('feedbackContent'); 		    
     } 	        
     window.setTimeout(loadEditorFeedback,1000);
</script>
</body>
</html>