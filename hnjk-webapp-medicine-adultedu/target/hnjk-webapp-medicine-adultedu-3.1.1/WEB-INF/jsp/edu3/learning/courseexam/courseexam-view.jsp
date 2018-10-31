<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>题库试题查看</title>
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

.pageFormContent img {
	vertical-align: middle;
}
</style>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="25">
				<table class="form"
					style="table-layout: fixed; word-wrap: break-word">
					<tr>
						<td>课程:</td>
						<td colspan="3"><c:choose>
								<c:when test="${courseExam.isEnrolExam eq 'Y' }">${ghfn:dictCode2Val('CodeEntranceExam',courseExam.courseName)}</c:when>
								<c:otherwise>${courseExam.course.courseName }</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td style="width: 15%">题型:</td>
						<td style="width: 35%">${ghfn:dictCode2Val('CodeExamType',courseExam.examType)}</td>
						<td style="width: 15%">类别:</td>
						<td style="width: 35%">${ghfn:dictCode2Val('CodeExamNodeType',courseExam.examNodeType)}</td>
					</tr>
					<tr>
						<td style="width: 15%">难度:</td>
						<td style="width: 35%">${ghfn:dictCode2Val('CodeExamDifficult',courseExam.difficult)}</td>
						<td>考试要求:</td>
						<td>${ghfn:dictCode2Val('CodeTeachingRequest',courseExam.requirement)}</td>
					</tr>
					<tr>
						<td>关键字:</td>
						<td>${courseExam.keywords }</td>
						<td>题库序号:</td>
						<td>${courseExam.showOrder }</td>
					</tr>
					<c:choose>
						<c:when test="${courseExam.examType eq '6' }">
							<tr>
								<td colspan="4">${courseExam.question }</td>
							</tr>
							<c:forEach items="${courseExam.childs }" var="child">
								<tr>
									<td colspan="4">
										<div>${child.question }</div>
										<div>答案:${child.answer }</div>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td>问题:</td>
								<td colspan="3">${courseExam.question }</td>
							</tr>
							<tr>
								<td>答案:</td>
								<td colspan="3">${courseExam.answer }</td>
							</tr>
							<tr>
								<td>解析:</td>
								<td colspan="3">${courseExam.parser }</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
			</form>
		</div>
	</div>
</body>
</html>