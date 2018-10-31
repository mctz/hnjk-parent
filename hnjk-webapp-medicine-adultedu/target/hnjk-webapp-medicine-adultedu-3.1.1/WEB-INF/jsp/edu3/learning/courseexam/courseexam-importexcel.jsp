<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试题导入(excel)</title>
<style type="text/css">
#explanationSection li, #explanationSection p,
	#courseExamImportTemplate_ul li {
	line-height: 22px;
	width: 100%;
	height: auto;
	padding: 0;
}

#explanationSection ._bolder {
	font-weight: bold;
}
</style>
<script type="text/javascript">
		$(function() {	  		
	  		$("#uploadify_courseexam_form").uploadify({ //初始化附件组件
                'script'         : baseUrl+'/edu3/filemanage/upload.html?storePath=importfiles', //上传URL
                'auto'           : true, //自动上传
                'multi'          : false, //多文件上传
                'scriptData'     :{fillinName:'${currentUser.cnName}',fillinNameId:'${currentUser.resourceid}',formType:'CourseExamImport'},
                'fileDesc'       : '支持格式:xls',  //限制文件上传格式描述
                'fileExt'        : '*.xls;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 31457280, //限制单个文件上传大小30M 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	$("#courseexam_uploadifyQueue").html("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#courseexam_hideFileId").html("<input type='hidden' id='hideFileId_"+response.split("|")[0]+"' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
				    $('#uploadify_courseexam_form').uploadifyClearQueue(); //清空上传队列
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
						$("#courseexam_uploadifyQueue > li[id='"+attid+"']").remove();
						$("#hideFileId_"+attid).remove();
					});		
				}
			}); 
	   } 
	   
	   function courseExamImportValidateCallback(form){
		   var courseId = $(form).find("[name='courseId']").val();
		   if(courseId==""){
			   alertMsg.warn("课程不能为空！");
			   return false;
		   }
		   var uploadfileid = $(form).find("[name='uploadfileid']").size();
		   if(uploadfileid<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			    //DWZ.ajaxDone(json);
				//if (json.statusCode == 200){
					/* if (json.navTabId){
						navTab.reload(json.reloadUrl, {}, json.navTabId);
					}
					$.pdialog.closeCurrent(); */
					alertMsg.warn(json.message);
				//}
		   });
	   }
	   //下载导入模板
	   function downloadCourseExamTemplate(course,fileName){
		   var url = "${baseUrl}/edu3/framework/courseexam/template/download.html?course="+course+"&fileName="+encodeURIComponent(fileName);
			downloadFileByIframe(url,"downloadCourseExamImportTemplateIframe"); 
	   }
	</script>
</head>
<body>
	<h2 class="contentTitle">试题导入(excel)</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/metares/courseexam/importByExcel.html"
				class="pageForm"
				onsubmit="return courseExamImportValidateCallback(this);">
				<input type="hidden" name="isEnrolExam" value="${isEnrolExam }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程:</td>
							<td colspan="3"><gh:courseAutocomplete name="courseId"
									tabindex="1" isFilterTeacher="Y" classCss="required"
									id="courseexamimport_courseId" displayType="code"
									style="width:50%;" /></td>
						</tr>
						<%-- <c:if test="${isEnrolExam eq 'N' }">
					<tr>
					<td width="20%">试题考试形式:</td>
					<td>
						<gh:select dictionaryCode="CodeExamform" classCss="required" name="examform" choose="N" filtrationStr="online_exam,unit_exam,final_exam"/>
						<span style="color: red;">*</span>
					</td>
				</tr> 
				</c:if>--%>
						<tr>
							<td>试题文件:</td>
							<td colspan="3"><font color="green">(单个文件上传大小不能大于30M)</font><br />
								<input type="file" name="uploadify"
								id="uploadify_courseexam_form" />
								<div id="courseexam_uploadifyQueue" class="uploadifyQueue"></div>
								<div id="courseexam_hideFileId" style="display: none"></div></td>
						</tr>
						<tr>
							<td>导入模板:</td>
							<td colspan="3">
								<ul id="courseExamImportTemplate_ul">
									<li><img style="cursor: pointer; height: 10px"
										src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"><a
										href="##"
										onclick="downloadCourseExamTemplate('Illustration',this.title);"
										style="color: blue;" title="题库模板.xlsx">试题导入模板说明.xls</a></li>
								</ul>
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