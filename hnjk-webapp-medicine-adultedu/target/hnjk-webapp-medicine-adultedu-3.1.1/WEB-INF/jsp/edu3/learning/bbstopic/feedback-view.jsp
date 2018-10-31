<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>${bbsTopic.title }</title>
<style>
.pageFormContent p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
}
</style>
</head>
<body>
	<h2 class="contentTitle" style="border-bottom: medium;">
		<div>
			反馈问题：
			<c:if test="${not empty bbsTopic.facebookType }">[${ghfn:dictCode2Val('CodeFacebookType',bbsTopic.facebookType)}]</c:if>${bbsTopic.title }<span
				style="color: #006699; float: right;">反馈时间时间：<fmt:formatDate
					value="${bbsTopic.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" />
				&nbsp;&nbsp; 反馈学生：${bbsTopic.fillinMan }
			</span>
		</div>
	</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layouth="72">
				<table class="form">
					<thead>
						<tr>
							<td width="20%"><div style="color: #006699;">${bbsTopic.fillinMan }
									(反馈学生)<br />
									<fmt:formatDate value="${bbsTopic.fillinDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
									<br />
								</div></td>
							<td>
								<div style="line-height: 26px;">${bbsTopic.content }</div>
							</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${bbsTopic.bbsReplys }" var="bbsReply"
							varStatus="vs">
							<tr>
								<td style="line-height: 26px;"><div style="color: #006699;">${bbsReply.replyMan }<br />
										<fmt:formatDate value="${bbsReply.replyDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
										<br />
									</div></td>
								<td>
									<div align="justify">${bbsReply.replyContent }</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
