<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学站替学生申请免修免考</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/noexamapply/noexamapp-brschool-stu-list.html"
				method="post">
				<div id="brSchool_noExamApp_stuInfo" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="brschool_noexamapp_brSchoolName" sel-name="branchSchool"
								sel-onchange="noExamApp_stuInfoQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="brschool_noexamapp_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>

						<li><label>年级：</label> <span
							sel-id="brschool_noexamapp_stuGrade" sel-name="stuGrade"
							sel-onchange="noExamApp_stuInfoQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="brschool_noexamapp_classic" sel-name="classic"
							sel-onchange="noExamApp_stuInfoQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="brSchool_noExamApp_major" sel-name="major"
							sel-onchange="noExamApp_stuInfoQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>姓名：</label><input type="text" name="name"
							id="brSchool_noExamApp_name" value="${condition['name']}"
							style="width: 130px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							id="brSchool_noExamApp_studyNo" value="${condition['studyNo']}"
							style="width: 130px" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="brschool_noexamapp_classesid" sel-name="classesid"
							sel-classs="flexselect"></span></li>
						<li><label>身份证号：</label><input type="text" name="certNum"
							id="brSchool_noExamApp_certNum" value="${condition['certNum']}"
							style="width: 130px" /></li>
						<li><label>学籍状态：</label> <gh:select name="stuStatus"
								id="brSchool_noExamApp_stuStatus"
								value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" filtrationStr="11,21,25"
								style="width:120px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_NOEXAM_APP_BYBRSCHOOL_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="162">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="brSchool_noExamApp_check_all_info"
							onclick="checkboxAll('#brSchool_noExamApp_check_all_info','resourceid','#brSchool_noExamApp_infoBody')" /></th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="5%">性别</th>
						<th width="12%">身份证</th>
						<th width="8%">民族</th>
						<th width="8%">年级</th>
						<th width="8%">培养层次</th>
						<th width="13%">专业</th>
						<th width="11%">教学站</th>
						<th width="8%">学籍状态</th>
					</tr>
				</thead>
				<tbody id="brSchool_noExamApp_infoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/teaching/noexamapply/noexamapp-brschool-stu-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
<script type="text/javascript">
$(document).ready(function(){
	var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	//orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11");
	$("#brschool_noexamapp_brSchoolName").flexselect({
		  specialevent:function(){brschool_Major();}
	});
	
//	brschool_Major();
	$("#brschool_noexamapp_classesid").flexselect();
	$("#brschool_noexamapp_major").flexselect();
	noExamApp_stuInfoQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function noExamApp_stuInfoQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'brschool_noexamapp_brSchoolName',gradeId:'brschool_noexamapp_stuGrade',classicId:'brschool_noexamapp_classic',majorId:'brSchool_noExamApp_major',classesId:'brschool_noexamapp_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function noExamApp_stuInfoQueryUnit() {
	var defaultValue = $("#brschool_noexamapp_brSchoolName").val();
	var selectIdsJson = "{gradeId:'brschool_noexamapp_stuGrade',classicId:'brschool_noexamapp_classic',majorId:'brSchool_noExamApp_major',classesId:'brschool_noexamapp_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function noExamApp_stuInfoQueryGrade() {
	var defaultValue = $("#brschool_noexamapp_brSchoolName").val();
	var gradeId = $("#brschool_noexamapp_stuGrade").val();
	var selectIdsJson = "{classicId:'brschool_noexamapp_classic',majorId:'brSchool_noExamApp_major',classesId:'brschool_noexamapp_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function noExamApp_stuInfoQueryClassic() {
	var defaultValue = $("#brschool_noexamapp_brSchoolName").val();
	var gradeId = $("#brschool_noexamapp_stuGrade").val();
	var classicId = $("#brschool_noexamapp_classic").val();
	var selectIdsJson = "{majorId:'brSchool_noExamApp_major',classesId:'brschool_noexamapp_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function noExamApp_stuInfoQueryMajor() {
	var defaultValue = $("#brschool_noexamapp_brSchoolName").val();
	var gradeId = $("#brschool_noexamapp_stuGrade").val();
	var classicId = $("#brschool_noexamapp_classic").val();
	
	var majorId = $("#brSchool_noExamApp_major").val();
	var selectIdsJson = "{classesId:'brschool_noexamapp_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
}

/* //年级-专业 级联
function searchExamResultChangMajor(){
	var gradeId = $("#brschool_noexamapp_stuGrade").val(); //年级
	var classic=$("#brschool_noexamapp_classic").val();  //层次
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major/page2.html",
		data:{gradeId_page1:gradeId,id_page1:'brschool_noexamapp_major',name_page1:'major',click_page1:'searchExamResultMajorClick()',classic:classic},
		dataType:"json",
		success:function(data){
			$('#brschool_noexamapp-gradeToMajor4').html('<label>专业：</label>'+data['msg']);
			$("select[class*=flexselect]").flexselect();
			searchExamResultMajorClick();
		}
	});
 }
//年级-专业-班级 级联
function searchExamResultMajorClick(){
	var gradeId = $('#brschool_noexamapp_stuGrade').val(); //年级
	var majorId = $('#brschool_noexamapp_major').val(); //专业
	var brschoolId = $('#brschool_noexamapp_brSchoolName').val(); //教学站
	var classic=$("#brschool_noexamapp_classic").val(); //层次
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major-classes/page2.html",
		data:{gradeId_page1:gradeId,majorId_page1:majorId,brschoolId_page1:brschoolId,id_page1:'brschool_noexamapp_classesid',name_page1:'classId',classic:classic},
		dataType:"json",
		success:function(data){
			$('#brschool_noexamapp-gradeToMajorToClasses4').html('<label>班级：</label>'+data['msg']);
			$("select[class*=flexselect]").flexselect();
		}
	});
 }
function brschool_Major(){
	var unitId = $("#brschool_noexamapp_brSchoolName").val();
	var majorId = $("#brschool_noexamapp_major").val();
	var classicId = $("#brschool_noexamapp_classic").val();
	var gradeId   = $("#brschool_noexamapp_stuGrade").val();
	var classesId = $("#brschool_noexamapp_classesid").val();//$("#classesId").val();
	var flag = "unitor";
	var url = "${baseUrl}/edu3/register/studentinfo/brschool_Major.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{unitId:unitId,majorId:majorId,classicId:classicId,gradeId:gradeId,classesId:classesId,flag:flag},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['result'] == 300){
				if(undefined!=data['msg']){
					alertMsg.warn(data['msg']);
				}
		    }else{
		    	//$("#major").replaceWith("<select id=\"major\" style=\"width:120px\" name=\"major\" onchange=\"brschool_Major()\">"+data['majorOption']+"</select>");
		    	$("#major").replaceWith("<select  class=\"flexselect textInput\" id=brschool_noexamapp_major name=\"major\" tabindex=1 style=width:120px; onchange='brschool_Major()' >"+data['majorOption']+"</select>");
			    $("#major_flexselect").remove();
			    $("#major_flexselect_dropdown").remove();
			    $("#major").flexselect();
		    	$("#stuClasses").replaceWith("<select id=\"brschool_noexamapp_classesid\" style=\"width: 120px\" name=\"classesid\">"+data['classesOption']+"</select>");
			}
		}
	});
	
}  */
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	//申请免考
	function appNoexamByBrschool(){
		if(isCheckOnlyone('resourceid','#brSchool_noExamApp_infoBody')){
			var studentId 	  = $("#brSchool_noExamApp_infoBody input[@name='resourceid']:checked").attr("value");
			var url           = "${baseUrl}/edu3/teaching/noexamapply/noexamapp-brschool-form.html?resourceid="+studentId;
			navTab.openTab('RES_TEACHING_NOEXAM_APP_BYBRSCHOOL_FORM',url,'免修免考申请');
		}
	}
	
</script>
</html>