<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的学习小组</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="20%">所属课程</th>
						<th width="15%">组名</th>
						<th width="15%">组描述</th>
						<th width="10%">组长</th>
						<th width="35%">小组成员</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${bbsGroups }" var="bbsGroup" varStatus="vs">
						<tr>
							<td>&nbsp;</td>
							<td>${bbsGroup.course.courseName }</td>
							<td><a
								href="${baseUrl }/edu3/learning/bbs/section.html?courseId=${bbsGroup.course.resourceid }&sectionId=${bbsGroupSectiopnId }"
								target="_blank">${bbsGroup.groupName }</a></a></td>
							<td>${bbsGroup.groupDescript }</td>
							<td>${bbsGroup.leaderName}</td>
							<td>${bbsGroup.groupUserNames }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>