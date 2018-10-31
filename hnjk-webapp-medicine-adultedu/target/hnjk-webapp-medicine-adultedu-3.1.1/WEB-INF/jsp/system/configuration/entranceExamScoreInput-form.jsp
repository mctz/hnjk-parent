<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学考试成绩录入人设定</title>
<script type="text/javascript">
 
    //选择用户
   	function selectEntranceExamScoreUserList(){
   		$.pdialog.open('${baseUrl }/edu3/system/configuration/entranceExamScoreInput-select-user.html','entranceExamScoreInputSelectUser','选择用户',{mask:true,width:800,height:600});
   	}
</script>
</head>
<body>
	<h2 class="contentTitle">设定${sysConfiguration.memo }</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/configuration/entranceExamScoreInput-save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${sysConfiguration.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="18%">参数名称:</td>
							<td width="82%" colspan="3"><input type="text"
								name="paramName" style="width: 20%"
								value="${sysConfiguration.paramName }" readonly="readonly"
								class="required" /> <input type="hidden"
								" id="paramCode_entranceExamScoreInput" name="paramCode"
								style="width: 50%" value="${sysConfiguration.paramCode }"
								class="required" /></td>
						</tr>
						<tr>
							<td width="18%">录入人:</td>
							<td width="82%"><input type="hidden"
								" id="paramValue_entranceExamScoreInput" name="paramValue"
								value="${sysConfiguration.paramValue }" /> <input type="text"
								id="paramValue_entranceExamScoreInput_show"
								name="paramValue_show" style="width: 20%"
								value="${ghfn:ids2Names('user',sysConfiguration.paramValue) }"
								class="required" readonly="readonly" /> <input type="hidden"
								" name="memo" value="${sysConfiguration.memo }" /> <span
								class="buttonActive" style="margin-left: 20px"><div
										class="buttonContent">
										<button type="button"
											onclick="selectEntranceExamScoreUserList()">选择用户</button>
									</div></span></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>

</body>
</html>