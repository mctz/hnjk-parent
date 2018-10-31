<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入免考申请</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="noExamApplyUploadFileForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="noExamApplyUploadFileTable">
						<tr>
							<td>
								<p>切记按以下顺序填写EXCEL文件,免考课程名,须与系统中保持一致:</p>

								<p>学号，姓名，课程，申请类型，理由,成绩</p>
							</td>
						</tr>
						<tr>

							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,${user.username}"
									attachList="${attachsList }" extendStorePath="attachs"
									formId="${user.resourceid}" formType="noExamApplyFileUpload"
									uploadLimit="1" isMulti="false" /></td>
						</tr>

						<tr>
							<td style="color: red">
								注意：导入EXCEL文件应为Excel2003及以前版本，同时为了获取更快的处理速度，同一份文件中，请勿超过1000条记录，谢谢你的配合！
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
										onclick="return noExamApplyUploadFileVerified()">提交</button>
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

	function noExamApplyUploadFileVerified(){

		var attid    = new Array();
		$("#noExamApplyUploadFileTable input[name='attachId']").each(function(index){
			attid.push($(this).val());
		});

		var url 	 = "${baseUrl}/edu3/teaching/noexamapply/import/upload.html";
		if(attid.length <= 0){
			alertMsg.warn("请上传一个Excel文件！");
			return false; 
		}
		

		$.ajax({
			type:'POST',
			url:url,
			data:"attachId="+attid.toString(),
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(resultData){
				var success = resultData['success'];
				var msg     = resultData['msg'];
				if(success==true){
					alertMsg.confirm(msg);
					$.pdialog.closeCurrent();
				}else{
					alertMsg.confirm(msg);
				}
			}
		});
	}
</script>
</body>
</html>
