<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>老师在线时长列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingManagement/loginLongList_teacher.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>所属单位：</label> <c:choose>
								<c:when test="${isManager=='Y' }">
									<input type="text" value="${unitName}" readonly="readonly" style="width: 240px;" />
									<input type="hidden" name="unitId"
										value="${condition['unitId']}" />
								</c:when>
								<c:otherwise>
									<gh:brSchoolAutocomplete name="unitId" tabindex="1"
										id="teacher_loginLong_unitId" scope="all" displayType="code"
										defaultValue="${condition['unitId']}" style="width:240px;"/>
								</c:otherwise>
							</c:choose></li>
						<li><label>用户名：</label> <input type="text" name="userName"
							value="${condition['userName'] }" /></li>
						<li><label>中文名：</label> <input type="text" name="chinessName"
							value="${condition['chinessName'] }" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_LOGINLONGINFO_TEACHER"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="137">
				<thead>
					<tr>
						<th width="15%">用户名</th>
						<th width="15%">中文名</th>
						<th width="20%">所属单位</th>
						<th width="20%">登录IP</th>
						<th width="15%">最后一次登录时间</th>
						<th width="15%">在线时长</th>
					</tr>
				</thead>
				<tbody id="teacherLoginLongBody">
					<c:forEach items="${teacherLoginLongList.result}" var="ll"
						varStatus="vs">
						<tr>
							<td>${ll.username }</td>
							<td>${ll.cnName }</td>
							<td>${ll.orgUnit.unitName }</td>
							<td>${ll.loginIp }</td>
							<td>${ll.lastLoginTime }</td>
							<td>${ll.loginLongStr }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${teacherLoginLongList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingManagement/loginLongList_teacher.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>