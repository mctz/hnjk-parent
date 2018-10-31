<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入统考成绩</title>

<script type="text/javascript">
	$(document).ready(function(){
		$("#div_submit_universalExam").show();
    	$("#div_submit_universalExam_ing").hide();

	});
	  
	   function _iframeCallback(form, callback){
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
			if(!$(form).valid()) {return false;}
			else{
				$("#div_submit_universalExam_ing").show();
		    	$("#div_submit_universalExam").hide();
			}
			window.donecallback = callback || DWZ.ajaxDone;
			if ($("#callbackframe").size() == 0) {
				$("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
			}
			form.target = "callbackframe";	
		}
	   function dialogAjaxDone(json){
		   // DWZ.ajaxDone(data);
		    	$("#div_submit_universalExam").show();
		    	$("#div_submit_universalExam_ing").hide();
				
			
		        if(json.statusCode == 200){
		    		if(json.forwardUrl==""){
		    			alertMsg.correct(json.message);
		    	        var form =_getPagerForm(navTab.getCurrentPanel(),"RES_TEACHING_UNIVERSALEXAM_LIST_NEW");
		    			navTab.reload(form.action,$(form).serializeArray());
		    		}else{
		    			alertMsg.confirm(json.message+"是否下载导入失败名单?", { 
		    				okCall: function(){ 
		    					downloadFileByIframe("${baseUrl}"+json.forwardUrl,'tgradeIframe');
		    				} 
		    			}); 
		    		} 
		    	}else{
		    		alertMsg.error(json.message);
		    	}
		    }
	</script>
</head>
<body>
	<h2 class="contentTitle">导入统考成绩</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu/teaching/universalExam/universalExam-new-import-details.html"
				class="pageForm" onsubmit="return _iframeCallback(this,dialogAjaxDone);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
								<td width="30%">学期</td>
								<td width="70%">
								${fn:split(_term,"_")[0]}年${fn:split(_term,"_")[1] =='01' ?'一学期':'二学期'}
								<input type="hidden" name="_term" readOnly="true" value="${_term}" /></td>
								
							</tr>
						<tr>
						<tr>
								<td width="30%">考试类型：</td>
								<td width="70%">
								${ghfn:dictCode2Val('ExamResult',condition['examType']) }
								<input type="hidden" name="examType" readOnly="true" value="${condition['examType'] }" />
								</td>
							</tr>
						<tr>
							<td width="30%">上传成绩模板:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="registerImport" fileExt="xls" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								使用本功能前：1、应<font color='red'>下载导出模板</font>，并按照导出模板中的样式填写学生的成绩。2、上传附件的功能需要激活<font color='red'>Flash插件</font>。如果您看不见上传附件按钮，请先安装Flash插件
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table class="list" width="100%">
									<tbody id="universalExamImportBody"></tbody>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div id="div_submit_universalExam_ing" >
								<div class="buttonContent">
									<input
							type="button" style="color: green" value="正在提交...请耐心等待..."
							name="submiting" disabled="true" />
								</div>
							</div></li>
						<li><div id="div_submit_universalExam" class="buttonActive">
								<div class="buttonContent">
									<button id="universalExam_import_button" type="submit">导入</button>
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