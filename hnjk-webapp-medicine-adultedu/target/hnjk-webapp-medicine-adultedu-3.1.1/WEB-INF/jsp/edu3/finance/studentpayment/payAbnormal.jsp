<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>处理缴费异常</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/studentpayment/replenishByPos.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">学号:</td>
							<td width="35%"><input type="text" name="studyNo" style="width: 50%" class="required" /></td>
							<td width="15%">金额:</td>
							<td width="35%">
								<input type="text"  name="money" class="required"/>
							</td>
						</tr>
						<tr>
							<td>学籍情况:</td>
							<td>
								<select name="showType">
									<option value="P">已有学籍</option>
									<option  value="M">未注册</option>
								</select>
							</td>
							<td>准考证号:</td>
							<td>
								<input type="text" name="examCertificateNo" class="required"/>
							</td>
						</tr>
						<tr>
							<td>终端号：</td>
							<td><input type="text"  name="carrTermNum" class="required"/></td>
							<td>终端流水号：</td>
							<td><input type="text" name="carrTermSN" /></td>
						</tr>
						<tr>
							<td>银行卡号：</td>
							<td><input type="text"  name="carrCardNo" /></td>
							<td>唯一性：</td>
							<td>
								<select name="uniqueValue">
									<option value="0">准考证号</option>
									<option  value="1">考生号</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>缴费时间:</td>
							<td ><input type="text"  name="payTime"  class="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
							<td>备注:</td>
							<td ><input type="text"  name="memo" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>

</body>
</html>