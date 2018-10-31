<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table border="1">
				<thead>
					<tr align="center">
						<th colspan="15" style="font-size: large;">在线学习情况</th>
					</tr>
					<tr>
						<th width="7%">年度</th>
						<th width="4%">学期</th>
						<th width="9%">课程</th>
						<th width="8%">学号</th>
						<th width="5%">姓名</th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="10%">专业</th>
						<th width="10%">班级</th>
						<th width="4%">问答分</th>
						<th width="4%">练习分</th>
						<th width="4%">作业分</th>
						<th width="5%">综合分数</th>
						<th width="4%">状态</th>
						<th width="14%">成绩比重</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${onlineLearningInfoList}" var="info"
						varStatus="vs">
						<tr>
							<td>${info.yearName}</td>
							<td>${ghfn:dictCode2Val('CodeTerm',info.term) }</td>
							<td>${info.courseName}<input type="hidden" name="courseId"
								value="${info.courseId }" /></td>
							<td>${info.studyNo}</td>
							<td>${info.studentName}</td>
							<td>${info.unitName}</td>
							<td>${info.gradeName}</td>
							<td>${info.majorName}</td>
							<td>${info.classesName}</td>
							<td>${info.askQuestionResults}</td>
							<td>${info.courseExamResults}</td>
							<td>${info.exerciseResults}</td>
							<td><fmt:formatNumber pattern="###.#"
									value="${info.usualResults}" /></td>
							<td><c:choose>
									<c:when test="${info.status eq '1' }">已提交</c:when>
									<c:when test="${info.status eq '0' }">已保存</c:when>
									<c:otherwise>未录入</c:otherwise>
								</c:choose></td>
							<td>${info.resultsProportion }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>