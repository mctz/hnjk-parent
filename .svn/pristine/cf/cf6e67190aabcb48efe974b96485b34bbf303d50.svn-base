<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程随堂问答FAQ问题</title>
<style>
#nostyleMore td {
	height: 8px
}
</style>
<script type="text/javascript">		
    	//课堂问答回复情况
		function showBbsReply(bbsTopicId,title){
			$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbsreply/list.html?bbsTopicId="+bbsTopicId, "dialogBbsReply", title, {mask:true,height:550,width:800});
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="50">
				<thead>
					<tr>
						<th width="10%">所属知识节点</th>
						<th width="25%">问题</th>
						<th width="20%">提问时间</th>
						<th width="10%">回复数</th>
						<th width="15%">最后回复人</th>
						<th width="20%">最后回复日期</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${bbsTopics.result }" var="bbsTopic"
						varStatus="vs">
						<tr>
							<td>${bbsTopic.syllabus.syllabusName }</td>
							<td><a href="javascript:;"
								onclick="showBbsReply('${bbsTopic.resourceid }','${bbsTopic.title }')">${bbsTopic.title }</a></td>
							<td><fmt:formatDate value="${bbsTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${bbsTopic.replyCount }</td>
							<td>${bbsTopic.lastReplyMan}</td>
							<td><fmt:formatDate value="${bbsTopic.lastReplyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsTopics}"
				goPageUrl="${baseUrl}/edu3/learning/interactive/faq/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>
