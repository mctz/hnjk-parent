<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入报名信息</title>
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
		   return validateCallback(form,function (json){
			   if(json.statusCode == 300) {
					alertMsg.error(json.message);
				} else if (json.statusCode == 200){
					alertMsg.confirm(json.message);
					if (json.reloadUrl){
						navTab.reload(json.reloadUrl, {recruitPlanId:json.planid}, json.navTabId);
					}
					var $table = $("#exameeinfo_messageTbody");
					$table.html("");//清空
					$table.html("<tr><td class='success'>"+json.message+"</td></tr>");//清空
					if(json.errorMessageList){//存在导入失败日志,输出						
						var arr = [];
						arr.push("<tr><td>导入失败数据及原因：</td></tr>");
						$.each(json.errorMessageList, function(i, n){
							arr.push("<tr><td class='error'>"+n+"</td></tr>"); 
						});						
						$table.html($table.html()+arr.join(""));						
					} else {
						$.pdialog.closeCurrent();
					}					
				}
			});
	   }	  
	</script>
</head>
<body>
	<h2 class="contentTitle">导入报名信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/exameeinfo/import.html"
				class="pageForm"
				onsubmit="return _exameeinfoValidateCallback(this);">
				<input type="hidden" name="isEnrolExam" value="${isEnrolExam }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">招生计划:</td>
							<td><gh:selectModel id="exameeinfo_planid" name="planid"
									bindValue="resourceid" displayValue="recruitPlanname"
									classCss="required"
									orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
									modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
									style="width:55%;" condition="isPublished='Y'" /> <span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td width="20%">报名信息文件:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="ExameeInfoImport" fileExt="dbf" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
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
									<button type="submit" style="cursor: pointer;">导入</button>
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