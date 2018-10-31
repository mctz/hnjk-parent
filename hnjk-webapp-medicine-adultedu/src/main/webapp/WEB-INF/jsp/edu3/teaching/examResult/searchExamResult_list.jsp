<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询学生成绩</title>
<style type="text/css">
th,td{text-align: center;}
</style>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 130px;">
			<form id="queryExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/search-examresults-list.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<div class="searchBar" id="searchExamResultDiv">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool}"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="query_searchExamresult_brSchoolId"
								sel-name="branchSchool"
								sel-onchange="searchExamresultQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="query_searchExamresult_brSchoolId"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span
							sel-id="query_searchExamresult_gradeid" sel-name="gradeid"
							sel-onchange="searchExamresultQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层 次：</label> <span
							sel-id="query_searchExamresult_classic" sel-name="classic"
							sel-onchange="searchExamresultQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习方式：</label> <span
							sel-id="query_examresults_learningStyle" sel-name="learningStyle"
							sel-onchange="searchExamresultQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 100px"></span>
						</li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专 业：</label> <span
							sel-id="query_searchExamresult_major" sel-name="major"
							sel-onchange="searchExamresultQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>学号：</label><input type="text"
							id="query_examresults_studyNo" name="studyNo"
							value="${condition['studyNo']}" style="width: 140px;" /></li>
						<li><label>姓名：</label><input type="text"
							id="query_examresults_name" name="name"
							value="${condition['name']}" style="width: 53%" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
							id="query_examresults_studentStatus"
							dictionaryCode="CodeStudentStatus" choose="Y"
							value="${condition['studentStatus']}" style="width:100px;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="searchExamResult_classesid" sel-name="classId"
							sel-classs="flexselect"></span></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								id="faceStudyExamResults_courseId" tabindex="1"
								displayType="code" isFilterTeacher="Y" style="width:140px;"
								value="${condition['courseId'] }" /></li>
						
						<li><label>毕业时间：</label> <select name="graduateDateStr"
							style="width: 55%" id="query_examresults_graduateDateStr">
								<option value="">请选择</option>
								<c:forEach items="${graduateDateList }" var="gMap">
									<option value="${gMap.graduateDate }"
										<c:if test="${condition['graduateDateStr'] eq gMap.graduateDate }">selected="selected"</c:if>>${gMap.graduateDate}</option>
								</c:forEach>
						</select></li>
						<li><label>全部通过：</label> <gh:select name="isAllPass"
								id="query_examresults_isAllPass"
								dictionaryCode="yesOrNo" choose="Y"
								value="${condition['isAllPass']}" style="width:100px;" /></li>
					</ul>
					<ul class="searchContent">
						
						<li style="width: 360px"><label style="width: 90px">毕业确认时间：</label>
							<input type="text" id="confirmGraduateDateb_id"
							style="width: 80px;" name="confirmGraduateDateb" class="Wdate"
							value="${condition['confirmGraduateDateb']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 到 <input
							type="text" id="confirmGraduateDatee_id" style="width: 80px;"
							name="confirmGraduateDatee" class="Wdate"
							value="${condition['confirmGraduateDatee']}"
							onfocus="WdatePicker({isShowWeek:true })" /></li>
						<li><label>打印时间：</label> <input type="text"
							id="searchExamResult_printDate" name="printDate"
							style="width: 140px; text-align: center;" class="date1"
							onFocus="WdatePicker({isShowWeek:true})" /></li>
						<c:if test="${!empty schoolCode && schoolCode=='11078'}">
							<li style="margin-left: 60px"> <input type="checkbox"
								name="isUseElectronicalSign"
								id="searchExamResult_isUseElectronicalSign" value="1" /> 使用电子签名
							</li>
						</c:if>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
					<ul class="searchContent">
						<c:if test="${!empty terms}">
							<li style="width: 600px;margin-top: 8px;"><label>学期：</label> ${terms}</li>
						</c:if>

						<li style="margin-top: 8px;"> <input type="checkbox" name="degreeUnitExam" value="Y"  style="margin-top: 5px" /> 仅导出学位外语通过名单
						</li>
						<li style="margin-top: 8px;"> <input
								type="checkbox" name="passExam" value="Y"
								style="margin-top: 5px" /> 仅打印及格成绩
						</li>
					</ul>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_SEARCH_A" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<input type="hidden" name="examSubId"
				value="${condition['examSub'] }" /> <input type="hidden"
				name="examSubId" value="${condition['examSub'] }" />
			<table class="table" layouth="225">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_examResults_search"
							onclick="checkboxAll('#check_all_examResults_search','resourceid','#examResultsSearchBody'),defaultCheckAll()"
							<c:if test="${condition['defaultCheckAll'] eq 'Y' }"> checked="checked" </c:if> /></th>
						<th width="8%">学号</th>
						<th width="5%">姓名</th>
						<th width="18%">教学站</th>
						<th width="6%">年级</th>
						<th width="5%">层次</th>
						<th width="5%">学习方式</th>
						<th width="13%">专业</th>
						<th width="19%">班级</th>
						<!-- <th width="4%">性别</th>
						<th width="8%">出生日期</th> -->
						<th width="6%">在学状态</th>
						<th width="6%">操作</th>
					</tr>
				</thead>
				<tbody id="examResultsSearchBody">
					<c:forEach items="${objPage.result}" var="studentinfo"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${studentinfo.resourceid }" autocomplete="off"
								<c:if test="${condition['defaultCheckAll'] eq 'Y' }"> checked="checked" </c:if> /></td>
							<td><a href="javascript:void(0)"
								onclick="viewStuExamResults('${studentinfo.resourceid }','${studentinfo.studentName }');">${studentinfo.studyNo }</a></td>
							<td><a href="javascript:void(0)"
								onclick="viewStuExamResults('${studentinfo.resourceid }','${studentinfo.studentName }');">${studentinfo.studentName }</a></td>
							<td>${studentinfo.unitName}</td>
							<td>${studentinfo.gradeName }</td>
							<td>${studentinfo.classicName}</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',studentinfo.learningStyle) }</td>
							<td>${studentinfo.majorName}</td>
							<td>${studentinfo.classesname}</td>
							<%-- <td>${ghfn:dictCode2Val('CodeSex',studentinfo.studentBaseInfo.gender) }</td>
							<td>${studentinfo.studentBaseInfo.bornDay}</td> --%>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',studentinfo.studentStatus) }</td>
							<td><a href="javascript:void(0)"
								onclick="viewStuExamResults('${studentinfo.resourceid }','${studentinfo.studentName }');">查看成绩</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${objPage}"
				goPageUrl="${baseUrl}/edu3/teaching/result/search-examresults-list.html"
				pageType="sys" condition="${condition}"
				beforeForm="queryExamResultsSearchForm" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl}/edu3/teaching/result/search-examresults-list.html"
				pageType="sys" condition="${condition}" beforeForm="queryExamResultsSearchForm"/></div>
	</div>
	<script type="text/javascript">
$(document).ready(function(){
	searchExamresultQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function searchExamresultQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['learningStyle']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classId']}";
	var selectIdsJson = "{unitId:'query_searchExamresult_brSchoolId',gradeId:'query_searchExamresult_gradeid',classicId:'query_searchExamresult_classic',teachingType:'query_examresults_learningStyle',majorId:'query_searchExamresult_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function searchExamresultQueryUnit() {
	var defaultValue = $("#query_searchExamresult_brSchoolId").val();
	var selectIdsJson = "{gradeId:'query_searchExamresult_gradeid',classicId:'query_searchExamresult_classic',teachingType:'query_examresults_learningStyle',majorId:'query_searchExamresult_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function searchExamresultQueryGrade() {
	var defaultValue = $("#query_searchExamresult_brSchoolId").val();
	var gradeId = $("#query_searchExamresult_gradeid").val();
	var selectIdsJson = "{classicId:'query_searchExamresult_classic',teachingType:'query_examresults_learningStyle',majorId:'query_searchExamresult_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function searchExamresultQueryClassic() {
	var defaultValue = $("#query_searchExamresult_brSchoolId").val();
	var gradeId = $("#query_searchExamresult_gradeid").val();
	var classicId = $("#query_searchExamresult_classic").val();
	var selectIdsJson = "{teachingType:'query_examresults_learningStyle',majorId:'query_searchExamresult_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function searchExamresultQueryTeachingType() {
	var defaultValue = $("#query_searchExamresult_brSchoolId").val();
	var gradeId = $("#query_searchExamresult_gradeid").val();
	var classicId = $("#query_searchExamresult_classic").val();
	var teachingTypeId = $("#query_examresults_learningStyle").val();
	var selectIdsJson = "{majorId:'query_searchExamresult_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function searchExamresultQueryMajor() {
	var defaultValue = $("#query_searchExamresult_brSchoolId").val();
	var gradeId = $("#query_searchExamresult_gradeid").val();
	var classicId = $("#query_searchExamresult_classic").val();
	var teachingTypeId = $("#query_examresults_learningStyle").val();
	var majorId = $("#query_searchExamresult_major").val();
	var selectIdsJson = "{classesId:'searchExamResult_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

	//导出成绩册封面
	function printResultCoverInput(){
		var url = "${baseUrl}/edu3/teaching/result/printResultCoverInput.html";
		$.pdialog.open(url, 'coverinput_Iframe_INPUT', '导出成绩册封面', {width: 600, height: 100});
	}

	//翻页默认全选
	function defaultCheckAll(){
		
		var url 		 = "${baseUrl }/edu3/teaching/result/search-examresults-list.html";
		var formSelector = "form[action='"+url+"'][id='pagerForm']";
		
		if($("#check_all_examResults_search").attr("checked")){
			if($(formSelector).find("input[name='defaultCheckAll']").size()>0){
				$(formSelector).find("input[name='defaultCheckAll']").val("Y");
			}else{
				$(formSelector).append("<input type='hidden'  name ='defaultCheckAll' value='Y'/>");
			}
		}else{
			if($(formSelector).find("input[name='defaultCheckAll']").size()>0){
				$(formSelector).find("input[name='defaultCheckAll']").val("N");
			}else{
				$(formSelector).append("<input type='hidden' name ='defaultCheckAll' value='N'/>");
			}
		}
	}
	

	function ajaxFreshClasses(){
		$("#queryExamResultsSearchForm").submit();
	}
	
    //查看学生成绩
	function viewStuExamResults(studentId,studentName){
	
		var url = "${baseUrl}/edu3/teaching/result/view-student-examresults.html?studentId="+studentId;
		navTab.openTab("RES_TEACHING_RESULT_MANAGE_SUBMIT_RESULTS_LIST",url,studentName+"成绩列表");	
	}
    
    //打印当前条件下全部学生的成绩单
	function printAllReportCard(flag){
    	var totalSize    = "${objPage.totalCount}";
		var url 		 = "${baseUrl}/edu3/teaching/result/personalReportCard-printall-page.html?flag="+flag+"&totalSize="+totalSize;
    
		var branchSchool = $("#queryExamResultsSearchForm #query_searchExamresult_brSchoolId").val();
		var gradeid      = $("#queryExamResultsSearchForm #query_searchExamresult_gradeid").val();
		var classic      = $("#queryExamResultsSearchForm #query_searchExamresult_classic").val();
		var major        = $("#queryExamResultsSearchForm #query_searchExamresult_major").val();
		var classId      = $("#queryExamResultsSearchForm #searchExamResult_classesid").val();
		var studyNo      = $("#queryExamResultsSearchForm #query_examresults_studyNo").val();
		var name         = $("#queryExamResultsSearchForm #query_examresults_name").val();
		var learningStyle= $("#queryExamResultsSearchForm #query_examresults_learningStyle").val();
		var studentStatus= $("#queryExamResultsSearchForm #query_examresults_studentStatus").val();
		var graduateDate = $("#queryExamResultsSearchForm #query_examresults_graduateDateStr").val();
		var degreeUnitExam ;
        var passExam;
    	$('input[name="degreeUnitExam"]:checked').each(function(){    
    		degreeUnitExam = $(this).val();    
    		url += "&degreeUnitExam="+degreeUnitExam;
        });
        $('input[name="passExam"]:checked').each(function(){
            passExam = $(this).val();
            url += "&passExam="+passExam;
        });
		//添加毕业确认时间 
		var confirmGraduateDateb= $("#confirmGraduateDateb_id").val();
		var confirmGraduateDatee= $("#confirmGraduateDatee_id").val();
		
		/* //if(""!=studyNo && undefined==studyNo){
		if(undefined==studyNo){
			alertMsg.warn("需要打印指定学生的成绩单，请点击查询后，勾选后点击打印！");
			return false;
		}
		//if(""!=name && undefined==name){
		if(undefined==name){	
			alertMsg.warn("需要打印指定学生的成绩单，请点击查询后，勾选后点击打印！");
			return false;
		} */
		
		if(""!=branchSchool && undefined!=branchSchool){
			url         += "&branchSchool="+branchSchool;
		}
		if(""!=gradeid && undefined!=gradeid){
			url         += "&gradeid="+gradeid;
		}
		if(""!=classic && undefined!=classic){
			url         += "&classic="+classic;
		}
		if(""!=major && undefined!=major){
			url         += "&major="+major;
		}
		if(""!=classId && undefined!=classId){
			url         += "&classId="+classId;
		}
		if(""!=studyNo && undefined!=studyNo){
			url         += "&studyNo="+studyNo;
		}
		if(""!=name && undefined!=name){
			url         += "&name="+name;
		}
		if(""!=learningStyle && undefined!=learningStyle){
			url         += "&learningStyle="+learningStyle;
		}
		if(""!=studentStatus && undefined!=studentStatus){
			url         += "&studentStatus="+studentStatus;
		}
		if(""!=graduateDate && undefined!=graduateDate){
			url         += "&graduateDateStr="+graduateDate;
		}
		if(""!=confirmGraduateDateb && undefined!=confirmGraduateDateb){
			url         += "&confirmGraduateDateb="+confirmGraduateDateb;
		}
		if(""!=confirmGraduateDatee && undefined!=confirmGraduateDatee){
			url         += "&confirmGraduateDatee="+confirmGraduateDatee;
		}
		navTab.openTab("RES_TEACHING_RESULT_PRINT_ALL_CHOOSE_PAGE",url,"选择打印范围");	
	}
    
	 //打印个性化成绩（广西医：gxykd；汕大:stdx）
	function printAllExamResult(printPage){
		
		var studentId = new Array();
		var alt_msg = "";
		var totalSize    = "${objPage.totalCount}";
		var schoolCode = "${schoolCode}";
		jQuery("#examResultsSearchBody input[name='resourceid']:checked").each(function(){
			studentId.push(jQuery(this).val());
		});
		var terms = [];
    	$('input[name="term"]:checked').each(function(){    
    		terms.push($(this).val());    
        });
    	
    	var termNames = [];
    	$('input[name="term"]:checked').each(function(){    
    		termNames.push($(this).attr("title"));    
        });
    	
    	var degreeUnitExam ;
    	var passExam;
    	$('input[name="degreeUnitExam"]:checked').each(function(){
    		degreeUnitExam = $(this).val();
        });
        $('input[name="passExam"]:checked').each(function(){
            passExam = $(this).val();
        });
    	var printDate = $("#searchExamResult_printDate").val();
    	//广西医科大为必填项
    	if(!(printDate!=null && printDate!="" && printDate!=undefined) && printPage=='gxykd'){
    		alertMsg.warn("请选择打印时间!");
    		return false;
		}
		if(studentId.length>0){
			totalSize = $("#examResultsSearchBody input[name='resourceid']:checked").size();
			//alt_msg = "确定要按照勾选条件打印"+totalSize+"个学生成绩吗？";
		}else {
			jQuery("#examResultsSearchBody input[name='resourceid']").each(function(){
				studentId.push(jQuery(this).val());
			});
			//alt_msg = "确定要按照查询条件打印"+totalSize+"个学生成绩吗？";
		}
		var assessMsg = "选择方式</br><input type='radio' name='isPdf'  value='' checked />  1、打印成绩。"+
		"</br> <input type='radio' name='isPdf'  value='Y' />  2、下载pdf文件。";
		alertMsg.confirm(assessMsg,{okCall:function(){
			var isPdf = $("input:radio[name='isPdf']:checked").val();
			if("Y"==isPdf){
				var url = "${baseUrl}/edu3/teaching/result/personalYearReportCard-print.html?printDate="+printDate+"&studentId="+studentId.toString()+"&terms="+terms+"&degreeUnitExam="+degreeUnitExam+"&passExam="+passExam+"&printPage="+printPage;
				downloadFileByIframe(url+"&isPdf=Y",'studentExam_exportIframe');
			}else{
				var url = "${baseUrl}/edu3/teaching/result/personalYearReportCard-view.html?printDate="+printDate+"&studentId="+studentId.toString()+"&terms="+terms+"&degreeUnitExam="+degreeUnitExam+"&passExam="+passExam+"&printPage="+printPage;
				$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
			}
					
		}});
	}
    
    //按班级导出成绩单
    function printExamResultsByClasses(){
        var terms = [];
        $('input[name="term"]:checked').each(function(){
            terms.push($(this).val());
        });

        var termNames = [];
        $('input[name="term"]:checked').each(function(){
            termNames.push($(this).attr("title"));
        });

        var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-by-classes.html?terms="+terms;
    	var studentStatus= $("#queryExamResultsSearchForm #query_examresults_studentStatus").val();

        var branchSchool = $("#queryExamResultsSearchForm #query_searchExamresult_brSchoolId").val();
        var gradeid      = $("#queryExamResultsSearchForm #query_searchExamresult_gradeid").val();
        var classicid      = $("#queryExamResultsSearchForm #query_searchExamresult_classic").val();
        var learningStyle  = $("#queryExamResultsSearchForm #query_examresults_learningStyle").val();
        var majorid        = $("#queryExamResultsSearchForm #query_searchExamresult_major").val();
        var classes		 = $("#searchExamResult_classesid").val();
        if (branchSchool != "" && branchSchool != undefined && gradeid != "" && gradeid != undefined) {
            url += "&branchSchool="+branchSchool;
        }else if(classes == "" || classes == undefined){
            alertMsg.warn("请选择教学点、年级或选择班级再进行导出！");
            return false;
        }
        if (gradeid != "" && gradeid != undefined) {
            url += "&gradeid="+gradeid;
        }
        if (classicid != "" && classicid != undefined) {
            url += "&classicid="+classicid;
        }
        if (learningStyle != "" && learningStyle != undefined) {
            url += "&learningStyle="+learningStyle;
        }
        if (majorid != "" && majorid != undefined) {
            url += "&majorid="+majorid;
        }
        if (classes != "" && classes != undefined) {
            url += "&classes="+classes;
        }
        if (studentStatus != "" && studentStatus != undefined) {
            url += "&studentStatus="+studentStatus;
        }
    	var degreeUnitExam ;
    	var passExam;
    	$('input[name="degreeUnitExam"]:checked').each(function(){    
    		degreeUnitExam = $(this).val();    
        });
        $('input[name="passExam"]:checked').each(function(){
            passExam = $(this).val();
        });
        if (degreeUnitExam != "" && degreeUnitExam != undefined) {
            url += "&degreeUnitExam="+degreeUnitExam;
        }
        if (passExam != "" && passExam != undefined) {
            url += "&passExam="+passExam;
        }

        var alt_msg = "确定要导出学生"+termNames+"的成绩吗？";
		alertMsg.confirm(alt_msg, {
            okCall: function(){            	
            	$('#frame_exportXlsStudentInfoList').remove();
            	var iframe = document.createElement("iframe");
            	iframe.id = "frame_exportXlsStudentInfoList";
            	iframe.src = url;
            	iframe.style.display = "none";
            	//创建完成之后，添加到body中
            	document.body.appendChild(iframe);
            }
		});
    }
    
    //打印所选学生的成绩单
	function printChooseStuReportCard(flag,printPage){
		var studentId = new Array();
		jQuery("#examResultsSearchBody input[name='resourceid']:checked").each(function(){
			studentId.push(jQuery(this).val());
		});
        var param = "flag="+flag;
        var printDate = $("#searchExamResult_printDate").val();
        param += "&printDate="+printDate;
        var degreeUnitExam ;
        var passExam;
        $('input[name="degreeUnitExam"]:checked').each(function(){
            degreeUnitExam = $(this).val();
        });
        $('input[name="passExam"]:checked').each(function(){
            passExam = $(this).val();
        });
        param += "&degreeUnitExam="+degreeUnitExam+"&passExam="+passExam;

        var terms = [];
        $('input[name="term"]:checked').each(function(){
            terms.push($(this).val());
        });
        param += "&terms="+terms;

        var termNames = [];
        $('input[name="term"]:checked').each(function(){
            termNames.push($(this).attr("title"));
        });
        param += "&printPage="+printPage;

        var classesid		 = $("#searchExamResult_classesid").val();
    	if(studentId.length>0){
            param += "&studentId="+studentId.toString();

    	}else{
            var branchSchool = $("#queryExamResultsSearchForm #query_searchExamresult_brSchoolId").val();
            var gradeid      = $("#queryExamResultsSearchForm #query_searchExamresult_gradeid").val();
            var classicid      = $("#queryExamResultsSearchForm #query_searchExamresult_classic").val();
            var teachingType  = $("#queryExamResultsSearchForm #query_examresults_learningStyle").val();
            var majorId        = $("#queryExamResultsSearchForm #query_searchExamresult_major").val();

            param += "&branchSchoolId="+branchSchool+"&gradeId="+gradeid+"&classicId="+classicid+"&teachingType="+teachingType+"&majorId="+majorId+"&classesid="+classesid;
    	}
        if("pass"==flag){
            var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?"+param;
            alertMsg.confirm("确定要打印所选学生的及格成绩单吗？", {
                okCall: function(){
                    $.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
                }
            });
            //打印所有成绩单
        }else if("all"==flag){
            var alt_msg = "确定要打印所选学生"+termNames+"的所有成绩单吗？";
            var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?"+param;
            alertMsg.confirm(alt_msg, {
                okCall: function(){
                    $.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
                }
            });
        }else if("fail"==flag){
            var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?"+param;
            alertMsg.confirm("确定要打印所选学生的不及格与无数据的成绩单吗？", {
                okCall: function(){
                    $.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
                }
            });
        } else if("gdy"==flag || "gdwy"==flag){// 广东医,广东外语
            var alt_msg = "确定要打印所选学生"+termNames+"的成绩单吗？";
            var url = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?"+param;
            alertMsg.confirm(alt_msg, {
                okCall: function(){
                    $.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
                }
            });
        }else if("stdx"==flag){//汕头大学：暂时未使用

            var courseid = $("#faceStudyExamResults_courseId").val();
            if(classes.length < 1){
                alertMsg.warn("请选择要打印的班级!");
                return false;
            }

            if(courseid.length < 1){
                alertMsg.warn("请选择要打印的课程!");
                return false;
            }
            param += "&courseid="+courseid;
            var alt_msg = "确定要打印所选学生的及格成绩单吗？";
            var url = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?"+param;
            alertMsg.confirm(alt_msg, {
                okCall: function(){
                    $.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
                }
            });
        }
	}
    
    //打印历年成绩单
	function printYearStuReportCard(){
		var studentId = new Array();
		var totalSize    = "${objPage.totalCount}";
		jQuery("#examResultsSearchBody input[name='resourceid']:checked").each(function(){
			studentId.push(jQuery(this).val());
		});
		var electronicalSign = "";
		if($("#searchExamResult_isUseElectronicalSign").attr("checked")){
			electronicalSign = $("#searchExamResult_isUseElectronicalSign").val();
		}
		var printDate = $("#searchExamResult_printDate").val();
		var terms = [];
    	$('input[name="term"]:checked').each(function(){    
    		terms.push($(this).val());    
        });
    	
    	var termNames = [];
    	$('input[name="term"]:checked').each(function(){    
    		termNames.push($(this).attr("title"));    
        });
    	var degreeUnitExam ;
    	var passExam;
    	$('input[name="degreeUnitExam"]:checked').each(function(){    
    		degreeUnitExam = $(this).val();    
        });
        $('input[name="passExam"]:checked').each(function(){
            passExam = $(this).val();
        });
    	if(studentId.length>0){
    		totalSize = $("#examResultsSearchBody input[name='resourceid']:checked").size();
    	}else{
    		jQuery("#examResultsSearchBody input[name='resourceid']").each(function(){
				studentId.push(jQuery(this).val());
			});
    		totalSize = studentId.length;
    	}
		var param = "?studentId="+studentId.toString()+"&terms="+terms+"&printDate="+printDate+"&electronicalSign="+electronicalSign+"&degreeUnitExam="+degreeUnitExam+"&passExam="+passExam;
    	var assessMsg = "选择方式</br><input type='radio' name='isPdf'  value='' checked />  1、打印成绩。"
			+"</br> <input type='radio' name='isPdf'  value='Y' />  2、下载pdf文件。"
			+"</br>将打印<font color='red'>"+totalSize+"</font>个学生"+termNames+"的历年成绩单";
		alertMsg.confirm(assessMsg,{okCall:function(){
			var isPdf = $("input:radio[name='isPdf']:checked").val();
			if("Y"==isPdf){
				var url = "${baseUrl}/edu3/teaching/result/personalYearReportCard-print.html"+param;
				downloadFileByIframe(url+"&isPdf=Y",'studentExam_exportIframe');
			}else{
				var url = "${baseUrl}/edu3/teaching/result/personalYearReportCard-view.html"+param;
				$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});
			}
		}});
		
	}
    
    // 上传成绩单签名
    function uploadSign() {
    	var url = "${baseUrl}/edu3/teaching/result/uploadSign.html";
    	$.pdialog.open(url,"RES_TEACHING_VIEW_STUDENT_RESULT_UPLOAD_SIGN","上传成绩单签名",{mask:true,width:800, height:600});	
    }

	function getParamUrl() {

	}
</script>
</body>
</html>