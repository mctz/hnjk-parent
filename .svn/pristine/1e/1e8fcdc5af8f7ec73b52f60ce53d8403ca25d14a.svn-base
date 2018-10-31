<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线帮助文档管理</title>
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
   
   //切换
   function switchDisplayDiv(evenHtmlId,areaHtmlId){   
   		if($(evenHtmlId).attr("checked")){
   			$(areaHtmlId).css({ display:"block" });
   		}else{
   			$(areaHtmlId).css({display:"none"});
   		}   
   }
</script>

	<h2 class="contentTitle">
		<c:if test="${method=='add'}">新增在线帮助文章</c:if>
		<c:if test="${method=='edit'}">编辑在线帮助文章</c:if>
	</h2>

	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/helper/article/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${helpArticle.resourceid }" />
						<tr>
							<td style="width: 12%">帮助文档标题:</td>
							<td colspan="3" style="width: 88%"><input type="text"
								name="title" size="120" value="${helpArticle.title }"
								class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">所属栏目:</td>
							<td colspan="3"><c:if test="${method=='edit'}">
									<select name="channelId">
										<c:forEach items="${allChanList}" var="hC">
											<option value="${hC.resourceid}"
												<c:if test="${hC.resourceid==helpArticle.channel.resourceid}">selected="selected"</c:if>>
												<c:forEach begin="1" end="${hC.channelLevel}">
				            	&nbsp;&nbsp;&nbsp;
				            	</c:forEach>${hC.channelName}
											</option>
										</c:forEach>
									</select>
									<font color="red">注意: 编辑状态下暂时不能修改文章的栏目。</font>
								</c:if> <c:if test="${method=='add'}">
									<select name="channelId">
										<c:forEach items="${allChanList}" var="hC">
											<option value="${hC.resourceid}"
												<c:if test="${hC.resourceid==helpArticle.channel.resourceid}">selected="selected"</c:if>>
												<c:forEach begin="1" end="${hC.channelLevel}">
				            	&nbsp;&nbsp;&nbsp;
				            	</c:forEach>${hC.channelName}
											</option>
										</c:forEach>
									</select>
									<font color="red">注意：默认情况为您最近一次选择的栏目。</font>
								</c:if>
						</tr>
						<tr>
							<td>关键字:</td>
							<td colspan="3"><input type="text" name="tags" size="60"
								value="${helpArticle.tags }" alt="多个关键字使用,号分隔" /></td>
						</tr>
						<tr>
							<td style="width: 20%">排序:</td>
							<td><input type="text" name="showOrder"
								value="${helpArticle.showOrder }" size="6" /> <span
								style="padding: 4px; color: red">数字越小越靠前</span></td>
						</tr>
						<tr id="nostyle">
							<td>内容:</td>
							<td colspan="3"><textarea id="content" name="content"
									class="required"
									style="width: 700px; height: 300px; visibility: hidden;">${helpArticle.content}</textarea>
							</td>
						</tr>
						<tr>
							<td>添加附件:</td>
							<td colspan="3"><gh:upload hiddenInputName="attachId"
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
										value="${helpArticle.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${helpArticle.fillinManId }" /> <input
										type="hidden" name="fillinDate"
										value="<fmt:formatDate value="${helpArticle.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
									<input type="hidden" name="method" value="${method}" />
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