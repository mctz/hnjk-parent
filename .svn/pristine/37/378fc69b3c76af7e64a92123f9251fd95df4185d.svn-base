<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍异动</title>
<script type="text/javascript">
$(function() {
	var scroll_offset = $("#jumpto").offset();  //得到pos这个div层的offset，包含两个值，top和left
	if(null!=scroll_offset){
		$("#changeInfoView").animate({
	 	scrollTop:scroll_offset.top-150  },1000);
	}
});
</script>
</head>
<body>

	<h2 class="contentTitle">查看学籍异动</h2>
	<div class="page">
		<div class="pageContent">


			<div id="changeInfoView" class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li class="selected"><a href="#"><span>异动信息</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<div>
							<table class="form" id="changeTab">
								<tr>
									<td width="20%">学生姓名:</td>
									<td width="30%">${stuChangeInfo.studentInfo.studentName }

									</td>
									<td width="20%">学生学号:</td>
									<td width="30%">${stuChangeInfo.studentInfo.studyNo }</td>
								</tr>
								<tr>
									<td width="20%">年级:</td>
									<td width="30%">
										${stuChangeInfo.studentInfo.grade.gradeName }</td>
									<td width="20%">当前教学站:</td>
									<td width="30%">
										${stuChangeInfo.studentInfo.branchSchool.unitName }</td>
								</tr>
								<tr>
									<td width="20%">当前专业:</td>
									<td width="30%">
										${stuChangeInfo.studentInfo.major.majorName }</td>
									<td width="20%">当前层次:</td>
									<td width="30%">
										${stuChangeInfo.studentInfo.classic.classicName }</td>
								</tr>
								<c:if test="${not empty stuChangeInfo.memo}">
									<tr>
										<td width="20%">备注:</td>
										<td width="80%" colspan="3">${stuChangeInfo.memo}</td>
									</tr>
								</c:if>
								<tr>
									<td colspan="4">
										<table class="list">
											<tr>
												<th style="width: 6%">序号</th>
												<th style="width: 8%">申请时间</th>
												<th style="width: 6%">异动类型</th>
												<th style="width: 6%">异动原因</th>
												<th style="width: 20%">异动前信息</th>
												<th style="width: 20%">异动后信息</th>
												<th style="width: 6%">申请人</th>
												<th style="width: 8%">申请时间</th>
												<th style="width: 6%">审核人</th>
												<th style="width: 8%">审核时间</th>
												<th style="width: 7%">审核结果</th>
											</tr>
											<c:forEach items="${stuChangeInfoList}" var="stu"
												varStatus="vs">
												<tr
													<c:if test="${stu.resourceid eq stuchangeinfoid }">id="jumpto" </c:if>>
													<td>${vs.index +1}<c:if
															test="${stu.resourceid eq stuchangeinfoid }">
															<blink>
																<font style="background-color: red" color="yellow">本条</font>
															</blink>
														</c:if></td>
													<td><fmt:formatDate value="${stu.applicationDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>
														${ghfn:dictCode2Val("CodeStudentStatusChange",stu.changeType) }</td>
													<td>${stu.reason}</td>
													<td>
														<!-- 转专业--> <c:if
															test="${stu.changeType eq '23' or stu.changeType eq '12'}">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 																
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.changeBeforeClass.classname}
																</c:if> <!-- 转办学单位 --> <c:if
															test="${empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '82' }">
																【办学单位】${stu.changeBeforeBrSchool.unitName }
																</c:if> <c:if
															test="${not empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '82' }">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.studentInfo.classes.classname}
																</c:if> <!-- 转学习方式 --> <c:if
															test="${empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '81' }">
																【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeLearingStyle)}
																</c:if> <c:if
															test="${not empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '81' }">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 
																【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeTeachingGuidePlan.teachingPlan.schoolType) }<br>
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.changeBeforeClass.classname}
																</c:if> <!-- 有异动前学籍状态记录 --> <c:if
															test="${not empty stu.changeBeforeStudentStatus}">【学籍状态】${ghfn:dictCode2Val('CodeStudentStatus',stu.changeBeforeStudentStatus)}
																</c:if> <c:if test="${ stu.changeType eq 'C_24' }">
																【原学校】${stu.changeBeforeSchoolName}<br> 
																【原层次】${stu.changeBeforeClassicName }<br>
																【原形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeLearingStyle)}<br>
																【原专业】${stu.changeBeforeMajorName }
																【学籍状态】无
																</c:if>
													</td>
													<td>
														<!-- 转专业--> <c:if
															test="${empty stu.changeTeachingGuidePlan and  (stu.changeType eq '23'  or stu.changeType eq '12' )}">
																	【年级】${stu.studentInfo.grade }<br>  																
																	【专业】${stu.changeMajor.majorName }<br> 
																	【层次】${stu.studentInfo.classic.classicName }<br> 
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and  (stu.changeType eq '23'  or stu.changeType eq '12') }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <!-- 转办学单位 --> <c:if
															test="${empty stu.changeTeachingGuidePlan and stu.changeType eq '82' }">
																	【办学单位】${stu.changeBrschool.unitName }
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and stu.changeType eq '82' }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <!-- 转学习方式 --> <c:if
															test="${empty stu.changeTeachingGuidePlan and stu.changeType eq '81' }">
																	【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingType} }
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and stu.changeType eq '81' }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeTeachingGuidePlan.teachingPlan.schoolType) }<br> 
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <c:if test="${ stu.changeType eq 'C_24' }">
																【办学单位】${stu.changeBrschool.unitName}<br> 
																【层次】${stu.changeTeachingGuidePlan.teachingPlan.classic.classicName }<br>
																【形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeTeachingType)}<br>
																【专业】${stu.changeTeachingGuidePlan.teachingPlan.major.majorName }<br>
																【学籍状态】在学<br>
																【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>
																【班级】${stu.changeClass }<br>
														</c:if>
													</td>
													<td>${stu.applicationMan }</td>
													<td><fmt:formatDate value="${stu.applicationDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>${stu.auditMan }</td>
													<td><fmt:formatDate value="${stu.auditDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>${stu.finalAuditStatus == 'Y' ?"<font color='blue'>通过</font>":(stu.finalAuditStatus == 'N' ?"<font color='red'>未通过</font>":"待审核")}</td>
												</tr>
											</c:forEach>
										</table>
									</td>
								</tr>
							</table>
						</div>

					</div>

					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>
			</div>



		</div>
	</div>
</body>
</html>