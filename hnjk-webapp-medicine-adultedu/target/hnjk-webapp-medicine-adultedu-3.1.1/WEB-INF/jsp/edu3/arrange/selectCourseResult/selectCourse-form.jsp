<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑教学班</title>
<style type="text/css">
    .recordScoreTeacher{cursor: pointer;}
</style>
<script language="javascript">
$(document).ready(function(){
	$("#selectCourse_classNames").val("${teachCourse.classNames}");memo
	$("#selectCourse_memo").val("${teachCourse.memo}");
});

/* //给特定某条记录设置登分老师
function  setRecordScordTeach(obj){
	var targetObject = $(obj).parent().find("input[name='resourceid']:first");
	var resIds = targetObject.val();
	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/selectteacher.html";
	$.pdialog.open(url+"?teachClassesid="+resIds+"&teachtype=record"
			,"SelectTeacher","选择登分老师",{height:600,width:800});
} */
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty teachCourse.resourceid)?'新增':'编辑' }教学班</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/selectcourseresult/save.html" class="pageForm" onsubmit="return validateCallback(this);" id="teachCourse_edit">
		<input type="hidden" name="resourceid" value="${teachCourse.resourceid }"/>     
		<div class="pageFormContent" layoutH="97">
			<table class="form" id="teachCourseTable">
				<tr>
					<td width="20%">教学班名：</td>
					<td width="30%"><input type="text" class="required" name="teachingClassname" style="width:50%" value="${teachCourse.teachingClassname}" /></td>
					
					<td width="20%">教学班号：</td>
					<td width="30%"><input type="text" class="required" name="teachingCode" style="width:50%" value="${teachCourse.teachingCode }" readonly="readonly"/></td>
				</tr>
				<tr>
					<td width="20%">课程名称：</td>
					<td width="30%"><input type="text" class="required" name="courseName" style="width:50%" value="${teachCourse.courseName}" /></td>
					
					<td width="20%">上课学期：</td>
					<td width="30%"><gh:select name="openTerm" value="${teachCourse.openTerm }" dictionaryCode="CodeCourseTermType" style="width:50%"  classCss="required" disabled="true" /></td>
					<%-- <td width="30%"><input type="text" class="required" name="openTerm" style="width:50%" value="${teachCourse.openTerm }" /></td> --%>
				</tr>
				<tr>
					<td width="20%">层次：</td>
					<td width="30%"><gh:selectModel  name="classicid" bindValue="resourceid" displayValue="classicName"  disabled="true" 
		 				modelClass="com.hnjk.edu.basedata.model.Classic" value="${teachCourse.classic.resourceid}" style="width:50%;"/></td>
					
					<td width="20%">学习形式：</td>
					<td width="30%"><gh:select name="teachingtype" value="${teachCourse.teachingtype }" dictionaryCode="CodeTeachingType" style="width:50%" classCss="required" disabled="true"/></td>
				</tr>
				<tr>
					<td width="20%">学时：</td>
					<td width="30%"><input type="text" class="required" name="studyHour" style="width:50%" value="${teachCourse.studyHour}" /></td>
					
					<td width="20%">考核形式：</td>
					<td width="30%"><gh:select name="examClassType" value="${teachCourse.examClassType }" dictionaryCode="CodeCourseExamType" style="width:50%" classCss="required" disabled="true"/></td>
				</tr>
				<%-- <tr>
					<td width="20%">主讲老师：</td>
					<td width="30%"><input type="text" name="teacherNames" style="width:50%" value="${teachCourse.teacherNames}" /></td>
					
					<td width="20%">登分老师：</td>
					<td width="30%"><input type="text" name="teachingCode" style="width:50%" value="${teachCourse.teacherNames }" /></td>
				</tr> --%>
				<tr>
					<td width="20%">合班状态：</td>
					<td width="30%"><gh:select name="status" value="${teachCourse.status }" dictionaryCode="CodeTeachClassesStatus" style="width:50%" classCss="required" disabled="true"/></td>
					
					<td width="20%">发布状态：</td>
					<td width="30%"><gh:select name="publishStatus" value="${teachCourse.publishStatus }" dictionaryCode="CodePublishStatus" style="width:50%" classCss="required" disabled="true"/></td>
				</tr>
				<tr>
					<td width="20%">创建人：</td>
					<td width="30%"><input type="text" name="operatorName" style="width:50%" value="${teachCourse.operatorName}" readOnly="true"/></td>
					
					<td width="20%">创建时间：</td>
					<td width="30%"><input type="text" name="createDate" style="width:50%" value="<fmt:formatDate value="${teachCourse.createDate }" pattern="yyyy-MM-dd" />" readOnly="true"/></td>
				</tr>
				<tr>
					<td width="20%">班级信息：</td>
					<td width="30%"><textarea id="selectCourse_classNames" name="classNames" style="width:50%" value="${teachCourse.classNames}" rows="3" readonly="readonly"/></td>
					
					<td width="20%">备注：</td>
					<td width="30%"><textarea name="selectCourse_memo" style="width:50%" value="${teachCourse.memo}" rows="3"/></td>
				</tr>
				<tr><td colspan="4"></td></tr>
				<tr><td colspan="4" style="width: 100%">
				<table style="width: 100%">
					
					<thead>
						<tr><th colspan="4" style="font-size: medium;font-weight: bold;text-align: center;" >班级信息</th></tr>
						<tr><th width="35%" style="text-align: center;">班级名称</th><th width="25%" style="text-align: center;">上课结束日期</th>
						<th width="20%" style="text-align: center;">上课结束周</th><th width="20%" style="text-align: center;">登分老师</th></tr>
					</thead>
					<c:forEach items="${page.result }" var="class" varStatus="vs">
						<tr>
							<td style="text-align: center;">${class.classes.classname }<input type="hidden" name="teachClassid" value="${class.resourceid}"></td>
							<td style="text-align: center;"><input name="teachEndDate" value="<fmt:formatDate value="${class.teachEndDate }" pattern="yyyy-MM-dd"/>" class="date1" 
								onFocus="WdatePicker({isShowWeek:true})"/></td>
							<td style="text-align: center;"><input name="teachEndWeek" value="${class.teachEndWeek }" class="digits"></td>
							<td style="text-align: center;">${class.recordScorerName }</td>
							<%-- <td style="cursor: pointer;text-align: center;" onclick="setRecordScordTeach(this)" id = "setteacherid" >
			           			<a href="javaScript:void(0)"  class="recordScoreTeacher"  >${class.recordScorerName}</a>
		           			</td> --%>
						</tr>
					</c:forEach>
				</table>
				</td></tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>