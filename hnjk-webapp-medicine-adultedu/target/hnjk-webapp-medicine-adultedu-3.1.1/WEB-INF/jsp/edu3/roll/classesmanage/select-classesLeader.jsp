<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <script type="text/javascript">

        function setClassesLeader(){
            var classesid = "${classesid}";
            if(!isChecked('resourceid',"#classLeaderSearchBody")){
                alertMsg.warn('请选择一条记录。');
                return false;
            }
            var classesLeaderId = [];
            var classesLeaderName = [];
            $("#classLeaderSearchBody input[name='resourceid']:checked").each(function(){
                classesLeaderId.push($(this).val());
                classesLeaderName.push($(this).attr("studentName"));
            });
            var url = "${baseUrl}/edu3/roll/classes/save.html";
            $.ajax({
                type:"post",
                url:url,
                data:{"classIds":classesid,"classesLeaderId":classesLeaderId.toString(),"classesLeaderName":classesLeaderName.toString()},
                dataType:"json",
                success:function(data){
                    if(data.statusCode==200){
                        alertMsg.correct(data.message);
                        navTab.reload("${baseUrl}/edu3/roll/classes/list.html", $("#classesListForm").serializeArray(), "RES_ROLL_CLASSES");
                    }else{
                        alertMsg.error(data.message);
                    }
                }
            });
        }
    </script>
</head>

<body>
<div class="page">
    <div class="pageHeader">
        <div class="pageHeader">
            <form id="masterSearchForm" onsubmit="return dialogSearch(this);"
                  action="${baseUrl}/edu3/roll/classes/select-classesLeader.html"
                  method="post">
                <input type="hidden" name="classesid" value="${classesid }">
                <div class="searchBar">
                    <ul class="searchContent">
                        <li><label>姓名：</label><input type="text" name="studentName"
                                                     value="${condition['studentName']}" /></li>
                        <li><label>学号：</label><input type="text"
                                                       name="studyNo" value="${condition['studyNo']}" /></li>
                    </ul>
                    <div class="subBar">
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查 询</button>
                            </div>
                        </div>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="button" onclick="setClassesLeader()">确 定</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="pageContent">
            <table class="table" layouth="168">
                <thead>
                <tr>
                    <th width="10%">
                        <input type="checkbox" name="checkall" id="check_all_classLeader" onclick="checkboxAll('#check_all_classLeader','resourceid','#classLeaderSearchBody')"/>
                    </th>
                    <th width="15%">学号</th>
                    <th width="15%">姓名</th>
                    <th width="10%">性别</th>
                    <th width="15%">联系电话</th>

                </tr>
                </thead>
                <tbody id="classLeaderSearchBody">
                <c:forEach items="${studentPage.result}" var="t" varStatus="vs">
                    <tr>
                        <td>
                            <input type="checkbox" name="resourceid" value="${t.resourceid }" autocomplete="off" studentName="${t.studentName}"
                                    <c:if test="${fn:contains(idsN,t.resourceid)}">checked="checked"</c:if>
                            />
                        </td>
                        <td>${t.studyNo}</td>
                        <td>${t.studentName }</td>
                        <td>${ghfn:dictCode2Val('CodeSex',t.studentBaseInfo.gender) }</td>
                        <td>${t.studentBaseInfo.mobile}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <gh:page page="${studentPage}"
                     goPageUrl="${baseUrl }/edu3/roll/classes/select-classesLeader.html"
                     pageType="sys" targetType="dialog" condition="${condition}" />
        </div>
    </div>
</body>