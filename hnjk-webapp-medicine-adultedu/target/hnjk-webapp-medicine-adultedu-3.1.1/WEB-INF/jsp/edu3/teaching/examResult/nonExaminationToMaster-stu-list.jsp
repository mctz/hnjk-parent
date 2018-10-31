<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>非统考课程补考名单</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}";
		if(null!=msg&&""!=msg){
			alertMsg.warn(msg);
		}
	});
	
	function ajaxRefreshClassesAndMajor(){
		var brschool = "${condition['unitId']}";
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		var major_id = $("#nonExam_examresults_major").val();
		var class_id = $("#nonExam_classesid").val();
		$.ajax({
			type:'post',
			url:url,
			data:{brschool:brschool,major:major_id,class_id:class_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_classesid").html(data['classes']);
				}
			}
		}); 
	}
	
	function ajaxFreshInNonExamByGrade(){
		var grade = $("#nonExam_grade1").val();
		if(''==grade){
			grade="${condition['gradeId']}";
		}
		var brschool = $("#nonbranchSchool").val();
		if(''==brschool || brschool==null){
			brschool="${condition['branchSchool']}";
		}
		var major_id = $("#nonExam_examresults_major").val();
		if(''==major_id || major_id==null){
			major_id="${condition['major']}";
		}
		
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_classesid").html(data['classes']);
				}
			}
		}); 
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_examresults_major").html(data['majorList']);
				}
			}
		}); 
	} 
	
	function ajaxFreshMajorInNonExamByBranchSchool(){
		var grade = $("#nonExam_grade1").val();
		var brschool = $("#nonbranchSchool").val();
		//var class_id = $("#nonExam_classesid").val();
		var major_id = $("#nonExam_examresults_major").val();
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_classesid").html(data['classes']);
				}
			}
		}); 
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_examresults_major").html(data['majorList']);
				}
			}
		}); 
	} 
	
	function ajaxFreshInNonExamByMajor(){
		var grade = $("#nonExam_grade1").val();
		//var brschool = "${condition['unitId']}";
		var brschool = $("#nonbranchSchool").val();
		//var class_id = $("#nonExam_classesid").val();
		var major_id = $("#nonExam_examresults_major").val();
		
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_classesid").html(data['classes']);
				}
			}
		});
		
	}

	function ajaxFreshClasses(){
		var grade = $("#nonExam_grade1").val();
		var brschool = "${condition['unitId']}";
		var class_id = $("#nonExam_classesid").val();
		var major_id = $("#nonExam_examresults_major").val();
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClasses.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,class_id:class_id,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_classesid").html(data['classes']);
				}
			}
		});
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_examresults_major").html(data['majorList']);
				}
			}
		}); 
	}
	
	function ajaxFreshMajorInNonExam(){
		var brschool = $("#nonbranchSchool").val();
		var major_id = $("#nonExam_examresults_major").val();
		var grade = $("#nonExam_grade1").val();
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#nonExam_examresults_major").html(data['majorList']);
				}
			}
		}); 
	}
	
	//查询条件约束检查
	function isValidateNonInputExamination(){
		
		var gradeId = $("#nonExam_grade1").val();
		var major = $("#nonExam_examresults_major").val();
		var classesid = $("#nonExam_classesid").val();
		var examSubId = $("#nonStudyResults_ExamSub1").val();
		
		//alert("gradeId:" +gradeId +" classesid:" + classesid + " major:" + major + " examSubId:" + examSubId);
		
		if(''==gradeId || ''==examSubId){
			alertMsg.warn("年级,学期要不为空!");
			return false;
		}
		return true;
	}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="nonExaminationMasterStuSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/nonExaminationToMaster-stu-list.html"
				method="post">
				<input name="fromPage" value="Y" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">

						<li><label>年级：</label> <gh:selectModel id="nonExam_grade1"
								name="gradeId" bindValue="resourceid" displayValue="gradeName"
								style="width:55%" modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeId']}"
								onchange="ajaxFreshInNonExamByGrade()" orderBy="gradeName desc" />
							<span style="color: red;">*</span></li>
						<li><c:set
								value=" and exists ( from com.hnjk.edu.teaching.model.TeachingGuidePlan p where p.isDeleted=0 and p.teachingPlan.major.resourceid=t.resourceid "
								var="exCondition" /> <c:if
								test="${not empty condition['branchSchool'] }">
								<c:set
									value="${exCondition } and p.teachingPlan.orgUnit.resourceid='${condition.branchSchool }' "
									var="exCondition" />
							</c:if> <c:if test="${not empty condition['gradeId'] }">
								<c:set
									value="${exCondition } and p.grade.resourceid='${condition.gradeId }' "
									var="exCondition" />
							</c:if> <c:set value="${exCondition } ) " var="exCondition" /> <label>专业：</label>
							<gh:selectModel id="nonExam_examresults_major" name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" orderBy="majorCode"
								onchange="ajaxFreshInNonExamByMajor()"
								exCondition="${exCondition }" style="width:55%" /></li>
						<li><label>学期：</label> <select id="nonStudyResults_ExamSub1"
							name="examSubId" style="width: 55%">
								<option value=""></option>
								<c:forEach items="${examSubs}" var="examSub">
									<option
										<c:if test="${condition['examSubId'] eq examSub.resourceid}">selected = "selected"</c:if>
										value="${examSub.resourceid}">${examSub.term eq '1' ?examSub.yearInfo.firstYear:(examSub.yearInfo.firstYear+1)}年${examSub.term eq '1' ?"秋季":"春季"}</option>
								</c:forEach>
						</select> <span style="color: red;">*</span> <%--
					<gh:selectModel id="nonStudyResults_ExamSub1" name="examSubId" bindValue="resourceid" displayValue="batchName"  style="width:55%" 
							modelClass="com.hnjk.edu.teaching.model.ExamSub" value="${condition['examSubId']}"  condition="batchType='exam'" orderBy="examinputStartTime desc"/>
					--%> <%-- 
					<gh:select id="teachingPlanCourseStatus_term1" name="term" value="${condition['term']}" dictionaryCode="CodeCourseTermType" style="width:55%"  />
					
					<gh:select id="teachingPlanCourseStatus_term1" name="term" value="${condition['term']}" dictionaryCode="CodeTerm" style="width:55%" />
				--%> <c:if test="${isadmin eq 'Y' }">
								<li><label>教学站：</label> <gh:brSchoolAutocomplete
										name="branchSchool" tabindex="2" id="nonbranchSchool"
										displayType="code"
										onchange="ajaxFreshMajorInNonExamByBranchSchool();"
										defaultValue="${condition['branchSchool']}" style="width:53%" />
								</li>
							</c:if>
					</ul>

					<ul class="searchContent">
						<label>班级：</label>
						<c:choose>
							<c:when
								test="${not empty condition['branchSchool'] and not empty condition['gradeId'] and not empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition="brSchool.resourceid='${condition['branchSchool']}' and isDeleted=0 and grade.resourceid='${condition['gradeId']}' and major.resourceid='${condition['major']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${not empty condition['branchSchool'] and not empty condition['gradeId'] and empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition="brSchool.resourceid='${condition['branchSchool']}' and isDeleted=0 and grade.resourceid='${condition['gradeId']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${not empty condition['branchSchool'] and empty condition['gradeId'] and empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition="brSchool.resourceid='${condition['branchSchool']}' and isDeleted=0  and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${not empty condition['branchSchool'] and empty condition['gradeId'] and not empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition="brSchool.resourceid='${condition['branchSchool']}' and isDeleted=0 and major.resourceid='${condition['major']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${not empty condition['branchSchool'] and empty condition['gradeId'] and empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']} "
									condition="brSchool.resourceid='${condition['branchSchool']}' and isDeleted=0 and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${empty condition['branchSchool'] and not empty condition['gradeId'] and not empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition="isDeleted=0 and major.resourceid='${condition['major']}' and grade.resourceid='${condition['gradeId']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${empty condition['branchSchool'] and not empty condition['gradeId'] and empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition=" isDeleted=0 and grade.resourceid='${condition['gradeId']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:when
								test="${empty condition['branchSchool'] and empty condition['gradeId'] and not empty condition['major']}">
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition=" isDeleted=0 and major.resourceid='${condition['major']}' and classesMasterId='${condition['classesMasterId'] }'" />
							</c:when>
							<c:otherwise>
								<gh:selectModel id="nonExam_classesid" name="classesId"
									bindValue="resourceid" displayValue="classname"
									style="width:350px"
									modelClass="com.hnjk.edu.roll.model.Classes"
									orderBy="classname desc" value="${condition['classesId']}"
									condition=" isDeleted=0 and classesMasterId='${condition['classesMasterId'] }'" />
							</c:otherwise>
						</c:choose>
					</ul>

					<ul class="searchContent">
						<li><label>课程：</label> <input type="text"
							id="noExamCourseNameId" name="courseName"
							value="${condition['courseName']}" /></li>
					</ul>
					<ul>
						<li><div class="buttonActive" style="float: right">
								<div class="buttonContent">
									<button type="submit"
										onclick="return isValidateNonInputExamination();">查 询
									</button>
								</div>
							</div></li>
					</ul>

				</div>
			</form>
		</div>

		<gh:resAuth parentCode="RES_CLASSESMASTER_FACESTUDY_STULIST"
			pageType="list"></gh:resAuth>
		<div class="pageContent">
			<%-- 
		<table class="table" layouth="140" width="100%">
		 --%>
			<table class="table" layouth="188">
				<thead>
					<tr>

						<th width="15%"
							style="text-align: center; vertical-align: middle;">教学站</th>
						<th width="10" style="text-align: center; vertical-align: middle;">姓名</th>
						<th width="15%"
							style="text-align: center; vertical-align: middle;">学号</th>
						<th width="20%"
							style="text-align: center; vertical-align: middle;">专业</th>
						<th width="10" style="text-align: center; vertical-align: middle;">成绩</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="30%"
							style="text-align: center; vertical-align: middle;">班级</th>
					</tr>
				</thead>
				<tbody id="nonExaminationMasterStuBody">
					<c:forEach items="${page.result}" var="failExamStudentVo"
						varStatus="vs">
						<tr>

							<td style="text-align: center; vertical-align: middle;">${failExamStudentVo.unitName }</td>
							<td style="text-align: center; vertical-align: middle;">
								${failExamStudentVo.studentName }</td>
							<td style="text-align: center; vertical-align: middle;">
								${failExamStudentVo.studyNo }</td>
							<td style="text-align: center; vertical-align: middle;">${failExamStudentVo.majorName }</td>
							<td style="text-align: center; vertical-align: middle;">
								<%-- <c:choose> 
				            	<c:when test="${empty failExamStudentVo.integratedscore}">   
								   ${ghfn:dictCode2Val('CodeExamAbnormity',failExamStudentVo.examabnormity) }
								</c:when> 
								<c:otherwise>   --%> ${failExamStudentVo.integratedscore } <%-- </c:otherwise>
				            </c:choose> 	 --%>
							</td>
							<td style="text-align: center; vertical-align: middle;">${failExamStudentVo.courseName}</td>
							<td style="text-align: center; vertical-align: middle;">${failExamStudentVo.className }</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page }"
				goPageUrl="${baseUrl }/edu3/teaching/result/nonExaminationToMaster-stu-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>