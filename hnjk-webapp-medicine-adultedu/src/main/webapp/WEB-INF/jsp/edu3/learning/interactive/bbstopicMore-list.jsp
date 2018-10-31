<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程随堂问答列表</title>
<style>
#nostyleMore td {
	height: 8px
}
</style>
<script type="text/javascript">	
		var teachType = "${teachType}";
    	//课堂问答回复情况
		function showBbsReply(bbsTopicId,title){
			if(teachType!='faceTeach'){
				$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbsreply/list.html?bbsTopicId="+bbsTopicId, "dialogBbsReply", title, {mask:true,height:550,width:800});
			}else{
				alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
			}
			
		}		
		//提问
		function askMoreQuestion(){
			if(teachType!='faceTeach'){
				$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbstopic/ask.html?questiontype=more&courseId=${course.resourceid }&syllabusId=${syllabusId}", "dialogQuestion", "我的问题", {mask:true,height:550,width:700});
			}else{
				alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
			}
			
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<div class="subBar">
					<span style="float: left; color: red; font-weight: bold;"> <span
						style="margin-right: 5px;">总帖子数：${totalTopicNum }</span> <span
						style="color: black; font-weight: bolder;">|</span> <span
						style="margin-left: 5px;">有效帖数：${validTopicNum }</span>
					</span>
					<ul>
						<li><div>
								<a class="button" href="javascript:;"
									onclick="askMoreQuestion();"><span>我要提问</span></a>
							</div></li>
					</ul>
				</div>
			</div>
		</div>

		<div class="pageContent">
			<table class="table" layouth="90">
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
					<c:if test="${empty bbsTopics.result }">
						<tr>
							<td colspan="6">暂时没有检索到提问......</td>
						</tr>
					</c:if>
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
				goPageUrl="${baseUrl}/edu3/learning/interactive/bbstopic/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>
