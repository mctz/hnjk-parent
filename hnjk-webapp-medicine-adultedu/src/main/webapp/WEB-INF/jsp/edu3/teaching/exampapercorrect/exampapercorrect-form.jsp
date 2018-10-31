<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>阅卷评分</title>
<style type="text/css">
.pageFormContent table.form {
	table-layout: fixed;
}

.pageFormContent div {
	line-height: 170%;
}

.pageFormContent p {
	width: 100%;
	height: auto;
}

.pageFormContent img {
	vertical-align: middle;
}

.score_mode {
	width: 100%;
}

.score_mode li {
	float: left;
	width: 20%;
	text-align: center;
}

.score_mode li a {
	width: 95%;
}

.score_mode li span {
	width: 90%;
	padding: 0 5px;
}

.fixtr td {
	height: 0;
	border: 0;
}
</style>
<script type="text/javascript">
	$(function (){
		var q = "${showOrHide1 }",a = "${showOrHide2 }";
		if(q=="hide") examToggle('exampapercorrect_question');
		if(a=="hide") examToggle('exampapercorrect_answer');
	});
	//题目显示和隐藏
	function examToggle(rid,obj){
		$("#"+rid).toggle();
		$("#"+rid+"_rel").toggle();
		var tx1 = $("#"+rid+"_span").text();
		var tx2 = $("#"+rid+"_span").attr('rel');
		$("#"+rid+"_span").attr('rel',tx1).text(tx2);
		$('#exampapercorrect_showOrHide'+('exampapercorrect_question'==rid?'1':'2')).val(tx1.indexOf('显示')>-1?'show':'hide');
	}
	//计分
	function addExamScore(scoreMode,num){
		if(scoreMode==1){//加减分模式
			var cnum = parseFloat($("#exampapercorrect_score").val())+num;
			var maxscore = parseFloat($("#exampapercorrect_maxscore").val());
			cnum = cnum >0 ? cnum :0;
			cnum = cnum<=maxscore ? cnum :maxscore;
			if(cnum>=0 && cnum<=maxscore){//必须小于题目总分数
				$("#exampapercorrect_score").val(cnum);
			}			
		} else {//累计模式
			$("#exampapercorrect_score").val(num);
		}
	}
	//批改下一份试卷
	function nextExamPaper(answerid,flag){
		$.ajax({
    		type:'POST',
    		url:$("#examPaperCorrect_edit_form").attr("action"),
    		data:$("#examPaperCorrect_edit_form").serializeArray(),
    		dataType:"json",
    		cache: false,
    		success: function (json){
    			if (json.statusCode == 200){
    				 if(answerid==""){
    					alertMsg.info('批改结束');
    					navTab.closeCurrentTab();
    				} else {
    					if(json.reloadUrl){
        					navTab.reload(json.reloadUrl,{answerid:answerid,showOrHide1:$('#exampapercorrect_showOrHide1').val(),showOrHide2:$('#exampapercorrect_showOrHide2').val()},json.navTabId);  
        				}  
    				}    				  				  				
    			} else {
    				alertMsg.error(json.message);
    			}
    		},	    		
    		error: DWZ.ajaxError
    	});
	}
	//评分模式切换
	function scoreModeOnChange(obj){
		$("#scoreMode_div_1,#scoreMode_div_2").hide();
		$("#scoreMode_div_"+$(obj).val()).show();
	}
	//
	function addExamScoreOnChange(obj){
		$("#exampapercorrect_score").val($(obj).val());
	}
	</script>
</head>
<body>
	<h2 class="contentTitle" style="margin-bottom: 5px;">
		批阅答卷 <span
			style="float: right; line-height: 30px; margin-right: 20px; margin-bottom: 5px;">
			<c:choose>
				<c:when test="${isFinishCorrect eq 'Y' }">
	全部批阅完毕, 第 ${studentExamAnswer.rn } / ${studentExamAnswer.totalCount } 份 
	</c:when>
				<c:otherwise>
	还有 ${studentExamAnswer.totalCount } 份待批阅
	</c:otherwise>
			</c:choose>

		</span>
	</h2>
	<div class="page">
		<div class="pageContent">
			<form id="examPaperCorrect_edit_form" method="post"
				action="${baseUrl}/edu3/teaching/exampapercorrect/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="examSubId"
					value="${studentExamAnswer.examsubid }" /> <input type="hidden"
					name="answerid" value="${studentExamAnswer.answerid }" /> <input
					type="hidden" name="isFinishCorrect" value="${isFinishCorrect }" />
				<input type="hidden" id="exampapercorrect_maxscore"
					value="<fmt:formatNumber value='${studentExamAnswer.score }' pattern='###.#'/>" />
				<input type="hidden" id="exampapercorrect_showOrHide1"
					name="showOrHide1" value="${showOrHide1 }" /> <input type="hidden"
					id="exampapercorrect_showOrHide2" name="showOrHide2"
					value="${showOrHide2 }" />
				<div class="pageFormContent" layoutH="97" style="padding-top: 5px;">
					<div>
						<ul>
							<li style="float: right; width: auto; margin-right: 20px;">
								<label style="width: auto;">评分模式:</label> <select
								onchange="scoreModeOnChange(this)" name="scoreMode">
									<option value="1"
										<c:if test="${scoreMode eq '1' }">selected="selected"</c:if>>加减分模式</option>
									<option value="2"
										<c:if test="${scoreMode eq '2' }">selected="selected"</c:if>>累计模式</option>
							</select>
							</li>
						</ul>
					</div>
					<table class="form">
						<tr class="fixtr">
							<td width="10%"></td>
							<td width="15%"></td>
							<td width="65%"></td>
							<td width="10%;"></td>
						</tr>
						<tr>
							<td width="10%" style="font-weight: bold;">本小题 <span
								style="font-size: 170%; font-weight: bolder; color: green;"><fmt:formatNumber
										value="${studentExamAnswer.score }" pattern="###.#" /></span> 分
							</td>
							<td width="80%" colspan="2"><div
									id="exampapercorrect_question">${studentExamAnswer.question }</div>
								<div id="exampapercorrect_question_rel" style="display: none;">题干</div></td>
							<td width="10%" style="vertical-align: top;"><a
								class="button" href="javascript:void(0)"
								onclick="examToggle('exampapercorrect_question')"><span
									id="exampapercorrect_question_span" rel="显示题干">隐藏题干</span></a></td>
						</tr>
						<tr>
							<td width="10%">参考答案</td>
							<td width="80%" colspan="2"><div
									id="exampapercorrect_answer">${studentExamAnswer.answer }</div>
								<div id="exampapercorrect_answer_rel" style="display: none;">参考答案</div></td>
							<td width="10%" style="vertical-align: top;"><a
								class="button" href="javascript:void(0)"
								onclick="examToggle('exampapercorrect_answer')"><span
									id="exampapercorrect_answer_span" rel="显示参考答案">隐藏参考答案</span></a></td>
						</tr>
						<tr>
							<td width="10%">答卷</td>
							<td width="80%" colspan="2"><div>${fn:replace(studentExamAnswer.stuanswer, lineChar, '<br/>')}
								</div></td>
							<td width="10%" style="vertical-align: top;">学生答卷字数：<span
								style="font-weight: bolder;">${fn:length(studentExamAnswer.stuanswer)}</span></td>
						</tr>
						<tr>
							<td width="10%">评分区</td>
							<td width="15%"><input type="text" name="score"
								id="exampapercorrect_score" style="width: 90%;"
								value="<fmt:formatNumber value='${studentExamAnswer.result }' pattern='###.#'/>"
								readonly="readonly" /></td>
							<td width="65%">
								<div id="scoreMode_div_1"
									<c:if test="${scoreMode eq '2' }">style="display:none;"</c:if>>
									<ul class="score_mode">
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,1)"><span>+1</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,2)"><span>+2</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,3)"><span>+3</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,4)"><span>+4</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,5)"><span>+5</span></a></li>
									</ul>
									<ul class="score_mode">
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,-1)"><span>-1</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,-2)"><span>-2</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,-3)"><span>-3</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,-4)"><span>-4</span></a></li>
										<li><a class="button" href="javascript:void(0)"
											onclick="addExamScore(1,-5)"><span>-5</span></a></li>
									</ul>
								</div>
								<div id="scoreMode_div_2"
									<c:if test="${scoreMode ne '2' }">style="display:none;"</c:if>>
									<c:choose>
										<c:when test="${studentExamAnswer.score > 10 }">
											<ul class="score_mode">
												<li><select onchange="addExamScoreOnChange(this)"
													style="width: 80%;">
														<c:forEach begin="0" end="${studentExamAnswer.score }"
															var="i">
															<option value="${i }">${i }</option>
														</c:forEach>
												</select></li>
											</ul>
										</c:when>
										<c:otherwise>
											<c:forEach begin="0" end="${studentExamAnswer.score }"
												var="i" step="5">
												<ul class="score_mode">
													<c:if test="${i<=studentExamAnswer.score }">
														<li><a class="button" href="javascript:void(0)"
															onclick="addExamScore(2,${i })"><span>${i }</span></a></li>
													</c:if>
													<c:if test="${i+1<=studentExamAnswer.score }">
														<li><a class="button" href="javascript:void(0)"
															onclick="addExamScore(2,${i+1 })"><span>${i+1 }</span></a></li>
													</c:if>
													<c:if test="${i+2<=studentExamAnswer.score }">
														<li><a class="button" href="javascript:void(0)"
															onclick="addExamScore(2,${i+2 })"><span>${i+2 }</span></a></li>
													</c:if>
													<c:if test="${i+3<=studentExamAnswer.score }">
														<li><a class="button" href="javascript:void(0)"
															onclick="addExamScore(2,${i+3 })"><span>${i+3 }</span></a></li>
													</c:if>
													<c:if test="${i+4<=studentExamAnswer.score }">
														<li><a class="button" href="javascript:void(0)"
															onclick="addExamScore(2,${i+4 })"><span>${i+4 }</span></a></li>
													</c:if>
												</ul>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</div>
							</td>
							<td width="10%">
								<%-- <a class="button" href="javascript:void(0)" onclick="nextExamPaper('${studentExamAnswer.preresourceid}',0);" ><span>改上一份</span></a> --%>
								<a class="button" href="javascript:void(0)"
								onclick="nextExamPaper('${studentExamAnswer.nextresourceid}',1);"><span>改下一份</span></a>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>