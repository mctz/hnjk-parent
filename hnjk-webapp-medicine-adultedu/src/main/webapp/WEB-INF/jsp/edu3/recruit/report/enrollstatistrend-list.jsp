<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生人数趋势统计</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/enrollStatisticTrend.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>起始招生批次：</label>
						<gh:selectModel id="start_enrollstatis_year"
								name="start_enrollstatis_year" bindValue="startDate"
								displayValue="recruitPlanname"
								value="${condition['start_enrollstatis_year']}"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:55%" orderBy="startDate desc" choose="N"
								onclick="recruitYearStart();" /><font color="red">*</font></li>
						<li><label>结束招生批次：</label>
						<gh:selectModel id="end_enrollstatis_year"
								name="end_enrollstatis_year" bindValue="startDate"
								displayValue="recruitPlanname"
								value="${condition['end_enrollstatis_year']}"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:55%" orderBy="startDate desc" choose="N"
								onclick="recruitYearEnd();" /><font color="red">*</font></li>
					</ul>
					<ul class="searchContent">
						<c:if test="${enrollstatis2_hide eq 'N'}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="enrollstatis2_school" tabindex="1"
									id="enrollstatis2_school"
									defaultValue="${condition['enrollstatis2_school']}"
									style="width:55%" /> <%--<gh:selectModel name="enrollstatis2_school" bindValue="resourceid" displayValue="unitName" value="${condition['enrollstatis2_school']}"
									modelClass="com.hnjk.security.model.OrgUnit" condition="unitType='brSchool'" style="width:150px"/>
				--%></li>
						</c:if>
						<li><label>专业：</label>
						<gh:selectModel name="enrollstatis2_major" bindValue="resourceid"
								displayValue="majorName"
								value="${condition['enrollstatis2_major']}"
								modelClass="com.hnjk.edu.basedata.model.Major" style="width:55%" />
						</li>
						<li><label>层次：</label>
						<gh:selectModel name="enrollstatis2_classic"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['enrollstatis2_classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:55%" /></li>
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
			<span id="containerEnrollStatisTrend"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="10%"></th>
						<th width="15%">报名人数</th>
						<th width="15%">来现场确认人数</th>
						<th width="15%">参加入学考试人数</th>
						<th width="10%">免考人数</th>
						<th width="10%">缺考人数</th>
						<th width="10%">被录取人数</th>
						<!--<th width="15%">注册人数</th>    
				-->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${trendlist}" var="t" varStatus="vs">
						<tr>
							<td>${t.recruitName}</td>
							<td><fmt:formatNumber type="number" value="${t.apply}" />
								&nbsp;</td>
							<td><fmt:formatNumber type="number" value="${t.confirm}" />
								&nbsp;</td>
							<td><fmt:formatNumber type="number" value="${t.exam}" />
								&nbsp;</td>
							<td><fmt:formatNumber type="number" value="${t.exemption}" />
								&nbsp;</td>
							<td><fmt:formatNumber type="number"
									value="${t.confirm - t.exam - t.exemption}" /> &nbsp;</td>
							<td><fmt:formatNumber type="number" value="${t.enroll}" />
								&nbsp;</td>
							<!--<td ><fmt:formatNumber type="number" value="${t.register}"/> &nbsp;</td>
			    -->
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
				renderTo: 'containerEnrollStatisTrend',
				defaultSeriesType: 'line',
				marginRight: 130,
				marginBottom: 25
			},
			title: {
				text: '${title}',
				x: -20 //center
			},
			xAxis: {
				categories: [${xTitle}]
			},
			yAxis:{
				title: {
					text: '人数'
				},
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			tooltip: {
				formatter: function() {
		                return '<b>'+ this.series.name +'</b><br/>'+
						this.x +': '+ this.y +'个';
				}
			},
			legend:{
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'top',
				x: -10,
				y: 100,
				borderWidth: 0
			},
			series: [${chart}]
		});
	});
	
	function recruitYearStart(){
			$("#start_enrollstatis_year > option").each(function(ind){
				if(StringToDate($("#end_enrollstatis_year").val()) <= StringToDate($(this).val())){
					$(this).attr("disabled",true);
				}else{
					$(this).attr("disabled",false);
				}
			});
	}
	function recruitYearEnd(){
			$("#end_enrollstatis_year > option").each(function(ind){
				if(StringToDate($("#start_enrollstatis_year").val()) >= StringToDate($(this).val())){
					$(this).attr("disabled",true);
				}else{
					$(this).attr("disabled",false);
				}
			});
	}
	
	//+---------------------------------------------------  
	//| 字符串转成日期类型   
	//| 格式 MM/dd/YYYY MM-dd-YYYY YYYY/MM/dd YYYY-MM-dd  
	//+---------------------------------------------------  
	function StringToDate(DateStr) {   
	    var converted = Date.parse(DateStr);  
	    var myDate    = new Date(converted);  
	    if (isNaN(myDate)){   
	        var arys= DateStr.split('-');  
	        myDate = new Date(arys[0],--arys[1],arys[2]);  
	    }  
	    return myDate;  
	}	
</script>
</html>