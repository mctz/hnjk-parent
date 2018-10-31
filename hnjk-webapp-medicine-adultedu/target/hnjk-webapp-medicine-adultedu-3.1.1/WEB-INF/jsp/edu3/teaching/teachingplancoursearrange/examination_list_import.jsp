<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title}</title>
<style type="text/css">
#exameeinfo_messageTbody .success {
	color: green;
}

#exameeinfo_messageTbody .error {
	color: red;
}
</style>
<script type="text/javascript">
	   function _exameeinfoValidateCallback(form){
		   
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
           $.ajax({
               type:'POST',
               url:url,
               data:{"attachId":attid.toString()},
               dataType:"json",
               cache: false,
               error: DWZ.ajaxError,
               success:function(resultData){
                   var msg     = resultData['message'];
                   var $table_result = $("#studentInfo_importResult");
                   $table_result.html("");//清空
                   $table_result.html(msg);
                   //$("#attachId").remove();
                   if(resultData['failDLUrl']){
                       alertMsg.confirm("是否下载导入失败记录？",{
                           okCall:function(){
                               downloadFileByIframe("${baseUrl}"+resultData.failDLUrl,'studyNoImportFailIframe');
                           }
                       });
                   }
                   //$.pdialog.closeCurrent();
               }
           });
	   }
	   
	</script>
</head>
<body>
	<h2 class="contentTitle">${title}</h2>
	<div class="page">
		<div class="pageContent">
			<form id="timetableform" method="post"
				action="${baseUrl}/edu3/teaching/teachingplancourse/submitExaminitionImport.html?courseids=${planCourse.course.resourceid}"
				class="pageForm"
				onsubmit="return _exameeinfoValidateCallback(this);">
				<input type="hidden" name="classid" value="${classes.resourceid}" />
				<input type="hidden" name="plancourseid" value="${planCourse.resourceid}" />
				<input type="hidden" name="term" value="${term}" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">班级:</td>
							<td>${classes.classname }</td>
						</tr>
						<tr>
							<td width="20%">课程:</td>
							<td>${planCourse.course.courseName }</td>
						</tr>

						<tr>
							<td width="20%">上传要导入的文件:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="ExameeInfoImport" fileExt="xls" />
								<input value="" id="roomids" name="roomids" type="hidden">
								<input value="" id="roomnames" name="roomnames" type="hidden">
							</td>
						</tr>
						<tr>
							<td colspan="2" style="color: red;">
								<table class="list" width="100%">
									<tbody id="exameeinfo_messageTbody"></tbody>
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
									<button id="timetableCls" type="button" class="close"
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