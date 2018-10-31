<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}-<decorator:title
		default="学生服务" />
</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<gh:loadCom components="netedu3-defaultcss,jquery" />
<decorator:head />
</head>
<!-- top header -->
<body>
	<!-- body content -->
	<div id="main_body">
		<decorator:body />
	</div>
</body>
</html>
