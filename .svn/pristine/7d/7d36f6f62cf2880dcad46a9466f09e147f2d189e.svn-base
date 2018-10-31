<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生年度缴费标准管理</title>
<script type="text/javascript">
$(document).ready(function(){
	annualFeesQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function annualFeesQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'annualFees_brSchool',gradeId:'annualFees_gradeid',classicId:'annualFees_classicid',teachingType:'id~teachingType',majorId:'annualFees_majorid',classesId:'annualFees_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function annualFeesQueryUnit() {
	var defaultValue = $("#annualFees_brSchool").val();
	var selectIdsJson = "{gradeId:'annualFees_gradeid',classicId:'annualFees_classicid',teachingType:'id~teachingType',majorId:'annualFees_majorid',classesId:'annualFees_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function annualFeesQueryGrade() {
	var defaultValue = $("#annualFees_brSchool").val();
	var gradeId = $("#annualFees_gradeid").val();
	var selectIdsJson = "{classicId:'annualFees_classicid',teachingType:'id~teachingType',majorId:'annualFees_majorid',classesId:'annualFees_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function annualFeesQueryClassic() {
	var defaultValue = $("#annualFees_brSchool").val();
	var gradeId = $("#annualFees_gradeid").val();
	var classicId = $("#annualFees_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'annualFees_majorid',classesId:'annualFees_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function annualFeesQueryMajor() {
	var defaultValue = $("#annualFees_brSchool").val();
	var gradeId = $("#annualFees_gradeid").val();
	var classicId = $("#annualFees_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#annualFees_majorid").val();
	var selectIdsJson = "{classesId:'annualFees_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
//修改应缴金额
function updateRecpayFee(){
	if(isCheckOnlyone('resourceid','#annualFeesBody')){
		var annualFeesId = $("#annualFeesBody input[@name='resourceid']:checked").val();
       // TODO:只有学费才能操作
       
       xxx
		var url = "${baseUrl}/edu3/finance/annualFees/prepareUpdate.html?annualFeesId="+annualFeesId+"&type=update";
		$.pdialog.open(url,"RES_FINANCE_ANNUALFEES_UPDATE", "勾选修改应缴金额",{width:200,height:200,mask:true});
	}
}
//勾选退费
function setRefund(){
	if(isCheckOnlyone('resourceid','#annualFeesBody')){
		var chargeStatus = $("#annualFeesBody input:checked").attr("chargeStatus");
		 if(chargeStatus==0){
			alertMsg.warn("未缴费的学生无法退费!"); 
			return ;
		} 
		var annualFeesId = $("#annualFeesBody input[@name='resourceid']:checked").val();	
		//var recpayFee = $("#annualFeesBody input:checked").attr("recpayFee");
		//var facepayFee = $("#annualFeesBody input:checked").attr("facepayFee");
		var url = "${baseUrl}/edu3/finance/annualFees/prepareUpdate.html?annualFeesId="+annualFeesId+"&type=refund";
		$.pdialog.open(url,"RES_FINANCE_ANNUALFEES_REFUND", "勾选退还费用",{width:200,height:200,mask:true});
	}
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="annualFees_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/annualFees/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span sel-id="annualFees_brSchool"
							sel-name="brSchool" sel-onchange="annualFeesQueryUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span sel-id="annualFees_gradeid"
							sel-name="gradeid" sel-onchange="annualFeesQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="annualFees_classicid"
							sel-name="classicid" sel-onchange="annualFeesQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						
						<li class="custom-li"><label>专业：</label> <span sel-id="annualFees_majorid"
							sel-name="majorid" sel-onchange="annualFeesQueryMajor()"
							sel-classs="flexselect"></span></li>
						
						<li><label>姓名：</label><input id="annualFees_name" type="text"
							name="name" value="${condition['name']}" style="width: 52%;" /></li>
						<li><label>学号：</label><input id="annualFees_studyNo"
							type="text" name="studyNo" value="${condition['studyNo']}"
							style="width: 52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="annualFees_classesid"
							sel-name="classesId" sel-classs="flexselect"></span></li>
						<li><label>年度：</label> <gh:selectModel id="annualFees_yearId"
								name="yearId" bindValue="resourceid" displayValue="yearName"
								orderBy="firstYear desc" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" /></li>
						<li><label>缴费状态：</label>
						<gh:select name="chargeStatus" id="annualFees_chargeStatus"
								dictionaryCode="CodeChargeStatus"
								value="${condition['chargeStatus']}" style="width:52%;" /></li>
						<li>
							<label>收费项：</label>
							<gh:select dictionaryCode="CodeChargingItems" id="annualFees_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_ANNUALFEES" pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_studentPayment"
							onclick="checkboxAll('#check_all_studentPayment','resourceid','#annualFeesBody')" /></th>
						<th width="10%">学号</th>
						<th width="8%">学生姓名</th>
						<th width="12%">教学点</th>
						<th width="5%">学年</th>
						<th width="5%">年级</th>
						<th width="5%">层次</th>
						<th width="14%">专业</th>
						<th width="5%">收费项</th>
						<th width="6%">应缴金额</th>
						<th width="6%">已缴金额</th>
						<th width="5%">退费金额</th>
						<th width="5%">补交金额</th>
						<th width="5%">欠费金额</th>
						<th width="5%">缴费状态</th>
					</tr>
				</thead>
				<tbody id="annualFeesBody">
					<c:forEach items="${annualFeesList.result}" var="annualFees"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${annualFees.resourceid }"
								recpayFee="${annualFees.recpayFee }"
								facepayFee="${annualFees.facepayFee }"
								chargeStatus="${annualFees.chargeStatus }" autocomplete="off" /></td>
							<td title="${annualFees.studyNo }">${annualFees.studentInfo.studyNo }</td>
							<td title="${annualFees.studentInfo.studentName }">${annualFees.studentInfo.studentName }</td>
							<td title="${annualFees.studentInfo.branchSchool.unitName }">${annualFees.studentInfo.branchSchool.unitName }</td>
							<td title="${annualFees.yearInfo.firstYear }">${annualFees.yearInfo.firstYear }</td>
							<td title="${annualFees.studentInfo.grade.gradeName }">${annualFees.studentInfo.grade.gradeName }</td>
							<td title="${annualFees.studentInfo.classic.classicName }">${annualFees.studentInfo.classic.classicName }</td>
							<td title="${annualFees.studentInfo.major.majorName }">${annualFees.studentInfo.major.majorName }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',annualFees.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',annualFees.chargingItems) }</td>	
							<td
								title="<fmt:formatNumber value='${annualFees.recpayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${annualFees.recpayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${annualFees.facepayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${annualFees.facepayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${annualFees.returnPremiumFee }' pattern='####.##' />"><fmt:formatNumber
									value="${annualFees.returnPremiumFee eq null?0:annualFees.returnPremiumFee}"
									pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${annualFees.payAmount }' pattern='####.##' />">
								<fmt:formatNumber value="${annualFees.payAmount }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${annualFees.unpaidFee }' pattern='####.##' />"><fmt:formatNumber
									value="${annualFees.unpaidFee }" pattern="####.##" /></td>
							<td
								title="${ghfn:dictCode2Val('CodeChargeStatus',annualFees.chargeStatus) }">${ghfn:dictCode2Val('CodeChargeStatus',annualFees.chargeStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${annualFeesList}"
				goPageUrl="${baseUrl }/edu3/finance/annualFees/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>