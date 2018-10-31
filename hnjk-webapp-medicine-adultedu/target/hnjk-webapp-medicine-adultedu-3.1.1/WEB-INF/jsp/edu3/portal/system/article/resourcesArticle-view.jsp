<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源文章查看</title>
<style>
#nostyle td {
	height: 4px
}
</style>
</head>
<body>
	<script type="text/javascript">  
/*
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
    */
    
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
   
    //提示登录后才能下载
	function showmsg(){
		alertMsg.warn("请先登录后下载！");
	}
    
	function downloadArticleFile(attachId){
		var url ="${baseUrl }/portal/site/article/download.html?id="+attachId;
		 downloadFileByIframe(url, "downloadArticleFile");
	}
</script>

	<h2 class="contentTitle">资料信息</h2>

	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/portal/manage/article/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${article.resourceid }" />
						<tr>
							<td style="width: 12%">资料标题:</td>
							<td style="width: 88%"><input type="text"
								value="${article.title }" readonly="readonly" style="width: 80%" /></td>
						</tr>
						<tr id="nostyle">
							<td>资料内容:</td>
							<td><textarea id="content" name="content"
									readOnly="readOnly" style="width: 600px; height: 350px;">${article.content}</textarea>
							</td>
						</tr>
						<tr>
							<td>附件:</td>
							<td><c:choose>
									<c:when test="${not empty attachList and not empty curUser}">
										<c:forEach items="${attachList }" var="attach" varStatus="vs">
		      	附件${vs.index+1}(点击下载)： <a onclick="downloadArticleFile('${attach.resourceid}')"
												href="##"
												style="color: blue;">${attach.attName}</a>
											<br />
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach items="${attachList }" var="attach" varStatus="vs">
			          附件${vs.index+1}(点击下载)： <a
												href="${basePath}/portal/project/register-page.html?tableIndex=0"
												style="color: blue;" onclick="showmsg();">${attach.attName}</a>
											<br />
										</c:forEach>
									</c:otherwise>
								</c:choose></td>
						</tr>

					</table>
				</div>
				<div class="formBar">
					<ul>
						<%--
	<li>
	<div class="buttonActive">
	<div class="buttonContent"><input type="hidden" name="fillinMan"
		value="${article.fillinMan }" /> <input type="hidden"
		name="fillinManId" value="${article.fillinManId }" /> <input
		type="hidden" name="fillinDate"
		value="<fmt:formatDate value="${article.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
	<input type="hidden" name="auditMan" value="${article.auditMan }" /> <input
		type="hidden" name="auditManId" value="${article.auditManId }" /> <input
		type="hidden" name="auditStatus" value="${article.auditStatus }" /> <input
		type="hidden" name="auditDate"
		value="<fmt:formatDate value="${article.auditDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
	<input type="hidden" name="orgUnitId"
		value="${article.orgUnit.resourceid }" />
<button type="submit">提交</button>
	</div>
	</div>
	</li>
	 --%>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.dialog.closeCurrentTab();">关闭</button>
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
/*
function createKE(){
	KE.create('content');
}
setTimeout(createKE,1000); 
*/
</script>
</html>