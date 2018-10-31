<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>2012国家级精品资源共享课</title>
<gh:loadCom components="jquery" />
<script type="text/javascript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>
<style>
html, body {
	background: #0a4aac
		url(${basePath }/style/default/resources/images/login_bg2.jpg)
		no-repeat top center;
	padding: 0;
	margin: 0 auto;
}

img {
	border: none;
}

#box1 {
	padding: 223px 0 0 0;
	margin: 0 auto;
	width: 1000px;
	height: auto;
}

.box2 {
	background:
		url(${basePath }/style/default/resources/images/login2_06.jpg) top
		no-repeat;
	width: 221px;
	height: 339px;
	float: left;
	margin: 0 50px 0 30px;
}

.box3 {
	background:
		url(${basePath }/style/default/resources/images/login2_08.jpg) top
		no-repeat;
	width: 221px;
	height: 339px;
	float: left;
	margin: 0 70px;
}

.box4 {
	background:
		url(${basePath }/style/default/resources/images/login2_10.jpg) top
		no-repeat;
	width: 221px;
	height: 339px;
	float: left;
	margin: 0 0 0 50px;
}

.kc_pic {
	padding: 14px 0 0 15px;
	margin: 0;
	height: 190px;
}

.kc_pic a:hover {
	border-bottom: 3px solid #e8c000;
	padding: 0;
	margin: 0;
}

.login {
	margin: 0 0 0 40px;
}

.login_input {
	width: 105px;
	border: 1px solid #3f42b8;
	background: #575bdb;
	color: #f2f1f3;
	margin: 4px 0;
}

.left {
	float: left;
	width: 122px;
}
/*------------------弹出------------------------*/
#fade {
	display: none;
	background: #000;
	position: fixed;
	left: 0;
	top: 0;
	z-index: 10;
	width: 100%;
	height: 100%;
	opacity: .80;
	z-index: 9999;
}

.popup_block {
	display: none;
	background: #fff;
	padding: 20px;
	border: 20px solid #ddd;
	float: left;
	font-size: 1.2em;
	position: fixed;
	top: 50%;
	left: 50%;
	z-index: 99999;
	-webkit-box-shadow: 0px 0px 20px #000;
	-moz-box-shadow: 0px 0px 20px #000;
	box-shadow: 0px 0px 20px #000;
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	border-radius: 10px;
}

img.btn_close {
	float: right;
	margin: -55px -55px 0 0;
}

.popup p {
	padding: 5px 10px;
	margin: 5px 0;
}

.red {
	font-size: 20px;
	color: #b1061e;
	font-family: "微软雅黑";
}
</style>

<body
	onload="MM_preloadImages('${basePath }/style/default/resources/images/xfx_out.jpg')">
	<div id="box1">
		<div class="box2">
			<div class="kc_pic">
				<a href="http://jpkc.scutde.net/2012/xfa/" target="_blank"
					onmouseover="MM_swapImage('Image1','','${basePath }/style/default/resources/images/xfx_out.jpg',1)"
					onmouseout="MM_swapImgRestore()"><img
					src="${basePath }/style/default/resources/images/xfx_on.jpg"
					alt="刑法学" name="Image1" width="191" height="182" border="0"
					id="Image1" /></a>
			</div>
			<div class="login">
				<form id="loginForm1" action="${baseUrl}/j_spring_security_check"
					method="post">
					<input type="hidden" name="spring-security-redirect"
						value="/edu3/framework/index.html" /> <input type="hidden"
						name="authenticationFailureUrl"
						value="/edu3/login.html?error=true" />
					<div class="left">
						<input class="login_input" name="j_username" type="text" /><input
							class="login_input" name="j_password" type="password" />
					</div>
					<div>
						<a href="#?w=500" rel="loginForm1" class="poplight"><img
							src="${basePath }/style/default/resources/images/bn_dl.gif"
							width="42" height="50" vspace="3" /></a>
					</div>
				</form>
			</div>
		</div>
		<!--end box2-->

		<div class="box3">
			<div class="kc_pic">
				<a href="http://jpkc.scutde.net/2012/c" target="_blank"
					onmouseover="MM_swapImage('Image2','','${basePath }/style/default/resources/images/c_out.jpg',1)"
					onmouseout="MM_swapImgRestore()"><img
					src="${basePath }/style/default/resources/images/c_on.jpg"
					alt="高级语言程序设计C++" name="Image2" width="191" height="182" border="0"
					id="Image2" /></a>
			</div>
			<div class="login">
				<form id="loginForm2" action="${baseUrl}/j_spring_security_check"
					method="post">
					<input type="hidden" name="spring-security-redirect"
						value="/edu3/framework/index.html" /> <input type="hidden"
						name="authenticationFailureUrl"
						value="/edu3/login.html?error=true" />
					<div class="left">
						<input class="login_input" name="j_username" type="text" /><input
							class="login_input" name="j_password" type="password" />
					</div>
					<div>
						<a href="#?w=500" rel="loginForm2" class="poplight"><img
							src="${basePath }/style/default/resources/images/bn_dl.gif"
							width="42" height="50" vspace="3" /></a>
					</div>
				</form>
			</div>
		</div>
		<!--end box3-->

		<div class="box4">
			<div class="kc_pic">
				<a href="http://jpkc.scutde.net/2012/dxyy/" target="_blank"
					onmouseover="MM_swapImage('Image3','','${basePath }/style/default/resources/images/eng_out.jpg',1)"
					onmouseout="MM_swapImgRestore()"><img
					src="${basePath }/style/default/resources/images/eng_on.jpg"
					alt="大学英语" name="Image3" width="191" height="182" border="0"
					id="Image3" /></a>
			</div>
			<div class="login">
				<form id="loginForm3" action="${baseUrl}/j_spring_security_check"
					method="post">
					<input type="hidden" name="spring-security-redirect"
						value="/edu3/framework/index.html" /> <input type="hidden"
						name="authenticationFailureUrl"
						value="/edu3/login.html?error=true" />
					<div class="left">
						<input class="login_input" name="j_username" type="text" /><input
							class="login_input" name="j_password" type="password" />
					</div>
					<div>
						<a href="#?w=500" rel="loginForm3" class="poplight"><img
							src="${basePath }/style/default/resources/images/bn_dl.gif"
							width="42" height="50" vspace="3" /></a>
					</div>
				</form>
			</div>
		</div>
		<!--end box4-->
	</div>
	<div
		style="clear: both; font-size: 12px; text-align: center; color: #CCC">Copyright
		© 2012 ${school}${schoolConnectName}All rights reserved</div>


	<!--POPUP START-->
	<div id="popup1" class="popup_block">
		<p class="red">
			评审于2013年1月5日开始，请届时登录，谢谢。<br /> 如需观看开放课程，请直接点击课程名。
		</p>
	</div>


	<script type="text/javascript">
/*
$(document).ready(function(){
						   		   
	//When you click on a link with class of poplight and the href starts with a # 
	$('a.poplight[href^=#]').click(function() {
		var popID = $(this).attr('rel'); //Get Popup Name
		var popURL = $(this).attr('href'); //Get Popup href to define size
				
		//Pull Query & Variables from href URL
		var query= popURL.split('?');
		var dim= query[1].split('&');
		var popWidth = dim[0].split('=')[1]; //Gets the first query string value

		//Fade in the Popup and add close button
		$('#' + popID).fadeIn().css({ 'width': Number( popWidth ) }).prepend('<a href="#" class="close"><img src="${basePath }/style/default/resources/images/close_pop.png" class="btn_close" title="Close Window" alt="Close" /></a>');
		
		//Define margin for center alignment (vertical + horizontal) - we add 80 to the height/width to accomodate for the padding + border width defined in the css
		var popMargTop = ($('#' + popID).height() + 80) / 2;
		var popMargLeft = ($('#' + popID).width() + 80) / 2;
		
		//Apply Margin to Popup
		$('#' + popID).css({ 
			'margin-top' : -popMargTop,
			'margin-left' : -popMargLeft
		});
		
		//Fade in Background
		$('body').append('<div id="fade"></div>'); //Add the fade layer to bottom of the body tag.
		$('#fade').css({'filter' : 'alpha(opacity=80)'}).fadeIn(); //Fade in the fade layer 
		
		return false;
	});
	
	
	//Close Popups and Fade Layer
	$('a.close, #fade').live('click', function() { //When clicking on the close or fade layer...
	  	$('#fade , .popup_block').fadeOut(function() {
			$('#fade, a.close').remove();  
	}); //fade them both out
		
		return false;
	});

	
});*/

$(document).ready(function(){
	$("a.poplight[rel^='loginForm']").click(function() {
		var formId = $(this).attr('rel');
		var $form = $("#"+formId);
		if($form.find("input[name='j_username']").val()==""){
			alert("请输入登录名！");
			$form.find("input[name='j_username']").focus();
			return false;
		}
		if($form.find("input[name='j_password']").val()==""){
			alert("请输入密码！");
			$form.find("input[name='j_password']").focus();
			return false;
		}						
		$form.submit();		
		return false;
	});	
});
</script>
	<!--Google Analytics begin-->
	<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
	<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-9747705-2");
pageTracker._trackPageview();
} catch(err) {}
</script>
</body>
</html>

