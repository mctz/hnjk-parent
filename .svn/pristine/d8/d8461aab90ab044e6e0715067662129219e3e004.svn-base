<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提示页面</title>
<gh:loadCom components="jquery" />
<script type="text/javascript"><%--
    jQuery(document).ready(function(){	
      var msg = "${massage}";
      jQuery("#back").click(function(){
			   window.parent.location.href= "${baseUrl}${backurl}";		
			});    
	 });    
 --%>
 	function goBack(){
 		 window.parent.location.href="${baseUrl}${backurl}";		
 	}
 </script>
</head>
<body>
	<div style="text-align: center">
		<c:if test="${results eq 'success'}">
			<img src="${baseUrl}/images/portal/bm_success.jpg" />
		</c:if>
		<c:if test="${results eq 'failure'}">
			<img src="${baseUrl}/images/portal/bm_failure.jpg" />
		</c:if>
		<c:if test="${not empty massage }">
			<script type="text/javascript">
		  var msg = "${massage }";
		   if(null!=msg && ""!= msg){ 
			   alert(msg);
		   }
		</script>
		</c:if>
	</div>
	<div style="text-align: center">
		<img src="${baseUrl}/images/portal/back-1.jpg" onclick="goBack();" />
	</div>
</body>
