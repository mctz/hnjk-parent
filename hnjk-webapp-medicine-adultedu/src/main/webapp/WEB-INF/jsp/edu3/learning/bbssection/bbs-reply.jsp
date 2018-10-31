<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>
<script type="text/javascript">    	
    	$(function() {
	  		KE.init({
		      id : 'newReplyTextarea',	
		      resizeMode : 0,  		             
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=newReplyTextarea&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',
		      afterCreate : function(id) {
		      		KE.util.focus(id);
					KE.event.ctrl(document, 13, function() {
						KE.util.setData(id);
						$("#newReplyForm").find("#bbsRepySubmit").click();
					});
					KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
						KE.util.setData(id);						
						$("#newReplyForm").find("#bbsRepySubmit").click();//按 Ctrl+Enter发布  
					});
				}         
	  		});	
	  		
	  		$("#uploadify_bbsreply").uploadify({ //初始化附件组件
                'script'         : baseUrl+'/edu3/filemanage/upload.html?storePath=users,${storeDir},attachs', //上传URL
                'auto'           : true, //自动上传
                'multi'          : true, //多文件上传
                'fileDesc'       : '支持格式:zip/rar',  //限制文件上传格式描述
                'fileExt'        : '*.ZIP;*.RAR;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	$("#uploadifyQueue_bbsreply").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#hideFileId_bbsreply").append("<input type='hidden' id='uploadfileid_bbsreply' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件:" + fileObj.name + "上传失败"); 
				    $('#uploadify_bbsreply').uploadifyClearQueue(); //清空上传队列
				}
	       }); 	   	       	        
    	}); 
    	
    	//附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	   //附件删除
	   function deleteAttachFile(attid){
		  if(confirm("确定要删除这个附件？")){
		  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
			$.get(url,{fileid:attid},function(data){
				$("#uploadifyQueue_bbsreply > li[id='"+attid+"']").remove();
				$("#uploadfileid_bbsreply[value='"+attid+"']").remove();
			});	
		  }
	    } 
	</script>

<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV id="top"
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">广东学苑在线</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A>
			<c:forEach items="${bbsSections }" var="section" varStatus="vs">
						→ <A
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${section.resourceid }${coursequerystr }"
					class="STYLE1"> ${section.sectionName }</A>
			</c:forEach>
			→ <A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?topicId=${bbsTopic.resourceid }${coursequerystr }"
				class="STYLE1"> ${bbsTopic.title }</A> →
			<c:choose>
				<c:when test="${not empty bbsReply }">编辑回复</c:when>
				<c:otherwise>新的回复</c:otherwise>
			</c:choose>
		</DIV>
	</DIV>
</DIV>

<form id="newReplyForm" method="post"
	action="${baseUrl}/edu3/learning/bbs/reply/save.html"
	onsubmit="return validateTopicSubmit(this);">
	<div style="margin: 10px auto;" class="postlary2 postlary">
		<input type="hidden" name="bbsTopicId" value="${bbsTopic.resourceid }" />
		<input type="hidden" name="bbsReplyId" value="${bbsReply.resourceid }" />
		<input type="hidden" name="pageNum" value="${pageNum }" /> <input
			type="hidden" name="courseId" value="${course.resourceid }" />
		<table cellspacing="0" cellpadding="0" border="0"
			style="width: 100%; line-height: normal;">
			<tr>
				<td style="text-align: center; line-height: normal; padding: 0px;"
					class="td_a"><b>回复标题：</b></td>
				<td
					style="text-align: left; width: 85%; line-height: normal; padding: 0px;"
					class="td_d"><input type="text"
					style="margin: 3px; width: 280px;" readonly="readonly"
					value="Re: ${bbsTopic.title }" /></td>
			</tr>
			<tr>
				<td style="text-align: center; line-height: normal; padding: 0px;"
					class="td_a"><b>内容：</b></td>
				<td style="text-align: left; line-height: normal; padding: 0px;"
					class="td_d"><textarea id="newReplyTextarea"
						name="replyContent" rows="5" cols=""
						style="width: 100%; height: 300px; visibility: hidden;">
							<c:if test="${not empty quoteReply }">
								<fieldset
								style="background: none repeat scroll 0 0 #FAFAFA; border: 1px solid #CCCCCC; margin: 0 5px 5px 15px; padding: 3px;">
									<legend>
									<b>引用${quoteReply.showOrder }楼 原帖由${quoteReply.replyMan }于<fmt:formatDate
											value="${quoteReply.replyDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />发表</b>
								</legend>
									<div>${quoteReply.replyContent }</div>
								</fieldset>	
							</c:if>
							${bbsReply.replyContent }<br />								
						</textarea></td>
			</tr>
			<tr>
				<td style="text-align: center; line-height: normal; padding: 0px;"
					class="td_a">上传附件：</td>
				<td class="td_d"><font color="green">(单个文件上传大小不能大于10M)</font><br />
					<input type="file" name="uploadify" id="uploadify_bbsreply" />
					<div id="uploadifyQueue_bbsreply" class="uploadifyQueue">
						<c:forEach items="${bbsReply.attachs}" var="attach" varStatus="vs">
							<li id="${attach.resourceid }"><img
								style="cursor: pointer; height: 10px;"
								src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
								<a onclick="downloadAttachFile('${attach.resourceid }')"
								href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp; <img
								style="cursor: pointer; height: 10px;"
								onclick="deleteAttachFile('${attach.resourceid }');"
								src="${baseUrl}/jscript/jquery.uploadify/images/cancel.png" />
							</li>
						</c:forEach>
					</div>
					<div id="hideFileId_bbsreply" style="display: none">
						<c:forEach items="${bbsReply.attachs}" var="attach" varStatus="vs">
							<input type="hidden" value="${attach.resourceid }"
								name="uploadfileid" id="uploadfileid_bbsreply">
						</c:forEach>
					</div></td>
			</tr>
			<tr>
				<td style="text-align: center; line-height: normal; padding: 0px;"
					class="td_a"><b>&nbsp;</b></td>
				<td style="text-align: left; line-height: normal; padding: 0px;"
					class="td_d"><input id="bbsRepySubmit" type="submit"
					style="margin: 3px;" value="发表"> <input type="reset"
					onclick="clearReset()" value="清空内容" style="margin: 3px;"> <span>[完成后可按
						Ctrl+Enter 发布] </span></td>
			</tr>
		</table>
	</div>
</form>

<script type="text/javascript">
		function loadEditor(){
		     KE.create('newReplyTextarea'); 		    
	    } 	        
	    window.setTimeout(loadEditor,1000);
	    
		function clearReset(){
			KE.text("newReplyTextarea","");
		}
		//检查合法性
		function validateTopicSubmit(form){	
			if(KE.isEmpty("newReplyTextarea")){
				alert("内容不能为空");
				return false;
			}			
			return validateCallback(form,function(json){
				if(json.statusCode == 200){							
					location.href=json.reloadUrl+"#"+json.bbsReplyId;
				}
			});
		}		
	</script>

<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>