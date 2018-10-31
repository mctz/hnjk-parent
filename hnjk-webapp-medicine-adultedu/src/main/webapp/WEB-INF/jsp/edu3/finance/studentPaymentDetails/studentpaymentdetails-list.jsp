<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费详细信息</title>
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
	var selectIdsJson = "{unitId:'studentPaymentDetail_brSchool',gradeId:'studentPaymentDetail_gradeid',classicId:'studentPaymentDetail_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetail_majorid',classesId:'studentPaymentDetail_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function studentPaymentDetailQueryUnit() {
	var defaultValue = $("#studentPaymentDetail_brSchool").val();
	var selectIdsJson = "{gradeId:'studentPaymentDetail_gradeid',classicId:'studentPaymentDetail_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetail_majorid',classesId:'studentPaymentDetail_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function studentPaymentDetailQueryGrade() {
	var defaultValue = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var selectIdsJson = "{classicId:'studentPaymentDetail_classicid',teachingType:'id~teachingType',majorId:'studentPaymentDetail_majorid',classesId:'studentPaymentDetail_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function studentPaymentDetailQueryClassic() {
	var defaultValue = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var classicId = $("#studentPaymentDetail_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'studentPaymentDetail_majorid',classesId:'studentPaymentDetail_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function studentPaymentDetailQueryMajor() {
	var defaultValue = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var classicId = $("#studentPaymentDetail_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#studentPaymentDetail_majorid").val();
	var selectIdsJson = "{classesId:'studentPaymentDetail_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

//根据查询条件打印缴费明细记录
function queryPrint(){
	var brSchool = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var classicId = $("#studentPaymentDetail_classicid").val();
	var majorId = $("#studentPaymentDetail_majorid").val();
	var name = $("#studentPaymentDetail_name").val();
	 var studyNo = $("#studentPaymentDetail_studyNo").val(); 
	var studentStatus = $("#studentPaymentDetail_studentStatus").val();
	var payDate = $("#studentPaymentDetail_payDate").val();
	
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/quaryPrint.html?brSchool="+brSchool+"&gradeId="+gradeId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId+"&name="+name+"&studyNo="+studyNo+"&studentStatus="+studentStatus+"&payDate="+payDate;
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT", "根据查询条件打印缴费明细记录",{width:200,height:100,mask:true});
}

//根据勾选打印缴费明细记录
function selectPrint(){
	var detailIds = new Array();
	jQuery("#studentPaymentDetailBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/quaryPrint.html?detailIds="+detailIds.toString();
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT", "根据查询条件打印缴费明细记录",{width:200,height:100,mask:true});
}

//合并勾选查询
function printDetailPayment(){
	var brSchool = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var classicId = $("#studentPaymentDetail_classicid").val();
	var majorId = $("#studentPaymentDetail_majorid").val();
	var name = $("#studentPaymentDetail_name").val();
	var studyNo = $("#studentPaymentDetail_studyNo").val(); 
	var studentStatus = $("#studentPaymentDetail_studentStatus").val();
	var beginDate = $("#studentPaymentDetail_beginDate").val();
	var endDate = $("#studentPaymentDetail_endDate").val();
	
	var detailIds = new Array();
	jQuery("#studentPaymentDetailBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/quaryPrint.html?brSchool="+brSchool+"&gradeId="+gradeId+"&classicId="+classicId+"&majorId="+majorId+"&gradeId="+gradeId+"&name="+name+"&studyNo="+studyNo+"&studentStatus="+studentStatus+"&beginDate="+beginDate+"&endDate="+endDate+"&detailIds="+detailIds.toString();
	$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT", "根据条件打印缴费明细记录",{width:200,height:100,mask:true});
}

//导出
function exportstuPaymentRecord(){
	var brSchool = $("#studentPaymentDetail_brSchool").val();
	var gradeId = $("#studentPaymentDetail_gradeid").val();
	var classicId = $("#studentPaymentDetail_classicid").val();
	var majorId = $("#studentPaymentDetail_majorid").val();
	var name = $("#studentPaymentDetail_name").val();
	var studyNo = $("#studentPaymentDetail_studyNo").val(); 
	var studentStatus = $("#studentPaymentDetail_studentStatus").val();
	var beginDate = $("#studentPaymentDetail_beginDate").val();
	var endDate = $("#studentPaymentDetail_endDate").val();
	
	var detailIds = new Array();
	jQuery("#studentPaymentDetailBody input[name='resourceid']:checked").each(function(){
		detailIds.push(jQuery(this).val());
	});
	var url = "${baseUrl}/edu3/finance/studentPaymentDetails/export.html?brSchool="+brSchool+"&gradeid="+gradeId+"&classicid="+classicId+"&majorid="+majorId+"&name="+name+"&studyNo="+studyNo+"&studentStatus="+studentStatus+"&beginDate="+beginDate+"&endDate="+endDate+"&detailIds="+detailIds.toString()+"&type=record&operateType=export";
	alertMsg.confirm("您确定要导出这些记录吗？",{
		okCall:function(){
			downloadFileByIframe(url);
		}
	}); 
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="studentPaymentDetail_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentPaymentDetails/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span
							sel-id="studentPaymentDetail_brSchool" sel-name="brSchool"
							sel-onchange="studentPaymentDetailQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span
							sel-id="studentPaymentDetail_gradeid" sel-name="gradeid"
							sel-onchange="studentPaymentDetailQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="studentPaymentDetail_classicid" sel-name="classicid"
							sel-onchange="studentPaymentDetailQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="studentPaymentDetail_majorid" sel-name="majorid"
							sel-onchange="studentPaymentDetailQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>姓名：</label><input id="studentPaymentDetail_name"
							type="text" name="name" value="${condition['name']}"
							style="width: 52%;" /></li>
						<li><label>学号：</label><input
							id="studentPaymentDetail_studyNo" type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 52%;" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="studentPaymentDetail_studentStatus"
								value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:100px;"
								excludeValue="14,15,17,19,20,22" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="studentPaymentDetail_classesid" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li><label>缴费开始日期：</label> <input type="text"
							id="studentPaymentDetail_beginDate" name="beginDate"
							value="${condition['beginDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" width="53%" /></li>

						<li><label>缴费结束日期：</label> <input type="text"
							id="studentPaymentDetail_endDate" name="endDate"
							value="${condition['endDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" width="53%" /></li>
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems" id="studentPaymentDetail_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:53%;" />
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
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENTDETAIL_RECORD"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentPaymentDetail"
							onclick="checkboxAll('#check_all_studentPaymentDetail','resourceid','#studentPaymentDetailBody')" /></th>
						<th width="7%">学号</th>
						<th width="6%">学生姓名</th>
						<th width="6%">教学点</th>
						<th width="4%">年级</th>
						<th width="4%">层次</th>
						<th width="6%">专业</th>
						<th width="5%">办学模式</th>
						<th width="5%">学籍状态</th>
						<th width="4%">收费项</th>
						<c:choose>
							<c:when test="${payType=='2' }">
								<th width="6%">票据号</th>
								<th width="6%">终端号</th>
								<th width="6%">pos流水号</th>
								<th width="6%">银行卡号</th>
								<th width="6%">缴费金额</th>
								<th width="4%">收款方式</th>
								<th width="6%">缴费时间</th>
								<th width="6%">经手人</th>
							</c:when>
							<c:otherwise>
								<th width="5%">应缴金额</th>
								<th width="5%">缴费金额</th>
								<th width="5%">已缴金额</th>
								<th width="5%">欠费金额</th>
								<th width="10%">缴费时间</th>
								<th width="6%">操作人</th>
							</c:otherwise>
						</c:choose>
					</tr>
				</thead>
				<tbody id="studentPaymentDetailBody">
					<c:forEach items="${studentPaymentDetailsList.result}"
						var="studentPaymentDetail" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${studentPaymentDetail.resourceid }" autocomplete="off" /></td>
							<td title="(${studentPaymentDetail.studentInfo.studyNo }">${studentPaymentDetail.studentInfo.studyNo }</td>
							<td title="${studentPaymentDetail.studentInfo.studentName }">${studentPaymentDetail.studentInfo.studentName }</td>
							<td
								title="${studentPaymentDetail.studentInfo.branchSchool.unitName }">${studentPaymentDetail.studentInfo.branchSchool.unitName }</td>
							<td title="${studentPaymentDetail.studentInfo.grade.gradeName }">${studentPaymentDetail.studentInfo.grade.gradeName }</td>
							<td
								title="${studentPaymentDetail.studentInfo.classic.classicName }">${studentPaymentDetail.studentInfo.classic.classicName }</td>
							<td title="${studentPaymentDetail.studentInfo.major.majorName }">${studentPaymentDetail.studentInfo.major.majorName }</td>
							<td
								title="${ghfn:dictCode2Val('CodeTeachingType',studentPaymentDetail.studentInfo.teachingType) }">${ghfn:dictCode2Val('CodeTeachingType',studentPaymentDetail.studentInfo.teachingType) }</td>
							<td
								title="${ghfn:dictCode2Val('CodeStudentStatus',studentPaymentDetail.studentInfo.studentStatus) }"
								style="color: 
		        			<c:if test="${studentPaymentDetail.studentInfo.studentStatus!=11 }">red</c:if>
		        			<c:if test="${studentPaymentDetail.studentInfo.studentStatus==25 }">blue</c:if>;">
								${ghfn:dictCode2Val('CodeStudentStatus',studentPaymentDetail.studentInfo.studentStatus) }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',studentPaymentDetail.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',studentPaymentDetail.chargingItems) }</td>
							<c:choose>
								<c:when test="${payType=='2' }">
									<td title="${studentPaymentDetail.receiptNumber }">${studentPaymentDetail.receiptNumber }</td>
									<td title="${studentPaymentDetail.carrTermNum }">${studentPaymentDetail.carrTermNum }</td>
									<td title="${studentPaymentDetail.posSerialNumber }">${studentPaymentDetail.posSerialNumber }</td>
									<td title="${studentPaymentDetail.carrCardNo }">${studentPaymentDetail.carrCardNo }</td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetail.payAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetail.payAmount }" pattern="####.##" /></td>
									<td
										title="${ghfn:dictCode2Val('CodePaymentMethod',studentPaymentDetail.paymentMethod) }">${ghfn:dictCode2Val("CodePaymentMethod",studentPaymentDetail.paymentMethod) }</td>
								</c:when>
								<c:otherwise>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetail.shouldPayAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetail.shouldPayAmount }"
											pattern="####.##" /></td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetail.payAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetail.payAmount }" pattern="####.##" /></td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetail.paidAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetail.paidAmount }" pattern="####.##" /></td>
									<td
										title="<fmt:formatNumber value='${studentPaymentDetail.shouldPayAmount-studentPaymentDetail.paidAmount }' pattern='####.##' />"><fmt:formatNumber
											value="${studentPaymentDetail.shouldPayAmount-studentPaymentDetail.paidAmount  }"
											pattern="####.##" /></td>
								</c:otherwise>
							</c:choose>
							<td
								title="<fmt:formatDate value='${studentPaymentDetail.operateDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
									value="${studentPaymentDetail.operateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td title="${studentPaymentDetail.operatorName }">${studentPaymentDetail.operatorName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${studentPaymentDetailsList}"
				goPageUrl="${baseUrl }/edu3/finance/studentPaymentDetails/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>