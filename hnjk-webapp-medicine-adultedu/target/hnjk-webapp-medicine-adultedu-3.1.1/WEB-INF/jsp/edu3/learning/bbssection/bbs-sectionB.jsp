<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!--话题列表-->
<FORM name="batch" action="" method="post">
	<!--表头-->
	<DIV class=th>
		<DIV class=list_r>
			<DIV style="FLOAT: left; WIDTH: 100px">作 者</DIV>
			<DIV style="FLOAT: left; WIDTH: 100px">话题权限</DIV>
			<DIV>讨论截止日期</DIV>
		</DIV>
		<DIV class=list1 style="TEXT-ALIGN: center">状态</DIV>
		<DIV>讨论话题</DIV>
	</DIV>
	<c:forEach items="${bbsTopicPage.result }" var="bbsTopic">
		<!--列表-->
		<DIV class=list>
			<DIV class=list_r1>
				<DIV class=list_a>
					<a class="glink"
						href="${baseUrl }/edu3/learning/bbs/user.html?userid=${bbsTopic.bbsUserInfo.sysUser.resourceid }${coursequerystr }"
						target="_blank" title="查看用户${bbsTopic.fillinMan }的资料">${bbsTopic.fillinMan }</a>
				</DIV>
				<DIV class=list_i>
					<SPAN>${ghfn:dictCode2Val('CodeViewPermiss',bbsTopic.viewPermiss) }</SPAN>
				</DIV>
				<DIV class=list_t>
					<a class="glink"
						href="${baseUrl }/edu3/learning/bbs/user.html?userid=${bbsTopic.lastReplyManId }${coursequerystr }"
						target="_blank" title="查看用户${bbsTopic.lastReplyMan }的资料">${bbsTopic.lastReplyMan }</a>
					<BR>
					<fmt:formatDate value="${bbsTopic.endTime }"
						pattern="yyyy-MM-dd HH:mm:ss" />
				</DIV>
			</DIV>
			<DIV style="FLOAT: right; PADDING-TOP: 8px"></DIV>
			<DIV class=list_s>
				<a target="_blank" title="新窗口打开"
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?courseId=${course.resourceid}&topicId=${bbsTopic.resourceid }">
					<c:choose>
						<c:when test="${bbsTopic.topLevel gt 0}">
							<img align="absMiddle"
								src="${baseUrl }/style/default/images/topic_top.gif" alt="总固顶">
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${bbsTopic.status eq 1}">
									<img align="absMiddle"
										src="${baseUrl }/style/default/images/topic_jihua.gif"
										alt="含精华贴的话题" />
								</c:when>
								<c:when test="${bbsTopic.status eq -1}">
									<img align="absMiddle"
										src="${baseUrl }/style/default/images/topic_lock.gif"
										alt="锁定的话题" />
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${fn:length(bbsTopic.bbsReplys) > 10}">
											<img align="absMiddle"
												src="${baseUrl }/style/default/images/topic_hot.gif"
												alt="回复超过10贴">
										</c:when>
										<c:otherwise>
											<img align="absMiddle"
												src="${baseUrl }/style/default/images/topic_normal.gif"
												alt="开放的话题" />
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</a>
			</DIV>
			<DIV class=listtitle>
				<A class=big title="查看小组话题"
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?courseId=${course.resourceid}&topicId=${bbsTopic.resourceid }"
					style="color: #0B7AC0;">${bbsTopic.title }</A>
				<c:forEach items="${bbsTopic.childs }" var="t" varStatus="idx">
						${idx.first?'(':''}
						<a style="color: #DD5E10;"
						href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?courseId=${t.course.resourceid}&topicId=${t.resourceid }">${t.bbsGroup.groupName }
						${(not idx.last)?',':''} </a>
						${idx.last?')':''}
					</c:forEach>
			</DIV>
		</DIV>
		<DIV class=mainbar3 style="DISPLAY: none; TEXT-ALIGN: left">
			<DIV
				style="PADDING-RIGHT: 2px; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px"></DIV>
		</DIV>
	</c:forEach>
</FORM>
