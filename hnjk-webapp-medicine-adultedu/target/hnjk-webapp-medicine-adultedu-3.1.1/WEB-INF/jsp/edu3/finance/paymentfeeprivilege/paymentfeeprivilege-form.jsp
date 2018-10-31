<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习中心优惠设置</title>
</head>
<body>
	<h2 class="contentTitle">
		<c:if test="${not empty paymentFeePrivilege.recruitMajor.resourceid }">招生专业</c:if>
		学习中心优惠设置
	</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/paymentfeeprivilege/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="paymentFeePrivilegeId"
					value="${paymentFeePrivilege.resourceid }" /> <input type="hidden"
					name="unitId" value="${paymentFeePrivilege.brSchool.resourceid }" />
				<input type="hidden" name="recruitMajorId"
					value="${paymentFeePrivilege.recruitMajor.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<c:choose>
							<%-- 学习中心优惠设置 --%>
							<c:when
								test="${empty paymentFeePrivilege.recruitMajor.resourceid }">
								<tr>
									<td width="20%">学习中心编码:</td>
									<td width="30%">${paymentFeePrivilege.brSchool.unitCode }</td>
									<td width="20%">学习中心名称:</td>
									<td width="30%">${paymentFeePrivilege.brSchool.unitName }</td>
								</tr>
								<tr>
									<td>优惠前每学分学费:</td>
									<td><input type="text" name="beforePrivilegeFee"
										value="<fmt:formatNumber value='${paymentFeePrivilege.beforePrivilegeFee }' pattern='####.##' />"
										min="0" class="required number" /></td>
									<td>优惠后每学分学费:</td>
									<td><input type="text" name="afterPrivilegeFee"
										value="<fmt:formatNumber value='${paymentFeePrivilege.afterPrivilegeFee }' pattern='####.##' />"
										min="0" class="required number" /></td>
								</tr>
							</c:when>
							<%-- 招生专业学习中心优惠设置 --%>
							<c:otherwise>
								<tr>
									<td width="20%">学习中心:</td>
									<td colspan="3">${paymentFeePrivilege.brSchool.unitName }</td>
								</tr>
								<tr>
									<td width="20%">招生批次:</td>
									<td width="30%">${paymentFeePrivilege.recruitMajor.recruitPlan.recruitPlanname }</td>
									<td width="20%">招生专业:</td>
									<td width="30%">${paymentFeePrivilege.recruitMajor.recruitMajorName }</td>
								</tr>
								<tr>
									<td>优惠费总金额:</td>
									<td colspan="3"><input type="text"
										name="totalPrivilegeFee"
										value="<fmt:formatNumber value='${paymentFeePrivilege.totalPrivilegeFee }' pattern='####.##' />"
										min="0" class="required number" /></td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea rows="2" cols="5" name="memo"
									style="width: 80%;">${paymentFeePrivilege.memo }</textarea></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>