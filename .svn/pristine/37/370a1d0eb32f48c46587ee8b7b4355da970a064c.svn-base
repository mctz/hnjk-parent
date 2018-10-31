<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程学习材料</title>
<style type="text/css">
.img {
	width: 20px;
	height: 18px;
	line-height: 20px;
	margin-left: 8px;
	text-align: center;
	vertical-align: middle;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#matesList").height($(".tabsContent").height()-10);
	});	
	</script>
</head>
<body>
	<div id="matesList"
		style="float: left; width: 25%; overflow: auto; padding-bottom: 6px; border-right: solid 1px #CCC; line-height: 21px;">
		<table class="list" width="98%">
			<tr>
				<th width="10%">*</th>
				<th>课件名称</th>
			</tr>
			<c:if test="${empty mateResources }">
				<tr>
					<td colspan="2"><span style="color: green;">该章节没有课件材料</span></td>
				</tr>
			</c:if>
			<c:forEach items="${mateResources }" var="m" varStatus="vs">
				<tr>
					<td>${vs.index+1 }</td>
					<td>${m.mateName}
						(${ghfn:dictCode2Val('CodeMateType',m.mateType)}) <span> <c:choose>
								<%-- 文档,综合 --%>
								<c:when test="${m.mateType eq '5'}">
									<a href="javascript:;"
										style="color: green; text-decoration: none;"
										onclick="showMate('${m.mateName }','${m.resourceid}','${m.mateUrl }','${m.mateType}','0')">
										<img src="${baseUrl }/themes/default/images/icon_attch.gif"
										title="下载" class="img" />
									</a>
								</c:when>
								<%-- 视频(三分屏) --%>
								<c:when test="${(m.mateType eq '7')}">
									<a href="${m.mateUrl }" target="_blank"
										style="color: green; text-decoration: none;"> <img
										src="${baseUrl }/themes/default/images/icon_window2.png"
										title="打开" class="img" /></a>
								</c:when>
								<%-- 视频(网页) --%>
								<c:when test="${(m.mateType eq '8')}">
									<a href="javascript:;"
										style="color: green; text-decoration: none;"
										onclick="showMate('${m.mateName }','${m.resourceid}','${m.mateUrl }','${m.mateType}','0')">
										<img src="${baseUrl }/themes/default/images/icon_window1.png"
										title="本版面打开" class="img" />
									</a>&nbsp;
								<a href="${m.mateUrl }" target="_blank"
										style="color: green; text-decoration: none;"> <img
										src="${baseUrl }/themes/default/images/icon_window2.png"
										title="新窗口打开" class="img" /></a>
								</c:when>
								<%-- 视频,网页 --%>
								<c:otherwise>
									<a href="javascript:;"
										style="color: green; text-decoration: none;"
										onclick="showMate('${m.mateName }','${m.resourceid}','${m.mateUrl }','${m.mateType}','0')">
										<c:choose>
											<c:when test="${m.mateType eq '1'}">
												<img
													src="${baseUrl }/themes/default/images/icon_window11.png"
													title="本版面打开" class="img" />
											</c:when>
											<c:when test="${m.mateType eq '2'}">
												<img
													src="${baseUrl }/themes/default/images/icon_window12.png"
													title="本版面打开" class="img" />
											</c:when>
											<c:otherwise>
												<img
													src="${baseUrl }/themes/default/images/icon_window1.png"
													title="本版面打开" class="img" />
											</c:otherwise>
										</c:choose>
									</a>&nbsp;
								<a href="javascript:;"
										style="color: green; text-decoration: none;"
										onclick="showMate('${m.mateName }','${m.resourceid}','${m.mateUrl }','${m.mateType}','1')">
										<img src="${baseUrl }/themes/default/images/icon_window2.png"
										title="新窗口打开" class="img" />
									</a>
									<c:if test="${(m.mateType eq '1')}">
										<a href="javascript:;"
											style="color: green; text-decoration: none;"
											onclick="showMate('${m.mateName }','${m.resourceid}','${fn:replace(m.mateUrl,".pdf",".ppt")}','${m.mateType}','-1')">
											<img src="${baseUrl }/themes/default/images/icon_attch.gif"
											title="下载" class="img" />
										</a>
									</c:if>
								</c:otherwise>
							</c:choose>
					</span>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div id="matesContent" style="float: left; width: 74%">
		<iframe id='webContentFrame' width='100%' height='100%'
			frameborder='0' src='${baseUrl }/common/blank.jsp'></iframe>
	</div>

	<script type="text/javascript">
		//材料
		function showMate(mateName,mateId,mateUrl,mateType,openType){
			if($("#header").height() == 118){				
				fullScreen();
			} 		
						
			if(openType=='0'){
				//$("#flashPlayer").html("");				
				//var obj1 = "<embed width='382' height='330' hidden='no' autostart='true' src='"+mateUrl+"'>";	
				//var obj2 = "<iframe id='webContentFrame' width='100%' height='100%' frameborder='0' src='"+mateUrl+"'></iframe>	";	
				if(mateType=='5'){//综合，下载
					var elemIF = document.createElement("iframe");  
					elemIF.src = mateUrl;  
					elemIF.style.display = "none";  
					document.body.appendChild(elemIF); 
				} else if(mateType=='1' || mateType=='6' || mateType=='8') {//网页,视频(网页)	
					$("#webContentFrame").attr("src","");
					$("#webContentFrame").attr("src",mateUrl);	
				} else {//视频||音频
					$("#webContentFrame").attr("src","");
					$("#webContentFrame").attr("src","${baseUrl}/edu3/learning/interactive/materesource/view.html?courseId=${courseId}&mateId="+mateId+"&mateName="+encodeURIComponent(mateName)+"&mateType="+mateType+"&mateUrl="+mateUrl);
				} 
			$("#webContentFrame").height($(".tabsContent").height());//重新调整iframe高度
	
			} else if(openType=='2'){				 
				//window.open(mateUrl,mateId);
			}else if(openType=='-1'){				 
				var elemIF = document.createElement("iframe");  
				elemIF.src = mateUrl;  
				elemIF.style.display = "none";  
				document.body.appendChild(elemIF); 
			}else { //新窗口打开				
				var url = "${baseUrl }/edu3/learning/interactive/materesource/view.html";
				$.pdialog.open(url+"?courseId=${courseId}&mateId="+mateId+"&mateUrl="+mateUrl+"&mateType="+mateType+"&mateName="+encodeURIComponent(mateName),mateId,mateName,{height:600,width:800});
			}
		}		
		
	</script>
</body>
</html>
