<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}-在线帮助 - 首页</title>
<gh:loadCom components="bbs-defaultcss,jquery,ztree" />

</head>
<body>

	<DIV class=bbs_column2 style="margin-left: 12px">
		<H1>热门问题</H1>
		<UL>
			<!--子版块-->
			<LI style="padding-left: 12px; width: 100%">
				<table border="0" cellpadding="0" cellspacing="0" class="STYLE2"
					style="width: 99%">
					<c:forEach items="${helpArticleListHot.result}" var="helpArticle"
						varStatus="vs">
						<tr>
							<td align="left" width="75%"
								background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
								<a
								href="${baseUrl}/portal/help/list.html?id=${helpArticle.channel.resourceid}">【${helpArticle.channel.channelName}】</a>
								<a
								href="${baseUrl}/portal/help/detail.html?id=${helpArticle.resourceid}">${helpArticle.title}</a>
							</td>
							<td align="right" width="25%"
								background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
								<fmt:formatDate value="${helpArticle.fillinDate }"
									pattern="yyyy年MM月dd日 HH:mm:ss" />
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td align="center">
							<form action="" method="post">
								<gh:page page="${helpArticleList}" pageType="other"
									condition="${condition }" />
							</form>
						</td>
					</tr>
				</table> <!--如果最后一个版块,需要加上以下这句-->
				<DIV style="CLEAR: both"></DIV>
			</LI>

		</UL>
	</DIV>
	<DIV style="CLEAR: both"></DIV>
	<!--顶级版块完-->

	<DIV class=bbs_column2 style="margin-left: 12px">
		<H1>最新帮助指南</H1>
		<UL>
			<LI style="padding-left: 12px; width: 100%">
				<table border="0" cellpadding="0" cellspacing="0" class="STYLE2"
					style="width: 99%">
					<c:forEach items="${helpArticleListNew.result}" var="helpArticle"
						varStatus="vs">
						<tr>
							<td align="left" width="75%"
								background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
								<a
								href="${baseUrl}/portal/help/list.html?id=${helpArticle.channel.resourceid}">【${helpArticle.channel.channelName}】</a>
								<a
								href="${baseUrl}/portal/help/detail.html?id=${helpArticle.resourceid}">${helpArticle.title}</a>
							</td>
							<td align="right" width="25%"
								background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
								<fmt:formatDate value="${helpArticle.fillinDate }"
									pattern="yyyy年MM月dd日 HH:mm:ss" />
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td align="center">
							<form action="" method="post">
								<gh:page page="${helpArticleList}" pageType="other"
									condition="${condition }" />
							</form>
						</td>
					</tr>
				</table>
		</UL>
	</DIV>
	<DIV style="CLEAR: both"></DIV>


</body>
</html>