<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷成卷规则管理</title>
<script type="text/javascript">
	//新增
	function addCourseExamRules(){
		var url = "${baseUrl}/edu3/learning/courseexamrules/input.html";
		navTab.openTab('RES_LEARNING_COURSEEXAMRULES_INPUT', url, '新增试卷成卷规则');
	}
	//修改
	function modifyCourseExamRules(){
		var url = "${baseUrl}/edu3/learning/courseexamrules/input.html";
		if(isCheckOnlyone('resourceid','#courseExamRulesBody')){
			navTab.openTab('RES_LEARNING_COURSEEXAMRULES_INPUT', url+'?resourceid='+$("#courseExamRulesBody input[@name='resourceid']:checked").val(), '修改试卷成卷规则');
		}			
	}
		
	//删除
	function removeCourseExamRules(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/learning/courseexamrules/remove.html","#courseExamRulesBody");
	}
    
	function courseExamRuleTypeOnchange(isEnrolExam){
		if(isEnrolExam=='entrance_exam'){			
			$("#courseExamRulesSearch_Course1").hide();
			$("#courseExamRulesSearch_Course2").show();
		} else {
			$("#courseExamRulesSearch_Course1").show();
			$("#courseExamRulesSearch_Course2").hide();
		}
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/courseexamrules/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <li>
					<label>课程：</label> 
					<input type="hidden" name="isEnrolExam" value="Y"/>
					<gh:select id="courseExamRules_courseName" name="courseName" value="${condition['courseName'] }" dictionaryCode="CodeEntranceExam"/>
				</li> --%>
						<c:choose>
							<c:when test="${not empty condition['teacherId'] }">
								<li><label>课程:</label> <input type="hidden"
									name="isEnrolExam" value="${condition['isEnrolExam']}" /> <gh:courseAutocomplete
										id="courseExamRules_CourseId" name="courseId" tabindex="1"
										value="${condition['courseId']}" isFilterTeacher="Y" /></li>
							</c:when>
							<c:otherwise>
								<li><label>考试性质：</label> <%--
							<gh:select name="isEnrolExam" value="${condition['isEnrolExam']}" choose="N" dictionaryCode="yesOrNo" onchange="courseExamRuleTypeOnchange(this.value)"/> --%>
									<select name="paperSourse"
									onchange="courseExamRuleTypeOnchange(this.value)">
										<%-- <c:if test="${empty condition['teacherId'] }">
								<option value="entrance_exam" <c:if test="${condition['paperSourse'] eq 'entrance_exam' }">selected="selected"</c:if> >入学机考</option>
								</c:if> --%>
										<option value="final_exam"
											<c:if test="${condition['paperSourse'] eq 'final_exam' }">selected="selected"</c:if>>期末机考</option>
										<option value="online_exam"
											<c:if test="${condition['paperSourse'] eq 'online_exam' }">selected="selected"</c:if>>网上机考</option>
								</select></li>
								<li>
									<div id="courseExamRulesSearch_Course1"
										style="display: ${(condition['isEnrolExam'] eq 'Y')?'none':'block' };">
										<label>课程:</label>
										<gh:courseAutocomplete id="courseExamRules_CourseId"
											name="courseId" tabindex="1" value="${condition['courseId']}"
											isFilterTeacher="Y" />
									</div>
									<div id="courseExamRulesSearch_Course2"
										style="display: ${(condition['isEnrolExam'] eq 'Y')?'block':'none' };">
										<label>入学考试课程:</label>
										<gh:select id="courseExamRules_courseName" name="courseName"
											value="${condition['courseName']}"
											dictionaryCode="CodeEntranceExam" />
									</div>
								</li>
							</c:otherwise>
						</c:choose>
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
			<div class="panelBar">
				<ul class="toolBar">
					<gh:resAuth parentCode="RES_LEARNING_COURSEEXAMRULES"
						pageType="list"></gh:resAuth>
				</ul>
			</div>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="6%"><input type="checkbox" name="checkall"
							id="check_all_courseExamRules"
							onclick="checkboxAll('#check_all_courseExamRules','resourceid','#courseExamRulesBody')" /></th>
						<th width="15%">规则名称</th>
						<th width="15%">考试时长(分钟)</th>
						<th width="16%">试题类型</th>
						<th width="16%">试题题型</th>
						<th width="16%">试题数</th>
						<th width="16%">试题分数</th>
					</tr>
				</thead>
				<tbody id="courseExamRulesBody">
					<c:forEach items="${courseExamRulesPage.result}" var="examRule"
						varStatus="vs">
						<tr>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }"><input
								type="checkbox" name="resourceid"
								value="${examRule.resourceid }" autocomplete="off" /></td>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }">${examRule.courseExamRulesName }</td>
							<td rowspan="${fn:length(examRule.courseExamRulesDetails)+1 }">${examRule.examTimeLong }</td>
						</tr>
						<c:forEach items="${examRule.courseExamRulesDetails }"
							var="detail">
							<tr>
								<td>${ghfn:dictCode2Val('CodeExamNodeType',detail.examNodeType) }</td>
								<td>${ghfn:dictCode2Val('CodeExamType',detail.examType) }</td>
								<td>${detail.examNum }</td>
								<td>${detail.examValue }</td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamRulesPage}"
				goPageUrl="${baseUrl }/edu3/learning/courseexamrules/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
