<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的教务员</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="" class="pageForm"
				onsubmit="return false;">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>我的教务员</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<c:if test="${not empty currentUser.orgUnit.resourceid }">
									<table class="form">
										<tr>
											<td style="width: 12%">教务员姓名:</td>
											<td style="width: 38%">${currentUser.orgUnit.orgUnitExtends['teachingMan'].exValue}</td>
											<td style="width: 12%">教务员电话:</td>
											<td style="width: 38%">${currentUser.orgUnit.orgUnitExtends['teachingTel'].exValue }</td>
										</tr>
										<tr>
											<td style="width: 12%">教务员E-mail:</td>
											<td style="width: 38%">${currentUser.orgUnit.orgUnitExtends['teachingEmail'].exValue }</td>
											<td style="width: 12%">教务员QQ:</td>
											<td style="width: 38%">${currentUser.orgUnit.orgUnitExtends['teachingQQ'].exValue }</td>
										</tr>
									</table>
								</c:if>
							</div>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>