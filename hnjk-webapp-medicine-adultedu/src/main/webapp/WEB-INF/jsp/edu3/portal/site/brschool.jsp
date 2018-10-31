<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}- 教学站</title>
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/portal-default-2.css" />
<gh:loadCom components="easyslide" />
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
			<!-- 教学站 -->
			<table border="0" cellpadding="0" cellspacing="0" class="STYLE2"
				style="width: 97%">
				<c:forEach items="${schoolType}" var="stype" varStatus="vs">
					<c:if test="${stype.isUsed eq 'Y' }">
						<tr>
							<td colspan="4" align="left">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" align="left">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="STYLE7">
									<tr>
										<td><img
											src="${baseUrl}/style/default/portal-images-2/title.png"
											width="11" height="11" /> ${stype.dictName }</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="left">&nbsp;</td>
						</tr>

						<tr>
							<td width="100%" align="left"><c:if
									test="${not empty orgList }">
									<c:forEach items="${ orgList}" var="brSchool">
										<c:if
											test="${fn:indexOf(brSchool.schoolType,stype.dictValue)>-1 and empty brSchool.childs}">
											<div style="margin-left: 4px; width: 190px; float: left">
												<a
													href="${baseUrl }/portal/site/brschool/school.html?id=${brSchool.resourceid }">·${ brSchool.unitName}</a>
											</div>
										</c:if>
									</c:forEach>
								</c:if></td>
						</tr>
					</c:if>
				</c:forEach>

			</table>

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

<c:if test="${param.error == true }">
	<script type="text/javascript">   
  		alert('${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}');
  	</script>
</c:if>

</body>
</html>
