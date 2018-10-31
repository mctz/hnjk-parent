<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>延迟毕业申请时间段-<c:if test="${method=='new'}">新增</c:if><c:if
		test="${method=='modify'}">修改</c:if></title>
<script type="text/javascript">
function saveGraduateNoSetting(){
	var gNSettingMethod= $("#gNSettingMethod").val();
	var yearInfo 		= $("#GraduateNoSetting #yearInfo").val();
	var term       		= $("#GraduateNoSetting #term").val();
	var beginDate 	= $("#GraduateNoSetting input[name='startDate'] ").val();
	var endDate    	= $("#GraduateNoSetting input[name='endDate'] ").val();
	var revokeDate  = $("#GraduateNoSetting input[name='revokeDate'] ").val();
	var resourceid   = $("#GraduateNoSetting input[name='resourceid'] ").val();
	var condition = "&yearInfo="+yearInfo+"&term="+term+"&beginDate="+beginDate+"&endDate="+endDate+"&revokeDate="+revokeDate+"&resourceid="+resourceid;
	var postUrl = "${baseUrl}//edu3/roll/graduateNoSetting/checkBeforeSave.html?type="+gNSettingMethod+condition;
	$.ajax({
		type:"post",
		url:postUrl,
		dataType:"json",
		success:function(data){
			if(data['isOk']==true){
				var postUrl2 = "${baseUrl}//edu3/roll/graduateNoSetting/doSave.html?type="+gNSettingMethod+condition;
				$.ajax({
					type:"post",
					url:postUrl2,
					dataType:"json",
					success:function(data){
							alertMsg.warn(data['result']);
							$("#graduataNoTimeSubmit").hide();
					}
				});
			}else{
				alertMsg.warn(data['reason']);
			}
			
		}
	});
}
</script>
</head>
<body>
	<h2 class="contentTitle">
		<c:if test="${method=='new'}">新增</c:if>
		<c:if test="${method=='modify'}">修改</c:if>
		延迟毕业申请时间段
	</h2>
	<div class="page">
		<div class="pageContent">
			<form id="saveGraduateNoSetting" method="post"
				action="${baseUrl}/edu3/roll/graduateNoSetting/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" id="gNSettingMethod" name="method"
					value="${method}" />.

				<div class="pageFormContent" id="GraduateNoSetting" layoutH="97">
					<input type="hidden" name="resourceid"
						value="${graduateNoSetting.resourceid }" />
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"><gh:selectModel id="yearInfo"
									name="yearInfo" bindValue="resourceid" displayValue="yearName"
									style="width:50%"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${graduateNoSetting.yearInfo.resourceid}"
									classCss="required" validate="Require" mes="年度" /><font
								color="red">*</font></td>
							<td width="20%">学期：</td>
							<td width="30%"><gh:select id="term" name="term"
									value="${graduateNoSetting.term}" dictionaryCode="CodeTerm" /><font
								color="red">*</font></td>
						</tr>
						<tr>
							<td width="20%">开始申请时间:</td>
							<td width="30%"><input type="text"
								value="<fmt:formatDate value='${graduateNoSetting.startDate}' pattern='yyyy-MM-dd'/>"
								name="startDate" class="required" readonly="readonly"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
							<td width="20%">结束申请时间:</td>
							<td width="30%"><input
								value="<fmt:formatDate value='${graduateNoSetting.endDate}' pattern='yyyy-MM-dd' />"
								type="text" name="endDate" class="required" readonly="readonly"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
						</tr>
						<tr>
							<td width="20%">自动撤销时间:</td>
							<td width="30%"><input type="text"
								value="<fmt:formatDate value='${graduateNoSetting.revokeDate}' pattern='yyyy-MM-dd'/>"
								name="revokeDate" class="required" readonly="readonly"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
							<td width="20%"></td>
							<td width="30%"></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<!--  <li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>-->
						<li><div id="graduataNoTimeSubmit" class="button">
								<div class="buttonContent">
									<button type="button" onclick="saveGraduateNoSetting()">提交</button>
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