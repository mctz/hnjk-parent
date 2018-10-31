<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生预交费用</title>
<gh:loadCom components="tab,validator" />
<script type="text/javascript">
    jQuery(document).ready(function(){
    	jQuery('#container-1').tabs();			
		jQuery("#savaFee").click(function(){
			return jQuery("#inputFeeForm").validator("alert");	
		});
	});
	</script>
</head>
<body>
	<div style="height: 500px">
		<table border="1" class="search">
			<tr>
				<td align="left" id="blue"><div align="left">学籍管理
						&gt;&gt; 学生预交费用 &gt;&gt; 填写信息</div></td>
			</tr>
		</table>
		<form id="inputFeeForm"
			action="${baseUrl}/edu3/schoolroll/gradefeerule/save.html"
			method="post">
			<div id="container-1" style="width: 98%">
				<ul>
					<li><a href="#fragment-1"><span>信息填写</span></a></li>
				</ul>
				<div id="fragment-1">
					<input type="hidden" name="resourceid" value="${resourceid }" />
					<table class="form">
						<tr>
							<td width="20%">学生姓名:</td>
							<td width="30%"><input type="text" name="stuName"
								style="width: 50%" value="${fee.studentInfo }"
								validate="Require" mes="学生姓名" /></td>
							<td width="20%">享受学费折扣:</td>
							<td width="30%"><gh:selectModel name="feeDiscountId"
									bindValue="resourceid" displayValue="disCount"
									modelClass="com.hnjk.edu.roll.model.FeeDiscount"
									value="${fee.feeDiscountId }" style="width:52%" /></td>
						</tr>
						<tr>
							<td width="20%">已缴费用金额:</td>
							<td width="30%"><input type="text" name="payedFee"
								style="width: 50%" value="${fee.payedFee }" validate="number" />
							</td>
							<td width="20%">&nbsp;</td>
							<td width="30%">&nbsp;</td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3"><textarea name="memo" rows="5"
									style="width: 60%">${fee.memo}</textarea></td>
						</tr>
					</table>
				</div>
			</div>
			<div style="text-align: center">
				<input type="submit" id="savaFee" value=" 保 存 " class="b1"
					onmouseover="this.className='b2'" onmouseout="this.className='b1'" />
				&nbsp; <input type="button" value=" 返 回 " onclick="history.go(-1);"
					class="b1" onmouseover="this.className='b2'"
					onmouseout="this.className='b1'" />
			</div>
		</form>
	</div>
</body>
</html>