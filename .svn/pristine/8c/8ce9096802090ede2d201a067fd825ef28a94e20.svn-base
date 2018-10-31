<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重新报名表单</title>
<script type="text/javascript">
jQuery(document).ready(function(){
	var isRecruitUser   = "${isRecruitUser }";
	/**~~~~~~~~~~~~~~~~~~~~1.初始化生成准考证号~~~~~~~~~~~~~~~~~~~~**/
	 jQuery("#_enrolleeinf_reform #recruitMajorid").change(function(){
		 var url = "${baseUrl}/edu3/framework/get-examcode.html";
         var recruitMajorStartPointURL = "${baseUrl}/edu3/framework/get-RecruitMajorStartPoint.html";
		 var recruitMajorid = jQuery(this).val();
		 var branchSchoolid = jQuery("#_enrolleeinf_reform #branchSchoolid").val();
		 var recruitPlanId  = jQuery("#_enrolleeinf_reform #recruitPlanId").val();
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
		if(flag ==  false){
			$("#_enrolleeinf_reform #examCertificateNo").val("");
		}
		   		 
		jQuery.post(url,{recruitMajorid:recruitMajorid,branchSchoolid:branchSchoolid},function(myJSON){
			jQuery("#_enrolleeinf_reform #examCertificateNo").val("");
			jQuery("#_enrolleeinf_reform #examCertificateNo").val(myJSON);
			jQuery.post(recruitMajorStartPointURL,{recruitMajorId:recruitMajorid},function(msg){
				if(null != msg && ""!=msg && "高中"==msg){
					jQuery("#_enrolleeinf_reform #graduateSchoolInfoTr").hide();
			 		jQuery("#_enrolleeinf_reform #graduateInfoTr").hide();
			 		jQuery("#_enrolleeinf_reform #showMoreInfoFlag").attr("value","N");
			 		
			 		jQuery("#_enrolleeinf_reform #graduateSchool").removeAttr("class");
			 		jQuery("#_enrolleeinf_reform #graduateSchoolCode").removeAttr("class");
			 		jQuery("#_enrolleeinf_reform #graduateId").removeAttr("class");
			 		jQuery("#_enrolleeinf_reform #graduateMajor").removeAttr("class");
			 		
			 	}else{
			 		jQuery("#_enrolleeinf_reform #graduateSchoolInfoTr").show();
			 		jQuery("#_enrolleeinf_reform #graduateInfoTr").show();
			 		jQuery("#_enrolleeinf_reform #classicCode").attr("value",msg);
			 		jQuery("#_enrolleeinf_reform #showMoreInfoFlag").attr("value","Y");
					 		
			 		jQuery("#_enrolleeinf_reform #graduateSchool").attr("class","required");
			 		jQuery("#_enrolleeinf_reform #graduateSchoolCode").attr("class","required");
			 		jQuery("#_enrolleeinf_reform #graduateId").attr("class","required");
			 		jQuery("#_enrolleeinf_reform #graduateMajor").attr("class","required");
			 	}
			},"json");
		},"json");
	   });
	/**~~~~~~~~~~~~~~~~~~~~~~~2.初始化招生批次、专业下拉~~~~~~~~~~~~~~~~~~~**/	
	
	//招生办用户可更改教学站
	if("Y"==isRecruitUser){
		
		 jQuery("#_enrolleeinf_reform #recruitPlanId").change(function(){
				
				var url 		  = "${baseUrl}/edu3/framework/branchshoolplan/branchSchoolPlan-list.html";
				var recruitPlanId =  jQuery(this).val();
				//如果当前用户是教学站用户，此变量值不为空
				var currentUserBrs= "${branchSchoolid}";   
				var flag 		  = true;
				if(recruitPlanId==null||recruitPlanId==""){
					flag = false;
					jQuery("#_enrolleeinf_reform #branchSchoolid").children().remove();
					alertMsg.warn("请选择一个招生批次!");
				}
				$("#_enrolleeinf_reform #branchSchoolid").children().remove();
				$("#_enrolleeinf_reform #recruitMajorid").children().remove();
				$("#_enrolleeinf_reform #examCertificateNo").val("");
				if(flag != false){
					jQuery.post(url,{recruitPlanId:recruitPlanId},function(myJSON){
							jQuery("#_enrolleeinf_reform #branchSchoolid").children().remove(); 
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
						 	 jQuery("#_enrolleeinf_reform #branchSchoolid").html(selectObj);
					},"json");
				}
		   });
		
		jQuery("#_enrolleeinf_reform #branchSchoolid").change(function(){
   		 	
			var url 		   = "${baseUrl}/edu3/framework/branchshoolMajor/branchMajor-list.html";
			var recruitPlanId  = jQuery("#_enrolleeinf_reform #recruitPlanId option:selected").val();
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
					jQuery("#_enrolleeinf_reform #recruitMajorid").children().remove();
					jQuery("#_enrolleeinf_reform #recruitMajorid").show();
				  	 var selectObj="<option value=''>请选择</option>";
				 	 for (var i = 0; i < myJSON.length; i++) {
				 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
					 }
				 	 jQuery("#_enrolleeinf_reform #recruitMajorid").html(selectObj);
				},"json");
			}else{
				$("#_enrolleeinf_reform #recruitMajorid").children().remove();
				$("#_enrolleeinf_reform #examCertificateNo").val("");
			}
			
	   });
	  
	}else{
		jQuery("#_enrolleeinf_reform #recruitPlanId").change(function(){
			var url = "${baseUrl}/edu3/framework/branchshoolMajor/branchMajor-list.html";
			var recruitPlanId =  jQuery("#_enrolleeinf_reform #recruitPlanId").val();			
			var brSchoolId    = "${brSchoolId}";
			//当前用户不是教学站
			if( null==brSchoolId ||""== brSchoolId){
				brSchoolId    =  jQuery("#_enrolleeinf_reform #branchSchoolid").val();	
			}
			var flag = true;
			if(recruitPlanId==null||recruitPlanId==""){
				flag = false;
				("#_enrolleeinf_reform #recruitMajorid").children().remove();
				alert("请选择招生批次!");
			}
			
			$("#_enrolleeinf_reform #recruitMajorid").children().remove();
			$("#_enrolleeinf_reform #examCertificateNo").val("");
			if(flag != false){
				jQuery.post(url,{branchPlanId:recruitPlanId,branchSchoolId:brSchoolId},function(myJSON){
					  	 var selectObj="<option value=''>请选择</option>";
					 	 for (var i = 0; i < myJSON.length; i++) {
					 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
							}
					 	 jQuery("#_enrolleeinf_reform #recruitMajorid").html(selectObj);
				},"json");
			}
		});
	}	
		
		
	
		 jQuery('#_enrolleeinf_reform #fromMedia').change(function(){
	            if($('#_enrolleeinf_reform #fromMedia').val()=="6"){
	                  alertMsg.warn("请在右边的文本框里输入其他信息");
	                  $('#_enrolleeinf_reform #fromOtherMedia').attr("readonly","false");
	           }else{
	                $('#_enrolleeinf_reform #fromOtherMedia').val("");
	             	$('#_enrolleeinf_reform #fromOtherMedia').attr("readonly","true");
	             }
	           return false;
	        }); 
}); 


///保存回调
function save(form){
 //籍贯	
	jQuery("#_enrolleeinf_reform #homePlace").val(jQuery("#_enrolleeinf_reform #homePlace_province option:selected").text()+","
	                  +jQuery("#_enrolleeinf_reform #homePlace_city option:selected").text()+","
	                  +jQuery("#_enrolleeinf_reform #homePlace_county option:selected").text());
	 //户口  
	 jQuery("#_enrolleeinf_reform #residence").val(jQuery("#_enrolleeinf_reform #residence_province option:selected").text()+","
	                       +jQuery("#_enrolleeinf_reform #residence_city option:selected").text()+","
	                        +jQuery("#_enrolleeinf_reform #residence_county option:selected").text());  
    //验证身份证
    if(jQuery("#_enrolleeinf_reform #certType").val()=="idcard"){
	     var idNumber =jQuery("#_enrolleeinf_reform #certNum").val();
	     if(_idCardValidate(idNumber)){ 
				$("#_enrolleeinf_reform #certNum").css({"backgroundColor":"#fff"});
	        }else{
	       	 	alertMsg.warn("身份证号码不合法!");
	       	 	$("#_enrolleeinf_reform #certNum").css({"backgroundColor":"#ffc8c8"});
	        	return false;
	        }
      
      }
		 //如果职业状况选择了从业则需要填写相关信息
      if(jQuery('#_enrolleeinf_reform #workStatus').val()=="worked"){
            var message="";
            if(jQuery('#_enrolleeinf_reform #officeName').val()=="") {
              	 message+="请填写工作单位!<br>";
             }
             if(message!="") {
              	alertMsg.warn(message);
              	return false;
              }
           }
		  //报读专什本的学生验证学历相关
       if(jQuery('#_enrolleeinf_reform #classiccode').val()=="2"){
           var message="";
           if(jQuery('#_enrolleeinf_reform #graduateSchool').val()=="") {
               message+="入学前学历学校名称不能为空!<br>";
             }
           if(jQuery('#_enrolleeinf_reform #graduateSchoolCode').val()=="")  {
                message+="入学前学历学校代码不能为空!<br>";
           }else if(jQuery('#_enrolleeinf_reform #graduateSchoolCode').val().length!=5){
              	 message+="入学前学历学校代码是毕业证书号的前五位!<br>";
            }
            if(jQuery('#_enrolleeinf_reform #graduateId').val()=="")  {
              	message+="入学前学历证书编号不能为空!<br>";
            }
           if(message!="") {
                alertMsg.warn(message);
                return false;
             }
           }
         	//信息地媒体来源             
         if(jQuery('#_enrolleeinf_reform #fromMedia').val()=="6" && jQuery('#_enrolleeinf_reform #fromOtherMedia').val()=="" ) {
                alertMsg.warn("请输入信息地媒体来源!");
                  return false;
            }else{
                	if(jQuery('#_enrolleeinf_reform #fromMedia').val() == ""){
                 		alertMsg.warn("请选择信息地媒体来源!");
                      return false;
                	}
           } 
           return validateCallback(form,_reloadEnrollList);
       }
       
	function _reloadEnrollList(json){
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			//关闭当前table，并刷新列表页
			navTab.closeCurrentTab();
			if (json.navTabId){
				navTab.reload(json.reloadUrl, {}, json.navTabId);
			}		
		}
	}
 </script>
</head>
<body>
<body>
	<h2 class="contentTitle">${enrolleeinfo.studentBaseInfo.name }
		-
		<c:choose>
			<c:when test="${not empty newMajorForm }">老生报新专业</c:when>
			<c:otherwise>重新报名</c:otherwise>
		</c:choose>

	</h2>
	<div class="page">
		<div class="pageContent">
			<form id="_enrolleeinf_reform" method="post"
				action="${baseUrl}/edu3/recruit/enroll/resave.html" class="pageForm"
				onsubmit="return save(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>填写报名信息</span></a></li>
									<li><a href="#"><span>学生信息</span> </a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<input type="hidden" name="resourceid"
										value="${enrolleeinfo.resourceid }" />
									<input type="hidden" name="signupDate"
										value="<fmt:formatDate value="${enrolleeinfo.signupDate }" pattern="yyyy-MM-dd" />" />
									<input type="hidden" name="studentId"
										value="${enrolleeinfo.studentBaseInfo.resourceid }">
									<%--显示原招生批次、专业、教学站 --%>
									<c:if test="${null!=enrolleeinfo }">
										<tr>
											<td style="color: blue">原招生批次:</td>
											<td>${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname }</td>
											<td style="color: blue">原教学站:</td>
											<td>${enrolleeinfo.branchSchool.unitName }</td>
										</tr>
										<tr>
											<td style="color: blue">原招生专业:</td>
											<td>${enrolleeinfo.recruitMajor.recruitMajorName }</td>
											<td style="color: blue">原准考证号:</td>
											<td>${enrolleeinfo.examCertificateNo}</td>
										</tr>
									</c:if>
									<%--显示原招生批次、专业、教学站 --%>
									<tr>
										<td>招生批次:</td>
										<td><select id="recruitPlanId" name="recruitPlan"
											class="required" style="width: 52%">
												<option value="">请选择招生批次</option>
												<%--  <c:if test="${null!=enrolleeinfo }"> <option value="${enrolleeinfo.recruitMajor.recruitPlan.resourceid }" selected="selected">${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname }</option> </c:if>--%>
												<c:forEach items="${publishedPlanList }" var="recruitPlan">
													<option value="${recruitPlan.resourceid }"
														<c:if test="${recruitPlan.resourceid eq enrolleeinfo.recruitMajor.recruitPlan.resourceid}"> selected="selected" </c:if>>${recruitPlan.recruitPlanname }</option>
												</c:forEach>
										</select> <font color='red'>* </font></td>
										<td>教学站:</td>
										<td><select id="branchSchoolid" name="branchSchool"
											class="required" style="width: 52%">
												<c:choose>
													<c:when test="${ isRecruitUser ne 'Y' }">
														<option value="${enrolleeinfo.branchSchool.resourceid }"
															selected="selected">${enrolleeinfo.branchSchool.unitName }</option>
													</c:when>
													<c:otherwise>
														<option value="">请选择</option>
													</c:otherwise>
												</c:choose>
										</select> <font color='red'>* </font></td>

									</tr>
									<tr>
										<td>招生专业:</td>
										<td><select id="recruitMajorid" name="recruitMajor"
											class="required" style="width: 52%">
												<option value="">请选择</option>
												<%-- <option value="${enrolleeinfo.recruitMajor.resourceid }" selected="selected" >${enrolleeinfo.recruitMajor.recruitMajorName }</option>--%>
										</select> <font color='red'>* </font></td>
										<td><c:choose>
												<c:when test="${newMajorForm eq 'Y' }">

													<%-- <input type="text" id="examCertificateNo" name="examCertificateNo" size="34" value="" class="required" readonly />--%>
												</c:when>
												<c:otherwise>
										准考证号:
									</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${newMajorForm eq 'Y' }">
													<%-- <input type="text" id="examCertificateNo" name="examCertificateNo" size="34" value="" class="required" readonly />--%>
												</c:when>
												<c:otherwise>
													<input type="text" id="examCertificateNo"
														name="examCertificateNo" size="34"
														value="${enrolleeInfo.examCertificateNo }"
														class="required" readonly />
												</c:otherwise>
											</c:choose></td>
									</tr>
									<tr>
										<td>申请免试:</td>
										<td><input type="radio"
											<c:if test="${enrolleeinfo.isApplyNoexam eq 'N'}">CHECKED</c:if>
											value="N" name="isApplyNoexam"> 不申请 <input
											type="radio"
											<c:if test="${enrolleeinfo.isApplyNoexam eq 'Y'}">CHECKED</c:if>
											value="Y" name="isApplyNoexam"> 申请</td>
										<td>入学前国民教育最高学历层次:</td>
										<td><gh:select name="educationalLevel"
												dictionaryCode="CodeEducationalLevel"
												value="${enrolleeinfo.educationalLevel}" classCss="required" />
										</td>
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
										<td>入学前学历学校名称:</td>
										<td><input type="text" id="graduateSchool"
											name="graduateSchool" size="34"
											value="${enrolleeInfo.graduateSchool }" /></td>
										<td>入学前学历学校代码:</td>
										<td><input type="text" id="graduateSchoolCode"
											name="graduateSchoolCode" size="34"
											value="${enrolleeInfo.graduateSchoolCode }" maxlength="5" />
											(毕业证书号的前五位)</td>
									</tr>
									<tr id="graduateInfoTr"
										<c:if test="${ isDisplayGraduateSchool eq 'N'}">style="display:none;"</c:if>>
										<td>入学前学历证书编号:</td>
										<td><input type="text" id="graduateId" name="graduateId"
											size="34" value="${enrolleeInfo.graduateId }" /></td>
										<td>毕业专业:</td>
										<td><input type="text" id="graduateMajor"
											name="graduateMajor" size="34"
											value="${enrolleeInfo.graduateMajor }" /></td>
									</tr>
									<tr>
										<td>入学前最高学历毕业日期:</td>
										<td><input type="text" name="graduateDate" size="34"
											class="Wdate" onfocus="WdatePicker({isShowWeek:true})"
											class="required" value="${enrolleeinfo.graduateDate}" /> <font
											color='red'>* </font></td>
										<td>信息地媒体来源：</td>
										<td><gh:select id="fromMedia" name="fromMedia"
												dictionaryCode="CodeFromMedia"
												value="${enrolleeinfo.fromMedia}" classCss="required" /> <input
											id="fromOtherMedia" type="text" name="fromOtherMedia"
											size="18" value="${enrolleeinfo.fromOtherMedia}"
											readonly="true" /> <font color='red'>* </font></td>
									</tr>
								</table>
							</div>
							<div>
								<input type="hidden" id="classicCode" name="classicCode"
									value="0" />
								<c:set var="isPermision" value="N" />
								<c:set var="isAble" value="true" />
								<security:authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_ADMIN">
									<c:set var="isPermision" value="Y" />
									<c:set var="isAble" value="false" />
								</security:authorize>
								<table class="form">
									<tr>
										<td>姓名:</td>
										<td colspan="3"><input type="text" name="name" size="34"
											class="required"
											value="${enrolleeinfo.studentBaseInfo.name }"
											<c:if test="${isPermision eq 'N' }">readonly="readonly"</c:if>
											maxlength="20" /></td>
									</tr>
									<tr>
										<td>证件类别:</td>
										<td><gh:select id="certType" name="certType"
												classCss="required" disabled="${isAble }"
												dictionaryCode="CodeCertType"
												value="${enrolleeinfo.studentBaseInfo.certType}" /></td>
										<td>证件号码：</td>
										<td><input type="text" id="certNum" name="certNum"
											size="34" class="required"
											<c:if test="${isPermision eq 'N' }">readonly="readonly"</c:if>
											value="${enrolleeinfo.studentBaseInfo.certNum}"
											maxlength="18" /></td>
									</tr>
									<tr>
										<td>出生日期:</td>
										<td><input type="text" id="bornDay" name="bornDay"
											size="34" value="${enrolleeinfo.studentBaseInfo.bornDay }"
											class="required Wdate"
											onfocus="WdatePicker({isShowWeek:true})" /></td>
										<td>性别:</td>
										<td><gh:select name="gender" dictionaryCode="CodeSex"
												classCss="required"
												value="${enrolleeinfo.studentBaseInfo.gender}" /></td>
									</tr>

									<tr>
										<td>民族:</td>
										<td><gh:select name="nation" dictionaryCode="CodeNation"
												classCss="required"
												value="${enrolleeinfo.studentBaseInfo.nation}" /></td>
										<td>政治面目:</td>
										<td><gh:select name="politics"
												dictionaryCode="CodePolitics" classCss="required"
												value="${enrolleeinfo.studentBaseInfo.politics}" /></td>
									</tr>
									<tr>
										<td>婚否:</td>
										<td><gh:select name="marriage"
												dictionaryCode="CodeMarriage" classCss="required"
												value="${enrolleeinfo.studentBaseInfo.marriage}" /></td>
										<td>职位职称:</td>
										<td><input type="text" name="title" size="34"
											value="${enrolleeinfo.studentBaseInfo.title}" /></td>
									</tr>
									<tr>
										<td>职业状态:</td>
										<td><gh:select id="workStatus" name="workStatus"
												dictionaryCode="CodeWorkStatus"
												value="${enrolleeinfo.studentBaseInfo.workStatus}"
												classCss="required" /></td>
										<td>行业类别:</td>
										<td><gh:select id="industryType" name="industryType"
												dictionaryCode="CodeIndustryType"
												value="${enrolleeinfo.studentBaseInfo.industryType}" /></td>
									</tr>
									<tr>
									</tr>
									<tr>
										<td>工作单位:</td>
										<td><input type="text" id="officeName" name="officeName"
											size="34" value="${enrolleeinfo.studentBaseInfo.officeName }"
											maxlength="50" /></td>
										<td>单位电话:</td>
										<td><input type="text" id="officePhone"
											name="officePhone" size="34"
											value="${enrolleeinfo.studentBaseInfo.officePhone }"
											maxlength="12" /></td>
									</tr>
									<tr>
										<td width="12%">地址：</td>
										<td width="38%"><input type="text" name="contactAddress"
											size="34"
											value="${enrolleeInfo.studentBaseInfo.contactAddress }"
											class="required" maxlength="100" />
										<!--  <font color='red'>* 用于寄送通知书,请填写准确</font>--></td>
										<td width="12%">邮编:</td>
										<td width="38%"><input type="text" name="contactZipcode"
											size="34"
											value="${enrolleeInfo.studentBaseInfo.contactZipcode }"
											maxlength="6" class="required postcode" /></td>
									</tr>
									<tr>
										<td>固定电话:</td>
										<td><input type="text" name="contactPhone" size="34"
											value="${enrolleeinfo.studentBaseInfo.contactPhone }"
											class="phone" /></td>
										<td>手机号码:</td>
										<td><input type="text" name="mobile" size="34"
											class="required mobile"
											value="${enrolleeinfo.studentBaseInfo.mobile }"
											maxlength="11" /></td>

									</tr>
									<tr>
										<td>电子邮件:</td>
										<td><input type="text" name="email" size="34"
											style="width: 38%"
											value="${enrolleeinfo.studentBaseInfo.email }" class="email" />
										</td>
										<td></td>
										<td></td
									</tr>

								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
</body>
</html>