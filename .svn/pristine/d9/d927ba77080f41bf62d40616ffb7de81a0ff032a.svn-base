<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>未录入成绩课程</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader"
			style="padding-top: 10px; padding-bottom: 10px">
			<h2>
				以下是学号为：<font color="red">${condition['studentNo'] }</font>
				的同学未发布成绩的课程
			</h2>
		</div>
		<div class="pageContent">
			<table class="table" layouth="112" width="100%">
				<thead>
					<tr>
						<th width="20%">考试批次</th>
						<th width="25%">课程名称</th>
						<th width="15%">登分教师</th>
						<th width="10%">开课学期</th>
						<th width="15%">考核方式</th>
						<th width="15%">发布状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${noExamResultList}" var="ner" varStatus="vs">
						<tr>
							<td>${ner.batchname }</td>
							<td>${ner.coursename}</td>
							<td>${ner.teachername}</td>
							<td>${ghfn:dictCode2Val('CodeCourseTermType',ner.term)}</td>
							<td>${ghfn:dictCode2Val('CodeExamClassType',ner.examclasstype)}</td>
							<td><c:if test="${ner.checkstatus eq  null }">未录入</c:if> <c:if
									test="${!(ner.checkstatus eq  null) }">${ghfn:dictCode2Val('CodeExamResultCheckStatus',ner.checkstatus)}</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>