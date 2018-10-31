<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学生评优信息管理</title>
    <style type="text/css">th,td{text-align: center}</style>
    <script type="text/javascript">
        $(document).ready(function(){
            appraisingQueryBegin();
        });

        //打开页面或者点击查询（即加载页面执行）
        function appraisingQueryBegin() {
            var defaultValue = "${condition['studentInfo.branchSchool.resourceid']}";
            var schoolId = "";
            var isBrschool = "${isBrschool}";
            if(isBrschool==true || isBrschool=="true"){
                schoolId = defaultValue;
            }
            var gradeId = "${condition['studentInfo.grade.resourceid']}";
            var classesId = "${condition['studentInfo.classes.resourceid']}";
            var selectIdsJson = "{unitId:'appraisingPage_school',gradeId:'appraisingPage_gradeid',classesId:'appraisingPage_classes'}";
            cascadeQuery("begin", defaultValue, schoolId, gradeId, "","", "", classesId, selectIdsJson);
        }

    </script>
</head>
<body>
<div class="page">
    <div class="pageHeader">
        <form id="appraisingManageForm" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/work/appraisingManage/list.html" method="post">
            <div class="searchBar">
                <c:if test="${not isStudent}">
                <ul class="searchContent">
                    <li class="custom-li"><label>教学点：</label>
                        <span sel-id="appraisingPage_school" sel-name="studentInfo.branchSchool.resourceid" sel-onchange="cadreInfoQueryUnit()"
                              sel-classs="flexselect"></span>
                    </li>
                    <li>
                        <label>年度：</label>
                        <gh:selectModel id="appraisingPage_yearInfoId" name="yearInfo.resourceid" bindValue="resourceid" displayValue="yearName"
                                        modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfo.resourceid']}" orderBy="yearName desc" style="width:120px"/>
                    </li>
                    <li><label>年级：</label>
                        <span sel-id="appraisingPage_gradeid" sel-name="studentInfo.grade.resourceid"
                            sel-onchange="examResultsManager2QueryGrade()" sel-style="width: 120px"></span>
                    </li>

                </ul>
                <ul class="searchContent">
                    <li class="custom-li"><label>班级：</label>
                        <span sel-id="appraisingPage_classes" sel-name="studentInfo.classes.resourceid" sel-classs="flexselect" ></span>
                    </li>
                    <li>
                        <label>学号：</label>
                        <input type="text" name="studyNo" value="${condition['studyNo']}" />
                    </li>
                    <li>
                        <label>姓名：</label>
                        <input type="text" name="studentName-like" value="${condition['studentName-like']}" />
                    </li>

                </ul>
                <ul class="searchContent">
                    <li>
                        <label>评优类型：</label>
                        <gh:select id="appraisingPage_type" name="type" dictionaryCode="Code.WorkManage.appraisingType" value="${condition['type']}" style="width:120px" />
                    </li>

                   <%-- <li>
                        <label>班干部：</label>
                        <gh:select name="isClassLeader" dictionaryCode="yesOrNo" value="${condition['isClassLeader']}" style="width:120px" />
                    </li>--%>
                    <li>
                        <label>审核状态：</label>
                        <gh:select name="auditStatus" dictionaryCode="CodeAuditStatus2" value="${condition['auditStatus']}" style="width:120px" />
                    </li>
                    <li>
                        <label>平均分：</label>
                        <input type="text" name="avgScore-begin" value="${condition['avgScore']}" />
                    </li>
                </ul>
                <ul class="searchContent">
                    <div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div>
                </ul>
                </c:if>
            </div>
        </form>
    </div>
    <div  class="pageContent">
        <gh:resAuth parentCode="RES_WORK_APPRAISING_LIST" pageType="list"></gh:resAuth>
        <table class="table" layouth="185" width="100%">
            <thead>
                <tr>
                    <th width="3%"><input type="checkbox" name="checkall" id="check_all_appraising" onclick="checkboxAll('#check_all_appraising','resourceid','#appraisingManageBody')"/></th>
                    <th width="8%">年度</th>
                    <th width="10%">学号</th>
                    <th width="8%">姓名</th>
                    <th width="12">班级</th>
                    <th width="8%">评优类型</th>
                    <th width="8%">成绩平均分</th>
                    <th width="10%">统考课程情况</th>
                    <th width="8%">是否班干部</th>
                    <th width="8%">审核状态</th>
                    <th width="10%">备注</th>
                </tr>
            </thead>
            <tbody id="appraisingManageBody">
            <c:forEach items="${pageResult.result}" var="temp" varStatus="vs">
                <tr>
                    <td><input type="checkbox" name="resourceid" value="${temp.resourceid }" autocomplete="off" auditStatus="${temp.auditStatus}" /></td>
                    <td title="${temp.yearInfo.yearName }">${temp.yearInfo.yearName }</td>
                    <td title="${temp.studyNo}">${temp.studyNo}</td>
                    <td title="${temp.studentName}">${temp.studentName}</td>
                    <td title="${temp.studentInfo.classes.classname}">${temp.studentInfo.classes.classname}</td>
                    <td title="${temp.type}">${ghfn:dictCode2Val('Code.WorkManage.appraisingType',temp.type ) }</td>
                    <td title="${temp.avgScore}">${temp.avgScore}</td>
                    <td title="${temp.courseCondition}">${temp.courseCondition}</td>
                    <td title="${temp.isClassLeader}">${temp.isClassLeader }</td>
                    <td title="${temp.auditStatus}">${ghfn:dictCode2Val('CodeAuditStatus2',temp.auditStatus ) }</td>
                    <td title="${temp.memo}">${temp.memo}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${not isStudent}">
            <gh:page page="${pageResult}" goPageUrl="${baseUrl }/edu3/work/appraisingManage/list.html" pageType="sys" condition="${condition}"/>
        </c:if>
    </div>
</div>
<script type="text/javascript">

    //新增
    function addAppraising() {
        $.pdialog.open('${baseUrl}/edu3/work/appraisingManage/input.html','RES_WORK_APPRAISING_ADD','评优申请',{height:600, width:800});
    }

    //编辑
    function editAppraising() {
        var url = "${baseUrl}/edu3/work/appraisingManage/input.html";
        if(isCheckOnlyone('resourceid','#appraisingManageBody')){
            var checekObj = $("#appraisingManageBody input[name='resourceid']:checked")
            if (checekObj.attr("auditStatus") != 'W' && checekObj.attr("auditStatus") != '') {
                alertMsg.warn("该记录已经审核，不允许编辑！");
                return false;
            }
            $.pdialog.open(url+'?resourceid='+$("#appraisingManageBody input[name='resourceid']:checked").val(),'RES_WORK_APPRAISING_INPUT','编辑学生评优信息',{mask:true,height:600, width:800});
        }
    }

    //审核 operate：pass、notpass、recheck、delete
    function operateAppraising(operate){
        var appraisingIds = [];
        var message = "";
        $("#appraisingManageBody input[name='resourceid']:checked").each(function(){
            var checekObj = $(this);
            if (operate != 'recheck') {
                if (checekObj.attr("auditStatus") != "W" && checekObj.attr("auditStatus") != '') {
                    message = "请选择待审核状态的记录！";
                }
            } else {
                if (checekObj.attr("auditStatus") != "R") {
                    message = "请选择待复审状态的记录！";
                }
            }
            appraisingIds.push(checekObj.val());
        });
        if(message!=""){
            alertMsg.warn(message);
            return false;
        }
        var info = "您确定要按照查询条件进行操作吗？";
        if(appraisingIds.length>0){
            info = "一共选择了"+appraisingIds.length+"个记录，您确定要进行下一步操作吗？";
        }

        var url = "${baseUrl}/edu3/work/appraisingManage/operate.html"+getUrlByParam()+"&operatingType="+operate;
        alertMsg.confirm(info, {
            okCall:function(){
                $.ajax({
                    type:'POST',
                    url:url,
                    data:{},
                    dataType:"json",
                    cache: false,
                    error: DWZ.ajaxError,
                    success: function(resultData){
                        var success  = resultData['success'];
                        var msg      = resultData['msg'];
                        if(success==false){
                            alertMsg.warn(msg);
                        }else{
                            alertMsg.info(msg);
                            navTab.reload($("#appraisingManageForm").attr("action"), $("#appraisingManageForm").serializeArray());
                        }
                    }
                });
            }
        })
    }

    //导出数据
    function exportAppraising() {
        var yearid = $("#appraisingPage_yearInfoId").val();
        var gradeid = $("#appraisingPage_gradeid").val();
        var type = $("#appraisingPage_type").val();
        if (yearid == '' || gradeid == '') {
            alertMsg.warn("请选择年度和年级！");
            return false;
        }
        if (type == '') {
            alertMsg.warn("请选择评优类型！");
            return false;
        }
        var url = "${baseUrl}/edu3/work/appraisingManage/export.html"+getUrlByParam();
        downloadFileByIframe(url, "exportAppraising");
    }

    //打印数据
    function printAppraising() {
        var yearid = $("#appraisingPage_yearInfoId").val();
        var type = $("#appraisingPage_type").val();
        var printUrl = "${baseUrl}/edu3/work/appraisingManage/printView.html";
        printUrl += renameUrlParam(getUrlByParam());
        if (yearid == '') {
            alertMsg.warn("请选择年度！");
            return false;
        }
        if (type == '') {
            alertMsg.warn("请选择评优类型！");
            return false;
        }
        $.pdialog.open(printUrl,'RES_WORK_CADREINFO_PRINT','打印预览',{mask:true,height:600, width:800});
    }

    function getUrlByParam() {
        var url = "";
        if(isChecked('resourceid','#appraisingManageBody')){
            var resIds = [];
            $("#appraisingManageBody input[name='resourceid']:checked").each(function(){
                resIds.push($(this).val());
            });
            url += "?resourceids="+resIds.toString()+"&"+$("#appraisingManageForm").serialize();;
        }else{
            url += "?"+$("#appraisingManageForm").serialize();
        }
        return url;
    }

    function renameUrlParam(url) {
        var url_new = "";
        url_new = url.replace("studentInfo.branchSchool.resourceid","branchSchoolid")
            .replace("yearInfo.resourceid","yearInfoid")
            .replace("studentInfo.grade.resourceid","gradeid")
            .replace("studentInfo.classes.resourceid","classesid")
            .replace("studyNo-like","studyNo")
            .replace("studentName-like","studentName")
            .replace("avgScore-begin","avgScore");
        return url_new;
    }

    // 选择教学点
    function appraisingQueryUnit() {
        var defaultValue = $("#appraisingPage_school").val();
        var selectIdsJson = "{gradeId:'appraisingPage_gradeid',classesId:'appraisingPage_classes'}";
        cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
    }

    // 选择年级
    function appraisingQueryGrade() {
        var defaultValue = $("#appraisingPage_school").val();
        var gradeId = $("#appraisingPage_gradeid").val();
        var selectIdsJson = "{classesId:'appraisingPage_classes'}";
        cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
    }

</script>
</body>
</html>