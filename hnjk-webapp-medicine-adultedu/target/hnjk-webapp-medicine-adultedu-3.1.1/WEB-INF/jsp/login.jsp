<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<script type="text/javascript" src="${baseUrl }/jscript/jquery-1.4.4.min.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${school}${schoolConnectName}平台</title>
	<link href="${baseUrl }/themes/css/login.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
.payonline a {
	font-size: 20px;
	font-weight: bold;
}

.payonline a:link {
	color: #0463b4;
	text-decoration: none;
}

.payonline a:active: {
	color: red;
}

.payonline a:visited {
	color: #0463b4;
	text-decoration: none;
}

.payonline a:hover {
	color: red;
	text-decoration: underline;
}
</style>
<script type="text/javascript">	 	
	$(document).ready(function(){
		var $wind = $(window);
		setAppDownloadWidth();
		
  		$wind.resize(function(){
  			setAppDownloadWidth();
  		  });
  	});
  	
	function setAppDownloadWidth(){
		var $main = $("#main");
		var $appDownload =$("#appDownload");
		var mainWidth = $main.width();
	    var mainOffsetLeft = $main.offset();
	  //  $("#appDownload").width(mainWidth+mainOffsetLeft.left+160);
	    $appDownload.offset({ top: $main.height()-60, left: mainOffsetLeft.left+18 });
	}
	
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
		//document.getElementById('bt_login').value="请稍等...";
		//document.getElementById('bt_login').disabled=true;
	}	
	
	//打开门户
	function openPortal(url){
		window.open(url);
	}
	
	function downloadNoSys(name){
	    var url = '${baseUrl }/edu3/framework/filemanage/public/simpleDownload.html?name='+name+'&subPath=downloads';
	    window.open(url,"download");
	}
	
</script>
<body>
	<c:choose>
		<c:when test="${param.relogin ==true}">
			<script type="text/javascript">	 
  			window.location='${baseUrl}/edu3/login.html';
  		</script>
		</c:when>
		<c:otherwise>
			<div id="main">
				<div class="leftside">
					<p class="title1">系统依赖软件下载</p>
					<div class="list01">
						<ul>
							<!--  <li><a>打印功能所需的JRE运行环境，</a><a href="${baseServerUrl }${rootUrl}system/software/jre-6u26-windows-i586.zip">点击下载</a></li>-->
							<li><a>打印功能所需的JRE运行环境：</a><br>&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="${baseServerUrl }${rootUrl}system/software/jre-7u7-windows-i586.zip"><label style="color: red">Windows XP</label>系统点击这里下载</a> &nbsp;&nbsp;&nbsp;&nbsp;
								<a href="${baseServerUrl }${rootUrl}system/software/jre-8u91-windows-i586.zip"><label style="color: red">Windows 7以上</label>系统点击这里下载</a><br>&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="${baseUrl }/system/ActiveXConfigDownload.html?baseServerUrl=${baseServerUrl}"> ActiveX插件配置安装</a>
							</li>
							<li><a>火狐浏览器(Firefox3.6)，</a>
								<a href="${baseServerUrl }${rootUrl}system/software/FirefoxSetup3.6.20.zip">点击下载</a>
								<a style="margin-left: 30px;">极速PDF阅读器，</a>
								<a href="http://dl.jisupdf.com/jisupdf_setup_3.0.0.1019.exe">点击下载</a>
								<a href="https://www.jisupdf.com/" target="_blank">/官网下载</a>
							</li>
							<li><a>IE8(XP升级包)，</a><a
								href="${baseServerUrl }${rootUrl}system/software/IE8-WindowsXP-x86-CHS.zip">点击下载</a></li>
							<!--<li><a>学生图像采集插件（JMF），</a><a href="${baseServerUrl }${rootUrl}system/software/jmf-2_1_1e-windows-i586.zip">点击下载</a></li>
				<li><a>机考客户端(用于期末机考)，</a><a href="${baseServerUrl }${rootUrl}system/software/exam-client.zip">点击下载</a></li> -->
						</ul>
					</div>
					<p class="title1"><br>系统提示</p>
					<div class="list01">
						<ul>
							<li><a>请使用<span style="color: blue">1360 × 768</span>以上分辨率，最低不低于<span style="color: red">1152 × 864</span>，否则会影响使用
							</a></li>
							<%--<li><a>登录系统前建议清空浏览器缓存</a></li>--%>
							<li><a>请使用<span style="color: red">Firefox(火狐)、或IE8浏览器</span>操作，搜狗、世界之窗、MyIe等部分功能会有错误
							</a></li>
							<c:if test="${schoolCode eq '11846' }" >
							<li><a href="${baseUrl }/simpleDownloads.html" target="dialog">常用打印模板下载</a></li>
							</c:if>
							<c:if test="${usePayOnline=='Y' }">
								<li><a>学生<span style="color: red">网上缴费操作说明文档，</span></a>
									<a onclick="downloadNoSys('学生网页版缴费步骤.docx')" style="cursor: pointer;">点击下载</a></li>
								<%-- <li><a>学生<font color="red">网上缴费操作说明文档，</font></a><a href="${baseServerUrl }${rootUrl}system/software/学生网页版缴费步骤.docx">点击下载</a></li> --%>
							</c:if>
						</ul>
					</div>
				</div>

				<div class="rightside">
					<c:choose>
						<c:when test="${param.error == true }">
							<p class="title2" style="color: red">
								<c:if
									test="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message eq 'Bad credentials'}">错误的账号或密码</c:if>
								<c:if
									test="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message ne 'Bad credentials'}">
											${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}
											</c:if>
							</p>
						</c:when>
						<c:otherwise>
							<p class="title2">请登录</p>
						</c:otherwise>
					</c:choose>

					<form id="loginform" action="${baseUrl}/j_spring_security_check"
						method="post">
						<table cellpadding="5" cellspacing="0" width="100%">
							<tr class="font14">
								<td align="right" width="30%">用户名:&nbsp;&nbsp;</td>
								<td align="left" width="70%"><input id="j_username"
									name="j_username" type="text" class="input1" /></td>
							</tr>
							<tr class="font14">
								<td align="right" width="30%">密&nbsp;&nbsp;码:&nbsp;&nbsp;</td>
								<td align="left" width="70%"><input id="j_password"
									name="j_password" type="password" class="input1" autocomplete="off"/></td>
							</tr>
							<c:if test="${loginNum >3 }">
								<tr class="font14">
									<td align="right" width="30%">验证码:&nbsp;&nbsp;</td>
									<td align="left" width="70%"><input type='text' size='4'
										maxlength="4" name="j_checkcode" class="sk_login_textstyle"
										id="j_checkcode" onfocus="this.select();"
										onkeydown="if(event.keyCode==0xD){login(); return false;}" />
										<img src="${baseUrl}/imageCaptcha" id="checkCodeImg"
										style="margin-bottom: -6px; padding-left: 6px"
										onclick="this.src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()"
										title="看不清，点击换一张"> <a href="#"
										onclick="javascript:document.getElementById('checkCodeImg').src='${baseUrl}/imageCaptcha?now=' + new Date().getTime()">换一张</a>
									</td>
								</tr>
							</c:if>
							<%-- 
		<tr class="font14"><td align="right" width="30%">入&nbsp;&nbsp;口:&nbsp;&nbsp;</td><td align="left" width="70%"><label><input type="radio" name="fromNet" value="edu" />教育网&nbsp;&nbsp;</lable>&nbsp;&nbsp;&nbsp;&nbsp;<lable><input type="radio" name="fromNet" value="pub" checked="checked"/>公众网&nbsp;</lable></td></tr>
		 --%>
						</table>
						<input type="hidden" name="fromNet" value="pub" />
						<p align="center">
							<input name="Submit" id="bt_login" type="submit" class="btn1"
								value="登&nbsp;&nbsp;录" style="cursor: pointer;" />
						</p>
						<c:choose>
							<c:when test="${schoolCode eq '10571'}">
								<p style="line-height: 28px;margin-top: 15px;">
									<font color="red" style="font-weight: bolder;" size="4">温馨提示：</font><br>
									<span style="margin-left: 30px;font-weight: bolder;font-size: 15px">学生用户的登录用户名为：&nbsp;身份证号，初始密码为：&nbsp;11。登录系统后请及时修改密码，以免账户被盗用导致信息泄露。</span>
								</p>
							</c:when>
							<c:otherwise>
								<p align="right">
									<a href="#">系统依赖软件包下载 登录失败？</a><br />
		<%-- 							<a href="${baseUrl }/edu3/portal/user/findPassWord.html" id="_user_resetPwd"	rel="dlg_page1"					 --%>
		<!-- 						     target="dialog" mask="true" width="800px" height="400px">忘记密码怎么办？</a> -->
									<c:choose>
										<c:when test="${schoolCode eq '11078' or schoolCode eq '11846'}">
											<a href="${baseUrl }/edu3/portal/user/findPassWord.html" target="dialog">忘记密码怎么办？</a>
										</c:when>
										<c:otherwise>
											<a href="#" >忘记密码怎么办？</a>
										</c:otherwise>
									</c:choose>
								</p>
							</c:otherwise>
						</c:choose>
						
						<c:if test="${usePayOnline=='Y' }">
							<p class="payonline" align="center"
								style="margin-top: ${loginNum>3?30:50 }px;">
								<a href="${payOnlineUrl }" target="_blank">网上缴费入口</a>
							</p>
						</c:if>
					</form>
				</div>
				<div class="link">
					<a href="##" onclick="openPortal('${url}');">学院门户</a>
				</div>
				<div class="footer">
					Copyright&nbsp;&copy;&nbsp;2012-2013
					${school}${schoolConnectName}All Rights Reserved
					<gh:version />
				</div>
			</div>
			<c:if test="${hasAPP=='Y' }">
				<div id="appDownload">
					<!-- <div><img alt="扫描二维码下载APP" src="${baseUrl }/apk/gzdx_release_android.png"></div> -->
					<div align="left">
						<label style="color: blue;">扫描下方二维码下载使用<br>手机版“在线学习平台”app</label>
					</div>
					<div align="left">
						<img alt="扫描二维码下载APP" 
							src="${baseServerUrl }/QRCodeDownlodAPP/apk/${QRCodeName}">
					</div>
					<!-- <div><a href="${baseUrl }/${android_apk}"><button class="button bg-blue text-large">安卓版下载</button></a></div> -->
					<div align="left">
						<a href="${baseServerUrl }/QRCodeDownlodAPP/${android_apk}" ><button
								class="button bg-blue text-large">安卓版下载</button></a>
					</div>
				</div>
			</c:if>
		</c:otherwise>
	</c:choose>

</body>
</html>