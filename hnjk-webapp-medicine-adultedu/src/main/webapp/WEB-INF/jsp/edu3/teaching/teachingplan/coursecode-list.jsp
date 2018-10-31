
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划</title>
<style type="text/css">
th,td{text-align: center;}
</style>
<script type="text/javascript">
//打开页面或者点击查询（即加载页面执行）
$(document).ready(function(){
	planCourseCodeQueryBegin();
});
function planCourseCodeQueryBegin() {
	var defaultValue = "${condition['unitid']}";
	var schoolId = "${school}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'planCourseCode_unitid',gradeId:'planCourseCode_gradeid',classicId:'planCourseCode_classicid',"
			+"teachingType:'planCourseCode_teachingType',majorId:'planCourseCode_majorid',classesId:'planCourseCode_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

//选择教学点
function planCourseCodeQueryUnit() {
	   var defaultValue = $("#planCourseCode_unitid").val();
	   var selectIdsJson = "{gradeId:'planCourseCode_gradeid',classicId:'planCourseCode_classicid',"
		   +"teachingType:'planCourseCode_teachingType',majorId:'planCourseCode_majorid',classesId:'planCourseCode_classesid'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
}
// 选择年级
function planCourseCodeQueryGrade() {
	   var defaultValue = $("#planCourseCode_unitid").val();
	   var gradeId = $("#planCourseCode_gradeid").val();
	   var selectIdsJson = "{classicId:'planCourseCode_classicid',teachingType:'planCourseCode_teachingType',"
			  +"majorId:'planCourseCode_majorid',classesId:'planCourseCode_classesid'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
}
// 选择层次
function planCourseCodeQueryClassic() {
	var defaultValue = $("#planCourseCode_unitid").val();
	var gradeId = $("#planCourseCode_gradeid").val();
	var classicId = $("#planCourseCode_classicid").val();
	var selectIdsJson = "{teachingType:'planCourseCode_teachingType',majorId:'planCourseCode_majorid',classesId:'planCourseCode_classesid'}";
	cascadeQuery("classicid", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function planCourseCodeQueryTeachingType() {
	var defaultValue = $("#planCourseCode_unitid").val();
	var gradeId = $("#planCourseCode_gradeid").val();
	var classicId = $("#planCourseCode_classicid").val();
	var teachingTypeId = $("#planCourseCode_teachingType").val();
	var selectIdsJson = "{majorId:'planCourseCode_majorid',classesId:'planCourseCode_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}
//选择专业
function planCourseCodeQueryMajor() {
	var defaultValue = $("#planCourseCode_unitid").val();
	var gradeId = $("#planCourseCode_gradeid").val();
	var classicId = $("#planCourseCode_classicid").val();
	var teachingTypeId = $("#planCourseCode_teachingType").val();
	var majorId = $("#planCourseCode_majorid").val();
	var selectIdsJson = "{classesId:'planCourseCode_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/teaching/teachingplancourse_code/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <c:choose>
								<c:when test="${not isBrschool }">
									<span sel-id="planCourseCode_unitid" sel-name="unitid"
										sel-onchange="planCourseCodeQueryUnit()" sel-classs="flexselect"
										sel-style="width: 120px"></span>
								</c:when>
								<c:otherwise>
									<input type="hidden" value="${condition['brSchoolId']}"
										name="unitid" id="planCourseCode_unitid">
									<input type="text" value="${schoolname}" style="width: 120px"
										readonly="readonly">
								</c:otherwise>
							</c:choose></li>
						<li><label>年级：</label> <span sel-id="planCourseCode_gradeid"
							sel-name="gradeid" sel-onchange="planCourseCodeQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="planCourseCode_classicid"
							sel-name="classicid" sel-onchange="planCourseCodeQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习形式：</label> <span sel-id="planCourseCode_teachingType"
							sel-name="teachingType" sel-onchange="planCourseCodeQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label> <span sel-id="planCourseCode_majorid"
							sel-name="majorid" sel-onchange="planCourseCodeQueryMajor()"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>
						<li><label>班级：</label> <span sel-id="planCourseCode_classesid"
							sel-name="classesid" sel-classs="flexselect"
							sel-style="width: 120px"></span></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseid" tabindex="1" id="planCourseCode_courseid"
								value="${condition['courseid']}" displayType="code"
								isFilterTeacher="Y" style="width:120px" /></li>
						<li><label>序列号：</label> <input name="code" id="planCourseCode_code"
								value="${condition['code']}" style="width:120px" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent"> <button type="submit">查 询</button> </div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_TEACHINGPLANCOURSE_CODE" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="8%">年级</th>
						<th width="15%">教学点</th>
						<th width="5%">层次</th>
						<th width="5%">学习形式</th>
						<th width="12%">专业</th>
						<th width="18%">班级</th>
						<th width="10%">学期</th>
						<th width="10%">课程名称</th>
						<th width="10%">序列号</th>
					</tr>
				</thead>
				<tbody id="planCourseCodeBody">
					<c:forEach items="${courseCodePage.result}" var="pc" varStatus="vs">
						<tr>
							<td>${pc.gradeName }</td>
							<td>${pc.unitName }</td>
							<td>${pc.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',pc.teachingType) }</td>
							<td>${pc.majorName }</td>
							<td>${pc.classesName }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTermType',pc.term) }</td>
							<td>${pc.courseName }</td>
							<td>${pc.code }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseCodePage}" pageType="sys" condition="${condition}" 
				goPageUrl="${baseUrl }/edu3/teaching/teachingplancourse_code/list.html" />
		</div>
	</div>
</body>
</html>