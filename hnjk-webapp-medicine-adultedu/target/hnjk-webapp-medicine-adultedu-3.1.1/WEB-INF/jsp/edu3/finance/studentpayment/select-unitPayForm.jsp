<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置课程教学类型</title>
<script type="text/javascript">
		// 设置教学点收费形式
		function subUnitPayForm(){
			var unitPayForm = $("#selectUnitPayForm_payForm").val();
			
			if(unitPayForm==""){
				alertMsg.warn("请选择教学点收费形式！");
				return false;
			}
			
			var unitIds = $("#selectUnitPayForm_unitIds").val();
			$.ajax({
		 		url: '${baseUrl}/edu3/finance/studentpayment/setUnitPayForm.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{unitIds:unitIds,unitPayForm:unitPayForm},
		 		success: function(json){  
		 			if(json.returnCode== 200){
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
		<div style="margin-top: 40px;">
			<input id="selectUnitPayForm_unitIds" name="unitIds"
				value="${unitIds }" type="hidden" />
			<div align="left" style="margin-left: 25px; margin-bottom: 10px;">
				<span style="font-size: 13px; font-weight: bolder;">教学站收费形式：</span>
				<gh:select dictionaryCode="CodeUnitPayForm" name="payForm"
					id="selectUnitPayForm_payForm" style="width:100px;" />
			</div>
		</div>

		<div style="margin-top: 110px; margin-right: 5px;" align="right">
			<button id="selectUnitPayForm_submit" type="button"
				onclick="return subUnitPayForm();" style="cursor: pointer;">提交</button>
			<button id="selectUnitPayForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>