<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统考成绩列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="facestudy_inputExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/universalExam/universalExam-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <c:choose>
								<c:when test="${isUEBrSchool }">
									<input id="query_universalExam_brSchoolName" type="text"
										name="UESchoolName" value="${UESchoolName}"
										readonly="readonly" style="width: 240px;" />
									<input id="query_universalExam_brSchoolId" type="hidden"
										name="UESchool" value="${condition['UESchool']}" />
								</c:when>
								<c:otherwise>
									<span sel-id="query_universalExam_brSchoolId"
										sel-name="UESchool" sel-onchange="universalExamQueryUnit()"
										sel-classs="flexselect" sel-style="width: 240px"></span>
								</c:otherwise>
							</c:choose></li>
						<li><label>年级：</label> <span
							sel-id="query_universalExam_gradeid" sel-name="UEGradeid"
							sel-onchange="universalExamQueryGrade()" sel-style="width: 120px"></span>

						</li>
						<li><label>层次：</label> <span
							sel-id="query_universalExam_classic" sel-name="UEClassic"
							sel-onchange="universalExamQueryClassic()"
							sel-style="width: 120px"></span></li>
						
					</ul>
					<ul class="searchContent">
						<li id="universalExam_major_select" class="custom-li"><label>专业：</label> <span
							sel-id="query_universalExam_major" sel-name="UEMajor"
							sel-onchange="universalExamQueryMajor()" sel-classs="flexselect"
							></span></li>
						
						<li><label>姓名：</label> <input type="text"
							name="UEStudentName" value="${condition['UEStudentName']}"
							style="width: 55%" /></li>
						<li><label>学号：</label> <input type="text" name="UEStudyNo"
							value="${condition['UEStudyNo']}" style="width: 120px;" /></li>
						<li><label>证书编号：</label> <input type="text"
							name="certificateNo" value="${condition['certificateNo']}"
							style="width: 120px;" /></li>
					</ul>
					<ul class="searchContent">
						<li id="universalExam_classes_select" class="custom-li"><label>班级：</label> <span
							sel-id="query_universalExam_classes" sel-name="UEClassId"
							sel-classs="flexselect"></span></li>
						<li><label>考试日期：</label> <input type="text" name="examDate"
							value="${condition['examDate']}" class="Wdate"
							id="universalExam_examDate" style="width: 55%"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li><label>当前通过：</label> <gh:select id="universalExam_isPass"
								name="isPass" value="${condition['isPass']}"
								dictionaryCode="yesOrNo" style="width:120px;" /></li>
						<li><label>最终通过：</label> <gh:select id="universalExam_finalPass"
								name="finalPass" value="${condition['finalPass']}"
								dictionaryCode="yesOrNo" style="width:120px;" /></li>
					</ul>
				
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> <gh:courseAutocomplete
							name="UECourseId" id="universalExam_courseId" tabindex="1"
							displayType="code" isFilterTeacher="Y" style="width:240px;"
							value="${condition['UECourseId'] }" /></li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_UNIVERSALEXAM_LIST"
			pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="188" style="width: 100%">
				<thead>
					<tr>
						<%-- 
					<th width="4%"><input type="checkbox" name="checkallUniversalExam" id="check_all_universalExam_list" onclick="checkboxAll('#check_all_universalExam_list','resourceid','#univeralExamBody')"/></th>
		        --%>
						<th width="9%">学号</th>
						<th width="5%">姓名</th>
						<th width="8%">课程名称</th>
						<th width="9%">教学点</th>
						<th width="5%">年级</th>
						<th width="4%">层次</th>
						<th width="8%">专业</th>
						<th width="9%">班级</th>
						<th width="3%">成绩</th>
						<th width="5%">当前成绩</th>
						<th width="4%">成绩状态</th>
						<th width="9%">证书编号</th>
						<th width="7%">考试日期</th>
						<th width="50px">第几次</th>
						<th width="69px">考试类别</th>
						<th width="78px">操作人</th>
						<th width="4%">是否通过</th>
					</tr>
				</thead>
				<tbody id="univeralExamBody">
					<c:forEach items="${universalExamList.result}"
						var="universalExamVo" varStatus="vs">

						<%--<tr>
			        	
			        		<td><input type="checkbox" name="resourceid" value="${universalExamVo.resourceid }" autocomplete="off" /></td>
			            --%>
						<%-- 
         			    <td>${universalExamVo.studyNo}</td>
			            <td>${universalExamVo.studentName}</td>	
			            <td>${universalExamVo.courseName }</td>
			            <td>${universalExamVo.unitName }</td>
			            <td>${universalExamVo.gradeName}</td>
			            <td>${universalExamVo.classicName}</td>
			            <td>${universalExamVo.majorName}</td>
			            <td>${universalExamVo.classesName}</td>
			            <td>${universalExamVo.score}</td>				             
			            <td>${universalExamVo.currentScore}</td>				             
			            <td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',universalExamVo.checkStatus) }</td> 
			            <td>${universalExamVo.certificateNo}</td>   
			            <td><fmt:formatDate value="${universalExamVo.examDate}" pattern="yyyy-MM-dd" /></td>
			            <td>${universalExamVo.whichTime}</td>   
			            <td>${ghfn:dictCode2Val('CodeExamClassType', universalExamVo.examClassType) }</td> 	
			            <td>${universalExamVo.operatorName}</td>
			        </tr>
			           --%>
						<tr>
							<td>${universalExamVo.studentInfo.studyNo}</td>
							<td>${universalExamVo.studentInfo.studentName}</td>
							<td>${universalExamVo.course.courseName }</td>
							<td>${universalExamVo.studentInfo.branchSchool.unitName }</td>
							<td>${universalExamVo.studentInfo.grade.gradeName}</td>
							<td>${universalExamVo.studentInfo.classic.classicName}</td>
							<td>${universalExamVo.studentInfo.major.majorName}</td>
							<td>${universalExamVo.studentInfo.classes.classname}</td>
							<td>${universalExamVo.score}</td>
							<td>${universalExamVo.examResults.integratedScore}</td>
							<td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',universalExamVo.checkStatus) }</td>
							<td>${universalExamVo.certificateNo}</td>
							<td><fmt:formatDate value="${universalExamVo.examDate}"
									pattern="yyyy-MM-dd" /></td>
							<td>${universalExamVo.whichTime}</td>
							<td>${ghfn:dictCode2Val('CodeExamClassType', universalExamVo.teachingPlanCourse.examClassType) }</td>
							<td>${universalExamVo.operatorName}</td>
							<td>${ghfn:dictCode2Val('yesOrNo',universalExamVo.isPass) }</td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${universalExamList}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/universalExam/universalExam-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		universalExamQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function universalExamQueryBegin() {
		var defaultValue = "${condition['UESchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['UEGradeid']}";
		var classicId = "${condition['UEClassic']}";
		
		var majorId = "${condition['UEMajor']}";
		var classesId = "${condition['UEClassId']}";
		var selectIdsJson = "{unitId:'query_universalExam_brSchoolId',gradeId:'query_universalExam_gradeid',classicId:'query_universalExam_classic',teachingType:'id~teachingType',majorId:'query_universalExam_major',classesId:'query_universalExam_classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function universalExamQueryUnit() {
		var defaultValue = $("#query_universalExam_brSchoolId").val();
		var selectIdsJson = "{gradeId:'query_universalExam_gradeid',classicId:'query_universalExam_classic',teachingType:'id~teachingType',majorId:'query_universalExam_major',classesId:'query_universalExam_classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function universalExamQueryGrade() {
		var defaultValue = $("#query_universalExam_brSchoolId").val();
		var gradeId = $("#query_universalExam_gradeid").val();
		var selectIdsJson = "{classicId:'query_universalExam_classic',teachingType:'id~teachingType',majorId:'query_universalExam_major',classesId:'query_universalExam_classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function universalExamQueryClassic() {
		var defaultValue = $("#query_universalExam_brSchoolId").val();
		var gradeId = $("#query_universalExam_gradeid").val();
		var classicId = $("#query_universalExam_classic").val();
		var selectIdsJson = "{teachingType:'id~teachingType',majorId:'query_universalExam_major',classesId:'query_universalExam_classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择专业
	function universalExamQueryMajor() {
		var defaultValue = $("#query_universalExam_brSchoolId").val();
		var gradeId = $("#query_universalExam_gradeid").val();
		var classicId = $("#query_universalExam_classic").val();
		var teachingTypeId = $("#id~teachingType").val();
		var majorId = $("#query_universalExam_major").val();
		var selectIdsJson = "{classesId:'query_universalExam_classes'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	
	// 下载统考成绩录入模板
	function downloadUEModel() {
		window.location.href="${baseUrl }/edu3/teaching/universalExam/downloadUEModel.html";
	}
	
	// 导入统考成绩
	function importUEScore() {
		var url = "${baseUrl }/edu3/teaching/universalExam/importUESCore.html";
		$.pdialog.open(url,"RES_TEACHING_UNIVERSALEXAM_INPUTSCORE","导入统考成绩",{width:650, height:550});
	}
	function downloadUniversalExam(){		
		var UESchool = $("#query_universalExam_brSchoolId").val();
		var UEGradeid =  $("#query_universalExam_gradeid").val();  
		var UEClassic = $("#query_universalExam_classic").val();   
		var UEMajor =   $("#query_universalExam_major").val();   
		var UEClassId = $("#query_universalExam_classes").val();   
// 		var UEStudentName =$("#query_universalExam_classes").val();
// 		var UEStudyNo =  $("#query_universalExam_brSchoolId").val();  
// 		var certificateNo =$("#query_universalExam_brSchoolId").val();
		var examDate =     $("#universalExam_examDate").val();
		var UECourseId =   $("#universalExam_courseId").val();
		var isPass =    $("#universalExam_isPass").val(); 
		var url ="${baseUrl}/edu3/teaching/universalExam/downloadUniversalExam.html?UESchool="+UESchool+"&UEGradeid="+UEGradeid+"&UEClassic="+UEClassic+
				"&UEMajor="+UEMajor+"&UEClassId="+UEClassId+"&examDate="+examDate+"&UECourseId="+UECourseId+"&isPass="+isPass;
		//downloadFileByIframe(url, "downloadUniversalExamIframe");
		alertMsg.confirm("您确定要按查询条件导出学生的统考成绩单吗？",{
			okCall:function(){
				downloadFileByIframe(url, "downloadUniversalExamIframe");
			}
		});
	}
</script>
</body>
</html>