<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提交离线课后作业</title>
<script type="text/javascript">	
		$(function(){			
			$("#uploadify_student_upload").uploadify({ //初始化附件组件
                'script'         : baseUrl+'/edu3/filemanage/upload.html?storePath=users,${storeDir},attachs', //上传URL
                'auto'           : false, 
                'multi'          : false, 
                'fileDesc'       : '支持格式:zip/rar',  //限制文件上传格式描述
                'fileExt'        : '*.ZIP;*.RAR;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
                'queueSizeLimit' : 1,
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数                	
                	$("#isAttachsChange").val('Y');
                	$("#uploadifyQueues_student_upload").html("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#hideFileId_student_upload").html("<input type='hidden' id='uploadfileid_student_upload' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
				    $('#uploadify_student_upload').uploadifyClearQueue(); //清空上传队列
				},
				onSelect : function(event, queueID, fileObj) {	
					if($("#uploadifyQueues_student_upload li").size()>0){
						alertMsg.confirm("您确定要重新上传作业，新的作业将覆盖旧的作业？", {
							okCall: function(){
								$('#uploadify_student_upload').uploadifyUpload();	
							},
							cancelCall:function(){
								$('#uploadify_student_upload').uploadifyClearQueue();								
							}
						});
					} else {
						$('#uploadify_student_upload').uploadifyUpload();
					}	
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
	   		alertMsg.confirm("确定要删除这个附件？", {
				okCall: function(){
					var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
					$.get(url,{fileid:attid},function(data){
						$("#uploadifyQueues_student_upload > li[id='"+attid+"']").remove();
						$("#uploadfileid_student_upload[value='"+attid+"']").remove();
					});		
				}
			});
	   }  	
	   function _exercise_upload_dialogAjaxDone(json){
		   DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				if (json.reloadUrl){
					$.pdialog.reload(json.reloadUrl);
				}
				//$.pdialog.closeCurrent();
			}
	   }
	</script>
</head>
<body>
	<h2 class="contentTitle">提交离线课后作业</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/learning/interactive/exercise/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,_exercise_upload_dialogAjaxDone);"
				enctype="multipart/form-data">
				<input type="hidden" name="exerciseBatchId"
					value="${exerciseBatch.resourceid }" /> <input type="hidden"
					name="studentExerciseId" value="${studentExercise.resourceid }" />
				<input type="hidden" id="isAttachsChange" name="isAttachsChange"
					value="N" />
				<div class="pageFormContent" layoutH="97">
					<c:choose>
						<c:when test="${isStudent }">
							<table class="form">
								<tr>
									<td width="12%">所属作业批次:</td>
									<td width="88%" colspan="3"><input type="text"
										style="width: 50%" value="${exerciseBatch.colName }"
										readonly="readonly" /></td>
								</tr>
								<tr>
									<td>上传作业附件:</td>
									<td colspan="3"><input type="file" name="uploadify"
										id="uploadify_student_upload" size="50" />
										<div id="uploadifyQueues_student_upload"
											class="uploadifyQueue">
											<c:forEach items="${studentExercise.attachs}" var="attach"
												varStatus="vs">
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
										<div id="hideFileId_student_upload" style="display: none">
											<c:forEach items="${studentExercise.attachs}" var="attach"
												varStatus="vs">
												<input type="hidden" value="${attach.resourceid }"
													name="uploadfileid" id="uploadfileid_student_upload">
											</c:forEach>
										</div></td>
								</tr>
							</table>
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
													onclick="javacript:$.pdialog.closeCurrent();">取消</button>
											</div>
										</div></li>
								</ul>
							</div>
						</c:when>
						<c:otherwise>
							<span style="color: green;">您不是学生，无法操作!</span>
						</c:otherwise>
					</c:choose>
				</div>
			</form>
		</div>
	</div>
</body>
</html>