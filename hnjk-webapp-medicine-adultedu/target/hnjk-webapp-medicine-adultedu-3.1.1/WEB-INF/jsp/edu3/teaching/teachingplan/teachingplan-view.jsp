<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划</title>
</head>
<body>
	<h2 class="contentTitle">${teachingPlan.major }-
		${teachingPlan.classic } (${teachingPlan.versionNum})
		${ghfn:dictCode2Val('CodeTeachingType',teachingPlan.schoolType) }</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="#"><span>计划信息</span></a></li>
								<li class="#"><a href="#"><span>课程信息</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<!-- 1 -->
						<div>
							<table class="form">
								<tr>
									<td width="15%">计划名称:</td>
									<td><c:choose>
											<c:when test="${not empty teachingPlan.planName }">
			            			${teachingPlan.planName }
			            		</c:when>
											<c:when test="${not empty teachingPlan.orgUnit }">
			            			${teachingPlan.major } - ${teachingPlan.classic } (${teachingPlan.orgUnit.unitShortName})
			            		</c:when>
											<c:otherwise>
			            			${teachingPlan.major } - ${teachingPlan.classic } (${teachingPlan.versionNum})
			            		</c:otherwise>
										</c:choose></td>
									<td width="15%">办学模式:</td>
									<td width="35%">${ghfn:dictCode2Val('CodeTeachingType',teachingPlan.schoolType) }</td>
								</tr>
								<tr>
									<td width="15%">专业:</td>
									<td width="35%">${teachingPlan.major.majorName }</td>
									<td width="15%">层次:</td>
									<td width="35%">${teachingPlan.classic.classicName }</td>
								</tr>
								<tr>
									<td width="15%">毕业论文申请最低学分:</td>
									<td width="35%">${teachingPlan.applyPaperMinResult }</td>
									<td width="15%">毕业最低学分:</td>
									<td width="35%">${teachingPlan.minResult }</td>
								</tr>
								<tr>
									<td width="15%">选修课修读门数:</td>
									<td width="35%">${teachingPlan.optionalCourseNum }</td>
									<td width="15%">学位授予:</td>
									<td width="35%">${ghfn:dictCode2Val('CodeDegree',teachingPlan.degreeName ) }</td>
								</tr>
								<tr>
									<td>培养目标:</td>
									<td colspan="3">${teachingPlan.trainingTarget }</td>
								</tr>
								<tr>
									<td>学制:</td>
									<td colspan="3">${teachingPlan.eduYear }</td>
								</tr>
								<tr>
									<td>主干课程:</td>
									<td colspan="3">${teachingPlan.mainCourse }</td>
								</tr>
								<tr>
									<td>修读说明:</td>
									<td colspan="3">${teachingPlan.learningDescript }</td>
								</tr>
								<tr>
									<td>备注:</td>
									<td colspan="3">${teachingPlan.memo }</td>
								</tr>
							</table>
						</div>
						<!-- 2 -->
						<div>
							<table class="list" id="courseTab">
								<tr>
									<th style="width: 3%">序号</th>
									<th style="width: 8%">课程类型</th>
									<th style="width: 10%">课程名称</th>
									<th style="width: 10%">课程类别</th>
<!-- 									<th style="width: 6%">教学方式</th> -->
<!-- 									<th style="width: 8%">前置课程名称</th> -->
									<th style="width: 5%">总学时</th>
									<th style="width: 5%">面授学时</th>
									<th style="width: 5%">实验学时</th>
									<th style="width: 5%">学分</th>
									<th style="width: 6%">教材</th>
									<th style="width: 7%">考试类别</th>
									<!-- <th style="width: 7%">成绩类型</th> -->
									<th style="width: 6%">主干课程</th>
									<th style="width: 6%">学位课程</th>
									<th style="width: 7%">建议学期</th>

								</tr>
								<c:forEach items="${teachingPlan.teachingPlanCourses }" var="c"
									varStatus="vs">
									<tr id='tr${c.courseType}'>
										<td>${vs.index +1}</td>
										<td>${ghfn:dictCode2Val('CodeCourseType',c.courseType) }</td>
										<td>${c.course.courseName }</td>
										<td>${ghfn:dictCode2Val('courseNature',c.courseNature) }</td>
<%-- 										<td>${ghfn:dictCode2Val('teachType',c.teachType) }</td> --%>
<%-- 										<td>${c.beforeCourse.courseName }</td> --%>
										<td>${c.stydyHour }</td>
										<td>${c.faceStudyHour }</td>
										<td>${c.experimentPeriod }</td>
										<td>${c.creditHour }</td>
										<%-- <td><a href="${baseUrl }/edu3/sysmanager/textbook/viewTextBook.html?resourceid=${c.resourceid}" target="dialog" title="查看教材信息" width="700" id="teachPlan_viewTextBook">查看</a></td> --%>
										<td style="cursor: pointer;color: blue;" onclick="teachPlanViewTextBook('${c.resourceid}')">查看</td>
										<td>${ghfn:dictCode2Val('CodeExamClassType',c.examClassType) }</td>
										<%-- <td>${ghfn:dictCode2Val('CodeCourseScoreStyle',c.scoreStyle) }</td> --%>
										<td>${ghfn:dictCode2Val('yesOrNo',c.isMainCourse) }</td>
										<td>${ghfn:dictCode2Val('yesOrNo',c.isDegreeCourse) }</td>
										<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>

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

		</div>
	</div>
	<script type="text/javascript">
		function showTextBookFirst(){
			var tbDialog = $("body").data("teachPlan_viewTextBook");
			$.pdialog.switchDialog(tbDialog);
		}
		
		function teachPlanViewTextBook(tbid){
			$.pdialog.open(baseUrl+'/edu3/sysmanager/textbook/viewTextBook.html?resourceid='+tbid,
							'teachPlan_viewTextBook',
							'查看教材信息',
							{width:700,height:300}
							);
			
			window.setTimeout(showTextBookFirst,200);
		}
		
	</script>
</body>
</html>