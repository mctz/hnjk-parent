<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的主题</title>
</script>
</head>
<body>
	<div class="page">
		<c:if test="${condition['currentIndex'] ne '3' }">
			<div class="pageHeader">
				<form
					onsubmit="return localAreaSearch('mybbstopic${condition['currentIndex'] }',this);"
					action="${baseUrl }/edu3/framework/student/topic/view.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>课程：</label> <select name="courseId"
								style="width: 125px;">
									<option value="">请选择</option>
									<c:forEach items="${courseList }" var="c">
										<option value="${c.resourceid }" title="${c.courseName }"
											<c:if test="${c.resourceid eq condition['courseId'] }">selected="selected"</c:if>>${c.courseName }</option>
									</c:forEach>
							</select> <input type="hidden" name="currentIndex"
								value="${condition['currentIndex'] }" /></li>
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
			<c:choose>
				<c:when test="${condition['currentIndex'] eq '0' }">
					<table class="table" layouth="150">
						<thead>
							<tr>
								<th width="5%">&nbsp;</th>
								<th width="8%">课程</th>
								<th width="8%">知识节点</th>
								<th width="34%">标题</th>
								<th width="10%">发帖人</th>
								<th width="10%">发帖时间</th>
								<th width="5%">回复数</th>
								<th width="10%">最后回复人</th>
								<th width="10%">最后回复日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
								varStatus="vs">
								<tr>
									<td>&nbsp;</td>
									<td>${bbsTopic.course.courseName }</td>
									<td>${bbsTopic.syllabus.nodeName }</td>
									<td><strong>${bbsTopic.topLevel>0?'置顶':''}
											${bbsTopic.status != 0 ? ghfn:dictCode2Val('CodeBbsTopicStatus',bbsTopic.status):'' }</strong>
										<a href="javascript:;"
										onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a>
									</td>
									<td>${bbsTopic.fillinMan}</td>
									<td><fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${bbsTopic.replyCount }</td>
									<td><c:if test="${bbsTopic.replyCount>0 }">${bbsTopic.lastReplyMan }</c:if></td>
									<td><c:if test="${bbsTopic.replyCount>0 }">
											<fmt:formatDate value="${bbsTopic.lastReplyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${bbsTopicListPage}"
						goPageUrl="${baseUrl}/edu3/framework/student/topic/view.html"
						condition="${condition }" pageType="sys" localArea="mybbstopic0"
						targetType="localArea" />
				</c:when>
				<c:when test="${condition['currentIndex'] eq '1' }">
					<table class="table" layouth="150">
						<thead>
							<tr>
								<th width="5%">&nbsp;</th>
								<th width="9%">所属课程</th>
								<th width="30%">讨论话题</th>
								<th width="10%">发表人</th>
								<th width="10%">发表时间</th>
								<th width="8%">话题权限</th>
								<th width="10%">讨论截止时间</th>
								<th width="8%">最后回复人</th>
								<th width="10%">最后回复日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
								varStatus="vs">
								<tr>
									<td>&nbsp;</td>
									<td>${bbsTopic.course.courseName }</td>
									<td><strong>${bbsTopic.topLevel>0?'置顶':''}
											${bbsTopic.status != 0 ? ghfn:dictCode2Val('CodeBbsTopicStatus',bbsTopic.status):'' }</strong>
										<a href="javascript:;"
										onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a>
									</td>
									<td>${bbsTopic.fillinMan}</td>
									<td><fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${ghfn:dictCode2Val('CodeViewPermiss',bbsTopic.viewPermiss) }</td>
									<td><fmt:formatDate value="${bbsTopic.endTime }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><c:if test="${bbsTopic.replyCount>0 }">${bbsTopic.lastReplyMan }</c:if></td>
									<td><c:if test="${bbsTopic.replyCount>0 }">
											<fmt:formatDate value="${bbsTopic.lastReplyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${bbsTopicListPage}"
						goPageUrl="${baseUrl}/edu3/framework/student/topic/view.html"
						condition="${condition }" pageType="sys" localArea="mybbstopic1"
						targetType="localArea" />
				</c:when>
				<c:when test="${condition['currentIndex'] eq '2' }">
					<table class="table" layouth="150">
						<thead>
							<tr>
								<th width="5%">&nbsp;</th>
								<th width="10%">课程</th>
								<th width="10%">所属论坛版块</th>
								<th width="26%">标题</th>
								<th width="8%">帖子类型</th>
								<th width="8%">发帖人</th>
								<th width="10%">发帖时间</th>
								<th width="5%">回复数</th>
								<th width="8%">最后回复人</th>
								<th width="10%">最后回复日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
								varStatus="vs">
								<tr>
									<td>&nbsp;</td>
									<td>${bbsTopic.course.courseName }</td>
									<td>${bbsTopic.bbsSection.sectionName }</td>
									<td><strong>${bbsTopic.topLevel>0?'置顶':''}
											${bbsTopic.status != 0 ? ghfn:dictCode2Val('CodeBbsTopicStatus',bbsTopic.status):'' }</strong>
										<a href="javascript:;"
										onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a>
									</td>
									<td>${ghfn:dictCode2Val('CodeBbsTopicType',bbsTopic.topicType) }</td>
									<td>${bbsTopic.fillinMan}</td>
									<td><fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${bbsTopic.replyCount }</td>
									<td><c:if test="${bbsTopic.replyCount>0 }">${bbsTopic.lastReplyMan }</c:if></td>
									<td><c:if test="${bbsTopic.replyCount>0 }">
											<fmt:formatDate value="${bbsTopic.lastReplyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${bbsTopicListPage}"
						goPageUrl="${baseUrl}/edu3/framework/student/topic/view.html"
						condition="${condition }" pageType="sys" localArea="mybbstopic2"
						targetType="localArea" />
				</c:when>
				<c:otherwise>
					<table class="table" layouth="88">
						<thead>
							<tr>
								<th width="5%">&nbsp;</th>
								<th width="10%">所属论坛版块</th>
								<th width="32%">标题</th>
								<th width="8%">帖子类型</th>
								<th width="10%">发帖人</th>
								<th width="10%">发帖时间</th>
								<th width="5%">回复数</th>
								<th width="10%">最后回复人</th>
								<th width="10%">最后回复日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
								varStatus="vs">
								<tr>
									<td>&nbsp;</td>
									<td>${bbsTopic.bbsSection.sectionName }</td>
									<td><strong>${bbsTopic.topLevel>0?'置顶':''}
											${bbsTopic.status != 0 ? ghfn:dictCode2Val('CodeBbsTopicStatus',bbsTopic.status):'' }</strong>
										<a href="javascript:;"
										onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a>
									</td>
									<td>${ghfn:dictCode2Val('CodeBbsTopicType',bbsTopic.topicType) }</td>
									<td>${bbsTopic.fillinMan}</td>
									<td><fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${bbsTopic.replyCount }</td>
									<td><c:if test="${bbsTopic.replyCount>0 }">${bbsTopic.lastReplyMan }</c:if></td>
									<td><c:if test="${bbsTopic.replyCount>0 }">
											<fmt:formatDate value="${bbsTopic.lastReplyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${bbsTopicListPage}"
						goPageUrl="${baseUrl}/edu3/framework/student/topic/view.html"
						condition="${condition }" pageType="sys" localArea="mybbstopic3"
						targetType="localArea" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>