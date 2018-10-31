<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>缓考申请</title>
<script type="text/javascript">
		function validateCallback(form, callback) {
			var $form = $(form);
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			var score	= $("input[name='scoreForCount']").val();
			var checked_4 = true;
			 if(isNaN(score)){
				 checked_4 = false;
			 }else if(0>score||100<score){
				 checked_4 = false;
			 }

			if(checked_4==false){
				alertMsg.warn("保存申请失败。存在非法成绩(0到100之间数字)。");
				return false;
			}
			$.ajax({
				type:'POST',
				url:$form.attr("action"),
				data:$form.serializeArray(),
				dataType:"json",
				cache: false,
				success: callback || validateFormCallback,
				
				error: DWZ.ajaxError
			});
			return false;
		}
		
		function getPlanCourseId(obj){
			var planCourseId = $(obj).find("option[value='"+$(obj).val()+"']").attr("pcid");
			$("#abnormalExamApply_planCourse").val(planCourseId);
		}
		
		function closeThisTab() {
			navTab.closeCurrentTab();
			navTab.reload('${baseUrl}/edu3/teaching/abnormalExam/list.html');
		}
		
		function confirmGrade(obj){
			var selectGradeId = $(obj).val();
			navTab.reload('${baseUrl}/edu3/teaching/abnormalExam/saveOrEdit.html?selectGrageId='+selectGradeId+"&more=Y");
		}
		
	</script>
</head>
<body>
	<h2 class="contentTitle">编辑缓考申请</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/abnormalExam/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${abnormalExam.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<c:if test="${more eq 'Y' }">
							<tr>
								<td width="20%">请选择年级:</td>
								<td colspan="3"><select id="select_grade"
									onchange="confirmGrade(this)" class="required">
										<option value="">请选择</option>
										<c:forEach items="${studentInfoList }" var="stuinfo">
											<option value="${stuinfo.grade.resourceid }"
												<c:if test="${stuinfo.grade.resourceid eq abnormalExam.studentInfo.grade.resourceid }"> selected </c:if>>${stuinfo.grade.gradeName }</option>
										</c:forEach>
								</select></td>
							</tr>
						</c:if>
						<tr>
							<td width="20%">学生学号:</td>
							<td width="30%"><input type="text" style="width: 50%"
								value="${abnormalExam.studentInfo.studyNo }" readonly="readonly"
								class="required" /> <input type="hidden" name="studentId"
								value="${abnormalExam.studentInfo.resourceid }" /></td>
							<td width="20%">学生姓名:</td>
							<td width="30%"><input type="text" name="studentName"
								style="width: 50%"
								value="${abnormalExam.studentInfo.studentName }"
								readonly="readonly" class="required" /></td>
						</tr>
						<tr>
							<td>教学站:</td>
							<td><input type="text" id="stuCenter4" style="width: 50%"
								value="${abnormalExam.studentInfo.branchSchool }"
								readonly="readonly" /></td>
							<td>年级:</td>
							<td><input type="text" id="grade4" style="width: 50%"
								value="${abnormalExam.studentInfo.grade }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>专业:</td>
							<td><input type="text" id="major4" style="width: 50%"
								value="${abnormalExam.studentInfo.major }" readonly="readonly" /></td>
							<td>层次:</td>
							<td><input type="text" id="classic4" style="width: 50%"
								value="${abnormalExam.studentInfo.classic }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>成绩类型:</td>
							<td><input type="text" value="百分制" readonly="readonly" /> <input
								type="hidden" name="courseScoreType" value="11" /></td>
							<td>成绩:</td>
							<td><input type="text" name="scoreForCount" value="0"
								class="number" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>课程名称:</td>
							<td><select name="courseId" class="required"
								onchange="getPlanCourseId(this)"
								<c:if test="${ not empty abnormalExam.course.resourceid }"> disabled="disabled" </c:if>>
									<option value="" pcid="">请选择...</option>
									<c:forEach
										items="${abnormalExam.studentInfo.teachingPlan.teachingPlanCourses }"
										var="pc" varStatus="vs">
										<option value="${pc.course.resourceid }"
											pcid="${pc.resourceid }"
											<c:if test="${ pc.course.resourceid eq abnormalExam.course.resourceid }">selected</c:if>>
											${pc.course.courseCode }-${pc.course.courseName }</option>
									</c:forEach>
							</select> <font color="red">*</font> <input type="hidden"
								id="abnormalExamApply_planCourse" name="planCourseId"
								value="${abnormalExam.teachingPlanCourse.resourceid }" /></td>
							<td>申请类型:</td>
							<td><c:choose>
									<c:when test="${ not empty abnormalExam.abnormalType }">
										<input type="text" id="abnormalExamApply_examType"
											name="abnormalType"
											value="${ghfn:dictCode2Val('CodeAbnormalType',abnormalExam.abnormalType)}"
											readonly="readonly" />
									</c:when>
									<c:otherwise>
										<gh:select name="abnormalType"
											dictionaryCode="CodeAbnormalType"
											value="${abnormalExam.abnormalType}" classCss="required"
											style="width:52%" />
										<input type="hidden" id="abnormalExamApply_examType"
											name="examType" value="N" />
										<font color="red">*</font>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td>申请理由:</td>
							<td ><textarea name="reason"
									style="width: 50%; height: 80px" class="required">${abnormalExam.reason }</textarea></td>
							<td>上传附件(点击附件名称即可下载):</td>
							<td ><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="users,${abnormalExam.studentInfo.studyNo }"
									extendStorePath="attachs" formType="ABNORMALEXAMAPPLY" attachList="${attachList }" /></td>
						</tr>
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
									<button type="button" class="close" onclick="closeThisTab()">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>