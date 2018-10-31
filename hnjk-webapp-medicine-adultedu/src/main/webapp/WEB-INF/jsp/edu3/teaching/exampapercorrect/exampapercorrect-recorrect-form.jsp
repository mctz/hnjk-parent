<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重新批阅</title>
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
	//题目显示和隐藏
	function _examToggle(rid,obj){
		$("#"+rid).toggle();
		$("#"+rid+"_rel").toggle();
		var tx1 = $("#"+rid+"_span").text();
		var tx2 = $("#"+rid+"_span").attr('rel');
		$("#"+rid+"_span").attr('rel',tx1).text(tx2);
	}
	//计分
	function _addExamScore(scoreMode,num,scoreid){
		var $score = $("#"+scoreid);
		if(scoreMode==1){//加减分模式
			var cnum = parseFloat($score.val())+num;
			var maxscore = parseFloat($score.attr('rel'));
			cnum = cnum >0 ? cnum :0;
			cnum = cnum<=maxscore ? cnum :maxscore;
			if(cnum>=0 && cnum<=maxscore){//必须小于题目总分数
				$score.val(cnum);
			}			
		} else {//累计模式
			$score.val(num);
		}
	}
	//批改下一份试卷
	function recorrectExamPaper(examResultId,answerid){
		var score = $("#exampapercorrect_score"+answerid).val();
		$.ajax({
    		type:'POST',
    		url:$("#examPaperCorrect_edit_form").attr("action"),
    		data:{examResultId:examResultId,answerid:answerid,score:score},
    		dataType:"json",
    		cache: false,
    		success: function (json){
    			if (json.statusCode == 200){
    				alertMsg.correct(json.message);   				  				  				
    			} else {
    				alertMsg.error(json.message);
    			}
    		},	    		
    		error: DWZ.ajaxError
    	});
	}
	//评分模式切换
	function _scoreModeOnChange(obj){
		$(".scoreMode_div_1,.scoreMode_div_2").hide();
		$(".scoreMode_div_"+$(obj).val()).show();
	}
	function _addExamScoreOnChange(obj,scoreid){
		$("#"+scoreid).val($(obj).val());
	}
	</script>
</head>
<body>
	<h2 class="contentTitle" style="margin-bottom: 5px;">重新批阅</h2>
	<div class="page">
		<div class="pageContent">
			<form id="examPaperCorrect_edit_form" method="post"
				action="${baseUrl}/edu3/teaching/exampapercorrect/recorrect/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="examResultId"
					value="${examResult.resourceid }" />
				<div class="pageFormContent" layoutH="97" style="padding-top: 5px;">
					<div>
						<ul>
							<li style="float: right; width: auto; margin-right: 20px;">
								<label style="width: auto;">评分模式:</label> <select
								onchange="_scoreModeOnChange(this)" name="scoreMode">
									<option value="1">加减分模式</option>
									<option value="2">累计模式</option>
							</select>
							</li>
						</ul>
					</div>
					<c:forEach items="${examList }" var="studentExamAnswer">
						<table class="form" style="padding-top: 10px;">
							<tr class="fixtr">
								<td width="10%"></td>
								<td width="15%"></td>
								<td width="65%"></td>
								<td width="10%;"></td>
							</tr>
							<tr>
								<td width="10%" style="font-weight: bold;">本小题 <fmt:formatNumber
										value="${studentExamAnswer.courseExamPaperDetails.score }"
										pattern="###.#" /> 分
								</td>
								<td width="80%" colspan="2"><div
										id="exampapercorrect_question${studentExamAnswer.resourceid}">${studentExamAnswer.courseExamPaperDetails.courseExam.question }</div>
									<div
										id="exampapercorrect_question${studentExamAnswer.resourceid}_rel"
										style="display: none;">题干</div></td>
								<td width="10%" style="vertical-align: top;"><a
									class="button" href="javascript:void(0)"
									onclick="_examToggle('exampapercorrect_question${studentExamAnswer.resourceid}')"><span
										id="exampapercorrect_question${studentExamAnswer.resourceid}_span"
										rel="显示题干">隐藏题干</span></a></td>
							</tr>
							<tr>
								<td width="10%">参考答案</td>
								<td width="80%" colspan="2"><div
										id="exampapercorrect_answer${studentExamAnswer.resourceid}">${studentExamAnswer.courseExamPaperDetails.courseExam.answer }</div>
									<div
										id="exampapercorrect_answer${studentExamAnswer.resourceid}_rel"
										style="display: none;">参考答案</div></td>
								<td width="10%" style="vertical-align: top;"><a
									class="button" href="javascript:void(0)"
									onclick="_examToggle('exampapercorrect_answer${studentExamAnswer.resourceid}')"><span
										id="exampapercorrect_answer${studentExamAnswer.resourceid}_span"
										rel="显示参考答案">隐藏参考答案</span></a></td>
							</tr>
							<tr>
								<td width="10%">答卷</td>
								<td width="80%" colspan="2"><div>${fn:replace(studentExamAnswer.answer, lineChar, '<br/>')}
									</div></td>
								<td width="10%" style="vertical-align: top;">学生答卷字数：${fn:length(studentExamAnswer.answer)}</td>
							</tr>
							<tr>
								<td width="10%">评分区</td>
								<td width="15%"><input type="hidden"
									id="answerid${studentExamAnswer.resourceid}" name="answerid"
									value="${studentExamAnswer.resourceid}" /> <input type="text"
									name="score"
									id="exampapercorrect_score${studentExamAnswer.resourceid}"
									style="width: 90%;"
									value="<fmt:formatNumber value='${not empty studentExamAnswer.result ? studentExamAnswer.result : 0 }' pattern='###.#'/>"
									rel="<fmt:formatNumber value='${studentExamAnswer.courseExamPaperDetails.score }' pattern='###.#'/>"
									readonly="readonly" /></td>
								<td width="65%">
									<div class="scoreMode_div_1"
										<c:if test="${scoreMode eq '2' }">style="display:none;"</c:if>>
										<ul class="score_mode">
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,1,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>+1</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,2,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>+2</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,3,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>+3</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,4,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>+4</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,5,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>+5</span></a></li>
										</ul>
										<ul class="score_mode">
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,-1,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>-1</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,-2,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>-2</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,-3,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>-3</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,-4,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>-4</span></a></li>
											<li><a class="button" href="javascript:void(0)"
												onclick="_addExamScore(1,-5,'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>-5</span></a></li>
										</ul>
									</div>
									<div class="scoreMode_div_2"
										<c:if test="${scoreMode ne '2' }">style="display:none;"</c:if>>
										<c:set
											value="${studentExamAnswer.courseExamPaperDetails.score }"
											var="mscore" />
										<c:choose>
											<c:when test="${mscore > 10 }">
												<ul class="score_mode">
													<li><select
														onchange="_addExamScoreOnChange(this,'exampapercorrect_score${studentExamAnswer.resourceid}')"
														style="width: 80%;">
															<c:forEach begin="0" end="${mscore }" var="i">
																<option value="${i }">${i }</option>
															</c:forEach>
													</select></li>
												</ul>
											</c:when>
											<c:otherwise>
												<c:forEach begin="0" end="${mscore }" var="i" step="5">
													<ul class="score_mode">
														<c:if test="${i<=mscore }">
															<li><a class="button" href="javascript:void(0)"
																onclick="_addExamScore(2,${i },'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>${i }</span></a></li>
														</c:if>
														<c:if test="${i+1<=mscore }">
															<li><a class="button" href="javascript:void(0)"
																onclick="_addExamScore(2,${i+1 },'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>${i+1 }</span></a></li>
														</c:if>
														<c:if test="${i+2<=mscore }">
															<li><a class="button" href="javascript:void(0)"
																onclick="_addExamScore(2,${i+2 },'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>${i+2 }</span></a></li>
														</c:if>
														<c:if test="${i+3<=mscore }">
															<li><a class="button" href="javascript:void(0)"
																onclick="_addExamScore(2,${i+3 },'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>${i+3 }</span></a></li>
														</c:if>
														<c:if test="${i+4<=mscore }">
															<li><a class="button" href="javascript:void(0)"
																onclick="_addExamScore(2,${i+4 },'exampapercorrect_score${studentExamAnswer.resourceid}')"><span>${i+4 }</span></a></li>
														</c:if>
													</ul>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
								<td width="10%"><a class="button" href="javascript:void(0)"
									onclick="recorrectExamPaper('${examResult.resourceid }','${studentExamAnswer.resourceid}');"><span>批改</span></a></td>
							</tr>
						</table>
					</c:forEach>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>