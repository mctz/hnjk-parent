<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>
<script type="text/javascript">
<!--
$(document).ready(function(){
	ajaxUpdateSectionClickCount();
});
function ajaxUpdateSectionClickCount(){
	var url = "${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic/update/ajax.html";
	jQuery.post(url,{type:'section',sectionId:'${bbsSection.resourceid }'});
}
//-->
</script>
<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">学苑网院论坛</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A>
			<c:forEach items="${bbsSections }" var="section" varStatus="vs">
						→ <A
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${section.resourceid }${coursequerystr }"
					class="STYLE1"> ${section.sectionName }</A>
			</c:forEach>
			→ 帖子列表
		</DIV>
	</DIV>
</DIV>

<c:if test="${not empty bbsSection.childs }">
	<!--帖子版块区-->
	<DIV id=left></DIV>
	<DIV id=center1></DIV>
	<DIV id=center2>
		<!-- 顶级版块开始 -->
		<DIV class=bbs_column2 style="width: 960px;">
			<H1>${bbsSection.sectionName }</H1>
			<UL>
				<c:forEach items="${bbsSection.childs }" var="section"
					varStatus="vs">
					<!--子版块-->
					<LI style="WIDTH: 48%">
						<DL class="${(section.todayCount==0)?'today':'todaynew' }">
							<DT>${section.todayCount }
							<DD>今日帖</DD>
						</DL>
						<DIV>
							<a
								href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${section.resourceid }${coursequerystr }">『
								${section.sectionName } 』</a>
						</DIV> <SPAN>主题：${section.statTopicCount }&nbsp;&nbsp;
							|&nbsp;&nbsp;帖子：${section.invitationCount }<BR> <c:choose>
								<c:when test="${empty section.masterName }">本版暂无版主</c:when>
								<c:otherwise>
									<c:forEach items="${fn:split(section.masterId, ',')}" var="mid">
										<a
											href="${baseUrl }/edu3/learning/bbs/user.html?userid=${mid}${coursequerystr }"
											target="_blank" title="查看版主${ghfn:ids2Names('user',mid)}的资料">${ghfn:ids2Names('user',mid)}</a>
					            		&nbsp;
					            	</c:forEach>
								</c:otherwise>
							</c:choose>
					</SPAN> <c:if test="${vs.last }">
							<!--如果最后一个版块,需要加上以下这句-->
							<DIV style="CLEAR: both"></DIV>
						</c:if>
					</LI>
					<!--子版块1完-->
				</c:forEach>
			</UL>
		</DIV>
		<DIV style="CLEAR: both"></DIV>
		<!--顶级版块完-->
</c:if>

<!--用户操作按钮-->
<DIV class=main style="MARGIN-TOP: 4px; LINE-HEIGHT: 28px; HEIGHT: 28px">
	<c:if
		test="${(not empty user)and(bbsSection.sectionCode ne groupSectionCode )and(bbsSection.sectionCode ne questionSectionCode) and (user.userType ne 'student' or bbsSection.isReadonly ne 'Y')}">
		<DIV class=nav style="FLOAT: left">
			<UL>
				<LI class=menu2 onmouseover="this.className='menu1'"
					onmouseout="this.className='menu2'"><A href="javascript:;">&nbsp;&nbsp;我要发表</A>
					<DIV class=postlist>
						<A title=发表一个新帖子
							href="${baseUrl }/edu3/learning/bbs/newtopic.html?sectionId=${bbsSection.resourceid }${coursequerystr }"><SPAN><IMG
								src="${baseUrl }/style/default/images/new_topic.gif" border=0>
						</SPAN>新的主题</A> <BR>
					</DIV></LI>
			</UL>
		</DIV>
	</c:if>
</DIV>

<!--版主信息-->
<DIV class=mainbar4 id=boardmaster
	style="border-bottom: 1px solid #9CC3D9;">
	<div id="boardmanage">
		<a title="查看全部主题"
			href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${bbsSection.resourceid }${coursequerystr }">全部</a>
		| <a title="查看本版精华"
			href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${bbsSection.resourceid }${coursequerystr }&search=digest">精华</a>
	</div>
	<DIV class=masterpic></DIV>
	<DIV style="FLOAT: left; TEXT-INDENT: 5px">
		<c:choose>
			<c:when test="${empty bbsSection.masterName }">本版暂无版主</c:when>
			<c:otherwise>
				<c:forEach items="${fn:split(bbsSection.masterId, ',')}" var="mid">
					<a
						href="${baseUrl }/edu3/learning/bbs/user.html?userid=${mid}${coursequerystr }"
						target="_blank" title="查看版主${ghfn:ids2Names('user',mid)}的资料">${ghfn:ids2Names('user',mid)}</a>
	            		&nbsp;
	            	</c:forEach>
			</c:otherwise>
		</c:choose>
	</DIV>
</DIV>
<c:if test="${empty bbsTopicPage.result }">
	<div>本论坛或指定的范围内尚无主题。</div>
</c:if>
<c:if test="${not empty bbsTopicPage.result }">
	<!--帖子列表-->
	<c:choose>
		<c:when test="${bbsSection.sectionCode ne groupSectionCode }">
			<%@ include
				file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-sectionA.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include
				file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-sectionB.jsp"%>
		</c:otherwise>
	</c:choose>
	<!--分页-->
	<gh:page page="${bbsTopicPage}"
		goPageUrl="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html"
		condition="${condition }" pageType="bbs" />
</c:if>
<BR>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>