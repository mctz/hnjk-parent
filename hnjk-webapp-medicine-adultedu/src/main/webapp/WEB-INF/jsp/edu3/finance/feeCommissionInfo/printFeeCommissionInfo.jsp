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
	<gh:printView reportUrl="${baseUrl}/edu3/finance/feeCommissionInfo/print.html?brSchool=${brSchool}&operatedate=${operatedate }&flag=print&isBrschool=${isBrschool}&year=${year}&month=${month}&day=${day}&beginDate=${beginDate}&endDate=${endDate}" />
	<%--<gh:printView
		reportUrl="${baseUrl}/edu3/finance/feeCommissionInfo/print.html?brSchool=${brSchool}&operatedate=${operatedate }&flag=print&isBrschool=${isBrschool}&userBrSchool=${userBrSchool}&title=${title }&year=${year}&month=${month}&day=${day}&studentNum1=${studentNum1}&studentNum2=${studentNum2}&studentNum3=${studentNum3}&sumStudentFullNum=${sumStudentFullNum}&sumShouldFullFees=${sumShouldFullFees}&sumRealFullFees=${sumRealFullFees}&sumNotPayFullNum=${sumNotPayFullNum}&sumNotFullFees=${sumNotFullFees}&sumSchoolProportionPay=${sumSchoolProportionPay}&sumProportionPay=${sumProportionPay}
		&beginDate=${beginDate}&endDate=${endDate}&sumRealFullFees_tuition=${sumRealFullFees_tuition}&sumRealFullFees_others=${sumRealFullFees_others}&sumRoyaltyRatePay=${sumRoyaltyRatePay}&sumRoyaltyRate2Pay=${sumRoyaltyRate2Pay}&sumShouldReturnPay=${sumShouldReturnPay}&sumReserveRatioPay=${sumReserveRatioPay}&sumFirstReturnPay=${sumFirstReturnPay}&sumSecondReturnPay=${sumSecondReturnPay}&sumShouldReturnFees=${sumShouldReturnFees}" />--%>
</body>
</html>