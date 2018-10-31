<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费标准管理</title>
<script type="text/javascript">
$(document).ready(function(){
	studentPaymentQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function studentPaymentQueryBegin() {
	var defaultValue = "${condition['brSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['majorid']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'studentPayment_brSchool',gradeId:'studentPayment_gradeid',classicId:'studentPayment_classicid',teachingType:'id~teachingType',majorId:'studentPayment_majorid',classesId:'studentPayment_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function studentPaymentQueryUnit() {
	var defaultValue = $("#studentPayment_brSchool").val();
	var selectIdsJson = "{gradeId:'studentPayment_gradeid',classicId:'studentPayment_classicid',teachingType:'id~teachingType',majorId:'studentPayment_majorid',classesId:'studentPayment_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function studentPaymentQueryGrade() {
	var defaultValue = $("#studentPayment_brSchool").val();
	var gradeId = $("#studentPayment_gradeid").val();
	var selectIdsJson = "{classicId:'studentPayment_classicid',teachingType:'id~teachingType',majorId:'studentPayment_majorid',classesId:'studentPayment_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function studentPaymentQueryClassic() {
	var defaultValue = $("#studentPayment_brSchool").val();
	var gradeId = $("#studentPayment_gradeid").val();
	var classicId = $("#studentPayment_classicid").val();
	var selectIdsJson = "{teachingType:'id~teachingType',majorId:'studentPayment_majorid',classesId:'studentPayment_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function studentPaymentQueryMajor() {
	var defaultValue = $("#studentPayment_brSchool").val();
	var gradeId = $("#studentPayment_gradeid").val();
	var classicId = $("#studentPayment_classicid").val();
	var teachingTypeId = $("#id~teachingType").val();
	var majorId = $("#studentPayment_majorid").val();
	var selectIdsJson = "{classesId:'studentPayment_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}

	//修改
	function modifyStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')=='Y' 
					&& $("#studentPaymentBody input[@name='resourceid']:checked").attr('isMultiDefer')=='Y'){//多次缓缴记录
				alertMsg.warn("多次缓缴记录请在原标准的多次缓缴管理进行设置.");	
				return false;
			} else {
				navTab.openTab('RES_FINANCE_STUDENTPAYMENT_INPUT', "${baseUrl}/edu3/finance/studentpayment/input.html?resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), '修改学生缴费标准');	
			}		
		}
	}
	//减免
	function derateStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else {
				navTab.openTab('RES_FINANCE_STUDENTPAYMENT_INPUT', "${baseUrl}/edu3/finance/studentpayment/input.html?isDerate=Y&resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), '修改学生缴费标准');	
			}		
		}
	}
	//导出
	function exportStudentPayment(){
		var isempty = true;
	//	$("#studentPayment_search_form [name]").each(function (){
	//		if($(this).val()!=""){
	//			isempty = false;//查询条件不为空
	//		}
	//	});
	//	if(isempty){
	//		alertMsg.warn("请选择一个条件进行导出.");
	//		return false;
	//	}
		var name1 = encodeURIComponent(encodeURIComponent($("#name1").val()));
		var url = "${baseUrl}/edu3/finance/studentpayment/defere/export.html?"+$("#studentPayment_search_form").serialize()+"&name1="+name1;
		alertMsg.confirm("您确定要按查询条件导出学生缴费记录吗？",{
			okCall:function(){
				downloadFileByIframe(url,"studentExerciseStatExportIframe");
			}
		});
	}
	//导入缴费信息
	function importPay(){
		var url = "${baseUrl}/edu3/finance/studentpayment/upload.html";
		$.pdialog.open(url,"RES_RECRUIT_EXAMEEINFO_FEE_IMP","导入缴费信息",{width:480, height:320});		
	}
	
	//导入缴费信息（右江医）
	function importPay1(){
		var url = "${baseUrl}/edu3/finance/studentpayment/upload1.html";
		$.pdialog.open(url,"RES_RECRUIT_EXAMEEINFO_FEE_IMP1","导入缴费信息",{width:480, height:320});	
	}
	
	//设置缓缴期限
	function deferStudentPayment(deferType){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录的缓缴记录.");	
				return false;
			} else {
				var res = $("#studentPaymentBody input[@name='resourceid']:checked").val();
				if(deferType==0){//取消缓缴					
					if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')!='Y'){
						alertMsg.warn("请选择一条缓缴标准.");	
						return false;
					} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('isMultiDefer')=='Y'){//多次缓缴记录
						alertMsg.warn("请在原标准的多次缓缴管理进行设置.");	
						return false;
					} else {
						$.post("${baseUrl}/edu3/finance/studentpayment/deferenddate/edit.html",{resourceid:res,deferType:0},navTabAjaxDone,"json");	
					}					
				} else {//设置缓缴期限
					alertMsg.confirm("缓缴期限：<input type='text' id='studentPayment_list_deferEndDate' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d}'})\">",{okCall:function (){
						var deferEndDate = $("#studentPayment_list_deferEndDate").val();
						if(deferEndDate!=""){									
							$.post("${baseUrl}/edu3/finance/studentpayment/deferenddate/edit.html",{resourceid:res,deferEndDate:deferEndDate,deferType:1},navTabAjaxDone,"json");
						} else {
							deferStudentPayment();//重新选择时间
						}
					},okName:'设置'});
				}					
			}		
		}
	}
	//单独缴费
	function setPay(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')=='1'){//只能编辑欠费记录
				alertMsg.warn("请选择一条欠费记录.");	
				return false;
			}else {//修改缴费信息
				var studentPaymentId = $("#studentPaymentBody input[@name='resourceid']:checked").val();
				var url = "${baseUrl}/edu3/finance/studentpayment/pay_input.html?studentPaymentId="+studentPaymentId+"&pageNum=${page.pageNum}";
				$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENT_PAY","学生缴费",{mask:true,width:350, height:250});	
				/**
				var res = $("#studentPaymentBody input[@name='resourceid']:checked").val();
				alertMsg.confirm("已缴金额：<input type='text' id='studentPayment_list_setPay' style='width:40%'/><span id='error_message' style='color:red;'></span>",{okCall:function (){
					var pay = $("#studentPayment_list_setPay").val();
					if(pay!=""){
						$.ajax({
							type:"post",
						url:"${baseUrl}/edu3/finance/studentpayment/pay/edit.html",
						data:{resourceid:res,pay:pay},
							dataType:"json",
							success:function(data){
								if(data.statusCode==200){
									alertMsg.correct(data.message);
									navTabPageBreak(); 
								}else if(data.statusCode==400){
									setPay();
									$("#studentPayment_list_setPay").val(data.value);
									$("#error_message").text(data.message);
								}else {
									alertMsg.error(data.message);
								}
							}
						});
					} else {
						setPay();//重新设置
					}
				},okName:'确定'});
				**/
			}
		}
	}
	
	//多次缓缴管理
	function listDeferStudentPayment(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			if($("#studentPaymentBody input[@name='resourceid']:checked").attr('rel')!='0'){//只能编辑未缴费记录
				alertMsg.warn("请选择一条未缴费记录.");	
				return false;
			} else if($("#studentPaymentBody input[@name='resourceid']:checked").attr('defer')=='Y'){//缓缴记录
				alertMsg.warn("缓缴记录请在原标准的多次缓缴管理进行设置.");	
				return false;
			} else {
				$.pdialog.open("${baseUrl}/edu3/finance/studentpayment/defere/list.html?resourceid="+$("#studentPaymentBody input[@name='resourceid']:checked").val(), "studentPaymentDeferList", "多次缓缴管理",{width: 800, height: 600 });	
			}		
		}		
	}
	
	// 查看学生缴费明细
	function viewStudentPaymentDetails(studentInfoId,stuPaymentId) {
		var url = "${baseUrl}/edu3/finance/studentPaymentDetails/single-list.html?studentInfoId="+studentInfoId+"&stuPaymentId="+stuPaymentId;
		$.pdialog.open(url,"RES_SCHOOL_SCHOOLROLL_VIEWSTUDENTPAYMENTDETAILS", "查看学生缴费明细",{width:800,height:600,mask:true});
	}
	
	// 下载学生缴费模板
	function downloadPayTemplate() {
		var url = "${baseUrl}/edu3/finance/studentpayment/paymentTemplate.html";
		//$("#background,#progressBar").show();
		downloadFileByIframe(url,"downloadPaymentTemplateIframe"); 
	}
	
	// 跳转到pos机缴费页面
	/** TODO:	这个不用了
	function payByPos() {
		var stuPaymentIds = new Array();
		$("#studentPaymentBody input[@name='resourceid']:checked").each(function(){
			stuPaymentIds.push($(this).val());
		});
		
		if(stuPaymentIds.length <= 0){
			alertMsg.warn("请选择一条要操作的记录！");	
			return false;
		}
		if(stuPaymentIds.length > 1){
			alertMsg.warn("只能选择一条记录操作！");	
			return false;
		}
		
		var url = "${baseUrl}/edu3/finance/studentpayment/pos_payment_form.html?stuPaymentId="+stuPaymentIds.toString();
		$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENT_PAYMENT_POS", "POS机缴费",{width:800,height:600,mask:true,close:''});
	}
	**/
	// 生成缴费临时记录（供同步到第三方系统使用）
	function createPayTempStuFee(){
		var resourceIds = [];
		$("#studentPaymentBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var brSchool = $("#studentPayment_brSchool").val();
		var gradeId = $("#studentPayment_gradeid").val();
		var classicId = $("#studentPayment_classicid").val();
		var majorId = $("#studentPayment_majorid").val();
		var classesId = $("#studentPayment_classesid").val();
		var name = $("#name1").val();
		var studyNo = $("#studentPayment_studyNo").val();
		var studentStatus = $("#studentPayment_studentStatus").val();
		var chargeStatus = $("#studentPayment_chargeStatus").val();
		
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/finance/studentpayment/createPayTempStuFee.html",
	          data:{resourceIds:resourceIds,brSchool:brSchool,gradeId:gradeId,classicId:classicId,majorId:majorId,classesId:classesId,name:name,studyNo:studyNo,studentStatus:studentStatus,chargeStatus:chargeStatus},
	          dataType:  'json',
	          success:function(data){          	   		
	         		 if(data['statusCode'] === 200){         		 	
	         		 	 alertMsg.correct(data['message']);	  
	         		 }else{
	         			 alertMsg.error(data['message']);
	         		 }         
	          }            
		});
	}
	//勾选退费
	function refund(){
		if(isCheckOnlyone('resourceid','#studentPaymentBody')){
			var stuPaymentId = $("#studentPaymentBody input[@name='resourceid']:checked").val();
			var url = "${baseUrl}/edu3/finance/studentPayment/refund.html?stuPaymentId="+stuPaymentId;
			$.pdialog.open(url,"RES_FINANCE_STUDENTPAYMENT_REFUND", "勾选退还费用",{width:200,height:100,mask:true});
		}
	}
	
	// 处理异常缴费情况
	function payAbnormal() {
		var url = "${baseUrl}/edu3/finance/studentPayment/payAbnormal.html";
		navTab.openTab('RES_FINANCE_STUDENTPAYMENT_PAYABNORMAL', url, '处理异常缴费');
	}
	
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="studentPayment_search_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentpayment/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span
							sel-id="studentPayment_brSchool" sel-name="brSchool"
							sel-onchange="studentPaymentQueryUnit()" sel-classs="flexselect"
							></span></li>
						<li><label>年级：</label> <span sel-id="studentPayment_gradeid"
							sel-name="gradeid" sel-onchange="studentPaymentQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="studentPayment_classicid" sel-name="classicid"
							sel-onchange="studentPaymentQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="studentPayment_majorid"
							sel-name="majorid" sel-onchange="studentPaymentQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>学号：</label><input id="studentPayment_studyNo"
							type="text" name="studyNo" value="${condition['studyNo']}"
							style="width: 52%;" /></li>
						<li><label>姓名：</label><input id="name1" type="text"
							name="name" value="${condition['name']}" style="width: 52%;" /></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="studentPayment_classesid" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								id="studentPayment_studentStatus"
								value="${condition['studentStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px;"
								excludeValue="14,15,17,19,20,22" /></li>
						<li><label>缴费状态：</label>
						<gh:select name="chargeStatus" id="studentPayment_chargeStatus"
								dictionaryCode="CodeChargeStatus"
								value="${condition['chargeStatus']}" style="width:120px;" /></li>
					</ul>
					<ul class="searchContent">
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
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENT" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_studentPayment"
							onclick="checkboxAll('#check_all_studentPayment','resourceid','#studentPaymentBody')" /></th>
						<th width="9%">学号</th>
						<th width="6%">学生姓名</th>
						<th width="12%">教学点</th>
						<th width="6%">年级</th>
						<th width="5%">层次</th>
						<th width="10%">专业</th>
						<th width="5%">办学模式</th>
						<th width="5%">学籍状态</th>
						<th width="5%">应缴金额</th>
						<th width="5%">已缴金额</th>
						<th width="5%">欠费金额</th>
						<th width="5%">退费金额</th>
						<th width="5%">补交金额</th>
						<th width="5%">缴费状态</th>
						<th width="6%">缴费期限</th>
						<th width="9%">联系电话</th>
						<!--  <th width="10%">是否缓缴</th> -->
					</tr>
				</thead>
				<tbody id="studentPaymentBody">
					<c:forEach items="${studentPaymentList.result}"
						var="studentPayment" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${studentPayment.resourceid }"
								rel="${studentPayment.chargeStatus }"
								defer="${studentPayment.isDefer }"
								isMultiDefer="${studentPayment.isMultiDefer }"
								autocomplete="off" /></td>
							<td title="查看学生(${studentPayment.studyNo })缴费明细"
								onclick="viewStudentPaymentDetails('${studentPayment.studentInfo.resourceid}','${studentPayment.resourceid  }')"
								style="color: blue; cursor: pointer;">${studentPayment.studentInfo.studyNo }</td>
							<td title="${studentPayment.studentInfo.studentName }">${studentPayment.studentInfo.studentName }</td>
							<td title="${studentPayment.branchSchool.unitName }">${studentPayment.branchSchool.unitName }</td>
							<td title="${studentPayment.grade.gradeName }">${studentPayment.grade.gradeName }</td>
							<td title="${studentPayment.classic.classicName }">${studentPayment.classic.classicName }</td>
							<td title="${studentPayment.major.majorName }">${studentPayment.major.majorName }</td>
							<td
								title="${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }">${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }</td>
							<td
								title="${ghfn:dictCode2Val('CodeStudentStatus',studentPayment.studentInfo.studentStatus) }"
								style="color: 
		        			<c:if test="${studentPayment.studentInfo.studentStatus!=11 }">red</c:if>
		        			<c:if test="${studentPayment.studentInfo.studentStatus==25 }">blue</c:if>;">
								${ghfn:dictCode2Val('CodeStudentStatus',studentPayment.studentInfo.studentStatus) }</td>
							<td
								title="<fmt:formatNumber value='${studentPayment.recpayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.recpayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${studentPayment.facepayFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.facepayFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${studentPayment.unpaidFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.unpaidFee }" pattern="####.##" /></td>
							<td
								title="<fmt:formatNumber value='${studentPayment.returnPremiumFee }' pattern='####.##' />"><fmt:formatNumber
									value="${studentPayment.returnPremiumFee eq null?0:studentPayment.returnPremiumFee}"
									pattern="####.##" /></td>
							<td title="<fmt:formatNumber value='${studentPayment.payAmount }' pattern='####.##' />">
								<fmt:formatNumber value="${studentPayment.payAmount }" pattern="####.##" /></td>
							<td
								title="${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargeStatus) }"
								style="color: 
		        		<c:choose>
		        			<c:when test="${studentPayment.chargeStatus=='-1' }">red</c:when>
		        			<c:when test="${studentPayment.chargeStatus=='1' }">green</c:when>
		        			<c:otherwise></c:otherwise>
		        		</c:choose>;">
								${ghfn:dictCode2Val('CodeChargeStatus',studentPayment.chargeStatus) }
							</td>
							<td
								title="<fmt:formatDate value='${studentPayment.chargeTime }' pattern='yyyy-MM-dd' />"><fmt:formatDate
									value="${studentPayment.chargeTime }" pattern="yyyy-MM-dd" /></td>
							<td title="${studentPayment.studentInfo.studentBaseInfo.mobile }">${studentPayment.studentInfo.studentBaseInfo.mobile }</td>
							<!-- 
		        	<td title="${ghfn:dictCode2Val('yesOrNo',studentPayment.isDefer) }<c:if test='${studentPayment.isDefer eq "Y" }'>(<fmt:formatDate value='${studentPayment.deferEndDate }' pattern='yyyy-MM-dd' />前)</c:if>">
		        	${ghfn:dictCode2Val('yesOrNo',studentPayment.isDefer) }
		        	<c:if test="${studentPayment.isDefer eq 'Y' }">(<fmt:formatDate value="${studentPayment.deferEndDate }" pattern="yyyy-MM-dd" />前)</c:if>
		        	</td>
		        	-->

						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentPaymentList}"
				goPageUrl="${baseUrl }/edu3/finance/studentpayment/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>