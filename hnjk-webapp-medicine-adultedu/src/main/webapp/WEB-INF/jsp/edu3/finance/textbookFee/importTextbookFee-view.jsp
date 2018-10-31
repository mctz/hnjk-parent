<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入年教材费标准</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="textbookFeeUploadForm" action="${baseUrl}/edu3/finance/textbookFee/importTextbookFee.html" class="pageForm" method="post" onsubmit="return _importTextbookFee(this);" >
				<input type="hidden"  id="exportErrorTextbookFeeUrl" />
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="textbookFeeUploadTable">
						<tr>
							<td width="20%">年度：</td>
							<td align="center" width="80%">
								<gh:selectModel name="yearId" bindValue="resourceid" displayValue="yearName"  modelClass="com.hnjk.edu.basedata.model.YearInfo" orderBy="firstYear desc"  classCss="required" style="width:50%"/>
							</td>
						</tr>
						<tr>
							<td width="20%">年教材费文件：</td>
							<td width="80%"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,textbookFee"
									extendStorePath="attachs" formType="textbookFeeUpload"
									uploadLimit="1" isMulti="false" /></td>
						</tr>

						<tr>
							<td width="20%">要求：</td>
							<td width="80%"><font color="red">注意：<br/>1、导入EXCEL文件应为Excel2003及以前版本<br/>2、上传附件的功能需要激活Flash插件。</font>
							</td>
						</tr>
						<tr>
							<td width="20%">导入结果：</td>
							<td align="center" width="80%" id="textbookFee_uploadResult">

							</td>
						</tr>
						<tr>
							<td width="20%">导入异常原因：</td>
							<td width="80%" >
									<table class="list" width="100%"><tbody id="textbookFee_uploadTbody"></tbody></table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
					    <li id="errorTextbookFee">
					    	<div class="buttonActive">
					    		<div class="buttonContent">
					    			<input type="button"  onclick="exportErrorTextbookFee()" style="color: red;cursor: pointer;font-weight: bold;background: none;border: 0;margin: 0;padding-top: 3px;" value="导出错误记录" />
				    			</div>
			    			</div>
		    			</li>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" >导入</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	 $(document).ready(function(){
		   $("#errorTextbookFee").hide();
	   });
	 
	   function _importTextbookFee(form){		 
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("请上传一个年教材费标准文件！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   if(json.statusCode == 300) {
					alertMsg.error(json.message);
				}else if(json.statusCode == 400){
					// 导入结果
					var $resultMsg = $("#textbookFee_uploadResult");
					$resultMsg.html("");//清空
					$resultMsg.html(json.resultMsg);
					// 失败原因
					var $table = $("#textbookFee_uploadTbody");
					$table.html("");//清空
					$table.html("<tr><td class='error'>"+json.message+"</td></tr>");
					 $("#errorTextbookFee").show();
					 $("#exportErrorTextbookFeeUrl").val(json.exportErrorTextbookFee);
				} else if (json.statusCode == 200){
					$.pdialog.closeCurrent();
					alertMsg.correct(json.message);
				}
			});
	   }	  
	   
	   // 导出错误的记录
	   function exportErrorTextbookFee(){
		   var exportUrl=$("#exportErrorTextbookFeeUrl").val();
		   downloadFileByIframe("${baseUrl}"+exportUrl,'exportErroTextbookFeeIframe');
	   }
	
</script>
</body>
</html>
