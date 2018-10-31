<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程作业列表</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layoutH="50">
				<thead>
					<tr>
						<th width="10%">年度</th>
						<th width="5%">学期</th>
						<th width="10%">课程</th>
						<th width="10%">作业标题</th>
						<th width="5%">作业类型</th>
						<th width="10%">截止日期</th>
						<th width="10%">剩余天数</th>
						<th width="10%">作业状态</th>
						<th width="8%">成绩</th>
						<!--                		<th width="6%">是否典型批改</th>  -->
						<!-- 	               	<th width="6%">是否优秀作业</th>  -->
						<th width="10%">评语</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${exerciseBatchs.result }" var="batch"
						varStatus="vs">
						<tr>
							<td>${batch.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',batch.term) }</td>
							<td>${batch.course.courseName}</td>
							<td>${batch.colName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningColType',batch.colType) }</td>
							<td><fmt:formatDate value="${batch.endDate }"
									pattern="yyyy-MM-dd" /></td>
							<td><c:choose>
									<c:when test="${batch.isEnd eq 'Y' }">已过截止日期</c:when>
									<c:when
										test="${batch.isEnd eq 'N' and batch.remainingTime gt 0 }">剩余${batch.remainingTime }天</c:when>
									<c:otherwise>今天</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when
										test="${empty stuExercises[batch.resourceid] or stuExercises[batch.resourceid].status eq 0 }">
										<a
											href="${baseUrl }/edu3/learning/interactive/exercise/list.html?exerciseBatchId=${batch.resourceid}"
											rel="exercise" target="navTab" title="${batch.colName }">现在做</a>
									</c:when>
									<c:otherwise>
										<a
											href="${baseUrl }/edu3/learning/interactive/exercise/list.html?exerciseBatchId=${batch.resourceid}"
											rel="exercise" target="navTab" title="${batch.colName }">${ghfn:dictCode2Val('CodeStudentExerciseStatus',stuExercises[batch.resourceid].status) }</a>
									</c:otherwise>
								</c:choose></td>
							<td><fmt:formatNumber
									value="${stuExercises[batch.resourceid].result }"
									pattern="###.#" /></td>
							<%-- 			   			<c:choose> --%>
							<%-- 			   			<c:when test="${stuExercises[batch.resourceid].status eq 2 }"> --%>
							<%-- 			   			<td>${ghfn:dictCode2Val('yesOrNo',stuExercises[batch.resourceid].isTypical) }</td> --%>
							<%-- 				   		<td>${ghfn:dictCode2Val('yesOrNo',stuExercises[batch.resourceid].isExcell) }</td> --%>
							<%-- 			   			</c:when> --%>
							<%-- 			   			<c:otherwise> --%>
							<!-- 			   			<td> </td> -->
							<!-- 				   		<td> </td> -->
							<%-- 			   			</c:otherwise> --%>
							<%-- 			   			</c:choose> --%>
							<td>${stuExercises[batch.resourceid].teacherAdvise }</td>
						</tr>
					</c:forEach>
					<c:if test="${not empty stuExercisesAvg }">
						<tr>
							<td colspan="8" style="text-align: right;">得分:</td>
							<td>${stuExercisesAvg }</td>
							<td colspan="3"></td>
						</tr>
					</c:if>
				</tbody>
			</table>
			<gh:page page="${exerciseBatchs}"
				goPageUrl="${baseUrl }/edu3/learning/interactive/exercisebatch/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
