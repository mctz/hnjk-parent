<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置学校内部批次编号</title>
<script type="text/javascript">
		// 设置学校内部批次编号
		function subBatchNo(){
		//	console.log("beatchNo");
			if(!$("#setBatchNo_form").valid()){
		//		console.log("beatchNo");
				return false;
			}
			
			var batchNo = $("#setBatchNoForm_batchNo").val();
		//	console.log(batchNo);
			$.ajax({
		 		url: '${baseUrl}/edu3/finance/studentpayment/setBatchNo.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{batchNo:batchNo},
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
			<form action="" id="setBatchNo_form">
				<div align="left" style="margin-left: 10px; margin-bottom: 10px;">
					<span style="font-size: 13px; font-weight: bolder;">学校内部批次编号
						：</span> <input id="setBatchNoForm_batchNo" name="batchNo" type="text"
						class="required" />
				</div>
			</form>
		</div>

		<div style="margin-top: 100px; margin-right: 5px;" align="right">
			<button id="setBatchNoForm_submit" type="button"
				onclick="return subBatchNo();" style="cursor: pointer;">提交</button>
			<button id="setBatchNoForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>