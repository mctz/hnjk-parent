<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印报名汇总表—条件选择</title>


</head>
<body>
	<div class="page">
		<div class="pageContent">

			<table class="form" id="statEnrolleeinfoReportPrint">
				<tr>
					<td>招生批次：</td>
					<td><gh:selectModel
							id="statEnrolleeinfoReportPrint_recruitPlan" name="recruitPlan"
							bindValue="resourceid" displayValue="recruitPlanname"
							modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
							value="${condition['recruitPlan']}" isSubOptionText="Y"
							orderBy="yearInfo.firstYear desc" choose="Y" /></td>
				</tr>
				<tr>
					<td>层次：</td>
					<td><gh:selectModel id="statEnrolleeinfoReportPrint_classic"
							name="classic" bindValue="resourceid" displayValue="classicName"
							modelClass="com.hnjk.edu.basedata.model.Classic"
							value="${condition['classic']}" style="width:120px" /></td>
				</tr>
				<tr>
					<td>专业：</td>
					<td><gh:selectModel id="statEnrolleeinfoReportPrint_major"
							name="major" bindValue="resourceid" displayValue="majorName"
							value="${condition['major']}"
							modelClass="com.hnjk.edu.basedata.model.Major"
							style="width:120px" /></td>
				</tr>


				<tr>
					<td>教学站：</td>
					<td><select id="statEnrolleeinfoReportPrint_brSchool"
						name="statEnrolleeinfoReportPrint_brSchool" multiple='multiple'
						size="10">
							<c:forEach items="${brSchoolList }" var="brSchool">
								<option value="${brSchool.resourceid }">${brSchool.unitName }</option>
							</c:forEach>
					</select></td>
				</tr>
			</table>
			<div>
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button id="plansubmit" type="button"
									onclick="printEnrolleeinfoReport();">打印</button>
							</div>
						</div>
					</li>
				</ul>
			</div>

		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$('#statEnrolleeinfoReportPrint_brSchool').multiselect2side({
			selectedPosition: 'right',
			moveOptions: false,
			labelsx: '',
			labeldx: ''
		});
	})
	function printEnrolleeinfoReport(){
		var recruitPlan = jQuery("#statEnrolleeinfoReportPrint #statEnrolleeinfoReportPrint_recruitPlan").val();
		var classic     = jQuery("#statEnrolleeinfoReportPrint #statEnrolleeinfoReportPrint_classic").val();
		var major       = jQuery("#statEnrolleeinfoReportPrint #statEnrolleeinfoReportPrint_major").val();
		var brSchool    = jQuery("#statEnrolleeinfoReportPrint #statEnrolleeinfoReportPrint_brSchoolms2side__dx").val();
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/recruit/enroll/print/statenrolleeinforeport-view.html?recruitPlan="+recruitPlan+"&classic="+classic+"&major="+major+"&brSchool="+brSchool;
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_EXAMFORM_VIEW','打印预览',{height:600, width:800});
		
	}
	
</script>
</body>
</html>