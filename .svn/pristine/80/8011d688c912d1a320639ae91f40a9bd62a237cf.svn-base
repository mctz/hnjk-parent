<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级教学计划课程开课状态</title>
</head>
<body>
	<script type="text/javascript">
$(document).ready(function(){
	//$("#teachingPlanCourseStatus_majorid").flexselect();
	courseStatusQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function courseStatusQueryBegin() {
	var defaultValue = "${condition['brSchoolid']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['major']}";
	
	var selectIdsJson = "{unitId:'CourseStatus_brSchoolid',gradeId:'CourseStatus_gradeid',classicId:'CourseStatus_classicid',teachingType:'CourseStatus_teachingType',majorId:'major'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, "", selectIdsJson);
}

// 选择教学点
function courseStatusQueryUnit() {
	var defaultValue = $("#CourseStatus_brSchoolid").val();
    var gradeId = $("#CourseStatus_gradeid").val();
    var classicId = $("#CourseStatus_classicid").val();
    var teachingTypeId = $("#CourseStatus_teachingType").val();
	var selectIdsJson = "{gradeId:'CourseStatus_gradeid',classicId:'CourseStatus_classicid',teachingType:'CourseStatus_teachingType',majorId:'major'}";
	cascadeQuery("unit", defaultValue, "", gradeId, classicId, teachingTypeId, "", "",selectIdsJson);
}

// 选择年级
function courseStatusQueryGrade() {
	var defaultValue = $("#CourseStatus_brSchoolid").val();
	var gradeId = $("#CourseStatus_gradeid").val();
	var selectIdsJson = "{classicId:'CourseStatus_classicid',teachingType:'CourseStatus_teachingType',majorId:'major'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function courseStatusQueryClassic() {
	var defaultValue = $("#CourseStatus_brSchoolid").val();
	var gradeId = $("#CourseStatus_gradeid").val();
	var classicId = $("#CourseStatus_classicid").val();
	var selectIdsJson = "{teachingType:'CourseStatus_teachingType',majorId:'major'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function courseStatusQueryTeachingType() {
	var defaultValue = $("#CourseStatus_brSchoolid").val();
	var gradeId = $("#CourseStatus_gradeid").val();
	var classicId = $("#CourseStatus_classicid").val();
	var teachingTypeId = $("#CourseStatus_teachingType").val();
	var selectIdsJson = "{majorId:'major'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 这个方法添加了一个_old，表示不用这个旧方法
function changeTeachingPlanCourseStatus_old(isOpen){
	var postUrl = "${baseUrl}/edu3/teaching/teachingplancoursestatus/save_old.html";
	var bodyname = "#teachingPlanCourseStatusBody";
	if(!isChecked('resourceid',bodyname)){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	
	var res = [], gid = [], cid = [],tid = [],gn = [];
	var k = 0;
	$(bodyname+" input[@name='resourceid']:checked").each(function(){
         res.push($(this).val()+' ');
         gid.push($(this).attr('guiplanid'));   
         cid.push($(this).attr('plancourseid'));
         tid.push($(this).attr('termid'));
         gn.push($(this).attr('gname'));
    });
	for(var i=0;i<gn.length;i++){
		if(gn[0]!=gn[i]){
			alertMsg.warn('不允许批量开不同年级的课！');
			return false;
		}
	}
	
	if('Y' == isOpen){
		var assessMsg;
		$.ajax({//加载选择框
	 		url: '${baseUrl}/edu3/teaching/teachingplancoursestatus/getOpenCourseSelect.html',
	 		type: 'POST',
	 		dataType: 'json', 	
	 		data:{resourceid:res.join(','),guiplanid:gid.join(','),plancourseid:cid.join(','),termid:tid.join(',')},//参数设置 	
	 		success: function(json){  
	 			if(200 == json.status){
	 				alertMsg.confirm("选择开课学期: "+json.msg,{okCall:function(){
		  				var term = $('#setTeachPlanCourseTime1').val();
		  				if('' == term){
		  					alert("请选择开课学期！");
		  					return false;
		  				}
		  				$.post(postUrl,{resourceid:res.join(','),guiplanid:gid.join(','),plancourseid:cid.join(','),termid:tid.join(','),isOpen:isOpen,term:term}, navTabAjaxDone, "json");
		  			}}); 
	 			}else{
	 				alertMsg.confirm(json.msg);
	 			}
			 }
		});
		
		//var assessMsg = "选择开课学期: <gh:select id='setTeachPlanCourseTime1'  name='setTeachPlanCourseTime1' dictionaryCode='CodeCourseTermType'/>";
	}else{
		alertMsg.confirm("您确定要取消开课吗？", {
			okCall: function(){	
				$.post(postUrl,{resourceid:res.join(','),guiplanid:gid.join(','),plancourseid:cid.join(','),termid:tid.join(','),isOpen:isOpen}, navTabAjaxDone, "json");
			}
		});	
	}
}

    // 现在使用的方法，新的逻辑;注：将开课功能分离为开课和调整开课
	function changeTeachingPlanCourseStatus(isOpen){
		var postUrl = "${baseUrl}/edu3/teaching/teachingplancoursestatus/saveOpenCourse.html";
		var bodyname = "#teachingPlanCourseStatusBody";
		if(!isChecked('resourceid',bodyname)){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		
		var res = [], gid = [], cid = [],tid = [],gn = [];
		var k = 0;
		var hasNull = false;
		var openedCourse = false;
		$(bodyname+" input[@name='resourceid']:checked").each(function(){
			 var isOpened = $(this).attr("isOpened"); 
			 if('Y'!=isOpen && isOpened!='Y'){
				 hasNull = true;
			 } 
			 if('Y'==isOpen && isOpened=='Y'){
				 openedCourse = true;
			 }
	         res.push($(this).val()+'');
	         gid.push($(this).attr('guiplanid'));   
	         cid.push($(this).attr('plancourseid'));
	         tid.push($(this).attr('termid'));
	         gn.push($(this).attr('gname'));
	    });
		if('Y'!=isOpen && hasNull){
			alertMsg.warn('请选择已经开课的课程！');
			return false;
		}
		if('Y'==isOpen && openedCourse){
			alertMsg.warn('请选择还未开课的课程！');
			return false;
		}
		if('N'!=isOpen){
			for(var i=0;i<gn.length;i++){
				if(gn[0]!=gn[i]){
					alertMsg.warn('不允许批量开或调整不同年级的课！');
					return false;
				}
			}
		}
		if('Y' == isOpen || 'W' == isOpen){//开课和调整开课
			var selectSchoolUrl = baseUrl+"/edu3/teaching/teachingplancoursestatus/select-schoolAndTerm.html?resourceid="+res.join(',')
			+"&guiplanid="+gid.join(',')+"&plancourseid="+cid.join(',')+"&termid="+tid.join(',')+"&isOpen="+isOpen;
			// 选择要开课的教学点和学期
			$.pdialog.open(selectSchoolUrl, 'RES_TEACHING_SELECTSCHOOLANDTERM', '选择开课信息', {mask:true,width: 300, height: 200});
            
		} else {
			alertMsg.confirm("您确定要取消开课吗？", {
				okCall: function(){	
					$.post(postUrl,{resourceid:res.join(','),guiplanid:gid.join(','),plancourseid:cid.join(','),termid:tid.join(','),isOpen:isOpen}, navTabAjaxDone, "json");
				}
			});	
		}
	}

function showOpenStatus(resid){
	$.pdialog.open("${baseUrl}/edu3/teaching/teachingplancoursestatus/showOpenSchool.html?resid="+resid
			,"selector","教学点开课情况",{mask:true,height:600,width:800});
}
 
 // 自动开课
 function autoOpenCourses(){
	 $.pdialog.open("${baseUrl}/edu3/teaching/teachingplancoursestatus/selectTerm.html","autoOpenCourseSelectTerm","选择开课学期",{mask:true,height:200,width:300});
 }
 
 // 设置课程教学形式
 function setCourseTeachType() {
	 var resids = [];
	 var isAllOpened = true;
	 $("#teachingPlanCourseStatusBody input[@name='resourceid']:checked").each(function(){
		 var isOpened = $(this).attr("isOpened"); 
		 if(isAllOpened && isOpened!='Y'){
			 isAllOpened = false;
		 } 
		 resids.push($(this).val());
    });
	 if(resids.length < 1){
			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 }
	 if(!isAllOpened){
			alertMsg.warn('请选择已开课的记录！');
			return false;
	 }
	 $.pdialog.open("${baseUrl}/edu3/teaching/teachingplancoursestatus/setCourseTeachType-form.html?resourceids="+resids.toString(),"setCourseTeachTypeForm","设置课程教学类型",{mask:true,height:200,width:300});
 }
 
//设置课程考试形式
 function setExamForm() {
	 var resids = [];
	 var isAllOpened = true;
	 $("#teachingPlanCourseStatusBody input[@name='resourceid']:checked").each(function(){
		 var isOpened = $(this).attr("isOpened"); 
		 if(isAllOpened && isOpened!='Y'){
			 isAllOpened = false;
		 } 
		 resids.push($(this).val());
    });
	 if(resids.length < 1){
			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 }
	 if(!isAllOpened){
			alertMsg.warn('请选择已开课的记录！');
			return false;
	 }
	 $.pdialog.open("${baseUrl}/edu3/teaching/teachingplancoursestatus/setCourseExamForm-form.html?resourceids="+resids.toString(),"setCourseExamForm","设置课程考试形式",{mask:true,height:200,width:300});
 }

 function teachingPlanCourseStatusExport() {
	 var gradeid = $("#CourseStatus_gradeid").val();
	 var classicid = $("#CourseStatus_classicid").val();
	 var teachingType = $("#CourseStatus_teachingType").val(); //学习形式
	 var brSchoolid =$("#CourseStatus_brSchoolid").val();
	 var majorid =$("#major").val();
	 var courseId =$("#faceStudyExamResults_courseId").val();
	 var term =$("#teachingPlanCourseStatus_courseterm").val();
	 var orgterm =$("#teachingPlanCourseStatus_orgterm").val();
	 var isOpenCourse =$("#isOpenCourse").val();
	 var teachType =$("#teachType").val(); //教学形式
	 var courseExamForm =$("#CourseStatus_courseExamForm").val(); 
	 var num = $("#teachingPlanCourseStatusBody input[name='resourceid']:checked").size();
	 var ids="";
	 var guiplanids="";
	 var plancourseids="";
	 var plancourseids="";
	 var planids="";
	 var maxNum=0; //判断是否拼接过长
	 var url = "${baseUrl}/edu3/teaching/teachingplancoursestatus/teachingPlanCourseStatus-export.html?"+"&gradeid="+gradeid
			 +"&classicid="+classicid+"&teachingType="+teachingType+"&brSchoolid="+brSchoolid+"&majorid="+majorid
			 +"&courseId="+courseId+"&term="+term+"&orgterm="+orgterm+"&isOpenCourse="+isOpenCourse
			 +"&teachType="+teachType+"&courseExamForm="+courseExamForm;
	 if(num>0){
	  $("#teachingPlanCourseStatusBody input[name='resourceid']").each(function(){
			if($(this).attr("checked")){
				if ($(this).val()!=null&&$(this).val()!="") {
					ids +=$(this).val()+",";
				}else{
					guiplanids+=$(this).attr("guiplanid")+",";
					plancourseids+=$(this).attr("plancourseid")+",";
					planids+=$(this).attr("planid")+",";
					maxNum++;
				}
			}
	  });
	  if (maxNum>50) {
	    alert("当前选定未开课的数据为"+maxNum+">50条，导致数据传输过大，请减少对未开课数据的勾选");
		return;
	  }
	  url+="&ischecked=true&ids="+ids+"&guiplanids="+guiplanids+"&plancourseids="+plancourseids+"&planids="+planids;
	 }

	alertMsg.confirm("确定导出开课数据吗?", {
		okCall: function(){
			downloadFileByIframe(url,'RES_TEACHING_TEACHINGPLANCOURSESTATUS_EXPORT',"post");
		}
	});	
 }
	
	
</script>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingplancoursestatus/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${not isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="CourseStatus_brSchoolid" sel-name="brSchoolid"
								sel-onchange="courseStatusQueryUnit()" sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" id="CourseStatus_brSchoolid"
								name="brSchoolid" value="${condition['brSchoolid'] }"
								style="width: 120px" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="CourseStatus_gradeid"
							sel-name="gradeid" sel-onchange="courseStatusQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="CourseStatus_classicid"
							sel-name="classicid" sel-onchange="courseStatusQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习方式：</label> <span
							sel-id="CourseStatus_teachingType" sel-name="teachingType"
							sel-onchange="courseStatusQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>

					</ul>
					<ul class="searchContent">
						<li  class="custom-li"><label>专业：</label>
							<span sel-id="major" sel-name="major" sel-classs="flexselect"></span>
						</li>
						<li><label>开课状态：</label> <select name="isOpenCourse"
							id="isOpenCourse" style="width: 120px;">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isOpenCourse'] eq 'Y'}">selected="selected"</c:if>>已开课</option>
								<option value="N"
									<c:if test="${condition['isOpenCourse'] eq 'N'}">selected="selected"</c:if>>未开课</option>
						</select></li>
						<li><label>教学形式：</label> <select name="teachType"
							id="teachType" style="width: 120px;">
							<option value="">请选择</option>
							<option value="networkTeach"
								<c:if test="${condition['teachType'] eq 'networkTeach'}">selected="selected"</c:if>>网授课程</option>
							<option value="faceTeach"
								<c:if test="${condition['teachType'] eq 'faceTeach'}">selected="selected"</c:if>>面授课程</option>
						</select></li>
						<li><label>考试形式：</label> <gh:select
								id="CourseStatus_courseExamForm" name="courseExamForm"
								dictionaryCode="CodeCourseExamForm" style="width: 120px" />
						<%--
				<li>
					<label>操作：</label>
					<select name="isOpenStatus" style="width: 150px;">
						<option value="">请选择</option>
						<option value="Y" <c:if test="${condition['isOpenStatus'] eq 'Y'}">selected="selected"</c:if> >开课</option>
						<option value="N" <c:if test="${condition['isOpenStatus'] eq 'N'}">selected="selected"</c:if>>不开课</option>
					</select>
				</li> --%>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程:</label> <gh:courseAutocomplete name="courseId"
							id="faceStudyExamResults_courseId" tabindex="1"
							displayType="code" isFilterTeacher="Y" style="width:240px;"
							value="${condition['courseId'] }" /></li>
						<li><label>上课学期：</label> <gh:select
							id="teachingPlanCourseStatus_courseterm" name="term"
							value="${condition['term']}" dictionaryCode="CodeCourseTermType"
							style="width:120px" /></li>
						<li><label>原计划学期：</label> <gh:select
							id="teachingPlanCourseStatus_orgterm" name="orgterm"
							value="${condition['orgterm']}" dictionaryCode="CodeTermType"
							style="width:120px" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_TEACHINGPLANCOURSESTATUS"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="165">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" style="display: block;"
							name="checkall" id="check_all_teachingPlanCourseStatus"
							onclick="checkboxAll('#check_all_teachingPlanCourseStatus','resourceid','#teachingPlanCourseStatusBody')" /></th>
						<th width="6%">年级</th>
						<th width="15%">专业</th>
						<th width="5%">层次</th>
						<th width="6%">学习方式</th>
						<th width="11%">教学站</th>
						<th width="10%">课程</th>
						<th width="6%">原计划学期</th>
						<th width="5%">现学期</th>
						<th width="8%">上课学期</th>
						<th width="6%">教学形式</th>
						<th width="6%">考试形式</th>
						<th width="6%">操作状态</th>
						<th width="6%">开课状态</th>
					</tr>
				</thead>
				<tbody id="teachingPlanCourseStatusBody">
					<c:forEach items="${coursePage.result}" var="c" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${c.resourceid }" gname="${c.gradeName}"
								guiplanid="${c.guiplanid }" plancourseid="${c.plancourseid}"
								termid="${c.term}" isOpened="${c.isopen }" planid="${c.planid}"
								autocomplete="off" /></td>
							<td>${c.gradeName }</td>
							<td>${c.majorName }</td>
							<td>${c.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',c.teachingType) }</td>
							<%--<td>${!empty unitname ? unitname : c.unitName }</td> --%>
							<td>${c.schoolname }</td>
							<td>${c.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>
							<td>${ghfn:digitalTerm(c.firstyear,c.courseterm) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTermType',c.courseterm) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTeachType',c.teachType) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseExamForm',c.examForm) }</td>
							<td><c:choose>
									<c:when
										test="${c.checks=='openW'||c.checks=='updateW'||c.checks=='cancelW' }">
										<font color="red">${ghfn:dictCode2Val('CodeOpenCourseStatus',c.checks) }</font>
									</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeOpenCourseStatus',c.checks) }</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${c.isopen eq 'Y'}">
										<font color="green">已开课</font>
									</c:when>
									<c:otherwise>未开课</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<%-- <gh:page page="${coursePage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingplancoursestatus/list.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${coursePage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingplancoursestatus/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>