<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师发帖统计</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/bbs/edumanager/stat.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="unitId" tabindex="1" id="edumanagerBbsstat_as_unitId"
								defaultValue="${condition['unitId']}" displayType="code"
								scope="all" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								id="edumanagerbbsstatCourseId" name="courseId" tabindex="1"
								value="${condition['courseId']}" displayType="code" /></li>
						<li><label>账号：</label><input type="text" name="username"
							value="${condition['username'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="cnName"
							value="${condition['cnName'] }" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/learning/bbs/edumanager/stat.html?con=advance"
								target="dialog" rel="edumanager_stat" title="查询条件"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="20%">教学站</th>
						<th width="20%">课程</th>
						<th width="15%">姓名</th>
						<th width="15%">登录名</th>
						<th width="10%">主题数</th>
						<th width="10%">回复数</th>
						<th width="10%">总发帖数</th>
					</tr>
				</thead>
				<tbody id="bbsStatBody">
					<c:forEach items="${bbsStatList.result }" var="stat">
						<tr>
							<td>${stat.unitName }</td>
							<td>${stat.courseName }</td>
							<td>${stat.cnName }</td>
							<td>${stat.username }</td>
							<td>${stat.topic }</td>
							<td>${stat.reply }</td>
							<td>${stat.topic + stat.reply }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsStatList}"
				goPageUrl="${baseUrl}/edu3/learning/bbs/edumanager/stat.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>