<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=2,user-scalable=yes" />
	<title>教学点分布情况</title>

<script type="text/javascript">
function pupopen(resourceid,unitName,unitShortName,localCity,reportTime,reportSite,contectCall){
	window.open("${baseUrl}/edu3/system/org/unitInfo.html?resourceid="+resourceid, "_blank", "height=400, width=200,toolbar=no ,scrollbars=no,menubar=no"); 
}

</script>
</head>
<body>
	<table class="gridtable">
		<thead>
			<tr>
				<th colspan="3" style="height: 40px;font-size: 15px;">广东医科大学成人高等教育教学点分布情况</th>
			</tr>
			<tr>
				<th style="width: 10%;">序号</th>
				<th>教学点名称</th>
				<th style="width: 20%;">操作</th>
				<!-- <th>所在城市</th>
				<th>报到时间</th>
				<th>报道地点</th>
				<th>联系电话</th> -->
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orgUnitList}" var="unit" varStatus="vs">
				<tr>
					<td style="text-align: center;">${vs.index+1}</td>
					<%-- <td>${unit.unitShortName }</td> --%>
					<td>${unit.unitName }</td>
					<td style="text-align: center;"><button onclick="pupopen('${unit.resourceid }','${unit.unitName }','${unit.unitShortName }','${unit.localCity }','${unit.reportTime }','${unit.reportSite }','${unit.contectCall }')">查看</button></td>
					<%-- <td style="text-align: center;">${unit.localCity }</td>
					<td style="text-align: center;">${unit.reportTime }</td>
					<td style="text-align: center;">${unit.reportSite }</td>
					<td style="text-align: center;">${unit.contectCall }</td> --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>