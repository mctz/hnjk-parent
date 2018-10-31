<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生注册缴费信息</title>
<script type="text/javascript">

	$(document).ready(function(){
		tempStuInfoQueryBegin();
		
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function tempStuInfoQueryBegin() {
		var defaultValue = "${condition['unitId']}";
		var schoolId = "${linkageQuerySchoolId}";
		var gradeId = "${condition['gradeId']}";
		var classicId = "";
		var teachingType = "";
		var majorId = "${condition['majorId']}";
		var classesId = "";
		var selectIdsJson = "{unitId:'tempStudentFee_list_unitId',gradeId:'tempStudentFee_list_gradeId',majorId:'tempStudentFee_list_majorId'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function tempStuInfoQueryUnit() {
		var defaultValue = $("#tempStudentFee_list_unitId").val();
		var selectIdsJson = "{gradeId:'tempStudentFee_list_gradeId',majorId:'tempStudentFee_list_majorId'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function tempStuInfoQueryGrade() {
		var defaultValue = $("#tempStudentFee_list_unitId").val();
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var selectIdsJson = "{majorId:'tempStudentFee_list_majorId'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}
	
	// 同步学生注册缴费信息
	function syncTempStuFee(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		
		alertMsg.confirm("您确定要同步这些记录吗？", {
			okCall: function(){	
				$.ajax({
			          type:"POST",
			          url:"${baseUrl}/order/syncTempFee.html",
			          data:{resourceIds:resourceIds.toString(),gradeId:gradeId,studentName:studentName,unitId:unitId,eduOrederNo:eduOrederNo,batchNo:batchNo,majorId:majorId},
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
		});
		
	}
	
	// 补缴费记录
	function makeRecord(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		if(resourceIds.length<1){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		if(resourceIds.length>1){
			alertMsg.warn('只能选择一条记录操作！');
			return false;
		}
		
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/finance/tempStudentFee/makeRecord.html",
	          data:{resourceId:resourceIds.toString()},
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
	
	//打印条形码
	function printCode(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		
		
		var url = baseUrl+"/edu3/finance/tempStudentFee/barcodePrintview.html";
		
		$.pdialog.open(url+"?resourceIds="+resourceIds.toString()+"&gradeId="+gradeId+"&studentName="+studentName+"&unitId="+unitId+"&eduOrederNo="+eduOrederNo+"&batchNo="+batchNo+"&majorId="+majorId,'RES_FINANCE_TEMPSTUDENTFEE_PRINTCODE','打印预览',{height:600, width:800});
		
	}
	//查看条形码
	function queryCode(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		if(resourceIds.length<1){
			alertMsg.warn('请选择一条要操作记录！');
			return false;
		}
		if(resourceIds.length>1){
			alertMsg.warn('只能选择一条记录操作！');
			return false;
		}
		navTab.openTab('RES_FINANCE_TEMPSTUDENTFEE_QUERYCODE', "${baseUrl}/edu3/finance/tempStudentFee/queryBarcode.html?resourceid="+resourceIds.toString(), '查看条形码');
	}
	

	
	//导出学生信息
	function exportStudentMs(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		var isUploaded = $("#tempStudentFee_list_isUploaded").val();
		var payStatus = $("#tempStudentFee_list_payStatus").val();
		
		var url = baseUrl+"/edu3/finance/tempStudentFee/export-stu.html";
		alertMsg.confirm("您确定要导出这些记录吗？",{
			okCall:function(){
				downloadFileByIframe(url+"?resourceIds="+resourceIds+"&gradeId="+gradeId+"&studentName="+studentName+"&unitId="+unitId+"&eduOrederNo="+eduOrederNo+"&batchNo="+batchNo+"&isUploaded="+isUploaded+"&payStatus="+payStatus+"&majorId="+majorId,'tgradeIframe');
			}
		});
		
	}
	
	//注销订单信息
	function deleteOrder(){
		handleDeleteOrder("delete");
	}
	
	//导出
	function exportTempFee(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		var isUploaded = $("#tempStudentFee_list_isUploaded").val();
		var payStatus = $("#tempStudentFee_list_payStatus").val();
		
		var url = baseUrl+"/edu3/finance/tempStudentFee/export.html";
		alertMsg.confirm("您确定要导出这些记录吗？",{
			okCall:function(){
				downloadFileByIframe(url+"?resourceIds="+resourceIds+"&gradeId="+gradeId+"&studentName="+studentName+"&unitId="+unitId+"&eduOrederNo="+eduOrederNo+"&batchNo="+batchNo+"&isUploaded="+isUploaded+"&payStatus="+payStatus+"&majorId="+majorId,'tgradeIframe');
			}
		});
		
	}
	
	//导出缴费信息
	function exportStudentFee(){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		var isUploaded = $("#tempStudentFee_list_isUploaded").val();
		var payStatus = $("#tempStudentFee_list_payStatus").val();
		
		var url = baseUrl+"/edu3/finance/tempStudentFee/export-fee.html";
		alertMsg.confirm("您确定要导出这些记录吗？",{
			okCall:function(){
				downloadFileByIframe(url+"?resourceIds="+resourceIds+"&gradeId="+gradeId+"&studentName="+studentName+"&unitId="+unitId+"&eduOrederNo="+eduOrederNo+"&batchNo="+batchNo+"&isUploaded="+isUploaded+"&payStatus="+payStatus+"&majorId="+majorId,'tgradeIframe');
			}
		});
		
	}
	
	// 导入线下缴费
	function importPayOffLine() {
		// 检查是否在开放的时间段内
		$.ajax({
			type:"POST",
			url:"${baseUrl }/edu3/payTime/validData.html",
			data:{},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(data){
				if(data.statusCode==200){
					var url = "${baseUrl}/edu3/finance/tempStudentFee/importPayOfflineForm.html";
					$.pdialog.open(url,"RES_FINANCE_TEMPSTUDENTFEE_IMPORTPAYOFFLINE","导入线下缴费", {width:800, height:600});
					//navTab.openTab('RES_FINANCE_TEMPSTUDENTFEE_IMPORTPAYOFFLINE', url, '导入线下缴费');
				} else if(data.statusCode==400) {
					alertMsg.info(data.message);
				} else {
					alertMsg.error(data.message);
				}
			}
		});
	}
	
	// 审核通过
	function payOfflinePass(){
		pageBarHandle("您确定要将这些订单审核通过吗？",
				"${baseUrl}/edu3/finance/tempStudentFee/payOffLine.html?auditResult=pass",
				"#tempStudentFeeBody");
	}
	
	// 审核不通过
	function payOfflineNoPass(){
		pageBarHandle("您确定要将这些订单审核不通过吗？",
				"${baseUrl}/edu3/finance/tempStudentFee/payOffLine.html?auditResult=noPass",
				"#tempStudentFeeBody");
	}
	
	
	// 撤销预缴费订单信息
	function backoutOrder(){
		handleDeleteOrder("backout");
	}
	
	// 处理删除预缴费订单
	function handleDeleteOrder(operate){
		var resourceIds = [];
		$("#tempStudentFeeBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		
		var gradeId = $("#tempStudentFee_list_gradeId").val();
		var studentName = $("#tempStudentFee_list_studentName").val();
		var majorId = $("#tempStudentFee_list_majorId").val();
		var unitId = $("#tempStudentFee_list_unitId").val();
		var eduOrederNo = $("#tempStudentFee_list_eduOrederNo").val();
		var batchNo = $("#tempStudentFee_list_batchNo").val();
		var isUploaded = $("#tempStudentFee_list_isUploaded").val();
		var payStatus = $("#tempStudentFee_list_payStatus").val();
		
		var tip;
		if(operate=="delete"){
			tip = "您确定要注销这些记录吗？";
		} else {
			tip = "您确定要撤销这些记录吗？";
		}
		
		alertMsg.confirm(tip, {
			okCall: function(){	
				$.ajax({
			          type:"POST",
			          url:"${baseUrl}/order/deleteTempFee.html",
			          data:{resourceids:resourceIds.toString(),gradeId:gradeId,studentName:studentName,unitId:unitId,eduOrederNo:eduOrederNo,batchNo:batchNo,majorId:majorId,operate:operate},
			          dataType:  'json',
			          success:function(data){          	   		
			         		 if(data['statusCode'] === 200){         		 	
			         		 	 alertMsg.correct(data['message']);	  
			         		 	 navTabPageBreak();
			         		 }else{
			         			 alertMsg.error(data['message']);
			         		 }         
			          }            
				});
			}
		});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="tempStudentFee_list" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/tempStudentFee/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span
							sel-id="tempStudentFee_list_unitId" sel-name="unitId"
							sel-onchange="tempStuInfoQueryUnit()" sel-classs="flexselect"
							></span> <%-- 
					<label>教学点：</label>
					<c:choose>
						<c:when test="${isManger=='Y' }">
							<gh:brSchoolAutocomplete name="unitId" id="tempStudentFee_list_unitId" tabindex="1" displayType="code" defaultValue="${condition['unitId'] }"></gh:brSchoolAutocomplete>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="unitId" id="tempStudentFee_list_unitId" value="${condition['unitId']}" style="width:52%;"/>
							<input type="text"  value="${condition['unitName']}" style="width:52%;" readonly="readonly"/>
						</c:otherwise>
					</c:choose>
					--%></li>
						<li><label>年级：</label> <span
							sel-id="tempStudentFee_list_gradeId" sel-name="gradeId"
							sel-onchange="tempStuInfoQueryGrade()" sel-style="width: 52%"></span>
						</li>
						<li><label>批次号：</label> <input type="text" name="batchNo"
							id="tempStudentFee_list_batchNo" value="${condition['batchNo']}"
							style="width: 52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="tempStudentFee_list_majorId" sel-name="majorId"
							sel-classs="flexselect"></span></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							id="tempStudentFee_list_studentName"
							value="${condition['studentName']}" style="width: 52%;" /></li>
						<li><c:choose>
								<c:when test="${uniqueId==0 }">
									<label>准考证号：</label>
								</c:when>
								<c:otherwise>
									<label>考生号：</label>
								</c:otherwise>
							</c:choose> <input type="text" name="examCertificateNo"
							id="tempStudentFee_list_examCertificateNo"
							value="${condition['examCertificateNo']}" style="width: 52%;" />
						</li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>订单号：</label> <input type="text" name="eduOrederNo"
							id="tempStudentFee_list_eduOrederNo" 
							value="${condition['eduOrederNo']}" class="custom-inp" /></li>
						<li><label>身份证号：</label> 
						<input type="text" name="certNum" id="tempStudentFee_list_certNum" value="${condition['certNum']}" style="width: 52%;" /></li>
						
						<li><label>支付状态：</label> <gh:select
								dictionaryCode="CodePayStatus"
								id="tempStudentFee_list_payStatus" name="payStatus"
								value="${condition['payStatus'] }" style="width:52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>已同步：</label> <gh:select dictionaryCode="yesOrNo"
								id="tempStudentFee_list_isUploaded" name="isUploaded"
								value="${condition['isUploaded'] }" style="width:52%;" /></li>
						<li><label>是否对账：</label> <gh:select dictionaryCode="yesOrNo"
								id="tempStudentFee_list_isReconciliation" name="isReconciliation"
								value="${condition['isReconciliation'] }" style="width:52%;" /></li>
						<li>
							<label>处理状态：</label> 
							<gh:select dictionaryCode="CodeHandlePaymentStatus"
								id="tempStudentFee_list_handleStatus" name="handleStatus"
								value="${condition['handleStatus'] }" style="width:52%;" />
						</li>
						<li>
							<label>收费项：</label> 
							<gh:select dictionaryCode="CodeChargingItems"
								id="tempStudentFee_list_chargingItems" name="chargingItems"
								value="${condition['chargingItems'] }" style="width:52%;" />
						</li>
					</ul>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_TEMPSTUDENTFEE" pageType="list"></gh:resAuth>
			<table class="table" layouth="187">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall" id="check_all_tempStudentFee" onclick="checkboxAll('#check_all_tempStudentFee','resourceid','#tempStudentFeeBody')" /></th>
						<th width="13%">身份证号</th>
						<c:choose>
							<c:when test="${uniqueId==0 }">
								<th width="10%">准考证号</th>
							</c:when>
							<c:otherwise>
								<th width="10%">考生号</th>
							</c:otherwise>
						</c:choose>
						<th width="6%">姓名</th>
						<th width="10%">教学点</th>
						<th width="5%">年级</th>
						<th width="6%">专业</th>
						<th width="6%">应缴总额</th>
						<th width="8%">教育系统订单号</th>
						<th width="9%">学校订单号</th>
						<th width="5%">批次编号</th>
						<th width="5%">收费项</th>
						<th width="3%">已同步</th>
						<th width="4%">支付状态</th>
						<th width="3%">对账状态</th>
						<th width="4%">处理状态</th>
					</tr>
				</thead>
				<tbody id="tempStudentFeeBody">
					<c:forEach items="${studentFeeList.result}" var="tempFee"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid" value="${tempFee.resourceid }" autocomplete="off" /></td>
							<td title="${tempFee.certNum }">${tempFee.certNum }</td>
							<td title="${tempFee.examCertificateNo }">${tempFee.examCertificateNo }</td>
							<td title="${tempFee.studentName }">${tempFee.studentName }</td>
							<td title="${tempFee.unit.unitName }">${tempFee.unit.unitName }</td>
							<td title="${tempFee.grade.gradeName }">${tempFee.grade.gradeName }</td>
							<td title="${tempFee.major.majorName }">${tempFee.major.majorName }</td>
							<td title="<fmt:formatNumber value='${tempFee.amount}' pattern='####.##' />">
							<fmt:formatNumber value="${tempFee.amount }" pattern="####.##" /></td>
							<td title="${tempFee.eduOrderNo }">${tempFee.eduOrderNo }</td>
							<td title="${tempFee.schoolOrderNo }">${tempFee.schoolOrderNo }</td>
							<td title="${tempFee.batchNo }">${tempFee.batchNo }</td>
							<td title="${ghfn:dictCode2Val('CodeChargingItems',tempFee.chargingItems) }">${ghfn:dictCode2Val('CodeChargingItems',tempFee.chargingItems) }</td>
							<td title="${ghfn:dictCode2Val('yesOrNo',tempFee.isUploaded) }">${ghfn:dictCode2Val('yesOrNo',tempFee.isUploaded) }</td>
							<td title="${ghfn:dictCode2Val('CodePayStatus',tempFee.payStatus) }">${ghfn:dictCode2Val('CodePayStatus',tempFee.payStatus) }</td>
							<td title="${ghfn:dictCode2Val('yesOrNo',tempFee.isReconciliation) }">${ghfn:dictCode2Val('yesOrNo',tempFee.isReconciliation) }</td>
							<td title="${ghfn:dictCode2Val('CodeHandlePaymentStatus',tempFee.handleStatus) }">${ghfn:dictCode2Val('CodeHandlePaymentStatus',tempFee.handleStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${studentFeeList}" goPageUrl="${baseUrl }/edu3/finance/tempStudentFee/list.html" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>