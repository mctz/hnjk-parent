<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退费汇总表</title>
<script type="text/javascript">
$(document).ready(function(){
	chargeBackQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function chargeBackQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'chargeback_brSchool',gradeId:'chargeback_gradeid',classicId:'chargeback_classicid',teachingType:'id~teachingType',majorId:'chargeback_majorid',classesId:'chargeback_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function chargeBackQueryUnit() {
	var defaultValue = $("#chargeback_brSchool").val();
	var selectIdsJson = "{gradeId:'chargeback_gradeid',classicId:'chargeback_classicid',teachingType:'id~teachingType',majorId:'chargeback_majorid',classesId:'chargeback_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function chargeBackQueryGrade() {
	var defaultValue = $("#chargeback_brSchool").val();
	var gradeId = $("#chargeback_gradeid").val();
	var selectIdsJson = "{classicId:'chargeback_classicid',teachingType:'id~teachingType',majorId:'chargeback_majorid',classesId:'chargeback_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function chargeBackQueryClassic() {
	var defaultValue = $("#chargeback_brSchool").val();
	var gradeId = $("#chargeback_gradeid").val();
	var classicId = $("#chargeback_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'chargeback_majorid',classesId:'chargeback_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function chargeBackQueryMajor() {
	var defaultValue = $("#chargeback_brSchool").val();
	var gradeId = $("#chargeback_gradeid").val();
	var classicId = $("#chargeback_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#chargeback_majorid").val();
	var selectIdsJson = "{classesId:'chargeback_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
//导出
function exportChargeBack(){
	$('#frameChargeBack_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frameChargeBack_exportExcel";
	var brSchool    = $("#chargeback_brSchool").val();
	var gradeId = $("#chargeback_gradeid").val();
	var classicId = $("#chargeback_classicid").val();
	var majorId = $("#chargeback_majorid").val();
	var classesId = $("#chargeback_classesid").val();
	var status = $("#chargeback_studentStatus").val();
	var paymentMethod = $("#chargeback_paymentMethod").val();
	var currentFee = $("#chargebacek_currentFee").val();
	var beginDate = $("#chargebacek_beginDate").val();
	var endDate = $("#chargebacek_endDate").val();
	var receiptNumber_begin = $("#chargeback_receiptNumber_begin").val();
	var receiptNumber_end = $("#chargeback_receiptNumber_end").val();
	iframe.src = "${baseUrl}/edu3/finance/studentPaymentDetails/summaryList/export.html?brSchool="+brSchool+"&gradeid="+gradeId+"&classicid="+classicId+"&majorid="+majorId+"&classesId="+classesId+
			"&studentStatus="+status+"&paymentMethod="+paymentMethod+"&flag=export&chargeBack=Y"+"&currentFee="+currentFee
			+"&beginDate="+beginDate+"&endDate="+endDate+"&receiptNumber_begin="+receiptNumber_begin+"&receiptNumber_end="+receiptNumber_end; 
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//打印
function printChargeBack() {
	var brSchool    = $("#chargeback_brSchool").val();
	var gradeId = $("#chargeback_gradeid").val();
	var classicId = $("#chargeback_classicid").val();
	var majorId = $("#chargeback_majorid").val();
	var classesId = $("#chargeback_classesid").val();
	var status = $("#chargeback_studentStatus").val();
	var paymentMethod = $("#chargeback_paymentMethod").val();
	var sumPaidcount = $("#chargeback_sumPaidcount").val();
	var sumStuCount = $("#chargeback_sumStuCount").val();
	var sumBKAmount = $("#chargeback_sumBKAmount").val();
	var sumZKAmount = $("#chargeback_sumZKAmount").val();
	var sumAmount = $("#chargeback_sumAmount").val();
	var currentFee = $("#chargebacek_currentFee").val();
	var beginDate = $("#chargebacek_beginDate").val();
	var endDate = $("#chargebacek_endDate").val();
	var receiptNumber_begin = $("#chargeback_receiptNumber_begin").val();
	var receiptNumber_end = $("#chargeback_receiptNumber_end").val();
	var url="${baseUrl}/edu3/finance/studentPaymentDetails/summaryList/print-view.html?brSchool="
		+brSchool+"&gradeid="+gradeId+"&classicid="+classicId+"&majorid="+majorId+"&classesId="+classesId+"&studentStatus="+status+"&paymentMethod="+paymentMethod+"&flag=print&sumPaidcount="+sumPaidcount+"&sumStuCount="+sumStuCount+"&sumBKAmount="+sumBKAmount+"&sumZKAmount="+sumZKAmount+"&sumAmount="+sumAmount+"&chargeBack=Y"+"&currentFee="+currentFee
		+"&beginDate="+beginDate+"&endDate="+endDate+"&receiptNumber_begin="+receiptNumber_begin+"&receiptNumber_end="+receiptNumber_end;
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_SUMMARY_PRINT","打印预览",{width:900, height:600});
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPaymentDetails/chargeBackList.html"
				method="post" id="chargeBackForm">
				<div class="searchBar">
					<input type="hidden" id="chargeback_sumPaidcount"
						name="sumPaidcount" value="${sumPaidcount }"> <input
						type="hidden" id="chargeback_sumStuCount" name="sumStuCount"
						value="${sumStuCount }"> <input type="hidden"
						id="chargeback_sumBKAmount" name="sumBKAmount"
						value="${sumBKAmount }"> <input type="hidden"
						id="chargeback_sumZKAmount" name="sumZKAmount"
						value="${sumZKAmount }"> <input type="hidden"
						id="chargeback_sumAmount" name="sumAmount" value="${sumAmount }">
					<input type="hidden" id="chargeback_chargeBack" name="chargeBack"
						value="${condition['chargeBack'] }">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span sel-id="chargeback_brSchool"
							sel-name="brSchool" sel-onchange="chargeBackQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span sel-id="chargeback_gradeid"
							sel-name="gradeid" sel-onchange="chargeBackQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="chargeback_classicid"
							sel-name="classicid" sel-onchange="chargeBackQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="chargeback_majorid"
							sel-name="majorid" sel-onchange="chargeBackQueryMajor()"
							sel-classs="flexselect"></span></li>
						
						<li class="custom-li"><label>缴费时间：</label> <input type="text"
							id="chargeback_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:80px;" /> 
							至 <input type="text"
							id="chargeback_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:80px;" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="chargeback_studentStatus"
								value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:52%;"
								excludeValue="14,15,17,19,20,22" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="chargeback_classesid"
							sel-name="classesId" sel-classs="flexselect"></span></li>
						<li class="custom-li"><label>票据号：</label> <input type="text"
							id="chargeback_receiptNumber_begin" name="receiptNumber_begin"
							value="${condition['receiptNumber_begin']}" style="width:80px;"/> 
							至 <input type="text"
							id="chargeback_receiptNumber_end" name="receiptNumber_end"
							value="${condition['receiptNumber_end']}" style="width:80px;" /></li>
						<li><label>退费方式：</label> <gh:select name="paymentMethod"
								id="chargeback_paymentMethod"
								value="${condition['paymentMethod']}"
								dictionaryCode="CodePaymentMethod" excludeValue="1,2,3,4"
								style="width:52%;" /></li>
					</ul>
					<div class="subBar">
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENTDETAIL_CHARGEBACK"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">退费方式</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">总笔数</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">总人数</th>
						<th colspan="3" width="30%"
							style="text-align: center; vertical-align: middle;">学费</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">开始票据号</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">结束票据号</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">开始时间</th>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">结束时间</th>
					</tr>
					<tr>
						<th style="text-align: center; vertical-align: middle;"
							width="10%">本科生</th>
						<th style="text-align: center; vertical-align: middle;"
							width="10%">专科生</th>
						<th style="text-align: center; vertical-align: middle;"
							width="10%">合计</th>
					</tr>
				</thead>
				<tbody id="chargeBackBody">
					<c:forEach items="${chargeBackList}" var="v" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${v.paymentMethod ne '-1' }">${v.paymentMethod }</c:if></td>
							<td style="text-align: center; vertical-align: middle;">${v.totalNum }</td>
							<td style="text-align: center; vertical-align: middle;">${v.stuCount }</td>
							<td style="text-align: center; vertical-align: middle;">${v.undergraduateFee }</td>
							<td style="text-align: center; vertical-align: middle;">${v.educationFee }</td>
							<td style="text-align: center; vertical-align: middle;">${v.totalFee }</td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${!empty v.beginReceiptNum}">${v.beginReceiptNum }</c:if></td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${!empty v.endReceiptNum}">${v.endReceiptNum }</c:if></td>
							<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
									value="${v.beginDate }" pattern="yyyy-MM-dd" /></td>
							<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
									value="${v.endDate }" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
					<tr>
						<td style="text-align: center; vertical-align: middle;">合计：</td>
						<td style="text-align: center; vertical-align: middle;">${sumPaidCount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumStuCount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumBKAmount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumZKAmount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumAmount }</td>
						<td colspan="4"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>