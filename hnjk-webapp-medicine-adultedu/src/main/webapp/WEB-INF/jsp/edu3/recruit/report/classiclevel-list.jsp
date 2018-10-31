<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>层次统计</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/classiclevel.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生批次：</label>
						<gh:selectModel name="classic_year" bindValue="resourceid"
								displayValue="recruitPlanname"
								value="${condition['classic_year']}" isSubOptionText="Y"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:150px" orderBy="publishDate desc" /></li>
						<c:if test="${classic_hide eq 'N'}">
							<li style="width: 260px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="classic_school" tabindex="1" id="classic_school"
									defaultValue="${condition['classic_school']}"
									style="width:150px" /> <%--<gh:selectModel name="classic_school" bindValue="resourceid" displayValue="unitName" value="${condition['classic_school']}"
									modelClass="com.hnjk.security.model.OrgUnit" condition="unitType='brSchool'" style="width:150px"/> --%>
							</li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="classic_major" bindValue="resourceid"
								displayValue="majorName" value="${condition['classic_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<!-- 
				<li>
					<label>层次：</label><gh:selectModel name="classic_classic" bindValue="resourceid" displayValue="classicName" value="${condition['classic_classic']}"
							modelClass="com.hnjk.edu.basedata.model.Classic" style="width:150px"/>
				</li>
				 -->
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
			<span id="containerClassic"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="35%">层次名称</th>
						<th width="30%">报名学生人数</th>
						<th width="30%">百分比</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${classicList}" var="c" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${c['classicname'] } &nbsp;</td>
							<td>${c['STU_NUM'] }&nbsp;</td>
							<td><fmt:formatNumber type="percent" pattern="###.####%"
									value="${c['percent'] }" /> &nbsp;</td>
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
						renderTo: 'containerClassic',
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