<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册信息查询</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/reginfo/registered-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label> <gh:selectModel id="stuGrade"
								name="grade" bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['grade']}" orderBy="yearInfo.firstYear desc"
								choose="Y" style="width: 120px" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 115px" />
						</li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
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
							id="check_all_reg"
							onclick="checkboxAll('#check_all_reg','resourceid','#regBody')" /></th>
						<th width="6%">注册号</th>
						<th width="6%">考生号</th>
						<th width="6%">姓名</th>
						<th width="7%">学号</th>
						<th width="5%">性别</th>
						<th width="10%">教学站</th>
						<th width="8%">身份证号</th>
						<th width="6%">年级</th>
						<th width="6%">培养层次</th>
						<th width="6%">专业</th>
						<th width="6%">学籍状态</th>
						<th width="6%">注册人</th>
						<th width="10%">注册时间</th>

					</tr>
				</thead>
				<tbody id="regBody">
					<c:forEach items="${reglist.result}" var="reg" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${reg.resourceid }" autocomplete="off" /></td>
							<td>${reg.studentInfo.registorNo }</td>
							<td>${reg.studentInfo.enrolleeCode}</td>
							<td>${reg.studentInfo.studentName }</td>
							<td>${reg.studentInfo.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',reg.studentInfo.studentBaseInfo.gender) }</td>
							<td>${reg.studentInfo.branchSchool }</td>
							<td>${reg.studentInfo.studentBaseInfo.certNum}</td>
							<td>${reg.studentInfo.grade}</td>
							<td>${reg.studentInfo.classic }</td>
							<td>${reg.studentInfo.major }</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',reg.studentInfo.studentStatus) }</td>
							<td>${reg.fillinMan }</td>
							<td><fmt:formatDate value="${reg.regDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${reglist}"
				goPageUrl="${baseUrl }/edu3/register/reginfo/registered-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
</html>