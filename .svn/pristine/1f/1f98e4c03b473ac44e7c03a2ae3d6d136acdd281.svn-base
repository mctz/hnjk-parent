<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生在线时长列表</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		studentLoginLongBegin();
	});
	
	// 打开页面或者点击查询（即加载页面执行）
   function studentLoginLongBegin() {
	   var defaultValue = "${condition['branchSchool']}";
	   var schoolId = "${unitId}";
	   var gradeId = "${condition['gradeid']}";
	   var classicId = "${condition['classic']}";
	   var teachingType = "${condition['schoolType']}";
	   var majorId = "${condition['major']}";
	   var classesId = "${condition['classesid']}";
	   var selectIdsJson = "{unitId:'loginLongList_student-brSchoolId',gradeId:'loginLongList_student-gradeId',classicId:'loginLongList_student-classicId',"
	 							  +"teachingType:'loginLongList_student-teachingType',majorId:'loginLongList_student-majorId',classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, classesId, selectIdsJson);
   }
	// 选择教学点
   function studentLoginLongUnit() {
	   var defaultValue = $("#loginLongList_student-brSchoolId").val();
	   var selectIdsJson = "{gradeId:'loginLongList_student-gradeId',classicId:'loginLongList_student-classicId',"
			  					  +"teachingType:'loginLongList_student-teachingType',majorId:'loginLongList_student-majorId',classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
   }
	// 选择年级
   function studentLoginLongGrade() {
	   var defaultValue = $("#loginLongList_student-brSchoolId").val();
	   var gradeId = $("#loginLongList_student-gradeId").val();
	   var selectIdsJson = "{classicId:'loginLongList_student-classicId',teachingType:'loginLongList_student-teachingType',majorId:'loginLongList_student-majorId',"
		   						  +"classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
   }
	// 选择层次
   function studentLoginLongClassic() {
	   var defaultValue = $("#loginLongList_student-brSchoolId").val();
	   var gradeId = $("#loginLongList_student-gradeId").val();
	   var classicId = $("#loginLongList_student-classicId").val();
	   var selectIdsJson = "{teachingType:'loginLongList_student-teachingType',majorId:'loginLongList_student-majorId',classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
   }
	// 选择学习形式
   function studentLoginLongTeachingType() {
	   var defaultValue = $("#loginLongList_student-brSchoolId").val();
	   var gradeId = $("#loginLongList_student-gradeId").val();
	   var classicId = $("#loginLongList_student-classicId").val();
	   var teachingTypeId = $("#loginLongList_student-teachingType").val();
	   var selectIdsJson = "{majorId:'loginLongList_student-majorId',classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("teachingType", defaultValue, "", gradeId, classicId, teachingTypeId, "", "", selectIdsJson);
   }
   //选择专业
   function studentLoginLongMajor(){
	   var defaultValue = $("#loginLongList_student-brSchoolId").val();
	   var gradeId = $("#loginLongList_student-gradeId").val();
	   var classicId = $("#loginLongList_student-classicId").val();
	   var teachingTypeId = $("#loginLongList_student-teachingType").val();
	   var majorId = $("#loginLongList_student-majorId").val();
	   var selectIdsJson = "{classesId:'loginLongList_student-classesId'}";
	   cascadeQuery("classes", defaultValue, "", gradeId, classicId, teachingTypeId, majorId, "", selectIdsJson);
   }
</script>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingManagement/loginLongList_student.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span
							sel-id="loginLongList_student-brSchoolId" sel-name="branchSchool"
							sel-onchange="studentLoginLongUnit()" sel-classs="flexselect"
							></span></li>
						<li><label>年级：</label> <span
							sel-id="loginLongList_student-gradeId" sel-name="gradeid"
							sel-onchange="studentLoginLongGrade()" sel-style="width: 53%"></span>
						</li>
						<li><label>层次：</label> <span
							sel-id="loginLongList_student-classicId" sel-name="classic"
							sel-onchange="studentLoginLongClassic()" sel-style="width: 53%"></span>
						</li>
						<li><label>办学模式：</label> <span
							sel-id="loginLongList_student-teachingType" sel-name="schoolType"
							sel-onchange="studentLoginLongTeachingType()"
							sel-style="width: 100px;"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="loginLongList_student-majorId" sel-name="major"
							sel-onchange="studentLoginLongMajor()" sel-classs="flexselect"
							></span></li>
						
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" style="width: 53%" /></li>
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name'] }" style="width: 53%" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
						sel-id="loginLongList_student-classesId" sel-name="classesid"
						sel-classs="flexselect"></span></li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_LOGINLONGINFO_STUDENT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="12%">教学站</th>
						<th width="8%">年级</th>
						<th width="8%">层次</th>
						<th width="8%">办学模式</th>
						<th width="12%">专业</th>
						<th width="12%">班级</th>
						<th width="12%">在线时长</th>
					</tr>
				</thead>
				<tbody id="loginLongBody">
					<c:forEach items="${studentLoginLongList.result}" var="ll"
						varStatus="vs">
						<tr>
							<td>${ll.studyNo }</td>
							<td>${ll.studentName }</td>
							<td>${ll.branchSchool.unitName }</td>
							<td>${ll.grade.gradeName }</td>
							<td>${ll.classic.classicName }</td>
							<td>${ghfn:dictCode2Val("CodeTeachingType",ll.teachingType) }</td>
							<td>${ll.major.majorName }</td>
							<td>${ll.classes.classname }</td>
							<td>${ll.loginLongStr }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${studentLoginLongList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingManagement/loginLongList_student.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>