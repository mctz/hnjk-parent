<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
/* span { */
/* 	font-size: 18px; */
/* } */
.table tbody tr td {
	vertical-align: middle;
}
</style>
</head>
<body>
	<div id="PayFeeOrdersID">
		<ul class="nav nav-tabs">
			<li><a href="#PayFeeDownloadTabs" data-toggle="tab">缴费说明下载</a></li>
			<li class="active"><a href="#orderInfoTabs" data-toggle="tab">订单信息</a></li>
			<li><a href="#selfInfoTabs" data-toggle="tab">个人信息</a></li>

		</ul>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="orderInfoTabs">
				<table class="table table-striped table-bordered">

					<th class="text-center">订单号</th>
					<th class="text-center">姓名</th>
					<th class="text-center">专业名称</th>
					<th class="text-center">专业代码</th>
					<th class="text-center">年份</th>
					<th class="text-center">收费项</th>
					<th class="text-center">费用</th>
					<th class="text-center">缴费状态</th>
					<th class="text-center">操作</th>
					<th class="text-center">是否打印发票</th>

					<c:forEach items="${feeList }" var="fee" varStatus="vs">
						<tr class="text-center">
							<td>${fee.schoolOrderNo }</td>
							<td>${fee.studentName }</td>
							<td>${fee.major.majorName }</td>
							<td>${fee.major.majorCode }</td>
							<td>${fee.batchNo }</td>
							<td>${ghfn:dictCode2Val('CodeChargingItems',fee.chargingItems) }</td>
							<td>${fee.amount}</td>
							<td>${ghfn:dictCode2Val('CodePayStatus',fee.payStatus )}</td>
							<td><c:if test="${fee.payStatus ne '2'}">
									<a herf="javascript:void(0)" id="goPayFeeId_${fee.schoolOrderNo }"
										style="cursor: pointer; text-decoration: none;"
										class="btn ladda-button" data-style="zoom-out"
										onclick="goPayFee(this,'${fee.schoolOrderNo }','${fee.chargingItems }')"
										<span class="ladda-label">去缴费</span></a>
								</c:if> <c:if
									test="${not empty fee.payPassword and fee.payStatus eq '2'}">
									<a href="${payUrl }${fee.payPassword}" class="btn" target="_blank">支付详情</a>
								</c:if></td>
							<td><c:choose>
									<c:when test="${fee.payStatus eq '2'}">
										<c:if test="${ fee.isInvoicing ne 'Y'}">
											<a href="#" class="btn ladda-button" data-toggle="modal"
												data-target="#myModal">申请打印发票</a>
										</c:if>
										<c:if test="${ fee.isInvoicing eq 'Y'}">已申请 /
										<a href="#" class="btn ladda-button" data-style="zoom-out"
												data-toggle="modal" data-target="#myModal">重新填写</a>
										</c:if>
									</c:when>
									<c:otherwise>请先完成缴费</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>

				</table>
			</div>
			<div class="tab-pane fade" id="selfInfoTabs">
				<div class="panel panel-default">
					<c:if test="${empty stuInfo }">
						<div class="panel-heading">您还未注册，还没有学籍信息</div>
					</c:if>
					<c:if test="${not empty stuInfo }">
						<div class="panel-heading">学生信息</div>
						<ul class="list-group">
							<li class="list-group-item">姓名：${stuInfo.studentBaseInfo.name }</li>
							<li class="list-group-item">身份证号：${stuInfo.studentBaseInfo.certNum }</li>
							<li class="list-group-item">性别：${ghfn:dictCode2Val('CodeSex',stuInfo.studentBaseInfo.gender) }</li>
							<li class="list-group-item">年级：${stuInfo.grade.gradeName }</li>
							<li class="list-group-item">层次：${stuInfo.classic.classicName }</li>
							<li class="list-group-item">专业：${stuInfo.major.majorName }</li>

						</ul>
					</c:if>
				</div>
			</div>
			<div class="tab-pane fade" id="PayFeeDownloadTabs">
				<div class="panel panel-default">
					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th style="width: 50%;">名称</th>
									<th style="width: 10%;">类型</th>
									<th style="width: 10%;">文件大小</th>
									<th style="width: 10%;">操作</th>
								</tr>
							</thead>
							<tbody id="picCarouselTbody">
								<c:forEach items="${articleList.result}" var="article"
									varStatus="vs">
									<tr>
										<td>${article.title}</td>
										<td>${article.artitype}</td>
										<td><c:choose>
												<c:when test="${article.fileSize==0}"></c:when>
												<c:when test="${article.fileSize<=1024}">${article.fileSize} byte</c:when>
												<c:when test="${article.fileSize<=1024*1024}">
													<fmt:formatNumber type="number"
														value="${article.fileSize/1024}" pattern="0.00"
														maxFractionDigits="2" /> KB</c:when>
												<c:when test="${article.fileSize<=1024*1024*1024}">
													<fmt:formatNumber type="number"
														value="${article.fileSize/1024/1024}" pattern="0.00"
														maxFractionDigits="2" /> M</c:when>
												<c:otherwise>
													<fmt:formatNumber type="number"
														value="${article.fileSize/1024/1024/1024}" pattern="0.00"
														maxFractionDigits="2" /> GB</c:otherwise>
											</c:choose></td>
										<td><a href="#" style="color: blue"
											onclick="downloadArticleZip('${article.resourceid}')">下载</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">填写发票单位名称</h4>
				</div>
				<div class="modal-body">
					<form id="ApplyPrintInvoiceForm" class="form-horizontal"
						role="form" action="${baseUrl}/tuitionFee/applyPrintInvoice.html"
						method="post">
						<div class="form-group has-success">
							<label for="invoiceTitle" class="col-sm-2 control-label">单位名称</label>
							<div class="col-sm-10">
								<input type="text" name="invoiceTitle" id="modalInvoiceTitle"
									class="form-control" id="invoiceTitle" placeholder="请在此处输入单位名称">
							</div>
						</div>
						<input id="modalSchoolOrderNo" name="schoolOrderNo" type="hidden"
							value="">
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary btn-success"
						onclick="applyPrintInvoice();">提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>

	<script type="text/javascript">
		//modal页面传值
		$('#myModal').on('show.bs.modal', function(event) {
			var modal = $(this); //get modal itself
			var btnThis = $(event.relatedTarget); //触发事件的按钮
			var content = btnThis.closest('tr').find('td').eq(0).text();
			modal.find('.modal-body #modalSchoolOrderNo').val(content);
			if ($("#modalInvoiceTitle").val() == '') {
				$.ajax({
					url : "${baseUrl}/tuitionFee/displayInvoice.html",
					type : "post",
					dataType : "json",
					data : {
						schoolOrderNo : content
					},
					success : function(json) {
						if (json.statusCode == 200) {
							$("#modalInvoiceTitle").val(json.invoiceTitle);
						} else {
							toastr.warning(json.message);
						}
					},
				});
			}

		});

		function goPayFee(_this, schoolOrderNo, charingItem) {
			var hasStudentInfo = "${hasStuInfo }";
			if (charingItem != 'tuition') {
				if (hasStudentInfo == 'N') {
					toastr.warning("当前学生还没有学籍，请先缴学费以完成注册生成学籍信息，再进行教材费用的缴纳");
					return false;
				}
			}
			var e = arguments.callee.caller.arguments[0] || window.event;
			e.preventDefault();
			var l = Ladda.create(_this);
			
			$("#goPayFeeId_"+schoolOrderNo).addClass("disabled");
			$("#goPayFeeId_"+schoolOrderNo).text("加载中请稍后");
			l.start();
			$.ajax({
				url : "${baseUrl}/tuitionFee/payFee.html",	
				type : "post",
				dataType : "json",
				data : {
					schoolOrderNo : schoolOrderNo
				},
				success : function(json) {
					if (json.statusCode == 200) {
						window.open(json.forWardUrl, "广东外语外贸大学缴费平台");
					} else {
						toastr.warning(json.message + " 请稍后再试");
					}
				},
				complete : function() {
					l.stop();
					$("#goPayFeeId_"+schoolOrderNo).removeClass("disabled");
					$("#goPayFeeId_"+schoolOrderNo).text("请刷新网页");
					$("#goPayFeeId_"+schoolOrderNo).attr("href", "javascript:_reload();");
					$("#goPayFeeId_"+schoolOrderNo).removeAttr("onclick");
				}
			});
		}
		function _reload() {
			var certNum = $("#certNum_search_id").val();
			$("#orderList").load(
					"${baseUrl }/tuitionFee/displayOrders.html?certNum="
							+ certNum);
		}
		function applyPrintInvoice() {
			var $form = $("#ApplyPrintInvoiceForm");
			var invoice = $form.find("[name='invoiceTitle']").val();

			if (invoice == '' || invoice.length == 0) {
				toastr.warning("单位名称不能为空");
				return false;
			}
			if (invoice == '' || invoice.length == 50) {
				toastr.warning("单位名称长度不能大于50个汉字");
				return false;
			}
			$.ajax({
				url : $form.attr("action"),
				type : "post",
				dataType : "json",
				data : $form.serializeArray(),
				success : function(json) {
					if (json.statusCode == 200) {
						$("#myModal").modal('hide');
						toastr.success(json.message + " 请刷新网页进行查看");

						// 								$("#orderList").html(
						// 												"${baseUrl }/tuitionFee/displayOrders.html?certNum="
						// 												+ json.certNum);

					} else {
						toastr.warning(json.message + " 请稍后再试");
					}
				},
			});
		}
	</script>
</body>
</html>