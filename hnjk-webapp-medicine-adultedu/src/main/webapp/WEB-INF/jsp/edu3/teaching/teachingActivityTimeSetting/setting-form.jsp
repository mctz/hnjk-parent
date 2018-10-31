<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学活动环节中的时间设置</title>
</head>
<body>
	<h2 class="contentTitle">教学活动环节中的时间设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingactivitytimeSetting/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${setting.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"><gh:selectModel
									id="teachingactivitytimeSetting_form_yearInfoId"
									name="yearInfoId" bindValue="resourceid"
									displayValue="yearName"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${setting.yearInfo.resourceid}" orderBy="yearName desc"
									classCss="required" validate="Require" mes="年度"
									style="width:40%" /><font color="red">*</font></td>
							<td width="20%">学期：</td>
							<td width="30%"><gh:select
									id="teachingactivitytimeSetting_form_term" name="term"
									value="${setting.term}" dictionaryCode="CodeTerm"
									style="width:40%" classCss="required" /><font color="red">*</font></td>
						</tr>
						<tr>
							<td width="20%">开始时间:</td>
							<td width="30%"><input
								id="teachingactivitytimeSetting_form_startTime" type="text"
								value="<fmt:formatDate value='${setting.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
								name="startTime" class="required" readonly="readonly"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'teachingactivitytimeSetting_form_endTime\')}'})" /></td>
							<td width="20%">结束时间:</td>
							<td width="30%"><input
								id="teachingactivitytimeSetting_form_endTime"
								value="<fmt:formatDate value='${setting.endTime}' pattern='yyyy-MM-dd HH:mm:ss' />"
								type="text" name="endTime" class="required" readonly="readonly"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'teachingactivitytimeSetting_form_startTime\')}'})" /></td>
						</tr>
						<tr>
							<td width="20%">预警时间:</td>
							<td width="30%"><input
								id="teachingactivitytimeSetting_form_warningTime"
								value="<fmt:formatDate value='${setting.warningTime}' pattern='yyyy-MM-dd HH:mm:ss' />"
								type="text" name="warningTime" readonly="readonly"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'teachingactivitytimeSetting_form_startTime\')}',maxDate:'#F{$dp.$D(\'teachingactivitytimeSetting_form_endTime\')}'})" />
							</td>
							<td width="20%">业务类型：</td>
							<td width="30%"><gh:select
									id="teachingactivitytimeSetting_form_mainProcessType"
									name="mainProcessType" value="${setting.mainProcessType}"
									dictionaryCode="CodeTeachingActivity" style="width:40%"
									classCss="required" /><font color="red">*</font></td>
						</tr>
						<tr>
							<td width="20%">备注</td>
							<td width="30%" colspan="3"><textarea
									id="teachingactivitytimeSetting_form_memo" name="memo"
									rows="10" cols="100">${setting.memo}</textarea></td>
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
