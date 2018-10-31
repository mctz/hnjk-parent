<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程学习目标管理</title>
<style>
#nostyle td {
	height: 8px
}
</style>
<script type="text/javascript">   
		$(function() {	  		
	  		KE.init({//初始化编辑器
		      id : 'courseLearningGuidContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=courseLearningGuidContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
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
	<h2 class="contentTitle">编辑学习目标</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/courselearningguid/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${courseLearningGuid.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>所属知识节点:</td>
							<td><input type="text" size="30" style="width: 50%"
								value="${courseLearningGuid.syllabus.syllabusName }"
								readonly="readonly" /> <input type="hidden" name="syllabusId"
								value="${courseLearningGuid.syllabus.resourceid }" /></td>
						</tr>
						<tr>
							<td>类型:</td>
							<td><gh:select name="type"
									value="${courseLearningGuid.type}"
									dictionaryCode="CodeCourseLearningGuidType" style="width:135px"
									classCss="required" /></td>
						</tr>
						<tr>
							<td>内容:</td>
							<td id="nostyle"><textarea name="content"
									id="courseLearningGuidContent" rows="5" cols=""
									style="width: 700px; height: 400px; visibility: hidden;"
									class="required">${courseLearningGuid.content }</textarea></td>
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
	 	 //创建编辑器
	 	 function loadEditor(){ 
		     KE.create('courseLearningGuidContent');
	     } 	     
	     window.setTimeout(loadEditor,3000);
	</script>
</body>
</html>