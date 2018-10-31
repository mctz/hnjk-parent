<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重置成绩</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="resetExamResultsSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/reset-examresults-list.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="reset_examresults_brSchoolId"
								defaultValue="${condition['branchSchool']}" /> <!-- 
					<input type="hidden" id="reset_examresults_brSchoolId" name="branchSchool" size="36" value="${condition['branchSchool']}"/>	
					<input type="text" id="reset_examresults_brSchoolName" name="branchSchoolName" value="${condition['branchSchoolName']}" style="width:120px"/>
					--></li>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:120px" /></li>
						<li><label>层 次：</label> <gh:selectModel name="classic"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:120px" /></li>
						<li><label>专 业：</label> <gh:selectModel name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" style="width:120px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 120px" /></li>
						<li><label>姓名：</label><input type="text" name="stuName"
							value="${condition['name']}" style="width: 120px" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								dictionaryCode="CodeStudentStatus" choose="Y"
								value="${condition['studentStatus']}" style="width:120px" /></li>
						<li><label>学习方式：</label> <gh:select name="learningStyle"
								dictionaryCode="CodeLearningStyle" choose="Y"
								value="${condition['learningStyle']}" style="width:120px" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>

			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE"
			pageType="resetSub"></gh:resAuth>
		<div class="pageContent">
			<form id="resetExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/reset-examresults-save.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<table class="table" layouth="190">
					<thead>
						<tr>
							<th width="4%"><input type="checkbox" name="checkall"
								id="check_all_examResults_reset_save"
								onclick="checkboxAll('#check_all_examResults_reset_save','resourceid','#examResultsResetBody')" /></th>
							<th width="10%">课程名称</th>
							<th width="7%">年级</th>
							<th width="10%">专业</th>
							<th width="10%">层次</th>
							<th width="8%">姓名</th>
							<th width="8%">选考次数</th>
							<th width="8%">卷面成绩</th>
							<th width="8%">平时成绩</th>
							<th width="8%">综合成绩</th>
							<th width="8%">成绩异常</th>
							<th width="10%">成绩状态</th>
						</tr>
					</thead>
					<tbody id="examResultsResetBody">
						<c:forEach items="${objPage.result}" var="examResults"
							varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${examResults.resourceid }" autocomplete="off" /></td>
								<td title="${examResults.course.courseName }">${examResults.course.courseName }</td>
								<td title="${examResults.studentInfo.grade.gradeName }">${examResults.studentInfo.grade.gradeName }</td>
								<td title="${examResults.studentInfo.major.majorName}">${examResults.studentInfo.major.majorName}</td>
								<td title="${examResults.studentInfo.classic.classicName}">${examResults.studentInfo.classic.classicName}</td>
								<td title="${examResults.studentInfo.studentName}">${examResults.studentInfo.studentName}</td>
								<td>${examResults.examCount}</td>
								<td><input type="text"
									name="writtenScore${examResults.resourceid }" align="middle"
									class="number" style="width: 30px"
									value="${examResults.writtenScore}" /></td>
								<td><input type="text"
									name="usuallyScore${examResults.resourceid }" align="middle"
									class="number" style="width: 30px"
									value="${examResults.usuallyScore}" /></td>
								<td><input type="text"
									name="integratedScore${examResults.resourceid }" align="middle"
									class="number" style="width: 30px"
									value="${examResults.integratedScore}" /></td>
								<td><gh:select
										name="examAbnormity${examResults.resourceid }"
										dictionaryCode="CodeExamAbnormity" choose="N"
										value="${examResults.examAbnormity}" /></td>
								<td
									title="${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}">
									${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}
								</td>
							</tr>
						</c:forEach>
					</tbody>
					</form>
				</table>
				<gh:page page="${objPage}"
					goPageUrl="${baseUrl }/edu3/teaching/result/reset-examresults-list.html?examSubId=${condition['examSub'] }"
					pageType="sys" targetType="dialog" pageNumShown="4"
					condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
    //重置成绩
	function resetExamResults(){
		var examresoultsResetResourceid = new Array();
		jQuery("#examResultsResetBody input[name='resourceid']:checked").each(function(){
			examresoultsResetResourceid.push(jQuery(this).val());
		});
		if(examresoultsResetResourceid.length>0){
			
			var $form = $("#resetExamResultsSaveForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要重置所选择的成绩吗", {
                okCall: function(){
                	$.ajax({
						type:'POST',
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								$.pdialog.reload($("#resetExamResultsSearchForm").attr("action"), $("#resetExamResultsSearchForm").serializeArray());
							}
						}
					});
                }
            });   
		}else{
			alertMsg.warn("请勾选要重置的成绩!")
		}	
	}
</script>
</body>
</html>