<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Excel导入</title>
</head>
<script type="text/javascript">

jQuery(document).ready(function(){
	$("#_imputDialogForm input[name='submit']").show();
	$("#_imputDialogForm input[name='submiting']").hide();
});
    function dialogAjaxDone(json){
    	$("#_imputDialogForm input[name='submit']").show();
    	$("#_imputDialogForm input[name='submiting']").hide();
        if(json.statusCode == "200"){
    		if(json.forwardUrl==""){
    			alertMsg.correct(json.message);
    	        var form =_getPagerForm(navTab.getCurrentPanel(),"${navTabId}");
    			navTab.reload(form.action,$(form).serializeArray());
    		}else{
    			alertMsg.confirm(json.message+"是否下载导入失败的记录?", {
    				okCall: function(){ 
    					downloadFileByIframe("${baseUrl}"+json.forwardUrl,'tgradeIframe');
    				} 
    			}); 
    		} 
    	}else{
    		alertMsg.error(json.message);
    	}
    }
    function setFilePath(){
    	var impValue = jQuery("#_imputDialogForm #importFile").val();
    	if(impValue==""){
    		alertMsg.warn("请选择将要导入的excel文件！");
    		return false;
    	}
    	return checkFileType();
    }
    function checkFileType(){
    	var impValue = jQuery("#_imputDialogForm #importFile").val().split(".");
    	var xlsType = impValue[impValue.length-1];
    	if(xlsType!="xls"){
    		alertMsg.warn("请导入2003版本或以下版本的excel文件！");
    		return false;
    	}
    	$("#_imputDialogForm input[name='submit']").hide();
    	$("#_imputDialogForm input[name='submiting']").show();
    	
    	return true;
    }
</script>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="_imputDialogForm"
				action="${baseUrl}${url}"
				enctype="multipart/form-data"
				onsubmit="return iframeCallback(this,dialogAjaxDone)">
				<table class="form">
					<tr>
						<td colspan="2">
							<p>
								<font color="red">&nbsp;注&nbsp;&nbsp;意：</font>
							</p>
							<p>1、导入的excel必须是2003版本或其以下的版本;</p>
							<p>2、必须按照导出模版进行填写;</p>
					</tr>
					<tr>
						<td>请选择导入的Excel文件：</td>
						<td><input name="importFile" id="importFile" type="file" />
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" value="提交"
							onclick="return setFilePath()" name="submit" /> <input
							type="button" style="color: green" value="正在提交...请耐心等待..."
							name="submiting" disabled="true" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>