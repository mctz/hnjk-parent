<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择自动开课学期</title>
<script type="text/javascript">
		// 自动开课
		function openCourse(){
			var terms = new Array();
			$("#autoOpenCourseTerms input[@name='term']:checked").each(function(){
				terms.push($(this).val());
			});
			
			if(terms.length<0){
				alertMsg.warn("请选择开课学期！");
				return false;
			}
			$("#auto_openCourse").attr("disabled","disabled");
			$("#auto_close").attr("disabled","disabled");
			$.ajax({
		 		url: '${baseUrl}/edu3/teaching/teachingplancoursestatus/autoOpenCourses.html',
		 		type: 'POST',
		 		dataType: 'json', 	
		 		data:{terms:terms.toString()},
		 		success: function(json){  
		 			if(json.returnCode="200"){
		 				alertMsg.correct(json.message);
		 			} else {
		 				alertMsg.error(json.message);
		 			}
		 			$("#auto_openCourse").removeAttr("disabled");
					$("#auto_close").removeAttr("disabled");
				 }
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="autoOpenCourseTerms">
			<div align="left" style="margin-left: 25px; margin-bottom: 10px;">
				<h3>开课学期：</h3>
			</div>
			<c:forEach items="${openCourseTermMap}" var="t">
				<div
					style="margin-top: 15px; vertical-align: middle; font-size: 14px;">
					<input type="checkbox" name="term" value="${t.key }" />${t.value }
				</div>
			</c:forEach>
		</div>

		<div style="margin-top: 65px; margin-right: 5px;" align="right">
			<button id="auto_openCourse" type="button"
				onclick="return openCourse();" style="cursor: pointer;">开课</button>
			<button id="auto_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>