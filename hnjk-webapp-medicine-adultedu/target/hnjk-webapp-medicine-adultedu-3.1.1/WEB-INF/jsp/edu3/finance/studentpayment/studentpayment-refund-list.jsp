<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退费明细报表</title>
<script type="text/javascript">
$(document).ready(function(){
	studentPaymentReturnQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function studentPaymentReturnQueryBegin() {
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
	var selectIdsJson = "{unitId:'studentPaymentReturn_brSchool',gradeId:'studentPaymentReturn_gradeid',classicId:'studentPaymentReturn_classicid',teachingType:'id~teachingType',majorId:'studentPaymentReturn_majorid',classesId:'studentPaymentReturn_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function studentPaymentReturnQueryUnit() {
	var defaultValue = $("#studentPaymentReturn_brSchool").val();
	var selectIdsJson = "{gradeId:'studentPaymentReturn_gradeid',classicId:'studentPaymentReturn_classicid',teachingType:'id~teachingType',majorId:'studentPaymentReturn_majorid',classesId:'studentPaymentReturn_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function studentPaymentReturnQueryGrade() {
	var defaultValue = $("#studentPaymentReturn_brSchool").val();
	var gradeId = $("#studentPaymentReturn_gradeid").val();
	var selectIdsJson = "{classicId:'studentPaymentReturn_classicid',teachingType:'id~teachingType',majorId:'studentPaymentReturn_majorid',classesId:'studentPaymentReturn_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function studentPaymentReturnQueryClassic() {
	var defaultValue = $("#studentPaymentReturn_brSchool").val();
	var gradeId = $("#studentPaymentReturn_gradeid").val();
	var classicId = $("#studentPaymentReturn_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'studentPaymentReturn_majorid',classesId:'studentPaymentReturn_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function studentPaymentReturnQueryMajor() {
	var defaultValue = $("#studentPaymentReturn_brSchool").val();
	var gradeId = $("#studentPaymentReturn_gradeid").val();
	var classicId = $("#studentPaymentReturn_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#studentPaymentReturn_majorid").val();
	var selectIdsJson = "{classesId:'studentPaymentReturn_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//合并勾选查询导出退费明细记录
function exportReturn(){
	var url = "${baseUrl}/edu3/finance/studentPayment/studentPayment-refund-export.html?"+getQueryUrl(3);
	alertMsg.confirm("您确定要导出这些退费记录吗？",{
		okCall:function(){
			downloadFileByIframe(url);
		}
	});
}

//合并勾选查询打印退费明细记录
function printReturn(){
	var url = "${baseUrl}/edu3/finance/studentPayment/studentPayment-refund-count.html?"+getQueryUrl(3);
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENT_REFUND_COUNT", "根据条件打印退费明细记录",{width:200,height:100,mask:true}); 
}
	
function cancelPrint(){
	if(!isChecked('resourceid',"#studentPaymentReturnBody")){
		alertMsg.warn('请选择一条要操作记录！');
	return false;
	}
	alertMsg.confirm("您确定要撤销这些记录吗？", {
		okCall: function(){//执行	
			$.post("${baseUrl}/edu3/finance/studentPayment/cancelPrint.html?"+getQueryUrl(2),{}, navTabAjaxDone, "json");
		}
	});
}

//获取参数
function getQueryUrl(type){//1：查询；2：勾选；3：查询+勾选
	var param = "";
	var detailIds = new Array();
	jQuery("#studentPaymentReturnBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	if(detailIds.length>0 && type!=1){
		param += "detailIds="+detailIds.toString();
	}else if(type!=2){
		var brSchool = $("#studentPaymentReturn_brSchool").val();
		var yearId = $("#studentPaymentReturn_yearId").val();
		var gradeId = $("#studentPaymentReturn_gradeid").val();
		var classicId = $("#studentPaymentReturn_classicid").val();
		var majorId = $("#studentPaymentReturn_majorid").val();
		var name = $("#studentPaymentReturn_name").val();
		var studyNo = $("#studentPaymentReturn_studyNo").val(); 
		var studentStatus = $("#studentPaymentReturn_studentStatus").val();
		var beginDate = $("#studentPaymentReturn_beginDate").val();
		var endDate = $("#studentPaymentReturn_endDate").val();
		var beginPrintDate = $("#studentPaymentReturn_beginPrintDate").val();
		var endPrintDate = $("#studentPaymentReturn_endPrintDate").val();
		var drawer = $("#studentPaymentReturn_drawer").val();
		var receiptNumber_begin = $("#studentPaymentReturn_receiptNumber_begin").val();
		var receiptNumber_end = $("#studentPaymentReturn_receiptNumber_end").val();
		var paymentMethod = $("#studentPaymentReturn_paymentMethod").val();
		var isPrint = $("#studentPaymentReturn_isPrint").val();
		var chargingItems = $("#studentPaymentReturn_chargingItems").val();
		var processType = $("#studentPaymentReturn_processType").val();
		param += "addParameter=Y";//填充第一个参数
		if(brSchool!="") param += "&brSchool="+brSchool;
		if(gradeId!="")  param += "&gradeId="+gradeId;
		if(classicId!="") param += "&classicId="+classicId;
		if(majorId!="") param += "&majorId="+majorId;
		if(name!="") param += "&name="+name;
		if(studyNo!="") param += "&studyNo="+studyNo;
		if(studentStatus!="") param += "&studentStatus="+studentStatus;
		if(beginDate!="") param += "&beginDate="+beginDate;
		if(endDate!="") param += "&endDate="+endDate;
		if(endPrintDate!="") param += "&endPrintDate="+endPrintDate;
		if(drawer!="") param += "&drawer="+drawer;
		if(receiptNumber_begin!="") param += "&receiptNumber_begin="+receiptNumber_begin;
		if(receiptNumber_end!="") param += "&receiptNumber_end="+receiptNumber_end;
		if(paymentMethod!="") param += "&paymentMethod="+paymentMethod;
		if(isPrint!="") param += "&isPrint="+isPrint;
		if(chargingItems!="") param += "&chargingItems="+chargingItems;
		if(processType!="") param += "&processType="+processType;
	}
	return param;
}

//打印退费签收表
function printReturnBill(){
	var url = "${baseUrl}/edu3/finance/studentPayment/printBillView.html?"+getQueryUrl(3);
	$.pdialog.open(url,"RES_FINANCE_RETURN_PRINTBILL", "打印发票签领表",{width:800,height:600,mask:true}); 
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="studentPaymentReturn_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPayment/returnList.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> 
							<%-- <c:choose>
								<c:when test="${not isBrschool }"> --%>
									<span sel-id="studentPaymentReturn_brSchool"
										sel-name="brSchool"
										sel-onchange="studentPaymentReturnQueryUnit()"
										sel-classs="flexselect"></span>
								<%-- </c:when>
								<c:otherwise>
									<input type="hidden" value="${condition['brSchool']}"
										id="studentPaymentReturn_brSchool">
									<input type="text" value="${schoolname}" readonly="readonly">
								</c:otherwise>
							</c:choose> --%>
						</li>
						<li><label>年度：</label> <gh:selectModel
								id="studentPaymentReturn_yearId" name="yearId"
								bindValue="resourceid" displayValue="yearName"
								style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" /></li>
						<li><label>年级：</label> <span
							sel-id="studentPaymentReturn_gradeid" sel-name="gradeid"
							sel-onchange="studentPaymentReturnQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="studentPaymentReturn_classicid" sel-name="classicid"
							sel-onchange="studentPaymentReturnQueryClassic()"
							sel-style="width: 100px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="studentPaymentReturn_majorid" sel-name="majorid"
							sel-onchange="studentPaymentReturnQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>姓名：</label><input id="studentPaymentReturn_name"
							type="text" name="name" value="${condition['name']}"
							style="width: 52%;" /></li>
					<%-- 	<li class="custom-li"><label>创建时间：</label> <input type="text"
							id="studentPaymentReturn_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="studentPaymentReturn_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li> --%>
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems" id="studentPaymentReturn_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="studentPaymentReturn_classesid" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li><label>学号：</label><input
							id="studentPaymentReturn_studyNo" type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 52%;" /></li>
						<li class="custom-li"><label>打印时间：</label> <input type="text"
							id="studentPaymentReturn_beginPrintDate" name="beginPrintDate"
							value="${condition['beginPrintDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="studentPaymentReturn_endPrintDate" name="endPrintDate"
							value="${condition['endPrintDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>付款方式：</label> <gh:select name="paymentMethod"
								id="studentPaymentReturn_paymentMethod"
								value="${condition['paymentMethod']}"
								dictionaryCode="CodePaymentMethod" excludeValue="1,2,3,4"
								style="width:52%;" /></li>
						<li><label>是否打印：</label> <gh:select name="isPrint"
								id="studentPaymentReturn_isPrint"
								value="${condition['isPrint']}" dictionaryCode="yesOrNo"
								style="width:52%;" /></li>
						<li>
							<label>处理类型：</label> 
							<gh:select dictionaryCode="CodeProcessType" id="studentPaymentReturn_processType" name="processType"
								value="${condition['processType'] }" style="width:52%;" />
						</li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_RETURN_RECORD" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentPayment"
							onclick="checkboxAll('#check_all_studentPayment','resourceid','#studentPaymentReturnBody')" /></th>
						<th width="8%">学号</th>
						<th width="7%">学生姓名</th>
						<th width="10%">教学点</th>
						<th width="4%">学年</th>
						<th width="4%">年级</th>
						<th width="5%">层次</th>
						<th width="10%">专业</th>
						<th width="4%">收费项</th>
						<th width="4%">处理类型</th>
						<th width="5%">应缴金额</th>
						<th width="5%">已缴金额</th>
						<th width="5%">退费金额</th>
						<th width="5%">补交金额</th>
						<th width="5%">付款方式</th>
						<!-- <th width="6%">创建时间</th> -->
						<th width="5%">开票人</th>
						<th width="5%">票据号</th>
						<th width="6%">打印时间</th>
					</tr>
				</thead>
				<tbody id="studentPaymentReturnBody">
					<c:forEach items="${returnPremiumList.result}" var="returnPremium"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${returnPremium.resourceid }" autocomplete="off" /></td>
							<td title="${returnPremium.studyNo }">${returnPremium.studentInfo.studyNo }</td>
							<td title="${returnPremium.studentInfo.studentName }">${returnPremium.studentInfo.studentName }</td>
							<td title="${returnPremium.studentInfo.branchSchool.unitName }">${returnPremium.studentInfo.branchSchool.unitName }</td>
							<td title="${returnPremium.yearInfo.firstYear }">${returnPremium.yearInfo.firstYear }</td>
							<td title="${returnPremium.studentInfo.grade.gradeName }">${returnPremium.studentInfo.grade.gradeName }</td>
							<td title="${returnPremium.studentInfo.classic.classicName }">${returnPremium.studentInfo.classic.classicName }</td>
							<td title="${returnPremium.studentInfo.major.majorName }">${returnPremium.studentInfo.major.majorName }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',returnPremium.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',returnPremium.chargingItems) }</td>
							<td title="${ghfn:dictCode2Val('CodeProcessType',returnPremium.processType) }">${ghfn:dictCode2Val('CodeProcessType',returnPremium.processType) }</td>	
							<td
								title="<fmt:formatNumber value='${returnPremium.recpayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${returnPremium.recpayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${returnPremium.facepayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${returnPremium.facepayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${returnPremium.returnPremiumFee }' pattern='####.##' />"><fmt:formatNumber
									value="${returnPremium.returnPremiumFee eq null?0:returnPremium.returnPremiumFee}"
									pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${returnPremium.payAmount }' pattern='####.##' />"><fmt:formatNumber
									value="${returnPremium.payAmount eq null?0:returnPremium.payAmount}"
									pattern="####.##" /></td>
							<td
								title="${ghfn:dictCode2Val('CodePaymentMethod',returnPremium.paymentMethod) }">${ghfn:dictCode2Val('CodePaymentMethod',returnPremium.paymentMethod) }</td>
							<%-- <td
								title="<fmt:formatDate value='${returnPremium.createDate }' pattern='yyyy-MM-dd' />"><fmt:formatDate
									value="${returnPremium.createDate }" pattern="yyyy-MM-dd" /></td> --%>
							<td title="${returnPremium.operatorName }">${returnPremium.operatorName }</td>
							<td title="${returnPremium.drawer }">${returnPremium.drawer }</td>
							<td title="${returnPremium.receiptNumber }">${returnPremium.receiptNumber }</td>
							<td title="${returnPremium.printDate }">${returnPremium.printDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${returnPremiumList}"
				goPageUrl="${baseUrl }/edu3/finance/studentPayment/returnList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>