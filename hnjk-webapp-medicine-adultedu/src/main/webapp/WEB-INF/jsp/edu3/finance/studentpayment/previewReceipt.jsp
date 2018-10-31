<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预览收据</title>
<style type="text/css">
#previewReceipt_table table {
	border-collapse: collapse;
	border: none;
	text-align: center;
}

#previewReceipt_table td {
	text-align: center;
	width: 260px;
	height: 40px;
	border: 1px solid #ccc;
}

#previewReceipt_table th {
	text-align: center;
	width: 250px;
	height: 40px;
	border: 1px solid #ccc;
	font-weight: bold;
}

.receiptWordSpacing {
	letter-spacing: 26px;
}

.receiptWordSpacing2 {
	letter-spacing: 83px;
}

.receiptWordSpacing3 {
	letter-spacing: 51px;
}

.receiptWordSpacing4 {
	letter-spacing: 15px;
}

.receiptWordSpacing7 {
	letter-spacing: 9px;
}

.receiptData {
	color: blue;
}

.elementSpacing {
	margin-bottom: 7px;
}
</style>
</head>
<body>
	<div align="center"
		style="margin-left: 8px; font-size: 15px;; width: 800px;"
		id="previewReceipt_main">
		<div style="margin-top: 10px; float: left; width: 780px;">
			<div id="previewReceipt_title"
				style="font-size: 20px; margin-bottom: 20px; font-weight: bold;">广东省高等、中专、成人学校教育收费收据</div>
			<div id="previewReceipt_info">
				<div style="float: left;" align="left">
					<p class="receiptData" style="margin-bottom: 7px;">付款方式：${ghfn:dictCode2Val("CodePaymentMethod",paymentDetail.paymentMethod) }</p>
					<p class="elementSpacing">
						缴款单位（人）：<font class="receiptData">${paymentDetail.checkPayable }</font>
					</p>
					<p class="elementSpacing">
						班别：<font class="receiptData">${paymentDetail.studentInfo.classes.classname }</font>
					</p>
				</div>
				<div style="float: right;" align="right">
					<p style="height: 37px; font-size: 20px;">${paymentDetail.receiptNumber }</p>
					<p>
						<span class="receiptData">${ghfn:getTimeByPattern(paymentDetail.operateDate,"yyyy") }</span>年<span
							class="receiptData">${ghfn:getTimeByPattern(paymentDetail.operateDate,"MM") }</span>月<span
							class="receiptData">${ghfn:getTimeByPattern(paymentDetail.operateDate,"dd") }</span>日
					</p>
				</div>
			</div>
		</div>
		<div id="previewReceipt_table" style="float: left; width: 780px;">
			<table>
				<thead>
					<tr>
						<th class="receiptWordSpacing">收费对象</th>
						<th class="receiptWordSpacing">收费项目</th>
						<th><span class="receiptWordSpacing2">金额</span>（元）</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="receiptWordSpacing">普通大学生<input type="checkbox"
							readonly="readonly" /></td>
						<td style="letter-spacing: 110px; padding-left: 48px;">学费</td>
						<td><span class="receiptData"><fmt:formatNumber
									type="number" pattern="###.##"
									value="${paymentDetail.payAmount }" /></span></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing">普通中专生<input type="checkbox"
							readonly="readonly" /></td>
						<td class="receiptWordSpacing3">考试费</td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing3">研究生<input type="checkbox"
							readonly="readonly" /></td>
						<td class="receiptWordSpacing3">住宿费</td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing">成人大学生<input type="checkbox"
							readonly="readonly" checked="checked" /></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing">成人中专生<input type="checkbox"
							readonly="readonly" /></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing7">夜大、电大、函数生<input
							type="checkbox" readonly="readonly" /></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing2">其他<input type="checkbox"
							readonly="readonly" /></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td class="receiptWordSpacing7">合计人民币（大写）</td>
						<td colspan="2"><span class="receiptData"
							style="margin-right: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"万") }</span>万<span
							class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"仟") }</span>仟<span
							class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"佰") }</span>佰
							<span class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"拾") }</span>拾<span
							class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"元") }</span>元<span
							class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"角") }</span>角
							<span class="receiptData"
							style="margin-right: 10px; margin-left: 10px;">${ghfn:getCNByMonetaryUnit(paymentDetail.payAmount,"分") }</span>分<span
							style="margin-left: 6px;"></span>￥<span class="receiptData"
							style="margin-left: 10px;"><fmt:formatNumber type="number"
									pattern="###.##" value="${paymentDetail.payAmount }" /></span></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="previewReceipt_sign"
			style="float: left; width: 780px; margin-top: 7px;">
			<span style="float: left;">收费单位（盖章）：<br />
			<small>(机打票据，手写无效)</small></span> <span
				style="margin-left: 50px; float: left;">开票人：</span><span
				class="receiptData" style="margin-left: 50px; float: left;">${paymentDetail.operatorName }</span>
			<span style="margin-left: 50px; float: left;">收款人：</span><span
				class="receiptData" style="margin-left: 50px; float: left;">${paymentDetail.operatorName }</span>
			<span style="float: right;">广东省财政厅印制</span>
		</div>
	</div>
</body>
</html>