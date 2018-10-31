<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <title>专业设置</title>
    <meta charset="utf-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="专业设置">
    <meta http-equiv="description" content="专业设置列表">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${baseUrl }/css/zzsc-demo.css">
	<link rel="stylesheet" type="text/css" href="${baseUrl }/css/style.css" />
	<link rel="stylesheet" type="text/css" href="${baseUrl }/css/basictable.css" />
  <body>
  </body>
	<div>
	  <table id="wx_majorSet" style="margin: 0px;">
		<thead>
		  <tr>
			<th>序号</th>
			<th>层次</th>
			<th>专业名称</th>
		  </tr>
		</thead>
		<tbody>
		<c:forEach items="${majorList }" var="major" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${major.classicName }</td> 
				<td>${major.majorName }</td>
			  </tr>
		</c:forEach>
		</tbody>
	  </table>
	</div>
	<script src="${baseUrl }/js/jquery-1.11.0.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${baseUrl }/js/jquery.basictable.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			  $('#wx_majorSet').basictable({
				breakpoint: 318
			  });
		});
  </script>
</html>
