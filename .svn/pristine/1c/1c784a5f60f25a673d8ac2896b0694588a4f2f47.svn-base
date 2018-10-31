<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>排课结果</title>
<style type="text/css">
    .recordScoreTeacher{cursor: pointer;}
</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	teachClassesQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function teachClassesQueryBegin() {
	var defaultValue = "${condition['brSchoolid']}";
	var schoolId = "${linkageQuerySchoolId}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'teachClasses_brSchoolid',classesId:'teachClasses_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, "", "","", "", classesId, selectIdsJson);
}

//给特定某条记录设置登分老师
function  setRecordScordTeach(obj){
	var targetObject = $(obj).parent().find("input[name='resourceid']:first");
	var resIds = targetObject.val();
	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/selectteacher.html";
	$.pdialog.open(url+"?teachClassesid="+resIds+"&teachtype=record"
			,"SelectTeacher","选择登分老师",{height:600,width:800});
}

//修改教学班级信息
function editTeachCourseClasses(){
	var teachClassesid = $("#teachClassesBody input[@name='resourceid']:checked").val()
	var url = "${baseUrl}/edu3/arrange/teachCourseClasses/edit.html";
	if(isCheckOnlyone('resourceid','#teachClassesBody')){
		$.pdialog.open(url+"?resourceid="+teachClassesid,"RES_ARRANGE_TEACHCLASSES_EDIT","编辑教学班级信息",{height:600,width:850,mask:true});
	}else{
		alertMsg.warn("请选择一个班级进行操作！");
	}
}
</script>
<div class="page">
	<div class="pageHeader">
		<form onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/arrange/arrangeCourseResult/classesInfo.html" method="post">
		<input type="hidden" name="showList" value="Y" />
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>教学站：</label>
					<c:choose>
						<c:when test="${not isBrschool }">
							<span sel-id="teachClasses_brSchoolid" sel-name="brSchoolid" sel-classs="flexselect"  sel-style="width: 120px" ></span>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${brSchoolid}" id="teachClasses_brSchoolid">
							<input type="text" value="${schoolname}" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</li>	
				<li>
					<label>上课学期：</label>
					<gh:select id="teachClasses_term" name="openTerm" value="${condition['openTerm']}" dictionaryCode="CodeCourseTermType" style="width:120px" orderType="desc" size="10"/>
				</li>
				<li>
					<label>教学班：</label>
					<gh:selectModel id="teachClasses_classesid" name="teachingClassname" bindValue="resourceid" displayValue="teachingClassname" modelClass="com.hnjk.edu.arrange.model.TeachCourse" style="width:120px"
									condition="unit.resourceid='${condition['brSchoolid']}',openTerm='${condition['openTerm']}'"/>
				</li>
				<li>
					<label>课程名称：</label> 
					<gh:courseAutocomplete name="courseId" tabindex="1" id="teachClasses_list_courseId" value="${condition['courseId']}" displayType="code" isFilterTeacher="Y" style="width:120px"/>
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
 		<gh:resAuth parentCode="RES_ARRANGE_ARRANGECOURSE_RESULR_LIST" pageType="clist"></gh:resAuth>
		<table class="table" layouth="110" >
			<thead>
				<tr>
					<th style="text-align: center;vertical-align: middle;" width="3%"><input type="checkbox" name="checkall" id="checkall_arrange_teachClasses" onclick="checkboxAll('#checkall_arrange_teachClasses','resourceid','#teachClassesBody')"/></th>
		        	<th style="text-align: center;vertical-align: middle;" width="10%">教学点</th>
		            <th style="text-align: center;vertical-align: middle;" width="15%">教学班</th>  
		            <th style="text-align: center;vertical-align: middle;" width="15%">班级名称</th>
		            <th style="text-align: center;vertical-align: middle;" width="8%">课程</th>  
		        	<th style="text-align: center;vertical-align: middle;" width="5%">登分老师</th>
		        	<th style="text-align: center;vertical-align: middle;" width="5%" >结束日期</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%">结束周</th>
		            <th style="text-align: center;vertical-align: middle;" width="5%">排课状态</th>	
				</tr>
			</thead>
			<tbody id="teachClassesBody">
		        <c:forEach items="${page.result}" var="item" varStatus="vs">
			        <tr>
			        	<td style="text-align: center;vertical-align: middle;">			        	
			        		<input type="checkbox" name="resourceid" value="${item.resourceid}" autocomplete="off" />
			        	</td>
			        	<td style="text-align: center;vertical-align: middle;" title="${item.teachCourse.unit.unitName }">${item.teachCourse.unit.unitName }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${item.teachCourse.teachingClassname }">${item.teachCourse.teachingClassname }</td>
			            <td style="text-align: center;vertical-align: middle;">${item.classes.classname }</td>
			            <td style="text-align: center;vertical-align: middle;" title="${c.teachCourse.courseName }">${item.course.courseName }</td>
			            <td style="cursor: pointer;text-align: center;" onclick="setRecordScordTeach(this)" id = "setteacherid" >
			           		<a href="javaScript:void(0)"  class="recordScoreTeacher"  >${item.recordScorerName}</a>
		           		</td>
			            <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${item.teachEndDate }" pattern="yyyy-MM-dd"/> </td>
			            <td style="text-align: center;vertical-align: middle;">${item.teachEndWeek}</td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeArrangeStatus',item.arrangeStatus) }</td>	
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${page}" goPageUrl="${baseUrl }/edu3/arrange/arrangeCourseResult/classesInfo.html" pageType="sys" condition="${condition}"/>
	</div>
</div>
</body>
</html>