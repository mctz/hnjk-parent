<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>打印预览</title>
</head>
<body>
<gh:printView
        reportUrl="${baseUrl}/edu3/work/appraisingManage/printOrExport.html?branchSchoolid=${branchSchoolid}&gradeid=${gradeid}&classesid=${classesid}&yearInfoid=${yearInfoid}&type=${type}&auditStatus=${auditStatus}&studyNo=${studyNo}&resourceids=${resourceids}&avgScore=${avgScore}" />
</body>
</html>