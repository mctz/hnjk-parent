<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出试室安排总表条件</title>

<script type="text/javascript">
function exportExamRoomPlanExcel(){
	//以免每次点击下载都创建一个iFrame，把上次创建的删除
	$('#frame_exportStudentChangeStat').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportStudentChangeStat";
	var brSchoolId = $('#ExamRoomPlan_eiinfo_brSchoolName').val();
	var examPlanId = $('#ExamRoomPlanSearchExamSub').val();
	if(""==brSchoolId&&""==examPlanId){
		alert("请选择需要导出的教学站和考试批次");
	}
	iframe.src = "${baseUrl }/edu3/teaching/exam/seat/exportExamRoomPlanExcel.html?brSchooId="+brSchoolId+"&examPlanId="+examPlanId;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
</script>
</head>
<body>
	<div class="page">
		<div id="examRoomPlan" class="pageContent" defH="150">
			<ul>
				<li><label>教学站：</label> <gh:brSchoolAutocomplete
						name="branchSchool" tabindex="1"
						id="ExamRoomPlan_eiinfo_brSchoolName" defaultValue=""
						displayType="code" style="width:55%" /></li>
				<li><label>考试批次：</label> <gh:selectModel
						id="ExamRoomPlanSearchExamSub" name="examSub"
						bindValue="resourceid" displayValue="batchName" style="width:55%"
						modelClass="com.hnjk.edu.teaching.model.ExamSub" value=""
						condition="batchType='exam',examsubStatus='2'"
						orderBy="batchName desc" /></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button type="button" onclick="exportExamRoomPlanExcel()">导出</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>