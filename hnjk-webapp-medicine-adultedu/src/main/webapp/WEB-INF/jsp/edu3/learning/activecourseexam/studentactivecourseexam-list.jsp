<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生随堂练习答题情况</title>
<script type="text/javascript">
function reDoStudentCourseExam(){
	pageBarHandle("您确定要把这些答题情况还原到未答状态吗？","${baseUrl}/edu3/teaching/studentactivecourseexam/redo.html","#studentanswercourseExamBody");
}
$(document).ready(function(){
	$("select[class*=flexselect]").flexselect();
});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/studentactivecourseexam/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- 	
				<li>
					<label>年度：</label>
					<gh:selectModel name="yearInfoId" id="studentAnswer_yearInfoId" bindValue="resourceid" displayValue="yearName" 
							modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfoId']}" orderBy="firstYear desc"/>
				</li>
				<li>
					<label>学期：</label><gh:select name="term" id="studentAnswer_term" value="${condition['term']}" dictionaryCode="CodeTerm" />
				</li>	
				 --%>
				 		<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="studentactivecourseexam_list_classid"
								tabindex="1" displayType="code" style="width: 240px;"
								defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"></gh:classesAutocomplete></li>
						<li><label>学号：</label> <input id='ddd' type="text"
							name="studyNo" value="${condition['studyNo'] }" /></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程:</label> ${studentactivecourseexamlistcourse }
							<font color="red"> *</font></li>
						<li><label>知识节点：</label> <select name="syllabusId"
							style="width: 125px;">
								<option value="">请选择</option>
								<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
									<option value="${syb.resourceid }"
										<c:if test="${syb.resourceid eq condition['syllabusId']}"> selected </c:if>>
										<c:forEach var="seconds" begin="1" end="${syb.syllabusLevel}"
											step="1">&nbsp;&nbsp;</c:forEach> ${syb.syllabusName }
									</option>
								</c:forEach>
						</select></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips"> 请选择学生学号或姓名以及课程进行查询 </span></li>
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
			<gh:resAuth
				parentCode="RES_TEACHING_ESTAB_ACTIVECOURSEEXAM_STUDENTANSWER"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentanswercourseexam"
							onclick="checkboxAll('#check_all_studentanswercourseexam','resourceid','#studentanswercourseExamBody')" /></th>
						<th width="8%">学号</th>
						<th width="5%">学生姓名</th>
						<th width="15%">班级</th>
						<th width="8%">课程</th>
						<th width="10%">知识节点</th>
						<th width="5%">题型</th>
						<th width="5%">题目序号</th>
						<th width="10%">参考答案</th>
						<th width="10%">学生答案</th>
						<th width="5%">是否答对</th>
						<th width="5%">是否已提交</th>
					</tr>
				</thead>
				<tbody id="studentanswercourseExamBody">
					<c:forEach items="${stuExamList.result }" var="exam" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.resourceid }" autocomplete="off" /></td>
							<td>${exam.studentInfo.studyNo}</td>
							<td>${exam.studentInfo.studentName}</td>
							<td>${exam.studentInfo.classes.classname}</td>
							<td>${exam.activeCourseExam.syllabus.course.courseName}</td>
							<td>${exam.activeCourseExam.syllabus.syllabusName}</td>
							<td>${ghfn:dictCode2Val('CodeExamType',exam.activeCourseExam.courseExam.examType) }</td>
							<td>${exam.activeCourseExam.showOrder }</td>
							<td>${exam.activeCourseExam.courseExam.answer }</td>
							<td>${exam.answer }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',exam.isCorrect) }</td>
							<td>${(empty exam.result)?'未提交':'已提交'}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${stuExamList}"
				goPageUrl="${baseUrl}/edu3/teaching/studentactivecourseexam/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>