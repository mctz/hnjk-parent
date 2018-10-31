<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%> 
<head> 
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=2,user-scalable=yes" />
    <title>教学点信息</title>
    <style type="text/css">
    	td{height: 50px;}
    </style>
</head>
<body bgcolor="#FAFAFA">
	<div align="center" style="padding-top: 20px;padding-bottom: 30px;font-size: 19px;font-weight: bold;">${unit.unitShortName }</div>
	<table style="background-color: #FFFFFF">
		<tr>
			<td style="width: 30%">所在城市：</td><td style="width: 70%">${unit.localCity }</td>
		</tr>
		<tr>
			<td>报到时间：</td><td>${unit.reportTime }</td>
		</tr>
		<tr>
			<td>报到地点：</td><td>${unit.reportSite }</td>
		</tr>
		<tr>
			<td>联系电话：</td><td>${unit.contectCall }</td>
		</tr>
		<tr><td><td><td></td></tr>
	</table>
</body>
</html>