<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费明细</title>

</head>
<body>
	<table border="1">
		<thead>
			<tr>
				<th style="text-align: center; vertical-align: middle; width: 10%">教学点</th>
				<th style="text-align: center; vertical-align: middle; width: 10%">学号</th>
				<th style="text-align: center; vertical-align: middle; width: 5%">年级</th>
				<th style="text-align: center; vertical-align: middle; width: 10%">班级</th>
				<th style="text-align: center; vertical-align: middle; width: 5%">层次</th>
				<th style="text-align: center; vertical-align: middle; width: 5%">学习形式</th>
				<th style="text-align: center; vertical-align: middle; width: 10%">缴费标准</th>
			</tr>
		</thead>
		<tbody id="viewNotPayBody">
			<c:forEach items="${list}" var="stu" varStatus="vs">
				<tr>
					<td style="text-align: center; vertical-align: middle;">${stu.unitname}</td>
					<td style="text-align: center; vertical-align: middle;">${stu.studyno}&nbsp;</td>
					<td style="text-align: center; vertical-align: middle;">${stu.gradename}</td>
					<td style="text-align: center; vertical-align: middle;">${stu.classesname}</td>
					<td style="text-align: center; vertical-align: middle;">${stu.classicname}</td>
					<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType) }</td>
					<td style="text-align: center; vertical-align: middle;">${stu.creditfee}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>