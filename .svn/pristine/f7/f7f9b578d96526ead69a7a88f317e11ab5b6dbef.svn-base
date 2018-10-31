<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>排考管理</title>
    <style>
        th{text-align: center}
    </style>
</head>
<body>
<script type="text/javascript">

    $(document).ready(function(){
        examinationQueryBegin();
    });
    
    //打开页面或者点击查询（即加载页面执行）
    function examinationQueryBegin() {
        var defaultValue = "${condition['branchSchool']}";
        var schoolId = "${brSchoolId}";
        var gradeId = "${condition['grade']}";
        //var classicId = "${condition['classic']}";
        //var teachingType = "${condition['learningStyle']}";
        var majorId = "${condition['major']}";
        var classesId = "${condition['classes']}";
        var selectIdsJson = "{unitId:'examination_branchSchool',gradeId:'examination_grade',majorId:'examination_major',classesId:'examination_classes'}";
        cascadeQuery("begin", defaultValue, schoolId, gradeId, "","", majorId, classesId, selectIdsJson);
    }

    // 选择教学点
    function examinationQueryUnit() {
        var defaultValue = $("#examination_branchSchool").val();
        var selectIdsJson = "{gradeId:'examination_grade',majorId:'examination_major',classesId:'examination_classes'}";
        cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
    }

    // 选择年级
    function examinationQueryGrade() {
        var defaultValue = $("#examination_branchSchool").val();
        var gradeId = $("#examination_grade").val();
        var selectIdsJson = "{classicId:'examination_classic',majorId:'examination_major',classesId:'examination_classes'}";
        cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
    }

    // 选择层次
    function examinationQueryClassic() {
        var defaultValue = $("#examination_branchSchool").val();
        var gradeId = $("#examination_grade").val();
        var classicId = $("#examination_classic").val();
        var selectIdsJson = "{majorId:'examination_major',classesId:'examination_classes'}";
        cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
    }

    //选择学习形式
    function examinationQueryTeachingType() {
        var defaultValue = $("#examination_branchSchool").val();
        var gradeId = $("#examination_grade").val();
        var classicId = $("#examination_classic").val();
        //var teachingTypeId = $("#learningStyle").val();
        var selectIdsJson = "{majorId:'examination_major',classesId:'examination_classes'}";
        cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,"", "", "", selectIdsJson);
    }
    //选择专业
    function examinationQueryMajor() {
        var defaultValue = $("#examination_branchSchool").val();
        var gradeId = $("#examination_grade").val();
        var classicId = $("#examination_classic").val();
        var majorId = $("#examination_major").val();
        var selectIdsJson = "{classesId:'examination_classes'}";
        cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
    }
    //新增
    function addExamination(){
        var branchSchool = $("#examination_branchSchool").val();
        var classes_id = "${classes.resourceid}";
        var plancourse_id = "${planCourse.resourceid}";
        var stunumber = "${stunumber}";
        var url = "${baseUrl}/edu3/teaching/teachingplancourse/examinationEdit.html?branchSchool="+branchSchool+"&classes_id="+classes_id+"&plancourse_id="+plancourse_id+"&stunumber="+stunumber;
        //navTab.openTab('courseExaminationInput', url, '新增排考');
        $.pdialog.open(url,"courseExaminationInput","新增排考",{width:800,height:600,mask:true});
    }
    //修改
    function modifyExamination(){
        var branchSchool = $("#examination_branchSchool").val();
        var classes_id = "${classes.resourceid}";
        var plancourse_id = "${planCourse.resourceid}";
        var stunumber = "${stunumber}";
        var url = "${baseUrl}/edu3/teaching/teachingplancourse/examinationEdit.html";
        if(isCheckOnlyone('resourceid','#examinationBody')){
            url += "?resourceid="+$("#examinationBody input[name='resourceid']:checked").val()+"&branchSchool="+branchSchool+"&classes_id="+classes_id+"&plancourse_id="+plancourse_id+"&stunumber="+stunumber;
            //navTab.openTab('courseExaminationInput', url, '编辑排考');
            $.pdialog.open(url,"courseExaminationInput","编辑排考",{width:800,height:600,mask:true});
        }
    }
    //删除
    function removEexamination(){
        pageBarHandle("您确定要删除这些排考记录吗？","${baseUrl}/edu3/teaching/teachingplancourse/examinationRemove.html","#examinationBody");
    }

    //导入
    function importExamination(){
        var classesid = "${condition['classesid']}";
        var plancourseid = "${condition['plancourseid']}";
        $.pdialog.open("${baseUrl}/edu3/teaching/teachingplancourse/examinationUpload.html?classesid="+classesid+"&plancourseid="+plancourseid, "courseExaminationImport", "导入排考结果", {width:800,height:500,mask:true});
    }

    //导出
    function exportExamination() {
        var url = "${baseUrl}/edu3/teaching/teachingplancourse/examinationList.html?flag=export&";
        var resIds = "";
        var classesid = "${classes.resourceid}";
        var plancourseid = "${planCourse.resourceid}";
        $("#examinationBody input[name='resourceid']:checked").each(function(){
            var checekObj = $(this);
            if(""==resIds){
                resIds += checekObj.val();
            }else{
                resIds += ","+checekObj.val();
            }
        });
        var param = getUrlByParam();
        url += "&classesid="+classesid+"&plancourseid="+plancourseid+"&resIds="+resIds+"&"+param;
        downloadFileByIframe(url,'examination_Ifram');
    }

    function getUrlByParam() {
        var branchSchool    = $("#examination_branchSchool").val();
        var grade 	  = $("#examination_grade").val();
        var major 	  = $("#examination_major").val();
        var classes	  = $("#examination_classes").val();
        var courseid = $("#examination_courseid").val();

        var url =  "branchSchool="+branchSchool+"&grade="+grade+"&major="+major+"&classes="+classes+"&courseid="+courseid;
        return url;
    }
</script>
<div class="page">
    <c:choose>
        <c:when test="${classes ne null}">
            <h2 class="contentTitle">
                班级: ${classes.classname } &nbsp;&nbsp;人数：${stunumber}
                <br />课程: ${planCourse.course.courseName }
            </h2>
        </c:when>
        <c:otherwise>
            <div class="pageHeader">
                <form id="examinationForm" onsubmit="return navTabSearch(this);"
                      action="${baseUrl }/edu3/teaching/teachingplancourse/examinationList.html" method="post">
                    <input type="hidden" name="classesid" value="${classes.resourceid }" />
                    <input type="hidden" name="plancourseid" value="${planCourse.resourceid }" />
                    <input type="hidden" name="stunumber" value="${stunumber }" />
                    <div class="searchBar">
                        <ul class="searchContent">
                            <li class="custom-li">
                                <label>教学点：</label>
                                <span sel-id="examination_branchSchool"
                                      sel-name="branchSchool" sel-onchange="examinationQueryUnit()"
                                      sel-classs="flexselect" ></span>
                            </li>
                            <li>
                                <label>年级：</label>
                                <span sel-id="examination_grade"
                                      sel-name="grade" sel-onchange="examinationQueryGrade()"
                                      sel-style="width: 120px"></span>
                            </li>
                            <li class="custom-li">
                                <label>专业：</label>
                                <span sel-id="examination_major" sel-name="major" sel-onchange="examinationQueryMajor()"
                                       sel-classs="flexselect" ></span>
                            </li>
                            </ul><ul class="searchContent">
                            <li class="custom-li">
                                <label>班级：</label>
                                <span sel-id="examination_classes"
                                      sel-name="classes" sel-classs="flexselect"></span>
                            </li>
                            <li class="custom-li">
                                <label>课程：</label>
                                <gh:courseAutocomplete  id="examination_courseid"
                                        name="courseid"  value="${condition['courseid']}" tabindex="1"
                                        displayType="code" isFilterTeacher="Y" style="width:200px;" />
                            </li>
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">查 询</button>
                                </div>
                            </div>

                        </ul>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="pageContent">
        <c:choose>
            <c:when test="${classes ne null}">
                <gh:resAuth parentCode="RES_TEACHING_PLANCOURSE_EXAMINATION" pageType="list,hlist"></gh:resAuth>
            </c:when>
            <c:otherwise>
                <gh:resAuth parentCode="RES_TEACHING_PLANCOURSE_EXAMINATION" pageType="list"></gh:resAuth>
            </c:otherwise>
        </c:choose>
        <table class="table" layouth="138">
            <thead>
            <tr>
                <th width="3%"><input type="checkbox" name="checkall"
                                      id="check_all_examination"
                                      onclick="checkboxAll('#check_all_examination','resourceid','#examinationBody')" /></th>
                <c:if test="${classes eq null}">
                    <th width="12%">教学点</th>
                    <th width="15%">班级</th>
                    <th width="8%">考试科目</th>
                    <th width="5%">考试类别</th>
                </c:if>
                <th width="12%">日期</th>
                <th width="8%">时间</th>
                <th width="5%">人数</th>
                <th width="10%">地点</th>
                <th width="10%">课室</th>
                <th width="5%">监考</th>
                <th width="8%">排考人</th>
            </tr>
            </thead>
            <tbody id="examinationBody">
            <c:forEach items="${examinationPage.result }" var="exam" varStatus="vs">
                <tr>
                    <td style="text-align: center"><input type="checkbox" name="resourceid"
                               value="${exam.resourceid }" autocomplete="off" /></td>
                    <c:if test="${classes eq null}">
                        <td>${exam.classes.brSchool.unitName }</td>
                        <td>${exam.classes.classname}</td>
                        <td>${exam.course.courseName }</td>
                        <td>${ghfn:dictCode2Val('CodeExamClassType',exam.teachingPlanCourse.examClassType) }</td>
                    </c:if>
                    <td>${exam.startExamDate} 至 ${exam.endExamDate}</td>
                    <%--<td>${exam.startTimePeriod} 至 ${exam.endTimePeriod}</td>--%>
                    <td> <fmt:formatDate value="${exam.startTimePeriod }" pattern="HH:mm" /> 至 <fmt:formatDate
                        value="${exam.endTimePeriod }" pattern="HH:mm" />
                    </td>
                    <td style="text-align: center">${exam.studentNum}</td>
                    <td>${exam.location}</td>
                    <td>${exam.classroom}</td>
                    <td>${exam.teacher}</td>
                    <td title="${exam.operatorName}">${exam.operatorName}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <gh:page page="${examinationPage}"
                 goPageUrl="${baseUrl}/edu3/teaching/teachingplancourse/examinationList.html"
                 condition="${condition }" pageType="sys" />

    </div>
</div>
</body>
</html>