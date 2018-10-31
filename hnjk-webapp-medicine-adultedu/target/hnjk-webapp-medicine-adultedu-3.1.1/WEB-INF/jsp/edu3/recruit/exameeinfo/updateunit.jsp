<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分配教学站</title>
</head>
<script type="text/javascript">

jQuery(document).ready(function(){
	$("#_imputDialogForm input[name='submit']").show();
	$("#_imputDialogForm input[name='submiting']").hide();
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
    			//navTabPageBreak();
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
    }
   
</script>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="_imputDialogForm"
				action="${baseUrl}/edu3/recruit/exameeinfo/updateunit.html"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="ids" value="${ids }" />
				<table class="form">
					<tr>
						<td>教学站：</td>
						<td>
							<%--<gh:brSchoolAutocomplete name="brSchoolid" tabindex="1" id="brSchoolid"  displayType="code" defaultValue="" style="width:55%" /> --%>
							<select class="flexselect" id="brSchoolid" name="brSchoolid"
							tabindex=1 style="width: 55%;">${unitOption}</select>
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" value="提交"
							onclick="return setFilePath()" name="submit" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>