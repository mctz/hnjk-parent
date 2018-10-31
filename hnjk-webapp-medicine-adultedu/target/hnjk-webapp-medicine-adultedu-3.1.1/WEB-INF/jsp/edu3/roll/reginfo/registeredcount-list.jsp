<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册情况统计</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/reginfo/registeredcount-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
								defaultValue="${condition['branchSchool']}" style="width:120px" />
						</li>
						<!--<li>
					<label>批次：</label><gh:selectModel name="RecruitPlan" bindValue="resourceid" displayValue="RecruitPlanname" value="${condition['RecruitPlan']}"
							modelClass="com.hnjk.edu.recruit.model.RecruitPlan" />
				</li>
				-->
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic" /></li>
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
				</ul>
			</div>
			<table class="table" layouth="138">
				<thead>
					<tr class="head_bg">
						<th width="5%"><input type="checkbox" name="checkall"
							id="registeredcount_check_all"
							onclick="checkboxAll('#registeredcount_check_all','resourceid','#classBody')" /></th>
						<th width="9%">注册号</th>
						<th width="9%">学号</th>
						<th width="9%">姓名</th>
						<th width="9%">性别</th>
						<th width="10%">教学站</th>
						<th width="10%">年级</th>
						<th width="10%">学制</th>
						<th width="10%">培养层次</th>
						<th width="10%">专业</th>
						<th width="9%">注册时间</th>
					</tr>
				</thead>
				<tbody id="classBody">
					<c:forEach items="${reglist.result}" var="reg" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${reg.resourceid }" autocomplete="off" /></td>
							<td>${reg.studentInfo.enrolleeCode }</td>
							<td>${reg.studentInfo.studyNo}</td>
							<td>${reg.studentInfo.studentName }</td>
							<td>${ghfn:dictCode2Val('CodeSex',reg.studentInfo.studentBaseInfo.gender) }</td>
							<td>${reg.studentInfo.branchSchool }</td>
							<td>${reg.studentInfo.grade}</td>
							<td>${reg.studentInfo.learningStyle}</td>
							<td>${reg.studentInfo.classic.classicName }</td>
							<td>${reg.studentInfo.major.majorName }</td>
							<td><fmt:formatDate value="${reg.regDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${reglist}"
				goPageUrl="${baseUrl }/edu3/register/reginfo/registeredcount-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
</html>