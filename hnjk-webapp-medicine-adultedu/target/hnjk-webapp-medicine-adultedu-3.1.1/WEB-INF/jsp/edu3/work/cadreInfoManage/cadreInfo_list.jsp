<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学生干部信息管理</title>
    <style type="text/css">th,td{text-align: center}</style>
    <script type="text/javascript">
        $(document).ready(function(){
            cadreInfoQueryBegin();
        });

        //打开页面或者点击查询（即加载页面执行）
        function cadreInfoQueryBegin() {
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
            var selectIdsJson = "{unitId:'cadreInfoPage_school',gradeId:'cadreInfoPage_grade',classicId:'cadreInfoPage_classic',teachingType:'cadreInfoPage_teachingType',majorId:'cadreInfoPage_major',classesId:'cadreInfoPage_classes'}";
            cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
        }

    </script>
</head>
<body>
<div class="page">
    <div class="pageHeader">
        <form id="cadreInfoPageForm" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/work/cadreInfoManage/list.html" method="post">
            <input type="hidden" id="cadreInfoPage_isAppoint" name="isAppoint" value="Y">
            <div class="searchBar">
                <c:if test="${not isStudent}">
                    <ul class="searchContent">
                        <li class="custom-li"><label>教学点：</label>
                            <span sel-id="cadreInfoPage_school" sel-name="studentInfo.branchSchool.resourceid" sel-onchange="cadreInfoQueryUnit()"
                                  sel-classs="flexselect"></span>
                        </li>
                        <li>
                            <label>年度：</label>
                            <gh:selectModel id="cadreInfoPage_yearInfoId" name="yearInfo.resourceid" bindValue="resourceid" displayValue="yearName"
                                            modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfo.resourceid']}" orderBy="yearName desc" style="width:125px"/>
                        </li>

                        <li>
                            <label>学期：</label>
                            <gh:select id="cadreInfoPage_term" name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:125px" />
                        </li>

                    </ul>
                    <ul class="searchContent">
                        <li class="custom-li"><label>班级：</label>
                            <span sel-id="cadreInfoPage_classes" sel-name="studentInfo.classes.resourceid" sel-classs="flexselect" ></span>
                        </li>
                        <li>
                            <label>学号：</label>
                            <input type="text" id="cadreInfoPage_studyNo" name="studyNo-likeR" value="${condition['studyNo-likeR']}" />
                        </li>
                        <li>
                            <label>姓名：</label>
                            <input type="text" id="cadreInfoPage_studentName" name="studentName-like" value="${condition['studentName-like']}" />
                        </li>
                    </ul>
                    <ul class="searchContent">
                        <li>
                            <label>是否候选：</label>
                            <gh:select id="cadreInfoPage_isCandidate" name="isCandidate" dictionaryCode="yesOrNo" value="${condition['isCandidate']}" style="width:125px" />
                        </li>
                        <li>
                            <label>组织：</label>
                            <gh:select id="cadreInfoPage_organization" name="organization" dictionaryCode="Code.WorkManage.organization" value="${condition['organization']}" style="width:125px" />
                        </li>
                        <li>
                            <label>职位状态：</label>
                            <gh:select id="cadreInfoPage_status" name="status" dictionaryCode="Code.WorkManage.positionStatus" value="${condition['status']}" style="width:125px" />
                        </li>
                            <%--<li>
                                <label>是否任用：</label>
                                <gh:select id="cadreInfoPage_isAppoint" name="isAppoint" dictionaryCode="yesOrNo" value="${condition['isAppoint']}" style="width:125px" />
                            </li>--%>
                    </ul>
                    <ul class="searchContent">
                        <div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div>
                    </ul>
                </c:if>
            </div>
        </form>
    </div>
    <div  class="pageContent">
        <gh:resAuth parentCode="RES_WORK_CADREINFO_LIST" pageType="list"></gh:resAuth>
        <table class="table" layouth="185" width="100%">
            <thead>
                <tr>
                    <th width="3%"><input type="checkbox" name="checkall" id="check_all_cadreInfo" onclick="checkboxAll('#check_all_cadreInfo','resourceid','#cadreInfoManageBody')"/></th>
                    <th width="8%">年度</th>
                    <th width="8%">学期</th>
                    <th width="10%">学号</th>
                    <th width="8%">姓名</th>
                    <th width="8%">竞选部门</th>
                    <th width="8%">竞选职位</th>
                    <th width="8%">现任部门</th>
                    <th width="8%">现任职位</th>
                    <th width="6%">是否候选</th>
                    <th width="5%">组织</th>
                    <th width="5%">状态</th>
                    <th width="6%">备注</th>
                </tr>
            </thead>
            <tbody id="cadreInfoManageBody">
            <c:forEach items="${pageResult.result}" var="ci" varStatus="vs">
                <tr>
                    <td><input type="checkbox" name="resourceid" value="${ci.resourceid }" autocomplete="off" /></td>
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
                    <td title="${ci.department_current}">${ghfn:dictCode2Val('Code.WorkManage.department',ci.department_current ) }</td>
                    <td title="${ci.position_current}">${ghfn:dictCode2Val('Code.WorkManage.position',ci.position_current ) }</td>
                    <td title="${ci.isCandidate}">${ghfn:dictCode2Val('yesOrNo_default',ci.isCandidate ) }</td>
                    <%--<td title="${ci.isAppoint}">${ghfn:dictCode2Val('yesOrNo_default',ci.isAppoint ) }</td>--%>
                    <td title="${ci.organization}">${ghfn:dictCode2Val('Code.WorkManage.organization',ci.organization ) }</td>
                    <td title="${ci.status}">${ghfn:dictCode2Val('Code.WorkManage.positionStatus',ci.status ) }</td>
                    <td title="${ci.memo}">${ci.memo}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${not isStudent}">
            <gh:page page="${pageResult}" goPageUrl="${baseUrl }/edu3/work/cadreInfoManage/list.html" pageType="sys" condition="${condition}"/>
        </c:if>
    </div>
</div>
<script type="text/javascript">

    //修改
    function editCadreInfo(){
        var url = "${baseUrl}/edu3/work/cadreInfoManage/input.html";
        if(isCheckOnlyone('resourceid','#cadreInfoManageBody')){
            navTab.openTab('RES_WORK_CADREINFO_INPUT', url+'?resourceid='+$("#cadreInfoManageBody input[name='resourceid']:checked").val(), '编辑学生干部信息');
        }
    }

    //导入数据
    function importCadreInfo() {
        $.pdialog.open(baseUrl+"/edu3/work/cadreInfoManage/importDialog.html", 'RES_WORK_CADREINFO_IMPORT', '导入学生干部评选结果', {width: 600, height: 360});
    }

    //导出汇总数据
    function exportCadreInfo() {
        var yearid = $("#cadreInfoPage_yearInfoId").val();
        if (yearid == '' || yearid == undefined) {
            alertMsg.warn("请选择年度！");
            return false;
        }
        var url = "${baseUrl}/edu3/work/cadreInfoManage/exportSummary.html"+getUrlByParam();
        downloadFileByIframe(url, "exportCadreInfoSummary");
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
        if(isChecked('resourceid','#cadreInfoManageBody')){
            var resIds = [];
            $("#cadreInfoManageBody input[name='resourceid']:checked").each(function(){
                resIds.push($(this).val());
            });
            url += "?resourceids="+resIds.toString()+"&"+$("#cadreInfoPageForm").serialize();
        }else{
            url += "?"+$("#cadreInfoPageForm").serialize();
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
    function cadreInfoQueryUnit() {
        var defaultValue = $("#cadreInfoPage_school").val();
        var selectIdsJson = "{gradeId:'cadreInfoPage_grade',classicId:'cadreInfoPage_classic',teachingType:'cadreInfoPage_teachingType',majorId:'cadreInfoPage_major',classesId:'cadreInfoPage_classes'}";
        cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
    }
</script>
</body>
</html>