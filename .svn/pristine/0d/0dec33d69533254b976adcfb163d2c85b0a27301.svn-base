<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title }</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="examResultsUploadFileForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="examResultsUploadFileTable">
						<tr>
							<td width="20%">文件：</td>
							<td width="80%"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,${user.username}"
									extendStorePath="attachs" formId="${formId }" formType="examResultsFileUpload"
									uploadLimit="1" fileExt="xls|xlsx|sql" isMulti="false" /></td>
						</tr>
						<tr>
							<td width="20%" height="30px;">导入结果：</td>
							<td align="center" width="80%" >
								<textarea id="examResults_importResult" style="width: 350px;height: 40px;"></textarea>
							</td>
						</tr>
						<tr>
							<td width="20%" height="80px;" align="center">导入异常原因：</td>
							<td width="80%" height="100px" id="examResults_importFailReason"></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" type="button"
										onclick="return examResultsImportFileVerified()">提交</button>
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
		function examResultsImportFileVerified() {
			
			var attid    = new Array();
			var isReupload = $("#attachId").val();
			var examType = $(":radio:checked").val();
            var $table = $("#examResults_importFailReason");
            $table.html("");//清空
            var $table_result = $("#examResults_importResult");
            $table_result.html("");//清空

			$("#examResultsUploadFileTable input[name='attachId']").each(function(index){
				attid.push($(this).val());
			});

            if(attid.length <= 0){
                alertMsg.warn("请重新选择上传文件！");
                return false;
            }

			var url 	 = "${baseUrl}${url}";
			
			if (!$("#examResultsUploadFileForm").valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}

			$.ajax({
				type:'POST',
				url:url,
				data:{attachId:attid.toString(),},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					var result = resultData['result'];
					$table.html(msg);
					$table_result.html(result);
					$("#attachId").remove();
				}
			});
		}
</script>
</body>
</html>
