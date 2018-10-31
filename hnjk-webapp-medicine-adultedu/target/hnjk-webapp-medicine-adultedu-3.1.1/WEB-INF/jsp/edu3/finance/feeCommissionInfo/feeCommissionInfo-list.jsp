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
	var operatedate = $("#feeCommissionInfo_operatedate").val();
	iframe.src = "${baseUrl}/edu3/finance/feeCommissionInfo/export.html?brSchool="
		+brSchool+"&operatedate="+operatedate+"&flag=export&isBrschool=${isBrschool}&year=${year}&month=${month}";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
function viewNotPayStudents(unitid){
	var url="${baseUrl}/edu3/finance/feeCommissionInfo/viewNotPay.html";
	if(unitid!=null && unitid!=""){
		url += "?brSchool="+unitid;
	}
	$.pdialog.open(url,"RES_FINANCE_VIEWNOTPAY","未缴费学生信息",{width:900, height:600});
}
function printFeeCommissionInfo() {
	
	var brSchool    = $("#feeCommissionInfo_brSchool").val();
	var operatedate = $("#feeCommissionInfo_operatedate").val();
	var url="${baseUrl}/edu3/finance/feeCommissionInfo/print-view.html?brSchool="
		+brSchool+"&operatedate="+operatedate+"&flag=print&isBrschool=${isBrschool}&year=${year}&month=${month}";
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
						<li><label>操作日期：</label> <input type="text"
							name="operatedate" id="feeCommissionInfo_operatedate"
							style="width: 50%;" class="required Wdate"
							value="${condition['operatedate'] }"
							onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM'})" /></li>
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
							style="text-align: center; vertical-align: middle; width: 2%">序号</th>
						<th rowspan="2" colspan="2"
							style="text-align: center; vertical-align: middle; width: 5%">教学点名称</th>
						<th rowspan="2"
							style="text-align: center; vertical-align: middle; width: 3%">层次</th>
						<th colspan="5"
							style="text-align: center; vertical-align: middle; width: 25%">${year}年${month}月成人高等教育在校学生数(正常注册)（人）</th>
						<th colspan="3"
							style="text-align: center; vertical-align: middle; width: 15%">${year}年学生应收学费金额(元)</th>
						<th colspan="2"
							style="text-align: center; vertical-align: middle; width: 10%">${year}年学生实收学费金额(元)</th>
						<th colspan="4"
							style="text-align: center; vertical-align: middle; width: 20%">未交人数及金额</th>
						<th colspan="4"
							style="text-align: center; vertical-align: middle; width: 20%">学费分成比例及分成金额</th>
					</tr>
					<tr>
						<th rowspan="2" style="display: none; width: 2%">序号</th>
						<th rowspan="2" colspan="2" style="display: none; width: 5%">教学点名称</th>
						<th rowspan="2" style="display: none; width: 3%">层次</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">${year-2}级</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">${year-1}级</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">${year}级</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">小计</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">合计</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">学费标准</th>
						<!-- 10 -->
						<th style="text-align: center; vertical-align: middle; width: 5%">学费全额</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">合计</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">学费全额</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">合计</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">未交（人）</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">合计（人）</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">未交金额（元）</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">合计（元）</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">广东医分成比例</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">分成金额(元)</th>
						<!-- 10 -->
						<th style="text-align: center; vertical-align: middle; width: 5%">教学点分成比例</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">分成金额(元)</th>
					</tr>
				</thead>
			</table>
			<!--  <div style="position:relative;overflow:auto;" layouth="138"> -->
			<!--  <table border="1" style="width: 99.7%;border-bottom-color:#ebf0f5;border-right-color:#ebf0f5;border-collapse: collapse;table-layout:fixed;"> -->
			<table class="table" layouth="138">
				<c:forEach items="${feeCommissionInfo}" var="feeInfo" varStatus="vs">
					<tr style="height: 25px">
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 2%; border-color: black;">${feeInfo.ordinal}</td>
						<td rowspan="2" colspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.unitName}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 3%; border-color: black;">专科</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.gradeNumVo1.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.gradeNumVo2.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.gradeNumVo3.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.studentNumSubtotal}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.studentFullNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.studentFeeRule}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.shouldFees}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.shouldFullFees}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.realFees}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.realFullFees}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.notPayNum}</td>
						<td rowspan="2" style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">
							<a href="#" style="color: blue;"
							onclick="viewNotPayStudents('${feeInfo.unitid}')" title="点击查看"></a>${feeInfo.notPayFullNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.juniorInfo.notFees}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.notFullFees}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.strGDYPro}<%-- <fmt:formatNumber value="${feeInfo.schoolProportion}" pattern="#,#00.0#"/> --%></td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.schoolProportionPay}</td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.strSchoolPro}<%-- <fmt:formatNumber value="${feeInfo.proportion}" pattern="#,#00.0#"/> --%></td>
						<td rowspan="2"
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.proportionPay}</td>
					</tr>
					<tr style="height: 25px">
						<th rowspan="2" style="display: none; width: 3%">序号</th>
						<th rowspan="2" colspan="2"
							style="display: none; width: 5%; border-color: black;">教学点名称</th>
						<td
							style="text-align: center; vertical-align: middle; width: 3%; border-color: black;">本科</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.gradeNumVo1.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.gradeNumVo2.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.gradeNumVo3.studentNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.studentNumSubtotal}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.studentFeeRule}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.shouldFees}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.realFees}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.notPayNum}</td>
						<td
							style="text-align: center; vertical-align: middle; width: 5%; border-color: black;">${feeInfo.universityInfo.notFees}</td>
					</tr>
				</c:forEach>
				<c:if test="${isBrschool eq 'Y'}">
					<%-- <tr><th colspan="19" align="right" style="font-size:13px" height="30">经手人签字：&nbsp;&nbsp;&nbsp;&nbsp; 打印时间：${date}</th></tr> --%>
				</c:if>
				<c:if test="${isBrschool eq 'N'}">
					<tr height="30">
						<td colspan="4"
							style="text-align: center; vertical-align: middle; border-color: black;">总计</td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${studentNum1}</td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${studentNum2}</td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${studentNum3}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumStudentFullNum}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumShouldFullFees}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumRealFullFees}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumNotPayFullNum}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumNotFullFees}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumSchoolProportionPay}</td>
						<td bgcolor="#D0D0D0" style="border-color: black;"></td>
						<td
							style="text-align: center; vertical-align: middle; border-color: black;">${sumProportionPay}</td>
					</tr>
				</c:if>
			</table>
		</div>
	</div>
</body>
</html>