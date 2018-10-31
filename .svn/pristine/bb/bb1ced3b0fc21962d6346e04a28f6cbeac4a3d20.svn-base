<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看条形码</title>
<style type="text/css">
#printReceipt_main .receiptData {
	color: blue;
}
</style>
<gh:loadCom components="jqprint,JsBarcode" />
<script src="http://code.jquery.com/jquery-migrate-1.1.0.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var options = {
				height: 100,
		        format:"CODE128",
		        displayValue:true,
		        textAlign:"center",
		        fontSize:18
		      };
		$("#barCode").empty().JsBarcode('${tempStudentFee.eduOrderNo}',options);
		
	});

</script>
</head>
<body>
	<div class="page">
		<div id="pos_payment_form_Info">
			<div class="pageContent">
				<div class="pageFormContent">
					<table class="form">
						<tr>
							<th width="25%">姓名</th>
							<!-- <th>学号</th> -->
							<c:choose>
								<c:when test="${uniqueValue=='0' }">
									<th width="25%">准考证号</th>
								</c:when>
								<c:otherwise>
									<th width="25%">考生号</th>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td>${tempStudentFee.studentName }</td>
							<%-- <td>${studentInfo.studentNo }</td> --%>
							<td>${tempStudentFee.examCertificateNo }</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="pageHeader">
				<h1>学生信息：</h1>
				<c:choose>
					<c:when test="${showType=='P' }">
						<div class="searchBar">
							<ul class="searchContent" style="margin-top: 5px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">教学站：</span>${ studentPaymentInfo.studentInfo.branchSchool.unitName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">年级：</span>${studentPaymentInfo.studentInfo.grade.gradeName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">层次：</span>${ studentPaymentInfo.studentInfo.classic.classicName}</li>
							</ul>
							<ul class="searchContent" style="margin-top: 5px; height: 30px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">班级：</span>${studentPaymentInfo.studentInfo.classes.classname}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">专业：</span>${studentPaymentInfo.studentInfo.major.majorName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">学习形式：</span>${ghfn:dictCode2Val('CodeTeachingType',studentPaymentInfo.studentInfo.teachingType)}</li>
							</ul>
							<ul class="searchContent" style="margin-top: 5px; height: 30px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">姓名：</span>${ studentPaymentInfo.studentInfo.studentName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">学号：</span>${ studentPaymentInfo.studentInfo.studyNo}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">缴费状态：</span><font color="red">${ghfn:dictCode2Val('CodeChargeStatus',studentPaymentInfo.chargeStatus) }</font></li>
							</ul>
							<ul class="searchContent" style="margin-top: 5px; height: 30px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">应缴金额：</span>
								<fmt:formatNumber value="${studentPaymentInfo.recpayFee }"
										pattern="####.##" />元</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">已缴金额：</span>
								<fmt:formatNumber value="${studentPaymentInfo.facepayFee }"
										pattern="####.##" />元</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">欠费金额：</span><font color="red"><fmt:formatNumber
											value="${studentPaymentInfo.unpaidFee }" pattern="####.##" />元</font></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<div class="searchBar">
							<ul class="searchContent" style="margin-top: 5px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">教学站：</span>${ tempStudentFee.unit.unitName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">年级：</span>${tempStudentFee.grade.gradeName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">层次：</span>${ tempStudentFee.enrolleeInfo.recruitMajor.classic.classicName}</li>
							</ul>
							<ul class="searchContent" style="margin-top: 5px; height: 30px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">专业：</span>${ tempStudentFee.enrolleeInfo.recruitMajor.major.majorName}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">学习形式：</span>${ghfn:dictCode2Val('CodeTeachingType',tempStudentFee.enrolleeInfo.teachingType)}</li>
								<li style="margin-left: 10px; margin-left: 10px; height: 30px;"><span
									style="color: #183152;">姓名：</span>${ tempStudentFee.studentName}</li>
							</ul>
							<ul class="searchContent" style="margin-top: 5px; height: 30px;">
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;"> <c:choose>
											<c:when test="${uniqueValue=='0' }">
											准考证号：
										</c:when>
											<c:otherwise>
											考生号：
										</c:otherwise>
										</c:choose>
								</span>${ tempStudentFee.examCertificateNo}</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">应缴金额：</span>
								<fmt:formatNumber value="${tempStudentFee.amount }"
										pattern="####.##" />元</li>
								<li style="margin-left: 10px; height: 30px;"><span
									style="color: #183152;">欠费金额：</span><font color="red"> <c:if
											test="${tempStudentFee.payStatus eq '1'}">
											<fmt:formatNumber value="${tempStudentFee.amount }"
												pattern="####.##" />
										</c:if> <c:if test="${tempStudentFee.payStatus eq '2'}">
											<fmt:formatNumber value="${0 }" pattern="####.##" />
										</c:if> 元
								</font></li>
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<div style="margin-left: 20px">
				条形码： <img  id="barCode" width="250px" />
				<%-- <img alt="条形码" src="${baseServerUrl }${rootUrl}barcode/${barCodePath}" style="margin-top: 50px; margin-left: 80px"> --%>
			</div>

		</div>
	</div>
</body>
</html>