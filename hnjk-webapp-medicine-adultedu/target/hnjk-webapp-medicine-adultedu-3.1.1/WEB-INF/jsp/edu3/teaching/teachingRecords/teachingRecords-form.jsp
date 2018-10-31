<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学记录管理</title>
</head>
<body>
	<script type="text/javascript">
	function freshForm(){
		var brSchoolId =  $("#teachingRecords_brSchoolId").val();
		var majorId = $("#teachingRecords_majorId").val();
		var resid =  "${teachRecord.resourceid}";
		navTab.openTab('RES_TEACHING_TEACHINGRECORDS_INPUT', '${baseUrl}/edu3/teaching/teachingrecords/input.html?brSchoolId='+brSchoolId+'&majorId='+majorId+'&resourceid='+resid, '教学记录管理');
		//('${baseUrl}/edu3/teaching/teachingrecords/input.html', $("#teachingRecordsForm").serializeArray(), RES_TEACHING_TEACHINGRECORDS_INPUT);
	}
</script>
	<h2 class="contentTitle">${(empty teachRecord.resourceid)?'新增':'编辑' }教学记录</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" id="teachingRecordsForm"
				action="${baseUrl}/edu3/teaching/teachingrecords/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${teachRecord.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">教学点：</td>
							<td width="30%"><gh:selectModel
									id="teachingRecords_brSchoolId" name="brSchoolId"
									bindValue="resourceid" displayValue="unitName"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${teachRecord.unit.resourceid}"
									condition=" unitType='brSchool' and  isDeleted=0 "
									style="width:50%" classCss="required"
									disabled="${brschool?'disabled':'' }" onchange="freshForm()" /><font
								color="red">*</font> <c:if test="${brschool }">
									<input type="hidden" name="brSchoolId"
										value="${teachRecord.unit.resourceid}" />
								</c:if></td>
							<td width="20%">年级：</td>
							<td width="30%"><gh:selectModel bindValue="resourceid"
									displayValue="gradeName"
									modelClass="com.hnjk.edu.basedata.model.Grade"
									condition=" isDeleted=0 "
									value="${teachRecord.grade.resourceid}" name="gradeId"
									style="width:50%" classCss="required" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td>专业：</td>
							<td><gh:selectModel id="teachingRecords_majorId"
									name="majorId" bindValue="resourceid"
									displayValue="majorCode,majorName" classCss="required"
									condition=" isDeleted=0 " onchange="freshForm()"
									modelClass="com.hnjk.edu.basedata.model.Major"
									value="${teachRecord.major.resourceid}"
									orderBy="majorName desc" style="width:50%" /></td>
							<td>课程：</td>
							<td><gh:courseAutocomplete name="courseId"
									id="teachingRecords_courseId" tabindex="1" displayType="code"
									style="width:50%" classCss="required"
									value="${teachRecord.planCourse.course.resourceid }"
									condition="resourceid in('${courseIds }')"></gh:courseAutocomplete></td>
						</tr>
						<tr>
							<td>授课教师：</td>
							<td><gh:selectModel name="teacherId" bindValue="resourceid"
									displayValue="teacherCode,cnName"
									modelClass="com.hnjk.security.model.User" style="width:50%"
									classCss="required" value="${teachRecord.teacher.resourceid}"
									condition=" orgUnit.resourceid='${teachRecord.unit.resourceid }' and userType='edumanager'"
									orderBy="cnName" /></td>
							<td>教学手段：</td>
							<td><input type="text" name="teachType" style="width: 50%"
								value="${teachRecord.teachType }" /></td>
						</tr>
						<tr>
							<td>周次：</td>
							<td><input type="text" name="week" style="width: 50%"
								value="${teachRecord.week }" class="required digits" /></td>
							<td>日期：</td>
							<td><input type="text" name="timeperiod" size="40"
								style="width: 50%"
								value="<fmt:formatDate value="${teachRecord.timeperiod }" pattern="yyyy-MM-dd" />"
								class="required" onFocus="WdatePicker()" /></td>
						</tr>
						<tr>
							<td>地点：</td>
							<td><input type="text" name="classroom" style="width: 50%"
								value="${teachRecord.classroom }" /></td>
							<td>理论教学内容：</td>
							<td colspan="3"><input type="text" name="contents"
								value="${teachRecord.contents }" style="width: 80%" /></td>
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