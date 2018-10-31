<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出成绩册封面</title>
</head>
<script type="text/javascript">

jQuery(document).ready(function(){
	$("#coverinput_examSubId").flexselect();
	$("#coverinput_classesId").flexselect();
	//$("#_imputDialogForm input[name='submit']").show();
	//$("#_imputDialogForm input[name='submiting']").hide();
});
    function dialogAjaxDone(json){
   // DWZ.ajaxDone(data);
    	$("#_imputDialogForm input[name='submit']").show();
    	$("#_imputDialogForm input[name='submiting']").hide();
        if(json.statusCode == "200"){
    		if(json.forwardUrl==""){
    			alertMsg.correct(json.message);
    	        var form =_getPagerForm(navTab.getCurrentPanel(),"RES_TEACHING_TEACHINGPLANCOURSETIMETABLE");
    			navTab.reload(form.action,$(form).serializeArray());
    		}else{
    			alertMsg.confirm(json.message+"是否下载失败招生专业名单?", { 
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
    	var impValue = jQuery("#_imputDialogForm #brSchoolid").val();
    	if(impValue==""){
    		alertMsg.warn("请选择教学站！");
    		return false;
    	}
    	return checkFileType();
    }
    function checkFileType(){
    	/*var impValue = jQuery("#_imputDialogForm #importFile").val().split(".");
    	var xlsType = impValue[impValue.length-1];
    	if(xlsType!="xls"){
    		alertMsg.warn("请导入2003版本或以下版本的excel文件！");
    		return false;
    	}*/
    	$("#_imputDialogForm input[name='submit']").hide();
    	$("#_imputDialogForm input[name='submiting']").show();
    	
    	return true;
    }
	function printResultCover(){
		var examSubId = $("#coverinput_examSubId").val();
		var classesId = $("#coverinput_classesId").val();
		if (classesId == "") {
			alertMsg.warn("请选择班级！");
			return;
		}
		if (examSubId == "") {
			alertMsg.warn("请选择考试批次！");
			return;
		}
		var url = "${baseUrl}/edu3/teaching/result/printResultCover.html";
		//var studentId = $("#examResultsSearchBody input[@name='resourceid']:checked").val();
		downloadFileByIframe(url+"?classesId="+classesId+"&examSubId="+examSubId,"coverinput_Iframe");
	}
</script>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="_imputDialogForm"
				action="${baseUrl}/edu3/recruit/exameeinfo/updateunit.html"
				onsubmit="return iframeCallback(this,dialogAjaxDone)">
				<input type="hidden" name="ids" value="${ids }" />
				<table class="form">
					<tr>
						<td>班级：</td>
						<td>${classesSelect }</td>
					</tr>
					<tr>
						<td>考试批次：</td>
						<td>
							<%--<gh:brSchoolAutocomplete name="brSchoolid" tabindex="1" id="brSchoolid"  displayType="code" defaultValue="" style="width:55%" /> --%>
							<select class="flexselect" id="coverinput_examSubId"
							name="examSubId" tabindex=1 style="width: 55%;">${examSubOption}</select>
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="button" value="导出"
							onclick="printResultCover()" /> <%--<input type="button" style="color:green" value="正在提交...请耐心等待..." name="submiting" disabled="true" /> --%>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>