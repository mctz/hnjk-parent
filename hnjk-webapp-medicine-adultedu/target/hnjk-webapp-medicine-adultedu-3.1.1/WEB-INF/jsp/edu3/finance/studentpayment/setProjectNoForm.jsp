<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置缴费项目编号</title>
<script type="text/javascript">
		// 设置缴费项目编号
		function subProjectNo(){
			
			if(!$("#setProjectNo_form").valid()){
				return false;
			}
			
			var projectNo = $("#setProjectNoForm_projectNo").val();
			
			$.ajax({
		 		url: '${baseUrl}/edu3/finance/studentpayment/setProjectNo.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{projectNo:projectNo},
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
			<form action="" id="setProjectNo_form">
				<div align="left" style="margin-left: 10px; margin-bottom: 10px;">
					<span style="font-size: 13px; font-weight: bolder;">缴费项目编号 ：</span>
					<input id="setProjectNoForm_projectNo" name="projectNo" type="text"
						class="required" />
				</div>
			</form>
		</div>

		<div style="margin-top: 100px; margin-right: 5px;" align="right">
			<button id="setProjectNoForm_submit" type="button"
				onclick="return subProjectNo();" style="cursor: pointer;">提交</button>
			<button id="setProjectNoForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>