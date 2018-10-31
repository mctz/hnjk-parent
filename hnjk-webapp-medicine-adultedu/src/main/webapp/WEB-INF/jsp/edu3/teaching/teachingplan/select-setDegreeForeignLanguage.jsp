<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择学位外语课程的信息</title>
<script type="text/javascript">		
		function setDegreeForeignLanguage(){
			var resourceid = $("#setDegreeForeignLanguageid").val();
			var courseid = $("#setsetDegreeForeignLanguageid").val();			
			var postUrl = "${baseUrl}/edu3/teaching/teachingplan/setDegreeForeignLanguage.html"; 			
			if(!courseid){
				alertMsg.warn("请选择课程！");
				return false;
			}	
			$.ajax({
		          type:"POST",
		          url:postUrl,
		          data:{resourceid:resourceid,courseid:courseid},
		          dataType:  'json',
		          cache: false,
		          success:function(data){ 
	         		 if(data['statusCode'] == 200){ 
	         		 	 alertMsg.correct(data['message']);
	         		 	 $.pdialog.closeCurrent(); 
	         		 }else{
	         			 alertMsg.error(data['message']);
	         		 }         
		          }            
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="select-setDegreeForeignLanguageid">
			<input type="hidden" id="setDegreeForeignLanguageid"
				value="${resourceid }" />
			<div align="left" style="margin-left: 30px; margin-bottom: 10px;">
				学位外语课程：${selectOption}</div>
		</div>
		<div style="margin-top: 70px; margin-right: 5px;" align="right">
			<button id="select-setDegreeForeignLanguage_open" type="button"
				onclick="return setDegreeForeignLanguage();"
				style="cursor: pointer;">确定</button>
			<button id="select-setDegreeForeignLanguage_close" type="button"
				class="close" onclick="$.pdialog.closeCurrent();"
				style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>