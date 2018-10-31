<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>${channel.channelName }</title>
<gh:loadCom components="iframeAutoHeight" />
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/ref.html?channelid=${channel.resourceid}">${channel.channelName }
					</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > ${channel.channelName }</div>
			<div class="clear"></div>
			<div id="content">
				<c:if test="${not empty channel.channelHref }">
					<iframe id="_otherRefpageIframe" name="_otherRefpageIframe"
						src="${baseUrl }/resource/course/transfer.html?url=${channel.channelHref }"
						scrolling="no" frameborder="0" width="100%"></iframe>
				</c:if>
			</div>
		</div>
		<!--end right-->
	</div>
	<script type="text/javascript">
$("#_otherRefpageIframe").iframeAutoHeight();
</script>
</body>
</html>