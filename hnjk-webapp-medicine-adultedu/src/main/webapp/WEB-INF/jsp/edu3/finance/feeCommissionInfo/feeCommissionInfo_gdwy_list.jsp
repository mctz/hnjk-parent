<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学费提成明细管理</title>
    <script type="text/javascript">
        $(document).ready(function(){
        });
        //导出
        function exportFeeCommissionInfo(){
            $('#frame_exportExcel').remove();
            var iframe = document.createElement("iframe");
            iframe.id = "frame_exportExcel";
            var brSchool    = $("#feeCommissionInfo_brSchool").val();
            var beginDate = $("#feeCommissionInfo_beginDate").val();
            var endDate = $("#feeCommissionInfo_endDate").val();
            iframe.src = "${baseUrl}/edu3/finance/feeCommissionInfo/export.html?brSchool="
                +brSchool+"&beginDate="+beginDate+"&endDate="+endDate+"&flag=export&isBrschool=${isBrschool}";
            //创建完成之后，添加到body中
            document.body.appendChild(iframe);
        }

        function printFeeCommissionInfo() {

            var brSchool    = $("#feeCommissionInfo_brSchool").val();
            var beginDate = $("#feeCommissionInfo_beginDate").val();
            var endDate = $("#feeCommissionInfo_endDate").val();
            var url="${baseUrl}/edu3/finance/feeCommissionInfo/print-view.html?brSchool="
                +brSchool+"&beginDate="+beginDate+"&endDate="+endDate+"&flag=print&isBrschool=${isBrschool}";
            $.pdialog.open(url,"RES_FINANCE_FEECOMMISSIONINFO","打印预览",{width:900, height:600});
        }
    </script>
</head>
<body>

<div class="page">
    <div class="pageHeader">
        <form id="studentPayment_search_form"
              onsubmit="return navTabSearch(this);"
              action="${baseUrl }/edu3/finance/studentpayment/feeCommissionInfo.html"
              method="post">
            <div class="searchBar">
                <ul class="searchContent">
                    <c:if test="${isBrschool eq 'N'}">
                        <li class="custom-li"><label>教学点：</label> <gh:brSchoolAutocomplete
                                name="brSchool" tabindex="1" id="feeCommissionInfo_brSchool"
                                defaultValue="${condition['brSchool'] }" displayType="code"
                                style="width:240px;" /></li>
                    </c:if>
                    <c:if test="${isBrschool eq 'Y'}">
                        <input type="hidden" name="brSchool"
                               id="feeCommissionInfo_brSchool" value="${condition['brSchool']}" />
                    </c:if>
                    <li class="custom-li"><label>操作日期：</label>
                        <input type="text"
                                name="beginDate" id="feeCommissionInfo_beginDate"
                                style="width: 80px;" class="required Wdate"
                                value="${condition['beginDate'] }"
                                onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" />
                        至 <input type="text"
                                name="endDate" id="feeCommissionInfo_endDate"
                                style="width: 80px;" class="required Wdate"
                                value="${condition['endDate'] }"
                                onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" />
                    </li>
                </ul>
                <div class="subBar">
                    <ul>
                        <li><div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查 询</button>
                            </div>
                        </div></li>
                    </ul>
                </div>
            </div>
        </form>
    </div>
    <div>
        <gh:resAuth parentCode="RES_FINANCE_FEECOMMISSIONINFO"
                    pageType="list"></gh:resAuth>
        <table class="table">
            <thead>
            <tr>
                <!-- 22 -->
                <th rowspan="2"
                    style="text-align: center; vertical-align: middle; width: 10%">教学点</th>
                <th rowspan="2" style="text-align: center; vertical-align: middle; width: 8%">结算金额合计</th>
                <th colspan="4"
                    style="text-align: center; vertical-align: middle; width: 30%">学费类别</th>
                <th rowspan="2" title="应返学费总金额"
                    style="text-align: center; vertical-align: middle; width: 8%">应返学费总金额</th>
                <th rowspan="2" title="预留比例"
                    style="text-align: center; vertical-align: middle; width: 5%">预留比例</th>
                <th rowspan="2"
                    style="text-align: center; vertical-align: middle; width: 8%">预留金额</th>
                <th rowspan="2" title="第一次已反学费金额（时间段）"
                    style="text-align: center; vertical-align: middle; width: 8%">第一次已反学费金额</th>
                <th rowspan="2" title="第二次已反学费金额（时间段）"
                    style="text-align: center; vertical-align: middle; width: 8%">第二次已反学费金额</th>
                <th rowspan="2" title="本次应返还金额"
                    style="text-align: center; vertical-align: middle; width: 8%">本次应返还金额</th>
                <th rowspan="2"
                    style="text-align: center; vertical-align: middle; width: 5%">市内教学点</th>
            </tr>
            <tr>
                <%--<th rowspan="2" style="display: none; width: 2%">序号</th>--%>
                <th style="text-align: center; vertical-align: middle; width: 10%">非外语类/广州市区教学点</th>
                <th style="text-align: center; vertical-align: middle; width: 5%">分成比例</th>
                <th style="text-align: center; vertical-align: middle; width: 10%">外语类</th>
                <th style="text-align: center; vertical-align: middle; width: 5%">分成比例</th>
            </tr>
            </thead>
        </table>
        <!--  <div style="position:relative;overflow:auto;" layouth="138"> -->
        <!--  <table border="1" style="width: 99.7%;border-bottom-color:#ebf0f5;border-right-color:#ebf0f5;border-collapse: collapse;table-layout:fixed;"> -->
        <table class="table" layouth="138">
            <c:forEach items="${feeCommissionInfo}" var="feeInfo" varStatus="vs">
                <tr style="height: 25px">
                    <%--<td rowspan="2" style="text-align: center; vertical-align: middle; width: 2%; border-color: black;">${feeInfo.ordinal}</td>--%>
                    <td title="${feeInfo.unitName}" style="text-align: center; vertical-align: middle; width: 10%; border-color: black;">
                        ${feeInfo.unitName}
                    </td>
                    <td style="text-align: center; vertical-align: middle; width:8%; border-color: black;">${feeInfo.realFullFees_tuition}</td>
                    <td style="text-align: center; vertical-align: middle; width: 10%; border-color: black;">${feeInfo.typeInfoVo.royaltyRatePay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.typeInfoVo.royaltyRate}%</td>
                    <td style="text-align: center; vertical-align: middle; width: 10%; border-color: black;">${feeInfo.typeInfoVo.royaltyRate2Pay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.typeInfoVo.royaltyRate2}%</td>
                    <td style="text-align: center; vertical-align: middle; width: 8%; border-color: black;">${feeInfo.typeInfoVo.shouldReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.typeInfoVo.reserveRatio}%</td>
                    <td style="text-align: center; vertical-align: middle; width: 8%; border-color: black;">${feeInfo.typeInfoVo.reserveRatioPay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 8%; border-color: black;">${feeInfo.firstReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 8%; border-color: black;">${feeInfo.secondReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; width: 8%; border-color: black;">${feeInfo.shouldReturnFees}</td>
                    <td style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.memo}</td>
                </tr>
            </c:forEach>

            <c:if test="${isBrschool eq 'N'}">
                <tr height="30">
                    <td style="text-align: center; vertical-align: middle; border-color: black;">总计</td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumRealFullFees_tuition}</td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumRoyaltyRatePay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumRoyaltyRate2Pay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumShouldReturnPay}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumReserveRatioPay}</td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumFirstReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumSecondReturnPay}</td>
                    <td style="text-align: center; vertical-align: middle; border-color: black;">${sumShouldReturnFees}</td>
                    <td bgcolor="#D0D0D0" style="border-color: black;"></td>
                </tr>
            </c:if>
        </table>
    </div>
</div>
</body>
</html>