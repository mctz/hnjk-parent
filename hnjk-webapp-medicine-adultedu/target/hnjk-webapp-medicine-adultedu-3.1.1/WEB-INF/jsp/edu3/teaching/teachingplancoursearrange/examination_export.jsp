<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>排考管理</title>
</head>
<body>
    <table class="table" border="1">
        <thead>
        <tr>
            <th width="15%">教学点</th>
            <th width="15%">班级</th>
            <th width="10%">考试科目</th>
            <th width="5%">考试类别</th>
            <th width="8%">日期</th>
            <th width="10%">时间</th>
            <th width="5%">人数</th>
            <th width="15%">地点</th>
            <th width="10%">课室</th>
            <th width="5%">监考</th>
            <th width="5%">排考人</th>
        </tr>
        </thead>
        <tbody id="examinationBody">
        <c:forEach items="${examinationPage.result }" var="exam"
                   varStatus="vs">
            <tr>
                <td>${exam.classes.brSchool.unitName }</td>
                <td>${exam.classes.classname}</td>
                <td>${exam.course.courseName }</td>
                <td>${ghfn:dictCode2Val('CodeExamClassType',exam.teachingPlanCourse.examClassType) }</td>
                <td>${exam.startExamDate} 至 ${exam.endExamDate}</td>
                <td> <fmt:formatDate value="${exam.startTimePeriod }" pattern="HH:mm" /> 至 <fmt:formatDate
                        value="${exam.endTimePeriod }" pattern="HH:mm" />
                </td>
                <td>${exam.studentNum}</td>
                <td>${exam.location}</td>
                <td>${exam.classroom}</td>
                <td>${exam.teacher}</td>
                <td>${exam.operatorName}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>