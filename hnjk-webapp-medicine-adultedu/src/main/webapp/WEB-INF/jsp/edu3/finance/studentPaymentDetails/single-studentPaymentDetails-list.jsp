<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费明细</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form>
				<input type="hidden" id="StudentPaymentDetails_studentInfoId"
					name="studentInfoId"
					value="${studentPaymentInfo.studentInfo.resourceid }" /> <input
					type="hidden" id="StudentPaymentDetails_stuPaymentId"
					name="stuPaymentId" value="${studentPaymentInfo.resourceid }" />
				<div class="searchBar">
					<ul class="searchContent" style="margin-top: 5px;">
						<li style="margin-left: 10px; height: 30px;"><span
							style="color: #183152;">教学站：</span>${ studentPaymentInfo.studentInfo.branchSchool.unitName}</li>
						<li style="height: 30px;"><span style="color: #183152;">年级${payType}：</span>${ studentPaymentInfo.studentInfo.grade.gradeName}</li>
						<li style="height: 30px;"><span style="color: #183152;">层次：</span>${ studentPaymentInfo.studentInfo.classic.classicName}</li>
					</ul>
					<ul class="searchContent" style="margin-top: 5px; height: 30px;">
						<li style="margin-left: 10px; height: 30px;"><span
							style="color: #183152;">班级：</span>${ studentPaymentInfo.studentInfo.classes.classname}</li>
						<li style="height: 30px;"><span style="color: #183152;">专业：</span>${ studentPaymentInfo.studentInfo.major.majorName}</li>
						<li style="height: 30px;"><span style="color: #183152;">学习形式：</span>${ghfn:dictCode2Val('CodeTeachingType',studentPaymentInfo.studentInfo.teachingType)}</li>
					</ul>
					<ul class="searchContent" style="margin-top: 5px; height: 30px;">
						<li style="margin-left: 10px; height: 30px;"><span
							style="color: #183152;">姓名：</span>${ studentPaymentInfo.studentInfo.studentName}</li>
						<li style="height: 30px;"><span style="color: #183152;">学号：</span>${ studentPaymentInfo.studentInfo.studyNo}</li>
						<li style="height: 30px;"><span style="color: #183152;">缴费状态：</span><font
							color="red">${ghfn:dictCode2Val('CodeChargeStatus',studentPaymentInfo.chargeStatus) }</font></li>
					</ul>
					<ul class="searchContent" style="margin-top: 5px; height: 30px;">
						<li style="margin-left: 10px; height: 30px;"><span
							style="color: #183152;">应缴金额：</span>
						<fmt:formatNumber value="${studentPaymentInfo.recpayFee }"
								pattern="####.##" />元</li>
						<li style="height: 30px;"><span style="color: #183152;">已缴金额：</span>
						<fmt:formatNumber value="${studentPaymentInfo.facepayFee }"
								pattern="####.##" />元</li>
						<li style="height: 30px;"><span style="color: #183152;">欠费金额：</span><font
							color="red"><fmt:formatNumber
									value="${studentPaymentInfo.unpaidFee }" pattern="####.##" />元</font></li>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="206">
				<thead>
					<tr>
						<c:choose>
							<c:when test="${payType=='2' }">
								<th width="12%">票据号</th>
								<th width="12%">终端号</th>
								<th width="12%">pos流水号</th>
								<th width="18%">银行卡号</th>
								<th width="12%">缴费金额</th>
								<th width="10%">收款方式</th>
								<th width="14%">缴费时间</th>
								<th width="12%">经手人</th>
							</c:when>
							<c:otherwise>
								<th width="20%">应缴金额</th>
								<th width="20%">已缴金额</th>
								<th width="20%">欠费金额</th>
								<th width="20%">缴费时间</th>
								<th width="20%">操作人</th>
							</c:otherwise>
						</c:choose>
					</tr>
				</thead>
				<tbody id="studentPaymentDetailsBody">
					<c:forEach items="${studentPaymentDetailsList.result}"
						var="studentPaymentDetails" varStatus="vs">
						<tr>
							<c:choose>
								<c:when test="${payType=='2' }">
									<td title="${studentPaymentDetails.receiptNumber }">${studentPaymentDetails.receiptNumber }</td>
									<td title="${studentPaymentDetails.carrTermNum }">${studentPaymentDetails.carrTermNum }</td>
									<td title="${studentPaymentDetails.posSerialNumber }">${studentPaymentDetails.posSerialNumber }</td>
									<td title="${studentPaymentDetails.carrCardNo }">${studentPaymentDetails.carrCardNo }</td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetails.payAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetails.payAmount }" pattern="####.##" /></td>
									<td
										title="${ghfn:dictCode2Val('CodePaymentMethod',studentPaymentDetails.paymentMethod) }">${ghfn:dictCode2Val("CodePaymentMethod",studentPaymentDetails.paymentMethod) }</td>
								</c:when>
								<c:otherwise>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetails.shouldPayAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetails.shouldPayAmount }"
											pattern="####.##" /></td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetails.paidAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetails.paidAmount }"
											pattern="####.##" /></td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetails.shouldPayAmount-studentPaymentDetails.paidAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetails.shouldPayAmount-studentPaymentDetails.paidAmount  }"
											pattern="####.##" /></td>
								</c:otherwise>
							</c:choose>
							<td
								title="<fmt:formatDate value='${studentPaymentDetails.operateDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${studentPaymentDetails.operateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td title="${studentPaymentDetails.operatorName }">${studentPaymentDetails.operatorName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentPaymentDetailsList}"
				goPageUrl="${baseUrl }/edu3/finance/studentPaymentDetails/single-list.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>