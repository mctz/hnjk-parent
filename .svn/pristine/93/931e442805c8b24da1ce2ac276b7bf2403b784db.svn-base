<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>批改学生在线作业</title>
<style>
.pageFormContent p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
}
</style>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/exercisecheck/studentexercise/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="exerciseBatchId"
					value="${studentExerciseResult.exerciseBatch.resourceid }" /> <input
					type="hidden" name="studentExerciseResultId"
					value="${studentExerciseResult.resourceid }" /> <input
					type="hidden" name="from" value="fact" />
				<div class="pageFormContent" layoutH="60">
					<table class="form" style="width: 99%">
						<c:forEach items="${exercises }" var="exercise" varStatus="vs">
							<tr>
								<td>
									<div>
										<div>${exercise.showOrder}.(${exercise.score }分)
											&nbsp;${exercise.courseExam.question }&nbsp;</div>
										<input type="hidden"
											value="${exercise.studentExercise.resourceid}"
											name="studentExerciseId" />
									</div>
									<div>
										学生答案：
										<div
											style="background-color: #fff; border: 1px solid #CACACA; min-height: 35px;">${exercise.studentExercise.answer }
										</div>
									</div>
									<div style="color: green;">
										参考答案：${exercise.courseExam.answer }</div>
								</td>
							</tr>
							<c:if
								test="${studentExerciseResult.exerciseBatch.scoringType eq '2' }">
								<tr
									style="display: ${(studentExerciseResult.exerciseBatch.scoringType eq '1')?'none':'' }">
									<td><label><font color="red">老师评分：</font></label><input
										type="text" name="result"
										value="${exercise.studentExercise.result }"
										class="required number" size="30" /></td>
								</tr>
							</c:if>
						</c:forEach>
						<tr
							style="display: ${(studentExerciseResult.exerciseBatch.scoringType eq '1')?'none':'' }">
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>
								<table width="100%">
									<tr>
										<td width="20%">是否评为优秀作业</td>
										<td width="30%"><gh:select name="isExcell"
												value="${studentExerciseResult.isExcell}"
												dictionaryCode="yesOrNo" choose="N" /></td>
										<td width="20%">是否为典型批改</td>
										<td width="30%"><gh:select name="isTypical"
												value="${studentExerciseResult.isTypical}"
												dictionaryCode="yesOrNo" choose="N" /></td>
									</tr>
									<tr>
										<td>老师评价：</td>
										<td colspan="3"><textarea name="teacherAdvise" rows="8"
												cols="" style="width: 99%;">${studentExerciseResult.teacherAdvise}</textarea></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<!-- 平均分摊记分时只查看作业 -->
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>