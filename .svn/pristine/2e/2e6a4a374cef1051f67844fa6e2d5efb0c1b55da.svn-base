<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程预约列表</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="notCourseOrderSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/not-courseorder-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isBranchSchool'] !=true}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="courseorder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>年度：</label>
						<gh:selectModel id="yearInfo" name="yearInfo"
								bindValue="resourceid" displayValue="yearName" style="width:57%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="yearName desc" value="${condition['yearInfo']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:57%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:57%" /></li>
						<li><label>层次：</label>
						<gh:selectModel id="classic" name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:57%" /></li>
						<li><label>专业：</label>
						<gh:selectModel id="major" name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorCode" style="width:57%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>课程:</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="courseId" value="${condition['courseId'] }"
								displayType="code" style="width:55%" /></li>
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}" style="width: 55%" />
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
		<div class="pageContent">
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_not_course_order"
							onclick="checkboxAll('#check_all_not_course_order','resourceid','#notCourseOrderListBody')" /></th>
						<th width="10%">姓名</th>
						<th width="30%">联系电话</th>
						<th width="10%">电子邮件</th>
						<th width="10%">年级</th>
						<th width="10%">层次</th>
						<th width="15%">专业</th>
						<th width="10%">教学站</th>
					</tr>
				</thead>

				<tbody id="notCourseOrderListBody">
					<c:forEach items="${page.result}" var="studentInfo" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid" value=""
								autocomplete="off" /></td>
							<td>${studentInfo.NAME }</td>
							<td>${studentInfo.TEMPCONTACTPHONE}</td>
							<td>${studentInfo.EMAIL }</td>
							<td>${studentInfo.GRADENAME}</td>
							<td>${studentInfo.CLASSICNAME}</td>
							<td>${studentInfo.MAJORNAME}</td>
							<td>${studentInfo.BRANCHNAME }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/not-courseorder-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
</body>
</html>