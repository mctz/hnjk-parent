<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>教学班级信息管理</title>
<script type="text/javascript">
function setTeach_classesInfo(){
	
	var resourceid =  $("#classesInfo_teachClassesid").val();
	var brSchoolid = $("#classesInfo_brSchoolid").val();
	var teacherids = $("#classesInfo_teacherids").val();
	var teachEndDate = $("#classesInfo_teachEndDate").val();
	var teachEndWeek = $("#classesInfo_teachEndWeek").val();
 	var url = "${baseUrl}/edu3/arrange/teachCourseClasses/selectteacher.html";
 	$.pdialog.open(url+"?resourceid="+resourceid+"&brSchoolid="+brSchoolid+"&teacherids="+teacherids+"&teachEndDate="+teachEndDate+"&teachEndWeek="+teachEndWeek,"classesInfoSelectTeacher","选择老师",{height:600,width:800,mask:true});
}
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty teachClasses.resourceid)?'新增':'编辑' }班级信息</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/teachCourseClasses/save.html" class="pageForm" onsubmit="return validateCallback(this);">
		<input type="hidden" id="classesInfo_teachClassesid" name="resourceid" value="${teachClasses.resourceid }"/>   
		<input type="hidden" id="classesInfo_teacherids" name="teacherids" value="${teacherids }" />
		<input type="hidden" id="classesInfo_brSchoolid" name="brSchoolid" value="${brSchoolid }" /> 
		<div class="pageFormContent" layoutH="97">
			<table class="form">
				<tr>
					<td width="20%">教学班：</td>
					<td width="30%"><input type="text" name="teachingClassname" style="width:50%" value="${teachClasses.teachCourse.teachingClassname }" readonly="readonly"/></td>
					<td width="20%">班级名称：</td>
					<td width="30%"><input type="text" name="classname" style="width:50%" value="${teachClasses.classes.classname }" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>课程：</td>
					<td><input type="text" name="courseName" style="width:50%" value="${teachClasses.course.courseName }" readonly="readonly"/></td>
					<td>排课状态：</td>
					<td><gh:select name="arrangeStatus" dictionaryCode="CodeArrangeStatus" value="${teachClasses.arrangeStatus}" style="width:50%" /></td>
				</tr>
				<tr>
					<td>上课结束日期：</td>
					<td><input type="text" id="classesInfo_teachEndDate" name="teachEndDate" size="40" style="width:50%" value="<fmt:formatDate value="${teachClasses.teachEndDate }" pattern="yyyy-MM-dd" />" class="required date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
					<td>上课结束周：</td>
					<td><input type="text" id="classesInfo_teachEndWeek" name="teachEndWeek" style="width:50%" value="${teachClasses.teachEndWeek }"  class="digits"/></td>
				</tr>
				<tr>
					<td>登分老师：</td>
					<td><input name="teacherNames" type="text" value="${teacherNames }" style="width:50%;" readonly="readonly" class="required" >
					<label></label><button type="button" onclick="setTeach_classesInfo();" class="">选择<c:if test="${not empty teacherNames}">其他</c:if>老师</button></td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>