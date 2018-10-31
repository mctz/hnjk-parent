<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<script type="text/javascript">
</script>
<div class="page">
	<div class="pageContent">
		<form id="refundSuppleFeeForm" method="post" action="${baseUrl}/edu3/roll/stuchangeinfo/refundSuppleFee.html"
			  onsubmit="return validateCallback(this);" class="pageForm">
			  <input type="hidden" name="resouceId" value="${resouceId }"/>
			<div class="pageFormContent" layouth="80">
				<table class="form">
					<tr>
						<td width="30%">处理类型：</td>
						<td width="70%">
							<gh:select id="refundSuppleFeeForm_processType" name="processType"  excludeValue="noNeedDeal" dictionaryCode="CodeProcessType" classCss="required"/>
						</td>
					</tr>
					<tr>
						<td width="30%">金额：</td>
						<td width="70%">
							<input type="text" id="refundSuppleFeeForm_money" name="money" style="width: 200px;" class="required number" />
						</td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">保存</button>
							</div>
						</div>
					</li>
					<li>
						<div class="button">
							<div class="buttonContent">
								<button type="button" class="close" href="#close">取消</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</form>
	</div>
</div>
</body>
</html>