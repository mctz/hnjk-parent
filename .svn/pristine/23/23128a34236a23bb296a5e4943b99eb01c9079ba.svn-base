<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<c:if test="${ empty fromFlag }">
	<gh:loadCom components="jquery" />
	<link href="${baseUrl }/themes/css/luqu.css" rel="stylesheet"
		type="text/css" />
</c:if>
<title>学士学位考试成绩列表</title>
<script type="text/javascript">	
	
function bachelorExamValidate(){
	var studentName = $("#bachelorExam_studentName").val();
	var cerNum = $("#bachelorExam_studentid").val();
	
	if($.trim(studentName)==""){
		alert("请填写姓名！");
		return false;
	}
	if ($.trim(cerNum)==""){
		alert("请填写学号！ ");
		return false;
	}
	return true;
}
	
</script>
</head>
<body>
	<div id="query_main">
		<div class="leftside" style="margin-left: 20%; height: 800px;">
			<div class="list01">
				<form id="bachelorExam_from" onsubmit="return navTabSearch(this);"
					action="${baseUrl }/bachelorExam.html" method="post">
					<input type="hidden" name="fromPage" value="Y" />
					<table style="width: 100%; height: 200px;">
						<tr>
							<td>学&nbsp;&nbsp;号：</td>
							<td><input type="text" id="bachelorExam_studentid"
								name="studentNO" value="${condition['studentNO']}" /></td>
						</tr>
						<tr>
							<td>姓&nbsp;&nbsp;名：</td>
							<td><input type="text" id="bachelorExam_studentName"
								name="studentName" value="${condition['studentName']}" /></td>
						</tr>
						<tr>
							<td colspan="2">
								<button type="submit" onclick="return bachelorExamValidate()"
									style="cursor: pointer;">查 询</button>
							</td>
						</tr>
						<tr>
							<td colspan="2"><label style="color: #DD0000">${message }</label></td>
						</tr>
					</table>
				</form>
				<table width="900">
					<c:if test="${ea!=null}">

						<tr>
							<td width="">教 学 点:</td>
							<td width="">${ea.unitName }</td>
						</tr>
						<tr>
							<td width="">序&nbsp;号&nbsp;:</td>
							<td width="">${ea.ordinal }</td>
						</tr>
						<tr>
							<td width="">考 场 号 :</td>
							<td width="">${ea.examNo }</td>
						</tr>
						<tr>
							<td width="">座 位 号 :</td>
							<td width="">${ea.seatNo }</td>
						</tr>
						<tr>
							<td width="">学&nbsp;号&nbsp;:</td>
							<td width="">${ea.studentNO }</td>
						</tr>
						<tr>
							<td width="">姓&nbsp;名&nbsp;:</td>
							<td width="">${ea.studentName }</td>
						</tr>
						<tr>
							<td width="">英 语 成 绩 :</td>
							<td width="">${ea.examResults }</td>
						</tr>
						<tr>
							<td width="">考 试 时 间 :</td>
							<td width="">${ea.examTime }</td>
						</tr>
					</c:if>
				</table>
				<br />
			</div>
		</div>
	</div>
</body>
</html>
