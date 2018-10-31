<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书</title>
</head>
<body>
	<h2 class="contentTitle">查看与评价</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachtask/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${teachTask.resourceid }" /> <input type="hidden"
					name="type" value="details" />
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
										<td>${teachTask.course.courseName}</td>
										<td>返回时限</td>
										<td><fmt:formatDate value='${teachTask.returnTime}'
												pattern='yyyy-MM-dd' /></td>
									</tr>
									<tr>
										<td>主讲老师</td>
										<td>${teachTask.teacherName }</td>
										<td>辅导老师</td>
										<td>${teachTask.assistantNames }</td>
									</tr>
									<tr>
										<td>备注</td>
										<td colspan="3">${teachTask.memo }</td>
									</tr>
									<tr>
										<td>教学任务建议</td>
										<td colspan="3">${teachTask.taskAdvise }</td>
									</tr>
								</table>
							</div>
							<!-- 2 -->
							<div>
								<table class="form" id="td_Tab">
									<tr>
										<td style="width: 4%">&nbsp;</td>
										<td style="width: 10%">教学活动</td>
										<td style="width: 35%">教学内容</td>
										<td style="width: 8%">开始时间</td>
										<td style="width: 8%">结束时间</td>
										<td style="width: 5%">任务所有者</td>
										<td style="width: 18%">参与者</td>
										<td style="width: 6%">状态</td>
										<td style="width: 6%">评价</td>
									</tr>
									<c:forEach items="${teachTask.teachTaskDetails }" var="c"
										varStatus="vs">
										<tr>
											<td>${c.showOrder }<input type="hidden" name='tk_id'
												value='${c.resourceid }'></td>
											<td>${ghfn:dictCode2Val('CodeTaskType',c.taskType) }</td>
											<td>${c.taskContent }</td>
											<td><fmt:formatDate value="${c.startTime }"
													pattern='yyyy-MM-dd HH:mm:ss' /></td>
											<td><fmt:formatDate value="${c.endTime }"
													pattern='yyyy-MM-dd HH:mm:ss' /></td>
											<td>${c.ownerName }</td>
											<td>${c.participantsName }</td>
											<td>${ghfn:dictCode2Val('CodeTeachTaskStatus',c.status) }</td>
											<td><gh:select name='evaluate'
													dictionaryCode='CodeTeachTaskEvaluate' style='width:80%'
													value='${c.evaluate }' /></td>
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
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>