<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入入学资格状态</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="recruitStatusUploadFileForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="recruitStatusUploadFileTable">
						<tr>
							<td style="inner-height:;">注意事项:<br>
								1.需按以下顺序填写EXCEL文件：表头依次为考生号，姓名，身份证号，专业，入学资格状态。<br>
								2.所有数据须与系统中保持一致。<br> 3.导入EXCEL文件应为Excel2003及以前版本，谢谢您的配合！<br>
							</td>
						</tr>
						<tr>

							<td><gh:upload hiddenInputName="attachId"
									uploadType="excel" baseStorePath="users,${user.username}"
									attachList="${attachsList }" extendStorePath="attachs"
									formId="${user.resourceid}" formType="recruitStatusUpload"
									uploadLimit="1" isMulti="false" />若本行未出现"上传附件"的按钮，请激活浏览器的flash插件。
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
										onclick="return recruitStatusUploadFileVerified()">
										提交</button>
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

	function recruitStatusUploadFileVerified(){

		var attid    = new Array();
		$("#recruitStatusUploadFileTable input[name='attachId']").each(function(index){
			attid.push($(this).val());
		});

		var url 	 = "${baseUrl}/edu3/register/studentinfo/recruitStatusImport/upload.html";
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
