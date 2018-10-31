<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑退补订单信息</title>
</head>
<body>
	<h2 class="contentTitle">编辑退补订单信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="${baseUrl}/edu3/finance/refundback/save.html" class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${refundback.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">学号:</td>
							<td width="35%">${refundback.studyNo }</td>
							<td width="15%">姓名:</td>
							<td width="35%">${refundback.studentInfo.studentName }</td>
						</tr>
						<tr>
							<td width="15%">教学点:</td>
							<td width="35%">${refundback.studentInfo.branchSchool.unitName }</td>
							<td width="15%">年级:</td>
							<td width="35%">${refundback.studentInfo.grade.gradeName }</td>
						</tr>
						<tr>
							<td width="15%">层次:</td>
							<td width="35%">${refundback.studentInfo.classic.classicName }</td>
							<td width="15%">专业:</td>
							<td width="35%">${refundback.studentInfo.major.majorName }"</td>
						</tr>
						<tr>
							<td width="15%">学习形式:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeTeachingType',refundback.studentInfo.teachingType) }</td>
							<td width="15%">学籍状态:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeStudentStatus',refundback.studentInfo.studentStatus) }</td>
						</tr>
						<tr>
							<td width="15%">学年:</td>
							<td width="35%">${refundback.yearInfo.firstYear }</td>
							<td width="15%">异动类型:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeStudentStatusChange',refundback.changeInfo.changeType) }</td>
						</tr>
						<tr>
							<td width="15%">处理类型:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeProcessType',refundback.processType) }</td>
							<td width="15%">收费项:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeChargingItems',refundback.chargingItems) }</td>
						</tr>
						<tr>
							<td width="15%">付款方式:</td>
							<td width="35%">${ghfn:dictCode2Val('CodePaymentMethod',refundback.paymentMethod) }</td>
							<td width="15%">处理状态:</td>
							<td width="35%">${ghfn:dictCode2Val('CodeProcessStatus',refundback.processStatus) }</td>
						</tr>
						<tr>
							<td width="15%">金额:</td>
							<td width="35%">
								<input name="money"  value="${refundback.money }" class="required number"/>
							</td>
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