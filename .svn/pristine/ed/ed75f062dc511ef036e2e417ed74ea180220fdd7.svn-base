<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申报专业表单</title>

</head>
<body>
	<script type="text/javascript">

	//var isAllowNewMajor     = '${isAllowNewMajor}';//是否允许申报新专业
	function brsrecruitplan_submitForm(type,callback){
		var recruitMajorArray = new Array();
		
		$("#majorTable select[name='recruitMajor']>option:selected").each(function(){
			recruitMajorArray.push(jQuery(this).val());
		})	
		
		$("#majorTable input[name='recruitMajor']").each(function(){
			recruitMajorArray.push(jQuery(this).val());
		})
	
		
		//if(recruitMajorArray.length<=0){
			//alertMsg.warn("请新增至少一个招生专业!");
			//return false;
		//}
		var f = document.getElementById("appMajorForm");
		f.action += "?CM="+type;
		return validateCallback(f,callback);
	}
	function brsrecruitplan_getBack(callback){
			var url="${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-getback.html";
			var f = document.getElementById("appMajorForm");
			f.action = url;
		 return validateCallback(f,callback);
	}	
	//新增申报专业
	function _addNewMajorPage(){
		var teachingPlanId = $('#_teachingPlanForm_teachingPlanId').val();
		$.pdialog.open('${baseUrl }/edu3/recruit/recruitmanage/branchschool-newmajor-form.html','AddNewMajorForm','新增申报专业',{width:800,height:400,mask:true});
	}
	//删除申报专业
	function _deleteNewMajor(){
		if(isCheckOnlyone("flag","#_addNewMajorBody")){
			$("#_addNewMajorBody input[name=flag]:checked").parent().parent().remove();
		}
	}
	//修改申报专业
	function _editNewMajor(){
		if(isCheckOnlyone("flag","#_addNewMajorBody")){
			var tp = $("#_addNewMajorBody input[name=flag]:checked").parent().parent();
			var editID       = $("#_addNewMajorBody input[name=flag]:checked").val();
			//var classicId    = tp.find("input[name=classic]").val();
			//var majorClass   = tp.find("input[name=majorClass]").val();
			//var nationMajor  = tp.find("input[name=nationMajor]").val();
			var baseMajor    = tp.find("[name='baseMajor']").val();
			var teachingType = tp.find("input[name=teachingType_baseMajor]").val();
			var studyperiod = tp.find("[name='studyperiod_baseMajor']").val();
			var dicrect 	 = tp.find("input[name='dicrect_baseMajor']").val();
			var scope 	     = tp.find("input[name='scope_baseMajor']").val();
			var address		 = tp.find("input[name='address_baseMajor']").val();
			var limitNum  = tp.find("[name='limitNum_baseMajor']").val();
			//var shape 		 = tp.find("input[name=shape]").val();
			var memo 		 = tp.find("textarea[name='memo_baseMajor']").val();
			var majorDescript = tp.find("textarea[name='majorDescript_baseMajor']").val();
			var editURL    	 = "${baseUrl }/edu3/recruit/recruitmanage/branchschool-newmajor-form.html?teachingType1="+
								teachingType+"&baseMajor1="+baseMajor+"&limitNum1="+limitNum+"&studyperiod1="+
								studyperiod+"&dicrect1="+dicrect+"&address1="+address+"&scope1="+scope+"&memo1="+memo+"&from=edit&majorDescript1="+majorDescript;
			$.pdialog.open(editURL,'AddNewMajorForm','修改申报专业',{width:600,height:400,mask:true});
			
		}
	}
	//附件删除
    function deleteAttachFile(attid){
	  if(confirm("确定要删除这个附件？")){
	  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
		$.get(url,{fileid:attid},function(data){
			$("#addNewMajorUL > li[id='"+attid+"']").remove();
			$("#uploaderNewMajorFileDIV input[value='"+attid+"']").remove();
		});	
	  }
    }
	//附件下载
   function downloadAttachFile(attid){
   		$('#frameForDownload').remove();
   		var elemIF = document.createElement("iframe");
   		elemIF.id = "frameForDownload"; //创建id
		elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
   }
   jQuery(document).ready(function(){
		
		
		 $("#appNewMajorAttachs").uploadify({ //初始化附件上传
	            'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	            'auto'           : true, //自动上传               
	            'multi'          : false, //多文件上传
	            'scriptData'	 : {'formType':'brSchoolAppNewMajor','isStoreToDatabase':'Y','storePath':'common,brsrecruitplan,${unitID}','replaceName':jQuery('#appMajorForm select[id=_recruitplanId]').val()},//按学生报名日期创建目录，按学生证件号命名照片
	            'fileDesc'       : '支持格式:zip',  //限制文件上传格式描述
	            'fileExt'        : '*.zip;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 204800, //限制单个文件上传大小2M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	            onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	            	 $("#addNewMajorUL").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;"+fileObj.name+"&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					 $("#uploaderNewMajorFileDIV").append("<input type='hidden'  name='uploadNewMajorFileFileId' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
	            },  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alert("文件" + fileObj.name + "上传失败"); 
				    $('#uploadify').uploadifyClearQueue(); //清空上传队列
				}
  		 });
		 
		var newMajorId = "${branchShoolNewMajor.classic.resourceid }";
		if(""!=newMajorId){
			
			$("#newMajorOperationAdd").attr("checked","checked")
			$("#newMajorOperationReset").attr("checked","")
			$("#newMajorTable").show();
		}
		
		jQuery('#appMajorForm select[id=_recruitplanId]').change(function(){
			   var recruitPlanId = jQuery(this).val();
			   //jQuery.ajax({
					  // type:"POST",
					 //  url:"${baseUrl}/edu3/framework/get-majorlist.html",
					 //  data:"planid="+recruitPlanId,
					 //  dataType:"json",
					 //  async:"false",
					 //  success:function(myJSON){	   
					   	//	var selectObj="<option value=''>请选择</option>";
					   //		for (var i = 0; i < myJSON.length; i++) {
							 //	 selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
						//	}
						//	jQuery("select[name='recruitMajor']").children().remove();
					//		jQuery("select[name='recruitMajor']").html(selectObj);
					   //} 
				//});	
			   jQuery("#majorTable tr:has(select[name='recruitMajor'])").remove();
		});
		
		
	});
	//新增专业
    function addMajor(){
		
  		var recruitPlanId = $("#appMajorForm select[id=_recruitplanId]").val();
  		if(recruitPlanId==""){
  			alertMsg.warn("请选择一个要申报的批次！");
  			return false;
  		}
		var idArray = new Array();
		//已选择的专业
		$("#majorTable select[name='recruitMajor'] option:selected").each(function(){
			idArray.push(jQuery(this).val());
		})
  		var url ="${baseUrl}/edu3/framework/get-brschoolSetting-limit-major-list.html";
    	jQuery.ajax({
					   type:"POST",
					   url:url,
					   data:"planid="+recruitPlanId+"&exceptMajor="+idArray.toString(),
					   dataType:"json",
					   async:"false",
					   success:function(myJSON){	   
					   		var selectObj="<select name='recruitMajor'><option value=''>请选择</option>";
					   		for (var i = 0; i < myJSON.length; i++) {
							 	 selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
							}
							selectObj +="</select>";
							var rowNum = jQuery("#majorTable").get(0).rows.length;
	 						if(myJSON.length>0){
	 							
	 							var trHTML = "<tr><td width='5%'><q>"+rowNum+"</q><input type='checkbox' name='checkId' value=''>"
										   	+"<input type='hidden' name='majorId' value=''></td>"
										   	+"<td width='5%'>招生专业:</td>"
											+"<td width='30%' >"
											+ selectObj
											+"</td>"
											//+"<td width='8%'>学费:</td>"
											//+"<td width='15%'><input type='text' name='tuitionFee' value='' class='required number'/></td>"
											+"<td width='15%'>学院下达的计划指标:</td>"
											+"<td width='15%'><input type='text' name='limitNum' value='0' min='0' class='required number' style='width:60px;'/>"
											+"<input type='hidden' name='lowerNum' value='0' min='0'  style='width:60px;'/></td>"
											+"<td width='10%'>审核状态:</td>"
											+"<td width='20%'>待审核</td>"
											//+"<td width='8%'>下限人数:</td>"
											//+"<td width='13%'><input type='text' name='lowerNum' value='0' min='0' class='required number' style='width:60px;'/></td></tr>"
								if(rowNum==1){
									jQuery("#majorTable tr:last").after(trHTML)
								}else{
									jQuery("#majorTable  tr[id='appendFlag']").after(trHTML);
								}
	 						}else{
	 							alertMsg.warn("你已选择了当前批次的所有专业或没有当前批次招生专业的申报权限！");
	 							return ;
	 						}
					    		
					   } 
					});
    	
    }
	
    
    //删除专业
    function delMajor(){
    	tab = jQuery("#majorTable").get(0);
  		var ids = "";
		var idArray = new Array();
		
  		//jQuery("input[name='checkId']:checkbox").each(function(index){
  			
			//if(jQuery(this).attr("checked")===true){
				//ids = ids + jQuery(this).val() + ",";
				//idArray.push(index);
			//}
		//})
		
		jQuery("#majorTable tr").each(function(index){
			if($(this).find("input[name='checkId']").attr("checked")===true){
				idArray.push(index);
			}
		})	
		if(idArray.length<1){alertMsg.warn("请选择一条要删除的记录！");return false;}
		if(window.confirm('确定删除该记录?')){
			<%--//var rowIndex;
			//var nextDiff =0;
			//for(j=0;j< idArray.length;j++){
				//rowIndex = idArray[j]+1-nextDiff;
				//tab.deleteRow(rowIndex);
				//tab.deleteRow(idArray[j]);
				//nextDiff++;
				
			//}
			var url = "${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-delete.html";
			if(ids!=""){
				jQuery.get(url,{resourceid:ids})
			}--%>
			
			jQuery("#majorTable tr").each(function(index){
				if($(this).find("input[name='checkId']").attr("checked")===true){
					$(this).html("");
				}
			})	
		}
    	jQuery("q").each(function(index){
    		jQuery(this).text(index+1);
    	})
    }
    jQuery(document).ready(function(){
  		var isBrschool = "${isBrschool}";
  		if("N"==isBrschool){
  			//jQuery("#brschoolButton").attr("style","").html("<font color='red'>操作权限不足，不允许申报专业，请与管理员联系！</font>");
  		}
	}); 
    
    //招生领导的取消专业事件
    function recruitLeaderUnCheck(obj,flag){
	
		if(false == $(obj).attr("checked")){
			var appMajorName = "";
			if(0==flag){
				appMajorName = $(obj).parent().parent().find("td:has(input[name=recruitMajor])").text();
			};
	    	if(1==flag){
	    		appMajorName = $(obj).parent().parent().find("select[name=nationMajorName_select] option:selected").text();
	    	}
	    	if(2==flag){
	    		appMajorName = $(obj).parent().parent().find("select[name=baseMajorName_select] option:selected").text();
	    	}
	    	alertMsg.confirm("确定要将 <font color='red'>"+appMajorName+"</font> 审核不通过吗？", {
	    	   okCall:function(){
	    	   },
	    	   cancelCall:function(){
					$(obj).attr("checked","checked");
			   }
			});
		}
    }
	</script>
	<h2 class="contentTitle">申报专业表单</h2>
	<div class="page">
		<div class="pageContent">
			<form id="appMajorForm" method="post"
				action="${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-save.html"
				class="pageForm">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${brSchoolPlan.resourceid }" /> <input type=hidden
					name=wf_id value="${brSchoolPlan.wfid }" /> <input type=hidden
					name=APP_WF_ID value="${brSchoolPlan.wfid }" /> <input
					type="hidden" id="CM" name="CM" value="">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs"
						<c:if test="${tabIndex == 1 }">currentIndex=1</c:if>>
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>申报专业信息</span></a></li>
									<li><a href="#"><span>流程跟踪</span></a></li>
									<%-- 
							<c:if test="${isAllowNewMajor eq 'Y' || not empty brSchoolPlan.branchShoolNewMajor }">
								<li><a href="#"><span>申报新专业</span></a></li>
							</c:if>	
							 --%>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="majorTable">

									<c:if test="${not empty brSchoolPlan.resourceid }">
										<tr>
											<td width="15%">申报教学站：</td>
											<td width="85%" colspan="6">${brSchoolPlan.branchSchool.unitName }</td>
											<input name="recruitplanId" type="hidden"
												value="${brSchoolPlan.recruitplan.resourceid }" />
										</tr>
									</c:if>

									<tr id="appendFlag">
										<td width="15%"><c:choose>
												<c:when
													test="${not empty brSchoolPlan.resourceid and   isBrschool != 'Y'}">
											教学站批次：
										</c:when>
												<c:otherwise>
											招生批次:
										</c:otherwise>
											</c:choose></td>
										<td width="55%" colspan="4"><c:choose>
												<c:when test="${ isBrschool eq 'Y'}">
													<select id="_recruitplanId" name="recruitplanId">
														<c:forEach items="${recruitPlanList}" var="recruitPlan">
															<c:choose>
																<c:when
																	test="${ brSchoolPlan.fillinManId  != null and currentUser.resourceid != brSchoolPlan.fillinManId }">
																	<c:if
																		test="${ brSchoolPlan.recruitplan.resourceid eq recruitPlan.resourceid }">
																		<option value="${recruitPlan.resourceid }">${recruitPlan.recruitPlanname }</option>
																	</c:if>
																</c:when>
																<c:otherwise>
																	<option value="${recruitPlan.resourceid }"
																		<c:if test="${brSchoolPlan.recruitplan.resourceid eq recruitPlan.resourceid}"> selected="selected" </c:if>>${recruitPlan.recruitPlanname }</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
											${brSchoolPlan.recruitplanName }
										</c:otherwise>
											</c:choose></td>
										<td width="30%" id="brschoolButton" colspan="2"
											style="text-align: right">
											<%--
									<c:if test="${isBrschool eq 'Y'}">
									<div class="formBar" >
										<ul>
											<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="addMajor()"  >新增</button></div></div></li>
											<li><div class="button"><div class="buttonContent"><button type="button" onclick="delMajor()" >删除</button></div></div></li>
										</ul>
									</div>
									</c:if>
									 --%>
										</td>
									</tr>

									<!-- 审核时列出申报的专业 -->
									<%-- 
							<c:forEach items="${branchSchoolMajorList}" var="major" varStatus="vs">
								<tr>
									<td width="5%"><q>${vs.count}</q>
										<c:choose>
											<c:when test="${ isBrschool eq 'Y'}">
												<input type="checkbox" name="checkId"   value="${major.resourceid}"    />
											</c:when>
											<c:when test="${ isRecruitLeader eq 'Y'}">
												<input type="checkbox" name="checkId"   value="${major.resourceid}" onclick="recruitLeaderUnCheck(this,0);"  checked="checked"  />
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="checkId"   value="${major.resourceid}"  style="display: none;"  />
											</c:otherwise>
										</c:choose>
										
										<input type="hidden" name="majorId" value="${major.resourceid}">
									</td>
									<td width="5%">招生专业:</td>
									<td width="30%">
									<input type="hidden" name="recruitMajor" value="${major.recruitMajor.resourceid}">
									${major.recruitMajor.recruitMajorName }
									
									  	
									</td>		
									<!--<td width="8%">学费:</td>
								  	<td width="15%"><input type="text" name="tuitionFee" value="${major.tuitionFee }" class="required number"/></td>
									--><td width="15%">学院下达的计划指标:</td>
									<td width="15%">
											<input type="text" name="limitNum"    value="${major.limitNum }" style='width:60px;' min="0"  class="required number"/>
											<input type="hidden"" name="lowerNum" value="${major.lowerNum }" style='width:60px;' min="0" />
									</td>
									<!--<td width="8%">下限人数:</td>
									<td width="13%"><input type="text" name="lowerNum" value="${major.lowerNum }" style='width:60px;' min="0" class="required number"/></td>
									-->
									<td width="10%">审核状态:</td>
									<td width="20%">
										${ghfn:dictCode2Val('CodeAuditStatus',major.isPassed)}
									</td>
								</tr>
							</c:forEach>
							 --%>
									<!-- 审核时列出申报的专业 -->

								</table>
								<table class="form">
									<c:if test="${null!=brSchoolPlan.resourceid}">
										<tr>
											<td width="15%">批准文号:</td>
											<td width="85%"><input type="text" name="documentCode"
												style="width: 10%" value="${brSchoolPlan.documentCode}"
												disabled="disabled" class="required" /></td>
										</tr>
									</c:if>
									<tr>
										<td width="15%">意见:</td>
										<td width="85%"><wf:opinion appType="brsrecruitplan"
												appWfId="${brSchoolPlan.wfid }" appFrom="${param.APP_FROM}" />
										</td>
									</tr>
									<tr>
										<td width="15%">&nbsp;</td>
										<td width="85%"><wf:availableAction
												appType="brsrecruitplan" appWfId="${brSchoolPlan.wfid }"
												appFrom="${param.APP_FROM}" /></td>
									</tr>
								</table>

								<!-- 专业申报 -->
								<c:if
									test="${isAllowNewMajor eq 'Y' || not empty brSchoolPlan.branchShoolNewMajor }">
									<div class="pageContent">
										<c:if
											test="${ isAllowNewMajor == 'Y' || empty brSchoolPlan.branchShoolNewMajor  }">
											<span class="buttonActive"><div class="buttonContent">
													<button type="button" onclick="_addNewMajorPage()">增加专业</button>
												</div></span>
											<span class="buttonActive"><div class="buttonContent">
													<button type="button" onclick="_editNewMajor()">编辑专业</button>
												</div></span>
											<span class="buttonActive"><div class="buttonContent">
													<button type="button" onclick="_deleteNewMajor()">删除专业</button>
												</div></span>
									&nbsp;<input type="file" id="appNewMajorAttachs"
												name="appNewMajorAttachs" />
											<font color="red">请上传不大于2M的ZIP文件</font>
										</c:if>
										<div>
											<ul id="addNewMajorUL">
												<c:forEach items="${brSchoolPlan.attachs }" var="att">
													<li id="${att.resourceid }"><img
														src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"
														style="cursor: pointer; height: 10px">&nbsp;&nbsp; <a
														href="javascript:void(0)"
														onclick="downloadAttachFile('${att.resourceid }')">${att.attName }</a>&nbsp;&nbsp;
														<c:if
															test="${ isAllowNewMajor == 'Y' || empty brSchoolPlan.branchShoolNewMajor}">
															<img
																src="${baseUrl }/jscript/jquery.uploadify/images/cancel.png"
																onClick="deleteAttachFile('${att.resourceid}')"
																style="cursor: pointer; height: 10px">
														</c:if></li>
												</c:forEach>
											</ul>
										</div>
										<div id="uploaderNewMajorFileDIV">
											<c:forEach items="${brSchoolPlan.attachs }" var="att">
												<input type='hidden' name='uploadNewMajorFileFileId'
													value="${att.resourceid }">
											</c:forEach>
										</div>
										<table id="newMajorListTable" class="list" width="100%">
											<%--
						           		<tr>
							           		<td  style="width:5%"><strong>选项</strong></td>
							           		<td  style="width:8%"><strong>层次</strong></td>
											<td  style="width:7%"><strong>专业大类</strong></td>
											<td  style="width:7%"><strong>专业类别</strong></td>
											<td  style="width:15%"><strong>专业</strong></td>
											<td  style="width:8%"><strong>办学模式</strong></td>
											<td  style="width:6%"><strong>专业方向</strong></td>
											<td  style="width:5%"><strong>招生范围</strong></td>
											<td  style="width:8%"><strong>办学地址</strong></td>
											<td  style="width:5%"><strong>形式</strong></td>
											<td  style="width:8%"><strong>下达的标数</strong></td>
											<td  style="width:5%"><strong>下限数</strong></td> 
										    <td  style="width:8%"><strong>审核状态</strong></td>
											<td  style="width:10%"><strong>备注</strong></td>
										</tr>
									--%>
											<tr>
												<td style="width: 5%"><strong>选项</strong></td>
												<td style="width: 8%"><strong>层次</strong></td>
												<td style="width: 6%"><strong>科类</strong></td>
												<td style="width: 13%"><strong>专业</strong></td>
												<td style="width: 6%"><strong>形式</strong></td>
												<td style="width: 5%"><strong>学制</strong></td>
												<td style="width: 8%"><strong>专业方向</strong></td>
												<td style="width: 8%"><strong>招生范围</strong></td>
												<td style="width: 8%"><strong>办学地址</strong></td>
												<td style="width: 5%"><strong>人数</strong></td>
												<td style="width: 8%"><strong>审核状态</strong></td>
												<td style="width: 10%"><strong>专业介绍</strong></td>
												<td style="width: 10%"><strong>备注</strong></td>
											</tr>
											<tbody id="_addNewMajorBody">


												<!-- 审核时列出申报的新专业  -->
												<c:forEach items="${newMajorList }"
													var="branchShoolNewMajor" varStatus="vs">
													<%-- ------------------从国家专业库中选择------------------ --%>
													<c:choose>
														<%-- 	<c:when test="${not empty branchShoolNewMajor.majorName }">
												
												<tr id='tr${branchShoolNewMajor.resourceid}'>
													<td>
														<c:choose>
															<c:when test="${isRecruitLeader eq 'Y' }">
																${vs.index +1}<input type='checkbox' name="flag" value="${branchShoolNewMajor.resourceid }" autocomplete="off" onclick="recruitLeaderUnCheck(this,1);" checked="checked"/>
															</c:when>
															<c:otherwise>
																${vs.index +1}<input type='checkbox' name="flag" value="${branchShoolNewMajor.resourceid }" autocomplete="off" />
															</c:otherwise>
														</c:choose>	
													</td>
													<td>
														<gh:selectModel name="resourceid" bindValue="resourceid" disabled="disabled" displayValue="classicName" modelClass="com.hnjk.edu.basedata.model.Classic" value="${branchShoolNewMajor.classic.resourceid }"/>
														<input name="classic_nationMajor" type="hidden" value="${branchShoolNewMajor.classic.resourceid }"/> 
													</td>									
												
													<td>
														
														<gh:select dictionaryCode="nationmajorParentCatolog" name="nationmajorParentCatolog_sel" value="${branchShoolNewMajor.parentCatalog }"   disabled="disabled"/>
														<input name="nationmajorParentCatolog" type="hidden" value="${branchShoolNewMajor.parentCatalog }"/> 
													</td>
													<td>
														
														<gh:select dictionaryCode="nationmajorChildCatolog" name="nationmajorChildCatolog_sel" value="${branchShoolNewMajor.childCatalog }"   disabled="disabled"/>
														<input name="nationmajorChildCatolog" type="hidden" value="${branchShoolNewMajor.childCatalog }"/> 
													</td>
												
													<td>

														<gh:selectModel name="nationMajorName_select" bindValue="resourceid" displayValue="nationMajorName" modelClass="com.hnjk.edu.basedata.model.NationMajor" disabled="disabled" value="${branchShoolNewMajor.majorName }"/>
														<input name="nationMajor" type="hidden" value="${branchShoolNewMajor.majorName }"/> 
			
													</td>
													
													<td>${ghfn:dictCode2Val('CodeTeachingType',branchShoolNewMajor.teachingType)}
														 <input name="teachingType_nationMajor" type="hidden" value="${branchShoolNewMajor.teachingType }"/> 
													</td>
													<td>${branchShoolNewMajor.dicrect }
														 <input name="dicrect_nationMajor" type="hidden" value="${branchShoolNewMajor.dicrect }"/> 
													</td>
													<td>${branchShoolNewMajor.scope }
														 <input name="scope_nationMajor" type="hidden" value="${branchShoolNewMajor.scope }"/> 
													</td>
													<td>${branchShoolNewMajor.address }
														 <input name="address_nationMajor" type="hidden" value="${branchShoolNewMajor.address }"/> 
													</td>
													<td>${branchShoolNewMajor.shape }
														 <input name="shape_nationMajor" type="hidden" value="${branchShoolNewMajor.shape }"/> 
													</td>
													<td>${branchShoolNewMajor.limitNum }
														 <input name="limitNum_nationMajor" type="hidden" value="${branchShoolNewMajor.limitNum }"/> 
														  <input name="lowerNum_nationMajor" type="hidden" value="${branchShoolNewMajor.lowerNum }"/> 
													</td>													
													 <td>${ghfn:dictCode2Val('CodeAuditStatus',branchShoolNewMajor.isPassed)}
														 <input name="isPassed_nationMajor" type="hidden" value="${branchShoolNewMajor.isPassed }"/> 
													</td>
													<td>${branchShoolNewMajor.memo }
														<textarea name="memo_nationMajor" rows="" cols="" style="display: none;">${branchShoolNewMajor.memo }</textarea>
													</td>
												</tr>
												
											</c:when>	 --%>
														<%-- ------------------从国家专业库中选择------------------ --%>

														<%-- ------------------从基础专业库中选择------------------  --%>
														<c:when test="${not empty branchShoolNewMajor.baseMajor }">
															<tr id='tr${branchShoolNewMajor.resourceid}'>
																<td><c:choose>
																		<c:when test="${isRecruitLeader eq 'Y' }">
															${vs.index +1}<input type='checkbox' name="flag"
																				value="${branchShoolNewMajor.resourceid }"
																				autocomplete="off" checked="checked"
																				onclick="recruitLeaderUnCheck(this,2);" />
																		</c:when>
																		<c:otherwise>
															${vs.index +1}<input type='checkbox' name="flag"
																				value="${branchShoolNewMajor.resourceid }"
																				autocomplete="off" />
																		</c:otherwise>
																	</c:choose></td>
																<td>
																	<%-- <gh:selectModel name="classic_baseMajor_select" bindValue="resourceid" disabled="disabled" displayValue="classicName" modelClass="com.hnjk.edu.basedata.model.Classic" value="${branchShoolNewMajor.classic.resourceid }"/> 
													<input name="classic_baseMajor" type="hidden" value="${branchShoolNewMajor.classic.resourceid }"/> --%>
																	<span>${branchShoolNewMajor.classicName }</span> <input
																	name="classic_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.classicid }" />
																</td>
																<td><span>${branchShoolNewMajor.shape }</span> <input
																	name="shape_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.shape }" /></td>
																<td>
																	<%--<gh:selectModel name="baseMajorName_select" bindValue="resourceid" displayValue="majorName" modelClass="com.hnjk.edu.basedata.model.Major" disabled="disabled" value="${branchShoolNewMajor.baseMajor }"/> --%>
																	<input name="baseMajor" type="hidden"
																	value="${branchShoolNewMajor.baseMajor }" /> <span>${branchShoolNewMajor.majorName }</span>
																</td>
																<td><span>${ghfn:dictCode2Val('CodeTeachingType',branchShoolNewMajor.teachingType)}</span>
																	<input name="teachingType_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.teachingType }" /></td>
																<td><span><fmt:formatNumber
																			value="${branchShoolNewMajor.studyperiod }"
																			pattern="##.#" /></span> <input name="studyperiod_baseMajor"
																	type="hidden"
																	value="<fmt:formatNumber value='${branchShoolNewMajor.studyperiod }' pattern='##.#'/>" />
																</td>
																<td><span>${branchShoolNewMajor.dicrect }</span> <input
																	name="dicrect_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.dicrect }" /></td>
																<td><span>${branchShoolNewMajor.scope }</span> <input
																	name="scope_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.scope }" /></td>
																<td><span>${branchShoolNewMajor.address }</span> <input
																	name="address_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.address }" /></td>
																<td><span>${branchShoolNewMajor.limitNum }</span> <input
																	name="limitNum_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.limitNum }" /></td>
																<td><span>${ghfn:dictCode2Val('CodeAuditStatus',branchShoolNewMajor.isPassed )}</span>
																	<input name="isPassed_baseMajor" type="hidden"
																	value="${branchShoolNewMajor.isPassed }" /></td>
																<td><span>${branchShoolNewMajor.majorDescript }</span>
																	<textarea name="majorDescript_baseMajor" rows=""
																		cols="" style="display: none;">${branchShoolNewMajor.majorDescript }</textarea>
																</td>
																<td><span>${branchShoolNewMajor.memo }</span> <textarea
																		name="memo_baseMajor" rows="" cols=""
																		style="display: none;">${branchShoolNewMajor.memo }</textarea>
																</td>
															</tr>
														</c:when>
														<%-- ------------------从基础专业库中选择------------------  --%>
													</c:choose>
												</c:forEach>
												<!-- 审核时列出申报的新专业 -->

											</tbody>
										</table>
									</div>
								</c:if>
							</div>
							<div>
								<wf:trace appType="brsrecruitplan"
									appWfId="${brSchoolPlan.wfid }" appFrom="${param.APP_FROM}" />
							</div>

						</div>

						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>

				<div class="formBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">返回</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>

</body>
</html>