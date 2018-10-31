<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印分教点交接表</title>
</head>
<body>

	<div class="page">
		<form id="examinationTransferListForm"
			action="${baseUrl}/edu3/teaching/exam/examPrint/printExaminationTransferList-view.html"
			onsubmit="return navTabAjaxDone(this);">
			<div class="pageHead" layoutH="77">
				<input type="hidden" id="examSubId" value="${examSubId}" /> <input
					type="hidden" id="printType" value="${type }" /> <select
					name="brSchoolSelect" id='brSchoolSelect' multiple='multiple'
					size='10' style="width: auto;">
					<c:forEach items="${brSchoolList }" var="brSchool">
						<option value="${brSchool[0] }">${brSchool[1] }</option>
					</c:forEach>
				</select>
				<!--
				<div class="buttonActive" style="float:right;"><div class="buttonContent"><button type="button" onclick="printExamInationTransferList();"> 打印 </button></div></div>
			 -->

			</div>
			<div class="formBar">
				<ul>
					<li style="float: right;"><div class="buttonActive">
							<div class="buttonContent">
								<button type="button" onclick="printExamInationTransferList();">提交</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
	<script type="text/javascript">
	$().ready(function(){
		$('#brSchoolSelect').multiselect2side({
				selectedPosition: 'right',
				moveOptions: false,
				labelsx: '',
				labeldx: ''
		});
	});
	function printExamInationTransferList(){
		
		var examSubId      = jQuery("#examSubId").val();
		var type           = jQuery("#printType").val();
		var brschoolSelect = jQuery("#brSchoolSelectms2side__dx").serializeArray();
		var brschoolIds    = new Array();
		jQuery.each( brschoolSelect, function(i,field){
			brschoolIds.push(field.value);
		});
		if(brschoolIds.length>0){
			$.pdialog.closeCurrent();
			var url = "${baseUrl}/edu3/teaching/exam/examPrint/printExaminationTransferList-view.html?examSubId="+examSubId+"&brschoolIds="+brschoolIds.toString()+"&type="+type;
			$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_STUDY_CENTER_TRANSFER_LIST_VIEW','打印预览',{height:600, width:800});
		}else {
			alertMsg.warn("请选择至少一个教学站!");
			return false;
		}
	}
</script>
</body>
</html>