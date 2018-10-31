<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生缴费明细</title>
<script type="text/javascript">
//导出
function exportViewForm(){
	$('#frame_exportExcel').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportExcel";
	var brSchool    = "${condition['brSchool']}";
	var operatedate = "${condition['operatedate']}";
	var stuids = "";
	$("#viewNotPayBody input[name='resourceid']:checked").each(function(){
		var checekObj = $(this);
		if(""==stuids){
			stuids += "'"+checekObj.val()+"'";
		}else{
			stuids += ",'"+checekObj.val()+"'";
		}
	});
	iframe.src = "${baseUrl}/edu3/finance/feeCommissionInfo/viewNotPay.html?brSchool="
		+brSchool+"&operatedate="+operatedate+"&stuids="+stuids+"&flag=export";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button onclick="exportViewForm()">导 出</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
		<div class="pageContent">
			<table id="viewNotPayForm" class="table" layouth="85">
				<thead>
					<tr>
						<th style="text-align: center; vertical-align: middle;" width="3%"><input
							type="checkbox" name="checkall" id="check_all_notpay"
							onclick="checkboxAll('#check_all_notpay','resourceid','#viewNotPayBody')" /></th>
						<th style="text-align: center; vertical-align: middle; width: 10%">教学点</th>
						<th style="text-align: center; vertical-align: middle; width: 8%">学号</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">年级</th>
						<th style="text-align: center; vertical-align: middle; width: 15%">班级</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">层次</th>
						<th style="text-align: center; vertical-align: middle; width: 5%">学习形式</th>
						<th style="text-align: center; vertical-align: middle; width: 8%">缴费标准</th>
					</tr>
				</thead>
				<tbody id="viewNotPayBody">
					<c:forEach items="${objPage.result}" var="stu" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;"><input
								type="checkbox" name="resourceid" value="${stu.studyno}"
								autocomplete="off" /></td>
							<td style="text-align: center; vertical-align: middle;">${stu.unitname}</td>
							<td style="text-align: center; vertical-align: middle;">${stu.studyno}</td>
							<td style="text-align: center; vertical-align: middle;">${stu.gradename}</td>
							<td style="text-align: center; vertical-align: middle;">${stu.classesname}</td>
							<td style="text-align: center; vertical-align: middle;">${stu.classicname}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTeachingType',stu.teachingType) }</td>
							<td style="text-align: center; vertical-align: middle;">${stu.creditfee}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/finance/feeCommissionInfo/viewNotPay.html"
				condition="${condition }" targetType="dialog" pageType="sys" />
		</div>
	</div>
</body>
</html>