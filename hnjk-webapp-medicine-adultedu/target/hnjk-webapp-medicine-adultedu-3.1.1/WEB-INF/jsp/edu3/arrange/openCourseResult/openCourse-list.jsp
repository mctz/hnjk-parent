<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>开课结果</title>
<style type="text/css">
</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	openCourseQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function openCourseQueryBegin() {
	var defaultValue = "${condition['brSchoolid']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'openCourse_brSchoolid',gradeId:'openCourse_gradeid',classicId:'openCourse_classicid',teachingType:'openCourse_teachingType',majorId:'openCourse_majorid',classesId:'openCourse_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function openCourseQueryUnit() {
	var defaultValue = $("#openCourse_brSchoolid").val();
	var selectIdsJson = "{gradeId:'openCourse_gradeid',classicId:'openCourse_classicid',teachingType:'openCourse_teachingType',majorId:'openCourse_majorid',classesId:'openCourse_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function openCourseQueryGrade() {
	var defaultValue = $("#openCourse_brSchoolid").val();
	var gradeId = $("#openCourse_gradeid").val();
	var selectIdsJson = "{classicId:'openCourse_classicid',teachingType:'openCourse_teachingType',majorId:'openCourse_majorid',classesId:'openCourse_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function openCourseQueryClassic() {
	var defaultValue = $("#openCourse_brSchoolid").val();
	var gradeId = $("#openCourse_gradeid").val();
	var classicId = $("#openCourse_classicid").val();
	var selectIdsJson = "{teachingType:'openCourse_teachingType',majorId:'openCourse_majorid',classesId:'openCourse_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function openCourseQueryTeachingType() {
	var defaultValue = $("#openCourse_brSchoolid").val();
	var gradeId = $("#openCourse_gradeid").val();
	var classicId = $("#openCourse_classicid").val();
	var teachingTypeId = $("#openCourse_teachingType").val();
	var selectIdsJson = "{majorId:'openCourse_majorid',classesId:'openCourse_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function openCourseQueryMajor() {
	var defaultValue = $("#openCourse_brSchoolid").val();
	var gradeId = $("#openCourse_gradeid").val();
	var classicId = $("#openCourse_classicid").val();
	var teachingTypeId = $("#openCourse_teachingType").val();
	var majorId = $("#openCourse_majorid").val();
	var selectIdsJson = "{classesId:'openCourse_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//查看教学班信息
function viewTeachCourse(id){
	var url = "${baseUrl}/edu3/arrange/opencourseresult/view.html";
	$.pdialog.open(url+'?teachCourseid='+id, 'RES_ARRANGE_TEACHCOURSE_VIEW', '教学班信息', {width: 800, height: 600,mask:true});
}

//合班 逻辑：课程、课时、考核、形式、层次、必选修相同
function mergeClasses(){
	
	var resIds = "";
	var pcIds = "";
	var gpIds = "";
	var unitIds = "";
	var unitId = "";
	var grades = "";
	var terms = "";
	var classesIds = "";
	var courseIds = "";
	var stuCounts = "";
	var teacherIds = "";
	var check = "y";
	var unitName = "";
	var termName = "";
	var courseName = "";
	var teachType = "";
	$("#openCourseResultBody input[name='resourceid']:checked").each(function(){
		var checekObj = $(this);
		if(""==resIds){
			resIds += checekObj.val()==null?"":checekObj.val();
			classesIds += checekObj.attr("classesid");
			pcIds += checekObj.attr("plancourseid");
			gpIds += checekObj.attr("guiplanid");
			courseIds += checekObj.attr("courseid");
			terms += checekObj.attr("term");
			stuCounts += checekObj.attr("stucount");
			teacherIds += checekObj.attr("teacherId")==null?" ":checekObj.attr("teacherId");
			grades += checekObj.attr("gradeName").substring(0,4);
			unitName = checekObj.attr("unitid");
			termName = checekObj.attr("term");
			courseName = checekObj.attr("courseName");
			teachType = checekObj.attr("teachType");
    	}else{
    		resIds += ","+(checekObj.val()==null?"":checekObj.val());
    		classesIds += ","+checekObj.attr("classesid");
			pcIds += ","+checekObj.attr("plancourseid");
			gpIds += ","+checekObj.attr("guiplanid");
			courseIds += ","+checekObj.attr("courseid");
			terms += ","+checekObj.attr("term");
			stuCounts += ","+checekObj.attr("stucount");
			teacherIds += ","+(checekObj.attr("teacherId")==null?" ":checekObj.attr("teacherId"));
			grades += ","+checekObj.attr("gradeName").substring(0,4);
			if(courseName.indexOf(checekObj.attr("courseName"))==-1){
    			courseName += ","+checekObj.attr("courseName");
    			alertMsg.info(courseName+";"+courseName.indexOf(checekObj.attr("courseName")));
    		}
			if(unitName != checekObj.attr("unitid")){
				check = "unit";
			}
			if(teachType!="" && teachType != checekObj.attr("teachType")){
				check = "type";
				alert(teachType+";"+checekObj.attr("teachType"))
			}
			if(termName!="" && checekObj.attr("term")!="" && termName!= checekObj.attr("term")){
				check = "term";
			}
			if(checekObj.attr("guiplanid")==null){
				check = "guip";
			}
			if(termName=="") termName = checekObj.attr("term");
    	}
    });
	if(resIds == ""){
		alertMsg.warn("请您至少选择一条记录进行操作！");
		return;
	}
	if(termName==""){
		alertMsg.warn("上课学期不能为空！");
		return;
	}
	if(check=='unit'){
		alertMsg.warn("教学点必须相同！");
		return;
	}else if(check=='term'){
		alertMsg.warn("上课学期必须相同！");
		return;
	}else if (check=='guip') {
		alertMsg.warn("教学计划不能为空！");
		return;
	}else if(check=='type'){
		alertMsg.warn("教学类型必须相同！");
		return;
	}
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/arrange/opencourseresult/merge.html",
		data:{"resIds":resIds,"unitName":unitName,"terms":terms,"openTerm":termName,"grades":grades,"pcIds":pcIds,"gpIds":gpIds,"classesIds":classesIds,"courseIds":courseIds,"teacherIds":teacherIds,"stuCounts":stuCounts},
		dataType:"json",
		success:function(data){
			var url = "${baseUrl}/edu3/arrange/opencourseresult/merge.html?resIds="+resIds+"&unitName="+unitName+"&terms="+terms+"&pcIds="+pcIds+"&gpIds="+gpIds+"&classesIds="+classesIds+"&courseIds="+courseIds+"&teacherIds="+teacherIds+"&stuCounts="+stuCounts+"&isMerge=Yes"+"&openTerm="+termName+"&courseNames="+encodeURI(courseName)+"&teachType="+teachType;
			if(data.statusCode == 200 && data.mergeStatus != -1){
				if(data.mergeStatus==0){
					alertMsg.confirm(data.message+"确定要合班吗？", {
						okCall: function(){	
							$.ajax({
								type:"post",
								url:url,
								data:{},
								dataType:"json",
								success:function(data){
									if(data.statusCode==200){
										alertMsg.correct(data.message);
										navTab.reload("${baseUrl}/edu3/arrange/opencourseresult/list.html", $("#openCourseListForm").serializeArray(), "RES_ARRANGE_OPENCOURSE_RESULR_LIST");
									}
								}
							});
						}});
				}else{
					$.ajax({
						type:"post",
						url:url,
						data:{},
						dataType:"json",
						success:function(data){
							if(data.statusCode==200){
								alertMsg.correct(data.message);
								navTab.reload("${baseUrl}/edu3/arrange/opencourseresult/list.html", $("#openCourseListForm").serializeArray(), "RES_ARRANGE_OPENCOURSE_RESULR_LIST");
							}
						}
					});
				}
			} else {
				alertMsg.error(data.message);
			}
		}
	});
}

//撤销合班
function cancelMerge(){
	var resIds = "";
	var check = "y";
	$("#openCourseResultBody input[name='resourceid']:checked").each(function(){
		var checekObj = $(this);
		if(""==resIds){
			resIds += checekObj.val()==null?"":checekObj.val();
		}else {
			resIds += ","+(checekObj.val()==null?"":checekObj.val());
		}
		if(checekObj.val()=="null"){
			check = "null";
		}
	});
	if(resIds == ""){
		alertMsg.warn("请您至少选择一条记录进行操作！");
	}else if (check=="null") {
		alertMsg.warn("请选择合班的教学班！");
	}else{
		pageBarHandle("您确定要撤销合班吗？","${baseUrl}/edu3/arrange/opencourseresult/cancelMerge.html","#openCourseResultBody");
	}
}
//生成开课结果
function generateResult(){
	var resIds = "";
	var pcIds = "";
	var classesIds = "";
	var terms = "";
	var stuCounts = "";
	var teacherIds = "";
	var check = "y";
	var courseName = "";
	$("#openCourseResultBody input[name='resourceid']:checked").each(function(){
		var checekObj = $(this);
		if(""==resIds){
			resIds += checekObj.val()==null?"":checekObj.val();
			classesIds += checekObj.attr("classesid");
			pcIds += checekObj.attr("plancourseid");
			terms += checekObj.attr("term");
			stuCounts += checekObj.attr("stucount");
			teacherIds += checekObj.attr("teacherId")==null?" ":checekObj.attr("teacherId");
    	}else{
    		resIds += ","+(checekObj.val()==null?"":checekObj.val());
    		classesIds += ","+checekObj.attr("classesid");
    		pcIds += ","+checekObj.attr("plancourseid");
    		terms += ","+checekObj.attr("term");
    		stuCounts += ","+checekObj.attr("stucount");
    		teacherIds += ","+checekObj.attr("teacherId")==null?" ":checekObj.attr("teacherId");
    	}
		if(checekObj.attr("term")==null){
			check = "term";
		}
		if(checekObj.attr("courseteachercl")==null){
			check = "tcl";
		}
    });
	if(check=="term"){
		alertMsg.warn("上课学期不能为空！");
		return;
	}else if(check=="tcl"){
		alertMsg.warn("上课学期不能为空！");
		return;
	}else if(resIds == ""){
		alertMsg.warn("请您至少选择一条记录进行操作");
		return;
	}else{
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/arrange/opencourseresult/generateresult.html",
			data:{"resIds":resIds,"classesIds":classesIds,"pcIds":pcIds,"terms":terms,"stuCounts":stuCounts,"teacherIds":teacherIds},
			dataType:"json",
			success:function(data){
				if(data.statusCode==200){
					alertMsg.correct(data.message);
					navTab.reload("${baseUrl}/edu3/arrange/opencourseresult/list.html", $("#openCourseListForm").serializeArray(), "RES_ARRANGE_OPENCOURSE_RESULR_LIST");
				}else{
					alertMsg.error(data.message)
				}
			}
		});
	}
}
function validateForm(form){
	var brSchoolid = $("#openCourse_brSchoolid").val();
	if(brSchoolid==null || brSchoolid==''){
		alertMsg.warn("请选择教学点！");
		return false;
	}else{
		return navTabSearch(form);
	}
}
</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return validateForm(this);" id="openCourseListForm" action="${baseUrl }/edu3/arrange/opencourseresult/list.html" method="post">
		<input type="hidden" name="showList" value="Y" />
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="openCourse_brSchoolid" sel-name="brSchoolid" sel-onchange="openCourseQueryUnit()" sel-classs="flexselect required"  sel-style="width: 120px" ></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${condition['brSchoolid']}" name="brSchoolid" id="openCourse_brSchoolid">
							<input type="text" value="${schoolname}" style="width: 120px" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</li>	
				<li>
					<label>年级：</label>
					<span sel-id="openCourse_gradeid" sel-name="gradeid" sel-onchange="openCourseQueryGrade()"  sel-style="width: 120px"></span>
				</li>
				<li>
					<label>层次：</label>
					<span sel-id="openCourse_classicid" sel-name="classicid" sel-onchange="openCourseQueryClassic()" sel-style="width: 120px"></span>
				</li>	
				<li>
					<label>学习方式：</label>
					<span sel-id="openCourse_teachingType" sel-name="teachingType" sel-onchange="openCourseQueryTeachingType()" dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
				</li>
			</ul>
			<ul class="searchContent">	
				<li>
					<label>专业：</label>
					<span sel-id="openCourse_majorid" sel-name="majorid" sel-onchange="openCourseQueryMajor()" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li>
				<li id="openCourse_classesli">
					<label>班级：</label>
					<span sel-id="openCourse_classesid" sel-name="classesid" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li>
				<li>
					<label>教学班：</label>
					<gh:teachCourseAutocomplete name="teachCourseid" id="openCourse_teachCourseid" value="${condition['teachCourseid']}" tabindex="1"></gh:teachCourseAutocomplete>
				</li>
				<li>
					<label>上课学期：</label>
					<gh:select id="openCourse_term" name="term" value="${condition['term']}" dictionaryCode="CodeCourseTermType" style="width:120px"  orderType="desc" size="10"/>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>课程名称：</label> 
					<gh:courseAutocomplete name="courseId" tabindex="1" id="openCourse_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
				</li>
				<li>
					<label>是否已排课：</label>
					<gh:select id="openCourse_status" name="status" value="${condition['status']}" dictionaryCode="yesOrNo" style="width:120px" />	
				</li>	
				<li>
					<label>生成状态：</label>
					<gh:select id="openCourse_generateStatus" name="generateStatus" value="${condition['generateStatus']}" dictionaryCode="CodeGenerateStatus" style="width:120px" />	
				</li>
				<li>
					<label>人数：</label>
					<input name="startNum" value="${condition['startNum']}" class="digits" style="width:50px"> - <input name="endNum" value="${condition['endNum']}" class="digits" style="width:50px">
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
		<gh:resAuth parentCode="RES_ARRANGE_OPENCOURSE_RESULR_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="188">
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="3%"><input type="checkbox" name="checkall" id="check_all_teachingPlanCourseStatus1" onclick="checkboxAll('#check_all_teachingPlanCourseStatus1','resourceid','#openCourseResultBody')"/></th>
		        	<th style="text-align: center;vertical-align: middle;" width="14%">班级</th>
		        	<th style="text-align: center;vertical-align: middle;" width="6%">年级</th>  
		            <th style="text-align: center;vertical-align: middle;" width="10%">专业</th>  
		            <th style="text-align: center;vertical-align: middle;" width="4%">层次</th>  
		        	<th style="text-align: center;vertical-align: middle;" width="5%">学习方式</th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%">教学类型</th>
		        	
		            <th style="text-align: center;vertical-align: middle;" width="10%">教学站</th>
		            <th style="text-align: center;vertical-align: middle;" width="8%">课程</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%">人数</th> 
		            <th style="text-align: center;vertical-align: middle;" width="8%">上课学期</th>	
		            
		            <th style="text-align: center;vertical-align: middle;" width="4%">学时</th>	
		            <th style="text-align: center;vertical-align: middle;" width="6%">班号</th> 
		            <th style="text-align: center;vertical-align: middle;" width="4%">合班数</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%">排课状态</th>
		            <th style="text-align: center;vertical-align: middle;" width="4%">生成状态</th> 
				</tr>
			</thead>
			<tbody id="openCourseResultBody">
		       <c:forEach items="${coursePage.result}" var="c" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;">			        	
			        		<input type="checkbox" name="resourceid" value="${c.resourceid}" courseteachercl="${c.tclid }" guiplanid="${c.guiplan }" teachType="${c.teachType }" stucount="${c.stucount}" term="${c.courseterm }" teacherId="${c.teacherId }"
			        			 gradeName="${c.gradeName }" courseName="${c.courseName }" publishStatus="${c.publishStatus }" plancourseid="${c.plancourseid}" autocomplete="off" unitid="${c.brschoolid}" classesid="${c.classesid}" teachtype="${c.teachingType }" courseid="${c.cid}"  />
			        	</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.classesname }"><c:if test="${c.resourceid ne 'null'}"><a href="#" onclick="viewTeachCourse('${c.resourceid}')" title="点击查看"></c:if>${c.classesname }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.gradeName }</td>	
			            <td style="text-align: center;vertical-align: middle;">${c.majorName }</td>		            
			            <td style="text-align: center;vertical-align: middle;">${c.classicName }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',c.teachingType) }</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseTeachType',c.teachType) }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.unitName }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.courseName }">${c.courseName }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.stucount }</td><!-- studentNumbers -->
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseTermType',c.courseterm) }</td>
			            <td style="text-align: center;vertical-align: middle;">${c.stydyHour }</td>
			            <td style="text-align: center;vertical-align: middle;"><c:if test="${c.resourceid ne 'null'}"><a href="#" onclick="viewTeachCourse('${c.resourceid}')" title="点击查看">${c.classesid }</c:if></td>
			            <td style="text-align: center;vertical-align: middle;">${c.classcount }</td>
			           	<td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCourseArrangeStatus',c.status) }</td>
			            <td style="text-align: center;vertical-align: middle;"><c:if test="${!empty c.generateStatus }">${ghfn:dictCode2Val('CodeGenerateStatus',c.generateStatus) }</c:if></td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${coursePage}" goPageUrl="${baseUrl }/edu3/arrange/opencourseresult/list.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>