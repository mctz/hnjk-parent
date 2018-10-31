<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入学生平时成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<table width="100%">
				<tr align="center" valign="middle" height="10px">
					<td colspan="3">
						<div style="position: fixed; float: left;">
							<div style="float: left">
								<input name="importFile" id="importExamResultsFile" type="file" />
							</div>
						</div>
						<div style="float: left; width: 100px; margin-left: 66px;">
							<a href="javascript:void(0)" class="button"
								onclick="checkExamResultUS();"><span style="height: 60px">核对成绩</span></a>
						</div>
					</td>
				</tr>
				<tr height="80px">
					<td colspan="2" width="60%">
						<div style="margin-left: 10px;">
							<font color="red">
								<p style="font-weight: bolder">操作说明：</p>
								<P>1、如果课程名为红色字体，说明导入成绩与系统中成绩不一致，请将鼠标移入红色文字处查看原成绩。</P>
								<P>2、上传同一批次的成绩文件</P>
								<P>3、建议一次只上传一个成绩文件</P>
								<P>4、点击核对成绩</P>
								<P>5、保存</P>
							</font>
						</div>
					</td>
					<td rowspan="2" width="40%"></td>
				</tr>
				<tr height="10px">
					<td colspan="2" width="50%">
						<ul id="examResultsFileUL">
							<c:forEach var="att" items="${attachList }">
								<li id="${att.resourceid }"><img
									src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"
									style='cursor: pointer; height: 10px'>&nbsp;&nbsp; <a
									href="javascript:void(0)"
									onclick="downloadAttachFile('${att.resourceid }')">${att.attName }&nbsp;</a>&nbsp;&nbsp;
									<img
									src="${baseUrl }/jscript/jquery.uploadify/images/cancel.png"
									onClick="deleteAttachFileUS('${att.resourceid }');"
									style="cursor: pointer; height: 10px"></li>
							</c:forEach>
						</ul>
					</td>
					<td width="50%"></td>
				</tr>
			</table>
		</div>
		<gh:resAuth
			parentCode="RES_TEACHING_RESULT_NETWORKSTUDY_INPUT_WRITTRNSCORELIST1"
			pageType="importSub"></gh:resAuth>
		<div class="pageContent" layouth="181">
			<form id="examResultExamSubFormUS"
				action="${baseUrl}/edu3/teaching/transcripts/upload-processUS.html">

				<div id="uploaderExamResultFileDIVUS" style="display: none;">
					<c:forEach var="att" items="${attachList }">
						<input type="hidden" id="uploadExamResultFileId"
							name="uploadExamResultFileId" value="${att.resourceid }">
					</c:forEach>
				</div>
				<div id="uploaderExamResultFileLogsDIV" style="display: none;">
					<c:forEach var="logs" items="${logList }">
						<input type="hidden" id="${logs.attachId }"
							value="${logs.resourceid }" />
					</c:forEach>
				</div>
				<input id="examSubId" name="examSubId" type="hidden"
					value="${resultMap['examSubId'] }" /> <input id="examInfoId"
					name="examInfoId" type="hidden" value="${resultMap['examInfoId'] }">
				<table class="list" width="100%">
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="import_examresult_check_all"
							onclick="checkboxAll('#import_examresult_check_all','importExamresoultsResourceid','#examResultsVoBodyUS')" /></th>
						<th width="4%">序号</th>
						<th width="15%">课程名</th>
						<th width="16%">教学站</th>
						<th width="17%">专业</th>
						<th width="8%">学号</th>
						<th width="8%">姓名</th>
						<th width="7%">平时成绩</th>
						<th width="7%">录入状态</th>
					</tr>
					<tbody id="examResultsVoBodyUS">

					</tbody>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	$(document).ready(function(){
 		var success = "${resultMap['success']}"	;
 		var msg     = "${resultMap['msg']}";
 		if(success=="false"){
 			alertMsg.warn(msg);
 			return ;
 		}
 		
 		initUploadExamResultsUS("/edu3/filemanage/upload.html");
	});

	//保存成绩
	function processImprotUS(){
		var importExamresoultsResourceid = new Array();
		jQuery("#examResultsVoBodyUS input[name='importExamresoultsResourceid']:checked").each(function(){
			importExamresoultsResourceid.push(jQuery(this).val());
		});
		if(importExamresoultsResourceid.length>0){
			var $form = $("#examResultExamSubFormUS");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要保存所选的平时成绩吗？", {
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
							var saveList = resultData['saveList'];
							var errorList= resultData['errorList'];
							var updateNum= resultData['updateRecordCount'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								if(null!=saveList){
									/*
									for(var i=0;i<saveList.length;i++){
										$("#examResultsVoBodyUS  input[name='importExamresoultsResourceid'][value='"+saveList[i]+"']").parent().parent().remove();
									}
									if($("#examResultsVoBodyUS  input[name='importExamresoultsResourceid']").size()==0){
										alertMsg.confirm("已保存：<font color='red'>"+updateNum+"</font>条记录!<br/>你已保存上传成绩文件中的所有成绩记录，是否将上传的成绩文件删除?", {
											okCall: function(){
												$("#uploaderExamResultFileDIVUS input[name='uploadExamResultFileId']").each(function(obj){
													deleteAttachFileUS($(this).val());
												});
											}
										});
									} else {
										alertMsg.info("已保存：<font color='red'>"+updateNum+"</font>条记录!");
									}*/
									for(var i=0;i<saveList.length;i++){
										$("#examResultsVoBodyUS  input[name='importExamresoultsResourceid'][value='"+saveList[i]+"']").parent().parent().remove();
									}
									alertMsg.info("已保存：<font color='red'>"+updateNum+"</font>条记录!<br/>");
									/*alertMsg.confirm("已保存：<font color='red'>"+updateNum+"</font>条记录!<br/>，是否将上传的成绩文件删除?", {
										okCall: function(){
											$("#uploaderExamResultFileDIVUS input[name='uploadExamResultFileId']").each(function(obj){
												deleteAttachFileUS($(this).val());
											});
										}
									});*/
								}
								if(null!=errorList){
									alertMsg.warn(msg);
								}
							}
						}
					});
                }
            });
		}else{
			alertMsg.warn("请先上传成绩文件后，点击核对成绩，再点击保存！");
		}
			
	}
	//清空成绩
	function revocationImprotUS(){
		alertMsg.confirm("确定要清除已填入的成绩吗？", {
			okCall: function(){
				$("#examResultsVoBodyUS  input[name='importExamresoultsResourceid']:checked").each(function(obj){
					$(this).parent().parent().find("input[name^=usuallyScore]").val("");
				});
			}
		});
	}
	
	//核对成绩
	function checkExamResultUS(){
		
		var examSubId  = jQuery("#examResultExamSubFormUS >input[id='examSubId']").val();
		var examInfoId = jQuery("#examResultExamSubFormUS >input[id='examInfoId']").val();
		var uploadExamResultFileId = new Array();
		
		jQuery("input[name='uploadExamResultFileId']").each(function(){
			uploadExamResultFileId.push(jQuery(this).val());
		});
		if(uploadExamResultFileId.length != 0){
			var url =baseUrl+"/edu3/teaching/transcripts/check-examresults.html";
			jQuery.ajax({
				type:"post",
				url:url,
				data:"uploadExamResultFileId="+uploadExamResultFileId.toString()+"&examSubId="+examSubId+"&examInfoId="+examInfoId+"&type=normal",
				dataType:"json",
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					if(success ==false){
						alertMsg.warn(msg);
					}else {
						var examResulstVolist = resultData['examResultsVoList'];
						var hasNoRecode       = resultData['hasNoRecode'];  
						var noRecodeList      = resultData['noRecodeList'];
						var isSpecialSub      = resultData['isSpecialSub'];
						$("#uploaderExamResultFileDIVUS input[name='uploadExamResultFileId']").each(function(obj){
							deleteAttachFileUS($(this).val());
						});
						if(examResulstVolist.length>0){//当前考试批次中有相应的预约记录
							jQuery("#examResultsVoBodyUS").children().remove();
							for(var i=0;i<examResulstVolist.length;i++){	
								var examResults   = examResulstVolist[i];
								var checkBoxHTML = "<input type='checkbox' name='importExamresoultsResourceid' value="+examResults.examResultsResourceId+" autocomplete='off' "; 
								if(null!=examResults.usuallyScore&&""!=examResults.usuallyScore&&(examResults.checkStatus==0||examResults.checkStatus==undefined)){
									checkBoxHTML+="checked='checked'";
								}
								if(examResults.checkStatus>0){
									checkBoxHTML+=" disabled = 'disabled' ";
								}
								checkBoxHTML+="/>";
								
								var tr = "";
								if("Y"==examResults.flag&&examResults.checkStatus==0){
									tr = "<tr style='color: red' title='导入成绩与系统中不一致，原成绩为："+examResults.message+"'>";
								}else{
									tr = "<tr >";
								}
								var readyOnly           = "";
								if(examResults.checkStatus>0){
									readyOnly =" readonly = 'readonly' ";
								}
								if(""!=examResults.memo){
									checkBoxHTML="";
								}	
								if(""!=examResults.memo){
								jQuery("#examResultsVoBodyUS").append(tr+"<td width='4%'>"+checkBoxHTML+"</td>"+
																	"<td width='4%'>"+examResults.sort+"</td>"+
																	"<td width='15%'>"+examResults.courseName+"</td>"+
																	"<td width='16%'>"+examResults.branchSchool+"</td>"+
																	"<td width='17%'>"+examResults.major+"</td>"+
																	"<td width='8%' style='color:green'>"+examResults.studyNo+"</td>"+
																	"<td width='8%' style='color:blue'>"+examResults.name+"</td>"+
																	"<td width='7%'>"+examResults.usuallyScore+"</td>"+
																	"<td width='7%'><font color='red'>"+examResults.memo+"</font></td>");
								}else{
									jQuery("#examResultsVoBodyUS").append(tr+"<td width='4%'>"+checkBoxHTML+"</td>"+
											"<td width='4%'>"+examResults.sort+"</td>"+
											"<td width='15%'>"+examResults.courseName+"</td>"+
											"<td width='16%'>"+examResults.branchSchool+"</td>"+
											"<td width='17%'>"+examResults.major+"</td>"+
											"<td width='8%' style='color:green'>"+examResults.studyNo+"</td>"+
											"<td width='8%' style='color:blue'>"+examResults.name+"</td>"+
											"<td width='7%'><input type='text' name='usuallyScore"+examResults.examResultsResourceId+"' "+readyOnly+"   align='middle'  style='width:30px' value='"+examResults.usuallyScore+"'/></td>"+
											"<td width='7%'>"+examResults.memo+"</td>");
								}
							}
						}
						
						if(hasNoRecode == true){//当前考试批次中没有相应的预约记录	
							if(noRecodeList.length>0){
								var noRecodeExamResultBody  = "<tbody id='noRecodeExamResultBody'><tr align='center'><td colspan='11'  style='color: red;text-align: center;font-weight: bolder' >以下成绩在所选的考试批次找不到记录！</td></tr>"
								    noRecodeExamResultBody += '<tr ><th width="4%"><input type="checkbox" name="checkall"  id="noRecode_import_check_all" onclick="';
								    noRecodeExamResultBody += 'checkboxAll(\'#noRecode_import_check_all\',\'directlyImportExamResoults\',\'#noRecodeExamResultBody\')"/></th>';
									noRecodeExamResultBody += "<th width='4%'>序号</th><th width='22%'>课程名</th><th width='23%'>教学中心</th> <th width='24%'>专业 </th>";
									noRecodeExamResultBody += "<th width='8%'>学号</th><th width='8%'>姓名</th><th width='7%'>平时成绩</th></tr>";    
								for(var i=0;i<noRecodeList.length;i++){
									
									var examResults         =  noRecodeList[i];
									var readyOnly           = "";
									if(examResults.checkStatus>0){
										readyOnly =" readonly = 'readonly' ";
									}
									noRecodeExamResultBody += "<tr ><td width='4%'><input type='checkbox' name='directlyImportExamResoults' value='"+examResults.studyNo+"$"+examResults.courseId+"$"+examResults.examSubId+"' autocomplete='off' /></td>";
									noRecodeExamResultBody += "<td width='4%'>"+examResults.sort+"</td>";
									noRecodeExamResultBody += "<td width='15%'></td>";
									noRecodeExamResultBody += "<td width='16%'>"+examResults.branchSchool+"</td>";
									noRecodeExamResultBody += "<td width='17%'>"+examResults.major+"</td>";
									noRecodeExamResultBody += "<td width='8%'>"+examResults.studyNo+"</td>";
									noRecodeExamResultBody += "<td width='8%'>"+examResults.name+"</td>";
									noRecodeExamResultBody += "<td width='7%'><input type='text' name='usuallyScore"+examResults.studyNo+examResults.courseId+"' align='middle' class='number' style='width:30px' "+readyOnly+" value='"+examResults.usuallyScore+"'/></td>/tr>";
									
								}
								noRecodeExamResultBody     += "</tbody>";
								if($("#noRecodeExamResultBody")){
									$("#noRecodeExamResultBody").remove();
								}
								jQuery("#examResultsVoBodyUS").after(noRecodeExamResultBody);
							}
						}
						if("Y" == isSpecialSub){//特殊考试批次成绩
							var specialSubList = resultData['specialSubList'];
							if(specialSubList.length>0){
								var specialSubExamResultBody  = "<tbody id='specialSubExamResultBody'><tr align='center'><td colspan='11'  style='color: red;text-align: center;font-weight: bolder' >以下成绩在所选的考试批次找不到记录！</td></tr>"
								    specialSubExamResultBody += '<tr ><th width="4%"><input type="checkbox" name="checkall"  id="specialSub_import_check_all" onclick="';
								    specialSubExamResultBody += 'checkboxAll(\'#specialSub_import_check_all\',\'directlyImportExamResoults\',\'#specialSubExamResultBody\')"/></th>';
								    specialSubExamResultBody += "<th width='4%'>序号</th><th width='15%'>课程名</th><th width='16%'>教学中心</th> <th width='17%'>专业 </th>";
								    specialSubExamResultBody += "<th width='8%'>学号</th><th width='8%'>姓名</th><th width='7%'>平时成绩</th></tr>";    
								
								for(var i=0;i<specialSubList.length;i++){
									
									var examResults           =  specialSubList[i];
									var readyOnly           = "";
									if(examResults.checkStatus>0){
										readyOnly =" readonly = 'readonly' ";
									}
									specialSubExamResultBody += "<tr ><td width='4%'><input type='checkbox' name='directlyImportExamResoults'  value='"+examResults.studyNo+"$"+examResults.courseId+"$"+examResults.examSubId+"' autocomplete='off' /></td>";
									specialSubExamResultBody += "<td width='4%'>"+examResults.sort+"</td>";
									specialSubExamResultBody += "<td width='15%'></td>";
									specialSubExamResultBody += "<td width='16%'>"+examResults.branchSchool+"</td>";
									specialSubExamResultBody += "<td width='17%'>"+examResults.major+"</td>";
									specialSubExamResultBody += "<td width='8%'>"+examResults.studyNo+"</td>";
									specialSubExamResultBody += "<td width='8%'>"+examResults.name+"</td>";
									specialSubExamResultBody += "<td width='7%'><input type='text' name='usuallyScore"+examResults.studyNo+examResults.courseId+"' align='middle' class='number' style='width:30px' "+readyOnly+" value='"+examResults.usuallyScore+"'/></td>";
								}
								specialSubExamResultBody     += "</tbody>";
								if($("#specialSubExamResultBody")){
									$("#specialSubExamResultBody").remove();
								}
								jQuery("#examResultsVoBodyUS").after(specialSubExamResultBody);
								
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
	function initUploadExamResultsUS(url){
		var examInfoId= jQuery("#examResultExamSubFormUS >input[id='examInfoId']").val();
		$("#importExamResultsFile").uploadify({
                'script'         : baseUrl+url, //上传URL
	            'auto'           : true, //自动上传
	            'scriptData'	 : {'formId':examInfoId,'formType':'examResultsImportUncheck'},
	            'multi'          : false, //多文件上传
	            'fileDesc'       : '支持格式:XLS/xls',  //限制文件上传格式描述
	            'fileExt'        : '*.XLS;*.xls;', //限制文件上传的类型,必须有fileDesc这个性质
	            'sizeLimit'      : 10485760, //限制单个文件上传大小10M 
	            'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
	                $("#examResultsFileUL").append("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFileUS(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#uploaderExamResultFileDIVUS").append("<input type='hidden' id='uploadExamResultFileId' name='uploadExamResultFileId' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
					//每上传一个附件记录一个成绩日志
					addExamResultsLogUS(examInfoId,response.split("|")[0]);
				},
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
					alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
					$('#importExamResultsFile').uploadifyClearQueue(); //清空上传队列
				}
         });
	}
	//附件下载
   function downloadAttachFileUS(attid){
   		$('#frameForDownload').remove();
   		var elemIF = document.createElement("iframe");
   		elemIF.id = "frameForDownload"; //创建id
		elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
   }
   
   //附件删除
   function deleteAttachFileUS1(attid){
	  if(confirm("确定要删除这个附件？")){
	  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
		$.get(url,{fileid:attid},function(data){
			$("#examResultsFileUL > li[id='"+attid+"']").remove();
			$("#uploadExamResultFileId[value='"+attid+"']").remove();
			//删除一个附件则删除对应的成绩日志
			removeExamResultsLogUS(attid);
		});	
	  }
    }
   function deleteAttachFileUS(attid){
  	  	var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
  		$.get(url,{fileid:attid},function(data){
  			$("#examResultsFileUL > li[id='"+attid+"']").remove();
  			$("#uploadExamResultFileId[value='"+attid+"']").remove();
  			//删除一个附件则删除对应的成绩日志
  			removeExamResultsLogUS(attid);
  		});	
      }
    //上传附件时增加日志
	function addExamResultsLogUS(examInfoId,attid){
		jQuery.ajax({
					type:'post',
					 url:baseUrl+'/edu3/teaching/examResultsLog/add.html',
					data:'examInfoId='+examInfoId+"&optionType=examResultsImportUncheck&attid="+attid,
			    dataType:'josn',
			     success:function(examResultsLogId){
			     	if(examResultsLogId != ''){
			     		jQuery("#uploaderExamResultFileLogsDIV").append("<input type='hidden' id='"+attid+"' value="+examResultsLogId+" />");
			     	}
			     }	
		});
	}
	//删除附件时删除对应的日志
	function removeExamResultsLogUS(attid){
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