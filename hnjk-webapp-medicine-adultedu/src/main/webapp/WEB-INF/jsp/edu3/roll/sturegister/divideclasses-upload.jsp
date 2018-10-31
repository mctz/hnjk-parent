<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Excel导入</title>
</head>
<script type="text/javascript">

jQuery(document).ready(function(){
	$("#_divideClassForm input[name='submit']").show();
	$("#_divideClassForm input[name='submiting']").hide();
});
    function dialogAjaxDone(json){
   // DWZ.ajaxDone(data);
    	$("#_divideClassForm input[name='submit']").show();
    	$("#_divideClassForm input[name='submiting']").hide();
        if(json.statusCode == "200"){
    		if(json.forwardUrl==""){
    			alertMsg.correct(json.message);
    	        var form =_getPagerForm(navTab.getCurrentPanel(),"RES_STU_PAYDETAIL");
    			navTab.reload(form.action,$(form).serializeArray());
    		}else{
    			alertMsg.confirm(json.message+"是否下载失败人员名单?", { 
    				okCall: function(){ 
    					downloadFileByIframe("${baseUrl}"+json.forwardUrl,'tgradeIframe');
    				} 
    			}); 
    		} 
    	}else{
    		alertMsg.error(json.message);
    	}
    }
    function checkDivideFilePath(){
    	var impValue = jQuery("#_divideClassForm #importFile").val();
    	if(impValue==""){
    		alertMsg.warn("请选择将要导入的excel文件！");
    		return false;
    	}
    	return checkFileType();
    }
    function checkFileType(){
    	var impValue = jQuery("#_jiaofeiForm #importFile").val().split(".");
    	var xlsType = impValue[impValue.length-1];
    	if(xlsType!="xls"){
    		alertMsg.warn("请导入2003版本或以下版本的excel文件！");
    		return false;
    	}
    	$("#_divideClassForm input[name='submit']").hide();
    	$("#_divideClassForm input[name='submiting']").show();
    	
    	return true;
    }
</script>
<body>
	<div class="page">
		<div class="pageContent">

			<form method="post" id="_divideClassForm"
				action="${baseUrl}/edu3/register/studentinfo/doXlsImpDivideClass.html?act=import"
				enctype="multipart/form-data"
				onsubmit="return iframeCallback(this,dialogAjaxDone)">
				<table class="form">
					<tr>
						<td colspan="2">
							<p>
								<font color="red">&nbsp;注&nbsp;&nbsp;意：</font>
							</p>
							<p>1、导入的excel必须是2003版本或其以下的版本;</p>
							<p>2、导入分班信息必须按照导出的分班信息列表排序;</p>
							<p>3、班级列中请输入班级名称全称;</p>
							<p>4、导入分班会覆盖学生之前的分班信息;</p>
					</tr>
					<tr>
						<td>请选择导入的Excel文件：</td>
						<td><input name="importFile" id="importFile" type="file" />
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" value="提交"
							onclick="return checkDivideFilePath()" name="submit" /> <input
							type="button" style="color: green" value="正在提交...请耐心等待..."
							name="submiting" disabled="true" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>