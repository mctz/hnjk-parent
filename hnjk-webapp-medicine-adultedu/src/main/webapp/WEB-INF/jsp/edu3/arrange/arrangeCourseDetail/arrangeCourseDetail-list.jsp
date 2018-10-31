<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课结果详细</title>
<style type="text/css">
.titleLable{font-weight:bold; font-size:14px;};
</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	courseStatusQueryBegin();
	var teachCourse =  "${teachCourse}";
	var arrangeStatus = "${teachCourse.arrangeStatus}";
	if(teachCourse!='' && arrangeStatus==0){
		addArrangeDetail();
	}
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
	var selectIdsJson = "{unitId:'arrangeCourseDetail_brSchoolid',gradeId:'arrangeCourseDetail_gradeid',classicId:'arrangeCourseDetail_classicid',teachingType:'arrangeCourseDetail_teachingType',majorId:'arrangeCourseDetail_majorid',classesId:'arrangeCourseDetail_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function courseStatusQueryUnit() {
	var defaultValue = $("#arrangeCourseDetail_brSchoolid").val();
	var selectIdsJson = "{gradeId:'arrangeCourseDetail_gradeid',classicId:'arrangeCourseDetail_classicid',teachingType:'arrangeCourseDetail_teachingType',majorId:'arrangeCourseDetail_majorid',classesId:'arrangeCourseDetail_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function courseStatusQueryGrade() {
	var defaultValue = $("#arrangeCourseDetail_brSchoolid").val();
	var gradeId = $("#arrangeCourseDetail_gradeid").val();
	var selectIdsJson = "{classicId:'arrangeCourseDetail_classicid',teachingType:'arrangeCourseDetail_teachingType',majorId:'arrangeCourseDetail_majorid',classesId:'arrangeCourseDetail_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusQueryClassic() {
	var defaultValue = $("#arrangeCourseDetail_brSchoolid").val();
	var gradeId = $("#arrangeCourseDetail_gradeid").val();
	var classicId = $("#arrangeCourseDetail_classicid").val();
	var selectIdsJson = "{teachingType:'arrangeCourseDetail_teachingType',majorId:'arrangeCourseDetail_majorid',classesId:'arrangeCourseDetail_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusQueryTeachingType() {
	var defaultValue = $("#arrangeCourseDetail_brSchoolid").val();
	var gradeId = $("#arrangeCourseDetail_gradeid").val();
	var classicId = $("#arrangeCourseDetail_classicid").val();
	var teachingTypeId = $("#arrangeCourseDetail_teachingType").val();
	var selectIdsJson = "{majorId:'arrangeCourseDetail_majorid',classesId:'arrangeCourseDetail_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

//新增
function addArrangeDetail(){
	var teachCourseid = "${teachCourse.resourceid}";
	var teacherNames = "${teachCourse.teacherNames}";
	var isArrange = "${isArrange}";
	var ischeck = "${ischeck}";
	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/edit.html?ischeck="+ischeck;
	if(teachCourseid==""){
		url += "&isAdd=Y";
		$.pdialog.open(url,"RES_ARRANGE_COURSEDETAIL_ADD","新增排课详情",{height:600,width:850});
	}else {
		url += "&teachCourseid="+teachCourseid+"&isArrange="+isArrange;
		$.pdialog.open(url,"RES_ARRANGE_COURSEDETAIL_EDIT","编辑排课详情",{height:600,width:850});
	}
}

//编辑
function editArrangeDetail(){
	var courseDetailid = $("#courseDetailBody input[@name='resourceid']:checked").val();
	var isArrange = "${isArrange}";
	var ischeck = "${ischeck}";
	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/edit.html?ischeck="+ischeck;
	if(isCheckOnlyone('resourceid','#courseDetailBody')){
		$.pdialog.open(url+"&courseDetailid="+courseDetailid+"&isArrange="+isArrange,"RES_ARRANGE_COURSEDETAIL_EDIT","编辑排课详情",{height:600,width:850});
	}else{
		alertMsg.warn("请选择一个开课结果！");
	}
}

//删除
function delArrangeDetail(){
	var teachCourseid = "${teachCourse.resourceid }";
	if(teachCourseid!=null && teachCourseid!=''){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/arrange/arrangeCourseDetail/delete.html?teachCourseid="+teachCourseid,"#courseDetailBody");
	}else{
		//alertMsg.warn("请在排课界面勾选教学班后，进入排课界面进行删除！");
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/arrange/arrangeCourseDetail/delete.html","#courseDetailBody");
	}
	
}

</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return navTabSearch(this);"  action="${baseUrl }/edu3/arrange/arrangecoursedetail/list.html" method="post" id="arrangeCourseDetailListForm">
		<input type="hidden" name="showList" value="Y" />
		<input type="hidden" name="teachCourseid" value="${teachCourse.resourceid }" />
		<input type="hidden" name="isArrange" value="${isArrange }" />
		<input type="hidden" name="ischeck" value="${ischeck }" />
		<div class="searchBar">
			<c:if test="${isArrange eq 'Y'}">
				<ul class="searchContent">
					<li><label class="titleLable">教学点：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.unit.unitName}" >
					</li>
					<li>
						<label class="titleLable">教学班：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.teachingClassname }">
					</li>
					<li>
						<label class="titleLable">课程：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.course.courseName }">
					</li>
					<li>
						<label class="titleLable">上课学期：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${ghfn:dictCode2Val('CodeCourseTermType',teachCourse.openTerm) }">
					</li>
				</ul>
				<ul class="searchContent">
					<li><label class="titleLable">层次：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.classic.classicName }" >
					</li>
					<li>
						<label class="titleLable">学习形式：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${ghfn:dictCode2Val('CodeTeachingType',teachCourse.teachingtype) }">
					</li>
					<li>
						<label class="titleLable">考核形式：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${ghfn:dictCode2Val('CodeCourseExamType',teachCourse.examClassType) }">
					</li>
					<li>
						<label class="titleLable">人数：</label>
						<input style="width: 58%;font-size: 14px;" readonly="readonly" value="${teachCourse.studentNumbers }">
					</li>
				</ul>
				<ul class="searchContent">
					<li><label class="titleLable">已排学时：</label>
						<input readonly="readonly" value="${teachCourse.arrangeStudyHour}" style="width: 58%;font-size: 14px;
							<c:if test="${teachCourse.arrangeStudyHour < teachCourse.studyHour-1 or teachCourse.arrangeStudyHour > teachCourse.studyHour+1}">color: red;border-color: red;</c:if>  ">
					</li>
					<li>
						<label class="titleLable">计划学时：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.studyHour }">
					</li>
					<li>
						<label class="titleLable">主讲老师：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.teacherNames }">
					</li>
					<li>
						<label class="titleLable">登分老师：</label>
						<input style="width: 58%;font-size: 14px" readonly="readonly" value="${teachCourse.recordScorerName }">
					</li>
				</ul>
				<div class="subBar">
					<ul><li></li></ul>
				</div>
			</c:if>
			<c:if test="${isArrange ne 'Y'}">
				<ul class="searchContent">
					<li>
		                <label>年度：</label> 
		                <gh:selectModel name="yearId" bindValue="resourceid" displayValue="yearName" 
								modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearId']}"/>
		            </li>
					<li>
						<label>学期：</label>
						<gh:select name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width: 120px"/>
					</li>
					<li>
						<label>教学站：</label>
						<c:if test="${not isBrschool }">
							<span sel-id="arrangeCourseDetail_brSchoolid" sel-name="brSchoolid" sel-onchange="courseStatusQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
						</c:if>
						<c:if test="${isBrschool }">
							<input type="hidden" value="${condition['brSchoolid']}" id="arrangeCourseDetail_brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:if>
					</li>	
					<li>
						<label>层次：</label>
						<span sel-id="arrangeCourseDetail_classicid" sel-name="classicid" sel-onchange="courseStatusQueryClassic()" sel-style="width: 120px"></span>
					</li>	
					
				</ul>
				<ul class="searchContent">	
					<li>
						<label>学习形式：</label>
						<span sel-id="arrangeCourseDetail_teachingType" sel-name="teachingType" sel-onchange="courseStatusQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
					</li>
					<li id="arrangeCourseDetail_classesli">
						<label>教学班：</label>
						<gh:selectModel id="arrangeCourseDetail_teachCourse" name="teachCourse" bindValue="resourceid" displayValue="teachingClassname" modelClass="com.hnjk.edu.arrange.model.TeachCourse" style="width:120px"
									condition="unit.resourceid='${condition['brSchoolid']}',classic.resourceid='${condition['classicid']}',teachingtype='${condition['teachingType']}',openTerm='${condition['openTerm']}'"/>
					</li>
					<li>
						<label>上课学期：</label>
						<gh:select id="arrangeCourseDetail_term" name="openTerm" value="${condition['openTerm']}" dictionaryCode="CodeCourseTermType" style="width:120px"  orderType="desc" size="10"/>
					</li>
					<li>
						<label>课程名称：</label> 
						<gh:courseAutocomplete name="courseId" tabindex="1" id="arrangeCourseDetail_list_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
					</li>
				</ul>
				<ul class="searchContent">
					<li>
						<label>班级：</label>
						<gh:classesAutocomplete name="className" id="" tabindex="1" displayType="code" defaultValue="${condition['className'] }" style="width:120px"></gh:classesAutocomplete>
					</li>
					<li>
						<label>主讲老师：</label>
						<input name="teacherName" value="${condition['teacherName'] }" style="width:120px">
					</li>
					<li>
						<label>排课员：</label>
						<input name="operatorName" value="${condition['operatorName'] }" style="width:120px">
					</li>
				</ul>
				<div class="subBar">
					<ul>
						<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>					
					</ul>
				</div>
			</c:if>
		</div>
		</form>
	</div>
	
	<div class="pageContent">	
		<gh:resAuth parentCode="RES_ARRANGE_ARRANGECOURSE_RESULR_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="188">
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="2%"><input type="checkbox" name="checkall" id="check_all_courseDetail" onclick="checkboxAll('#check_all_courseDetail','resourceid','#courseDetailBody')"/></th>
		        	<c:if test="${isArrange ne 'Y'}">
			        	<th style="text-align: center;vertical-align: middle;" width="8%">教学点</th>
			        	<th style="text-align: center;vertical-align: middle;" width="5%">教学班号</th>  
			            <th style="text-align: center;vertical-align: middle;" width="12%">教学班名</th> 
			            <th style="text-align: center;vertical-align: middle;" width="6%">上课学期</th>
		        	</c:if>
		            
		        	<th style="text-align: center;vertical-align: middle;" width="5%">上课教师</th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%">星期</th>
		        	<th style="text-align: center;vertical-align: middle;" width="6%">时间段</th>
		        	<th style="text-align: center;vertical-align: middle;" width="6%">上课时间</th>
		        	<th style="text-align: center;vertical-align: middle;" width="6%">上课地点</th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%">排课员</th>
		            <th style="text-align: center;vertical-align: middle;" width="6%" >创建时间</th>
				</tr>
			</thead>
			<tbody id="courseDetailBody">
		       <c:forEach items="${page.result}" var="c" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;">			        	
			        		<input type="checkbox" name="resourceid" value="${c.resourceid}" autocomplete="off" />
			        	</td>
			        	<c:if test="${isArrange ne 'Y'}">
			        		<td style="text-align: center;vertical-align: middle;">${c.teachCourse.unit.unitName }</td>
			            	<td style="text-align: center;vertical-align: middle;">${c.teachCourse.teachingCode }</td>
			            	<td style="text-align: center;vertical-align: middle;" title="${c.teachCourse.teachingClassname }">${c.teachCourse.teachingClassname }</td>
			            	<td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseTermType',c.teachCourse.openTerm) }</td>
			        	</c:if>
			            <td style="text-align: center;vertical-align: middle;">${c.teacher.cnName }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.daysName }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.timePeriodNames}">${c.timePeriodNames }</td>
			            <td style="text-align: center;vertical-align: middle;"><c:if test="${c.dateType eq 0 }">${c.weeksName }</c:if>
			            <c:if test="${c.dateType eq 1 }"><fmt:formatDate value="${c.startdate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${c.enddate}" pattern="yyyy-MM-dd"/></c:if> </td>
			           	<td style="text-align: center;vertical-align: middle;">${c.classroom.classroomName }</td>
			           	<td style="text-align: center;vertical-align: middle;">${c.operatorName }</td>
			            <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${c.createDate}" pattern="yyyy-MM-dd"/></td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${page}" goPageUrl="${baseUrl }/edu3/arrange/arrangecoursedetail/list.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>