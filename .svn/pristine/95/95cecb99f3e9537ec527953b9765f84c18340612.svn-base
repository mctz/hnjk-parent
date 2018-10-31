<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>多次缓缴管理</title>
<script type="text/javascript">
		//新增
		function addStudentPaymentDefer(){
			var rowNum = $("#studentPaymentDeferTable").get(0).rows.length;
			$("#studentPaymentDeferBody").append("<tr><td><q>"+rowNum+"</q><input type='hidden' name='deferid' value='' /></td><td><input type='text' name='deferFee' nameb='deferFee' value='' class='required number' min='0' style='width:50%;'/></td><td><input type='text' name='deferEndDate' nameb='deferEndDate' value='' class='required'  style='width:50%;' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\"></td><td><a class='button' href='javascript:void(0);' onclick='delStudentPaymentDefer(this);'><span>删除</span></a></td></tr>");
		}
		//删除
		function delStudentPaymentDefer(obj){
			$(obj).parent().parent().remove();
			$("#studentPaymentDeferBody q").each(function (n){
				$(this).text(n+1);
			});
		}
		//数据验证与提交
		function _studentPaymentDeferValidateCallback(form) {
			var $form = $(form);
	    	$("#studentPaymentDeferBody tr").each(function (row){
	    		$(this).find("[nameb]").each(function (){//有名称附加属性的表单元素
	    			$(this).attr("name",$(this).attr("name")+"_row"+row);
	    		});
	    	});
	    	if (!$form.valid()) {
	    		alertMsg.error(DWZ.msg["validateFormError"]);
	    		return false; 
	    	}
	    	$("#studentPaymentDeferBody tr").each(function (row){
	    		$(this).find("[nameb]").each(function (){    			
	    			$(this).attr("name",$(this).attr("nameb"));//验证后还原名称
	    		});
	    	});
	    	var beforeTotalFee = $("#studentPaymentDefer_beforeTotalFee").val();
	    	var totalFee = 0.0;
	    	$("#studentPaymentDeferBody input[name='deferFee']").each(function (){
	    		totalFee += parseFloat($(this).val());
	    	});
	    	if(totalFee >parseFloat(beforeTotalFee)){
	    		alertMsg.warn("缓缴总金额必须小于"+beforeTotalFee);
	    		return false; 
	    	}
			$.ajax({
	    		type:'POST',
	    		url:$form.attr("action"),
	    		data:$form.serializeArray(),
	    		dataType:"json",
	    		cache: false,
	    		success: dialogAjaxDone,	    		
	    		error: DWZ.ajaxError
	    	});
	    	return false;
	    }
	</script>
</head>
<body>
	<h2 class="contentTitle">设置多次缓缴</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/finance/studentpayment/defere/save.html"
				class="pageForm"
				onsubmit="return _studentPaymentDeferValidateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${studentPayment.resourceid }" /> <input type="hidden"
					id="studentPaymentDefer_beforeTotalFee"
					value="<fmt:formatNumber value='${beforeTotalFee }' pattern='####.##' />" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">缴费年度：</td>
							<td width="30%">${studentPayment.yearInfo.yearName }</td>
							<td width="20%">缴费学期：</td>
							<td width="30%">${ghfn:dictCode2Val('CodeTerm',studentPayment.term) }</td>
						</tr>
						<tr>
							<td>学号：</td>
							<td>${studentPayment.studyNo }</td>
							<td>姓名：</td>
							<td>${studentPayment.name }</td>
						</tr>
						<tr>
							<td>学习中心：</td>
							<td>${studentPayment.branchSchool.unitName }</td>
							<td>办学模式：</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',studentPayment.teachingType) }</td>
						</tr>
						<tr>
							<td>年级：</td>
							<td>${studentPayment.grade.gradeName }</td>
							<td>专业：</td>
							<td>${studentPayment.major.majorName }</td>
						</tr>
						<tr>
							<td>层次：</td>
							<td>${studentPayment.classic.classicName }</td>
							<td>缴费期数：</td>
							<td>${studentPayment.chargeTerm }</td>
						</tr>
						<tr>
							<td>应缴金额：</td>
							<td><fmt:formatNumber value='${studentPayment.recpayFee }'
									pattern='####.##' /></td>
							<td>缴费期限：</td>
							<td><fmt:formatDate value='${studentPayment.chargeEndDate }'
									pattern='yyyy-MM-dd' /></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td colspan="3">${studentPayment.memo }</td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td style="text-align: right">
								<div class="formBar">
									<span class="tips" style="float: left;">缓缴总金额必须小于<fmt:formatNumber
											value='${beforeTotalFee }' pattern='####.##' /></span>
									<ul>
										<li><div class="buttonActive">
												<div class="buttonContent">
													<button type="button" onclick="addStudentPaymentDefer()">新增缓缴记录</button>
												</div>
											</div></li>
									</ul>
								</div>
							</td>
						</tr>
					</table>
					<table class="form" id="studentPaymentDeferTable">
						<thead>
							<tr>
								<th width="5%"></th>
								<th width="45%">缓缴金额</th>
								<th width="40%">缓缴期限</th>
								<th width="10%"></th>
							</tr>
						</thead>
						<tbody id="studentPaymentDeferBody">
							<c:forEach items="${studentPaymentList}" var="payment"
								varStatus="vs">
								<tr>
									<td><q>${vs.index+1}</q><input type="hidden"
										name="deferid" value="${payment.resourceid }" /></td>
									<td><input type="text" name="deferFee" nameb="deferFee"
										value="<fmt:formatNumber value='${payment.recpayFee }' pattern='####.##' />"
										class="required number" min="0" style="width: 50%;" /></td>
									<td><input type="text" name="deferEndDate"
										nameb="deferEndDate"
										value="<fmt:formatDate value='${payment.deferEndDate }' pattern='yyyy-MM-dd'/>"
										class='required' style="width: 50%;"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
									<td><a class="button" href="javascript:void(0);"
										onclick="delStudentPaymentDefer(this);"><span>删除</span></a></td>
								</tr>
							</c:forEach>
						</tbody>
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
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>