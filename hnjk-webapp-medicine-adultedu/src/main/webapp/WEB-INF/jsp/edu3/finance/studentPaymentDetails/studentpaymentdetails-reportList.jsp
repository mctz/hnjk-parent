<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收费简明报表</title>
<script type="text/javascript">
$(document).ready(function(){
	studentPaymentDetailQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function studentPaymentDetailQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'studentPaymentDetailReport_brSchool',gradeId:'studentPaymentDetailReport_gradeid',classicId:'studentPaymentDetailReport_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetailReport_majorid',classesId:'studentPaymentDetailReport_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function studentPaymentDetailQueryUnit() {
	var defaultValue = $("#studentPaymentDetailReport_brSchool").val();
	var selectIdsJson = "{gradeId:'studentPaymentDetailReport_gradeid',classicId:'studentPaymentDetailReport_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetailReport_majorid',classesId:'studentPaymentDetailReport_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function studentPaymentDetailQueryGrade() {
	var defaultValue = $("#studentPaymentDetailReport_brSchool").val();
	var gradeId = $("#studentPaymentDetailReport_gradeid").val();
	var selectIdsJson = "{classicId:'studentPaymentDetailReport_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetailReport_majorid',classesId:'studentPaymentDetailReport_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function studentPaymentDetailQueryClassic() {
	var defaultValue = $("#studentPaymentDetailReport_brSchool").val();
	var gradeId = $("#studentPaymentDetailReport_gradeid").val();
	var classicId = $("#studentPaymentDetailReport_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'studentPaymentDetailReport_majorid',classesId:'studentPaymentDetailReport_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function studentPaymentDetailQueryMajor() {
	var defaultValue = $("#studentPaymentDetailReport_brSchool").val();
	var gradeId = $("#studentPaymentDetailReport_gradeid").val();
	var classicId = $("#studentPaymentDetailReport_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#studentPaymentDetailReport_majorid").val();
	var selectIdsJson = "{classesId:'studentPaymentDetailReport_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//合并勾选查询打印缴费明细记录
function printDetailPaymentReport(){
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/quaryPrint.html?"+getQueryUrl(3);
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT", "根据条件打印缴费明细记录",{width:200,height:100,mask:true}); 
}

//合并勾选查询导出缴费明细记录
function exportDetails(){
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/export.html?"+getQueryUrl(3)+"&operateType=export";
	alertMsg.confirm("您确定要导出这些记录吗？",{
		okCall:function(){
			downloadFileByIframe(url);
		}
	}); 
}

//撤销票据号
function cancelPrint(){
	if(!isChecked('resourceid',"#studentPaymentDetailReportBody")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	alertMsg.confirm("您确定要撤销这些记录吗？", {
		okCall: function(){//执行		
			$.post("${baseUrl}/edu3/finance/studentPaymentDetails/cancelPrint.html?"+getQueryUrl(2),{}, navTabAjaxDone, "json");
		}
	});
}

// 跳转下载对账文件对话框
function downloadCheckFileForm() {
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/downloadCheckFile.html";
	$.pdialog.open(url,"RES_STUDENTPAYMENTDETAIL_DOWNLOAD", "下载对账文件",{width:200,height:100,mask:true}); 
}

//打印签收表
function printBill(){
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/printBillView.html?"+getQueryUrl(3);
	//alert(url);
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_PRINTBILL", "打印学生发票签领表",{width:800,height:600,mask:true}); 
}

//获取参数
function getQueryUrl(type){//1：查询；2：勾选；3：查询+勾选
	var param = "";
	var detailIds = new Array();
	jQuery("#studentPaymentDetailReportBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	if(detailIds.length>0 && type!=1){
		param += "detailIds="+detailIds.toString();
	}else if(type!=2){
		var brSchool = $("#studentPaymentDetailReport_brSchool").val();
		var gradeId = $("#studentPaymentDetailReport_gradeid").val();
		var classicId = $("#studentPaymentDetailReport_classicid").val();
		var majorId = $("#studentPaymentDetailReport_majorid").val();
		var name = $("#studentPaymentDetailReport_name").val();
		var studyNo = $("#studentPaymentDetailReport_studyNo").val(); 
		var studentStatus = $("#studentPaymentDetailReport_studentStatus").val();
		var beginDate = $("#studentPaymentDetailReport_beginDate").val();
		var endDate = $("#studentPaymentDetailReport_endDate").val();
		var year = $("#year").val();
		var beginPrintDate = $("#studentPaymentDetailReport_beginPrintDate").val();
		var endPrintDate = $("#studentPaymentDetailReport_endPrintDate").val();
		var drawer = $("#studentPaymentDetailReport_drawer").val();
		var examCertificateNo = $("#studentPaymentDetailReport_examCertificateNo").val();
		var receiptNumber_begin = $("#studentPaymentDetailReport_receiptNumber_begin").val();
		var receiptNumber_end = $("#studentPaymentDetailReport_receiptNumber_end").val();
		var paymentMethod = $("#studentPaymentDetailReport_paymentMethod").val();
		var isPrint = $("#studentPaymentDetailReport_isPrint").val();
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
		<div class="pageHeader" style="height: 125px;">
			<form id="studentPaymentDetailReport_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPaymentDetails/reportList.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span
							sel-id="studentPaymentDetailReport_brSchool" sel-name="brSchool"
							sel-onchange="studentPaymentDetailQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>学年：</label> <gh:selectModel name="year"
								bindValue="firstYear" displayValue="firstYear"
								condition="isDeleted=0"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['year']}" orderBy="firstYear desc"
								style="width:52%" /></li>
						<li><label>年级：</label> <span
							sel-id="studentPaymentDetailReport_gradeid" sel-name="gradeid"
							sel-onchange="studentPaymentDetailQueryGrade()"
							sel-style="width: 120px"></span></li>

						<li><label>层次：</label> <span
							sel-id="studentPaymentDetailReport_classicid"
							sel-name="classicid"
							sel-onchange="studentPaymentDetailQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="studentPaymentDetailReport_majorid" sel-name="majorid"
							sel-onchange="studentPaymentDetailQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>准考证号：</label><input
							id="studentPaymentDetailReport_examCertificateNo" type="text"
							name="examCertificateNo"
							value="${condition['examCertificateNo']}" style="width: 52%;" />
						</li>
						<li><label>学号：</label><input
							id="studentPaymentDetailReport_studyNo" type="text"
							name="studyNo" value="${condition['studyNo']}"
							style="width: 52%;" /></li>
						<li><label>姓名：</label><input
							id="studentPaymentDetailReport_name" type="text" name="name"
							value="${condition['name']}" style="width: 52%;" /></li>


						<!-- <li>
					<label>班级：</label>
					<span sel-id="studentPaymentDetailReport_classesid" sel-name="classesId" sel-classs="flexselect" sel-style="width: 120px"></span>
				</li> -->


					</ul>

					<ul class="searchContent">
						<li class="custom-li"><label>缴费日期：</label> <input type="text"
							id="studentPaymentDetailReport_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text"
							id="studentPaymentDetailReport_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
						<li><label>收款方式：</label> <gh:select name="paymentMethod"
								id="studentPaymentDetailReport_paymentMethod"
								value="${condition['paymentMethod']}"
								dictionaryCode="CodePaymentMethod" style="width:52%;" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="studentPaymentDetailReport_studentStatus"
								value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:52%;"
								excludeValue="14,15,17,19,20,22" /></li>
						<li><label>开票人：</label><input
							id="studentPaymentDetailReport_drawer" type="text" name="drawer"
							value="${condition['drawer']}" style="width: 52%;" /></li>
						

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>打印时间：</label> <input type="text"
							id="studentPaymentDetailReport_beginPrintDate"
							name="beginPrintDate" value="${condition['beginPrintDate']}"
							class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
							style="width: 80px;" /> 至  <input type="text"
							id="studentPaymentDetailReport_endPrintDate" name="endPrintDate"
							value="${condition['endPrintDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
							<li><label>是否打印：</label> <gh:select name="isPrint"
								id="studentPaymentDetailReport_isPrint"
								value="${condition['isPrint']}" dictionaryCode="yesOrNo"
								style="width:52%;" /></li>
							<li class="custom-li"><label>票据号：</label> <input type="text"
							id="studentPaymentDetailReport_receiptNumber_begin"
							name="receiptNumber_begin"
							value="${condition['receiptNumber_begin']}" style="width: 80px;" /> 
							至 <input type="text"
							id="studentPaymentDetailReport_receiptNumber_end"
							name="receiptNumber_end"
							value="${condition['receiptNumber_end']}" style="width: 80px;" /></li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems" id="studentPaymentDetailReport_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
						<li>
							<label>开发票：</label> 
							<gh:select name="isInvoicing" id="studentPaymentDetailReport_isInvoicing" value="${condition['isInvoicing']}" dictionaryCode="yesOrNo" style="width:52%;" />
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
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENTDETAIL_REPORT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="233">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_studentPaymentDetail"
							onclick="checkboxAll('#check_all_studentPaymentDetail','resourceid','#studentPaymentDetailReportBody')" /></th>
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
				<tbody id="studentPaymentDetailReportBody">
					<c:forEach items="${studentPaymentDetailsList.result}"
						var="studentPaymentDetail" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${studentPaymentDetail.resourceid }" autocomplete="off" /></td>
							<td
								title="${studentPaymentDetail.studentInfo.branchSchool.unitName }">${studentPaymentDetail.studentInfo.branchSchool.unitName }</td>
							<td title="${studentPaymentDetail.receiptNumber }"><c:if
									test="${(studentPaymentDetail.receiptNumber ne '') and(studentPaymentDetail.receiptNumber ne null) }">
		        			${prefix}${studentPaymentDetail.receiptNumber }
		        	</c:if> <c:if
									test="${(studentPaymentDetail.receiptNumber eq '') or (studentPaymentDetail.receiptNumber eq null) }">
							${studentPaymentDetail.receiptNumber }
		        	</c:if></td>
							<td title="${studentPaymentDetail.year }">${studentPaymentDetail.year }</td>
							<td
								title="${ghfn:dictCode2Val('CodePaymentMethod',studentPaymentDetail.paymentMethod) }">${ghfn:dictCode2Val("CodePaymentMethod",studentPaymentDetail.paymentMethod) }</td>
							<%-- <td title="${studentPaymentDetail.cardType }">${studentPaymentDetail.cardType }</td>--%>
							<td
								title="${studentPaymentDetail.studentInfo!=null?studentPaymentDetail.studentInfo.examCertificateNo:'' }">${studentPaymentDetail.studentInfo!=null?studentPaymentDetail.studentInfo.examCertificateNo:'' }</td>
							<td title="(${studentPaymentDetail.studentInfo.studyNo }">${studentPaymentDetail.studentInfo.studyNo }</td>
							<td title="${studentPaymentDetail.studentInfo.studentName }">${studentPaymentDetail.studentInfo.studentName }</td>
							<td
								title="${studentPaymentDetail.studentInfo.classic.classicName }">${studentPaymentDetail.studentInfo.classic.classicName }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',studentPaymentDetail.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',studentPaymentDetail.chargingItems) }</td>
							<td title="${studentPaymentDetail.drawer }">${studentPaymentDetail.drawer }</td>
							<td
								title="<fmt:formatDate value='${studentPaymentDetail.operateDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${studentPaymentDetail.operateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td
								title="<fmt:formatNumber value='${studentPaymentDetail.payAmount }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPaymentDetail.payAmount }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${studentPaymentDetail.chargeMoney }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPaymentDetail.chargeMoney }" pattern="####.##" /></td>
							<td
								title="<fmt:formatDate value='${studentPaymentDetail.printDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${studentPaymentDetail.printDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td title="${ghfn:dictCode2Val('yesOrNo',studentPaymentDetail.isInvoicing) }">${ghfn:dictCode2Val('yesOrNo',studentPaymentDetail.isInvoicing) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${studentPaymentDetailsList}"
				goPageUrl="${baseUrl }/edu3/finance/studentPaymentDetails/reportList.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>