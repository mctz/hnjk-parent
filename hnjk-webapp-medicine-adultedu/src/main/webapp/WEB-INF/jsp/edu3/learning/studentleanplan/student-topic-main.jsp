<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的主题</title>
</head>
<body>
	<div class="page">
		<div class="pageContent" layouth="0">
			<div class="tabs" eventtype="click">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li><a
								href="${baseUrl}/edu3/framework/student/topic/view.html?currentIndex=0"
								class="j-ajax"><span>随堂问答</span></a></li>
							<li><a
								href="${baseUrl}/edu3/framework/student/topic/view.html?currentIndex=1"
								class="j-ajax"><span>小组话题</span></a></li>
							<li><a
								href="${baseUrl}/edu3/framework/student/topic/view.html?currentIndex=2"
								class="j-ajax"><span>课程论坛主题</span></a></li>
							<li><a
								href="${baseUrl}/edu3/framework/student/topic/view.html?currentIndex=3"
								class="j-ajax"><span>学院论坛主题</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<!-- 1 -->
					<div id="mybbstopic0"></div>
					<!-- 2 -->
					<div id="mybbstopic1"></div>
					<!-- 3 -->
					<div id="mybbstopic2"></div>
					<!-- 4 -->
					<div id="mybbstopic3"></div>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>