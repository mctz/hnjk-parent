<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${mateName }</title>
<style type="text/css">
html, body {
	height: 97%;
	margin: 0px;
}

#mateVideo_interactive {
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
	var courseId = '${courseId}';
	var mateId = '${mateId}';
</script>
</head>
<body>
	<h2 class="contentTitle">${mateName }</h2>

	<%-- 如果是视频就播放 old
			<c:when test="${mateType eq '1') or (mateType eq '3') or (mateType eq '4')}">
				<embed width='382' height='330' hidden='no' autostart='true' src='${mateUrl }'>
			</c:when>
			<c:otherwise>
				<iframe src="${mateUrl }" width="100%" height="100%"></iframe>
			</c:otherwise>
			--%>

	<%-- 如果是视频就播放 new--%>
	<c:choose>
		<c:when test="${mateType eq '1'}">
			<%-- <embed width='382' height='330' hidden='no' autostart='true' src='http://202.192.18.50/pdfjs/viewer.html?pdfurl=${mateUrl }'>--%>
			<iframe src="${resourceUrl }/pdfjs2/viewer.html?pdfurl=${mateUrl }"
				width="99%" height="99%"></iframe>
			<%-- <iframe src="${mateUrl }" width="99%" height="99%"></iframe> --%>
		</c:when>
		<c:when test="${(mateType eq '2')}">
			<div id="mateVideo_interactive"></div>
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

	<%-- <script type="text/javascript" src="${baseUrl }/jscript/jquery-1.4.4.min.js" charset="utf-8"></script> --%>
	<script type="text/javascript"
		src="${baseUrl }/common/player/player.js" charset="utf-8"></script>
	<script type="text/javascript">
	var flashvars={f:"${baseUrl }/common/player/m3u8.swf",a:"${mateUrl }",c:0,p:0,s:4,lv:0,loaded:'loadedHandler',};if(mateType===2){CKobject.embed("${baseUrl }/common/player/player.swf","mateVideo_interactive","ckplayer_a1","100%","100%",false,flashvars)};
	if(window.addEventListener){window.addEventListener("mousedown",function(e){if(e.button==1){callFlash()}if(e.button==2){if(e.stopPropagation){e.stopPropagation()}if(e.preventDefault){e.preventDefault()}if(e.preventCapture){e.preventCapture()}if(e.preventBubble){e.preventBubble()}}},true)}else{document.getElementById("flashcontent").onmousedown=function(){if(event.button==4){}if(event.button==2){document.oncontextmenu=function(){return false};this.setCapture();this.onmouseup=function(){this.releaseCapture()}}}};


	 var videoWatchTime = 0;// 观看时间
	 var setT = null; 
	 var videoTotalTime = 0; // 视频总时间
	 var _isRecord = false;// 是否已记录 
	 
	 function loadedHandler(){
			if(CKobject.getObjectById('ckplayer_a1').getType()){//说明使用html5播放器
				CKobject.getObjectById('ckplayer_a1').addListener('totaltime',totalHandler);// 获取总时长
				//CKobject.getObjectById('ckplayer_a1').addListener('paused',pausedHandlerT);// 暂停
				CKobject.getObjectById('ckplayer_a1').addListener('play',playHandler);
			} else {
				CKobject.getObjectById('ckplayer_a1').addListener('totaltime','totalHandler');
				CKobject.getObjectById('ckplayer_a1').addListener('paused','pausedHandlerT');
				//CKobject.getObjectById('ckplayer_a1').addListener('play','playHandler');
				CKobject.getObjectById('ckplayer_a1').addListener('seeking','seekingHandler');
			}
			
			
	}
	 
	function totalHandler(s){
		if(s>-1){
			//console.log('总时长：' + s);
			videoTotalTime = s;
		}
	}
	function playHandler(){
		//console.log("playHandler");
		//,g:'${studyProgress.currentTime }'		
		CKobject.getObjectById('ckplayer_a1').removeListener('play','playHandler');
		CKobject.getObjectById('ckplayer_a1').videoSeek('${studyProgress.currentTime }');
		CKobject.getObjectById('ckplayer_a1').addListener('paused','pausedHandler');
		
	}
	function seekingHandler(){
		console.log("seekingHandler");
		CKobject.getObjectById('ckplayer_a1').removeListener('seeking','seekingHandler');
		CKobject.getObjectById('ckplayer_a1').addListener('play','playHandler');
		//CKobject.getObjectById('ckplayer_a1').videoPause();
		
	}
	function pausedHandlerT(){
		//console.log("pausedHandlerT");	
		//CKobject.getObjectById('ckplayer_a1').videoPlay();
		CKobject.getObjectById('ckplayer_a1').removeListener('paused','pausedHandlerT');
		
		CKobject.getObjectById('ckplayer_a1').videoSeek('${studyProgress.currentTime }');
		
	}
	function pausedHandler(b){
		//console.log('paused');
		if(setT){
	        window.clearInterval(setT);
		}
		if(!b) {// 播放
			_isRecord = false;
		    setT = window.setInterval(setFunction,1000);
		 }
		// 记录观看时间(播放完先执行暂停方法)
		if(b && setT){
			_isRecord = true;
			recordStudyTime();
		 }
	 }
	
	 function setFunction(){
		 videoWatchTime += 1;
	   // console.log('当前观看时间：' + videoWatchTime);
	 }
    
	// 关闭或刷新浏览器(IE火狐)
	 window.onunload=function(){
		var userAgent = navigator.userAgent;
		if(!_isRecord || userAgent.indexOf("Firefox") > -1){
		 	recordStudyTime();
		}
	}
	// 关闭google
	window.onbeforeunload =function(){
		//console.log("window.onbeforeunload+"+_isRecord);
		if(!_isRecord){
			//console.log("userAgent");
			_isRecord = true;
		 	recordStudyTime();
		}
	}
	// 记录学习进度
	function recordStudyTime() {
		time = CKobject.getObjectById('ckplayer_a1').getStatus().time;//当前观看时长
		
		var xmlHttpReg = null;
		if (window.ActiveXObject) {
		 	xmlHttpReg = new ActiveXObject("Microsoft.XMLHTTP");
		}else{
		 	xmlHttpReg = new XMLHttpRequest(); 
		}
		if (xmlHttpReg != null) {
		    var  _murl = '${baseUrl}/edu3/studySituation/recordStudyProgress.html'; 
		    var content = "courseId="+courseId+"&mateId="+mateId+"&videoWatchTime="+videoWatchTime+"&videoTotalTime="+videoTotalTime+"&currentTime="+time;
		    xmlHttpReg.open("post", _murl, true);
		    xmlHttpReg.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		    xmlHttpReg.send(content);
		    xmlHttpReg.onreadystatechange = getResult; 
		}
		
		function getResult() {
		 	if (xmlHttpReg.readyState == 4) {
			    if (xmlHttpReg.status == 200) {
			    //	console.log("recorde 学习进度 success");
			     }
			}
		}
	}
</script>
</body>
</html>