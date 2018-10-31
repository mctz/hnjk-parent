<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置收据号前缀</title>
<script type="text/javascript">
		// 设置收据号前缀
		function subReceiptNOPrefix(){
			var receiptPrefix = $("#setReceiptNOPrefixForm_receiptPrefix").val();
			
			if(receiptPrefix==""){
				alertMsg.warn("请填写收据号前缀！");
				return false;
			}
			
			$.ajax({
		 		url: '${baseUrl}/edu3/finance/studentpayment/setReceiptNOPrefix.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{receiptPrefix:receiptPrefix},
		 		success: function(json){  
		 			if(json.returnCode== 200){
		 				alertMsg.correct(json.message);
		 				$.pdialog.closeCurrent();
		 				$("#pos_payment_form_receiptNumberPrefix").text(receiptPrefix);
		 			} else {
		 				alertMsg.error(json.message);
		 			}
				 }
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 20px;">
			<div align="left"
				style="color: red; margin-left: 10px; margin-bottom: 10px;">
				注：如果收据号前面有英文字母，则将这些英文字母
				<p style="margin-top: 5px;">设置为收据号前缀，否则请填写NULL（即为空）</p>
			</div>
			<div align="left" style="margin-left: 10px; margin-bottom: 10px;">
				<span style="font-size: 13px; font-weight: bolder;">收据号前缀：</span> <input
					id="setReceiptNOPrefixForm_receiptPrefix" name="receiptPrefix"
					value="${receiptPrefix }" type="text" />
			</div>
		</div>

		<div style="margin-top: 100px; margin-right: 5px;" align="right">
			<button id="setReceiptNOPrefixForm_submit" type="button"
				onclick="return subReceiptNOPrefix();" style="cursor: pointer;">提交</button>
			<button id="setReceiptNOPrefixForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>