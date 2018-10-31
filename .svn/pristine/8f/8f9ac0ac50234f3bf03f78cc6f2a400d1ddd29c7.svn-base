<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程随堂问答列表</title>
<style>
#nostyle td {
	height: 8px
}
</style>
<script type="text/javascript">		
		var teachType = "${teachType}";
    	//课堂问答回复情况
		function showBbsReply(bbsTopicId,title){
			$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbsreply/list.html?bbsTopicId="+bbsTopicId, "dialogBbsReply", title, {mask:true,height:550,width:800});
		}
		//回调函数
		function getResult(json){
		 	DWZ.ajaxDone(json);
		 	if (json.statusCode == 200){
		 		$("#interactive_tab5").click();
		 	}
		} 		
		//显示更多问题
		function showMoreBbsTopic(courseId,syllabusId){
			navTab.openTab("bbsTopicOnline",baseUrl+"/edu3/learning/interactive/bbstopic/list.html?type=more&courseId="+courseId+"&syllabusId="+syllabusId,"随堂问答");
		}
		//搜索
		function searchBbsTopic(courseId){
			if(teachType!='faceTeach'){
				navTab.openTab("bbsTopicOnline",baseUrl+"/edu3/learning/interactive/bbstopic/list.html?type=more&courseId="+courseId+"&keywords="+encodeURIComponent($("#topicKeywords").val()),"随堂问答");	
			}else{
				alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
			}
		}	
		//提问
		function askQuestion(){
			if(teachType!='faceTeach'){
				$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbstopic/ask.html?courseId=${course.resourceid }&syllabusId=${syllabusId}", "dialogQuestion", "我的问题", {height:550,width:700});
			}else{
				alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
			}
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div class="grid">
				<div class="">
					<table width="100%">
						<tbody>
							<tr style="margin-bottom: 10px;">
								<td colspan="2">
									<div style="font-weight: bold; color: red;">
										<span style="margin-right: 5px;">总帖子数：${totalTopicNum }</span>
										<span style="color: black; font-weight: bolder;">|</span> <span
											style="margin-left: 5px;">有效帖数：${validTopicNum }</span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="left" style="width: 40%;"><div>
										<a class="button" href="javascript:;" onclick="askQuestion();"><span>提出新的问题</span></a>
									</div></td>
								<td class="left" style="width: 60%;">
									<div style="float: right;">
										<ul>
											<li style="float: left;"><span>进入论坛搜索： <img
													style="margin-bottom: -5px;"
													src="${baseUrl }/images/framework/icon_search.jpg"><input
													type="text" id="topicKeywords" name="keywords" /></span></li>
											<li style="float: left;"><a class="button"
												href="javascript:;"
												onclick="searchBbsTopic('${course.resourceid }')"><span>搜索</span></a></li>
										</ul>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="gridHeader">
					<div class="gridThead">
						<table width="100%">
							<thead>
								<tr>
									<th class="left" style="cursor: default; width: 15%;">
										<div title="知识节点" class="gridCol">
											<div class="gridCol">知识节点</div>
										</div>
									</th>
									<th class="left" style="cursor: default; width: 25%;">
										<div title="问题" class="gridCol">
											<div class="gridCol">问题</div>
										</div>
									</th>
									<th class="left" style="cursor: default; width: 20%;">
										<div title="提问时间" class="gridCol">
											<div class="gridCol">提问时间</div>
										</div>
									</th>
									<th class="left" style="cursor: default; width: 5%;">
										<div title="回复数" class="gridCol">
											<div class="gridCol">回复数</div>
										</div>
									</th>
									<th class="left" style="cursor: default; width: 15%;">
										<div title="最后回复人" class="gridCol">
											<div class="gridCol">最后回复人</div>
										</div>
									</th>
									<th class="left" style="cursor: default; width: 20%;">
										<div title="最后回复日期" class="gridCol">
											<div class="gridCol">最后回复日期</div>
										</div>
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div layouth="142" class="gridScroller">
					<div class="gridTbody">
						<table width="100%">
							<tbody>
								<c:if test="${empty bbsTopics.result }">
									<tr>
										<td colspan="6"><div>您在该知识节点暂时没有提问......</div></td>
									</tr>
								</c:if>
								<c:forEach items="${bbsTopics.result }" var="bbsTopic"
									varStatus="vs" end="15">
									<tr>
										<td class="left" style="width: 15%;"
											title="${bbsTopic.syllabus.syllabusName }"><div>${bbsTopic.syllabus.syllabusName }</div></td>
										<td class="left" style="width: 25%;"
											title="${bbsTopic.title }"><div>
												<a href="javascript:;"
													onclick="showBbsReply('${bbsTopic.resourceid }','${bbsTopic.title }')">${bbsTopic.title }</a>
											</div></td>
										<td class="left" style="width: 20%;"
											title="<fmt:formatDate value='${bbsTopic.fillinDate }' pattern='yyyy-MM-dd HH:mm:ss'/>"><div>
												<fmt:formatDate value="${bbsTopic.fillinDate }"
													pattern="yyyy-MM-dd HH:mm:ss" />
											</div></td>
										<td class="left" style="width: 5%;"
											title="${bbsTopic.replyCount }"><div>${bbsTopic.replyCount }</div></td>
										<td class="left" style="width: 15%;"
											title="${bbsTopic.lastReplyMan}"><div>${bbsTopic.lastReplyMan}</div></td>
										<td class="left" style="width: 20%;"
											title="<fmt:formatDate value='${bbsTopic.lastReplyDate }' pattern='yyyy-MM-dd HH:mm:ss'/>"><div>
												<fmt:formatDate value="${bbsTopic.lastReplyDate }"
													pattern="yyyy-MM-dd HH:mm:ss" />
											</div></td>
									</tr>
								</c:forEach>
								<c:if test="${not empty bbsTopics.result }">
									<tr>
										<td colspan="6"><div style="float: right;">
												<a href="javascript:;"
													onclick="showMoreBbsTopic('${course.resourceid }','${syllabusId }')"><strong>更多提问
														&gt;&gt;</strong></a>
											</div></td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
