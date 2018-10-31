<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
	td,th{text-align: center;}
</style>
<title>分配专业</title>
<script type="text/javascript">
$(document).ready(function(){
	rtcwQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function rtcwQueryBegin() {
	var defaultValue = "${condition['branchSchoolId']}";
	var schoolId = "${condition['branchSchoolId']}";
	var gradeId = "${condition['gradeId']}";
	var classicId = "${condition['classicId']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorId']}";
	var classesId = "${condition['classes']}";
	var selectIdsJson = "{unitId:'rtcw_branchSchool',gradeId:'rtcw_grade',classicId:'rtcw_classic',"
		+"teachingType:'rtcw_teachingType',majorId:'rtcw_major',classesId:'rtcw_classesId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	
	$("#rtcw_branchSchool").find("*").attr("disabled", "disabled");
}
//选择教学点
function rtcwQueryUnit() {
	   var defaultValue = $("#rtcw_branchSchool").val();
	   var selectIdsJson = "{gradeId:'rtcw_grade',classicId:'rtcw_classic',"
			  +"teachingType:'rtcw_teachingType',majorId:'rtcw_major',classesId:'rtcw_classesId'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
}
	// 选择年级
function rtcwQueryGrade() {
	   var defaultValue = $("#rtcw_branchSchool").val();
	   var gradeId = $("#rtcw_grade").val();
	   var selectIdsJson = "{classicId:'rtcw_classic',teachingType:'rtcw_teachingType',majorId:'rtcw_major',classesId:'rtcw_classesId'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
}
	// 选择层次
function rtcwQueryClassic() {
	   var defaultValue = $("#rtcw_branchSchool").val();
	   var gradeId = $("#rtcw_grade").val();
	   var classicId = $("#rtcw_classic").val();
	   var selectIdsJson = "{teachingType:'rtcw_teachingType',majorId:'rtcw_major',classesId:'rtcw_classesId'}";
	   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}
	// 选择学习形式
function rtcwQueryTeachingType() {
	   var defaultValue = $("#rtcw_branchSchool").val();
	   var gradeId = $("#rtcw_grade").val();
	   var classicId = $("#rtcw_classic").val();
	   var teachingTypeId = $("#rtcw_teachingType").val();
	   var selectIdsJson = "{majorId:'rtcw_major',classesId:'rtcw_classesId'}";
	   cascadeQuery("teachingType", defaultValue, "", gradeId, classicId, teachingTypeId, "", "", selectIdsJson);
}

	// 选择专业时查询可选教学计划
function rtcwQueryMajor(){
	 var defaultValue = $("#rtcw_branchSchool").val();
	 var gradeId = $("#rtcw_grade").val();
	 var classicId = $("#rtcw_classic").val();
	 var teachingType = $("#rtcw_teachingType").val();
	 var teachPlan = $("#rtcw_teachPlan").val();
	 var major = $("#rtcw_major").val();
	 var classicId = $("#rtcw_classic").val();
	 var teachingType = $("#rtcw_teachingType").val();
	 var selectIdsJson = "{classesId:'rtcw_classesId'}";
	 cascadeQuery("majorId", defaultValue, "", gradeId, classicId, teachingType, major, "", selectIdsJson);
	 $.ajax({
			type:"post",
			url:baseUrl+"/edu3/system/standby/getTeachPlan.html",
			data:{teachPlan:teachPlan,classic:classicId,schoolType:teachingType,major:major,isUsed:"Y"},
			dataType:"json",
			success:function(data){
				if(data.statusCode==200){
					$("#rtcw_teachPlan").html(data.teachPlanInfo);
				}else{
					alertMsg.warn(data.message);
				}
			}
	});
}
//分配专业
function saveRTCW(){
	var studentId = [];
	var branchSchool = $("#rtcw_branchSchool").val();
	var gradeid = $("#rtcw_grade").val();
	var classicId = $("#rtcw_classic").val();
	var teachingType = $("#rtcw_teachingType").val();
	var majorid = $("#rtcw_major").val();
	var teachPlan = $("#rtcw_teachPlan").val();
	var isUpdateMajor = $("#rtcw_isUpdateMajor").val();
	//var isUpdateClass = $("#rtcw_isUpdateClass").val();
	var calssesid = $("#rtcw_classesId").val();
	$("#RTCWBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	if(gradeid==""){
		alertMsg.warn("请选择年级！");
		return false;
	}else if(majorid==""){
		alertMsg.warn("请选择要分配的专业！");
		return false;
	}else if(studentId.length==0){
		alertMsg.warn("请勾选要分配专业的学生！");
	}else{
		$.ajax({
			type:"post",
			url:baseUrl+"/edu3/system/standby/saveRTCW.html",
			data:{branchSchool:branchSchool,gradeid:gradeid,classicId:classicId,teachingType:teachingType,majorid:majorid,calssesid:calssesid,teachPlan:teachPlan,isUpdateMajor:isUpdateMajor,studentIds:studentId.join(',')},
			dataType:"json",
			success:function(data){
				if(data.statusCode==200){
					alertMsg.correct(data.message);
					navTab.reload("${baseUrl }/edu3/system/standby/RTCW-list.html", $("#RTCWForm").serializeArray(), "RES_SYS_STANDBY_RTCW")
				}else{
					alertMsg.warn(data.message);
				}
			}
		});
	}
}
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);" id="RTCWForm"
				action="${baseUrl }/edu3/system/standby/RTCW-list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li>教 学 站：<span sel-id="rtcw_branchSchool" 
							sel-name="branchSchoolId" sel-onchange="rtcwQueryUnit()"
							sel-style="width: 140px"></span></li>
						<li>年 级： <span sel-id="rtcw_grade"
							sel-name="gradeId" sel-onchange="rtcwQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li>层 次： <span sel-id="rtcw_classic"
							sel-name="classicId" sel-onchange="rtcwQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li style="width: 200px;">学习形式： <span
							sel-id="rtcw_teachingType" sel-name="teachingType"
							sel-onchange="rtcwQueryTeachingType()" sel-style="width: 100px;"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li>分配专业： <span
							sel-id="rtcw_major" sel-name="majorId"
							sel-onchange="rtcwQueryMajor()" sel-classs="flexselect"
							sel-style="width: 140px"></span></li>
						<%-- <li class="custom-li"><label style="width: 120px;">重新分配班级：</label>
							<gh:select id="rtcw_isUpdateClass" name="isUpdateClass"
								value="${condition['isUpdateClass'] }"
								dictionaryCode="yesOrNo" style="width:120px;" />
						</li> --%>
						
						<li>班 级： <span
							sel-id="rtcw_classesId" sel-name="classesId" sel-style="width: 140px;"></span>
						</li>
						<li>教学计划：
							<select id="rtcw_teachPlan" name="teachPlan" style="width:140px;">
							${teachPlanInfo }
							</select>
						</li>
						<li class="custom-li"><label style="width: 120px;">更新招生专业：</label>
							<gh:select id="rtcw_isUpdateMajor" name="isUpdateMajor"
								value="${condition['isUpdateMajor'] }"
								dictionaryCode="yesOrNo" style="width:120px;" />
						</li>
					</ul>
					<ul class="searchContent">
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="button" onclick="saveRTCW()">分 配</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="110">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" id="check_all_RTCW" name="checkall"
							onclick="checkboxAll('#check_all_RTCW','resourceid','#RTCWBody')" /></th>
						<th style="width: 10%;">学号</th>
						<th style="width: 10%;">姓名</th>
						<th style="width: 7%;">层次</th>
						<th style="width: 15%;">专业</th>
						<th style="width: 25%;">班级</th>
						<th style="width: 20%;">教学计划</th>
					</tr>
				</thead>
				<tbody id="RTCWBody">
					<c:forEach items="${studentInfoList.result }" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td>${stu.studyNo }</td>
							<td>${stu.studentName }</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.classes.classname }</td>
							<td title="${stu.teachingPlan.learningDescript }">${stu.teachingPlan.planName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentInfoList}" pageType="sys" condition="${condition}" 
				goPageUrl="${baseUrl }/edu3/system/standby/RTCW-list.html" />
		</div>
	</div>
</body>
</html>