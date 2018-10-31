<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年度设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑年度</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/sysmanager/yearinfo/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${yearInfo.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%">年度名称:</td>
							<td style="width: 38%"><input type="text" name="yearName"
								style="width: 50%" value="${yearInfo.yearName }"
								class="required" /></td>
							<td style="width: 12%">起始年份:</td>
							<td style="width: 38%"><input type="text" name="firstYear"
								style="width: 50%" value="${yearInfo.firstYear }"
								class="required digits" /></td>
						</tr>
						<tr>
							<td style="width: 12%">第一学期首周一日期:</td>
							<td style="width: 38%"><input type="text" id="d1"
								name="firstMondayOffirstTerm" style="width: 50%"
								value="${yearInfo.firstMondayOffirstTerm }" class="Wdate"
								onclick="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'d2\')}'})" />
							</td>
							<td>第一学期周数:</td>
							<td style="width: 38%"><input type="text"
								name="firstTermWeekNum" style="width: 50%"
								value="${yearInfo.firstTermWeekNum }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">第二学期首周一日期:</td>
							<td style="width: 38%"><input type="text" id="d2"
								name="firstMondayOfSecondTerm" style="width: 50%"
								value="${yearInfo.firstMondayOfSecondTerm }" class="Wdate"
								onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'d1\')}'})" />
							</td>
							<td style="width: 12%">第二学期周数:</td>
							<td style="width: 38%"><input type="text"
								name="secondTermWeekNum" style="width: 50%"
								value="${yearInfo.secondTermWeekNum }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 12%">报到注册日:</td>
							<td colspan="3">
							<input type="text" name="registrationDate" style="width: 50%"
								value="${yearInfo.registrationDate }" class="Wdate" onFocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})" />
						</tr>
						<tr>
							<td style="width: 12%">备注:</td>
							<td colspan="3"><textarea name="memo"
									style="height: 30px; width: 80%">${yearInfo.memo }</textarea></td>
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