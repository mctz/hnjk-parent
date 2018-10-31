<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课详情</title>
</head>
<body>
	<script type="text/javascript">
$(function (){
	var len = "${fn:length(timetableList)}";
	if(len=="0"){
		addCourseTimetable();
	}
});
/*function stopTimetable(res,isStoped){
	alertMsg.confirm("您确定要进行"+(isStoped=='Y'?'取消停课':'停课')+"操作吗？", {
		okCall: function(){		
			$.post("${baseUrl }/edu3/teaching/teachingplancoursetimetable/stop.html",{resourceid:res}, navTabAjaxDone, "json");
		}
	});	
}*/

function setTeach(){
	var resIds = "";
	var gpIds = "";
	var pcIds = "";
	var unitIds = "";
	var classesIds = "";
	//var courseIds = "";
	$("#teachingPlanCourseStatusBody1 input[name='resourceid']:checked").each(function(){
		// alert($(this).parents("tr:first").html());
		var checekObj = $(this);
		if(""==resIds){
			resIds += checekObj.val();
			classesIds += checekObj.attr("classesid");
			gpIds += checekObj.attr("guiplanid");
			pcIds += checekObj.attr("plancourseid");
			unitIds += checekObj.attr("unitid");
			//courseIds += checekObj.attr("courseid");
    	}else{
    		resIds += ","+checekObj.val();
    		classesIds += ","+checekObj.attr("classesid");
			gpIds += ","+checekObj.attr("guiplanid");
			pcIds += ","+checekObj.attr("plancourseid");
			unitIds += ","+checekObj.attr("unitid");
			//courseIds += ","+checekObj.attr("courseid");
    	}
    });
	if(resIds == ""){
		alertMsg.warn("请您至少选择一条记录进行分配操作.");
		return;
	}
	
	var url = "${baseUrl}/edu3/teaching/teachingplancoursetimetable/teacher_1.html";
	$.pdialog.open(url+"?type=0&unitIds="+unitIds+"&resIds="+resIds+"&classesIds="+classesIds//+"&gpIds="+gpIds+"&pcIds="+pcIds+"&courseIds="+courseIds
			,"SelectTeacher","选择老师",{height:600,width:800});
}
//新增课程安排
function addCourseTimetable(){
	var teacherName = "${teacherid}";
	$.pdialog.open(baseUrl+"/edu3/teaching/teachingplancoursetimetable/input.html?plancourseid=${planCourse.resourceid }&teacherName="+teacherName+"&classesid=${classes.resourceid}&term=${term}", 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '新增上课安排', {width: 800, height: 600});
}
//调整上课安排
function modifyCourseTimetable(res){	
	if(isCheckOnlyone('resourceid','#teachingPlanCourseTimetableBody')){
		var teacherName = "${teacherid}";
		var url = baseUrl+"/edu3/teaching/teachingplancoursetimetable/input.html?term=${term}&teacherName="+teacherName+"&resourceid="+$("#teachingPlanCourseTimetableBody input[@name='resourceid']:checked").val();
		$.pdialog.open(url, 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '调停课', {width: 800, height: 600});
	}			
}	
//删除上课安排
function removeCourseTimetable(){	
	pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachingplancoursetimetable/remove.html?plancourseid=${planCourse.resourceid }&classesid=${classes.resourceid}&term=${term}","#teachingPlanCourseTimetableBody");
}

//导入排课结果
function techPlanTimeTableImport(){
	$.pdialog.open("${baseUrl}/edu3/teaching/teachingplantimetable/import.html?classesid=${classes.resourceid}&plancourseid=${planCourse.resourceid}&term=${term}"
			,"RES_TEACHING_TEACHINGPLANTIMETABLE_IMPORT","导入排课结果", {width:1000, height:600});
}

</script>
	<div class="page">
		<h2 class="contentTitle">
			班级：${classes.classname }&nbsp;&nbsp;人数：${stunumber}
			<br />课程：${planCourse.course.courseName }&nbsp;&nbsp;-&nbsp;&nbsp;登分老师：${teacherName }

		</h2>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_TEACHINGPLANCOURSETIMETABLE"
				pageType="list"></gh:resAuth>
			<input type="hidden" value="${classes.resourceid }" id="classeid">
			<input type="hidden" value="${teacherName }" id="teacherName_s">
			<input type="hidden" value="${cid}" id="ccid">
			<table class="table" layouth="116">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_teachingPlanCourseTimetable"
							onclick="checkboxAll('#check_all_teachingPlanCourseTimetable','resourceid','#teachingPlanCourseTimetableBody')" /></th>
						<th width="10%">上课教师</th>
						<th width="10%">星期</th>
						<th width="22%">时间段</th>
						<th width="8%">上课时间</th>
						<th width="15%">上课地点</th>
						<th width="10%">状态</th>
						<th width="20%">操作</th>
					</tr>
				</thead>
				<tbody id="teachingPlanCourseTimetableBody">
					<c:forEach items="${timetableList}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off" /></td>
							<td>${t.teacherName }</td>
							<td>${ghfn:dictCode2Val('CodeWeek',t.week) }</td>
							<td>
								<%-- ${ghfn:dictCode2Val('CodeCourseTimePeriod',t.unitTimePeriod.timePeriod) }${t.unitTimePeriod.timeName }<fmt:formatDate value="${t.unitTimePeriod.startTime }" pattern="HH:mm" />-<fmt:formatDate value="${t.unitTimePeriod.endTime }" pattern="HH:mm" />--%>
								${t.unitTimePeriod.courseTimeName }
							</td>
							<td>${t.teachDate }</td>
							<td>${t.classroom.classroomName }</td>
							<td><c:choose>
									<c:when test="${t.isStoped eq 'N' }">正常</c:when>
									<c:otherwise>停课</c:otherwise>
								</c:choose></td>
							<td>
								<%-- 
			            	<a href="javascript:void(0)" onclick="stopTimetable('${t.resourceid}','${t.isStoped }')">
			            	<c:choose>
				            <c:when test="${t.isStoped eq 'N' }">停课</c:when>
				            <c:otherwise>取消停课</c:otherwise>
				            </c:choose>
			            	</a> --%> <a
								href="${baseUrl }/edu3/teaching/teachingplancoursetimetable/input.html?term=${term}&resourceid=${t.resourceid}"
								rel="RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT"
								target="dialog" width="800" height="600" title="调停课">调停课</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>