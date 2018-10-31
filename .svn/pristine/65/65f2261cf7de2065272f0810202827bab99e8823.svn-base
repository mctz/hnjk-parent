<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习导入</title>
<script type="text/javascript">
		$(function() {	  		
	  		$("#uploadify_activecourseexam_form").uploadify({ //初始化附件组件
                'script'         : baseUrl+'/edu3/filemanage/upload.html?storePath=importfiles', //上传URL
                'auto'           : true, //自动上传
                'multi'          : false, //多文件上传
                'scriptData'     :{fillinName:'${currentUser.cnName}',fillinNameId:'${currentUser.resourceid}',formType:'ActiveCourseExamImport'},
                'fileDesc'       : '支持格式:rtf/doc',  //限制文件上传格式描述
                'fileExt'        : '*.rtf;*.doc;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 31457280, //限制单个文件上传大小30M 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	$("#activecourseexam_uploadifyQueue").html("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#activecourseexam_hideFileId").html("<input type='hidden' id='hideFileId_"+response.split("|")[0]+"' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
				    $('#uploadify_activecourseexam_form').uploadifyClearQueue(); //清空上传队列
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
						$("#activecourseexam_uploadifyQueue > li[id='"+attid+"']").remove();
						$("#hideFileId_"+attid).remove();
					});		
				}
			}); 
	   } 
	   
	   function activecourseExamImportValidateCallback(form){		   
		   var uploadfileid = $(form).find("[name='uploadfileid']").size();
		   if(uploadfileid<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   DWZ.ajaxDone(json);
				if (json.statusCode == 200){
					$.pdialog.closeCurrent();
					
			 		$.pdialog.open("${baseUrl}/edu3/metares/exercise/activeexercise/preview.html?courseId="+json.courseId+"&syllabusId="+json.syllabusId, "courseexam_previw", "预览随堂练习", {width:800,height:600});
										
					var url = "${baseUrl}/edu3/framework/metares/list.html?syllabusId="+json.syllabusId+"&courseId="+json.courseId+"&currentIndex="+json.currentIndex;
			 		$("#mateTab"+json.currentIndex).attr('href',url).click();
				}
		   });
	   }	   
	 	
	</script>
</head>
<body>
	<h2 class="contentTitle">随堂练习导入</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/metares/exercise/activeexercise/import.html"
				class="pageForm"
				onsubmit="return activecourseExamImportValidateCallback(this);">
				<input type="hidden" name="isEnrolExam" value="${isEnrolExam }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程:</td>
							<td colspan="3">${course.courseName } <input type="hidden"
								name="courseId" value="${course.resourceid }" />
							</td>
						</tr>
						<tr>
							<td>习题文件:</td>
							<td colspan="3"><font color="green">(单个文件上传大小不能大于30M)</font><br />
								<input type="file" name="uploadify"
								id="uploadify_activecourseexam_form" />
								<div id="activecourseexam_uploadifyQueue" class="uploadifyQueue"></div>
								<div id="activecourseexam_hideFileId" style="display: none"></div>
							</td>
						</tr>
					</table>
					<div style="color: green;">
						<ul>
							<li>说明：</li>
							<li>请先下载模板，参照模板格式</li>
						</ul>
					</div>
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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>