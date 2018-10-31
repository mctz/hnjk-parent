<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>查看教学计划成绩录入情况</title>
</head>
<body>
<div class="page">
    <div class="pageContent">
        <table class="table" border="1">
            <thead>
            <tr>
                <th width="10%">教学站</th>
                <th width="4%">年级</th>
                <th width="5%">层次</th>
                <th width="4%">学习形式</th>
                <th width="10%">专业</th>
                <th width="10%">教学计划</th>
                <th width="10%">班级名称</th>
                <th width="4%">人数</th>
                <th width="5%">班主任</th>
                <th width="6%">开课/总课程</th>
                <th width="7%">有登分老师/开课课程</th>
                <th width="7%">已排课/开课课程</th>
                <th width="8%">已录成绩/开课课程</th>
            </tr>
            </thead>
            <tbody id="teachingPlanExamresultBody">
            <c:forEach items="${tpelist.result}" var="tpe" varStatus="vs">
                <tr>
                    <td style="text-align: center; vertical-align: middle;">${tpe.unitname}</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.gradename}</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.classicname}</td>
                    <td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',tpe.teachingtype)}</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.majorname }</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.trainingtarget}</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.classesname}</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.studentNum }</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.classesmaster }</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.openCourseNum }&nbsp;/&nbsp;${tpe.totalCourse }</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.hasTeacherNum }&nbsp;/&nbsp;${tpe.openCourseNum }</td>
                    <td style="text-align: center; vertical-align: middle;">${tpe.arrangedCourseNum }&nbsp;/&nbsp;${tpe.openCourseNum }</td>
                    <td style="text-align: center; vertical-align: middle;">
                        <c:if test="${tpe.hasResultNum < tpe.openCourseNum }">
                            <span style="color: red; line-height: 21px;">
                        </c:if> ${tpe.hasResultNum }&nbsp;/&nbsp;${tpe.openCourseNum }
                         <c:if test="${tpe.hasResultNum < tpe.openCourseNum }">
                            </span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>