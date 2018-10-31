<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文章管理</title>
<style>
#nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<script type="text/javascript">  
	$(function() {
		KE.init({//初始化编辑器
	      id : 'content',
	      skinsPath:'${baseUrl}/jscript/editor/skins/',	     
	      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=content&storePath=portal,images,${storeDir}',//textAreaId:为编辑器ID 一定要跟storePath参数，指定文件存储的位置	       
	      allowFileManager:true,
	      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
	      rootPath:'portal,images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f
	      afterCreate : function(id) {
				KE.util.focus(id);
			}	      
  		});
    });
    
    function selectPic(){//选择封面图片
    //TODO
    }
    		  
   //删除附件   
   /*
   function removeAttach(artid,attid){  
   	alertMsg.confirm("确定要删除这个附件？", {
		okCall: function(){
			$.post(baseUrl+"/edu3/framework/portal/manage/article/deleteattach.html",{attachid:attid,articleid:artid},function(data){
				navTabAjaxDone(data);
			})			
		}
	});   		
   }
  */
   function selectFile(type){
	  // $.pdialog.open('${baseUrl }/edu3/portal/message/user.html?usersN=messageUserNames&namesN=messageUserCnNames','selector','选择用户',{mask:true,width:800,height:500});
   }
   
   //切换
   function switchDisplayDiv(evenHtmlId,areaHtmlId){   
   		if($(evenHtmlId).attr("checked")){
   			$(areaHtmlId).css({ display:"block" });
   		}else{
   			$(areaHtmlId).css({display:"none"});
   		}   
   }
</script>

	<h2 class="contentTitle">${(empty article.resourceid)?'新增':'编辑'}资料</h2>

	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/article/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${article.resourceid }" />
						<input type="hidden" id="isDraft" name="isDraft" value="N" />
						<input type="hidden" id="channelId" name="channelId"
							value="${article.channel.resourceid }" />
						<input type="hidden" id="artitype" name="artitype"
							value=${(empty article.artitype)?'resources':article.artitype} />
						<tr>
							<td style="width: 12%">资料标题:</td>
							<td style="width: 88%"><input type="text" name="title"
								size="120" maxlength="50" value="${article.title }"
								class="required" /></td>
						</tr>
						<%-- 
	<tr>
		<td style="width: 12%">栏目:</td>
		<td style="width: 38%"><gh:channel channelList="${channelList }"
			viewType="select" defaultValue="${article.channel.resourceid }" /> <font
			color="red">注意：请选择最底级的栏目。</font></td>
		<td style="width: 12%">文章类型：</td>
		<td style="width: 38%"><gh:select name="artitype"
			value="${article.artitype}" dictionaryCode="artitype"
			style="width:50%" /></td>
	</tr>
	<tr>
		<td>文章摘要:</td>
		<td colspan="3"><input type="text" name="summary" size="60"
			value="${article.summary }" /></td>
	</tr>
	<tr>
		<td>关键字:</td>
		<td colspan="3"><input type="text" name="tags" size="60"
			value="${article.tags }" class="required" alt="多个关键字使用,号分隔" /></td>
	</tr>
	<tr>
		<td>来源:</td>
		<td><input type="text" name="source" size="40"
			value="${article.source }" class="required" /></td>
		<td>选项：</td>
		<td><input type="checkbox" name="isDraft" value="Y"
			<c:if test="${article.isDraft eq 'Y' }"> checked</c:if> /> 保存为草稿 <input
			type="checkbox" name="topLevel" value="1"
			<c:if test="${article.topLevel == 1 }"> checked</c:if> /> 置顶</td>

	</tr>
	--%>
						<tr id="nostyle">
							<td>资料内容:</td>
							<td colspan="3"><textarea id="content" name="content"
									class="required"
									style="width: 700px; height: 300px; visibility: hidden;">${article.content}</textarea>
							</td>
						</tr>
						<%--
	<tr>
		<td>设置新闻图片封面:</td>
		<td colspan="3">
		<gh:upload hiddenInputName="imgPath" 
				uploadType="image" 
				baseStorePath="portal,images" 
				extendStorePath="${storeDir}" 
				fileSize="1048576" 
				defaultImagePath="${article.imgPath }"/>
		
		</td>
	</tr>
	--%>
						<tr>
							<td>添加附件:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="portal,attachs"
									extendStorePath="${storeDir}" attachList="${attachList }" /></td>
						</tr>

					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<input type="hidden" name="fillinMan"
										value="${article.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${article.fillinManId }" /> <input
										type="hidden" name="fillinDate"
										value="<fmt:formatDate value="${article.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
									<input type="hidden" name="auditMan"
										value="${article.auditMan }" /> <input type="hidden"
										name="auditManId" value="${article.auditManId }" /> <input
										type="hidden" name="auditStatus"
										value="${article.auditStatus }" /> <input type="hidden"
										name="auditDate"
										value="<fmt:formatDate value="${article.auditDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
									<input type="hidden" name="orgUnitId"
										value="${article.orgUnit.resourceid }" />
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
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