<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<%@ page language="java"
	import="java.util.*,com.hnjk.edu.finance.vo.*,java.net.URLEncoder"
	pageEncoding="UTF-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学费提成明细管理</title>
</head>
<body>
	<div class="pageContent">
		<table border="1">
			<thead>
				<tr>
					<th colspan="21" align="center" style="font-size: 20px" height="50">${title}</th>
				</tr>
				<c:if test="${isBrschool eq 'Y'}">
					<tr>
						<th colspan="21" align="left" style="font-size: 15px" height="30">教学点名称（盖章）：${userBrSchool}</th>
					</tr>
				</c:if>
				<tr>
					<!-- <th rowspan="2" width="2%" align="center"><input type="checkbox" name="checkall" id="check_all_studentPayment" onclick="checkboxAll('#check_all_studentPayment','resourceid','#feeCommissionBody')"/></th> -->
					<th rowspan="2" width="5%" align="center">序号</th>
					<th rowspan="2" width="10%" align="center">教学点名称</th>
					<th rowspan="2" width="5%" align="center">层次</th>
					<th colspan="5" width="25%" align="center">${year}年${month}月成人高等教育在校学生数(正常注册)（人）</th>
					<th colspan="3" width="15%" align="center">${year}年学生应收学费金额(元)</th>
					<th colspan="2" width="10%" align="center">${year}年学生实收学费金额(元)</th>
					<th colspan="4" width="20%" align="center">未交人数及金额</th>
					<th colspan="4" width="20%"
						style="text-align: center; vertical-align: middle;">学费分成比例及分成金额</th>
				</tr>
				<tr>
					<th align="center" width="5%">${year-2}级</th>
					<th align="center" width="5%">${year-1}级</th>
					<th align="center" width="5%">${year}级</th>
					<th align="center" width="5%">小计</th>
					<th align="center" width="5%">合计</th>
					<th align="center" width="5%">学费标准</th>
					<th align="center" width="5%">学费全额</th>
					<th align="center" width="5%">合计</th>
					<th align="center" width="5%">学费全额</th>
					<th align="center" width="5%">合计</th>
					<th align="center" width="5%">未交（人）</th>
					<th align="center" width="5%">合计（人）</th>
					<th align="center" width="5%">未交金额（元）</th>
					<th align="center" width="5%">合计（元）</th>
					<th align="center" width="5%">广东医分成比例</th>
					<th align="center" width="5%">分成金额(元)</th>
					<th align="center" width="5%">教学点分成比例</th>
					<th align="center" width="5%">分成金额(元)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${feeCommissionInfo}" var="feeInfo" varStatus="vs">
					<tr>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle;">${feeInfo.ordinal}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle;">${feeInfo.unitName}</td>
						<td style="text-align: center; vertical-align: middle;">专科</td>
						<td class="numForExcel">${feeInfo.juniorInfo.gradeNumVo1.studentNum}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.gradeNumVo2.studentNum}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.gradeNumVo3.studentNum}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.studentNumSubtotal}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.studentFullNum}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.studentFeeRule}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.shouldFees}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.shouldFullFees}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.realFees}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.realFullFees}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.notPayNum}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.notPayFullNum}</td>
						<td class="numForExcel">${feeInfo.juniorInfo.notFees}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.notFullFees}</td>
						<td rowspan="2" style="text-align: center; vertical-align: middle;">${feeInfo.schoolProportion}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.schoolProportionPay}</td>
						<td rowspan="2" style="text-align: center; vertical-align: middle;">${feeInfo.proportion}</td>
						<td rowspan="2" class="numForExcel">${feeInfo.proportionPay}</td>

					</tr>
					<tr>
						<td style="text-align: center; vertical-align: middle;">本科</td>
						<td class="numForExcel">${feeInfo.universityInfo.gradeNumVo1.studentNum}</td>
						<td class="numForExcel">${feeInfo.universityInfo.gradeNumVo2.studentNum}</td>
						<td class="numForExcel">${feeInfo.universityInfo.gradeNumVo3.studentNum}</td>
						<td class="numForExcel">${feeInfo.universityInfo.studentNumSubtotal}</td>
						<td class="numForExcel">${feeInfo.universityInfo.studentFeeRule}</td>
						<td class="numForExcel">${feeInfo.universityInfo.shouldFees}</td>
						<td class="numForExcel">${feeInfo.universityInfo.realFees}</td>
						<td class="numForExcel">${feeInfo.universityInfo.notPayNum}</td>
						<td class="numForExcel">${feeInfo.universityInfo.notFees}</td>
					</tr>
				</c:forEach>
				<c:if test="${isBrschool eq 'Y'}">
					<tr>
						<th colspan="21" align="right" style="font-size: 13px" height="30">经手人签字：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							打印时间：${date}</th>
					</tr>
				</c:if>
				<c:if test="${isBrschool eq 'N'}">
					<tr height="30" style="font-size: 15px" align="center">
						<!-- <th bgcolor="#D0D0D0"></th> -->
						<th colspan="3">总计</th>
						<!-- <th bgcolor="#D0D0D0"></th> -->
						<th style="vnd .ms-excel.numberformat: #,##0">${studentNum1}</th>
						<th style="vnd .ms-excel.numberformat: #,##0">${studentNum2}</th>
						<th style="vnd .ms-excel.numberformat: #,##0">${studentNum3}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0">${sumStudentFullNum}</th>
						<th bgcolor="#D0D0D0"></th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0 .00">${sumShouldFullFees}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0 .00">${sumRealFullFees}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0">${sumNotPayFullNum}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0 .00">${sumNotFullFees}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0 .00">${sumSchoolProportionPay}</th>
						<th bgcolor="#D0D0D0"></th>
						<th style="vnd .ms-excel.numberformat: #,##0 .00">${sumProportionPay}</th>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</body>
</html>