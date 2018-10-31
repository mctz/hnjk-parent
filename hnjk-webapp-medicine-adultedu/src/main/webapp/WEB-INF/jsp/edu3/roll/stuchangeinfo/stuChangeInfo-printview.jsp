<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印预览</title>
<script type="text/javascript">
 	$(document).ready(function(){
		var errorMsg = "${msg}";

		if(null!=errorMsg && ""!=errorMsg){
			$("#courseOrderdetailsPrintViewBody").html("");
			alertMsg.warn(errorMsg);
			$.pdialog.closeCurrent();
		}
	});
  </script>
</head>
<body>
	<gh:printView
		reportUrl="${baseUrl}/edu3/register/stuchangeinfo/excel/printStuChangeInfoList.html?stuId=${stuId }&branchSchool=${branchSchool }&major=${major }&classes=${classes }&gradeid=${gradeid }&stuStatus=${stuStatus }&classic=${classic }&learningStyle=${learningStyle }&stuNum=${stuNum }&stuName=${stuName }&stuChange=${stuChange }&changeProperty=${changeProperty }&finalAuditStatus=${finalAuditStatus }&flag=${flag }&applicationDateb=${applicationDateb }&applicationDatee=${applicationDatee }&auditDateb=${auditDateb }&auditDatee=${auditDatee }" />
	<!--  <gh:printView reportUrl="${baseUrl}/edu3/register/stuchangeinfo/excel/printStuChangeInfoList.html?stuName=${stuName }&branchSchool=${branchSchool }&major=${major }&classic=${classic }&unit=${unit}&changeType=${changeType }&grade=${grade }&stuNum=${stuNum }&finalAuditStatus=${finalAuditStatus }&mobile=${mobile }&classes=${classes }&teachingType=${teachingType }&ounit=${ounit }&omajor=${omajor }&oclasses=${oclasses }&oteachType=${oteachType }&oclassic=${oclassic }&stuId=${stuId }"/>	 -->
</body>
<script type="text/javascript">
  </script>
</html>
