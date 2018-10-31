<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}-${orgUnit.unitName } -
	${channel.channelName }</title>
<gh:loadCom components="jquery" />
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/portal-default-2.css" />

</head>
<body>
	<table width="1003" border="0" align="center" cellpadding="0"
		cellspacing="0" class="kuang">
		<tr>
			<td height="137" colspan="2" align="right" valign="bottom"
				background="${baseUrl}/style/default/portal-images-2/RtopBr.jpg">
				<div
					style="padding-bottom: 70px; text-align: left; margin-left: 420px; font-size: 14pt">
					<b>${orgUnit.unitName }</b>
				</div>
				<table width="255" border="0" cellpadding="0" cellspacing="0"
					class="btmore2">
					<tr>
						<td width="328" align="right"><span style="color: #fff">今天是
								${todayStr }</span></td>
						<td width="10">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td height="29" colspan="2">
				<!-- 头部 --> <gh:portal channelList="${channelList }"
					channelType="index-header" />
			</td>
		</tr>
		<tr>
			<td width="185" height="354" valign="top">
				<!-- 左侧 -->
				<table width="185" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="328" align="center" valign="top"
							background="${baseUrl }/style/default/portal-images-2/Rtslm.png">
							<table width="147" border="0" cellpadding="0" cellspacing="0"
								class="STYLE5">
								<tr>
									<td style="height: 24px"></td>
								</tr>
								<tr>
									<td style="padding-top: 6px">&nbsp;<span
										style="margin-left: 30px; margin-top: 22px; font-size: 11pt">${channel.parent.channelName }</span></td>
								</tr>
								<tr>
									<td style="height: 16px"></td>
								</tr>
								<!-- 遍历子栏目 -->
								<c:if test="${not empty channel.parent }">
									<c:forEach items="${channel.parent.children}" var="chan"
										varStatus="vs">
										<c:if test="${chan.channelPosition lt 'NAV' }">
											<tr>
												<td height="22" align="left" valign="bottom"><a
													href="${baseUrl }/portal/site/channel/show.html?id=${chan.resourceid }">
														<c:choose>
															<c:when test="${channel.resourceid eq chan.resourceid }">
																<span class="STYLE4">${chan.channelName }</span>
															</c:when>
															<c:otherwise>
	            ${chan.channelName }
	            </c:otherwise>
														</c:choose>

												</a></td>
											</tr>
										</c:if>
									</c:forEach>
								</c:if>
							</table>
						</td>
					</tr>
					<tr>
						<td><a href="#"></a><a href="#"></a><a href="#"></a><a
							href="#"></a></td>
					</tr>
				</table>

			</td>
			<td width="818" valign="top">
				<!-- 正文 -->
				<table width="100%" height="354" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td valign="top"><table width="816" height="30" border="0"
								cellpadding="0" cellspacing="0">
								<tr>
									<td height="30"><span class="STYLE4">当前位置：<a
											href="${baseUrl }/portal/site/brschool/school.html?id=${orgUnit.resourceid }">${orgUnit.unitName }</a>
											>> <a href="#">${channel.channelName}</a></span></td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td height="324" align="left" valign="top"><table border="0"
								cellpadding="0" cellspacing="0" class="STYLE2"
								style="width: 99%">
								<c:forEach items="${articleList.result}" var="article"
									varStatus="vs">
									<tr>
										<td align="left" width="80%"
											background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
											<a
											href="${baseUrl}/portal/site/channel/show.html?id=${article.channel.resourceid}&uid=${ article.orgUnit.resourceid}">【${article.channel.channelName}】</a>
											<a
											href="${baseUrl}/portal/site/article/show.html?id=${article.resourceid}&uid=${ article.orgUnit.resourceid}">${article.title}</a>
										</td>
										<td align="right" width="20%"
											background="${baseUrl }/style/default/portal-images-2/Rdi.gif">
											<fmt:formatDate value="${article.fillinDate }"
												pattern="yyyy-MM-dd" />
										</td>
									</tr>
								</c:forEach>
								<tr>
									<td align="center">
										<form action="" method="post">
											<gh:page page="${articleList}" pageType="other"
												condition="${condition }" />
										</form>
									</td>
								</tr>
							</table></td>
					</tr>
				</table>


			</td>
		</tr>

		<!-- 底部 -->
		<tr>
			<td height="22" colspan="2" align="center" bgcolor="#1C627E"><span
				style="color: #fff">粤ICP备号 Copyright 2001-2010
					${school}${schoolConnectName}All Rights Reserved <gh:version />
			</span></td>
		</tr>
		<tr>
			<td height="21" colspan="2" align="center">&nbsp;</td>
		</tr>
	</table>
</body>
</html>
