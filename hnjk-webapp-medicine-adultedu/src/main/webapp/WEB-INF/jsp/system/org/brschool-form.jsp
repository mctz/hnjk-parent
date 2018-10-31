<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点信息管理</title>
</head>
<body>
	<h2 class="contentTitle">站点信息管理</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/org/brschool/save.html"
				class="pageForm" id="unitForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>站点信息管理</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<c:if test="${not empty orgunit.resourceid }">
									<table class="form">
										<tr>
											<td style="width: 12%">站点名称:</td>
											<td style="width: 38%">${orgunit.unitName }</td>
											<td style="width: 12%">站点编码:</td>
											<td style="width: 38%">${orgunit.unitCode }</td>
										</tr>
										<tr>
											<td style="width: 12%">站点地址:</td>
											<td colspan="3"><input type="text"
												value="${orgunit.address }" name="address"
												style="width: 50%;" class="required" /></td>
										</tr>
										<tr>
											<td style="width: 12%">负责人姓名:</td>
											<td><input type="text" value="${orgunit.principal }"
												name="principal" style="width: 50%;"  /></td>
											<td style="width: 12%">负责人电话:</td>
											<td><input type="text" value="${orgunit.contectCall }"
												name="contectCall" style="width: 50%;"
												class="phone" /></td>
										</tr>
										<tr>
											<td style="width: 12%">教务员姓名:</td>
											<td><input type="text"
												value="${orgunit.orgUnitExtends['teachingMan'].exValue}"
												name="teachingMan" style="width: 50%;" class="required" /></td>
											<td style="width: 12%">教务员电话:</td>
											<td><input type="text"
												value="${orgunit.orgUnitExtends['teachingTel'].exValue }"
												name="teachingTel" style="width: 50%;"
												class="required phone" /></td>
										</tr>
										<tr>
											<td style="width: 12%">教务员E-mail:</td>
											<td><input type="text"
												value="${orgunit.orgUnitExtends['teachingEmail'].exValue }"
												name="teachingEmail" style="width: 50%;"
												class="required email" /></td>
											<td style="width: 12%">教务员QQ:</td>
											<td><input type="text"
												value="${orgunit.orgUnitExtends['teachingQQ'].exValue }"
												name="teachingQQ" style="width: 50%;" /></td>
										</tr>
										<tr>
											<td style="width: 12%">专业主任姓名:</td>
											<td colspan="3"><input type="text"
												value="${orgunit.orgUnitExtends['majorDirector'].exValue }"
												name="majorDirector" style="width: 50%;" /></td>
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
				<c:if test="${not empty orgunit.resourceid }">
					<div class="formBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">提 交</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close"
											onclick="navTab.closeCurrentTab();">关 闭</button>
									</div>
								</div></li>
						</ul>
					</div>
				</c:if>
			</form>
		</div>
	</div>
</body>
</html>