<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>排课模版管理</title>
<script type="text/javascript">
$(document).ready(function(){
	if("${template.dateType}"==1){
		showDate();
	}else{
		showWeek();
	}
	//上课时间
	$("#template_timePeriodL").html("${timePeriodOption}");
	$("#template_timePeriodR").html("");
	//将选中的option移到右边
	var leftSel = $("#template_timePeriodL");
	var rightSel = $("#template_timePeriodR");
	leftSel.find("option:selected").each(function(){
		$(this).remove().appendTo(rightSel);
	});
	//周/日期 单选框
   $("#templateForm input[name='weekOrDate']").click(function(){
	   var weekOrDate = $(this).val();
	   if(weekOrDate=="0"){
		   showWeek();
	   } else {
		   showDate();
	   }
   });
   //周期类型 单选框
   $("#templateForm input[name='weekType']").click(function(){
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
   //回显数据
   var checkeds = "${template.days}";
   var checkArray =checkeds.split(",");
   var checkBoxAll = $("input[name='day']");
   for(var i=0;i<checkArray.length;i++){
	   $.each(checkBoxAll,function(j,checkbox){
			var checkValue=$(checkbox).val();
			if(checkArray[i]==checkValue){
				$(checkbox).attr("checked",true);
			}
	   })
   }
   var checkeds  = "${template.weeks}";
   var checkArray = checkeds.split(",");
   var checkBoxAll = $("input[name='week']");
   for(var i=0;i<checkArray.length;i++){
	   $.each(checkBoxAll,function(j,checkbox){
			var checkValue=$(checkbox).val();
			if(checkArray[i]==checkValue){
				$(checkbox).attr("checked",true);
			}
	   })
   }
 });
 
$(function(){//时间段
	var leftSel = $("#template_timePeriodL");
	var rightSel = $("#template_timePeriodR");
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

function showWeek(){//显示周期
	$(".weeks").each(function(){
		$(this).show();
		$(this).find("input[name='week']").attr("class","required");
	});
	 $(".date").each(function(){
			$(this).hide();
			$(this).find("input[name='templateDetail_startDate']").removeAttr("class");
			$(this).find("input[name='templateDetail_endDate']").removeAttr("class");
	});
	$("input[name=weekOrDate]:eq(0)").attr("checked",'checked'); 
}

function showDate(){//显示日期
	$(".date").each(function(){
		$(this).show();
		$(this).find("input[name='templateDetail_startDate']").attr("class","required");
		$(this).find("input[name='templateDetail_endDate']").attr("class","required");
	});
	$(".weeks").each(function(){
		$(this).hide();
		$(this).find("input[name='week']").removeAttr("class");
	});
	$("input[name=weekOrDate]:eq(1)").attr("checked",'checked'); 
}

//刷新页面数据
 function refresh() {
	var brSchoolid = $("#template_brSchoolid").val();
	var templateid = $("#template_templateid").val();
	var schoolCalendarid = $("#template_schoolCalendarid").val();
	$.ajax({
		type:"post",
		url:"${baseUrl}/edu3/arrange/template/updateForm.html",
		data:{"brSchoolid":brSchoolid,"templateid":templateid,"schoolCalendarid":schoolCalendarid},
		dataType:"json",
		success:function(data){
			if(data.statusCode == 200){
				$("#template_timePeriodL").html(data.timePeriodOption);
				$("#template_timePeriodR").html("");
				$("#template_schoolCalendarid").html(data.schoolCalendarOption);
				//将选中的option移到右边
				var leftSel = $("#template_timePeriodL");
				var rightSel = $("#template_timePeriodR");
				leftSel.find("option:selected").each(function(){
					$(this).remove().appendTo(rightSel);
				});
				var maxWeek = data.maxWeek;
				var checkBoxAll = $("input[name='week']");
				var weeks = "";
				$.each(checkBoxAll,function(j,checkbox){
					var week = j+1;
					if(week<=maxWeek){
						weeks += "<input name='week' type='checkbox'  value='"+week+"'/>";
						if(week<10){
							weeks += "第 "+week+" 周";
						}else{
							weeks += "第"+week+"周";
						}
					}
					if(week%10==0 && week!=1) weeks += "<br>";
				})
				$("#weekInfo").html(weeks);
			}else{
				alertMsg.error(data.message);
			}
		}
	});
 }
 
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty template.resourceid)?'新增':'编辑' }排课模版</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/template/save.html" class="pageForm" onsubmit="return validateCallback(this);" id="templateForm">
		<input type="hidden" name="resourceid" value="${template.resourceid }"/>     
		<input type="hidden" name="hasSCalendar" value="${hasSCalendar }"/>  
		<input type="hidden" id="template_templateid" name="templateid" value="${templateid }"/> 
		<div layoutH="75">
			<table class="form">
				<tr>
					<td width="20%">教学点：</td>
					<td width="30%"><select class="flexselect" name="brSchoolid" id="template_brSchoolid" onchange="refresh()" style="width:50%;">${unitOption}</select></td>
					<td width="20%">所属院历：(只能选择已发布的院历)</td>
					<td width="30%"><select class="flexselect" id="template_schoolCalendarid" name="schoolCalendarid" onchange="refresh()" style="width:50%;" required="required">${sCalendarOption}</select>
					<label style="color: red;">*</label></td>
				</tr>
				<tr>	
					<td>模版名称：</td>
					<td><input type="text" id="template_brSchoolid" name="templateName" style="width:50%" value="${template.templateName }" class="required"/></td>
				</tr>
				<tr>
					<td>上课时间</td>
					<td style="width:80%" colspan="3">
					<div>
						<select id="template_timePeriodL" name="timePeriodL" size="6" multiple='multiple' style="width: 30%;float: left;"></select>
						<div style="float: left;margin-left: 8px;margin-top: 20px;height: 100%;width: 40px"> 
        					<input id="toright" type="button" style="margin-top:5px" title="添加" value=">>">
        					<input id="toleft" type="button" style="margin-top:5px" title="移除" value="<<">
    					 </div>
    					 <select id="template_timePeriodR" name="timePeriod" size="6" multiple="multiple" style="width: 30%;float: left;" ></select>
					</div>
					</td>
				</tr>
				<tr>
					<td><gh:checkBox id="template_weekOrDate" name="weekOrDate" dictionaryCode="CodeDateType"  inputType="radio" value="${template.dateType }" /></td>
					<td colspan="2" class="weeks">
						<input id="day1" name="day" type="checkbox"  value="1"/>星期一
						<input id="day2" name="day" type="checkbox"  value="2"/>星期二
						<input id="day3" name="day" type="checkbox"  value="3"/>星期三
						<input id="day4" name="day" type="checkbox"  value="4"/>星期四
						<input id="day5" name="day" type="checkbox"  value="5"/>星期五
						<input id="day6" name="day" type="checkbox"  value="6"/>星期六
						<input id="day7" name="day" type="checkbox"  value="7"/>星期日
					</td>
					<td colspan="3"></td>
				</tr>
				<tr class="date">
					<td align="center">日期</td>
					<td colspan="3">
						具体开始时间：
						<input type="text"  name="templateDetail_startDate" size="40" style="width:20%" value="<fmt:formatDate value="${template.startDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required date1" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/>
						&nbsp;&nbsp;&nbsp;具体结束时间：
						<input type="text"  name="templateDetail_endDate" size="40" style="width:20%" value="<fmt:formatDate value="${template.endDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required date1" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/>
				</td>
				</tr>
				<tr class="weeks">
					<td><gh:checkBox id="templateDetail_weekType" name="weekType" dictionaryCode="CodeWeekType"  inputType="radio" value=""  /></td>
					<td colspan="3" id="weekInfo">
						<input name="week" type="checkbox"  value="1"/> 第 1 周
						<input name="week" type="checkbox"  value="2"/>第 2 周
						<input name="week" type="checkbox"  value="3"/>第 3 周
						<input name="week" type="checkbox"  value="4"/> 第 4 周
						<input name="week" type="checkbox"  value="5"/>第 5 周
						<input name="week" type="checkbox"  value="6"/>第 6 周
						<input name="week" type="checkbox"  value="7"/>第 7 周
						<input name="week" type="checkbox"  value="8"/> 第 8 周
						<input name="week" type="checkbox"  value="9"/>第 9 周
						<input name="week" type="checkbox"  value="10"/>第10周
						<input name="week" type="checkbox"  value="11"/>第11周
						<input name="week" type="checkbox"  value="12"/>第12周
						<br>
						<input name="week" type="checkbox"  value="13"/>第13周
						<input name="week" type="checkbox"  value="14"/>第14周
						<input name="week" type="checkbox"  value="15"/>第15周
						<input name="week" type="checkbox"  value="16"/>第16周
						<input name="week" type="checkbox"  value="17"/>第17周
						<input name="week" type="checkbox"  value="18"/>第18周
						<input name="week" type="checkbox"  value="19"/>第19周
						<input name="week" type="checkbox"  value="20"/>第20周
						<input name="week" type="checkbox"  value="21"/>第21周
						<input name="week" type="checkbox"  value="22"/>第22周
						<input name="week" type="checkbox"  value="23"/>第23周
						<input name="week" type="checkbox"  value="24"/>第24周
					</td>
				</tr>
				<%-- <tr>
					<td>开始时间：</td>
					<td><input type="text"  name="startDate" size="40" style="width:50%" value="<fmt:formatDate value="${template.startDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required date1" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/></td>
					<td>结束时间：</td>
					<td><input type="text"  name="endDate" size="40" style="width:50%" value="<fmt:formatDate value="${template.endDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required date1" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/></td>
				</tr> --%>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>