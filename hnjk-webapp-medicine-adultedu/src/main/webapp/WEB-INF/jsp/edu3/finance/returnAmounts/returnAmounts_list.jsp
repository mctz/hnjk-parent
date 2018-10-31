<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>已返学费金额管理</title>
    <style type="text/css">th,td{text-align: center}</style>
    <script type="text/javascript">
        $(document).ready(function(){
            returnAmountsQueryBegin();
        });

        //打开页面或者点击查询（即加载页面执行）
        function returnAmountsQueryBegin() {
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
            var selectIdsJson = "{unitId:'returnAmountsPage_school',gradeId:'returnAmountsPage_grade',classicId:'returnAmountsPage_classic',teachingType:'returnAmountsPage_teachingType',majorId:'returnAmountsPage_major',classesId:'returnAmountsPage_classes'}";
            cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
        }

    </script>
</head>
<body>
<div class="page">
    <div class="pageHeader">
        <form id="returnAmountsPageForm" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/finance/studentpayment/returnAmountInfo.html" method="post">
            <div class="searchBar">
                <ul class="searchContent">
                    <li class="custom-li"><label>教学点：</label>
                        <span sel-id="returnAmountsPage_school" sel-name="unit.resourceid" sel-onchange="returnAmountsQueryUnit()"
                              sel-classs="flexselect"></span>
                    </li>
                    <li><label>年度：</label>
                        <gh:selectModel id="returnAmountsPage_yearId" name="yearInfo.resourceid"
                                modelClass="com.hnjk.edu.basedata.model.YearInfo" bindValue="resourceid"
                                value="${condition['yearInfo.resourceid']}" displayValue="yearName" orderBy="firstYear desc" />
                    </li>
                    <li>
                        <label>次数：</label>
                        <select name="count" style="width: 100px;"><option value=""></option><option value="1">1</option><option value="2">2</option></select>
                    </li>
                </ul>
                <ul class="searchContent">
                    <div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div>
                </ul>
            </div>
        </form>
    </div>
    <div  class="pageContent">
        <gh:resAuth parentCode="RES_FINANCE_RETURNAMOUNTS" pageType="list"></gh:resAuth>
        <table class="table" layouth="135" width="100%">
            <thead>
            <tr>
                <th width="5%"><input type="checkbox" name="checkall" id="check_all_returnAmounts" onclick="checkboxAll('#check_all_returnAmounts','resourceid','#returnAmountsManageBody')"/></th>
                <th width="25%">教学点</th>
                <th width="15%">缴费学年</th>
                <th width="10%">次数</th>
                <th width="15%">已返金额</th>
                <th width="15%">日期</th>
                <th width="10"></th>
            </tr>
            </thead>
            <tbody id="returnAmountsManageBody">
            <c:forEach items="${pageResult.result}" var="t" varStatus="vs">
                <tr>
                    <td><input type="checkbox" name="resourceid" value="${t.resourceid }" autocomplete="off" /></td>
                    <td title="${t.unit.unitName }">${t.unit.unitName }</td>
                    <td title="${t.yearInfo.yearName}">${t.yearInfo.yearName}</td>
                    <td title="${t.count}">${t.count }</td>
                    <td title="${t.amounts}"><fmt:formatNumber value="${t.amounts}" pattern="#.##" minFractionDigits="2"/></td>
                    <td><fmt:formatDate value="${t.operateDate }" pattern="yyyy-MM-dd"/></td>
                    <td></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <gh:page page="${pageResult}" goPageUrl="${baseUrl }/edu3/finance/studentpayment/returnAmountInfo.html" pageType="sys" condition="${condition}"/>
    </div>
</div>
<script type="text/javascript">
    //新增
    function addReturnAmounts(){
        $.pdialog.open('${baseUrl}/edu3/finance/returnAmount/input.html','RES_FINANCE_RETURNAMOUNTS_ADD', '新增已返学费金额', {width: 800, height: 400});
        //navTab.openTab('RES_FINANCE_RETURNAMOUNTS_ADD', '${baseUrl}/edu3/finance/returnAmount/input.html', '新增已返学费金额');
    }

    //修改
    function editReturnAmounts(){
        var url = "${baseUrl}/edu3/finance/returnAmount/input.html";
        if(isCheckOnlyone('resourceid','#returnAmountsManageBody')){
            var checekObj = $("#returnAmountsManageBody input[name='resourceid']:checked")

            $.pdialog.open(url+'?resourceid='+checekObj.val(),'RES_FINANCE_RETURNAMOUNTS_INPUT', '编辑已返学费金额', {width: 800, height: 400});
            //navTab.openTab('RES_FINANCE_RETURNAMOUNTS_INPUT', url+'?resourceid='+$("#returnAmountsManageBody input[name='resourceid']:checked").val(), '编辑已返学费金额');
        }
    }

    //下载模版
    function downloadReturnAmounts() {
        var url = "${baseUrl}/edu3/finance/returnAmount/downloadModel.html";
        downloadFileByIframe(url, "downloadReturnAmountModel");
    }

    //导入
    function importReturnAmounts() {
        $.pdialog.open(baseUrl+"/edu3/finance/returnAmount/inputAmounts.html", '导入选修课成绩成绩', {width: 600, height: 400});
    }
   
    //删除
    function deleteReturnAmounts(){
        pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/finance/returnAmount/remove.html","#returnAmountsManageBody");
    }

    function getUrlByParam() {
        var url = "";
        if(isChecked('resourceid','#returnAmountsManageBody')){
            var resIds = [];
            $("#returnAmountsManageBody input[name='resourceid']:checked").each(function(){
                resIds.push($(this).val());
            });
            url += "?resourceids="+resIds.toString()+"&"+$("#returnAmountsPageForm").serialize();
        }else{
            url += "?"+$("#returnAmountsPageForm").serialize();
        }
        return url;
    }

    function renameUrlParam(url) {
        var url_new = "";
        url_new = url.replace("studentInfo.branchSchool.resourceid","branchSchoolid")
            .replace("yearInfo.resourceid","yearInfoid");
        return url_new;
    }

    // 选择教学点
    function returnAmountsQueryUnit() {
        var defaultValue = $("#returnAmountsPage_school").val();
        var selectIdsJson = "{gradeId:'returnAmountsPage_grade',classicId:'returnAmountsPage_classic',teachingType:'returnAmountsPage_teachingType',majorId:'returnAmountsPage_major',classesId:'returnAmountsPage_classes'}";
        cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
    }
</script>
</body>
</html>