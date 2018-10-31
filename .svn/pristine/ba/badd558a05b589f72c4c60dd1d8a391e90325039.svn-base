<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收费汇总表</title>

</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="188" border="1">
				<thead>
					<tr>
						<c:choose>
							<c:when test="${chargeBack eq 'Y' }">
								<th rowspan="2" width="10%" align="center" width="5%">退款方式</th>
							</c:when>
							<c:otherwise>
								<th rowspan="2" width="10%" align="center" width="5%">缴费方式</th>
							</c:otherwise>
						</c:choose>
						<th rowspan="2" width="10%" align="center" width="5%">总笔数</th>
						<th rowspan="2" width="10%" align="center" width="5%">总人数</th>
						<th colspan="3" width="30%" align="center" width="5%">学费</th>
						<th rowspan="2" width="10%" align="center" width="5%">开始票据号</th>
						<th rowspan="2" width="10%" align="center" width="5%">结束票据号</th>
						<th rowspan="2" width="10%" align="center" width="5%">开始时间</th>
						<th rowspan="2" width="10%" align="center" width="5%">结束时间</th>
					</tr>
					<tr>
						<th align="center" width="5%" width="10%">本科生</th>
						<th align="center" width="5%" width="10%">专科生</th>
						<th align="center" width="5%" width="10%">合计</th>
					</tr>
				</thead>
				<tbody id="chargeSummaryBody">
					<c:forEach items="${summaryList}" var="summary" varStatus="vs">
						<tr>
							<td align="center" width="5%">${summary.paymentMethod }</td>
							<td align="center" width="5%">${summary.totalNum }</td>
							<td align="center" width="5%">${summary.stuCount }</td>
							<td align="center" width="5%">${summary.undergraduateFee }</td>
							<td align="center" width="5%">${summary.educationFee }</td>
							<td align="center" width="5%">${summary.totalFee }</td>
							<td align="center" width="5%"><c:if
									test="${!empty summary.beginReceiptNum}">${summary.beginReceiptNum }</c:if></td>
							<td align="center" width="5%"><c:if
									test="${!empty summary.endReceiptNum}">${summary.endReceiptNum }</c:if>
							</td>
							<td align="center" width="5%"><fmt:formatDate
									value="${summary.beginDate }" pattern="yyyy-MM-dd" /></td>
							<td align="center" width="5%"><fmt:formatDate
									value="${summary.endDate }" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
					<tr>
						<td align="center" width="5%">合计：</td>
						<td align="center" width="5%">${sumPaidCount }</td>
						<td align="center" width="5%">${sumStuCount }</td>
						<td align="center" width="5%">${sumBKAmount }</td>
						<td align="center" width="5%">${sumZKAmount }</td>
						<td align="center" width="5%">${sumAmount }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>