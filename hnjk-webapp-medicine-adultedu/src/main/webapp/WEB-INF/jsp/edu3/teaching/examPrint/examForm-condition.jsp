<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试打印</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<input type="hidden" id="chooseExamSubId" value="${examSubId}" />
			<div class="searchBar">
				<ul class="searchContent">
					<li style="width: 250px"><label>年级：</label>
					<gh:selectModel id="gradeId" name="gradeid" bindValue="resourceid"
							displayValue="gradeName"
							modelClass="com.hnjk.edu.basedata.model.Grade"
							value="${condition['gradeid']}" orderBy="gradeName desc"
							style="width:120px" /></li>
					<li style="width: 250px"><label>专业：</label>
					<gh:selectModel id="majorId" name="major" bindValue="resourceid"
							displayValue="majorCodeName" value="${condition['major']}"
							modelClass="com.hnjk.edu.basedata.model.Major"
							style="width:120px" /></li>
					<li style="width: 250px"><label>层次：</label>
					<gh:selectModel id="classicId" name="classic"
							bindValue="resourceid" displayValue="classicName"
							modelClass="com.hnjk.edu.basedata.model.Classic"
							value="${condition['classic']}" style="width:120px" /></li>
				</ul>

			</div>
		</div>
		<div class="buttonActive" style="float: right;">
			<div class="buttonContent">
				<button type="button" onclick="printExamForm();">打印</button>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	function printExamForm(){
		var examSubId = jQuery("#chooseExamSubId").val();
		var gradeId   = jQuery("#gradeId").val();
		var majorId   = jQuery("#majorId").val();
		var classicId = jQuery("#classicId").val();
		
		
		//var gradeId = jQuery("#gradeId").val();
		if(""==gradeId || null == gradeId){
			alertMsg.warn("请选择要打印考试形式的年级!");
			return false;
		}
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/teaching/exam/examPrint/printExamForm-view.html?examSubId="+examSubId+"&gradeId="+gradeId+"&majorId="+majorId+"&classicId="+classicId;
		$.pdialog.open(url,'RES_TEACHING_EXAM_PRINT_EXAMFORM_VIEW','打印预览',{height:600, width:800});
		
	}
</script>
</body>
</html>