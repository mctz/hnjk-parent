<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>在学自测系统</title>
<gh:loadCom components="iframeAutoHeight" />
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/exercisebatch.html?courseId=${course.resourceid}">作业系统</a></li>
					<li><a
						href="${baseUrl }/resource/course/mocktest.html?courseId=${course.resourceid}">在线自测系统</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > 在学测试系统</div>
			<div class="clear"></div>
			<div id="content">
				<div class="stlx_tab">
					<c:forEach items="${courseMockTestList }" var="mockTest"
						varStatus="vs">
						<div
							<c:choose>
							<c:when test="${mockTest.resourceid eq courseMockTest.resourceid }">class="stlx_tab_over"</c:when>
							<c:otherwise>class="stlx_tab_out"</c:otherwise>
						</c:choose>>
							<a
								href="${baseUrl }/resource/course/mocktest.html?mocktestid=${mockTest.resourceid}">自测题${vs.index+1<10?'0':'' }${vs.index+1 }</a>
						</div>
					</c:forEach>
				</div>
				<div class="clear"></div>
				<c:if test="${not empty courseMockTest.resourceid }">
					<h2>${courseMockTest.mocktestName }</h2>
					<iframe id="_mockTestIframe" name="_mockTestIframe"
						src="${baseUrl }/resource/course/transfer.html?url=${courseMockTest.mateUrl }"
						scrolling="no" frameborder="0" width="100%" height="100%"></iframe>
					<script type="text/javascript">
			$("#_mockTestIframe").iframeAutoHeight();	 
			</script>
				</c:if>
			</div>
		</div>
		<!--end right-->
	</div>
</body>
</html>