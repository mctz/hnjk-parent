<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		
	});
  </script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/finance/studentPaymentDetails/chargeSummary/print.html?brSchool=${brSchool}&gradeid=${gradeid }&classicid=${classicid }&majorid=${majorid }&classesId=${classesId }&studentStatus=${studentStatus }&paymentMethod=${paymentMethod }&sumPaidcount=${sumPaidcount }&sumStuCount=${sumStuCount }&sumBKAmount=${sumBKAmount }&sumZKAmount=${sumZKAmount }&sumAmount=${sumAmount }&chargeBack=${chargeBack }&currentFee=${currentFee }&beginDate=${beginDate }&endDate=${endDate }&receiptNumber_begin=${receiptNumber_begin }&receiptNumber_end=${receiptNumber_end }" />
</body>
</html>