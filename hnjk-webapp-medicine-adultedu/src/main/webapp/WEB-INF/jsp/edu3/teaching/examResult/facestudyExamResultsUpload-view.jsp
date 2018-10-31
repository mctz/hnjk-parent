<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入面授成绩</title>
<style type="text/css">
	p{height: 17px}
</style>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
		    <div style="float:left; ">
				<div id="facestudy_importExamResultsFile" style="width: 380px;"></div>
				<div style="float:left; ">
					<table style="width: 100%">
						<tr>
							<td colspan="4" style="text-align: left;">
								<p style="font-weight: bolder;color: red;">操作说明</p>
							</td>
						</tr>
						<tr height="70px">
							<td colspan="4" style="text-align: left;">
								<div style="margin-left: 10px;margin-top: 5px;">
									<P style="color: red;">1、如果课程名为红色字体，说明导入成绩与系统中成绩不一致，请将鼠标移入查看原始成绩！</P>
									<P style="color: red;">2、上传同一批次的成绩文件</P>
									<P style="color: red;">3、建议一次只上传一个成绩文件</P>
									<P style="color: red;">4、点击核对成绩</P>
									<P style="color: red;">5、保存</P>
									<p style="font-weight: bolder;color: red;">成绩异常：（作弊、缺考、无卷、其它）,
										请严格将（作弊、缺考、无卷、其它）的其一个词语按照结果选择后，规范填写在卷面成绩栏里</p>
									<c:if test="${not empty scoreper }"><p>${scoreper }</p></c:if>
								</div>
							</td>
						</tr>
						<tr height="10px">
							<td colspan="2" width="50%">
								<ul id="facestudy_examResultsFileUL">
									<c:forEach var="att" items="${attachList }">
										<li id="${att.resourceid }"><img
											src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"
											style='cursor: pointer; height: 10px'>&nbsp;&nbsp; <a
											href="javascript:void(0)"
											onclick="facestudy_downloadAttachFile('${att.resourceid }')">${att.attName }&nbsp;</a>&nbsp;&nbsp;
											<img src="${baseUrl }/jscript/jquery.uploadify/images/cancel.png"
											onClick="facestudy_deleteAttachFile('${att.resourceid }');"
											style="cursor: pointer; height: 10px"></li>
									</c:forEach>
								</ul>
							</td>
							<td width="50%"></td>
						</tr>
					</table>
				</div>
				<!-- <div style="float: left">
					<input name="importFile" id="facestudy_importExamResultsFile" type="file" />
				</div> -->
			</div>
		</div>
		<a href="javascript:void(0)" class="button" onclick="facestudy_checkExamResult();">
			<span style="height: 60px">核对成绩</span>
		</a>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST" pageType="sublist"></gh:resAuth>
		<div class="pageContent" layouth="390">
			<form id="facestudy_examResultExamSubForm"
				action="${baseUrl}/edu3/teaching/transcripts/facestudy/upload-process.html">

				<div id="facestudy_uploaderExamResultFileDIV" style="display: none;">
					<c:forEach var="att" items="${attachList }">
						<input type="hidden" id="facestudy_uploadExamResultFileId"
							name="uploadExamResultFileId" value="${att.resourceid }">
					</c:forEach>
				</div>
				<div id="facestudy_uploaderExamResultFileLogsDIV"
					style="display: none;">
					<c:forEach var="logs" items="${logList }">
						<input type="hidden" id="${logs.attachId }" value="${logs.resourceid }" />
					</c:forEach>
				</div>
				<input id="facestudy_examSubId" name="examSubId" type="hidden" value="${resultMap['examSubId'] }" />
                <input id="facestudy_guidePlanId" name="guidePlanId" type="hidden" value="${resultMap['guidePlanId'] }" />
                <input id="facestudy_gradeId" name="gradeId" type="hidden" value="${resultMap['gradeId'] }" />
                <input id="facestudy_unitId" name="unitId" type="hidden" value="${resultMap['unitId'] }" />
                <input id="facestudy_teachingPlanCourseId" name="teachingPlanCourseId" type="hidden" value="${resultMap['teachingPlanCourseId'] }">
				<input id="facestudy_classesId" name="classesId" type="hidden" value="${resultMap['classesId']}" />
                <input id="facestudy_isMachineExam" name="isMachineExam" type="hidden" value="${resultMap['isMachineExam']}" />
                <input id="facestudy_examInfoId" name="examInfoId" type="hidden" value="${examInfoId}" />
				<table class="list" width="100%">
					<tr>
						<th width="2%"><input type="checkbox" name="checkall"
							id="facestudy_import_examresult_check_all"
							onclick="checkboxAll('#facestudy_import_examresult_check_all','importExamresultsResourceid','#facestudy_examResultsVoBody')" /></th>
						<th width="4%">序号</th>
						<th width="18%">教学中心</th>
						<th width="10%">专业</th>
						<th width="13%">学号</th>
						<th width="8%">姓名</th>
						<th width="10%">平时考核成绩</th>
						<th width="8%">卷面成绩</th>
						<th width="10%">网上学习成绩</th>
						<th width="8%">综合成绩</th>
						<th width="8%" nowrap="nowrap">录入状态</th>
					</tr>
					<tbody id="facestudy_examResultsVoBody">

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
 		facestudy_initUploadExamResults();
	});
	//保存成绩
	function facestudy_processImprot(){
		var importExamresultsResourceid = new Array();
		jQuery("#facestudy_examResultsVoBody input[name='importExamresultsResourceid']:checked").each(function(){
			importExamresultsResourceid.push(jQuery(this).val());
		});
		var emptyCount = 0;
		jQuery("#facestudy_examResultsVoBody input[name='importExamresultsResourceid']").each(function(){
			var studyNo = $(this).val();
			var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
			var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
			if(writtenScore=="" && usuallyScore=="" ){
				emptyCount++;
			}
		});
		var isDefaultAbsent = "${isDefaultAbsent}";
		
		if(importExamresultsResourceid.length>0){
			var $form = $("#facestudy_examResultExamSubForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			var reminder =""
			if(isDefaultAbsent=='Y'){
				if(emptyCount>0){
					reminder = "你当前有未录入成绩<font color='red'>"+emptyCount+"</font>条，将默认录入为<font color='red'>缺考</font>";
				}
				jQuery("#facestudy_examResultsVoBody input[name='importExamresultsResourceid']").each(function(){
					var studyNo = $(this).val();
					$(this).attr("checked",true);
					var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
					var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
					if(writtenScore=="" && usuallyScore=="" ){
						$("#facestudy_integratedScore_"+studyNo+"td").text("缺考");
						$("#facestudy_writtenScore_"+studyNo).val("缺考")
						$("#facestudy_integratedScore_"+studyNo).val("缺考");
					}
				});
			}
			alertMsg.confirm("确认要保存所选成绩吗？"+reminder, {
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
									$("#facestudy_examResultsVoBody  input[name='importExamresultsResourceid'][value='"+saveList[i]+"']").parent().parent().remove();
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
                },
                cancelCall:function(){
                	if(isDefaultAbsent=='Y'){
                		jQuery("#facestudy_examResultsVoBody input[name='importExamresultsResourceid']").each(function(){
            				var studyNo = $(this).val();        				
            				var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
            				var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
            				if(writtenScore=="缺考" && usuallyScore==""){
            					$(this).attr("checked",false);
            					$("#facestudy_integratedScore_"+studyNo).val("");
            					$("#facestudy_writtenScore_"+studyNo).val("");
            					$("#facestudy_integratedScore_"+studyNo+"td").text("0");
            				}
            			});
                	}
                }
            });
		}else{
			alertMsg.warn("请先上传成绩文件后，点击核对成绩，再点击保存！如已核对成绩，请检查是否勾选需保存的成绩记录。");
		}
			
	}
	
	//核对成绩
	function facestudy_checkExamResult(){
		var examSubId  = jQuery("#facestudy_examResultExamSubForm >input[id='facestudy_examSubId']").val();
		var guidePlanId = jQuery("#facestudy_examResultExamSubForm >input[id='facestudy_guidePlanId']").val();
		var teachingPlanCourseId = jQuery("#facestudy_examResultExamSubForm >input[id='facestudy_teachingPlanCourseId']").val();
		var classesId   = $("#facestudy_examResultExamSubForm >input[id='facestudy_classesId']").val();
		var gradeId   = $("#facestudy_examResultExamSubForm >input[id='facestudy_gradeId']").val();
		var unitId   = $("#facestudy_examResultExamSubForm >input[id='facestudy_unitId']").val();
		var uploadExamResultFileId = new Array();
		var examInfoId = $("#facestudy_examInfoId").val();
		jQuery("input[name='uploadExamResultFileId']").each(function(){
			uploadExamResultFileId.push(jQuery(this).val());
		});
		if(uploadExamResultFileId.length != 0){
			var url =baseUrl+"/edu3/teaching/transcripts/facestudy/check-examresults.html";
			jQuery.ajax({
				type:"post",
				url:url,
				data:{uploadExamResultFileId:uploadExamResultFileId.toString(),examSubId:examSubId,examInfoId:examInfoId,teachingPlanCourseId:teachingPlanCourseId,guidePlanId:guidePlanId,classesId:classesId,gradeId:gradeId,unitId:unitId},
				dataType:"json",
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					var courseTeachType = resultData['courseTeachType'];
					var readonly = "";
					if(courseTeachType=="networkTeach"){
						readonly = " readonly='readonly' ";
					}
					if(success ==false){
						alertMsg.warn(msg);
					}else {
						$("#facestudy_uploaderExamResultFileDIV input[name='uploadExamResultFileId']").each(function(obj){
							facestudy_deleteAttachFile($(this).val());
						});
						var examResulstVolist = resultData['examResultsVoList'];	
						if(examResulstVolist.length>0){
							jQuery("#facestudy_examResultsVoBody").children().remove();
							for(var i=0;i<examResulstVolist.length;i++){	
								var examResults   = examResulstVolist[i];								
								var checkBoxHTML = "<input type='checkbox' name='importExamresultsResourceid' value="+examResults.studyNo+" autocomplete='off' "; 
								if(null!=examResults.writtenScore&&""!=examResults.writtenScore){
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
									checkBoxHTML="";
								}
								if(""!=examResults.memo){
									jQuery("#facestudy_examResultsVoBody").append(tr+"<td>"+checkBoxHTML+"</td>"+
											"<td>"+examResults.sort+"</td>"+																	
											"<td>"+examResults.branchSchool+"</td>"+
											"<td>"+examResults.major+"</td>"+
											"<td style='color:green'>"+examResults.studyNo+"</td>"+
											"<td style='color:blue'>"+examResults.name+"</td>"+
											"<td>"+examResults.usuallyScore+""+
											"<input id='facestudy_integratedScore_"+examResults.studyNo+"' type='hidden' name='integratedScore"+examResults.studyNo+"' value='' /></td>"+
											"<td>"+examResults.writtenScore+"</td>"+
											"<td>"+examResults.onlineScore+"</td>"+
											"<td id='facestudy_integratedScore_"+examResults.studyNo+"td'>"+(examResults.integratedScore!=null?examResults.integratedScore:"")+"</td>"+
											"<td ><font color='red'>"+examResults.memo+"</font></td></tr><br/>");
								}else{
									var onchange = " onchange=\"ajaxGetIntegratedScore('"+examResults.studyNo+"','"+(examResults.examResultsResourceId!=null?examResults.examResultsResourceId:"")+"')\"";
									jQuery("#facestudy_examResultsVoBody").append(tr+"<td>"+checkBoxHTML+"</td>"+
											"<td>"+examResults.sort+"</td>"+																	
											"<td>"+examResults.branchSchool+"</td>"+
											"<td>"+examResults.major+"</td>"+
											"<td style='color:green'>"+examResults.studyNo+"</td>"+
											"<td style='color:blue'>"+examResults.name+"</td>"+
											"<td><input type='text' id='facestudy_usuallyScore_"+examResults.studyNo+"'"+onchange+" name='usuallyScore"+examResults.studyNo+"' align='middle' class='number' style='width:30px' "+readonly+" value='"+examResults.usuallyScore+"'/>"+
											"<input id='facestudy_integratedScore_"+examResults.studyNo+"' type='hidden' name='integratedScore"+examResults.studyNo+"' value='' /></td>"+
											"<td><input type='text' id='facestudy_writtenScore_"+examResults.studyNo+"'"+onchange+" name='writtenScore"+examResults.studyNo+"' align='middle'  style='width:30px' value='"+examResults.writtenScore+"'/></td>"+
											"<td>"+examResults.onlineScore+""+
											"<input id='facestudy_onlineScore_"+examResults.studyNo+"' type='hidden' name='onlineScore"+examResults.studyNo+"' value='"+examResults.onlineScore+"' /></td>"+
											"<td id='facestudy_integratedScore_"+examResults.studyNo+"td'>"+(examResults.integratedScore!=null?examResults.integratedScore:"")+"</td>"+
											"<td>"+examResults.memo+"</td></tr><br/>");
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
	function ajaxGetIntegratedScore(studyno,resourceid){
		var us  = $("#facestudy_usuallyScore_"+studyno).val();
		var ws = $("#facestudy_writtenScore_"+studyno).val();
		var teachingPlanCourseId = $("#facestudy_teachingPlanCourseId").val();
		var examSubId = $("#facestudy_examSubId").val();
		if(undefined ==ws ){
			ws=0;
		}
		if(undefined ==us ){
			us=0;
		}
		$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/teaching/result/facestudy/caculateIntegratedScore.html",
			data:{ws:ws,us:us,resourceid:resourceid,teachingPlanCourseId:teachingPlanCourseId,examSubId:examSubId,studyno:studyno},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	//alert(data['highest']+"--"+data['highest1']+"--"+data['integratedScore']);
			    	if(undefined!=data['highest']){
						$("#facestudy_writtenScore_"+studyno).val(data['highest']);
					}
			    	if(undefined!=data['highest1']){
						$("#facestudy_usuallyScore_"+studyno).val(data['highest1']);
					}
			    	if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
					$("#facestudy_integratedScore_"+studyno).val(data['integratedScore']);
					$("#facestudy_integratedScore_"+studyno+"td").html(data['integratedScore']);
					/*if("" !=ws&&"" !=us ){
						var $form = $("#facestudy_inputExamResultsSaveForm");
						$("#facestudy_inputExamResultsSearchForm_only").val(studyno);
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
								if(success==false){
									alertMsg.warn(msg);
								}else{
									var pageNum = "${page.pageNum}";
									if(pageNum==""){
										pageNum = "1";
									}
									navTabPageBreak({pageNum:pageNum});
								}
								$("#facestudy_inputExamResultsSearchForm_only").val("");
							}
						});
					}*/
					
				}
			}
		});
		
	}
	//AJAX上传文件
	function facestudy_initUploadExamResults(){
		var teachingPlanCourseId = jQuery("#facestudy_examResultExamSubForm >input[id='facestudy_teachingPlanCourseId']").val();
		var fillinName = "${curUser.cnName}";
		var fillinNameId = "${curUser.resourceid}";
		
		 $("#facestudy_importExamResultsFile").fineUploader({
	            multiple: false,    
				request: {
			        endpoint: baseUrl+'/edu3/filemanage/uploader.html',
			        params: {'formId':teachingPlanCourseId,'formType':'examResultsImportUncheck','fillinName':fillinName,'fillinNameId':fillinNameId}
			    },
		        validation: {  
	                allowedExtensions: ['XLS', 'xls'],
	                sizeLimit: 10485760,
				    acceptFiles:'application/vnd.ms-excel,application/x-excel'             
	             },  
		         messages: {                                               
		        	 typeError: "{file} 无效的扩展名.支持的扩展名: {extensions}.",
	                 sizeError: "{file} 文件太大，最大支持的文件大小 {sizeLimit}.",
	                 minSizeError: "{file} 太小，最小文件大小为：{minSizeLimit}.",
	                 emptyError: "{file} 文件为空，请重新选择",
	                 noFilesError: "文件不存在",
	                 onLeave:"文件正在上传，如果你现在离开，上传将被取消" 
		         },
				  callbacks: {
						onComplete: function(id,name, responseJSON) {
							if(responseJSON.success && responseJSON.result=="OK"){
								$("#facestudy_examResultsFileUL").append("<li id='"+responseJSON.attId+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='facestudy_downloadAttachFile(\""+responseJSON.attId+"\")'>"+name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='facestudy_deleteAttachFile(\""+responseJSON.attId+"\");' style='cursor:pointer;height:10px'></li>")
								$("#facestudy_uploaderExamResultFileDIV").append("<input type='hidden' id='facestudy_uploadExamResultFileId' name='uploadExamResultFileId' value='"+responseJSON.attId+"'>"); //添加隐藏域与业务建立关系
								//每上传一个附件记录一个成绩日志
								facestudy_addExamResultsLog(teachingPlanCourseId,responseJSON.attId);
								
							}else{
								alertMsg.error("文件" + name + "上传失败"); 
							}
						}
					}
			});
		
		/**$("#facestudy_importExamResultsFile").uploadify({
                'script'         : baseUrl+"/edu3/filemanage/upload.html", //上传URL
	            'auto'           : true, //自动上传
	            'scriptData'	 : {'formId':teachingPlanCourseId,'formType':'examResultsImportUncheck',fillinName:fillinName,fillinNameId:fillinNameId},
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:XLS/xls',  //限制文件上传格式描述
	            'fileExt'        : '*.XLS;*.xls;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	
	                $("#facestudy_examResultsFileUL").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='facestudy_downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='facestudy_deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#facestudy_uploaderExamResultFileDIV").append("<input type='hidden' id='facestudy_uploadExamResultFileId' name='uploadExamResultFileId' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
					//每上传一个附件记录一个成绩日志
					facestudy_addExamResultsLog(teachingPlanCourseId,response.split("|")[0]);
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
					$('#facestudy_importExamResultsFile').uploadifyClearQueue(); //清空上传队列
				}
         });
		**/
	}
	//附件下载
   function facestudy_downloadAttachFile(attid){
   		$('#facestudy_frameForDownload').remove();
   		var elemIF = document.createElement("iframe");
   		elemIF.id = "facestudy_frameForDownload"; //创建id
		elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
   }
   
   //附件删除
   function facestudy_deleteAttachFile1(attid){
	  if(confirm("确定要删除这个附件？")){
	  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
		$.get(url,{fileid:attid},function(data){
			$("#facestudy_examResultsFileUL > li[id='"+attid+"']").remove();
			$("#facestudy_uploadExamResultFileId[value='"+attid+"']").remove();
			//删除一个附件则删除对应的成绩日志
			facestudy_removeExamResultsLog(attid);
		});	
	  }
    }
   function facestudy_deleteAttachFile(attid){
		  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
			$.get(url,{fileid:attid},function(data){
				$("#facestudy_examResultsFileUL > li[id='"+attid+"']").remove();
				$("#facestudy_uploadExamResultFileId[value='"+attid+"']").remove();
				//删除一个附件则删除对应的成绩日志
				facestudy_removeExamResultsLog(attid);
			});
	    }
    //上传附件时增加日志
	function facestudy_addExamResultsLog(examInfoId,attid){
		jQuery.ajax({
				type:'post',
				url:baseUrl+'/edu3/teaching/examResultsLog/add.html',
				data:'examInfoId='+examInfoId+"&optionType=examResultsImportUncheck&attid="+attid,
			    dataType:'josn',
			    success:function(examResultsLogId){
			     	if(examResultsLogId != ''){
			     		jQuery("#facestudy_uploaderExamResultFileLogsDIV").append("<input type='hidden' id='"+attid+"' value="+examResultsLogId+" />");
			     	}
			    }	
		});
	}
	//删除附件时删除对应的日志
	function facestudy_removeExamResultsLog(attid){
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