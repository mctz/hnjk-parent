<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业系统</title>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyExercise"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/exercisebatch.html?courseId=${course.resourceid}">作业系统</a></li>
					<li><a
						href="${baseUrl }/resource/course/mocktest.html?courseId=${course.resourceid}">在线自测系统</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > 作业系统</div>
			<div class="clear"></div>
			<div id="content">
				<table class="list" width="100%">
					<thead>
						<tr>
							<th width="10%">年度</th>
							<th width="8%">学期</th>
							<th width="12%">课程</th>
							<th width="12%">作业标题</th>
							<th width="8%">作业类型</th>
							<th width="10%">截止日期</th>
							<th width="10%">剩余天数</th>
							<th width="12%">作业状态</th>
							<th width="8%">成绩</th>
							<th width="10%">评语</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${exerciseBatchs }" var="batch" varStatus="vs">
							<tr>
								<td>${batch.yearInfo.yearName }</td>
								<td>${ghfn:dictCode2Val('CodeTerm',batch.term) }</td>
								<td>${batch.course.courseName}</td>
								<td>${batch.colName }</td>
								<td class="center">${ghfn:dictCode2Val('CodeLearningColType',batch.colType) }</td>
								<td><fmt:formatDate value="${batch.endDate }"
										pattern="yyyy-MM-dd" /></td>
								<td class="center"><c:choose>
										<c:when test="${batch.isEnd eq 'Y' }">已过截止日期</c:when>
										<c:when
											test="${batch.isEnd eq 'N' and batch.remainingTime gt 0 }">剩余${batch.remainingTime }天</c:when>
										<c:otherwise>今天</c:otherwise>
									</c:choose></td>
								<td class="center"><c:choose>
										<c:when
											test="${empty stuExercises[batch.resourceid] or stuExercises[batch.resourceid].status eq 0 }">
											<a
												href="${baseUrl }/resource/course/exercise.html?exerciseBatchId=${batch.resourceid}"
												title="${batch.colName }">现在做</a>
										</c:when>
										<c:otherwise>
											<a
												href="${baseUrl }/resource/course/exercise.html?exerciseBatchId=${batch.resourceid}"
												title="${batch.colName }">${ghfn:dictCode2Val('CodeStudentExerciseStatus',stuExercises[batch.resourceid].status) }</a>
										</c:otherwise>
									</c:choose></td>
								<td class="center"><fmt:formatNumber
										value="${stuExercises[batch.resourceid].result }"
										pattern="###.#" /></td>
								<td>${stuExercises[batch.resourceid].teacherAdvise }</td>
							</tr>
						</c:forEach>
						<c:if test="${isStudent eq 'Y' and not empty stuExercisesAvg }">
							<tr>
								<td colspan="8" style="text-align: right;">得分:</td>
								<td>${stuExercisesAvg }</td>
								<td colspan="3"></td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
		<!--end right-->
	</div>
</body>
</html>