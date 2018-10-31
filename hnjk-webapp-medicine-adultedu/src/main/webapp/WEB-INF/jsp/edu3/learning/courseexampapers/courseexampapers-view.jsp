<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷查看</title>
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
	<h2 class="contentTitle">${courseExamPapers.paperName }</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="60">
				<table class="form"
					style="table-layout: fixed; word-wrap: break-word">
					<tr>
						<td style="width: 10%">试卷名称:</td>
						<td style="width: 40%">${courseExamPapers.paperName }</td>
						<td style="width: 10%">层次:</td>
						<td style="width: 40%">${courseExamPapers.classic.classicName}</td>
					</tr>
					<tr>
						<td>试卷类型:</td>
						<td>${ghfn:dictCode2Val('CodePaperType',courseExamPapers.paperType)}</td>
						<td>课程:</td>
						<td><c:choose>
								<c:when test="${courseExamPapers.paperType eq 'entrance_exam' }">${ghfn:dictCode2Val('CodeEntranceExam',courseExamPapers.courseName)}</c:when>
								<c:otherwise>${courseExamPapers.course.courseName }</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td>考试时长:</td>
						<td>${courseExamPapers.paperTime}分钟</td>
						<td>是否开放:</td>
						<td>${ghfn:dictCode2Val('yesOrNo',courseExamPapers.isOpened)}</td>
					</tr>
					<tr>
						<td>总题目数:</td>
						<td>${examCount}</td>
						<td>总分数:</td>
						<td><fmt:formatNumber value="${examScore}" pattern="###.#" /></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3">${courseExamPapers.memo }</td>
					</tr>
				</table>
				<table class="form">
					<c:forEach items="${courseExamPapers.courseExamPaperDetails }"
						var="detail" varStatus="vs">
						<tr>
							<td
								<c:if test="${detail.courseExam.examType eq '6' }">style="background-color: #FFF;border:1px solid #CACACA;"</c:if>>
								<div>
									<span style="font-weight: bold;">${showOrders[detail.resourceid] }${(detail.courseExam.examType ne '6')?'.':''}</span>
									${detail.courseExam.question }
								</div>
								<div>&nbsp;</div> <c:if
									test="${detail.courseExam.examType ne '6' }">
									<div>【答案： ${detail.courseExam.answer }】</div>
									<div>&nbsp;</div>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			</form>
		</div>
	</div>
</body>
</html>