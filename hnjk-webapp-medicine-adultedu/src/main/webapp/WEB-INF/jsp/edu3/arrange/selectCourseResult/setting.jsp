<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>同步教学班</title>
</head>
<body>
<script type="text/javascript">
	$(document).ready(function(){
		setTeachCourseQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function setTeachCourseQueryBegin() {
		var defaultValue = "${condition['brSchoolid']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classicid']}";
		var teachingType = "${condition['teachingType']}";
		var majorId = "${condition['majorid']}";
		var classesId = "${condition['classesid']}";
		var selectIdsJson = "{unitId:'setTeachCourse_brSchoolid',gradeId:'setTeachCourse_gradeid',classicId:'setTeachCourse_classicid',teachingType:'setTeachCourse_teachingType',majorId:'setTeachCourse_majorid',classesId:'setTeachCourse_classesid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function setTeachCourseQueryUnit() {
		var defaultValue = $("#setTeachCourse_brSchoolid").val();
		var selectIdsJson = "{gradeId:'setTeachCourse_gradeid',classicId:'setTeachCourse_classicid',teachingType:'setTeachCourse_teachingType',majorId:'setTeachCourse_majorid',classesId:'setTeachCourse_classesid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function setTeachCourseQueryGrade() {
		var defaultValue = $("#setTeachCourse_brSchoolid").val();
		var gradeId = $("#setTeachCourse_gradeid").val();
		var selectIdsJson = "{classicId:'setTeachCourse_classicid',teachingType:'setTeachCourse_teachingType',majorId:'setTeachCourse_majorid',classesId:'setTeachCourse_classesid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function setTeachCourseQueryClassic() {
		var defaultValue = $("#setTeachCourse_brSchoolid").val();
		var gradeId = $("#setTeachCourse_gradeid").val();
		var classicId = $("#setTeachCourse_classicid").val();
		var selectIdsJson = "{teachingType:'setTeachCourse_teachingType',majorId:'setTeachCourse_majorid',classesId:'setTeachCourse_classesid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function setTeachCourseQueryTeachingType() {
		var defaultValue = $("#setTeachCourse_brSchoolid").val();
		var gradeId = $("#setTeachCourse_gradeid").val();
		var classicId = $("#setTeachCourse_classicid").val();
		var teachingTypeId = $("#setTeachCourse_teachingType").val();
		var selectIdsJson = "{majorId:'setTeachCourse_majorid',classesId:'setTeachCourse_classesid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function setTeachCourseQueryMajor() {
		var defaultValue = $("#setTeachCourse_brSchoolid").val();
		var gradeId = $("#setTeachCourse_gradeid").val();
		var classicId = $("#setTeachCourse_classicid").val();
		var teachingTypeId = $("#setTeachCourse_teachingType").val();
		var majorId = $("#setTeachCourse_majorid").val();
		var selectIdsJson = "{classesId:'setTeachCourse_classesid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	function sync(){
		var brSchoolid = $("#setTeachCourse_brSchoolid").val();
	   var gradeId = $("#setTeachCourse_gradeid").val();
	   var classicId = $("#setTeachCourse_classicid").val();
	   var teachingType = $("#setTeachCourse_teachingType").val();
	   var majorId = $("#setTeachCourse_majorid").val();
	   var classesId = $("#setTeachCourse_classesid").val();
	   var courseId = $("#setTeachCourse_courseid").val();
	   var url = "${baseUrl}/edu3/arrange/teachcourse/sync.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{brSchoolid:brSchoolid,gradeId:gradeId,classicId:classicId,teachingType:teachingType,majorId:majorId,classesId:classesId,courseId:courseId},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				$("#setTeachCourse_open").removeAttr("disabled");
				$("#setTeachCourse_close").removeAttr("disabled");
				if(data['statusCode'] == 300){
					alertMsg.error(data['message']);
			    }else{
		    		alertMsg.correct(data['message']);
		    		navTabPageBreak();
				}
			}
		});
	}
</script>
<div class="page">
	<div class="pageContent">
		<form method="post"  class="pageForm" onsubmit="return navTabSearch(this);">
			<div class="pageFormContent" layoutH="50">		
				<div align="left" style="margin-left: 33px;margin-bottom: 10px;color: red;">
					备注：按照选择条件将班级同步到教学班
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="setTeachCourse_brSchoolid" sel-name="brSchoolid" sel-onchange="setTeachCourseQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${brSchoolid}" id="setTeachCourse_brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>年级：</label>
					<span sel-id="setTeachCourse_gradeid" sel-name="gradeid" sel-onchange="setTeachCourseQueryGrade()"  sel-style="width: 120px"></span>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>层次：</label>
					<span sel-id="setTeachCourse_classicid" sel-name="classicid" sel-onchange="setTeachCourseQueryClassic()" sel-style="width: 120px"></span>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>学习方式：</label>
					<span sel-id="setTeachCourse_teachingType" sel-name="teachingType" sel-onchange="setTeachCourseQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>专业：</label>
					<span sel-id="setTeachCourse_majorid" sel-name="majorid" sel-onchange="setTeachCourseQueryMajor()" sel-classs="flexselect" sel-style="width: 120px"></span>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>班级：</label>
					<span sel-id="setTeachCourse_classesid" sel-name="classesid" sel-classs="flexselect" sel-style="width: 120px"></span>
				</div>
				<div align="left" style="margin-left: 30px;margin-bottom: 5px;">
					<label>课程：</label>
					<gh:courseAutocomplete name="courseId" tabindex="1" id="setTeachCourse_courseid"  displayType="code" isFilterTeacher="Y" style="width:120px"/>
				</div>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button id="setTeachCourse_open" type="button" onclick="return sync();">同步</button></div></div></li>
					<li><div class="button"><div class="buttonContent"><button id="setTeachCourse_close" type="button" class="close" onclick="$.pdialog.closeCurrent();">取消</button></div></div></li>
				</ul>
			</div>
		</form>
	</div>
</div>
</body>
</html>