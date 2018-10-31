<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生收费管理</title>
<script type="text/javascript">
	var handle = 0;
	var pos;
	function getPosObject(){
		pos = document.getElementById("posxxxxx");
		handle = pos.posInit2();
		alert(handle);
	
	}
</script>
</head>
<body>
	<object classid="clsid:444B92AA-28A9-4D88-9BDF-7554779D5966"
		id="posxxxxx" codeBase="${baseUrl}/ocx/pos.CAB#version=1,0,0,1"
		width="0" height="0"> </object>

	<input type="button" onclick="getPosObject()" value="测试" />
</body>
</html>