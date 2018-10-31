<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>意愿信息</title>
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
	var selectIdsJson = "{unitId:'willingnessList_brSchoolid',gradeId:'willingnessList_gradeid',classicId:'willingnessList_classicid',teachingType:'willingnessList_teachingType',majorId:'willingnessList_majorid',classesId:'willingnessList_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function courseStatusQueryUnit() {
	var defaultValue = $("#willingnessList_brSchoolid").val();
	var selectIdsJson = "{gradeId:'willingnessList_gradeid',classicId:'willingnessList_classicid',teachingType:'willingnessList_teachingType',majorId:'willingnessList_majorid',classesId:'willingnessList_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusQueryClassic() {
	var defaultValue = $("#willingnessList_brSchoolid").val();
	var gradeId = $("#willingnessList_gradeid").val();
	var classicId = $("#willingnessList_classicid").val();
	var selectIdsJson = "{teachingType:'willingnessList_teachingType',majorId:'willingnessList_majorid',classesId:'willingnessList_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusQueryTeachingType() {
	var defaultValue = $("#willingnessList_brSchoolid").val();
	var gradeId = $("#willingnessList_gradeid").val();
	var classicId = $("#willingnessList_classicid").val();
	var teachingTypeId = $("#willingnessList_teachingType").val();
	var selectIdsJson = "{majorId:'willingnessList_majorid',classesId:'willingnessList_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}


//编辑
function editWillingness(){
	var url = "${baseUrl}/edu3/arrange/teachingWillingness/edit.html";
	var brSchoolid = $("#willingnessBody input[@name='resourceid']:checked").attr("brSchoolid");
	if(isCheckOnlyone('resourceid','#willingnessBody')){
		navTab.openTab('RES_ARRANGE_WILLINGNESS_EDIT', url+'?brSchoolid='+brSchoolid+'&resId='+$("#willingnessBody input[@name='resourceid']:checked").val(), '编辑意愿信息');
	}			
}

//撤销
function delWillingness(){	
	pageBarHandle("您确定要撤销这些记录吗？","${baseUrl}/edu3/arrange/teachingWillingness/delete.html","#willingnessBody");
}

</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return navTabSearch(this);" id="teachingWillingnessForm" action="${baseUrl }/edu3/arrange/teachingWillingness/list.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="willingnessList_brSchoolid" sel-name="brSchoolid" sel-onchange="courseStatusQueryUnit()"  sel-style="width: 120px"></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value=""${condition['brSchoolid']}"" id="willingnessList_brSchoolid" name="brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</li>	
				<li>
					<label>层次：</label>
					<span sel-id="willingnessList_classicid" sel-name="classicid" sel-onchange="courseStatusQueryClassic()" sel-style="width: 120px"></span>
				</li>	
				<li>
					<label>学习方式：</label>
					<span sel-id="willingnessList_teachingType" sel-name="teachingType" sel-onchange="courseStatusQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</li>
			</ul>
			<ul class="searchContent">	
				<li>
					<label>教学班：</label>
					<gh:teachCourseAutocomplete name="teachCourseid" tabindex="1" id="willingnessList_teachCourseid" value="${condition['teachCourseid']}" style="width:120px"/>
				</li>
				<li>
					<label>上课学期：</label>
					<gh:select id="willingnessList_term" name="openTerm" value="${condition['openTerm']}" dictionaryCode="CodeCourseTermType" style="width:120px"  orderType="desc" size="10"/>
				</li>
				<li>
					<label>课程名称：</label> 
					<gh:courseAutocomplete name="courseId" tabindex="1" id="willingnessList_list_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
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
		<gh:resAuth parentCode="RES_ARRANGE_WILLINGNESS_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="160" width="100%">
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="3%"><input type="checkbox" name="checkall" id="check_all_selectCourse" onclick="checkboxAll('#check_all_selectCourse','resourceid','#willingnessBody')"/></th>
		        	<th style="text-align: center;vertical-align: middle;" width="8%">教学点</th>
		        	<!-- <th style="text-align: center;vertical-align: middle;" width="8%">教学班号</th>  --> 
		            <th style="text-align: center;vertical-align: middle;" width="15%">教学班</th>  
		            <th style="text-align: center;vertical-align: middle;" width="8%">上课学期</th>
		        	
		            <th style="text-align: center;vertical-align: middle;" width="8%">课程</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%">学时</th>	
		            <th style="text-align: center;vertical-align: middle;" width="5%" >人数</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%" >申请人</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%" >操作人</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%" >课室属性</th>
		            <th style="text-align: center;vertical-align: middle;" width="10%" >星期</th>
		            <th style="text-align: center;vertical-align: middle;" width="15%" >时间段</th>
		            <th style="text-align: center;vertical-align: middle;" width="15%" >意愿信息</th>
				</tr>
			</thead>
			<tbody id="willingnessBody">
		       <c:forEach items="${page.result}" var="c" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;">			        	
			        		<input type="checkbox" name="resourceid" value="${c.resourceid}" brSchoolid="${c.teachCourse.unit.resourceid }" autocomplete="off" />
			        	</td>
			        	<td style="text-align: center;vertical-align: middle;">${c.teachCourse.unit.unitName }</td>
			            <%-- <td style="text-align: center;vertical-align: middle;" title="${c.teachCourse.teachingCode }">${c.teachCourse.teachingCode }</td> --%>
			            <td style="text-align: center;vertical-align: middle;" title="${c.teachCourse.teachingClassname }">${c.teachCourse.teachingClassname }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseTermType',c.teachCourse.openTerm) }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.teachCourse.courseName }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.teachCourse.studyHour }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.teachCourse.studentNumbers }</td>
			            <td style="text-align: center;vertical-align: middle;"><c:if test="${not empty c.proposer }"> ${c.proposer.cnName }</c:if></td>
			            <td style="text-align: center;vertical-align: middle;">${c.operator.cnName }</td>
			           	<td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeClassRoomStyle',c.classroomType) }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.daysName }">${c.daysName }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.timePeriodNames }">${c.timePeriodNames }</td>
			           	<td style="text-align: center;vertical-align: middle;" title="${c.info }">${c.info }</td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${page}" goPageUrl="${baseUrl }/edu3/arrange/teachingWillingness/list.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>