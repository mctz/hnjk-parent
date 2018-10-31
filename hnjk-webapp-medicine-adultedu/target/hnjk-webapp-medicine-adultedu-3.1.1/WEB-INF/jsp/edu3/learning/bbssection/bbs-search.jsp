<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>
<c:if test="${not empty param.sType }">
	<script type="text/javascript">		
		$(function (){			
			var kwd = "${condition['keyword']}";	
			$(".listtitle a,.list_a a,div[name='searchcontent']").highlight(kwd);
		});	
	</script>
</c:if>
<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">广东学苑在线</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A> →
			<c:choose>
				<c:when
					test="${(not empty condition['search']) and condition['search']=='hot' }">热门主题</c:when>
				<c:when
					test="${(not empty condition['search']) and condition['search']=='new' }">最新主题</c:when>
				<c:when
					test="${(not empty condition['search']) and condition['search']=='digest' }">精华主题</c:when>
				<c:when
					test="${(not empty condition['search']) and condition['search']=='mine' }">我的主题</c:when>
				<c:otherwise>论坛搜索</c:otherwise>
			</c:choose>
		</DIV>
	</DIV>
</DIV>

<c:if test="${empty searchBbsTopicPage.result }">
	<DIV>对不起，没有找到匹配结果。</DIV>
</c:if>
<c:if test="${not empty searchBbsTopicPage.result }">
	<!--帖子列表-->
	<FORM name="batch" action="" method="post" id="searchForm">
		<!--表头-->
		<DIV class=th>
			<DIV class=list_r>
				<DIV style="FLOAT: left; WIDTH: 100px">作 者</DIV>
				<DIV style="FLOAT: left; WIDTH: 100px">回复 / 人气</DIV>
				<DIV>最后更新</DIV>
			</DIV>
			<DIV class=list1 style="TEXT-ALIGN: center">状态</DIV>
			<DIV>主 题</DIV>
		</DIV>
		<c:forEach items="${searchBbsTopicPage.result }" var="bbsTopic">
			<!--列表-->
			<DIV class=list>
				<DIV class=list_r1>
					<DIV class=list_a>
						<A class=glink href="#" target=_blank>${bbsTopic.fillinMan }</A>
					</DIV>
					<DIV class=list_i>
						<SPAN>${bbsTopic.replyCount }</SPAN> / <SPAN>${bbsTopic.clickCount }</SPAN>
					</DIV>
					<DIV class=list_t>
						<a class="glink" href="#" target="_blank">${bbsTopic.lastReplyMan }</a>
						<BR>
						<fmt:formatDate value="${bbsTopic.lastReplyDate }"
							pattern="yyyy-MM-dd HH:mm:ss" />
					</DIV>
				</DIV>
				<DIV style="FLOAT: right; PADDING-TOP: 8px"></DIV>
				<DIV class=list_s>
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

				</DIV>
				<DIV class=listtitle>
					<A class=big title=""
						href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?topicId=${bbsTopic.resourceid }${coursequerystr }"
						target="_blank" style="color: #0B7AC0;">${bbsTopic.title }&nbsp;<c:if
							test="${not empty bbsTopic.bbsGroup }">(${bbsTopic.bbsGroup.groupName })</c:if></A>
				</DIV>
			</DIV>
			<DIV name="searchcontent" class=mainbar3
				style="TEXT-ALIGN: left;display: ${fn:containsIgnoreCase(param.sType,'content')?'block':'none'}">
				<c:if test="${fn:containsIgnoreCase(param.sType,'content')}">
					<DIV name="searchDiv"
						style="PADDING: 2px; line-height: 20px; max-height: 100px; overflow: hidden;">
						${bbsTopic.content }</DIV>
				</c:if>
			</DIV>
		</c:forEach>
	</FORM>

	<!--分页-->
	<gh:page page="${searchBbsTopicPage}"
		goPageUrl="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html"
		condition="${condition }" pageType="bbs" />

</c:if>
<BR>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>