<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- 在线帮助</title>
<gh:loadCom components="bbs-defaultcss,jquery,ztree" />
<style type="text/css">
.blink {
	color: #F00;
	background-color: #FFFF00;
}
<!--
字体
 
-->
</style>
</head>
<body>


	<DIV class=bbs_column2 style="margin-left: 12px">
		<H1>${routeStr}</H1>
	</DIV>
	<table border="0" width="90%" cellpadding="0" cellspacing="0">
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="16" align="center" valign="top"
				style="font-size: 11pt; text-align: center;"><strong>${helpArticle.title}</strong></td>
		</tr>
		<tr>
			<td height="12" align="center" valign="top">&nbsp;</td>
		</tr>
		<tr>
			<td height="215" valign="top"
				style="font-size: 14px; font-weight: normal; line-height: 26px;">
				<c:choose>
					<c:when test="${null!=tags}">${helpArticle_content}</c:when>
					<c:otherwise>${helpArticle.content}</c:otherwise>
				</c:choose>


			</td>
		</tr>
		<tr>
			<td height="12" align="center" valign="top">&nbsp;</td>
		</tr>
		<tr>
			<td height="26" valign="top"><c:if
					test="${not empty helpArticle.attachs }">
					<c:forEach items="${helpArticle.attachs }" var="attach"
						varStatus="vs">
					                	 		附件${vs.index+1}： <a
							href="${baseUrl}/portal/site/article/download.html?id=${attach.resourceid}">${attach.attName}</a>
						<br />
					</c:forEach>
				</c:if></td>
		</tr>
		<tr>
			<td height="16" align="center" class="detail_title2">以上内容是否解决了您的问题？
				<input type="radio" name="userfacebook" value="1" checked="true" />是
				<input type="radio" name="userfacebook" value="-1" />否 <input
				type="button" value="提交" onclick="ajaxUpdateArticleResolveCount()" />
			</td>
		</tr>
		<tr>
			<td height="16" align="center" class="detail_title2"
				style="text-align: right"><a href="javascript:window.print();">打印本页</a>
			</td>
		</tr>
		<tr>
			<td height="12" align="center" valign="top">&nbsp;</td>
		</tr>
	</table>


</body>
</html>