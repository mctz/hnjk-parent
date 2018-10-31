<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>访问日志统计</title>
<script type="text/javascript">
	function accesslogsDatetyoeOnchange(type){
		$("input[name='accessDate']").attr("disabled",true).val("").hide();
		if(type == '1' || type == '2'){
			$("#accesslogsDateli").show();
			$("#accesslogsDate"+type).attr("disabled",false).removeClass("disabled").show();
		} else {
			$("#accesslogsDateli").hide();
		}
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/accesslogs/status/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>时间类型：</label> <select name="type"
							onchange="accesslogsDatetyoeOnchange(this.value)">
								<option value="1"
									<c:if test="${condition['type'] eq '1' }">selected="selected"</c:if>>按日</option>
								<option value="2"
									<c:if test="${condition['type'] eq '2' }">selected="selected"</c:if>>按月</option>
								<option value="3"
									<c:if test="${condition['type'] eq '3' }">selected="selected"</c:if>>近三个月</option>
								<option value="6"
									<c:if test="${condition['type'] eq '6' }">selected="selected"</c:if>>近六个月</option>
						</select></li>
						<li id="accesslogsDateli"
							<c:if test="${(condition['type'] eq '3') or (condition['type'] eq '6') }">style="display: none;"</c:if>>
							<label>日期：</label> <input type="text" id="accesslogsDate1"
							name="accessDate"
							<c:if test="${(not empty condition['type']) and (condition['type'] ne '1') }">style="display: none;" disabled="disabled"</c:if>
							value="${condition['accessDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-{%d-1}',readOnly:true,isShowWeek:true})" />
							<input type="text" id="accesslogsDate2" name="accessDate"
							<c:if test="${condition['type'] ne '2' }">style="display: none;" disabled="disabled"</c:if>
							value="${condition['accessDate']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',readOnly:true,isShowWeek:true})" />
						</li>
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


		<div class="pageContent" layoutH="71">
			<div class="tabs">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li><a href="#"><span>用户人数</span></a></li>
							<li><a href="#"><span>资源总数</span></a></li>
							<li><a href="#"><span>流量总数</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<!-- 1 -->
					<div>
						<span id="containerAccessLogs1"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
					</div>
					<div>
						<span id="containerAccessLogs2"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
					</div>
					<div>
						<span id="containerAccessLogs3"
							style="width: 800px; height: 400px; margin: 0 auto"></span>
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
	  chart1 = accessLogsChart('containerAccessLogs1','${title} 用户人数统计','用户人数(个)','个',[${chart1}]);
      chart2 = accessLogsChart('containerAccessLogs2','${title} 资源总数统计','资源总数(个)','个',[${chart2}]);
      chart3 = accessLogsChart('containerAccessLogs3','${title} 流量总数统计','流量总数(字节)','字节',[${chart3}]);
});

function accessLogsChart(toId,title,ytext,units,arr){
	return new Highcharts.Chart({
	      chart: {
	          renderTo: toId,
	          defaultSeriesType: 'spline',
	          marginRight: 130,
	          marginBottom: 25
	       },
	       title: {
	          text: title,
	          x: -20 //center
	       },      
	       xAxis: {
	     	  type: 'datetime',
	     	  dateTimeLabelFormats:{
	     		hour: '%H:%M',
	     		day: '%e',
	     		year: '%Y'
	     	  }	     	  
	       },
	       yAxis: {
	          title: {
	             text: ytext
	          },
	          min:0,
	          plotLines: [{
	             value: 0,
	             width: 1,
	             color: '#808080'
	          }]
	       },
	       tooltip: {
	          formatter: function() {
	        	  		var ty = "${condition['type']}";
	        	  		var f = (ty=='1')?'%H:00':'%b%e日';
	                    return '<b>'+ this.series.name +'</b><br/>'+
	                Highcharts.dateFormat(f, this.x) +': '+ this.y +units; 
	          }
	       },
	       legend: {
	          layout: 'vertical',
	          align: 'right',
	          verticalAlign: 'top',
	          x: -10,
	          y: 100,
	          borderWidth: 0
	       },
	       plotOptions: {
	           spline: {
	              lineWidth: 4,
	              states: {
	                 hover: {
	                    lineWidth: 5
	                 }
	              },
	              marker: {
	                 enabled: false,
	                 states: {
	                    hover: {
	                       enabled: true,
	                       symbol: 'circle',
	                       radius: 5,
	                       lineWidth: 1
	                    }
	                 }   
	              },
	              pointInterval: ${pointInterval}, 
	              pointStart: ${pointStart}
	           }
	        },
	       series: arr
	    });
}
</script>
</body>
</html>