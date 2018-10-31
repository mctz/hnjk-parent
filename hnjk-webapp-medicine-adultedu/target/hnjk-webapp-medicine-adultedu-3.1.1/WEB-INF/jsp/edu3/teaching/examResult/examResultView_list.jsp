<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询学生成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="queryExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/examresults-view.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isBranchSchool'] != 'Y' }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="query_examresults_view_brSchoolId"
									defaultValue="${condition['branchSchool']}" /> <!-- 
						<input type="hidden" id="query_examresults_view_brSchoolId" name="branchSchool" size="36" value="${condition['branchSchool']}"/>	
						<input type="text" id="query_examresults_view_brSchoolName" name="branchSchoolName" value="${condition['branchSchoolName']}" style="width:120px"/>
						--></li>
						</c:if>
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
						<li><label>成绩状态：</label> <gh:select name="checkStatus"
								dictionaryCode="CodeExamResultCheckStatus" choose="Y"
								value="${condition['checkStatus']}" style="width:120px" /></li>
						<li><label>考试批次：</label> <c:choose>
								<c:when test="${condition['isBranchSchool'] != 'Y' }">
									<gh:selectModel id="examSub" name="examSub"
										bindValue="resourceid" displayValue="batchName"
										style="width:120px"
										modelClass="com.hnjk.edu.teaching.model.ExamSub"
										value="${condition['examSub']}" />
								</c:when>
								<c:otherwise>
									<gh:selectModel id="examSub" name="examSub"
										bindValue="resourceid" displayValue="batchName"
										style="width:120px"
										modelClass="com.hnjk.edu.teaching.model.ExamSub"
										value="${condition['examSub']}"
										condition="brSchool.resourceid=${condition['branchSchool']}" />
								</c:otherwise>
							</c:choose></li>
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
		<div class="pageContent">
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="8%">学号</th>
						<th width="6%">姓名</th>
						<th width="6%">年级</th>
						<th width="20%">专业</th>
						<th width="8%">层次</th>
						<th width="14%">教学中心</th>
						<th width="14%">课程名称</th>
						<th width="6%">卷面成绩</th>
						<th width="6%">平时成绩</th>
						<th width="6%">综合成绩</th>
						<th width="6%">成绩状态</th>
					</tr>
				</thead>
				<tbody id="examResultsViewBody">
					<c:forEach items="${objPage.result}" var="examResults"
						varStatus="vs">
						<tr>

							<td><a href="javascript:void(0)"
								onclick="viewStuExamResults('${examResults.studentInfo.resourceid }','${examResults.studentInfo.studentName }');">${examResults.studentInfo.studyNo }</a></td>
							<td><a href="javascript:void(0)" onclick="">${examResults.studentInfo.studentName }</a></td>
							<td>${examResults.studentInfo.grade.gradeName }</td>
							<td>${examResults.studentInfo.major.majorName}</td>
							<td>${examResults.studentInfo.classic.classicName}</td>
							<td>${examResults.studentInfo.branchSchool.unitName}</td>
							<td>${examResults.course.courseName }</td>
							<td>${examResults.writtenScore}</td>
							<td>${examResults.usuallyScore }</td>
							<td>${examResults.integratedScore }</td>
							<td>${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/examresults-view.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">	
    //查看学生成绩
	function viewStuExamResults(studentId,studentName){
	
		var url = "${baseUrl}/edu3/teaching/result/view-student-examresults.html?studentId="+studentId;
		$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_SUBMIT_RESULTS_LIST",studentName+"成绩列表",{width:900, height:600});	
	}
</script>
</body>
</html>