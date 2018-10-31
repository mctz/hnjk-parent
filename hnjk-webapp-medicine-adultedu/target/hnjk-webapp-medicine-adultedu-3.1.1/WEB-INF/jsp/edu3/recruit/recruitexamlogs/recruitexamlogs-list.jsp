<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学考试日志列表</title>
<script type="text/javascript">
//个别延长考试时间
function modifyRecruitExamLogs(){	
	var url = "${baseUrl}/edu3/recruit/recruitexamlogs/input.html";
	if(isCheckOnlyone('resourceid','#recruitExamLogsBody')){
		navTab.openTab('RES_RECRUIT_RECRUITEXAMLOGS_INPUT', url+'?resourceid='+$("#recruitExamLogsBody input[@name='resourceid']:checked").val(), '修改考试记录');
	}	
}
//强制交卷
function handRecruitExamLogs(){	
	pageBarHandle("您确定要让这些考生强制交卷吗？","${baseUrl}/edu3/recruit/recruitexamlogs/hand.html","#recruitExamLogsBody");
}
//重考
function reexamRecruitExamLogs(){
	pageBarHandle("您确定要让这些考生重考吗？","${baseUrl}/edu3/recruit/recruitexamlogs/reexam.html","#recruitExamLogsBody");
}
//清除交卷状态，继续考试
function continueexamRecruitExamLogs(){
	pageBarHandle("您确定要让这些考生继续考试吗？","${baseUrl}/edu3/recruit/recruitexamlogs/continueexam.html","#recruitExamLogsBody");
}
//导出入学考名单
function exportEntranceRecruitExamLogs(){
	var recruitExamPlanId = $("#RecruitExamLogs_recruitExamPlanId").val();
	if(recruitExamPlanId==""){
		alertMsg.warn("请选择一个考试场次!");
		return false;
	} else {
		var url = "${baseUrl}/edu3/recruit/recruitexamlogs/export.html?"+$("#entrance_recruitExamLogs_Form").serialize();
		downloadFileByIframe(url,"entrance_recruitExamLogs_Iframe");
	}
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="entrance_recruitExamLogs_Form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitexamlogs/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${not isBrSchool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="brSchool" id="RecruitExamLogs_brSchool" tabindex="1"
									defaultValue="${condition['brSchool'] }"></gh:brSchoolAutocomplete>
							</li>
						</c:if>
						<li><label>考试场次：</label> <gh:selectModel style="width:150px;"
								id="RecruitExamLogs_recruitExamPlanId" name="recruitExamPlanId"
								bindValue="resourceid" displayValue="examplanName"
								orderBy="recruitPlan.yearInfo.firstYear desc,recruitPlan.term desc,recruitPlan.resourceid desc,resourceid desc"
								modelClass="com.hnjk.edu.recruit.model.RecruitExamPlan"
								condition="recruitPlan.isPublished='Y'"
								value="${condition['recruitExamPlanId']}" /></li>
						<li><label>考试科目：</label> <gh:select
								dictionaryCode="CodeEntranceExam"
								id="RecruitExamLogs_courseName" name="courseName"
								value="${condition['courseName']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name'] }" /></li>
						<li><label>准考证号：</label> <input type="text"
							name="examCertificateNo"
							value="${condition['examCertificateNo'] }" /></li>
						<li><label>状态：</label> <select name="status">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['status'] eq -1 }">selected="selected"</c:if>>未登录</option>
								<option value="0"
									<c:if test="${condition['status'] eq 0 }">selected="selected"</c:if>>考试中</option>
								<option value="2"
									<c:if test="${condition['status'] eq 2 }">selected="selected"</c:if>>已交卷</option>
								<option value="1"
									<c:if test="${condition['status'] eq 1 }">selected="selected"</c:if>>退出</option>
								<option value="3"
									<c:if test="${condition['status'] eq 3 }">selected="selected"</c:if>>断线</option>
						</select></li>
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
			<gh:resAuth parentCode="RES_RECRUIT_RECRUITEXAMLOGS" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_recruitExamLogs"
							onclick="checkboxAll('#check_all_recruitExamLogs','resourceid','#recruitExamLogsBody')" /></th>
						<th width="10%">教学站</th>
						<th width="8%">考试场次</th>
						<th width="8%">考试科目</th>
						<th width="8%">准考证号</th>
						<th width="8%">姓名</th>
						<th width="15%">考试时间</th>
						<th width="10%">登录时间</th>
						<th width="10%">退出时间</th>
						<c:if test="${not isBrSchool }">
							<th width="8%">登录ip</th>
						</c:if>
						<th width="10%">状态</th>
					</tr>
				</thead>
				<tbody id="recruitExamLogsBody">
					<c:forEach items="${recruitExamLogsPage.result}"
						var="recruitExamLog" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${recruitExamLog.resourceid }" autocomplete="off" /></td>
							<td>${recruitExamLog.brSchool }</td>
							<td>${recruitExamLog.recruitExamPlan }</td>
							<td>${ghfn:dictCode2Val('CodeEntranceExam',recruitExamLog.courseName) }</td>
							<td>${recruitExamLog.enrolleeInfo.examCertificateNo }</td>
							<td>${recruitExamLog.enrolleeInfo.studentBaseInfo.name }</td>
							<td><fmt:formatDate value="${recruitExamLog.startTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
									value="${recruitExamLog.endTime }" pattern="HH:mm:ss" /></td>
							<td><fmt:formatDate value="${recruitExamLog.loginTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${recruitExamLog.logoutTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<c:if test="${not isBrSchool }">
								<td>${recruitExamLog.loginIp }</td>
							</c:if>
							<td><c:choose>
									<c:when test="${recruitExamLog.isExceptional eq 'Y' }">
										<span style="color: red;">断线</span>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${recruitExamLog.status eq 0 }">
												<span style="color: green;">考试中</span>
											</c:when>
											<c:when test="${recruitExamLog.status eq -1 }">未登录</c:when>
											<c:when test="${recruitExamLog.status eq 1 }">退出</c:when>
											<c:when test="${recruitExamLog.status eq 2 }">已交卷</c:when>
											<c:otherwise>&nbsp;</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${recruitExamLogsPage}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitexamlogs/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
