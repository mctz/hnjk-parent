<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补考成绩录入</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}";
		if(null!=msg&&""!=msg){
			alertMsg.warn(msg);
		}
		if("${examInfo}"!=null && "${examInfo}"!="" && "11078"=="${schoolCode}"){//广大
			var option = {resizable:true,drawable:true};
			alertMsg.confirm("${examInfo}"+"${courseInfo}");
		}
		//ajaxExamSubByGrade();
		failexam_courseQueryBegin();
	});

	//打开页面或者点击查询（即加载页面执行）
	function failexam_courseQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "";
		var isBranchSchool = "${condition['isBranchSchool']}";
		if(isBranchSchool=='Y'){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeId']}";
		var classicId = "${condition['classicId']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classesId']}";
		var selectIdsJson = "{unitId:'failexam_brSchoolName',gradeId:'failExam_grade',classicId:'failexam_classic',majorId:'failexam_major',classesId:'failexam_classesid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function failexam_courseQueryUnit() {
		var defaultValue = $("#failexam_brSchoolName").val();
		var selectIdsJson = "{gradeId:'failExam_grade',classicId:'failexam_classic',majorId:'failexam_major',classesId:'failexam_classesid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function failexam_courseQueryGrade() {
		var defaultValue = $("#failexam_brSchoolName").val();
		var gradeId = $("#failExam_grade").val();
		var selectIdsJson = "{classicId:'failexam_classic',majorId:'failexam_major',classesId:'failexam_classesid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function failexam_courseQueryClassic() {
		var defaultValue = $("#failexam_brSchoolName").val();
		var gradeId = $("#failExam_grade").val();
		var classicId = $("#failexam_classic").val();
		var selectIdsJson = "{majorId:'failexam_major',classesId:'failexam_classesid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId,"", "", "", selectIdsJson);
	}
	
	// 选择专业
	function failexam_courseQueryMajor() {
		var defaultValue = $("#failexam_brSchoolName").val();
		var gradeId = $("#failExam_grade").val();
		var classicId = $("#failexam_classic").val();
		var majorId = $("#failexam_major").val();
		var selectIdsJson = "{classesId:'failexam_classesid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
	}
	
	//合法性检查
	function isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName){
		if("Y"!=isAllowInputExamResults){
			alertMsg.warn("考试批次未关闭，或者成绩录入未开放，不允许导出或录入成绩单！");
			return false;
		}
		
		/*if(isBranchSchool!="Y"){
			alertMsg.warn("你不是教学站教务员，无法录入成绩");
			return false;
		}
		*/
		 if(!isAllow() && isBranchSchool=="Y"){//管理员有此权限
				alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
				return false;
			}
		if(teachType=="networkstudy"){		
			alertMsg.warn("课程《"+courseName+"》不是面授课程");
			return false;
		}	
		return true;
	}
	
	function isAllow(){
		var isAllow = "${condition['isAllow']}";
		if("N" == isAllow){
			return false;
		}else {
			return true;
		}
	}
	
	//查询条件约束检查
	function isValidateNonInput(){
		
		var gradeId = $("#failExam_grade").val();
		var nonexamType = $("#nonexamType").val();
		var schoolCode = "${schoolCode}";
		/*if(''==gradeId){
			gradeId = "${condition['gradeId']}";
		}*/

		//var examSubId = $("#failexam_ExamSub").val();
		//var examResult = $("#examResult").val();
		
		//alert("gradeId:" +gradeId +" classesid:" + classesid + " major:" + major + " examSubId:" + examSubId);
		
 		if(''==nonexamType || nonexamType==null){
 			if(schoolCode == '11078'){
 				alertMsg.warn("考试类型不能为空!");
 	 			return false;
 			}
 		}
		return true;
	}
	
	//导出空白成绩单
	function exportfailExamResultsTranscripts(examSubId,gradeid,classesid,majorid,unitid,courseid,coursename,plansourceid){
		
		//var gradeId = $("#failExam_grade").val();
		//if(''==gradeId){
		//	gradeId = "${condition['gradeId']}";
		//}
		//var classesid = $("#failExam_classesid").val();
		var teachType = "facestudy";
		
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		//var isBranchSchool = "${condition['isBranchSchool']}";
		//var examSubId = "${examSub.resourceid}";
		//var examSubId = $("#failExamResults_ExamSub").val();
		//var examSubId = $("#failexam_ExamSub").val();
		
		//var gradeId= "${condition['gradeid']}";
		//if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
		if(isAllow()){
			//if(isValidateNonInput()){
				var url = "${baseUrl }/edu3/teaching/transcripts/facestudy/faildownload.html?gradeid="+gradeid+"&examSubId="+examSubId
				+"&classesid="+classesid+"&majorid="+majorid+"&unitid="+unitid+"&courseid="+courseid+"&plansourceid="+plansourceid;
				alertMsg.confirm("确定下载《"+coursename+"》的成绩单？", {
					okCall: function(){
						downloadFileByIframe(url,'faceStudyCourseListForDownloadExportIframe');
					}
				});	
			//}
			
		}else {
			alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
			return false;
		}		
	}
	
	//导入成绩单
	function importfailExamResultsTranscripts(examSubId,gradeid,classesid,majorid,unitid,courseid,coursename,plansourceid,isReachTime,examinputstarttime){
		if(isReachTime!='Y'){
			alertMsg.warn("该课程不在录入时间范围内,该课程开始录入时间为"+examinputstarttime);
			return false;
		}
		//var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		//var isBranchSchool = "${condition['isBranchSchool']}";
		//var examSubId = "${examSub.resourceid}";
		
		//var gradeId = $("#failExam_grade").val();
		//if(''==gradeId){
		//	gradeId = "${condition['gradeId']}";
		//}
		//var classesid = $("#failExam_classesid").val();
		//var examSubId = $("#failExamResults_ExamSub").val();
		//var examSubId = $("#failexam_ExamSub").val();
		//if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
		if(isAllow()){
			var url = "${baseUrl}/edu3/teaching/transcripts/facestudy/failupload-showpage.html?gradeid="+gradeid+"&examSubId="+examSubId+
			"&classesid="+classesid+"&majorid="+majorid+"&unitid="+unitid+"&courseid="+courseid+"&plansourceid="+plansourceid;
			$.pdialog.open(url,"RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_IMPORT",coursename+"成绩导入",{width:900, height:640});		
		}else {
			alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
			return false;
		}
	}
	
	//在线录入
	function inputfailExamResults(examSubId,gradeid,classesid,majorid,unitid,courseid,courseName,plansourceid,isReachTime,examinputstarttime){
		if(isReachTime!='Y'){
			alertMsg.warn("该课程不在录入时间范围内,该课程开始录入时间为"+examinputstarttime);
			return false;
		}
		/*
		var gradeId = $("#failExam_grade").val();
		if(''==gradeId){
			gradeId = "${condition['gradeId']}";
		}
		var courseName = "${condition['courseName']}";
		var branchschoolid = "${condition['branchSchool']}";
		
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${condition['examSubId']}";
		//alert(examSubId);
		var teachType = "facestudy";
		var major = "${condition['major']}";
		*/
		//var gradeId= "${condition['gradeid']}";
		//if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			//if(isValidateNonInput()){
			if(isAllow()){
				//var url = "${baseUrl}/edu3/teaching/result/facestudy/non-input-examresults-list.html?teachingPlanCourseId="+
				//		teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid+"&gradeId="+gradeId+"&branchschoolid="+branchschoolid;
				$.ajax({
					type:'post',
					url:"${baseUrl}/edu3/teaching/examresults/checkLastFaileExam.html",
					data:{examSubId:examSubId,courseId:courseid,classesId:classesid},
					dataType:"json",
					cache:false,
					error:DWZ.ajaxError,
					success:function(data){
						if(data.statusCode==200){
							var url = "${baseUrl}/edu3/teaching/result/facestudy/non-input-examresults-list.html?examSubId="+examSubId+"&gradeId="+
							gradeid+"&classesId="+classesid+"&majorid="+majorid+"&branchschoolid="+unitid+"&courseid="+courseid+"&plansourceid="+plansourceid;
							navTab.openTab("RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST_ONLINE",url,courseName+"成绩录入");
						}else{
							alertMsg.warn(data.message);
							return false;
						}
					}
				}); 
			}else {
				alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
				return false;
			}
			
		//}
	}
	
	//提交成绩
	function submitnonFaceStudyExamResults(examSubId,gradeid,classesid,majorid,unitid,courseid,courseName,plansourceid){
		//var gradeId = $("#failExam_grade").val();
		//if(''==gradeId){
		//	gradeId = "${condition['gradeId']}";
		//}
		//var classesid = $("#failExam_classesid").val();
		//if(''==classesid){
		//	classesid = "${condition['classesId']}";
		//}
		//var teachType = "facestudy";
		//var courseName = "${condition['courseName']}";
		
		//var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		//var isBranchSchool = "${condition['isBranchSchool']}";
		
		//这个考试批次要替换成补考的
		//var examSubId = "${examSub.resourceid}";
		//var gradeId = "${condition['gradeid']}";
		//if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
		  //if(isValidateNonInput()){
			if(isAllow()){
			var url = "${baseUrl}/edu3/teaching/result/facestudy/non-input-examresults-submit.html";
			alertMsg.confirm("您确定要提交《"+courseName+"》的成绩到考务办吗？", {
				okCall: function(){
					jQuery.post(url,{examSubId:examSubId,plansourceId:plansourceid,classesId:classesid,gradeId:gradeid,major:majorid,branchSchool:unitid,courseId:courseid},function(resultData){	
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
		  }else {
			alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
			return false;
		}
		 //} 
	}
	
	//查看打印
	function printfailStudyExamResults(examSubId,gradeid,classesid,majorid,unitid,courseid,courseName,plansourceid,coursestatusid,counts){	
		var url = "${baseUrl}/edu3/teaching/result/faileExamresults-check-view.html";
		//var examSubId = $("#failexam_ExamSub").val();
		$.ajax({
			type:'post',
			url:url,
			data:{examSubId:examSubId,courseId:courseid,planCourceId:plansourceid,classesId:classesid},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['statusCode']==200){
					if(data['isAllow']=='Y') {
						url = "${baseUrl}/edu3/teaching/result/failexamresults-print-view.html?totalNum="+counts+"&gradeid="+gradeid+"&isShowUnitCode=Y"
						+"&examSubId="+examSubId+"&classesid="+classesid+"&majorid="+majorid+"&unitid="+unitid+"&courseid="+courseid+"&plansourceid="+plansourceid+"&coursestatusid="+coursestatusid;
						$.pdialog.open(url, "RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST_PRINT", courseName+"成绩打印", {width:800,height:600});
					} else {
						alertMsg.info("不是学校中心人员，不允许操作此功能！");
					}
				}else{
					alertMsg.error(data['message']);
				}
			}
		}); 
		//var gradeId = "${condition['gradeId']}";
		//var classesid = "${condition['classesid']}";
		//var courseName = "${condition['courseName']}";
		/*
		var gradeId = $("#failExam_grade").val();
		if(''==gradeId){
			gradeId = "${condition['gradeId']}";
		}
		var classesid = $("#failExam_classesid").val();
		if(''==classesid){
			classesid = "${condition['classesId']}";
		}
		*/
		//var teachType = "facestudy";
		
		//var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		//var isBranchSchool = "${condition['isBranchSchool']}";
		//var examSubId = "${examSub.resourceid}";
		//var branchSchool = "${condition['branchSchool']}";
	//	var examSubId = $("#failexam_ExamSub").val();
		//if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			//if(isValidateNonInput()){
		//	var url = "${baseUrl}/edu3/teaching/result/failexamresults-print-view.html?gradeid="+
		//		gradeid+"&examSubId="+examSubId+"&classesid="+classesid+"&majorid="+majorid+"&unitid="+unitid+"&courseid="+courseid+"&plansourceid="+plansourceid+"&coursestatusid="+coursestatusid;
		//	$.pdialog.open(url, "RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST_PRINT", courseName+"成绩打印", {width:800,height:600});
			//}
		//}                     
	}
	
	//导出面授成绩(总评成绩)
	function exportfailStudyExamResults(examSubId,gradeid,classesid,majorid,unitid,courseid,coursename,plansourceid,coursestatusid){

		var teachType = "facestudy";
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";

		if(isAllow()){
			var url = "${baseUrl}/edu3/teaching/result/failexamresults-print.html?gradeid="+
					gradeid+"&examSubId="+examSubId+"&classesid="+classesid+"&majorid="+majorid+"&unitid="+unitid
					+"&courseid="+courseid+"&plansourceid="+plansourceid+"&coursestatusid="+coursestatusid+"&operatingType=export";
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
		}else {
			alertMsg.warn("抱歉您没有该权限,不允许操作此功能！");
			return false;
		}

	}
	
	function ajaxFreshInFailExamByGrade(){
		var grade = $("#failExam_grade").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		
		var brschool = $("#failexam_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
		
		//var class_id = $("#failExam_classesid").val();
		var major_id = $("#failexam_major").val();
		if(''==major_id){
			major_id = "${condition['major']}";
		}
		
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failExam_classesid").html(data['classes']);
				}
			}
		}); 
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failexam_major").html(data['majorList']);
				}
			}
		}); 
	} 
	
	function ajaxFreshInFailExamByMajor(){
		var grade = $("#failExam_grade").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		
		var brschool = $("#failexam_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
		
		//var class_id = $("#failExam_classesid").val();
		var major_id = $("#failexam_major").val();
		if(''==major_id){
			major_id = "${condition['major']}";
		}
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		//alert("ajaxFreshInFailExamByMajor" + brschool);
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failExam_classesid").html(data['classes']);
				}
			}
		});
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failexam_major").html(data['majorList']);
				}
			}
		}); 
	}
	
	function ajaxFreshMajorInFailExamByBrschool(){
		var grade = $("#failExam_grade").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		var brschool = $("#failexam_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
	
		var major_id = $("#failexam_major").val();
		if(''==major_id){
			major_id = "${condition['major']}";
		}
		
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failExam_classesid").html(data['classes']);
				}
			}
		});
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#failexam_major").html(data['majorList']);
				}
			}
		}); 
		
	}
	
	$(document).ready(function(){
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		//orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,18,21,19,23,a11,b11");
//		 $("#failexam_brSchoolName").flexselect({
//			  specialevent:function(){brschool_Major();}
//		});
//		
//		brschool_Major(); 
		$("#failexam_brSchoolName").flexselect();
		$("#failexam_classesid").flexselect();
		$("#failexam_major").flexselect();
		
		
		<gh:listResAuth parentCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST" pageType="listRes" resourceCode="RES_TEACHING_RESULT_FACESTUDY_FAILSTUDENTSIGNATURE">
		$("span[name='RES_TEACHING_RESULT_FACESTUDY_FAILSTUDENTSIGNATURE']").each(function(){
			$(this).show();
		});
		</gh:listResAuth>
	});

	
	/*
	 打印补考名单签到表
	 */
	function printFailStudentSignature(examSubId,gradeid,classesid,majorid,unitid,courseid,courseName,examtype){
		var url = "${baseUrl}/edu3/teaching/result/failStudentSignature-print-view.html?examSubId="+examSubId+"&gradeId="+
		gradeid+"&classesId="+classesid+"&major="+majorid+"&branchschoolid="+unitid+"&courseId="+courseid;
		alertMsg.confirm("确定打印补考考生签到表吗？注意:请打印两份，一份存教务科，一份与试卷装订",{
				okCall:function(){
					$.pdialog.open(url, "RES_TEACHING_RESULT_FACESTUDY_FAILSTUDENTSIGNATU", courseName+" "+examtype+" 补考考生签到表", {width:800,height:600});
				}
		});			
	}

	//打印补考考试签到表new
	function printFailStudentSignature1(){
		var num = $("#failExamResultsCourseBody input[name='resourceid']:checked").size();
		
		var yearId 	 = $("#nonyearId").val();
		var branchSchool = $("#failexam_brSchoolName").val();
		var gradeId 	 =  $("#failExam_grade").val();;
		var major 		 = $("#failexam_major").val();
		var classesId    = $("#failexam_classesid").val();
		var term    	 = $("#nonterm").val();
		var examType 	 = $("#nonexamType").val();
		var courseId 	 = $("#coursestatus_forarrange_list_courseId").val();
		var failResultStatus 	= $("#failResultStatus").val();
		
		var url = "${baseUrl}/edu3/teaching/result/failStudentSignature-print-view1.html?&branchSchool="+branchSchool+"&yearId="+yearId+"&major="+major+"&gradeId="+gradeId+"&classesId="+classesId+"&term="+term+"&examType="+examType+"&courseId="+courseId+"&failResultStatus="+failResultStatus;
		if (yearId==null||yearId=="") {
	 		alertMsg.warn("请选择年度!");
	 	 	return false;
		}
		if (term==null||term=="") {
	 		alertMsg.warn("请选择学期!");
	 	 	return false;
		}
		//TODO 		
		if(num>0){
			var courseids = "";
			var classesids = "";
			var plansourceids = "";
			/* var gradeids = "";
			var teachplanids = "";
			var majorids = "";
			var unitids = ""; */
			var k = 0;
			var num = $("#failExamResultsCourseBody input[name='resourceid']:checked").size();
			
			if(num>0){//按照勾选条件审核
				 /* if (num>30) {
					alertMsg.warn('请勾选30条以下的数据进行操作');
					return;
				}  */
				$("#failExamResultsCourseBody input[@name='resourceid']:checked").each(function(){
					var checekObj = $(this);
					courseids += checekObj.attr("courseid");
		    		classesids += checekObj.attr("classesid");
		    		plansourceids += checekObj.attr("plansourceid");
		    		/* gradeids += checekObj.attr("gradeid");
					teachplanids += checekObj.attr("teachplanid");
		    		majorids += checekObj.attr("majorid");
					unitids += checekObj.attr("unitid"); */
			        if(k != num -1 ) {
			        	courseids += ",";
			    		classesids += ",";
			    		plansourceids += ",";
			    		/* gradeids += ",";
						teachplanids += ",";
			    		majorids += ",";
						courseids += ",";
						unitids += ","; */
			        }
			        k++;
			});
			
			//iframe.src = "${baseUrl }/edu3/register/studentinfo/exportExcel.html?selectedid="+res+"&exportType=1";
//			url += "&type=checked&courseids="+courseids+"&gradeids="+gradeids+"&majorids="+majorids+"&classesids="+classesids+"&teachplanids="+teachplanids+"&unitids="+unitids;
			url += "&type=checked&courseids="+courseids+"&classesids="+classesids+"&plansourceids="+plansourceids;
			//创建完成之后，添加到body中
			}
		}
			//创建完成之后，添加到body中
		
		alertMsg.confirm("确定打印补考考生签到表吗?",{
			okCall:function(){
				$.pdialog.open(url, "RES_TEACHING_RESULT_FAILSTUDY_PRINT_SINGNATURE", "补考考试签到表", {width:800,height:600});
			}
		});
	}

    /**
	 * 导出补考成绩录入情况
     */
	function failExamList2Excel(){
		var yearId= $("#nonyearId").val();
		var term=$("#nonterm").val();
		var examType=$("#nonexamType").val();
		var gradeId=$("#failExam_grade").val();
		var major=$("#failexam_major").val();
		var branchSchool=$("#failexam_brSchoolName").val();
		var failResultStatus=$("#failResultStatus").val();
		var classesId=$("#failexam_classesid").val();
		var courseId=$("#coursestatus_forarrange_list_courseId").val();
		
		var url = "${baseUrl}/edu3/teaching/examresults/failExamList2Excel.html?yearId="+yearId+"&term="+term+"&examType="+examType+"&gradeId="+gradeId+"&major="+major+"&branchSchool="+branchSchool+"&failResultStatus="+failResultStatus+"&classesId="+classesId+"&courseId="+courseId;
		alertMsg.confirm("确定按查询条件导出补考成绩列表吗？", {
			okCall: function(){				
				downloadFileByIframe(url,'exportFailExamList2ExcelForDownloadExportIframe');
			}
		});
	}

    /**
	 * 导出学生成绩 - 按查询或勾选条件
     */
	function exportFailExamResult() {
        var yearId= $("#nonyearId").val();
        var term=$("#nonterm").val();
        var examType=$("#nonexamType").val();
        if (yearId==null || yearId=="" || term==null || term=="") {
            alertMsg.warn("请选择年度和学期！");
            return false;
        }
        if (examType==null || examType=="") {
            alertMsg.warn("请选择考试类型！");
            return false;
        }
		var url = "${baseUrl}/edu3/teaching/examresults/exportFailExamResult.html"+getUrlByParam();
        if (url.indexOf("failResultStatus")>0 && url.indexOf("classesIds")<0) {
            alertMsg.warn("抱歉，系统暂不支持成绩状态查询，请勾选要导出的班级成绩再进行导出！");
            return false;
        }
        alertMsg.confirm("请选择导出文件类型：<br><br> <select id='fileType'><option value='pdf'>PDF</option>" +
            "<c:if test="${schoolCode ne '10560'}"><option value='doc'>Word</option><option value='xls'>Excel</option></c:if></select>", {
            okCall: function(){
                var fileType = $("#fileType").val();
                if(isAllow()){
                    downloadFileByIframe(url+"&fileType="+fileType,'exportFailExamResultIfram');
                }else{
                    alertMsg.info("不是学校中心人员，不允许操作此功能！");
                }
            }
        });
    }

    //获取查询条件
    function getUrlByParam() {
        var url = "";
        if(isChecked('resourceid','#failExamResultsCourseBody')){
            var classesIds = [];
            var courseIds = [];
            $("#failExamResultsCourseBody input[name='resourceid']:checked").each(function(){
                classesIds.push($(this).attr("classesid"));
                courseIds.push($(this).attr("courseid"));
            });
            url += "?classesIds="+classesIds.toString()+"&courseIds="+courseIds.toString()+"&"+$("#failExamCourseSearchForm").serialize();
        }else{
            url += "?"+$("#failExamCourseSearchForm").serialize();
        }
        return url;
    }

</script>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="failExamCourseSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examresults/failexam-course-list.html"
				method="post">
				<input name="fromPage" value="${condition['fromPage']}"
					type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel id="nonyearId"
								name="yearId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" orderBy="firstyear desc"
								style="width:120px;" /></li>
						<%-- <c:if test="${isadmin eq 'Y' }"> --%>
							<li class="custom-li"><label>教学站：</label> <span sel-id="failexam_brSchoolName"
								sel-name="branchSchool"
								sel-onchange="failexam_courseQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if> --%>
						<li><label>年级：</label> <span sel-id="failExam_grade"
							sel-name="gradeId" sel-onchange="failexam_courseQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="failexam_classic"
							sel-name="classicId" sel-onchange="failexam_courseQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li><label>学期：</label> <gh:select id="nonterm" name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:120px" /></li>
						<li class="custom-li"><label>专业：</label> <span sel-id="failexam_major"
							sel-name="major" sel-onchange="failexam_courseQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>成绩状态：</label> <select id="failResultStatus"
														 name="failResultStatus" style="width: 120px;">
							<option value="">请选择</option>
							<option value="notAllInput"
							${condition['failResultStatus']=='notAllInput'?'selected':''}>未录入</option>
							<option value="partSave"
							${condition['failResultStatus']=='partSave'?'selected':''}>部分保存</option>
							<option value="partSubmit"
							${condition['failResultStatus']=='partSubmit'? 'selected':'' }>部分提交</option>
							<option value="partPublish"
							${condition['failResultStatus']=='partPublish'? 'selected':'' }>部分发布</option>
							<option value="allSave"
							${condition['failResultStatus']=='allSave'? 'selected':'' }>全部保存</option>
							<option value="allSubmit"
							${condition['failResultStatus']=='allSubmit'? 'selected':'' }>全部提交</option>
							<option value="allPublish"
							${condition['failResultStatus']=='allPublish'? 'selected':'' }>全部发布</option>
						</select></li>
						<li>
							<label>教学形式：</label>
							<gh:select id="failexam_CourseTeachType" name="courseTeachType"
									   value="${condition['courseTeachType'] }"
									   dictionaryCode="CodeCourseTeachType" style="width:120px;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>考试类型：</label>
							<c:choose>
								<c:when test="${schoolCode eq '11078'}">
									<gh:select id="nonexamType" name="examType"
											   dictionaryCode="ExamResult" value="${condition['examType']}"
											   filtrationStr="Y,T,Q" style="width:120px" classCss="required" />
									<font color="red">*</font>
								</c:when>
								<c:when test="${schoolCode eq '10598'}">
									<gh:select id="nonexamType" name="examType"
											   dictionaryCode="ExamResult" value="${condition['examType']}"
											   filtrationStr="Y,T,Q,R,G" style="width:120px" />
								</c:when>
								<c:otherwise>
									<gh:select id="nonexamType" name="examType"
											   dictionaryCode="ExamResult" value="${condition['examType']}"
											   filtrationStr="Y,T,Q" style="width:120px" />
								</c:otherwise>
							</c:choose>
						</li>
						<li id="failexam-gradeToMajorToClasses4" class="custom-li"><label>班级：</label> <span
							sel-id="failexam_classesid" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li> &nbsp;&nbsp;课程：
							<gh:courseAutocomplete name="courseId" tabindex="1"
												   id="coursestatus_forarrange_list_courseId"
												   value="${condition['courseId']}" displayType="code"
												   isFilterTeacher="Y" style="width:160px" />
						</li>
						<li>
							<label>登分老师：</label>
							<input name="teacherName" value="${condition['teacherName']}" style="width: 120px;">
						</li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit" onclick="return isValidateNonInput();">
								查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="183" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all"
							onclick="checkboxAll('#check_all','resourceid','#failExamResultsCourseBody')" /></th>
						<th width="8%">课程名称</th>
						<%-- 
		        	<th width="8%" style="text-align: center;vertical-align: middle;">开课学期</th>  
		        	--%>
						<th width="8%">上课学期</th>
						<th width="14%">班级</th>
						<th width="4%">教学形式</th>
						<th width="4%">保/提/发</th>
						<th width="4%">录/总</th>
						<th width="6%">导出成绩</th>
						<th width="6%">导入成绩</th>
						<th width="6%">成绩录入</th>
						<th width="6%">考生签到表</th>
						<th width="10%">总评成绩</th>
					</tr>
				</thead>
				<tbody id="failExamResultsCourseBody">
					<c:forEach items="${failExamCourseList.result}" var="t"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.courseid}" teachplanid="${t.teachplanid}"
								gradeid="${t.gradeid}" courseid="${t.courseid}"
								classesid="${t.classesid}" plansourceid="${t.plansourceid}"
								majorid="${t.majorid}" unitid="${t.unitid}" autocomplete="off" /></td>
							<td>${t.coursename}</td>
							<%--<td  style="text-align: center;vertical-align: middle;">--%>
							<%--${fn:split(t.coursestatusterm,"_")[0]}年${fn:split(t.coursestatusterm,"_")[1] =='01' ?'春季':'秋季'}--%>
							<%--${fn:split(t.coursestatusterm,"_")[1] =='01' ?'春季':'秋季'}--%>
							<%--${examSub.term eq '1' ?examSub.yearInfo.firstYear:(examSub.yearInfo.firstYear+1)}年${examSub.term eq '1' ?"秋季":"春季"}<%/*${examSub.batchName }*/%>--%>
							<%--</td>--%>
							<!--  <td  style="text-align: center;vertical-align: middle;">${t.course.courseCode}</td>-->
							<td>${ghfn:dictCode2Val('CodeCourseTermType',t.courseterm) }</td>
							<td>${t.classesname }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTeachType',t.teachType)}</td>
							<td>${t.checkStatus0 }/${t.checkStatus1 }/${t.checkStatus4 }</td>
							<td><span style="line-height: 21px; vertical-align: middle;
									<c:if test="${t.inputcount==0 }">color: red;</c:if>
									<c:if test="${t.checkStatus4==t.counts }">color: green;</c:if>
									">${t.inputcount }/${t.counts }
								</span>
							</td>
							<td><a
								href="javaScript:void(0)"
								onclick="exportfailExamResultsTranscripts('${t.examSubId} ','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}')">导出</a>
							</td>
							<td><a
								href="javaScript:void(0)"
								onclick="importfailExamResultsTranscripts('${t.examSubId} ','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}','${t.isReachTime}','${t.examinputstarttime}')">导入</a></td>
							<td>
								<%--
			            	<a href="javaScript:void(0)"  onclick="inputfailExamResults('${t.resourceid}','${t.classesid }')">录入</a>
			            	 --%> <a href="javaScript:void(0)"
								onclick="inputfailExamResults('${t.examSubId} ','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}','${t.isReachTime}','${t.examinputstarttime}')">录入</a>
								/ <a href="javaScript:void(0)"
								onclick="submitnonFaceStudyExamResults('${t.examSubId} ','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}')"
								title="提交${t.coursename}的成绩到考务办">提交</a>
							</td>
							<td><span
								name="RES_TEACHING_RESULT_FACESTUDY_FAILSTUDENTSIGNATURE"
								style="display: none;"> <a href="javaScript:void(0)"
									onclick="printFailStudentSignature('${t.examSubId}','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${ghfn:dictCode2Val('ExamResult',t.examtype) }')">打印</a>
							</span></td>
							<td><gh:listResAuth
									parentCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST"
									pageType="listRes"
									resourceCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST_PRINT">
									<a href="javaScript:void(0)"
										onclick="printfailStudyExamResults('${t.examSubId}','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}','${t.coursestatusid}','${t.counts }')">打印</a>
									<a href="javaScript:void(0)" title="可导出Word、Excel、PDF"
									   onclick="exportfailStudyExamResults('${t.examSubId}','${t.gradeid}','${t.classesid}','${t.majorid}','${t.unitid}','${t.courseid}','${t.coursename}','${t.plansourceid}','${t.coursestatusid}')"> /
										<c:if test="${schoolCode eq '10560'}">导出打印</c:if><c:if test="${schoolCode ne '10560'}">导出</c:if>
									</a>
								</gh:listResAuth>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${failExamCourseList}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresults/failexam-course-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>