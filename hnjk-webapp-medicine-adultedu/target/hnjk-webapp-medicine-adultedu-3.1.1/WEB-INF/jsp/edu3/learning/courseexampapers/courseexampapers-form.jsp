<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试题管理</title>
<script type="text/javascript"> 
		function courseExamPaperTypeOnchange(paperType){
			if(paperType=="entrance_exam"){				
				$("#courseExamPapers_Course1").hide();
				$("#courseExamPapers_Course2").show();
			} else {
				$("#courseExamPapers_Course1").show();
				$("#courseExamPapers_Course2").hide();
			}
		}
		
		function courseExamPapersValidateCallback(form){
			var $form = $(form);
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			var course_val;
			if($form.find("[name='paperType']").val()=="entrance_exam"){
				course_val = $form.find("[name='courseName']").val();
			} else {
				course_val = $form.find("[name='courseId']").val();	
			}
			if(course_val==""){
				alertMsg.error("请选择一门课程!");
				return false; 
			}
			return validateCallback(form);
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">${(empty courseExamPapers.resourceid)?'新增':'编辑' }试卷</h2>
	<div class="page">
		<div class="pageContent">
			<form id="courseExamPapersEditForm" method="post"
				action="${baseUrl}/edu3/metares/courseexampapers/save.html"
				class="pageForm"
				onsubmit="return courseExamPapersValidateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${courseExamPapers.resourceid }" /> <input type="hidden"
					name="fillinMan" value="${courseExamPapers.fillinMan }" /> <input
					type="hidden" name="fillinManId"
					value="${courseExamPapers.fillinManId }" /> <input type="hidden"
					name="fillinDate"
					value="<fmt:formatDate value='${courseExamPapers.fillinDate }' pattern='yyyy-MM-dd HH:mm:ss'/>" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 10%">试卷名称:</td>
							<td style="width: 40%"><input type="text" name="paperName"
								value="${courseExamPapers.paperName }" style="width: 80%;"
								class="required" /></td>
							<td style="width: 10%">层次:</td>
							<td style="width: 40%"><gh:selectModel name="classicid"
									bindValue="resourceid" displayValue="classicName"
									modelClass="com.hnjk.edu.basedata.model.Classic"
									value="${courseExamPapers.classic.resourceid}" /></td>
						</tr>
						<tr>
							<td>试卷类型:</td>
							<td><gh:select name="paperType"
									value="${courseExamPapers.paperType}"
									dictionaryCode="CodePaperType"
									onchange="courseExamPaperTypeOnchange(this.value)"
									classCss="required" /><span style="color: red;">*</span></td>
							<td>课程:</td>
							<td>
								<div id="courseExamPapers_Course1"
									style="display: ${(courseExamPapers.paperType eq 'entrance_exam')?'none':'block' };">
									<gh:courseAutocomplete id="courseExamPapers_CourseId"
										name="courseId" tabindex="1"
										value="${courseExamPapers.course.resourceid}"
										displayType="code" style="width:80%;" />
									<span style="color: red;">*</span>
								</div>
								<div id="courseExamPapers_Course2"
									style="display: ${(courseExamPapers.paperType eq 'entrance_exam')?'block':'none' };">
									<gh:select name="courseName"
										value="${courseExamPapers.courseName}"
										dictionaryCode="CodeEntranceExam" />
									<span style="color: red;">*</span>
								</div>
							</td>
						</tr>
						<tr>
							<td>考试时长(单位:分钟):</td>
							<td><input type="text" name="paperTime"
								value="${courseExamPapers.paperTime}" style="width: 50%;"
								class="required digits" /></td>
							<td>是否开放:</td>
							<td><gh:select name="isOpened"
									value="${courseExamPapers.isOpened }" dictionaryCode="yesOrNo" /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" rows="5" cols=""
									style="width: 80%;">${courseExamPapers.memo }</textarea></td>
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
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>