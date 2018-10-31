<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费表单页面</title>
<script type="text/javascript">
	     $(document).ready(function(){
	    	hideTr();
		   $("#payment-pay-form input[name='paymentMethod']").click(function(){
			   var paymentMethod = $(this).val();
			   if(paymentMethod=="2"){
				   showTr();
			   } else {
				   hideTr();
			   }
		   });
	     });
	   
	    function showTr(){
	    	$(".postr").each(function(){
	    		$(this).show();
	    		$(this).find("input").attr("class","required digits");
	    	});
	    	
	    }
	    
	    function hideTr(){
	    	$(".postr").each(function(){
	    		$(this).hide();
	    		$(this).find("input").removeAttr("class");
	    	});
	    }
	     
		function payFee(){
			var $form = $("#payment-pay-form");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			var url = "${baseUrl}/edu3/finance/studentpayment/pay/edit.html";
			$.ajax({
				type:"post",
				url:url,
				data:$form.serialize(),
				dataType:"json",
				success:function(data){
					if(data.statusCode==200){
						alertMsg.correct(data.message);
						$.pdialog.closeCurrent();
						closeThisTab();
					} else {
						alertMsg.error(data.message);
					}
				}
			});
		}
	
		function closeThisTab(){
			var pageNum = "${pageNum}";
			if(pageNum==""){
				pageNum = "1";
			}
			navTabPageBreak({pageNum:pageNum});
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="payment-pay-form" class="pageForm">
				<input type="hidden" name="studentPaymentId"
					value="${studentPaymentId }" />
				<div class="pageFormContent" layoutH="60">
					<table class="form">
						<tr>
							<td width="25%">缴费金额:</td>
							<td width="75%"><input id="pay-payAmount" name="payAmount"
								class="required number" style="width: 60%" /></td>
						</tr>
						<tr>
							<td>付款方式:</td>
							<td><gh:checkBox name="paymentMethod"
									dictionaryCode="CodePaymentMethod" id="pay-paymentMethod"
									inputType="radio" value="1" /></td>
						</tr>
						<%-- 使用pos机付款 --%>
						<tr class="postr">
							<td>pos流水号:</td>
							<td><input id="pay-posSerialNumber" name="posSerialNumber"
								class="required digits" style="width: 60%" /></td>
						</tr>
						<tr class="postr">
							<td>终端号:</td>
							<td><input id="pay-carrTermNum" name="carrTermNum"
								class="required digits" style="width: 60%" /></td>
						</tr>
						<tr class="postr">
							<td>银行卡号:</td>
							<td><input id="pay-carrCardNo" name="carrCardNo"
								class="required digits" style="width: 60%" /></td>
						</tr>
						<tr class="postr">
							<td>票据号:</td>
							<td><input id="pay-receiptNumber" name="receiptNumber"
								style="width: 60%" class="required  digits" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="payFee()">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close" onclick="closeThisTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>