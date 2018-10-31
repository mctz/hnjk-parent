<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school }${schoolConnectName}- <decorator:title
		default="首页" />
</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<gh:loadCom components="netedu3-defaultcss,jquery,sdmenu,common" />
<decorator:head />
</head>
<!-- top header -->
<body>

	<div id="top_container">
		<div class="frame_class top_bg">
			<table class="topnav" cellspacing="0" cellpadding="0" width="100%"
				height="100%">
				<tr>
					<td height="138" valign="bottom">
						<!-- 头部导航 -->
						<div id="topmenu">
							<div class="left">&nbsp;</div>
							<div class="con">
								<ul>
									<li><a href="${baseUrl }/edu3/frame/index.html">首页</a></li>
									<li><a href="#">申请与审批</a></li>
									<li><a href="#">查看公告</a></li>
									<li><a href="#">常用下载</a></li>
									<li><a href="#">我的博客</a></li>
									<li><a href="#">学院公共论坛</a></li>
									<li><a href="#">网上图书馆</a></li>
									<li><a href="${baseUrl }/portal/index.html"
										target="_blank">网院门户</a></li>
								</ul>
							</div>
							<div class="right">&nbsp;</div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
				</tr>
			</table>
		</div>
	</div>
	<div id="wrapper">
		<div id="menu_container">
			<!--left menu-->
			<div id="leftmenu">
				<gh:currentUserSystemMenu />
			</div>
		</div>
		<!-- body content -->
		<div id="main_body">
			<decorator:body />
		</div>
	</div>
	<div id="footer" style="border-top: 1px solid #ccc;">
		<div id="footer-content" style="text-align: center; padding: 4px">
			技术支持：广东学苑教育发展有限公司
			<p>
				<gh:version />
		</div>
	</div>

	<script type="text/javascript">
<!--
//var Mysubmenu=null;
$(function(){
	$("#my_menu").SDMenu().init();
	//Mysubmenu=$("#my_menu").submenu({oneSmOnly:true,speed:500,expandNum:1,savestatus:true});	
})


function logout(){
	if(window.confirm("您真的要退出系统吗？")){	
		window.location.href="${basePath}/j_spring_security_logout";
	}
}
-->
</script>
</body>
</html>
