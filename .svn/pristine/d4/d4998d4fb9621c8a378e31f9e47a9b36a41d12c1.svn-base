<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费明细记录</title>
<script type="text/javascript">
$(document).ready(function(){
    var total = parseInt("${total}");
	if(total<=0){
		alertMsg.warn("查询数据为空,(已有收据号的无法打印),无法打印"); 
		$.pdialog.close("RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT");
	}
});
//根据查询条件打印缴费明细记录
function valReceiptNumber() {
	var receiptNumber_begin = $("#studentPaymentDetail_receiptNumber_begin");
	var receiptNumber_end = $("#studentPaymentDetail_receiptNumber_end");
	var $drawer = $("#studentPaymentDetail_drawer");
	var total = parseInt("${total}");
	var beginNum = receiptNumber_begin.val();
	var endNum = (parseInt(receiptNumber_begin.val())+total-1);
	var drawer = $drawer.val();
		
	if(total<=0){
		alertMsg.warn("查询数据为空,(已有收据号的无法打印),无法打印"); 
		$.pdialog.close("RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT");
		return ;
	}
	if(beginNum=="" || isNaN(parseInt(beginNum))){//开始不能为空
		alertMsg.warn("请输入数字到收据号开始"); 
		receiptNumber_begin.focus();
		return ;
	}
	if(beginNum.length !=8){
		alertMsg.warn("收据号开始必须为<font color='red'> 8 </font>位数字"); 
		receiptNumber_begin.focus();
		return ;
	}
	if(receiptNumber_end!='' && receiptNumber_end.val().length !=8){
		alertMsg.warn("收据号结束必须为<font color='red'> 8 </font>位数字"); 
		receiptNumber_begin.focus();
		return ;
	}
	if(drawer==""){//开票人不能为空
		alertMsg.warn("请输入开票人"); 
		$drawer.focus();
		return ;
	}

	// 检查票据号是否重复
	$.ajax({
		type:"post",
		url:baseUrl+"/edu3/finance/studentPaymentDetails/checkReceiptNumber.html",
		data:{receiptNumber_begin:beginNum,receiptNumber_end:endNum},
		dataType:"json",
		success:function(data){
			if(data.statusCode==200){
				if(receiptNumber_end.val()=="" || isNaN(parseInt(endNum))){//结束空就自己加
					alertMsg.confirm("您确定按收据号 "+parseInt(receiptNumber_begin.val())+" 到   "+(parseInt(receiptNumber_begin.val())+total-1)+" 为  "+total+"  个学生缴费明细记录打印时加入收据号", {
						okCall:function(){
							receiptNumber_begin.val(parseInt(receiptNumber_begin.val()));
							receiptNumber_end.val(parseInt(receiptNumber_begin.val())+total-1);
							$.pdialog.close("RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT");
							$.pdialog.open(encodeURI(baseUrl+"/edu3/finance/studentPaymentDetails/printListView.html?total="+"${total }"+"&brSchool="+"${condition.brSchool }"+"&gradeId="+"${condition.gradeId }"+"&classicId="+"${condition.classicId }"+"&majorId="+"${condition.majorId }"+"&name="+"${condition.name }"+"&studyNo="+"${condition.studyNo }"+"&studentStatus="+"${condition.studentStatus }"+"&beginDate="+"${condition.beginDate }"+"&endDate="+"${condition.endDate }"+"&receiptNumber_begin="+parseInt(receiptNumber_begin.val())+"&receiptNumber_end="+parseInt(receiptNumber_end.val())+"&detailIds="+"${condition.detailIds }"+"&drawer="+encodeURI(drawer)+"&year=${condition.year }&examCertificateNo=${condition.examCertificateNo }&paymentMethod=${condition.paymentMethod }&isPrint=${condition.isPrint }"),'RES_FINANCE_STUDENTPAYMENTDETAIL_RECORD_QUERYPRINT_PRINT','打印预览',{height:600, width:800,mask:true});
				
							/* $("#studentPaymentDetail_print_form").submit(); */
						}
					});
				}else if((parseInt(receiptNumber_end.val())-parseInt(receiptNumber_begin.val()))>=(total-1)){
					alertMsg.confirm("您确定按收据号 "+parseInt(receiptNumber_begin.val())+" 到   "+(parseInt(receiptNumber_begin.val())+total-1)+" 为  "+total+" 个学生缴费明细记录打印时加入收据号", {
						okCall:function(){
							receiptNumber_begin.val(parseInt(receiptNumber_begin.val()));
							receiptNumber_end.val(parseInt(receiptNumber_begin.val())+total-1);
							$.pdialog.close("RES_FINANCE_STUDENTPAYMENTDETAIL_QUERYPRINT");
							$.pdialog.open(encodeURI(baseUrl+"/edu3/finance/studentPaymentDetails/printListView.html?total="+"${total }"+"&brSchool="+"${condition.brSchool }"+"&gradeId="+"${condition.gradeId }"+"&classicId="+"${condition.classicId }"+"&majorId="+"${condition.majorId }"+"&name="+"${condition.name }"+"&studyNo="+"${condition.studyNo }"+"&studentStatus="+"${condition.studentStatus }"+"&beginDate="+"${condition.beginDate }"+"&endDate="+"${condition.endDate }"+"&receiptNumber_begin="+parseInt(receiptNumber_begin.val())+"&receiptNumber_end="+parseInt(receiptNumber_end.val())+"&detailIds="+"${condition.detailIds }"+"&drawer="+encodeURI(drawer)+"&year=${condition.year }&examCertificateNo=${condition.examCertificateNo }&paymentMethod=${condition.paymentMethod }&isPrint=${condition.isPrint }"),'RES_FINANCE_STUDENTPAYMENTDETAIL_RECORD_QUERYPRINT_PRINT','打印预览',{height:600, width:800,mask:true});
				
							/* $("#studentPaymentDetail_print_form").submit(); */
						}
					});
				}else{
					alertMsg.warn("输入的收据号开始 "+parseInt(receiptNumber_begin.val())+" 和收据号结束 "+parseInt(receiptNumber_end.val())+" 的数量少于打印的总数  "+total); 
					receiptNumber_begin.focus();
					return ;
				}
			} else {
				alertMsg.error(data.message);
			}
		}
	});

}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="studentPaymentDetail_print_form" target=""
				action="${baseUrl }/edu3/finance/studentPaymentDetails/printListView.html"
				method="post">
				<input id="total" value="${total }" type="hidden" name="total" />

				<div class="searchBar">
					<ul>
						<li><label>票据号开始：</label><input
							id="studentPaymentDetail_receiptNumber_begin" type="text"
							name="receiptNumber_begin" style="width: 22%;" /><font
							color="red">请输入数字</font></li>
					</ul>
					<br />
					<ul>
						<li><label>票据号结束：</label><input
							id="studentPaymentDetail_receiptNumber_end" type="text"
							name="receiptNumber_end" style="width: 22%;" /></li>
					</ul>
					<br />
					<ul>
						<li><label>开票人：</label><input
							id="studentPaymentDetail_drawer" type="text" name="drawer"
							style="width: 22%;" required="required" /><font color="red">*</font>
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="valReceiptNumber()">
											确认</button>
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