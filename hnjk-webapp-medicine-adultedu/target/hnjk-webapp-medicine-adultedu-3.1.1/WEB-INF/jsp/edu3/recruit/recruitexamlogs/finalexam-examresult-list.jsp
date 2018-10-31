<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期末机考考后数据统计</title>
<script type="text/javascript">
//保存备注
function savefinalExamResult(){
	var $form = $("#finalexamresult_recruitExamLogsBodyForm");
	if(!isChecked('resourceid',"#finalexamresult_recruitExamLogsBody")){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	$.ajax({
		type:'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: function (json){
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				navTabPageBreak();
			}
		},		
		error: DWZ.ajaxError
	});    	 
    return false;
}
//导出
function finalExamResultExport(){
	var examSubId = $("#finalexamresultRecruitExamLogs_examSubId").val();
	var courseId = $("#finalexamresultCourseExamLogs_courseId").val();
	if(examSubId==""||courseId==""){
		alertMsg.warn("请选择考试批次与课程");
		return false;
	} else {
		var url = "${baseUrl}/edu3/teaching/finalexam/examresult/export.html?"+$("#finalexamresult_recruitExamLogsForm").serialize();
		downloadFileByIframe(url,"finalexamresult_recruitExamLogsIframe");
	}	
}
//打印
function finalExamResultPrint(){
	var examSubId = $("#finalexamresultRecruitExamLogs_examSubId").val();
	var courseId = $("#finalexamresultCourseExamLogs_courseId").val();
	if(examSubId==""||courseId==""){
		alertMsg.warn("请选择考试批次与课程");
		return false;
	} else {
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printfinalonlineexam-view.html?examSubId="+examSubId+"&courseId="+courseId;
		$.pdialog.open(url,'finalonlineexam_printview','打印预览',{height:600, width:800});
	}	
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="finalexamresult_recruitExamLogsForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/finalexam/examresult/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次</label> <gh:selectModel
								id="finalexamresultRecruitExamLogs_examSubId" name='examSubId'
								bindValue='resourceid' displayValue='batchName'
								value="${condition['examSubId']}"
								modelClass='com.hnjk.edu.teaching.model.ExamSub'
								condition="batchType='exam'"
								orderBy='yearInfo.firstYear desc,term desc' style="width:50%" />
							<span style="color: red;">*</span></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="finalexamresultCourseExamLogs_courseId"
								value="${condition['courseId'] }" displayType="code" /> <span
							style="color: red;">*</span></li>
					</ul>
					<ul class="searchContent">
						<c:if test="${not isBrSchool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" id="finalexamRecruitExamLogs_brSchool"
									tabindex="1" defaultValue="${condition['branchSchool'] }"></gh:brSchoolAutocomplete>
							</li>
						</c:if>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">考试批次与课程为必选项</span></li>
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
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_RECRUITEXAMLOGS2"
				pageType="list"></gh:resAuth>
			<form id="finalexamresult_recruitExamLogsBodyForm"
				action="${baseUrl }/edu3/teaching/finalexam/examresult/save.html"
				method="post">
				<table class="table" layouth="161">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_finalexamresult_recruitExamLogs"
								onclick="checkboxAll('#check_all_finalexamresult_recruitExamLogs','resourceid','#finalexamresult_recruitExamLogsBody')" /></th>
							<th width="15%">考试批次</th>
							<th width="10%">课程</th>
							<th width="10%">教学站</th>
							<th width="10%">学号</th>
							<th width="10%">姓名</th>
							<th width="10%">结果状态</th>
							<th width="10%">考试成绩</th>
							<th width="20%">备注</th>
						</tr>
					</thead>
					<tbody id="finalexamresult_recruitExamLogsBody">
						<c:forEach items="${recruitExamLogsPage.result}"
							var="recruitExamLog" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${recruitExamLog.resourceid }" autocomplete="off" /></td>
								<td>${recruitExamLog.examInfo.examSub.batchName }</td>
								<td>${recruitExamLog.examInfo.course.courseName }</td>
								<td>${recruitExamLog.studentInfo.branchSchool }</td>
								<td>${recruitExamLog.studentInfo.studyNo }</td>
								<td>${recruitExamLog.studentInfo.studentName }</td>
								<td>${recruitExamLog.finalStatus }</td>
								<td><c:choose>
										<c:when test="${not empty recruitExamLog.examScore }">${recruitExamLog.examScore }</c:when>
										<c:otherwise>缺考</c:otherwise>
									</c:choose></td>
								<td><input type="text"
									name="memo${recruitExamLog.resourceid }"
									value="${recruitExamLog.memo }" /></td>
							</tr>
						</c:forEach>
						<c:if test="${not empty statResult }">
							<tr>
								<td colspan="9" style="text-align: right;">预约总人数
									${statResult.totalCount } 人， 缺考 ${statResult.noscoreCount }
									人，实考 ${statResult.assistCount } 人， 成绩合格 ${statResult.passCount }
									人，成绩不合格 ${statResult.nopassCount } 人， 合格率为 <fmt:formatNumber
										value="${statResult.passPer }" type="percent" pattern="###.#%" />
									。
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</form>
			<gh:page page="${recruitExamLogsPage}"
				goPageUrl="${baseUrl }/edu3/teaching/finalexam/examresult/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>