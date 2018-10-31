<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程素材资源预览</title>
<style type="text/css">
html, body {
	height: 97%;
	margin: 0px;
}

#mateVideo {
	background-color: #000000;
	height: 80%;
	border: 1px solid #00457b;
	padding: 8px;
	font-size: .85em;
}
</style>
<script type="text/javascript">
	var videoUrl = '${mateUrl }';
	var mateType = ${mateType };
	if(mateType!=2){
		$("#mateVideo").hide();
	}
</script>
</head>
<body>
	<div class="page">
		<h2 class="contentTitle">材料类型为${ghfn:dictCode2Val('CodeMateType',mateType)}</h2>
		<div class="pageContent" style="margin-left: 10px;" layouth="60">
			<div id="mateVideo"></div>
			<c:choose>
				<%-- 如果是视频就播放--%>
				<c:when test="${mateType eq '1'}">
					<%-- <embed width='382' height='330' hidden='no' autostart='true' src='http://202.192.18.50/pdfjs/viewer.html?pdfurl=${mateUrl }'>--%>
					<iframe src="${resourceUrl }/pdfjs2/viewer.html?pdfurl=${mateUrl }"
						width="99%" height="99%"></iframe>
				</c:when>
				<c:when test="${(mateType eq '2')}">
				</c:when>
				<c:when
					test="${(mateType eq '3') or (mateType eq '4') or (mateType eq '6') or (mateType eq '7') or (mateType eq '8')}">
					<iframe src="${mateUrl }" width="99%" height="99%"></iframe>
				</c:when>
				<%-- 如果是其他就下载 --%>
				<c:otherwise>
					<a href="${mateUrl }" class="button"><span>下载</span></a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script type="text/javascript"
		src="${baseUrl }/common/player/player.js" charset="utf-8"></script>
	<script type="text/javascript">
	var flashvars={f:"${baseUrl }/common/player/m3u8.swf",a:"${mateUrl }",c:0,p:0,s:4,lv:0};if(mateType===2){CKobject.embed("${baseUrl }/common/player/player.swf","mateVideo","ckplayer_a1","100%","100%",false,flashvars)};
	if(window.addEventListener){window.addEventListener("mousedown",function(e){if(e.button==1){callFlash()}if(e.button==2){if(e.stopPropagation){e.stopPropagation()}if(e.preventDefault){e.preventDefault()}if(e.preventCapture){e.preventCapture()}if(e.preventBubble){e.preventBubble()}}},true)}else{document.getElementById("flashcontent").onmousedown=function(){if(event.button==4){}if(event.button==2){document.oncontextmenu=function(){return false};this.setCapture();this.onmouseup=function(){this.releaseCapture()}}}};
	if(mateType===1){
		$("#mateVideo").hide();
	}
</script>
</body>
</html>