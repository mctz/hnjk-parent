<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑时间设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="${baseUrl}/edu3/system/settime/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${setTime.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">业务类型:</td>
							<td width="35%"><gh:select name="businessType"
									value="${setTime.businessType}"
									dictionaryCode="CodeBusinessType" style="width:52%"
									classCss="required" /></td>
							<td width="15%">开始时间:</td>
							<td width="35%"><input type="text" id="settime_beginDate"
								name="beginDate"
								value='<fmt:formatDate value="${setTime.beginDate  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="setBeginDate()" class="required" /></td>
						</tr>
						<tr>
							<td>结束时间:</td>
							<td><input type="text" id="settime_endDate" name="endDate"
								value='<fmt:formatDate value="${setTime.endDate  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="setEndDate()" class="required" /></td>
							<td>预警时间:</td>
							<td><input type="text" id="settime_warnDate" name="warnDate"
								value='<fmt:formatDate value="${setTime.warnDate  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="return setWarnTime()" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" style="width: 50%"
									rows="3">${setTime.memo }</textarea></td>
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
	<script type="text/javascript">
		function setBeginDate(){
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'settime_endDate\')}'});
			var beginDate = $dp.$('settime_beginDate').value;
			var warnDate = $dp.$('settime_warnDate').value;
			if(beginDate>warnDate){
				$dp.$('settime_warnDate').value="";
			}
		}
		
		function setEndDate(){
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'settime_beginDate\')}'});
			var endDate = $dp.$('settime_endDate').value;
			var warnDate = $dp.$('settime_warnDate').value;
			if(endDate<warnDate){
				$dp.$('settime_warnDate').value="";
			}
		}
		
		function setWarnTime(){
			var beginDate = $dp.$('settime_beginDate').value;
			var endDate = $dp.$('settime_endDate').value;
			if(beginDate=="" || endDate==""){
				alertMsg.warn("请先设置开始时间和结束时间！");
				return false;
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'settime_beginDate\')}',maxDate:'#F{$dp.$D(\'settime_endDate\')}'});
		}
	</script>
</body>
</html>