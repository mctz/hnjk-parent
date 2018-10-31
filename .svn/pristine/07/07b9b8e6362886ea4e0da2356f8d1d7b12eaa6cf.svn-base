<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>排课详情管理</title>
<style type="text/css">
.conflict{color: red;};
</style>
<script type="text/javascript">
$(document).ready(function(){
	//$("select[class*=flexselect]").flexselect();
	/*//星期单选或多选
	var teachCourseid = "${teachCourseid}";
	if(teachCourseid!=null && teachCourseid!=""){
		showDay("single");
	}else{
		showDay("multiple");
	} */
   var courseDetailid = "${courseDetail.resourceid}";
   if(courseDetailid != null && courseDetailid != ""){
	   $(".editAble").attr("disabled","disabled").attr("readonly","readonly");
   }
 	//上课时间
	$("#courseDetail_timePeriodL").html("${timePeriodOption}");
	$("#courseDetail_timePeriodR").html("");
	//将选中的option移到右边
	var leftSel = $("#courseDetail_timePeriodL");
	var rightSel = $("#courseDetail_timePeriodR");
	leftSel.find("option:selected").each(function(){
		$(this).remove().appendTo(rightSel);
	});
   if("${teacherids}" !=""){//回显
	   $("#courseDetail_templateid").val("${templateid}");
	   $("#courseDetail_classroomType").val("${classroomType}");
	   $("#courseDetail_classroom").val("${classroomid}");
		//周期、日期
		if("${weekOrDate}"=="1") {//具体时间
			showDate();
		   if("${startDate}"){
			   var startDate = new Date("${startDate}");
			   $("#courseDetail_startDate").val(startDate.getFullYear()+"-"+startDate.getMonth()+"-"+startDate.getDay());
		   }
		   if("${endDate}"){
			   var endDate = new Date("${endDate}");
			   $("#courseDetail_endDate").val(endDate.getFullYear()+"-"+endDate.getMonth()+"-"+endDate.getDay());
		   }
		}else {//周数
			showWeek();
			var weeks = "${weeks}";
			var checkArray = weeks.split(",");
		   var checkBoxAll = $("input[name='week']");
		   $.each(checkBoxAll,function(j,checkbox){
			   $(checkbox).attr("checked",false);
		   })
		   for(var i=0;checkArray && i<checkArray.length;i++){
			   $.each(checkBoxAll,function(j,checkbox){
					var checkValue=$(checkbox).val();
					if(Number(checkArray[i])==checkValue){
						$(checkbox).attr("checked",true);
					}
			   })
		   }
		}
		//星期
		var days = "${days}";
	   var checkArray = days.split(",");
	   var checkBoxAll = $("input[name='day']");
	   $.each(checkBoxAll,function(j,checkbox){
		   $(checkbox).attr("checked",false);
	   })
	   for(var i=0;checkArray && i<checkArray.length;i++){
		   $.each(checkBoxAll,function(j,checkbox){
			   var checkValue=$(checkbox).val();
				if(Number(checkArray[i])==checkValue){
					$(checkbox).attr("checked",true);
				}
		   })
	   }
   }
 });
$(".days").each(function(){//星期改为必选
	$(this).find("input[name='day']").attr("class","required");
});
$("#courseDetailForm input[name='weekOrDate']").click(function(){//周期、日期
   var weekOrDate = $(this).val();
   if(weekOrDate=="0"){
	   showWeek();
   } else if(weekOrDate=="1") {
	   showDate();
   }
});
//周期类型 单选框
$("#courseDetailForm input[name='weekType']").click(function(){
	   var checkBoxAll = $("input[name='week']");
	   if($(this).val() == "1"){//单周
		   $("input[name='week']").removeAttr('checked'); 
		   $("input[name='week']:even").attr('checked','true'); 
		   
		}else if($(this).val() == "2"){//反选
			$("input[name='week']").each(function(){ 
				if($(this).attr('checked')){ 
					$(this).removeAttr('checked'); 
				}else{ 
					$(this).attr('checked','true'); 
				} 
			});
		}else{//全部
			$("input[name='week']").attr('checked','true'); 
		}
});

function showDay(type){
	$(".single_day").each(function(){
		$(this).hide();
	});
	$(".multiple_day").each(function(){
		$(this).hide();
	});
	if(type=="single"){
		$(".single_day").each(function(){
			$(this).show();
		});
		$(".multiple_day").each(function(){
			$(this).remove();
		});
	}else if(type=="multiple"){
		$(".multiple_day").each(function(){
			$(this).show();
		});
		$(".single_day").each(function(){
			$(this).remove();
		});
	}
}
function showWeek(){//显示周期
	var maxWeek = $("#courseDetail_maxWeek").val();
	$(".weeks").each(function(){
		$(this).show();
		$(this).find("input[name='week']").attr("class","required");
	});
	$(".multiple_day").each(function(){
		$(this).show();
		$(this).find("input[name='day']").attr("class","required");
	});
	 $(".date").each(function(){
			$(this).hide();
			$(this).find("input[name='courseDetail_startDate']").removeAttr("class");
			$(this).find("input[name='courseDetail_endDate']").removeAttr("class");
	});
	var checkBoxAll = $("#courseDetailForm input[name='week']");
	var weeks = "";
	$.each(checkBoxAll,function(j,checkbox){
		var week = j+1;
		if(week<=maxWeek){
			weeks += "<input name='week' type='checkbox' onclick='refreshCourseDetailForm()' value='"+week+"'/>";
			if(week<10){
				weeks += "第 "+week+" 周  ";
			}else{
				weeks += "第"+week+"周  ";
			}
		}
		if(week%10==0 && week!=1) weeks += "<br>";
	})
	$("#weekInfo").html(weeks);
	$("input[name=weekOrDate]:eq(0)").attr("checked",'checked'); 
}

function showDate(){//显示日期
	$(".date").each(function(){
		$(this).show();
		$(this).find("input[name='courseDetail_startDate']").attr("class","required");
		$(this).find("input[name='courseDetail_endDate']").attr("class","required");
	});
	$(".weeks").each(function(){
		$(this).hide();
		$(this).find("input[name='week']").removeAttr("class");
	});
	$(".multiple_day").each(function(){
		$(this).hide();
		$(this).find("input[name='day']").removeAttr("class");
	});
	$("input[name=weekOrDate]:eq(1)").attr("checked",'checked'); 
}

$(function(){//时间段
	var leftSel = $("#courseDetail_timePeriodL");
	var rightSel = $("#courseDetail_timePeriodR");
	$("#toright").bind("click",function(){		
		leftSel.find("option:selected").each(function(){
			$(this).remove().appendTo(rightSel);
		});
	});
	$("#toleft").bind("click",function(){		
		rightSel.find("option:selected").each(function(){
			$(this).remove().appendTo(leftSel);
		});
	});
	leftSel.dblclick(function(){
		$(this).find("option:selected").each(function(){
			$(this).remove().appendTo(rightSel);
		});
	});
	rightSel.dblclick(function(){
		$(this).find("option:selected").each(function(){
			$(this).remove().appendTo(leftSel);
		});
	});
});
 function refreshForm(){
	 refreshCourseDetailForm("Y");
 }
//刷新数据
 function refreshCourseDetailForm(optType) {//刷新
	var days = [];//星期
	$("#courseDetailForm input[name='day']:checked").each(function(i,n) {
		days.push(n.value);
	});
	var weeks = [];//周数
	$("#courseDetailForm input[name='week']:checked").each(function(i,n) {
		weeks.push(n.value);
	});
	var timePeriod = []//上课时间
	var select = document.getElementById("courseDetail_timePeriodR"); 
    for(i=0;i<select.length;i++){ 
    	timePeriod.push(select[i].value); 
    }
	var brSchoolid = $("#courseDetail_brSchoolid").val();
	var templateid = $("#courseDetail_templateid").val();
	var courseDetailid = $("#courseDetail_courseDetailid").val();
	var buildingid = $("#courseDetail_building").val();
	var classroomType = $("#courseDetail_classroomType").val();
	var classroomid = $("#courseDetail_classroom").val();
	var teachCourseid = $("#courseDetail_teachCourseid").val();
	var teacherids = $("#courseDetail_teacherids").val();
	var teacherNames = $("#courseDetail_teacherNames").val();
	var openTerm = $("#courseDetail_openTerm").val();
	var weekOrDate = $('#courseDetailForm input:radio[name="weekOrDate"]:checked').val();
	var isUpdate = "N";
	if(optType!=null && optType!='' && optType!=undefined){
		isUpdate = "Y"
	}
	jQuery.ajax({
		data:{"brSchoolid":brSchoolid,"teachCourseid":teachCourseid,"templateid":templateid,"courseDetailid":courseDetailid,"classroomType":classroomType,"classroomid":classroomid,"teacherid":teacherids,"teacherNames":teacherNames},
		dataType:"json",
		url:"${baseUrl}/edu3/arrange/arrangeCourseDetail/update.html?weekOrDate="+weekOrDate+"&days="+days+"&weeks="+weeks+"&timePeriod="+timePeriod+"&openTerm="+openTerm+"&isUpdate="+isUpdate+"&buildingid="+buildingid,
		success:function(data){ //刷新数据
			//上课时间
			$("#courseDetail_timePeriodL").html(data.timePeriodOption);
			$("#courseDetail_timePeriodR").html("");
			$("#courseDetail_templateid").html(data.templateOption);
			$("#courseDetail_teachCourseid").html(data.teachCourseOption);
			$("#courseDetail_willingness").html(data.willingnessInfo);
			$("#courseDetail_teacherids").val(data.teacherid);
			$("#courseDetail_teacherNames").val(data.teacherNames.replace("！",""));
			if(data.teacherNames.indexOf("！")>-1){
				$("#courseDetail_teacherNames").css('color','red');
			}else{
				$("#courseDetail_teacherNames").css('color','black');
			}
			$("#courseDetail_maxWeek").val(data.maxWeek);
			//将选中的option移到右边
			var leftSel = $("#courseDetail_timePeriodL");
			var rightSel = $("#courseDetail_timePeriodR");
			leftSel.find("option:selected").each(function(){
				$(this).remove().appendTo(rightSel);
			});
			//周期/日期
			if(data.weekOrDate=='1') {
				showDate();
			}else {
				showWeek();
			}
			//星期和周数
		   var courseDetailid = $("#courseDetail_courseDetailid").val();
		   if(data.days!=null && courseDetailid==''){//如果是编辑，则不修改星期
			   var checkBoxAll = $("#courseDetailForm input[name='day']");
				$.each(checkBoxAll,function(j,checkbox){
			    	$(checkbox).attr("checked",false);
				})
			   var checkArray = data.days.split(",");
			   for(var i=0;checkArray && i<checkArray.length;i++){
				   $.each(checkBoxAll,function(j,checkbox){
					   var checkValue=$(checkbox).val();
						if(Number(checkArray[i])==checkValue){
							$(checkbox).attr("checked",true);
						}
				   })
			   }
		   }
		   var checkBoxAll = $("#courseDetailForm input[name='week']");
		   $.each(checkBoxAll,function(j,checkbox){
			   $(checkbox).attr("checked",false);
		   })
		   if(data.weeks!=null){
			   var checkArray = data.weeks.split(",");
			   for(var i=0;checkArray && i<checkArray.length;i++){
				   $.each(checkBoxAll,function(j,checkbox){
						var checkValue=$(checkbox).val();
						if(Number(checkArray[i])==checkValue){
							$(checkbox).attr("checked",true);
						}
				   })
			   }
		   }
		   //具体时间
		   if(data.startDate){
			   var startDate = new Date(data.startDate);
			   $("#courseDetail_startDate").val(startDate.getFullYear()+"-"+startDate.getMonth()+"-"+startDate.getDay());
		   }
		   if(data.endDate){
			   var endDate = new Date(data.endDate);
			   $("#courseDetail_endDate").val(endDate.getFullYear()+"-"+endDate.getMonth()+"-"+endDate.getDay());
		   }
		   $("#courseDetail_classroomType").val(data.classroomType);
		   $("#courseDetail_classroom").html(data.classroomOption);
		 //$("select[class*=flexselect]").flexselect();
		}
	});
 }
//选择任课老师
function setTeach_courseDetail(){//record  lecturer  
	var days = [];//星期
	$("#courseDetailForm input[name='day']:checked").each(function(i,n) {
		days.push(n.value);
	});
	var weeks = [];//周数
	$("#courseDetailForm input[name='week']:checked").each(function(i,n) {
		weeks.push(n.value);
	});
	var timePeriod = []//上课时间
	var select = document.getElementById("courseDetail_timePeriodR"); 
    for(i=0;i<select.length;i++){ 
    	timePeriod.push(select[i].value); 
    }
	var resId = $("#courseDetail_courseDetailid").val();
	var teachCourseid =  $("#courseDetail_teachCourseid").val();
	var brSchoolid = $("#courseDetail_brSchoolid").val();
	var templateid = $("#courseDetail_templateid").val();
	var classroomType = $("#courseDetail_classroomType").val();
	var classroomid = $("#courseDetail_classroom").val();
	var weekOrDate = $("#courseDetail_weekOrDate").val();
	var startDate = $("#courseDetail_startDate").val();
	var endDate = $("#courseDetail_endDate").val();
	var isArrange = $("#courseDetail_isArrange").val();
	var isAdd = $("#courseDetail_isAdd").val();
	var ischeck = $("#courseDetail_ischeck").val();
 	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/selectteacher.html";
 	$.pdialog.open(url+"?courseDetailid="+resId+"&teachCourseid="+teachCourseid+"&brSchoolid="+brSchoolid+"&templateid="+templateid+"&classroomType="+classroomType+"&classroomid="+classroomid+"&weekOrDate="+weekOrDate+"&ischeck="+ischeck
 			+"&teachtype=lecturer"+"&days="+days+"&weeks="+weeks+"&timePeriod="+timePeriod+"&startDate="+startDate+"&endDate="+endDate+"&isArrange="+isArrange+"&isAdd="+isAdd,"courseDetailSelectTeacher","选择老师",{height:600,width:800,mask:true});
 	
 }
 
 //可选时间
function courseDetailTimePeriod(){
	var brSchoolid = $("#courseDetail_brSchoolid").val();
	if("${brSchoolid}"!=null && "${brSchoolid}"!=""){
		brSchoolid = "${brSchoolid}";
	}
	var classroomid = $("#courseDetail_classroom").val();
	var teacherids = $("#courseDetail_teacherids").val();
	var teacherNames = $("#courseDetail_teacherNames").val();
	var teachCourseid = "${teachCourseid }";
	if(brSchoolid==""){
		alertMsg.warn("请重新打开界面！");
		return;
	}
	if(teachCourseid==""){
		alertMsg.warn("请选择教学班！");
		return;
	}
	if(classroomid==""){
		alertMsg.warn("请选择课室！");
		return;
	}
	if(teacherNames=="" || teacherids==""){
		alertMsg.warn("请选择老师！");
		return;
	}
	var url = "${baseUrl}/edu3/arrange/arrangeCourseDetail/arrangeTimePeriod.html?brSchoolid="+brSchoolid+"&teachCourseid="+teachCourseid+"&teacherid="+teacherids+"&classroomid="+classroomid;
	$.pdialog.open(url,"courseDetail_timePeriod","可排课时间表",{height:600,width:800,mask:true});
}
 
function showValue(obj) {
	//alert(obj.value);
}
 
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty courseDetail)?'新增':'编辑' }排课详情</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/arrangeCourseDetail/save.html" class="pageForm" onsubmit="return validateCallback(this);" id="courseDetailForm">
		<input type="hidden" id="courseDetail_courseDetailid" name="courseDetailid" value="${courseDetail.resourceid }"/>
		<input type="hidden" id="courseDetail_teacherids" name="teacherids" value="${teacherids }"/>
		<input type="hidden" id="courseDetail_openTerm" name="openTerm" value="${courseDetail.teachCourse.openTerm }"/>
		<c:if test="${not empty courseDetail.resourceid }">
			<input type="hidden" id="courseDetail_day" name="day" value="${courseDetail.days }"/>
		</c:if>
		<c:if test="${not empty teachCourseid }">
			<input type="hidden" id="courseDetail_brSchoolid" name="brSchoolid" value="${brSchoolid }"/>
			<input type="hidden" id="courseDetail_teachCourseid" name="teachCourseid" value="${teachCourseid}"/>
		</c:if>
		<input type="hidden" id="courseDetail_addType" name="addType" value="${addType }"/>
		<input type="hidden" id="courseDetail_isArrange" name="isArrange" value="${isArrange }"/>
		<input type="hidden" id="courseDetail_isAdd" name="isAdd" value="${isAdd}"/>
		<input type="hidden" id="courseDetail_ischeck" name="ischeck" value="${ischeck}"/>
		<input type="hidden" id="courseDetail_maxWeek" name="maxWeek" value="${28}"/>
		<div layoutH="75">
			<table class="form">
				<c:if test="${isAdd eq 'Y'}">
				<tr>
					<td width="15%">教学点：</td>
					<td width="35%"><select class="flexselect" name="brSchoolid" id="courseDetail_brSchoolid" onchange="refreshForm()" style="width:70%;">${unitOption}</select></td>
					<td width="15%">教学班：</td>
					<td width="35%"><gh:selectModel id="courseDetail_teachCourseid" name="teachCourseid" bindValue="resourceid" displayValue="teachingClassname" modelClass="com.hnjk.edu.arrange.model.TeachCourse"
							value="${teachCourseid }" condition="unit.resourceid='${brSchoolid }',isDeleted=0" style="width:70%;" onchange="refreshForm()" classCss="required"/>
					</td>
				</tr>
				</c:if>
				<tr>
					<td width="15%">排课模版：</td>
					<td width="35%"><gh:selectModel name="templateid" id="courseDetail_templateid" value="${templateid }" onchange="refreshForm()" style="width:70%;" bindValue="resourceid" displayValue="templateName"
					 	condition="schoolCalendar.unit.resourceid='${brSchoolid }',isDeleted=0" modelClass="com.hnjk.edu.arrange.model.ArrangeTemplate"/>
					</td>
					<td colspan="2"></td>
				</tr>
				<tr class="days">
					<td><gh:checkBox id="courseDetail_weekOrDate" name="weekOrDate" dictionaryCode="CodeDateType" onclick="showValue(this)" inputType="radio" value="${weekOrDate }" /></td>
					<td colspan="3" class="multiple_day">
						<input name="day" type="checkbox" value="1" onclick="refreshCourseDetailForm()" class="editAble"/>星期一
						<input name="day" type="checkbox" value="2" onclick="refreshCourseDetailForm()" class="editAble"/>星期二
						<input name="day" type="checkbox" value="3" onclick="refreshCourseDetailForm()" class="editAble"/>星期三
						<input name="day" type="checkbox" value="4" onclick="refreshCourseDetailForm()" class="editAble"/>星期四
						<input name="day" type="checkbox" value="5" onclick="refreshCourseDetailForm()" class="editAble"/>星期五
						<input name="day" type="checkbox" value="6" onclick="refreshCourseDetailForm()" class="editAble"/>星期六
						<input name="day" type="checkbox" value="7" onclick="refreshCourseDetailForm()" class="editAble"/>星期日
					</td>
					<!-- <td colspan="3" class="single_day">
						<input name="day" type="radio" value="1" class="editAble"/>星期一
						<input name="day" type="radio" value="2" class="editAble"/>星期二
						<input name="day" type="radio" value="3" class="editAble"/>星期三
						<input name="day" type="radio" value="4" class="editAble"/>星期四
						<input name="day" type="radio" value="5" class="editAble"/>星期五
						<input name="day" type="radio" value="6" class="editAble"/>星期六
						<input name="day" type="radio" value="7" class="editAble"/>星期日
					</td> -->
				</tr>
				<tr class="date">
					<td align="center">日期</td>
					<td colspan="3">
						具体开始时间：
						<input type="text" id="courseDetail_startDate" name="startDate" size="40" style="width:20%" value="<fmt:formatDate value="${template.startDate }" pattern="yyyy-MM-dd" />" 
							 onFocus="WdatePicker({isShowWeek:true})"/>
						<label></label>具体结束时间：
						<input type="text" id="courseDetail_endDate" name="endDate" size="40" style="width:20%" value="<fmt:formatDate value="${template.endDate }" pattern="yyyy-MM-dd" />" 
							 onFocus="WdatePicker({isShowWeek:true})"/>
					</td>
				</tr>
				<tr class="weeks">
					<td><gh:checkBox id="courseDetail_weekType" name="weekType" dictionaryCode="CodeWeekType"  inputType="radio" value="${CodeWeekType }"  /></td>
					<td colspan="3" id="weekInfo" >
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="1" /> 第 1 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="2"/>第 2 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="3"/>第 3 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="4"/> 第 4 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="5"/>第 5 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="6"/>第 6 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="7"/>第 7 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="8"/> 第 8 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="9"/>第 9 周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="10"/>第10周
						<br>
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="11"/>第11周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="12"/>第12周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="13"/>第13周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="14"/>第14周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="15"/>第15周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="16"/>第16周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="17"/>第17周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="18"/>第18周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="19"/>第19周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="20"/>第20周
						<br>
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="21"/>第21周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="22"/>第22周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="23"/>第23周
						<input name="week" type="checkbox" onclick="refreshCourseDetailForm()" value="24"/>第24周
					</td>
				</tr>
				<tr>
					<td>主讲老师：</td>
					<td><input id="courseDetail_teacherNames" name="teacherNames" type="text" 
						value="${fn:replace(teacherNames,'！','') }" style="width:50%;" readonly="readonly" class="required" ><label></label>
						<button type="button" onclick="setTeach_courseDetail();" class="">选择<c:if test="${not empty teacherNames}">其他</c:if>老师</button>
					</td>
				</tr>
				<tr>
					<td>上课时间</td>
					<td style="width:80%" colspan="3">
					<div>
						<select id="courseDetail_timePeriodL" name="timePeriodL" size="6" multiple='multiple' style="width: 30%;float: left;"></select>
						<div style="float: left;margin-left: 8px;margin-top: 15px;height: 100%;width: 40px"> 
        					<input id="toright" type="button" style="margin-top:5px" title="添加" value=">>">
        					<input id="toleft" type="button" style="margin-top:5px" title="移除" value="<<">
    					 </div>
    					 <select id="courseDetail_timePeriodR" name="timePeriod" size="6" multiple="multiple" style="width: 30%;float: left;margin-left: 8px" ></select>
					</div>
					</td>
				</tr>
				<tr>
					<td>教室类型：</td>
					<td><gh:select id="courseDetail_classroomType" name="classroomType" value="${classroomType }" dictionaryCode="CodeClassRoomStyle" style="width:50%" onchange="refreshCourseDetailForm()"/></td>
					<td>课室：</td>
					<td><select class='flexselect required' id='courseDetail_classroom' onchange="refreshCourseDetailForm()" name='classroomid'>${classroomOption }</select></td>
				</tr>
				<c:if test="${ischeck eq 'Y' }">
					<tr>
						<td colspan="4"><button type="button" onclick="courseDetailTimePeriod();">排课可选时间</button><label style="color: red;">*查看可选老师，可选上课时间，可选课室</label></td>
					</tr>
				</c:if>
				<tr>
					<td colspan="5" style="width: 100%"><table id="courseDetail_willingness" style="width: 100%;">
						<tr>
							<td style="text-align: center;font-weight: bold;width: 10%">申请教师</td>
							<td style="text-align: center;font-weight: bold;width: 10%">申请课室类型</td>
							<td style="text-align: center;font-weight: bold;width: 25%">意愿上课时间</td>
							<td style="text-align: center;font-weight: bold;width: 25%">星期</td>
							<td style="text-align: center;font-weight: bold;width: 25%">申请信息</td>
						</tr>
						<c:forEach items="${willingnessList}" var="w" varStatus="vs">
							<tr><td style="text-align: center;">${w.proposer.cnName }</td>
							<td style="text-align: center;">${ghfn:dictCode2Val('CodeClassRoomStyle',w.classroomType) }</td>
							<td style="text-align: center;">${w.timePeriodNames }</td>
							<td style="text-align: center;">${w.daysName }</td>
							<td style="text-align: center;">${w.info }</td></tr>
						</c:forEach>
					</table></td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit" onclick="">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>