<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师复习总结录像</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div layoutH="5">
				<div
					style="float: left; width: 25%; overflow: auto; padding-bottom: 6px; border-right: solid 1px #CCC; line-height: 21px; height: 100%;">
					<table class="list" width="98%">
						<tr>
							<th width="10%">*</th>
							<th>录像名称</th>
						</tr>
						<c:forEach items="${reviseList.result }" var="m" varStatus="vs">
							<tr>
								<td>${vs.index+1 }</td>
								<td>${m.mateName}
									(${ghfn:dictCode2Val('CodeMateType',m.mateType)}) <span>
										<a href="javascript:;"
										onclick="showMateRevise('${m.mateName }','${m.mateUrl }');">
											<img src="${baseUrl }/themes/default/images/icon_window1.png"
											title="查看" />
									</a>&nbsp; </a> <a href="${m.mateUrl }" target="_blank"> <img
											src="${baseUrl }/themes/default/images/icon_attch.gif"
											title="下载" />
									</a>
								</span>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div style="float: left; width: 74%; height: 100%;">
					<iframe id="mateReviseContentFrame" width='100%' height='100%'
						frameborder='0' src='${baseUrl }/common/blank.jsp'></iframe>
				</div>
				<script type="text/javascript">
		function showMateRevise(mateName,mateUrl){
			$("#mateReviseContentFrame").attr("src","");
			$("#mateReviseContentFrame").attr("src","${baseUrl}/edu3/learning/interactive/materesource/view.html?mateName="+encodeURIComponent(mateName)+"&mateType=2&mateUrl="+mateUrl);
		}
		</script>
			</div>
		</div>
	</div>
</body>
</html>
