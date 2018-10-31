<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约考试统计导出—条件选择</title>


</head>
<body>
	<div class="page">
		<div class="pageContent">

			<input type="hidden" id="examinerTransferTableCondition_examSubId"
				name="examSubId" value="${examSubId}" /> <input type="hidden"
				id="examinerTransferTableCondition_operatingType"
				name="operatingType" value="${operatingType}" /> <input
				type="hidden" id="examinerTransferTableCondition_flag" name="flag"
				value="${flag}" />

			<table class="form" id="examinerTransferTable_Condition_Table">
				<tr>
					<td>教学站：</td>
					<td><select id="examinerTransferTable_Condition_BranchSchool"
						name="examinerTransferTable_Condition_BranchSchool"
						multiple='multiple' size="10">
							<c:forEach items="${list }" var="brSchool">
								<option value="${brSchool.resourceid }">${brSchool.unitCode}-${brSchool.unitName }</option>
							</c:forEach>
					</select></td>
				</tr>
			</table>
			<table class="form"
				id="examinerTransferTable_Condition_examMode_Table">
				<tr>
					<td><input type="radio" name="isMachineExam" value="Y" />机考 <input
						type="radio" name="isMachineExam" value="N" />笔试</td>
				</tr>
			</table>
			<div>
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<c:choose>
									<c:when test="${operatingType eq 'export' }">
										<button type="button"
											onclick="executeExaminerTransferTableExport();">导出</button>
									</c:when>
									<c:when test="${operatingType eq 'print' }">
										<button type="button"
											onclick="executeExaminerTransferTablePrint();">打印</button>
									</c:when>
								</c:choose>

							</div>
						</div>
					</li>
				</ul>
			</div>

		</div>
	</div>

	<script type="text/javascript">
	
	$(document).ready(function(){
		$('#examinerTransferTable_Condition_BranchSchool').multiselect2side({
			selectedPosition: 'right',
			moveOptions: false,
			labelsx: '',
			labeldx: ''
		});
	})
	//打印
	function executeExaminerTransferTablePrint(){
		var examSubId   = $("#examinerTransferTableCondition_examSubId").val();
		var flag	    = $("#examinerTransferTableCondition_flag").val();
		var isMachineExam = $("input[name='isMachineExam']:checked").val();
		var brSchool    = jQuery("#examinerTransferTable_Condition_Table #examinerTransferTable_Condition_BranchSchoolms2side__dx").val();

		if(null==examSubId || ""==examSubId){
			alertMsg.warn("未选择要导出巡考员交接表的考试批次！");
			return false;
		}
		if(null==brSchool ||""==brSchool){
			alertMsg.warn("未选择要导出巡考员交接表的教学站！");
			return false;
		}
		if(undefined==isMachineExam){isMachineExam = "";}
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/teaching/exam/print/transferTable-view.html?flag="+flag+"&examSubId="+examSubId+"&unitIds="+brSchool+"&isMachineExam="+isMachineExam;
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_EXAMINERTRANSFERTABLE','打印预览',{height:600, width:800,mask:true})
	}
	//导出
	function executeExaminerTransferTableExport(){
		var examSubId   = $("#examinerTransferTableCondition_examSubId").val();
		var flag	    = $("#examinerTransferTableCondition_flag").val();
		var isMachineExam = $("input[name='isMachineExam']:checked").val();
		var brSchool    = jQuery("#examinerTransferTable_Condition_Table #examinerTransferTable_Condition_BranchSchoolms2side__dx").val();

		if(null==examSubId || ""==examSubId){
			alertMsg.warn("未选择要导出巡考员交接表的考试批次！");
			return false;
		}
		if(null==brSchool ||""==brSchool){
			alertMsg.warn("未选择要导出巡考员交接表的教学站！");
			return false;
		}
		if(undefined==isMachineExam){isMachineExam = "";}
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/teaching/exam/export/transferTable.html?flag="+flag+"&examSubId="+examSubId+"&unitIds="+brSchool+"&isMachineExam="+isMachineExam;
		downloadFileByIframe(url,'examinerTransferTableExportIframe');
	}
	
</script>
</body>
</html>