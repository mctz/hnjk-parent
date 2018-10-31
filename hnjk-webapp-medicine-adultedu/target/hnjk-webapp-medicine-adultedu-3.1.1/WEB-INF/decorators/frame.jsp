<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>广东学苑基础平台 - <decorator:title default="我的工作台" /></title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<gh:loadCom components="framworkcss" />
<decorator:head />
</head>
<body>
	<!-- top header -->
	<div id="top_container">
		<h1>广东学苑基础平台V1.0</h1>
		<div class="top_data">
			欢迎您,
			<security:authentication property="name" />
		</div>
		<ul>
			<li><a href="#"><img
					src="${baseUrl }/images/framework/icon_change.gif">&nbsp;更换用户</a></li>
			<li><a href="#"><img
					src="${baseUrl }/images/framework/icon_bangzhu.gif">&nbsp;帮助</a></li>
			<li><a href="${baseUrl}/j_spring_security_logout"
				target="_parent"><img
					src="${baseUrl }/images/framework/icon_exit.gif" />&nbsp;注销</a></li>
		</ul>
	</div>
	<div id="wrapper">
		<!-- left menu -->
		<div id="menu_container">
			<gh:currentUserSystemMenu />
		</div>
		<!-- body content -->
		<div id="main_body">
			<decorator:body />
		</div>
	</div>
	<!-- bottom footer -->
	<div id="copyright">&copy; 广东学苑教育发展有限公司</div>
</body>
</html>