<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title><c:choose>
		<c:when test="${type eq '2' }">案例分析</c:when>
		<c:when test="${type eq '3' }">刑法全文及司法解释</c:when>
	</c:choose></title>
<gh:loadCom components="iframeAutoHeight" />
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<div id="left_menu">
				<ul>
					<c:forEach items="${folderList }" var="f">
						<c:choose>
							<c:when test="${type eq '2' }">
								<li><a
									href="${baseUrl }/resource/course/caseanalysis.html?courseId=${course.resourceid}&fid=${f.resourceid}">${f.fileName}</a></li>
							</c:when>
							<c:when test="${type eq '3' }">
								<li><a
									href="${baseUrl }/resource/course/lawplain.html?courseId=${course.resourceid}&fid=${f.resourceid}">${f.fileName}</a></li>
							</c:when>
						</c:choose>

					</c:forEach>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">
				当前位置：课程学习 > ${dirName }
				<c:if test="${not empty dirParentName }">> ${dirParentName }</c:if>
			</div>
			<div class="clear"></div>
			<div id="content">
				<c:choose>
					<c:when test="${type eq '2' }">
						<div class="stlx_tab">
							<c:forEach items="${teacherFilesList }" var="file" varStatus="vs">
								<div
									<c:choose>
							<c:when test="${dirFiles.resourceid eq file.resourceid }">class="stlx_tab_over"</c:when>
							<c:otherwise>class="stlx_tab_out"</c:otherwise>
						</c:choose>>
									<a
										href="${baseUrl }/resource/course/caseanalysis.html?courseId=${course.resourceid}&caseid=${file.resourceid}">${file.fileName }</a>
								</div>
							</c:forEach>
						</div>
						<div class="clear"></div>
						<c:if test="${not empty dirFiles.resourceid }">
							<h2>${dirFiles.fileName }</h2>
							<iframe id="_caseAnalysisIframe" name="_caseAnalysisIframe"
								src="${baseUrl }/resource/course/transfer.html?url=${dirFiles.fileUrl }"
								scrolling="no" frameborder="0" width="100%" height="100%"></iframe>
							<script type="text/javascript">
				$("#_caseAnalysisIframe").iframeAutoHeight();
				</script>
						</c:if>
					</c:when>
					<c:when test="${type eq '3' }">
						<c:choose>
							<%-- 司法解释列表 --%>
							<c:when test="${isPlainFiles eq 'Y' and isPlainIndexPage eq 'Y'}">
								<table class="list" width="100%">
									<thead>
										<th width="5%"></th>
										<th width="85%">标题</th>
										<th width="10%">查看</th>
									</thead>
									<tbody>
										<c:forEach items="${teacherFilesList }" var="file"
											varStatus="vs">
											<tr <c:if test="${vs.index mod 2 ne 0 }">class="odd"</c:if>>
												<td align="center">${vs.index+1 }</td>
												<td>${file.fileName }</td>
												<td align="center">
													<form action="" method="post">
														<input type="hidden" value="${vs.index+1 }" name="pageNum">
														<a href="javascript:void(0);" onclick="goToPage(this);"
															title="${file.fileName }">查看</a>
													</form>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<script type="text/javascript">
							function goToPage(obj){
								$(obj).parent().submit();
							}
						</script>
							</c:when>
							<c:otherwise>
								<c:forEach items="${lawplainPage.result }" var="law">
									<iframe id="_resourceIframe" name="_resourceIframe"
										src="${baseUrl }/resource/course/transfer.html"
										rel="${baseUrl }/resource/course/transfer.html?url=${law.fileUrl }"
										scrolling="no" frameborder="0" width="100%"></iframe>
								</c:forEach>
								<gh:page condition="${condition }" pageType="resource"
									page="${lawplainPage }" />
							</c:otherwise>
						</c:choose>
					</c:when>
				</c:choose>

			</div>

		</div>
		<!--end right-->
	</div>
	<%-- 司法解释 分页加上返回目录 --%>
	<c:if test="${isPlainFiles eq 'Y'}">
		<script type="text/javascript">
	$(function (){
		if($("#Page").length>0){
			$("#Page").prepend("<a title=\"返回列表\" style=\"width:104px;\" href=\""+location.href+"\">返回列表</a>");
		}
	});
</script>
	</c:if>
	<script type="text/javascript">
	if($('#_resourceIframe').length>0){
		setTimeout(function (){
			$('#_resourceIframe').attr('src',$('#_resourceIframe').attr('rel')).iframeAutoHeight({minHeight:100});	 
		},1000);
	}	
</script>
</body>
</html>