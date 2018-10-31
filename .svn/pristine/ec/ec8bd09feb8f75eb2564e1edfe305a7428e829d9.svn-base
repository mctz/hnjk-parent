<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习模式统计</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/studymodel.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生批次：</label>
						<gh:selectModel name="recruit_year" bindValue="resourceid"
								displayValue="recruitPlanname"
								value="${condition['recruit_year']}" isSubOptionText="Y"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:150px" orderBy="publishDate desc" /></li>
						<c:if test="${recruit_hide eq 'N'}">
							<li style="width: 300px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="recruit_school" tabindex="1" id="recruit_school"
									defaultValue="${condition['recruit_school']}"
									style="width:150px" /> <%-- <gh:selectModel name="recruit_school" bindValue="resourceid" displayValue="unitName" value="${condition['recruit_school']}"
									modelClass="com.hnjk.security.model.OrgUnit" condition="unitType='brSchool'" style="width:150px"/>--%>
							</li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="recruit_major" bindValue="resourceid"
								displayValue="majorName" value="${condition['recruit_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="recruit_classic" bindValue="resourceid"
								displayValue="classicName"
								value="${condition['recruit_classic']}"
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
			<span id="containerModel"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="35%">学习模式</th>
						<th width="30%">报名学生人数</th>
						<th width="30%">百分比</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${stulist}" var="stu" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td><c:if test="${stu['ISADULT'] eq 'Y'}">网络成人直属班</c:if> <c:if
									test="${stu['ISADULT'] eq 'N'}">成人教育</c:if>&nbsp;</td>
							<td>${stu['STU_NUM'] }&nbsp;</td>
							<td><fmt:formatNumber type="percent" pattern="###.####%"
									value="${stu['percent'] }" /> &nbsp;</td>
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
						renderTo: 'containerModel',
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
							right: '50px',
							top: '100px'
						}
					},
				    series: [${chart}]
				});
			});
				
		</script>
</html>