<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}- 首页</title>
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/portal-default-2.css" />
<gh:loadCom components="jquery,easyslide" />
<script type="text/javascript">
	//2011.12.14朝上提出将所有访问到/edu3/portal/index.html页面的，跳转到登录页面
	window.location.href = "${baseUrl}/edu3/framework/index.html";
	function login(){		
		if(document.getElementById('j_username').value==""){
			alert("请输入登录名！");
			document.getElementById('j_username').focus();
			return false;
		}
		if(document.getElementById('j_password').value==""){
			alert("请输入密码！");
			document.getElementById('j_password').focus();
			return false;
		}	
					
		document.getElementById('loginform').submit();
	}
</script>
</head>
<body>

	<c:if test="${param.error == true }">
		<script type="text/javascript">   
  		alert('${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}');
  	</script>
	</c:if>
</body>
<table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0" class="kuang">
	<tr>
		<td height="137" colspan="2" align="right" valign="bottom"
			background="${baseUrl}/style/default/portal-images-2/Rtop.jpg"><table
				width="255" border="0" cellpadding="0" cellspacing="0"
				class="btmore2">
				<tr>
					<td width="328" align="right"><span style="color: #fff">今天是
							${todayStr }</span></td>
					<td width="10">&nbsp;</td>
				</tr>
			</table></td>
	</tr>

	<tr>
		<td height="29" colspan="2">
			<!-- 头部 --> <gh:portal channelList="${channelList }"
				channelType="index-header" />
		</td>
	</tr>
	<tr>
		<td width="185" height="602" valign="top">
			<!-- 左侧 --> <gh:portal channelList="${leftSideList }"
				linkList="${linkTextList }" channelType="index-leftside" />
		</td>
		<td width="818" valign="top">
			<!-- 中部 --> <gh:portal articleMap="${ articleMap}"
				linkList="${linkBannerist }" channelList="${navCenterBottomList }"
				channelType="index-center" /> <!-- 友情点击 --> <gh:portal
				channelList="${rightSideList }" linkList="${linkImgList }"
				channelType="index-link-img" />
		</td>
	</tr>
</table>
</td>
</tr>
<!-- 底部 -->
<tr>
	<td height="22" colspan="2" align="center" bgcolor="#1C627E"><span
		style="color: #fff">粤ICP备号 Copyright 2001-2010
			${school}${schoolConnectName} All Rights Reserved <gh:version />
	</span></td>
</tr>
<tr>
	<td height="21" colspan="2" align="center">&nbsp;</td>
</tr>
</table>

<c:if test="${param.error == true }">
	<script type="text/javascript">   
  		alert('${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}');
  	</script>
</c:if>

<!-- Live800默认跟踪代码: 开始-->
<script language="javascript"
	src="http://chat8.live800.com/live800/chatClient/monitor.js?jid=6206208730&companyID=62954&configID=50956&codeType=custom"></script>
<!-- Live800默认跟踪代码: 结束-->

</body>
</html>
