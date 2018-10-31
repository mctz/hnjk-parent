<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线录入成绩</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}";
		if(null!=msg&&""!=msg){
			alertMsg.warn(msg);
		}

		<c:if test="${fn:length(teachingPlanCourseList.result) > 0}">
		<gh:listResAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST" pageType="listRes" resourceCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT_SCORE">
		$("span[name='RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT_SCORE']").each(function(){
			$(this).show();
		});
		</gh:listResAuth>
		<gh:listResAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST" pageType="listRes" resourceCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT">
		$("span[name='RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT']").each(function(){
			$(this).show();
		});
		</gh:listResAuth>
		<gh:listResAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST" pageType="listRes" resourceCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT">
		$("span[name='RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT']").each(function(){
			$(this).show();
		});
		</gh:listResAuth>
		<gh:listResAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST" pageType="listRes" resourceCode="RES_TEACHING_RESULT_FACESTUDY_STUDENTSIGNATURE">
		$("span[name='RES_TEACHING_RESULT_FACESTUDY_STUDENTSIGNATURE']").each(function(){
			$(this).show();
		});
		</gh:listResAuth>
		</c:if>
		
		faceStudyExamResultsQueryBegin();
	});

	//打开页面或者点击查询（即加载页面执行）
	function faceStudyExamResultsQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "";
		var isBranchSchool = "${condition['isBranchSchool']}";
		if(isBranchSchool=='Y'){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeId']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['teachingtype']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classesId']}";
		var selectIdsJson = "{unitId:'branchSchool',gradeId:'query_examresults_gradeid',classicId:'classicid',teachingType:'teachingtype',majorId:'query_examresults_major',classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function faceStudyExamResultsQueryUnit() {
		var defaultValue = $("#branchSchool").val();
		var selectIdsJson = "{gradeId:'query_examresults_gradeid',classicId:'classicid',teachingType:'teachingtype',majorId:'query_examresults_major',classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function faceStudyExamResultsQueryGrade() {
		var defaultValue = $("#branchSchool").val();
		var gradeId = $("#query_examresults_gradeid").val();
		var selectIdsJson = "{classicId:'classicid',teachingType:'teachingtype',majorId:'query_examresults_major',classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function faceStudyExamResultsQueryClassic() {
		var defaultValue = $("#branchSchool").val();
		var gradeId = $("#query_examresults_gradeid").val();
		var classicId = $("#classicid").val();
		var selectIdsJson = "{teachingType:'teachingtype',majorId:'query_examresults_major',classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function faceStudyExamResultsQueryTeachingType() {
		var defaultValue = $("#branchSchool").val();
		var gradeId = $("#query_examresults_gradeid").val();
		var classicId = $("#classicid").val();
		var teachingTypeId = $("#teachingtype").val();
		var selectIdsJson = "{majorId:'query_examresults_major',classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function faceStudyExamResultsQueryMajor() {
		var defaultValue = $("#branchSchool").val();
		var gradeId = $("#query_examresults_gradeid").val();
		var classicId = $("#classicid").val();
		var teachingTypeId = $("#teachingtype").val();
		var majorId = $("#query_examresults_major").val();
		var selectIdsJson = "{classesId:'faceStudyExamResults_classesid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	
	//合法性检查
	function isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,pcteachtype,courseName,teacherid){
		if("Y"!=isAllowInputExamResults){
			alertMsg.warn("考试批次未关闭，或者成绩录入未开放，不允许导出或录入成绩单！");
			return false;
		}
		/* if(isBranchSchool!="Y"){
			alertMsg.warn("你不是教学站教务员，无法录入成绩");
			return false;
		} */
		//console.log(teacherid);
		if('${user.resourceid}'!=teacherid){//增加登分老师的判断
			if(!isAllow() && isBranchSchool=="Y"){//管理员有此权限
				alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
				return false;
			}
		}
		
		/* if(pcteachtype=="networkstudy"){		
			alertMsg.warn("课程《"+courseName+"》不是面授课程");
			return false;
		}	 */
		return true;
	}
	//导出空白成绩单
	function exportFaceStudyExamResultsTranscripts(teachingPlanCourseId,courseName,teachType,flag,classesid,gradeId,classesname,teacherid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		//var gradeId= "${condition['gradeid']}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,flag,courseName,teacherid)){
			var url = "${baseUrl }/edu3/teaching/transcripts/facestudy/download.html?teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid+"&gradeId="+gradeId+"&classesname="+encodeURIComponent(encodeURIComponent(classesname));
			alertMsg.confirm("确定下载《"+courseName+"》的成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'faceStudyCourseListForDownloadExportIframe');
				}
			});	
		}		
	}
	//导入成绩单
	function importFaceStudyExamResultsTranscripts(teachingPlanCourseId,courseName,teachType,flag,classesid,gradeId,unitId,teacherid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,flag,courseName,teacherid)){
			var url = "${baseUrl}/edu3/teaching/transcripts/facestudy/upload-showpage.html?teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid+"&gradeId="+gradeId+"&unitId="+unitId+"&teachType="+teachType;
			$.pdialog.open(url,"RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_IMPORT",courseName+"成绩导入",{width:900, height:640});		
		}		
	}
	//在线录入
	function inputFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,flag,classesid,gradeId,unitId,facestudyScorePer,teacherid,examForm){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		//var gradeId= "${condition['gradeid']}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,flag,courseName,teacherid)){
			var url = "${baseUrl}/edu3/teaching/result/facestudy/input-examresults-list.html?teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid+"&gradeid="+gradeId+"&unitId="+unitId+"&facestudyScorePer="+facestudyScorePer+"&courseTeachType="+teachType+"&courseExamForm="+examForm;
			navTab.openTab("RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_ONLINE",url,courseName+"成绩录入");	
		}
	}
	//提交成绩
	function submitFaceStudyExamResultslist(teachingPlanCourseId,courseName,teachType,pcteachtype,classesid,gradeId,unitId,teacherid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		//var gradeId = "${condition['gradeid']}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,pcteachtype,courseName,teacherid)){
			var url = "${baseUrl}/edu3/teaching/result/facestudy/input-examresults-submit.html";
			alertMsg.confirm("您确定要提交《"+courseName+"》的成绩到考务办吗？", {
				okCall: function(){
					jQuery.post(url,{teachingPlanCourseId:teachingPlanCourseId,examSubId:examSubId,classesId:classesid,gradeId:gradeId,unitId:unitId,teachType:teachType},function(resultData){
						var msg 	   		= resultData['message'];
					  	var statusCode 		= resultData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
					  		var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				}
			});	
		}			
	}
	//查看打印
	function printFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,flag,classesid,gradeId,teacherid,examform){	
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var examInfoId = "${examInfo.resourceid}";
		var branchSchool = "${condition['branchSchool']}";
		//alert(teachType+";"+flag)
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,flag,courseName,teacherid)){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print-view.html?gradeid="+gradeId+"&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&teachType="+teachType+"&flag="+flag+"&classesId="+classesid+"&branchSchool="+branchSchool+"&examInfoId="+examInfoId+"&examform="+examform+"&isShowUnitCode=Y";
			alertMsg.confirm("确定打印《"+courseName+"》的总评成绩单吗？", {
				okCall: function(){
					<c:choose>
					<c:when test="${isallsubmit == '1'}">
					var url2 = "${baseUrl}/edu3/teaching/result/examresults-check-view.html?gradeid="+gradeId+"&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&teachType="+teachType+"&flag="+flag+"&classesId="+classesid+"&branchSchool="+branchSchool;
					jQuery.post(url2,{teachingPlanCourseId:teachingPlanCourseId,teachType:teachType,flag:flag,examSubId:examSubId,classesId:classesid,gradeId:gradeId,examform:examform},function(resultData){
						var msg 	   		= resultData['message'];
					  	var statusCode 		= resultData['statusCode'];
					  	if(statusCode==200){
							if(isAllow()){
								$.pdialog.open(url, "RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT", courseName+"成绩打印", {width:800,height:600});
							}else{
								alertMsg.info("不是学校中心人员，不允许操作此功能！");
							}
						}else{
							alertMsg.warn(msg);
							return;
						}
					},"json");
					</c:when>
					<c:otherwise>
					if(isAllow()){
						$.pdialog.open(url, "RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT", courseName+"成绩打印", {width:800,height:600});
					}else{
						alertMsg.info("不是学校中心人员，不允许操作此功能！");
					}
					</c:otherwise>
					</c:choose>
				}
			});		
		}
	}
	//导出面授成绩
	function exportFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,flag,classesid,gradeId,teacherid){	
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var branchSchool = "${condition['branchSchool']}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,flag,courseName,teacherid)){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print.html?gradeid="+gradeId+"&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&flag="+flag+"&teachType="+teachType+"&operatingType=export&classesId="+classesid+"&branchSchool="+branchSchool+"&isShowUnitCode=Y";
			alertMsg.confirm("请选择导出文件类型：<br><br> <select id='fileType'><option value='pdf'>PDF</option>" +
				"<c:if test="${schoolCode ne '10560'}"><option value='doc'>Word</option><option value='xls'>Excel</option></c:if></select>", {
				okCall: function(){
				    var fileType = $("#fileType").val();
					if(isAllow()){
						downloadFileByIframe(url+"&fileType="+fileType,'exportFaceStudyCourseExamResultsForDownloadExportIframe');
					}else{
						alertMsg.info("不是学校中心人员，不允许操作此功能！");
					}
				}
			});		
		}
	}
	
	function isAllow(){
		var isAllow = "${condition['isAllow']}";
		if("Y" == isAllow){
			return true;
		}else{
			return false;
		}
	}
	
	function ajaxFreshClasses(){
		var grade = $("#faceStudyExamResults_grade").val();
		var brschool = "${condition['branchSchool']}";
		var class_id = $("#faceStudyExamResults_classesid").val();
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClasses.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,class_id:class_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#faceStudyExamResults_classesid").html(data['classes']);
				}
			}
		});
	}


	/* function brschool_Major_Classes(){
		var unitId = $("#stuchangeinfo_list_brSchoolName").val();
		var majorId = $("#stuChangeInfo #major").val();
		var classId = $("#stuChangeInfo #stuchangeinfo_list_class").val();
		var url = "${baseUrl}/edu3/framework/register/stuchangeinfo/brschool_Major_Classes.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId,classId:classId},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	$("#stuChangeInfo #major").replaceWith("<select  id=\"major\" name=\"major\" style=width:55%  onchange=\"brschool_Major_Classes()\">"+data['majorOption']+"</select>");
			    	$("#stuChangeInfo #stuchangeinfo_list_class").replaceWith("<select  id=\"stuchangeinfo_list_class\" name=\"stuchangeinfo_list_class\" style=width:55% onchange=\"brschool_Major_Classes()\" >"+data['classesOption']+"</select>");
				}
			}
		});
		
	} */

	function printstudentSignature(gradeid,classesid,courseid,courseName){
		var url = "${baseUrl}/edu3/teaching/result/studentSignature-print-view.html?gradeid="+gradeid+"&classesid="+classesid+"&courseid="+courseid;
		alertMsg.confirm("确定下载考生签到表吗？注意:请打印两份，一份存教务科，一份与试卷装订",{
				okCall:function(){
					$.pdialog.open(url, "RES_TEACHING_RESULT_FACESTUDY_STUDENTSIGNATURE", courseName+"考生签到表", {width:800,height:600});
					
				}
		});
	}

	//根据成绩状态查询条筛选数据
	function updateFormForStatus(value) {
        resultStatus = value;
        alert(resultStatus);
    }

	function validateForm(form){
		var examSubId = $("#faceStudyResults_ExamSub").val();
		if(examSubId==null || examSubId==''){
			alertMsg.warn("请选择学期！");
			return false;
		}else{
			return navTabSearch(form);
		}
	}
</script>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="faceStudyExamResultsCourseSearchForm"
				onsubmit="return validateForm(this);"
				action="${baseUrl}/edu3/teaching/examresults/facestudy-course-list_newedition.html"
				method="post">
				<input name="fromPage" value="Y" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <li>
					<label>年度：</label>
					<gh:selectModel id="nonyearId" name="yearId" bindValue="resourceid" displayValue="yearName" 
							modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearId']}" orderBy="firstyear desc" style="width:55%"/>
               	</li>
               	 <li>
					<label>学期：</label>
					<gh:select id="nonterm" name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:55%"/>
				</li>	 --%>
						<li><label>学期：<span style="color: red;">(必选)</span></label>
							<gh:semesterAutocomplete name="examSubId"
								id="faceStudyResults_ExamSub" tabindex="1" displayType="code"
								defaultValue="${condition['examSubId']}" style="width:130px;"></gh:semesterAutocomplete><span
							style="color: red;">*</span></li>
						<%-- <c:if
							test="${condition['isAllow'] eq 'N' || condition['isSchoolCenterTeacher'] eq 'Y' }">
							
						</c:if> --%>
						<li class="custom-li"><label>教学站：</label> <span sel-id="branchSchool"
								sel-name="branchSchool"
								sel-onchange="faceStudyExamResultsQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- <c:if test="${condition['isAllow'] eq 'Y' }">
							<input type="hidden" id="branchSchool" name="branchSchool"
								value="${condition['branchSchool'] }" />
						</c:if> --%>

						<li><label>年级：</label> <span
							sel-id="query_examresults_gradeid" sel-name="gradeId"
							sel-onchange="faceStudyExamResultsQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classicid"
							sel-name="classic"
							sel-onchange="faceStudyExamResultsQueryClassic()"
							sel-style="width: 100px"></span></li>
					</ul>
					<ul class="searchContent">
						<li><label>学习方式：</label> <span sel-id="teachingtype"
							sel-name="teachingtype"
							sel-onchange="faceStudyExamResultsQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li>
						<li id="graduateudit-gradeToMajor4" class="custom-li"><label>专业：</label> <span
							sel-id="query_examresults_major" sel-name="major"
							sel-onchange="faceStudyExamResultsQueryMajor()"
							sel-classs="flexselect" ></span></li>
						<li><label>成绩状态：</label> <select id="resultStatus"
							name="resultStatus" style="width: 53%">
								<option value="allStatus">请选择</option>
								<option value="partSave"
									${condition['resultStatus']=='partSave'?'selected':''}>部分保存</option>
								<option value="partSubmit"
									${condition['resultStatus']=='partSubmit'? 'selected':'' }>部分提交</option>
								<option value="partPublish"
									${condition['resultStatus']=='partPublish'? 'selected':'' }>部分发布</option>
								<option value="allSave"
									${condition['resultStatus']=='allSave'? 'selected':'' }>全部保存</option>
								<option value="allSubmit"
									${condition['resultStatus']=='allSubmit'? 'selected':'' }>全部提交</option>
								<option value="allPublish"
									${condition['resultStatus']=='allPublish'? 'selected':'' }>全部发布</option>
						</select></li>
						<li><label>考核方式：</label> <gh:select id="examClassType"
								name="examClassType" dictionaryCode="CodeExamClassType"
								value="${condition['examClassType']}"
								filtrationStr="1,2,4,5,6" style="width:100px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label style="width:40px;">课程：</label>
						<gh:courseAutocomplete name="courseId" tabindex="1"
								id="examResultsManager_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:170px;" /></li>
						<%-- --%>
						<li class="custom-li"><label>班级：</label> <span
							sel-id="faceStudyExamResults_classesid" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li>
							<label>教学形式：</label>
							<gh:select id="faceStudyExamResults_courseTeachType" name="courseTeachType"
									   value="${condition['courseTeachType'] }"  dictionaryCode="CodeCourseTeachType" style="width:120px;" />
						</li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button id="searchGData" type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="158">
				<thead>
					<tr>
						<c:if test="${'10560' eq schoolCode }">
							<th width="5%" style="text-align: center; vertical-align: middle;">序号</th>
						</c:if>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">教学站</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">上课学期</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">授课教师</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">教学形式</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">考核方式</th>
						<th width="13%"
							style="text-align: center; vertical-align: middle;">班级</th>
						<th width="7%" style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">建议学期</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">保/提/发</th>
						<th width="4%" style="text-align: center; vertical-align: middle;">录/总</th>
						<!-- <th width="4%" style="text-align: center;">导出成绩</th> -->
						<th width="5%" style="text-align: center;">导入Excel成绩</th>
						<th width="5%" style="text-align: center;">在线录入成绩</th>
						<th width="5%" style="text-align: center;">考生签到表</th>
						<th width="6%" style="text-align: center;">总评成绩</th>
					</tr>
				</thead>
				<tbody id="faceStudyExamResultsCourseBody">
					<c:forEach items="${teachingPlanCourseList.result}" var="t" varStatus="vs">
						<%--<c:if test="${not empty condition[t.complexresourceid] }"> --%>
						<%--<c:if test="${condition['resultStatus'] == 'allStatus' || t.resultStatus == condition['resultStatus'] }">--%>
						<tr>
							<c:if test="${'10560' eq schoolCode }">
								<td style="text-align: center; vertical-align: middle;" title="${t.classcode}">${t.classcode}</td>
							</c:if>
							<td style="text-align: center; vertical-align: middle;" title="${t.unitcode}-${t.unitname}"> ${t.unitname}</td>
							<td style="text-align: center; vertical-align: middle;">
								${fn:split(t.coursestatusterm,"_")[0]}年${fn:split(t.coursestatusterm,"_")[1] =='01' ?'春季':'秋季'}
								<%--${examSub.term eq '1' ?examSub.yearInfo.firstYear:(examSub.yearInfo.firstYear+1)}年${examSub.term eq '1' ?"秋季":"春季"}<%/*${examSub.batchName }*/%>--%>
							</td>
							<td style="text-align: center; vertical-align: middle;" title="${t.lecturername}">
								${t.lecturername}</td>
							<td style="text-align: center; vertical-align: middle;" title="${t.teachtype}">
								${ghfn:dictCode2Val('CodeCourseTeachType',t.teachtype)}</td>
							<td style="text-align: center; vertical-align: middle;">
								${ghfn:dictCode2Val('CodeExamClassType',t.examclasstype)}</td>
							<td style="text-align: center; vertical-align: middle;" title="${t.classesname}">
								${t.classesname}</td>
							<%--  <td  style="text-align: center;vertical-align: middle;">${t.course.courseCode}</td>--%>
							<td style="text-align: center; vertical-align: middle;" title="${t.coursecode }-${t.coursename}">${t.coursename}</td>
							<td style="text-align: center; vertical-align: middle;" title="${ghfn:dictCode2Val('CodeTermType',t.term) }">${ghfn:dictCode2Val('CodeTermType',t.term) }</td>
							<%--  <td  style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('teachType',t.teachType) }</td>--%>
							<%--<td style="text-align: center;vertical-align: middle;">${condition[t.complexresourceid] }</td> --%>
							<td style="text-align: center; vertical-align: middle;" title="保存状态${t.checkStatus0 }人；提交状态${t.checkStatus1 }人；发布状态${t.checkStatus4 }人">
								<font color="red">${t.checkStatus0 }</font>/<font color="green">${t.checkStatus1 }</font>/${t.checkStatus4 }</td>
							<td style="text-align: center; vertical-align: middle;">
								<c:if test="${t.inputcount==0 }">
									<span style="color: red; line-height: 21px; vertical-align: middle;">
								</c:if>
								<c:if test="${t.checkStatus4== t.inputTotalNum }">
									<span style="color: green; line-height: 21px; vertical-align: middle;">
								</c:if>
								${t.inputcount }/${empty t.inputTotalNum?0:t.inputTotalNum }
								<c:if test="${t.inputcount==0 ||t.checkStatus4== t.inputTotalNum}">
									</span>
								</c:if>
							</td>
							<!-- <td style="text-align: center; vertical-align: middle;"></td> -->
							<td style="text-align: center; vertical-align: middle;">
								<span name="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT" style="display: none;"> <a href="javaScript:void(0)" title="导出学生成绩模版"
									onclick="exportFaceStudyExamResultsTranscripts('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype}','${t.classesid }','${t.gradeid }','${t.classesname}','${t.teacherid}')">导出</a>
								</span>
								<c:if test="${!(t.facestudyScorePer eq 100.0) && !(t.examForm eq '3' )}">
									<a href="javaScript:void(0)" title="导入学生成绩"
									   onclick="importFaceStudyExamResultsTranscripts('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype}','${t.classesid }','${t.gradeid }','${t.unitId }','${t.teacherid}')"> / 导入</a>
								</c:if>
							</td>
							<td style="text-align: center; vertical-align: middle;">
								<a href="javaScript:void(0)" title="在线录入学生成绩"
								onclick="inputFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype}','${t.classesid }','${t.gradeid }','${t.unitId }','${t.facestudyScorePer}','${t.teacherid}','${t.examForm }')">录入</a>
								/ <a href="javaScript:void(0)" title="提交${examInfo.courseName}的成绩到考务办"
								onclick="submitFaceStudyExamResultslist('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype}','${t.classesid }','${t.gradeid }','${t.unitId }','${t.teacherid}')">提交</a>
							</td>
							<td style="text-align: center; vertical-align: middle;"><span
								name="RES_TEACHING_RESULT_FACESTUDY_STUDENTSIGNATURE"
								style="display: none;"> <a href="javaScript:void(0)"
									onclick="printstudentSignature('${t.gradeid }','${t.classesid }','${t.courseid}','${t.coursename }')">打印</a>
							</span></td>
							<td style="text-align: center; vertical-align: middle;">
								<span name="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT" style="display: none;">
									 <a href="javaScript:void(0)" title="打印预览"
										onclick="printFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype }','${t.classesid }','${t.gradeid }','${t.teacherid}','${t.examform }')">打印
									 </a>
								</span>
								<span name="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_EXPORT_SCORE" style="display: none;">
									<a href="javaScript:void(0)" title="可导出Word、Excel、PDF"
										onclick="exportFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.pcteachtype }','${t.classesid }','${t.gradeid }','${t.teacherid}')"> /
										<c:if test="${schoolCode eq '10560'}">导出打印</c:if><c:if test="${schoolCode ne '10560'}">导出</c:if>
									</a>
								</span>
							</td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${teachingPlanCourseList }"
				goPageUrl="${baseUrl }/edu3/teaching/examresults/facestudy-course-list_newedition.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>