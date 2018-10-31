<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
</head>
<body>
	<script type="text/javascript">
		
	</script>
	<gh:printView
		reportUrl="${baseUrl}/edu3/finance/studentPayment/studentPayment-refund-printList.html?brSchool=${condition.brSchool}&majorid=${condition.majorid}&classicid=${condition.classicid}&name=${condition.name}&studyNo=${condition.studyNo}&gradeid=${condition.gradeid}&classesId=${condition.classesId}&studentStatus=${condition.studentStatus}&payType=${condition.payType}&beginDate=${condition.beginDate}&endDate=${condition.endDate}&total=${condition.total}&receiptNumber_end=${condition.receiptNumber_end}&receiptNumber_begin=${condition.receiptNumber_begin}&detailIds=${condition.detailIds}&drawer=${condition.drawer}&year=${condition.year}&beginPrintDate=${condition.beginPrintDate}&examCertificateNo=${condition.examCertificateNo}&endPrintDate=${condition.endPrintDate}&paymentMethod=${condition.paymentMethod}&isPrint=${condition.isPrint}" />
</body>
</html>