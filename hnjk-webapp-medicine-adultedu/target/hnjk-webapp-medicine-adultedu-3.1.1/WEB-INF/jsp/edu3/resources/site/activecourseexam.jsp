<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">	
	//显示参考答案
	function showActiveExamAnswer(){
		if($(".correctAnswers").length){
			$(".correctAnswers").toggle();
		}
	}
	//多选题显示更多选项，兼容旧版的随堂练习多选题，新版的有指定选项数
	function exchangeoption(resourceid){
		$("#select_more_show_"+resourceid).show();
		$("#select_more_hide_"+resourceid).hide();
	}
	//提交答案
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
		} else if(type=='test'){//匿名用户测试
			var _num = _notAnswerExamNum();
			if(_num>0){
				alert("你还有"+_num+"道题未做.");
				return false;
			}
			msg += "您确定要测试本页的练习吗？";
		} else {
			msg += "您确定现在就要提交本页的练习吗？";
		}
		if(window.confirm(msg)){
			$(obj).attr("disabled",true);
			$.ajax({
				type:'POST',
				url:$('#StuActiveCourseExamForm').attr("action")+"?type="+type,
				data:$('#StuActiveCourseExamForm').serializeArray(),
				dataType:"json",
				cache: false,
				success: function (json){
					if(json.message){
						alert(json.message);
					}
					if (json.statusCode && json.statusCode == 200){						
						if(type!='test'){
							goPage('${resPage.pageNum}');
						}						
					} else {
						$(obj).attr("disabled",false);
					}
					if(type=='submit_done' || type=='submit'){
						$.post(baseUrl+"/resource/course/learningtrace/log.html?resType=2&syllabusid="+$("#StuActiveCourseExamForm input[name='syllabusId']").val());
					}
				}
			});
		}		
	}
	//未做的题目数
	function _notAnswerExamNum(){
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
			if($.trim(answer)==""){
				num++;
			}
		});
		return num;
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
	font-size: 10pt;
	font-family: Arial, sans-serif;
}

.list p {
	line-height: 170%;
	width: 100%;
	font-family: Arial, sans-serif;
}

.list img {
	vertical-align: middle;
}

.activeanswer {
	padding-top: 5px;
}
-->
</style>
<c:if
	test="${(not empty resPage.result) or (fn:length(resPage.result)>0)}">
	<form id="StuActiveCourseExamForm" method="post"
		action="${baseUrl}/resource/course/studentactivecourseexam/save.html"
		onsubmit="return false;">
		<input type="hidden" value="${syllabus.resourceid}" name="syllabusId" />
		<c:if test="${isStudent eq 'Y'}">
			<div style="color: blue; line-height: 170%; text-align: center;">
				<span>本次练习有${results.x1 }题，你已做${results.x2 }题，已提交${results.x3 }题，其中答对${results.x4 }题。</span>
				<c:if test="${not empty results.y1 }">
					<br />
					<span>当前页有${results.y1 }题，你已做${results.y2 }题，已提交${results.y3 }题，其中答对${results.y4 }题。</span>
				</c:if>
			</div>
		</c:if>
		<table width="100%" class="list">
			<c:set
				value="${fn:split('_,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
				var="letterList" />
			<c:forEach items="${resPage.result }" var="exam" varStatus="vs">
				<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
					<td>
						<div>
							<c:if test="${exam.courseExam.examType ne '6' }">
								<span style="font-weight: bold;">${exam.showOrder}.</span>&nbsp;</c:if>
							${exam.courseExam.question }&nbsp;
						</div> <c:if test="${exam.courseExam.examType ne '6' }">
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
							<%-- 只有登录的学生用户可以查看答案 --%>
							<c:if test="${results.y3 ge results.y1 and isStudent eq 'Y'}">
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
		<table width="100%" class="list">
			<c:if
				test="${results.y3 lt results.y1 and isActiveCourseExamOpen eq 'Y' and isStudent eq 'Y'}">
				<tr>
					<td width="100%">
						<div>
							<span style="color: #63A7E6; font-weight: bold;">
								*提示：点击【保存我的答案】按钮,保存本页已答练习答案。<br />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 点击【提交当前页】按钮，提交当前页随堂练习。<br />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								点击【提交已做的题目】按钮，提交本练习所有已做随堂练习,题目不再允许修改。<br />
							</span>
						</div>
					</td>
				</tr>
			</c:if>
			<tr>
				<td width="100%">
					<div
						style="float: right; margin-left: 10px; margin-right: 50px; line-height: 100%;">
						<c:choose>
							<%-- 只有登录的学生可以保存提交答案 --%>
							<c:when test="${(results.x1 gt 0) and isStudent eq 'Y'}">
								<c:if test="${results.y1 gt 0 }">
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
												</c:when>
												<c:otherwise>
													<a class="button" href="javascript:;"
														onclick="showActiveExamAnswer()"><span>参考答案</span></a>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:when>
							<%-- 匿名用户 或 非学生用户 --%>
							<c:otherwise>
								<a class="button" href="javascript:void(0);"
									id="ActiveExamTestButton"
									onclick="_activeCourseExamSubmit(this,'test')"><span>
										测 试 </span></a>
							</c:otherwise>
						</c:choose>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<gh:page page="${resPage}" condition="${condition }"
		pageType="resource" />
</c:if>