<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论文讨论小组</title>
<script type="text/javascript">
//新增
function addGroupGraduateMsg(){
	navTab.openTab('RES_TEACHING_THESIS_MOOT_ADD', '${baseUrl}/edu3/teaching/graduatemsg/group/input.html', '论文讨论小组');
}
</script>
</head>
<body>
	<div class="page">
		<c:if test="${condition['isStudent'] eq 'N'}">
			<div class="pageHeader">
				<form onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/teaching/graduatemsg/group/list.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>论文批次：</label> <select name="batchId"
								style="width: 50%;">
									<option value="">请选择...</option>
									<c:forEach items="${ examSubList}" var="examSub">
										<option value="${examSub.resourceid }"
											<c:if test="${examSub.resourceid eq  condition['batchId']}">selected</c:if>>${examSub.batchName }</option>
									</c:forEach>
							</select></li>
							<li><security:authorize
									ifNotGranted="ROLE_TEACHER_GRADUATE,ROLE_STUDENT">
									<span class="tips">提示：只能导师和学生才能使用该功能</span>
								</security:authorize></li>
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
		</c:if>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_THESIS_MOOT" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_msg_group"
							onclick="checkboxAll('#check_all_msg_group','resourceid','#msg_groupBody')" /></th>
						<th width="25%">标题</th>
						<th width="10%">发帖人</th>
						<th width="10%">发帖时间</th>
						<th width="10%">最新回帖人</th>
						<th width="10%">最新回帖时间</th>
						<th width="30%">小组成员</th>
					</tr>
				</thead>
				<tbody id="msg_groupBody">
					<c:forEach items="${msgList.result}" var="msg" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${msg.resourceid }" autocomplete="off" /></td>
							<td><a
								href="${baseUrl}/edu3/teaching/graduatemsg/group/input.html?parentId=${msg.resourceid}"
								rel="RES_TEACHING_THESIS_MOOT_ADD" target="navTab"
								title="${msg.title }">${msg.title }</a></td>
							<td>${msg.fillinMan }</td>
							<td><fmt:formatDate value="${msg.sendTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${msg.lastReplyMan }</td>
							<td><fmt:formatDate value="${msg.lastReplyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${msg.graduateMentor.studentDetail }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:choose>
				<c:when test="${condition['isStudent'] eq 'N'}">
					<gh:page page="${msgList}"
						goPageUrl="${baseUrl }/edu3/teaching/graduatemsg/group/list.html"
						pageType="sys" condition="${condition}" />
				</c:when>
				<c:otherwise>
					<gh:page page="${msgList}"
						goPageUrl="${baseUrl }/edu3/teaching/graduatemsg/group/list.html"
						pageType="sys" targetType="localArea" localArea="jbsxBox"
						condition="${condition}" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>

</body>
</html>