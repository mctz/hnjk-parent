<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排考详情</title>
<script type="text/javascript">

	function refreshForm() {
        var brSchoolid = $("#examination_brSchoolid").val();
        var gradeid = $("#examination_gradeid").val();
        var classesid = $("#examination_classesid").val();
        var teachingPlanCourseid = $("#examination_teachingPlanCourseid").val();
        jQuery.ajax({
            data:{"brSchoolid":brSchoolid,"gradeid":gradeid,"classesid":classesid,"teachingPlanCourseid":teachingPlanCourseid},
            dataType:"json",
            url:"${baseUrl}/edu3/teaching/teachingplancourse/examinationUpdate.html",
            success:function(data){ //刷新数据
                //上课时间
                $("#examination_classesid").html(data.classesOption);
                $("#examination_teachingPlanCourseid").html(data.courseOption);
                //$("select[class*=flexselect]").flexselect();
            }
        });
    }

	function dialogAjaxDone1(json){//设置成功后，直接对话框及TAB，返回到排课列表
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			$.pdialog.closeCurrent();
			if (json.navTabId){
				//navTab.reload(json.reloadUrl,{},json.navTabId);
                navTab.closeCurrentTab();
                navTab.openTab(json.navTabId,json.reloadUrl,"排考详情");
			}
		}
	}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingplancourse/examinationSave.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone1);">
				<input type="hidden" name="resourceid" value="${examination.resourceid }" />
				<input type="hidden" name="operatorName" value="${examination.operatorName }" />
				<input type="hidden" name="classes_id" value="${classes.resourceid }" />
				<input type="hidden" name="plancourse_id" value="${planCourse.resourceid }" />
				<input type="hidden" name="stunumber" value="${stunumber }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<c:if test="${classes ne null}">
								<td width="10%">教学点：</td>
								<td width="40%">${classes.brSchool.unitName}
									<input type="hidden" name="brSchoolid" value="${classes.brSchool.resourceid}">
								</td>
								<td width="10%">年级：</td>
								<td width="40%">${classes.grade.gradeName}
									<input type="hidden" name="gradeid" value="${classes.grade.resourceid}">
								</td>
							</c:if>
							<c:if test="${classes eq null}">
								<td width="10%">教学点：</td>
								<td width="40%">
									<select class="flexselect" name="brSchoolid" id="examination_brSchoolid" onchange="refreshForm()" style="width:70%;">${unitOption}</select>
								</td>
								<td width="10%">年级：</td>
								<td width="40%"><gh:selectModel id="examination_gradeid" name="gradeid" bindValue="resourceid" displayValue="gradeName"
																value="${examination.classes.grade.resourceid}" modelClass="com.hnjk.edu.basedata.model.Grade" onchange="refreshForm()" />
								</td>
							</c:if>
						</tr>
						<tr>
							<td width="10%">班级：</td>
							<td width="40%">
								<select class="flexselect" name="classesid" id="examination_classesid" style="width:70%;" onchange="refreshForm()">${classesOption}</select>
								<%--<gh:selectModel id="examination_classesid" name="classesid" bindValue="resourceid" displayValue="classname" modelClass="com.hnjk.edu.roll.model.Classes"
												value="${examination.classes.resourceid }" condition="brSchool.resourceid='${brSchoolid}'" style="width:70%;" onchange="refreshForm()" />--%>
							</td>
							<td width="10%">课程：</td>
							<td width="40%">
								<select class="flexselect" name="plancourseid" id="examination_teachingPlanCourseid" style="width:70%;">${courseOption}</select>
							</td>
						</tr>
						<tr>
							<td>起止日期：</td>
							<td><input type="text"  name="startExamDate" size="40" style="width:50%" value="<fmt:formatDate value="${examination.startExamDate }" pattern="yyyy-MM-dd" />" class="required"
																						onFocus="WdatePicker({isShowWeek:true})"/> 至
								<input type="text"  name="endExamDate" size="40" style="width:50%" value="<fmt:formatDate value="${examination.endExamDate }" pattern="yyyy-MM-dd" />" class="required"
									   onFocus="WdatePicker({isShowWeek:true})"/></td>
							<td>时间段：</td>
							<td><input type='text' id="dialog_timeperiod_startTimePeriod" name='startTimePeriod_s'
									   value='<fmt:formatDate value="${examination.startTimePeriod }" pattern='HH:mm'/>'
									   style='width: 60%' class='required'
									   onFocus="WdatePicker({dateFmt:'HH:mm',maxDate:'#F{$dp.$D(\'dialog_timeperiod_endTimePeriod\')}'})"> 至
								<input type='text' id="dialog_timeperiod_endTimePeriod" name='endTimePeriod_s'
									   value='<fmt:formatDate value="${examination.endTimePeriod }" pattern='HH:mm'/>'
									   style='width: 60%' class='required'
									   onFocus="WdatePicker({dateFmt:'HH:mm',minDate:'#F{$dp.$D(\'dialog_timeperiod_startTimePeriod\')}'})">
							</td>
						</tr>
						<tr>
							<td>课室：</td>
							<td><input type="text" name="classroom" style="width: 70%;"
									   value="${examination.classroom }" />
							</td>
							<td>地点：</td>
							<td><input type="text" name="location" style="width: 70%;"
									   value="${examination.location }" /></td>
						</tr>
						<tr>
							<td>人数：</td>
							<td><input type="text" name="studentNum" class="digits"
									   value="${examination.studentNum }" /></td>

							<td>监考教师：</td>
							<td>
								<input type="text" name="teacher"
									   value="${examination.teacher }" />
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="3"><input type="text" maxlength="255"
								name="memo" value="${examination.memo }" style="width: 80%;" /></td>
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