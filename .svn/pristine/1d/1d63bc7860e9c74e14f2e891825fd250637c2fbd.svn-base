<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>任课情况</title>
<style type="text/css">
td,th {
	text-align: center;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/learning/student/addressbook.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 180px;">年度：
						<gh:selectModel name="yearInfo" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" orderBy="firstYear desc" /></li>
						<li style="width: 180px;">学期：
						<gh:select name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" /></li>
						<c:if test="${empty onlineSchoolName }">
							<li class="custom-li"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="student_addressbook_branchSchool" displayType="code"
									defaultValue="${condition['branchSchool']}"></gh:brSchoolAutocomplete>
							</li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> ${studentaddressbookCourseSelect }</li>
						<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="student_addressbook_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"></gh:classesAutocomplete></li>
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
			<%--<gh:resAuth parentCode="RES_METARES_STUDENTADDRESSBOOK" pageType="list"></gh:resAuth> --%>
			<table class="table" layouth="130">
				<thead>
					<tr>
						<th width="15%">任课班级</th>
						<th width="15%">教学点</th>
						<th width="8%">年级</th>
						<th width="8%">层次</th>
						<th width="10%">专业</th>
						<th width="5%">学习方式</th>
						<th width="15%">任课课程</th>
						<th width="10%">上课学期</th>
						<th width="5%">人数</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody id="studentAddressBook">
					<c:set value="0" var="sum" />
					<c:forEach items="${stuplanlist.result}" var="plan" varStatus="vs">
						<c:if test="${plan.stuNumber ne '0' }">
							<tr>
								<td>${plan.classname }</td>
								<td>${plan.unitname }</td>
								<td>${plan.gradename }</td>
								<td>${plan.classicname }</td>
								<td>${plan.majorname }</td>
								<td>${ghfn:dictCode2Val('CodeTeachingType',plan.teachingtype) }</td>
								<td>${plan.coursename }</td>
								<td>${ghfn:dictCode2Val('CodeCourseTermType',plan.term) }</td>
								<td>${plan.stuNumber } <input type="hidden" value="${totalNumber+plan.stuNumber }"></td>
								<td><a href="${baseUrl }/edu3/learning/student/online-student.html?classesid=${plan.resourceid}"
									target="navTab" rel="timetable" title="查看学生名单">查看学生名单</a></td>
							</tr>
							 <c:set value="${sum+plan.stuNumber}" var="sum" /> 
						</c:if>
					</c:forEach>
					<%-- <tr><td>总人数：${sum }</td></tr> --%>
				</tbody>
			</table>
			<gh:page page="${stuplanlist}"
				goPageUrl="${baseUrl}/edu3/learning/student/addressbook.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>