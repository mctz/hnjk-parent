<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印演示</title>
</head>
<body>
	<script type="text/javascript">
  function _demo_printer(){	 
	var url = "${baseUrl}/edu3/system/demo/webplugin/print/view.html";
	$.pdialog.open(url,'RES_DEMO_PRINTER_VIEW','打印预览',{width:800,height:600});
  }
  </script>
	<h2 class="contentTitle">打印预览</h2>
	<div class="page">
		<div class="pageContent">
			<div class="buttonActive">
				<div class="buttonContent">
					<button id="plansubmit" type="button" onclick="_demo_printer();">打印</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>