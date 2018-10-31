<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改处理</title>
<script type="text/javascript">
$(document).ready(function(){
    var message = '${message}';
	if(message!=""){
		alertMsg.warn(message); 
		$.pdialog.close("RES_FINANCE_ANNUALFEES_REFUND");
	}
});

//计算修改应缴金额
function countFee(flag) {
	var type = $("#type").val();
	if(type=='update'){
		var recpayFee = $("#annualFees_update_recpayFee").val();
		var facepayFee = $("#annualFees_update_facepayFee").val();
		
		if(recpayFee=="" || isNaN(parseFloat(Number(recpayFee)))||parseFloat(Number(recpayFee))<0||(parseFloat(recpayFee)-parseFloat(facepayFee))<0){//不能为空
			alertMsg.warn("请输入数字到应缴费用,必须大于已缴费用"); 
			return false;
		}
		if(flag=='N'){
			return false;
		}else{
			alertMsg.confirm("你确定修改成"+parseFloat(recpayFee).toFixed(2)+"？", {
				okCall:function(){
					$.ajax({
				          type:"POST",
				          url:$("#annualFees_update_form").attr("action"),
				          data:$("#annualFees_update_form").serialize(),
				          dataType:  'json',
				          success:function(data){          	   		
				         		 if(data['statusCode'] === 200){         		 	
				         		 	alertMsg.correct(data['message']);
				         		 	$.pdialog.close("RES_FINANCE_ANNUALFEES_UPDATE");
				         		 	navTab.reload(data['reloadUrl']);
				         		 }else{
				         			 alertMsg.error(data['message']);
				         		 }         
				          }            
					});
				}
			});
		}
	}else if(type=='refund'){
		var recpayFee = $("#annualFees_update_recpayFee").val();
		var value = "${annualFees.recpayFee}";//原应缴
		var facepayFee = $("#annualFees_update_facepayFee").val();
		var refund = $("#annualFees_update_refund");
		var paymentMethod = $("#annualFees_update_paymentMethod").val();
		
		if(recpayFee=="" || isNaN(parseFloat(Number(recpayFee)))||parseFloat(Number(recpayFee))<0||(parseFloat(value)-parseFloat(recpayFee))<0){//不能为空
			alertMsg.warn("请输入数字到应缴费用,必须大于0且小于原本的应缴费用"); 
			return false;
		}
		
		refund.val(((parseFloat(facepayFee)-parseFloat(recpayFee))>0?parseFloat(facepayFee)-parseFloat(recpayFee):0.00).toFixed(2));
		
		if(flag=='N'){
			return false;
		}else{
			if(paymentMethod==""){
				alertMsg.warn("请选择退费方式"); 
				return false;
			}
			alertMsg.confirm("你确定退费"+refund.val()+"？", {
				okCall:function(){
					$.ajax({
				          type:"POST",
				          url:$("#annualFees_update_form").attr("action"),
				          data:$("#annualFees_update_form").serialize(),
				          dataType:  'json',
				          success:function(data){          	   		
				         		 if(data['statusCode'] === 200){         		 	
				         		 	alertMsg.correct(data['message']);
				         		 	
				         		 	$.pdialog.close("RES_FINANCE_ANNUALFEES_REFUND");
				         		 	navTab.reload(data['reloadUrl']);
				         		 }else{
				         			 alertMsg.error(data['message']);
				         		 }         
				          }            
					});
				}
			});
		}
	}
	
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="annualFees_update_form"
				action="${baseUrl }/edu3/finance/annualFees/comfirmUpdate.html"
				method="post">
				<input id="resourceid" value="${annualFees.resourceid }"
					type="hidden" name="resourceid" /> <input id="type" name="type"
					value="${condition.type }" type="hidden" name="type" />
				<div class="searchBar">
					<c:if test="${condition.type eq 'refund' }">
						<ul>
							<li><label>应缴金额：</label><input
								id="annualFees_update_recpayFee" type="text" name="recpayFee"
								value="${annualFees.recpayFee} " onblur="countFee('N')"
								style="width: 22%;" /> <font color="red">* 修改则更新退费金额</font></li>
						</ul>
						<br />
						<ul>
							<li><label>已缴金额：</label><input
								id="annualFees_update_facepayFee" type="text" name="facepayFee"
								value="${annualFees.facepayFee-annualFees.returnPremiumFee} "
								readonly="readonly" style="width: 22%;" /></li>
						</ul>
						<br />
						<ul>
							<li><label>退费金额：</label><input id="annualFees_update_refund"
								type="text" name="refund" style="width: 22%;"
								readonly="readonly" /></li>
						</ul>
						<br />
						<ul>
							<li><label>退费方式：</label> <gh:select name="paymentMethod"
									id="annualFees_update_paymentMethod"
									dictionaryCode="CodePaymentMethod" value="5"
									excludeValue="1,2,3,4" style="width:32%;" /></li>
						</ul>
					</c:if>
					<c:if test="${condition.type eq 'update' }">
						<ul>
							<li><label>应缴金额：</label><input
								id="annualFees_update_recpayFee" name="recpayFee" type="text"
								value="${annualFees.recpayFee} " onblur="countFee('N')"
								style="width: 22%;" /> <font color="red">* 必须大于已缴金额</font></li>
						</ul>
						<br />
						<ul>
							<li><label>已缴金额：</label><input
								id="annualFees_update_facepayFee" type="text" name="facepayFee"
								value="${annualFees.facepayFee} " readonly="readonly"
								style="width: 22%;" /></li>
						</ul>
						<br />
					</c:if>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="countFee('Y')">确认</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>