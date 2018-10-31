<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程论坛帖子编辑</title>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'topicContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=topicContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	 
		       afterCreate : function(id) {
				KE.util.focus(id);
			}           
	  		});
	  		
    	});
    	
	</script>
<style>
#nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<h2 class="contentTitle">编辑帖子</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsSectionForm" method="post"
				action="${baseUrl}/edu3/metares/topicreply/bbstopic/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsTopic.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>所属版块:</td>
							<td colspan="3">
								<%--
						<input type="text" style="width: 280px" value="${bbsTopic.bbsSection.sectionName }" readonly="readonly"/> --%>
								<gh:bbsSectionSelect id="form_bbsSectionId" name="bbsSectionId"
									style="width: 40%;" value="${bbsTopic.bbsSection.resourceid }"
									classCss="required" /> <span style="color: red">*</span> <%-- 
						<select name="bbsSectionId" style="width: 40%;">
							<option value="" ${(empty bbsTopic.bbsSection.resourceid)?'selected':''}>
								选取所有版面
							</option>											
							<c:forEach items="${parentBbsSections}" var="item">
								<c:forEach items="${item.value}" var="section" varStatus="vs">
									<c:choose>
										<c:when test="${vs.first }">
											<option value="${section.resourceid }" ${(bbsTopic.bbsSection.resourceid==section.resourceid)?'selected':''}>
											╋${section.sectionName }
											</option>
										</c:when>
										<c:otherwise>
											<option value="${section.resourceid }" ${(bbsTopic.bbsSection.resourceid==section.resourceid)?'selected':''}>
												<c:forEach begin="1" end="${section.sectionLevel }" step="1">&nbsp;&nbsp;</c:forEach>
												├${section.sectionName }
											</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:forEach>		
						</select>
						 --%>
							</td>
						</tr>
						<tr>
							<td width="10%">帖子类型:</td>
							<td width="40%"><gh:select name="topicType"
									value="${bbsTopic.topicType}" dictionaryCode="CodeBbsTopicType" />
							</td>
							<td width="10%">帖子状态:</td>
							<td width="40%"><gh:select name="status"
									value="${bbsTopic.status}" dictionaryCode="CodeBbsTopicStatus" />
							</td>
						</tr>
						<tr>
							<td>标题:</td>
							<td colspan="3"><input type="text" name="title"
								value="${bbsTopic.title }" class="required" /></td>
						</tr>
						<tr id="nostyle">
							<td>内容:</td>
							<td colspan="3"><textarea id="topicContent" name="content"
									rows="5" cols=""
									style="width: 700px; height: 400px; visibility: hidden;">${bbsTopic.content }</textarea>
							</td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3"><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${bbsTopic.attachs}"
									fileExt="zip|rar" /> <font color="green">(单个文件上传大小不能大于10M)</font>
							</td>
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
 	 function loadEditor(){
	     KE.create('topicContent'); 		    
     } 	        
     window.setTimeout(loadEditor,1000);
</script>
</body>
</html>