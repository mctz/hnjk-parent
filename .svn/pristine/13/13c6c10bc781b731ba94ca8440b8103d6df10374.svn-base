<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置上课时间</title>
</head>
<body>
	<h2 class="contentTitle">设置上课时间</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/timeperiod/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${timePeriod.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>教学站:</td>
							<td colspan="3"><c:choose>
									<c:when
										test="${not isBrschool and empty timePeriod.resourceid}">
										<gh:brSchoolAutocomplete name="brSchoolid" tabindex="1"
											id="dialog_timeperiod_brSchoolid" style="width:50%;"
											displayType="code"></gh:brSchoolAutocomplete>
									</c:when>
									<c:otherwise>
						${timePeriod.brSchool.unitName }
						<input type="hidden" name="brSchoolid"
											value="${timePeriod.brSchool.resourceid }" />
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td>上课时间名称:</td>
							<td><input type="text" name="timeName"
								value="${timePeriod.timeName }" class="required" /></td>
							<td>上课时间段:</td>
							<td><gh:select dictionaryCode="CodeCourseTimePeriod"
									id="dialog_timeperiod_timePeriod" name="timePeriod"
									classCss="required" value="${timePeriod.timePeriod }"
									style="width:120px;" /></td>
						</tr>
						<tr>
							<td width="15%">上课开始时间:</td>
							<td width="35%"><input type='text' name='startTime'
								value='<fmt:formatDate value="${timePeriod.startTime }" pattern='HH:mm'/>'
								style='width: 60%' class='required'
								id='dialog_timeperiod_startTime'
								onFocus="WdatePicker({dateFmt:'HH:mm',maxDate:'#F{$dp.$D(\'dialog_timeperiod_endTime\')}'})">
							</td>
							<td width="15%">上课结束时间:</td>
							<td width="35%"><input type='text' name='endTime'
								value='<fmt:formatDate value="${timePeriod.endTime }" pattern='HH:mm'/>'
								style='width: 60%' class='required'
								id='dialog_timeperiod_endTime'
								onFocus="WdatePicker({dateFmt:'HH:mm',minDate:'#F{$dp.$D(\'dialog_timeperiod_startTime\')}'})">
							</td>
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