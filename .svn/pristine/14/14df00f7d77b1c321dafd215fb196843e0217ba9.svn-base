<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>非统考成绩管理》补考成绩录入》成绩录入</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}"
		if(""!=msg){
			$("#nonfacestudy_examResultsInputBody").hide();
			alertMsg.warn(msg);
		}
	});
	//撤销成绩
	function delnonfacestudyExamResults(resid,checkStatus,studentName,courseName){
		//alert(checkStatus + "checkStatus");
		if('0'==checkStatus){
			alertMsg.confirm("确认要撤销<font color='red'>"+studentName+"《"+courseName+"》</font>的成绩吗？", {
	            okCall: function(){
	            	$.ajax({
						type:'POST',
						url:"${baseUrl}/edu3/teaching/result/facestudy/non-examresults-del.html",
						data:{resourceid:resid},
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
	
	//保存录入的成绩(功能按钮)
	function fail_facestudy_inputSave(){
		if(!isChecked('resourceid',"#nonfacestudy_examResultsInputBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var isHasEmptyScore             = false;
		jQuery("#nonfacestudy_examResultsInputBody input[name='resourceid']:checked").each(function(){
			var studyNo = $(this).val();
			var writtenScore = $.trim($("#nonfacestudy_writtenScore_"+studyNo).val());
			var usuallyScore = $.trim($("#nonfacestudy_usuallyScore_"+studyNo).val());
			var integratedScore = $.trim($("#nonfacestudy_integratedScore_"+studyNo).val());
			var examAbnormity = $("#nonfacestudy_examAbnormity_"+studyNo).val();
			
			var parentNo = $("#nonfacestudy_parentId_"+studyNo).val();
			
			if(examAbnormity=="0"&&(writtenScore==""||usuallyScore==""
					||!writtenScore.isNumber()||!usuallyScore.isNumber()
					||parseFloat(writtenScore)<0||parseFloat(writtenScore)>100
					||parseFloat(usuallyScore)<0||parseFloat(usuallyScore)>100
					||parseFloat(integratedScore<0||parseFloat(integratedScore)>100))){
				isHasEmptyScore = true;
				return false;
			}
		});
		if(isHasEmptyScore){
			alertMsg.warn("成绩不能为空且必须为0-100的数字！");
			return false; 
		}
		var $form = $("#nonfacestudy_inputExamResultsSaveForm");
		if (!$form.valid()) {
			alertMsg.error(DWZ.msg["validateFormError"]);
			return false; 
		}
		alertMsg.confirm("确认要保存成绩记录吗？", {
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
            }
        });  
	}
	
	function ajaxGetIntegratedScore(studyno,resourceid,parentId){
		var us = $("#nonfacestudy_writtenScore_"+studyno).val();
		var ws = $("#nonfacestudy_writtenScore_"+studyno).val();
		var teachingPlanCourseId = $("#nonfacestudy_inputExamResultsSearchForm_teachingPlanCourseId").val();
		var examSubId = $("#nonfacestudy_inputExamResultsSearchForm_examSubId").val();
		if(undefined ==ws ){
			ws=0;
		}
		if(undefined ==us ){
			us=0;
		}
		$.ajax({
			type:'POST',
			url:"${baseUrl}/edu3/teaching/result/facestudy/noncaculateIntegratedScore.html",
			data:{ws:ws,us:us,resourceid:resourceid,teachingPlanCourseId:teachingPlanCourseId,examSubId:examSubId,studyno:studyno,parentId:parentId},
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
						$("#nonfacestudy_writtenScore_"+studyno).val(data['highest']);
					}
			    	if(undefined!=data['highest1']){
						$("#nonfacestudy_usuallyScore_"+studyno).val(data['highest1']);
					}
			    	//if(undefined!=data['highest1']){
					//	$("#nonfacestudy_usuallyScore_"+studyno).val(data['highest1']);
					//}
			    	if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
					$("#nonfacestudy_integratedScore_"+studyno).val(data['integratedScore']);
					$("#nonfacestudy_integratedScore_"+studyno+"td").html(data['integratedScore']);
					//var dataForm = $form.serializeArray();
					if("" !=ws&&"" !=us ){
						var $form = $("#nonfacestudy_inputExamResultsSaveForm");
						$("#nonfacestudy_inputExamResultsSearchForm_only").val(studyno);
						$("#nonfacestudy_inputExamResultsSearchForm_parentOnly").val(parentId);
						//alert(parentId);
						$.ajax({
							type:'POST',
							url:$form.attr("action"),//+"?,parentId=" + parentId
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
								$("#nonfacestudy_inputExamResultsSearchForm_only").val("");
								$("#nonfacestudy_inputExamResultsSearchForm_parentOnly").val("");
							}
						});
					}
					
				}
			}
		});
		
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="nonfacestudy_inputExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/facestudy/non-input-examresults-list.html"
				method="post">
				<input id="nonfacestudy_inputExamResultsSearchForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input
					id="nonfacestudy_inputExamResultsSearchForm_teachingPlanCourseId"
					type="hidden" name="teachingPlanCourseId"
					value="${condition['teachingPlanCourseId'] }" /> <input
					id="nonfacestudy_inputExamResultsSearchForm_guidPlanId"
					type="hidden" name="guidPlanId" value="${condition['guidPlanId'] }" />
				<input id="nonfacestudy_inputExamResultsSearchForm_classesId"
					type="hidden" name="classesId" value="${condition['classesId'] }" />

				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
						<li><label>录入状态：</label> <select name="checkStatus">
								<option value="">请选择</option>
								<%-- 
						<option value="-1" <c:if test="${condition['checkStatusMakeup'] eq '-1'}"> selected="selected"</c:if>>未录入</option>
						 --%>
								<option value="0"
									<c:if test="${condition['checkStatusMakeup'] eq '0'}"> selected="selected"</c:if>>保存</option>
								<option value="1"
									<c:if test="${condition['checkStatusMakeup'] eq '1'}"> selected="selected"</c:if>>提交</option>
						</select></li>
					</ul>
					<div class="subBar">
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
		<gh:resAuth parentCode="RES_TEACHING_RESULT_FAILSTUDY_INPUT_LIST"
			pageType="subsave"></gh:resAuth>
		<div class="pageContent">
			<form id="nonfacestudy_inputExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/facestudy/noninput-examresults-save.html"
				method="post">
				<input id="nonfacestudy_inputExamResultsSearchForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input
					id="nonfacestudy_inputExamResultsSearchForm_teachingPlanCourseId"
					type="hidden" name="teachingPlanCourseId"
					value="${condition['teachingPlanCourseId'] }" /> <input
					id="nonfacestudy_inputExamResultsSearchForm_guidPlanId"
					type="hidden" name="guidPlanId" value="${condition['guidPlanId'] }" />
				<input id="nonfacestudy_inputExamResultsSearchForm_only"
					type="hidden" name="only" value="" /> <input
					id="nonfacestudy_inputExamResultsSearchForm_parentOnly"
					type="hidden" name="parentOnly" value="" />
				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<th width="4%"><input type="checkbox" name="checkall"
								id="check_all_nonfacestudy_examResults_input_save"
								onclick="checkboxAll('#check_all_nonfacestudy_examResults_input_save','resourceid','#nonfacestudy_examResultsInputBody')" /></th>
							<th width="15%">课程名称</th>
							<th width="10%">教学站</th>
							<th width="18%">专业</th>
							<th width="12%">学号</th>
							<th width="9%">姓名</th>
							<th width="9%">卷面成绩</th>
							<%--
		            <th width="7%">平时成绩</th>	
		              --%>
							<th width="9%">综合成绩</th>
							<th width="7%">成绩异常</th>
							<th width="7%">录入状态</th>
						</tr>
					</thead>
					<tbody id="nonfacestudy_examResultsInputBody">
						<c:forEach items="${page.result}" var="vo" varStatus="vs">
							<c:choose>
								<c:when test="${vo.checkStatusMakeup>0 }">
									<tr style="background-color: #e4f5ff">
										<td></td>
										<td>${course.courseName }</td>
										<td>${vo.branchSchool }</td>
										<td>${vo.major}</td>
										<td>${vo.studyNo}</td>
										<td>${vo.name}</td>
										<td>${vo.writtenScoreMakeup}</td>
										<%--
					            <td>${vo.usuallyScoreMakeup}</td>
					             --%>
										<td>${vo.integratedscoreMakeup}</td>
										<td>${ghfn:dictCode2Val('CodeExamAbnormity',vo.examabnormityMakeup) }</td>
										<td style="color: green">${ghfn:dictCode2Val('CodeExamResultCheckStatus',vo.checkStatusMakeup)}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${vo.studyNo}" autocomplete="off" /> <input
											type="hidden" name="resultsId${vo.studyNo}"
											value="${vo.resultidMakeup}" /> <input type="hidden"
											name="parentsId${vo.studyNo}"
											value="${vo.examResultsResourceId}" /> <%--
					        		<input type="hidden" name="parentsId${vo.examResultsResourceId}" value="${vo.examResultsResourceId}"/>
					        		 --%> <input type="hidden" name="parentId"
											value="${vo.examResultsResourceId}" /> <input type="hidden"
											name="parentNos" id="nonfacestudy_parentId_${vo.studyNo}"
											value="${vo.examResultsResourceId}" /></td>
										<td>${course.courseName }</td>
										<td>${vo.branchSchool }</td>
										<td>${vo.major}</td>
										<td>${vo.studyNo}</td>
										<td>${vo.name}<c:if test="${vo.studentstatus eq '16' }">
												<span style="color: red">[${ghfn:dictCode2Val('CodeStudentStatus',vo.studentstatus) }]</span>
											</c:if>
										</td>
										<td><input id="nonfacestudy_writtenScore_${vo.studyNo}"
											type="text" name="writtenScore${vo.studyNo}"
											onchange="ajaxGetIntegratedScore('${vo.studyNo}','${vo.resultidMakeup}','${vo.examResultsResourceId }')"
											align="middle" class="number" style="width: 30px"
											value="${vo.writtenScoreMakeup}" /></td>
										<%--
					            <td>
					            	<c:choose>
						            	<c:when test="${teachType eq 'netsidestudy'  or teachType eq 'networkTeach' }">
						            		<c:choose>
						            			<c:when test="${ not empty usualResults[vo.stuId] }">
						            				${usualResults[vo.stuId] }<input id="nonfacestudy_usuallyScore_${vo.studyNo}" type="hidden" name="usuallyScore${vo.studyNo}" align="middle" class="number" style="width:30px" value="${usualResults[vo.stuId] }" />
						            			</c:when>
						            			<c:otherwise>
						            				0<input id="nonfacestudy_usuallyScore_${vo.studyNo}" type="hidden" name="usuallyScore${vo.studyNo}" align="middle" class="number" style="width:30px" value="0" />
						            			</c:otherwise>
						            		</c:choose>
						            	</c:when>
						            	<c:otherwise>
						            		<input id="nonfacestudy_usuallyScore_${vo.studyNo}" type="text" name="usuallyScore${vo.studyNo}" onchange="ajaxGetIntegratedScore('${vo.studyNo}','${vo.resultidMakeup}','${vo.examResultsResourceId }')"  align="middle" class="number" style="width:30px" value="${vo.usuallyScoreMakeup}" />
						            	</c:otherwise>
					            	</c:choose>
					            	<input id="nonfacestudy_integratedScore_${vo.studyNo}" type="hidden" name="integratedScore${vo.studyNo}" align="middle" class="number" style="width:30px" value="${vo.integratedscoreMakeup}" />
					            </td>
					             --%>
										<td id="nonfacestudy_integratedScore_${vo.studyNo}td">${vo.integratedscoreMakeup}</td>
										<td><gh:select
												id="nonfacestudy_examAbnormity_${vo.studyNo}"
												name="examAbnormity${vo.studyNo}"
												dictionaryCode="CodeExamAbnormity" choose="N"
												value="${vo.examabnormityMakeup}" /></td>
										<td
											<c:if test="${vo.checkStatusMakeup > -1 }"> style="color:blue" </c:if>>
											${ghfn:dictCode2Val('CodeExamResultCheckStatus',vo.checkStatusMakeup)}
											<c:if test="${vo.checkStatusMakeup == 0 }">
												<a href="javaScript:void(0)"
													onclick="delnonfacestudyExamResults('${vo.resultidMakeup}','${vo.checkStatusMakeup}','${vo.name}','${course.courseName }')">|
													撤销</a>
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
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/facestudy/non-input-examresults-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>