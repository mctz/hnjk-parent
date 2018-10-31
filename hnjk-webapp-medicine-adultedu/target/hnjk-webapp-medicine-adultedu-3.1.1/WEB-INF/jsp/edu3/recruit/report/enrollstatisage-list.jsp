<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生年龄统计</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/enrollStatisticAge.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生批次：</label>
						<gh:selectModel name="age_year" bindValue="resourceid"
								displayValue="recruitPlanname" value="${condition['age_year']}"
								isSubOptionText="Y"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:150px" orderBy="publishDate desc" /></li>
						<c:if test="${age_hide eq 'N'}">
							<li style="width: 300px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="age_school" tabindex="1" id="age_school"
									defaultValue="${condition['age_school']}" style="width:150px" />
								<%-- <gh:selectModel name="age_school" bindValue="resourceid" displayValue="unitName" value="${condition['age_school']}"
									modelClass="com.hnjk.security.model.OrgUnit" condition="unitType='brSchool'" style="width:150px"/>--%>
							</li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="age_major" bindValue="resourceid"
								displayValue="majorName" value="${condition['age_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="age_classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['age_classic']}"
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
			<span id="containerEnrollStatisAge"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="15%">报名总人数</th>
						<th width="15%">20岁以下(含)</th>
						<th width="15%">20岁~30岁(含)</th>
						<th width="15%">30岁~40岁(含)</th>
						<th width="15%">40岁~50岁(含)</th>
						<th width="20%">50岁以上(含)</th>
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
						renderTo: 'containerEnrollStatisAge',
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