<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出错了</title>
<gh:loadCom components="netedu3-defaultcss,jquery,framework" />
</head>
<body>

	<center>
		<input type="hidden" id="currentUser" value="${currentUser }" />
		<div class="alert" id="alertMsgBox"
			style="top: 270.5px; z-index: 9999;">
			<div class="alertContent">
				<div class="error">
					<div class="alertInner">
						<h1>系统出错了！</h1>
						<div class="msg" id="errorMsg">
							错误句柄：${errHandle }<br /> 错误类型：${ex }<br /> 错误栈：${error }
						</div>
					</div>
					<div class="toolBar">
						<ul>
							<li><a href="javascript:" onclick="history.go(-1);" rel=""
								class="button"><span>返回</span></a></li>
							<li><a href="javascript:" onclick="sendError()" rel=""
								class="button"><span>发送错误报告</span></a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="alertFooter">
				<div class="alertFooter_r">
					<div class="alertFooter_c"></div>
				</div>
			</div>
		</div>

	</center>

	<!-- 
<div class="sk_Box">
     <div class="sk_error">
     <div class="sk_errorfont" id="errorMsg">
	     错误句柄：${errHandle }<br/>
	     错误类型：${ex }<br/>
	     错误栈：${error }
     <input type="hidden" id="currentUser" value="${currentUser }"/>
     </div>
       <div class="sk_ButtonBox4" style="width:200px">
          <div class="sk_Button2"><a href="javascript:history.go(-1);">返 回</a></div>
          <div class="sk_Button9" style="width:120px"><a href="javascript:sendError();">发送错误报告</a></div>
          </div>
     </div>
  
</div>
 -->
	<script type="text/javascript">
//发送错误
function sendError(){
	$.ajax({
 		url: '${baseUrl}/system/sendError.html',
 		type: 'POST',
 		dataType: 'html', 	
 		data:"errMsg="+$('#errorMsg').text()+"&currentUser="+$('#currentUser').val(),//参数设置 	
 		success: function(html){
  			//加载成功处理
  			alert('发送成功!\n感谢您的报告，我们将及时处理.');  			
		 }
		});
}


</script>
</body>
</html>
