<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}- 学生服务</title>
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/portal-default-2.css" />
<gh:loadCom components="jquery,blockUI" />
<script type="text/javascript">	
	var serviceFlag = "${serviceFlag}";
	var hasPublishedPlan = "${hasPublishedPlan}";
	//校验用户是否提交验证码
	$(document).ready(function() { 
		if(hasPublishedPlan=="false"){
			 $.blockUI({message: $('#noPublishedPlan')});
			 return false;
		}
		if(serviceFlag == null || serviceFlag == "false"){	  
		     $.blockUI({ message: $('#loginForm') }); 		   
		 }else{
		 	$('#frame_content').attr("src","${baseUrl }${forwardurl }?uid=${uid}");
		 	$.unblockUI();
		 }
    }); 	
	</script>
</head>
<body>
	<table width="1003" border="0" align="center" cellpadding="0"
		cellspacing="0" class="kuang">
		<tr>
			<td height="137" colspan="2" align="right" valign="bottom"
				background="${baseUrl}/style/default/portal-images-2/Rtop.jpg"><table
					width="255" border="0" cellpadding="0" cellspacing="0"
					class="btmore2">
					<tr>
						<td width="328" align="right"><span style="color: #fff">
								<%-- 今天是 ${todayStr }--%>
						</span></td>
						<td width="10">&nbsp;</td>
					</tr>
				</table></td>
		</tr>

		<%-- <tr>
    <td height="29" colspan="2">
	<!-- 头部 -->
	 <gh:portal channelList="${channelList }" channelType="index-header"/>
  </td>
  </tr>
  --%>
		<tr>
			<td>
				<!--正文  -->
				<div id="loginForm"
					style="width: 380px; padding: 4px; border: 2px #0b5fa5 solid; background: #66a1d2; display: none">
					<div id="errorInfo" style="color: red"></div>
					<p>
						<label><font color="red">请输入验证码：</font></label><input type="text"
							name="vildateCode" id="vildateCode" size="4" autocomplete="off" />
						<img src="${baseUrl}/imageCaptcha" id="checkCodeImg"
							style="margin-left: -6px; margin-bottom: -10px; padding-left: 8px"
							onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
							title="看不清，点击换一张"> <a href="#"
							onclick="javascript:document.getElementById('checkCodeImg').src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()">换一张</a>
						<br /> <input type="button" name="submit" value=" 提 交 "
							onclick="submitValidataCode()" class="input_but" /> <input
							type="button" name="back" value="回首页" onclick="gohome()"
							class="input_but" />
					</p>
				</div>
				<div id="noPublishedPlan"
					style="width: 340px; padding: 4px; border: 2px #0b5fa5 solid; background: #66a1d2; display: none">
					<font color="red">网上报名未开放...</font><input type="button" name="back"
						value="回首页" onclick="gohome()" class="input_but" />
				</div>
				<div id="con_infor"
					style="width: 1000px; height: auto; border-right: 1px #eee solid; border-left: 1px #eee solid;">
					<iframe id="frame_content" height="880px"
						src="${baseUrl }/common/wait.htm" marginwidth="0" marginheight="0"
						frameborder="0" scrolling="yes"
						style="z-index: 1; visibility: inherit; width: 100%;"></iframe>
				</div>
			</td>
		</tr>
		<!-- 底部 -->
		<tr>
			<td height="22" colspan="2" align="center" bgcolor="#1C627E"><span
				style="color: #fff">粤ICP备号 Copyright 2001-2010
					${school}${schoolConnectName}All Rights Reserved <gh:version />
			</span></td>
		</tr>
		<tr>
			<td height="21" colspan="2" align="center">&nbsp;</td>
		</tr>
	</table>
	<script type="text/javascript"> 
    function gohome(){
		window.location.href = "${baseUrl}/portal/index.html";
    }

	//** iframe自动适应页面 **//
	function SetCwinHeight() {
		var iframeid = document.getElementById("frame_content"); //iframe id
		iframeid.height = "10px";//先给一个够小的初值,然后再长高.
		if (document.getElementById) {
			if (iframeid && !window.opera) {		
				   if (iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight) //如果用户的浏览器是NetScape
					 iframeid.height = iframeid.contentDocument.body.offsetHeight;
					else if(iframeid.contentWindow && iframeid.contentWindow.document.documentElement.scrollHeight)
					 iframeid.height = iframeid.contentWindow.document.documentElement.scrollHeight;	
					else if(iframeid.Document && iframeid.Document.body.scrollHeight)//IE6
					 iframeid.height = iframeid.Document.body.scrollHeight;	
			}
	     }
	} 

	//提交验证
	function submitValidataCode(){
		var vildateCode = $("#vildateCode").val();
		if(vildateCode == "" || vildateCode.length == 0){
			$("#errorInfo").html("请输入验证码！")
			return false;
		}
		$("#frame_content").contents().find("waitText").html("正在加载系统服务，请等待...");
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/j_hnjk_authorize_check.html",
	          data:{validateCode:vildateCode},
	          dataType:  'json',
	          beforeSend:function(){$("#errorInfo").html("正在验证...");},             
	          success:function(date){          	   		
	         		 if(date.isSucess === true){         		 	
	         		 	$('#frame_content').attr("src","${baseUrl }${forwardurl }?uid=${uid}");         		 
	         		    $.unblockUI();         		  
	         		 }else{
	         		 	$("#errorInfo").html(date.error)
	         		 }         
	          }            
		});
	}

</script>
</body>

</html>
