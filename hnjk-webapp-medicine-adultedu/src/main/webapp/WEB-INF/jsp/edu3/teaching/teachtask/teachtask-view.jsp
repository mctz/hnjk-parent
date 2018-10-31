<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书查看</title>
</head>
<body>
	<c:choose>
		<c:when test="${teachTask.isTemplate eq 'Y'}">
			<h2 class="contentTitle">${teachTask.yearInfo }${ghfn:dictCode2Val('CodeTerm',teachTask.term) }教学任务书模板</h2>
			<div class="page">
				<div class="pageContent">
					<div class="pageFormContent" layoutH="97">
						<table class="form"">
							<tr>
								<td style="width: 5%">序号</td>
								<td style="width: 12%">开始时间</td>
								<td style="width: 12%">结束时间</td>
								<td style="width: 10%">教学活动</td>
								<td style="width: 39%">教学任务内容</td>
								<td style="width: 10%">是否允许修改</td>
								<td style="width: 12%">预警时间</td>
							</tr>
							<c:forEach items="${teachTask.teachTaskDetails }" var="c"
								varStatus="vs">
								<tr>
									<td>${c.showOrder }</td>
									<td><fmt:formatDate value="${c.startTime }"
											pattern='yyyy-MM-dd HH:mm:ss' /></td>
									<td><fmt:formatDate value="${c.endTime }"
											pattern='yyyy-MM-dd HH:mm:ss' /></td>
									<td>${ghfn:dictCode2Val('CodeTaskType',c.taskType) }</td>
									<td>${c.taskContent }</td>
									<td>${ghfn:dictCode2Val('yesOrNo',c.isAllowModify) }</td>
									<td><fmt:formatDate value="${c.warningTime }"
											pattern='yyyy-MM-dd HH:mm:ss' /></td>
								</tr>
							</c:forEach>
						</table>
					</div>
					<div class="formBar">
						<ul>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close">关闭</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<h2 class="contentTitle">教学任务书</h2>
			<div class="page">
				<div class="pageContent">
					<div class="pageFormContent" layoutH="97">
						<div class="tabs" curentIndex="2">
							<div class="tabsHeader">
								<div class="tabsHeaderContent">
									<ul>
										<li><a href="#"><span>任务书</span></a></li>
										<li class="selected"><a href="#"><span>教学任务安排</span></a></li>
									</ul>
								</div>
							</div>
							<div class="tabsContent" style="height: 100%;">
								<!-- 1 -->
								<div>
									<table class="form">
										<tr>
											<td width="10%">年度</td>
											<td width="40%">${teachTask.yearInfo}</td>
											<td width="10%">学期</td>
											<td width="40%">${ghfn:dictCode2Val('CodeTerm',teachTask.term) }</td>
										</tr>
										<tr>
											<td>课程</td>
											<td>${teachTask.course.courseCode}-${teachTask.course.courseName}</td>
											<td>任务状态</td>
											<td>${ghfn:dictCode2Val('CodeTaskStatus',teachTask.taskStatus) }</td>
										</tr>
										<tr>
											<td>返回时限</td>
											<td><fmt:formatDate value='${teachTask.returnTime}'
													pattern='yyyy-MM-dd' /></td>
											<td>实际返回时间</td>
											<td><fmt:formatDate value='${teachTask.realReturnTime}'
													pattern='yyyy-MM-dd' /></td>
										</tr>
										<tr>
											<td>主讲老师</td>
											<td>${teachTask.teacherName }</td>
											<td>辅导老师</td>
											<td>${teachTask.assistantNames }</td>
										</tr>
										<tr>
											<td>审核人</td>
											<td>${teachTask.auditMan }</td>
											<td>审核时间</td>
											<td><fmt:formatDate value='${teachTask.auditDate }'
													pattern='yyyy-MM-dd' /></td>
										</tr>
										<tr>
											<td>教学任务建议</td>
											<td colspan="3">${fn:replace(teachTask.taskAdvise, newLine, "<br/>")}</td>
										</tr>
										<tr>
											<td>备注</td>
											<td colspan="3">${teachTask.memo }</td>
										</tr>
									</table>
								</div>
								<!-- 2 -->
								<div>
									<table class="form"">
										<tr>
											<td style="width: 4%">&nbsp;</td>
											<td style="width: 8%">教学活动</td>
											<td style="width: 35%">教学任务内容</td>
											<td style="width: 8%">开始时间</td>
											<td style="width: 8%">结束时间</td>
											<td style="width: 8%">预警时间</td>
											<td style="width: 8%">任务所有者</td>
											<td style="width: 11%">参与者</td>
											<td style="width: 5%">状态</td>
											<td style="width: 5%">评价</td>
										</tr>
										<c:forEach items="${teachTask.teachTaskDetails }" var="c"
											varStatus="vs">
											<tr>
												<td>${c.showOrder }</td>
												<td>${ghfn:dictCode2Val('CodeTaskType',c.taskType) }</td>
												<td>${c.taskContent }</td>
												<td><fmt:formatDate value="${c.startTime }"
														pattern='yyyy-MM-dd HH:mm:ss' /></td>
												<td><fmt:formatDate value="${c.endTime }"
														pattern='yyyy-MM-dd HH:mm:ss' /></td>
												<td><fmt:formatDate value="${c.warningTime }"
														pattern='yyyy-MM-dd HH:mm:ss' /></td>
												<td>${c.ownerName }</td>
												<td>${c.participantsName }</td>
												<td>${ghfn:dictCode2Val('CodeTeachTaskStatus',c.status) }</td>
												<td>${ghfn:dictCode2Val('CodeTeachTaskEvaluate',c.evaluate) }</td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
							<div class="tabsFooter">
								<div class="tabsFooterContent"></div>
							</div>
						</div>
					</div>
					<div class="formBar">
						<ul>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close">关闭</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>