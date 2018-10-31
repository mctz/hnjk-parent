<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>复查试卷</title>
<style type="text/css">
.list div {
	line-height: 170%;
}

.list td {
	word-wrap: break-word
}

.list p {
	width: 100%;
}
</style>
</head>
<body>
	<h2 class="contentTitle">复查试卷</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="" class="pageForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">学号：</td>
							<td>${examResult.studentInfo.studyNo }</td>
							<td width="20%">姓名：</td>
							<td>${examResult.studentInfo.studentName }</td>
						</tr>
						<tr>
							<td width="20%">课程：</td>
							<td>${examResult.course.courseName }</td>
							<td width="20%">卷面成绩：</td>
							<td><fmt:formatNumber value="${examResult.writtenScore }"
									pattern="###.#" /></td>
						</tr>
						<tr>
							<td width="20%">客观题成绩：</td>
							<td><fmt:formatNumber
									value="${examResult.writtenMachineScore }" pattern="###.#" /></td>
							<td width="20%">主观题成绩：</td>
							<td><fmt:formatNumber
									value="${examResult.writtenOnlineHandworkScore }"
									pattern="###.#" /></td>
						</tr>
					</table>
					<table class="list" width="98%;">
						<tbody>
							<c:forEach items="${courseExamPaperDetails }" var="exam">
								<tr bgcolor="#ffffff">
									<td width="100%" style="word-wrap: break-word">
										<div style="width: 100%; word-wrap: break-word">
											<span style="font-weight: bold;">${showOrders[exam.resourceid] }${(exam.courseExam.examType ne '6')?'.':''}
												<c:if test="${exam.courseExam.examType ne '6' }">
													<c:choose>
														<c:when
															test="${(not empty exam.courseExam.examNodeType) and (exam.courseExam.examType eq '4' or exam.courseExam.examType eq '5') }">
								 (${ghfn:dictCode2Val('CodeExamNodeType',exam.courseExam.examNodeType) })
								 </c:when>
														<c:otherwise>(${ghfn:dictCode2Val('CodeExamType',exam.courseExam.examType) })</c:otherwise>
													</c:choose>
								 (<fmt:formatNumber value="${exam.score }" pattern="###.#" />分)
							 </c:if>
											</span>
											<c:if test="${exam.courseExam.examType eq '6' }">
												<span style="font-weight: bold;">${ghfn:dictCode2Val('CodeExamNodeType',exam.courseExam.examNodeType) }</span>
												<br />
											</c:if>
											${exam.courseExam.question }
										</div> <c:if test="${exam.courseExam.examType ne '6'}">
											<div>
												<span style="font-weight: bold;">参考答案：</span>
												${exam.courseExam.answer }
											</div>
											<div style="padding-top: 5px;">
												<span style="font-weight: bold;">学生答案：</span>
												<c:choose>
													<%-- 选择题 --%>
													<c:when test="${exam.courseExam.examType lt 3 }">${studentAnswers[exam.resourceid].answer }</c:when>
													<c:when test="${exam.courseExam.examType eq 3 }">${'T' eq studentAnswers[exam.resourceid].answer ? '对' : '错'}</c:when>
													<c:otherwise>
														<div>${fn:replace(studentAnswers[exam.resourceid].answer, lineChar, '<br/>')}</div>
													</c:otherwise>
												</c:choose>
											</div>
											<div style="padding-top: 5px;">
												<span style="font-weight: bold;">得分：<fmt:formatNumber
														value="${studentAnswers[exam.resourceid].result }"
														pattern="###.#" /></span>
											</div>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close">关 闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>