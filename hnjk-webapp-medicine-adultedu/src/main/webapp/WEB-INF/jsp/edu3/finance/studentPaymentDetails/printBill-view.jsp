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
		reportUrl="${baseUrl}${url }?brSchool=${brSchool}&majorId=${majorId}&classicId=${classicId}&name=${name}&studyNo=${studyNo}&gradeId=${gradeId}&classesId=${classesId}&studentStatus=${studentStatus}&payType=${payType}&beginDate=${beginDate}&endDate=${endDate}&total=${total}&receiptNumber_end=${receiptNumber_end}&receiptNumber_begin=${receiptNumber_begin}&detailIds=${detailIds}&drawer=${drawer}&year=${year}&beginPrintDate=${beginPrintDate}&examCertificateNo=${examCertificateNo}&endPrintDate=${endPrintDate}&paymentMethod=${paymentMethod}&isPrint=${isPrint}" />
</body>
</html>