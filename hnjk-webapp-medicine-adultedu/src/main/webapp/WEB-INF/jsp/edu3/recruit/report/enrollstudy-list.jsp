<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习模式</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/enrollStudy.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 280px"><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="enrollStudy_year" tabindex="2"
								id="_enrollStudy_recruitPlan"
								value="${condition['enrollStudy_year']}" style="width:180px" />

						</li>
						<c:if test="${enrollStudy_hide eq 'N'}">
							<li style="width: 300px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="enrollStudy_school" tabindex="1" id="enrollStudy_school"
									defaultValue="${condition['enrollStudy_school']}"
									style="width:150px" /></li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="enrollStudy_major" bindValue="resourceid"
								displayValue="majorName"
								value="${condition['enrollStudy_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="enrollStudy_classic" bindValue="resourceid"
								displayValue="classicName"
								value="${condition['enrollStudy_classic']}"
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
			<span id="containerEnrollStudy"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="35%">学习模式</th>
						<th width="30%">学生人数</th>
						<th width="30%">百分比</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${enrollStudyList}" var="e" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td><c:if test="${e['ISADULT'] eq 'Y'}">网络成人直属班</c:if> <c:if
									test="${e['ISADULT'] eq 'N'}">成人教育</c:if>&nbsp;</td>
							<td>${e['STU_NUM'] }&nbsp;</td>
							<td><fmt:formatNumber type="percent" pattern="###.####%"
									value="${e['percent'] }" /> &nbsp;</td>
						</tr>
					</c:forEach>
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
						renderTo: 'containerEnrollStudy',
						margin: [50, 200, 60, 170]
					},
					title: {
						text: '${title}'
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
									if (this.y > 5) return this.point.name;
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
				    series: [${chart}]
				});
			});
				
		</script>
</html>