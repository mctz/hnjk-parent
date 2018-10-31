<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出报名汇总表—条件选择</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="customerExportForm"
				action="${baseUrl}/edu3/recruit/enroll/export/statenrolleeinfo.html"
				class="pageForm">
				<table class="form" id="statEnrolleeinfoReportExport" layouth="60">
					<tr>
						<td>招生批次：</td>
						<td><select id="statEnrolleeinfoReportExport_recruitPlan"
							name="recruitPlan" multiple="multiple" style="width: 50%"
							size="5">
								<c:forEach items="${planList }" var="recruitPlan">
									<option value="${recruitPlan.resourceid }">${recruitPlan.recruitPlanname }</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>教学站：</td>
						<td><select id="statEnrolleeinfoReportExport_brSchool"
							name="brSchool" multiple='multiple' size="5" style="width: 50%">
								<c:forEach items="${brSchoolList }" var="brSchool">
									<option value="${brSchool.resourceid }">${brSchool.unitName }</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>层次：</td>
						<td><gh:selectModel id="statEnrolleeinfoReportExport_classic"
								name="classic" bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:120px" /></td>
					</tr>
					<tr>
						<td>专业：</td>
						<td><gh:selectModel id="statEnrolleeinfoReportExport_major"
								name="major" bindValue="resourceid" displayValue="majorName"
								value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorName desc" style="width:120px" /></td>
					</tr>
					<tr>
						<td>资格审核情况：</td>
						<td><select name="auditStatus" id="auditStatus">
								<option value="">请选择</option>
								<option value="a.signupflag='Y'">报名资格审核通过</option>
								<option value="a.entranceflag='Y'">入学资格审核通过</option>
								<option value="a.noexamflag='Y'">免试资格审核通过</option>
						</select></td>
					</tr>
				</table>
				<div>
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="exportExcelEnrolleeInfo();">导出</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>

	</div>

	<script type="text/javascript">

	function exportExcelEnrolleeInfo(){
		var recruitPlan = "";
		var brSchool    = "";
		var classic     = jQuery("#statEnrolleeinfoReportExport #statEnrolleeinfoReportExport_classic").val();
		var major       = jQuery("#statEnrolleeinfoReportExport #statEnrolleeinfoReportExport_major").val();
		var auditStatus = jQuery("#statEnrolleeinfoReportExport select[id=auditStatus] option:selected").val();
		var brschoolNum	= $("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_brSchool] option:selected").size();
		var k 			= 0;
		$("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_brSchool] option:selected").each(function(){
			brSchool+=$(this).val();
            if(k != brschoolNum -1 ) brSchool += ",";
            k++;
        })
        k = 0;
		var planNum	= $("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_recruitPlan] option:selected").size();
		$("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_recruitPlan] option:selected").each(function(){
			recruitPlan+=$(this).val();
            if(k != planNum -1 ) recruitPlan += ",";
            k++;
        })
        if(""==recruitPlan){
        	alertMsg.warn("请选择要导出汇总数据的招生批次！");
        	return false;
        }
		var url = "${baseUrl}/edu3/recruit/enroll/export/statenrolleeinfo.html?recruitPlan="+recruitPlan+"&brSchool="+brSchool+"&classic="+classic+"&major="+major+"&auditStatus="+auditStatus;
		//以免每次点击下载都创建一个iFrame，把上次创建的删除
		$('#frameForDownload_exportEnrolleeInfo').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frameForDownload_exportEnrolleeInfo";
		iframe.src = url;
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}

	function exportEnrolleeinfoReport(){
		
		var url         = "${baseUrl}/edu3/recruit/enroll/export/statenrolleeinfo.html";
		var recruitPlan = "";
		var classic     = jQuery("#statEnrolleeinfoReportExport #statEnrolleeinfoReportExport_classic").val();
		var major       = jQuery("#statEnrolleeinfoReportExport #statEnrolleeinfoReportExport_major").val();
		var brSchool    = jQuery("#statEnrolleeinfoReportExport #statEnrolleeinfoReportExport_brSchoolms2side__dx").val();
		
		var num  		= $("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_recruitPlan] option:selected").size();
		var k 			= 0;
		
		$("#statEnrolleeinfoReportExport select[id=statEnrolleeinfoReportExport_recruitPlan] option:selected").each(function(){
			recruitPlan+=$(this).val();
            if(k != num -1 ) recruitPlan += ",";
            k++;
        })
		
        if(""==recruitPlan){
        	alertMsg.warn("请选择要导出汇总数据的招生批次！");
        }
		
		//$.pdialog.closeCurrent();
		jQuery.post(url,{recruitPlan:recruitPlan,classic:classic,major:major,brSchool:brSchool},function(myJSON){
				
		},"json");
		
	}
	
</script>
</body>
</html>