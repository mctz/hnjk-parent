<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style>
        .type {
            text-align: center;
            line-height: 28px;
        }
    </style>
</head>
<body>

<div class="page" >

    <table class="table" border="1">
        <thead>
            <tr>
                <th width="20%">标题</th>
                <th width="8%">申请人</th>
                <th width="12%">申请时间</th>
                <th width="30%">修改内容</th>
                <%--<th width="5%">是否阅读</th>--%>
                <th width="5%">审核通过</th>
            </tr>
        </thead>
        <tbody id="confirmMSGBody">
            <c:forEach items="${messagePage.result}" var="message" varStatus="s">
                <tr>
                    <td>${message.msgTitle }</td>
                    <td><a href="#" onclick="viewStudentInfo('${message.studyNo}')" title="点击查看"></a>${message.senderName }</td>
                    <td><fmt:formatDate value="${message.sendTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <c:if test="${fn:endsWith(message.content,'的学生')}">
                        <td></td>
                    </c:if>
                    <c:if test="${ not fn:endsWith(message.content,'的学生')}">
                        <td>${fn:replace(fn:replace(message.content,"green","red"),"<br/>"," ") }</td>
                    </c:if>
                        <%--<td>${ghfn:dictCode2Val('yesOrNo',message.isReply) }</td>--%>
                    <td <c:if test="${message.status == 'Y'}">style='color:blue'</c:if>
                        <c:if test="${message.status != 'Y'}">style='color:red'</c:if>
                    >${ghfn:dictCode2Val('yesOrNo',message.status) }</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>