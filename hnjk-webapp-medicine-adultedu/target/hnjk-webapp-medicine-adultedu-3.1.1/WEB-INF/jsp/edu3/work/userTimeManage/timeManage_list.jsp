<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学生工作时间管理</title>
    <style type="text/css">th,td{text-align: center}</style>
</head>
<body>
<div class="page">
    <div class="pageHeader">
        <form id="userTimeManage" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/work/userTimeManage/list.html" method="post">
            <div class="searchBar">
                <ul class="searchContent">
                    <li>
                        <label>年度：</label>
                        <gh:selectModel id="uTimeManage_yearInfoId" name="yearInfo.resourceid" bindValue="resourceid" displayValue="yearName"
                                        modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfo.resourceid']}" orderBy="yearName desc" style="width:125px"/>
                    </li>
                    <li>
                        <label>学期：</label>
                        <gh:select name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:125px" />
                    </li>
                    <li>
                        <label>教学点：</label>
                        <c:if test="${isBrschool }"><input type="hidden" name="unit.resourceid" value="${condition['unit.resourceid']}" >
                            <input type="text" value="${brschoolName }" readonly="readonly" style="width: 125px"></c:if>
                        <c:if test="${!isBrschool }">
                            <gh:selectModel name="unit.resourceid" bindValue="resourceid" displayValue="unitName"
                                            modelClass="com.hnjk.security.model.OrgUnit" value="${condition['unit.resourceid']}" style="width:125px"/></c:if>
                    </li>
                </ul>
                <ul class="searchContent">
                    <li>
                        <label>活动类型：</label>
                        <gh:select name="workType" value="${condition['workType']}" dictionaryCode="Code.WorkManage.workType" style="width:125px" />
                    </li>
                    <div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div>
                </ul>

            </div>
        </form>
    </div>
    <div  class="pageContent">
        <gh:resAuth parentCode="RES_WORK_TIMEMANAGE_LIST" pageType="list"></gh:resAuth>
        <table class="table" layouth="135" width="100%">
            <thead>
            <tr>
                <th width="3%"><input type="checkbox" name="checkall" id="check_all_uTimeManage" onclick="checkboxAll('#check_all_uTimeManage','resourceid','#userTimeManageBody')"/></th>
                <th width="10%">年度</th>
                <th width="10%">学期</th>
                <th width="20%">教学点</th>
                <th width="10%">活动类型</th>
                <th width="20%">时间</th>
                <th width="15%">竞选时间</th>
            </tr>
            </thead>
            <tbody id="userTimeManageBody">
            <c:forEach items="${pageResult.result}" var="tm" varStatus="vs">
                <tr>
                    <td><input type="checkbox" name="resourceid" value="${tm.resourceid }" autocomplete="off" /></td>
                    <td>${tm.yearInfo.yearName }</td>
                    <td>${ghfn:dictCode2Val('CodeTerm',tm.term ) }</td>
                    <td>${tm.unit.unitName}</td>
                    <td>${ghfn:dictCode2Val('Code.WorkManage.workType',tm.workType ) }</td>
                    <td>
                        <fmt:formatDate value="${tm.startTime }" pattern="yyyy-MM-dd"/>
                        至 <fmt:formatDate value="${tm.endTime }" pattern="yyyy-MM-dd"/>
                    </td>
                    <td><fmt:formatDate value="${tm.joinTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <gh:page page="${pageResult}" goPageUrl="${baseUrl }/edu3/work/userTimeManage/list.html" pageType="sys" condition="${condition}"/>
    </div>
</div>
<script type="text/javascript">
    //新增
    function addTimeManage(){
        navTab.openTab('RES_WORK_TIMEMANAGE_INPUT', '${baseUrl}/edu3/work/userTimeManage/input.html', '新增工作时间管理');
    }
    //修改
    function editTimeManage(){
        var url = "${baseUrl}/edu3/work/userTimeManage/input.html";
        if(isCheckOnlyone('resourceid','#userTimeManageBody')){
            navTab.openTab('RES_WORK_TIMEMANAGE_INPUT', url+'?resourceid='+$("#userTimeManageBody input[name='resourceid']:checked").val(), '编辑工作时间管理');
        }
    }
    //删除
    function deleteTimeManage(){
        pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/work/userTimeManage/remove.html","#userTimeManageBody");
    }
</script>
</body>
</html>