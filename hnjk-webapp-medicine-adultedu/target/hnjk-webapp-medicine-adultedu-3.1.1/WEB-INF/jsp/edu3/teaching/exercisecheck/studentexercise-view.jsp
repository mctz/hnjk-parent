<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>在线作业</title>
<style>
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
</style>
</head>
<body>
	<div class="page">
		<div class="pageContent" layoutH="50">
			<h2 class="contentTitle" style="border: 0px solid #FFF;">${exerciseBatch.course.courseName}·${exerciseBatch.colName}</h2>
			<table width="100%" class="list">
				<c:set
					value="${fn:split('_,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
					var="letterList" />
				<c:forEach items="${exerciseBatch.exercises }" var="exercise"
					varStatus="vs">
					<tr>
						<td>
							<div>
								<span style="font-weight: bold;">${exercise.showOrder}.</span>&nbsp;
								${exercise.courseExam.question }&nbsp;
							</div> <c:if
								test="${exerciseBatch.colType eq '2' and not empty exercise.attachs }">
								<div>
									<span style="font-weight: bold;">附件： <c:forEach
											items="${exercise.attachs}" var="attach" varStatus="vs">
											<a onclick="downloadAttachFile('${attach.resourceid }')"
												href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
								</c:forEach></span>
								</div>
							</c:if> <input type="hidden" value="${exercise.resourceid}"
							name="exerciseId"
							<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
						</td>
					</tr>
					<tr>
						<td>
							<div id="answer${exercise.resourceid }"
								rel="${exercise.courseExam.examType}" style="height: auto;">
								<c:choose>
									<c:when test="${exerciseBatch.colType eq '1' }">
										<c:if test="${exercise.courseExam.examType ne '6' }">答题：</c:if>
										<c:choose>
											<c:when test="${exercise.courseExam.examType eq '1' }">
												<c:choose>
													<c:when
														test="${not empty exercise.courseExam.answerOptionNum }">
														<c:forEach begin="1"
															end="${exercise.courseExam.answerOptionNum }" var="idx">
															<input type="radio" value="${letterList[idx] }"
																name="answer${exercise.resourceid }"
																<c:if test="${letterList[idx] eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> ${letterList[idx] }.
													</c:forEach>
													</c:when>
													<c:otherwise>
														<input type="radio" value="A"
															name="answer${exercise.resourceid }"
															<c:if test="${'A' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
													<input type="radio" value="B"
															name="answer${exercise.resourceid }"
															<c:if test="${'B' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
													<input type="radio" value="C"
															name="answer${exercise.resourceid }"
															<c:if test="${'C' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
													<input type="radio" value="D"
															name="answer${exercise.resourceid }"
															<c:if test="${'D' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
												</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${exercise.courseExam.examType eq '2' }">
												<c:choose>
													<c:when
														test="${not empty exercise.courseExam.answerOptionNum }">
														<c:forEach begin="1"
															end="${exercise.courseExam.answerOptionNum }" var="idx">
															<input type="checkbox" value="${letterList[idx] }"
																name="answer${exercise.resourceid }"
																<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,letterList[idx]) }">checked="checked"</c:if>
																<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> ${letterList[idx] }.
												</c:forEach>
													</c:when>
													<c:otherwise>
														<input type="checkbox" value="A"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'A') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
												<input type="checkbox" value="B"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'B') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
												<input type="checkbox" value="C"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'C') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
												<input type="checkbox" value="D"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'D') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
												<span id="select_more_show_${exercise.resourceid }"
															<c:if test="${not(fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(results[exercise.resourceid].answer,'F'))}">style="display:none"</c:if>>
															<input type="checkbox" value="E"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
															E. <input type="checkbox" value="F"
															name="answer${exercise.resourceid }"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F') }">checked="checked"</c:if>
															<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
															F.
														</span>
														<span id="select_more_hide_${exercise.resourceid }"
															title="显示更多选项"
															<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F')}">style="display:none"</c:if>><a
															href="javascript:void(0)"
															onclick="exercise_exchangeoption('${exercise.resourceid }')"
															style="font-weight: bold; color: black; text-decoration: none;">
																&nbsp;&gt;&gt; </a></span>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${exercise.courseExam.examType eq '4' }">
												<input type="text"
													value="${answers[exercise.resourceid].answer }"
													name="answer${exercise.resourceid }"
													<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
												</span>
											</c:when>
											<c:when test="${exercise.courseExam.examType eq '3' }">
												<input type="radio" value="T"
													name="answer${exercise.resourceid }"
													<c:if test="${'T' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 对.
										<input type="radio" value="F"
													name="answer${exercise.resourceid }"
													<c:if test="${'F' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
													<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 错.
										</c:when>
										</c:choose>
									</c:when>
									<c:otherwise>
										<span style="font-weight: bold;">学生答案：</span>${answers[exercise.resourceid].answer }
										<c:if
											test="${not empty answers[exercise.resourceid].attachs }">
											<div>
												<span style="font-weight: bold;">学生附件：</span>
												<c:forEach items="${answers[exercise.resourceid].attachs }"
													var="attach" varStatus="vs">
													<a onclick="downloadAttachFile('${attach.resourceid }')"
														href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
										</c:forEach>
											</div>
										</c:if>
									</c:otherwise>
								</c:choose>
							</div> <c:if test="${answers['exerciseResult'].status eq 2 }">
								<div class="answer">
									<span style="font-weight: bold;">参考答案：</span>${exercise.courseExam.answer }</div>
							</c:if>
							<div style="both: clear; height: 12px"></div>
						</td>
					</tr>
				</c:forEach>
				<c:if test="${not empty studentExercise.attachs}">
					<tr>
						<td>
							<div>
								<div style="font-weight: bold;">典型批改：</div>
								<div>
									<c:forEach items="${studentExercise.attachs }" var="attach"
										varStatus="vs">
										<a onclick="downloadAttachFile('${attach.resourceid }')"
											href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
				</c:forEach>
								</div>
							</div>
						</td>
					</tr>
				</c:if>
			</table>

		</div>
		<script type="text/javascript">
//附件下载
function downloadAttachFile(attid){
		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		var elemIF = document.createElement("iframe");  
		elemIF.src = url;  
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
}
</script>
</body>
</html>