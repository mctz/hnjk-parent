<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退费处理</title>
<script type="text/javascript">

//加载应缴金额
function loadRecpayfee() {
	var year = $("#studentPaymentDetail_refund_year option:selected").attr("id");
	var recpayfee = $("#studentPaymentDetail_refund_recpayfee");
	var value = $("#studentPaymentDetail_refund_year option:selected").attr("id");	
	recpayfee.val(value);
}

//计算减免费用
function countFee(flag) {
	var recpayfee = $("#studentPaymentDetail_refund_recpayfee").val();
	var refund = $("#studentPaymentDetail_refund");
	var value = $("#studentPaymentDetail_refund_year option:selected").attr("id");
	var paymentMethod = $("#studentPaymentDetail_refund_paymentMethod").val();
	if(value==""){
		alertMsg.warn("请先选择学年"); 
		return false;
	}
	if(recpayfee=="" || isNaN(parseFloat(Number(recpayfee)))||parseFloat(Number(recpayfee))<0||(parseFloat(value)-parseFloat(recpayfee))<0){//不能为空
		alertMsg.warn("请输入数字到应缴费用且大于0且小于原本的应缴费用"); 
		return false;
	}
	
	refund.val((parseFloat(value)-parseFloat(recpayfee)).toFixed(2));
	
	if(flag=='N'){
		return false;
	}else{
		if(paymentMethod==""){
			alertMsg.warn("请选择退费方式"); 
			return false;
		}
		alertMsg.confirm("你确定退费"+(parseFloat(value)-parseFloat(recpayfee)).toFixed(2)+"？", {
			okCall:function(){
				$.ajax({
			          type:"POST",
			          url:$("#studentPayment_refund_form").attr("action"),
			          data:$("#studentPayment_refund_form").serialize(),
			          dataType:  'json',
			          success:function(data){          	   		
			         		 if(data['statusCode'] === 200){         		 	
			         		 	alertMsg.correct(data['message']);
			         		 	
			         		 	$.pdialog.close("RES_FINANCE_STUDENTPAYMENT_REFUND");
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

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="studentPayment_refund_form"
				action="${baseUrl }/edu3/finance/studentPayment/comfirmRefund.html"
				method="post">
				<input id="resourceid" value="${condition.resourceid }"
					type="hidden" name="resourceid" /> <input id="yearMap"
					value="${yearMap }" type="hidden" name="yearMap" />
				<div class="searchBar">
					<ul>
						<li><label>学年：</label> <select
							id="studentPaymentDetail_refund_year" name="year"
							style="width: 22%" onchange="loadRecpayfee()">
								<option value="">请选择</option>
								<c:forEach items="${yearMap }" var="num">
									<option value="${num.key }" id="${num.value.creditFee }">
										${num.key }</option>
								</c:forEach>
						</select> <font color="red">* 请先选择</font></li>
					</ul>
					<br />
					<ul>
						<li><label>应缴金额：</label><input
							id="studentPaymentDetail_refund_recpayfee" type="text"
							name="recpayfee" onblur="countFee('N')" style="width: 22%;" /> <font
							color="red">* 修改则更新退费金额</font></li>
					</ul>
					<br />
					<ul>
						<li><label>退费金额：</label><input
							id="studentPaymentDetail_refund" type="text" name="refund"
							style="width: 22%;" readonly="readonly" /></li>
					</ul>
					<br />
					<ul>
						<li><label>退费方式：</label> <gh:select name="paymentMethod"
								id="studentPaymentDetail_refund_paymentMethod"
								dictionaryCode="CodePaymentMethod" excludeValue="1,2,3,4"
								style="width:32%;" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="countFee('Y')">确认</button>
										<font color="red">*</font>
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