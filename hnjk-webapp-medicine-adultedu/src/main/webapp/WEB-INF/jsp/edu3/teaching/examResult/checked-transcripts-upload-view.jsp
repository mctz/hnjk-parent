<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入已审核的成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="uploadCheckedTranscriptsFileForm" class="pageForm"
				method="post"
				action="${baseUrl }/edu3/teaching/transcripts/upload-checked-transcripts-process.html">
				<input id="examSubId" name="examSubId" value="${examSubId }" />
				<table class="form">

					<tr>
						<td>成绩文件：</td>
						<td>
							<div style="position: fixed;">
								<div>
									<input type="file" name="uploadCheckedTranscriptsFile"
										id="uploadCheckedTranscriptsFile" />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" type="button"
										onclick="return examIniationUploadFileVerified()">提交
									</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	$(document).ready(function(){
		
 		initUploadExamResults("/edu3/filemanage/upload.html")
	});
 	//AJAX上传文件
	function initUploadExamResults(url){
		var examSubId= jQuery("#uploadCheckedTranscriptsFileForm >input:id='examSubId'").val();
		$("#uploadCheckedTranscriptsFile").uploadify({
                'script'         : baseUrl+url, //上传URL
	            'auto'           : true, //自动上传
	            'scriptData'	 : {'formId':examSubId,'formType':'examInationFileUpload'},
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:XML/xml',  //限制文件上传格式描述
	            'fileExt'        : '*.XML;*.xml;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
         			
         			if($("#uploadCheckedTranscriptsFileForm >input:id='examinationUploadFileAttId'").length<=0){
	         			//添加隐藏域与业务建立关系
						$("#uploadCheckedTranscriptsFileForm").append("<input type='hidden' id='uploadCheckedTranscriptsFileAttId' name='uploadCheckedTranscriptsFileAttId' value='"+response.split("|")[0]+"'>"); 
         			}else{
         				$("#uploadCheckedTranscriptsFileForm >input:id='uploadCheckedTranscriptsFileAttId'").attr("value",response.split("|")[0]);
         			}
         			alert("上传成功！");
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
					$('#uploadCheckedTranscriptsFile').uploadifyClearQueue(); //清空上传队列
				}
        });
	}
</script>
</body>
</html>
