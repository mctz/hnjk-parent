<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>访问结果</title>
</head>
<body>

	<div class="page">
		<div class="pageContent" layoutH="0">
			<div class="tabs">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li><a href="#"><span>访问结果分析</span></a></li>
							<li><a href="#"><span>客户端浏览器分布</span></a></li>
							<li><a href="#"><span>客户操作系统分布</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<!-- 1 -->
					<div>
						<span id="containerServerstatus"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="35%">服务器返回状态</th>
									<th width="30%">个数</th>
									<th width="30%">百分比</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${serverstatuslist}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${a['target'] }</td>
										<td>${a['resultcount'] }&nbsp;</td>
										<td><fmt:formatNumber type="percent" pattern="###.####%"
												value="${a['percent'] }" /> &nbsp;</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<span id="containerClientBrowser"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="35%">客户端浏览器</th>
									<th width="30%">个数</th>
									<th width="30%">百分比</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${clientbrowserlist}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${ghfn:dictCode2Val('CodeClientBrowser',a['target'])}</td>
										<td>${a['resultcount'] }&nbsp;</td>
										<td><fmt:formatNumber type="percent" pattern="###.####%"
												value="${a['percent'] }" /> &nbsp;</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div>
						<span id="containerClientOs"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
						<table class="list" style="width: 96%">
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="35%">客户端操作系统</th>
									<th width="30%">个数</th>
									<th width="30%">百分比</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${clientoslist}" var="a" varStatus="vs">
									<tr>
										<td>${vs.index+1 }</td>
										<td>${ghfn:dictCode2Val('CodeClientOs',a['target'])}</td>
										<td>${a['resultcount'] }&nbsp;</td>
										<td><fmt:formatNumber type="percent" pattern="###.####%"
												value="${a['percent'] }" /> &nbsp;</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">		
	var chart1;
	var chart2;
	var chart3;
	$(document).ready(function() {
		chart1 = createChart('containerServerstatus','访问结果',[${serverstatuschart}]);
		chart2 = createChart('containerClientBrowser','客户端浏览器',[${clientbrowserchart}]);
		chart3 = createChart('containerClientOs','客户操作系统',[${clientoschart}]);
	});
	
	function createChart(toId,title,arr){
		return new Highcharts.Chart({
			chart: {
				renderTo: toId,
				margin: [50, 200, 60, 170]
			},
			title: {
				text: title
			},
			plotArea: {
				shadow: null,
				borderWidth: null,
				backgroundColor: null
			},
			tooltip: {
				formatter: function() {
					return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
				}
			},
			plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: {
						enabled: true,
						formatter: function() {
							return this.point.name;
						},
						color: 'white',
						style: {
							font: '13px Trebuchet MS, Verdana, sans-serif'
						}
					}
				}
			},
			legend: {
				layout: 'vertical',
				style: {
					left: 'auto',
					bottom: 'auto',
					right: '0px',
					top: '50px'
				}
			},
		    series: arr
		});
	}
		
</script>
</body>
</html>