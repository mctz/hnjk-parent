<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入招生预约报读信息</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="enrollmentBookUploadForm" action="${baseUrl}/edu3/enrollment/booking/batchImport.html" class="pageForm" method="post" onsubmit="return _importEnrollmentBook(this);" >
				<input type="hidden"  id="exportErrorEnrollmentBookUrl" />
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="enrollmentBookUploadTable">
						<tr>
							<td width="20%">通过时间：</td>
							<td align="center" width="80%">
								<gh:selectModel name="gradeId" bindValue="resourceid" displayValue="gradeName"
											orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
											modelClass="com.hnjk.edu.basedata.model.Grade" style="width:55%;"  classCss="required" />
							</td>
						</tr>
						<tr>
							<td width="20%">预约报读文件：</td>
							<td width="80%"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users"
									extendStorePath="attachs" formType="enrollmentBookUpload"
									uploadLimit="1" isMulti="false" /></td>
						</tr>

						<tr>
							<td width="20%">要求：</td>
							<td width="80%"><font color="red">注意：<br/>1、导入EXCEL文件应为Excel2003及以前版本<br/>2、上传附件的功能需要激活Flash插件。</font>
							</td>
						</tr>
						<tr>
							<td width="20%">提示：</td>
							<td width="80%" >
								<div style="color: red; margin-left: 10px;font-size: 14px;height: 50px;margin-top: 8px;">
									在学校成功预约报读的学生，请在&nbsp;<span style="font-weight: bolder;background-color: yellow;">${examinationTime }</span>&nbsp;到广西招生考试院网上进行正式报考
									<div style="margin-top: 8px;font-size: 14px;">
									（网站地址：<span style="font-weight: bolder;background-color: yellow;">www.gxeea.edu.cn</span>）,并按要求进行确认和参加成人高考。
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td width="20%">导入结果：</td>
							<td align="center" width="80%" id="enrollmentBook_uploadResult">

							</td>
						</tr>
						<tr>
							<td width="20%">导入异常原因：</td>
							<td width="80%" >
									<table class="list" width="100%"><tbody id="enrollmentBook_uploadTbody"></tbody></table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
					    <li id="errorEnrollmentBook">
					    	<div class="buttonActive">
					    		<div class="buttonContent">
					    			<input type="button"  onclick="exportErrorEnrollmentBook()" style="color: red;cursor: pointer;font-weight: bold;background: none;border: 0;margin: 0;padding-top: 3px;" value="导出错误记录" />
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
		   $("#errorEnrollmentBook").hide();
	   });
	 
	   function _importEnrollmentBook(form){		 
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("请上传一个招生预约报读信息文件！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   if(json.statusCode == 300) {
					alertMsg.error(json.message);
				}else if(json.statusCode == 400){
					// 导入结果
					var $resultMsg = $("#enrollmentBook_uploadResult");
					$resultMsg.html("");//清空
					$resultMsg.html(json.resultMsg);
					// 失败原因
					var $table = $("#enrollmentBook_uploadTbody");
					$table.html("");//清空
					$table.html("<tr><td class='error'>"+json.message+"</td></tr>");
					 $("#errorEnrollmentBook").show();
					 $("#exportErrorEnrollmentBookUrl").val(json.exportErrorEnrollmentBook);
				} else if (json.statusCode == 200){
					$.pdialog.closeCurrent();
					alertMsg.correct(json.message);
				}
			});
	   }	  
	   
	   // 导出错误的记录
	   function exportErrorEnrollmentBook(){
		   var exportUrl=$("#exportErrorEnrollmentBookUrl").val();
		   downloadFileByIframe("${baseUrl}"+exportUrl,'exportErrorEnrollmentBookIframe');
	   }
	
</script>
</body>
</html>
