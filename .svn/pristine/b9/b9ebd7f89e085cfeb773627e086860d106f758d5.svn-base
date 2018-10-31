<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报名表单</title>

</head>
<body>
	<script type="text/javascript">	 
		  
jQuery(document).ready(function(){
	
	     var _hasPublishedPlan = "${hasPublishedPlan}";//是否有已经发布的招生计划
	     var _isBrSchool = "${isBrSchool}";//是否为教学站
	     
	     /**~~~~~~~~~~~~~1.检查是否有开放的招生计划~~~~~~~~~~~~~~~*/
		 if(_hasPublishedPlan=='false'){
		    $("#_enrolleeinfo_form").hide();
		    alertMsg.warn('没有开放的招生计划！');
		   return false;
	 	 }
		  
	     /**~~~~~~~~~~~~~~~2.初始化照片上传组件~~~~~~~~~~~~~~~~~*/
		 $("#_enrolleeinfo_form #uploadify_images_photoPath").uploadify({ //初始化学生照片上传
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}','replaceName':'${student.certNum}_photo'},//按学生报名日期创建目录，按学生证件号命名照片
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 40960, //限制单个文件上传大小40K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");

	              	$('#_enrolleeinfo_form #student_photoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#_enrolleeinfo_form #photoPath').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#_enrolleeinfo_form #uploadify').uploadifyClearQueue(); //清空上传队列
				}
     	 });
      	 var student_photoPath = '${student.photoPath}';
      	 if(student_photoPath != ''){
    		$('#_enrolleeinfo_form #student_photoPath').attr('src','${rootUrl}common/students/${student.photoPath}');
     	 }
     
     	 $("#_enrolleeinfo_form #uploadify_images_certPhotoPath").uploadify({ //初始化学生身份证照片上传
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}','replaceName':'${student.certNum}_cert'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 61440, //限制单个文件上传大小60K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#_enrolleeinfo_form #student_certPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#_enrolleeinfo_form #certPhotoPath').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#_enrolleeinfo_form #uploadify').uploadifyClearQueue(); //清空上传队列
				}
      	 });
      	 var student_certPhotoPath = '${student.certPhotoPath}';
     	 if(student_certPhotoPath != ''){
    		$('#_enrolleeinfo_form #student_certPhotoPath').attr('src','${rootUrl}common/students/${student.certPhotoPath}');
     	 }
     
     	 $("#_enrolleeinfo_form #uploadify_images_eduPhotoPath").uploadify({ //初始化学生毕业证照片上传
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'isStoreToDatabase':'N','storePath':'common,students,${storeDir}','replaceName':'${student.certNum}_edu'},//按学生报名日期创建目录
	            'fileDesc'       : '支持格式:jpg',  //限制文件上传格式描述
	            'fileExt'        : '*.JPG;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 204800, //限制单个文件上传大小60K 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	             	var result = response.split("|");                	             	
	              	$('#_enrolleeinfo_form #student_eduPhotoPath').attr('src','${rootUrl}common/students/${storeDir}/'+result[1]);
	               	$('#_enrolleeinfo_form #eduPhotoPath').val('${storeDir}/'+result[1]);
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#_enrolleeinfo_form #uploadify').uploadifyClearQueue(); //清空上传队列
				}
     	 });
     	 var student_eduPhotoPath = '${student.eduPhotoPath}';
      	 if(student_eduPhotoPath != ''){
    		$('#_enrolleeinfo_form #student_eduPhotoPath').attr('src','${rootUrl}common/students/${student.eduPhotoPath}');
     	 }  
     	 
		 /**~~~~~~~~~~~~~~~~~~~3.初始化省市组件~~~~~~~~~~~~~~~~~~~~~~*/   
		 jQuery("#_enrolleeinfo_form #ChinaArea1").jChinaArea({
			//	 aspnet:true,
				 s1:"${homePlace_province}",//默认选中的省名
				 s2:"${homePlace_city}",//默认选中的市名
				 s3:"${homePlace_county}"//默认选中的县区名
		 })
		 jQuery("#_enrolleeinfo_form #ChinaArea2").jChinaArea({
			// 	 aspnet:true,
				 s1:"${residence_province}",//默认选中的省名
				 s2:"${residence_city}",//默认选中的市名
				 s3:"${residence_county}"//默认选中的县区名
		 })
			 
			 
		 /**~~~~~~~~~~~~~~~~~~~4.初始化生成准考证号 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
		 jQuery("#_enrolleeinfo_form #recruitMajorid").change(function(){
			 
                var url = "${baseUrl}/edu3/framework/get-examcode.html";
            	var recruitMajorStartPointURL = "${baseUrl}/edu3/framework/get-RecruitMajorStartPoint.html";
            	var recruitPlanId  = jQuery("#_enrolleeinfo_form #recruitPlanId").val();
		    	var recruitMajorid = jQuery(this).val();
		   		var branchSchoolid = jQuery("#_enrolleeinfo_form #branchSchoolid").val();
		   		var flag           = true;
		   		
		   		if(""==recruitPlanId){
		   			alertMsg.warn("请选择一个招生批次!");
					flag =  false;
		   		} 
		   		 
				if(""==branchSchoolid){
					alertMsg.warn("请选择一个教学站!");
					flag =  false;
				}
		   		 
		   		if(""==recruitMajorid){
		   			flag =  false;
					alertMsg.warn("请选择一个专业!");
					
				}	 
				//if(flag ==  false){
					//$("#_enrolleeinfo_form #examCertificateNo").val("");
				//}
		    	//jQuery.post(url,{recruitMajorid:recruitMajorid,branchSchoolid:branchSchoolid},function(myJSON){
					//jQuery("#_enrolleeinfo_form #examCertificateNo").val("");
					//jQuery("#_enrolleeinfo_form #examCertificateNo").val(myJSON);
					jQuery.post(recruitMajorStartPointURL,{recruitMajorId:recruitMajorid},function(msg){
						 if(null != msg && ""!=msg && "高中"==msg){
							jQuery("#_enrolleeinfo_form #graduateSchoolInfoTr").hide();
					 		jQuery("#_enrolleeinfo_form #graduateInfoTr").hide();
					 		jQuery("#_enrolleeinfo_form #showMoreInfoFlag").attr("value","N");
					 		
					 		jQuery("#_enrolleeinfo_form #graduateSchool").removeAttr("classCss");
					 		jQuery("#_enrolleeinfo_form #graduateSchoolCode").removeAttr("classCss");
					 		jQuery("#_enrolleeinfo_form #graduateId").removeAttr("classCss");
					 		jQuery("#_enrolleeinfo_form #graduateMajor").removeAttr("classCss");
					 		
					 	}else{
					 		jQuery("#_enrolleeinfo_form #graduateSchoolInfoTr").show();
					 		jQuery("#_enrolleeinfo_form #graduateInfoTr").show();
					 		jQuery("#_enrolleeinfo_form #classicCode").attr("value",msg);
					 		jQuery("#_enrolleeinfo_form #showMoreInfoFlag").attr("value","Y");
					 		
					 		jQuery("#_enrolleeinfo_form #graduateSchool").attr("classCss","required");
					 		jQuery("#_enrolleeinfo_form #graduateSchoolCode").attr("classCss","required");
					 		jQuery("#_enrolleeinfo_form #graduateId").attr("classCss","required");
					 		jQuery("#_enrolleeinfo_form #graduateMajor").attr("classCss","required");
					 	}
					},"json");
				//},"json");
	      });
		 
	      /**~~~~~~~~~~~~~~~~5.初始化批次、教学站、专业联动组件~~~~~~~~~~~~~~~~*/
	      if(_isBrSchool == 'Y'){//如果是教学站
	    	  jQuery("#_enrolleeinfo_form #recruitPlanId").change(function(){
					
					var url 		  = "${baseUrl}/edu3/framework/branchshoolMajor/branchMajor-list.html";
					var recruitPlanId =  jQuery(this).val();
					//如果当前用户是教学站用户，此变量值不为空
					var currentUserBrs= "${branchSchoolid}";   
					var flag 		  = true;
					if(recruitPlanId==null||recruitPlanId==""){
						flag = false;
						jQuery("#_enrolleeinfo_form #recruitMajorid").children().remove();
						alertMsg.warn("请选择一个招生批次!");
					}
					$("#_enrolleeinfo_form #examCertificateNo").val("");
					if(flag != false){
						jQuery.post(url,{branchPlanId:recruitPlanId,branchSchoolId:currentUserBrs},function(myJSON){
								 $("#_enrolleeinfo_form #recruitMajorid").children().remove();
							  	 var selectObj="<option value=''>请选择</option>";
							  	 for (var i = 0; i < myJSON.length; i++) {
								 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
								 }
							 	 jQuery("#_enrolleeinfo_form #recruitMajorid").html(selectObj);
						},"json");
					}
			   });
	      }else{
	    	  jQuery("#_enrolleeinfo_form #branchSchoolid").change(function(){
	       		 	
					var url 		   = "${baseUrl}/edu3/framework/branchshoolMajor/branchMajor-list.html";
					var recruitPlanId  = jQuery("#_enrolleeinfo_form #recruitPlanId option:selected").val();
					var branchSchoolId = jQuery(this).val();
					var flag = true;
					if(recruitPlanId==null||recruitPlanId==""){
						flag = false;
						alertMsg.warn("招生批次是必需的,请选择招生批次!");
					}
					if(branchSchoolId == null||branchSchoolId==""){
						flag = false;
						alertMsg.warn("教学站是必需的,请选择教学站!");
					}
					if(flag != false){
						
						jQuery.post(url,{branchPlanId:recruitPlanId,branchSchoolId:branchSchoolId},function(myJSON){
							jQuery("#_enrolleeinfo_form #recruitMajorid").children().remove();
							jQuery("#_enrolleeinfo_form #recruitMajorid").show();
						  	 var selectObj="<option value=''>请选择</option>";
						 	 for (var i = 0; i < myJSON.length; i++) {
						 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
							 }
						 	 jQuery("#_enrolleeinfo_form #recruitMajorid").html(selectObj);
						},"json");
					}else{
						$("#_enrolleeinfo_form #recruitMajorid").children().remove();
						$("#_enrolleeinfo_form #examCertificateNo").val("");
					}
					
			   });
			   jQuery("#_enrolleeinfo_form #recruitPlanId").change(function(){
					
					var url 		  = "${baseUrl}/edu3/framework/branchshoolplan/branchSchoolPlan-list.html";
					var recruitPlanId =  jQuery(this).val();
					//如果当前用户是教学站用户，此变量值不为空
					var currentUserBrs= "${branchSchoolid}";   
					var flag 		  = true;
					if(recruitPlanId==null||recruitPlanId==""){
						flag = false;
						jQuery("#_enrolleeinfo_form #branchSchoolid").children().remove();
						alertMsg.warn("请选择一个招生批次!");
					}
					$("#_enrolleeinfo_form #branchSchoolid").children().remove();
					$("#_enrolleeinfo_form #recruitMajorid").children().remove();
					$("#_enrolleeinfo_form #examCertificateNo").val("");
					if(flag != false){
						jQuery.post(url,{recruitPlanId:recruitPlanId},function(myJSON){
								jQuery("#_enrolleeinfo_form #branchSchoolid").children().remove(); 
							  	 var selectObj="<option value=''>请选择</option>";
							 	 for (var i = 0; i < myJSON.length; i++) {
							 		 //当前用户如果是教学站只显示当前教学站
							 		 if(null!=currentUserBrs && currentUserBrs !="" && currentUserBrs==myJSON[i].key){
							 			selectObj += '<option value="' + myJSON[i].key + '" selected="selected" >' + myJSON[i].value + '</option>';    
							 		 }
							 		 //当前用户如果不是教学站显示所有教学站
									 if(null==currentUserBrs || currentUserBrs =="" ){
							 			selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
							 		 }
								}
							 	 jQuery("#_enrolleeinfo_form #branchSchoolid").html(selectObj);
						},"json");
					}
			   });
	      }
       	 /**~~~~~~~~~~~~~~~~~~~~~~~~~~6.初始化媒体来源~~~~~~~~~~~~~~~~~~~~~~~~*/
		   jQuery('#_enrolleeinfo_form #fromMedia').change(function(){
                   if(jQuery('#_enrolleeinfo_form #fromMedia').val()=="6")
                   {
                     alertMsg.warn("请在右边的文本框里输入其他信息");
                     jQuery('#_enrolleeinfo_form #fromOtherMedia').attr("readonly",false);
                   }else{
                     jQuery('#_enrolleeinfo_form #fromOtherMedia').val("");
                	 jQuery('#_enrolleeinfo_form #fromOtherMedia').attr("readonly",true);
                   }
                   return false;
            });   
          /**~~~~~~~~~~~~~~~~~~~~~~~~~7.初始化简历表单~~~~~~~~~~~~~~~~~~~~~~~~*/      
            jQuery('#_enrolleeinfo_form #addResume').click(function(){
                  var rowNum = jQuery("#_enrolleeinfo_form #studentResume").get(0).rows.length;
				  jQuery("#_enrolleeinfo_form #studentResume").append(
				   "<tr><td><q>"+rowNum+"</q>"
					+"<input type='checkbox' id='checkId' value=''/>"
					+"<input type='hidden' name='resumeid' value=''/></td>"
					+"<td>开始年:<gh:select name='startYear' dictionaryCode='CodeYear' value='' />"	           
					+"开始月:<gh:select name='startMonth' dictionaryCode='CodeMonth' value='' /></td>"			
					+"<td>截止年:<gh:select name='endYear' dictionaryCode='CodeYear' value='' />"	           
					+"截止月:<gh:select name='endMonth' dictionaryCode='CodeMonth' value='' /></td>"				  
					+"<td>工作单位:<input type='text' name='company' size='34'  value=''/></td>"			  
					+"<td>职务:<input type='text' name='resumetitle' size='12'  value=''/></td>"				
					+"</tr>"			
				  );
            });                
            jQuery('#_enrolleeinfo_form #delResume').click(function(){
            	
            	tab = jQuery("#_enrolleeinfo_form #studentResume").get(0);
		  		var ids = "";
				var idArray = new Array();
		  		jQuery("input[id='checkId']:checkbox").each(function(index){
					if(jQuery(this).attr("checked")){
					  if(jQuery(this).val()!="")
					  {
						ids = ids + jQuery(this).val() + ",";
					  }
						idArray.push(index);
					}
				})
				if(idArray.length<1){ alertMsg.warn("请选择要删除的记录！");return false;}
				if(window.confirm('确定删除该记录?')){
					var rowIndex;
					var nextDiff =0;
					for(j=0;j< idArray.length;j++){
						rowIndex = idArray[j]+1-nextDiff;
						tab.deleteRow(rowIndex);
						nextDiff++;
					}
					var url = "${baseUrl}/edu3/framework/delResume.html";
					if(ids!=""){
						jQuery.get(url,{ids:ids})
					}
				}
		    	jQuery("q").each(function(index){
		    		jQuery(this).text(index+1);
		    	});
		    });
            

    		//当选择入学前最高学历为高中、职高时不用填写  入学前学历学校代码 、入学前学历学校名称等信息    		
    		jQuery('#_enrolleeinfo_form #educationalLevel').change(function(){
    			if($(obj).val()==1){
    				jQuery("#_enrolleeinfo_form #graduateSchoolInfoTr").hide();
    		 		jQuery("#_enrolleeinfo_form #graduateInfoTr").hide();
    		 		
    		 		jQuery("#_enrolleeinfo_form #graduateSchool").removeAttr("classCss");
    		 		jQuery("#_enrolleeinfo_form #graduateSchoolCode").removeAttr("classCss");
    		 		jQuery("#_enrolleeinfo_form #graduateId").removeAttr("classCss");
    		 		jQuery("#_enrolleeinfo_form #graduateMajor").removeAttr("classCss");
    			}else{
    				var showMoreInfoFlag =  jQuery("#_enrolleeinfo_form #showMoreInfoFlag").val();
    				if("Y"==showMoreInfoFlag){
    					jQuery("#_enrolleeinfo_form #graduateSchoolInfoTr").show();
    			 		jQuery("#_enrolleeinfo_form #graduateInfoTr").show();

    			 		jQuery("#_enrolleeinfo_form #graduateSchool").attr("classCss","required");
    			 		jQuery("#_enrolleeinfo_form #graduateSchoolCode").attr("classCss","required");
    			 		jQuery("#_enrolleeinfo_form #graduateId").attr("classCss","required");
    			 		jQuery("#_enrolleeinfo_form #graduateMajor").attr("classCss","required");
    				}
    			}
    		 }); 
    		
    	//初始化身份证自动填充    	
		$("#_enrolleeinfo_form #certNum").unbind("blur").bind("blur",function(){			
		 if($("#_enrolleeinfo_form #certType").val() == 'idcard')			
		 	showBirthday($(this).val());
		});
});     
   
		
		//保存回调
        function save(form){
	         //籍贯	
			 jQuery("#_enrolleeinfo_form #homePlace").val(jQuery("#_enrolleeinfo_form #homePlace_province option:selected").text()+","
	                       				 +jQuery("#_enrolleeinfo_form #homePlace_city option:selected").text()+","
	                        			 +jQuery("#_enrolleeinfo_form #homePlace_county option:selected").text());
		 	 //户口  
			 jQuery("#_enrolleeinfo_form #residence").val(jQuery("#_enrolleeinfo_form #residence_province option:selected").text()+","
	                       				 +jQuery("#_enrolleeinfo_form #residence_city option:selected").text()+","
	                       				 +jQuery("#_enrolleeinfo_form #residence_county option:selected").text());  
		 	 
			 var isAdd = "${enrolleeinfo.resourceid}";
		 	 if(""==isAdd || null== isAdd){
		 		var certNum = $("#_enrolleeinfo_form #enrolleeinfo_form_table input[id=certNum]").val();
			  	  if(""==certNum){
			  		alertMsg.warn("请填写一个证件号码！")
			  		  return false;
			  	  }else{
			  			  var checkExistsURL = "${baseUrl}/edu3/framework/checkexistsuser.html"
				  		  $.get(checkExistsURL,{certNum:certNum},function(data){
				  			  if("true"==data){
				  				//alertMsg.warn("该证件号已存在！");
				  				//return false;
				  				  //$("#_enrolleeinfo_form #enrolleeinfo_form_table input[id=isExstisCertNum]").attr("value","true");
				  			  }//else{
				  				 // $("#_enrolleeinfo_form #enrolleeinfo_form_table input[id=isExstisCertNum]").attr("value","false");
				  			 // }
				  		  })
				  		// var isExists = $("#_enrolleeinfo_form #enrolleeinfo_form_table input[id=isExstisCertNum]").val();
				  		 // if("true"==isExists){
				  				//alertMsg.warn("该证件号已存在！");
				  			 // return false;
				  		 // }
			  	  }
		 	 }
		 	 //验证身份证
	         if(jQuery("#_enrolleeinfo_form #certType").val()=="idcard")
	         {
	            var idNumber =jQuery("#_enrolleeinfo_form #certNum").val();
	      		//身份证正则表达式(15位)
				//var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
				//身份证正则表达式(18位)
				//var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/;
	            
				if(_idCardValidate(idNumber)){ 
					$("#_enrolleeinfo_form #certNum").css({"backgroundColor":"#fff"});
		        }else{
		       	 	alertMsg.warn("身份证号码不合法!");
		       	 	$("#_enrolleeinfo_form #certNum").css({"backgroundColor":"#ffc8c8"});
		        	return false;
		        }
	      			
	         }
	         if(null==jQuery("#_enrolleeinfo_form #graduateDate").val()||""==jQuery("#_enrolleeinfo_form #graduateDate").val()){
               	alertMsg.warn("请填写毕业日期");
               	return false;
	         }
	         if(null==jQuery("#_enrolleeinfo_form #bornDay").val()||""==jQuery("#_enrolleeinfo_form #bornDay").val()){
               	alertMsg.warn("请填写正确的出生日期");
               	return false;
	         }
	                  
	         //如果职业状况选择了从业则需要填写相关信息
	         if(jQuery('#_enrolleeinfo_form #workStatus').val()=="worked"){
	        	var message="";
          	    if(jQuery('#_enrolleeinfo_form #officeName').val()=="")
          	    {
          	      message+="请填写工作单位!<br>";
          	    }
          	    if(message!="")
          	    {
          	      alertMsg.warn(message);
          	      return false;
          	    }
	         }
	         //报读专什本的学生验证学历相关
	         if(jQuery('#_enrolleeinfo_form #classiccode').val()=="2"){
	        	 
	        	var message="";
          	    if(jQuery('#_enrolleeinfo_form #graduateSchool').val()==""){
          	      message+="入学前学历学校名称不能为空!<br>";
          	    }
          	    if(jQuery('#_enrolleeinfo_form #graduateSchoolCode').val()==""){
          	      message+="入学前学历学校代码不能为空!<br>";
          	    }else if(jQuery('#_enrolleeinfo_form #graduateSchoolCode').val().length!=5){
          	      message+="入学前学历学校代码是毕业证书号的前五位!<br>";
          	    }
          	    if(jQuery('#_enrolleeinfo_form #graduateId').val()==""){
          	      message+="入学前学历证书编号不能为空!<br>";
          	    }
          	    if(message!=""){
          	      alertMsg.warn(message);
          	      return false;
          	    }
	         }
	         //信息地媒体来源             
			 if(jQuery('#_enrolleeinfo_form #fromMedia').val()=="6" && jQuery('#_enrolleeinfo_form #fromOtherMedia').val()=="" )
             {
               alertMsg.warn("请输入信息地媒体来源!");
               return false;
             }else{
             	if(jQuery('#_enrolleeinfo_form #fromMedia').val() == ""){
             		alertMsg.warn("请选择信息地媒体来源!");
                    return false;
             	}
             }      
	         return validateCallback(form);
      }
	
      //身份证获取性别及出生年月	
   	 function showBirthday(val){
   		var birthdayValue;
   		var _bornDay = $("#_enrolleeinfo_form #bornDay");
   		var _gender = $("#_enrolleeinfo_form #gender");
   		if(15==val.length){ //15位身份证号码
   		    birthdayValue = val.charAt(6)+val.charAt(7);
   		    if(parseInt(birthdayValue)<10){
   		     birthdayValue = '20'+birthdayValue;
   		    } else {
   		     birthdayValue = '19'+birthdayValue;
   		    }
   		    birthdayValue=birthdayValue+'-'+val.charAt(8)+val.charAt(9)+'-'+val.charAt(10)+val.charAt(11);
   		    if(parseInt(val.charAt(14)/2)*2!=val.charAt(14)){		    	
   		    	_gender.val("1");
   		    }else{ 	
   		    	_gender.val("2");
   		    }
   		    _bornDay.val(birthdayValue);		   
   		 }
   		
   		  if(18==val.length){ //18位身份证号码
   		    birthdayValue=val.charAt(6)+val.charAt(7)+val.charAt(8)+val.charAt(9)+'-'+val.charAt(10)+val.charAt(11) +'-'+val.charAt(12)+val.charAt(13);
   		    if(parseInt(val.charAt(16)/2)*2!=val.charAt(16)){
   		    	_gender.val("1");
   		    }else{
   		    	_gender.val("2");
   		    }
   		    if(val.charAt(17)!=IDCard(val))  {
   		     //document.all.idCard.style.backgroundColor='#ffc8c8';
   		     $("#_enrolleeinfo_form #certNum").css({"backgroundColor":"#ffc8c8"});
   		     _gender.val("");//清除
   		     _bornDay.val("");
   		    }else{
   		    	$("#_enrolleeinfo_form #certNum").css({"backgroundColor":"#fff"});	
   		    	_bornDay.val(birthdayValue);
   		    }   		  
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
</script>
	<h2 class="contentTitle">报名表单</h2>
	<div class="page">
		<div class="pageContent">
			<form id="_enrolleeinfo_form" method="post"
				action="${baseUrl}/edu3/recruit/enroll/saveenrolleeinfo.html"
				class="pageForm" onsubmit="return save(this);"
				enctype="multipart/form-data">
				<input type="hidden" name="act" value="${act }" /> <input
					type="hidden" name="resourceid" value="${enrolleeinfo.resourceid}" />
				<input type="hidden" name="signupDate"
					value="<fmt:formatDate value="${enrolleeinfo.signupDate }" pattern="yyyy-MM-dd" />" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>填写报名信息</span> </a></li>
									<li class="selected"><a href="#"><span>填写个人信息</span> </a>
									</li>
									<c:if test="${ not empty enrolleeinfo }">
										<li><a href="#"><span>上传证件信息</span> </a></li>
									</c:if>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<tr>
										<td width="12%">招生批次:</td>
										<td width="38%"><c:choose>
												<c:when test="${enrolleeinfo.signupFlag!='Y'}">
													<select id="recruitPlanId" name="recruitPlan"
														class="required" style="width: 50%">
														<option value="">请选择招生批次</option>
														<c:choose>
															<c:when
																test="${null != enrolleeinfo and enrolleeinfo.recruitMajor.recruitPlan.isPublished eq 'Y' }">
																<c:forEach items="${publishedPlanList }"
																	var="recruitPlan">
																	<option value="${recruitPlan.resourceid }"
																		<c:if test="${recruitPlan.resourceid eq enrolleeinfo.recruitMajor.recruitPlan.resourceid}"> selected="selected" </c:if>>${recruitPlan.recruitPlanname }</option>
																</c:forEach>
															</c:when>
															<c:otherwise>
																<c:if test="${null!=enrolleeinfo }">
																	<option
																		value="${enrolleeinfo.recruitMajor.recruitPlan.resourceid }"
																		selected="selected">${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</option>
																</c:if>
																<c:forEach items="${publishedPlanList }"
																	var="recruitPlan">
																	<option value="${recruitPlan.resourceid }">${recruitPlan.recruitPlanname }</option>
																</c:forEach>
															</c:otherwise>
														</c:choose>
														<%--
														 <c:if test="${null != enrolleeinfo }">
														 	<option value="${enrolleeinfo.recruitMajor.recruitPlan.resourceid }" selected="selected">${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</option>
														 </c:if>
														 <c:forEach items="${publishedPlanList }" var="recruitPlan">
															 	<option value="${recruitPlan.resourceid }" <c:if test="${recruitPlan.resourceid eq enrolleeinfo.recruitMajor.recruitPlan.resourceid}"> selected="selected" </c:if> >${recruitPlan.recruitPlanname }</option>
														 </c:forEach>
														 --%>
													</select>
												</c:when>
												<c:when
													test="${enrolleeinfo.entranceflag=='Y' or enrolleeinfo.entranceflag=='W' or  empty enrolleeinfo.entranceflag}">
													<%-- <select id="recruitPlanId" name="recruitPlan"  class="required" style="width: 50%" disabled="disabled">
												  		    <option value="" >请选择招生批次</option>
														 <c:if test="${null != enrolleeinfo }">
														 	<option value="${enrolleeinfo.recruitMajor.recruitPlan.resourceid }" selected="selected">${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</option>
														 </c:if>
														 <c:forEach items="${publishedPlanList }" var="recruitPlan">
														 	<option value="${recruitPlan.resourceid }" <c:if test="${recruitPlan.resourceid eq enrolleeinfo.recruitMajor.recruitPlan.resourceid}"> selected="selected" </c:if> >${recruitPlan.recruitPlanname }</option>
														 </c:forEach>
													 </select>
													--%> 
													${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}
													<input type="hidden"
														name="${enrolleeinfo.recruitMajor.recruitPlan.resourceid }" />
												</c:when>
											</c:choose> <font color='red'>* </font></td>

										<td width="12%">教学站:</td>
										<td width="38%"><c:set var="isAble" value="true" /> <c:choose>
												<c:when test="${isBrSchool eq 'Y' }">
													<input type="hidden" name="branchSchool"
														value="${branchSchoolid }">
													<gh:selectModel id="branchSchoolid" name="branchSchool_chs"
														bindValue="resourceid" displayValue="unitCodeAndName"
														modelClass="com.hnjk.security.model.OrgUnit"
														value="${branchSchoolid }" style="width: 50%"
														orderBy="unitCode asc" classCss="required" disabled="true" />
												</c:when>
												<c:otherwise>
													<c:if test="${enrolleeinfo.signupFlag!='Y'}">
														<c:set var="isAble" value="false" />
													</c:if>
													<gh:selectModel id="branchSchoolid" name="branchSchool"
														bindValue="resourceid" displayValue="unitCodeAndName"
														modelClass="com.hnjk.security.model.OrgUnit"
														value="${enrolleeinfo.branchSchool.resourceid}"
														classCss="required" orderBy="unitCode asc"
														condition="resourceid='${branchSchoolid}',unitType='brSchool'"
														disabled="${isAble }" style="width: 50%" />
													<input type="hidden" name="branchSchool"
														value="${enrolleeinfo.branchSchool.resourceid }">
												</c:otherwise>
											</c:choose> <font color='red'>* </font></td>

									</tr>
									<tr>
										<td width="12%">招生专业:</td>
										<td width="38%"><c:choose>
												<c:when test="${enrolleeinfo.signupFlag ne 'Y'}">
													<%-- 
												<gh:selectModel id="recruitMajorid" name="recruitMajor"
													bindValue="resourceid" displayValue="recruitMajorName"
													modelClass="com.hnjk.edu.recruit.model.RecruitMajor"
													value="${enrolleeinfo.recruitMajor.resourceid}"
													classCss="required"  condition="recruitPlan.isPublished='Y'" />
												--%>
													<select id="recruitMajorid" name="recruitMajor"
														class="required" style="width: 50%">
														<option value="">请选择</option>
														<c:forEach items="${rmList }" var="brm">
															<option value="${brm.resourceid }"
																<c:if test="${brm.resourceid eq enrolleeinfo.recruitMajor.resourceid }"> selected="selected" </c:if>>${brm.recruitMajorName }</option>
														</c:forEach>
													</select>
													<font color='red'>* </font>
												</c:when>
												<c:when
													test="${enrolleeinfo.signupFlag eq 'Y' and enrolleeinfo.entranceflag=='Y' or enrolleeinfo.entranceflag=='W' or  empty enrolleeinfo.entranceflag}">
													<input type="hidden" name="recruitMajor"
														value="${enrolleeinfo.recruitMajor.resourceid }">
													<gh:selectModel id="recruitMajorid" name="recruitMajor"
														bindValue="resourceid" displayValue="recruitMajorName"
														modelClass="com.hnjk.edu.recruit.model.RecruitMajor"
														value="${enrolleeinfo.recruitMajor.resourceid}"
														classCss="required" disabled="true" style="width: 50%" />
													<font color='red'>* </font>
												</c:when>
											</c:choose></td>

										<td width="12%"><c:if
												test="${ not empty  enrolleeinfo.examCertificateNo }">
													准考证号:
												</c:if></td>
										<td width="38%"><c:if
												test="${ not empty  enrolleeinfo.examCertificateNo }">
												<input type="text" id="examCertificateNo"
													name="examCertificateNo" size="34"
													value="${enrolleeinfo.examCertificateNo }" readonly />
											</c:if></td>

									</tr>
									<tr>
										<td width="12%">申请免试:</td>
										<td width="38%"><input type="radio" CHECKED value="N"
											name="isApplyNoexam"> 不申请 <input type="radio"
											<c:if test="${enrolleeinfo.isApplyNoexam=='Y'}">CHECKED</c:if>
											value="Y" name="isApplyNoexam"> 申请</td>
										<td width="12%">入学前国民教育最高学历层次:</td>
										<td width="38%"><gh:select id="educationalLevel"
												name="educationalLevel"
												dictionaryCode="CodeEducationalLevel"
												value="${enrolleeinfo.educationalLevel}" classCss="required" /><font
											color='red'>*</font></td>
									</tr>
									<input type="hidden" " id="showMoreInfoFlag"
										name="showMoreInfoFlag" />
									<c:set var="isDisplayGraduateSchool" value="N" />
									<c:if
										test="${enrolleeinfo.recruitMajor.classic.classicCode eq '2' }">
										<c:set var="isDisplayGraduateSchool" value="Y" />
									</c:if>
									<tr id="graduateSchoolInfoTr"
										<c:if test="${ isDisplayGraduateSchool eq 'N'}">style="display:none;"</c:if>>
										<td width="12%">入学前学历学校名称:</td>
										<td width="38%"><input type="text" id="graduateSchool"
											name="graduateSchool" size="34"
											value="${enrolleeinfo.graduateSchool }" /><font color='red'>*</font>
										</td>
										<td width="12%">入学前学历学校代码:</td>
										<td width="38%"><input type="text"
											id="graduateSchoolCode" name="graduateSchoolCode" size="34"
											value="${enrolleeinfo.graduateSchoolCode }" maxlength="5" /><font
											color='red'>*</font> (毕业证书号的前五位)</td>
									</tr>
									<tr id="graduateInfoTr"
										<c:if test="${ isDisplayGraduateSchool eq 'N'}">style="display:none;"</c:if>>
										<td width="12%">入学前学历证书编号:</td>
										<td width="38%"><input type="text" id="graduateId"
											name="graduateId" size="34"
											value="${enrolleeinfo.graduateId }" /><font color='red'>*</font>
										</td>
										<td width="12%">毕业专业:</td>
										<td width="38%"><input type="text" id="graduateMajor"
											name="graduateMajor" size="34"
											value="${enrolleeinfo.graduateMajor }" /> <font color='red'>*</font>
										</td>
									</tr>
									<tr>
										<td width="12%">入学前最高学历毕业日期:</td>
										<td width="38%"><input id="graduateDate" type="text"
											name="graduateDate" size="34" class="required Wdate"
											onfocus="WdatePicker({isShowWeek:true})"
											value="${enrolleeinfo.graduateDate}" /></td>
										<td width="12%">信息地媒体来源：</td>
										<td width="38%"><gh:select id="fromMedia"
												name="fromMedia" dictionaryCode="CodeFromMedia"
												value="${enrolleeinfo.fromMedia}" /> <input
											id="fromOtherMedia" type="text" name="fromOtherMedia"
											size="18" value="${enrolleeinfo.fromOtherMedia}"
											readonly="true" /> <font color='red'>*</font></td>
									</tr>
								</table>
							</div>
							<div>
								<input type="hidden" id="homePlace" name="homePlace" value="" />
								<input type="hidden" id="residence" name="residence" value="" />
								<input type="hidden" name="enrolleeType"
									value="${enrolleeType }" /> <input type="hidden"
									id="classicCode" name="classicCode" value="0" />
								<!-- 赋予管理员可修改权限 -->
								<c:set var="isPermision" value="N" />
								<c:set var="isAble" value="false" />
								<security:authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_ADMIN">
									<c:set var="isPermision" value="Y" />
								</security:authorize>

								<table id="enrolleeinfo_form_table" class="form">
									<tr>
										<td width="12%">姓名:</td>
										<td colspan="3" width="88%"><input type="text"
											name="name" size="34" value="${student.name }"
											class="required" maxlength="20"
											<c:if test="${(isPermision eq 'N') and (enrolleeinfo.signupFlag eq 'Y') }">
													readonly="readonly"
												</c:if>
											style="width: 16%" /></td>
									</tr>
									<tr>
										<td width="12%">证件类别:</td>
										<td width="38%">
											<!-- 如果没有权限，则DISABLED --> <c:if
												test="${(isPermision eq 'N') and (enrolleeinfo.signupFlag eq 'Y') }">
												<c:set var="isAble" value="true" />
												<input type="hidden" name="certType"
													value="${student.certType}" />
											</c:if> <gh:select id="certType" name="certType"
												disabled="${isAble }" dictionaryCode="CodeCertType"
												value="${student.certType}" classCss="required"
												style="width: 39%" /> <font color='red'>* </font>
										</td>
										<td width="12%">证件号码：</td>
										<td width="38%"><input type="hidden" id="isExstisCertNum"
											name="isExstisCertNum" value="false" /> <input type="text"
											id="certNum" name="certNum" size="34"
											<c:if test="${(isPermision eq 'N') and (enrolleeinfo.signupFlag eq 'Y') }">
														readonly="readonly"
													</c:if>
											value="${student.certNum}" class="required" maxlength="18"
											style="width: 37%" /> <span class="tips">身份证末位X要大写</span></td>
									</tr>
									<tr>
										<td width="12%">出生日期:</td>
										<td width="38%"><input type="text" id="bornDay"
											name="bornDay" size="34" value="${student.bornDay }"
											class="required Wdate" style="width: 38%"
											onfocus="WdatePicker({isShowWeek:true})" /></td>
										<td width="12%">性别:</td>
										<td width="38%"><gh:select name="gender"
												dictionaryCode="CodeSex" value="${student.gender}"
												classCss="required" style="width:39%" /><font color='red'>*
										</font></td>
									</tr>
									<tr>
										<td width="12%">籍贯:</td>
										<td width="38%">
											<div id="ChinaArea1">
												<select id="homePlace_province" name="homePlace_province"
													style="width: 100px;"></select> <select id="homePlace_city"
													name="homePlace_city" style="width: 100px;"></select> <select
													id="homePlace_county" name="homePlace_county"
													style="width: 120px;">
												</select>
											</div>
										</td>
										<td width="12%">户口所在地:</td>
										<td width="38%">
											<div id="ChinaArea2">
												<select id="residence_province" name="residence_province"
													style="width: 100px;"></select> <select id="residence_city"
													name="residence_city" style="width: 100px;"></select> <select
													id="residence_county" name="residence_county"
													style="width: 120px;">
												</select>
											</div>
										</td>
									</tr>
									<tr>
										<td width="12%">民族:</td>
										<td width="38%"><gh:select name="nation"
												dictionaryCode="CodeNation" value="${student.nation}"
												classCss="required" style="width: 39%" /><font color='red'>*
										</font></td>
										<td width="12%">政治面目:</td>
										<td width="38%"><gh:select name="politics"
												dictionaryCode="CodePolitics" value="${student.politics}"
												classCss="required" style="width: 39%" /><font color='red'>*
										</font></td>
									</tr>
									<tr>
										<td width="12%">婚否:</td>
										<td width="38%"><gh:select name="marriage"
												dictionaryCode="CodeMarriage" value="${student.marriage}"
												classCss="required" style="width: 39%" /><font color='red'>*
										</font></td>
										<td width="12%">工作岗位:</td>
										<td width="38%"><input type="text" name="title" size="34"
											value="${student.title}" style="width: 38%" /></td>
									</tr>
									<tr>
										<td width="12%">职业状态:</td>
										<td width="38%"><gh:select id="workStatus"
												name="workStatus" dictionaryCode="CodeWorkStatus"
												value="${student.workStatus}" style="width: 39%"
												classCss="required" /><font color='red'>*</font></td>
										<td width="12%">行业类别:</td>
										<td width="38%"><gh:select id="industryType"
												name="industryType" dictionaryCode="CodeIndustryType"
												value="${student.industryType}" style="width: 39%" /></td>
									</tr>
									<tr>
									</tr>
									<tr>
										<td width="12%">工作单位:</td>
										<td width="38%"><input type="text" id="officeName"
											name="officeName" size="34" value="${student.officeName }"
											maxlength="50" style="width: 38%" /></td>
										<td width="12%">单位电话:</td>
										<td width="38%"><input type="text" id="officePhone"
											name="officePhone" size="34" value="${student.officePhone }"
											maxlength="12" style="width: 38%" /></td>
									</tr>
									<tr>
										<td width="12%">地址：</td>
										<td width="38%"><input type="text" name="contactAddress"
											size="34" style="width: 38%"
											value="${student.contactAddress }" class="required"
											maxlength="100" />
										<!--  <font color='red'>* 用于寄送通知书,请填写准确</font>--></td>
										<td width="12%">邮编:</td>
										<td width="38%"><input type="text" name="contactZipcode"
											size="34" style="width: 38%"
											value="${student.contactZipcode }" maxlength="6"
											class="required postcode" /></td>
									</tr>
									<tr>
										<td width="12%">固定电话:</td>
										<td width="38%"><input type="text" name="contactPhone"
											size="34" value="${student.contactPhone }" style="width: 38%"
											class="phone" /></td>
										<td width="12%">手机号码:</td>
										<td width="38%"><input type="text" name="mobile"
											size="34" style="width: 38%" value="${student.mobile }"
											class="required mobile" maxlength="11" /></td>

									</tr>
									<tr>
										<td width="12%">电子邮件：</td>
										<td width="38%"><input type="text" name="email" size="34"
											style="width: 38%" value="${student.email }" class="email" />
										</td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td width="12%">备注说明:</td>
										<td colspan="3" width="88%"><textarea name="memo"
												cols="60" rows="5">${student.memo }</textarea></td>
									</tr>
								</table>
								<br />
								<table class="form" id="studentResume">
									<thead>
										<strong> 填写个人简历:</strong>
									</thead>
									<tr>
										<td colspan="5" style="text-align: right"><input
											type="button" id="addResume" value="新增" class="b1"
											onmouseover="this.className='b2'"
											onmouseout="this.className='b1'"> &nbsp; <input
											type="button" id="delResume" value="删除" class="b1"
											onmouseover="this.className='b2'"
											onmouseout="this.className='b1'"> &nbsp;</td>
									</tr>
									<c:forEach items="${student.studentResume}" var="resume"
										varStatus="vs">
										<tr>
											<td><q>${vs.index+1}</q> <input type="checkbox"
												id="checkId" value="${resume.resourceid}" /> <input
												type="hidden" name="resumeid" value="${resume.resourceid}" />
											</td>
											<td>开始年: <gh:select name="startYear"
													dictionaryCode="CodeYear" value="${resume.startYear}" />
												开始月: <gh:select name="startMonth" dictionaryCode="CodeMonth"
													value="${resume.startMonth}" />
											</td>
											<td>截止年: <gh:select name="endYear"
													dictionaryCode="CodeYear" value="${resume.endYear}" />
												截止月: <gh:select name="endMonth" dictionaryCode="CodeMonth"
													value="${resume.endMonth}" />
											</td>
											<td>工作单位: <input type="text" name="company" size="34"
												value="${resume.company}" />
											</td>
											<td>职务: <input type="text" name="resumetitle" size="12"
												value="${resume.title}" />
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
							<c:if test="${ not empty enrolleeinfo }">
								<div>
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
											<td width="20%"><c:choose>
													<c:when
														test="${(isPermision eq 'Y') or (enrolleeinfo.signupFlag ne 'Y') }">
														<input name="uploadify_images_photoPath"
															id="uploadify_images_photoPath" type="file" />
													</c:when>
													<c:otherwise>
														<font color="green">缺少权限</font>
													</c:otherwise>
												</c:choose></td>
											<td><img id="student_photoPath"
												src="${baseUrl }/themes/default/images/img_preview.png"
												width="90" height="126" /> <!-- 
										<c:if test="${not empty student.photoPath  }"><a href="${baseUrl }/edu3/framework/filemanage/preview.html?storePath=common,students&fileName=${student.photoPath}" target="dialog" mask="true">查看原图</a></c:if>	 -->
												<input type="hidden" name="photoPath" id="photoPath"
												value="${student.photoPath}" /></td>
										</tr>
										<tr>
											<td width="30%">选择身份证复印扫描件：<br /> 1、中华人民共和国第二（一）代居民身份证；<br />
												2、大小：≤60k,格式 :jpg
											</td>
											<td width="20%"><c:choose>
													<c:when
														test="${(isPermision eq 'Y') or (enrolleeinfo.signupFlag ne 'Y') }">
														<input name="uploadify_images_certPhotoPath"
															id="uploadify_images_certPhotoPath" type="file" />
													</c:when>
													<c:otherwise>
														<font color="green">缺少权限</font>
													</c:otherwise>
												</c:choose></td>
											<td><img id="student_certPhotoPath"
												src="${baseUrl }/themes/default/images/img_preview.png"
												width="150" height="100" /> <input type="hidden"
												name="certPhotoPath" id="certPhotoPath"
												value="${student.certPhotoPath }" /></td>
										</tr>
										<tr>
											<td width="30%">选择毕业证复印扫描件：<br /> 1、普通大中专院校证件；<br />
												2、大小：≤200k,格式 :jpg
											</td>
											<td width="20%"><c:choose>
													<c:when
														test="${(isPermision eq 'Y') or (enrolleeinfo.signupFlag ne 'Y') }">
														<input name="uploadify_images_eduPhotoPath"
															id="uploadify_images_eduPhotoPath" type="file" />
													</c:when>
													<c:otherwise>
														<font color="green">缺少权限</font>
													</c:otherwise>
												</c:choose></td>
											<td><img id="student_eduPhotoPath"
												src="${baseUrl }/themes/default/images/img_preview.png"
												width="150" height="100" /> <input type="hidden"
												name="eduPhotoPath" id="eduPhotoPath"
												value="${student.certPhotoPath }" /></td>
										</tr>
									</table>
									<span><font color="red">说明：点击上传附件后，需要"提交"保存表单.</font> </span>
								</div>
							</c:if>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><c:choose>
								<c:when
									test="${enrolleeinfo.enrolleeType==0 and ghfn:hasAuth('RES_RECRUIT_ENROLLEE_EDIT')}">
									<div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">提交</button>
										</div>
									</div>
								</c:when>
								<c:when
									test="${enrolleeinfo.enrolleeType==1 and ghfn:hasAuth('RES_RECRUIT_ENROLLEE_CHECKINFO')}">
									<div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">审核并提交</button>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">提交</button>
										</div>
									</div>
								</c:otherwise>
							</c:choose></li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
