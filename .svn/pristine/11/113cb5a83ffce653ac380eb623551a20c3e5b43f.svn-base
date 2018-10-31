<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业附件下载</title>
<script type="text/javascript">
		//附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	</script>
</head>
<body>
	<table width="100%">
		<tr>
			<td>
				<div style="width: 100%; border-left: #ccc solid 1px;">
					<table class="table" layouth="32">
						<thead>
							<tr>
								<th width="50%">附件名称</th>
								<th width="50%">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty attachs }">
								<td colspan="2"><font color="green">附件不存在</font></td>
							</c:if>
							<c:forEach items="${attachs}" var="a" varStatus="vs">
								<tr>
									<td>${a.attName }</td>
									<td><a href="#"
										onclick="downloadAttachFile('${a.resourceid }')"
										style="color: blue">下载</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
	</table>
</body>