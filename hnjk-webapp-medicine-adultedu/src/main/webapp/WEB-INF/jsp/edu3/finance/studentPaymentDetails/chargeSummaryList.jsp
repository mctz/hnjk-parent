<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收费汇总表</title>
<script type="text/javascript">
$(document).ready(function(){
	chargeSummaryQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function chargeSummaryQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name_teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'summary_brSchool',gradeId:'summary_gradeid',classicId:'summary_classicid',teachingType:'id_teachingType',majorId:'summary_majorid',classesId:'summary_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function chargeSummaryQueryUnit() {
	var defaultValue = $("#summary_brSchool").val();
	var selectIdsJson = "{gradeId:'summary_gradeid',classicId:'summary_classicid',teachingType:'id_teachingType',majorId:'summary_majorid',classesId:'summary_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function chargeSummaryQueryGrade() {
	var defaultValue = $("#summary_brSchool").val();
	var gradeId = $("#summary_gradeid").val();
	var selectIdsJson = "{classicId:'summary_classicid',teachingType:'id_teachingType',majorId:'summary_majorid',classesId:'summary_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function chargeSummaryQueryClassic() {
	var defaultValue = $("#summary_brSchool").val();
	var gradeId = $("#summary_gradeid").val();
	var classicId = $("#summary_classicid").val();
	var selectIdsJson = "{teachingType:'id_teachingType',majorId:'summary_majorid',classesId:'summary_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function chargeSummaryQueryMajor() {
	var defaultValue = $("#summary_brSchool").val();
	var gradeId = $("#summary_gradeid").val();
	var classicId = $("#summary_classicid").val();
	var teachingTypeId = $("#id_teachingType").val();
	var majorId = $("#summary_majorid").val();
	var selectIdsJson = "{classesId:'summary_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
//导出
function exportSummary(){
	$('#frame_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportExcel";
	var brSchool    = $("#summary_brSchool").val();
	var gradeId = $("#summary_gradeid").val();
	var classicId = $("#summary_classicid").val();
	var majorId = $("#summary_majorid").val();
	var classesId = $("#summary_classesid").val();
	var status = $("#summary_studentStatus").val();
	var paymentMethod = $("#summary_paymentMethod").val();
	var beginDate = $("#summary_beginDate").val();
	var endDate = $("#summary_endDate").val();
	var receiptNumber_begin = $("#summary_receiptNumber_begin").val();
	var receiptNumber_end = $("#summary_receiptNumber_end").val();
	iframe.src = "${baseUrl}/edu3/finance/studentPaymentDetails/summaryList/export.html?brSchool="+brSchool+"&gradeid="+gradeId+"&classicid="+classicId+"&majorid="+majorId+"&classesId="+classesId+"&studentStatus="+status+"&paymentMethod="+paymentMethod+"&flag=export"
			+"&beginDate="+beginDate+"&endDate="+endDate+"&receiptNumber_begin="+receiptNumber_begin
			+"&receiptNumber_end="+receiptNumber_end; 
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

//打印
function printSummary() {
	var brSchool    = $("#summary_brSchool").val();
	var gradeId = $("#summary_gradeid").val();
	var classicId = $("#summary_classicid").val();
	var majorId = $("#summary_majorid").val();
	var classesId = $("#summary_classesid").val();
	var status = $("#summary_studentStatus").val();
	var paymentMethod = $("#summary_paymentMethod").val();
	var sumPaidcount = $("#summary_sumPaidcount").val();
	var sumStuCount = $("#summary_sumStuCount").val();
	var sumBKAmount = $("#summary_sumBKAmount").val();
	var sumZKAmount = $("#summary_sumZKAmount").val();
	var sumAmount = $("#summary_sumAmount").val();
	var beginDate = $("#summary_beginDate").val();
	var endDate = $("#summary_endDate").val();
	var receiptNumber_begin = $("#summary_receiptNumber_begin").val();
	var receiptNumber_end = $("#summary_receiptNumber_end").val();
	var url="${baseUrl}/edu3/finance/studentPaymentDetails/summaryList/print-view.html?brSchool="+brSchool+"&gradeid="+gradeId+"&classicid="+classicId+"&majorid="+majorId+"&classesId="+classesId+"&studentStatus="
		+status+"&paymentMethod="+paymentMethod+"&flag=print&sumPaidcount="+sumPaidcount+"&sumStuCount="+sumStuCount+"&sumBKAmount="+sumBKAmount+"&sumZKAmount="+sumZKAmount+"&sumAmount="+sumAmount
		+"&beginDate="+beginDate+"&endDate="+endDate
		+"&receiptNumber_begin="+receiptNumber_begin
		+"&receiptNumber_end="+receiptNumber_end;
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_SUMMARY_PRINT","打印预览",{width:900, height:600});
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="summary_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPaymentDetails/summaryList.html"
				method="post">
				<div class="searchBar">
					<input type="hidden" id="summary_sumPaidcount" name="sumPaidcount"
						value="${sumPaidcount }"> <input type="hidden"
						id="summary_sumStuCount" name="sumStuCount"
						value="${sumStuCount }"> <input type="hidden"
						id="summary_sumBKAmount" name="sumBKAmount"
						value="${sumBKAmount }"> <input type="hidden"
						id="summary_sumZKAmount" name="sumZKAmount"
						value="${sumZKAmount }"> <input type="hidden"
						id="summary_sumAmount" name="sumAmount" value="${sumAmount }">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> 
						<%-- <c:if test="${!isBrschool }"> --%>
								<span sel-id="summary_brSchool" sel-name="brSchool"
									sel-onchange="chargeSummaryQueryUnit()" sel-classs="flexselect"
									></span>
							<%-- </c:if> 
							<c:if test="${isBrschool}">
								<input type="text"  value="${brSchoolName}" readonly="readonly" />
								<input type="hidden" name="brSchool" id="summary_brSchool" value="${condition['brSchool']}" />
							</c:if></li> --%>
						<li><label>年级：</label> <span sel-id="summary_gradeid"
							sel-name="gradeid" sel-onchange="chargeSummaryQueryGrade()"
							sel-style="width: 52%"></span></li>
						<li class="custom-li"><label>专业：</label> <span sel-id="summary_majorid"
							sel-name="majorid" sel-onchange="chargeSummaryQueryMajor()"
							sel-classs="flexselect"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="summary_classesid"
							sel-name="classesId" sel-classs="flexselect"></span></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="summary_studentStatus" value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:52%;"
								excludeValue="14,15,17,19,20,22" /></li>
						<li class="custom-li"><label>缴费日期：</label> <input type="text"
							id="summary_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="summary_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>票据号：</label> <input type="text"
							id="summary_receiptNumber_begin" name="receiptNumber_begin"
							value="${condition['receiptNumber_begin']}" style="width: 80px;" /> 
							至 <input type="text"
							id="summary_receiptNumber_end" name="receiptNumber_end"
							value="${condition['receiptNumber_end']}" style="width: 80px;" /></li>
						<li><label>收款方式：</label> <gh:select name="paymentMethod"
							id="summary_paymentMethod" value="${condition['paymentMethod']}"
							dictionaryCode="CodePaymentMethod" style="width:52%;" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENTDETAIL_SUMMARY"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th rowspan="2" width="10%"
							style="text-align: center; vertical-align: middle;">缴费方式</th>
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
				<tbody id="chargeSummaryBody">
					<c:forEach items="${summaryList}" var="summary" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">${summary.paymentMethod }</td>
							<td style="text-align: center; vertical-align: middle;">${summary.totalNum }</td>
							<td style="text-align: center; vertical-align: middle;">${summary.stuCount }</td>
							<td style="text-align: center; vertical-align: middle;">${summary.undergraduateFee }</td>
							<td style="text-align: center; vertical-align: middle;">${summary.educationFee }</td>
							<td style="text-align: center; vertical-align: middle;">${summary.totalFee }</td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${!empty summary.beginReceiptNum}">${summary.beginReceiptNum }</c:if></td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${!empty summary.endReceiptNum}">${summary.endReceiptNum }</c:if>
							</td>
							<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
									value="${summary.beginDate }" pattern="yyyy-MM-dd" /></td>
							<td style="text-align: center; vertical-align: middle;"><fmt:formatDate
									value="${summary.endDate }" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
					<tr>
						<td style="text-align: center; vertical-align: middle;">合计：</td>
						<td style="text-align: center; vertical-align: middle;">${sumPaidCount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumStuCount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumBKAmount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumZKAmount }</td>
						<td style="text-align: center; vertical-align: middle;">${sumAmount }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>