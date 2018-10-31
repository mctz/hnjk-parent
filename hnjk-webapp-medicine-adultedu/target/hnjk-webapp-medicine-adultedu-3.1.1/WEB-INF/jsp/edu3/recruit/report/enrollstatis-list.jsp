<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生人数统计</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/enrollStatistic.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${enrollstatis_hide eq 'N'}">
							<li style="width: 260px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="enrollstatis_school" tabindex="1"
									id="enrollstatis_school"
									defaultValue="${condition['enrollstatis_school']}"
									style="width:150px" /></li>
						</c:if>
						<li style="width: 280px"><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="enrollstatis_year" tabindex="1"
								id="_enrollstatis_year_recruitPlan"
								value="${condition['enrollstatis_year']}" style="width:180px" />
							<font color="red">*</font></li>

					</ul>
					<ul class="searchContent">
						<li style="width: 260px"><label>专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</label>
						<gh:selectModel name="enrollstatis_major" bindValue="resourceid"
								displayValue="majorName"
								value="${condition['enrollstatis_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<li style="width: 260px"><label>层&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：</label>
						<gh:selectModel name="enrollstatis_classic" bindValue="resourceid"
								displayValue="classicName"
								value="${condition['enrollstatis_classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:150px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>



		<div class="pageContent" layoutH="71">
			<span id="containerEnrollStatis"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="15%">报名人数</th>
						<th width="15%">来现场确认人数</th>
						<th width="15%">参加入学考试人数</th>
						<th width="10%">免考人数</th>
						<th width="10%">缺考人数</th>
						<th width="15%">被录取人数</th>
						<th width="15%">注册人数</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>&nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['total'] }" /> &nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['countNum1'] }" /> &nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['countNum2'] }" /> &nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['countNum3'] }" /> &nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['total'] - condition['countNum2'] - condition['countNum3']}" />
							&nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['countNum4'] }" /> &nbsp;</td>
						<td><fmt:formatNumber type="number"
								value="${condition['countNum5'] }" /> &nbsp;</td>
					</tr>
				</tbody>
			</table>
		</div>

	</div>

</body>
<script type="text/javascript">
				var chart;
			$(document).ready(function() {
				chart = new Highcharts.Chart({
					chart: {
						renderTo: 'containerEnrollStatis',
						defaultSeriesType: 'column'
					},
					title: {
						text: '${title}'
					},
					xAxis: {
						categories: [
							'${title}'
						]
					},
					yAxis: {
						min: 0,
						title: {
							text: '人数'
						}
					},
					legend: {
						layout: 'vertical',
						backgroundColor: '#FFFFFF',
						align: 'right',
						verticalAlign: 'top',
						x: 0,
						y: 70
					},
					tooltip: {
						formatter: function() {
							return ''+
								this.y +' 人';
						}
					},
					plotOptions: {
						column: {
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
				        series: [${chart}]
				});
				
				
			});			
		</script>
</html>