<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网上学习时间设置</title>
</head>
<body>
	<h2 class="contentTitle">网上学习时间设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/learningtimesetting/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${learningTimeSetting.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">年度：</td>
							<td width="35%"><gh:selectModel
									id="learningTimeSetting_form_yearInfoId" name="yearInfoId"
									bindValue="resourceid" displayValue="yearName"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${learningTimeSetting.yearInfo.resourceid}"
									orderBy="firstYear desc" style="width:50%;" classCss="required" />
								<span style="color: red;">*</span></td>
							<td width="15%">学期：</td>
							<td width="35%"><gh:select
									id="learningTimeSetting_form_term" name="term"
									value="${learningTimeSetting.term }" dictionaryCode="CodeTerm"
									classCss="required" /> <span style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>开始时间：</td>
							<td><input type='text' name='startTime'
								value='<fmt:formatDate value="${learningTimeSetting.startTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 50%' class='required'
								id='learningTimeSettingStartTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'learningTimeSettingEndTime\')}',readOnly:true})">
							</td>
							<td>截止时间：</td>
							<td><input type='text' name='endTime'
								value='<fmt:formatDate value="${learningTimeSetting.endTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 50%' class='required'
								id='learningTimeSettingEndTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'learningTimeSettingStartTime\')}',readOnly:true})">
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="3"><textarea rows="3" cols="10"
									style="width: 50%;">${learningTimeSetting.memo }</textarea></td>
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
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>