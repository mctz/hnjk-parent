<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入缴费信息</title>
<style type="text/css">
#register_messageTbody .success {
	color: green;
}

#register_messageTbody .error {
	color: red;
}
</style>
<script type="text/javascript">	   
	   function _registerValidateCallback(form){	
		   
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   if(json.statusCode == 300) {
					alertMsg.error(json.message);
				} else if (json.statusCode == 200){
			   alertMsg.correct(json.message);
				if (json.reloadUrl){
					navTab.reload(json.reloadUrl, {recruitPlanId:json.planid}, json.navTabId);
				}
				var $table = $("#register_messageTbody");
				$table.html("");//清空
				$table.html("<tr><td class='success'>"+json.message+"</td></tr>");//清空
				if(json.errorMessageList){//存在导入失败日志,输出						
					alertMsg.confirm(json.message+"是否下载失败人员名单?", { 
	    				okCall: function(){ 
	    					downloadFileByIframe("${baseUrl}"+json.forwardUrl,'tgradeIframe');
	    				} 
	    			}); 						
				} else {
					$.pdialog.closeCurrent();
				}					
				}
			});
		   
	   }	  
	</script>
</head>
<body>
	<h2 class="contentTitle">导入缴费信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/studentpayment/import-new1.html"
				class="pageForm" onsubmit="return _registerValidateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">导入缴费文件:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="registerImport" fileExt="xls" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<!-- 使用本功能前，需在上一级页面导出学生缴费信息。只能修改实缴金额，导入时将对相应缴费数据进行操作。 -->上传附件的功能需要激活Flash插件。
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table class="list" width="100%">
									<tbody id="register_messageTbody"></tbody>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">导入</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>