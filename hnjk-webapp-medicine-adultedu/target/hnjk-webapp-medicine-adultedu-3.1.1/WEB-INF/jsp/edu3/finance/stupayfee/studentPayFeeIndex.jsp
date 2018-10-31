<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>缴费平台</title>
<link rel="stylesheet"
	href="${baseUrl }/themes/bootstrap/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet"
	href="${baseUrl }/themes/bootstrap/toastr.min.css" rel="stylesheet" />
<link rel="stylesheet"
	href="${baseUrl }/themes/bootstrap/ladda-themeless.min.css"
	rel="stylesheet" />
<script src="${baseUrl }/jscript/jquery-2.1.1.js" type="text/javascript"></script>
<script src="${baseUrl }/jscript/validator/jquery.validator.min.js"
	type="text/javascript"></script>
<script src="${baseUrl }/jscript/bootstrap/bootstrap.min.js"
	type="text/javascript"></script>
<script src="${baseUrl }/jscript/bootstrap/toastr.min.js"
	type="text/javascript"></script>
<script src="${baseUrl }/jscript/bootstrap/spin.min.js"
	type="text/javascript"></script>
<script src="${baseUrl }/jscript/bootstrap/ladda.min.js"
	type="text/javascript"></script>
<style type="text/css">
#header {
	width: auto;
	min-width: 980px;
	height: 100px;
	background: #99c84b;
}

#header .headerin {
	width: 980px;
	height: 100px;
	margin: 0 auto;
}

.headerin .logo {
	float: left;
	margin-top: 20px;
	margin-left: 20px;
}

.headerin .quickin {
	float: right;
	margin-top: 35px;
	margin-right: 20px;
}

.headerin .quickin span {
	font-size: 16px;
}

#foot {
	width: 100%;
	min-width: 980px;
	height: 150px;
	background: #2F4050;
	bottom: 0px;
	position: fixed;
}

#foot .footer {
	width: 980px;
	margin: 0 auto;
	height: 126px;
	padding-top: 24px;
}

#foot .footer p {
	text-align: center;
	font-size: 16px;
	color: #f3f3f3;
}

.tab-content {
	color: #000;
	font-size: 14px;
}

.tab-content .active {
	padding: 10px;
	background: #fff;
	font-size: 14px;
}

.tab-content .active p, .tab-content .active span {
	font-size: 14px;
}

.tab-content .tab-pane {
	font-size: 14px;
}
</style>

</head>
<body>

	<div id="header">
		<div class="headerin">
			<div class="logo">
				<a href="#"><img src="${baseUrl}/images/img/logo.png" alt=""
					width="323" height="64"></a>
			</div>
			<div id="quickInID" class="quickin">
				<a href="${baseUrl }/edu3/login.html" target="_blank" class="btn"><span>进入教务系统</span></a>
			</div>
		</div>
	</div>

	<div id="content" class="container" style="margin-top: 50px">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<form id="searchStudentFeeForm"
					action="${baseUrl }/tuitionFee/searchOrders.html" method="post"
					role='form' class="form-horizontal">
					<div class="input-group">
						<input id="certNum_search_id" type="text"
							class="form-control input-lg" name="certNum"
							placeholder="输入身份证号查询缴费情况"> <span class="input-group-btn">
							<button id="FeeSearchButton"
								class="btn btn-success btn-lg ladda-button" type="button"
								data-style="zoom-out" onclick="validateSubmit(this);">
								<span class="ladda-label">查询</span>
							</button>
						</span>
					</div>
				</form>
			</div>
			<div class="col-md-3"></div>
		</div>
		<hr width=80% size=20 color="red"
			style="FILTER: alpha(opacity = 100, finishopacity = 0, style = 3)">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10" id="orderList">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#PayFeeDownloadTabs"
						data-toggle="tab">缴费说明下载</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane fade in active" id="PayFeeDownloadTabs">
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
																value="${article.fileSize/1024/1024/1024}"
																pattern="0.00" maxFractionDigits="2" /> GB</c:otherwise>
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
			<div class="col-md-1"></div>
		</div>
	</div>

	<div id="foot">
		<div class="footer">
			<p>广东外语外贸大学继续教育学院</p>
			<p>地址：广州市白云大道北2号 粤ICP备06106464号 &nbsp&nbsp 邮编：510420 &nbsp&nbsp
				（网站维护）联系电话：17620035803</p>
			<p>
				Copyright&nbsp;&copy;&nbsp;2012-2018
				${school}${schoolConnectName}All Rights Reserved
				<gh:version />
			</p>
		</div>
	</div>

	<script type="text/javascript">
		toastr.options = {
			closeButton : true,
			debug : false,
			progressBar : true,
			positionClass : "toast-top-center",
			onclick : null,
			showDuration : "300",
			hideDuration : "1000",
			timeOut : "6000",
			extendedTimeOut : "1000",
			showEasing : "swing",
			hideEasing : "linear",
			showMethod : "fadeIn",
			hideMethod : "fadeOut"
		};
		function downloadArticleZip(articleId) {
			var url = "${baseUrl }/portal/site/article/download.html?articleId="
					+ articleId;
			_downloadFileByIframe(url, "downloadArticleZip");
		}
		function _downloadFileByIframe(url, iframeId) {
			$('#' + iframeId).remove();
			var iframe = document.createElement("iframe");
			iframe.id = iframeId;
			iframe.src = url;
			iframe.style.display = "none";

			document.body.appendChild(iframe);

		}
		function validateSubmit(_this) {
			if ($.trim($("#certNum_search_id").val()) == "") {
				toastr.warning("身份证号不能为空");
				return false;
			}
			var idNumber = $("#certNum_search_id").val();
			if (!_idCardValidate(idNumber)) {
				toastr.warning("身份证号码不合法!");
				return false;
			}
			var $form = $("#searchStudentFeeForm");

			var e = arguments.callee.caller.arguments[0] || window.event;
			e.preventDefault();
			var l = Ladda.create(_this);

			l.start();
			$.ajax({
				type : $form.attr("method"),
				url : $form.attr("action"),
				data : $form.serializeArray(),
				dataType : "json",
				cache : false,
				success : function(data) {
					if (data.statusCode == 200) {
						var certNum = data.certNum;
						$("#orderList").load(
								"${baseUrl }/tuitionFee/displayOrders.html?certNum="
										+ certNum);
					} else if (data.statusCode == 300) {
						toastr.warning(data.message);
						return false;
					}

				},
				complete : function() {
					l.stop();
				}
			})
		}
	</script>
</body>
</html>
