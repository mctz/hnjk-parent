<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程信息</title>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyInfo"></div>
			<!-- <img src="${basePath }/style/default/resources/images/sub1_02.jpg" width="187" height="105" /> -->
			<div id="left_menu">
				<ul>
					<c:forEach items="${courseOverviewList }" var="v" varStatus="vs">
						<li><a
							href="${baseUrl }/resource/course/courseoverview.html?overviewid=${v.resourceid}">${ghfn:dictCode2Val('CodeCourseOverviewType',v.type) }</a></li>
					</c:forEach>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->
		<div id="right">
			<div class="position">当前位置：课程信息 >
				${ghfn:dictCode2Val('CodeCourseOverviewType',courseOverview.type) }</div>
			<br />
			<div>${courseOverview.content }</div>
		</div>
	</div>
</body>
</html>
