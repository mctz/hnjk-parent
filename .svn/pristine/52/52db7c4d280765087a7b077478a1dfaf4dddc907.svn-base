<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网上考试考场状态列表</title>
<script type="text/javascript">
	function reOnlineExam(){
		pageBarHandle("您确定要让这些考生重考吗？","${baseUrl}/edu3/teaching/recruitexamlogs/reexam.html","#online_recruitExamLogsBody");
	}
	//强制交卷
	function handOnlineExam(){	
		pageBarHandle("您确定要让这些考生强制交卷吗？","${baseUrl}/edu3/recruit/recruitexamlogs/hand.html?from=online","#online_recruitExamLogsBody");
	}
	//清除交卷状态，继续考试
	function continueOnlineExam(){
		pageBarHandle("您确定要让这些考生继续考试吗？","${baseUrl}/edu3/recruit/recruitexamlogs/continueexam.html?from=online","#online_recruitExamLogsBody");
	}
	function _onlineExamLogNavTabSearch(form){
		var statType = $(form).find("select[name='statType']").val();
		if(statType=="2"){
			var startScore = $.trim($(form).find("input[name='examStartScore']").val());
			var endScore = $.trim($(form).find("input[name='examEndScore']").val());
			if((startScore!=""&&!startScore.isInteger()) || (endScore!=""&&!endScore.isInteger())){
				alertMsg.warn("请输入整数!");
				return false;
			}
			if(startScore!=""&&endScore!=""&&(parseInt(startScore)<60||parseInt(startScore)>parseInt(endScore))){
				alertMsg.warn("起始分数应该大于60且小于结束分数!");
				return false;
			}
		} else if(statType=="3" || statType=="4"){
			var examSubId = $(form).find("select[name='examSubId']").val();
			var courseId = $(form).find("[name='courseId']").val();
			if(examSubId==""||courseId==""){
				alertMsg.warn("请选择考试批次与课程");
				return false;
			}
		}
		
		return navTabSearch(form);
	}
	
	function statTypeOnChange(statType){
		$("#RecruitExamLogs_statType input").val("");
		if(statType!="2"){
			$("#RecruitExamLogs_statType").hide();
		} else {
			$("#RecruitExamLogs_statType").show();
			$("#RecruitExamLogs_statType input[name='examStartScore']").val("60");
			$("#RecruitExamLogs_statType input[name='examEndScore']").val("100");
		}		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return _onlineExamLogNavTabSearch(this);"
				action="${baseUrl }/edu3/teaching/recruitexamlogs/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次</label> <gh:selectModel
								id="RecruitExamLogs_examSubId" name='examSubId'
								bindValue='resourceid' displayValue='batchName'
								value="${condition['examSubId']}"
								modelClass='com.hnjk.edu.teaching.model.ExamSub'
								condition="batchType='exam'"
								orderBy='yearInfo.firstYear desc,term desc' style="width:50%" />
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="onlineCourseExamLogs_courseId"
								isFilterTeacher="Y" value="${condition['courseId'] }"
								displayType="code" /></li>
						<li><label>状态：</label> <select name="status">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['status'] eq 0 }">selected="selected"</c:if>>在考</option>
								<option value="2"
									<c:if test="${condition['status'] eq 2 }">selected="selected"</c:if>>已交卷</option>
								<option value="1"
									<c:if test="${condition['status'] eq 1 }">selected="selected"</c:if>>退出</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<c:if test="${not isBrSchool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" id="RecruitExamLogs_brSchool" tabindex="1"
									defaultValue="${condition['branchSchool'] }"></gh:brSchoolAutocomplete>
							</li>
						</c:if>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>统计类型：</label> <select name="statType" size="1"
							onchange="statTypeOnChange(this.value)">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['statType'] eq '1' }">selected="selected"</c:if>>已考试未通过</option>
								<option value="2"
									<c:if test="${condition['statType'] eq '2'}">selected="selected"</c:if>>已考试且通过</option>
								<option value="3"
									<c:if test="${condition['statType'] eq '3'}">selected="selected"</c:if>>未参加考试</option>
								<option value="4"
									<c:if test="${condition['statType'] eq '4'}">selected="selected"</c:if>>重考统计</option>
						</select></li>
						<li id="RecruitExamLogs_statType"
							<c:if test="${condition['statType'] ne '2'}">style="display:none"</c:if>>
							<label>分数段：</label> <input type="text" name="examStartScore"
							value='<fmt:formatNumber value="${condition['examStartScore'] }" pattern="###"/>'
							size="3" class="digits" /> - <input type="text"
							name="examEndScore"
							value='<fmt:formatNumber value="${condition['examEndScore'] }" pattern="###"/>'
							size="3" class="digits" />
						</li>
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
		<c:choose>
			<c:when
				test="${condition['statType'] ne '3' and condition['statType'] ne '4' }">
				<div class="pageContent">
					<gh:resAuth parentCode="RES_TEACHING_EXAM_RECRUITEXAMLOGS"
						pageType="list"></gh:resAuth>
					<table class="table" layouth="184">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_online_recruitExamLogs"
									onclick="checkboxAll('#check_all_online_recruitExamLogs','resourceid','#online_recruitExamLogsBody')" /></th>
								<th width="8%">考试批次</th>
								<th width="8%">教学站</th>
								<th width="8%">课程</th>
								<th width="8%">学号</th>
								<th width="6%">姓名</th>
								<th width="16%">考试时段</th>
								<th width="15%">考试时间</th>
								<th width="8%">登录时间</th>
								<th width="8%">退出时间</th>
								<th width="5%">状态</th>
								<th width="5%">成绩</th>
							</tr>
						</thead>
						<tbody id="online_recruitExamLogsBody">
							<c:forEach items="${recruitExamLogsPage.result}"
								var="recruitExamLog" varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${recruitExamLog.resourceid }" autocomplete="off" /></td>
									<td>${recruitExamLog.examInfo.examSub.batchName }</td>
									<td>${recruitExamLog.studentInfo.branchSchool }</td>
									<td>${recruitExamLog.examInfo.course.courseName }</td>
									<td>${recruitExamLog.studentInfo.studyNo }</td>
									<td>${recruitExamLog.studentInfo.studentName }</td>
									<td><fmt:formatDate
											value="${recruitExamLog.examInfo.examStartTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
											value="${recruitExamLog.examInfo.examEndTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${recruitExamLog.startTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
											value="${recruitExamLog.endTime }" pattern="HH:mm:ss" /></td>
									<td><fmt:formatDate value="${recruitExamLog.loginTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${recruitExamLog.logoutTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><c:choose>
											<c:when test="${recruitExamLog.status eq 0 }">
												<span style="color: green;">在考</span>
											</c:when>
											<c:when test="${recruitExamLog.status eq 1 }">退出</c:when>
											<c:when test="${recruitExamLog.status eq 2 }">已交卷</c:when>
											<c:otherwise>&nbsp;</c:otherwise>
										</c:choose></td>
									<td>${recruitExamLog.examScore }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${recruitExamLogsPage}"
						goPageUrl="${baseUrl }/edu3/teaching/recruitexamlogs/list.html"
						pageType="sys" condition="${condition}" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="pageContent">
					<gh:resAuth parentCode="RES_TEACHING_EXAM_RECRUITEXAMLOGS"
						pageType="sblist"></gh:resAuth>
					<table class="table" layouth="184">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_online_recruitExamLogs"
									onclick="checkboxAll('#check_all_online_recruitExamLogs','resourceid','#online_recruitExamLogsBody')" /></th>
								<th width="10%">考试批次</th>
								<th width="10%">教学站</th>
								<th width="15%">课程</th>
								<th width="20%">考试时段</th>
								<th width="10%">学号</th>
								<th width="10%">姓名</th>
								<th width="10%">移动电话</th>
								<th width="10%">Email</th>
							</tr>
						</thead>
						<tbody id="online_recruitExamLogsBody">
							<c:forEach items="${recruitExamLogsPage.result}" var="plan"
								varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${recruitExamLog.resourceid }" autocomplete="off" /></td>
									<td>${plan.examInfo.examSub.batchName }</td>
									<td>${plan.studentInfo.branchSchool }</td>
									<td>${plan.examInfo.course.courseName }</td>
									<td><fmt:formatDate
											value="${plan.examInfo.examStartTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
											value="${plan.examInfo.examEndTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${plan.studentInfo.studyNo }</td>
									<td>${plan.studentInfo.studentName }</td>
									<td>${plan.studentInfo.studentBaseInfo.mobile }</td>
									<td>${plan.studentInfo.studentBaseInfo.email }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${recruitExamLogsPage}"
						goPageUrl="${baseUrl }/edu3/teaching/recruitexamlogs/list.html"
						pageType="sys" condition="${condition}" />
				</div>
			</c:otherwise>
		</c:choose>

	</div>
</body>
</html>
