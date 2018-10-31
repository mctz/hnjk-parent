<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>开课审核</title>
</head>
<body>
	<script type="text/javascript">
$(document).ready(function(){
	courseStatusCheckQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function courseStatusCheckQueryBegin() {
	var defaultValue = "${condition['checkSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorid']}";
	
	var selectIdsJson = "{unitId:'teachingPlanCourseStatus_brSchoolid',gradeId:'teachingPlanCourseStatus_gradeid',classicId:'classicId',teachingType:'teachingPlanCourseStatus_teachingType',majorId:'majorId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, "", selectIdsJson);
}

// 选择教学点
function courseStatusCheckQueryUnit() {
	var defaultValue = $("#teachingPlanCourseStatus_brSchoolid").val();
    var gradeId = $("#teachingPlanCourseStatus_gradeid").val();
    var classicId = $("#classicId").val();
    var teachingTypeId = $("#teachingPlanCourseStatus_teachingType").val();
	var selectIdsJson = "{gradeId:'teachingPlanCourseStatus_gradeid',classicId:'classicId',teachingType:'teachingPlanCourseStatus_teachingType',majorId:'majorId'}";
	cascadeQuery("unit", defaultValue, "", gradeId, classicId, teachingTypeId, "", "",selectIdsJson);
}

// 选择年级
function courseStatusCheckQueryGrade() {
	var defaultValue = $("#teachingPlanCourseStatus_brSchoolid").val();
	var gradeId = $("#teachingPlanCourseStatus_gradeid").val();
	var selectIdsJson = "{classicId:'classicId',teachingType:'teachingPlanCourseStatus_teachingType',majorId:'majorId'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusCheckQueryClassic() {
	var defaultValue = $("#teachingPlanCourseStatus_brSchoolid").val();
	var gradeId = $("#teachingPlanCourseStatus_gradeid").val();
	var classicId = $("#classicId").val();
	var selectIdsJson = "{teachingType:'teachingPlanCourseStatus_teachingType',majorId:'majorId'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusCheckQueryTeachingType() {
	var defaultValue = $("#teachingPlanCourseStatus_brSchoolid").val();
	var gradeId = $("#teachingPlanCourseStatus_gradeid").val();
	var classicId = $("#classicId").val();
	var teachingTypeId = $("#teachingPlanCourseStatus_teachingType").val();
	var selectIdsJson = "{majorId:'majorId'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

/**
 * 开课审核
 */
function openCourseCheck(){
	var postUrl = "${baseUrl}/edu3/teaching/teachingplancoursestatus/opencoursecheck.html";
	var bodyname = "#check_all_openCourse_body";
	if(!isChecked('resourceid',bodyname)){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	
	var res = [];
	$(bodyname+" input[@name='resourceid']:checked").each(function(){
         res.push($(this).attr('ckeckid'));
    });
	var assessMsg = "操作: <select name='checkStatus' id='checkStatus'><option value='Y'>审核通过</option><option value='N'>审核不通过</option></select>";
	alertMsg.confirm(assessMsg,{okCall:function(){
		var term = $('#checkStatus').val();
	$.ajax({
	          type:"POST",
	          url:postUrl,
	          data:{resourceid:res.join(','),status:term},
	          dataType:  'json',
	          success:function(data){      		 	
	        	  alertMsg.info(data.message);
	        	  $("#checkCourseForm").submit();
	          }            
		});
		//$.post(postUrl,{resourceid:res.join(','),status:term}, navTabAjaxDone, "json");
	}});
}

</script>

	<div class="page">
		<div id="errorInfo" style="color: red"></div>
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/exclassroom/listcheck.html"
				method="post" id="checkCourseForm">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="teachingPlanCourseStatus_brSchoolid"
								sel-name="checkSchool"
								sel-onchange="courseStatusCheckQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool }">
							<input type="hidden" id="teachingPlanCourseStatus_brSchoolid"
								name="checkSchool" value="${condition['checkSchool'] }" />
						</c:if> --%>
						<li><label>年级：</label> <span
							sel-id="teachingPlanCourseStatus_gradeid" sel-name="gradeid"
							sel-onchange="courseStatusCheckQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classicId"
							sel-name="classicid"
							sel-onchange="courseStatusCheckQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习方式：</label> <span
							sel-id="teachingPlanCourseStatus_teachingType"
							sel-name="teachingType"
							sel-onchange="courseStatusCheckQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 100px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label>
							<span sel-id="majorId" sel-name="major" sel-classs="flexselect"></span>
						</li>
						
						<li><label>原计划学期：</label> <gh:select
								id="teachingPlanCourseStatus_orgterm" name="orgterm"
								value="${condition['orgterm']}" dictionaryCode="CodeTermType"
								style="width:120px;" /></li>
						<li><label>操作：</label><select name="operation" style="width: 120px;">
								<option value="">请选择</option>
								<option value="open"
									<c:if test="${operation eq 'open' }">selected="selected"</c:if>>开课</option>
								<option value="cancel"
									<c:if test="${operation eq 'cancel' }">selected="selected"</c:if>>取消开课</option>
						</select></li>
						<li><label>审核状态：</label> <select name="checkStatus" style="width: 100px;">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${checkStatus eq 'Y'}">selected="selected"</c:if>>审核通过</option>
								<option value="N"
									<c:if test="${checkStatus eq 'N'}">selected="selected"</c:if>>审核不通过</option>
								<option value="W"
									<c:if test="${checkStatus eq 'W'}">selected="selected"</c:if>>待审核</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程:</label> <gh:courseAutocomplete name="courseId"
								id="faceStudyExamResults_courseId" tabindex="1"
								displayType="code" isFilterTeacher="Y" style="width:240px;"
								value="${condition['courseId'] }" /></li>
						<li><label>审核人：</label><input type="text" name="checkpeople"
							value="${condition['checkpp']}"></li>
						
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_TEACHINGPLANCOURSESTATUS_CHECK"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_openCourse"
							onclick="checkboxAll('#check_all_openCourse','resourceid','#check_all_openCourse_body')" /></th>
						<th width="10%">教学点</th>
						<th width="3%">年级</th>
						<th width="4%">专业</th>
						<th width="5%">层次</th>
						<th width="5%">学习方式</th>
						<%--  <th width="10%">教学站</th>--%>
						<th width="10%">课程</th>
						<th width="4%">原计划学期</th>
						<th width="4%">变动后学期</th>
						<th width="4%">变动前学期</th>
						<th width="4%">变动后学期</th>
						<th width="5%">操作</th>
						<th width="5%">审核状态</th>
						<th width="5%">审核人</th>
						<th width="7%">审核时间</th>
						<th width="5%">申请人</th>
						<th width="8%">申请时间</th>
					</tr>
				</thead>
				<tbody id="check_all_openCourse_body">
					<c:forEach items="${coursePage.result}" var="c" varStatus="vs">
						<tr>
							<td>
								<%--待审核的才给勾选 --%> <c:if test="${c.checks eq 'W'}">
									<input type="checkbox" name="resourceid" ckeckid="${c.ckid}"
										value="${c.resourceid }" guiplanid="${c.guiplanid }"
										plancourseid="${c.plancourseid}" autocomplete="off" />
								</c:if>
							</td>
							<td>${c.schoolName }</td>
							<td>${c.gradeName }</td>
							<td>${c.majorName }</td>
							<td>${c.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',c.teachingType) }</td>
							<%-- <td>${schoolname }</td> --%>
							<td>${c.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>
							<td>${ghfn:digitalTerm(c.firstyear,c.updateterm) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTermType',c.nowTerm) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTermType',c.updateterm)}
							</td>

							<td>${c.operate eq 'open' ? '开课' : ''}${c.operate eq 'cancel' ? '取消开课' : ''}${c.operate eq 'update' ? '调整开课' : ''}</td>
							<td><c:choose>
									<c:when test="${c.checks eq 'Y' }">
										<font color="green">审核通过</font>
									</c:when>
									<c:when test="${c.checks eq 'N' }">
										<font color="red">审核不通过</font>
									</c:when>
									<c:when test="${c.checks eq 'W' }">待审核</c:when>
								</c:choose></td>
							<td><c:if test="${c.checks eq 'Y' or c.checks eq 'N'}">${c.name}</c:if></td>
							<td><c:if test="${c.checks eq 'Y' or c.checks eq 'N'}">${c.time}</c:if></td>
							<td>${c.applyname}</td>
							<td>${c.applytime }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${coursePage}"
				goPageUrl="${baseUrl }/edu3/sysmanager/exclassroom/listcheck.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>