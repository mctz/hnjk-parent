<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}- ${orgUnit.unitName }</title>
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/portal-default-2.css" />
<gh:loadCom components="jquery,easyslide" />
<script type="text/javascript">				
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
			background="${baseUrl}/style/default/portal-images-2/RtopBr.jpg">
			<div
				style="padding-bottom: 70px; text-align: left; margin-left: 420px; font-size: 14pt">
				<b>${orgUnit.unitName }</b>
			</div>
			<table width="255" border="0" cellpadding="0" cellspacing="0"
				class="btmore2">
				<tr>
					<td width="328" align="right"><span style="color: #fff">今天是
							${todayStr }</span></td>
					<td width="10">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td height="29" colspan="2">
			<!-- 头部导航 --> <gh:portal channelList="${channelList }"
				channelType="brschool-index-header" />
		</td>
	</tr>
	<tr>
		<td width="258" height="372" valign="top">
			<!-- 左侧 --> <gh:portal articleMap="${ articleMap}"
				channelType="brschool-index-leftside"
				schoolId="${orgUnit.resourceid }" />
		</td>
		<td width="741" valign="top">
			<!-- 右侧 --> <gh:portal articleMap="${ articleMap}"
				channelList="${navCenterBottomList }"
				channelType="brschool-index-center"
				schoolId="${orgUnit.resourceid }" />
		</td>
	</tr>
	<tr>
		<td height="71" colspan="2" align="center" valign="top">
			<!-- 友情点击 --> <gh:portal channelList="${rightSideList }"
				linkList="${linkListImg }" channelType="index-link-img" />
		</td>
	</tr>
	<!-- 底部 -->
	<tr>
		<td height="6" colspan="2" align="center">&nbsp;</td>
	</tr>
	<tr>
		<td height="20" colspan="2" align="center" bgcolor="#1C627E"><span
			style="color: #fff">粤ICP备号 Copyright 2001-2010
				${school}${schoolConnectName}All Rights Reserved <gh:version />
		</span></td>
	</tr>
	<tr>
		<td height="21" colspan="2" align="center">&nbsp;</td>
	</tr>
</table>
</body>
</html>
