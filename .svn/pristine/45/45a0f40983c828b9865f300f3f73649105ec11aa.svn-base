<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title><c:choose>
		<c:when test="${askType eq 'ask' }">随堂问答</c:when>
		<c:when test="${askType eq 'faq' }">常见问题</c:when>
		<c:otherwise>反馈意见</c:otherwise>
	</c:choose></title>
<style type="text/css">
.list td div {
	text-align: left;
	width: auto;
}

.list td p {
	width: auto;
}
</style>
<script type="text/javascript">
		function goBackCourseAsk(){
			var askType = "${askType}";
			var url = "";
			if(askType == 'ask'){
				url = baseUrl+"/resource/course/ask.html?type="+askType+"&syllabusid=${bbsTopic.syllabus.resourceid }";			
			} else 	{
				url = baseUrl+"/resource/course/ask.html?type="+askType+"&courseId=${bbsTopic.course.resourceid }";
			}
			var title = askType=="ask"?"随堂问答":(askType=="faq"?"常见问题":"反馈意见");
			Dialogs.load(url,{id:'resource_course_dialog',draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:800,height:600}).title(title);
		}
	</script>
</head>
<body>
	<div style="padding: 5px;">
		<h2 class="title1">
			<c:choose>
				<c:when test="${askType eq 'ask'}">随堂问答</c:when>
				<c:when test="${askType eq 'faq'}">常见问题</c:when>
				<c:otherwise>反馈意见</c:otherwise>
			</c:choose>
			> ${bbsTopic.title } (${bbsTopic.fillinMan }:
			<fmt:formatDate value="${bbsTopic.fillinDate }"
				pattern="yyyy-MM-dd HH:mm:ss" />
			) <a href="javascript:void(0)" onclick="goBackCourseAsk();"
				style="float: right; color: #63A7E6;"><span>返回</span></a>
		</h2>
		<table class="list" width="100%" style="table-layout: fixed;">
			<tbody>
				<tr>
					<td width="20%"><div style="color: #006699;">${bbsTopic.fillinMan }
							<c:choose>
								<c:when test="${askType eq 'ask' or askType eq 'faq'}">(提问人)</c:when>
								<c:otherwise>(反馈人)</c:otherwise>
							</c:choose>
							<br />
							<fmt:formatDate value="${bbsTopic.fillinDate }"
								pattern="yyyy-MM-dd HH:mm:ss" />
							<br />
						</div></td>
					<td>
						<div>${bbsTopic.content }</div>
					</td>
				</tr>
				<c:forEach items="${replyList }" var="bbsReply" varStatus="vs">
					<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
						<td><div style="color: #006699;">${bbsReply.replyMan }<br />
								<fmt:formatDate value="${bbsReply.replyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" />
								<br />
							</div></td>
						<td>
							<div align="justify">${bbsReply.replyContent }</div> <c:if
								test="${not empty bbsReply.attachs}">
								<div
									style="margin-left: 5px; border: 1px solid #E6E6E2; margin-bottom: 6px; padding: 0.5em;">
									<div style="font-weight: bold;">附件：</div>
									<c:forEach items="${bbsReply.attachs}" var="attach"
										varStatus="vs">
										<li id="${attach.resourceid }"><img
											style="cursor: pointer; height: 10px;"
											src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />
											&nbsp;&nbsp; <a
											onclick="downloadAttachFile('${attach.resourceid }')"
											href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;</li>
									</c:forEach>
								</div>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>