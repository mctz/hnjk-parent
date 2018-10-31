<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业展示</title>
<gh:loadCom components="editor,fileupload" />
<style>
.nostyle td {
	height: 4px
}

.list {
	table-layout: fixed;
	word-wrap: break-word
}

.list td div {
	line-height: 170%;
	font-size: 10pt;
	font-family: Arial, sans-serif;
	height: auto;
}

.list p {
	line-height: 170%;
	font-size: 10pt;
	width: 100%;
	height: auto;
	font-family: Arial, sans-serif;
}

.list li {
	list-style-type: none;
}

.list img {
	vertical-align: middle;
}
</style>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyExercise"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/exerciseshow.html?courseId=${course.resourceid}">作业展示</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > 作业展示 >
				${exerciseBatch.colName }(${studentExerciseResult.studentInfo.studentName })
			</div>
			<div class="clear"></div>
			<div id="content">
				<table width="100%" class="list">
					<c:forEach items="${exerciseBatch.exercises }" var="exercise"
						varStatus="vs">
						<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
							<td width="100%">
								<div>
									<span style="font-weight: bold;">${exercise.showOrder}.</span>&nbsp;
									${exercise.courseExam.question }&nbsp;
								</div> <c:if
									test="${exerciseBatch.colType eq '2' and not empty exercise.attachs }">
									<div>
										<span style="font-weight: bold;">作业附件： <c:forEach
												items="${exercise.attachs}" var="attach" varStatus="vs">
												<a onclick="downloadAttachFile('${attach.resourceid }')"
													href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
								</c:forEach></span>
									</div>
								</c:if> <input type="hidden" value="${exercise.resourceid}"
								name="exerciseId"
								<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
								<div id="answer${exercise.resourceid }"
									rel="${exercise.courseExam.examType}">
									<c:choose>
										<c:when test="${exerciseBatch.colType eq '1' }">
											<span style="font-weight: bold;">学生答案：</span>
											<c:choose>
												<c:when test="${exercise.courseExam.examType eq '1' }">
													<input type="radio" value="A"
														name="answer${exercise.resourceid }"
														<c:if test="${'A' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
										<input type="radio" value="B"
														name="answer${exercise.resourceid }"
														<c:if test="${'B' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
										<input type="radio" value="C"
														name="answer${exercise.resourceid }"
														<c:if test="${'C' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
										<input type="radio" value="D"
														name="answer${exercise.resourceid }"
														<c:if test="${'D' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
										</c:when>
												<c:when test="${exercise.courseExam.examType eq '2' }">
													<input type="checkbox" value="A"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'A') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
										<input type="checkbox" value="B"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'B') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
										<input type="checkbox" value="C"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'C') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
										<input type="checkbox" value="D"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'D') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
										<span id="select_more_show_${exercise.resourceid }"
														<c:if test="${not(fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(results[exercise.resourceid].answer,'F'))}">style="display:none"</c:if>>
														<input type="checkbox" value="E"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
														E. <input type="checkbox" value="F"
														name="answer${exercise.resourceid }"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F') }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
														F.
													</span>
													<span id="select_more_hide_${exercise.resourceid }"
														title="显示更多选项"
														<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F')}">style="display:none"</c:if>><a
														href="javascript:void(0)"
														onclick="exercise_exchangeoption('${exercise.resourceid }')"
														style="font-weight: bold; color: black; text-decoration: none;">
															&nbsp;&gt;&gt; </a></span>
												</c:when>
												<c:when test="${exercise.courseExam.examType eq '4' }">
													<input type="text"
														value="${answers[exercise.resourceid].answer }"
														name="answer${exercise.resourceid }"
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
													</span>
												</c:when>
												<c:otherwise>
													<input type="radio" value="T"
														name="answer${exercise.resourceid }"
														<c:if test="${'T' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 对.
										<input type="radio" value="F"
														name="answer${exercise.resourceid }"
														<c:if test="${'F' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
														<c:if test="${not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 错.
										</c:otherwise>
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
				</table>
				<script type="text/javascript">	
				function downloadAttachFile(attid){
					var url = baseUrl+"/edu3/framework/filemanage/${empty user ? 'public/':''}download.html?id="+attid;
					var elemIF = document.createElement("iframe");  
					elemIF.src = url;  
					elemIF.style.display = "none";  
					document.body.appendChild(elemIF); 
				}
			</script>
			</div>
		</div>
		<!--end right-->
	</div>
</body>
</html>