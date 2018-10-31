<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title><c:choose>
		<c:when test="${curseFileType eq '1' }">知识园地</c:when>
		<c:otherwise>特色资源</c:otherwise>
	</c:choose></title>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<div id="left_menu">
				<ul>
					<c:choose>
						<c:when test="${curseFileType eq '1' }">
							<li><a
								href="${baseUrl }/resource/course/knowledge.html?courseId=${course.resourceid}">知识园地</a></li>
						</c:when>
						<c:otherwise>
							<li><a
								href="${baseUrl }/resource/course/coursefiles.html?courseId=${course.resourceid}">特色资源</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">
				当前位置：首页 >
				<c:choose>
					<c:when test="${curseFileType eq '1' }">知识园地</c:when>
					<c:otherwise>特色资源</c:otherwise>
				</c:choose>
			</div>
			<div class="clear"></div>
			<div id="content">
				<table class="list" width="100%">
					<thead>
						<th width="5%"></th>
						<th width="40%">文件名</th>
						<th width="45%">描述</th>
						<th width="10%"><c:choose>
								<c:when test="${curseFileType eq '1' }">查看/下载</c:when>
								<c:otherwise>下载</c:otherwise>
							</c:choose></th>
					</thead>
					<tbody>
						<c:forEach items="${teacherFilesList.result }" var="file"
							varStatus="vs">
							<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
								<td align="center">${vs.index+1 }</td>
								<td>${file.fileName }</td>
								<td>${file.description }</td>
								<td align="center"><c:choose>
										<%-- 外部附件 --%>
										<c:when test="${empty file.attach.resourceid }">
											<a href="${file.fileUrl }" target="_blank"
												title="${file.fileName }"><c:choose>
													<c:when test="${curseFileType eq '1' }">查看/下载</c:when>
													<c:otherwise>下载</c:otherwise>
												</c:choose></a>
										</c:when>
										<%-- 本地附件 --%>
										<c:otherwise>
											<a href="javascript:void(0)"
												onclick="downloadAttachFile('${file.attach.resourceid }')"
												title="${file.fileName }.${file.attach.attType }"><c:choose>
													<c:when test="${curseFileType eq '1' }">查看/下载</c:when>
													<c:otherwise>下载</c:otherwise>
												</c:choose></a>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page condition="${condition }" pageType="resource"
					page="${teacherFilesList }" />
			</div>
		</div>
		<!--end right-->
	</div>
	<script type="text/javascript">
	function downloadAttachFile(attid){
			var url = baseUrl+"/edu3/framework/filemanage/${empty user ? 'public/':''}download.html?id="+attid;
			var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	}
</script>
</body>
</html>