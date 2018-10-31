<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业生数据</title>
<style type="text/css">
	th,td{text-align: center;}
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		if('${isflush}'){
			var url = "${baseUrl}/edu3/schoolroll/graduation/student/list.html";
			//刷新页面
			navTab.openTab('RES_SCHOOL_GRADUATION_MANAGE', url, '毕业生库');
		}
		graduationDataQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function graduationDataQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "";
		var showCenter = "${showCenter}";
		if(showCenter!='show'){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['grade']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['teachingType']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classId']}";
		var selectIdsJson = "{unitId:'eiinfo_brSchoolName',gradeId:'graduationData_grade',classicId:'classic',teachingType:'graduationData_teachingtype_id',majorId:'major',classesId:'graduationData_classid'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function graduationDataQueryUnit() {
		var defaultValue = $("#eiinfo_brSchoolName").val();
		var selectIdsJson = "{gradeId:'graduationData_grade',classicId:'classic',teachingType:'graduationData_teachingtype_id',majorId:'major',classesId:'graduationData_classid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function graduationDataQueryGrade() {
		var defaultValue = $("#eiinfo_brSchoolName").val();
		var gradeId = $("#graduationData_grade").val();
		var selectIdsJson = "{classicId:'classic',teachingType:'graduationData_teachingtype_id',majorId:'major',classesId:'graduationData_classid'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function graduationDataQueryClassic() {
		var defaultValue = $("#eiinfo_brSchoolName").val();
		var gradeId = $("#graduationData_grade").val();
		var classicId = $("#classic").val();
		var selectIdsJson = "{teachingType:'graduationData_teachingtype_id',majorId:'major',classesId:'graduationData_classid'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择学习形式
	function graduationDataQueryTeachingType() {
		var defaultValue = $("#eiinfo_brSchoolName").val();
		var gradeId = $("#graduationData_grade").val();
		var classicId = $("#classic").val();
		var teachingTypeId = $("#graduationData_teachingtype_id").val();
		var selectIdsJson = "{majorId:'major',classesId:'graduationData_classid'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function graduationDataQueryMajor() {
		var defaultValue = $("#eiinfo_brSchoolName").val();
		var gradeId = $("#graduationData_grade").val();
		var classicId = $("#classic").val();
		var teachingTypeId = $("#graduationData_teachingtype_id").val();
		var majorId = $("#major").val();
		var selectIdsJson = "{classesId:'graduationData_classid'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
	}
	
	function switchStudentInfo(){//切换学生账号	
		if(isCheckOnlyone('graduateDataResourceid','#graduateBody')){
			var obj = $("#graduateBody input[name='graduateDataResourceid']:checked");
			var username = obj.attr("title");
			switchSecurityTargetUser(username);
		}
		
	}
	//删除  保留功能
	function doGraduationStudentDel(){	

		if(!isChecked('graduateDataResourceid',"#graduateBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		/*
		var res = "";
		var k = 0;
		var num  = $("#graduateBody"+" input[name='graduateDataResourceid']:checked").size();
		$("#graduateBody"+" input[@name='graduateDataResourceid']:checked").each(function(){
                res+=$(this).val();
                if(k != num -1 ) res += ",";
                k++;
        });
		*/
		alertMsg.confirm("您确定要将本页选定的学生从毕业生库中删除么？", {
			okCall: function(){//执行			
				var res = "";
				var k = 0;
				var num  = $("#graduateBody"+" input[name='graduateDataResourceid']:checked").size();
				$("#graduateBody"+" input[@name='graduateDataResourceid']:checked").each(function(){
                        res+=$(this).val();
                        if(k != num -1 ) res += ",";
                        k++;
                });
				$.post("${baseUrl}/edu3/roll/graduation/student/delete.html",{resourceid:res}, navTabAjaxDone, "json");
				var url = "${baseUrl}/edu3/schoolroll/graduation/student/list.html";
				//刷新页面
				navTab.openTab('RES_SCHOOL_GRADUATION_MANAGE', url, '毕业生库');
			}
		});	
		
		
		/*
	 	//验证所选学生的毕业信息发布属性是否为未发布 废弃
		var url= "${baseUrl}/edu3/roll/graduation/student/validatePublishStatus.html?stus="+res;
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['isLegal']==false){
					alert("待删除毕业信息的学生中存在已发布毕业状态的学生，不可删除。请您先撤销发布状态再进行删除。");
					return false;
				}else if(data['isLegal_Degree']==false){
					alert("待删除毕业信息的学生中存在通过了学位审核的学生，不可删除。请您先撤销学生的学位获得状态再进行删除。");
					return false;
				}else{
					alertMsg.confirm("您确定要将选定的学生的学籍状态从\"毕业生库中删除么？", {
						okCall: function(){//执行			
							var res = "";
							var k = 0;
							var num  = $("#graduateBody"+" input[name='resourceid']:checked").size();
							$("#graduateBody"+" input[@name='resourceid']:checked").each(function(){
			                        res+=$(this).val();
			                        if(k != num -1 ) res += ",";
			                        k++;
			                });
							$.post("${baseUrl}/edu3/roll/graduation/student/delete.html",{resourceid:res}, navTabAjaxDone, "json");
							var url = "${baseUrl}/edu3/schoolroll/graduation/student/list.html";
							//刷新页面
							navTab.openTab('RES_SCHOOL_GRADUATION_MANAGE', url, '毕业生库');
						}
					});	
				}
			}
		});
			*/	
	}
	//查看学籍异动详情  保留功能
	function viewStuChangeInfoInGraduationLab(stuId){//查看
		$.pdialog.open('${baseUrl}/edu3/framework/register/stuchangeinfo/view.html?studentId='+stuId, 'RES_SCHOOL_SCHOOLROLL_CHANGE_VIEW', '查看学籍异动', {width: 800, height: 600});
	}
	//打印学生毕业证明  保留功能
	function printGraduateCertification(){
		var stus="";
		$("input[name='graduateDataResourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).val()+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false;
		}
		/*
		var graduateCertificationDate = $('#graduateCertificationDate').val();
		if(""==graduateCertificationDate){
			alertMsg.warn("请选择毕业证明的落款时间！");return false;
		}
		*/
		var url="${baseUrl}/edu3/roll/graduation/student/print-view.html?stus="+stus;//+"&graduateCertificationDate="+graduateCertificationDate;
		$.pdialog.open(url,"RES_TEACHING_GRADUATE_PRINT",'打印预览',{height:600, width:800});
	}
	
	//毕业信息统计  保留功能
	function exportGraduateStuStatToExcel(){
		var url = "${baseUrl}/edu3/schoolroll/graduate/excel/exportGraduateStuCondition.html";
		navTab.openTab('RES_SCHOOL_GRADUATESTU_STAT', url, '毕业信息统计');
	}
	//学位统计导出  保留功能
	function exportDegreeStat(){
		var url="${baseUrl}/edu3/roll/graduationStudent/exportDegreeInfoCondition.html";
		navTab.openTab('RES_TEACHING_GRADUATE_EXPORTDEGREE', url, '导出学位统计条件');
		//$.pdialog.open(url,"RES_TEACHING_GRADUATE_EXPORTDEGREE",'导出学位统计条件',{height:600, width:800});
	}
	//撤销学位 保留功能
	function undoDegree(){
		if(!isChecked('graduateDataResourceid',"#graduateBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		var confirmMsg="您确定要撤销本页选定的学生的学位获得状态么？";
		
		alertMsg.confirm(confirmMsg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#graduateBody"+" input[name='graduateDataResourceid']:checked").size();
					$("#graduateBody"+" input[@name='graduateDataResourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                });
					//$.post("${baseUrl}/edu3/roll/graduation/student/undoDegree.html",{resourceid:res}, navTabAjaxDone,async : false, "json");
					$.ajax({  
				        url : '${baseUrl}/edu3/roll/graduation/student/undoDegree.html',  
				        async : false, // 注意此处需要同步，因为返回完数据后，下面才能让结果的第一条selected  
				        type : "POST",  
				        dataType : "json",
				        data:{resourceid:res},
				        success : function(data) { 
				        	alertMsg.info(data.message);
				        }  
				    });
					$('#graduationDate-form').submit();
				}
		});	
		//var url = "${baseUrl}/edu3/schoolroll/graduation/student/list.html";
		//刷新页面
		//navTab.openTab('RES_SCHOOL_GRADUATION_MANAGE', url, '毕业生库');	
	}
	
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	//毕业数据自定义导出信息
	function exportGraduateStuToExcel(){
		var unit 					= $("#graduateData #eiinfo_brSchoolName").val();
		var grade 				= $("#graduateData #graduationData_grade").val();
		var major				= $("#graduateData #major").val();
		var classic 				= $("#graduateData #classic").val();
		var graduateDate 	= $("#graduateData #graduateDate").val();
		var unit_txt 				= $("#graduateData #eiinfo_brSchoolName_flexselect").val();
		var grade_txt 			= $("#graduateData select[id = 'grade'] option[value='"+grade+"']").text();
		var major_txt 			= $("#graduateData select[id = 'major'] option[value='"+major+"']").text();
		var classic_txt 		= $("#graduateData select[id = 'classic'] option[value='"+classic+"']").text();
		var confirmGraduateDateb = $("#gl_confirmGraduateDateb").val();//确认毕业日期始
		var confirmGraduateDatee = $("#gl_confirmGraduateDatee").val();//确认毕业日期终
		var graduationType		= $("#graduationData_graduationType_id").val();//毕业类型
		var teachingType        = $("#graduationData_teachingtype_id").val();//学习形式
		var classes				= $("#graduationData_classid").val();//班级
		var degreeStatus        = $("#degreeStatus").val();//学位状态
		var degreeApplyStatus        = $("#graduationData_degreeApplyStatus").val();//学位申请状态
		
		var selectCondition = "unit="+unit+"&grade="+grade+"&major="+major+"&classic="+classic+"&graduateDate="+graduateDate+"&confirmGraduateDateb="+confirmGraduateDateb+"&confirmGraduateDatee="+confirmGraduateDatee;
		//var selectContent    = "unit_txt="+unit_txt+"&grade_txt="+grade_txt+"&major_txt="+major_txt+"&classic_txt="+classic_txt;
		var url = "${baseUrl}/edu3/roll/graduationStudent/exportCustomExcelInGBDCondition.html?"+selectCondition+"&classes="+classes+"&teachingType="
				+teachingType+"&graduationType="+graduationType+"&degreeStatus="+degreeStatus+"&degreeApplyStatus="+degreeApplyStatus;
		$.pdialog.open(url,'RES_SCHOOL_GRADUATESTU_CUSTOMEXPORT','毕业数据自定义导出信息',{width:800,height:500});
		
	}
	
	
	function graduate_pageBarHandle(msg,postUrl,bodyname){
		if(!isChecked('graduateDataResourceid',bodyname)){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm(msg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $(bodyname+" input[name='graduateDataResourceid']:checked").size();
					$(bodyname+" input[@name='graduateDataResourceid']:checked").each(function(){
	                        res+=$(this).attr('studentId');
	                        if(k != num -1 ) res += ",";
	                        k++;
	                 });	                
					$.post(postUrl,{resourceid:res}, navTabAjaxDone, "json");
				}
		});			
	}
	function disenabledGraduateStudentAccount(){//停用账号			
		graduate_pageBarHandle("您确定要停用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=disenable","#graduateBody");		
	}	
	function enabledGraduateStudentAccount(){//启用账号		
		graduate_pageBarHandle("您确定要启用这些学生账号吗？","${baseUrl}/edu3/register/studentinfo/enableaccount.html?type=enable","#graduateBody");			
	}
	function resetGraduateStudentAccountPassword(){//重置密码	
		graduate_pageBarHandle("您确定要重置这些学生账号的密码吗？","${baseUrl}/edu3/register/studentinfo/resetpassword.html","#graduateBody");			
	}
	//打印所选学生的成绩单 函数名已与打印成绩单页面作了区别
	function printChooseStuScoreReportCard(flag){
		var studentId = new Array();
		jQuery("#graduateBody input[name='graduateDataResourceid']:checked").each(function(){
			studentId.push(jQuery(this).attr('studentId'));
		});
    	if(studentId.length>0){
    		if("pass"==flag){
    			var url       = "${baseUrl}/edu3/roll/graduationStudent/personalReportCard-view.html?flag="+flag+"&studentId="+studentId.toString();
    			alertMsg.confirm("确定要打印所选学生的及格成绩单吗？", {
                    okCall: function(){
                    	$.pdialog.open(url,"RES_SCHOOL_GRADUATION_VIEW_STUDENT_RESULT_CHOOSE_PRINT_PASS_VIEW","打印预览",{width:800, height:600});	
                    }
    			});
    		}
    	}else{
    		alertMsg.warn("请选择要打印的学生!");
    		return false;
    	}
		
	}
	//学籍卡打印 
	function studentRollCardPrint(){
		
		var url          = "${baseUrl}/edu3/roll/studentCard/print-view.html?flag=print";
		var param = getUrlParam();
		if(param==null){
			alertMsg.warn("请选择查询条件或勾选学生后再进行操作！");	
			return false;
		}
		$.pdialog.open(url+param,"RES_SCHOOL_GRADUATION_MANAGE_ROLL_CARD_PRINT","打印预览",{width:800, height:600});	
	}
	//毕业生登记表
	function studentRegistryFormPrint(){
		var url          = "${baseUrl}/edu3/roll/RegistryForm/print-view.html?flag=print";
		var param = getUrlParam();
		if(param==null){
			alertMsg.warn("请选择查询条件或勾选学生后再进行操作！");	
			return false;
		}
		$.pdialog.open(url+param,"RES_SCHOOL_GRADUATION_REGISTRYFORM","打印预览",{width:800, height:600});	
	}
	
	//打印毕业证书,学位证书
	function printDiploma(diplomaType){//1-毕业证书	2-学位证书
		var url          = "${baseUrl}/edu3/roll/graduation/diploma/print-view.html?diplomaType="+diplomaType;
		var param = getUrlParam();
		if(param==null){
			alertMsg.warn("请选择查询条件或勾选学生后再进行操作！");	
			return false;
		}
		$.pdialog.open(url+param,"RES_SCHOOL_GRADUATION_DIPLOMA","打印预览",{width:800, height:600});	
	}
	//导出毕业信息
	function exportCertificateNo() {
	    var url = "${baseUrl}/edu3/schoolroll/graduation/student/exportCertificateNo.html?flag=export&";
	    url += $("#graduationDate-form").serialize();
		//var param = getUrlParam();
        downloadFileByIframe(url,'exportCertificateNo_Ifram',"post");
    }
	//导入毕业信息
	function importCertificateNo() {
        $.pdialog.open("${baseUrl}/edu3/schoolroll/graduation/student/importCertificateNoForm.html", 'RES_SCHOOL_GRADUATION_IMPORT', '导入毕业信息', {width: 600, height: 360});
    }
	//获取请求参数
	function getUrlParam(){
		var url = "";		
		var idsArray 	 = new Array();
		var ids          = "";
		var branchSchool = "${condition['branchSchool']}";
		var grade        = "${condition['grade']}";
		var major        = "${condition['major']}";
		var classic      = "${condition['classic']}";
		var teachingtype = "${condition['teachingType']}";
		var name         = "${condition['name']}";
		var studyNo      = "${condition['studyNo']}";
		var graduateDate = "${condition['graduateDate']}";
		var gl_confirmGraduateDateb = "${condition['confirmGraduateDateb']}";
		var gl_confirmGraduateDatee = "${condition['confirmGraduateDatee']}";
		var classes				= $("#graduationData_classid").val();//班级
		var degreeStatus        = "${condition['degreeStatus']}";//学位状态
		var degreeApplyStatus        = "${condition['degreeApplyStatus']}";//学位申请状态
		
		
		$("#graduateBody input[name='graduateDataResourceid']:checked").each(function(){
			idsArray.push($(this).val());
		});
		
		if(!branchSchool&&!grade&&!major&&!classic&&!name&&!studyNo&&!graduateDate
				&&!gl_confirmGraduateDateb&&!gl_confirmGraduateDatee&&idsArray.length==0&&!degreeApplyStatus){
			return null;
		}
		if(idsArray.length>0){
			ids = idsArray.join(",");
			url += "&resourceid="+ids; 
		}else{
			if(branchSchool) url += "&branchSchool="+branchSchool; 
			if(classes) url += "&classes="+classes; 
			if(degreeStatus) url += "&degreeStatus="+degreeStatus; 
			if(grade) url += "&grade="+grade; 
			if(major) url += "&major="+major; 
			if(classic) url += "&classic="+classic; 
			if(teachingtype) url += "&teachingtype="+teachingtype;
			if(name) url += "&name="+encodeURI(name); 
			if(studyNo) url  += "&studyNo="+studyNo; 
			if(graduateDate) url += "&graduateDate="+graduateDate; 
			if(gl_confirmGraduateDateb) ur += "&confirmGraduateDateb="+gl_confirmGraduateDateb; 
			if(gl_confirmGraduateDatee) url += "&confirmGraduateDatee="+gl_confirmGraduateDatee; 
			if(degreeApplyStatus) url += "&degreeApplyStatus="+degreeApplyStatus; 
		}
		return url;
	}
	//撤销学位
	function DegreeDel(){
		var idsArray 	 = new Array();
		var ids          = "";
		var count = 0;
		$("input[name='graduateDataResourceid']").each(function(){
			if($(this).attr("checked")){
				var id = $(this).val()+"status";
				var status = $('#'+id).val();
				if('Y' == status || 'N' == status ){					
					idsArray.push($(this).val());	
				}
			}
			count++;
		});
		if(idsArray.length == 0 || count == 0){
			alertMsg.warn("请先选择一条可操作的数据(<font color='blue'>非未审核状态的数据)!");	
			return false;
		}
		alertMsg.confirm("共"+count+"条数据,可操作"+idsArray.length+"条(<font color='blue'>不含未审核状态的数据)</font>;是否要为这些数据执行撤销学位操作?", {
            okCall: function(){
            	if(idsArray.length>0) ids = idsArray.join(",");
            	$.ajax({  
			        url : '${baseUrl}/edu3/roll/graduation/student/degree_delete.html',  
			        async : false,
			        type : "POST",  
			        dataType : "json",
			        data:{resourceid:ids},
			        success : function(data) { 
			        	alertMsg.info(data.message);
			        }  
			    });
				$('#graduationDate-form').submit();
            }
		});
	}
	
	//修正学分
	function UpdateGraduationStudent(){
		var stus="";
		$("input[name='graduateDataResourceid']").each(function(){
			if($(this).attr("checked")){
				stus += ""+$(this).attr('studentId')+",";
			}
		});
		if(""==stus){
			alertMsg.warn("请选择一条或多条记录！");return false;
		}
		var url= "${baseUrl }/edu3/teaching/result/graduation_calculateTotalCreditHour.html?stus="+stus;
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				var msg= data['msg'];
				alertMsg.warn(msg);
			}
		});
	}
	
	// 学位申请审核通过
	function degreeApplyPass(){
		pageBarHandle("您确定审核通过这些记录吗？","${baseUrl}/edu3/roll/graduation/degreeApply/audiate.html?result=pass","#graduateBody");
	}
	
	// 学位申请审核不通过
	function degreeApplyNoPass(){
		pageBarHandle("您确定将这些记录审核不通过吗？","${baseUrl}/edu3/roll/graduation/degreeApply/audiate.html?result=nopass","#graduateBody");
	}
	
	function pageBarHandle(msg,postUrl,bodyname){
		if(!isChecked('graduateDataResourceid',bodyname)){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm(msg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $(bodyname+" input[name='graduateDataResourceid']:checked").size();
					$(bodyname+" input[@name='graduateDataResourceid']:checked").each(function(){
						 res+=$(this).val();
                         if(k != num -1 ) res += ",";
	                     k++;
	                 });	                
					$.post(postUrl,{resourceid:res}, navTabAjaxDone, "json");
				}
		});			
	}
	
	// 打印学位申请表
	function printDegreeApplyForm(){
		var url = "${baseUrl}/edu3/roll/graduation/degreeApply/print-view.html?flag=print";
		var param = getUrlParam();
		if(param==null){
			alertMsg.warn("请选择查询条件或勾选学生后再进行操作！");	
			return false;
		}
		alertMsg.confirm("确定打印学位申请状态为审核通过的学生申请表？", {
            okCall: function(){
            	$.pdialog.open(url+param,"RES_SCHOOL_GRADUATION_DEGREEAPPLY","学位申请打印预览",{width:800, height:600});	
            }
        });
	}
	
	// 导出学位申请汇总表
	function exportDegreeApplySummary(){
		var purl = "${baseUrl }/edu3/roll/graduation/degreeApply/exportSummary.html?flag=export";
		var param = getUrlParam();
		if(param==null){
			alertMsg.warn("请选择查询条件或勾选学生后再进行操作！");	
			return false;
		}
		alertMsg.confirm("确定要导出学位申请汇总表吗？",{
			okCall:function(){
				downloadFileByIframe(purl+param,'exportDegreeApplySummaryIframe');
			}
		});
	}
	
	// 替学生申请学位
	function applyDegreeForStudent(){
		var idsArray = new Array();
		$("#graduateBody input[name='graduateDataResourceid']:checked").each(function(){
			idsArray.push($(this).val());
		});
		if(idsArray.length < 1){
			alertMsg.info("请至少选择一条要操作的记录");
			return false;
		}
		
		alertMsg.confirm("您确定要替符合条件的学生申请学位吗？",{
			okCall:function(){
				$.ajax({  
			        url : '${baseUrl}/edu3/schoolroll/graduation/applyDegree.html',  
			        async : false, 
			        type : "POST",  
			        dataType : "json",
			        data:{applyResource:'noStudent',graduateDataId:idsArray.join(",")},
			        success : function(data) { 
			        	if(data.statusCode==200){
			        		alertMsg.correct(data.message);
			        		
			        	}else{
			        		alertMsg.error(data.message);
			        	}
			        }  
			    });
			}
		});
	}

    /**
	 * 下载学位材料编辑模版
     */
	function downloadMaterialsStatusModel() {
        window.location.href="${baseUrl }/edu3/roll/graduation/degreeMaterialsModel/download.html"
    }

    /**
	 * 导入学位材料状态结果
     */
    function importMaterialsStatus(){
        $.pdialog.open(baseUrl+"/edu3/roll/graduation/degreeMaterialsModel/import.html", '导入学位审核材料提交状态', {width: 600, height: 400});
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<input type="hidden" id="changePStatus" value="" />
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduation/student/list.html"
				method="post" id="graduationDate-form">
				<div id="graduateData" class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${showCenter eq 'show'}"> --%>
							<li style="width:360px;"><label>教学站：</label> <span sel-id="eiinfo_brSchoolName"
								sel-name="branchSchool" sel-onchange="graduationDataQueryUnit()"
								sel-classs="flexselect" ></span></li>
						<%-- </c:if>
						<c:if test="${!(showCenter eq 'show')}">
							<input type="hidden" name="branchSchool" id="eiinfo_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="graduationData_grade"
							sel-name="grade" sel-onchange="graduationDataQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="graduationDataQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习形式：</label> <span
							sel-id="graduationData_teachingtype_id" sel-name="teachingType"
							sel-onchange="graduationDataQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 100px"></span>
						</li>

						<!-- 
				<li>
					<label>发布状态：</label><gh:select dictionaryCode="CodeReleaseState" name="publishStatus" id="publishStatus" value="${condition['publishStatus']}"
							 style="width:120px" filtrationStr="0,1" />
				</li>
				 -->
					</ul>
					<ul class="searchContent">
						<li style="width:360px;"><label>专业：</label> <span sel-id="major" sel-name="major"
							sel-onchange="graduationDataQueryMajor()" sel-classs="flexselect"></span></li>
						
						<li><label>姓名：</label> <input type="text" name="name"
							id="name" value="${condition['name']}" style="width: 120px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							id="studyNo" value="${condition['studyNo']}" style="width: 115px" />
						</li>
						<li><label>学籍卡状态：</label>
						<gh:select id="rollCard" name="rollCard"
								dictionaryCode="CodeRollCardStatus"
								value="${condition['rollCard']}" style="width: 100px" /></li>
					</ul>
					<ul class="searchContent">
						<li style="width:360px;"><label>班级：</label> <span sel-id="graduationData_classid"
							sel-name="classId" sel-classs="flexselect"></span></li>
						
						<li><label>毕业时间：</label> <select name="graduateDate"
							id="graduateDate" style="width: 120px">
								<option value="">请选择</option>
								<c:forEach items="${gDates} " var="gdate">
									<option value="${fn:replace(fn:replace(gdate,'[',''),']','')}"
										<c:if test="${fn:replace(fn:replace(gdate,'[',''),']','')==condition['graduateDate']}"> selected="selected"  </c:if>>${fn:replace(fn:replace(gdate,'[',''),']','')}</option>
								</c:forEach>
						</select></li>
						<li><label>毕业类型：</label> 
						<c:choose>
						<c:when test="${schoolCode eq '12962' }">
							<gh:select dictionaryCode=""
								isUseCustom="Y" customValues="16,27" customNames="毕业,缓毕业"
								style="width:120px;" name="graduationType"
								id="graduationData_graduationType_id"
								value="${condition['graduationType']}" />
						</c:when>
						<c:otherwise>
						<gh:select dictionaryCode=""
								isUseCustom="Y" customValues="16,24,27" customNames="毕业,结业,预毕业"
								style="width:120px;" name="graduationType"
								id="graduationData_graduationType_id"
								value="${condition['graduationType']}" />
						</c:otherwise>
						</c:choose>
						</li>
						<li><label>学位状态：</label> <select name="degreeStatus"
							id="degreeStatus" style="width: 100px;">
								<option value="">请选择</option>
								<option
									<c:if test="${condition['degreeStatus']=='W'}">selected="selected"</c:if>
									value="W">待审核</option>
								<option
									<c:if test="${condition['degreeStatus']=='Y'}">selected="selected"</c:if>
									value="Y">已获得</option>
								<option
									<c:if test="${condition['degreeStatus']=='N'}">selected="selected"</c:if>
									value="N">未获得</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 360px;">
							<label>审核时间：</label> 
							<input type="text"
								id="gl_confirmGraduateDateb" style="width: 80px;"
								name="confirmGraduateDateb" class="Wdate"
								value="${condition['confirmGraduateDateb']}"
								onfocus="WdatePicker({isShowWeek:true })" /> 到 <input
								type="text" id="confirmGraduateDatee" style="width: 80px;"
								name="confirmGraduateDatee" class="Wdate"
								value="${condition['gl_confirmGraduateDatee']}"
								onfocus="WdatePicker({isShowWeek:true })" />
						</li>
						<li>
							毕业材料交齐：
							<gh:select dictionaryCode="yesOrNo"  style="width:120px;" name="hasPracticeMaterials" id="graduationData_hasPracticeMaterials" value="${condition['hasPracticeMaterials']}" />
						</li>
						<li>
							学位材料交齐：
							<gh:select dictionaryCode="yesOrNo"  style="width:120px;" name="hasDegreeMaterials" id="graduationData_hasDegreeMaterials" value="${condition['hasDegreeMaterials']}" />
						</li>
						<!-- 目前只有广外使用，以后多学校使用则使用全局参数 -->
						<c:if test="${schoolCode eq '11846' }">
							<li>
								<label>学位申请：</label>
								<gh:select dictionaryCode="CodeDegreeApplyStatus"  style="width:120px;" name="degreeApplyStatus" id="graduationData_degreeApplyStatus" value="${condition['degreeApplyStatus']}" />
							</li>
						</c:if>
					</ul>
					<ul>
						<li style="width: 500px;margin-top: 5px;"><font color="red">注意：若学生无选框，说明该学生丢失了基本信息，将无法打印毕业证明。
						</font></li>
					</ul>
					<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					<!-- 
			<ul>
				<li>
					    <font color="red">学位审核条件</font>&nbsp;&nbsp; &nbsp; &nbsp;     主干课<input type="checkbox" 	id="mainC" value="on" checked="checked" /> &nbsp; &nbsp; 
						毕业论文<input type="checkbox" 	id="graduateC" value="on"  checked="checked"/> &nbsp; &nbsp; 
						专业基础课<input type="checkbox" id="majorBaseC" value="on"  checked="checked"/> &nbsp; &nbsp; 
						专业课<input type="checkbox" 	id="majorC" value="on"  checked="checked"/> &nbsp; &nbsp; 
						外国语课<input type="checkbox" 	id="flC" value="on"  checked="checked"/> &nbsp; &nbsp; 
				</li>
			</ul>
			 -->
					<ul>
						<!-- 
				<li>
				毕业证明时间:   <input type="text" name="graduateCertificationDate" id="graduateCertificationDate" class="Wdate" value="${setDate}" onfocus="WdatePicker({isShowWeek:true})" /><font color="red">注意：若学生无选框，说明该学生丢失了基本信息，将无法打印毕业证明。 </font>
				</li>
				 -->
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_MANAGE" pageType="list"></gh:resAuth><!-- 175 -->
				<%-- <table class="table" <c:choose><c:when test="${funcNum>13 }">layouth="199"</c:when><c:otherwise>layouth="175"</c:otherwise></c:choose>> --%>
				<table class="table"  layouth="205">
				<thead>
				
					<tr>
						<th width="3%"><input type="checkbox" id="check_all_gd" name="checkall"
							<c:if test="${condition['checkAllFlag_dA']=='1'}">checked="checked"</c:if>
							onclick="trueCheckAll()" /></th>
						<th width="6%">学号</th>
						<th width="4.5%">姓名</th>
						<th width="4%">年级</th>
						<th width="4%">学习形式</th>
						<th width="8%">教学站</th>
						<th width="6%">专业</th>
						<th width="9%">班级</th>
						<th width="3.5%">层次</th>
						<th width="9%">毕业证书编号</th>
						<th width="8%">学位证书编号</th>
						<th width="4%">学位名称</th>
						<th width="4%">毕业类型</th>
						<th width="5%">毕业日期</th>
						<th width="5%">审核材料交齐</th>
						<th width="4%">学籍卡</th>
						<th width="4%">学位状态</th>
						<th width="4%">账号状态</th>
						<c:if test="${schoolCode eq '11846' }">
							<th width="5%">学位申请状态</th>
						</c:if>
						<th width="5%">其他操作</th>
						
						<!--  <th width="5%">发布状态</th> -->
					</tr>
				</thead>
				<tbody id="graduateBody">
					<c:forEach items="${graduationStudentList.result}" var="g"
						varStatus="vs">
						<tr>
							<td><c:if test="${g.hasBaseInfo==true }">
									<input type="checkbox" name="graduateDataResourceid" value="${g.resourceid }"
										title="${g.studentInfo.sysUser.username}"
										studentId="${g.studentInfo.resourceid }" autocomplete="off"
										<c:if test="${condition['checkAllFlag_dA']=='1'}">checked="checked"</c:if> />
								</c:if></td>
							<td>${g.studentInfo.studyNo }</td>
							<td><c:if test="${g.hasStuChange==true}">
									<a href="javascript:void(0)" style="color: red;"
										onclick="viewStuChangeInfoInGraduationLab('${g.studentInfo.resourceid}')"
										title="点击查看">${g.studentInfo.studentName }</a>
								</c:if> <c:if test="${g.hasStuChange==false}">${g.studentInfo.studentName }</c:if>
							</td>
							<td>${g.studentInfo.grade }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',g.studentInfo.teachingType)}</td>
							<td style="text-align: left;">${g.studentInfo.branchSchool.unitName }</td>
							<td style="text-align: left;">${g.studentInfo.major }</td>
							<td style="text-align: left;">${g.studentInfo.classes.classname }</td>
							<td>${g.studentInfo.classic }</td>
							<td>${g.diplomaNum }</td>
							<td>${g.degreeNum }</td>
							<td>${g.degreeName }</td>
							<td>${ghfn:dictCode2Val("CodeGraduateType",g.graduateType) }</td>
							<%-- <td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${g.graduateDate }" /></td> --%>
							<td>${g.graduateDate }</td>
							<td title="毕业审核材料交齐/学位审核材料交">${ghfn:dictCode2Val('yesOrNo_default',g.studentInfo.hasPracticeMaterials)}/${ghfn:dictCode2Val('yesOrNo_default',g.studentInfo.hasDegreeMaterials)}</td>
							<%--<td>${g.studentInfo.hasPracticeMaterials} / ${g.studentInfo.hasDegreeMaterials}</td>--%>
							<td <c:if test="${g.studentInfo.rollCardStatus eq '2'}">style='color: blue'</c:if>
								<c:if test="${g.studentInfo.rollCardStatus eq '1'}">style='color: green'</c:if>
								<c:if test="${g.studentInfo.rollCardStatus eq '0'}">style='color: red'</c:if>
								><c:choose>
									<c:when test="${g.studentInfo.rollCardStatus eq '2'}">
										已提交
									</c:when>
									<c:when test="${g.studentInfo.rollCardStatus eq '1'}">
										已保存
									</c:when>
									<c:otherwise>
										未保存
									</c:otherwise>
								</c:choose></td>
							<td <c:if test="${g.degreeStatus=='Y'}">style='color: blue'</c:if>
								<c:if test="${g.degreeStatus=='N'}">style='color: red'</c:if>
								 ><c:choose>
									<c:when test="${g.degreeStatus=='Y'}">
										已获得
									</c:when>
									<c:when test="${g.degreeStatus=='N'}">
										未获得
									</c:when>
									<c:when test="${g.degreeStatus=='W'}">待审核</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose> <input type="hidden" id="${g.resourceid}status"
								value="${g.degreeStatus}"></td>
							<td <c:if test="${g.studentInfo.accountStatus ne 1}">style='color: red'</c:if>>
								${g.studentInfo.accountStatus==1?"激活":"停用"}</td>
							<c:if test="${schoolCode eq '11846' }">	
								<td>${ghfn:dictCode2Val('CodeDegreeApplyStatus',g.degreeApplyStatus)}</td>
							</c:if>
							<td><a href="javascript:void(0)"
								onclick="viewStuInfo2('${g.studentInfo.resourceid}')"
								title="点击查看"><font color="#578ACE">学籍</font></a> | <a
								href="javascript:void(0)"
								onclick="studentTranscriptPrint('${g.studentInfo.resourceid}','${g.studentInfo.studentName }');">成绩</a>
							</td>
							<!-- 
			            <td >
			            <c:choose>
			            <c:when test="${g.publishStatus=='Y'}"><font color="red">已发布</font></c:when>
			            <c:when test="${g.publishStatus=='N'}">已撤销</c:when>
			            <c:when test="${g.publishStatus=='W'}">未发布</c:when>
			            <c:otherwise>-</c:otherwise>
			            </c:choose>
			            </td>
			            -->
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${graduationStudentList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/student/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
	<script type="text/javascript">
function trueCheckAll(){
	if($("#check_all_gd").attr("checked")==true){
		$("input[name='checkAllFlag_dA']").val("1");
	}else{
		$("input[name='checkAllFlag_dA']").val("0");
	}
	checkboxAll('#check_all_gd','graduateDataResourceid','#graduateBody');
}

function studentTranscriptPrint(studentId,studentName){
	var url = "${baseUrl}/edu3/teaching/result/view-student-examresults.html?studentId="+studentId;
	navTab.openTab("RES_TEACHING_RESULT_MANAGE_SUBMIT_RESULTS_LIST_1",url,studentName+"成绩列表");	
}

//学位审核(移动到新的页面)
/*
function degreeAudit(){
	var stus="";
	$("input[name='graduateDataResourceid']").each(function(){
		if($(this).attr("checked")){
			stus += ""+$(this).val()+",";
		}
	});
	if(""==stus){
		alertMsg.warn("请选择一条或多条记录！");return false
	}
	//审核条件
	var mainC = $("#mainC").attr("checked");
	var graduateC = $("#graduateC").attr("checked");
	var majorBaseC = $("#majorBaseC").attr("checked");
	var majorC = $("#majorC").attr("checked");
	var flC = $("#flC").attr("checked");
    var degreeCon = "&mainC="+mainC+
    "&graduateC="+graduateC+
    "&majorBaseC="+majorBaseC+
    "&majorC="+majorC+
    "&flC="+flC;
    //查询条件
    var branchSchool=$('#graduateData #eiinfo_brSchoolName').val()==undefined?"":$('#graduateData #eiinfo_brSchoolName').val();
	var major		=$('#graduateData #major').val()==undefined?"":$('#graduateData #major').val();
	var classic		=$('#graduateData #classic').val()==undefined?"":$('#graduateData #classic').val();
	var publishStatus	=$('#graduateData #publishStatus').val()==undefined?"":$('#graduateData #publishStatus').val();
	var name		=$('#graduateData #name').val()==undefined?"":$('#graduateData #name').val();
	var matriculateNoticeNo	=$('#graduateData #studyNo').val()==undefined?"":$('#graduateData #studyNo').val();
	var grade 		=$('#graduateData #graduationData_grade').val()==undefined?"":$('#graduateData #grade').val();
	var selectCon =  "&branchSchool="+branchSchool+
    "&major="+major+
    "&classic="+classic+
    "&publishStatus="+publishStatus+
    "&name="+name+
    "&matriculateNoticeNo="+matriculateNoticeNo+
    "&grade="+grade;
	//是否使用全选
	var isSelectAll = $("input[name='checkAllFlag_dA']").val();
	if(""==grade&&"1"==isSelectAll){
		alertMsg.warn("您使用了按查询结果审核，请您选择年级条件，以保证审核的时间。"); return false;
	}
	
	var pNum       =$('#pNum').val()==undefined?"":$('#pNum').val();
	var postUrl="${baseUrl}/edu3/roll/graduateaudit/viaDegree.html?stus="+stus+degreeCon+selectCon+"&isSelectAll="+isSelectAll+"&doAudit=Y";
	alertMsg.confirm("确定学位审核，学位审核可能需要耗费比较长的时间，请耐心等待。",{
		okCall: function(){
			$.pdialog.open(postUrl, 'RES_SCHOOL_GRADUATION_DEGREEAUDIT', '学位审核结果', {max:true,maxable:true});
		}
	});
	
	//}
}*/
</script>
</body>
</html>