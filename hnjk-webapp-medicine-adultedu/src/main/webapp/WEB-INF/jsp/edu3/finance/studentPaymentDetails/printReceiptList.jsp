<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印收据</title>
<script type="text/javascript">
$(document).ready(function(){
	prinReceiptQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function prinReceiptQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'prinReceipt_brSchool',gradeId:'prinReceipt_gradeid',classicId:'prinReceipt_classicid',teachingType:'id~teachingType',majorId:'prinReceipt_majorid',classesId:'prinReceipt_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function prinReceiptQueryUnit() {
	var defaultValue = $("#prinReceipt_brSchool").val();
	var selectIdsJson = "{gradeId:'prinReceipt_gradeid',classicId:'prinReceipt_classicid',teachingType:'id~teachingType',majorId:'prinReceipt_majorid',classesId:'prinReceipt_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function prinReceiptQueryGrade() {
	var defaultValue = $("#prinReceipt_brSchool").val();
	var gradeId = $("#prinReceipt_gradeid").val();
	var selectIdsJson = "{classicId:'prinReceipt_classicid',teachingType:'id~teachingType',majorId:'prinReceipt_majorid',classesId:'prinReceipt_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function prinReceiptQueryClassic() {
	var defaultValue = $("#prinReceipt_brSchool").val();
	var gradeId = $("#prinReceipt_gradeid").val();
	var classicId = $("#prinReceipt_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'prinReceipt_majorid',classesId:'prinReceipt_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function prinReceiptQueryMajor() {
	var defaultValue = $("#prinReceipt_brSchool").val();
	var gradeId = $("#prinReceipt_gradeid").val();
	var classicId = $("#prinReceipt_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#prinReceipt_majorid").val();
	var selectIdsJson = "{classesId:'prinReceipt_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//合并勾选查询打印缴费明细记录
function printDetailPaymentPrint(){
	var brSchool = $("#prinReceipt_brSchool").val();
	var gradeId = $("#prinReceipt_gradeid").val();
	var classicId = $("#prinReceipt_classicid").val();
	var majorId = $("#prinReceipt_majorid").val();
	var name = $("#prinReceipt_name").val();
	var studyNo = $("#prinReceipt_studyNo").val(); 
	var studentStatus = $("#prinReceipt_studentStatus").val();
	var beginDate = $("#prinReceipt_beginDate").val();
	var endDate = $("#prinReceipt_endDate").val();
	var year = $("#prinReceipt_year").val();
	var beginPrintDate = $("#prinReceipt_beginPrintDate").val();
	var endPrintDate = $("#prinReceipt_endPrintDate").val();
	var drawer = $("#prinReceipt_drawer").val();
	var examCertificateNo = $("#prinReceipt_examCertificateNo").val();
	var receiptNumber_begin = $("#prinReceipt_receiptNumber_begin").val();
	var receiptNumber_end = $("#prinReceipt_receiptNumber_end").val();
	var paymentMethod = $("#prinReceipt_paymentMethod").val();
	var isPrint = $("#prinReceipt_isPrint").val();
	
	var detailIds = new Array();
	jQuery("#prinReceiptBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/quaryPrint.html?"+getQueryUrl(3);
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT", "根据条件打印缴费明细记录",{width:200,height:100,mask:true});
} 

//合并勾选查询导出缴费明细记录
function exportDetailPaymentPrint(){
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/export.html?"+getQueryUrl(3)+"&operateType=export";
	alertMsg.confirm("您确定要导出这些记录吗？",{
		okCall:function(){
			downloadFileByIframe(url);
		}
	}); 
}

//撤销票据号
function abolishPrint(){
	if(!isChecked('resourceid',"#prinReceiptBody")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	alertMsg.confirm("您确定要撤销这些记录吗？", {
		okCall: function(){//执行		
			var detailIds = new Array();
			jQuery("#prinReceiptBody input[name='resourceid']:checked").each(function(){
				detailIds.push(jQuery(this).val());
			});
			$.post("${baseUrl}/edu3/finance/studentPaymentDetails/cancelPrint.html?"+getQueryUrl(2),{}, navTabAjaxDone, "json");
		}
	});
}
//获取参数
function getQueryUrl(type){//1：查询；2：勾选；3：查询+勾选
	var param = "";
	var detailIds = new Array();
	jQuery("#prinReceiptBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	if(detailIds.length>0 && type!=1){
		param += "detailIds="+detailIds.toString();
	}else if(type!=2){
		var brSchool = $("#prinReceipt_brSchool").val();
		var gradeId = $("#prinReceipt_gradeid").val();
		var classicId = $("#prinReceipt_classicid").val();
		var majorId = $("#prinReceipt_majorid").val();
		var name = $("#prinReceipt_name").val();
		var studyNo = $("#prinReceipt_studyNo").val(); 
		var studentStatus = $("#prinReceipt_studentStatus").val();
		var beginDate = $("#prinReceipt_beginDate").val();
		var endDate = $("#prinReceipt_endDate").val();
		var year = $("#prinReceipt_year").val();
		var beginPrintDate = $("#prinReceipt_beginPrintDate").val();
		var endPrintDate = $("#prinReceipt_endPrintDate").val();
		var drawer = $("#prinReceipt_drawer").val();
		var examCertificateNo = $("#prinReceipt_examCertificateNo").val();
		var receiptNumber_begin = $("#prinReceipt_receiptNumber_begin").val();
		var receiptNumber_end = $("#prinReceipt_receiptNumber_end").val();
		var paymentMethod = $("#prinReceipt_paymentMethod").val();
		var isPrint = $("#prinReceipt_isPrint").val();
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
		if(year!="") param += "&year="+year;
		if(beginPrintDate!="") param += "&beginPrintDate="+beginPrintDate;
		if(endPrintDate!="") param += "&endPrintDate="+endPrintDate;
		if(drawer!="") param += "&drawer="+drawer;
		if(examCertificateNo!="") param += "&examCertificateNo="+examCertificateNo;
		if(receiptNumber_begin!="") param += "&receiptNumber_begin="+receiptNumber_begin;
		if(receiptNumber_end!="") param += "&receiptNumber_end="+receiptNumber_end;
		if(paymentMethod!="") param += "&paymentMethod="+paymentMethod;
		if(isPrint!="") param += "&isPrint="+isPrint;
	}
	return param;
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="prinReceipt_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPaymentDetails/printReceiptList.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span sel-id="prinReceipt_brSchool"
							sel-name="brSchool" sel-onchange="prinReceiptQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>学年：</label> <gh:selectModel id="prinReceipt_year"
								name="year" bindValue="firstYear" displayValue="firstYear"
								condition="isDeleted=0"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['year']}" orderBy="firstYear desc"
								style="width:52%" /></li>
						<li><label>年级：</label> <span sel-id="prinReceipt_gradeid"
							sel-name="gradeid" sel-onchange="prinReceiptQueryGrade()"
							sel-style="width: 120px"></span></li>

						<li><label>层次：</label> <span sel-id="prinReceipt_classicid"
							sel-name="classicid" sel-onchange="prinReceiptQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="prinReceipt_majorid"
							sel-name="majorid" sel-onchange="prinReceiptQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>准考证号：</label><input
							id="prinReceipt_examCertificateNo" type="text"
							name="examCertificateNo"
							value="${condition['examCertificateNo']}" style="width: 52%;" />
						</li>
						<li><label>学号：</label><input id="prinReceipt_studyNo"
							type="text" name="studyNo" value="${condition['studyNo']}"
							style="width: 52%;" /></li>
						<li><label>姓名：</label><input id="prinReceipt_name"
							type="text" name="name" value="${condition['name']}"
							style="width: 52%;" /></li>
						<!-- <li>
					<label>班级：</label>
					<span sel-id="prinReceipt_classesid" sel-name="classesId" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li> -->
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>缴费日期：</label> <input type="text"
							id="prinReceipt_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="prinReceipt_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
						<li><label>收款方式：</label> <gh:select name="paymentMethod"
								id="prinReceipt_paymentMethod"
								value="${condition['paymentMethod']}"
								dictionaryCode="CodePaymentMethod" style="width:52%;" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="prinReceipt_studentStatus"
								value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:52%;"
								excludeValue="14,15,17,19,20,22" /></li>
						<li><label>开票人：</label><input id="prinReceipt_drawer"
							type="text" name="drawer" value="${condition['drawer']}"
							style="width: 52%;" /></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>打印时间：</label> <input type="text"
							id="prinReceipt_beginPrintDate" name="beginPrintDate"
							value="${condition['beginPrintDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="prinReceipt_endPrintDate" name="endPrintDate"
							value="${condition['endPrintDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;"/></li>
						<li><label>是否打印：</label> <gh:select name="isPrint"
								id="prinReceipt_isPrint" value="${condition['isPrint']}"
								dictionaryCode="yesOrNo" style="width:52%;" /></li>
						<li class="custom-li"><label>票据号：</label> <input type="text"
							id="prinReceipt_receiptNumber_begin" name="receiptNumber_begin"
							value="${condition['receiptNumber_begin']}" style="width: 80px;" /> 
							至 <input type="text"
							id="prinReceipt_receiptNumber_end" name="receiptNumber_end"
							value="${condition['receiptNumber_end']}" style="width: 80px;" /></li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems" id="prinReceipt_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
						<li>
							<label>开发票：</label> 
							<gh:select name="isInvoicing" id="prinReceipt_isInvoicing" value="${condition['isInvoicing']}" dictionaryCode="yesOrNo" style="width:52%;" />
						</li>
					</ul>
					
					<div class="subBar">
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth
				parentCode="RES_FINANCE_STUDENTPAYMENTDETAIL_PRINTRECEIPT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="233">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_stuPayDetail_print"
							onclick="checkboxAll('#check_all_stuPayDetail_print','resourceid','#prinReceiptBody')" /></th>
						<th width="13%">教学点</th>
						<th width="7%">票据号</th>
						<th width="4%">学年</th>
						<th width="5%">收款方式</th>
						<th width="8%">准考证号</th>
						<th width="11%">学号</th>
						<th width="6%">姓名</th>
						<th width="4%">培养层次</th>
						<th width="4%">收费项</th>
						<th width="6%">开票人</th>
						<th width="7%">缴费时间</th>
						<th width="5%">金额</th>
						<th width="5%">手续费</th>
						<th width="7%">打印时间</th>
						<th width="4%">开发票</th>
					</tr>
				</thead>
				<tbody id="prinReceiptBody">
					<c:forEach items="${printReceiptList.result}" var="receipt"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${receipt.resourceid }" autocomplete="off" /></td>
							<td title="${receipt.studentInfo.branchSchool.unitName }">${receipt.studentInfo.branchSchool.unitName }</td>
							<td title="${receipt.receiptNumber }">${receipt.receiptNumber }</td>
							<td title="${receipt.year }">${receipt.year }</td>
							<td
								title="${ghfn:dictCode2Val('CodePaymentMethod',receipt.paymentMethod) }">${ghfn:dictCode2Val("CodePaymentMethod",receipt.paymentMethod) }</td>
							<td
								title="${receipt.studentInfo!=null?receipt.studentInfo.examCertificateNo:'' }">${receipt.studentInfo!=null?receipt.studentInfo.examCertificateNo:'' }</td>
							<td title="(${receipt.studentInfo.studyNo }">${receipt.studentInfo.studyNo }</td>
							<td title="${receipt.studentInfo.studentName }">${receipt.studentInfo.studentName }</td>
							<td title="${receipt.studentInfo.classic.classicName }">${receipt.studentInfo.classic.classicName }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',receipt.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',receipt.chargingItems) }</td>
							<td title="${receipt.drawer }">${receipt.drawer }</td>
							<td
								title="<fmt:formatDate value='${receipt.operateDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${receipt.operateDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td
								title="<fmt:formatNumber value='${receipt.payAmount }' pattern='####.##' />"><fmt:formatNumber
									value="${receipt.payAmount }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${receipt.chargeMoney }' pattern='####.##' />"><fmt:formatNumber
									value="${receipt.chargeMoney }" pattern="####.##" /></td>
							<td
								title="<fmt:formatDate value='${receipt.printDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${receipt.printDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td title="${ghfn:dictCode2Val('yesOrNo',receipt.isInvoicing) }">${ghfn:dictCode2Val('yesOrNo',receipt.isInvoicing) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${printReceiptList}"
				goPageUrl="${baseUrl }/edu3/finance/studentPaymentDetails/printReceiptList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>