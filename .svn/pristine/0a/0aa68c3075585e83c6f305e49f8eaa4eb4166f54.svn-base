<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置分成比例</title>
<script type="text/javascript">
		// 设置分成比例
		function subRoyaltyRate(){
			
			if(!$("#setRoyaltyRate_form").valid()){
				return false;
			}
			
			var royaltyRate = $("#setRoyaltyRateForm_royaltyRate").val();
            var royaltyRate2 = $("#setRoyaltyRateForm_royaltyRate2").val();
            var reserveRatio = $("#setRoyaltyRateForm_reserveRatio").val();
			var unitIds = $("#setRoyaltyRateForm_unitIds").val();
            if (royaltyRate2 == undefined) {
                royaltyRate2 = 0;
            }
            if (reserveRatio == undefined) {
                reserveRatio = 0;
            }
			$.ajax({
		 		url: '${baseUrl}/edu3/finance/studentpayment/setRoyaltyRate.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{unitIds:unitIds,royaltyRate:royaltyRate,royaltyRate2:royaltyRate2,reserveRatio:reserveRatio},
		 		success: function(json){  
		 			if(json.returnCode == 200){
		 				alertMsg.correct(json.message);
		 				$.pdialog.closeCurrent();
		 				navTabPageBreak();
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
			<form action="" id="setRoyaltyRate_form">
				<input id="setRoyaltyRateForm_unitIds" name="unitIds"
					value="${unitIds }" type="hidden" />
				<div align="left"
					style="color: red; margin-left: 10px; margin-bottom: 10px;">注：分成比例只能为数字</div>
				<div align="left" style="margin-left: 10px; margin-bottom: 10px;">
					<span style="font-size: 13px; font-weight: bolder;">分成比例(<span style="color: red;">%</span>)：</span>
					<input id="setRoyaltyRateForm_royaltyRate" name="royaltyRate" type="text" class="required number" />
					<c:if test="${schoolCode eq '11846'}">
						<br><br><span style="font-size: 13px; font-weight: bolder;">分成比例2(<span style="color: red;">%</span>)：</span>
						<input id="setRoyaltyRateForm_royaltyRate2" name="royaltyRate2" placeholder="外语类分成比例" type="text" class="required number" />
						<br><br><span style="font-size: 13px; font-weight: bolder;">预留比例(<span style="color: red;">%</span>)：</span>
						<input id="setRoyaltyRateForm_reserveRatio" name="reserveRatio" type="text" class="required number" />
					</c:if>
				</div>
			</form>
		</div>

		<div style="margin-top: 50px; margin-right: 5px;" align="right">
			<button id="setRoyaltyRateForm_submit" type="button"
				onclick="return subRoyaltyRate();" style="cursor: pointer;">提交</button>
			<button id="setRoyaltyRateForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>