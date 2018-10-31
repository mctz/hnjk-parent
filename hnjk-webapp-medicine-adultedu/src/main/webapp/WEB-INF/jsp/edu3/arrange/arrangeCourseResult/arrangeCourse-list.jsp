<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课结果</title>
<style type="text/css">
.conflict{text-align:center;color: red;;margin-top: 5px;};
</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	courseStatusQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function courseStatusQueryBegin() {
	var defaultValue = "${condition['brSchoolid']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'arrangeCourse_brSchoolid',gradeId:'arrangeCourse_gradeid',classicId:'arrangeCourse_classicid',teachingType:'arrangeCourse_teachingType',majorId:'arrangeCourse_majorid',classesId:'arrangeCourse_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function courseStatusQueryUnit() {
	var defaultValue = $("#arrangeCourse_brSchoolid").val();
	var selectIdsJson = "{gradeId:'arrangeCourse_gradeid',classicId:'arrangeCourse_classicid',teachingType:'arrangeCourse_teachingType',majorId:'arrangeCourse_majorid',classesId:'arrangeCourse_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function courseStatusQueryGrade() {
	var defaultValue = $("#arrangeCourse_brSchoolid").val();
	var gradeId = $("#arrangeCourse_gradeid").val();
	var selectIdsJson = "{classicId:'arrangeCourse_classicid',teachingType:'arrangeCourse_teachingType',majorId:'arrangeCourse_majorid',classesId:'arrangeCourse_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusQueryClassic() {
	var defaultValue = $("#arrangeCourse_brSchoolid").val();
	var gradeId = $("#arrangeCourse_gradeid").val();
	var classicId = $("#arrangeCourse_classicid").val();
	var selectIdsJson = "{teachingType:'arrangeCourse_teachingType',majorId:'arrangeCourse_majorid',classesId:'arrangeCourse_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusQueryTeachingType() {
	var defaultValue = $("#arrangeCourse_brSchoolid").val();
	var gradeId = $("#arrangeCourse_gradeid").val();
	var classicId = $("#arrangeCourse_classicid").val();
	var teachingTypeId = $("#arrangeCourse_teachingType").val();
	var selectIdsJson = "{majorId:'arrangeCourse_majorid',classesId:'arrangeCourse_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function courseStatusQueryMajor() {
	var defaultValue = $("#arrangeCourse_brSchoolid").val();
	var gradeId = $("#arrangeCourse_gradeid").val();
	var classicId = $("#arrangeCourse_classicid").val();
	var teachingTypeId = $("#arrangeCourse_teachingType").val();
	var majorId = $("#arrangeCourse_majorid").val();
	var selectIdsJson = "{classesId:'arrangeCourse_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//排课
function arrangeCourse() {
	var teachCourseid = $("#arrangeCourseBody input[@name='resourceid']:checked").val()
	var teacherid = $("#arrangeCourseBody input[@name='teacherid']:checked").val()
	var url = "${baseUrl}/edu3/arrange/arrangeCourse/arrange.html";
	var selectedArray =  new Array();
	if(teachCourseid){
		selectedArray = teachCourseid.split(",");
	}
	var ischeck = "${ischeck}";
	url += "?ischeck="+ischeck;
	if(selectedArray.length==1){
		navTab.openTab('RES_ARRANGE_ARRANGECOURSE_ARRANGE', url+'&teachCourseid='+teachCourseid+'&isArrange=Y', '排课');
	}else {
		//alertMsg.warn("请选择一条记录进行操作！");
		navTab.openTab('RES_ARRANGE_ARRANGECOURSE_ARRANGE', url,'排课');
	}
}

//导出列表
function exportPageList () {
	$('#frame_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportExcel";
	var url = "${baseUrl}/edu3/arrange/arrangeCourse/export.html";
	var ids = new Array();
	jQuery("#arrangeCourseBody input[name='resourceid']:checked").each(function(){
		ids.push("'"+jQuery(this).val()+"'");
	});
	if(ids.length>0){
		url += "?resourceids="+ids;
	}
	iframe.src = url; 
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//发布课表
function publishSchedule () {
	$('#frame_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportExcel";
	var url = "${baseUrl}/edu3/arrange/arrangeCourse/schedule.html";
	var ids = new Array();
	jQuery("#arrangeCourseBody input[name='resourceid']:checked").each(function(){
		ids.push("'"+jQuery(this).val()+"'");
	});
	if(ids.length>0){
		url += "?resourceids="+ids;
	}
	iframe.src = url; 
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//查看教学班班级信息
function getTeachClassesInfo(){
	var ids = new Array();
	jQuery("#arrangeCourseBody input[name='resourceid']:checked").each(function(){
		ids.push("'"+jQuery(this).val()+"'");
	});
	var url = "${baseUrl}/edu3/arrange/arrangeCourseResult/classesInfo.html";
	if(ids.length>0){
		url += "?teachCourseids="+ids;
	}
	navTab.openTab('RES_ARRANGE_TEACHCOURSECLASSES_LIST', url, '教学班班级信息');
}

//检查冲突
function checkConflict() {
	var url = "${baseUrl}/edu3/arrange/arrangeCourse/checkConflict.html";
	navTab.openTab('RES_ARRANGE_ARRANGECOURSE_RESULR_LIST', url, '排课结果');
}
</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return navTabSearch(this);" id="arrangeCourseListForm" action="${baseUrl }/edu3/arrange/arrangecourseresult/list.html" method="post">
		<input type="hidden" name="showList" value="Y" />
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="arrangeCourse_brSchoolid" sel-name="brSchoolid" sel-onchange="courseStatusQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${condition['brSchoolid']}" id="arrangeCourse_brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</li>	
				<li>
					<label>上课学期：</label>
					<gh:select id="arrangeCourse_term" name="openTerm" value="${condition['openTerm']}" dictionaryCode="CodeCourseTermType" style="width:120px" orderType="desc" size="10"/>
				</li>
				<li>
					<label>层次：</label>
					<span sel-id="arrangeCourse_classicid" sel-name="classicid" sel-onchange="courseStatusQueryClassic()" sel-style="width: 120px"></span>
				</li>	
				<li>
					<label>学习形式：</label>
					<span sel-id="arrangeCourse_teachingType" sel-name="teachingType" sel-onchange="courseStatusQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>教学班：</label>
					<gh:teachCourseAutocomplete id="arrangeCourse_teachCourseid" name="teachCourseid" value="${condition['teachCourseid']}" tabindex="1" style="width:120px"
									exCondition="and unit.resourceid='${condition['brSchoolid']}'"/>
				</li>
				<li>
					<label>课程名称：</label> 
					<gh:courseAutocomplete name="courseId" tabindex="1" id="arrangeCourse_list_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
				</li>
				<li>
					<label>选课状态：</label>
					<gh:select id="arrangeCourse_selectedStatus" name="selectedStatus" value="${condition['selectedStatus']}" dictionaryCode="CodeSelectedStatus" style="width:120px" />	
				</li>
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>					
				</ul>
			</div>
		</div>
		</form>
	</div>
	<div class="pageContent">
		<gh:resAuth parentCode="RES_ARRANGE_ARRANGECOURSE_RESULR_LIST" pageType="hlist"></gh:resAuth>
		<table class="list" style="width:98.8%;">
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="2%"><input type="checkbox" name="checkall" id="check_all_arrangeCourse" onclick="checkboxAll('#check_all_arrangeCourse','resourceid','#arrangeCourseBody')"/></th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%">教学点</th>
		            <th style="text-align: center;vertical-align: middle;" width="6%">教学班</th> 
		            <th style="text-align: center;vertical-align: middle;" width="10%">班级信息</th> 
		            <c:if test="${ischeck ne 'Y' }">
		           		<th style="text-align: center;vertical-align: middle;" width="4%">上课学期</th>
		            	<th style="text-align: center;vertical-align: middle;" width="3%">层次</th>  
		        		<th style="text-align: center;vertical-align: middle;" width="4%">学习形式</th>
		        		<th style="text-align: center;vertical-align: middle;" width="4%" >考核形式</th>
		        		<th style="text-align: center;vertical-align: middle;" width="3%" >人数</th>
		            </c:if>
		            <th style="text-align: center;vertical-align: middle;" width="5%">课程</th>
		            <th style="text-align: center;vertical-align: middle;" width="3%">学时</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%" >主讲老师</th>
		            <c:if test="${ischeck eq 'Y' }">
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期一</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期二</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期三</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期四</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期五</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期六</th>
			            <th style="text-align: center;vertical-align: middle;" width="8%" >星期天</th>
		            </c:if>
		            <th style="text-align: center;vertical-align: middle;" width="5%" >排课人</th>
				</tr>
			</thead>
		</table>
	</div>
	<div  style="overflow:scroll;position:relative;" layouth="115">	<!-- class="pageContent" -->
		<table class="list">
			<tbody id="arrangeCourseBody">
		        <c:forEach items="${list}" var="item" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;" width="2%">			        	
			        		<input type="checkbox" name="resourceid" value="${item.teachCourse.resourceid}" teacherid="item.teacher.resourceid" autocomplete="off" />
			        	</td>
			        	<td style="text-align: center;vertical-align: middle;" width="5%" title="${item.teachCourse.unit.unitName }">${item.teachCourse.unit.unitName }</td>
			            <td style="text-align: center;vertical-align: middle;" width="6%" title="${item.teachCourse.teachingClassname }">${item.teachCourse.teachingClassname }</td>
			            <td style="text-align: center;vertical-align: middle;" width="10%" title="${fn:replace(item.classTitleName, ",", "&#13;&#10;")}">${item.className }</td>
			            <c:if test="${ischeck ne 'Y' }">
				            <td style="text-align: center;vertical-align: middle;" width="4%" title="${ghfn:dictCode2Val('CodeCourseTermType',item.teachCourse.openTerm) }">${ghfn:dictCode2Val('CodeCourseTermType',item.teachCourse.openTerm) }</td>
				            <td style="text-align: center;vertical-align: middle;" width="3%">${item.teachCourse.classic.classicName }</td>
				            <td style="text-align: center;vertical-align: middle;" width="4%">${ghfn:dictCode2Val('CodeTeachingType',item.teachCourse.teachingtype) }</td>
				            <td style="text-align: center;vertical-align: middle;" width="4%">${ghfn:dictCode2Val('CodeCourseExamType',item.teachCourse.course.examType) }</td>
				            <td style="text-align: center;vertical-align: middle;" width="3%">${item.teachCourse.studentNumbers }</td>
			            </c:if>
			            <td style="text-align: center;vertical-align: middle;" width="5%" height="50px" title="${c.teachCourse.courseName }">${item.teachCourse.courseName }</td>
			            <td style="text-align: center;vertical-align: middle;" width="3%">
			            	<c:if test="${item.teachCourse.arrangeStudyHour < item.teachCourse.studyHour-1 or item.teachCourse.arrangeStudyHour > item.teachCourse.studyHour+1}"><label style="color: red;"></c:if> ${item.teachCourse.arrangeStudyHour}/${item.teachCourse.studyHour }</td>
			            <td style="text-align: center;vertical-align: middle;" width="5%" title="${item.teacherName}">${item.teacherName}&nbsp;</td>
			       		<c:if test="${ischeck eq 'Y' }">
				       		<c:forEach items="${item.timePeriod}" var="t" varStatus="index">
				       			<td style="text-align: center;vertical-align: middle;" width="8%" title="${fn:replace(item.classroom[index.count-1], "；", "&#13;&#10;")}">${t}</td>
				       		</c:forEach>
				       	</c:if>
			       		<td style="text-align: center;vertical-align: middle;" width="5%" title="${item.arrangementName }">${item.arrangementName }</td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</body>
</html>