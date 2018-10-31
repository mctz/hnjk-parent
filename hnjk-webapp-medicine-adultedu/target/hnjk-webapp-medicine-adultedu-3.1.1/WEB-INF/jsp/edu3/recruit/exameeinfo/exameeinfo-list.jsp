<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生信息</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("#exameeinfo_brSchoolName").flexselect({});
		linkageQueryBegin();
	});
	
	// 打开页面或者点击查询（即加载页面执行）
   function linkageQueryBegin() {
	   var defaultValue = "${condition['branchSchool']}";
	   var schoolId = "";
	   var isBrschool = "${isBrschool}";
	   if(isBrschool==true || isBrschool=="true"){
		   schoolId = defaultValue;
	   }
	   var gradeId = "";
	   var classicId = "${condition['classic']}";
	   var teachingType = "";
	 
	   var majorId = "${condition['major']}";
	
	   var selectIdsJson = "{unitId:'exameeinfo_brSchoolName',classicId:'exameeInfo_classic',majorId:'exameeInfo_major'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, "", selectIdsJson);
	   
   }
   
   // 选择教学点
   function linkageQueryUnit() {
	   var defaultValue = $("#exameeinfo_brSchoolName").val();
	   var selectIdsJson = "{classicId:'exameeInfo_classic',majorId:'exameeInfo_major'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
   }
	
   
	// 选择层次
   function linkageQueryClassic() {
	   var defaultValue = $("#exameeinfo_brSchoolName").val();
	   var classicId = $("#exameeInfo_classic").val();
	   var selectIdsJson = "{majorId:'exameeInfo_major'}";
	   cascadeQuery("classic", defaultValue, "", "", classicId, "", "", "", selectIdsJson); 
   }

  
	
// 根据年级同步联动信息（招生专业）
   function syncLinkageQuery() {
   	var selectGradeUrl = "${baseUrl}/edu3/teaching/linkageQuery/selectGrade.html";
	   $.pdialog.open(selectGradeUrl, 'RES_TEACHING_LINKAGEQUERY_SELECTGRADE', '选择年级', {mask:true,width: 300, height: 200});
   }
	
	//导入报名信息
	function importExameeInfo(){
		$.pdialog.open("${baseUrl}/edu3/framework/recruit/exameeinfo/upload.html?from=ExameeInfo","RES_RECRUIT_EXAMEEINFO_IMPORT","导入报名信息", {width:800, height:600});
	}
	//导入考生相片
	function importExameeInfoPhoto(){
		$.pdialog.open("${baseUrl}/edu3/framework/recruit/exameeinfo/upload.html?from=ExameeInfoPhoto","RES_RECRUIT_EXAMEEINFO_PHOTO_IMPORT","导入考生相片", {width:800, height:600});
	}
	//打印考生信息
	function printExameeInfo(){
		if(!isChecked('resourceid',"#exameeInfoBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		var studentId = [];
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			studentId.push($(this).val());
    	});
		$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/printview.html?studentId="+studentId.join(','),'RES_RECRUIT_EXAMEEINFO_PRINTVIEW','打印预览',{height:600, width:800});
	}
	//打印注销考生信息
	function printCancelExameeInfo(){
		$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/cancel/printview.html",'RES_RECRUIT_EXAMEEINFO_ZX_PRINT','打印预览',{height:600, width:800});
	}
	
	//注销入学资格
	function cancelExameeInfo(){
		var xm = new Array();
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			var rel = $(this).attr('rel');
            if(rel!='5' && rel!='10'){
            	xm.push($(this).attr('xm'));
            	//return false;
            }
        });
		if(xm.length>0){
			alertMsg.warn("考生"+xm.join(",")+"的入学资格已被注销,请不要重复操作.");
			return false;
		}
		pageBarHandle("您确定要注销这些考生的入学资格吗？","${baseUrl}/edu3/recruit/exameeinfo/cancel.html","#exameeInfoBody");
	}
	//恢复注销
	function recoveryExameeInfo(){
		var xm = new Array();
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
            if($(this).attr('rel')!='6'){
            	xm.push($(this).attr('xm'));
            	//return false;
            }
        });
		if(xm.length>0){
			alertMsg.warn("考生"+xm.join(",")+"的不是注销学生,不能进行恢复注销操作.");
			return false;
		}
		pageBarHandle("您确定要撤销这些考生的注销状态吗？","${baseUrl}/edu3/recruit/exameeinfo/recovery.html","#exameeInfoBody");
	}
	//申请注销
	function applyCancelExameeInfo(){
		var xm = "";
		var ids = "";
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			if(""==ids){
				ids += $(this).val();
	    	}else{
	    		ids += ","+$(this).val();
	    	}
			if($(this).attr('rel')=='6'||$(this).attr('rel')=='7'){
            	if(""==xm){
            		xm += $(this).attr('xm');
            	}else{
            		xm += ","+$(this).attr('xm');
            	}
            }
        });
		if(ids == ""){
			alertMsg.warn("请您至少选择一条记录进行申请注销操作.");
			return false;
		}else if(xm!=""){
			alertMsg.warn("考生"+xm+"的入学资格已被注销或已申请注销,请不要重复操作.");
			return false;
		}else{
			$.ajax({
		          type:"POST",
		          url:"${baseUrl}/edu3/recruit/exameeinfo/applyAuditCancelExameeInfoCheck.html",
		          data:{resourceid:ids},
		          dataType:  'json',
		          success:function(date){          	   		
		         		 if(date['unable'] === '1'){         		 	
		         		 	 alertMsg.warn(date['msg'])	;	  
		         		 }else{
		         			pageBarHandle("您确定要为这些考生申请注销入学资格吗？","${baseUrl}/edu3/recruit/exameeinfo/applycancel_keepStudent.html?type=cancel","#exameeInfoBody");
		         		 }         
		          }            
			});
		}
	}
	//审核注销
	function auditCancelExameeInfo(){
		var xm = "";
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
            if($(this).attr('rel')!='7'){
            	if(""==xm){
            		xm += $(this).attr('xm');
            	}else{
            		xm += ","+$(this).attr('xm');
            	}
            }
        });
		if(xm!=""){
			alertMsg.warn("考生"+xm+"的入学资格非申请注销,不可进行审核.");
			return false;
		}
		var ids ="";
		
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			if(""==ids){
				ids += $(this).val();
        	}else{
        		ids += ","+$(this).val();
        	}
		});
		if(""==ids){
			alertMsg.warn("请您至少选择一条记录进行审核注销操作.");
			return false;
		}else{
			var url = "${baseUrl}/edu3/recruit/exameeinfo/exameeinfo-select.html?type=cancel&ids="+ids;
			$.pdialog.open(url,"RES_RECRUIT_EXAMEEINFO_AUDITSELECT","审核注销", {mask:true,width:300, height:200});
		}
	}
	
	//注销(新) 不记得这段是用来做啥的了
	function withDrawExameeInfo(){
		var xm = "";
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
            if($(this).attr('rel')=='6'){
            	xm = $(this).attr('xm');
            	return false;
            }
        });
		if(xm!=""){
			alertMsg.warn("考生"+xm+"已被注销,请不要重复操作.");
			return false;
		}
		pageBarHandle("您确定要注销这些学生吗？","${baseUrl}/edu3/recruit/exameeinfo/withDraw.html","#exameeInfoBody");
	}
	
	function expToExcel(){
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		var url = baseUrl+"/edu3/recruit/exameeinfo/list-upload.html";
		if(isChecked('resourceid','#exameeInfoBody')){
			alertMsg.confirm("您确定要导出选择的记录吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&resIds="+resIds,'tgradeIframe');
				}
			});
		}else{
			var branchSchool = jQuery("input[name='branchSchool']").val();
			var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
			var name         = $("input[name='name']").val();
			var enrolleeCode = $("input[name='enrolleeCode']").val();
			
			var certNum      = $("input[name='certNum']").val();
			var kszt         = $("#exameeInfo_kszt option:selected").val();
			var isExistsPhoto= jQuery("#isExistsPhoto option:selected").val();
			//var classic      = $("input[name='classic']").val();
			var classic      = $("#exameeInfo_classic option:selected").val();
			var major      = $("input[name='major']").val();
			var registorFlag= jQuery("#registorFlag option:selected").val();
			
			var conditionStr = "branchSchool="+branchSchool+"&recruitPlanId="+recruitPlanId+"&name="+name+"&enrolleeCode="+enrolleeCode+"&certNum="+certNum;
			conditionStr += "&kszt="+kszt+"&isExistsPhoto="+isExistsPhoto+"&registorFlag="+registorFlag;
			if(classic!='' && classic!=undefined){
				conditionStr += "&classic="+classic;
			}
			if(major!='' && major!=undefined){
				conditionStr += "&major="+major;
			}
			
			alertMsg.confirm("您确定要根据条件以上查询条件导出吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&"+conditionStr,'tgradeIframe');
				}
			});
		}
	}
	//导出教学站分配名单
	function exportUnit(){
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		var url = baseUrl+"/edu3/recruit/exameeinfo/exportunit.html";
		if(isChecked('resourceid','#exameeInfoBody')){
			alertMsg.confirm("您确定要导出选择的记录吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&resIds="+resIds,'tgradeIframe');
				}
			});
		}else{
			var branchSchool = jQuery("input[name='branchSchool']").val();
			var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
			var name         = $("input[name='name']").val();
			var enrolleeCode = $("input[name='enrolleeCode']").val();
			
			var certNum      = $("input[name='certNum']").val();
			var kszt         = $("#exameeInfo_kszt option:selected").val();
			var isExistsPhoto= jQuery("#isExistsPhoto option:selected").val();
			//var classic      = $("input[name='classic']").val();
			var classic      = $("#exameeInfo_classic option:selected").val();
			var major      = $("input[name='major']").val();
			var registorFlag= jQuery("#registorFlag option:selected").val();
			var conditionStr = "branchSchool="+branchSchool+"&recruitPlanId="+recruitPlanId+"&name="+name+"&enrolleeCode="+enrolleeCode+"&certNum="+certNum;
			conditionStr += "&kszt="+kszt+"&isExistsPhoto="+isExistsPhoto+"&registorFlag="+registorFlag;
			if(classic!='' && classic!=undefined){
				conditionStr += "&classic="+classic;
			}
			if(major!='' && major!=undefined){
				conditionStr += "&major="+major;
			}
			alertMsg.confirm("您确定要根据条件以上查询条件导出吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&"+conditionStr,'tgradeIframe');
				}
			});
		}
	}
	//导入教学站分配情况
	function importUnit(){
		$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/imputunit.html", 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '导入教学站分配情况', {width: 600, height: 360});
	}
	//分配教学站
	function updateUnit(){
		var ids = "";
		var isSameMajor = true;
		var major = "";
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			if(""==ids){
				ids += $(this).val();
	    	}else{
	    		ids += ","+$(this).val();
	    	}
			// 判断是否是同一个录取专业
			if(isSameMajor){
				if(major != "" && major != $(this).attr("major") ){
					isSameMajor = false;
				}
				major = $(this).attr("major");
		    }
        });
		if(ids == ""){
			alertMsg.warn("请您至少选择一条记录进行分配操作.");
			return;
		}
		if(!isSameMajor){
			alertMsg.warn("请您选择同一个录取专业");
			return;
		}
		$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/updateinputunit.html?ids="+ids+"&majorName="+encodeURI(major), 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '分配教学站', {width: 600, height: 100});
	}
	// 生成学生缴费标准(TODO:该功能移到学籍信息页面)
	function studentPayment(){
		
		var branchSchool = jQuery("input[name='branchSchool']").val();
		var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
		var classcicId = $("#exameeInfo_classic").val();
		var major = $("#exameeInfo_major").val();
		var name         = $("input[name='name']").val();
		name = encodeURIComponent(encodeURIComponent(name));
		var enrolleeCode = $("input[name='enrolleeCode']").val();
		var certNum      = $("input[name='certNum']").val();
		var kszt = $("#exameeInfo_kszt").val();
		var isExistsPhoto = $("input[name='isExistsPhoto']").val();
		//var examCertificateNo = $("input[name='examCertificateNo']").val();
		var registorFlag = $("#registorFlag").val();
		
		var postUrl="${baseUrl}/edu3/recruit/exameeinfo/fee.html?recruitPlanId="+recruitPlanId+"&name="+name+"&branchSchool=" 
			+branchSchool+"&certNum="+certNum+"&enrolleeCode="+enrolleeCode+"&isExistsPhoto="+isExistsPhoto+"&kszt="+kszt+"&major="+major;
		
		alertMsg.confirm("您确定要给按查询条件查出的学生生成缴费信息吗？", {
			okCall: function(){//执行	
				$.post(postUrl,"", navTabAjaxDone, "json");
			}
		});	
	}
	// 生成录取编号
	function generateEnrollNO() {
		var recruitPlanId = $("#exameeInfo_recruitPlanId").val();
		if(!recruitPlanId){
			alertMsg.warn("请先选择一个招生批次！");
			return false;
		}
		var url = "${baseUrl}/edu3/recruit/exameeinfo/generateEnrollNO.html"; 
		alertMsg.confirm("你确定要给该招生批次的考生生成录取编号吗？", {
			okCall: function(){
				$.ajax({
			          type:"POST",
			          url:url,
			          data:{recruitPlanId:recruitPlanId},
			          dataType:  'json',
			          success:function(data){          	   		
			         		 if(data['statusCode'] === '200'){         		 	
			         		 	 alertMsg.correct(data['msg']);	  
			         		 	 navTabPageBreak();
			         		 }else{
			         			 alertMsg.error(data['msg']);
			         		 }         
			          }            
				});
			}
		});	
	}
	
	// 编辑考生报名信息
	function editEnrolleeOtherInfo() {
		if(isCheckOnlyone('resourceid','#exameeInfoBody')){
			$("#exameeInfoBody input[name='resourceid']:checked").each(function(){
				var enrolleeId = $(this).attr("enrolleeId");
				var url = "${baseUrl}/edu3/recruit/enroll/edit.html";
				$.pdialog.open(url+'?resourceid='+enrolleeId, 'RES_RECRUIT_EXAMEEINFO_EDIT_ENROLLEEINFO', '编辑信息', {width: 800, height: 600});
			});
		}
	}

	// 导出学生信息
	/* function exportStudentMs(){
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		if(k>8000){
			alertMsg.warn("操作的数据量"+k+"过大,请选择低于8000条数据");
		}
		var url = baseUrl+"/edu3/recruit/exameeinfo/export-stu.html";
		if(isChecked('resourceid','#exameeInfoBody')){
			alertMsg.confirm("您确定要导出选择的记录吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?resIds="+resIds,'tgradeIframe');
				}
			});
		}else{
			var branchSchool = jQuery("input[name='branchSchool']").val();
			var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
			var name         = $("input[name='name']").val();
			var enrolleeCode = $("input[name='enrolleeCode']").val();
			
			var certNum      = $("input[name='certNum']").val();
			var kszt         = $("#exameeInfo_kszt option:selected").val();
			var isExistsPhoto= jQuery("#isExistsPhoto option:selected").val();
			//var classic      = $("input[name='classic']").val();
			var classic      = $("#exameeInfo_classic option:selected").val();
			var major      = $("input[name='major']").val();
			var registorFlag= jQuery("#registorFlag option:selected").val();
			
			
			
			var conditionStr = "branchSchool="+branchSchool+"&recruitPlanId="+recruitPlanId+"&name="+name+"&enrolleeCode="+enrolleeCode+"&certNum="+certNum;
			conditionStr = conditionStr+"&kszt="+kszt+"&isExistsPhoto="+isExistsPhoto+"&classic="+classic+"&major="+major+"&registorFlag="+registorFlag;
			alertMsg.confirm("您确定要根据条件以上查询条件导出吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?"+conditionStr,'tgradeIframe');
				}
			});
		}
	}  */
	
    //打印条形码
	function printCode(){
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		var url = baseUrl+"/edu3/recruit/exameeinfo/print-code.html";
		if(isChecked('resourceid','#exameeInfoBody')){
			alertMsg.confirm("您确定要打印选择的记录吗？",{
				okCall:function(){
					$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/barcodePrintview.html"+"?resIds="+resIds,'RES_RECRUIT_EXAMEEINFO','打印预览',{height:600, width:800});
				}
			});
		}else{
			var branchSchool = jQuery("input[name='branchSchool']").val();
			var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
			var name         = $("input[name='name']").val();
			var enrolleeCode = $("input[name='enrolleeCode']").val();
			
			var certNum      = $("input[name='certNum']").val();
			var kszt         = $("#exameeInfo_kszt option:selected").val();
			var isExistsPhoto= jQuery("#isExistsPhoto option:selected").val();
			//var classic      = $("input[name='classic']").val();
			var classic      = $("#exameeInfo_classic option:selected").val();
			var major      = $("input[name='major']").val();
			var registorFlag= jQuery("#registorFlag option:selected").val();
			
			var conditionStr = "branchSchool="+branchSchool+"&recruitPlanId="+recruitPlanId+"&name="+name+"&enrolleeCode="+enrolleeCode+"&certNum="+certNum;
			conditionStr += "&kszt="+kszt+"&isExistsPhoto="+isExistsPhoto+"&registorFlag="+registorFlag;
			if(classic!='' && classic!=undefined){
				conditionStr += "&classic="+classic;
			}
			if(major!='' && major!=undefined){
				conditionStr += "&major="+major;
			}
			
			alertMsg.confirm("您确定要根据条件以上查询条件打印吗？",{
				okCall:function(){
					$.pdialog.open(baseUrl+"/edu3/recruit/exameeinfo/barcodePrintview.html"+"?"+conditionStr,'RES_RECRUIT_EXAMEEINFO','打印预览',{height:600, width:800});
				}
			});
		}
	}

	// 生成学费订单信息
	function createTempStuFee(){
		createAdvanceOrder("tuition");
	}
	
	//导出注册统计
	function enrollStatistical(flag){
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		var url = baseUrl+"/edu3/recruit/exameeinfo/enrollStatistical.html";
		if(isChecked('resourceid','#exameeInfoBody')){
			alertMsg.confirm("您确定要导出选择的记录吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&resIds="+resIds+"&flag="+flag,'tgradeIframe');
				}
			});
		}else{
			var url = baseUrl+"/edu3/recruit/exameeinfo/enrollStatistical.html";
			var branchSchool = jQuery("input[name='branchSchool']").val();
			var recruitPlanId  = jQuery("input[name='recruitPlanId']").val();
			var name         = $("input[name='name']").val();
			var enrolleeCode = $("input[name='enrolleeCode']").val();
			var certNum      = $("input[name='certNum']").val();
			var kszt         = $("#exameeInfo_kszt option:selected").val();
			var isExistsPhoto= jQuery("#isExistsPhoto option:selected").val();
			//var classic      = $("input[name='classic']").val();
			var classic      = $("#exameeInfo_classic option:selected").val();
			var major      = $("input[name='major']").val();
			var registorFlag= jQuery("#registorFlag option:selected").val();
			
			var conditionStr = "flag="+flag+"&branchSchool="+branchSchool+"&recruitPlanId="+recruitPlanId+"&name="+name+"&enrolleeCode="+enrolleeCode+"&certNum="+certNum;
			conditionStr += "&kszt="+kszt+"&isExistsPhoto="+isExistsPhoto+"&registorFlag="+registorFlag;
			
			if(classic!='' && classic!=undefined){
				conditionStr += "&classic="+classic;
			}
			if(major!='' && major!=undefined){
				conditionStr += "&major="+major;
			}
			alertMsg.confirm("您确定要根据条件以上查询条件导出吗？",{
				okCall:function(){
					downloadFileByIframe(url+"?&"+conditionStr,'tgradeIframe');
				}
			});
		}
		
	}
	
	// 申请保留学籍
	function applyKeepStudent(){
		var xm = new Array();
		var ids = new Array();
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			ids.push($(this).val());
			if($(this).attr('rel') != '5'){
				xm.push($(this).attr('xm'));
            }
        });
		if(ids.length<1){
			alertMsg.warn("请您至少选择一条记录进行操作");
			return false;
		}else if(xm.length>0){
			alertMsg.warn("考生"+xm.toString()+"的考生状态不是已录取,请只操作已录取的考生");
			return false;
		}else{
			$.ajax({
		          type:"POST",
		          url:"${baseUrl}/edu3/recruit/exameeinfo/applyAuditCancelExameeInfoCheck.html",
		          data:{resourceid:ids.toString()},
		          dataType:  'json',
		          success:function(date){          	   		
		         		 if(date['unable'] === '1'){         		 	
		         		 	 alertMsg.warn(date['msg'])	;	  
		         		 }else{
		         			pageBarHandle("您确定要为这些考生申请保留学籍吗？","${baseUrl}/edu3/recruit/exameeinfo/applycancel_keepStudent.html?type=keepStudent","#exameeInfoBody");
		         		 }         
		          }            
			});
		}
	}
	
	// 审核保留学籍
	function auditKeepStudent(){
		var ids = new Array();
		var xm = new Array();
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			ids.push($(this).val());
            if($(this).attr('rel')!='9'){
            	xm.push($(this).attr('xm'));
            }
        });
		if(ids.length<1){
			alertMsg.warn("请您至少选择一条记录进行操作.");
			return false;
		}else if(xm.length>0){
			alertMsg.warn("考生"+xm.toString()+"的考生状态不是申请保留学籍中,不可进行审核.");
			return false;
		}else{
			var url = "${baseUrl}/edu3/recruit/exameeinfo/exameeinfo-select.html?type=keepStudent&ids="+ids.toString();
			$.pdialog.open(url,"RES_RECRUIT_EXAMEEINFO_AUDITSELECT","审核保留学籍", {mask:true,width:300, height:200});
		}
	}
	//发送短信
	function sendNote(){
		//发送短信请求地址
		var url="${baseUrl}/edu/recruit/exameeinfo/sendNote.html";
		var k   = 0;
		var resIds = "";
		var num = $("#exameeInfoBody input[name='resourceid']:checked").size();
		$("#exameeInfoBody  input[@name='resourceid']:checked").each(function(){
			resIds+=$(this).val();
            if(k != num -1 ) resIds += ",";
            k++;
        });
		if(k>0){
			alertMsg.confirm("当前已勾选<font color='red'> "+k+" 人</font>，确认给这些学生发送消息吗？",{
				okCall:function(){
					$.ajax({
						type:"POST",
						url:url,
						dataType:"json",
						data:{resId:resIds},
						success:function(data){
							if(data.statusCode==200){
								alertMsg.correct("发送成功");
							}else{
								alertMsg.error(data.message);
							}
						}
					});
				}
			})
		}else{
			//统计当前查询条件的人数请求地址
			var preUrl = "${baseUrl}/edu/recruit/exameeinfo/selectCount.html";
			var recruitPlanId=jQuery("input[name='recruitPlanId']").val();
			var unitId= $("#exameeinfo_brSchoolName").val();
			var classicId=$("#exameeInfo_classic").val();
			var majorId=$("#exameeInfo_major").val();
			var recruitPlanName="无";
			var unitName="无";
			var classicName="无";
			var majorName="无";
			if(recruitPlanId!=''){
				recruitPlanName=jQuery("#exameeInfo_recruitPlanId_flexselect").val();
			}
			if(unitName!=''){
				unitName=$("#exameeinfo_brSchoolName_flexselect").val();
			}
			if(recruitPlanId!=''){
				classicName=jQuery("#exameeInfo_classic").val();
			}
			if(unitName!=''){
				majorName=$("#exameeInfo_major_flexselect").val();
			}			
			
			$.ajax({
				type:"POST",
				url:preUrl,
				dataType:"json",
				data:{recruitPlanId:recruitPlanId,unitId:unitId,classicId:classicId,majorId:majorId},
				success:function(data){
					if(data.statusCode==200){
						var selectCount = data.selectCount;
						alertMsg.confirm("当前选择条件：<br>招生批次:"+recruitPlanName+"<br>教学点："+unitName+"<br>层次："+classicName+"<br>专业："+majorName+"，<br>共："+selectCount+" 人，确认给这些学生发送消息吗？",{
							okCall:function(){
			 					$.ajax({
								type:"POST",
								url:url,
								dataType:"json",
								data:{recruitPlanId:recruitPlanId,unitId:unitId,classicId:classicId,majorId:majorId},
								success:function(data){
									if(data.statusCode==200){
										alertMsg.correct("发送成功");
									}else{
										alertMsg.error(data.message);
									}
								}
							});
							}
						});
					}
				}
			});
		}
	}
	
	// 生成教材费订单
	function createTextbookFeeA() {
		createAdvanceOrder("textbookFee");
	}
	
	// 生成预缴费订单
	function createAdvanceOrder(chargingItems) {
		var resourceIds = [];
		$("#exameeInfoBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).attr("enrolleeId"));
		});
		
		var recruitPlanId  = $("#exameeInfo_recruitPlanId").val();
		var branchSchool = $("#exameeinfo_brSchoolName").val();
		var classcicId = $("#exameeInfo_classic").val();
		var major = $("#exameeInfo_major").val();
		var name = $("#exameeInfo_name").val();
		var enrolleeCode = $("#exameeInfo_enrolleeCode").val();
		var certNum      = $("#exameeInfo_certNum").val();
		// 只处理未注册的学生
		var registorFlag = "N";
		
		if(!chargingItems){
			chargingItems = "tuition";
		}
		// 提示语
		var tip;
		if(chargingItems == "textbookFee"){
			tip = "你确定要给这些考生生成教材费订单吗？";
		} else {
			tip = "你确定要给这些考生生成学费订单吗？";
		}
		
		alertMsg.confirm(tip, {
			okCall: function(){
				$.ajax({
			          type:"POST",
			          url:"${baseUrl}/edu3/finance/tempStudentFee/create.html",
			          data:{resourceIds:resourceIds.toString(),recruitPlanId:recruitPlanId,branchSchool:branchSchool,classcicId:classcicId,
			        	       majorId:major,name:name,enrolleeCode:enrolleeCode,certNum:certNum,registorFlag:registorFlag,chargingItems:chargingItems},
			          dataType:  'json',
			          success:function(data){          	   		
			         		 if(data['statusCode'] === 200){         		 	
			         		 	 alertMsg.correct("成功生成");	  
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
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/exameeinfo/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						
					<%-- 	<c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="exameeinfo_brSchoolName" sel-name="branchSchool"
								sel-onchange="linkageQueryUnit()" sel-classs="flexselect"></span></li>
						<%-- </c:if> --%>
						<%-- <c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="exameeinfo_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlanId" tabindex="1" id="exameeInfo_recruitPlanId"
								value="${condition['recruitPlanId']}" style="width:140px;" /></li>
						<li><label>层次：</label> <span sel-id="exameeInfo_classic"
							sel-name="classic" sel-onchange="linkageQueryClassic()"
							sel-style="width: 120px;"></span></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="exameeInfo_major"
							sel-name="major" sel-classs="flexselect"></span>
						</li>
						<li><label>考生号：</label> <input type="text"
							name="enrolleeCode" value="${condition['enrolleeCode']}"
							style="width: 140px;" id="exameeInfo_enrolleeCode" /></li>
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name']}" style="width: 115px;"
							id="exameeInfo_name" /></li>
						<li><label>考生状态：</label> <gh:select
								dictionaryCode="CodeEnrollStatus" id="exameeInfo_kszt"
								name="kszt" value="${condition['kszt'] }" style="width:120px;" />
						</li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>证件号码：</label> <input type="text" name="certNum"
							value="${condition['certNum']}" class="custom-inp"
							id="exameeInfo_certNum" /></li>
						
						<li><label>注册状态：</label> <select name="registorFlag"
							id="registorFlag" style="width: 120px;">
								<option value="">请选择</option>
								<%--  --%>
								<option value="N"
									<c:if test="${condition['registorFlag'] eq 'N' }">selected="selected"</c:if>>未注册</option>
								<option value="Y"
									<c:if test="${condition['registorFlag'] eq 'Y' }">selected="selected"</c:if>>已注册</option>
						</select></li>
						<li><label>相片状态：</label> <select name="isExistsPhoto"
							id="isExistsPhoto" style="width: 120px;">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isExistsPhoto'] eq 'Y' }">selected="selected"</c:if>>存在</option>
								<option value="N"
									<c:if test="${condition['isExistsPhoto'] eq 'N' }">selected="selected"</c:if>>缺失</option>
						</select></li>
						<li><label>录取通知书：</label> <select name="isPrint"
							id="isPrint" style="width: 120px;">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isPrint'] eq 'Y' }">selected="selected"</c:if>>已下载</option>
								<option value="N"
									<c:if test="${condition['isPrint'] eq 'N' }">selected="selected"</c:if>>未下载</option>
						</select></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_RECRUIT_EXAMEEINFO" pageType="list" ></gh:resAuth>
			<%-- <table class="table" <c:choose><c:when test="${funcNum>12 }">layouth="209"</c:when><c:otherwise>layouth="189"</c:otherwise></c:choose> > --%>
				<table class="table" layouth="209">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_exameeinfo"
							onclick="checkboxAll('#check_all_exameeinfo','resourceid','#exameeInfoBody')" /></th>
						<th width="7%">招生批次</th>
						<th width="6%">考生号</th>
						<th width="6%">准学号</th>
						<th width="4%">姓名</th>
						<th width="9%">证件号码</th>
						<th width="8%">录取专业</th>
						<th width="8%">准考证号</th>
						<th width="3%">性别</th>
						<th width="6%">出生日期</th>
						<th width="4%">考生状态</th>
						<th width="4%">已注册</th>
						<th width="3%">已下载</th>
						<th width="10%">教学站</th>
						<c:choose>
							<c:when test="${isShow=='Y' }">
								<th width="5%">联系电话</th>
								<th width="3%">原因</th>
								<th width="3%">跟读</th>
								<th width="4%">录取编号</th>
								<th width="3%">备注</th>
							</c:when>
							<c:otherwise>
								<th width="8%">联系电话</th>
								<th width="3%">原因</th>
								<th width="3%">跟读</th>
								<th width="4%">备注</th>
							</c:otherwise>
						</c:choose>
					</tr>
				</thead>
				<tbody id="exameeInfoBody">
					<c:forEach items="${exameeInfoPage.result}" var="exameeInfo"
						varStatus="vs">
						<tr>
							<%-- 
			        <td><input type="checkbox" name="resourceid" value="${exameeInfo.resourceid }" autocomplete="off" rel="${exameeInfo.KSZT }" xm="${exameeInfo.XM }" /></td>
		        	<td>${exameeInfo.recruitPlan.recruitPlanname}</td>
		            <td>${exameeInfo.KSH }</td>
		            <td><a href="${baseUrl }/edu3/framework/exameeinfo/view.html?resourceid=${exameeInfo.resourceid }" target="dialog" rel="exameeInfoView" width="800" height="600" title="${exameeInfo.XM }">${exameeInfo.XM }</a></td>
		            <td>${exameeInfo.SFZH }</td>
		            <td>${exameeInfo.LQZYMC }</td>
		            <td>${exameeInfo.ZKZH }</td>		           
		            <td>${ghfn:dictCode2Val('CodeSex',exameeInfo.XBDM)}</td>
		            <td><fmt:formatDate value="${exameeInfo.CSRQ }" pattern="yyyy-MM-dd"/></td>
		            <td>${ghfn:dictCode2Val('CodeEnrollStatus',exameeInfo.KSZT) }</td>
		         --%>
							<td><input type="checkbox" name="resourceid"
								value="${exameeInfo['RESOURCEID']}" autocomplete="off"
								rel="${exameeInfo['KSZT']}" xm="${exameeInfo['XM']}"
								major="${exameeInfo['LQZYMC']}"
								enrolleeId="${exameeInfo.enrolleeId }" /></td>
							<td>${exameeInfo['RECRUITPLANNAME']}</td>
							<td>${exameeInfo['KSH']}</td>
							<td>${exameeInfo['MATRICULATENOTICENO'] }</td>
							<td><a
								href="${baseUrl }/edu3/framework/exameeinfo/view.html?resourceid=${exameeInfo['RESOURCEID'] }"
								target="dialog" rel="exameeInfoView" width="800" height="600"
								title="${exameeInfo['XM'] }">${exameeInfo['XM'] }</a></td>
							<td>${exameeInfo['SFZH'] }</td>
							<td>${exameeInfo['LQZYMC'] }</td>
							<td>${exameeInfo['ZKZH'] }</td>
							<td>${ghfn:dictCode2Val('CodeSex',exameeInfo['XBDM'])}</td>
							<td>${exameeInfo['CSRQ'] }</td>
							<td>${ghfn:dictCode2Val('CodeEnrollStatus',exameeInfo['KSZT']) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',exameeInfo['REGISTERFLAG']) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',exameeInfo['isPrint']) }</td>
							<td>${exameeInfo['UNITNAME'] }</td>
							<td>${exameeInfo['LXDH'] }</td>
							<td>${exameeInfo['NOREPORTREASON'] }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',exameeInfo['ISSTUDYFOLLOW'])}</td>
							<c:if test="${isShow=='Y' }">
								<td>${exameeInfo['ENROLLNO'] }</td>
							</c:if>
							<td>${exameeInfo['MEMO'] }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
		<gh:page page="${exameeInfoPage}"
				goPageUrl="${baseUrl }/edu3/recruit/exameeinfo/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>
