<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/mulitpage.css" />
<script type="text/javascript">	
	function _activeCourseExamValidateCallback(form){
		alertMsg.confirm("您确定要提交吗？", {
			okCall: function(){//执行	
				$("#ActiveExamSubmitButton").attr("disabled","disabled");
				return validateCallback(form,getResult);
			}
		});	
		return false;
	}
	
	function showActiveExamAnswer(){
		if($(".correctAnswers")){
			$(".correctAnswers").toggle();
		}
	}
	function exchangeoption(resourceid){
		$("#select_more_show_"+resourceid).show();
		$("#select_more_hide_"+resourceid).hide();
	}
	function _activeCourseExamSubmit(obj,type){
		var msg = "";
		if(type=='save'){
			msg += "您确定要保存吗？";
		} else if(type=='submit_done'){
			msg += "你将要提交所有已做的题目，提交后的题目不再允许修改，请确实是否要提交？";
		} else if(type=='submit_all'){			
			msg += "你将要提交本练习所有的题目，提交后本练习所有题目不再允许修改。";
			var notDone = ${results.x1-results.x2 };
			if(notDone>0){
				msg += "你还有"+notDone+"题尚未做，";		
			}
			msg += "请确认是否要提交？";			
		} else {
			msg += "您确定现在就要提交本页的练习吗？";
		}
		alertMsg.confirm(msg, {
			okCall: function(){//执行	
				$(obj).attr("disabled",true);
				$.ajax({
					type:'POST',
					url:$('#StuActiveCourseExamForm').attr("action")+"?type="+type,
					data:$('#StuActiveCourseExamForm').serializeArray(),
					dataType:"json",
					cache: false,
					success: function (json){
						DWZ.ajaxDone(json);
						if (json.statusCode == 200){					
							//$("#interactive_tab3").click();
							var pageNum = ${activeCourseExams.pageNum};
							localAreaPageBreak("interactive_tab3Content",{pageNum:pageNum});
						} else {
							$(obj).attr("disabled",false);
						}
					},
					
					error: DWZ.ajaxError
				});
			}
		});	
	}
	function _activelocalAreaPageBreak(scopeType){
		var form = $("#interactive_tab3Content #pagerForm").get(0);
		if (form) {
			form.scopeType.value = scopeType;	
		}
		localAreaPageBreak("interactive_tab3Content");
	}
	function _isAutoSaveChange(obj){
		var isAutoSave = "N";
		if($(obj).attr("checked")){
			var isAutoSave = "Y";
		}
		var form = $("#interactive_tab3Content #pagerForm").get(0);
		if (form) {
			form.isAutoSave.value = isAutoSave;	
		}
	}
	function _showStudentActiveCourseExam(syllabusId,title){
		var url = baseUrl+"/edu3/learning/interactive/studentactivecourseexam/view.html?syllabusId="+syllabusId;
		$.pdialog.open(url, "viewStudentActiveCourseExam"+syllabusId, title+" 随堂练习答题情况", {width:800,height:500});
	}
	function goPage(pageNo){
		if($("#activeCourseExam_isAutoSave").size()>0){
			if($("#activeCourseExam_isAutoSave").attr("checked")){
				if(answerChangeNum()>0){//答题变更
					$.ajax({
						type:'POST',
						url:$('#StuActiveCourseExamForm').attr("action")+"?type=save",
						data:$('#StuActiveCourseExamForm').serializeArray(),
						dataType:"json",
						cache: false,
						success: function (json){
							if (json.statusCode == 200){					
								localAreaPageBreak("interactive_tab3Content",{pageNum:pageNo});
							}
						}
					});			
				} else {
					localAreaPageBreak("interactive_tab3Content",{pageNum:pageNo});
				}		
			} else {
				var examNum = answerChangeNum();
				if(examNum>0){
					alertMsg.confirm("当前页有"+examNum+"题你尚未保存答案，请先保存答案再翻页。是否要继续翻页？", {
						okCall: function(){	
							localAreaPageBreak("interactive_tab3Content",{pageNum:pageNo});
						}
					});	
				} else {
					localAreaPageBreak("interactive_tab3Content",{pageNum:pageNo});
				}
			}
		} else {
			localAreaPageBreak("interactive_tab3Content",{pageNum:pageNo});
		} 
	}
	//答题而未保存的题目数
	function answerChangeNum(){
		var num = 0;
		$("input[id^='answer']:enabled").each(function(){
			var answer = "";
			if($(this).attr('rel')=='4'){
				answer = $("input[name='"+this.id+"']").val();
			} else {
				$("input[name='"+this.id+"']:checked").each(function(){
					answer += $(this).val();
				});	
			}	
			if(answer!=$(this).val()){
				num++;
			}
		});
		return num;
	}
	//重新做题
	function currentSyllabusRedo(studentSyllabusid,syllabusid,redotimes){
		var url=baseUrl+"/edu/learning/interactive/activecourseexam/currentSyllabusRedo.html"
		alertMsg.confirm("您当前还有【"+redotimes+"】次机会重做，重做将清空本章的之前的作答，允许重新作答，使用后，您将还剩下【"+(redotimes-1)+"】次重做机会，您确定要使用一次重做机会吗？", {
			okCall: function(){//执行	
				$.ajax({
					type:"POST",
					dataType:"json",
					url:url,
					data:{studentSyllabusid:studentSyllabusid,syllabusid:syllabusid,redotimes:redotimes},
					success:function(json){
						DWZ.ajaxDone(json);
						if (json.statusCode == 200){					
							localAreaPageBreak("interactive_tab3Content",{pageNum:1});
						} else {
							//$(obj).attr("disabled",false);
						}
					},
					error: DWZ.ajaxError
				})
			}
		});	
		return false;
	}
	//放弃重做的机会
	function giveUpRedoTimes(redotimes,studentSyllabusid){		
		var url=baseUrl+"/edu/learning/interactive/activecourseexam/giveUpRedoTimes.html"
		alertMsg.confirm("您当前还有【"+redotimes+"】次机会重做，放弃为重做则重做机会为【0】，但能查看本章习题的答案，您确定放弃重做的机会吗？", {
			okCall: function(){//执行	
				$.ajax({
					type:'POST',
					dataType:"json",
					url:url,
					data:{studentSyllabusid:studentSyllabusid},
					success:function(json){
						DWZ.ajaxDone(json);
						if (json.statusCode == 200){					
							//$("#interactive_tab3").click();
							
							localAreaPageBreak("interactive_tab3Content",{pageNum:1});
						} else {
							//$(obj).attr("disabled",false);
						}
					},
					error: DWZ.ajaxError
				})
			}
		});	
		return false;
	}
</script>
<style>
<!--
.list {
	table-layout: fixed;
	word-wrap: break-word
}

.list td div {
	line-height: 170%;
	font-size: 10pt
}

.list p {
	line-height: 170%;
	font-size: 10pt;
	width: 100%
}
-->
</style>
<c:if
	test="${(not empty activeCourseExams.result) or (not empty results.x1)}">
	<form id="StuActiveCourseExamForm" method="post"
		action="${baseUrl}/edu3/learning/interactive/studentactivecourseexam/save.html"
		onsubmit="return false;">
		<input type="hidden" value="${syllabus.resourceid}" name="syllabusId" />
		<input type="hidden" value="${teachType}" name="teachType" />
		<h2 class="contentTitle">${title}</h2>
		<c:if test="${isStudent eq 'Y'}">
			<div style="color: blue; line-height: 170%; text-align: center;">
				<span>本次练习有${results.x1 }题，你已做${results.x2 }题，已提交${results.x3 }题，其中答对${results.x4 }题。</span>
				<c:if test="${not empty results.y1 }">
					<br />
					<span>当前页有${results.y1 }题，你已做${results.y2 }题，已提交${results.y3 }题，其中答对${results.y4 }题。</span>
				</c:if>
			</div>
<%-- 			<c:if test="${results.y3 lt results.y1}"> --%>
				<div>
					<span style="color: green; font-weight: bolder;">
						*提示：<br />
						1、点击底部【保存我的答案】按钮,保存本页已答练习答案。<br/>
						2、点击【提交当前页】按钮，提交当前页随堂练习，已提交的题目不再允许修改。<br />
						3、点击【提交已做的题目】按钮，提交本练习所有已做随堂练习。<br />
						<c:if test="${ sumRedoTimes gt 0}">
						4、如您对做题结果不满意，当本章（或节）全部提交后，可允许重新做题【${sumRedoTimes }】次，重做则清空本章所有题目的作答，当前还有【${redoTimes}】 次重做机会。<br />
						5、如您没有全做对本章题目，但已满意，并且对于本章题目不再作答，想看正确答案，可点击【放弃重做】，则重做机会为0，放弃重做即本章题目不再做改动<br/>
						6、当重做机会为0时，可点击右下角【参考答案】查看本章（或节）全部题目的正确答案<br/>
						</c:if>
					</span>
				</div>
<%-- 			</c:if> --%>
<%-- 			<c:if test="${results.x3 eq results.x1 and redoTimes gt 0 and results.x4 lt results.x1}"> --%>
<!-- 				<div> -->
<!-- 					<span style="color: green; font-weight: bolder;"> -->
<!-- 						*提示：<br /> -->
<%-- 						1、本章（或节）已全部提交，当前还有【${redoTimes}】 次重做机会。<br /> --%>
<!-- 						2、点击右下角【本章重做】，则清空本章题目答案，可重新做题<br/> -->
<!-- 						3、点击【放弃重做】，则重做机会为0；当重做机会为0时，可点击右下角【参考答案】查看本章（或节）全部题目的正确答案<br/> -->
<!-- 					</span> -->
<!-- 				</div> -->
<%-- 			</c:if> --%>
<%-- 			<c:if test="${redoTimes eq 0 }"> --%>
<!-- 				<div> -->
<!-- 					<span style="color: green; font-weight: bolder;"> -->
<!-- 						*提示：<br /> -->
<%-- 						1、本章（或节）还有【${redoTimes}】 次重做机会。<br />						 --%>
<!-- 					</span> -->
<!-- 				</div> -->
<%-- 			</c:if> --%>
		</c:if>
		<table width="100%" class="list">
			<c:set
				value="${fn:split('_,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
				var="letterList" />
			<c:forEach items="${activeCourseExams.result }" var="exam"
				varStatus="vs">
				<tr>
					<td>
						<div>
							<c:if test="${exam.courseExam.examType ne '6' }">
								<span style="font-weight: bold;">${exam.showOrder}.</span>&nbsp;</c:if>
							<font color="red">(${ghfn:dictCode2Val("CodeExamType",exam.courseExam.examType) })</font>${exam.courseExam.question }&nbsp;</div>
						<c:if test="${exam.courseExam.examType ne '6' }">
							<input type="hidden" value="${exam.resourceid}"
								name="activeCourseExamId"
								<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if> />
							<div class="activeanswer">
								答题： <input type="hidden"
									value="${answers[exam.resourceid].answer }"
									id="answer${exam.resourceid }"
									rel="${exam.courseExam.examType }"
									<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if> />
								<c:choose>
									<c:when test="${exam.courseExam.examType eq '1' }">
										<c:choose>
											<c:when test="${not empty exam.courseExam.answerOptionNum }">
												<c:forEach begin="1"
													end="${exam.courseExam.answerOptionNum }" var="idx">
													<input type="radio" value="${letterList[idx] }"
														name="answer${exam.resourceid }"
														<c:if test="${letterList[idx] eq answers[exam.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> ${letterList[idx] }.
									</c:forEach>
											</c:when>
											<c:otherwise>
												<input type="radio" value="A"
													name="answer${exam.resourceid }"
													<c:if test="${'A' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> A.
									<input type="radio" value="B" name="answer${exam.resourceid }"
													<c:if test="${'B' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> B.
									<input type="radio" value="C" name="answer${exam.resourceid }"
													<c:if test="${'C' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> C.
									<input type="radio" value="D" name="answer${exam.resourceid }"
													<c:if test="${'D' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> D.
								</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${exam.courseExam.examType eq '2' }">
										<c:choose>
											<c:when test="${not empty exam.courseExam.answerOptionNum }">
												<c:forEach begin="1"
													end="${exam.courseExam.answerOptionNum }" var="idx">
													<input type="checkbox" value="${letterList[idx] }"
														name="answer${exam.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,letterList[idx]) }">checked="checked"</c:if>
														<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> ${letterList[idx] }.
									</c:forEach>
											</c:when>
											<c:otherwise>
												<input type="checkbox" value="A"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'A') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> A.
									<input type="checkbox" value="B"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'B') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> B.
									<input type="checkbox" value="C"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'C') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> C.
									<input type="checkbox" value="D"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'D') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> D.
									<span id="select_more_show_${exam.resourceid }"
													<c:if test="${not(fn:containsIgnoreCase(answers[exam.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exam.resourceid].answer,'F'))}">style="display:none"</c:if>>
													<input type="checkbox" value="E"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'E') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>>
													E. <input type="checkbox" value="F"
													name="answer${exam.resourceid }"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'F') }">checked="checked"</c:if>
													<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>>
													F.
												</span>
												<span id="select_more_hide_${exam.resourceid }"
													title="显示更多选项"
													<c:if test="${fn:containsIgnoreCase(answers[exam.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exam.resourceid].answer,'F')}">style="display:none"</c:if>><a
													href="javascript:void(0)"
													onclick="exchangeoption('${exam.resourceid }')"
													style="font-weight: bold; color: black; text-decoration: none;">
														&nbsp;&gt;&gt; </a></span>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${exam.courseExam.examType eq '3' }">
										<input type="radio" value="T" name="answer${exam.resourceid }"
											<c:if test="${'T' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
											<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> 对.
						<input type="radio" value="F" name="answer${exam.resourceid }"
											<c:if test="${'F' eq answers[exam.resourceid].answer }">checked="checked"</c:if>
											<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if>> 错.
						</c:when>
									<c:otherwise>
										<input type="text" value="${answers[exam.resourceid].answer }"
											name="answer${exam.resourceid }"
											<c:if test="${not empty answers[exam.resourceid].result }">disabled="disabled"</c:if> />
									</c:otherwise>
								</c:choose>
								<c:if test="${not empty answers[exam.resourceid].result }">
									<span style="color: red;">（已提交）</span>
								</c:if>
							</div>
							<c:if test="${(results.y3 ge results.y1) and isStudent eq 'Y' and isActiveCourseExamOpen eq 'Y' and hasChance eq 'N'}">
								<div class="correctAnswers" style="display: none;">
									<div style="color: green;">参考答案：${exam.courseExam.answer }</div>
									<div style="color: green;">问题解析：${exam.courseExam.parser }</div>
								</div>
							</c:if>
						</c:if>
						<div style="both: clear; height: 12px"></div>
					</td>
				</tr>
			</c:forEach>
		</table>
		<c:if test="${(results.x1 gt 0) and isStudent eq 'Y'}">
			<table width="100%" style="background-color: #EBF0F5;">
				<tr>
					<td colspan="2" style="color: green;">&nbsp;</td>
				</tr>
				<tr>
					<td width="30%">
						<div style="margin-left: 5px;">
							请选择查看范围： <select name="scopeType"
								onchange="_activelocalAreaPageBreak(this.value)">
								<option value="all">所有题目</option>
								<option value="unsave"
									<c:if test="${condition['scopeType'] eq 'unsave' }">selected="selected"</c:if>>未做题目</option>
								<option value="unfinished"
									<c:if test="${condition['scopeType'] eq 'unfinished' }">selected="selected"</c:if>>未提交题目</option>
							</select> <br /> <input type="checkbox" id="activeCourseExam_isAutoSave"
								onchange="_isAutoSaveChange(this)" name="isAutoSave" value="Y"
								<c:if test="${condition['isAutoSave'] eq 'Y' }">checked="checked"</c:if> />翻页时自动保存答案
						</div>
					</td>
					<td>
						<div style="float: right; margin-left: 10px; margin-right: 50px;">
							<c:if test="${results.y1 gt 0}">
								<c:choose>
									<c:when test="${isActiveCourseExamOpen ne 'Y' }">
										<span style="float: left; font-weight: bold; color: blue;">${msg }</span>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when
												test="${isActiveCourseExamOpen eq 'Y' and results.y3 lt results.y1 }">
												<a class="button" href="javascript:void(0);"
													id="ActiveExamSaveButton"
													onclick="_activeCourseExamSubmit(this,'save')"><span>保存我的答案</span></a>
												<a class="button" href="javascript:void(0);"
													id="ActiveExamSubmitButton"
													onclick="_activeCourseExamSubmit(this,'submit')"><span>提交当前页</span></a>
												<a class="button" href="javascript:void(0);"
													id="ActiveExamSubmitButton1"
													onclick="_activeCourseExamSubmit(this,'submit_done')"><span>提交已做的题目</span></a>
												<!-- <a class="button" href="javascript:void(0);" id="ActiveExamSubmitButton2" onclick="_activeCourseExamSubmit(this,'submit_all')"><span>提交本练习所有题目</span></a> -->
											</c:when>
											<c:when
												test="${isActiveCourseExamOpen eq 'Y' and results.x1 eq results.x3 and (redoTimes eq 0 or results.x1 eq results.x4)}">
												<a class="button" href="javascript:;"
													onclick="showActiveExamAnswer()"><span>参考答案</span></a>
											</c:when>
											<c:when
												test="${isActiveCourseExamOpen eq 'Y' and redoTimes ne 0 and results.x1 eq results.x3 and results.x1 ne results.x4}">
												<c:if test="${results.x1 gt results.x4 }">
												<a class="button" href="javascript:;"
													onclick="currentSyllabusRedo('${studentSyllabus.resourceid }','${syllabus.resourceid}','${redoTimes}')"><span>本章重做【${redoTimes}】</span></a>
												</c:if>
												<a class="button" href="javascript:;"
													onclick="giveUpRedoTimes('${redoTimes}','${studentSyllabus.resourceid }')"><span>放弃重做</span></a>
											</c:when>
												
										</c:choose>
										<a class="button" href="javascript:void(0);"
											onclick="_showStudentActiveCourseExam('${syllabus.resourceid}','${title}')"><span>查看答题详情</span></a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
			</table>
		</c:if>
	</form>
	<gh:page page="${activeCourseExams}"
		goPageUrl="${baseUrl}/edu3/learning/interactive/activecourseexam/list.html"
		localArea="interactive_tab3Content" targetType="localArea"
		condition="${condition }" pageType="other" />
</c:if>