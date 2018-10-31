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
	$(document).ready(function(){
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		nonExaminationQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function nonExaminationQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "";
		var isadmin = "${isadmin}";
		if(isadmin!='Y'){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeId']}";
		var classicId = "${condition['classic']}";
		
		var majorId = "${condition['major']}";
		var classesId = "${condition['classesId']}";
		var selectIdsJson = "{unitId:'nonexam_brSchoolName',gradeId:'nonExam_grade1',classicId:'nonexam_classic',majorId:'nonExam_examresults_major',classesId:'nonExam_classesid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function nonExaminationQueryUnit() {
		var defaultValue = $("#nonexam_brSchoolName").val();
		var selectIdsJson = "{gradeId:'nonExam_grade1',classicId:'nonexam_classic',majorId:'nonExam_examresults_major',classesId:'nonExam_classesid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function nonExaminationQueryGrade() {
		var defaultValue = $("#nonexam_brSchoolName").val();
		var gradeId = $("#nonExam_grade1").val();
		var selectIdsJson = "{classicId:'nonexam_classic',majorId:'nonExam_examresults_major',classesId:'nonExam_classesid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function nonExaminationQueryClassic() {
		var defaultValue = $("#nonexam_brSchoolName").val();
		var gradeId = $("#nonExam_grade1").val();
		var classicId = $("#nonexam_classic").val();
		var selectIdsJson = "{majorId:'nonExam_examresults_major',classesId:'nonExam_classesid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择专业
	function nonExaminationQueryMajor() {
		var defaultValue = $("#nonexam_brSchoolName").val();
		var gradeId = $("#nonExam_grade1").val();
		var classicId = $("#nonexam_classic").val();
		
		var majorId = $("#nonExam_examresults_major").val();
		var selectIdsJson = "{classesId:'nonExam_classesid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
	}
	
	/* function brschool_Major(){
		var unitId = $("#nonexam_brSchoolName").val();
		var majorId = $("#nonExam_examresults_major").val();
		var classicId = $("#nonexam_classic").val();
		var gradeId   = $("#nonExam_grade1").val();
		var classesId = $("#nonExam_classesid").val();//$("#classesId").val();
		var flag = "unitor";
		var url = "${baseUrl}/edu3/register/studentinfo/brschool_Major.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId,classicId:classicId,gradeId:gradeId,classesId:classesId,flag:flag},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	//$("#major").replaceWith("<select id=\"major\" style=\"width:120px\" name=\"major\" onchange=\"brschool_Major()\">"+data['majorOption']+"</select>");
			    	$("#major").replaceWith("<select  class=\"flexselect textInput\" id=nonExam_examresults_major name=\"major\" tabindex=1 style=width:120px; onchange='brschool_Major()' >"+data['majorOption']+"</select>");
				    $("#major_flexselect").remove();
				    $("#major_flexselect_dropdown").remove();
				    $("#major").flexselect();
			    	$("#stuClasses").replaceWith("<select id=\"nonExam_classesid\" style=\"width: 120px\" name=\"classesId\">"+data['classesOption']+"</select>");
				}
			}
		});
		
	} 	 */
	
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
		var brschool = $("#nonexam_brSchoolName").val();
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
		var brschool = $("#nonexam_brSchoolName").val();
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
		var brschool = $("#nonexam_brSchoolName").val();
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
		var brschool = $("#nonexam_brSchoolName").val();
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
		var examSubId = $("#examResultsManager_ExamSub").val();
		
		//alert("gradeId:" +gradeId +" classesid:" + classesid + " major:" + major + " examSubId:" + examSubId);
		
// 		if(''==examSubId){
// 			alertMsg.warn("考试批次要不为空!");
// 			return false;
// 		}
		return true;
	}
	
	/* 导出补考的学生列表(excel格式) */
	function exportStudents(){
		var examSubId = $("#nonStudyResults_ExamSub1").val();
		if(''==grade){
			grade = "${condition['examSubId']}";
		}
		var grade = $("#nonExam_grade1").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		
		var major = $("#nonExam_examresults_major").val();
		if(''==major){
			major = "${condition['major']}";
		}
		
		var class_id = $("#nonExam_classesid").val();
		if(''==class_id){
			class_id = "${condition['classesId']}";
		}
		
		var course = $("#noExam_list_courseId").val();
		if(''==course){
			course = "${condition['courseId']}";
		}
		
		var branchSchool = $("#nonexam_brSchoolName").val();
		if(''==branchSchool || branchSchool==null){
			branchSchool = "${condition['branchSchool']}";
		}
		 
		var unitId = "${condition['unitId']}";
		//var branchSchool = $("#nonexam_brSchoolName").val();unitId
		
		if(grade== "" || ''==examSubId){
			alertMsg.warn("年级,学期要不为空!");
			return false;
		}else{
			//var url ="${baseUrl }/edu3/teaching/result/nonexamination-student-export.html?gradeId="+grade+"&major="+major+"&courseName="+encodeURI(encodeURI(course))+"&classesId="+class_id+"&examSubId="+examSubId;
			var url ="${baseUrl }/edu3/teaching/result/nonexamination-student-export.html?gradeId="+grade+"&major="+major+"&courseId="+course+"&classesId="+class_id+"&branchSchool="+branchSchool+"&unitId="+unitId;
			window.location.href=url;
		}
	}
	
	//按查询条件导出补考名单
	function exportNonExamResults(){
		var schoolCode = "${schoolCode}";
		var	gradeId 		=		$("#nonExam_grade1").val();//年级
		var major			=		$("#nonExam_examresults_major").val();//专业
		//var	examSubId		=		$("#examResultsManager_ExamSub").val(); //批次
		var	branchSchool	=		$("#nonexam_brSchoolName").val(); //教学点
		var	classic	=		$("#nonexam_classic").val(); //层次
		var	classesId		=		$("#nonExam_classesid").val();//班级
		var courseId = $("#noExam_list_courseId").val();//课程ID
		var term = $("#nonterm").val();//学期
		var yearId = $("#nonyearId").val();//年度
		var examType = $("#nonexamType").val();//考试类型  正考  一补  二补  结补
		var isPass = $("#nonExamination_isPass").val();//是否通过
		var finalPass = $("#nonExamination_finalPass option:selected").val();//最终是否通过
		//nonExamination_isPass
// 		if('' == examSubId){
// 			alertMsg.warn("批次为必选项!");
// 			return false;
// 		}
        if (schoolCode == '10601') {//桂林医
            if (classesId == '') {
                alertMsg.warn("请选择班级进行导出！");
                return false;
            }
        }
		var url       = "${baseUrl}/edu3/teaching/result/nonexamination-export.html?gradeId="+gradeId+"&major="+major+"&branchSchool="
				+branchSchool+"&classicId="+classic+"&classesId="+classesId+"&courseId="+courseId+"&yearId="+yearId+"&term="+term+"&examType="+examType+"&isPass="+isPass+"&finalPass="+finalPass;	
		var msg   = "确定要按查询条件导出补考名单吗？";
		alertMsg.confirm(msg, {
               okCall: function(){
            	   $('#frame_exportNonExamResults_byclass').remove();
	   				var iframe = document.createElement("iframe");
	   				iframe.id = "frame_exportNonExamResults_byclass";
	   				iframe.src = url;
	   				iframe.style.display = "none";
	   				//创建完成之后，添加到body中
	   				document.body.appendChild(iframe);
               }
		});
	}

	function gzdxExportNonExamResults(){
		var	gradeId 		=		$("#nonExam_grade1").val();//年级
		var major			=		$("#nonExam_examresults_major").val();//专业
		//var	examSubId		=		$("#examResultsManager_ExamSub").val(); //批次
		var	branchSchool	=		$("#nonexam_brSchoolName").val(); //教学点
		var	classic	=		$("#nonexam_classic").val(); //层次
		var	classesId		=		$("#nonExam_classesid").val();//班级
		var courseId = $("#noExam_list_courseId").val();//课程ID
		var term = $("#nonterm").val();//学期
		var yearId = $("#nonyearId").val();//年度
		var examType = $("#nonexamType").val();//考试类型  正考  一补  二补  结补
		var isPass = $("#nonExamination_isPass").val();//是否通过
		var url       = "${baseUrl}/edu3/teaching/result/nonexamination-export-gzdx.html?gradeId="+gradeId+"&major="+major+"&branchSchool="
		+branchSchool+"&classicId="+classic+"&classesId="+classesId+"&courseId="+courseId+"&yearId="+yearId+"&term="+term+"&examType="+examType+"&isPass="+isPass;	
		var msg   = "确定要按查询条件导出补考名单吗？";
		alertMsg.confirm(msg, {
               okCall: function(){
            	   $('#frame_exportNonExamResults_byclass').remove();
	   				var iframe = document.createElement("iframe");
	   				iframe.id = "frame_exportNonExamResults_byclass";
	   				iframe.src = url;
	   				iframe.style.display = "none";
	   				//创建完成之后，添加到body中
	   				document.body.appendChild(iframe);
               }
		});
	}
</script>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="nonExaminationStudentSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/nonexamination-student-list.html"
				method="post">
				<input name="fromPage" value="Y" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<!-- 			    <li> -->
						<!-- 					<label>考试批次：</label> -->
						<%-- 					<gh:selectModel id="examResultsManager_ExamSub" name="examSubId" bindValue="resourceid" displayValue="batchName"  style="width:55%"  --%>
						<%-- 												modelClass="com.hnjk.edu.teaching.model.ExamSub" value="${condition['examSubId']}"  condition="isDeleted=0 and batchType='exam' and examType<>'N'" orderBy="examinputStartTime desc"/> --%>
						<!-- 												<span style="color:red;">*</span> -->
						<!-- 				</li>	 -->
						<li><label>年度：</label> <gh:selectModel id="nonyearId"
								name="yearId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" orderBy="firstyear desc"
								style="width:120px;" /></li>

						<li class="custom-li"><label>教学站：</label> <%--<gh:brSchoolAutocomplete name="branchSchool" tabindex="2" id="nonexam_brSchoolName" displayType="code" onchange="//ajaxFreshMajorInNonExamByBranchSchool();" defaultValue="${condition['branchSchool']}" style="width:55%" /> --%>
							<%-- <select class="flexselect" id="nonexam_brSchoolName" onchange="searchExamResultChangMajor()" name="branchSchool" tabindex=1 style=width:120px onchange="brschool_Major()" >${unitOption}</select> --%>
							<span sel-id="nonexam_brSchoolName" sel-name="branchSchool"
										sel-onchange="nonExaminationQueryUnit()"
										sel-classs="flexselect"></span>
						</li>
						<li><label>年级：</label> <span sel-id="nonExam_grade1"
							sel-name="gradeId" sel-onchange="nonExaminationQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层 次：</label> <span sel-id="nonexam_classic"
							sel-name="classic" sel-onchange="nonExaminationQueryClassic()"
							sel-style="width: 100px"></span></li>
					</ul>
					<ul class="searchContent">
						<li><label>学期：</label> <gh:select id="nonterm" name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:120px;" /></li>
						<li id="nonexam-gradeToMajor4" class="custom-li"><label>专业：</label> <span
							sel-id="nonExam_examresults_major" sel-name="major"
							sel-onchange="nonExaminationQueryMajor()" sel-classs="flexselect"
							></span></li>
						<li><label>当前通过：</label> <gh:select
								id="nonExamination_isPass" name="isPass"
								value="${condition['isPass']}" dictionaryCode="yesOrNo_default"
								style="width:120px;" /></li>
						<li><label>最终通过：</label><gh:select
								id="nonExamination_finalPass" name="finalPass"
								value="${condition['finalPass']}" dictionaryCode="yesOrNo"
								style="width:100px;" />
							<%-- <select id="nonExamination_finalPass" name="finalPass" style="width: 100px;">
								<option value="">请选择</option>
								<option value='Y'
									<c:if test="${condition['finalPass'] eq 'Y'}">selected="selected"</c:if>>最终通过</option>
								<option value='N'
									<c:if test="${condition['finalPass'] eq 'N'}">selected="selected"</c:if>>最终不通过</option>
						</select> --%></li>
					</ul>
					<ul class="searchContent">
						<li>课程：<gh:courseAutocomplete name="courseId"
								tabindex="1" id="noExam_list_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" style="width:70%" /> <%--
					<input type="text" id ="noExamCourseNameId" name="courseName" value="${condition['courseName']}"/>
					 --%></li>
						<li class="custom-li"><label>班级：</label> <span sel-id="nonExam_classesid"
							sel-name="classesId" sel-classs="flexselect"
							></span></li>
						<li><label>考试类型：</label> <gh:select id="nonexamType"
							name="examType" dictionaryCode="ExamResult"
							filtrationStr="Y,T,Q" value="${condition['examType']}"
							style="width:120px;" /></li>
						<li>
							教学形式：
							<gh:select id="nonExam_courseTeachType" name="courseTeachType"
									   value="${condition['courseTeachType'] }"
									   dictionaryCode="CodeCourseTeachType" style="width:120px;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>学号：</label>
							<input type="text" name="studyNo" value="${condition['studyNo']}">
						</li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit"
									onclick="return isValidateNonInputExamination();">查 询
								</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>

		<gh:resAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_STU_LIST"
			pageType="list"></gh:resAuth>
		<div class="pageContent">
			<%-- 
		<table class="table" layouth="140" width="100%">
		 --%>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="14%"
							style="text-align: center; vertical-align: middle;">教学站</th>
						<th width="6%">年级</th>
						<th width="5%">层次</th>
						<th width="8%">专业</th>
						<th width="6%">姓名</th>
						<th width="8%"
							style="text-align: center; vertical-align: middle;">学号</th>
						<th width="5%">成绩</th>
						<th width="5%">补考成绩</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="4%">教学形式</th>
						<th width="8%"
							style="text-align: center; vertical-align: middle;">考试批次</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">班级</th>
						<th width="6%">(当前/最终)通过</th>

					</tr>
				</thead>
				<tbody id="nonExaminationStudentBody">
					<c:forEach items="${page.result}" var="failExamStudentVo"
						varStatus="vs">
						<tr>

							<td>${failExamStudentVo.unitName }</td>
							<td>${failExamStudentVo.gradename }</td>
							<td>${failExamStudentVo.classicname }</td>
							<td>${failExamStudentVo.majorName }</td>
							<td>${failExamStudentVo.studentName }</td>
							<td>${failExamStudentVo.studyNo }</td>
							
							<td>
								<c:choose>
									<c:when test="${failExamStudentVo.examabnormity ne '0'}">
									   ${ghfn:dictCode2Val('CodeExamAbnormity',failExamStudentVo.examabnormity) }
									</c:when>
									<c:otherwise> ${failExamStudentVo.integratedscore } </c:otherwise>
				            	</c:choose>
							</td>
							<td <c:if test="${failExamStudentVo.nextScore eq '无'}">style="color: red" </c:if>>${failExamStudentVo.nextScore }</td>
							<td>${failExamStudentVo.courseName}</td>
							<td>${ghfn:dictCode2Val('CodeCourseTeachType',failExamStudentVo.teachType)}</td>
							<td>${failExamStudentVo.subyear}年第${failExamStudentVo.subterm}学期
									${ghfn:dictCode2Val('ExamResult',failExamStudentVo.examType)}</td>
							<td>${failExamStudentVo.className }</td>
							<%-- 			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('yesOrNo',failExamStudentVo.examabnormity) }</td> --%>
							<td>${ghfn:dictCode2Val('yesOrNo',failExamStudentVo.isPass)} /
								<c:if test="${failExamStudentVo.finalPass eq 'Y'}">是</c:if>
								<c:if test="${failExamStudentVo.finalPass eq 'N'}">否</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/result/nonexamination-student-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>