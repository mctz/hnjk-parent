<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课详情</title>
<script type="text/javascript">
	$(document).ready(function(){
		// $("#dialog_timetable_classroomid").attr("class","flexselect");
		$("select[class*=flexselect]").flexselect();
	});
	function dialogAjaxDone1(json){//排课页面，设置成功后，直接对话框及TAB，返回到排课列表
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			$.pdialog.closeCurrent();
			if (json.navTabId){
				navTab.closeCurrentTab();
			}
		}
	}
	</script>
</head>
<body>
	<h2 class="contentTitle">${empty timetable.resourceid ? '新增' : '调整' }排课详情</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingplancoursetimetable/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone1);">
				<input type="hidden" name="resourceid" value="${timetable.resourceid }" /> 
				<input type="hidden" name="term" value="${timetable.term }" /> 
				<input type="hidden" name="classesid" value="${timetable.classes.resourceid }" />
				<input type="hidden" name="plancourseid" value="${timetable.teachingPlanCourse.resourceid }" />
				<input type="hidden" name="operatorName" value="${timetable.operatorName }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="10%">班级:</td>
							<td width="40%">${timetable.classes.classname }</td>
							<td width="10%">课程:</td>
							<td width="40%">${timetable.course.courseName }</td>
						</tr>
						<tr>
							<td>上课老师:</td>
							<td><select id="dialog_timetable_teacherId" name="teacherId"
								style="width: 60%;" class="flexselect required">
									<option value=""></option>
									<c:forEach items="${edumanagerList }" var="t">
										<option value="${t.resourceid }"
											${t.resourceid eq timetable.teacherId?'selected=selected':''}>${t.cnName}_${t.teacherCode}</option>
									</c:forEach>
							</select> <span
								style="color: red; line-height: 22px; vertical-align: middle; font-weight: bolder; padding-left: 2px">${fn:length(edumanagerList)>0?'':'请先设置任课老师'}</span>
							</td>
							<td>上课地点:</td>
							<td><gh:selectModel id="dialog_timetable_classroomid"
									defaultOptionText="" name="classroomid" bindValue="resourceid"
									displayValue="classroomName"
									modelClass="com.hnjk.edu.basedata.model.Classroom"
									classCss="flexselect  required"
									value="${timetable.classroom.resourceid }"
									condition="building.branchSchool.resourceid='${timetable.classes.brSchool.resourceid }'"
									orderBy="building,showOrder,resourceid" style="width:60%;" /></td>
						</tr>
						<tr>
							<td>上课日期:</td>
							<td><input type="text" name="teachDate"
								value="${timetable.teachDate }" class="required" /></td>
							<td>上课星期:</td>
							<td><gh:select dictionaryCode="CodeWeek"
									id="dialog_timetable_week" name="week" classCss="required"
									value="${timetable.week }" style="width:120px;" /><span
								style="color: red;">*</span></td>
							<%--
					<td>上课时间段:</td>
					<td>					
					<gh:select dictionaryCode="CodeCourseTimePeriod" id="dialog_timetable_timePeriod" name="timePeriod" classCss="required" value="${timetable.timePeriod }" style="width:120px;"/>
					</td> --%>
						</tr>
						<tr>
							<td>上课时间段:</td>
							<td colspan="3"><gh:selectModel bindValue="resourceid"
									displayValue="courseTimeName"
									modelClass="com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod"
									orderBy="brSchool.unitCode,timePeriod,startTime,resourceid"
									name="timePeriodid" id="timetable_timePeriodid"
									value="${timetable.unitTimePeriod.resourceid }"
									style="width:50%;" classCss="required"
									condition="brSchool.resourceid='${timetable.classes.brSchool.resourceid }'" />
								<span style="color: red;">*</span></td>
						</tr>
						<c:if test="${not empty timetable.resourceid }">
							<tr>
								<td>是否停课:</td>
								<td colspan="3"><gh:select dictionaryCode="yesOrNo"
										id="dialog_timetable_isStoped" name="isStoped"
										value="${timetable.isStoped }" style="width:120px;" /></td>
							</tr>
						</c:if>
						<c:if test="${empty timetable.resourceid }">
							<input type="hidden" value="N" name="isStoped"
								id="dialog_timetable_isStoped" />
						</c:if>
						<tr>
							<td>临时调课备注:</td>
							<td colspan="3"><input type="text" maxlength="255"
								name="memo" value="${timetable.memo }" style="width: 80%;" /></td>
						</tr>
						<tr>
							<td>合班情况:</td>
							<td colspan="3"><textarea name="mergeMemo" rows="3" maxlength="255" style="width: 80%;">
								${timetable.mergeMemo }</textarea></td>
						</tr>
						<%-- 
				<tr>
					<td width="15%">上课开始时间:</td>
					<td width="35%">
						<input type='text' name='startTimeStr' value='<fmt:formatDate value="${timetable.startTime }" pattern='HH:mm'/>' style='width:60%' class='required' id='dialog_timetable_startTime' onFocus="WdatePicker({dateFmt:'HH:mm',maxDate:'#F{$dp.$D(\'dialog_timetable_endTime\')}'})">
					</td>
					<td width="15%">上课结束时间:</td>
					<td width="35%">
						<input type='text' name='endTimeStr' value='<fmt:formatDate value="${timetable.endTime }" pattern='HH:mm'/>' style='width:60%' class='required' id='dialog_timetable_endTime' onFocus="WdatePicker({dateFmt:'HH:mm',minDate:'#F{$dp.$D(\'dialog_timetable_startTime\')}'})">
					</td>
				</tr>
				 --%>
						<tr>
							<td>登分老师：</td>
							<td colspan="3">${teacherName }</td>
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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>