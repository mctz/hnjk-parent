<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍修改</title>
<style type="text/css">
#studentInfo_stuFactFee td {
	text-align: center;
}

#studentInfo_stuFactFee th {
	text-align: center;
}
</style>
</head>
<body>
	<h2 class="contentTitle">查看学籍</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<!-- tabs -->
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="javascript:void(0)"><span>学籍信息</span></a></li>
								<li><a href="javascript:void(0)"><span>基本信息</span></a></li>
								<li><a href="javascript:void(0)"><span>学分信息</span></a></li>
								<li><a href="javascript:void(0)"><span>学费信息</span></a></li>
								<c:if test="${studentInfo.classic.classicName eq '大专起点本科'}">
									<li><a href="javascript:void(0)"><span>专科资格</span></a></li>
								</c:if>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<div>
							<table class="form">
								<tr>
									<td style="width: 20%">学生号：</td>
									<td style="width: 30%">${studentInfo.studyNo }</td>
									<td style="width: 20%">考生号：</td>
									<td style="width: 30%">${studentInfo.enrolleeCode }</td>
								</tr>
								<tr>
									<td>学生姓名：</td>
									<td>${studentInfo.studentName }</td>
									<td>注册号：</td>
									<td>${studentInfo.registorNo }</td>
								</tr>
								<tr>
									<td>年级:</td>
									<td>${studentInfo.grade.gradeName }</td>
									<td>层次：</td>
									<td>${studentInfo.classic.classicName }</td>
								</tr>
								<tr>
									<td>进修性质:</td>
									<td>${ghfn:dictCode2Val('CodeAttendAdvancedStudies',studentInfo.attendAdvancedStudies) }</td>
									<td>学习形式：</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',studentInfo.teachingType) }</td>
								</tr>
								<tr>
									<td>教学站:</td>
									<td>${studentInfo.branchSchool.unitName}</td>
									<td>专业：</td>
									<td>${ studentInfo.major.majorName}</td>
								</tr>
								<tr>
									<td>就读方式:</td>
									<td>${ghfn:dictCode2Val('CodeStudyInSchool',studentInfo.studyInSchool ) }</td>
									<td>学习类别：</td>
									<td>${studentInfo.studentKind }</td>
								</tr>
								<tr>
									<td>学籍状态:</td>
									<td>${ghfn:dictCode2Val('CodeStudentStatus',studentInfo.studentStatus ) }</td>
									<td>在学状态:</td>
									<td>${ghfn:dictCode2Val('CodeLearingStatus',studentInfo.learingStatus  ) }</td>
								</tr>
								<tr>
									<td>入学资格审核:</td>
									<td><c:choose>
											<c:when test="${studentInfo.enterAuditStatus=='N'}">
												<font color="red"><b>${ghfn:dictCode2Val('CodeAuditStatus',studentInfo.enterAuditStatus)}</b></font>
											</c:when>
											<c:otherwise>
								${ghfn:dictCode2Val('CodeAuditStatus',studentInfo.enterAuditStatus)}
							</c:otherwise>
										</c:choose></td>
									<td>特殊学生(是否跟读)</td>
									<td>${ghfn:dictCode2Val('yesOrNo',studentInfo.isStudyFollow)}</td>
								</tr>
								<tr>
									<td>学制:</td>
									<td>${studentInfo.teachingPlan.eduYear }年</td>
									<td>班级:</td>
									<td>${studentInfo.classes.classname  }</td>
								</tr>
								<tr>
									<td>自我鉴定:</td>
									<td colspan="3"><textarea rows="8" style="width: 99%"
											readonly="readonly"> ${studentInfo.selfAssessment } </textarea></td>
								</tr>
								<tr>
									<td>备注:</td>
									<td colspan="3">${studentInfo.memo }</td>
								</tr>
							</table>
						</div>
						<!-- 2 -->
						<div>
							<table class="form">
								<tr>
									<td style="width: 20%">姓名:</td>
									<td style="width: 30%">${studentInfo.studentBaseInfo.name }</td>
									<td style="width: 16%" rowspan="5">照片:<br>第一张为录取照片<br>第二张为学籍照片
									</td>
									<td style="width: 17%" rowspan="5"><c:choose>
											<c:when
												test="${not empty studentInfo.studentBaseInfo.recruitPhotoPath }">
												<img
													src="${rootUrl}common/students/${studentInfo.studentBaseInfo.recruitPhotoPath}"
													width="90" height="126" />
											</c:when>
											<c:otherwise>
												<img src="${baseUrl}/themes/default/images/img_preview.png"
													width="90" height="126" />
											</c:otherwise>
										</c:choose></td>
									<td style="width: 17%" rowspan="5"><c:choose>
											<c:when
												test="${not empty studentInfo.studentBaseInfo.photoPath }">
												<img
													src="${rootUrl}common/students/${studentInfo.studentBaseInfo.photoPath}"
													width="90" height="126" />
											</c:when>
											<c:otherwise>
												<img src="${baseUrl}/themes/default/images/img_preview.png"
													width="90" height="126" />
											</c:otherwise>
										</c:choose></td>
								</tr>
								<tr>
									<td>曾用名:</td>
									<td>${studentInfo.studentBaseInfo.nameUsed }</td>
								</tr>
								<tr>
									<td>性别:</td>
									<td>${ghfn:dictCode2Val("CodeSex",studentInfo.studentBaseInfo.gender) }</td>
								</tr>
								<tr>
									<td>证件类别:</td>
									<td>${ghfn:dictCode2Val("CodeCertType",studentInfo.studentBaseInfo.certType) }</td>
								</tr>

								<tr>
									<td>证件号码：</td>
									<td>${studentInfo.studentBaseInfo.certNum }</td>
								</tr>
								<tr>
									<td>联系地址:</td>
									<td>${studentInfo.studentBaseInfo.contactAddress }</td>
									<td>联系邮编：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.contactZipcode }</td>
								</tr>
								<tr>
									<td>联系电话:</td>
									<td>${studentInfo.studentBaseInfo.contactPhone }</td>
									<td>移动电话：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.mobile }</td>
								</tr>
								<tr>
									<td>邮件:</td>
									<td>${studentInfo.studentBaseInfo.email }</td>
									<td>个人主页：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.homePage }</td>
								</tr>
								<tr>
									<td>身高:</td>
									<td>${studentInfo.studentBaseInfo.height }</td>
									<td>血型：</td>
									<td colspan="2">${ghfn:dictCode2Val("CodeBloodStyle",studentInfo.studentBaseInfo.bloodType) }</td>
								</tr>
								<tr>
									<td>出生日期:</td>
									<td><fmt:formatDate
											value="${studentInfo.studentBaseInfo.bornDay }"
											pattern="yyyy-MM-dd" /></td>
									<td>出生地:</td>
									<td colspan="2">${studentInfo.studentBaseInfo.bornAddress }</td>
								</tr>
								<tr>
									<td>国籍:</td>
									<td>${ghfn:dictCode2Val("CodeCountry",studentInfo.studentBaseInfo.country) }</td>
									<td>籍贯:</td>
									<td colspan="2">${studentInfo.studentBaseInfo.homePlace }</td>
								</tr>
								<tr>
									<td>港澳侨代码:</td>
									<td>${ghfn:dictCode2Val("CodeGAQ",studentInfo.studentBaseInfo.gaqCode) }</td>
									<td>民族:</td>
									<td colspan="2">${ghfn:dictCode2Val("CodeNation",studentInfo.studentBaseInfo.nation) }</td>
								</tr>
								<tr>
									<td>身体健康状态:</td>
									<td>${ghfn:dictCode2Val("CodeHealth",studentInfo.studentBaseInfo.health ) }</td>
									<td>婚姻状况:</td>
									<td colspan="2">${ghfn:dictCode2Val("CodeMarriage",studentInfo.studentBaseInfo.marriage ) }</td>
								</tr>
								<tr>
									<td>政治面貌:</td>
									<td>${ghfn:dictCode2Val("CodePolitics",studentInfo.studentBaseInfo.politics ) }
									</td>
									<td>宗教信仰:</td>
									<td colspan="2">${studentInfo.studentBaseInfo.faith }</td>
								</tr>
								<tr>
									<td>户口性质:</td>
									<td>${ghfn:dictCode2Val("CodeRegisteredResidenceKind",studentInfo.studentBaseInfo.residenceKind  ) }
									</td>
									<td>户口所在地：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.residence }</td>
								</tr>
								<tr>
									<td>现住址:</td>
									<td>${studentInfo.studentBaseInfo.currenAddress }</td>
									<td>家庭住址：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.homeAddress }</td>
								</tr>
								<tr>
									<td>家庭住址邮编:</td>
									<td>${studentInfo.studentBaseInfo.homezipCode }</td>
									<td>家庭电话：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.homePhone }</td>
								</tr>
								<tr>
									<td>公司名称:</td>
									<td>${studentInfo.studentBaseInfo.officeName }</td>
									<td>公司电话：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.officePhone }</td>
								</tr>
								<tr>
									<td>职务职称:</td>
									<td>${studentInfo.studentBaseInfo.title }</td>
									<td>特长：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.specialization }</td>
								</tr>

								<tr>
									<td>备注:</td>
									<td colspan="4">${studentInfo.studentBaseInfo.memo }</td>
								</tr>
							</table>
						</div>
						<!-- 3 -->
						<div>
							<table class="form">
								<tr>
									<td style="width: 20%">教学计划名称:</td>
									<%-- <td style="width: 30%">${studentInfo.teachingPlan.major }
										- ${studentInfo.teachingPlan.classic }
										(${studentInfo.teachingPlan.versionNum})</td> --%>
									<td style="width: 30%" title="${studentInfo.teachingPlan.major} - ${studentInfo.teachingPlan.classic} (${studentInfo.teachingPlan.versionNum})">
										${studentInfo.teachingPlan.planName}</td>
									<td style="width: 20%">办学模式:</td>
									<td style="width: 30%">${ghfn:dictCode2Val('CodeTeachingType',studentInfo.teachingPlan.schoolType) }</td>
								</tr>
								<tr>
									<td width="15%">毕业论文申请最低学分:</td>
									<td width="35%">${studentInfo.teachingPlan.applyPaperMinResult }</td>
									<td width="15%">毕业最低学分:</td>
									<td width="35%">${studentInfo.teachingPlan.minResult }</td>
								</tr>
								<tr>
									<td width="15%">选修课修读门数:</td>
									<td width="35%">${studentInfo.teachingPlan.optionalCourseNum }</td>
									<td width="15%">学位授予:</td>
									<td width="35%">${ghfn:dictCode2Val('CodeDegree',studentInfo.teachingPlan.degreeName ) }</td>
								</tr>
								<tr>
									<td width="15%">取得总学分:</td>
									<td width="35%">${studentInfo.finishedCreditHour }</td>
									<td width="15%">取得必修学分:</td>
									<td width="35%">${studentInfo.finishedNecessCreditHour }</td>
								</tr>
								<tr>
									<td width="15%">结业最低学分:</td>
									<td colspan="3">${studentInfo.teachingPlan.theGraduationScore }</td>
								</tr>
							</table>
							<div
								style="padding-top: 2px; padding-bottom: 2px; font-weight: bold"}">修读情况:</div>
							<table class="list" id="courseTab">
								<tr>
									<th style="width: 5%">序号</th>
									<th style="width: 10%">课程类型</th>
									<th style="width: 15%">课程名称</th>
									<th style="width: 10%">课程类别</th>
									<th style="width: 8%">学时</th>
									<th style="width: 8%">学分</th>
									<th style="width: 10%">成绩</th>
									<!-- <th style="width:10%">学位统考课</th> -->
									<th style="width: 7%">主干课</th>
									<th style="width: 15%">建议学期</th>
									<th style="width: 15%">备注</th>
								</tr>
								<%int index = 1;%>
								<c:forEach
									items="${studentInfo.teachingPlan.teachingPlanCourses }"
									var="c" varStatus="vs">
									<tr id='tr${c.courseType}'>
										<td><%=index++ %></td>
										<td>${ghfn:dictCode2Val('CodeCourseType',c.courseType) }</td>
										<td>${c.course.courseName }</td>
										<td>${ghfn:dictCode2Val('courseNature',c.courseNature) }</td>
										<td>${c.stydyHour }</td>
										<td>${c.creditHour }</td>
										<td><c:forEach items="${examResults }" var="result">
												<c:if test="${ result.courseId eq c.course.resourceid}">
													<c:choose>
														<c:when test="${result.isNoExam eq 'Y' }">${result.checkStatus }</c:when>
														<c:when test="${not empty result.examResultsChs}">
							            			${result.examResultsChs }
							            		</c:when>
														<c:otherwise>
							            			${result.integratedScore}
							            		</c:otherwise>
													</c:choose>
													<br />
												</c:if>
											</c:forEach></td>
										<td>${ghfn:dictCode2Val('yesOrNo',c.isMainCourse) }</td>
										<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>
										<td>${c.memo }</td>
									</tr>

								</c:forEach>
								<c:forEach items="${examResults }" var="result" varStatus="vs">
									<c:choose>
										<c:when
											test="${fn:length(examResults) == (vs.index+1) or !fn:startsWith(result.resourceid,'elective_') }">
										</c:when>
										<c:otherwise>
											<tr>
												<td><%=index++ %></td>
												<c:choose>
													<c:when test="${not empty result.courseType }">
														<td>${result.courseType }</td>
													</c:when>
													<c:otherwise>
														<td>${ghfn:dictCode2Val('CodeCourseType',result.courseTypeCode)}</td>
													</c:otherwise>
												</c:choose>
												<td>${result.courseName }</td>
												<td>${ghfn:dictCode2Val('courseNature',result.courseNature) }</td>
												<td>${result.stydyHour }</td>
												<td>${result.inCreditHour }</td>
												<c:choose>
													<c:when test="${not empty result.examResultsChs}">
														<td>${result.examResultsChs }</td>
													</c:when>
													<c:otherwise>
														<td>${result.integratedScore}</td>
													</c:otherwise>
												</c:choose>
												<td>否</td>
												<td>${ghfn:dictCode2Val('CodeTermType',result.courseTerm) }</td>
												<td>选修课成绩</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</table>
						</div>
						<!-- 4 -->
						<div>
							<c:choose>
								<c:when test="${ not empty studentPayments}">
									<table class="list" width="100%;" id="studentInfo_stuFactFee">
										<tr>
											<th style="width: 10%">序号</th>
											<th style="width: 15%">应缴金额</th>
											<th style="width: 15%">实缴金额</th>
											<th style="width: 15%">欠费金额</th>
											<th style="width: 15%">减免金额</th>
											<th style="width: 10%">缴费情况</th>
											<th style="width: 20%">缴费截止日期</th>
										</tr>
										<c:set var="totalFactFee" value="0.0" />
										<c:forEach items="${studentPayments }" var="studentPayment"
											varStatus="vs">
											<tr>
												<td>${vs.index +1}</td>
												<td>${studentPayment.recpayFee }</td>
												<td>${studentPayment.facepayFee }</td>
												<td>${studentPayment.unpaidFee }</td>
												<td>${studentPayment.derateFee }</td>
												<td>${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargeStatus) }</td>
												<td><fmt:formatDate
														value="${studentPayment.chargeEndDate }"
														pattern="yyyy-MM-dd" /></td>
											</tr>
										</c:forEach>
										<!--	
								<tr>													
									<td colspan="4">
									<div style="text-align: right;margin-right: 50px;">
									合计：  ${totalFactFee}元
									</div>
									</td>	
									<td colspan="2"></td>												
								</tr>	
							
							-->
									</table>
								</c:when>
								<c:otherwise>
									<font color="red"><b>没有缴费信息！</b></font>
								</c:otherwise>
							</c:choose>

						</div>
						<!-- 5 -->
						<div>
							<table class="form"
								<c:if test="${studentInfo.classic.classicName ne '大专起点本科'}">style="display:none;" </c:if>>
								<tr id="gSchoolInfoTr">
									<td width="12%">毕业院校:</td>
									<td width="38%">${studentInfo.graduateSchool }</td>
									<td width="12%">毕业学校代码:</td>
									<td width="38%">${studentInfo.graduateSchoolCode }"</td>
								</tr>
								<tr id="graduateInfoTr">
									<td width="12%">毕业证书号:</td>
									<td width="38%">${studentInfo.graduateId }</td>
									<td width="12%">毕业专业:</td>
									<td width="38%">${studentInfo.graduateMajor }</td>
								</tr>
								<tr>
									<td width="12%">毕业日期:</td>
									<td width="38%">${studentInfo.graduateDate}</td>
									<td width="12%"></td>
									<td width="38%"></td>
								</tr>
							</table>

						</div>
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