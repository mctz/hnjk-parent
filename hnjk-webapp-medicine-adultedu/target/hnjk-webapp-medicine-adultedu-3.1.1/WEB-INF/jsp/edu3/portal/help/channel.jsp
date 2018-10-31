<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- 在线帮助</title>
<gh:loadCom components="bbs-defaultcss,jquery,ztree" />
<style type="text/css">
/* ------ 分页样式(开始) ------------*/
.sk_file_page {
	text-align: center;
	height: 25px;
	float: left;
	margin: 10px auto;
}
/*第一页*/
.sk_pageL {
	width: 16px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
}

.sk_pageL a {
	display: block;
	background: url(../../images/framework/file_botton_f.gif) left top
		no-repeat;
	width: 16px;
	height: 17px;
}

.sk_pageL a span {
	display: none;
}

.sk_pageL a:hover {
	background-position: 0px -17px;
}

.sk_pageL a:active {
	background-position: 0px -34px;
}
/*最后页*/
.sk_pageR {
	width: 16px;
	height: 17px;
	margin: 5px 0px 0px 0px;
	float: left;
}

.sk_pageR a {
	display: block;
	background: url(../../images/framework/file_botton_f.gif) left top
		no-repeat;
	width: 16px;
	height: 17px;
	background-position: -16px 0px;
}

.sk_pageR a span {
	display: none;
}

.sk_pageR a:hover {
	background-position: -16px -17px;
}

.sk_pageR a:active {
	background-position: -16px -34px;
}
/*上一页*/
.sk_pageup {
	width: 46px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
}

.sk_pageup a {
	display: block;
	background: url(../../images/framework/file_botton_page.gif) left top
		no-repeat;
	width: 46px;
	height: 17px;
}

.sk_pageup a span {
	display: none;
}

.sk_pageup a:hover {
	background-position: 0px -17px;
}

.sk_pageup a:active {
	background-position: 0px -34px;
}
/*下一页*/
.sk_pagedown {
	width: 46px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
}

.sk_pagedown a {
	display: block;
	background: url(../../images/framework/file_botton_page.gif) left top
		no-repeat;
	width: 46px;
	height: 17px;
	background-position: -46px 0px;
}

.sk_pagedown a span {
	display: none;
}

.sk_pagedown a:hover {
	background-position: -46px -17px;
}

.sk_pagedown a:active {
	background-position: -46px -34px;
}
/*页码*/
.sk_pagelist {
	width: 16px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
	text-align: center;
}

.sk_pagelist a {
	display: block;
	background: url(../../images/framework/file_botton_pagelist.gif) left
		top no-repeat;
	width: 16px;
	height: 17px;
}

.sk_pagelist a span {
	display: none;
}

.sk_pagelist a:hover {
	background-position: 0px -17px;
}

.sk_pagelist a:active {
	background-position: 0px -34px;
}
/*分页描述*/
.sk_pagelist_text {
	height: 17px;
	margin: 5px 5px 0px 2px;
	float: left;
}
/*不可点击状态*/
.sk_pageL2 {
	width: 16px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
	background: url(../../images/framework/file_botton_f.gif) left top
		no-repeat;
	background-position: 0px -0px;
}

.sk_pageup2 {
	width: 46px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
	background: url(../../images/framework/file_botton_page.gif) left top
		no-repeat;;
	background-position: 0px -34px;
}

.sk_pageR2 {
	width: 16px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
	background: url(../../images/framework/file_botton_f.gif) left top
		no-repeat;
	background-position: -16px -0px;
}

.sk_pagedown2 {
	width: 46px;
	height: 17px;
	margin: 5px 5px 0px 0px;
	float: left;
	background: url(../../images/framework/file_botton_page.gif) left top
		no-repeat;;
	background-position: -46px -34px;
}

.sk_pagelist_now {
	width: 16px;
	height: 17px;
	text-align: center;
	margin: 5px 5px 0px 0px;
	float: left;
	background: left top no-repeat;
	background-position: 0px -17px;
}
/* ------ 分页样式(结束) ------------ */
</style>
</head>
<body>


	<DIV class=bbs_column2 style="margin-left: 12px">
		<H1>当前位置：${routeStr}</H1>

		<UL>
			<!--子版块-->
			<LI style="padding-left: 2px; width: 100%">
				<table width="703" border="0" align="left" cellpadding="0"
					cellspacing="0" class="">
					<tr>
						<td width="518" valign="top">
							<table width="100%" height="354" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td height="324" align="left" valign="top">
										<table border="0" cellpadding="0" cellspacing="0" class=""
											style="width: 99%">
											<c:forEach items="${helpArticleList.result}"
												var="helpArticle" varStatus="vs">
												<tr>
													<td align="left" width="70%"
														background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
														<a
														href="${baseUrl}/portal/help/list.html?id=${helpArticle.channel.resourceid}">【${helpArticle.channel.channelName}】</a>
														<a
														href="${baseUrl}/portal/help/detail.html?id=${helpArticle.resourceid}">${helpArticle.title}</a>
													</td>
													<td align="right" width="30%"
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

									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table> <!--如果最后一个版块,需要加上以下这句-->
				<DIV style="CLEAR: both"></DIV>

			</LI>

		</UL>
	</DIV>

</body>
</html>