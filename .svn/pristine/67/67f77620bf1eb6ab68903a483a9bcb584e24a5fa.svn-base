<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生课程不通过信息</title>
</head>
<body>
	<div class="page">
		<div class="panelBar">
			<ul class="toolBar">
				<li style="margin-top: 5px;"><h5>
						以下是<font color="blue">${studentInfo.studentName }</font>的不及格课程信息
					</h5></li>
			</ul>
		</div>
		<div class="pageContent" layoutH="95%">
			<input id="studentId" name="studentId" type="hidden"
				value="${studentInfo.resourceid}" />
			<table class="list" width="100%">
				<thead>
					<tr>
						<th width="4%" align="center">序号</th>
						<th width="8%">考试批次</th>
						<th width="12%">考试时间</th>
						<th width="12%">考试课程</th>
						<th width="8%">学时</th>
						<th width="8%">课程性质</th>
						<th width="8%">取得学分</th>
						<th width="8%">卷面成绩</th>
						<th width="8%">平时成绩</th>
						<th width="8%">综合成绩</th>
						<th width="8%">成绩状态</th>
						<!-- <th width="8%">选考次数</th> -->
						<th width="8%">考试类型</th>
					</tr>
				</thead>
				<tbody id="examResultsViewBody">
					<c:forEach items="${list}" var="examResults" varStatus="vs">
						<c:choose>
							<c:when test="${fn:length(list) == (vs.index+1)  }">
								<tr style="height: 50px">
									<td colspan="12">
										<table>
											<tr>
												<td style="text-align: right; color: #183152"><strong>合计
														：</strong></td>
												<td style="text-align: left; color: #183152"><strong>取得总学分：</strong></td>
												<td>${examResults.totalCredit}</td>
												<td style="text-align: left; color: #183152"><strong>取得本专业总学分：</strong></td>
												<td>${examResults.totalMajorCredit }</td>
												<td style="text-align: left; color: #183152"><strong>必修总学分：</strong></td>
												<td>${examResults.requiredCredit}</td>
												<td style="text-align: left; color: #183152"><strong>已修必修课：</strong></td>
												<td>${examResults.compulsoryed}</td>
												<td style="text-align: left; color: #183152"><strong>选修总学分：</strong></td>
												<td>${examResults.electiveCredit}</td>
												<td style="text-align: left; color: #183152"><strong>参考平均分：</strong></td>
												<td>${examResults.avgScore}</td>
												<td style="text-align: left; color: #183152"><strong>最低毕业学分：</strong></td>
												<td>${studentInfo.teachingPlan.minResult}</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>${vs.index+1 }</td>
									<td>${examResults.batchName }</td>
									<td><c:choose>
											<c:when test="${not empty examResults.passTime}">
												<fmt:formatDate value="${examResults.passTime}"
													pattern="yyyy-MM-dd" />
											</c:when>
											<c:otherwise>
												<fmt:formatDate value="${examResults.examStartTime}"
													pattern="yyyy-MM-dd" />
												<fmt:formatDate value="${examResults.examStartTime}"
													pattern="HH:mm" />-<fmt:formatDate
													value="${examResults.examEndTime}" pattern="HH:mm" />
											</c:otherwise>
										</c:choose></td>
									<td>${examResults.courseName }</td>
									<td>${examResults.stydyHour }</td>
									<td>${examResults.courseType }</td>
									<td>${examResults.inCreditHourStr }</td>
									<td>${examResults.writtenScore}</td>
									<td>${examResults.usuallyScore}</td>
									<td><c:choose>
											<c:when test="${not empty examResults.examResultsChs}">
			            			${examResults.examResultsChs}
			            		</c:when>
											<c:otherwise>
			            			${examResults.integratedScore}
			            		</c:otherwise>
										</c:choose></td>
									<c:choose>
										<c:when test="${examResults.checkStatusCode eq '4'}">
											<c:set var="c_l" value="green"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="c_l" value="red"></c:set>
										</c:otherwise>
									</c:choose>
									<td style="color: ${c_l}">${examResults.checkStatus}</td>
									<!-- <td>${examResults.examCount}</td> -->
									<td>${examResults.isdelayexam eq 'Y' ? "缓考" : ghfn:dictCode2Val('ExamResult',examResults.isMakeupExam) }
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>