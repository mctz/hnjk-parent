<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业导师公告</title>
<style type="text/css">
.list_content {
	font-size: 10pt;
	text-align: left;
	line-height: 170%;
}

.list_content p {
	width: 100%;
	height: 100%
}
</style>
</head>
<body>
	<h2 class="contentTitle">查看毕业导师公告</h2>
	<div class="page">
		<div class="pageContent">
			<form>
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">毕业论文批次:</td>
							<td colspan="3">${graduatePapersNotice.examSub.batchName }</td>
						</tr>
						<tr>
							<td width="20%">导师:</td>
							<td width="30%">${graduatePapersNotice.guidTeacherName}</td>
							<td width="20%">发布时间:</td>
							<td width="30%"><fmt:formatDate
									value="${graduatePapersNotice.pubTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
						<tr>
							<td>标题:</td>
							<td colspan="3">${graduatePapersNotice.title }</td>
						</tr>
						<tr id="nostyle">
							<td>内容:</td>
							<td colspan="3"><div class="list_content">${graduatePapersNotice.content }</div>
							</td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3">
								<div id="graduateFile">
									<c:forEach items="${filelist}" var="attach" varStatus="vs">
										<li id="${attach.resourceid }"><img
											style="cursor: pointer; height: 10px;"
											src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
											<a onclick="downloadAttachFile('${attach.resourceid }')"
											href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
									</c:forEach>
								</div>
								<div id="hideGraduateFile" style="display: none"></div>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	//附件下载
	   function downloadAttachFile(attid){
	   		$('#frameForDownload').remove();
	   		var elemIF = document.createElement("iframe");
	   		elemIF.id = "frameForDownload"; //创建id
			elemIF.src = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }

	</script>
</html>