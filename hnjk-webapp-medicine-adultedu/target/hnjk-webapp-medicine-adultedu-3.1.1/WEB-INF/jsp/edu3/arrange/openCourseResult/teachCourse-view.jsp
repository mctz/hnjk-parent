<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<head>
<script language="javascript">
$(document).ready(function(){
	$("#tcourseView_classNames").val("${teachCourse.classNames}");
});

function deleteTeachClass(teachClassid){
	var teachCourseid = "${teachCourse.resourceid}";
	if(teachClassid!=null && teachClassid!="" && teachCourseid!=null){
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/arrange/teachCourseClasses/delete.html",
			data:{"resourceid":teachClassid,"teachCourseid":teachCourseid},
			dataType:"json",
			success:function(data){
				if(data.statusCode == 200){
					alertMsg.confirm(data.message);
					$("tr[id="+teachClassid+"]").remove();
				}else{
					alertMsg.error(data.message);
				}
			}}); 
	}else{
		alertMsg.warn("无法删除选中行！");
	}
};
</script>
</head>
<body>
	<div class="page">
	<div class="pageContent">
		<div class="pageFormContent" layoutH="97">	
			<table class="form">
				<tr>
					<td width="15%">教学班名：</td>
					<td width="35%"><input type="text" class="required" name="teachingClassname" style="width:80%" value="${teachCourse.teachingClassname}" /></td>
					
					<td width="15%">教学班号：</td>
					<td width="35%"><input type="text" class="required" name="teachingCode" style="width:80%" value="${teachCourse.teachingCode }" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>课程名称：</td>
					<td><input type="text" class="required" name="courseName" style="width:80%" value="${teachCourse.courseName}" /></td>
					
					<td>上课学期：</td>
					<td><gh:select name="openTerm" value="${teachCourse.openTerm }" dictionaryCode="CodeCourseTermType" style="width:80%"  disabled="true" /></td>
					<%-- <td><input type="text" class="required" name="openTerm" style="width:50%" value="${teachCourse.openTerm }" /></td> --%>
				</tr>
				<tr>
					<td>层次：</td>
					<td><gh:selectModel  name="classicid" bindValue="resourceid" displayValue="classicName"  disabled="true"
		 				modelClass="com.hnjk.edu.basedata.model.Classic" value="${teachCourse.classic.resourceid}" style="width:80%;"/></td>
					
					<td>学习形式：</td>
					<td><gh:select name="teachingtype" value="${teachCourse.teachingtype }" dictionaryCode="CodeTeachingType" style="width:80%" disabled="true"/></td>
				</tr>
				<tr>
					<td>学时：</td>
					<td><input type="text" class="required" name="studyHour" style="width:80%" value="${teachCourse.studyHour}" readonly="readonly"/></td>
					
					<td>考核形式：</td>
					<td><gh:select name="examClassType" value="${teachCourse.examClassType }" dictionaryCode="CodeCourseExamType" style="width:80%" disabled="true"/></td>
				</tr>
				<%-- <tr>
					<td>主讲老师：</td>
					<td><input type="text" name="teacherNames" style="width:50%" value="${teachCourse.teacherNames}" /></td>
					
					<td>登分老师：</td>
					<td><input type="text" name="teachingCode" style="width:50%" value="${teachCourse.teacherNames }" /></td>
				</tr> --%>
				<tr>
					<td>合班状态：</td>
					<td><gh:select name="status" value="${teachCourse.status }" dictionaryCode="CodeTeachClassesStatus" style="width:80%" disabled="true"/></td>
					
					<td>发布状态：</td>
					<td><gh:select name="publishStatus" value="${teachCourse.publishStatus }" dictionaryCode="CodePublishStatus" style="width:80%" disabled="true"/></td>
				</tr>
				<tr>
					<td>创建人：</td>
					<td><input type="text" name="operatorName" style="width:80%" value="${teachCourse.operatorName}" readOnly="true"/></td>
					
					<td>创建时间：</td>
					<td><input type="text" name="createDate" style="width:80%" value="<fmt:formatDate value="${teachCourse.createDate }" pattern="yyyy-MM-dd" />" readOnly="true"/></td>
				</tr>
				<tr>
					<td>班级信息：</td>
					<td><textarea id="tcourseView_classNames" name="classNames" style="width:80%" value="${teachCourse.classNames}" rows="3" readonly="readonly"/></td>
					
					<td>备注：</td>
					<td><textarea name="memo" style="width:80%" value="memo" rows="3" /></td>
				</tr>
				<tr><td colspan="5"></td></tr>
				<tr><td colspan="5" style="width: 100%">
					<table style="width: 100%" id="teachCourseView_classinfoForm">
						<thead>
							<tr><th colspan="5" style="font-size: medium;font-weight: bold;text-align: center;" >班级信息</th></tr>
							<tr>
								<th width="30%" style="text-align: center;">班级名称</th>
								<th width="20%" style="text-align: center;">上课结束日期</th>
								<th width="10%" style="text-align: center;">上课结束周</th>
								<th width="10%" style="text-align: center;">登分老师</th>
								<th width="10%" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<c:forEach items="${page.result }" var="class" varStatus="vs">
							<tr id="${class.resourceid}">
								<td style="text-align: center;">${class.classes.classname }<input type="hidden" name="teachClassid" value="${class.resourceid}"></td>
								<td style="text-align: center;"><input name="teachEndDate" value="<fmt:formatDate value="${class.teachEndDate }" pattern="yyyy-MM-dd"/>" class="date1" readonly="readonly"/></td>
								<td style="text-align: center;"><input name="teachEndWeek" value="${class.teachEndWeek }" class="digits" readonly="readonly"></td>
								<td style="text-align: center;">${class.recordScorerName }</td>
								<td style="text-align: center;"><button style="color: red;" onclick="deleteTeachClass('${class.resourceid}')">移除</button></td>
							</tr>
						</c:forEach>
					</table>
				</td></tr>
			</table>
		</div>
	</div>
	</div>
</body>
</html>