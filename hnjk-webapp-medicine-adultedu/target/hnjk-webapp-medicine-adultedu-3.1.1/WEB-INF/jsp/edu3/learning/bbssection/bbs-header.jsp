<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- 论坛</title>
<gh:loadCom
	components="bbs-defaultcss,jquery,framework,editor,fileupload,datePicker" />
<style type="text/css">
<!--
.STYLE1 {
	color: #FFFFFF;
	font-weight: bold;
}

.STYLE2 {
	color: #999999
}
-->
</style>
</head>
<body>
	<UL id=header>
		<LI style="FLOAT: left"><a href="##"><IMG
				src="${baseUrl }/style/default/images/img.gif" alt="" width="765"
				height="85"
				style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px">
		</a></LI>
		<LI
			style="MARGIN-TOP: 5px; FLOAT: left; MARGIN-LEFT: 50px; TEXT-ALIGN: center">
		<LI
			style="BORDER-RIGHT: #ffffff 1px solid; PADDING: 5px 0px 0px; BORDER-TOP: #ffffff 1px solid; MARGIN-TOP: 0px; BORDER-LEFT: #ffffff 1px solid; BORDER-BOTTOM: #ffffff 1px solid; LIST-STYLE-TYPE: none">
			<UL>
				<c:if test="${not empty course}">
					<c:set var="querys" value="?courseId=${course.resourceid}"></c:set>
					<c:set var="coursequerystr" value="&courseId=${course.resourceid}"></c:set>
				</c:if>
				<c:choose>
					<c:when test="${not empty user }">
						<DIV>
							欢迎您：<a
								href="${baseUrl }/edu3/learning/bbs/user.html?userid=${user.resourceid}${coursequerystr }"
								target="_blank" title="查看用户${user.username }的资料">${user.username }</a>
						</DIV>
						<DIV>
							<LI class=m_li_top style="DISPLAY: inline"></LI>
							<DIV class="submenu submunu_popup">
								<DIV class=menuitems></DIV>
							</DIV>
							<A
								href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html?search=mine${coursequerystr }">我的主题</A>
							| <A
								href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/advancedSearch.html${querys}">搜索</A>
						</DIV>
					</c:when>
					<c:otherwise>
						<DIV>
							欢迎您：游客！请先 <a href="#">登录</a>
						</DIV>
					</c:otherwise>
				</c:choose>
			</UL>
		</LI>
	</UL>
	<br />
	<c:if test="${not empty course }">
		<gh:courseBbsSection course="${course }" />
	</c:if>