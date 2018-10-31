<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置课程考试形式</title>
<script type="text/javascript">
		// 设置课程考试形式
		function subCourseExamForm(){
			var courseExamForm = $("#setCourseExamForm_courseExamForm").val();
			
			if(courseExamForm==""){
				alertMsg.warn("请选择课程考试形式！");
				return false;
			}
			
			var resourceids = $("#setCourseExamForm_resourceids").val();
			
			$("#setCourseExamForm_submit").attr("disabled","disabled");
			$("#setCourseExamForm_close").attr("disabled","disabled");
			$.ajax({
		 		url: '${baseUrl}/edu3/teaching/teachingplancoursestatus/setCourseExamForm.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{resourceids:resourceids,courseExamForm:courseExamForm},
		 		success: function(json){  
		 			if(json.returnCode= 200){
		 				alertMsg.correct(json.message);
		 				$.pdialog.closeCurrent();
		 				navTabPageBreak();
		 			} else {
		 				alertMsg.error(json.message);
		 			}
		 			$("#setCourseExamForm_submit").removeAttr("disabled");
					$("#setCourseExamForm_close").removeAttr("disabled");
				 }
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="setourseExamForm">
			<input id="setCourseExamForm_resourceids" name="resourceids"
				value="${resourceids }" type="hidden" />
			<div align="left" style="margin-left: 25px; margin-bottom: 10px;">
				<span style="font-size: 13px; font-weight: bolder;">课程考试形式：</span>
				<gh:select dictionaryCode="CodeCourseExamForm" name="courseExamForm"
					id="setCourseExamForm_courseExamForm" style="width:100px;" />
			</div>
		</div>

		<div style="margin-top: 100px; margin-right: 5px;" align="right">
			<button id="setCourseExamForm_submit" type="button"
				onclick="return subCourseExamForm();" style="cursor: pointer;">提交</button>
			<button id="setCourseExamForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>