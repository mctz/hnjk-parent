<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入统考成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="examinationUploadFileForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="examinationUploadFileTable">
						<tr>
							<td width="20%">通过时间：</td>
							<td align="center" width="80%"><input type="text"
								id="passDate" name="passDate" class="required"
								onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})" />
							</td>
						</tr>
						<tr>
							<td width="20%">成绩文件：</td>
							<td width="80%"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,${user.username}"
									extendStorePath="attachs" formType="examInationFileUpload"
									uploadLimit="1" isMulti="false" /></td>
						</tr>

						<tr>
							<td width="20%">Excel要求：</td>
							<td width="80%"><font color="red">注意：导入EXCEL文件应为Excel2003及以前版本</font>
							</td>
						</tr>
						<tr>
							<td width="20%">导入结果：</td>
							<td align="center" width="80%" id="exameeinfo_uploadResult">

							</td>
						</tr>
						<tr>
							<td width="20%">导入异常原因：</td>
							<td align="center" width="80%" id="exameeinfo_uploadTbody">

							</td>
						</tr>
					</table>
				</div>
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
	/*
	$(document).ready(function(){
		
 		initUploadExamResults("/edu3/filemanage/upload.html")
	});
 	//AJAX上传文件
	function initUploadExamResults(url){
		$("#examinationUploadFile").uploadify({
                'script'         : baseUrl+url, //上传URL
	            'auto'           : true, //自动上传
	            'scriptData'	 : {'formType':'examInationFileUpload',"fillinNameId":'${curUserId}'},
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:XLS/xls',  //限制文件上传格式描述
	            'fileExt'        : '*.XLS;*.xls;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
         			
         			if($("#examinationUploadFileForm >input:id='examinationUploadFileAttId'").length<=0){
	         			//添加隐藏域与业务建立关系
						$("#examinationUploadFileForm").append("<input type='hidden' id='examinationUploadFileAttId' name='examinationUploadFileAttId' value='"+response.split("|")[0]+"'>"); 
         			}else{
         				$("#examinationUploadFileForm >input:id='examinationUploadFileAttId'").attr("value",response.split("|")[0]);
         			}
         			alert("上传成功！");
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
					$('#examinationUploadFile').uploadifyClearQueue(); //清空上传队列
				}
        });
	}
*/
	function examIniationUploadFileVerified(){

		var passDate = $("#examinationUploadFileForm input[name='passDate']").val();
		var attid    = new Array();
		$("#examinationUploadFileTable input[name='attachId']").each(function(index){
			attid.push($(this).val());
		});

		var url 	 = "${baseUrl}/edu3/teaching/result/statexam/analysis-examination-file.html";
		if(attid.length <= 0){
			alertMsg.warn("请上传一个统考成绩文件！");
			return false; 
		}
		
		if (!$("#examinationUploadFileForm").valid()) {
			alertMsg.error(DWZ.msg["validateFormError"]);
			return false; 
		}

		$.ajax({
			type:'POST',
			url:url,
			data:"passDate="+passDate+"&attachId="+attid.toString(),
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(resultData){
				var success = resultData['success'];
				var msg     = resultData['msg'];
				var result = resultData['result'];
				//alertMsg.confirm(msg);
				var $table = $("#exameeinfo_uploadTbody");
				$table.html("");//清空
				$table.html(msg);
				var $table_result = $("#exameeinfo_uploadResult");
				$table_result.html("");//清空
				$table_result.html(result);
				
				//$.pdialog.closeCurrent();
				
			}
		});
	}
</script>
</body>
</html>
