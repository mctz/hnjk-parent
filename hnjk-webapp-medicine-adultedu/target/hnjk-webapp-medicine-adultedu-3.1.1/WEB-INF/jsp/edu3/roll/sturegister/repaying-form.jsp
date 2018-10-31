<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="studentInfoUploadFileForm" class="pageForm" method="post">
				<input type="hidden" name="studentIds" value="${studentIds }">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="studentInfoUploadFileTable">
						<tr>
							<td width="20%" style="text-align: center;">清退原因：</td>
							<td align="center" width="80%">
								<textarea rows="5" id="reason" name="reason" style="width: 80%"></textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="submit" type="button" onclick="saveRepaying()">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		function saveRepaying(){
			var studentIds = "${studentIds }";
			var reason = $("#reason").val();
	    	$.ajax({
				type:'POST',
				url:"${baseUrl}/edu3/register/studentinfo/repaying.html",
				data:{studentIds:studentIds,reason:reason},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success: function(data){
					if(data.statusCode==200){
						$.pdialog.closeCurrent();
						alertMsg.correct(data.message);
					}else{
						alertMsg.error(data.message);
					}
				}
			});
		}
	</script>
</body>
</html>
