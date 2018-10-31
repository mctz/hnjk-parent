<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍异动</title>
<style type="text/css">
	th,td{text-align: center;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	schoolchangeinfoQueryBegin();
	
	var studentStatusSet= '${stuStatusSet}';
	var statusRes= '${stuStatusRes}';
	orgStuStatus("#stuChangeInfo #stuStatus",studentStatusSet,statusRes,"12,13,15,16,18,21,a11,b11,25");
	if(""=="${widthFlag}"){
		$("a[id^=0]").show();
	}else{
		$("a[id^=0]").show();
		$("a[id^=1]").hide();
		//显示是否增宽的操作连接
		/*
		if("${idColWidthC}"=="${idColWidth}"){$("#0idCol").show();$("#1idCol").hide();}else{$("#1idCol").show();$("#0idCol").hide();}
		if("${stuNumColWidthC}"=="${stuNumColWidth}"){$("#0stuNumCol").show();$("#1stuNumCol").hide();}else{$("#1stuNumCol").show();$("#0stuNumCol").hide();}
		if("${unitColWidthC}"=="${unitColWidth}"){$("#0unitCol").show();$("#1unitCol").hide();}else{$("#1unitCol").show();$("#0unitCol").hide();}
		if("${majorColWidthC}"=="${majorColWidth}"){$("#0majorCol").show();$("#1majorCol").hide();}else{$("#1majorCol").show();$("#0majorCol").hide();}
		if("${classesColWidthC}"=="${classesColWidth}"){$("#0classesCol").show();$("#1classesCol").hide();}else{$("#1classesCol").show();$("#0classesCol").hide();}
		if("${omajorColWidthC}"=="${omajorColWidth}"){$("#0omajorCol").show();$("#1omajorCol").hide();}else{$("#1omajorCol").show();$("#0omajorCol").hide();}
		if("${oclassesColWidthC}"=="${oclassesColWidth}"){$("#0oclassesCol").show();$("#1oclassesCol").hide();}else{$("#1oclassesCol").show();$("#0oclassesCol").hide();}
		if("${applyDateColWidthC}"=="${applyDateColWidth}"){$("#0applyDateCol").show();$("#1applyDateCol").hide();}else{$("#1applyDateCol").show();$("#0applyDateCol").hide();}
		if("${auditDateColWidthC}"=="${auditDateColWidth}"){$("#0auditDateCol").show();$("#1auditDateCol").hide();}else{$("#1auditDateCol").show();$("#0auditDateCol").hide();}
		if("${auditStatusColWidthC}"=="${auditStatusColWidth}"){$("#0auditStatusCol").show();$("#1auditStatusCol").hide();}else{$("#1auditStatusCol").show();$("#0auditStatusCol").hide();}
		*/
	}
});

//打开页面或者点击查询（即加载页面执行）
function schoolchangeinfoQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "${brSchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['learningStyle']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classes']}";
	var selectIdsJson = "{unitId:'stuchangeinfo_list_brSchoolName',gradeId:'stuchange_gradeid',classicId:'stuchange_classic',teachingType:'learningStyle',majorId:'stuchange_major',classesId:'stuchange_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function schoolrollQueryUnit() {
	var defaultValue = $("#stuchangeinfo_list_brSchoolName").val();
	var selectIdsJson = "{gradeId:'stuchange_gradeid',classicId:'stuchange_classic',teachingType:'learningStyle',majorId:'stuchange_major',classesId:'stuchange_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function schoolrollQueryGrade() {
	var defaultValue = $("#stuchangeinfo_list_brSchoolName").val();
	var gradeId = $("#stuchange_gradeid").val();
	var selectIdsJson = "{classicId:'stuchange_classic',teachingType:'learningStyle',majorId:'stuchange_major',classesId:'stuchange_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function schoolrollQueryClassic() {
	var defaultValue = $("#stuchangeinfo_list_brSchoolName").val();
	var gradeId = $("#stuchange_gradeid").val();
	var classicId = $("#stuchange_classic").val();
	var selectIdsJson = "{teachingType:'learningStyle',majorId:'stuchange_major',classesId:'stuchange_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

//选择学习形式
function schoolrollQueryTeachingType() {
	var defaultValue = $("#stuchangeinfo_list_brSchoolName").val();
	var gradeId = $("#stuchange_gradeid").val();
	var classicId = $("#stuchange_classic").val();
	var teachingTypeId = $("#learningStyle").val();
	var selectIdsJson = "{majorId:'stuchange_major',classesId:'stuchange_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}
//选择专业
function schoolrollQueryMajor() {
	var defaultValue = $("#stuchangeinfo_list_brSchoolName").val();
	var gradeId = $("#stuchange_gradeid").val();
	var classicId = $("#stuchange_classic").val();
	var teachingType = $("#stuchange_classic").val();
	var majorId = $("#stuchange_major").val();
	var selectIdsJson = "{classesId:'stuchange_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
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
	//新增（暂废弃）
	function addInfoChangeApply(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/edit.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_EDIT', url, '新增学籍异动');
	}
	//确认接收
	function acceptChange(){
		pageBarHandle("您确定要接收所选学生吗？","${baseUrl}/edu3/roll/stuchangeinfo/changebrschool/accept.html","#changeBody");
	}
	//审核
	function auditChange(){
		//禁用学生申请（走流程）
		//var url = "${baseUrl}/edu3/register/stuchangeinfo/edit.html";
		//if(isCheckOnlyone('resourceid','#changeBody')){
		//	navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_EDIT', url+'?resourceid='+$("#changeBody input[@name='resourceid']:checked").val(), '编辑学籍异动');
		//}	
		//pageBarHandle("您确定要审核这些记录吗？","${baseUrl}/edu3/framework/graduation/student/stuaudit.html?auditType=Y","#changeBody");
		if(!isChecked('resourceid',"#changeBody")){
 			alertMsg.warn('请至少选择一条要执行审核操作的记录。');
			return false;
 		}
		var num  	   = $("#changeBody input[name='resourceid']:checked").size();
		var studentIds = "" ;
		var k = 0;
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			studentIds+=$(this).val();
            if(k != num -1 ) studentIds += ",";
            k++;
        });
		
		$.pdialog.open('${baseUrl}/edu3/register/stuchangeinfo/audit.html?studentId='+studentIds, 'RES_SCHOOL_SCHOOLROLL_CHANGE_AUDIT', '设置审核状态', {width: 320, height: 240});
	}
	
	//撤销审核
	function disableAuditChange(){
		//TODO
	}
	
	//替学生异动（当前使用的申请学籍异动）
	function selfChangeApply(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/self/edit.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_EDIT', url, '申请学籍异动');
		//给本页加上重载的设定
		navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_CHANGE");
	}
		
	//删除
	function deleteChange(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/classic/delete.html","#changeBody");
	}
	
	function _viewStuChangeInfo(stuId,resourceid){//查看
		$.pdialog.open('${baseUrl}/edu3/framework/register/stuchangeinfo/view.html?studentId='+stuId+"&resourceid="+resourceid, 'RES_SCHOOL_SCHOOLROLL_CHANGE_VIEW', '查看学籍异动', {width: 1100, height: 600});
	}
	//导出学习异动统计条数Excel
	function exportStuChangeStatToExcel(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/excel/exportStatCondition.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_STAT', url, '导出学籍异动条数统计');
	}
	//导出学习异动Excel
	function exportStuChangeToExcel(){
		//以免每次点击下载都创建一个iFrame，把上次创建的删除
		$('#frame_exportStudentChange').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportStudentChange";
		var stuName       = document.getElementById('stuName').value;
		//var stuName  = $("#stuChangeInfo #stuName").val();
		var branchSchool  = $("#stuChangeInfo #stuchangeinfo_list_brSchoolName").val();
		//var major         = $("#stuChangeInfo #stuchange_major option:selected").val();
		var major         = $("#stuchange_major").val();
		var classic 	  = $("#stuChangeInfo #stuchange_classic option:selected").val();
		var stuStatus 	  = $("#stuChangeInfo #stuStatus option:selected").val();
		var stuChange 	  = $("#stuChangeInfo #stuChange option:selected").val();
		var gradeid 	  = $("#stuChangeInfo #stuchange_gradeid option:selected").val();
		var learningStyle = $("#stuChangeInfo #learningStyle option:selected").val();
		var applicationDateb = $("#stuChangeInfo #applicationDateb_id").val();
		var applicationDatee = $("#stuChangeInfo #applicationDatee_id").val(); 
		var auditDateb		 = $("#stuChangeInfo #auditDateb_id").val();
		var auditDatee		 = $("#stuChangeInfo #auditDatee_id").val();
		var finalAuditStatus		 = $("#stuChangeInfo #finalAuditStatus").val();
		
	/* 	var branchSchoolText  = $("#stuChangeInfo #stuchangeinfo_list_brSchoolName").text();
		var majorText         = $("#stuChangeInfo #stuchange_major option:selected").text();
		var majorText         = $("#stuChangeInfo #stuchange_major option:selected").text();
		var classicText 	  = $("#stuChangeInfo #stuchange_classic option:selected").text();
		var stuStatusText	  = $("#stuChangeInfo #stuStatus option:selected").text();
		var stuChangeText 	  = $("#stuChangeInfo #stuChange option:selected").text();
		var gradeidText 	  = $("#stuChangeInfo #stuchange_gradeid option:selected").text();
		var learningStyleText = $("#stuChangeInfo #learningStyle option:selected").text(); */
		
		var stuNum 		  = document.getElementById('stuNum').value;
		//var mobile 		  = document.getElementById('mobile').value;
		/* iframe.src = "${baseUrl }/edu3/register/stuchangeinfo/excel/export.html?act=default&stuName="
				+encodeURIComponent(encodeURIComponent(stuName))+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic
				+"&stuStatus="+stuStatus+"&stuChange="+stuChange+"&gradeid="+gradeid				
				+"&learningStyle="+learningStyle+"&stuNum="+stuNum+"&finalAuditStatus="+finalAuditStatus
				+"&applicationDateb="+applicationDateb+"&applicationDatee="+applicationDatee+"&auditDateb="+auditDateb+"&auditDatee="+auditDatee
				+"&branchSchoolText="+branchSchoolText
				+"&majorText="+majorText+"&classicText="+classicText+"&stuStatusText="+stuStatusText
				+"&stuChangeText="+stuChangeText+"&gradeidText="+gradeidText+"&learningStyleText="
				+learningStyleText; */
				 iframe.src = "${baseUrl }/edu3/register/stuchangeinfo/excel/export.html?act=default&stuName="
						+encodeURIComponent(encodeURIComponent(stuName))+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic
						+"&stuStatus="+stuStatus+"&stuChange="+stuChange+"&gradeid="+gradeid				
						+"&learningStyle="+learningStyle+"&stuNum="+stuNum+"&finalAuditStatus="+finalAuditStatus
						+"&applicationDateb="+applicationDateb+"&applicationDatee="+applicationDatee+"&auditDateb="+auditDateb+"&auditDatee="+auditDatee; 
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
		/*
		var url="${baseUrl}/edu3/teaching/teachingcourse/excel/coursebookchoice.html";
		$.pdialog.open(url,'RES_TEACHING_ESTAB_COURSEBOOK_TOEXCEL','',{height:100, width:150,mask:true});  
		*/
	}
	//撤销学籍异动（实际上就是删除未审核通过的学籍异动）
	function deleteStuInfoChange(){
		if(!isChecked('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要删除这些记录吗？", {
			okCall: function(){//执行	
				var res = "";
				var k = 0;
				var num  = $("#changeBody"+" input[name='resourceid']:checked").size();
				$("#changeBody"+" input[@name='resourceid']:checked").each(function(){
                        res+=$(this).val();
                        if(k != num -1 ) res += ",";
                        k++;
                });
				var url= "${baseUrl}/edu3/register/stuchangeinfo/delete.html?resourceid="+res;
				$.ajax({
					type:"post",
					url:url,
					dataType:"json",
					success:function(data){
						if(data['statusCode']==200){
							$.post("${baseUrl}/edu3/register/stuchangeinfo/delete.html",{resourceid:res}, navTabAjaxDone, "json");
							if(data['messageF']!='' ||data['messageS']!=''  ){
								alertMsg.warn(data['messageF']+data['messageS']);
							}
							
							var url = "${baseUrl}/edu3/register/stuchangeinfo/stuchange-list.html";
							//刷新页面
							navTab.openTab( 'RES_SCHOOL_SCHOOLROLL_CHANGE',url,'学籍异动审批与查询');
						}else if(data['statusCode']==300){
							alertMsg.warn(data['message']);
							return false;
						}
					}
				});
			}
		});	
	}
	//删除误退学的学生
	function deleteStuInfoChangeDropout(){
		if(!isChecked('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要删除这些记录吗？", {
			okCall: function(){//执行	
				var res = "";
				var k = 0;
				var num  = $("#changeBody"+" input[name='resourceid']:checked").size();
				$("#changeBody"+" input[@name='resourceid']:checked").each(function(){
                        res+=$(this).val();
                        if(k != num -1 ) res += ",";
                        k++;
                });
				var url= "${baseUrl}/edu3/register/stuchangeinfo/deleteDropout.html?resourceid="+res;
				$.ajax({
					type:"post",
					url:url,
					dataType:"json",
					success:function(data){
						if(data['statusCode']==200){
							$.post("${baseUrl}/edu3/register/stuchangeinfo/deleteDropout.html",{resourceid:res}, navTabAjaxDone, "json");
							if(data['messageF']!='' ||data['messageS']!=''  ){
								alertMsg.warn(data['messageF']+data['messageS']);
							}
							
							var url = "${baseUrl}/edu3/register/stuchangeinfo/stuchange-list.html";
							//刷新页面
							navTab.openTab( 'RES_SCHOOL_SCHOOLROLL_CHANGE',url,'学籍异动审批与查询');
						}else if(data['statusCode']==300){
							alertMsg.warn(data['message']);
							return false;
						}
					}
				});
			}
		});	
	}
	
	function changeIntoSchool(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/changeIntoSchool.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_INTOSCHOOL', url, '申请校级转入');
		//给本页加上重载的设定
		navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_CHANGE");
	}
	/* function brschool_Major_Classes(){
		var unitId = $("#stuchangeinfo_list_brSchoolName").val();
		var majorId = $("#stuChangeInfo #stuchange_major").val();
		var classId = $("#stuChangeInfo #stuchange_classesid").val();
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
			    	$("#stuChangeInfo #stuchange_major").replaceWith("<select  id=\"stuchange_major\" name=\"major\" style=width:55%  onchange=\"brschool_Major_Classes()\">"+data['majorOption']+"</select>");
			    	$("#stuChangeInfo #stuchange_classesid").replaceWith("<select  id=\"stuchange_classesid\" name=\"stuchange_classesid\" style=width:55% onchange=\"brschool_Major_Classes()\" >"+data['classesOption']+"</select>");
				}
			}
		});
		
	} */
	
	function changeWidth_Expand(obj){
		var flag = $("#widthFlag").val();
		if(""==flag){
			$("#idColWidth").val($(".idCol").width());
			$("#stuNumColWidth").val($("#stuNumCol").width());
			$("#nameColWidth").val($(".nameCol").width());
			$("#gradeColWidth").val($(".gradeCol").width());
			$("#mobileColWidth").val($("#mobileCol").width());
			$("#changeTypeColWidth").val($(".changeTypeCol").width());
			$("#unitColWidth").val($(".unitCol").width());
			$("#majorColWidth").val($(".majorCol").width());
			$("#classesColWidth").val($(".classesCol").width());
			$("#classicColWidth").val($(".classicCol").width());
			$("#teachTypeColWidth").val($(".teachTypeCol").width());
			$("#ounitColWidth").val($(".ounitCol").width());
			$("#omajorColWidth").val($(".omajorCol").width());
			$("#wmajorColWidth").val($(".wmajorCol").width());
			$("#oclassesColWidth").val($(".oclassesCol").width());
			$("#oclassicColWidth").val($(".oclassicCol").width());
			$("#oteachTypeColWidth").val($(".oteachTypeCol").width());
			$("#applyDateColWidth").val($(".applyDateCol").width());
			$("#auditDateColWidth").val($(".auditDateCol").width());
			$("#auditStatusColWidth").val($(".auditStatusCol").width());
			$("#widthFlag").val("Y");
		}
		
		var columnName =$(obj).attr("id").replace("0","").replace("1","");
		var w = parseInt($("#"+columnName+"Width").val())+parseInt(40);
		if("stuNumCol"==columnName) w = 120;
		if("unitCol"==columnName) w=160;
		if("majorCol"==columnName) w=132;
		if("classesCol"==columnName) w=248;
		if("ounitCol"==columnName) w=160;
		if("omajorCol"==columnName) w=132;
		if("wmajorCol"==columnName) w=132;
		if("oclassesCol"==columnName) w=248;
		if("applyDateCol"==columnName) w=114;
		if("auditDateCol"==columnName) w=114;
		if("auditStatusCol"==columnName) w=97;
		
		$("."+columnName).width(w);
		
		$("#0"+columnName).hide();
		$("#1"+columnName).show();
		$("#idColWidthC").val($("#idCol").width());
		$("#stuNumColWidthC").val($("#stuNumCol").width());
		$("#mobileColWidthC").val($("#mobileCol").width());
		$("#unitColWidthC").val($("#unitCol").width());
		$("#majorColWidthC").val($("#majorCol").width());
		$("#classesColWidthC").val($("#classesCol").width());
		$("#ounitColWidthC").val($("#ounitCol").width());
		$("#wunitColWidthC").val($("#wunitCol").width());
		$("#omajorColWidthC").val($("#omajorCol").width());
		$("#wmajorColWidthC").val($("#wmajorCol").width());
		$("#oclassesColWidthC").val($("#oclassesCol").width());
		$("#applyDateColWidthC").val($("#applyDateCol").width());
		$("#auditDateColWidthC").val($("#auditDateCol").width());
		$("#auditStatusColWidthC").val($("#auditStatusCol").width());
	}
	function changeWidth_rec(obj){
		var columnName =$(obj).attr("id").replace("0","").replace("1","");
		$("."+columnName).width($("#"+columnName+"Width").val());
		$("#1"+columnName).hide();
		$("#0"+columnName).show();
		$("#idColWidthC").val($("#idCol").width());
		$("#stuNumColWidthC").val($("#stuNumCol").width());
		$("#mobileColWidthC").val($("#mobileCol").width());
		$("#unitColWidthC").val($("#unitCol").width());
		$("#majorColWidthC").val($("#majorCol").width());
		$("#classesColWidthC").val($("#classesCol").width());
		$("#ounitColWidthC").val($("#ounitCol").width());
		$("#wunitColWidthC").val($("#wunitCol").width());
		$("#omajorColWidthC").val($("#omajorCol").width());
		$("#wmajorColWidthC").val($("#wmajorCol").width());
		$("#oclassesColWidthC").val($("#oclassesCol").width());
		$("#applyDateColWidthC").val($("#applyDateCol").width());
		$("#auditDateColWidthC").val($("#auditDateCol").width());
		$("#auditStatusColWidthC").val($("#auditStatusCol").width());
		
	}
	
//	function printStuChangeInfo(){
//		var stuid ="";
//		$( "input[name='resourceid']" ).each(function() {
//			 var stu = $( this ).val();
//			 stuid += stu+",";
//			});
		
//		var url  = "${baseUrl}/edu3/register/stuchangeinfo/excel/printStuChangeInfo.html?stuid="+stuid;
//		$.pdialog.open(url,'RES_SCHOOL_SCHOOLROLL_CHANGE_EXEXCEL_PRINT','打印预览',{height:500, width:1200});
//	}
	//打印异动信息 
	function printStuChangeInfo(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/excel/printStuChangeInfo.html?"+$("#stuchangeinfo_search_form").serialize();
            $.pdialog.open(url,'RES_SCHOOL_SCHOOLROLL_CHANGE_EXEXCEL_PRINT','打印预览',{height:500, width:1200});   
	}
	
	// 打印学籍异动审批表
	function printApprovalForm(){
		if(!isChecked('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		
		var stuChangeIds = new Array();
		var isAllNoAudit = true;
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			stuChangeIds.push($(this).val());
			var audit = $(this).attr("auditStatus");
			// 只要有一个不是未审核的都不能打印
			if(audit != "W"){
				isAllNoAudit = false;
			}
        });
		
		if(!isAllNoAudit){
			alertMsg.warn('只能打印未审核的记录！');
			return false;
		}
		
		var alt_msg = "确定要打印所选学生的异动信息吗？";
		var url = "${baseUrl}/edu3/roll/stuchangeinfo/approvalForm-printview.html?stuChangeIds="+stuChangeIds.toString();
		alertMsg.confirm(alt_msg, {
           okCall: function(){
           	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_CHANGE_PRINTAPPROVALFORM","打印预览",{width:800, height:600});	
           }
		});
	}
	
	//打印退费银行信息
	function printRefundInformation(){
		if(!isChecked('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		
		var stuChangeIds = new Array();
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			var changeType = $(this).attr("changeType");
			if(changeType == "13"){
				stuChangeIds.push($(this).val());
			}
        });
		var alt_msg = "确定要打印所选学生的异动信息吗？";
		var url = "${baseUrl}/edu3/roll/stuchangeinfo/approvalForm-printview.html?isRefundInformation=Y&stuChangeIds="+stuChangeIds.toString();
		alertMsg.confirm(alt_msg, {
           okCall: function(){
           	$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_CHANGE_REFUNDINFORMATION_PRINT","打印预览",{width:800, height:600});	
           }
		});
	}
	
	// 查看复学成绩
	function checkMarkDelete(){
		if(!isCheckOnlyone('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 	}
		
		var id = "";
		var isNoAudit = true;
		var teachingPlan = "";
		var brSchool = "";
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			id=$(this).attr("studentId");
			var audit = $(this).attr("auditStatus");
			var changeType = $(this).attr("changeType");
			teachGuidePlan = $(this).attr("changeTeachGuidePlan");
			brSchool = $(this).attr("brSchool");
			//console.log(teachingPlan);
			
			// 未审核的复学异动才可以查看
			if(audit != "W" || changeType!="12"){
				isNoAudit = false;
				//console.log(audit);
				//console.log(changeType);
				//console.log(isNoAudit);
			}
        });
		
		if(!isNoAudit){
			alertMsg.warn('只能查看未审核的复学记录！');
			return false;
		}
		
		$.pdialog.open('${baseUrl}/edu3/register/stuchangeinfo/self/viewExamResult.html?studentId='+id+'&seeOnly=true'+'&teachGuidePlan='+teachGuidePlan+'&brSchool='+brSchool, 'RES_SCHOOL_SCHOOLROLL_CHANGE_VIEW', '查看复学成绩详情', {width: 1100, height: 600});
	
	}
	
	//网上申请
	function fillInApplication(){
		if(!isCheckOnlyone('resourceid',"#changeBody")){
 			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 	}
		var studentId = "";
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			studentId=$(this).attr("studentId");
		});
		var url = "${baseUrl}/edu3/roll/stuchangeinfo/stuChangeOnlineInfo-form.html?studentId="+studentId;
		$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_DROPOUTAPPLICATION', '网上学籍异动申请', {width: 600, height: 450});
	}
	
	// 退补教材费
	function refundSuppleTextbookFee() {
		var resouceId = new Array();
		var auditStatus = "";
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			resouceId.push($(this).val());
			auditStatus = $(this).attr("auditStatus");
		});
		if(resouceId.length<1){
 			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 	}
		if(resouceId.length>1){
 			alertMsg.warn('只能选择一条要操作的记录！');
			return false;
	 	}
		// 判断是否已审核通过
		if(auditStatus!="Y") {
			alertMsg.warn('只能处理审批通过的记录！');
			return false;
		}
		var url = "${baseUrl}/edu3/roll/stuchangeinfo/refundSupple-form.html?resouceId="+resouceId;
		$.pdialog.open(url, 'RES_SCHOOL_SCHOOLROLL_REFUNDSUPPLEFEE', '退费补交教材费', {width: 600, height: 450});
	}
	
	// 处理退补学费
	function handleTuition() {
		var resouceId = new Array();
		var auditStatus = "";
		$("#changeBody input[@name='resourceid']:checked").each(function(){
			resouceId.push($(this).val());
			auditStatus = $(this).attr("auditStatus");
		});
		if(resouceId.length<1){
 			alertMsg.warn('请选择一条要操作的记录！');
			return false;
	 	}
		if(resouceId.length>1){
 			alertMsg.warn('只能选择一条要操作的记录！');
			return false;
	 	}
		// 判断是否已审核通过
		if(auditStatus!="Y") {
			alertMsg.warn('只能处理审批通过的记录！');
			return false;
		}
		var url= "${baseUrl}/edu3/roll/stuchangeinfo/handleTuition.html?resouceId="+resouceId;
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['statusCode']==200){
					alertMsg.correct(data['message']);
				} else {
					alertMsg.error(data['message']);
				}
			}
		});
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="stuchangeinfo_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/stuchangeinfo/stuchange-list.html"
				method="post">
				<input type='hidden' id="widthFlag" name="widthFlag" value="" /> <input
					type='hidden' id="idColWidth" name="idColWidth" value="" /> <input
					type='hidden' id="stuNumColWidth" name="stuNumColWidth" value="" />
				<input type='hidden' id="nameColWidth" name="nameColWidth" value="" />
				<input type='hidden' id="gradeColWidth" name="gradeColWidth"
					value="" /> <input type='hidden' id="mobileColWidth"
					name="mobileColWidth" value="" /> <input type='hidden'
					id="changeTypeColWidth" name="changeTypeColWidth" value="" /> <input
					type='hidden' id="unitColWidth" name="unitColWidth" value="" /> <input
					type='hidden' id="majorColWidth" name="majorColWidth" value="" />
				<input type='hidden' id="classesColWidth" name="classesColWidth"
					value="" /> <input type='hidden' id="classicColWidth"
					name="classicColWidth" value="" /> <input type='hidden'
					id="teachTypeColWidth" name="teachTypeColWidth" value="" /> <input
					type='hidden' id="ounitColWidth" name="ounitColWidth" value="" />
				<input type='hidden' id="omajorColWidth" name="omajorColWidth"
					value="" /> <input type='hidden' id="wmajorColWidth"
					name="wmajorColWidth" value="" /> <input type='hidden'
					id="oclassesColWidth" name="oclassesColWidth" value="" /> <input
					type='hidden' id="oclassicColWidth" name="oclassicColWidth"
					value="" /> <input type='hidden' id="oteachTypeColWidth"
					name="oteachTypeColWidth" value="" /> <input type='hidden'
					id="applyDateColWidth" name="applyDateColWidth" value="" /> <input
					type='hidden' id="auditDateColWidth" name="auditDateColWidth"
					value="" /> <input type='hidden' id="auditStatusColWidth"
					name="auditStatusColWidth" value="" /> <input type='hidden'
					id="idColWidthC" name="idColWidthC" value="" /> <input
					type='hidden' id="stuNumColWidthC" name="stuNumColWidthC" value="" />
				<input type='hidden' id="mobileColWidthC" name="mobileColWidthC"
					value="" /> <input type='hidden' id="nameColWidthC"
					name="nameColWidthC" value="" /> <input type='hidden'
					id="gradeColWidthC" name="gradeColWidthC" value="" /> <input
					type='hidden' id="changeTypeColWidthC" name="changeTypeColWidthC"
					value="" /> <input type='hidden' id="unitColWidthC"
					name="unitColWidthC" value="" /> <input type='hidden'
					id="majorColWidthC" name="majorColWidthC" value="" /> <input
					type='hidden' id="classesColWidthC" name="classesColWidthC"
					value="" /> <input type='hidden' id="classicColWidthC"
					name="classicColWidthC" value="" /> <input type='hidden'
					id="teachTypeColWidthC" name="teachTypeColWidthC" value="" /> <input
					type='hidden' id="ounitColWidthC" name="ounitColWidthC" value="" />
				<input type='hidden' id="wunitColWidthC" name="wunitColWidthC"
					value="" /> <input type='hidden' id="omajorColWidthC"
					name="omajorColWidthC" value="" /> <input type='hidden'
					id="wmajorColWidthC" name="wmajorColWidthC" value="" /> <input
					type='hidden' id="oclassesColWidthC" name="oclassesColWidthC"
					value="" /> <input type='hidden' id="oclassicColWidthC"
					name="oclassicColWidthC" value="" /> <input type='hidden'
					id="oteachTypeColWidthC" name="oteachTypeColWidthC" value="" /> <input
					type='hidden' id="applyDateColWidthC" name="applyDateColWidthC"
					value="" /> <input type='hidden' id="auditDateColWidthC"
					name="auditDateColWidthC" value="" /> <input type='hidden'
					id="auditStatusColWidthC" name="auditStatusColWidthC" value="" />

				<div id="stuChangeInfo" class="searchBar">
				
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <%--<select  class="flexselect" id="stuchangeinfo_list_brSchoolName" name="branchSchool" tabindex=1 style=width:53% onchange="brschool_Major_Classes()" >${unitOption}</select> --%>
							<span sel-id="stuchangeinfo_list_brSchoolName"
							sel-name="branchSchool" sel-onchange="schoolrollQueryUnit()" 
							sel-classs="flexselect" ></span> <%-- <c:choose>
						<c:when test="${condition['isBranchSchool'] ne 'Y' }">
						<span sel-id="stuchangeinfo_list_brSchoolName" sel-name="branchSchool" sel-onchange="schoolrollQueryUnit()" sel-classs="flexselect"  sel-style="width: 120px" ></span>
					
						</c:when>
						<c:otherwise>
							<input type="hidden"  id="stuchangeinfo_list_brSchoolName"  name="branchSchool" value="${condition['branchSchool'] }"/>
							<input type="text" readonly="readonly" value="${brSchoolName }"/>
						</c:otherwise>
					</c:choose> --%> <%/*<gh:brSchoolAutocomplete  name="branchSchool" tabindex="1" id="stuchangeinfo_list_brSchoolName" defaultValue="${condition['branchSchool']}" displayType="code" style="width:53%"/> */ %>
						</li>
						<li><label>年级：</label> <span sel-id="stuchange_gradeid"
							sel-name="gradeid" sel-onchange="schoolrollQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="stuchange_classic"
							sel-name="classic" sel-onchange="schoolrollQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习方式：</label> <!-- <span sel-id="learningStyle" sel-name="learningStyle" sel-onchange="schoolrollQueryTeachingType()" dictionaryCode="CodeLearningStyle" sel-style="width: 120px"></span> -->
							<gh:select id="learningStyle" name="learningStyle"
								dictionaryCode="CodeLearningStyle"
								value="${condition['learningStyle']}" style="width:120px" /></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="stuchange_major"
							sel-name="major" sel-onchange="schoolrollQueryMajor()"
							sel-classs="flexselect" ></span></li>
						<%--<li>
					<label>班级：</label>
					<gh:selectModel id="stuchangeinfo_list_class" name="stuchangeinfo_list_class" bindValue="resourceid" displayValue="classname" value="${condition['classes']}" modelClass="com.hnjk.edu.roll.model.Classes" style="width:55%"  onchange="brschool_Major_Classes()"  />
				</li> --%>
						<li><label>姓名：</label><input id="stuName" name="stuName"
							value="${condition['stuName']}" style="width: 120px" /></li>
						<li><label>学号：</label> <input id="stuNum" name="stuNum"
							value="${condition['stuNum']}" style="width: 120px"></li>
						<li><label>学籍状态：</label> <select name="stuStatus"
							id="stuStatus" style="width: 120px">
						</select> <!--<gh:select id="stuStatus" name="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:120px" />-->
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="stuchange_classesid"
							sel-name="classes" sel-classs="flexselect"></span></li>
						<li><label>变动类别：</label> <gh:select id="stuChange"
								name="stuChange" value="${condition['stuChange']}"
								filtrationStr="12,23,81,82,11,13,C_23,C_24,slowGraduation,18"
								dictionaryCode="CodeStudentStatusChange" style="width:120px" />
						</li>
						<li><label>变动属性：</label> <select id="changeProperty"
							name="changeProperty" style="width: 120px">
								<option value="">请选择</option>
								<option
									<c:if test="${condition['changeProperty'] eq '1'}">selected ="selected"</c:if>
									value="1">学院</option>
								<option
									<c:if test="${condition['changeProperty'] eq '2'}">selected ="selected"</c:if>
									value="2">专业</option>
								<option
									<c:if test="${condition['changeProperty'] eq '3'}">selected ="selected"</c:if>
									value="3">学习形式</option>
						</select></li>
						<li><label>审核结果：</label> <select id="finalAuditStatus"
							name="finalAuditStatus" style="width: 120px">
								<option value="">请选择</option>
								<option value="W"
									<c:if test="${condition['finalAuditStatus'] eq 'W'}"> selected="selected"</c:if>>未审核</option>
								<option value="Y"
									<c:if test="${condition['finalAuditStatus'] eq 'Y'}"> selected="selected"</c:if>>审核通过</option>
								<option value="N"
									<c:if test="${condition['finalAuditStatus'] eq 'N'}"> selected="selected"</c:if>>审核不通过</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>申请时间：</label> <input type="text" style="width: 80px;"
							id="applicationDateb_id" name="applicationDateb" class="Wdate"
							value="${condition['applicationDateb']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 至<input
							type="text" style="width: 80px;" id="applicationDatee_id"
							name="applicationDatee" class="Wdate"
							value="${condition['applicationDatee']}"
							onfocus="WdatePicker({isShowWeek:true })" />
						</li>
						<li class="custom-li"><label>审核时间：</label><input type="text" style="width: 80px;"
							id="auditDateb_id" name="auditDateb" class="Wdate"
							value="${condition['auditDateb']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 至<input
							type="text" style="width: 80px;" id="auditDatee_id"
							name="auditDatee" class="Wdate"
							value="${condition['auditDatee']}"
							onfocus="WdatePicker({isShowWeek:true })" />
						</li>
						<li><span id="testContent"></span></li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent" id="studentChangeInfoList" >
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_CHANGE" pageType="list"></gh:resAuth>
			<table class="table" targetType="dialog" width="100%" layouth='190'>
				<thead>
					<tr>
						<!--  
					<th width="5%"><input type="checkbox" name="checkall" id="check_all_change" onclick="checkboxAll('#check_all_change','resourceid','#changeBody')"/></th>
		             <th width="10%">学号</th>
		        	<th width="9%">姓名</th>
		            <th width="10%">教学站</th>
		            <th width="9%">年级</th>
		            <th width="9%">层次</th>
		            <th width="9%">专业</th>		           
		            <th width="6%">民族</th>
		            <th width="10%">身份证号</th>
		            <th width="10%">申请时间</th>
		            <th width="8%">异动类型</th>
		            <th width="8%">申请结果</th>
				-->
						<c:choose>
							<c:when test="${empty widthFlag}">
								<th class="idCol" id="idCol" width="3%"><input
									type="checkbox" name="checkall" id="check_all_change"
									onclick="checkboxAll('#check_all_change','resourceid','#changeBody')" /></th>
								<th class="stuNumCol" id="stuNumCol" width="5%">学号<a
									style="display: none;" href="#" id="0stuNumCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1stuNumCol"
									onclick="changeWidth_rec(this)"><b><b>-</b></a></th>
								<th class="nameCol" id="nameCol" width="4%">姓名</th>
								<th class="gradeCol" id="gradeCol" width="4%">年级</th>
								<th class="mobileCol" id="mobileCol" width="4%">联系电话<a
									style="display: none;" href="#" id="0mobileCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1mobileCol"
									onclick="changeWidth_rec(this)"><b><b>-</b></a></th>
								<th class="changeTypeCol" id="changeTypeCol" width="5%">异动类型</th>
								<!--  信息多余不需要
		            <th class="unitCol" id="unitCol" width="5%">现学院<a style="display: none;" href="#" id="0unitCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1unitCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		           	<th class="majorCol" id="majorCol" width="5%">现专业<a style="display: none;" href="#" id="0majorCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1majorCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		            <th class="classesCol" id="classesCol" width="6%">现班级<a style="display: none;" href="#" id="0classesCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1classesCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		            <th class="classicCol" id="classicCol" width="4%">现层次</th>
		            <th class="teachTypeCol" id="teachTypeCol" width="4%">现形式</th>
		            -->
								<th class="ounitCol" id="ounitCol" width="5%">原学院<a
									style="display: none;" href="#" id="0ounitCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1ounitCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wunitCol" id="wunitCol" width="5%">拟转入学院<a
									style="display: none;" href="#" id="0wunitCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1wunitCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="omajorCol" id="omajorCol" width="5%">原专业<a
									style="display: none;" href="#" id="0omajorCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1omajorCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wmajorCol" id="wmajorCol" width="6%">拟转入专业<a
									style="display: none;" href="#" id="0wmajorCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1wmajorCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="oclassesCol" id="oclassesCol" width="6%">原班级<a
									style="display: none;" href="#" id="0oclassesCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1oclassesCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wclassesCol" id="wclassesCol" width="6%">拟转入班级<a
									style="display: none;" href="#" id="0wclassesCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1oclassesCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="oclassicCol" id="oclassicCol" width="4%">原层次</th>
								<th class="wclassicCol" id="wclassicCol" width="4%">拟转入层次</th>
								<th class="oteachTypeCol" id="oteachTypeCol" width="4%">原形式</th>
								<th class="wteachTypeCol" id="wteachTypeCol" width="4%">拟转入形式</th>
								<th class="applyDateCol" id="applyDateCol" width="5%">申请时间<a
									style="display: none;" href="#" id="0applyDateCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1applyDateCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="auditDateCol" id="auditDateCol" width="5%">审核时间<a
									style="display: none;" href="#" id="0auditDateCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1auditDateCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="auditStatusCol" id="auditStatusCol" width="5%"><c:if test="${schoolCode eq '12962' }">查看附件/</c:if>申请结果<a
									style="display: none;" href="#" id="0auditStatusCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1auditStatusCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
							</c:when>
							<c:otherwise>
								<th class="idCol" id="idCol" width="${idColWidthC}px"><input
									type="checkbox" name="checkall" id="check_all_change"
									onclick="checkboxAll('#check_all_change','resourceid','#changeBody')" /></th>
								<th class="stuNumCol" id="stuNumCol"
									width="${stuNumColWidthC}px">学号<a style="display: none;"
									href="#" id="0stuNumCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1stuNumCol"
									onclick="changeWidth_rec(this)"><b><b>-</b></a></th>
								<th class="nameCol" id="nameCol" width="${nameColWidth}px6%">姓名</th>
								<th class="gradeCol" id="gradeCol" width="${gradeColWidth}px">年级</th>
								<th class="mobileCol" id="mobileCol" width="${mobileColWidth}px">联系电话<a
									style="display: none;" href="#" id="0mobileCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1mobileCol"
									onclick="changeWidth_rec(this)"><b><b>-</b></a></th>
								<th class="changeTypeCol" id="changeTypeCol"
									width="${changeTypeColWidth}px">异动类型</th>
								<!--  
		            <th class="unitCol" id="unitCol" width="${unitColWidthC}px">现学院<a style="display: none;" href="#" id="0unitCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1unitCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		           	<th class="majorCol" id="majorCol" width="${majorColWidthC}px">现专业<a style="display: none;" href="#" id="0majorCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1majorCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		            <th class="classesCol" id="classesCol" width="${classesColWidthC}px">现班级<a style="display: none;" href="#" id="0classesCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a style="display: none;" href="#" id="1classesCol" onclick="changeWidth_rec(this)"><b>-</b></a></th>
		            <th class="classicCol" id="classicCol" width="${classicColWidth}px">现层次</th>
		            <th class="teachTypeCol" id="teachTypeCol" width="${teachTypeColWidth}px">现形式</th>
		            -->
								<th class="ounitCol" id="ounitCol" width="${ounitColWidthC}px">原学院<a
									style="display: none;" href="#" id="0ounitCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1ounitCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wunitCol" id="wunitCol" width="${wunitColWidthC}px">拟转入学院<a
									style="display: none;" href="#" id="0wunitCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1wunitCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="omajorCol" id="omajorCol"
									width="${omajorColWidthC}px">原专业<a style="display: none;"
									href="#" id="0omajorCol" onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1omajorCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wmajorCol" id="wmajorCol"
									width="${wmajorColWidthC}px">拟转入专业<a
									style="display: none;" href="#" id="0wmajorCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1wmajorCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="oclassesCol" id="oclassesCol"
									width="${oclassesColWidthC}px">原班级<a
									style="display: none;" href="#" id="0oclassesCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1oclassesCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="wclassesCol" id="wclassesCol"
									width="${wclassesColWidthC}px">拟转入班级<a
									style="display: none;" href="#" id="0wclassesCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1oclassesCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="oclassicCol" id="oclassicCol"
									width="${oclassicColWidth}px">原层次</th>
								<th class="wclassicCol" id="wclassicCol"
									width="${wclassicColWidth}px">拟转入层次</th>
								<th class="oteachTypeCol" id="oteachTypeCol"
									width="${oteachTypeColWidth}px">原形式</th>
								<th class="wteachTypeCol" id="wteachTypeCol"
									width="${wteachTypeColWidth}px">拟转入形式</th>
								<th class="applyDateCol" id="applyDateCol"
									width="${applyDateColWidthC}px">申请时间<a
									style="display: none;" href="#" id="0applyDateCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1applyDateCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="auditDateCol" id="auditDateCol"
									width="${auditDateColWidthC}px">审核时间<a
									style="display: none;" href="#" id="0auditDateCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1auditDateCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
								<th class="auditStatusCol" id="auditStatusCol"
									width="${auditStatusColWidthC}px"><c:if test="${schoolCode eq '12962' }">查看附件</c:if>申请结果<a
									style="display: none;" href="#" id="0auditStatusCol"
									onclick="changeWidth_Expand(this)"><b>+</b></a><a
									style="display: none;" href="#" id="1auditStatusCol"
									onclick="changeWidth_rec(this)"><b>-</b></a></th>
							</c:otherwise>
							
						</c:choose>
						<!-- <th class="nationCol" width="5%">民族</th>-->
						<!-- <th class="certNumCol" width="11%">身份证号</th>-->
					</tr>
				</thead>
				<tbody id="changeBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td class="idCol">
								<input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" auditStatus="${stu.finalAuditStatus }" changeType="${ stu.changeType}"
								studentId="${stu.studentInfo.resourceid}" changeTeachGuidePlan="${stu.changeTeachingGuidePlan.resourceid}"
								brSchool="${stu.changeBrschool.resourceid}" />
							</td>
							<td class="stuNumCol" title="${stu.studentInfo.studyNo }">${stu.studentInfo.studyNo }</td>
							<td class="nameCol"><a href="###"
								onclick="_viewStuChangeInfo('${stu.studentInfo.resourceid}','${stu.resourceid}')"
								title="点击查看">${stu.studentInfo.studentName }</a></td>
							<td class="gradeCol" title="${stu.studentInfo.grade }">${stu.studentInfo.grade }</td>
							<td class="mobileCol"
								title="${stu.studentInfo.studentBaseInfo.mobile }">${stu.studentInfo.studentBaseInfo.mobile }</td>
							<td class="changeTypeCol"
								title="${ghfn:dictCode2Val('CodeStudentStatusChange',stu.changeType) }">${ghfn:dictCode2Val('CodeStudentStatusChange',stu.changeType) }</td>
							<!--  
			        	<td class="unitCol" title="${stu.studentInfo.branchSchool.unitShortName }" <c:if test="${condition['changeProperty'] eq '1'}">style="background-color: #EFF4F5"</c:if>  >${stu.studentInfo.branchSchool.unitShortName }</td>
			        	<td class="majorCol" title="${stu.studentInfo.major }" <c:if test="${condition['changeProperty'] eq '2'}">style="background-color: #EFF4F5"</c:if> >${stu.studentInfo.major }</td>
			        	<td class="classesCol" title="${stu.studentInfo.classes.classname }">${stu.studentInfo.classes.classname }</td>
			        	<td class="classicCol" title="${stu.studentInfo.classic.shortName }">${stu.studentInfo.classic.shortName }</td>
			        	<td class="teachTypeCol" title="${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingType) }" <c:if test="${condition['changeProperty'] eq '3'}">style="background-color: #EFF4F5"</c:if>  >${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingType) }</td> 
			        	-->
							<td class="ounitCol"
								title="${not empty stu.changeBeforeBrSchool? stu.changeBeforeBrSchool.unitShortName :"
								-"}" <c:if test="${condition['changeProperty'] eq '1'}">style="background-color: #EFF4F5"</c:if>>${not empty stu.changeBeforeBrSchool? stu.changeBeforeBrSchool.unitShortName :"-"}</td>
							<td class="wunitCol"
								title="${not empty stu.changeBrschool? stu.changeBrschool.unitShortName :"
								-"}" <c:if test="${condition['changeProperty'] eq '1'}">style="background-color: #EFF4F5"</c:if>>
									${(stu.changeType ne '11' and stu.changeType ne '13' and stu.changeType ne '18')? stu.changeBrschool.unitShortName :"—"}</td>
							<td class="omajorCol"
								<c:if test="${condition['changeProperty'] eq '2'}">style="background-color: #EFF4F5"</c:if>>
								<c:if test="${stu.changeType ne '13' and stu.changeType ne '18'}">${not empty stu.changeBeforeMajorName?stu.changeBeforeMajorName:stu.changeBeforeTeachingGuidePlan.teachingPlan.major.majorName}</c:if>
								<c:if test="${stu.changeType eq '13' or stu.changeType eq '18'}">${stu.studentInfo.major.majorName}</c:if>
							</td>
							<td class="wmajorCol"
								title="${not empty stu.changeMajor.majorName?stu.changeMajor.majorName:stu.changeTeachingGuidePlan.teachingPlan.major.majorName}"
								<c:if test="${condition['changeProperty'] eq '2'}">style="background-color: #EFF4F5"</c:if>>
									${(stu.changeType ne '11' and stu.changeType ne '13' and stu.changeType ne '18')?stu.changeMajor.majorName:"—"}</td>
							<td class="oclassesCol" title="${stu.changeBeforeClass}">${stu.changeBeforeClass}</td>
							<td class="wclassesCol" title="${stu.changeClass}">
								${(stu.changeType ne '11' and stu.changeType ne '13' and stu.changeType ne '18')?stu.changeClass:"—"}</td>
							
							<td class="oclassicCol">
								<c:if test="${stu.changeType ne '13' and stu.changeType ne '18'}">${not empty stu.changeBeforeClassicName?stu.changeBeforeClassicName:stu.changeBeforeTeachingGuidePlan.teachingPlan.classic.shortName}</c:if>
								<c:if test="${stu.changeType eq '13' or stu.changeType eq '18'}">${stu.studentInfo.classic.shortName}</c:if>
							</td>
							<td class="wclassicCol">
									${(stu.changeType ne '11' and stu.changeType ne '13' and stu.changeType ne '18' and (not empty stu.changeClassicId and stu.changeClassicId ne stu.changeTeachingGuidePlan.teachingPlan.classic))?stu.changeClassicId:"—"}</td>
							<td class="oteachTypeCol"
								<c:if test="${condition['changeProperty'] eq '3'}">style="background-color: #EFF4F5"</c:if>>
								${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeLearingStyle)}</td>
							<td class="wteachTypeCol"
								<c:if test="${condition['changeProperty'] eq '3'}">style="background-color: #EFF4F5"</c:if>>
								${(stu.changeType ne '11' and stu.changeType ne '13' and stu.changeType ne '18' and (not empty stu.changeTeachingType and stu.changeTeachingType ne stu.changeBeforeLearingStyle))?ghfn:dictCode2Val('CodeTeachingType',stu.changeTeachingType):"—"}</td>
							<td class="applyDateCol"
								title="<fmt:formatDate value="${stu.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate
									value="${stu.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td class="auditDateCol"
								title="<fmt:formatDate value="${stu.auditDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate
									value="${stu.auditDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>


							<td class="auditStatusCol"
								title="${stu.finalAuditStatus == 'Y' ?'通过':(stu.finalAuditStatus == 'N' ?'未通过':'待审核')}${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'Y' and stu.finalAuditStatus ne 'Y' ?'(同意接收)':''}${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'W' and stu.finalAuditStatus ne 'Y' ?'(等待接收)':''}"
								<c:if test="${stu.finalAuditStatus == 'Y'}">style='color:blue'</c:if>
								<c:if test="${stu.finalAuditStatus == 'N'}">style='color:red'</c:if>
								>
								<c:choose>
								<c:when test="${schoolCode eq '12962' }">
								<a href="${baseUrl }/edu3/roll/stuchangeinfo/displayAttachs.html?resourceid=${stu.resourceid }" target="dialog" width="400" height="300" title="查看及下载附件">
								${stu.finalAuditStatus == 'Y' ?"通过":(stu.finalAuditStatus == 'N' ?"未通过":"待审核")}
								<c:choose>
									<c:when
										test="${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'Y' and stu.finalAuditStatus ne 'Y' }">(同意接收)</c:when>
									<c:when
										test="${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'W' and stu.finalAuditStatus ne 'Y' }">(等待接收)</c:when>
								</c:choose>
								</a>
								</c:when>
								<c:otherwise>
								${stu.finalAuditStatus == 'Y' ?"通过":(stu.finalAuditStatus == 'N' ?"未通过":"待审核")}
								<c:choose>
									<c:when
										test="${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'Y' and stu.finalAuditStatus ne 'Y' }">(同意接收)</c:when>
									<c:when
										test="${stu.changeType eq '82' and stu.changeBrschoolAccept eq 'W' and stu.finalAuditStatus ne 'Y' }">(等待接收)</c:when>
								</c:choose>
								</c:otherwise>
								</c:choose>
								
							</td>
							<!-- <td class="nationCol" >${ghfn:dictCode2Val('CodeNation',stu.studentInfo.studentBaseInfo.nation) }</td>-->
							<!-- <td class="certNumCol" >${stu.studentInfo.studentBaseInfo.certNum }</td>-->
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<div class="panelBar" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/stuchangeinfo/stuchange-list.html"
				localArea="studentChangeInfoListArea" pageType="sys"
				condition="${condition}" /></div>
	</div>
</body>
</html>