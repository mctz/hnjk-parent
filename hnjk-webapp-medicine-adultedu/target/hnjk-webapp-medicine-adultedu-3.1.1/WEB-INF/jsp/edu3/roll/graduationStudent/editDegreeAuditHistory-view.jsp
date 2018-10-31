<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑学位审核记录</title>
</head>
<body>
	<h2 class="contentTitle">学位审核记录</h2>
	<div class="page">
		<div class="pageContent">
			<form id="saveDegreeHistory" method="post"
				action="${baseUrl}/edu3/schoolroll/graduation/student/saveDegreeAuditHistory.html"
				onsubmit="return validateCallback(this);" class="pageForm">
				<input type="hidden" name="saveDegreeAuditHistoryId"
					id="saveDegreeAuditHistoryId"
					value="${editDegreeAuditHistory.resourceid}">
				<div class="pageFormContent" layouth="21">
					<!-- tabs -->
					<div class="tabs">
						<div class="tabsContent" style="height: 100%;"
							items="${studentInfo }" var="studentInfo">
							<div class="tabs">
								<div class="tabsHeader">
									<div class="tabsHeaderContent">
										<ul>
											<li><a href="javascript:void(0)"><span>学位审核记录</span></a></li>
										</ul>
									</div>
								</div>
								<!-- 1 -->
								<div>
									<table class="form">
										<tr>
											<td style="width: 20%">学号：</td>
											<td style="width: 30%">${editDegreeAuditHistory.graduateData.studentInfo.studyNo }</td>
											<td style="width: 20%">姓名：</td>
											<td style="width: 30%">${editDegreeAuditHistory.graduateData.studentInfo.studentName }</td>
										</tr>
										<tr>
											<td style="width: 20%">原因：</td>
											<td style="width: 80%" colspan="3">${fn:split(editDegreeAuditHistory.degreeAuditMemo,'|')[0]}</td>
										</tr>
										<tr>
											<td style="width: 20%">实际情况：</td>
											<td style="width: 80%" colspan="3"><input type="hidden"
												name="eduitDegreeHistory0" size="140"
												value="${fn:split(editDegreeAuditHistory.degreeAuditMemo,'|')[0]}" />
												<input type="text" style="width: 90%"
												name="eduitDegreeHistory1" class="required" size="140"
												value="${fn:split(editDegreeAuditHistory.degreeAuditMemo,'|')[1]}" />
												<input type="hidden" name="eduitDegreeHistory2" size="100"
												value="${fn:split(editDegreeAuditHistory.degreeAuditMemo,'|')[2]}" />
											</td>
										</tr>
									</table>
									<div class="formBar">
										<ul>
											<li><div class="buttonActive">
													<div class="buttonContent">
														<button type="submit">保存</button>
													</div>
												</div></li>
											<li><div class="button">
													<div class="buttonContent">
														<button type="button" class="close" href="#close">取消</button>
													</div>
												</div></li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- tabs end-->
				</div>
			</form>
		</div>
	</div>
</body>

</html>