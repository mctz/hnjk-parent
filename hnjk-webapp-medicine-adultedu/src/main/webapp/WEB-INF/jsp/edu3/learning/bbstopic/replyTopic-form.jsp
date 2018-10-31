<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂问答批量评分</title>
<style type="text/css">
.nostyle td {
	height: 4px
}

.questino p {
	float: left;
	display: block;
	width: 100%;
	height: auto;
	margin: 0;
	padding: 5px 0;
	position: static;
	line-height: 1.2em;
}
</style>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'topic_replyContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=topic_replyContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',
		      afterCreate : function(id) {
				KE.util.focus(id);
			}          
	  		});	
    	});    	
		
	</script>
</head>
<body>
	<h2 class="contentTitle">帖子批量评分</h2>
	<div class="page">
		<div class="pageContent">
			<form id="batchReplyTopicForm" method="post"
				action="${baseUrl}/edu3/framework/bbstopic/score/batchSave.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<c:forEach items="${bbsTopicList }" var="bbsTopic">
						<table class="form">
							<tr>
								<td style="font-weight: bolder;" colspan="8">提出的问题 <input
									type="hidden" name="resourceid" value="${bbsTopic.resourceid }" /></td>
							</tr>
							<tr>
								<td width="8%">课程:</td>
								<td width="25%">${bbsTopic.course.courseName }</td>
								<td width="8%">所属知识节点:</td>
								<td width="20%">${bbsTopic.syllabus.syllabusName }</td>
								<td width="5%">总帖子数:</td>
								<td width="8%">${bbsTopic.totalTopicNum }</td>
								<td width="5%" style="color: red; font-weight: bolder;">有效贴数:</td>
								<td width="16%" style="color: red; font-weight: bolder;">${bbsTopic.validTopicNum }</td>
							</tr>
							<tr>
								<td width="8%">标题:</td>
								<td style="font-weight: bolder;" colspan="3">${bbsTopic.title }</td>
								<td width="5%">提问人:</td>
								<td width="8%" style="color: #006699;">${bbsTopic.fillinMan }</td>
								<td width="5%">提出时间:</td>
								<td width="16%" style="color: #006699;"><fmt:formatDate
										value="${bbsTopic.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
							<tr>
								<td width="8%">内容:</td>
								<td colspan="3"><div class="questino"
										style="line-height: 26px;">${bbsTopic.content }</div></td>
								<td width="5%">评级:</td>
								<td style="color: red;" align="center">${ghfn:dictCode2Val("CodeTopicScoreType",bbsTopic.scoreType) }</td>
								<td width="5%">是否回复:</td>
								<td width="16%" style="color: red;" align="center">${bbsTopic.isReply=='Y'?'已回复':'未回复'}</td>
							</tr>
							<tr>
								<td style="color: blue; font-weight: bolder;" colspan="8">回复列表</td>
							</tr>
							<tbody>
								<c:forEach items="${bbsTopic.replyList }" var="bbsReply"
									varStatus="vs">
									<tr>
										<td width="8%">回复人:</td>
										<td width="8%"
											style="line-height: 26px; ${bbsReply.isMine=='Y'?' color:red;':'' }">${bbsReply.replyMan }
											(<fmt:formatDate value="${bbsReply.replyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />)
										</td>
										<td colspan="5">
											<div class="questino" align="justify">${bbsReply.replyContent }</div>
											<c:if test="${not empty bbsReply.attachs}">
												<div
													style="margin-left: 5px 0; border: 1px solid #E6E6E2; margin-bottom: 6px; padding: 0.5em;">
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
										<td width="8%"><c:if
												test="${bbsReply.bbsUserInfo.sysUser.userType eq 'student' }">
												<gh:checkBox dictionaryCode="CodeTopicScoreType"
													name="score${bbsReply.resourceid }"
													value="${not empty bbsReply.scoreType ? bbsReply.scoreType : '0' }"
													onclick="showMessage(this);" inputType="radio" />
											</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:forEach>
					<h2 class="contentTitle">老师回复内容:</h2>
					<table class="form">
						<tr>
							<td><gh:checkBox dictionaryCode="CodeTopicScoreType"
									name="scoreType" value="1" inputType="radio" /></td>
						</tr>
						<tr>
							<td class="nostyle"><textarea id="topic_replyContent"
									name="replyContent" rows="5" cols=""
									style="width: 95%; height: 300px; visibility: hidden;"
									class="required"></textarea></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">保存答疑与评分</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">	 
 	 window.setTimeout(function (){KE.create('topic_replyContent'); },1000);
 	 
 	 function showMessage(obj){
 		 var checkValue = obj.value;
 		 if(checkValue=="-1"){
 			alertMsg.confirm("一个灌水帖，学生问答讨论将被扣1分，您确定要评吗？", {
 	 			 okCall:function(){
 	 				
 	 			 },
 	 			cancelCall:function(){
 					var name = obj.name;
 					$("input[name='"+name+"']:eq(1)").attr("checked",'checked');
 				}
 			});
 		 }
 	 }
</script>
</body>
</html>