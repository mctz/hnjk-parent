<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预退费补交订单</title>
<script type="text/javascript">
$(document).ready(function(){
	refundbackQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function refundbackQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "";
	var selectIdsJson = "{unitId:'refundback_brSchool',gradeId:'refundback_gradeid',classicId:'refundback_classicid',teachingType:'id~teachingType',majorId:'refundback_majorid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function refundbackQueryUnit() {
	var defaultValue = $("#refundback_brSchool").val();
	var selectIdsJson = "{gradeId:'refundback_gradeid',classicId:'refundback_classicid',teachingType:'id~teachingType',majorId:'refundback_majorid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function refundbackQueryGrade() {
	var defaultValue = $("#refundback_brSchool").val();
	var gradeId = $("#refundback_gradeid").val();
	var selectIdsJson = "{classicId:'refundback_classicid',teachingType:'id~teachingType',majorId:'refundback_majorid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function refundbackQueryClassic() {
	var defaultValue = $("#refundback_brSchool").val();
	var gradeId = $("#refundback_gradeid").val();
	var classicId = $("#refundback_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'refundback_majorid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 导出退费汇总表
function exportRefundSummary() {
	exportRefundback("refundSummary");
}

// 按查询条件导出退费补交记录
function exportRefundbackInfo(){
	exportRefundback("info");
}

// 导出
function exportRefundback(operateType){
	// 学年
	var yearId = $("#refundback_yearId").val();
	// 时间段，开始时间
	var beginDate = $("#refundback_beginDate").val();
	// 结束时间
	var endDate = $("#refundback_endDate").val();
	
	if(operateType=="refundSummary" && !(yearId || beginDate || endDate)){
		alertMsg.info("学年或者异动时间不能为空！");
		return false;
	}
	
	var brSchool = $("#refundback_brSchool").val();
	var gradeId = $("#refundback_gradeId").val();
	var classicId = $("#refundback_classicId").val();
	var majorId = $("#refundback_majorId").val();
	var studentName = $("#refundback_studentName").val();
	var studyNo = $("#refundback_studyNo").val(); 
	var chargingItems = $("#refundback_chargingItems").val();
	var processType = $("#refundback_processType").val();
	var processStatus = $("#refundback_processStatus").val();
	// 异动类型
	var changeType = $("#refundback_changeType").val();
	
	var refundbackIds = new Array();
	jQuery("#refundbackBody input[name='resourceid']:checked").each(function(){
		refundbackIds.push(jQuery(this).val());
	});
	var url = "${baseUrl}/edu3/finance/refundback/export.html?operateType="+operateType;
	if(refundbackIds.length > 0){
		url += "&refundbackIds="+refundbackIds.toString();
	}
	url += "&brSchoolId="+brSchool+"&gradeId="+gradeId+"&classicId="+classicId+"&majorId="+majorId
			+"&studentName="+studentName+"&studyNo="+studyNo+"&yearId="+yearId+"&chargingItems="+chargingItems+"&processType="+processType
			+"&processStatus="+processStatus+"&changeType="+changeType+"&beginDate="+beginDate+"&endDate="+endDate;
	
	alertMsg.confirm("您确定要导出这些记录吗？",{
		okCall:function(){
			downloadFileByIframe(url);
		}
	}); 
}

// 处理
function handleRefundback() {
	refundback("handled");
}

// 回退
function rollbackRefundback() {
	refundback("rollback");
}

function refundback(type){
	var tip = "";
	if(type == "handled"){
		tip = "您确定要设置这些记录为已处理吗？";
	} else {
		tip = "您确定要设置这些记录为未处理吗？";
	}
	pageBarHandle(tip,"${baseUrl}/edu3/finance/refundback/handle.html?type="+type,"#refundbackBody");
}

// 删除，未处理的
function deleteRefundback() {
	pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/finance/refundback/delete.html","#refundbackBody");
}

// 修改金额
function editRefundback(){
	var refundbackIds = new Array();
	var processStatus;
	jQuery("#refundbackBody input[name='resourceid']:checked").each(function(){
		refundbackIds.push(jQuery(this).val());
		processStatus = $(this).attr("processStatus");
	});
	if(refundbackIds.length<1){
		alertMsg.info("请选择一条你要操作的记录！");
		return false;
	}
	if(refundbackIds.length>1){
		alertMsg.info("只能选择一条你要操作的记录！");
		return false;
	}
	if(processStatus=="handled"){
		alertMsg.info("只能编辑待处理的订单！");
		return false;
	}
	var url = "${baseUrl}/edu3/finance/refundback/edit.html";
	navTab.openTab('_blank', url+'?resourceid='+refundbackIds.toString(), '编辑退补订单金额');
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="refundback_search_form" onsubmit="return navTabSearch(this);" action="${baseUrl }/edu3/finance/refundback/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li">
							<label>教学点：</label> 
							<span sel-id="refundback_brSchool" sel-name="brSchool" sel-onchange="refundbackQueryUnit()" sel-classs="flexselect"></span>
						</li>
						<li>
							<label>年级：</label> 
							<span sel-id="refundback_gradeid" sel-name="gradeId" sel-onchange="refundbackQueryGrade()" sel-style="width: 120px"></span>
						</li>
						<li>
							<label>层次：</label> 
							<span sel-id="refundback_classicid" sel-name="classicId" sel-onchange="refundbackQueryClassic()"sel-style="width: 120px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li">
							<label>专业：</label> 
							<span sel-id="refundback_majorid" sel-name="majorId" sel-onchange="refundbackQueryMajor()" sel-classs="flexselect"></span>
						</li>
						<li>
							<label>姓名：</label>
							<input id="refundback_studentName" type="text" name="studentName" value="${condition['studentName']}" style="width: 52%;" />
						</li>
						<li>
							<label>学号：</label>
							<input id="refundback_studyNo" type="text" name="studyNo"value="${condition['studyNo']}" style="width: 52%;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>学年：</label> 
							<gh:selectModel id="refundback_yearId"  name="yearId" bindValue="resourceid" displayValue="firstYear"  value="${condition['yearId'] }" modelClass="com.hnjk.edu.basedata.model.YearInfo" orderBy="firstYear desc" classCss="required" style="width:30%" />
						</li>
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems" id="refundback_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
						<li>
							<label>处理类型：</label> 
							<gh:select dictionaryCode="CodeProcessType" id="refundback_processType" name="processType"
								value="${condition['processType'] }" style="width:52%;" />
						</li>
						<li>
							<label>处理状态：</label> 
							<gh:select dictionaryCode="CodeProcessStatus" id="refundback_processStatus" name="processStatus"
								value="${condition['processStatus'] }" style="width:52%;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>异动类型：</label> 
							<gh:select id="refundback_changeType" 	name="changeType" value="${condition['changeType']}"
								filtrationStr="23,81,82,13" dictionaryCode="CodeStudentStatusChange" style="width:52%;" />
						</li>
						<li class="custom-li">
							<label>异动时间：</label> 
							<input type="text" id="refundback_beginDate" name="beginDate" value="${condition['beginDate']}" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /> 
							至 <input type="text" id="refundback_endDate" name="endDate" value="${condition['endDate']}" class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 80px;" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_REFUNDBACK" pageType="list"></gh:resAuth>
			<table class="table" layouth="208">
				<thead>
					<tr>
						<th width="5%">
							<input type="checkbox" name="checkall" id="check_all_refundback"onclick="checkboxAll('#check_all_refundback','resourceid','#refundbackBody')" /></th>
						<th width="9%">学号</th>
						<th width="8%">姓名</th>
						<th width="10%">教学点</th>
						<th width="5%">年级</th>
						<th width="5%">层次</th>
						<th width="12%">专业</th>
						<th width="5%">学习形式</th>
						<th width="5%">学籍状态</th>
						<th width="5%">异动类型</th>
						<th width="5%">学年</th>
						<th width="5%">处理类型</th>
						<th width="5%">收费项</th>
						<th width="6%">金额</th>
						<th width="5%">付款方式</th>
						<th width="5%">处理状态</th>
					</tr>
				</thead>
				<tbody id="refundbackBody">
					<c:forEach items="${refundbackList.result}" var="refundback" varStatus="vs">
						<tr>
							<td>
								<input type="checkbox" name="resourceid" value="${refundback.resourceid }"  processStatus="${refundback.processStatus }" autocomplete="off" />
							</td>
							<td title="(${refundback.studentInfo.studyNo }">${refundback.studentInfo.studyNo }</td>
							<td title="${refundback.studentInfo.studentName }">${refundback.studentInfo.studentName }</td>
							<td title="${refundback.studentInfo.branchSchool.unitName }">${refundback.studentInfo.branchSchool.unitName }</td>
							<td title="${refundback.studentInfo.grade.gradeName }">${refundback.studentInfo.grade.gradeName }</td>
							<td title="${refundback.studentInfo.classic.classicName }">${refundback.studentInfo.classic.classicName }</td>
							<td title="${refundback.studentInfo.major.majorName }">${refundback.studentInfo.major.majorName }</td>
							<td title="${ghfn:dictCode2Val('CodeTeachingType',refundback.studentInfo.teachingType) }">${ghfn:dictCode2Val('CodeTeachingType',refundback.studentInfo.teachingType) }</td>
							<td title="${ghfn:dictCode2Val('CodeStudentStatus',refundback.studentInfo.studentStatus) }"
								style="color: 
			        			<c:if test="${refundback.studentInfo.studentStatus!=11 }">red</c:if>
			        			<c:if test="${refundback.studentInfo.studentStatus==25 }">blue</c:if>;">
								${ghfn:dictCode2Val('CodeStudentStatus',refundback.studentInfo.studentStatus) }</td>
							<td title="${ghfn:dictCode2Val('CodeStudentStatusChange',refundback.changeInfo.changeType) }">${ghfn:dictCode2Val('CodeStudentStatusChange',refundback.changeInfo.changeType) }</td>
							<td title="${refundback.yearInfo.firstYear }">${refundback.yearInfo.firstYear }</td>
							<td title="${ghfn:dictCode2Val('CodeProcessType',refundback.processType) }">${ghfn:dictCode2Val('CodeProcessType',refundback.processType) }</td>	
							<td title="${ghfn:dictCode2Val('CodeChargingItems',refundback.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',refundback.chargingItems) }</td>	
							<td title="<fmt:formatNumber value='${refundback.money }' pattern='####.##' />">
								<fmt:formatNumber value="${refundback.money }" pattern="####.##" />
							</td>
							<td title="${ghfn:dictCode2Val('CodePaymentMethod',refundback.paymentMethod) }">${ghfn:dictCode2Val('CodePaymentMethod',refundback.paymentMethod) }</td>	
							<td title="${ghfn:dictCode2Val('CodeProcessStatus',refundback.processStatus) }">${ghfn:dictCode2Val('CodeProcessStatus',refundback.processStatus) }</td>			
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${refundbackList}" goPageUrl="${baseUrl }/edu3/finance/refundback/list.html" pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>