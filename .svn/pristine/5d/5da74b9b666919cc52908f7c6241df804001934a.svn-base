<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申请校际转入</title>
<style type="text/css">
.mytable td {
	border: 0;
	border-style: none;
}
</style>
<script type="text/javascript">	
	  jQuery(document).ready(function(){   
		  if("${finished}"=='Y'){
			 $("#changeTab [name^=2]").hide();
			 $("#changeStu [name^=2]").hide();
			 $("#changeTab #A,#B,#C,#D,#E,#F,#G").show();
			 $("#changeStu #I,#J,#K,#L,#M,#N,#O,#P,#Q,#R,#S,#T,#U,#V,#W,#X").show();
		  }else{
			$("#changeTab [name^=2]").show();
			$("#changeStu [name^=2]").show();
			$("#changeTab #A,#B,#C,#D,#E,#F,#G").hide();
			$("#changeStu #I,#J,#K,#L,#M,#N,#O,#P,#Q,#R,#S,#T,#U,#V,#W,#X").hide();
			var gradeIdb    = $("#changeIntoSchool_gradeName").val();
			if((""==gradeIdb)||(null==gradeIdb)){
				$("#gradeTip").show();
			}else{
				$("#gradeTip").hide();
			}
		  }
			
			//一打开页面是检查学生的证件类型，判断是否锁定生日日期的输入和性别的输入
			var certType =$("#changeStu #certType").val();
			if(certType!='idcard'){
		   	//若不是身份证，解除生日和 性别的锁定
	   			$("#changeStu #gender").attr("readonly","");
	   			$("#changeStu #bornDay").attr("readonly","");
			}
			
			//证件号码变更时，判断证件类型，为身份证时才自动生成生日日期与性别
			  $("#changeStu input[name='certNum']").bind("change",function(){
				//先判断是不是身份证
		   		var certType =$("#certType").val();
		   		if(certType=='idcard'){
			   		//是身份证时，生成生日性别等信息
			   		var id = $("#changeStu input[name='certNum']").val() ;
			   		showBirthday(id);
		   		}
				});
			//证件类型变更时，判断证件类型，改变生日与性别输入框的可写属性
			  $("#certType").bind("change",function(){	 
				//先判断是不是身份证
		   		var certType =$("#certType").val();
				if(certType=='idcard'){
			   		//是身份证时，锁定输入框
					$("#changeStu input[name='gender']").attr("readonly","readonly");
		   			$("#changeStu input[name='bornDay']").attr("readonly","readonly");
		   			$("#changeStu input[name='gender']").attr("class","textInput  readonly");
		   			$("#changeStu input[name='bornDay']").attr("class","textInput readonly");
		   		}else{
		   			//是其他证件时，解除锁定输入框
		   			$("#changeStu input[name='gender']").attr("readonly","");
		   			$("#changeStu input[name='bornDay']").attr("readonly","");
		   			$("#changeStu input[name='gender']").attr("class","textInput");
		   			$("#changeStu input[name='bornDay']").attr("class","textInput");
		   		}
			  });
			
			 //保持名字一致
			  $("#changeIntoSchool_stuName").bind("change",function(){	 
			   		var stuName =$("#changeIntoSchool_stuName").val();
			   		$("#changeStu input[name='name']").val(stuName);
			  });
			  
			  //保持名字一致
			  $("#name").bind("change",function(){	 
			   		var name =$("#name").val();
				  $("#changeStu input[name='changeIntoSchool_stuName']").val(name);
			  });
		  
		//籍贯
		$("#ChinaArea1").jChinaArea({
				//	 aspnet:true,
					 s1:"${homePlaceProvince}",//默认选中的省名
					 s2:"${homePlaceCity}",//默认选中的市名
					 s3:"${homePlaceDistrict}"//默认选中的县区名
			 });
		//户口
		$("#ChinaArea2").jChinaArea({
			//	 aspnet:true,
				 s1:"${HouseholdRegisterationProvince}",//默认选中的省名
				 s2:"${HouseholdRegisterationCity}",//默认选中的市名
				 s3:"${HouseholdRegisterationDistrict}"//默认选中的县区名
		 });
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		orgStuStatus("#studentInfoForm #studentStatus",studentStatusSet,statusRes,"a11,b11");
   			
   			
			//$("#changeIntoSchool_changeBeforeMajorId").flexselect();  
	  });
	  
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
	  
	//参考招生现场报名代码
	//通过身份证号获取性别和出生日期
	function showBirthday(val){
		var birthdayValue;
   		var bornDay = $("#changeStu #bornDay");
   		var gender = $("#changeStu #gender");
   		if(15==val.length){ //15位身份证号码
   		    birthdayValue = val.charAt(6)+val.charAt(7);
   		    if(parseInt(birthdayValue)<10){
   		     birthdayValue = '20'+birthdayValue;
   		    } else {
   		     birthdayValue = '19'+birthdayValue;
   		    }
   		    birthdayValue=birthdayValue+'-'+val.charAt(8)+val.charAt(9)+'-'+val.charAt(10)+val.charAt(11);
   		    if(parseInt(val.charAt(14)/2)*2!=val.charAt(14)){		    	
   		    	gender.val("男");
   		    }else{ 	
   		    	gender.val("女");
   		    }
   		    bornDay.val(birthdayValue);		   
   		 }  else if(18==val.length){	 //18位身份证号码
   		    birthdayValue=val.charAt(6)+val.charAt(7)+val.charAt(8)+val.charAt(9)+'-'+val.charAt(10)+val.charAt(11) +'-'+val.charAt(12)+val.charAt(13);
   		    if(parseInt(val.charAt(16)/2)*2!=val.charAt(16)){
   		    	gender.val("男");
   		    }else{
   		    	gender.val("女");
   		    }
   		    if(val.charAt(17)!=IDCard(val))  {
   		     $("#changeStu input[name='certNum']").css({"backgroundColor":"#ffc8c8"});
   		  	 alertMsg.warn("身份证号码不合法!");
   		     gender.val("");//清除
   		     bornDay.val("");
   		    }else{
   		    	$("#changeStu input[name='certNum']").css({"backgroundColor":"#fff"});
   		    	bornDay.val(birthdayValue);
   		    }   		  
   		   }else{
   			alertMsg.warn("身份证号码位数不正确!");  
   		   }
   		 }
	
	// 18位身份证号最后一位校验
   	function IDCard(Num){
   		if (Num.length!=18)   return false;
   		var x=0;
   		var y='';
		
   		for(i=18;i>=2;i--)
   		    x = x + (square(2,(i-1))%11)*parseInt(Num.charAt(19-i-1));
   		x%=11;
   		y=12-x;
   		if (x==0)
   		    y='1';
   		if (x==1)
   		    y='0';
   		if (x==2)
   		    y='X';
   		return y;
   	}
	
 	// 求得x的y次方
 	function square(x,y){
 		var i=1;
 		for (j=1;j<=y;j++)
 		   i*=x;
 		return i;
  	}
		
	function saveChange(){
		jQuery("#CM").val("0");
	}
	
	function submitChange(){
		jQuery("#CM").val("1");
	}
	
	function stuchangeinfo_submitForm(type,form){
		//var f =  document.getElementById("stuchangeintoSchoolForm");
		var type = jQuery("#CM").val("1");
		form.action += "?CM="+type;
		
		//参考招生部分的身份证验证
		//先判断是不是身份证
   		var certType =$("#certType").val();
		jQuery("#homePlaceAll").val(jQuery("#homePlaceprovince option:selected").text()+","
  				 +jQuery("#homePlacecity option:selected").text()+","
   			 +jQuery("#homePlacecounty option:selected").text());
		jQuery("#residenceAll").val(jQuery("#HouseholdRegisterationprovince option:selected").text()+","
 				 +jQuery("#HouseholdRegisterationcity option:selected").text()+","
  			 +jQuery("#HouseholdRegisterationcounty option:selected").text());
		if(certType!='idcard'){
			return validateCallback(form);
   		}
   		//是身份证时，执行身份证号码校验
		var id = $("#changeStu input[name='certNum']").val() ;
		if(_idCardValidate(id)){ 
			$("#changeStu input[name='certNum']").css({"backgroundColor":"#fff"});
        }else{
      	 	alertMsg.warn("身份证号码不合法!");
       	 	$("#changeStu input[name='certNum']").css({"backgroundColor":"#ffc8c8"});
       		return false;
        }
		showBirthday(id);
		
		return validateCallback(form);
	}
	
	function stuchangeinfo_submitForm(form){
		//var f =  document.getElementById("stuchangeintoSchoolForm");
		var type = jQuery("#CM").val("1");
		form.action += "?CM="+type;
		
		//参考招生部分的身份证验证
		//先判断是不是身份证
   		var certType =$("#certType").val();
		jQuery("#homePlaceAll").val(jQuery("#homePlaceprovince option:selected").text()+","
  				 +jQuery("#homePlacecity option:selected").text()+","
   			 +jQuery("#homePlacecounty option:selected").text());
		jQuery("#residenceAll").val(jQuery("#HouseholdRegisterationprovince option:selected").text()+","
 				 +jQuery("#HouseholdRegisterationcity option:selected").text()+","
  			 +jQuery("#HouseholdRegisterationcounty option:selected").text());
		if(certType!='idcard'){
			return validateCallback(form);
   		}
   		//是身份证时，执行身份证号码校验
		var id = $("#changeStu input[name='certNum']").val() ;
		if(_idCardValidate(id)){ 
			$("#changeStu input[name='certNum']").css({"backgroundColor":"#fff"});
        }else{
      	 	alertMsg.warn("身份证号码不合法!");
       	 	$("#changeStu input[name='certNum']").css({"backgroundColor":"#ffc8c8"});
       		return false;
        }
		showBirthday(id);
		
		return validateCallback(form);
	}
	
	//**核心方法区
	//申请校级转入
	function changeIntoSchoolApplyOnceMore(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/changeIntoSchool.html";
		navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_INTOSCHOOL', url, '申请校级转入');
		//给本页加上重载的设定
		navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_CHANGE");
	}
	//**校级转入方法组
	//选择教学站_校级转入
	function selectSchool_changeIntoSchool(){		
		var teachType  = $("#changeIntoSchool_teachingType").val();
		var classicId  = $("#changeIntoSchool_classicId").val();
		var gradeId    = $("#changeIntoSchool_gradeName").val();
		if(""==gradeId){
			$("#gradeTip").show();
			return false;
		}else{
			$("#gradeTip").hide();
		}
		if(""==classicId ||null==classicId ){
			$("#classicTip").show();
			return false;
		}else{
			$("#classicTip").hide();
		}
		if(""==teachType){
			$("#teachTypeTip").show();
			return false;
		}else{
			$("#teachTypeTip").hide();
		}
		
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeIntoSchool_stuCenterid").html(html);
		jQuery.ajax({
			data:"teachType="+teachType+"&classicId="+classicId+"&gradeId="+gradeId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getSchool.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.schoolList.length;i++){
						html += "<option value='"+data.schoolList[i]['RESOURCEID']+"'>"+data.schoolList[i]['UNITNAME']+"</option>";
					}
					$("#changeIntoSchool_stuCenterid").html(html);
					$("#unitTip").show();
				}
			}
		});
	}
	//选择专业_校级转入
	function selectMajor_changeIntoSchool(){
		var teachType  = $("#changeIntoSchool_teachingType").val();
		var gradeId    = $("#changeIntoSchool_gradeName").val();
		var classicId  = $("#changeIntoSchool_classicId").val();
		var brSchoolId = $("#changeIntoSchool_stuCenterid").val(); 
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeIntoSchool_majorId").html(html);
		if(""==gradeId||null==gradeId){
			$("#gradeTip").show();
			return false ;
		}else{
			$("#gradeTip").hide();
		}
		if(""==brSchoolId ||null==brSchoolId ){
			$("#unitTip").show();
			return false;
		}else{
			$("#unitTip").hide();
		}
		if(""==teachType){
			$("#teachTypeTip").show();
			return false;
		}else{
			$("#teachTypeTip").hide();
		}
		if(""==classicId ||null==classicId ){
			$("#classicTip").show();
			return false;
		}else{
			$("#classicTip").hide();
		}
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>";
					}
					$("#changeIntoSchool_majorId").html(html);
					$("#majorTip").show();
				}
			}
		});
	}
	//选择班级_校级转入
	function _selectClasses_changeIntoSchool(obj){
		
		var teachType  = $("#changeIntoSchool_teachingType").val();
		var gradeId    = $("#changeIntoSchool_gradeName").val();
		var classicId  = $("#changeIntoSchool_classicId").val();
		var brSchoolId = $("#changeIntoSchool_stuCenterid").val(); 
	
		var majorId   = "";
		if(null==obj){
			majorId = $("#backSchool_majorid").val();
		}else{
			majorId = $(obj).val();
		}
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeIntoSchool_changeClass").html(html);
		if((""==brSchoolId)||(null==brSchoolId) ){
			$("#unitTip").show();
			return false;
		}else{
			$("#unitTip").hide();
		}
		if((""==gradeId)||(null==gradeId)){
			$("#gradeTip").show();
			return false;
		}else{
			$("#gradeTip").hide();
		}
		if(""==teachType){
			$("#teachTypeTip").show();
			return false;
		}else{
			$("#teachTypeTip").hide();
		}
		if(""==classicId ||null==classicId ){
			$("#classicTip").show();
			return false;
		}else{
			$("#classicTip").hide();
		}
		if(""==majorId ||null==majorId ){
			$("#majorTip").show();
			return false;
		}else{
			$("#majorTip").hide();
		}
		jQuery.ajax({
			data:{teachType:teachType,gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,majorId:majorId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.classesList.length;i++){
						html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
					}
					$("#changeIntoSchool_changeClass").html(html);
					$("#classTip").show();
				}
			}
		});
	}
	//选择教学计划_校级转入
	function selectTeachPlan_changeIntoSchool(obj){
		var teachType  = $("#changeIntoSchool_teachingType").val();
		var gradeId    = $("#changeIntoSchool_gradeName").val();
		var classicId  = $("#changeIntoSchool_classicId").val();
		var brSchoolId = $("#changeIntoSchool_stuCenterid").val(); 
		var majorId    = $("#changeIntoSchool_majorId").val();
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeIntoSchool_teachingPlan").html(html);
		if((""==brSchoolId)||(null==brSchoolId) ){
			$("#unitTip").show();
			return false;
		}else{
			$("#unitTip").hide();
		}
		if((""==gradeId)||(null==gradeId)){
			$("#gradeTip").show();
			return false;
		}else{
			$("#gradeTip").hide();
		}
		if(""==teachType){
			$("#teachTypeTip").show();
			return false;
		}else{
			$("#teachTypeTip").hide();
		}
		if(""==classicId ||null==classicId ){
			$("#classicTip").show();
			return false;
		}else{
			$("#classicTip").hide();
		}
		if(""==majorId ||null==majorId ){
			$("#majorTip").show();
			return false;
		}else{
			$("#majorTip").hide();
		}
		//非参数
		var classesId = $("#changeIntoSchool_changeClass").val();
		if(""==classesId ||null==classesId ){
			$("#classTip").show();
			return false;
		}else{
			$("#classTip").hide();
		}
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&majorId="+majorId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
			success:function(data){
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
				}else{				
					for(var i=0;i<data.teachPlanList.length;i++){
						html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
					}
					$("#changeIntoSchool_teachingPlan").html(html);
					$("#teachingPlanTip").show();
					
				}
			}
		});
	}
	
	function check(){
		var changeIntoSchool_teachingPlanId = $("#changeIntoSchool_teachingPlan").val();
		if(""==changeIntoSchool_teachingPlanId ||null==changeIntoSchool_teachingPlanId ){
			$("#teachingPlanTip").show();
			return false;
		}else{
			$("#teachingPlanTip").hide();
		}	
	}
	function isCheckStuNOOnChange(){
		
		if($("#createStuNoid").attr("checked")){
			
			$("#inputStuNoid").hide();
			$("#verifyStuNoid").hide();
			//$("#inputStuNoid").attr("class","text");
			$("#inputStuNoid").val("");
			$("#inputStuNoid").removeAttr("class");
		}else{
			$("#inputStuNoid").attr("class","required");
			$("#inputStuNoid").show();
			$("#verifyStuNoid").show();
		}
	}
	function verifyStuNo(){
		var stuNOid = $("#inputStuNoid");
    	   	
    	if(stuNOid.val()==""){ alertMsg.warn("请输入登录账号"); userName.focus() ;return false; }
    	var url = "${baseUrl}/edu3/register/studentinfo/verifyStuNo.html";
    	jQuery.post(url,{studentNum:stuNOid.val(),},function(data){
    		if(data == "exist"){ alertMsg.warn("学号已存在!"); }else{ alertMsg.correct("恭喜，此学号可用！")}
    	})
	}
	</script>
</head>
<body>
	<!-- 本页面无法实现编辑校级转入的功能，敬请留意  -->
	<h2 class="contentTitle">申请校级转入</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/register/stuchangeinfo/changeIntoSchool/save.html"
				class="pageForm" id="stuchangeintoSchoolForm"
				onsubmit="return stuchangeinfo_submitForm(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${stuChangeInfo.resourceid }" /> <input type="hidden"
					name="applicationDate"
					value="<fmt:formatDate value="${stuChangeInfo.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<input type="hidden" id="CM" name="CM" value=""> <input
					type="hidden" id="homePlaceAll" name="homePlaceAll" value="" /> <input
					type="hidden" id="residenceAll" name="residenceAll" value="" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>异动信息</span></a></li>
									<li class="#"><a href="#"><span>学生信息录入</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="changeTab">
									<tr id="A">
										<td width="10%">年级:</td>
										<td width="30%">
											${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName }</td>
										<td width="10%">学号:</td>
										<td width="25%">${stuChangeInfo.studentInfo.studyNo}</td>
										<td width="10%">操作员:</td>
										<td width="14%"><input type="hidden" name="opManId"
											id="opManId" value="${opManId}" /> <input type="hidden"
											name="opMan" id="opMan" value="${opMan}" /> <span>${opMan}</span>
										</td>
									</tr>
									<tr style="display: none" name="2A">
										<td width="10%">年级:</td>
										<td width="30%"><gh:selectModel classCss="required"
												id="changeIntoSchool_gradeName"
												name="changeIntoSchool_gradeName" bindValue="resourceid"
												displayValue="gradeName"
												modelClass="com.hnjk.edu.basedata.model.Grade"
												value="${stuChangeInfo.changeTeachingGuidePlan.grade.gradeName}"
												onchange="selectSchool_changeIntoSchool()"
												orderBy="yearInfo.firstYear desc" choose="Y"
												style="float:none;width: 260px" /> <span id="gradeTip"
											style="display: none;"><font
												style="background-color: red" color="white">请选择年级</span></td>
										<td width="10%">学号:</td>
										<td width="25%"><input type="text" name="inputStuNo"
											id="inputStuNoid" class="required"> <span
											class="buttonActive" style="margin-left: 8px"
											id="verifyStuNoid"><div class="buttonContent">
													<button type="button" onclick="verifyStuNo();">检查唯一性</button>
												</div></span> <input type="checkbox" name="createStuNo"
											id="createStuNoid" onchange="isCheckStuNOOnChange()"
											value="checked"> 由系统生成</td>
										<td width="10%">操作员:</td>
										<td width="14%"><input type="hidden" name="opManId"
											id="opManId" value="${opManId}" /> <input type="hidden"
											name="opMan" id="opMan" value="${opMan}" /> <span>${opMan}</span>
										</td>
									</tr>

									<tr id="B">
										<td width="10%">姓名:</td>
										<td width="30%"><span>${stuChangeInfo.studentInfo.studentName}</span>
										</td>
										<td width="10%">变动原因:</td>
										<td width="25%">${stuChangeInfo.reason}</td>
										<td width="10%">备注:</td>
										<td width="14%">${stuChangeInfo.memo }</td>
									</tr>
									<tr style="display: none" name="2B">
										<td width="10%">姓名:</td>
										<td width="30%"><input type="text"
											id="changeIntoSchool_stuName" name="changeIntoSchool_stuName"
											style="width: 60%"
											value="${stuChangeInfo.studentInfo.studentName}"
											class="required" /></td>
										<td width="10%">变动原因:</td>
										<td width="25%"><textarea rows="2" cols="50%"
												name="changeIntoSchool_reason">${stuChangeInfo.reason}</textarea>
										</td>
										<td width="10%">备注:</td>
										<td width="14%"><textarea rows="2" cols="50%"
												name="changeIntoSchool_memo">${stuChangeInfo.memo }</textarea>
										</td>
									</tr>

									<tr id="C">
										<td width="10%">现学院:</td>
										<td width="30%"><span>${stuChangeInfo.changeBrschool.unitName}</span>
										</td>
										<td width="10%">原学校:</td>
										<td width="25%">${stuChangeInfo.changeBeforeSchoolName}</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2C">
										<td width="10%">现学院:</td>
										<td width="30%"><select
											name="changeIntoSchool_stuCenterid"
											id="changeIntoSchool_stuCenterid" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="selectMajor_changeIntoSchool()"><option
													value="">请选择</option></select><font color=red>*</font> <span
											id="unitTip" style="display: none;"><font
												style="background-color: red" color="white">请选择学院</span></td>
										<td width="10%">原学校:</td>
										<td width="25%"><input
											id="changeIntoSchool_changeBeForeSchoolName"
											name="changeIntoSchool_changeBeForeSchoolName" type="text"
											value="${stuChangeInfo.changeBeforeSchoolName}"
											class="required" /></td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>



									<tr id="D">
										<td width="10%">现层次:</td>
										<td width="30%"><span>${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.classic.classicName }</span>
										</td>
										<td width="10%">原层次:</td>
										<td width="25%"><span>${stuChangeInfo.changeBeforeClassicName }</span>
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2D">
										<td width="10%">现层次:</td>
										<td width="30%"><gh:selectModel
												id="changeIntoSchool_classicId"
												name="changeIntoSchool_classicId" bindValue="resourceid"
												displayValue="classicName"
												modelClass="com.hnjk.edu.basedata.model.Classic"
												style="float:none;width:260px;"
												onchange="selectSchool_changeIntoSchool()" /><font color=red>*</font>
											<span id="classicTip" style="display: none;"><font
												style="background-color: red" color="white">请选择层次</span></td>
										<td width="10%">原层次:</td>
										<td width="25%"><gh:selectModel
												id="changeIntoSchool_changeBeforeClassic"
												name="changeIntoSchool_changeBeforeClassic"
												bindValue="resourceid" displayValue="classicName"
												modelClass="com.hnjk.edu.basedata.model.Classic"
												style="float:none;width:260px;" /><font color=red>*</font></td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>

									<tr id="E">
										<td width="10%">现形式:</td>
										<td width="30%"><span>${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeTeachingType)}</span>
										</td>
										<td width="10%">原形式:</td>
										<td width="25%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2E">
										<td width="10%">现形式:</td>
										<td width="30%"><gh:select classCss="required"
												id="changeIntoSchool_teachingType"
												name="changeIntoSchool_teachingType" filtrationStr="2,4,7"
												dictionaryCode="CodeTeachingType"
												onchange="selectSchool_changeIntoSchool()"
												style="width:260px;" /><font color=red>*</font> <span
											id="teachTypeTip" style="display: none;"><font
												style="background-color: red" color="white">请选择形式</span></td>
										<td width="10%">原形式:</td>
										<td width="25%"><span id="teachingtype">${ghfn:dictCode2Val('CodeTeachingType',stuChangeInfo.changeBeforeLearingStyle)}</span>
											<gh:select classCss="required"
												id="changeIntoSchool_changeBeforeLearingStyle"
												name="changeIntoSchool_changeBeforeLearingStyle"
												filtrationStr="2,4,7" dictionaryCode="CodeTeachingType"
												onchange="" style="width:260px;" /><font color=red>*</font>
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>

									<tr id="F">
										<td width="10%">现专业:</td>
										<td width="30%">
											${stuChangeInfo.changeTeachingGuidePlan.teachingPlan.major.majorName }
										</td>
										<td width="10%">原专业:</td>
										<td width="25%">${stuChangeInfo.changeBeforeMajorName }</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2F">
										<td width="10%">现专业:</td>
										<td width="30%"><select name="changeIntoSchool_majorId"
											id="changeIntoSchool_majorId" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="_selectClasses_changeIntoSchool(this)"><option
													value="">请选择</option></select><font color=red>*</font> <span
											id="majorTip" style="display: none;"><font
												style="background-color: red" color="white">请选择专业</span></td>
										<td width="10%">原专业:</td>
										<td width="25%">
											<%-- 
									<select  class="flexselect" id="changeIntoSchool_changeBeforeMajorId" 
										name="changeIntoSchool_changeBeforeMajorId" tabindex=1 style="width:260px;" class="required" >
										${majorOption}</select><font color=red>*</font>
								--%> 原专业编号： <input name="changeIntoSchool_changeBeforeMajorId"
											id="changeIntoSchool_changeBeforeMajorId" /><br>
											原专业名称： <input name="changeIntoSchool_changeBeforeMajorName"
											id="changeIntoSchool_changeBeforeMajorName" class="required" />
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>


									<tr id="G">
										<td width="10%">现班级:</td>
										<td width="30%">${stuChangeInfo.changeClass }</td>
										<td width="10%">入学信息：</td>
										<td width="25%">
											入学总分：${stuChangeInfo.studentInfo.totalPoint }<br /> 入学日期：<fmt:formatDate
												value="${stuChangeInfo.studentInfo.inDate }"
												pattern="yyyy-MM-dd" /><br />
											考生号：${stuChangeInfo.studentInfo.enrolleeCode}<br />
											准考证号：${stuChangeInfo.studentInfo.examCertificateNo }
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2G">
										<td width="10%">现班级:</td>
										<td width="30%"><select
											name="changeIntoSchool_changeClass"
											id="changeIntoSchool_changeClass" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="selectTeachPlan_changeIntoSchool(this)"><option
													value="">请选择</option></select><font color=red>*</font> <span
											id="classTip" style="display: none;"><font
												style="background-color: red" color="white">请选择班级</span></td>
										<td width="10%">入学信息：</td>
										<td width="25%">
											<table class="mytable">
												<tr>
													<td>入学总分：</td>
													<td><input name="changeIntoSchool_totalPoint"
														id="changeIntoSchool_totalPoint" class="digits" /></td>
												</tr>
												<tr>
													<td>入学日期：</td>
													<td><input type="text" name="changeIntoSchool_inDate"
														id="changeIntoSchool_inDate"
														onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
														class="required" /></td>
												</tr>
												<tr>
													<td>考生号：</td>
													<td><input name="changeIntoSchool_enrolleeCode"
														id="changeIntoSchool_enrolleeCode" class="number" /></td>
												</tr>
												<tr>
													<td>准考证号：</td>
													<td><input name="changeIntoSchool_examCertificateNo"
														id="changeIntoSchool_examCertificateNo" class="number" /></td>
												</tr>
											</table>
										</td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
									<tr style="display: none" name="2H">
										<td width="10%">现教学计划:</td>
										<td width="30%"><select
											name="changeIntoSchool_teachingPlan"
											id="changeIntoSchool_teachingPlan" size="1"
											style="float: none; width: 260px;" class="required"
											onchange="check()"><option value="">请选择</option></select><font
											color=red>*</font> <span id="teachingPlanTip"
											style="display: none;"><font
												style="background-color: red" color="white">请选择教学计划</span></td>
										<td width="10%"></td>
										<td width="25%"></td>
										<td width="10%"></td>
										<td width="14%"></td>
									</tr>
								</table>
							</div>


							<!-- 2 -->
							<div>
								<table class="form" id="changeStu">
									<tr id="I">
										<td style="width: 20%">姓名:</td>
										<td style="width: 30%">
											${stuChangeInfo.studentInfo.studentBaseInfo.name }</td>
										<td style="width: 20%">性别:</td>
										<td style="width: 30%">${ghfn:dictCode2Val('CodeSex',stuChangeInfo.studentInfo.studentBaseInfo.gender) }
										</td>
									</tr>
									<tr style="display: none" name="2I">
										<td style="width: 20%">姓名:</td>
										<td style="width: 30%"><input type="text" id="name"
											name="name" size="34"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.name } "
											class="required" maxlength="20" style="width: 28%"
											readonly="readonly" /></td>
										<td style="width: 20%">性别:</td>
										<td style="width: 30%"><input id="gender" name="gender"
											type="text" readonly="readonly"
											value="${ghfn:dictCode2Val('CodeSex',stuChangeInfo.studentInfo.studentBaseInfo.gender) }" />
										</td>
									</tr>

									<tr id="J">
										<td>证件类别:</td>
										<td>${ghfn:dictCode2Val('CodeCertType',stuChangeInfo.studentInfo.studentBaseInfo.certType) }
										</td>
										<td>证件号码:</td>
										<td><input type="hidden" id="isExstisCertNumStuinfo"
											name="isExstisCertNum" value="false" /> <input type="hidden"
											id="originalCertNumStuinfo" name="originalCertNum"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.certNum}" />
											${stuChangeInfo.studentInfo.studentBaseInfo.certNum}</td>
									</tr>
									<tr style="display: none" name="2J">
										<td>证件类别:</td>
										<td><gh:select id="certType" name="certType"
												dictionaryCode="CodeCertType"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.certType}"
												classCss="required" /> <font color='red'>* </font></td>
										<td>证件号码:</td>
										<td><input type="hidden" id="isExstisCertNumStuinfo"
											name="isExstisCertNum" value="false" /> <input type="hidden"
											id="originalCertNumStuinfo" name="originalCertNum"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.certNum}" />
											<input type="text" id="certNumStuinfo" name="certNum"
											size="34"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.certNum}"
											class="required" maxlength="18" /> <span class="tips">身份证末位X要大写</span>
										</td>
									</tr>

									<tr id="K">
										<td>联系地址:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.contactAddress }</td>
										<td>邮编:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.contactZipcode }</td>
									</tr>
									<tr style="display: none" name="2K">
										<td>联系地址:</td>
										<td><input name="contactAddress"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.contactAddress }"
											style="width: 48%"></td><%-- class="required" --%>
										<td>邮编:</td>
										<td><input name="contactZipcode"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.contactZipcode }"
											style="width: 48%" class="postcode"></td><%-- class="required" --%>
									</tr>

									<tr id="L">
										<td>联系电话:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.contactPhone }</td>
										<td>移动电话:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.mobile }</td>
									</tr>
									<tr style="display: none" name="2L">
										<td>联系电话:</td>
										<td><input name="contactPhone"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.contactPhone }"
											style="width: 48%" class="phone"></td>
										<td>移动电话:</td>
										<td><input name="mobile"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.mobile }"
											style="width: 48%" class="mobile"></td><%-- class="required" --%>
									</tr>

									<tr id="M">
										<td>邮箱:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.email }</td>
										<td>个人主页:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.homePage }</td>
									</tr>
									<tr style="display: none" name="2M">
										<td>邮箱:</td>
										<td><input name="email"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.email }"
											style="width: 48%" class="email"></td>
										<td>个人主页:</td>
										<td><input name="homePage"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.homePage }"
											style="width: 48%" class="url"></td>
									</tr>

									<tr id="N">
										<td>身高:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.height }</td>
										<td>血型:</td>
										<td>${ghfn:dictCode2Val('CodeBloodStyle',stuChangeInfo.studentInfo.studentBaseInfo.bloodType) }</td>
									</tr>
									<tr style="display: none" name="2N">
										<td>身高:</td>
										<td><input name="height"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.height }"
											style="width: 48%"></td>
										<td>血型:</td>
										<td><gh:select name="bloodType"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.bloodType }"
												dictionaryCode="CodeBloodStyle" style="width:50%" /></td>
									</tr>

									<tr id="O">
										<td>出生日期:</td>
										<td><fmt:formatDate
												value="${stuChangeInfo.studentInfo.studentBaseInfo.bornDay }"
												pattern="yyyy-MM-dd" /></td>
										</td>
										<td>出生地:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.bornAddress }</td>
									</tr>
									<tr style="display: none" name="2O">
										<td>出生日期:</td>
										<td><input type="text" name="bornDay" id="bornDay"
											readonly="readonly"
											value="<fmt:formatDate value="${stuChangeInfo.studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd"/>" />
										</td>
										</td>
										<td>出生地:</td>
										<td><input name="bornAddress"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.bornAddress }"
											style="width: 48%"></td>
									</tr>

									<tr id="P">
										<td>国籍:</td>
										<td>${ghfn:dictCode2Val('CodeCountry',stuChangeInfo.studentInfo.studentBaseInfo.country) }</td>
										<td>籍贯:</td>
										<td>${ stuChangeInfo.studentInfo.studentBaseInfo.homePlace}
											</div>
									</tr>
									<tr style="display: none" name="2P">
										<td>国籍:</td>
										<td><gh:select name="country"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.country }"
												dictionaryCode="CodeCountry" style="width:50%" /></td>
										<td>籍贯:</td>
										<td>
											<div id="ChinaArea1">
												<select id="homePlaceprovince" name="homePlaceprovince"
													tyle="width: 100px;"></select> <select id="homePlacecity"
													name="homePlacecity" style="width: 100px;"></select> <select
													id="homePlacecounty" name="homePlacecounty"
													style="width: 120px;">
												</select>
											</div>
									</tr>

									<tr id="P">
										<td>港澳侨代码:</td>
										<td>${ghfn:dictCode2Val('CodeGAQ',stuChangeInfo.studentInfo.studentBaseInfo.gaqCode) }</td>
										<td>民族:</td>
										<td>${ghfn:dictCode2Val('CodeNation',stuChangeInfo.studentInfo.studentBaseInfo.nation) }</td>
									</tr>
									<tr style="display: none" name="2P">
										<td>港澳侨代码:</td>
										<td><gh:select name="gaqCode"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.gaqCode }"
												dictionaryCode="CodeGAQ" style="width:50%" /></td>
										<td>民族:</td>
										<td><gh:select name="nation"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.nation }"
												dictionaryCode="CodeNation" style="width:50%"/></td><%-- class="required" --%>
									</tr>

									<tr id="Q">
										<td>身体健康状态:</td>
										<td>${ghfn:dictCode2Val('CodeHealth',stuChangeInfo.studentInfo.studentBaseInfo.health) }</td>
										<td>婚姻状况:</td>
										<td>${ghfn:dictCode2Val('CodeMarriage',stuChangeInfo.studentInfo.studentBaseInfo.marriage) }</td>
									</tr>
									<tr style="display: none" name="2Q">
										<td>身体健康状态:</td>
										<td><gh:select name="health"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.health }"
												dictionaryCode="CodeHealth" style="width:50%" /></td>
										<td>婚姻状况:</td>
										<td><gh:select name="marriage"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.marriage }"
												dictionaryCode="CodeMarriage" style="width:50%"/></td><%-- class="required" --%>
									</tr>

									<tr id="R">
										<td>政治面目:</td>
										<td>${ghfn:dictCode2Val('CodePolitics',stuChangeInfo.studentInfo.studentBaseInfo.politics) }</td>
										<td>宗教信仰:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.faith }</td>
									</tr>
									<tr style="display: none" name="2R">
										<td>政治面目:</td>
										<td><gh:select name="politics"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.politics }"
												dictionaryCode="CodePolitics" style="width:50%"/></td><%-- class="required" --%>
										<td>宗教信仰:</td>
										<td><input name="faith"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.faith }"
											style="width: 48%"></td>
									</tr>

									<tr id="S">
										<td>户口性质:</td>
										<td>${ghfn:dictCode2Val('CodeRegisteredResidenceKind',stuChangeInfo.studentInfo.studentBaseInfo.residenceKind) }</td>
										<td>户口所在地:</td>
										<td>
											${stuChangeInfo.studentInfo.studentBaseInfo.residence }</td>
									</tr>
									<tr style="display: none" name="2S">
										<td>户口性质:</td>
										<td><gh:select name="residenceKind"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.residenceKind }"
												dictionaryCode="CodeRegisteredResidenceKind"
												style="width:50%" /></td>
										<td>户口所在地:</td>
										<td>
											<div id="ChinaArea2">
												<select id="HouseholdRegisterationprovince"
													name="HouseholdRegisterationprovince" tyle="width: 100px;"></select>
												<select id="HouseholdRegisterationcity"
													name="HouseholdRegisterationcity" style="width: 100px;"></select>
												<select id="HouseholdRegisterationcounty"
													name="HouseholdRegisterationcounty" style="width: 120px;">
												</select>
											</div>
										</td>
									</tr>

									<tr id="T">
										<td>现住址:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.currenAddress }</td>
										<td>家庭住址:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.homeAddress }</td>
									</tr>
									<tr style="display: none" name="2T">
										<td>现住址:</td>
										<td><input name="currenAddress"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.currenAddress }"
											style="width: 48%"></td>
										<td>家庭住址:</td>
										<td><input name="homeAddress"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.homeAddress }"
											style="width: 48%"></td>
									</tr>

									<tr id="T">
										<td>家庭住址邮编:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.homezipCode }</td>
										<td>家庭电话:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.homePhone }</td>
									</tr>
									<tr style="display: none" name="2T">
										<td>家庭住址邮编:</td>
										<td><input name="homezipCode"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.homezipCode }"
											style="width: 48%" class="postcode"></td>
										<td>家庭电话:</td>
										<td><input name="homePhone"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.homePhone }"
											style="width: 48%" class="phone"></td>
									</tr>

									<tr id="U">
										<td>公司名称:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.officeName }</td>
										<td>公司电话:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.officePhone }</td>
									</tr>
									<tr style="display: none" name="2U">
										<td>公司名称:</td>
										<td><input name="officeName"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.officeName }"
											style="width: 48%"></td>
										<td>公司电话:</td>
										<td><input name="officePhone"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.officePhone }"
											style="width: 48%" class="phone"></td>
									</tr>

									<tr id="V">
										<td>职务职称:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.title }</td>
										<td>特长:</td>
										<td>${stuChangeInfo.studentInfo.studentBaseInfo.specialization }</td>
									</tr>
									<tr style="display: none" name="2V">
										<td>职务职称:</td>
										<td><input name="title"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.title }"
											style="width: 48%"></td>
										<td>特长:</td>
										<td><input name="specialization"
											value="${stuChangeInfo.studentInfo.studentBaseInfo.specialization }"
											style="width: 48%"></td>
									</tr>

									<tr id="W">
										<td>备注:</td>
										<td colspan="3">${stuChangeInfo.studentInfo.studentBaseInfo.memo }</td>
									</tr>
									<tr style="display: none" name="2W">
										<td>备注:</td>
										<td colspan="3"><textarea rows="3" cols=""
												name="basememo" style="width: 50%">${stuChangeInfo.studentInfo.studentBaseInfo.memo }</textarea>
										</td>
									</tr>

								</table>
							</div>

						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>

				<div class="formBar">
					<ul>
						<c:if test="${ empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">保存</button>
									</div>
								</div></li>
						</c:if>
						<c:if test="${not empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button"
											onclick="changeIntoSchoolApplyOnceMore()">继续申请校级转入</button>
									</div>
								</div></li>
						</c:if>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>

			</form>
		</div>
	</div>
</body>
</html>