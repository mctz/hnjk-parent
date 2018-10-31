<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费标准</title>
<script type="text/javascript">
	function dereteReasonOnChange(obj){
		if($(obj).val()!=""){
			$("#studentPayment_form_dereteReason").val($(obj).find(":selected").text());
		}		
	}
	</script>
</head>
<body>
	<h2 class="contentTitle">
		<c:choose>
			<c:when test="${isDerate eq 'Y' }">设置减免缴费</c:when>
			<c:otherwise>编辑学生缴费标准</c:otherwise>
		</c:choose>
	</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/studentpayment/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${studentPayment.resourceid }" /> <input type="hidden"
					name="isDerate" value="${isDerate }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">自然年度：</td>
							<td width="30%">${studentPayment.chargeYear }年</td>
							<td width="20%">缴费学期：</td>
							<td width="30%">${studentPayment.yearInfo.yearName }${ghfn:dictCode2Val('CodeTerm',studentPayment.term) }</td>
						</tr>
						<tr>
							<td>学号：</td>
							<td>${studentPayment.studyNo }</td>
							<td>姓名：</td>
							<td>${studentPayment.name }</td>
						</tr>
						<tr>
							<td>学习中心：</td>
							<td>${studentPayment.branchSchool.unitName }</td>
							<td>办学模式：</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }</td>
						</tr>
						<tr>
							<td>年级：</td>
							<td>${studentPayment.grade.gradeName }</td>
							<td>专业：</td>
							<td>${studentPayment.major.majorName }</td>
						</tr>
						<tr>
							<td>层次：</td>
							<td>${studentPayment.classic.classicName }</td>
							<td>缴费期数：</td>
							<td>${studentPayment.chargeTerm }</td>
						</tr>
						<c:choose>
							<c:when test="${isDerate eq 'Y' }">
								<tr>
									<td>应缴金额：</td>
									<td><fmt:formatNumber value='${studentPayment.recpayFee }'
											pattern='####.##' /></td>
									<td>缴费期限：</td>
									<td><fmt:formatDate
											value='${studentPayment.chargeEndDate }' pattern='yyyy-MM-dd' /></td>
								</tr>
								<tr>
									<td>备注:</td>
									<td colspan="3">${studentPayment.memo }</td>
								</tr>
								<tr>
									<td>减免金额：</td>
									<td><input type="text" name="derateFee"
										value="<fmt:formatNumber value='${studentPayment.derateFee }' pattern='####.##' />"
										class="required number" min="0"
										max="<fmt:formatNumber value='${studentPayment.recpayFee }' pattern='####.##' />" /></td>
									<td>减免理由：</td>
									<td><input type="text"
										id="studentPayment_form_dereteReason" name="dereteReason"
										value="${studentPayment.dereteReason }" class="required" /> <gh:select
											dictionaryCode="CodeDereteReason"
											id="studentPayment_form_dereteReason_select" value=""
											onchange="dereteReasonOnChange(this);" /></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>应缴金额：</td>
									<td><input type="text" name="recpayFee"
										value="<fmt:formatNumber value='${studentPayment.recpayFee }' pattern='####.##' />"
										class="required number" min="0" /></td>
									<td>缴费期限：</td>
									<td><input type="text" name="chargeEndDate"
										value="<fmt:formatDate value='${studentPayment.chargeEndDate }' pattern='yyyy-MM-dd'/>"
										class='required' onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
								</tr>
								<tr>
									<td>备注:</td>
									<td colspan="3"><textarea rows="2" cols=""
											style="width: 80%;" name="memo">${studentPayment.memo }</textarea>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
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