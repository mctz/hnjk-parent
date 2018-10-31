<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入面授成绩(补考)</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<table width="100%">
				<tr align="center" valign="middle" height="10px">
					<td colspan="3">
						<div style="position: fixed; float: left;">
							<div style="float: left">
								<input name="importFile"
									id="failfacestudy_importExamResultsFile" type="file" />
							</div>
						</div>
						<div style="float: left; width: 100px; margin-left: 66px;">
							<a href="javascript:void(0)" class="button"
								onclick="failfacestudy_checkExamResult();"><span
								style="height: 60px">核对成绩</span></a>
						</div>
					</td>
				</tr>
				<tr height="80px">
					<td colspan="2" width="60%">
						<div style="margin-left: 10px;">
							<font color="red">
								<p style="font-weight: bolder">操作说明：</p>
								<P>1、如果课程名为红色字体，说明导入成绩与系统中成绩不一致，请将鼠标移入查看原始成绩！</P>
								<P>2、上传同一批次的成绩文件</P>
								<P>3、建议一次只上传一个成绩文件</P>
								<P>4、点击核对成绩</P>
								<P>5、保存</P>
								<p style="font-weight: bolder">成绩异常：（作弊、缺考、无卷、其它）,
									请严格将（作弊、缺考、无卷、其它）的其一个词语按照结果选择后，规范填写在卷面成绩栏里</p>
							</font>
						</div>
					</td>
					<td rowspan="2" width="40%"></td>
				</tr>
				<tr height="10px">
					<td colspan="2" width="50%">
						<ul id="failfacestudy_examResultsFileUL">
							<c:forEach var="att" items="${attachList }">
								<li id="${att.resourceid }"><img
									src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"
									style='cursor: pointer; height: 10px'>&nbsp;&nbsp; <a
									href="javascript:void(0)"
									onclick="failfacestudy_downloadAttachFile('${att.resourceid }')">${att.attName }&nbsp;</a>&nbsp;&nbsp;
									<img
									src="${baseUrl }/jscript/jquery.uploadify/images/cancel.png"
									onClick="failfacestudy_deleteAttachFile('${att.resourceid }');"
									style="cursor: pointer; height: 10px"></li>
							</c:forEach>
						</ul>
					</td>
					<td width="50%"></td>
				</tr>
			</table>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST"
			pageType="sublist"></gh:resAuth>
		<div class="pageContent" layouth="181">
			<form id="failfacestudy_examResultExamSubForm"
				action="${baseUrl}/edu3/teaching/transcripts/facestudy/failupload-process.html">

				<div id="failfacestudy_uploaderExamResultFileDIV"
					style="display: none;">
					<c:forEach var="att" items="${attachList }">
						<input type="hidden" id="failfacestudy_uploadExamResultFileId"
							name="uploadExamResultFileId" value="${att.resourceid }">
					</c:forEach>
				</div>
				<div id="failfacestudy_uploaderExamResultFileLogsDIV"
					style="display: none;">
					<c:forEach var="logs" items="${logList }">
						<input type="hidden" id="${logs.attachId }"
							value="${logs.resourceid }" />
					</c:forEach>
				</div>
				<input id="failfacestudy_examSubId" name="examSubId" type="hidden"
					value="${resultMap['examSubId'] }" />
				<%-- 
		<input id="failfacestudy_guidePlanId" name="guidePlanId" type="hidden" value="${resultMap['guidePlanId'] }"/>
		--%>
				<input id="failfacestudy_teachingPlanCourseId"
					name="teachingPlanCourseId" type="hidden"
					value="${resultMap['teachingPlanCourseId'] }"> <input
					id="failfacestudy_classesId" name="classesId" type="hidden"
					value="${resultMap['classesId']}" /> <input
					id="failfacestudy_type" name="type" type="hidden"
					value="${resultMap['type']}" /> <input
					id="failfacestudy_isMachineExam" name="isMachineExam" type="hidden"
					value="${resultMap['isMachineExam']}" /> <input
					id="failfacestudy_schoolId" name="schoolId" type="hidden"
					value="${resultMap['schoolId']}" />
				<table class="list" width="100%">
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="failfacestudy_import_examresult_check_all"
							onclick="checkboxAll('#failfacestudy_import_examresult_check_all','importExamresultsResourceid','#failfacestudy_examResultsVoBody')" /></th>
						<th width="5%">序号</th>
						<th width="15%">教学中心</th>
						<th width="20%">专业</th>
						<th width="20%">学号</th>
						<th width="10%">姓名</th>
						<th width="10%">卷面成绩</th>
						<%-- 
	            <th width="10%">平时成绩   </th>   
	             --%>
						<th width="10%">录入状态</th>
					</tr>
					<tbody id="failfacestudy_examResultsVoBody">

					</tbody>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	$(document).ready(function(){
 		var success = "${resultMap['success']}";	
 		var msg     = "${resultMap['msg']}";
 		if(success=="N"){
 			alertMsg.warn(msg);
 			return ;
 		} 		
 		failfacestudy_initUploadExamResults();
	});
	
	//保存成绩
	function failfacestudy_processImprot(){
		//alert("failfacestudy_processImprot");
		var importExamresultsResourceid = new Array();
		jQuery("#failfacestudy_examResultsVoBody input[name='importExamresultsResourceid']:checked").each(function(){
			importExamresultsResourceid.push(jQuery(this).val());
		});
		if(importExamresultsResourceid.length>0){
			var $form = $("#failfacestudy_examResultExamSubForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要保存所选成绩吗？", {
                okCall: function(){
                	$.ajax({
						type:'POST',
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							var updateNum= resultData['updateRecordCount'];
							var saveList = resultData['saveList'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								for(var i=0;i<saveList.length;i++){
									$("#failfacestudy_examResultsVoBody  input[name='importExamresultsResourceid'][value='"+saveList[i]+"']").parent().parent().remove();
								}
								/*
								if($("#facestudy_examResultsVoBody  input[name='importExamresultsResourceid']").size()==0){
									alertMsg.confirm("已保存：<font color='red'>"+updateNum+"</font>条记录!<br/>你已保存上传成绩文件中的所有成绩记录，是否将上传的成绩文件删除?", {
										okCall: function(){
											$("#facestudy_uploaderExamResultFileDIV input[name='uploadExamResultFileId']").each(function(obj){
												facestudy_deleteAttachFile($(this).val());
											});
										}
									});
								} else {
									alertMsg.confirm("已保存：<font color='red'>"+updateNum+"</font>条记录!", {
										okCall: function(){
											$("#facestudy_uploaderExamResultFileDIV input[name='uploadExamResultFileId']").each(function(obj){
												facestudy_deleteAttachFile($(this).val());
											});
										}
									});
									//alertMsg.info("已保存：<font color='red'>"+updateNum+"</font>条记录!");
								}*/
								alertMsg.info("已保存：<font color='red'>"+updateNum+"</font>条记录!");
								
							}
						}
					});
                }
            });
		}else{
			alertMsg.warn("请先上传成绩文件后，点击核对成绩，再点击保存！如已核对成绩，请检查是否勾选需保存的成绩记录。");
		}
			
	}
	
	//核对成绩
	function failfacestudy_checkExamResult(){
		var examSubId  = jQuery("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_examSubId']").val();
		//var guidePlanId = jQuery("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_guidePlanId']").val();
		var teachingPlanCourseId = jQuery("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_teachingPlanCourseId']").val();
		var classesId   = $("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_classesId']").val();
		var type   = $("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_type']").val();
		
		var uploadExamResultFileId = new Array();
		jQuery("input[name='uploadExamResultFileId']").each(function(){
			uploadExamResultFileId.push(jQuery(this).val());
		});
		if(uploadExamResultFileId.length != 0){
			var url =baseUrl+"/edu3/teaching/transcripts/facestudy/failcheck-examresults.html";
			jQuery.ajax({
				type:"post",
				url:url,
				data:"uploadExamResultFileId="+uploadExamResultFileId.toString()+"&examSubId="+examSubId+"&teachingPlanCourseId="+teachingPlanCourseId+"&classesId="+classesId+"&type="+type,
				dataType:"json",
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					if(success ==false){
						alertMsg.warn(msg);
					}else {
						$("#failfacestudy_uploaderExamResultFileDIV input[name='uploadExamResultFileId']").each(function(obj){
							failfacestudy_deleteAttachFile($(this).val());
						});
						var examResulstVolist = resultData['examResultsVoList'];						
						if(examResulstVolist.length>0){
							jQuery("#failfacestudy_examResultsVoBody").children().remove();
							for(var i=0;i<examResulstVolist.length;i++){	
								var examResults   = examResulstVolist[i];								
								var checkBoxHTML = "<input type='checkbox' name='importExamresultsResourceid' value="+examResults.studyNo+" autocomplete='off' "; 
								if(null!=examResults.writtenScore && ""!=examResults.writtenScore){
									checkBoxHTML+="checked='checked'";
								}
								checkBoxHTML+="/>";
								var tr = "";
								if("Y"==examResults.flag){
									tr = "<tr style='color: red' title='导入成绩与系统中不一致,原成绩为："+examResults.message+"'>";
								}else{
									tr = "<tr >";
								}
								
								if(""!=examResults.memo){
									//alert("提交"==examResults.memo);
									if("提交"==examResults.memo){
										checkBoxHTML="";
									}
								}
								if(""!=examResults.memo){
									if("提交"==examResults.memo){
										jQuery("#failfacestudy_examResultsVoBody").append("<tr >"+tr+"<td width='5%'>"+checkBoxHTML+"</td>"+
												"<td width='5%'>"+examResults.sort+"</td>"+																	
												"<td width='20%'>"+examResults.branchSchool+"</td>"+
												"<td width='20%'>"+examResults.major+"</td>"+
												"<td width='15%' style='color:green'>"+examResults.studyNo+"</td>"+
												"<td width='10%' style='color:blue'>"+examResults.name+"</td>"+
												"<td width='10%'>"+examResults.writtenScore+"</td>"+
												"<td width='5%' ><font color='red'>"+examResults.memo+"</font></td>");
									} else {
										jQuery("#failfacestudy_examResultsVoBody").append("<tr >"+tr+"<td width='5%'>"+checkBoxHTML+"</td>"+
												"<td width='5%'>"+examResults.sort+"</td>"+																	
												"<td width='20%'>"+examResults.branchSchool+"</td>"+
												"<td width='20%'>"+examResults.major+"</td>"+
												"<td width='15%' style='color:green'>"+examResults.studyNo+"</td>"+
												"<td width='10%' style='color:blue'>"+examResults.name+"</td>"+
												"<td width='10%'><input type='text' name='writtenScore"+examResults.studyNo+"' align='middle'  style='width:30px' value='"+examResults.writtenScore+"'/></td>"+
												"<td width='5%' ><font color='red'>"+examResults.memo+"</font></td>");
									}
									
								}else{
									jQuery("#failfacestudy_examResultsVoBody").append("<tr >"+tr+"<td width='5%'>"+checkBoxHTML+"</td>"+
											"<td width='5%'>"+examResults.sort+"</td>"+																	
											"<td width='20%'>"+examResults.branchSchool+"</td>"+
											"<td width='20%'>"+examResults.major+"</td>"+
											"<td width='15%' style='color:green'>"+examResults.studyNo+"</td>"+
											"<td width='10%' style='color:blue'>"+examResults.name+"</td>"+
											"<td width='10%'><input type='text' name='writtenScore"+examResults.studyNo+"' align='middle'  style='width:30px' value='"+examResults.writtenScore+"'/></td>"+
											"<td width='5%' >"+examResults.memo+"</td>");
								}
							}
						}												
					}
					
				}
			});
		}else{
			alertMsg.warn("未上传成绩文件，上传后再点击核对成绩!");
		}
 	}
 	//AJAX上传文件
	function failfacestudy_initUploadExamResults(){
		var teachingPlanCourseId = jQuery("#failfacestudy_examResultExamSubForm >input[id='failfacestudy_teachingPlanCourseId']").val();
		var fillinName = "${curUser.cnName}";
		var fillinNameId = "${curUser.resourceid}";
		$("#failfacestudy_importExamResultsFile").uploadify({
                'script'         : baseUrl+"/edu3/filemanage/upload.html", //上传URL
	            'auto'           : true, //自动上传
	            'scriptData'	 : {'formId':teachingPlanCourseId,'formType':'examResultsImportUncheck',fillinName:fillinName,fillinNameId:fillinNameId},
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:XLS/xls',  //限制文件上传格式描述
	            'fileExt'        : '*.XLS;*.xls;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	
	                $("#failfacestudy_examResultsFileUL").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='failfacestudy_downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='failfacestudy_deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#failfacestudy_uploaderExamResultFileDIV").append("<input type='hidden' id='failfacestudy_uploadExamResultFileId' name='uploadExamResultFileId' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
					//每上传一个附件记录一个成绩日志
					failfacestudy_addExamResultsLog(teachingPlanCourseId,response.split("|")[0]);
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
					$('#failfacestudy_importExamResultsFile').uploadifyClearQueue(); //清空上传队列
				}
         });
	}
	//附件下载
   function failfacestudy_downloadAttachFile(attid){
   		$('#failfacestudy_frameForDownload').remove();
   		var elemIF = document.createElement("iframe");
   		elemIF.id = "failfacestudy_frameForDownload"; //创建id
		elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
   }
   
   //附件删除
   function failfacestudy_deleteAttachFile1(attid){
	  if(confirm("确定要删除这个附件？")){
	  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
		$.get(url,{fileid:attid},function(data){
			$("#failfacestudy_examResultsFileUL > li[id='"+attid+"']").remove();
			$("#failfacestudy_uploadExamResultFileId[value='"+attid+"']").remove();
			//删除一个附件则删除对应的成绩日志
			failfacestudy_removeExamResultsLog(attid);
		});	
	  }
    }
   function failfacestudy_deleteAttachFile(attid){
		  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
			$.get(url,{fileid:attid},function(data){
				$("#failfacestudy_examResultsFileUL > li[id='"+attid+"']").remove();
				$("#failfacestudy_uploadExamResultFileId[value='"+attid+"']").remove();
				//删除一个附件则删除对应的成绩日志
				failfacestudy_removeExamResultsLog(attid);
			});
	    }
    //上传附件时增加日志
	function failfacestudy_addExamResultsLog(examInfoId,attid){
		jQuery.ajax({
				type:'post',
				url:baseUrl+'/edu3/teaching/examResultsLog/add.html',
				data:'examInfoId='+examInfoId+"&optionType=examResultsImportUncheck&attid="+attid,
			    dataType:'josn',
			    success:function(examResultsLogId){
			     	if(examResultsLogId != ''){
			     		jQuery("#failfacestudy_uploaderExamResultFileLogsDIV").append("<input type='hidden' id='"+attid+"' value="+examResultsLogId+" />");
			     	}
			    }	
		});
	}
	//删除附件时删除对应的日志
	function failfacestudy_removeExamResultsLog(attid){
		var examResultsLogId = jQuery("#"+attid).val();
		jQuery.ajax({
			type:'post',
			url:baseUrl+'/edu3/teaching/examResultsLog/remove.html',
			data:'examResultsLogId='+examResultsLogId,
		    dataType:'josn',
		    success:function(isSuccess){
		    	if(isSuccess =='true'){
		    		jQuery("#"+attid).remove();
		    	}
	        }	  
		});	
	}
</script>
</body>
</html>