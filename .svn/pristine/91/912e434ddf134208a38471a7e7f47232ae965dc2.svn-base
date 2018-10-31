<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看学生基本信息</title>

</head>
<body>
	<h2 class="contentTitle">查看学生基本信息</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<!-- tabs -->
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="javascript:void(0)"><span>学生信息</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<!-- 1 -->
						<div>
							<table class="form">
								<tr>
									<td style="width: 20%">学生号：</td>
									<td colspan="3">${studentInfo.studyNo }</td>
									<td colspan="1" rowspan="4"
										style="text-align: center; width: 15%;"><c:choose>
											<c:when
												test="${not empty studentInfo.studentBaseInfo.photoPath }">
												<img width="90" height="126"
													src="${photopathroot }${studentInfo.studentBaseInfo.photoPath}" />
											</c:when>
											<c:otherwise>
												<img width="90" height="126"
													src="${baseUrl }/themes/default/images/img_preview.png" />
											</c:otherwise>
										</c:choose></td>
								</tr>
								<tr>
									<td style="width: 20%">学生姓名：</td>
									<td colspan="3">${studentInfo.studentName }</td>
								</tr>
								<tr>
									<td>年级:</td>
									<td colspan="3">${studentInfo.grade.gradeName }</td>
								</tr>
								<tr>
									<td>层次：</td>
									<td colspan="3">${studentInfo.classic.classicName }</td>
								</tr>
								<tr>
									<td width="20%">教学站:</td>
									<td width="30%">${studentInfo.branchSchool.unitName}</td>
									<td width="20%">专业：</td>
									<td colspan="2">${ studentInfo.major.majorName}</td>
								</tr>
								<tr>
									<td>邮件:</td>
									<td>${studentInfo.studentBaseInfo.email }</td>
									<td>联系电话：</td>
									<td colspan="2">${studentInfo.studentBaseInfo.contactPhone }</td>
								</tr>
								<tr>
									<td>移动电话：</td>
									<td>${studentInfo.studentBaseInfo.mobile }</td>
									<td>联系地址:</td>
									<td colspan="2">${studentInfo.studentBaseInfo.contactAddress }</td>
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