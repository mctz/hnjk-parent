<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业展示</title>
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
			<div class="position">当前位置：课程学习 > 作业展示</div>
			<div class="clear"></div>
			<div id="content">
				<table class="list" width="100%">
					<thead>
						<tr>
							<th width="15%">学号</th>
							<th width="15%">学生姓名</th>
							<th width="15%">作业名称</th>
							<th width="10%">作业类型</th>
							<th width="10%">成绩</th>
							<th width="25%">评语</th>
							<th width="10%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${studentExerciseList }" var="exercise"
							varStatus="vs">
							<tr>
								<td class="center">${exercise.studentInfo.studyNo }</td>
								<td class="center">${exercise.studentInfo.studentName }</td>
								<td>${exercise.exerciseBatch.colName }</td>
								<td class="center">${ghfn:dictCode2Val('CodeLearningColType',exercise.exerciseBatch.colType) }</td>
								<td class="center"><fmt:formatNumber
										value="${exercise.result }" pattern="###.#" /></td>
								<td>${exercise.teacherAdvise }</td>
								<td class="center"><a
									href="${baseUrl }/resource/course/studentexercise.html?exerciseid=${exercise.resourceid}"
									title="查看">查看</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!--end right-->
	</div>
</body>
</html>