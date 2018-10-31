<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改学籍信息</title>
<script type="text/javascript">
	jQuery(document).ready(function(){
		//一打开页面是检查学生的证件类型，判断是否锁定生日日期的输入和性别的输入
		var certType =$("#changeStu #certType").val();
		if(certType!='idcard'){
	   	//若不是身份证，解除生日和 性别的锁定
   			$("#changeStu #gender").attr("readonly","");
   			$("#changeStu #bornDay").attr("readonly","");
		}
		//证件号码变更时，判断证件类型，为身份证时才自动生成生日日期与性别
		//$("#changeStu #certNum").bind("change",function(){
		  $("#changeStu input[name='certNum']").bind("change",function(){
			//先判断是不是身份证
	   		var certType =$("#certType").val();
	   		if(certType=='idcard'){
		   		//是身份证时，生成生日性别等信息
				//var id = $("#changeStu #certNum").val() ;
		   		var id = $("#changeStu input[name='certNum']").val() ;
		   		showBirthday(id);
	   		}
			});
		//证件类型变更时，判断证件类型，改变生日与性别输入框的可写属性
		//$("#changeStu #certType").bind("change",function(){	
		  $("#certType").bind("change",function(){	 
			//先判断是不是身份证
	   		var certType =$("#certType").val();
			if(certType=='idcard'){
		   		//是身份证时，锁定输入框
	   			/*
		   		$("#changeStu #gender").attr("readonly","readonly");
	   			$("#changeStu #bornDay").attr("readonly","readonly");
	   			$("#changeStu #gender").attr("class","textInput  readonly");
	   			$("#changeStu #bornDay").attr("class","textInput readonly");
	   			*/
				$("#changeStu input[name='gender']").attr("readonly","readonly");
	   			$("#changeStu input[name='bornDay']").attr("readonly","readonly");
	   			$("#changeStu input[name='gender']").attr("class","textInput  readonly");
	   			$("#changeStu input[name='bornDay']").attr("class","textInput readonly");
	   		}else{
	   			//是其他证件时，解除锁定输入框
	   			/*
	   			$("#changeStu #gender").attr("readonly","");
	   			$("#changeStu #bornDay").attr("readonly","");
	   			$("#changeStu #gender").attr("class","textInput");
	   			$("#changeStu #bornDay").attr("class","textInput");
	   			*/
	   			$("#changeStu input[name='gender']").attr("readonly","");
	   			$("#changeStu input[name='bornDay']").attr("readonly","");
	   			$("#changeStu input[name='gender']").attr("class","textInput");
	   			$("#changeStu input[name='bornDay']").attr("class","textInput");
	   		}
			});
		  
		  var current=$("#current").attr("value");				
			if(current!='administrator'){
				$("#changeStu input[name='name']").attr("readonly","readonly");
				$("#changeStu input[name='gender']").attr("readonly","readonly");
				$("#changeStu input[name='certNum']").attr("readonly","readonly");
				$("#changeStu select[name='politics']").attr("disabled","disabled");
				$("#changeStu select[name='nation']").attr("disabled","disabled");
			}
			
		
		//籍贯
				var sheng=$("#sheng").val();
			if(sheng==''){
				$("#ChinaArea1").jChinaArea({
				//	 aspnet:true,
					 s1:"请选择",
					 s2:"请选择",
					 s3:"请选择"
					 	 
			 	});
			}else{
				$("#ChinaArea1").jChinaArea({
					 s1:"${homePlaceProvince}",//默认选中的省名
					 s2:"${homePlaceCity}",//默认选中的市名
					 s3:"${homePlaceDistrict}"//默认选中的县区名
				});
			}
		//户口
		var HouseholdRegisterationprovince=$("#hukou").val();
		if(HouseholdRegisterationprovince==''||HouseholdRegisterationprovince==null){
			$("#ChinaArea2").jChinaArea({
			//	 aspnet:true,
				 s1:"请选择",
				 s2:"请选择",
				 s3:"请选择"
				 	 
		 	});
		}else{

			$("#ChinaArea2").jChinaArea({
				//	 aspnet:true,
					 s1:"${HouseholdRegisterationProvince}",//默认选中的省名
					 s2:"${HouseholdRegisterationCity}",//默认选中的市名
					 s3:"${HouseholdRegisterationDistrict}"//默认选中的县区名
			 });
		}
		
		var studentStatusSet= '${stuStatusSet}';
		var statusRes= '${stuStatusRes}';
		orgStuStatus("#studentInfoForm #studentStatus",studentStatusSet,statusRes,"a11,b11");
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

	function saveChangeForm(form){
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
   		//var id = $("#changeStu #certNum").val() ;
		var id = $("#changeStu input[name='certNum']").val() ;
		if(_idCardValidate(id)){ 
			//$("#changeStu #certNum").css({"backgroundColor":"#fff"});
			$("#changeStu input[name='certNum']").css({"backgroundColor":"#fff"});
        }else{
      	 	alertMsg.warn("身份证号码不合法!");
      	 	//$("#changeStu #certNum").css({"backgroundColor":"#ffc8c8"});
       	 	$("#changeStu input[name='certNum']").css({"backgroundColor":"#ffc8c8"});
       		return false;
        }
		showBirthday(id);
		return validateCallback(form);
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
   		 }  else if(18==val.length){ //18位身份证号码
   		    birthdayValue=val.charAt(6)+val.charAt(7)+val.charAt(8)+val.charAt(9)+'-'+val.charAt(10)+val.charAt(11) +'-'+val.charAt(12)+val.charAt(13);
   		    if(parseInt(val.charAt(16)/2)*2!=val.charAt(16)){
   		    	gender.val("男");
   		    }else{
   		    	gender.val("女");
   		    }
   		    if(val.charAt(17)!=IDCard(val))  {
   		     //$("#changeStu #certNum").css({"backgroundColor":"#ffc8c8"});
   		     $("#changeStu input[name='certNum']").css({"backgroundColor":"#ffc8c8"});
   		  	 alertMsg.warn("身份证号码不合法!");
   		     gender.val("");//清除
   		     bornDay.val("");
   		    }else{
   		    	//$("#changeStu #certNum").css({"backgroundColor":"#fff"});
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
 	
  	jQuery(document).ready(function(){
		 //初始化学生照片上传			
	      //alert("rootUrl:"+'${rootUrl}');
		 $("#changeStu #uploadify_images_photoPath").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html',
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 40960 , //限制单个文件上传大小40K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            'onInit': function () {       
		        	//载入时触发，将flash设置到最小             
		        	$("#fileQueue").hide();
		        },			        
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");
	              	$('#changeStu #student_photoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #photoPathId').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
    	 });
     	 var student_photoPath = '${stu.studentInfo.studentBaseInfo.photoPath}';
     	 if(student_photoPath != ''){	   
   		$('#student_photoPath').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.photoPath}'+"?"+Math.random()*1000);
   		$('#photoPathId').val('${stu.studentInfo.studentBaseInfo.photoPath}');
    	 }	   
     	 //初始化学生身份证照片上传
    	 $("#changeStu #uploadify_images_certPhotoPath").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 127200,//61440, //限制单个文件上传大小60K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #certPhotoPathId').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
     	 });
     	 var student_certPhotoPath = '${stu.studentInfo.studentBaseInfo.certPhotoPath}';
    	 if(student_certPhotoPath != ''){
   		$('#changeStu #student_certPhotoPath').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPath}'+"?"+Math.random()*1000);
   		$('#certPhotoPathId').val('${stu.studentInfo.studentBaseInfo.certPhotoPath}');
    	 }
    	 //初始化学生身份证照片上传(反面)
    	 $("#changeStu #uploadify_images_certPhotoPath_reverse").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 127200,//61440, //限制单个文件上传大小60K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#changeStu #student_certPhotoPathReverse').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #certPhotoPathIdReverse').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
     	 });
     	 var student_certPhotoPathReverse = '${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}';
    	 if(student_certPhotoPathReverse != ''){
   		$('#changeStu #student_certPhotoPathReverse').attr('src','${rootUrl}common/students/${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}'+"?"+Math.random()*1000);
   		$('#certPhotoPathIdReverse').val('${stu.studentInfo.studentBaseInfo.certPhotoPathReverse}');
    	 }
    	 
    	 //初始化学生其他证件照片上传
    	 $("#changeStu #uploadify_images_eduPhotoPath").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 102400, //限制单个文件上传大小100K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#changeStu #student_eduPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #eduPhotoPathId').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
    	 });
    	 
    	//户口册上传
    	 $("#changeStu #uploadify_images_bookletPhotoPath").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 102400, //限制单个文件上传大小100k 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#changeStu #student_bookletPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #bookletPhotoPathId').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
    	 });
    	
    	//其他图片上传
    	 $("#changeStu #uploadify_images_otherPhotoPath").uploadify({ 
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 102400, //限制单个文件上传大小100K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#changeStu #student_otherPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#changeStu #otherPhotoPathId').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#changeStu #uploadify').uploadifyClearQueue(); //清空上传队列
				}
    	 });
    
  	});
  	function Canceldisabled(){
  		$("#changeStu select[name='politics']").attr("disabled","");
  		$("#changeStu select[name='nation']").attr("disabled","");
  	}
 	
</script>
</head>
<body>
	<h2 class="contentTitle">修改学生学籍信息</h2>
	<div class="page">
		<div class="pageContent">
			<c:if test="${act ne 'view' }">
				<!--  saveChangeForm(this); -->
				<form id="changeStu" method="post"
					action="${baseUrl}/edu3/register/studentinfo/savestu.html?editImg=Y"
					onsubmit="return saveChangeForm(this);" class="pageForm">

					<input type="hidden" name="resourceid"
						value="${studentInfo.resourceid }"> <input type="hidden"
						name="enterSchool" value="${studentInfo.enterSchool }"> <input
						type="hidden" id="homePlaceAll" name="homePlaceAll" value="" /> <input
						type="hidden" id="residenceAll" name="residenceAll" value="" /> <input
						type="hidden" id="current" value="${current }">
					<div class="pageFormContent" layoutH="97">
						<div class="tabs">
							<div class="tabsHeader">
								<div class="tabsHeaderContent">
									<ul>
										<li><a href="#"><span>学籍信息</span></a></li>
										<li class="#"><a href="#"><span>基本信息</span></a></li>
										<li><a href="javascript:void(0)"><span>上传证件信息</span></a></li>
										<c:if test="${studentInfo.classic.classicName eq '大专起点本科'}">
											<li class="#"><a href="#"><span>专科资格</span></a></li>
										</c:if>
									</ul>
								</div>
							</div>
							<div class="tabsContent" id="studentInfoForm"
								style="height: 100%;">
								<!-- 1 -->
								<div>
									<h5 class="tips" style="width: 96%">说明：更改学生专业、层次、教学站等，请走学籍异动审批流程.</h5>
									<table class="form">
										<tr>
											<td style="width: 20%">学生号:</td>
											<td style="width: 30%">${studentInfo.studyNo }</td>
											<td style="width: 20%">考生号:</td>
											<td style="width: 30%">${studentInfo.enrolleeCode }</td>
										</tr>
										<tr>
											<td>学生姓名:</td>
											<td>${studentInfo.studentName}<!--  ${studentInfo.studentName }--></td>
											<td>注册号:</td>
											<td>${studentInfo.registorNo }</td>
										</tr>
										<tr>
											<td>年级:</td>
											<td>${studentInfo.grade.gradeName }<!--<gh:selectModel name="grade" bindValue="resourceid" displayValue="gradeName" value="${studentInfo.grade.resourceid }"
											modelClass="com.hnjk.edu.basedata.model.Grade" style="width:50%"/>
									--></td>
											<td>层次:</td>
											<td>${studentInfo.classic.classicName }<!--<gh:selectModel name="classic" bindValue="resourceid" displayValue="classicName" value="${studentInfo.classic.resourceid }"
											modelClass="com.hnjk.edu.basedata.model.Classic" style="width:50%"/>
									--></td>
										</tr>
										<tr>
											<td>入学方式:</td>
											<td>${ghfn:dictCode2Val('CodeEnterSchool',studentInfo.enterSchool) }</td>
											<td>学生系统账号:</td>
											<td>${studentInfo.sysUser.username }</td>
										</tr>
										<tr>
											<td>进修性质:</td>
											<td><gh:select name="attendAdvancedStudies"
													value="${studentInfo.attendAdvancedStudies }"
													dictionaryCode="CodeAttendAdvancedStudies"
													style="width:50%" /></td>
											<td>学习形式:</td>
											<td>${ghfn:dictCode2Val('CodeLearningStyle',studentInfo.learningStyle) }
												<!--<gh:select name="learningStyle" value="${studentInfo.learningStyle }" dictionaryCode="CodeLearningStyle" style="width:50%" />
									-->
											</td>
										</tr>

										<tr>
											<td>教学站:</td>
											<td>${studentInfo.branchSchool.unitName } <!-- 
										<gh:selectModel name="branchSchool" bindValue="resourceid" displayValue="unitName" style="width:50%"
											modelClass="com.hnjk.security.model.OrgUnit" value="${studentInfo.branchSchool.resourceid }"  condition=" unitType='brSchool' and  isDeleted = 0 and status = 'normal' " orderBy=" unitName asc "/>
									 -->
											</td>
											<td>专业:</td>
											<td><gh:selectModel name="major" bindValue="resourceid"
													displayValue="majorName"
													value="${studentInfo.major.resourceid }"
													modelClass="com.hnjk.edu.basedata.model.Major"
													disabled="disabled" style="width:50%" /></td>
										</tr>
										<tr>
											<td>就读方式:</td>
											<td><gh:select name="studyInSchool"
													value="${studentInfo.studyInSchool }"
													dictionaryCode="CodeStudyInSchool" style="width:50%" /></td>
											<td>学习类别:</td>
											<td><gh:select dictionaryCode="CodeStudentKind"
													value="${studentInfo.studentKind }" name="studentKind"
													style="width:50%" /></td>
										</tr>
										<tr>
											<td>学籍状态:</td>
											<td>
												<!--<gh:select name="studentStatus" value="${studentInfo.studentStatus }" dictionaryCode="CodeStudentStatus" style="width:50%" />
									--> <select id="studentStatus" name="studentStatus"
												style="width: 50%">
											</select>
											</td>
											<td>在学状态:</td>
											<td><gh:select name="learingStatus"
													value="${studentInfo.learingStatus }"
													dictionaryCode="CodeLearingStatus" style="width:50%" /></td>
										</tr>
										<tr>
											<td>入学资格审核:</td>
											<td><gh:select name="enterAuditStatus"
													dictionaryCode="CodeAuditStatus"
													value="${studentInfo.enterAuditStatus}" style="width:50%" />
											</td>
											<td>入学日期</td>
											<td><input type="text" id="stuIndate" name="stuIndate"
												class="required"
												value='<fmt:formatDate value="${studentInfo.inDate  }" pattern="yyyy-MM-dd"/>'
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
										</tr>
										<tr>
											<td>特殊学生(是否跟读):</td>
											<td><gh:select name="isStudyFollow"
													dictionaryCode="yesOrNo"
													value="${studentInfo.isStudyFollow}" style="width:50%" />
											</td>
											<td></td>
											<td></td>
										</tr>
										<!--
								<tr>
									<td>入学性质:</td>
									<td><gh:select name="enterSchool" value="${studentInfo.enterSchool }" dictionaryCode="CodeEnterSchool" style="width:50%" /></td>
									<td>考试预约状态:</td>
									<td><gh:select name="examOrderStatus" value="${studentInfo.examOrderStatus }" dictionaryCode="CodeExamSubscribeState" style="width:50%" /></td>
								</tr>
								<tr>
									<td>是否提前修读:</td>
									<td><gh:select name="isAhead" value="${studentInfo.isAhead }" dictionaryCode="yesOrNo" style="width:50%" /></td>
									<td>是否预约毕业论文:</td>
									<td><gh:select name="isOrderSubject" value="${studentInfo.isOrderSubject }" dictionaryCode="yesOrNo" style="width:50%" /></td>
								</tr>
								<tr>
									<td>是否申请学位:</td>
									<td><gh:select name="isApplyGraduate" value="${studentInfo.isApplyGraduate }" dictionaryCode="yesOrNo" style="width:50%" /></td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								-->
										<tr>

											<c:if test="${schoolCode eq '10560' || schoolCode eq '10601' || schoolCode eq '12962'}">
												<td>自我鉴定:</td>
												<td colspan="3"><textarea rows="8" cols=""
														name="selfAssessment" style="width: 82%"
														required="required">${studentInfo.selfAssessment }</textarea>
												</td>
											</c:if>
											<%-- <c:if test="${schoolCode ne '10560'}">
										<td colspan="3"><textarea rows="8" cols="" name="selfAssessment" style="width:82%">${studentInfo.selfAssessment }</textarea> </td>
									</c:if> --%>
										</tr>
										<tr>
											<td>备注:</td>
											<td colspan="3"><textarea rows="3" cols="" name="memo"
													style="width: 50%">${studentInfo.memo }</textarea></td>
										</tr>
									</table>

								</div>
								<!-- 2 -->
								<div>
									<table class="form">
										<tr>
											<td style="width: 20%">姓名:</td>
											<td colspan="3" style="width: 30%"><input type="text"
												id="name" name="name" size="34"
												value="${studentInfo.studentBaseInfo.name }"
												class="required" maxlength="20" style="width: 30%" />
										</tr>
										<tr>
											<td style="width: 20%">曾用名:</td>
											<td style="width: 30%"><input type="text" id="nameUsed"
												name="nameUsed" size="34"
												value="${studentInfo.studentBaseInfo.nameUsed }"
												maxlength="20" style="width: 16%" /> <!--${studentInfo.studentBaseInfo.name }  --></td>
											<td style="width: 20%">性别:</td>
											<td style="width: 30%"><input id="gender" name="gender"
												type="text" readonly="readonly"
												value="${ghfn:dictCode2Val('CodeSex',studentInfo.studentBaseInfo.gender) }" /></td>
										</tr>
										<tr>
											<td>证件类别:</td>
											<td><gh:select id="certType" name="certType"
													dictionaryCode="CodeCertType"
													value="${studentInfo.studentBaseInfo.certType}"
													classCss="required" /> <font color='red'>* </font> <!-- ${ghfn:dictCode2Val("CodeCertType",studentInfo.studentBaseInfo.certType) } --></td>
											<td>证件号码:</td>
											<td><input type="hidden" id="isExstisCertNum"
												name="isExstisCertNum" value="false" /> <input type="hidden"
												id="originalCertNum" name="originalCertNum"
												value="${studentInfo.studentBaseInfo.certNum}" /> <input
												type="text" id="certNum" name="certNum" size="34"
												value="${studentInfo.studentBaseInfo.certNum}"
												class="required" maxlength="18" /> <span class="tips">身份证末位X要大写</span>
												<!--${studentInfo.studentBaseInfo.certNum }  --></td>
										</tr>
										<tr>
											<td>联系地址:</td>
											<td><input name="contactAddress"
												value="${studentInfo.studentBaseInfo.contactAddress }"
												style="width: 48%" class="required"></td>
											<td>联系邮编:</td>
											<td><input name="contactZipcode"
												value="${studentInfo.studentBaseInfo.contactZipcode }"
												style="width: 48%" class="postcode required"></td>
										</tr>
										<tr>
											<td>联系电话:</td>
											<td><input name="contactPhone"
												value="${studentInfo.studentBaseInfo.contactPhone }"
												style="width: 48%" class="phone"></td>
											<td>移动电话:</td>
											<td><input name="mobile"
												value="${studentInfo.studentBaseInfo.mobile }"
												style="width: 48%" class="mobile required"></td>
										</tr>
										<tr>
											<td>邮件:</td>
											<td><input name="email"
												value="${studentInfo.studentBaseInfo.email }"
												style="width: 48%" class="email"></td>
											<td>个人主页:</td>
											<td><input name="homePage"
												value="${stuChangeInfo.studentInfo.studentBaseInfo.homePage }"
												style="width: 48%" class="url"></td>
										</tr>
										<tr>
											<td>身高：</td>
											<td><input name="height" type="text"
												value="${studentInfo.studentBaseInfo.height}"
												style="width: 50%"
												onkeyup="value=value.replace(/[^\d]/g,'') "
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
												<span class="tips">单位：cm,请填写整数</span></td>
											<!--<td>身高:</td>
									<td><input name="height" value="${studentInfo.studentBaseInfo.height }" style="width:48%"></td>
									-->
											<td>血型:</td>
											<td><gh:select name="bloodType"
													value="${studentInfo.studentBaseInfo.bloodType }"
													dictionaryCode="CodeBloodStyle" style="width:50%" /></td>
										</tr>
										<tr>
											<td>出生日期:</td>
											<td><input type="text" name="bornDay" id="bornDay"
												readonly="readonly"
												value="<fmt:formatDate value="${studentInfo.studentBaseInfo.bornDay }" pattern="yyyy-MM-dd"/>" /></td>
											<td>出生地:</td>
											<td><input name="bornAddress"
												value="${studentInfo.studentBaseInfo.bornAddress }"
												style="width: 48%"></td>
										</tr>
										<tr>
											<td>国籍:</td>
											<td><gh:select name="country"
													value="${studentInfo.studentBaseInfo.country }"
													dictionaryCode="CodeCountry" style="width:50%" /></td>
											<td>籍贯:</td>
											<td>
												<div id="ChinaArea1">
													<select id="homePlaceprovince" name="homePlaceprovince"
														tyle="width: 100px;"></select> <select id="homePlacecity"
														name="homePlacecity" style="width: 100px;"></select> <select
														id="homePlacecounty" name="homePlacecounty"
														style="width: 120px;">
													</select> <input type="hidden" id="sheng"
														value="${homePlaceProvince}">
												</div> <!--  <input name="homePlace" value="${studentInfo.studentBaseInfo.homePlace }" style="width:48%">-->
											</td>
										</tr>
										<tr>
											<td>港澳侨代码:</td>
											<td><gh:select name="gaqCode"
													value="${studentInfo.studentBaseInfo.gaqCode }"
													dictionaryCode="CodeGAQ" style="width:50%" /></td>
											<td>民族:</td>
											<td><gh:select name="nation"
													value="${studentInfo.studentBaseInfo.nation }"
													dictionaryCode="CodeNation" style="width:50%"
													classCss="required" /><font color='red'>* </font></td>
										</tr>
										<tr>
											<td>身体健康状态:</td>
											<td><gh:select name="health"
													value="${studentInfo.studentBaseInfo.health }"
													dictionaryCode="CodeHealth" style="width:50%" /></td>
											<td>婚姻状况:</td>
											<td><gh:select name="marriage"
													value="${studentInfo.studentBaseInfo.marriage }"
													dictionaryCode="CodeMarriage" style="width:50%"
													classCss="required" /><font color='red'>* </font></td>
										</tr>
										<tr>
											<td>政治面貌:</td>
											<td><gh:select name="politics"
													value="${studentInfo.studentBaseInfo.politics }"
													dictionaryCode="CodePolitics" style="width:50%"
													classCss="required" /><font color='red'>* </font></td>
											<td>宗教信仰:</td>
											<td><input name="faith"
												value="${studentInfo.studentBaseInfo.faith }"
												style="width: 48%"></td>
										</tr>
										<tr>
											<td>户口性质:</td>
											<td><gh:select name="residenceKind"
													value="${studentInfo.studentBaseInfo.residenceKind }"
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
													</select> <input type="hidden" id="hukou"
														value="${HouseholdRegisterationProvince}">
												</div> <!-- <input name="residence" value="${studentInfo.studentBaseInfo.residence }" style="width:48%"> -->
											</td>
										</tr>
										<tr>
											<td>现住址:</td>
											<td><input name="currenAddress"
												value="${studentInfo.studentBaseInfo.currenAddress }"
												style="width: 48%"></td>
											<td>家庭住址:</td>
											<td><input name="homeAddress"
												value="${studentInfo.studentBaseInfo.homeAddress }"
												style="width: 48%"></td>
										</tr>
										<tr>
											<td>家庭住址邮编:</td>
											<td><input name="homezipCode"
												value="${studentInfo.studentBaseInfo.homezipCode }"
												style="width: 48%"></td>
											<td>家庭电话:</td>
											<td><input name="homePhone"
												value="${studentInfo.studentBaseInfo.homePhone }"
												style="width: 48%" class="phone"></td>
										</tr>
										<tr>
											<td>公司名称:</td>
											<td><input name="officeName"
												value="${studentInfo.studentBaseInfo.officeName }"
												style="width: 48%"></td>
											<td>公司电话:</td>
											<td><input name="officePhone"
												value="${studentInfo.studentBaseInfo.officePhone }"
												style="width: 48%" class="phone"></td>
										</tr>
										<%--   	<tr>
									<td>公司地址:</td>
									<td><input name="officeAddress" value="${studentInfo.studentBaseInfo.officeAddress }" style="width:48%"></td>
									<td>公司邮编:</td>
									<td><input name="officeZipcode" value="${studentInfo.studentBaseInfo.officeZipcode }" style="width:48%"></td>
								</tr>--%>
										<tr>
											<td>职务职称:</td>
											<td><input name="title"
												value="${studentInfo.studentBaseInfo.title }"
												style="width: 48%"></td>
											<td>特长:</td>
											<td><input name="specialization"
												value="${studentInfo.studentBaseInfo.specialization }"
												style="width: 48%"></td>
										</tr>
										<tr>
											<td>备注:</td>
											<td colspan="3"><textarea rows="3" cols=""
													name="basememo" style="width: 50%">${studentInfo.studentBaseInfo.memo }</textarea>
											</td>
										</tr>
									</table>

								</div>
								<!-- 3 -->

								<div>
									<h5 class="tips" style="width: 96%">
										<c:if test="${studentInfo.auditResults eq '0'}">
							图片暂未审核
						</c:if>
										<c:if test="${studentInfo.auditResults eq '1'}">
							图片已审核通过
						</c:if>
										<c:if test="${studentInfo.auditResults eq '2'}">
							图片审核不通过
						</c:if>
										<c:if test="${studentInfo.auditResults eq '3'}">
							图片未提交
						</c:if>
									</h5>
									<table class="form">
										<tr>
											<td width="30%">选择文件</td>
											<td width="20%">选择文件</td>
											<td>图片预览</td>
										</tr>
										<tr>
											<td width="30%">选择照片：<br /> 1、背景要求：统一为蓝色；<br />
												2、服装：白色或浅色系；<br /> 3、图片尺寸（像素）宽：150、高：210；<br />
												4、大小：≤40K、格式：jpg
											</td>
											<td width="20%"><input id="uploadify_images_photoPath"
												type="file" /></td>
											<td><c:if
													test="${not empty studentInfo.studentBaseInfo.photoPath}">
													<img id="student_photoPath"
														src="${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}"
														width="90" height="126" />
												</c:if> <c:if
													test="${empty studentInfo.studentBaseInfo.photoPath }">
													<img id="student_photoPath"
														src="${baseUrl}/themes/default/images/img_preview.png"
														width="90" height="126" />
												</c:if> <input type="hidden" name="photoPath" id="photoPathId"
												value="${studentInfo.studentBaseInfo.photoPath}" /></td>
										</tr>
										<tr>
											<td width="30%">选择身份证复印扫描件：<br /> 1、中华人民共和国第二（一）代居民身份证；<br />
												2、大小：≤120k,格式 :jpg
											</td>
											<td width="20%">
												<ul>
													<li>注意：请使用采光均匀的相片或扫描件</li>
													<li>正面</li>
													<li><input id="uploadify_images_certPhotoPath"
														type="file" /></li>
												</ul>
												<ul>
													<li>反面</li>
													<li><input id="uploadify_images_certPhotoPath_reverse"
														type="file" /></li>
												</ul>
											</td>
											<td><c:if
													test="${not empty studentInfo.studentBaseInfo.certPhotoPath }">
													<img id="student_certPhotoPath"
														src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPath}"
														width="240" height="160" />
												</c:if> <c:if
													test="${empty studentInfo.studentBaseInfo.certPhotoPath }">
													<img id="student_certPhotoPath"
														src="${baseUrl}/themes/default/images/img_preview.png"
														width="240" height="160" />
												</c:if> <c:if
													test="${not empty studentInfo.studentBaseInfo.certPhotoPathReverse }">
													<img id="student_certPhotoPathReverse"
														src="${rootUrl}common/students/${studentInfo.studentBaseInfo.certPhotoPathReverse}"
														width="240" height="160" />
												</c:if> <c:if
													test="${empty studentInfo.studentBaseInfo.certPhotoPath }">
													<img id="student_certPhotoPathReverse"
														src="${baseUrl}/themes/default/images/img_preview.png"
														width="240" height="160" />
												</c:if> <input type="hidden" id="certPhotoPathId"
												name="certPhotoPath"
												value="${studentInfo.studentBaseInfo.certPhotoPath }" /> <input
												type="hidden" id="certPhotoPathIdReverse"
												name="certPhotoPathReverse"
												value="${studentInfo.studentBaseInfo.certPhotoPathReverse }" />
											</td>
										</tr>
										<tr>
											<td width="30%">选择毕业证复印扫描件：<br /> 1、普通大中专院校证件；<br />
												2、大小：≤100k,格式 :jpg
											</td>
											<td width="20%"><input
												id="uploadify_images_eduPhotoPath" type="file" /></td>
											<td><c:if
													test="${not empty studentInfo.studentBaseInfo.eduPhotoPath }">
													<img id="student_eduPhotoPath"
														src="${rootUrl}common/students/${studentInfo.studentBaseInfo.eduPhotoPath}"
														width="150" height="100" />
												</c:if> <c:if
													test="${empty studentInfo.studentBaseInfo.eduPhotoPath }">
													<img id="student_eduPhotoPath"
														src="${baseUrl }/themes/default/images/img_preview.png"
														width="150" height="100" />
												</c:if> <input type="hidden" name="eduPhotoPath"
												id="eduPhotoPathId"
												value="${studentInfo.studentBaseInfo.eduPhotoPath }" /></td>
										</tr>
										<tr>
											<td width="30%">选择户口簿复印扫描件：<br /> 1、户口簿证件；<br />
												2、大小：≤100k,格式 :jpg
											</td>
											<td width="20%"><input
												id="uploadify_images_bookletPhotoPath" type="file" /></td>
											<td><c:if
													test="${not empty studentInfo.bookletPhotoPath }">
													<img id="student_bookletPhotoPath"
														src="${rootUrl}common/students/${studentInfo.bookletPhotoPath}"
														width="150" height="100" />
												</c:if> <c:if test="${empty studentInfo.bookletPhotoPath }">
													<img id="student_bookletPhotoPath"
														src="${baseUrl }/themes/default/images/img_preview.png"
														width="150" height="100" />
												</c:if> <input type="hidden" name="bookletPhotoPath"
												id="bookletPhotoPathId"
												value="${studentInfo.bookletPhotoPath }" /></td>
										</tr>
										<tr>
											<td width="30%">其他：<br /> 1、其他证件；<br /> 2、大小：≤100k,格式
												:jpg
											</td>
											<td width="20%"><input
												id="uploadify_images_otherPhotoPath" type="file" /></td>
											<td><c:if
													test="${not empty studentInfo.otherPhotoPath }">
													<img id="student_otherPhotoPath"
														src="${rootUrl}common/students/${studentInfo.otherPhotoPath}"
														width="150" height="100" />
												</c:if> <c:if test="${empty studentInfo.otherPhotoPath }">
													<img id="student_otherPhotoPath"
														src="${baseUrl }/themes/default/images/img_preview.png"
														width="150" height="100" />
												</c:if> <input type="hidden" name="otherPhotoPath"
												id="otherPhotoPathId" value="${studentInfo.otherPhotoPath }" />
											</td>
										</tr>
									</table>
									<span><font color="red">说明：点击上传附件后，需要保存表单.</font> </span>
								</div>
								<!-- 4 -->
								<div>
									<table class="form"
										<c:if test="${studentInfo.classic.classicName ne '大专起点本科'}">style="display:none;" </c:if>>
										<tr id="gSchoolInfoTr">
											<td width="12%">毕业院校:</td>
											<td width="38%"><input type="text" id="gSchoolName"
												name="gSchoolName" size="34"
												value="${studentInfo.graduateSchool }" /></td>
											<td width="12%">毕业学校代码:</td>
											<td width="38%"><input type="text" id="gSchoolCode"
												name="gSchoolCode" size="34"
												value="${studentInfo.graduateSchoolCode }" maxlength="5" />
												(毕业证书号的前五位)</td>
										</tr>
										<tr id="graduateInfoTr">
											<td width="12%">毕业证书号:</td>
											<td width="38%"><input type="text" id="gId" name="gId"
												size="34" value="${studentInfo.graduateId }" /></td>
											<td width="12%">毕业专业:</td>
											<td width="38%"><input type="text" id="gMajor"
												name="gMajor" size="34"
												value="${studentInfo.graduateMajor }" /></td>
										</tr>
										<tr>
											<td width="12%">毕业日期:</td>
											<td width="38%"><input id="gDate" type="text"
												name="gDate" size="34" class="Wdate"
												onfocus="WdatePicker({isShowWeek:true})"
												value="${studentInfo.graduateDate}" /></td>
											<td width="12%"></td>
											<td width="38%"></td>
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
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit" onclick="Canceldisabled()">保存</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close"
											onclick="navTab.closeCurrentTab();">取消</button>
									</div>
								</div></li>
						</ul>
					</div>
				</form>
			</c:if>
		</div>
	</div>

</body>

</html>