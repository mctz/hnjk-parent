<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线录入面授成绩</title>
<style type="text/css">
td,th{text-align: center;}
</style>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}"
		if(""!=msg){
			$("#facestudy_examResultsInputBody").hide();
			alertMsg.warn(msg);
		}
	});
	//撤销成绩
	function delFaceStudyExamResults(resid,checkStatus,studentName,courseName){
		if('0'==checkStatus){
			alertMsg.confirm("确认要撤销<font color='red'>"+studentName+"《"+courseName+"》</font>的成绩吗？", {
	            okCall: function(){
	            	$.ajax({
						type:'POST',
						url:"${baseUrl}/edu3/teaching/result/facestudy/examresults-del.html",
						data:{resourceid:resid,courseTeachType:"${courseTeachType}"},
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
						}
					});
	            	
	            }
			});
		}else{
			alertMsg.warn("只允许撤销成绩状态为保存的成绩记录！");
		}
	}
	//保存录入的成绩
	function facestudy_inputSave(){
		var isDefaultAbsent = "${isDefaultAbsent}";
		if(isDefaultAbsent!='Y'){
			if(!isChecked('resourceid',"#facestudy_examResultsInputBody")){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 		}
		}
		
		
		var isHasEmptyScore             = false;
		jQuery("#facestudy_examResultsInputBody input[name='resourceid']:checked").each(function(){
			var studyNo = $(this).val();
			var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
			var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
			var onlineScore = $.trim($("#facestudy_onlineScore_"+studyNo).val());
			var integratedScore = $.trim($("#facestudy_integratedScore_"+studyNo).val());
			var examAbnormity = $("#facestudy_examAbnormity_"+studyNo).val();
			if("${(condition['facestudyScorePer'])}"!=100.0||"${(condition['facestudyScorePer'])}"!="100.0"){
				if(examAbnormity=="0"&&(writtenScore==""////||onlineScore==""
					||!usuallyScore.isNumber()||!writtenScore.isNumber()//||!onlineScore.isNumber()
					||parseFloat(writtenScore)<0||parseFloat(writtenScore)>100
					||parseFloat(usuallyScore)<0||parseFloat(usuallyScore)>100
					//||parseFloat(onlineScore)<0||parseFloat(onlineScore)>100
					||parseFloat(integratedScore<0||parseFloat(integratedScore)>100))){
					isHasEmptyScore = true;
					return false;
				};
				if(examAbnormity=="0"&&(usuallyScore==""//||onlineScore==""
					||!usuallyScore.isNumber()//||!writtenScore.isNumber()||!onlineScore.isNumber()
					//||parseFloat(writtenScore)<0||parseFloat(writtenScore)>100
					||parseFloat(usuallyScore)<0||parseFloat(usuallyScore)>100
					//||parseFloat(onlineScore)<0||parseFloat(onlineScore)>100
					||parseFloat(integratedScore<0||parseFloat(integratedScore)>100)
					)){
					isHasEmptyScore = true;
					return false;
				};
			}
			
			if("${(condition['facestudyScorePer'])}"==100.0||"${(condition['facestudyScorePer'])}"=="100.0"){
				if(examAbnormity=="0"&&(writtenScore==""||parseFloat(writtenScore)<0||parseFloat(writtenScore)>100)){
					isHasEmptyScore = true;
					return false;
				}
			};			
		});
		
		var emptyCount = 0;
		jQuery("#facestudy_examResultsInputBody input[name='resourceid']").each(function(){
			var studyNo = $(this).val();
			var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
			var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
			var examAbnormity = $("#facestudy_examAbnormity_"+studyNo).val();
			if(writtenScore=="" && usuallyScore=="" &&examAbnormity=="0"){
				emptyCount++;
			}
		});
		var reminder = "";
		if(isDefaultAbsent=='Y'){
			if(emptyCount>0){
				reminder = "你当前有未录入成绩<font color='red'>"+emptyCount+"</font>条，将默认录入为<font color='red'>缺考</font>";
			}
			jQuery("#facestudy_examResultsInputBody input[name='resourceid']").each(function(){
				var studyNo = $(this).val();
				$(this).attr("checked",true);
				var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
				var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
				var examAbnormity = $("#facestudy_examAbnormity_"+studyNo).val();
				if(writtenScore=="" && usuallyScore=="" &&examAbnormity=="0"){
					 $("#facestudy_examAbnormity_"+studyNo).val("2");
				}
			});
		}else if(isHasEmptyScore && ("${(condition['facestudyScorePer'])}"!=0.0 || "${(condition['facestudyScorePer'])}"!="0.0")){
			alertMsg.warn("成绩不能为空且必须为0-100的数字！");
			return false; 
		}
		var $form = $("#facestudy_inputExamResultsSaveForm");
		if (!$form.valid()) {
			alertMsg.error(DWZ.msg["validateFormError"]);
			return false; 
		}
		
		
		alertMsg.confirm("确认要保存成绩记录吗？"+reminder, {
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
						if(success==false){
							alertMsg.warn(msg);
						}else{
							var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}
					}
				});
            },
            cancelCall:function(){
            	if(isDefaultAbsent=='Y'){
            		jQuery("#facestudy_examResultsInputBody input[name='resourceid']").each(function(){
        				var studyNo = $(this).val();        				
        				var writtenScore = $.trim($("#facestudy_writtenScore_"+studyNo).val());
        				var usuallyScore = $.trim($("#facestudy_usuallyScore_"+studyNo).val());
        				var examAbnormity = $("#facestudy_examAbnormity_"+studyNo).val();
        				if(writtenScore=="" && usuallyScore==""){
        					$(this).attr("checked",false);
        					$("#facestudy_examAbnormity_"+studyNo).val("0");
        				}
        			});
            	}
            }
        });  
	}
	
	function unSubmitOnlineCourse(){
		
		var courseTeachType = "${courseTeachType}";
		if(courseTeachType!='networkTeach'){
			alertMsg.warn('该门课程不是网络课程！');
			return false;
		}
		if(!isChecked('resourceid',"#facestudy_examResultsInputBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var $form = $("#facestudy_inputExamResultsSaveForm");
		alertMsg.confirm("确认要撤销平时考核成绩吗？", {
            okCall: function(){
            	$.ajax({
					type:'POST',
					url:"${baseUrl}/edu3/teaching/result/facestudy/unSubmitOnlineCourse.html",
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
							alertMsg.correct(msg);
						}
					}
				});
            }
        });  
	}
	
	function ajaxGetIntegratedScore(studyno,resourceid){
		var us  = $("#facestudy_usuallyScore_"+studyno).val();
		var ws = $("#facestudy_writtenScore_"+studyno).val();
		var os = $("#facestudy_onlineScore_"+studyno).val();
		var teachingPlanCourseId = $("#facestudy_inputExamResultsSearchForm_teachingPlanCourseId").val();
		var examSubId = $("#facestudy_inputExamResultsSearchForm_examSubId").val();
		var examAbnormity = $("#facestudy_examAbnormity_"+studyno).val();
		if(undefined ==ws ){
			ws=0;
		}
		if(undefined ==us ){
			us=0;
		}
		if(undefined ==os ){
			os=0;
		}
		$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/teaching/result/facestudy/caculateIntegratedScore.html",
			data:{ws:ws,us:us,os:os,resourceid:resourceid,teachingPlanCourseId:teachingPlanCourseId,examSubId:examSubId,studyno:studyno},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	if(undefined!=data['highest']){
						$("#facestudy_writtenScore_"+studyno).val(data['highest']);
					}
			    	if(undefined!=data['highest1']){
						$("#facestudy_usuallyScore_"+studyno).val(data['highest1']);
					}
			    	if(undefined!=data['highest2']){
						$("#facestudy_onlineScore_"+studyno).val(data['highest2']);
					}
			    	if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
					$("#facestudy_integratedScore_"+studyno).val(data['integratedScore']);
					
					$("#facestudy_integratedScore_"+studyno+"td").html(data['integratedScore']);
		//			$("#facestudy_integratedScore_"+studyno+"td").attr("style","color:red;");
					if("" !=ws&&"" !=us ){
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
					}
					
				}
			}
		});
		
	}
	
	
	//替换平时成绩
	function replaceScore(){
		alertMsg.confirm("确认要用随堂练习分数替换平时成绩吗?", {
            okCall: function(){
            	var examSubId 				= 		$('#facestudy_inputExamResultsSearchForm_examSubId').val();
            	var studentStatus1 			= 		$('#facestudy_inputExamResultsSearchForm_studentStatus1').val();
            	var studentStatus2 			= 		$('#facestudy_inputExamResultsSearchForm_studentStatus2').val();
            	var teachplanid 			= 		$('#facestudy_inputExamResultsSearchForm_teachplanid').val();
            	var classesId 				= 		$('#facestudy_inputExamResultsSearchForm_classesId').val();
            	var teachType 				= 		$('#facestudy_inputExamResultsSearchForm_teachType').val();
            	var courseId 				= 		$('#facestudy_inputExamResultsSearchForm_courseId').val();
            	var isPass 					= 		$('#facestudy_inputExamResultsSearchForm_isPass').val();
            	var teachingPlanCourseId 	= 		$('#facestudy_inputExamResultsSearchForm_teachingPlanCourseId').val();
            	var branchschoolid			=		$('#facestudy_inputExamResultsSearchForm_branchschoolid').val();
            	var data = "examSubId="+examSubId+"&studentStatus1="+studentStatus1+"&studentStatus2="+studentStatus1+"&teachplanid="+teachplanid+"&classesId="+classesId+"&teachType="+teachType+"&courseId="+courseId+"&isPass="+isPass+"&teachingPlanCourseId="+teachingPlanCourseId+"&branchschoolid="+branchschoolid;
            	
            	$.ajax({
					type:'POST',
					url:"${baseUrl}/edu3/teaching/transcripts/facestudy/replace-score.html",
					data:data,
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(resultData){
						alertMsg.info(resultData.rtmsg);
						$('#facestudy_inputExamResultsSearchForm').submit();
					}
				});
            }
		});
	}
	
	//刷新网上学习成绩
	function updateOnlineScore(){
		var msg = "<span style='color:#FF0000'>此操作为自动计算与保存网上学习分数。<br/>"
			+ "注意点1：请在网上学习时间规定结束后再使用【刷新网上学习成绩】<br/>注意点2：刷新后如果成绩如须更改课后作业分数，请在“平时成绩录入”页面手动输入分数</span><br/>"
			+ "确认要刷新网上学习成绩吗?";
		alertMsg.confirm(msg, {
            okCall: function(){
            	var examSubId 				= 		$('#facestudy_inputExamResultsSearchForm_examSubId').val();
            	var studentStatus1 			= 		$('#facestudy_inputExamResultsSearchForm_studentStatus1').val();
            	var studentStatus2 			= 		$('#facestudy_inputExamResultsSearchForm_studentStatus2').val();
            	var teachplanid 			= 		$('#facestudy_inputExamResultsSearchForm_teachplanid').val();
            	var classesId 				= 		$('#facestudy_inputExamResultsSearchForm_classesId').val();
            	var teachType 				= 		$('#facestudy_inputExamResultsSearchForm_teachType').val();
            	var courseId 				= 		$('#facestudy_inputExamResultsSearchForm_courseId').val();
            	var isPass 					= 		$('#facestudy_inputExamResultsSearchForm_isPass').val();
            	var teachingPlanCourseId 	= 		$('#facestudy_inputExamResultsSearchForm_teachingPlanCourseId').val();
            	var branchschoolid			=		$('#facestudy_inputExamResultsSearchForm_branchschoolid').val();
            	var data = "examSubId="+examSubId+"&studentStatus1="+studentStatus1+"&studentStatus2="+studentStatus1+"&teachplanid="+teachplanid+"&classesId="+classesId+"&teachType="+teachType+"&courseId="+courseId+"&isPass="+isPass+"&teachingPlanCourseId="+teachingPlanCourseId+"&branchschoolid="+branchschoolid;
            	
            	$.ajax({
					type:'POST',
					url:"${baseUrl}/edu3/teaching/transcripts/facestudy/updateonlinescore.html",
					data:data,
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(resultData){
						alertMsg.info(resultData.rtmsg);
						$('#facestudy_inputExamResultsSearchForm').submit();
					}
				});
            }
		});
	}
	
	//提交成绩
	function submitFaceStudyExamResults(){
		/*var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		//var gradeId = "${condition['gradeid']}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){*/
			var url = "${baseUrl}/edu3/teaching/result/facestudy/input-examresults-submit.html";
			alertMsg.confirm("您确定要提交《${course.courseName}》的成绩到考务办吗？", {
				okCall: function(){
					jQuery.post(url,{teachingPlanCourseId:"${condition['teachingPlanCourseId']}",examSubId:"${condition['examSubId']}"
						,classesId:"${condition['classesId']}",gradeId:"${condition['gradeid']}",unitId:"${condition['unitId']}",teachType:"${condition['teachType']}"},function(resultData){
						var msg 	   		= resultData['message'];
					  	var statusCode 		= resultData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
					  		var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				}
			});	
		//}			
	}
	function checkId(studyno){
		$("input[name='resourceid'][value='"+studyno+"']").attr("checked", true);
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="facestudy_inputExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/facestudy/input-examresults-list.html"
				method="post">
				<input id="facestudy_inputExamResultsSearchForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input id="facestudy_inputExamResultsSearchForm_courseTeachType"
					type="hidden" name="courseTeachType" value="${courseTeachType}" />
					
				<input id="facestudy_inputExamResultsSearchForm_facestudyScorePer"
					type="hidden" name="facestudyScorePer"
					value="${condition['facestudyScorePer'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_teachingPlanCourseId"
					type="hidden" name="teachingPlanCourseId"
					value="${condition['teachingPlanCourseId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_guidPlanId" type="hidden"
					name="guidPlanId" value="${condition['guidPlanId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_classesId" type="hidden"
					name="classesId" value="${condition['classesId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_gradeid" type="hidden"
					name="gradeid" value="${condition['gradeid'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_schoolid" type="hidden"
					name="branchschoolid" value="${condition['branchschoolid'] }" />

				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName']}" style="width: 53%" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 53%" /></li>
						<li><label>录入状态：</label> <select name="checkStatus">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['checkStatus'] eq '-1'}"> selected="selected"</c:if>>未录入</option>
								<option value="0"
									<c:if test="${condition['checkStatus'] eq '0'}"> selected="selected"</c:if>>保存</option>
								<option value="1"
									<c:if test="${condition['checkStatus'] eq '1'}"> selected="selected"</c:if>>提交</option>
						</select></li>
					</ul>
					<div class="subBar">
						<label style="margin-top: 5px;color: red;width: 250px;">${scoreper }</label>
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST"
			pageType="subsave"></gh:resAuth>
		<div class="pageContent">
			<form id="facestudy_inputExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/facestudy/input-examresults-save.html?courseTeachType=${courseTeachType}"
				method="post">
				<input id="facestudy_inputExamResultsSearchForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input id="facestudy_inputExamResultsSearchForm_courseTeachType"
					type="hidden" name="courseTeachType" value="${condition['courseTeachType']}" />
				<input id="facestudy_inputExamResultsSearchForm_facestudyScorePer"
					type="hidden" name="facestudyScorePer"
					value="${condition['facestudyScorePer'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_teachingPlanCourseId"
					type="hidden" name="teachingPlanCourseId"
					value="${condition['teachingPlanCourseId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_guidPlanId" type="hidden"
					name="guidPlanId" value="${condition['guidPlanId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_isMachineExam"
					type="hidden" value="${condition['isMachineExam']}"
					name="isMachineExam" /> <input
					id="facestudy_inputExamResultsSearchForm_studentStatus1"
					type="hidden" name="studentStatus1"
					value="${condition['studentStatus1'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_studentStatus2"
					type="hidden" name="studentStatus2"
					value="${condition['studentStatus2'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_teachplanid" type="hidden"
					name="teachplanid" value="${condition['teachplanid'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_classesId" type="hidden"
					name="classesId" value="${condition['classesId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_teachType" type="hidden"
					name="teachType" value="${condition['teachType'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_courseId" type="hidden"
					name="courseId" value="${condition['courseId'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_isPass" type="hidden"
					name="isPass" value="${condition['isPass'] }" /> <input
					id="facestudy_inputExamResultsSearchForm_branchschoolid"
					type="hidden" value="${condition['branchschoolid']}" /> <input
					id="facestudy_inputExamResultsSearchForm_only" type="hidden"
					name="only" value="" />
				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<th width="2%"><input type="checkbox" name="checkall"
								id="check_all_facestudy_examResults_input_save"
								onclick="checkboxAll('#check_all_facestudy_examResults_input_save','resourceid','#facestudy_examResultsInputBody')" /></th>
							<th width="10%">课程名称</th>
							<th width="10%">教学站</th>
							<th width="10%">专业</th>
							<th width="10%">学号</th>
							<th width="6%">姓名</th>
							<th width="7%">平时考核成绩</th>
							<th width="7%">卷面成绩</th>
							<!-- <th width="7%">网上学习成绩</th> -->
							<th width="7%">综合成绩</th>
							<th width="7%">成绩情况</th>
							<th width="7%">录入状态</th>
						</tr>
					</thead>
					<tbody id="facestudy_examResultsInputBody">
						<c:forEach items="${page.result}" var="vo" varStatus="vs">
							<c:choose>
								<c:when test="${vo.checkStatus>0 }">
									<tr style="background-color: #e4f5ff">
										<td></td>
										<td>${course.courseName }</td>
										<td>${vo.branchSchool }</td>
										<td>${vo.major}</td>
										<td>${vo.studyNo}</td>
										<td>${vo.name}</td>
										<td>
											<c:choose>
												<c:when test="${vo.courseScoreType eq '25'}">
													${ghfn:dictCode2Val('CodeScoreChar',vo.usuallyScore) }
												</c:when>
												<c:otherwise>${vo.usuallyScore}</c:otherwise>
											</c:choose>
										</td>
										<td>
											<c:choose>
												<c:when test="${vo.courseScoreType eq '25'}">
													${ghfn:dictCode2Val('CodeScoreChar',vo.writtenScore) }
												</c:when>
												<c:otherwise>${vo.writtenScore}</c:otherwise>
											</c:choose>
											<%--<c:if test="${vo.examAbnormity eq '0' }">${vo.writtenScore}</c:if>--%>
										</td>
										<%-- <td>${vo.onlineScore}</td> --%>
										<td
											<c:if test="${vo.integratedScore lt 60.0}">style="color:red"</c:if>>
											<c:choose>
												<c:when test="${vo.courseScoreType eq '25'}">
													${ghfn:dictCode2Val('CodeScoreChar',vo.integratedScore) }
												</c:when>
												<c:otherwise>${vo.integratedScore}</c:otherwise>
											</c:choose>
											<%--<c:if test="${vo.examAbnormity eq '0' }">${vo.integratedScore}</c:if>--%>
										</td>
										<td>${ghfn:dictCode2Val('CodeExamAbnormity',vo.examAbnormity) }</td>
										<td style="color: green">${ghfn:dictCode2Val('CodeExamResultCheckStatus',vo.checkStatus)}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${vo.studyNo}" autocomplete="off" /> <input
											type="hidden" name="resultsId${vo.studyNo}"
											value="${vo.examResultsResourceId}" /></td>
										<td>${course.courseName }</td>
										<td>${vo.branchSchool }</td>
										<td>${vo.major}</td>
										<td>${vo.studyNo}</td>
										<td>${vo.name}<c:if test="${vo.studentstatus eq '16' }">
												<span style="color: red">[${ghfn:dictCode2Val('CodeStudentStatus',vo.studentstatus) }]</span>
											</c:if>
										</td>
										<td>
													<input id="facestudy_usuallyScore_${vo.studyNo}"
														type="text" name="usuallyScore${vo.studyNo}"
														onblur="checkId('${vo.studyNo}')"
														<c:if test="${condition['scoreSaveMode'] == '0' }">
						            		onchange="ajaxGetIntegratedScore('${vo.studyNo}','${vo.examResultsResourceId}')"
						            		</c:if>
														align="middle" class="number" style="width: 30px"
														value="${vo.usuallyScore}"
														${(courseTeachType=='networkTeach')?'readonly="readonly"':''} />
											<%-- 	</c:otherwise> 
											</c:choose> --%>
											<input id="facestudy_integratedScore_${vo.studyNo}"
											type="hidden" name="integratedScore${vo.studyNo}"
											align="middle" class="number" style="width: 30px"
											value="${vo.integratedScore}" /></td>
										<td><c:choose>
												<c:when test="${condition['isMachineExam']=='Y' }">
							         	 ${vo.writtenScore}
							         	 <input id="facestudy_writtenScore_${vo.studyNo}"
														type="hidden" name="writtenScore${vo.studyNo}"
														onblur="checkId('${vo.studyNo}')" align="middle"
														class="number" style="width: 30px"
														value="${vo.writtenScore}" />
												</c:when>
												<c:otherwise>
													<c:if test="${condition['facestudyScorePer'] gt 0.0}">
														<input id="facestudy_writtenScore_${vo.studyNo}"
															type="text" name="writtenScore${vo.studyNo}"
															onblur="checkId('${vo.studyNo}')"
															<c:if test="${condition['scoreSaveMode'] == '0' }">
									             onchange="ajaxGetIntegratedScore('${vo.studyNo}','${vo.examResultsResourceId}')"
									           		 </c:if>
															align="middle" class="number" style="width: 30px"
															value="${vo.writtenScore}" />
													</c:if>
												</c:otherwise>
											</c:choose></td>

										<%-- <td>${vo.onlineScore }<input
											id="facestudy_onlineScore_${vo.studyNo}" type="hidden"
											name="onlineScore${vo.studyNo}"
											onblur="checkId('${vo.studyNo}')" align="middle"
											class="number" style="width: 30px" value="${vo.onlineScore}" /> --%>
										</td>
										<td id="facestudy_integratedScore_${vo.studyNo}td"
											<c:if test="${vo.integratedScore lt 60.0}">style="color:red"</c:if>>
											<c:choose>
												<c:when test="${not empty vo.examResultsChs}">
													${vo.examResultsChs}
												</c:when>
												<c:otherwise>
													${vo.integratedScore}
												</c:otherwise>
											</c:choose>
										</td>
										<td><gh:select id="facestudy_examAbnormity_${vo.studyNo}"
												name="examAbnormity${vo.studyNo}"
												dictionaryCode="CodeExamAbnormity" choose="N"
												value="${vo.examAbnormity}" excludeValue="${noShowCodeStr }" />
										</td>
										<td
											<c:if test="${vo.checkStatus > -1 }"> style="color:blue" </c:if>>
											${ghfn:dictCode2Val('CodeExamResultCheckStatus',vo.checkStatus)}
											<c:if
												test="${vo.checkStatus == 0 && condition['isRevokeExam']=='Y' }">
												<a href="javaScript:void(0)"
													onclick="delFaceStudyExamResults('${vo.examResultsResourceId}','${vo.checkStatus}','${vo.name}','${course.courseName }')">
													| 撤销</a>
											</c:if>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>

						</c:forEach>

					</tbody>
				</table>
				<!--防止剩下单文本框时，回车就提交了表单  -->
				<div style="display: none;">
					<input type="text" id="empty" />
				</div>
			</form>
			<c:choose>
				<c:when test="${condition['scoreSaveMode'] == '0' }">
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/result/facestudy/input-examresults-list.html"
						pageType="sys" condition="${condition }" />
				</c:when>
				<c:otherwise>
					<gh:page page="${page}" targetType="navTab"
						beforeForm="facestudy_inputExamResultsSaveForm"
						postBeforeForm="${condition['isAllowInput']}"
						goPageUrl="${baseUrl }/edu3/teaching/result/facestudy/input-examresults-list.html"
						pageType="sys" condition="${condition }" />
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>