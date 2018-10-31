<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>校外学中心网上报名表单</title>
<link type="text/css" rel="stylesheet"
	href="${baseUrl}/style/default/netedu3-default.css" />
<gh:loadCom
	components="tab,validator,blockUI,cnArea,datePicker,multipleupload,preview" />

</head>
<body>
	<script type="text/javascript">
      		var brSchoolId = '${brSchoolId}';
      		
		 	jQuery(document).ready(function(){	 		
		        var tab = new $.fn.tab({
				tabList:"#demo1 .ui-tab-container .ui-tab-list li",
				contentList:"#demo1 .ui-tab-container .ui-tab-content"
				});   
                	 
                jQuery('#next').click(function(){
                	if(jQuery("#fragment-1").validator("alert")) {
                	  tab.triggleTab(1) 
                	   return false;
                	}  
                 });
                
                 jQuery('#prev').click(function(){
                	tab.triggleTab(0) ;
                   return false;
                 });
                   
                
                 
                 jQuery('#fromMedia').change(function(){
                   if(jQuery('#fromMedia').val()=="6")
                   {
                     alert("请在右边的文本框里输入其他信息");
                     jQuery('#fromOtherMedia').attr("readonly",false);
                   }else{
                     jQuery('#fromOtherMedia').val("");
                	 jQuery('#fromOtherMedia').attr("readonly",true);
                   }
                   return false;
                 });
                 
                 jQuery('#save').click(function(){
                  	//籍贯	
					jQuery("#homePlace").val(jQuery("#homePlace_province option:selected").text()+","
		                       			    +jQuery("#homePlace_city option:selected").text()+","
		                         			+jQuery("#homePlace_county option:selected").text());
				  	//户口  
					 jQuery("#residence").val(jQuery("#residence_province option:selected").text()+","
		                        			 +jQuery("#residence_city option:selected").text()+","
		                         			 +jQuery("#residence_county option:selected").text());  
		           
				  	  var certNum = $("#enrolleeinfo_brschool_add_table input[id=certNum]").val();
				  	  if(""==certNum){
				  		  alert("请填写一个证件号码！")
				  		  return false;
				  	  }else{
				  			  var checkExistsURL = "${baseUrl}/edu3/framework/checkexistsuser.html"
					  		  $.get(checkExistsURL,{certNum:certNum},function(data){
					  			  if("true"==data){
					  				  $("#enrolleeinfo_brschool_add_table input[id=isExstisCertNum]").attr("value","true");
					  			  }else{
					  				  $("#enrolleeinfo_brschool_add_table input[id=isExstisCertNum]").attr("value","false");
					  			  }
					  		  })
					  		  var isExists = $("#enrolleeinfo_brschool_add_table input[id=isExstisCertNum]").val();
					  		  if("true"==isExists){
					  			  alert("该证件号已存在！");
					  			  return false;
					  		  }
				  	  }
				  	
					 //如果职业状况选择了从业则需要填写相关信息
	                   if(jQuery('#workStatus').val()=="worked"){
	                	    var message="";
	                	    if(jQuery('#officeName').val()=="")
	                	    {
	                	      message+="请填写工作单位!<br>";
	                	    }
	                	    if(message!="")
	                	    {
	                	     alertMsg.warn(message);
	                	     return false;
	                	    }
	                   }
				  	
					//信息地媒体来源             
                    if(jQuery('#fromMedia').val()=="6" && jQuery('#fromOtherMedia').val()=="" ){
                      alert("请输入信息地媒体来源!");
                      return false;
                    }else{
                    	if(jQuery('#fromMedia').val() == ""){
                    		alert("请选择信息地媒体来源!");
                            return false;
                    	}
                    }               
                   if(jQuery("#inputForm1").validator("alert")){
                   
                   	//报读专什本的学生验证学历相关
	                 var checkClassicURL	="${baseUrl}/edu3/framework/checkclassic.html";
	                 var recruitMajorid 	= jQuery("#recruitMajorid").val();
	                 var graduateSchool 	= jQuery("#graduateSchool").val();
	                 var graduateSchoolCode = jQuery("#graduateSchoolCode").val();
	                 var graduateId 		= jQuery("#graduateId").val();
	                 var certType 			= jQuery("#certType").val();
	                 
                   //验证身份证
     				if(jQuery("#certType").val()=="idcard"){
	                  	var idNumber =jQuery("#certNum").val();
	        			//身份证正则表达式(15位)
						//var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
						//身份证正则表达式(18位)
						//var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/;
                    	//if(isIDCard1.test(idNumber) || isIDCard2.test(idNumber)){  
                    	if(_idCardValidate(idNumber)){	
				        }else{
				       	 	alert("证件号码不合法!");
				        	return false;
				        }
	                  	var url="${baseUrl}/edu3/framework/checkidnumber.html";
	                  	var idNumber =jQuery("#certNum").val();
	       				jQuery.post(url,{idNumber:idNumber,certType:certType},function(msg){
							var isPass = true;
							for(var i = 0;i<msg.length;i++){
								if(msg[i].result =="failure"){
									isPass =  false;
									alert(msg[i].msg);	
								}
								if(msg[i].result =="idcard"){
									alert(msg[i].msg);
									//isPass = true;
									break;
								}
							}
			        		if(isPass == true){
			        			jQuery.post(checkClassicURL,{recruitMajorid:recruitMajorid,graduateSchool:graduateSchool,
			        		          graduateSchoolCode:graduateSchoolCode,graduateId:graduateId},function(msg){
									if(msg!=""){
										alert(msg);
										return false;
									}else{
									   jQuery("#inputForm1").submit();
									}
								});
			        		}
						},"json");
					}else{	
						jQuery.post(checkClassicURL,{recruitMajorid:recruitMajorid,graduateSchool:graduateSchool,
			        		          graduateSchoolCode:graduateSchoolCode,graduateId:graduateId},function(msg){
									if(msg!=""){
										alert(msg);
										return false;
									}else{
									   jQuery("#inputForm1").submit();
									}
						});
					}	
                 }
               });
                 
               jQuery('#addResume').click(function(){
                  var rowNum = jQuery("#studentResume").get(0).rows.length;
				  jQuery("#studentResume").append(
				   "<tr><td width='4%'><q>"+rowNum+"</q>"
					+"<input type='checkbox' id='checkId' value=''/>"
					+"<input type='hidden' name='resumeid' value=''/></td>"
					+"<td width='23%'><gh:select name='startYear' dictionaryCode='CodeYear' value='' />"	           
					+" <gh:select name='startMonth' dictionaryCode='CodeMonth' value='' /></td>"			
					+"<td width='23%'><gh:select name='endYear' dictionaryCode='CodeYear' value='' />"	           
					+" <gh:select name='endMonth' dictionaryCode='CodeMonth' value='' /></td>"				  
					+"<td width='30%'><input type='text' name='company' size='26'  value=''/></td>"			  
					+"<td width='20%'><input type='text' name='resumetitle' size='12'  value=''/></td>"				
					+"</tr>"			
				  );
				
                });
                
          jQuery('#delResume').click(function(){
			tab = jQuery("#studentResume").get(0);
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
			if(idArray.length<1){alert ("请选择一个要删除记录！");return false;}
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
		    
		      var ip = new ImagePreview( $$("uploadPhoto"), $$("uploadimage1"), {maxWidth: 120, maxHeight: 120,onCheck:CheckPreview});
			ip.img.src = ImagePreview.TRANSPARENT;
			ip.file.onchange = function(){ ip.preview(); };
			
			var ip1 = new ImagePreview( $$("idcardPhoto"), $$("uploadimage2"), {maxWidth: 120, maxHeight: 120,onCheck:CheckPreview});
			ip1.img.src = ImagePreview.TRANSPARENT;
			ip1.file.onchange = function(){ ip1.preview(); };
			
			var ip2 = new ImagePreview( $$("graduationPhoto"), $$("uploadimage3"), {maxWidth: 120, maxHeight: 120,onCheck:CheckPreview});
			ip2.img.src = ImagePreview.TRANSPARENT;
			ip2.file.onchange = function(){ ip2.preview(); };	   	  
		      
		     jQuery("#ChinaArea1").jChinaArea({
			//	 aspnet:true,
				 s1:"广东省",//默认选中的省名
		 		 s2:"广州市",//默认选中的市名
			     s3:"天河区"//默认选中的县区名
			 });
			 jQuery("#ChinaArea2").jChinaArea({
			// 	 aspnet:true,
				 s1:"广东省",//默认选中的省名
			     s2:"广州市",//默认选中的市名
				 s3:"天河区"//默认选中的县区名
			 }); 
			jQuery("#recruitPlanId").change(function(){
				var url = "${baseUrl}/edu3/framework/branchshoolMajor/branchMajor-list.html";
				var recruitPlanId =  jQuery(this).val();
				var branchSchoolId = jQuery("#branchSchoolid").val();
				var flag = true;
				if(recruitPlanId==null||recruitPlanId==""){
					flag = false;
					jQuery("#recruitMajorid").html("");
					alert("请选择招生批次!");
				}
				if(flag != false){
					jQuery.post(url,{branchPlanId:recruitPlanId,branchSchoolId:branchSchoolId},function(myJSON){
						jQuery("#recruitMajorid").children().remove();
						jQuery("#recruitMajorid").show();
					  	 var selectObj="<option value=''>请选择</option>";
					 	 for (var i = 0; i < myJSON.length; i++) {
					 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
						 }
					 	 jQuery("#recruitMajorid").html(selectObj);
					},"json");
				}
			});
		});
			
			 //检测程序
		var exts = "jpg|gif", paths = "|";
		function CheckPreview(){
			var value = this.file.value, check = true;
			if ( !value ) {
				check = false; alert("请先选择文件！");
			} else if ( !RegExp( "\.(?:" + exts + ")$$", "i" ).test(value) ) {
				check = false; alert("只能上传以下类型：" + exts);
			} else if ( paths.indexOf( "|" + value + "|" ) >= 0 ) {
				check = false; alert("已经有相同文件！");
			}
			check || ResetFile(this.file);
			return check;
		}
		function grnerateExamCode(){
			showMoreClassicInfo();
			var url = "${baseUrl}/edu3/framework/get-examcode.html";
	    	var recruitMajorId = $("#recruitMajorid").val();
	   		var branchSchoolid = jQuery("#branchSchoolid").val();
	   		var recruitPlanId  = jQuery("#recruitPlanId").val();
	   		var grnerateExamCodeFlag=true;
	   		if(recruitMajorId==null || recruitMajorId==""){
	   			grnerateExamCodeFlag=false;
	   			jQuery("#examCertificateNo").val("");
	   		}
	   		if(grnerateExamCodeFlag != false){
	   			jQuery.post(url,{recruitMajorid:recruitMajorId,branchSchoolid:branchSchoolid},function(myJSON){
					jQuery("#examCertificateNo").val("");
					jQuery("#examCertificateNo").val(myJSON);
					getExamSubjectList(recruitPlanId,recruitMajorId);
				},"json");
	   			$.blockUI({ message: $('#showexamsubject'),css:{top:"10%",left:"20%"}} ); 	
	   		}
		}
		function getExamSubjectList(recruitPlanId,recruitMajorId){
			var url 		= "${baseUrl}/edu3/framework/get-examsubjectlist.html";
			var examSubJect = "";
			jQuery("#examSubjectListBody").html("");
			jQuery.post(url,{recruitMajorId:recruitMajorId,recruitPlanId:recruitPlanId},function(myJSON){
					if(myJSON.length>0){
						for(var i=0;i<myJSON.length;i++){
							examSubJect += "<tr><td>"+(i+1)+"</td><td>"+myJSON[i].courseName+"</td><td>"+myJSON[i].startTimeStr+"-"+myJSON[i].endTimeStr+"</td></tr>"
						}	
					}else{
						examSubJect += "<tr><td colspan='3'>未按排考试课程!</td></tr>"
					}
					jQuery("#examSubjectListBody").html(examSubJect)
			},"json");
		}
		function showMoreClassicInfo(){
			 var recruitMajorid = $("#recruitMajorid").val();
			 var url = "${baseUrl}/edu3/framework/get-RecruitMajorStartPoint.html";
			 jQuery.post(url,{recruitMajorId:recruitMajorid},function(msg){
				 if(null != msg && ""!=msg && "高中"==msg){
					jQuery("#graduateSchoolInfoTr").hide();
			 		jQuery("#graduateInfoTr").hide();
			 		
			 		jQuery("#graduateSchool").removeAttr("validate");
			 		jQuery("#graduateSchoolCode").removeAttr("validate");
			 		jQuery("#graduateId").removeAttr("validate");
			 		jQuery("#graduateMajor").removeAttr("validate");
			 		
			 		jQuery("#showMoreInfoFlag").attr("value","N");
			 		
			 	}else{
			 		jQuery("#graduateSchoolInfoTr").show();
			 		jQuery("#graduateInfoTr").show();
			 		jQuery("#classicCode").attr("value",msg);
			 		jQuery("#showMoreInfoFlag").attr("value","Y");
			 		
			 		jQuery("#graduateSchool").attr("validate","Require");
			 		jQuery("#graduateSchoolCode").attr("validate","Require");
			 		jQuery("#graduateId").attr("validate","Require");
			 		jQuery("#graduateMajor").attr("validate","Require");
			 	}
			 },"json");
		}
		//当选择入学前最高学历为高中、职高时不用填写  入学前学历学校代码 、入学前学历学校名称等信息
		function hideClassicInfo(obj){
			if($(obj).val()==1){
				
		 		
		 		jQuery("#graduateSchool").removeAttr("validate");
		 		jQuery("#graduateSchoolCode").removeAttr("validate");
		 		jQuery("#graduateId").removeAttr("validate");
		 		jQuery("#graduateMajor").removeAttr("validate");
		 		
		 		jQuery("#graduateSchoolInfoTr").hide();
		 		jQuery("#graduateInfoTr").hide();
		 		
			}else{
				var showMoreInfoFlag =  jQuery("#showMoreInfoFlag").val();
				if("Y"==showMoreInfoFlag){
					jQuery("#graduateSchoolInfoTr").show();
			 		jQuery("#graduateInfoTr").show();
					
			 		jQuery("#graduateSchool").attr("validate","Require");
			 		jQuery("#graduateSchoolCode").attr("validate","Require");
			 		jQuery("#graduateId").attr("validate","Require");
			 		jQuery("#graduateMajor").attr("validate","Require");
				}
			}
		}
		
		function closeBlockUI(){
			
			$.unblockUI();
		}
	 </script>

	<div style="height: 680px;" id="demo1">
		<form id="inputForm1"
			action="${baseUrl}/edu3/recruit/enroll/webenrolleeinfo-save.html"
			method="post" enctype="multipart/form-data">
			<input type="hidden" " name="isSpeciallycode"
				value="${isSpeciallycode }" />
			<div id="container"
				style="width: 98%; margin-left: -200px; margin-top: -40px; text-align: left;">
				<div
					style="height: 20px; padding-left: 12px; text-align: left; color: red">
					<b>注意事项：往年曾报过名的报考人员和已经在读的进修生不能使用本功能!请亲临现场报名。</b>
				</div>
				<div class="ui-tab-container">
					<ul class="clearfix ui-tab-list">
						<li class="ui-tab-active"><b>第一步：填写报名信息</b></li>
						<li><b>第二步：填写个人信息</b></li>
					</ul>
					<div id="showexamsubject"
						style="width: 600px; height: 150px; padding: 4px; border: 2px #0b5fa5 solid; background: #66a1d2; display: none">
						<table id="examSubjectList" class="form">
							<tr>
								<td width="10%" align="center"><b>编号</b></td>
								<td width="50%" align="center"><b>课程名称</b></td>
								<td width="40%" align="center"><b>考试时间</b></td>
							</tr>
							<tbody id="examSubjectListBody">

							</tbody>
						</table>
						<input type="button" name="back" value="关闭"
							onclick="closeBlockUI()" class="input_but" />
					</div>
					<div class="ui-tab-bd">
						<div class="ui-tab-content">
							<table class="form" id="fragment-1">
								<tr>
									<td width="10%">招生批次:</td>
									<td width="40%"><select id="recruitPlanId"
										name="recruitPlan" validate="Require" mes="招生批次"
										style="width: 50%">
											<option value="">请选择招生批次</option>
											<c:forEach items="${planList }" var="recruitPlan">
												<option value="${recruitPlan.resourceid }"
													<c:if test="${defaultPlanId eq recruitPlan.resourceid}">selected="selected"</c:if>>${recruitPlan.recruitPlanname }</option>
											</c:forEach>
									</select> <font color='red'>*</font></td>
									<td width="10%">教学站:</td>
									<td width="40%"><select id="branchSchoolid"
										name="branchSchool" mes="教学站" validate="Require"
										style="width: 50%">
											<option value="${branschool.resourceid }" selected="selected">${branschool.unitName }</option>
									</select> <font color='red'>* 选择您的学习地点</font></td>

								</tr>
								<tr>
									<td>招生专业:</td>
									<td><select id="recruitMajorid" name="recruitMajor"
										onchange="grnerateExamCode();" validate="Require" mes="招生专业"
										style="width: 50%">
											<option value="">请选择专业</option>
											<c:forEach items="${defaultPlanMajor }" var="major">
												<option value="${major.resourceid }">${major.recruitMajorName }</option>
											</c:forEach>
									</select> <font color='red'>*</font></td>
									<td>准考证号:</td>
									<td><input type="text" id="examCertificateNo"
										name="examCertificateNo" size="34"
										value="${enrolleeinfo.examCertificateNo }" readonly="true"
										validate="Require" mes="准考证号" style="width: 50%" /> <font
										color='red'>*</font></td>
								</tr>
								<tr>
									<td>申请免试:</td>
									<td><input type="radio" CHECKED value="N"
										name="isApplyNoexam"> 不申请 <input type="radio"
										<c:if test="${enrolleeinfo.isApplyNoexam=='Y'}">CHECKED</c:if>
										value="Y" name="isApplyNoexam"> 申请</td>
									<td>入学前国民教育最高学历层次:</td>
									<td><gh:select name="educationalLevel"
											dictionaryCode="CodeEducationalLevel"
											value="${enrolleeinfo.educationalLevel}"
											onchange="hideClassicInfo(this);" validate="Require"
											mes="入学前国民教育最高学历层次" style="width: 50%" /> <font color='red'>*</font>
									</td>
								</tr>
								<input type="hidden" " id="showMoreInfoFlag"
									name="showMoreInfoFlag" />
								<tr id="graduateSchoolInfoTr" style="display: none;">
									<td>入学前学历学校名称:</td>
									<td><input type="text" id="graduateSchool"
										name="graduateSchool" size="34"
										value="${enrolleeinfo.graduateSchool }" mes="入学前学历学校名称" /><font
										color='red'>*</font></td>
									<td>入学前学历学校代码:</td>
									<td><input type="text" id="graduateSchoolCode"
										name="graduateSchoolCode" size="34"
										value="${enrolleeinfo.graduateSchoolCode }" maxlength="5"
										validate="Number" mes="入学前学历学校代码" />(毕业证书号的前五位)<font
										color='red'>*</font></td>
								</tr>
								<tr id="graduateInfoTr" style="display: none;">
									<td>入学前学历证书编号:</td>
									<td><input type="text" id="graduateId" name="graduateId"
										size="34" value="${enrolleeinfo.graduateId }" mes="入学前学历证书编号" /></td>
									<td>毕业专业:</td>
									<td><input type="text" name="graduateMajor" size="34"
										value="${enrolleeinfo.graduateMajor }" mes="毕业专业" /></td>
								</tr>
								<tr>
									<td>入学前最高学历毕业日期:</td>
									<td><input type="text" name="graduateDate" size="34"
										class="Wdate" style="width: 50%" validate="Require,Date2"
										onfocus="WdatePicker({isShowWeek:true})"
										value="${enrolleeinfo.graduateDate}" mes="入学前最高学历毕业日期" /> <font
										color='red'>*</font></td>
									<td>信息地媒体来源：</td>
									<td><gh:select id="fromMedia" name="fromMedia"
											dictionaryCode="CodeFromMedia"
											value="${enrolleeinfo.fromMedia}" validate="Require"
											mes="信息地媒体来源" style="width: 20%" /> <input
										id="fromOtherMedia" type="text" name="fromOtherMedia"
										size="18" value="${enrolleeinfo.fromOtherMedia}"
										readonly="true" style="width: 30%" /> <font color='red'>*</font></td>
									</td>
								</tr>
							</table>
							<br />
							<br />
							<table align="center" width="90%">
								<tr>
									<td style="text-align: center"><input type="button"
										id="next" value="下一步" class="b1"
										onmouseover="this.className='b2'"
										onmouseout="this.className='b1'" /> <c:if
											test="${enrolleeType==0}">
											<input type="button" id="back" value="返回" class="b1"
												onmouseover="this.className='b2'"
												onmouseout="this.className='b1'" />
										</c:if></td>
								</tr>
							</table>
						</div>
						<div class="ui-tab-content" style="display: none">
							<input type="hidden" id="homePlace" name="homePlace" value="" />
							<input type="hidden" id="residence" name="residence" value="" />
							<input type="hidden" name="enrolleeType" value="${enrolleeType}" />
							<table id="enrolleeinfo_brschool_add_table" class="form"
								style="width: 100%">
								<tr>
									<td width="10%">姓名:</td>
									<td colspan="3"><input type="text" name="name" size="34"
										value="${name}"
										validate="Require,Chinese,LimitLen(min:4|max:40)" mes="姓名" />
										<font color='red'>* 填写中文汉字</font></td>
								</tr>
								<tr>
									<td width="10%">证件类别:</td>
									<td width="40%"><gh:select id="certType" name="certType"
											dictionaryCode="CodeCertType" value="${student.certType}"
											validate="Require" mes="证件类别" /> <font color='red'>*</font></td>
									<td width="10%">证件号码：</td>
									<td width="40%"><input type="hidden" id="isExstisCertNum"
										name="isExstisCertNum" value="false" /> <input type="text"
										id="certNum" name="certNum" size="34" value="${certNum}"
										validate="Require" maxlength="18" mes="证件号码" /> <font
										color='red'>*</font></td>
								</tr>
								<tr>
									<td>出生日期:</td>
									<td><input type="text" name="bornDay" size="34"
										readonly="readonly" value="${student.bornDay }" class="Wdate"
										validate="Require,Date2"
										onfocus="WdatePicker({isShowWeek:true})" mes="出生日期" /> <font
										color='red'>*</font></td>
									<td>性别:</td>
									<td><gh:select name="gender" dictionaryCode="CodeSex"
											value="${student.gender}" validate="Require" mes="性别" /> <font
										color='red'>*</font></td>
								</tr>
								<tr>
									<td>籍贯:</td>
									<td>
										<div id="ChinaArea1">
											<select id="homePlace_province" name="homePlace_province"
												style="width: 100px;"></select> <select id="homePlace_city"
												name="homePlace_city" style="width: 100px;"></select> <select
												id="homePlace_county" name="homePlace_county"
												style="width: 120px;">
											</select>
										</div>
									</td>
									<td>户口所在地:</td>
									<td>
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
									<td>民族:</td>
									<td><gh:select name="nation" dictionaryCode="CodeNation"
											value="${student.nation}" validate="Require" mes="民族" /> <font
										color='red'>*</font></td>
									<td>政治面目:</td>
									<td><gh:select name="politics"
											dictionaryCode="CodePolitics" value="${student.politics}"
											validate="Require" mes="政治面目" /> <font color='red'>*</font></td>
								</tr>
								<tr>
									<td>婚否:</td>
									<td><gh:select name="marriage"
											dictionaryCode="CodeMarriage" value="${student.marriage}"
											validate="Require" mes="婚否" /> <font color='red'>*</font></td>
									<td>工作岗位</td>
									<td><input type="text" name="title" size="34"
										value="${student.title}" /></td>
								</tr>
								<tr>
									<td>职业状况:</td>
									<td><gh:select id="workStatus" name="workStatus"
											dictionaryCode="CodeWorkStatus" value="${student.workStatus}"
											mes="职业状况" /></td>
									<td>从业行业:</td>
									<td><gh:select id="industryType" name="industryType"
											dictionaryCode="CodeIndustryType"
											value="${student.industryType}" mes="从业行业" /></td>
								</tr>
								<tr>
									<td>工作单位:</td>
									<td><input type="text" id="officeName" name="officeName"
										size="34" value="${student.officeName }"
										validate="LimitLen(max:200)" mes="工作单位" /></td>
									<td>单位电话:</td>
									<td><input type="text" id="officePhone" name="officePhone"
										size="34" maxlength="12" value="${student.officePhone }" /></td>
								</tr>
								<tr>
									<td>地址：</td>
									<td><input type="text" name="contactAddress" size="34"
										value="${student.contactAddress }"
										validate="Require,LimitLen(max:200)" mes="本人地址" /> <font
										color='red'>*</font> <!-- <font color='red'>* 用于寄送通知书,请填写准确</font> --></td>
									<td>邮编:</td>
									<td><input type="text" name="contactZipcode" size="34"
										value="${student.contactZipcode }" maxlength="6"
										validate="Require,PostalCode" mes="本人地址邮编" /> <font
										color='red'>*</font></td>
								</tr>
								<tr>
									<td>固定电话:</td>
									<td><input type="text" name="contactPhone" size="34"
										value="${student.contactPhone }" validate="PhoneCall"
										mes="固定电话" /></td>
									<td>手机号码:</td>
									<td><input type="text" name="mobile" size="34"
										value="${student.mobile }" validate="Require,Mobile"
										maxlength="11" mes="手机号码" /> <font color='red'>*
											为方便联系,请填写准确</font></td>

								</tr>
								<tr>

									<td>电子邮件：</td>
									<td><input type="text" name="email" size="34"
										value="${student.email }" validate="Require,Email" mes="电子邮件" />
										<font color='red'>* 为方便联系,请填写准确</font></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>上传相片:</td>
									<td>
										<div id="filelist1"></div> <input type="file" id="uploadPhoto"
										name="uploadPhoto" size="40" />
									</td>
									<td>上传身份证复印件:</td>
									<td>
										<div id="filelist2"></div> <input type="file" id="idcardPhoto"
										name="idcardPhoto" size="40" />
									</td>
								</tr>
								<tr>
									<td>上传毕业证复印件:</td>
									<td>
										<div id="filelist3"></div> <input type="file"
										id="graduationPhoto" name="graduationPhoto" size="40" />
									</td>
									<td>备注说明:</td>
									<td><textarea name="memo" cols="30" rows="2"
											style="width: 300px">${student.memo }</textarea></td>
								</tr>
								<tr>
									<td>图片预览：</td>
									<td colspan="3">
										<div
											style="overflow: hidden; height: 120px; border: 1px solid ccc;">
											<div style="width: 100px; float: left; margin-left: 4px">
												<img id="uploadimage1" src="" />
											</div>
											<div style="width: 100px; float: left; margin-left: 4px">
												<img id="uploadimage2" src="" />
											</div>
											<div style="width: 100px; float: left; margin-left: 4px">
												<img id="uploadimage3" src="" />
											</div>
										</div>
									</td>
								</tr>
							</table>

							<div style="padding-top: 4px; padding-bottom: 4px">
								<strong>填写个人简历</strong>
							</div>
							<table class="form" id="studentResume" width="100%">
								<tr>
									<td colspan="5" style="text-align: right"><input
										type="button" id="addResume" value="新增工作经历" class="b1"
										onmouseover="this.className='b2'"
										onmouseout="this.className='b1'">&nbsp; <input
										type="button" id="delResume" value="删除" class="b1"
										onmouseover="this.className='b2'"
										onmouseout="this.className='b1'">&nbsp;</td>
								</tr>
								<tr>
									<td width="4%" align="center"></td>
									<td width="20%" align="center">开始年月</td>
									<td width="20%" align="center">截止年月</td>
									<td width="36%" align="center">学习或工作单位</td>
									<td width="20%" align="center">职务</td>
								</tr>
								<c:forEach items="${student.studentResume}" var="resume"
									varStatus="vs">
									<tr>
										<td width="4%"><q>${vs.index+1}</q> <input
											type="checkbox" id="checkId" value="${resume.resourceid}">
											<input type="hidden" name="resumeid"
											value="${resume.resourceid}"></td>
										<td width="20%"><gh:select name="startYear"
												dictionaryCode="CodeYear" value="${resume.startYear}" /> <gh:select
												name="startMonth" dictionaryCode="CodeMonth"
												value="${resume.startMonth}" /></td>
										<td width="20%"><gh:select name="endYear"
												dictionaryCode="CodeYear" value="${resume.endYear}" /> <gh:select
												name="endMonth" dictionaryCode="CodeMonth"
												value="${resume.endMonth}" /></td>
										<td width="36%"><input type="text" name="company"
											size="34" value="${resume.company}" /></td>
										<td width="20%"><input type="text" name="resumetitle"
											size="34" value="${resume.title}" /></td>
									</tr>
								</c:forEach>
							</table>
							<br />
							<table align="center" width="90%">
								<tr>
									<td style="text-align: center"><input type="button"
										id="prev" value="上一步" class="b1"
										onmouseover="this.className='b2'"
										onmouseout="this.className='b1'" /> <input type="button"
										id="save" value="保存" class="b1"
										onmouseover="this.className='b2'"
										onmouseout="this.className='b1'" /></td>
								</tr>
							</table>

						</div>
					</div>
				</div>
			</div>
		</form>
	</div>

</body>
