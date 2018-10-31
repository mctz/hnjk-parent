<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的历史成绩</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layouth="21">
				<!-- tabs -->
				<c:if test="${isDisplayScore eq 'N' }">
				<strong><span style="color:red;">  提示： 按学校要求，未有缴费记录或未缴费，则不显示成绩。可以在【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况<br></span></strong>
				<br/>
				</c:if>
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<c:forEach items="${resultsList }" var="stu">
									<li
										<c:if test="${stu.isDefaultStudentId eq 'Y' }">class="selected"</c:if>><a
										href="javascript:void(0)"><span>${stu.studentInfo.teachingPlan.major.majorName }-${stu.studentInfo.teachingPlan.classic.classicName }</span></a></li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<c:forEach items="${resultsList }" var="stu">
							<div class="pageContent">
								<table class="table" layouth="138">
									<thead>
										<tr>
											<th width="5%">序号</th>
											<th width="20%">课程名称</th>
											<th width="10%">课程性质</th>
											<th width="10%">考试形式</th>
											<!-- <th width="10%">卷面成绩</th>
									      <th width="10%">平时成绩</th> -->
											<th width="15%">综合成绩</th>
											<th width="20%">考试时间</th>
											<th width="20%">考试批次</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${stu.examResultsList }" var="vo"
											varStatus="vs">
											<tr>
												<td>${vs.index+1 }</td>
												<td>${vo.courseName }</td>
												<td>${vo.courseType}</td>
												<td>${vo.examType}</td>
												<!-- <td>${vo.writtenScore}</td>
									   			<td>${vo.usuallyScore}</td> -->
												<td><c:choose>
														<c:when test="${vo.isNoExam eq 'Y' }">${vo.checkStatus }</c:when>
														<c:when test="${not empty vo.examResultsChs}">
									            			${vo.examResultsChs}
									            		</c:when>
														<c:otherwise>
									            			${vo.integratedScore}
									            		</c:otherwise>
													</c:choose></td>
												<td>${vo.examTime }</td>
												<td>${vo.batchName }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:forEach>
					</div>
					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>
				<!-- end tabs -->
			</div>
		</div>
	</div>
</body>
</html>