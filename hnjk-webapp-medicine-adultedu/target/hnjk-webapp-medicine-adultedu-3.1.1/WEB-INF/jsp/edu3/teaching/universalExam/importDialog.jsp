<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入统考成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="universalExamUploadFileForm" class="pageForm" method="post">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="universalExamUploadFileTable">
						<tr>
							<td width="20%">统考课程：</td>
							<td width="80%"><gh:courseAutocomplete
									name="UEImportCourseId" id="universalExam_import_courseId"
									tabindex="1" displayType="code" isFilterTeacher="Y"
									style="width:55%;" value="${condition['UEImportCourseId'] }" />
							</td>
						</tr>
						<c:if test="${hasAuthority=='A' || hasAuthority=='Y'}">
							<tr>
								<td width="20%">考试类型：</td>
								<td width="80%"><c:if test="${hasAuthority=='A' }">
										<input type="radio" name="examType" value="N" />正考
								</c:if> <input type="radio" name="examType" value="Y" />补考</td>
							</tr>
						</c:if>
						<tr>
							<td width="20%">成绩文件：</td>
							<td width="80%"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,${user.username}"
									extendStorePath="attachs" formType="universalExamFileUpload"
									uploadLimit="1" isMulti="false" /></td>
						</tr>
						<tr>
							<td width="20%">Excel要求：</td>
							<td width="80%"><font color="red">注意：导入EXCEL文件应为Excel2003及以前版本</font>
							</td>
						</tr>
						<tr>
							<td width="20%">导入结果：</td>
							<td align="center" width="80%" id="universalExam_importResult">

							</td>
						</tr>
						<tr>
							<td width="20%">导入异常原因：</td>
							<td align="center" width="80%"
								id="universalExam_importFailReason"></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" type="button"
										onclick="return universalExamImportFileVerified()">
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
		function universalExamImportFileVerified() {
			var UECourse = $("#universalExam_import_courseId").val();
			var attid    = new Array();
			var isReupload = $("#attachId").val();
			var examType = $(":radio:checked").val();
			
			if(!examType){
				alertMsg.warn("请选择考试类型");
				return false; 
			}
			
			if(!isReupload){
				alertMsg.warn("请删除当前统考成绩文件，重新上传！");
				return false; 
			}
			$("#universalExamUploadFileTable input[name='attachId']").each(function(index){
				attid.push($(this).val());
			});
	
			var url 	 = "${baseUrl}/edu3/teaching/universalExam/handle-universalExam-import.html";
			if(attid.length <= 0){
				alertMsg.warn("请上传一个统考成绩文件！");
				return false; 
			}
			
			if (!$("#universalExamUploadFileForm").valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
	
			$.ajax({
				type:'POST',
				url:url,
				data:{"UEImportCourseId":UECourse,"attachId":attid.toString(),"examType":examType},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					var result = resultData['result'];
					var $table = $("#universalExam_importFailReason");
					$table.html("");//清空
					$table.html(msg);
					var $table_result = $("#universalExam_importResult");
					$table_result.html("");//清空
					$table_result.html(result);
					$("#attachId").remove();
				}
			});
		}
</script>
</body>
</html>
