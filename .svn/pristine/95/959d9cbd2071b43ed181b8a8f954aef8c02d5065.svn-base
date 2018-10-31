<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学生干部申请管理</title>
    <style type="text/css">th,td{text-align: center}</style>
    <script type="text/javascript">
        $(document).ready(function(){
            cadreApplyInfoQueryBegin();
        });

        //打开页面或者点击查询（即加载页面执行）
        function cadreApplyInfoQueryBegin() {
            var defaultValue = "${condition['studentInfo.branchSchool.resourceid']}";
            var schoolId = "";
            var isBrschool = "${isBrschool}";
            if(isBrschool==true || isBrschool=="true"){
                schoolId = defaultValue;
            }
            var gradeId = "${condition['gradeId']}";
            var classicId = "${condition['classicId']}";
            var teachingType = "${condition['teachingType']}";
            var majorId = "${condition['majorId']}";
            var classesId = "${condition['studentInfo.classes.resourceid']}";
            var selectIdsJson = "{unitId:'cadreApplyInfoPage_school',gradeId:'cadreApplyInfoPage_grade',classicId:'cadreApplyInfoPage_classic',teachingType:'cadreApplyInfoPage_teachingType',majorId:'cadreApplyInfoPage_major',classesId:'cadreApplyInfoPage_classes'}";
            cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
        }

    </script>
</head>
<body>
<div class="page">
    <div class="pageHeader">
        <form id="cadreInfoApplyPageForm" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/work/cadreInfoManage/applyList.html" method="post">
            <div class="searchBar">
                <c:if test="${not isStudent}">
                    <ul class="searchContent">
                    <li class="custom-li"><label>教学点：</label>
                        <span sel-id="cadreApplyInfoPage_school" sel-name="studentInfo.branchSchool.resourceid" sel-onchange="cadreApplyInfoQueryUnit()"
                              sel-classs="flexselect"></span>
                    </li>
                    <li>
                        <label>年度：</label>
                        <gh:selectModel id="cadreApplyInfoPage_yearInfoId" name="yearInfo.resourceid" bindValue="resourceid" displayValue="yearName"
                                        modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfo.resourceid']}" orderBy="yearName desc" style="width:125px"/>
                    </li>

                    <li>
                        <label>学期：</label>
                        <gh:select id="cadreApplyInfoPage_term" name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:125px" />
                    </li>

                    </ul>
                    <ul class="searchContent">
                        <li class="custom-li"><label>班级：</label>
                            <span sel-id="cadreApplyInfoPage_classes" sel-name="studentInfo.classes.resourceid" sel-classs="flexselect" ></span>
                        </li>
                        <li>
                            <label>学号：</label>
                            <input type="text" id="cadreApplyInfoPage_studyNo" name="studyNo-likeR" value="${condition['studyNo-likeR']}" />
                        </li>
                        <li>
                            <label>姓名：</label>
                            <input type="text" id="cadreApplyInfoPage_studentName" name="studentName-like" value="${condition['studentName-like']}" />
                        </li>
                    </ul>
                    <ul class="searchContent">
                        <li>
                            <label>组织：</label>
                            <gh:select id="cadreApplyInfoPage_organization" name="organization" dictionaryCode="Code.WorkManage.organization" value="${condition['organization']}" style="width:125px" />
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
        <gh:resAuth parentCode="RES_WORK_CADREINFO_APPLY" pageType="list"></gh:resAuth>
        <table class="table" layouth="185" width="100%">
            <thead>
            <tr>
                <th width="3%"><input type="checkbox" name="checkall" id="check_all_cadreInfo" onclick="checkboxAll('#check_all_cadreInfo','resourceid','#cadreApplyInfoManageBody')"/></th>
                <th width="8%">年度</th>
                <th width="8%">学期</th>
                <th width="10%">学号</th>
                <th width="8%">姓名</th>
                <th width="8%">竞选部门</th>
                <th width="8%">竞选职位</th>
                <th width="5%">组织</th>
                <th width="6%">备注</th>
            </tr>
            </thead>
            <tbody id="cadreApplyInfoManageBody">
            <c:forEach items="${pageResult.result}" var="ci" varStatus="vs">
                <tr>
                    <td><input type="checkbox" name="resourceid" value="${ci.resourceid }" autocomplete="off" isAppoint="${ci.isAppoint}" /></td>
                    <td title="${ci.yearInfo.yearName }">${ci.yearInfo.yearName }</td>
                    <td title="${ci.term}">${ghfn:dictCode2Val('CodeTerm',ci.term ) }</td>
                    <td title="${ci.studyNo}">
                        <a href="#" style="color: #034c50" onclick="viewCadreInfo('${ci.resourceid}')" title="查看详细信息"> ${ci.studyNo}</a>
                    </td>
                    <td title="${ci.studentName}">
                        <a href="#" style="color: #006fb7" onclick="viewStudentInfo('${ci.studyNo}')" title="查看学籍信息"> ${ci.studentName}</a>
                    </td>
                    <td title="${ci.department}">${ghfn:dictCode2Val('Code.WorkManage.department',ci.department ) }</td>
                    <td title="${ci.position}">${ghfn:dictCode2Val('Code.WorkManage.position',ci.position ) }</td>
                    <td title="${ci.organization}">${ghfn:dictCode2Val('Code.WorkManage.organization',ci.organization ) }</td>
                    <td title="${ci.memo}">${ci.memo}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${not isStudent}">
            <gh:page page="${pageResult}" goPageUrl="${baseUrl }/edu3/work/cadreInfoManage/applyList.html" pageType="sys" condition="${condition}"/>
        </c:if>
    </div>
</div>
<script type="text/javascript">
    //新增
    function addCadreInfo(){
        navTab.openTab('RES_WORK_CADREINFO_INPUT', '${baseUrl}/edu3/work/cadreInfoManage/input.html', '申请学生干部');
    }

    //修改
    function editCadreInfo(){
        var url = "${baseUrl}/edu3/work/cadreInfoManage/input.html";
        if(isCheckOnlyone('resourceid','#cadreApplyInfoManageBody')){
            var checekObj = $("#cadreApplyInfoManageBody input[name='resourceid']:checked")
            if (checekObj.attr("isAppoint") == 'Y') {
                alertMsg.warn("该记录已经审核，不允许编辑！");
                return false;
            }
            navTab.openTab('RES_WORK_CADREINFO_INPUT', url+'?resourceid='+$("#cadreApplyInfoManageBody input[name='resourceid']:checked").val(), '编辑学生干部信息');
        }
    }

    //下载模版
    function downloadCadreInfo() {
        var yearid = $("#cadreApplyInfoPage_yearInfoId").val();
        var term = $("#cadreApplyInfoPage_term").val();
        if (yearid == '' || term == '') {
            alertMsg.warn("请选择年度和学期！");
            return false;
        }
        var url = "${baseUrl}/edu3/work/cadreInfoManage/downloadModel.html?"+$("#cadreInfoApplyPageForm").serialize();
        downloadFileByIframe(url, "downloadCadreInfoModel");
    }

    //打印数据
    function printCadreInfo() {
        var schoolid = $("#cadreApplyInfoPage_school").val();
        var yearid = $("#cadreApplyInfoPage_yearInfoId").val();

        if (schoolid == '' || schoolid == undefined) {
            alertMsg.warn("请选择教学点！");
            return false;
        }

        var printUrl = "${baseUrl}/edu3/work/cadreInfoManage/printView.html"+renameUrlParam(getUrlByParam());

        $.pdialog.open(printUrl,'RES_WORK_CADREINFO_PRINT','打印预览',{mask:true,height:600, width:800});
    }

    //删除
    function deleteCadreInfo(){
        pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/work/cadreInfoManage/remove.html","#cadreApplyInfoManageBody");
    }

    //查看详细信息
    function viewCadreInfo(resourceid) {
        var url = "${baseUrl}/edu3/work/cadreInfoManage/viewCadreInfo.html";
        $.pdialog.open(url+'?resourceid='+resourceid, 'RES_WORK_CADREINFO_VIEW', '查看详细信息', {width: 800, height: 600});
    }

    //查看学籍信息
    function viewStudentInfo(studyNo) {
        var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
        $.pdialog.open(url+'?studyNo='+studyNo, 'RES_WORK_STUDENTINFO_VIEW', '查看详细信息', {width: 800, height: 600});
    }

    function getUrlByParam() {
        var url = "";
        if(isChecked('resourceid','#cadreApplyInfoManageBody')){
            var resIds = [];
            $("#cadreApplyInfoManageBody input[name='resourceid']:checked").each(function(){
                resIds.push($(this).val());
            });
            url += "?resourceids="+resIds.toString()+"&"+$("#cadreInfoApplyPageForm").serialize();
        }else{
            url += "?"+$("#cadreInfoApplyPageForm").serialize();
        }
        return url;
    }

    function renameUrlParam(url) {
        var url_new = "";
        url_new = url.replace("studentInfo.branchSchool.resourceid","branchSchoolid")
            .replace("yearInfo.resourceid","yearInfoid")
            .replace("studentInfo.grade.resourceid","gradeid")
            .replace("studentInfo.classes.resourceid","classesid")
            .replace("studyNo-likeR","studyNo")
            .replace("studentName-like","studentName")
            .replace("avgScore-begin","avgScore");
        return url_new;
    }

    // 选择教学点
    function cadreApplyInfoQueryUnit() {
        var defaultValue = $("#cadreApplyInfoPage_school").val();
        var selectIdsJson = "{gradeId:'cadreApplyInfoPage_grade',classicId:'cadreApplyInfoPage_classic',teachingType:'cadreApplyInfoPage_teachingType',majorId:'cadreApplyInfoPage_major',classesId:'cadreApplyInfoPage_classes'}";
        cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
    }
</script>
</body>
</html>