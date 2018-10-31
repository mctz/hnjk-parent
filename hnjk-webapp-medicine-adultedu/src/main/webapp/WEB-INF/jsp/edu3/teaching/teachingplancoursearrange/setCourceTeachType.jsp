<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置课程教学类型</title>
<script type="text/javascript">
		// 设置课程教学类型
		function subCourseTeachType(){
			var courseTeachType = $("#setCourseTeachTypeForm_courseTeachType").val();
			
			if(courseTeachType==""){
				alertMsg.warn("请选择课程教学类型！");
				return false;
			}
			
			var resourceids = $("#setCourseTeachTypeForm_resourceids").val();
			
			$("#setCourseTeachTypeForm_submit").attr("disabled","disabled");
			$("#setCourseTeachTypeForm_close").attr("disabled","disabled");
			$.ajax({
		 		url: '${baseUrl}/edu3/teaching/teachingplancoursestatus/setCourseTeachType.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{resourceids:resourceids,courseTeachType:courseTeachType},
		 		success: function(json){  
		 			if(json.returnCode= 200){
		 				alertMsg.correct("设置课程教学类型成功");
		 				$.pdialog.closeCurrent();
		 				navTabPageBreak();
		 			} else {
		 				alertMsg.error(json.message);
		 			}
		 			$("#setCourseTeachTypeForm_submit").removeAttr("disabled");
					$("#setCourseTeachTypeForm_close").removeAttr("disabled");
				 }
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="autoOpenCourseTerms">
			<input id="setCourseTeachTypeForm_resourceids" name="resourceids"
				value="${resourceids }" type="hidden" />
			<div align="left" style="margin-left: 25px; margin-bottom: 10px;">
				<span style="font-size: 13px; font-weight: bolder;">课程教学类型：</span>
				<gh:select dictionaryCode="CodeCourseTeachType"
					name="courseTeachType" id="setCourseTeachTypeForm_courseTeachType"
					style="width:100px;" />
			</div>
		</div>

		<div style="margin-top: 100px; margin-right: 5px;" align="right">
			<button id="setCourseTeachTypeForm_submit" type="button"
				onclick="return subCourseTeachType();" style="cursor: pointer;">提交</button>
			<button id="setCourseTeachTypeForm_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>