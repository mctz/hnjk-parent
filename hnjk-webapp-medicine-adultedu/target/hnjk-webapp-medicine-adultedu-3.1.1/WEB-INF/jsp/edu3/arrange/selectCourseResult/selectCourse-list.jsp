<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选课结果</title>
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
	var selectIdsJson = "{unitId:'selectCourse_brSchoolid',gradeId:'selectCourse_gradeid',classicId:'selectCourse_classicid',teachingType:'selectCourse_teachingType',majorId:'selectCourse_majorid',classesId:'selectCourse_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function courseStatusQueryUnit() {
	var defaultValue = $("#selectCourse_brSchoolid").val();
	var selectIdsJson = "{gradeId:'selectCourse_gradeid',classicId:'selectCourse_classicid',teachingType:'selectCourse_teachingType',majorId:'selectCourse_majorid',classesId:'selectCourse_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function courseStatusQueryGrade() {
	var defaultValue = $("#selectCourse_brSchoolid").val();
	var gradeId = $("#selectCourse_gradeid").val();
	var selectIdsJson = "{classicId:'selectCourse_classicid',teachingType:'selectCourse_teachingType',majorId:'selectCourse_majorid',classesId:'selectCourse_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusQueryClassic() {
	var defaultValue = $("#selectCourse_brSchoolid").val();
	var gradeId = $("#selectCourse_gradeid").val();
	var classicId = $("#selectCourse_classicid").val();
	var selectIdsJson = "{teachingType:'selectCourse_teachingType',majorId:'selectCourse_majorid',classesId:'selectCourse_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusQueryTeachingType() {
	var defaultValue = $("#selectCourse_brSchoolid").val();
	var gradeId = $("#selectCourse_gradeid").val();
	var classicId = $("#selectCourse_classicid").val();
	var teachingTypeId = $("#selectCourse_teachingType").val();
	var selectIdsJson = "{majorId:'selectCourse_majorid',classesId:'selectCourse_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function courseStatusQueryMajor() {
	var defaultValue = $("#selectCourse_brSchoolid").val();
	var gradeId = $("#selectCourse_gradeid").val();
	var classicId = $("#selectCourse_classicid").val();
	var teachingTypeId = $("#selectCourse_teachingType").val();
	var majorId = $("#selectCourse_majorid").val();
	var selectIdsJson = "{classesId:'selectCourse_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//编辑
function editCourseResult(){
	var url = "${baseUrl}/edu3/arrange/selectcourseresult/edit.html";
	if(isCheckOnlyone('resourceid','#selectCourseBody')){
		navTab.openTab('RES_ARRANGE_COURSERESULT_EDIT', url+'?resId='+$("#selectCourseBody input[@name='resourceid']:checked").val(), '编辑选课结果');
	}			
}

//撤销
function repealCourseResult(){	
	pageBarHandle("您确定要撤销这些记录吗？","${baseUrl}/edu3/arrange/selectcourseresult/repeal.html","#selectCourseBody");
}

//发布
function publish(){	
	pageBarHandle("您确定要发布这些记录吗？","${baseUrl}/edu3/arrange/selectcourseresult/publish.html?operate=publish","#selectCourseBody");
}

//取消发布
function notPublish(){	
	pageBarHandle("您确定要取消发布这些记录吗？","${baseUrl}/edu3/arrange/selectcourseresult/notpublish.html?operate=cancel","#selectCourseBody");
}

//意愿申请
function apply(){
	var url = "${baseUrl}/edu3/arrange/teachingWillingness/edit.html";
	var userRole = "${userRole}";
	if(!(userRole=="teacher" || userRole=="jwy")){
		alertMsg.warn("抱歉，您没有操作权限！");
		return;
	}
	if(!isCheckOnlyone('resourceid','#selectCourseBody')){
		alertMsg.warn("请选择一个教学班！");
		return;
	}
	if($("#selectCourseBody input[@name='resourceid']:checked").attr("selectedStatus")==1){
		alertMsg.warn("该教学班已经被选！");
		return;
	}
	if($("#selectCourseBody input[@name='resourceid']:checked").attr("publishStatus")!=1){
		alertMsg.warn("请选择已发布的教学班！");
		return;
	}
	/* $.pdialog.open(url+'?userRole='+userRole+'&teachCourseid='+$("#selectCourseBody input[@name='resourceid']:checked").val(),"RES_ARRANGE_WILLINGNESS_EDIT","意愿申请",{height:500,width:900}); */
	navTab.openTab('RES_ARRANGE_WILLINGNESS_EDIT', url+'?userRole='+userRole+'&type=apply'+'&teachCourseid='+$("#selectCourseBody input[@name='resourceid']:checked").val(), '意愿申请');
}

</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return navTabSearch(this);" id=selectCourse action="${baseUrl }/edu3/arrange/selectcourseresult/list.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="selectCourse_brSchoolid" sel-name="brSchoolid" sel-onchange="courseStatusQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${brSchoolid}" id="selectCourse_brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</li>	
				<!-- <li>
					<label>年级：</label>
					<span sel-id="selectCourse_gradeid" sel-name="gradeid" sel-onchange="courseStatusQueryGrade()"  sel-style="width: 120px"></span>
				</li> -->
				<li>
					<label>层次：</label>
					<span sel-id="selectCourse_classicid" sel-name="classicid" sel-onchange="courseStatusQueryClassic()" sel-style="width: 120px"></span>
				</li>	
				<li>
					<label>学习方式：</label>
					<span sel-id="selectCourse_teachingType" sel-name="teachingType" sel-onchange="courseStatusQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</li>
				<li>
					<label>教学班：</label>
					<gh:teachCourseAutocomplete name="teachCourseid" id="selectCourse_teachCourseid" tabindex="1" value="${condition['teachCourseid']}"></gh:teachCourseAutocomplete>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>上课学期：</label>
					<gh:select id="selectCourse_term" name="openTerm" value="${condition['openTerm']}" dictionaryCode="CodeCourseTermType" style="width:120px" orderType="desc" size="10" />
				</li>
				<li>
					<label>课程名称：</label> 
					<gh:courseAutocomplete name="courseId" tabindex="1" id="selectCourse_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
				</li>
				<li>
					<label>选课状态：</label>
					<gh:select id="selectCourse_selectedStatus" name="selectedStatus" value="${condition['selectedStatus']}" dictionaryCode="CodeSelectedStatus" style="width:120px" />	
				</li>
				<li>
					<label>发布状态：</label>
					<gh:select id="selectCourse_publishStatus" name="publishStatus" value="${condition['publishStatus']}" dictionaryCode="CodePublishStatus" style="width:120px" />	
				</li>
				<%-- <li>
					<label>合班状态：</label>
					<gh:select id="selectCourse_status" name="status" value="${condition['status']}" dictionaryCode="CodeTeachClassesStatus" style="width:120px" />	
				</li>	 --%>
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
		<gh:resAuth parentCode="RES_ARRANGE_SELECTCOURSE_RESULR_LIST" pageType="list"></gh:resAuth>
		<table class="table table-bordered table-hover table-condensed" layouth="163">
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="3%"><input type="checkbox" name="checkall" id="check_all_selectCourse" onclick="checkboxAll('#check_all_selectCourse','resourceid','#selectCourseBody')"/></th>
		        	<th style="text-align: center;vertical-align: middle;" width="8%">教学点</th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%">教学班号</th>  
		            <th style="text-align: center;vertical-align: middle;" width="12%">教学班名</th>  
		            <th style="text-align: center;vertical-align: middle;" width="6%">上课学期</th>
		            <th style="text-align: center;vertical-align: middle;" width="3%">层次</th>  
		        	<th style="text-align: center;vertical-align: middle;" width="4%">学习方式</th>
		        	
		            <th style="text-align: center;vertical-align: middle;" width="6%">课程</th>
		            <th style="text-align: center;vertical-align: middle;" width="3%">学时</th>	
		            <th style="text-align: center;vertical-align: middle;" width="3%" >人数</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%" >考核形式</th>
		            <!-- <th style="text-align: center;vertical-align: middle;" width="4%" >合班状态</th> -->
		            <th style="text-align: center;vertical-align: middle;" width="4%" >发布状态</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%">选课状态</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%" >意愿老师</th>
		       	    <!-- <th style="text-align: center;vertical-align: middle;" width="12%" >班级信息</th> -->
		            <th style="text-align: center;vertical-align: middle;" width="6%" >创建时间</th>
				</tr>
			</thead>
			<tbody id="selectCourseBody">
		       <c:forEach items="${page.result}" var="c" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;">			        	
			        		<input type="checkbox" name="resourceid" value="${c.resourceid}" publishStatus="${c.publishStatus }" selectedStatus="${c.selectedStatus }" cautocomplete="off" />
			        	</td>
			        	<td style="text-align: center;vertical-align: middle;">${c.unit.unitName }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.teachingCode }">${c.teachingCode }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.teachingClassname }">${c.teachingClassname }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseTermType',c.openTerm) }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.classic.classicName }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',c.teachingtype) }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.courseName }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.studyHour }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.studentNumbers }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseExamType',c.examClassType) }</td>	
			            <%-- <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachClassesStatus',c.status) }</td> --%>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodePublishStatus',c.publishStatus) }</td>
			           	<td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeSelectedStatus',c.selectedStatus) }</td>
			           	<td style="text-align: center;vertical-align: middle;">${c.willTeacherName }</td>
			           	<%-- <td style="text-align: center;vertical-align: middle;" title="${fn:replace(c.classNames, ",", "&#13;&#10;")}">${c.classNames }</td> --%>
			            <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${c.createDate}" pattern="yyyy-MM-dd"/></td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${page}" goPageUrl="${baseUrl }/edu3/arrange/selectcourseresult/list.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>