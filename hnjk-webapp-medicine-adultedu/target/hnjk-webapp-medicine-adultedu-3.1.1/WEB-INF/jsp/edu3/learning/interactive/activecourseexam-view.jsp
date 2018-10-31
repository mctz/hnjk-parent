<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习答题情况</title>
</head>
<body>
	<div class="page">
		<div class="pageContent" layouth="21">
			<table class="list" width="100%">
				<tbody>
					<c:forEach begin="1" end="${totalCols }" var="idx">
						<tr>
							<c:forEach begin="${(idx-1)*10 }" end="${idx*10 }" var="ind">
								<td><c:if test="${ind lt fn:length(activeCourseExams) }">
										<span style="font-weight: bold;">${activeCourseExams[ind].showOrder }.
											${answers[activeCourseExams[ind].resourceid].answer }</span>
										<c:choose>
											<c:when
												test="${not empty answers[activeCourseExams[ind].resourceid].result }">
												<span style="color: red;">(已提交)</span>
											</c:when>
											<c:otherwise>
												<span>(未提交)</span>
											</c:otherwise>
										</c:choose>
									</c:if></td>
							</c:forEach>
						</tr>
					</c:forEach>
					<c:if test="${oldCorrectCount gt 0 }">
						<tr>
							<td colspan="10">本练习的旧版本中，你做对了${oldCorrectCount }题，这些题目的答题结果有效，已列入计分范围。
							</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>