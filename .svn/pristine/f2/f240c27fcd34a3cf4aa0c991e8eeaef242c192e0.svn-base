<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生收费管理</title>
<style type="text/css">
#printReceipt_main .receiptData {
	color: blue;
}
</style>
<gh:loadCom components="jqprint" />
<!-- TODO:没有用到页面打印插件注释掉
<script src="http://code.jquery.com/jquery-migrate-1.1.0.js"></script>

<script language="javascript" src="../../jscript/jquery.jqprint-0.3.js"></script>
 -->
<script type="text/javascript">
    // 确认缴费
	function affirmPayByPos() {
	   if(!(window.ActiveXObject || "ActiveXObject" in window)){   
		   alertMsg.warn("该功能只能在IE浏览器使用！");
   		   return false; 
	   }
    	var $payForm = $("#pos_payment_form");
    	if(!$payForm.valid()){
    		alertMsg.error(DWZ.msg["validateFormError"]);
    		return false; 
    	}
    	//默认工作目录为C:\\PaxPos，可通过以下函数进行设置
		Proxyocx.setWorkPath("C:\\PaxPos");
    	
    	$.ajax({
			type:"post",
			url:"${baseUrl }/edu3/finance/studentpayment/validData.html",
			data:$payForm.serialize(),
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success:function(data){
				if(data.statusCode==200){
					posTrade(data.moneyStr,data.timestamp,data.sign);
				} else if(data.statusCode==400) {
					alertMsg.info(data.message);
				} else {
					alertMsg.error(data.message);
				}
			}
		});
	}
    // 调用POS机
    function posTrade(moneyStr,timestamp,sign){
    	//第1位：交易类型（1）
		//第2位：支付方式（1）
		//第3-14位：交易金额（12）
		//第15-20位：原交易流水号（6）
		//第21-32位：系统参考号（12）
		//第33-36位：交易日期（4）
    	// 交易类型
    	var _cType = "0";
    	// 支付方式
		var _payType = "1";
    	var oldBatch = "000000"
    	// 系统参考号
    	var _carrTradeSN = "000000000000";
    	// 交易日期
    	var date = "0000";
    	// 建设银行
    	//var returnData = webpos.posDoTrade2(handle,_cType+_payType+moneyStr+_carrTradeSN);
    	// 光大银行
    	var req = _cType + _payType + moneyStr + oldBatch + _carrTradeSN + date;
    	try{	
	    	var returnData = Proxyocx.cdcPay(req);	
	    	if(returnData.length<=42){
	    		alertMsg.error("缴费失败："+returnData.substr(2));
	    		return false; 
	    	}
	    	var paymentId = $("#pos_payment_form_paymentId").val();
			var paymentMethod = $("#pos_payment_form_paymentMethod").val();
			//var receiptNumber = $("#pos_payment_form_receiptNumber").val();
			var checkPayable = $("#pos_payment_form_checkPayable").val();
			var memo = $("#pos_payment_form_memo").val();
			var showType = $("#pos_payment_form_showType").val();
			var examCertificateNo = $("#pos_payment_form_examCertificateNo").val();
			var uniqueValue = $("#pos_payment_form_uniqueValue").val();
			var tempStudentFeeId = $("#pos_payment_form_tempStudentFeeId").val();
   			
  			$.ajax({
  				type:"post",
  				url:$("#pos_payment_form").attr("action"),
  				data:{paymentId:paymentId,returnData:returnData,paymentMethod:paymentMethod,checkPayable:checkPayable,memo:memo,timestamp:timestamp,
  					moneyStr:moneyStr,showType:showType,examCertificateNo:examCertificateNo,uniqueValue:uniqueValue,tempStudentFeeId:tempStudentFeeId,sign:sign},
  				dataType:"json",
  				cache: false,
  				error: DWZ.ajaxError,
  				success:function(data){
  					if(data.statusCode==200){
  						//alertMsg.correct(data.message);
  						// 给收据填充数据
						/**$("#printReceipt_paymentMethod").text(data.paymentDetail.printPayMethod);
						$("#printReceipt_checkPayable").text(data.paymentDetail.checkPayable);
						$("#printReceipt_className").text(data.paymentDetail.className);
						$("#printReceipt_year").text(data.paymentDetail.printYear);
						$("#printReceipt_month").text(data.paymentDetail.printMonth);
						$("#printReceipt_day").text(data.paymentDetail.printDay);
						$("#printReceipt_fee").text(data.paymentDetail.printPayAmount);
						$("#printReceipt_money").text(data.paymentDetail.printUpperMoney);
						$("#printReceipt_totalFee").text(data.paymentDetail.printPayAmount);
						$("#printReceipt_drawer").text(data.paymentDetail.operatorName);
						$("#printReceipt_payee").text(data.paymentDetail.operatorName);
						// 打印
						$("#printReceipt_main").show();
						$("#printReceipt_main").jqprint();
						$("#printReceipt_main").hide();
						**/
  						var $_form = $("#pos_payment_form_search")
  				    	navTab.reload($_form.attr("action"),$_form.serializeArray());
  					} else if(data.statusCode==400) {
  						alertMsg.info(data.message);
  					} else {
  						alertMsg.error(data.message);
  					}
  				}
  			});
    	}catch(e){
			alert(e);
		}
    }
    
    // 设置收据号前缀
    function setReceiptNOPrefix() {
    	 $.pdialog.open("${baseUrl}/edu3/finance/studentpayment/setReceiptNOPrefixForm.html","setReceiptNOPrefixForm","设置收据号前缀",{mask:true,height:200,width:300});
    }
    
    // 预览收据
    function previewReceipt() {
    	var resids = [];
    	$("#posPaymentFormBody input[@name='resourceid']:checked").each(function(){
    		resids.push($(this).val());
    	});
    	if(resids.length < 1){
    		alertMsg.warn("请选择一条要操作的记录！");	
			return false;
    	}else if(resids.length > 1){
    		alertMsg.warn("只能选择一条记录操作！");	
			return false;
    	}
    	
    	$.pdialog.open("${baseUrl}/edu3/finance/studentpayment/previewReceipt.html?resourceid="+resids.toString(),"previewReceipt","预览收据",{mask:true,height:600,width:800,maxable: false,resizable:false });
    }
    
    // 打印收据
    function printReceipt() {
    	var url = "${baseUrl}/edu3/finance/studentpayment/printReceipt.html";
    	var pdIds = [];
    	$("#posPaymentFormBody input[@name='resourceid']:checked").each(function(){
    		pdIds.push($(this).val());
    	});
    	if(pdIds.length < 1){
    		alertMsg.warn("请选择一条要操作的记录！");	
			return false;
    	}else if(pdIds.length > 1){
    		alertMsg.warn("只能选择一条记录操作！");	
			return false;
    	}
    	
    	$.ajax({
				type:"post",
				url:url,
				data:{paymentDetailId:pdIds.toString()},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(data){
					if(data.statusCode==200){
						// 给收据填充数据
						$("#printReceipt_paymentMethod").text(data.paymentDetail.printPayMethod);
						$("#printReceipt_checkPayable").text(data.paymentDetail.checkPayable);
						$("#printReceipt_className").text(data.paymentDetail.className);
						$("#printReceipt_year").text(data.paymentDetail.printYear);
						$("#printReceipt_month").text(data.paymentDetail.printMonth);
						$("#printReceipt_day").text(data.paymentDetail.printDay);
						$("#printReceipt_fee").text(data.paymentDetail.printPayAmount);
						$("#printReceipt_money").text(data.paymentDetail.printUpperMoney);
						$("#printReceipt_totalFee").text(data.paymentDetail.printPayAmount);
						$("#printReceipt_drawer").text(data.paymentDetail.operatorName);
						$("#printReceipt_payee").text(data.paymentDetail.operatorName);
						// 打印
						$("#printReceipt_main").show();
						$("#printReceipt_main").jqprint();
						$("#printReceipt_main").hide();
					} else {
						alertMsg.error(data.message);
					}
				}
			});
    }
    
</script>
</head>
<body>
    <object id="Proxyocx" classid="clsid:34A7E853-D8A3-4FC8-8DC8-D7E215E1B082" codebase="${baseUrl}/ocx/Proxyocx.CAB#version=5,0,0,0" width="0" height="0">
		<PARAM NAME="_Version" VALUE="65536">
		<PARAM NAME="_ExtentX" VALUE="2642">
		<PARAM NAME="_ExtentY" VALUE="1319">
		<PARAM NAME="_StockProps" VALUE="0">
	</object>
	
	<div class="page">
		<div class="pageHeader">
			<form
				action="${baseUrl }/edu3/finance/studentpayment/pos_payment_form.html"
				id="pos_payment_form_search" method="post"
				onsubmit="return navTabSearch(this);">
				<div class="searchBar">
					<ul class="searchContent" style="height: 40px;">
						<c:choose>
							<c:when test="${uniqueValue=='0' }">
								<li style="height: 30px; width: 300px;"><label
									style="color: #183152;">准考证号：</label><input
									id="pos_payment_form_enrolleecode" name="examCertificateNo"
									value="${condition['examCertificateNo'] }" /></li>
							</c:when>
							<c:otherwise>
								<li style="height: 30px; width: 300px;"><label
									style="color: #183152;">考号：</label><input
									id="pos_payment_form_enrolleecode" name="enrolleecode"
									value="${condition['enrolleecode'] }" /></li>
							</c:otherwise>
						</c:choose>
						<li style="height: 30px; width: 300px;"><label
							style="color: #183152;">姓名：</label><input
							id="pos_payment_form_studentName" name="studentName"
							value="${condition['name'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li style="height: 30px; width: 300px;"><label
							style="color: #183152;">身份证号：</label><input
							id="pos_payment_form_certNum" name="certNum"
							value="${condition['certNum']}" /></li>
						<li style="height: 30px; width: 300px;"><label
							style="color: #183152;">学号：</label><input
							id="pos_payment_form_studentNo" name="studentNo"
							value="${condition['studyNo']}" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div id="pos_payment_form_Info">
			<c:choose>
				<c:when test="${message ne '200'}">
					<span
						style="font-weight: bolder; font-size: 25px; color: red; margin-top: 30px; margin-left: 50px; height: 300px;">${message }</span>
					<c:if test="${showType=='M'}">
						<div class="pageContent">
							<div class="pageFormContent">
								<table class="form">
									<tr>
										<th>姓名</th>
										<th>学号</th>
										<c:choose>
											<c:when test="${uniqueValue=='0' }">
												<th>准考证号</th>
											</c:when>
											<c:otherwise>
												<th>考生号</th>
											</c:otherwise>
										</c:choose>
									</tr>
									<c:forEach items="${psiList }" var="psi">
										<tr>
											<td>${psi.studentName }</td>
											<td>${psi.studentNo }</td>
											<td>${psi.examCertificateNo }</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
					</c:if>
				</c:when>
				<c:otherwise>
					<div class="pageHeader">
						<h1>学生信息：</h1>
						<c:choose>
							<c:when test="${showType=='P' }">
								<div class="searchBar">
									<ul class="searchContent" style="margin-top: 5px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">教学站：</span>${ studentPaymentInfo.studentInfo.branchSchool.unitName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">年级：</span>${ studentPaymentInfo.studentInfo.grade.gradeName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">层次：</span>${ studentPaymentInfo.studentInfo.classic.classicName}</li>
									</ul>
									<ul class="searchContent"
										style="margin-top: 5px; height: 30px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">班级：</span>${ studentPaymentInfo.studentInfo.classes.classname}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">专业：</span>${ studentPaymentInfo.studentInfo.major.majorName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">学习形式：</span>${ghfn:dictCode2Val('CodeTeachingType',studentPaymentInfo.studentInfo.teachingType)}</li>
									</ul>
									<ul class="searchContent"
										style="margin-top: 5px; height: 30px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">姓名：</span>${ studentPaymentInfo.studentInfo.studentName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">学号：</span>${ studentPaymentInfo.studentInfo.studyNo}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">缴费状态：</span><font color="red">${ghfn:dictCode2Val('CodeChargeStatus',studentPaymentInfo.chargeStatus) }</font></li>
									</ul>
									<ul class="searchContent"
										style="margin-top: 5px; height: 30px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">应缴金额：</span>
										<fmt:formatNumber value="${studentPaymentInfo.recpayFee }"
												pattern="####.##" />元</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">已缴金额：</span>
										<fmt:formatNumber value="${studentPaymentInfo.facepayFee }"
												pattern="####.##" />元</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">欠费金额：</span><font color="red"><fmt:formatNumber
													value="${studentPaymentInfo.unpaidFee }" pattern="####.##" />元</font></li>
									</ul>
								</div>
							</c:when>
							<c:otherwise>
								<div class="searchBar">
									<ul class="searchContent" style="margin-top: 5px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">教学站：</span>${ tempStudentFee.unit.unitName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">年级：</span>${tempStudentFee.grade.gradeName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">层次：</span>${ tempStudentFee.enrolleeInfo.recruitMajor.classic.classicName}</li>
									</ul>
									<ul class="searchContent"
										style="margin-top: 5px; height: 30px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">专业：</span>${ tempStudentFee.enrolleeInfo.recruitMajor.major.majorName}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">学习形式：</span>${ghfn:dictCode2Val('CodeTeachingType',tempStudentFee.enrolleeInfo.teachingType)}</li>
										<li
											style="margin-left: 10px; margin-left: 10px; height: 30px;"><span
											style="color: #183152;">姓名：</span>${ tempStudentFee.studentName}</li>
									</ul>
									<ul class="searchContent"
										style="margin-top: 5px; height: 30px;">
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;"> <c:choose>
													<c:when test="${uniqueValue=='0' }">
											准考证号：
										</c:when>
													<c:otherwise>
											考生号：
										</c:otherwise>
												</c:choose>
										</span>${ tempStudentFee.examCertificateNo}</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">应缴金额：</span>
										<fmt:formatNumber value="${tempStudentFee.amount }"
												pattern="####.##" />元</li>
										<li style="margin-left: 10px; height: 30px;"><span
											style="color: #183152;">欠费金额：</span><font color="red"><fmt:formatNumber
													value="${tempStudentFee.amount }" pattern="####.##" />元</font></li>
									</ul>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="pageHeader">
						<h1>缴费表单：</h1>
						<%--<h5 style="color: red;margin-top: 5px;">注：请使用下面“设置收据号前缀”的功能设置收据号前缀（即收据号前面的英文字母，如果前面没有英文字母则设置为NULL）</h5>--%>
						<div class="searchBar" style="height: 150px;">
							<form
								action="${baseUrl}/edu3/finance/studentpayment/affirmPayByPos.html"
								id="pos_payment_form">
								<input type="hidden" id="pos_payment_form_paymentId"
									name="stuPaymentId" value="${studentPaymentInfo.resourceid }" />
								<input type="hidden" id="pos_payment_form_examCertificateNo"
									name="examCertificateNo"
									value="${tempStudentFee.examCertificateNo }" /> <input
									type="hidden" id="pos_payment_form_showType" name="showType"
									value="${showType }" /> <input type="hidden"
									id="pos_payment_form_uniqueValue" name="uniqueValue"
									value="${uniqueValue }" /> <input type="hidden"
									id="pos_payment_form_tempStudentFeeId" name="tempStudentFeeId"
									value="${tempStudentFee.resourceid }" />
								<table style="margin-top: 5px; margin-bottom: 5px;">
									<tr>
										<td style="color: #183152;">收款方式：</td>
										<td><gh:select id="pos_payment_form_paymentMethod"
												name="paymentMethod" dictionaryCode="CodePaymentMethod"
												filtrationStr="4" choose="4" style="width:120px;" /></td>
										<%--  收据号统一打印
								<td style="color: #183152; padding-left: 8px">收据号：</td>
								<td><span id="pos_payment_form_receiptNumberPrefix" style="color: red;font-weight: bolder;">${receiptPrefix }</span><input type="text"  id="pos_payment_form_receiptNumber" name="receiptNumber" class="required digits" value="${_receiptNumber }"/></td>
								--%>
										<td style="color: #183152; padding-left: 8px">缴费金额：</td>
										<td>
											<c:choose>
												<c:when test="${showType=='P' }">
													<input type="text" id="pos_payment_form_money"name="money" class="required number"  readonly="readonly" 
													value="<fmt:formatNumber value="${studentPaymentInfo.unpaidFee }" pattern="####.##" />"/>元
												</c:when>
												<c:otherwise>
													<input type="text" id="pos_payment_form_money"name="money" class="required number"  readonly="readonly"  
													value="<fmt:formatNumber value="${tempStudentFee.amount }" pattern="####.##" />"/>元
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<c:if test="${showType=='P' }">
										<tr>
											<td style="color: #183152;">缴款单位（人）：</td>
											<td colspan="5"><input type="text"
												id="pos_payment_form_checkPayable" name="checkPayable"
												style="width: 300px;" readonly="readonly"
												value="${studentPaymentInfo.studentInfo.studentName }(学号${studentPaymentInfo.studentInfo.studyNo})" /></td>
										</tr>
									</c:if>
									<tr>
										<td style="color: #183152;">备注：</td>
										<td colspan="5"><textarea rows="3" style="width: 300px"
												id="pos_payment_form_memo" name="memo"></textarea></td>
									</tr>
									<tr>
										<td colspan="6" style="padding-top: 5px;"><div
												class="buttonActive">
												<div class="buttonContent">
													<button type="button" onclick="return affirmPayByPos();"
														id="pos_payment_form_affirm" style="width: 50px">收取</button>
												</div>
											</div></td>
									</tr>
								</table>
							</form>
						</div>
					</div>
					<div class="pageContent">
						<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENT_PAY"
							pageType="list"></gh:resAuth>
						<table class="table" layouth="166">
							<thead>
								<tr>
									<th width="4%"><input type="checkbox" name="checkall"
										id="check_all_posPaymentForm"
										onclick="checkboxAll('#check_all_posPaymentForm','resourceid','#posPaymentFormBody')" /></th>
									<th width="16%">收据号</th>
									<th width="10%">终端号</th>
									<th width="10%">pos流水号</th>
									<th width="6%">缴费金额</th>
									<th width="20%">银行卡号</th>
									<th width="18%">收费时间</th>
									<th width="10%">经手人</th>
									<th width="6%">收款方式</th>
									<!-- <th width="12%">操作</th> -->

								</tr>
							</thead>
							<tbody id="posPaymentFormBody">
								<c:forEach items="${studentPaymentDetailsList}"
									var="studentPaymentDetails" varStatus="vs">
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${studentPaymentDetails.resourceid }"
											autocomplete="off" /></td>
										<td title="${studentPaymentDetails.receiptNumber }">${studentPaymentDetails.receiptNumber }</td>
										<td title="${studentPaymentDetails.carrTermNum }">${studentPaymentDetails.carrTermNum }</td>
										<td title="${studentPaymentDetails.posSerialNumber }">${studentPaymentDetails.posSerialNumber }</td>
										<td
											title="<fmt:formatNumber value='${studentPaymentDetails.payAmount }' pattern='####.##' />"><fmt:formatNumber
												value="${studentPaymentDetails.payAmount }"
												pattern="####.##" /></td>
										<td title="${studentPaymentDetails.carrCardNo }">${studentPaymentDetails.carrCardNo }</td>
										<td
											title="<fmt:formatDate value='${studentPaymentDetails.operateDate }' pattern='yyyy-MM-dd HH:mm:ss' />"><fmt:formatDate
												value="${studentPaymentDetails.operateDate }"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td title="${studentPaymentDetails.operatorName }">${studentPaymentDetails.operatorName }</td>
										<td
											title="${ghfn:dictCode2Val('CodePaymentMethod',studentPaymentDetails.paymentMethod) }">${ghfn:dictCode2Val("CodePaymentMethod",studentPaymentDetails.paymentMethod) }</td>
										<!-- <td><button onclick="" style="cursor: pointer;margin-right: 2px;height: 21px;margin-left: 3%;">预览</button><button onclick=""  style="cursor: pointer;margin-left: 2px;height: 21px;">重新打印</button></td> -->
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<!-- 收据 -->
	<div align="center"
		style="margin-left: 3px; font-size: 50px; width: 550px; display: none;"
		id="printReceipt_main">
		<div style="float: left; width: 550px;">
			<div id="printReceipt_info">
				<div style="float: left; margin-top: 10px;" align="left">
					<!-- 付款方式，缴费单位，班别-->
					<p class="receiptData" id="printReceipt_paymentMethod"
						style="margin-bottom: 10px;"></p>
					<p class="receiptData"
						style="margin-left: 86px; margin-bottom: 5px;"
						id="printReceipt_checkPayable"></p>
					<p class="receiptData"
						style="margin-left: 50px; margin-bottom: 10px;"
						id="printReceipt_className"></p>
				</div>
				<div style="float: right;" align="right">
					<!-- 收据号，收费日期-->
					<p class="receiptData" style="height: 45px;"></p>
					<!--年月日-->
					<p class="receiptData">
						<span style="margin-right: 35px;" id="printReceipt_year"></span> <span
							style="margin-right: 35px;" id="printReceipt_month"></span> <span
							style="margin-right: 35px;" id="printReceipt_day"></span>
					</p>
				</div>
			</div>
		</div>
		<div id="printReceipt_table"
			style="float: left; width: 550px; height: 218px; margin-bottom: 9px;">
			<div
				style="float: left; width: 550px; height: 180px; margin-top: 25px;">
				<!--收费对象-->
				<div
					style="float: left; margin-left: 138px; margin-top: 65px; font-size: 22px;"
					class="receiptData">√</div>
				<!--金额-->
				<div style="float: right; margin-right: 100px;" class="receiptData"
					id="printReceipt_fee"></div>
			</div>
			<!--合计人民币-->
			<div style="float: left; width: 550px;">
				<div style="float: right;">
					<!--大写-->
					<span class="receiptData"
						style="letter-spacing: 25px; margin-right: 6px;"
						id="printReceipt_money"></span>
					<!--数字-->
					<span style="margin-right: 30px; margin-left: 15px;"
						class="receiptData" id="printReceipt_totalFee"></span>
				</div>
			</div>
		</div>
		<div id="printReceipt_sign" style="float: left; width: 550px;">
			<!--开票人-->
			<span class="receiptData" style="margin-left: 233px; float: left;"
				id="printReceipt_drawer"></span>
			<!--收款人-->
			<span class="receiptData" style="margin-left: 78px; float: left;"
				id="printReceipt_payee"></span>
		</div>
	</div>
</body>
</html>