<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报名方式</title>
<gh:loadCom components="cnArea" />
</head>
<body>
	<script type="text/javascript">
	jQuery(document).ready(function(){	
		jQuery("#ChinaArea1").jChinaArea({
			//	 aspnet:true,
				 //s1:"广东省",//默认选中的省名
		 		 //s2:"广州市",//默认选中的市名
			     //s3:"天河区"//默认选中的县区名
		 });
		 jQuery("#ChinaArea2").jChinaArea({
			//aspnet:true,
			 //s1:"广东省",//默认选中的省名
		     //s2:"广州市",//默认选中的市名
			 //s3:"天河区"//默认选中的县区名
		 }); 
		 jQuery("#residence_province").append("<option value='' selected='selected'>请选择</option>");
		 jQuery("#residence_city").append("<option value='' selected='selected'>请选择</option>");
		 jQuery("#residence_county").append("<option value='' selected='selected'>请选择</option>");
		 jQuery("#homePlace_province").append("<option value='' selected='selected'>请选择</option>");
		 jQuery("#homePlace_city").append("<option value='' selected='selected'>请选择</option>");
		 jQuery("#homePlace_county").append("<option value='' selected='selected'>请选择</option>");
	});	 
	function statisticSearch(Obj){
		var recruitPlan   = jQuery("#statisticForm  select[id='recruitPlan'] option:selected").val();
		var statisticType = jQuery("#statisticForm  select[id='statisticType'] option:selected").val();
		if(statisticType==""){ 
			alertMsg.info("请选择一个统计方向!"); 
			return false;
		}
		//if(recruitPlan==""){   
			//alertMsg.info("请选择一个招生批次!"); 
			//return false;
		//}
		return navTabSearch(Obj);
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="statisticForm" onsubmit="return statisticSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/enrollStatisticStuInfo.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 280px"><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlan" tabindex="2"
								id="_enrollStatisticStuInfo_recruitPlan"
								value="${condition['recruitPlan']}" style="width:180px" /></li>

						<li style="width: 250px"><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:150px" /></li>
						<li style="width: 250px"><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:150px" /></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 300px"><label>性别：</label>
						<gh:select name='sex' dictionaryCode="CodeSex"
								value="${condition['sex']}" style="width:150px" /></li>
						<li style="width: 250px"><label>民族：</label>
						<gh:select name='nation' dictionaryCode="CodeNation"
								value="${condition['nation']}" style="width:150px" /></li>
						<li style="width: 250px"><label>婚否：</label> <gh:select
								name="marriage" dictionaryCode="CodeMarriage"
								value="${condition['marriage']}" style="width:150px" /></li>

					</ul>
					<ul class="searchContent">
						<li style="width: 300px"><label>政治面貌：</label>
						<gh:select name='politics' dictionaryCode="CodePolitics"
								value="${condition['politics']}" style="width:150px" /></li>
						<c:if test="${isBrschool != 'Y'}">
							<li style="width: 300px"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="brSchool" tabindex="1" id="brSchool"
									defaultValue="${condition['brSchool']}" style="width:150px" />
								<%-- <gh:selectModel name="brSchool" bindValue="resourceid" displayValue="unitName" value="${condition['brSchool']}"
									modelClass="com.hnjk.security.model.OrgUnit" condition="unitType='brSchool'" style="width:150px"/>--%>
							</li>
						</c:if>
					</ul>

					<ul class="searchContent">
						<div id="ChinaArea1">
							<label>籍贯：</label> <select id="homePlace_province"
								name="homePlace_province" style="width: 150px;"></select> <select
								id="homePlace_city" name="homePlace_city" style="width: 150px;"></select>
							<select id="homePlace_county" name="homePlace_county"
								style="width: 150px;">
							</select>
						</div>
					</ul>

					<ul class="searchContent">
						<div id="ChinaArea2">
							<label>户口所在地：</label> <select id="residence_province"
								name="residence_province" style="width: 150px;"></select> <select
								id="residence_city" name="residence_city" style="width: 150px;"></select>
							<select id="residence_county" name="residence_county"
								style="width: 150px;">
							</select>
						</div>
					</ul>
					<ul class="searchContent">
						<div>

							<label style="color: red">统计方向：</label>
							<gh:select id='statisticType' name='statisticType'
								dictionaryCode="CodeStatisticType"
								value="${condition['statisticType']}" style="width:150px" />
							<font color="red">(*必选项)</font>
						</div>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent" layoutH="175">
			<span id="statisticSpan"
				style="width: 800px; height: 400px; margin: 0 auto"></span>
			<table class="list" style="width: 96%">
				<thead>
					<tr>
						<th width="5%">编号</th>
						<th width="35%">${tdStr }</th>
						<th width="30%">报名学生人数</th>
						<th width="30%">百分比</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${resultList}" var="a" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${a[statisticColumn] }</td>
							<td>${a['STU_NUM'] }&nbsp;</td>
							<td><fmt:formatNumber type="percent" pattern="###.####%"
									value="${a['percent'] }" /> &nbsp;</td>
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
						renderTo: 'statisticSpan',
						margin: [50, 200, 60, 170]
					},
					title: {
						text: '${reportTitle}'
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