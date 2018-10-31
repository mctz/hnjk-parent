<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习计划</title>
</head>
<body>
	<div class="page">
		<div>
			<table id="leanPlanTable" class="list" width="100%">
				<thead>
					<tr>
						<td colspan="9"><strong>学制：</strong>${teachingPlanList['planTitle'].eduYear }
							<strong>最低毕业学分：</strong>${teachingPlanList['planTitle'].minResult }
							<strong>已修总学分：</strong>${teachingPlanList['planTitle'].totalScore}
							<strong>已修必修课总学分：</strong>${teachingPlanList['planTitle'].compulsoryedScore}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<strong>已修必修课数：</strong>${teachingPlanList['planTitle'].compulsoryed}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</thead>
				<tbody id="tab">
					<tr>
						<td colspan="9"
							style="background: #ff6600; color: #fff; font-weight: bold;">
							<marquee direction=left scrollamount=5 onmouseover="this.stop()"
								onmouseout="this.start()"> 全国统考时间：2009-12-5 距今：30天
								考试预约起止时间：2009-12-1 距今：20天 </marquee>
						</td>
					</tr>
					<!-- 学生学习计划 -->
					<tr>
						<th width="6%">编号</th>
						<th width="18%">课程名称</th>
						<th width="12%">课程性质</th>
						<th width="7%">学分</th>
						<th width="11%">建议学期</th>
						<th width="13%">状态</th>
						<th width="9%">预约</th>
						<th width="5%">成绩</th>
						<th width="19%">考试时间</th>
					</tr>
					<c:forEach items="${teachingPlanList['planCourseList']}"
						var="course" varStatus="vs">
						<tr>
							<td>${course.teachingPlan_Course_num }</td>
							<td><c:choose>
									<c:when test="${course.teachingPlan_Course_learnStatusInt>-1 }">
										<a href="javascript:void(0)"
											onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }')">${course.teachingPlan_Course_name }</a>
									</c:when>
									<c:otherwise>
				      			${course.teachingPlan_Course_name }
				      		</c:otherwise>
								</c:choose></td>
							<td>${course.teachingPlan_Course_type }</td>
							<td>${course.teachingPlan_Course_creditHour }</td>
							<td>${course.teachingPlan_Course_term }</td>

							<%--学习状态 --%>
							<c:choose>
								<c:when
									test="${course.teachingPlan_Course_learnStatus eq '正在学习' }">
									<td><font color='green'>${course.teachingPlan_Course_learnStatus}</font></td>
								</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatus eq '已预约考试' }">
									<td><font color='red'>${course.teachingPlan_Course_learnStatus}</font></td>
								</c:when>
								<c:otherwise>
									<td>${course.teachingPlan_Course_learnStatus}</td>
								</c:otherwise>
							</c:choose>
							<%--学习状态 --%>

							<%--预约状态 --%>
							<c:choose>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt ==-1 && course.teachingPlan_Course_learnStatus eq '未预约学习' }">
									<td><a href='javascript:void(0)'
										style='color: red; font-weight: normal; font-size: 10pt'
										onclick="changeTeachingPlanCourseStatus('1','${ course.teachingPlan_Course_Id}','${ course.teachingPlan_Id}')">${course.teachingPlan_Course_bookingStatus}</a></td>
								</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt ==2 && course.teachingPlan_Course_learnStatus eq '正在学习' }">
									<td><a href='javascript:void(0)'
										style='color: Blue; font-weight: normal; font-size: 10pt'
										onclick="changeTeachingPlanCourseStatus('${course.teachingPlan_Course_learnStatusInt}','${ course.teachingPlan_Course_Id}','${ course.teachingPlan_Id}')">${course.teachingPlan_Course_bookingStatus}</a></td>
								</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt ==2 && course.isNeedReExamination eq 'Y' }">
									<td><a href='javascript:void(0)'
										style='color: Blue; font-weight: normal; font-size: 10pt'
										onclick="changeTeachingPlanCourseStatus('${course.teachingPlan_Course_learnStatusInt}','${ course.teachingPlan_Course_Id}','${ course.teachingPlan_Id}')">${course.teachingPlan_Course_bookingStatus}</a></td>
								</c:when>
								<c:otherwise>
									<td>${course.teachingPlan_Course_bookingStatus}</td>
								</c:otherwise>
							</c:choose>
							<%--预约状态 --%>

							<td>${course.teachingPlan_Course_score}</td>
							<td><c:choose>
									<c:when
										test="${ course.teachingPlan_Course_examStartTime != null && course.teachingPlan_Course_examStartTime != ''}">
					      			${course.teachingPlan_Course_examStartTime}-${course.teachingPlan_Course_examEndTime }
					      		</c:when>
									<c:otherwise>
					      			&nbsp;
					      		</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
		</div>
	</div>

	</div>
</body>
</html>