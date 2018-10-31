<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍信息管理</title>
<script type="text/javascript">
$(document).ready(function(){
	var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	orgStuStatus("#stuInfo #stuStatus",studentStatusSet,statusRes,"12,13,26,15,18,21,19,23,a11,b11,25");
	schoolrollQueryBegin();
});

//打开页面或者点击查询（即加载页面执行）
function schoolrollQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	//alert(isBrschool==true+";"+schoolId);
	var gradeId = "${condition['stuGrade']}";
	var classicId = "${condition['classic']}";
	var teachingType = "";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classesid']}";
	var selectIdsJson = "{unitId:'eiinfo_brSchoolName',gradeId:'stuGrade',classicId:'classic',majorId:'query_examresults_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function schoolrollQueryUnit() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var selectIdsJson = "{gradeId:'stuGrade',classicId:'classic',majorId:'query_examresults_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function schoolrollQueryGrade() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var selectIdsJson = "{classicId:'classic',majorId:'query_examresults_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function schoolrollQueryClassic() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var selectIdsJson = "{majorId:'query_examresults_major',classesId:'searchExamResult_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

//选择专业
function schoolrollQueryMajor() {
	var defaultValue = $("#eiinfo_brSchoolName").val();
	var gradeId = $("#stuGrade").val();
	var classicId = $("#classic").val();
	var teachingTypeId = "";
	var majorId = $("#query_examresults_major").val();
	var selectIdsJson = "{classesId:'searchExamResult_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
//跨学年复学
function strideStudy(){
	if(!isChecked('resourceid',"#infoBody")){
		alertMsg.warn('请选择一条记录！');
		return false;
 	}
	var stuid = $("#infoBody input[@name='resourceid']:checked").val();
	var status = $("#infoBody input[@name='resourceid']:checked").attr("alt");
	if('12' != status){
		alertMsg.warn('学籍状态不为休学！');
		return false;
	}
	
	var url = "${baseUrl}/edu3/register/studentinfo/strideStudy.html?stuid="+stuid;
	navTab.openTab('RES_SCHOOL_SCHOOLROLL_STRIDE_STUDY', url, '跨学年复学');
	//给本页加上重载的设定
	navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_MANAGER");
}

//组合学籍状态的方法(参数为:空白的select控件,原始的组合学籍状态集合,上次查询选择的值,过滤得到的学籍状态)
function orgStuStatus(selectid,studentStatusSet,statusRes,val){
	var html = "<option value=''>请选择</option>";	
	var status= studentStatusSet.split(",");
	var filter = val.split(",");
	for(var i=0;i<(status.length-1)/2;i++){
		for(var j=0;j<filter.length;j++){
			if(filter[j]==status[2*i]){
				if(statusRes==status[2*i]){
					html += "<option selected='selected' value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}else{
					html += "<option value='"+status[2*i]+"'>"+status[2*i+1]+"</option>";
				}
			}
		}
	}
	$(selectid).html(html);
}

//导入入学资格数据
function recruitStatusImport(){
	var url = "${baseUrl}/edu3/register/studentinfo/recruitStatusImport/form.html";
	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_IMPORT_RECRUITSTATUS","导入入学资格",{width:600, height:300});
}

//导出学信网
function exportxxweb(){
	var stuGrade 	 = $("#stuGrade").val();
	var branchSchool = $("#eiinfo_brSchoolName").val();
	var major 		 = $("#query_examresults_major").val();
	var classic 	 = $("#classic").val();
	var stuStatus    = $("#stuStatus").val();
	var stuGrade 	 = $("#stuGrade").val();
	var name 		 = $("#name").val();
	var certNum 	 = $("#certNum").val();
	var studyNo 	 = $("#matriculateNoticeNo").val();
	
	var rollCard     = $("#rollCard").val(); //是否提交学籍卡
	var classes      = $("#searchExamResult_classesid").val();//班级
	
	//20140102 林辉 提议不限制办学单位
	$('#frame_exportXlsStudentInfoList').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportXlsStudentInfoList";
	iframe.src = "${baseUrl }/edu3/register/studentinfo/exportxxweb.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&stuGrade="+stuGrade+"&name="+encodeURIComponent(encodeURIComponent(name))+"&studyNo="+studyNo+"&certNum="+certNum+"&rollCard="+rollCard+"&classes="+classes;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//打印考生信息
function printExameeInfoInRollPage(){
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #query_examresults_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #searchExamResult_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	var param = "&unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag;
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	var url = "${baseUrl}/edu3/register/studentinfo/exameeinfo/calulatePrintNum.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['over1000']=='1'){
				alertMsg.warn(data['question']+" 打印数目超过1000，暂不支持此数量级打印。");
				return false;
			}else{
				alertMsg.confirm(data['question'],{ 
				    okCall:function(){
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/exameeinfo/printview.html?studentId="+studentId.join(',')+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTEXAMSTUINFO','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
}

//学籍卡打印 
function studentRollCardPrintInRoll(){
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #query_examresults_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #searchExamResult_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag=print&id="+studentId.join(',');
	var url = "${baseUrl}/edu3/register/studentinfo/calulatePrintNumForStudentCard.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,fromStudentRoll:1},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['over500']=='1'){
				alertMsg.warn(data['question']+" 打印数目超过500，暂不支持此数量级打印。");
				return false;
			}else{
				alertMsg.confirm(data['question'],{
				    okCall:function(){
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/studentCard/print-view.html"+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTSTUDENTCARD','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
}

function backToRegister(){
	pageBarHandle("您确定要驳回这些已经注册了的学生吗？","${baseUrl}/edu3/register/studentinfo/back-to-registering.html","#infoBody");
}

function exportDivideClass(flag){
	flag = flag == undefined ? "" : flag;
	var studentId = [];
	//条件 查询
	var branchSchool 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classic        	= $("#stuInfo #classic").val();
	var major 	= $("#stuInfo #graduateudit-gradeToMajor4 #query_examresults_major").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	
	var stuGrade  	= $("#stuInfo #stuGrade").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var classesid  	= $("#stuInfo #graduateudit-gradeToMajorToClasses4 #searchExamResult_classesid").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	
	
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	
	
	alertMsg.confirm("您确定要根据勾选或查询条件（联系地址不纳入导出条件范围）导出"+("W"==flag?"未":"")+"分班吗?", {
		okCall: function(){	
			var param = "branchSchool="+branchSchool+"&classic="+classic+"&major="+major+"&stuGrade="+stuGrade
			+"&classesid="+classesid+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+encodeURIComponent(encodeURIComponent(name))+"&rollCard="+rollCard
			+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag="+flag+"&studentIds="+studentId.join(',');
			//alert(param);return false;
			$('#frame_exportXlsStudentDivideclass').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frame_exportXlsStudentDivideclass";
			iframe.src = "${baseUrl }/edu3/register/studentinfo/exportXlsDivideClass.html?"+param;
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);
		}
	});	
}

//导入分班信息xls
function imputDivideClass(){
	var url ="${baseUrl}/edu3/register/studentinfo/impDivideXlsView.html";
	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_MANAGER_UPLOAD","导入分班信息", {mask:true,width:450, height:210});
}

//导入学号xls
function importStudyNo(){
	var url ="${baseUrl}/edu3/roll/studentinfo/importStudyNoDialog.html";
	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_MANAGER_IMPORT_STUDYNO","导入学号", {mask:true,width:450, height:210});
}

//导出班级花名册信息xls
function exportClassesRoster(){
	var studentId = [];
	//条件 查询
	var branchSchool 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classic        	= $("#stuInfo #classic").val();
	var major 	= $("#stuInfo #query_examresults_major").val();
	var grade  	= $("#stuInfo #stuGrade").val();
	var classes  	= $("#stuInfo #searchExamResult_classesid").val();
	
	var branchSchool_txt 		= $("#stuInfo #eiinfo_brSchoolName_flexselect").val();
	var classic_txt        	= $("#stuInfo select[id='classic'] option[value='"+classic+"']").text();
	var major_txt 	        = $("#stuInfo #major_flexselect").val();
	var grade_txt  	= $("#stuInfo select[id='stuGrade'] option[value='"+grade+"']").text();
	var classes_txt = $("#stuInfo select[id='stuClasses'] option[value='"+classes+"']").text();
	
	if(branchSchool==""||classic==""||major==""||grade==""||classes==""){
		alertMsg.warn("请选择教学站、年级、层次、专业、班级后再导出！");
		return false;
	}
	
	alertMsg.confirm("导出花名册只根据教学站、年级、层次、专业、班级导出！您确定要导出花名册吗?", {
		okCall: function(){	
			
			var param = "branchSchool="+branchSchool+"&classic="+classic+"&major="+major+"&grade="+grade+"&classes="+
			             classes+"&branchSchool_txt="+encodeURIComponent(encodeURIComponent(branchSchool_txt))+
			             "&classic_txt="+encodeURIComponent(encodeURIComponent(classic_txt))+"&major_txt="+
			             encodeURIComponent(encodeURIComponent(major_txt))+
			             "&grade_txt="+encodeURIComponent(encodeURIComponent(grade_txt))+
			             "&classes_txt="+encodeURIComponent(encodeURIComponent(classes_txt));
			
			var url = "${baseUrl}/edu3/register/studentinfo/exportClassesRosterDiy.html?"+param;
			$.pdialog.open(url,'RES_SCHOOL_GRADUATESTU_CUSTOMEXPORT','毕业数据自定义导出信息',{width:500,height:280});
			/*
			var param = "branchSchool="+branchSchool+"&classic="+classic+"&major="+major+"&grade="+grade+"&classes="+classes+"&flag="+flag;
			$('#frame_exportClassesRoster').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frame_exportClassesRoster";
			iframe.src = "${baseUrl }/edu3/register/studentinfo/exportClassesRoster.html?"+param;
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);*/
		}
	});	
}
//学生证打印
function studentCardPrint(){
	var studentInfoIds = [];
	var studentInfoId = "";
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentInfoIds.push($(this).val());
	});
	if(studentInfoIds.length==0){
		alertMsg.warn("请勾选要打印学生证的学生。");
	}else{
		//var url          = "${baseUrl}/edu3/register/studentinfo/studentCardPrint.html?studentInfoIds="+studentInfoIds.toString();
		//var url = "${baseUrl}/edu3/register/studentinfo/studentCardPrint.html?studentInfoId="+studentInfoId;
		//$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_MANAGER","学生证打印与打印pdf",{width:300, height:70});
		var assessMsg = "选择方式</br><input type='radio' name='isPdf'  value='' checked />  1、打印学生证。"+
		"</br> <input type='radio' name='isPdf'  value='Y' />  2、下载打印学生证pdf。"+
		"</br> 照片来源  <select name='photoFrom' id='photoFrom'><option value='student' selected='true'>学籍库</option><option value='recruit'>录取库</option></select>";
		alertMsg.confirm(assessMsg,{okCall:function(){
			var isPdf = $("input:radio[name='isPdf']:checked").val();
			var photoFrom = $("select[name='photoFrom'] option:selected").val();
			if("Y"==isPdf){
				var url          = "${baseUrl}/edu3/student/graduate/studentCardPrint.html?studentInfoIds="+studentInfoIds.toString()+"&isPdf="+isPdf+"&photoFrom="+photoFrom;
				downloadFileByIframe(url,'stucentCard_print_exportIframe');
			}else{
				var url = "${baseUrl}/edu3/register/studentinfo/studentCardPrint.html?studentInfoIds="+studentInfoIds.toString()+"&isPdf="+isPdf+"&photoFrom="+photoFrom;
				$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_MANAGER","学生证打印与打印pdf",{width:800, height:600});
			}
		}});
	}
}

function joinTeachingPlan(){		
	pageBarHandle("您确定要关联教学计划吗？","${baseUrl}/edu3/register/studentinfo/jointeachingplan.html","#infoBody");
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #graduateudit-gradeToMajor4 #query_examresults_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #graduateudit-gradeToMajorToClasses4 #searchExamResult_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	var contactAddress  = $("#stuInfo #contactAddress").val();
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	alertMsg.confirm("您确定要关联教学计划吗？",{
	    okCall:function(){
	    	$.ajax({
	    		type:'POST',
	    		url:"${baseUrl}/edu3/register/studentinfo/jointeachingplan.html",
	    		data:{resourceid:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,contactAddress:contactAddress},
	    		dataType:"json",
	    		cache: false,
	    		error: DWZ.ajaxError,
	    		success: function(data){
    				alertMsg.info(data['message']);
	    		}
	    	});
	    }
    });
}
//打印在校生证明
function printconfirmation(){
	var stus="";
	var print = true;
	$("#infoBody input[name='resourceid']").each(function(){
		if($(this).attr("checked")){
			if($(this).attr("alt") == "13"){
				alertMsg.warn("退学的学生不允许打印！");
				print = false;
				return false;
			}
			stus += ""+$(this).val()+",";
		}
	});
	if(print && ""==stus){
		alertMsg.warn("请选择一条或多条记录！");return false;
	}
	if (print) {
		var url="${baseUrl}/edu3/register/studentinfo/print-confirmationview.html?stus="+stus;
		$.pdialog.open(url,"RES_TEACHING_GRADUATE_PRINT",'打印预览',{height:600, width:800});
	}
}
//学籍卡打印 双面
function studentRollCardPrintInRollTwosided(type){//1正面2反面
	
	var studentId = [];
	//条件 查询
	var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
	var classicId 	= $("#stuInfo #classic").val();
	var majorId 	= $("#stuInfo #query_examresults_major").val();
	var gradeId  	= $("#stuInfo #stuGrade").val();
	var classesId 	= $("#stuInfo #searchExamResult_classesid").val();
	var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
	var name  		= $("#stuInfo #name").val();
	var rollCard 	= $("#stuInfo #rollCard").val();
	var certNum  	= $("#stuInfo #certNum").val();
	var stuStatus 	= $("#stuInfo #stuStatus").val();
	var entranceFlag  = $("#stuInfo #entranceFlag").val();
	$("#infoBody input[@name='resourceid']:checked").each(function(){
		studentId.push($(this).val());
	});
	var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId+"&type="+type
	+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
	+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&fromStudentRoll=1&flag=print&id="+studentId.join(',');
	var url = "${baseUrl}/edu3/register/studentinfo/calulatePrintNumForStudentCard.html";
	$.ajax({
		type:'POST',
		url:url,
		data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,fromStudentRoll:1},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data['over500']=='1'){
				alertMsg.warn(data['question']+" 打印数目超过500，暂不支持此数量级打印。");
				return false;
			}else{
				alertMsg.confirm(data['question'],{
				    okCall:function(){
				    	$.pdialog.open(baseUrl+"/edu3/register/studentinfo/studentCardTwosided/print-view.html"+param,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTSTUDENTCARDTWOSIDED','打印预览',{height:600, width:800});
				    }
			    });
			}
		}
	});
	
}//$(document).ready   end 
	//导出双面学籍卡PDF
	function studentRollCardPDFDownload(){
		
		var studentId = [];
		//条件 查询
		var unitId 		= $("#stuInfo #eiinfo_brSchoolName").val();
		var classicId 	= $("#stuInfo #classic").val();
		var majorId 	= $("#stuInfo #query_examresults_major").val();
		var gradeId  	= $("#stuInfo #stuGrade").val();
		var classesId 	= $("#stuInfo #searchExamResult_classesid").val();
		var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
		var name  		= $("#stuInfo #name").val();
		var rollCard 	= $("#stuInfo #rollCard").val();
		var certNum  	= $("#stuInfo #certNum").val();
		var stuStatus 	= $("#stuInfo #stuStatus").val();
		var entranceFlag  = $("#stuInfo #entranceFlag").val();
		$("#infoBody input[@name='resourceid']:checked").each(function(){
			studentId.push($(this).val());
		});
		
		var param = "?unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
				+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
		+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&flag=print&studentids="+studentId.join(',')+"&fromStudentRoll=1";

		var url = "${baseUrl}/edu3/register/studentinfo/calulatePrintNumForStudentCard.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{id:studentId.join(','),unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlagL:entranceFlag,fromStudentRoll:1},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['over500']=='1'){
					alertMsg.warn("下载学籍卡PDF:"+data['question'].split(':')[1]+" 当前查询数据超过500条，请勾选或细化查询条件再进行下载！");
					return false;
				}else{
					alertMsg.confirm("下载学籍卡PDF:"+data['question'].split(':')[1],{
					    okCall:function(){
					    	url = "${baseUrl}/edu3/roll/studentCardTwosided/downloadPDF.html"+param;
					    	downloadFileByIframe(url,'stucentCard_pdf_downloadIframe');
					    }
				    });
				}
			}
		});
	}

  	// 生成学生缴费记录
	function generateStudentPayment() {
		var url = "${baseUrl}/edu3/register/studentinfo/generateStudentFee.html";
		alertMsg.confirm("您确定要给按查询条件查出的这些学生生成缴费记录吗？",{
		    okCall:function(){
		    	$.ajax({
					type:'POST',
					url:url,
					data:$("#studentInfoForm").serialize(),
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(data){
						if(data['statusCode']==200){
							alertMsg.correct(data['message']);
						}else{
							alertMsg.error(data['message']);
						}
					}
				});
		    }
	    });
	}

    // 打印学籍表（供安徽医使用）
	function printSchoolRollTable(){
		var studentIds = [];
		$("#infoBody input[@name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
		// 勾选
		if(studentIds.length > 0){
			alertMsg.confirm("打印学籍表:"+studentIds.length+"人",{
			    okCall:function(){
			    	var printUrl = "${baseUrl}/edu3/register/studentinfo/studentCard/printRollTable-view.html?exportType=1&studentIds="+studentIds.join(',');
			    	$.pdialog.open(printUrl,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTROLLTABLE','学籍表打印预览',{mask:true,height:600, width:800});
			    }
		    });
		}else { // 按查询条件
			var unitId = $("#stuInfo #eiinfo_brSchoolName").val();
			var classicId 	= $("#stuInfo #classic").val();
			var majorId 	= $("#stuInfo #query_examresults_major").val();
			var gradeId  	= $("#stuInfo #stuGrade").val();
			var classesId = $("#stuInfo #searchExamResult_classesid").val();
			// TODO:根据需求这三个信息不需要
//			var matriculateNoticeNo = $("#stuInfo #matriculateNoticeNo").val();
//			var name  = $("#stuInfo #name").val();
//			var certNum  = $("#stuInfo #certNum").val();
			
			var matriculateNoticeNo	=$('#stuInfo #matriculateNoticeNo').val();
			var name  ="";
			var certNum  = "";
			var rollCard 	= $("#stuInfo #rollCard").val();
			var stuStatus = $("#stuInfo #stuStatus").val();
			var entranceFlag  = $("#stuInfo #entranceFlag").val();
			var param = "unitId="+unitId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId
			+"&classesId="+classesId+"&matriculateNoticeNo="+matriculateNoticeNo+"&name="+name+"&rollCard="+rollCard
			+"&certNum="+certNum+"&stuStatus="+stuStatus+"&entranceFlag="+entranceFlag+"&flag=print";

			var url = "${baseUrl}/edu3/schoolRollTable/calculateNum.html";
			$.ajax({
				type:'POST',
				url:url,
				data:{unitId:unitId,classicId:classicId,majorId:majorId,gradeId:gradeId,classesId:classesId,matriculateNoticeNo:matriculateNoticeNo,name:name,rollCard:rollCard,certNum:certNum,stuStatus:stuStatus,entranceFlag:entranceFlag},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success: function(data){
					if(data['over500']=='1'){
						alertMsg.warn("打印学籍表:"+data['question'].split(':')[1]+" 当前查询数据超过500条，请勾选或细化查询条件再进行下载！");
						return false;
					}else{
						alertMsg.confirm("打印学籍表:"+data['question'].split(':')[1],{
						    okCall:function(){
						    	var printUrl = "${baseUrl}/edu3/register/studentinfo/studentCard/printRollTable-view.html?"+param;
						    	$.pdialog.open(printUrl,'RES_SCHOOL_SCHOOLROLL_MANAGER_PRINTROLLTABLE','学籍表打印预览',{mask:true,height:600, width:800});
						    }
					    });
					}
				}
			});
		}
	}

    // 创建学习计划
    function generateStuPlan(){
    	var studentIds = [];
    	$("#infoBody input[@name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
    	if(studentIds.length <= 0){
    		alertMsg.info("请选择一条要操作记录！");
    		return false;
    	}
    	
    	$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/register/studentinfo/generateStuPlan.html",
			data:{studentIds:studentIds.toString()},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data.statusCode==200){
					alertMsg.correct(data.message);
				}else{
					alertMsg.error(data.message);
				}
			}
		});
    }
    
    function printStudentStatistics(){
    	var isBrschool = '${isBrschool}';
    	if(!isBrschool){
    		alertMsg.warn("教学点教务人员才能使用该打印功能");
    		return;
    	}
    	var printUrl = "${baseUrl}/edu3/roll/studentinfo/studentStatistics.html";
    	$.pdialog.open(printUrl,'RES_SCHOOL_SCHOOLROLL_STUDENTSTATISTICS','在校生统计打印预览',{mask:true,height:600, width:800});
    }
    
    function stuRollPhotoImport(){
    	var url = "${baseUrl}/edu3/roll/studentinfo/stuRollPhotoimport-view.html"
    	$.pdialog.open(url,'RES_SCHOOL_SCHOOLROLL_MANAGER_IMPORT_PHOTO','导入学生相片',{mask:true,height:320, width:480});
    }
    
    //清退
    function repaying(){
    	var studentIds = [];
    	$("#infoBody input[@name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
    	if(studentIds.length <= 0){
    		alertMsg.info("请选择一条要操作记录！");
    		return false;
    	}
    	var url = "${baseUrl}/edu3/register/studentinfo/repayingReason.html?studentIds="+studentIds.toString();
    	$.pdialog.open(url,'RES_SCHOOL_SCHOOLROLL_MANAGER_REPAYING','清退',{mask:true,height:320, width:480});
    }
    
    function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	
	function modifyStuInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/editstu.html";
		if(isCheckOnlyone('resourceid','#infoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_EDIT', url+'?resourceid='+$("#infoBody input[@name='resourceid']:checked").val(), '信息修改');
		}			
	}
	
	//设置学生修改学籍开放时间 
/*	function changeStudentInfoTime()tuih
		var url = "${baseUrl}/edu3/register/studentinfo/changeStudentInfoTime.html";
		if(isCheckOnlyone('resourceid','#infoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_SET', url+'?resourceid='+$("#infoBody input[@name='resourceid']:checked").val(), '设置修改学籍开放时间');
		}			
	}*/
	
	function disenabledStudentAccount(){//停用账号			
			pageBarHandle("您确定要停用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=disenable","#infoBody");		
		
	}
	
	function enabledStudentAccount(){//启用账号		
		pageBarHandle("您确定要启用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=enable","#infoBody");			
	}
	function resetStudentAccountPassword(){//重置密码	
		pageBarHandle("您确定要重置这些学生账号的密码吗？","${baseUrl}/edu3/register/studentinfo/resetpassword.html","#infoBody");			
	}
	
	function switchStudentInfo(){//切换学生账号		
		if(isCheckOnlyone('resourceid','#infoBody')){
			var obj = $("#infoBody input[@name='resourceid']:checked");
			var username = obj.attr("title");
			switchSecurityTargetUser(username);
		}
		
	}
	//按查询条件导出XLS
	function exportXlsStudentInfoList(){
		if(isChecked('resourceid',"#infoBody")){
			exportStudentInfoListBySelection();
		}else{
			var url = "${baseUrl }/edu3/register/studentinfo/exportXls.html?flag=Y";
			var stuGrade 	 = $("#stuGrade").val();
			var branchSchool = $("#eiinfo_brSchoolName").val();
			var major 		 = $("#query_examresults_major").val();
			var classic 	 = $("#classic").val();
			var stuStatus    = $("#stuStatus").val();
			var name 		 = $("#name").val();
			var certNum 	 = $("#certNum").val();
			var studyNo 	 = $("#matriculateNoticeNo").val();
			var stuClasses 	 = $("#searchExamResult_classesid").val();
			var rollCard 	= $("#stuInfo #rollCard").val();
			var endDate = $("#stuInfo #endDate").val();
			if(branchSchool!="") url += "&branchSchool="+branchSchool;
			if(stuGrade!="") url += "&stuGrade="+stuGrade;
			if(classic!="") url += "&classic="+classic;
			if(major!="") url += "&major="+major;
			if(stuClasses!="") url += "&stuClasses="+stuClasses;
			if(stuStatus!="") url += "&stuStatus="+stuStatus;
			if(studyNo!="") url += "&studyNo="+studyNo;
			if(name!="") url += "&name="+encodeURIComponent(name);
			if(certNum!="") url += "&certNum="+certNum;
			if(rollCard!="") url += "&rollCard="+rollCard;
			if(endDate!=""){
				if(stuStatus==""){
					alertMsg.warn("请选择学籍状态！");
					return false;
				}
				url += "&endDate="+endDate;
			}
			//20140102 林辉 提议不限制办学单位
			$('#frame_exportXlsStudentInfoList').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frame_exportXlsStudentInfoList";
			iframe.src = url;
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);
		}
	}
	
	//按查询条件导出CSV
	function printStudentInfoList(){
		var stuGrade = $("#stuGrade").val();
		var branchSchool = $("#eiinfo_brSchoolName").val();
		var major = $("#query_examresults_major").val();
		var classic = $("#classic").val();
		var stuStatus = $("#stuStatus").val();
		var stuGrade = $("#stuGrade").val();
		var name = $("#name").val();
		if(branchSchool== "" && stuGrade=="" && major == "" && classic == "" && stuStatus == ""){
			alertMsg.warn("请至少选择一个条件进行导出!");
			return false;
		}else{
			$('#frame_exportStudentInfoList').remove();
			var iframe = document.createElement("iframe");
			iframe.id = "frame_exportStudentInfoList";
			iframe.src = "${baseUrl }/edu3/register/studentinfo/exportExcel.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&stuGrade="+stuGrade+"&name="+name;
			
			iframe.style.display = "none";
			//创建完成之后，添加到body中
			document.body.appendChild(iframe);
		}
	}
	
	//根据学籍办需要，添加一种按勾选导出
	function exportStudentInfoListBySelection(){
		if(!isChecked('resourceid',"#infoBody")){
 			alertMsg.warn('请选择一条要导出的记录。');
			return false;
	 	}
		var res = "";
		var k = 0;
		var num  = $("#infoBody"+" input[name='resourceid']:checked").size();
		$("#infoBody"+" input[@name='resourceid']:checked").each(function(){
                res+=$(this).val();
                if(k != num -1 ) res += ",";
                k++;
        });
		$('#frame_exportStudentInfoList_bySelection').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportStudentInfoList_bySelection";
		//iframe.src = "${baseUrl }/edu3/register/studentinfo/exportExcel.html?selectedid="+res+"&exportType=1";
		iframe.src = "${baseUrl}/edu3/register/studentinfo/exportXls.html?selectedid="+res+"&exportType=1";
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
		
	}

	//设置学生修改学籍开放时间 
	function changeStudentInfoTime(){	
		var stus="";//选择的学生
		$("#infoBody input[name='resourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false; 
		}
		var param  = "?stus="+stus;
		var url = "${baseUrl}/edu3/register/studentinfo/changeStudentInfoTime.html"+param;
		$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_SET_RECRUITSTATUS', '设置时间',{width:500,height:100});
	}
	
	//批量设置入学资格审核、批量设置学籍状态
	function batchSetStudentInfoStatus(flag){
		var stus="";
		$("#infoBody input[name='resourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false
		}
		var branchSchool=$('#stuInfo #eiinfo_brSchoolName').val()==undefined?"":$('#eiinfo_brSchoolName').val();
		var major		=$('#stuInfo #query_examresults_major').val()==undefined?"":$('#query_examresults_major').val();
		var classic		=$('#stuInfo #classic').val()==undefined?"":$('#classic').val();
		var stuStatus	=$('#stuInfo #stuStatus').val()==undefined?"":$('#stuStatus').val();
		var name		=$('#stuInfo #name').val()==undefined?"":$('#name').val();
		var certNum     =$('#stuInfo #certNum').val()==undefined?"":$('#certNum').val();
		var matriculateNoticeNo	=$('#stuInfo #matriculateNoticeNo').val()==undefined?"":$('#matriculateNoticeNo').val();
		var grade 		=$('#stuInfo #stuGrade').val()==undefined?"":$('#stuGrade').val();
		var pageNum 	="${stulist.pageNum}";
		var url 		= ""; 
		var param       = "?stus="+stus+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&stuStatus="+stuStatus+"&name="+name+"&matriculateNoticeNo"+matriculateNoticeNo+"&grade="+grade+"&certNum="+certNum+"&pageNum="+pageNum;
		if("0"==flag){
			url         = "${baseUrl}/edu3/register/studentinfo/setRecruitStatus.html"+param+"&flag="+flag
			$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_SET_RECRUITSTATUS', '入学资格状态设置',{width:300,height:100});
		}else if("1"==flag){
			url         = "${baseUrl}/edu3/register/studentinfo/setStudentStatus.html"+param+"&flag="+flag
			$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_SET_RECRUITSTATUS', '学籍状态变更',{width:300,height:100});
		}
	}
	//学籍信息统计
	function exportStuInfoStatToExcel(){
		var url = "${baseUrl}/edu3/register/studentinfo/exportStuInfoStatCondition.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_STAT', url, '在校生统计');
	}
	//信息修改日志&学籍状态变更日志
	function getStudentInfoChangeHistory(val){
		var url = "${baseUrl}/edu3/register/studentinfo/getStudentInfoChangeHistory.html?val="+val;
		if(''==val){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGEHIS', url, '信息修改日志');
		}else if('statusonly'==val){
			//navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGEHIS_STATUS', url, '学籍状态变更日志');
			url = "${baseUrl}/edu3/register/studentinfo/getBatchStudentInfoStatusChangeHistory.html";
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGEHIS_STATUS', url, '学籍状态变更日志');
		}
	}
	//默认分班
	function assignStudentClasses(){
		alertMsg.confirm("您确定要执行默认分班吗?", {
			okCall: function(){	
				$.post("${baseUrl}/edu3/roll/studentinfo/classes/assign.html",{}, navTabAjaxDone, "json");
			}
		});			
	}
	//调整分班
	function adjustStudentClasses(){
		if(!isChecked('resourceid','#infoBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var res = [];
		var ck = "";
		var isSame = true;
		$("#infoBody input[@name='resourceid']:checked").each(function(){
			res.push($(this).val());
			if(ck!=""&&ck!=$(this).attr('rel')){
				isSame = false;
				return false;
			} else {
				ck = $(this).attr('rel');
			}
		});
		if(isSame){
			$.pdialog.open(baseUrl+"/edu3/framework/studentinfo/classes/adjust.html?resourceid="+res.join(','), 'RES_ROLL_STUDENTINFO_CLASSES_ADJUST', '调整分班', {width: 800, height: 600});
		} else {
			alertMsg.warn('请选择同个办学单位相同年级、专业、层次和学习方式的学生进行分班调整');
			return false;
		}		
	}
	//删除分班
	function removeStudentClasses(){
		pageBarHandle("您确定要删除这些学生的分班吗？","${baseUrl}/edu3/roll/studentinfo/classes/remove.html","#infoBody");				
	}
	
	//对学籍中上传的图片进行审核
	function AuditImg(){
		var stus="";
		var aduitImgStatus = $("#aduitImgStatus").val();
		var aduitImgUserName = $("#aduitImgUserName").val();

		$("#infoBody input[name='resourceid']").each(function(){
			if($(this).attr("checked")){
				stus = ""+$(this).val();
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false
		}
				
		url         = "${baseUrl}/edu3/register/studentinfo/auditStudentinfoImg.html?aduitImgStatus="+aduitImgStatus+"&aduitImgUserName="+aduitImgUserName+"&stus="+stus;
		$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_AUDITIMG', '学籍信息图片审核状态变更',{width:300,height:50});
		
	}
	
	// 学生首次确认（只提供给公司内部管理员使用，解决外省手机号无法获取验证码问题）
	function helpConfirmBaseInfo(){
		var studentId = new Array();
		$("#infoBody input[name='resourceid']:checked").each(function(){
			studentId.push($(this).val());
		});
		if(studentId.length<0){
			alertMsg.warn("请选择一条要操作的记录！");
			return false;
		}
		if(studentId.length>1){
			alertMsg.warn("只能选择一条要操作的记录！");
			return false;
		}
		$.pdialog.open('${baseUrl }/edu3/helpConfirmMsgView.html?studentId='+studentId.toString(),'helpConfirmStuInfo','确认学生首次登录基本信息',{mask:true,width:380,height:450});	
	}
	
	// 替学生提交学籍卡（只提供给公司内部管理员使用，解决外省手机号无法获取验证码问题）
	function helpSubmitStuCard(){
		var studentIds = new Array();
		$("#infoBody input[name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
		if(studentIds.length<0){
			alertMsg.warn("请选择一条或多条要操作的记录！");
			return false;
		}
		alertMsg.confirm("您确定要提交这些学生的学籍卡吗？",{
		    okCall:function(){
		    	$.ajax({
		    		type:'POST',
		    		url:"${baseUrl}/edu3/roll/helpSubmitStuCard.html",
		    		data:{studentIds:studentIds.toString()},
		    		dataType:"json",
		    		cache: false,
		    		error: DWZ.ajaxError,
		    		success: function(data){
	    				if(data.statusCode===200){
	    					alertMsg.correct(data.message);
	    				}else{
	    					alertMsg.error(data.message);
	    				}
		    		}
		    	});
		    }
	    });
	}
	
	// 生成教材费订单
	function createTextbookFeeB(){
		var url = "${baseUrl}/edu3/register/studentinfo/createTextbookFee.html";
		var paramData;
		var studentIds = new Array();
		$("#infoBody input[name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
		if(studentIds.length>0){
			paramData = {resouceids:studentIds.join(",")};
		} else {
			paramData = $("#studentInfoForm").serialize();
		}
		
		alertMsg.confirm("您确定要给这些学生生成教材费订单吗？",{
		    okCall:function(){
		    	$.ajax({
					type:'POST',
					url:url,
					data:paramData,
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(data){
						if(data['statusCode']==200){
							alertMsg.correct(data['message']);
						}else{
							alertMsg.error(data['message']);
						}
					}
				});
		    }
	    });
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="studentInfoForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/schoolroll-list.html"
				method="post">
				<input type="hidden" id="isFromPage" name="isFromPage" value="1" />
				<input type="hidden" id="isBrschool" name="isBrschool"
					value="${isBrschool }" />
				<div id="stuInfo" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span sel-id="eiinfo_brSchoolName"
								sel-name="branchSchool" sel-onchange="schoolrollQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool" id="eiinfo_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="stuGrade"
							sel-name="stuGrade" sel-onchange="schoolrollQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="schoolrollQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li" id="graduateudit-gradeToMajor4"><label>专业：</label> <span
							sel-id="query_examresults_major" sel-name="major"
							sel-onchange="schoolrollQueryMajor()" sel-classs="flexselect"
							></span></li>
						<li><label>学籍状态：</label> <select name="stuStatus"
							id="stuStatus" style="width: 120px">
						</select> <!--<gh:select name="stuStatus" id="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:125px" />-->
						</li>
						<li><label>学籍卡状态：</label>
						<gh:select id="rollCard" name="rollCard"
								dictionaryCode="CodeRollCardStatus"
								value="${condition['rollCard']}" style="width: 120px" /></li>
						<li><label>截止日期：</label> <input type="text" id="endDate" name="endDate" size="40" style="width:120px" value="${condition['endDate']}" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',isShowWeek:true})"/>
						</li>
						
					</ul>
					<ul class="searchContent">
						<li id="graduateudit-gradeToMajorToClasses4" class="custom-li"><label>班级：</label>
							<span sel-id="searchExamResult_classesid" sel-name="classesid"
							sel-classs="flexselect" ></span></li>
						<li><label>姓名：</label><input type="text" name="name"
							id="name" value="${condition['name']}" style="width: 120px" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo" id="matriculateNoticeNo"
							value="${condition['studyno']}" style="width: 120px" /></li>
						<li><label>入学资格：</label>
						<gh:select id="entranceFlag" name="entranceFlag"
								dictionaryCode="CodeAuditStatus"
								value="${condition['entranceFlag']}" choose="Y"
								style="width: 120px" /></li>
						
						
						<%-- <li><label>联系地址：</label> <input type="text"
							name="contactAddress" id="contactAddress"
							value="${condition['contactAddress']}" style="width: 120px" /></li> --%>
						<%-- 
				<li>
						<label>审核结果：</label><gh:select name="entranceFlag" dictionaryCode="CodeAuditStatus"  value="${condition['entranceFlag']}" choose="Y" style="width: 120px"/> 
				</li> --%>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>身份证号：</label><input type="text" name="certNum"
							id="certNum" value="${condition['certNum']}" class="custom-inp" />
						</li>
						<li><label>手机号：</label><input type="text" name="mobile"
							id="mobile" value="${condition['mobile']}" style="width: 120px" /></li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_MANAGER" pageType="list" ></gh:resAuth>
			<table class="table" layouth="243">
				<thead>
					<tr>
						<!-- 原始列宽设置
			    	<th width="5%"><input type="checkbox" name="checkall" id="check_all_info" onclick="checkboxAll('#check_all_info','resourceid','#infoBody')"/></th>
		            <th width="6%">姓名</th>
		            <th width="9%">学号</th>
		            <th width="4%">性别</th>
		            <th width="8%">身份证</th>
		            <th width="8%">民族</th>
		        	<th width="8%">年级</th>
			        <th width="6%">入学日期</th>
			        <th width="8%">培养层次</th>
			        <th width="8%">专业</th>
		            <th width="15%">办学单位</th>
			        <th width="4%">学籍状态</th>
			        <th width="4%">账号状态</th>
			        <th width="6%">入学资格</th>
			        -->
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_info"
							onclick="checkboxAll('#check_all_info','resourceid','#infoBody')" /></th>
						<th width="3.5%">姓名</th>
						<th width="7.5%">学号</th>
						<th width="2%">性别</th>
						<th width="10.5%">身份证</th>
						<th width="3%">民族</th>
						<th width="4.7%">年级</th>
						<th width="7.8%">联系地址</th>
						<th width="4%">培养层次</th>
						<th width="7.5%">专业</th>
						<th width="6%">办学单位</th>
						<th width="4%">学籍状态</th>
						<th width="3%">账号</th>
						<th width="9%">班级</th>
						<th width="4%">入学日期</th>
						<th width="3.5%">入学资格</th>
						<th width="4%">学籍卡</th>
						<th width="3%">学习形式</th>
						<th width="7.5%">准考证号</th>
						<th width="6.5%">入学总分</th>
					</tr>
				</thead>
				<tbody id="infoBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" title="${stu.sysUser.username}"
								alt="${stu.studentStatus}"
								rel="${stu.branchSchool.resourceid}|${stu.grade.resourceid}|${stu.major.resourceid}|${stu.classic.resourceid}|${stu.teachingType}"
								autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看"><c:if test="${empty stu.studentBaseInfo.name}">${stu.studentName }</c:if>
									<c:if test="${ not empty stu.studentBaseInfo.name}">${stu.studentBaseInfo.name }</c:if></a></td>
							<td>${stu.studyNo}</td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.studentBaseInfo.contactAddress}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
							<td <c:if test="${stu.studentStatus == '11'}">style='color: green'</c:if>
								<c:if test="${stu.studentStatus == '13' or stu.studentStatus == '26'}">style='color: red'</c:if>
								><c:choose>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==1}">正常注册</c:when>
									<c:when
										test="${stu.studentStatus == '11' and stu.accountStatus==0}">正常未注册</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus)}</c:otherwise>
								</c:choose>

							</td>
							<td <c:if test="${stu.accountStatus ne 1}">style='color: red'</c:if>
								>${stu.accountStatus==1?"激活":"停用"}
							</td>
							<td title="${stu.classes.classname }">
								${stu.classes.classname }</td>
							<td><fmt:formatDate value="${stu.inDate }"
									pattern="yyyy-MM-dd" /></td>
							<td <c:if test="${stu.enterAuditStatus=='N'}">style='color:red'</c:if>
								<c:if test="${stu.enterAuditStatus=='Y'}">style='color:blue'</c:if>
								><c:choose>
									<c:when test="${stu.enterAuditStatus=='N'}">
										不通过
									</c:when>
									<c:when test="${stu.enterAuditStatus=='Y'}">通过</c:when>
									<c:otherwise>待审核</c:otherwise>
								</c:choose></td>
							<td <c:if test="${stu.rollCardStatus eq '2'}">style='color: blue'</c:if>
								<c:if test="${stu.rollCardStatus eq '1'}">style='color: green'</c:if>
								<c:if test="${stu.rollCardStatus eq '0'}">style='color: red'</c:if>
								><c:choose>
									<c:when test="${stu.rollCardStatus eq '2'}">
										已提交
									</c:when>
									<c:when test="${stu.rollCardStatus eq '1'}">
										已保存
									</c:when>
									<c:otherwise>
										未保存
									</c:otherwise>
								</c:choose></td>
							<td>
								${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType)} <input
								type="hidden" value="${stu.auditResults}" id="aduitImgStatus">
								<input type="hidden" value="${stu.studentBaseInfo.name}"
								id="aduitImgUserName">

							</td>
							<td>${stu.examCertificateNo }</td>
							<td>${stu.totalPoint }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
		<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/studentinfo/schoolroll-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>