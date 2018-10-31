<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划 - 课程</title>
</head>
<body>
	<script type="text/javascript">
	
	function validateTeachingCourseForm(obj){
		return validateCallback(obj,_dailogAjaxDone1);
	}
	
	function _dailogAjaxDone1(json){
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			if(json.reloadUrl){
				$.pdialog.reload(json.reloadUrl);
			}
			
			if (json.navTabId){
				navTab.reload(json.reloadTabUrl, {}, json.navTabId);
			}		
		}
	}
	function _checkFaceStudyHour(obj){
		var studyHour = $.trim($("#teachingPlanCourse_studyHour").val());
		var faceStudyHour = $.trim($("#teachingPlanCourse_faceStudyHour").val());
		var experimentPeriod = $.trim($("#teachingPlanCourse_experimentPeriod").val());
		//if(studyHour.isInteger() && faceStudyHour.isInteger() && parseInt(studyHour)>=parseInt(faceStudyHour)){
		//	$("#teachingPlanCourse_selfStudyHour").val(parseInt(studyHour)-parseInt(faceStudyHour));
		//} else {
		//	$("#teachingPlanCourse_selfStudyHour").val('');
		//}
		if(studyHour.isInteger() && faceStudyHour.isInteger() && experimentPeriod.isInteger()){
			if(parseInt(studyHour)>=(parseInt(faceStudyHour)+parseInt(experimentPeriod))){
				$("#teachingPlanCourse_selfStudyHour").val(parseInt(studyHour)-parseInt(faceStudyHour)-parseInt(experimentPeriod));
			}else {
				alertMsg.error('(面授学时+实验学时)不能超过总学时');
			}
		} else {
			$("#teachingPlanCourse_selfStudyHour").val('0');
		}
	}
	</script>
	<h2 class="contentTitle">${teachingPlan.major }-
		${teachingPlan.classic } (${teachingPlan.versionNum})
		${ghfn:dictCode2Val('CodeTeachingType',teachingPlan.schoolType) }</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/framework/teaching/teachingplan/teachingcourse/save.html"
				class="pageForm" onsubmit="return validateTeachingCourseForm(this);">
				<input type="hidden" name="teachingPlanId"
					value="${teachingPlan.resourceid }" /> <input type="hidden"
					name="resourceid" value="${teachingPlanCourse.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">课程名称:</td>
							<td width="35%" colspan="3"><c:if
									test="${not empty teachingPlanCourse.course.resourceid }">
									<input type="text"
										value="${teachingPlanCourse.course.courseName }"
										readonly="readonly" style="width: 60%;">
									<input type="hidden"
										value="${teachingPlanCourse.course.resourceid }"
										name="courseId" id="_teachingCourseId">
								</c:if> <c:if test="${empty teachingPlanCourse.course.resourceid }">
									<gh:courseAutocomplete id="_teachingCourseId" name="courseId"
										tabindex="1" value="${teachingPlanCourse.course.resourceid }"
										displayType="code" classCss="required" style="width: 60%;" />
								</c:if></td>
						</tr>
						<tr>
							<td width="15%">课程类型:</td>
							<td width="35%"><gh:select name='courseType'
									dictionaryCode='CodeCourseType' style='width:80%'
									classCss="required" value='${teachingPlanCourse.courseType }' /></td>
							<td width="15%">课程类别:</td>
							<td width="35%"><gh:select name='courseNature'
									dictionaryCode='courseNature' style='width:80%'
									value='${teachingPlanCourse.courseNature }' classCss="required" /></td>
						</tr>
						<tr>
<!-- 							<td>前置课程:</td> -->
<%-- 							<td><gh:courseAutocomplete id="_teachingCourseBeforeId" --%>
<%-- 									name="beforeCourseId" tabindex="2" --%>
<%-- 									value="${teachingPlanCourse.beforeCourse.resourceid }" --%>
<%-- 									displayType="code" /></td> --%>
							<td>学分:</td>
							<td><input type='text' name='creditHour'
								value='${teachingPlanCourse.creditHour }' style='width: 80%'
								class="required number"></td>
						</tr>
						<tr>
							<td>总学时:</td>
							<td><input id="teachingPlanCourse_studyHour"
								onkeyup="_checkFaceStudyHour();" type='text' name='stydyHour'
								value='${teachingPlanCourse.stydyHour }' style='width: 80%'
								class="required digits"></td>
							<td>面授学时:</td>
							<td><input onkeyup="_checkFaceStudyHour();"
								id="teachingPlanCourse_faceStudyHour" type='text'
								name='faceStudyHour'
								value='${teachingPlanCourse.faceStudyHour }' style='width: 80%'
								class='digits'></td>
						</tr>
						<tr>
							<td>实验学时:</td>
							<td><input onkeyup="_checkFaceStudyHour();"
								id="teachingPlanCourse_experimentPeriod" type='text'
								name='experimentPeriod'
								value='${empty teachingPlanCourse.experimentPeriod?0:teachingPlanCourse.experimentPeriod }'
								style='width: 80%' class='digits'></td>
							<td>自学学时:</td>
							<td><input type='text' id="teachingPlanCourse_selfStudyHour"
								value='${teachingPlanCourse.selfStudyHour }' style='width: 80%'
								readonly="readonly"></td>
						</tr>
						<tr>
							<td>是否主干课:</td>
							<td><gh:select name="isMainCourse" dictionaryCode="yesOrNo"
									style="width:80%" value="${teachingPlanCourse.isMainCourse }" /></td>
							<td>建议学期:</td>
							<td><gh:select name='term' dictionaryCode='CodeTermType'
									style='width:80%' value="${teachingPlanCourse.term }"
									classCss="required" /><span style="color: red;">*</span></td>
						</tr>
						<tr>
<!-- 							<td>是否学位课:</td> -->
<%-- 							<td><gh:select name="isDegreeCourse" --%>
<%-- 									dictionaryCode="yesOrNo" style="width:80%" --%>
<%-- 									value="${teachingPlanCourse.isDegreeCourse }" /></td> --%>
							<td>考试类别:</td>
							<td><gh:select name='examClassType'
									dictionaryCode='CodeExamClassType' style='width:80%'
									value="${teachingPlanCourse.examClassType }" /></td>
									<c:if test="${showRankScore eq 'Y' }">
							<td>成绩类型:</td>
							<td><gh:select dictionaryCode="CodeCourseScoreStyle"
									id="course_scoreStyle" name="scoreStyle" 
									value="${teachingPlanCourse.scoreStyle }" /></td> 
									</c:if>
						</tr>
<!-- 						<tr> -->
<!-- 							<td>成绩类型:</td> -->
<%-- 							<td><gh:select dictionaryCode="CodeCourseScoreStyle" --%>
<%-- 									id="course_scoreStyle" name="scoreStyle" --%>
<%-- 									value="${teachingPlanCourse.scoreStyle }" /></td> --%>
<!-- 							<td>教学方式:</td> -->
<%-- 							<td><c:if test="${empty teachingPlanCourse.teachType }"> --%>
<%-- 									<gh:select name="teachType" value="facestudy" --%>
<%-- 										dictionaryCode="teachType" style="width:50%;" --%>
<%-- 										classCss="required" /> --%>
<%-- 								</c:if> <c:if test="${not empty teachingPlanCourse.teachType }"> --%>
<%-- 									<gh:select name="teachType" --%>
<%-- 										value="${ teachingPlanCourse.teachType }" --%>
<%-- 										dictionaryCode="teachType" style="width:50%;" /> --%>
<%-- 								</c:if></td> --%>
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>排序号:</td> -->
<!-- 							<td><input type='text' name='showOrder' -->
<%-- 								value='${teachingPlanCourse.showOrder }' style='width: 30%' --%>
<!-- 								class="digits"></td> -->
<!-- 							<td>备注:</td> -->
<!-- 							<td><input type='text' name='memo' -->
<%-- 								value='${teachingPlanCourse.memo }' style='width: 90%'></td> --%>
<!-- 						</tr> -->
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>

<script type="text/javascript">

</script>

</html>