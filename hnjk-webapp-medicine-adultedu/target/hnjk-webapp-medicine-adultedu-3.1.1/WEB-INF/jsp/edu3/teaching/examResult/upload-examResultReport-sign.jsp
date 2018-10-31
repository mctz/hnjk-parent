<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传成绩单签名</title>
<script type="text/javascript">
  	jQuery(document).ready(function(){
		 //上传校本部			
		 $("#examResultReport-sign input[type='file']").each(function(){
			 var uploadId=$(this).attr("id");
			 var replaceName = uploadId.split("_")[1];
			 var attachId = $("#"+uploadId+"_photoPathId").val();
			 $(this).uploadify({ 
		            'script'         : baseUrl+'/edu3/filemanage/upload.html',
		            'auto'           : true, //自动上传               
		            'multi'          : false, //多文件上传
		            'scriptData'	 : {'storePath':'examResultReportSign','acceptType':'png','formType':'examResultReportSign','attachId':attachId,'replaceName':replaceName},
		            'fileDesc'       : '支持格式:png',  //限制文件上传格式描述
		            'fileExt'        : '*.PNG;', //限制文件上传的类型,必须有fileDesc这个性质
		            'sizeLimit'      : 20480, //限制单个文件上传大小20K 
		            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
		            'onInit': function () {       
			        	//载入时触发，将flash设置到最小             
			        	$("#fileQueue").hide();
			        },		
			        onSelectOnce: function (event,data) {
			        	if(data.allBytesTotal>20480){
			        		alert("选择上传小于等于20K的文件！");
			        		return false;
			        	}
			        },
		            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
		            	var result = response.split("|");
		            	var rootUrl = $("#examResultReport-sign-rootUrl").val();
		            	$('#examResultReport_'+result[1].split(".")[0]+'_view').attr('src',rootUrl+'examResultReportSign/'+result[1]);
		               	$('#examResultReport_'+result[1].split(".")[0]+'_photoPathId').val(result[0]);
		             	attachId =$("#"+uploadId+"_photoPathId").val();
		               	$("#"+uploadId).uploadifySettings('scriptData',{'storePath':'examResultReportSign','acceptType':'png','formType':'examResultReportSign','attachId':attachId,'replaceName':replaceName}); 
					},  
					onError: function(event, queueID, fileObj,errorObj) {  // 上传失败回调函数
					    alert("文件" + fileObj.name + "上传失败！"); 
					    $('#examResultReport #uploadify').uploadifyClearQueue(); //清空上传队列
					}
	    	 });
		 });
  	});
		 
</script>
</head>
<body>
	<h2 class="contentTitle">上传成绩单签名</h2>
	<div class="page">
		<div class="pageContent">
			<form id="examResultReport-sign" method="post" action="" onsubmit=""
				class="pageForm">
				<input type="hidden" id="examResultReport-sign-rootUrl"
					value="${rootUrl }" />
				<div class="pageFormContent" layoutH="97">
					<div>
						<table class="form">
							<%-- 上传校本部院长签名 --%>
							<tr>
								<td width="40%">文件要求描述</td>
								<td width="20%">选择文件</td>
								<td>图片预览</td>
							</tr>
							<tr>
								<td width="40%"><h2
										style="margin-top: 5px; margin-bottom: 5px">校本部院长签名要求：</h2>
									1、背景要求：透明；<br /> 2、图片尺寸（像素）宽：165、高：60；<br /> 3、大小：≤20K、格式：png</td>
								<td width="20%"><input id="examResultReport_signDean0"
									type="file" /></td>
								<td><c:choose>
										<c:when test="${not empty signDean0}">
											<img id="examResultReport_signDean0_view"
												src="${rootUrl}examResultReportSign/${signDean0.serName}"
												width="165" height="60" />
										</c:when>
										<c:otherwise>
											<img id="examResultReport_signDean0_view"
												src="${baseUrl}/themes/default/images/img_preview.png"
												width="165" height="60" />
										</c:otherwise>
									</c:choose> <input type="hidden" name="photoPath"
									id="examResultReport_signDean0_photoPathId"
									value="${signDean0.resourceid}" /></td>
							</tr>
							<%-- 上传校本部审核人签名 --%>
							<tr>
								<td width="40%"><h2
										style="margin-top: 5px; margin-bottom: 5px">校本部审核人签名要求：</h2>
									1、背景要求：透明；<br /> 2、图片尺寸（像素）宽：165、高：60；<br /> 3、大小：≤20K、格式：png</td>
								<td width="20%"><input id="examResultReport_signAuditor0"
									type="file" /></td>
								<td><c:choose>
										<c:when test="${not empty signAuditor0}">
											<img id="examResultReport_signAuditor0_view"
												src="${rootUrl}examResultReportSign/${signAuditor0.serName}"
												width="165" height="60" />
										</c:when>
										<c:otherwise>
											<img id="examResultReport_signAuditor0_view"
												src="${baseUrl}/themes/default/images/img_preview.png"
												width="165" height="60" />
										</c:otherwise>
									</c:choose> <input type="hidden" name="photoPath"
									id="examResultReport_signAuditor0_photoPathId"
									value="${signAuditor0.resourceid}" /></td>
							</tr>
							<%-- 上传校外点院长签名 --%>
							<tr>
								<td width="40%"><h2
										style="margin-top: 5px; margin-bottom: 5px">校外点院长签名要求：</h2>
									1、背景要求：透明；<br /> 2、图片尺寸（像素）宽：165、高：60；<br /> 3、大小：≤20K、格式：png</td>
								<td width="20%"><input id="examResultReport_signDean1"
									type="file" /></td>
								<td><c:choose>
										<c:when test="${not empty signDean1}">
											<img id="examResultReport_signDean1_view"
												src="${rootUrl}examResultReportSign/${signDean1.serName}"
												width="165" height="60" />
										</c:when>
										<c:otherwise>
											<img id="examResultReport_signDean1_view"
												src="${baseUrl}/themes/default/images/img_preview.png"
												width="165" height="60" />
										</c:otherwise>
									</c:choose> <input type="hidden" name="photoPath"
									id="examResultReport_signDean1_photoPathId"
									value="${signDean1.resourceid}" /></td>
							</tr>
							<%-- 上传校外点审核人签名 --%>
							<tr>
								<td width="40%"><h2
										style="margin-top: 5px; margin-bottom: 5px">校外点审核人签名要求：</h2>
									1、背景要求：透明；<br /> 2、图片尺寸（像素）宽：165、高：60；<br /> 3、大小：≤20K、格式：png</td>
								<td width="20%"><input id="examResultReport_signAuditor1"
									type="file" /></td>
								<td><c:choose>
										<c:when test="${not empty signAuditor1}">
											<img id="examResultReport_signAuditor1_view"
												src="${rootUrl}examResultReportSign/${signAuditor1.serName}"
												width="165" height="60" />
										</c:when>
										<c:otherwise>
											<img id="examResultReport_signAuditor1_view"
												src="${baseUrl}/themes/default/images/img_preview.png"
												width="165" height="60" />
										</c:otherwise>
									</c:choose> <input type="hidden" name="photoPath"
									id="examResultReport_signAuditor1_photoPathId"
									value="${signAuditor1.resourceid}" /></td>
							</tr>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>