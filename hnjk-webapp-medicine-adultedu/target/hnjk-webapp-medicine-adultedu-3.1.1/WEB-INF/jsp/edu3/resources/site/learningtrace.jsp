<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>学习笔记</title>
<style type="text/css">
</style>
</head>
<body>
	<div style="padding: 5px;" class="leaningTraceArea">
		<div id="learningTabbedPanels" class="TabbedPanels">
			<ul class="TabbedPanelsTabGroup">
				<li class="TabbedPanelsTab" tabindex="0">学习统计</li>
				<li class="TabbedPanelsTab" tabindex="0">学习记录</li>
				<li class="TabbedPanelsTab" tabindex="0">学习建议</li>
			</ul>

			<div class="TabbedPanelsContentGroup font_12">
				<!-- tab1 -->
				<div class="TabbedPanelsContent">
					<div class="bottom_line1">
						<%-- <p><span class="font_12 cDGray">登录次数</span>：<span class="cRed">5</span> 次</p>  --%>
						<p>
							学习进度，已经学习了<span class="cRed">${hasStudyResource }</span> 个内容,
							全课程共有<span class="cRed">${totalResource }</span>个内容。
						</p>
						<p>
							累计平时分共<span class="cRed"><fmt:formatNumber
									value="${usualResults }" pattern="###" /></span>分。
						</p>
						<p>
							——已提交随堂练习<span class="cRed">${totalFinishedExamCount }</span>题，全课程共有<span
								class="cRed">${totalExamCount }</span>题；
						</p>
						<p>
							——已提交作业<span class="cRed">${totalFinishedExerciseCount }</span>次，全课程共有<span
								class="cRed">${totalExerciseCount }</span>次；
						</p>
						<p>
							——论坛发贴<span class="cRed">${topicCountMap.topicCount }</span>次，回帖<span
								class="cRed">${topicCountMap.replyCount }</span>次，精华帖<span
								class="cRed">${topicCountMap.bestCount }</span>次。
						</p>
					</div>
				</div>
				<!-- tab2 -->
				<div class="TabbedPanelsContent">
					<div align="center"></div>
					<table width="98%" border="1" cellpadding="0" cellspacing="0"
						class="list">
						<thead>
							<tr>
								<th width="25%"><strong>已经学习课程内容 </strong></th>
								<th width="20%"><strong>讲义 </strong></th>
								<th width="20%"><strong>授课 </strong></th>
								<th width="20%"><strong>习题 </strong></th>
								<th width="15%"><strong>在线笔记 </strong></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${syllabusVoList }" var="vo" varStatus="vs">
								<c:if test="${vo.hasResource eq 'Y' }">
									<tr>
										<td>${vo.syllabusName }</td>
										<td align="center"><fmt:formatDate
												value="${vo.learHandoutTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td align="center"><fmt:formatDate
												value="${vo.learMeteTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td align="center">共有<span class="cRed">${vo.examTotalCount }</span>题；<br />
											已做<span class="cRed">${vo.examDoneCount }</span>题；<br /> 正确<span
											class="cRed">${vo.examCorrectCount }</span>题。
										</td>
										<td align="center"><a href="javascript:void(0)"
											onclick="goCourseLearningNote('${vo.resourceid}');">在线笔记</a></td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
					<br />
					<table width="98%" border="1" cellpadding="0" cellspacing="0"
						class="list">
						<thead>
							<tr>
								<th><strong>已经提交的作业</strong>
								</td>
								<th><strong>提交时间 </strong>
								</td>
								<th><strong>得分</strong></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${exercisetList }" var="b" varStatus="vs">
								<c:if test="${b.status ge 1 }">
									<tr>
										<td align="center"><a href="javascript:void(0)"
											onclick="goExercise('${b.resourceid}');" title="查看作业">${b.colName }</a></td>
										<td align="center"><a href="javascript:void(0)"
											onclick="goExercise('${b.resourceid}');" title="查看作业"><fmt:formatDate
													value="${b.comitdate }" pattern="yyyy-MM-dd HH:mm:ss" /></a></td>
										<td align="center"><a href="javascript:void(0)"
											onclick="goExercise('${b.resourceid}');" title="查看作业"><fmt:formatNumber
													value="${b.result }" pattern="###.#" /></a></td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- tab3 -->
				<div class="TabbedPanelsContent">
					<h2 class="htitle2">错题集</h2>
					<table width="100%" border="1" cellpadding="0" cellspacing="0"
						class="list">
						<tbody>
							<c:forEach items="${mistakeExamList }" var="exam" varStatus="vs">
								<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
									<td width="100%">
										<div>
											<span style="font-weight: bold;"> ${vs.index+1 }. </span>${exam.question }</div>
										<div>
											<span style="font-weight: bold;">学生答案: </span>${exam.stuanswer }</div>
										<div>
											<span style="font-weight: bold;">参考答案: </span>${exam.answer }</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<p>&nbsp;</p>
				</div>
			</div>

		</div>

		<script type="text/javascript">
	<!--
	var learningTab = new Spry.Widget.TabbedPanels("learningTabbedPanels");
	
	function goCourseLearningNote(syllabusid){
		var _courseDialogOp = {id:'resource_course_dialog', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false, autopos:'fixed',width:800,height:600, onClose: _learningNoteOnClose};
		Dialogs.load(baseUrl+"/resource/course/note.html?syllabusid="+syllabusid,_courseDialogOp).title("在线笔记");
	}
	function goExercise(batchid){
		window.open(baseUrl+"/resource/course/exercise.html?exerciseBatchId="+batchid,"_exercise");
	}
	//-->
	</script>
	</div>
</body>
</html>