<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学费提成明细管理</title>
</head>
<body>
    <div class="page">
        <table class="table" border="1">
            <thead>
            <tr>
                <th colspan="13" align="center" style="font-size: 20px" height="50">各成教教学点学费分成明细表（${beginDate} 至 ${endDate}）</th>
            </tr>
            <tr>
                <!-- 22 -->
                <%--  <th rowspan="2"
                      style="text-align: center; vertical-align: middle; width: 2%">序号</th>--%>
                <th rowspan="2"
                    style="text-align: center; vertical-align: middle; width: 10%">教学点</th>
                <th rowspan="2" class="numForExcel">结算金额合计</th>
                <th colspan="4" class="numForExcel">学费类别</th>
                <th rowspan="2" title="应返学费总金额" class="numForExcel">应返学费总金额</th>
                <th rowspan="2" class="numForExcel">预留比例</th>
                <th rowspan="2" class="numForExcel">预留金额</th>
                <th rowspan="2" title="第一次已反学费金额" class="numForExcel">第一次已反学费<br>金额</th>
                <th rowspan="2" title="第二次已反学费金额" class="numForExcel">第二次已反学费<br>金额</th>
                <th rowspan="2" title="本次应返还金额" class="numForExcel">本次应返还金额</th>
                <th rowspan="2" class="numForExcel">市内教学点</th>
            </tr>
            <tr>
                <%--<th rowspan="2" style="display: none; width: 2%">序号</th>--%>
                <th style="text-align: center; vertical-align: middle; width: 10%">非外语类/广州市区教学点</th>
                <th  class="numForExcel">分成比例</th>
                <th style="text-align: center; vertical-align: middle; width: 10%">外语类</th>
                <th  class="numForExcel">分成比例</th>
            </tr>
            </thead>
        </table>

        <table class="table" border="1">
            <c:forEach items="${feeCommissionInfo}" var="feeInfo" varStatus="vs">
                <tr style="height: 25px">
                        <%--<td rowspan="2" style="text-align: center; vertical-align: middle; width: 2%; border-color: black;">${feeInfo.ordinal}</td>--%>
                    <td title="${feeInfo.unitName}" class="numForExcel">
                            ${feeInfo.unitName}
                    </td>
                    <td class="numForExcel">${feeInfo.realFullFees_tuition}</td>
                    <td class="numForExcel">${feeInfo.typeInfoVo.royaltyRatePay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%;">${feeInfo.typeInfoVo.royaltyRate}%</td>
                    <td class="numForExcel">${feeInfo.typeInfoVo.royaltyRate2Pay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%;">${feeInfo.typeInfoVo.royaltyRate2}%</td>
                    <td class="numForExcel">${feeInfo.typeInfoVo.shouldReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%;">${feeInfo.typeInfoVo.reserveRatio}%</td>
                    <td class="numForExcel">${feeInfo.typeInfoVo.reserveRatioPay}</td>
                    <td class="numForExcel">${feeInfo.firstReturnPay}</td>
                    <td class="numForExcel">${feeInfo.secondReturnPay}</td>
                    <td class="numForExcel">${feeInfo.shouldReturnFees}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.memo}</td>
                </tr>
            </c:forEach>

            <c:if test="${isBrschool eq 'N'}">
                <tr height="30">
                    <td style="text-align: center; vertical-align: middle; border-color: black;">总计</td>
                    <td class="numForExcel">${sumRealFullFees_tuition}</td>
                    <td class="numForExcel">${sumRoyaltyRatePay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td class="numForExcel">${sumRoyaltyRate2Pay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td class="numForExcel">${sumShouldReturnPay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td class="numForExcel">${sumReserveRatioPay}</td>
                    <td class="numForExcel">${sumFirstReturnPay}</td>
                    <td class="numForExcel">${sumSecondReturnPay}</td>
                    <td class="numForExcel">${sumShouldReturnFees}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>


                </tr>
            </c:if>
        </table>
    </div>
</body>
</html>