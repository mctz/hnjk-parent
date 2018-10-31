<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生反馈管理</title>
<script type="text/javascript">
	function addFeedback(){
		navTab.openTab('RES_STUDENT_FEEDBACK_ADD', "${baseUrl}/edu3/student/feedback/input.html", '新增反馈');
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<c:if test="${empty condition['from'] }">
				<gh:resAuth parentCode="RES_STUDENT_FEEDBACK" pageType="list"></gh:resAuth>
			</c:if>
			<table class="table" layouth="76">
				<thead>
					<tr>
						<th width="30%">反馈问题</th>
						<th width="10%">反馈类型</th>
						<th width="15%">反馈学生</th>
						<th width="15%">反馈时间</th>
						<th width="15%">回复人</th>
						<th width="15%">回复日期</th>
					</tr>
				</thead>
				<tbody id="feedbackBody">
					<c:forEach items="${feedbackPage.result }" var="bbsTopic"
						varStatus="vs">
						<tr>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/student/feedback/input.html?resourceid=${bbsTopic.resourceid}','viewFeedback','查看反馈',{width:700,height:400});">${bbsTopic.title }</a>
							</td>
							<td>${ghfn:dictCode2Val('CodeFacebookType',bbsTopic.facebookType)}</td>
							<td>${bbsTopic.fillinMan}</td>
							<td><fmt:formatDate value="${bbsTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><c:if test="${bbsTopic.replyCount gt 0 }">${bbsTopic.lastReplyMan }</c:if></td>
							<td><c:if test="${bbsTopic.replyCount gt 0 }">
									<fmt:formatDate value="${bbsTopic.lastReplyDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${feedbackPage}"
				goPageUrl="${baseUrl}/edu3/student/feedback/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>