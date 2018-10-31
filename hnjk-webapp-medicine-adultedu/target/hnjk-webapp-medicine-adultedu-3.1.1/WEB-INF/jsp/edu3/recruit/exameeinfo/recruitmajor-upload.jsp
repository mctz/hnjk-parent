<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审批招生专业</title>
<script type="text/javascript">	   
	   function _recruitMajorAuditValidateCallback(form){		   
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,dialogAjaxDone);
	   }	  
	</script>
</head>
<body>
	<h2 class="contentTitle">上传计划库文件并审批招生专业</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitmajor/audit.html"
				class="pageForm"
				onsubmit="return _recruitMajorAuditValidateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">招生计划:</td>
							<td colspan="3">${recruitPlan.recruitPlanname }<input
								type="hidden" name="planid" value="${recruitPlan.resourceid }" /></td>
						</tr>
						<tr>
							<td width="20%">计划库文件:</td>
							<td colspan="3"><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="RecruitMajorImport"
									formId="${recruitPlan.resourceid }" fileExt="dbf" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">审批</button>
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