<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费明细记录</title>
<script type="text/javascript">
	function downloadCheckFile(){
		var tradeDate = $("#downloadCheckFile_form_tradeDate").val();
		if(!tradeDate){
			alert("请填写交易日期");
			return false;
		}
		var $from = $("#downloadCheckFile_form");
		downloadFileByIframe($from.attr("action")+"?tradeDate="+tradeDate,"downloadCheckFile_iframe");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="downloadCheckFile_form"
				action="${baseUrl }/pay/statement.html" method="post">
				<div class="searchBar">
					<ul>
						<li><label>交易日期：</label> <input
							id="downloadCheckFile_form_tradeDate" type="text"
							name="receiptNumber_begin" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width: 42%;" />
						</li>
					</ul>
					<div class="subBar" style="margin-top: 140px">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="return downloadCheckFile();">
											下载</button>
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