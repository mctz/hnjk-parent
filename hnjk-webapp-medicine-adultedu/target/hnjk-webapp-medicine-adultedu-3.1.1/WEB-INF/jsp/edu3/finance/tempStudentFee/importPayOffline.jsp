<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>导入线下缴费信息</title>
	<style type="text/css">
		#error_userOrderbody .error {color:red;}
	</style>
	<script type="text/javascript">	   
	 $(document).ready(function(){
		   $("#errorPayOfflineInfo").hide();
		   
	   });
	 
		// 下载导入线下缴费信息模板
		function downloadPayOfflineTemplate(){
			var dlUrl = "${baseUrl}/edu3/finance/tempStudentFee/payOffLineTemplate.html";
			downloadFileByIframe(dlUrl, "downloadPayOfflineTemplate");
		}
	 
	   function importPayOfflineInfo(form){		 
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   if(json.statusCode == 300) {
					alertMsg.error(json.message);
				}else if(json.statusCode == 400){
					var $table = $("#error_payOfflineInfobody");
					$table.html("");//清空
					$table.html("<tr><td>导入失败数据及原因：</td></tr>");
					$table.html("<tr><td class='error'>"+json.message+"</td></tr>");
					 $("#errorPayOfflineInfo").show();
					 $("#exportErrorPayOfflineInfoUrl").val(json.exportErrorPayOffline);
				} else if (json.statusCode == 200){
					$.pdialog.closeCurrent();
					alertMsg.correct(json.message);
					if (json.reloadUrl){
						navTab.reload(json.reloadUrl, null,json.navTabId);
					}
				}
			});
	   }	  
	   
	   // 导出错误的记录
	   function exportErrorPayOfflineInfo(){
		   var exportUrl=$("#exportErrorPayOfflineInfoUrl").val();
		   downloadFileByIframe("${baseUrl}"+exportUrl,'exportErrorPayOfflineInfoIframe');
	   }
	</script>
</head>
<body>
	<h2 class="contentTitle">导入线下缴费信息</h2>
	<div class="page">
	<div class="pageContent">
	<form method="post" action="${baseUrl}/edu3/finance/tempStudentFee/importPayOffline.html" class="pageForm" onsubmit="return importPayOfflineInfo(this);" >
		<input type="hidden"  id="exportErrorPayOfflineInfoUrl" />
		<div class="pageFormContent" layoutH="97">
			<table class="form">
				<tr>
					<td width="40%">导入线下缴费信息文件:</td>
					<td>
					<gh:upload hiddenInputName="attachId"	
							uploadType="attach" 
							baseStorePath="upload"	
							isMulti="false" uploadLimit="1" 
							formType="payOfflineImport"
							fileExt="xls"
							/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						上传附件的功能需要激活Flash插件。
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<a style="cursor: pointer; color: red;font-weight: bolder;" onclick="downloadPayOfflineTemplate()" href="#">下载导入线下缴费信息模板</a>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table class="list" width="100%"><tbody id="error_payOfflineInfobody"></tbody></table>
					</td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li id="errorPayOfflineInfo"><div class="buttonActive"><div class="buttonContent"><input type="button"  onclick="exportErrorPayOfflineInfo()" style="color: red;cursor: pointer;font-weight: bold;background: none;border: 0;margin: 0;padding-top: 3px;" value="导出错误记录" /></div></div></li>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">导入</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="$.pdialog.closeCurrent();">关闭</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>
</body>
</html>