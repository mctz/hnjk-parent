<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂问答评分</title>
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
	<h2 class="contentTitle">帖子评分</h2>
	<div class="page">
		<div class="pageContent">
			<form id="bbsTopicScoreForm" method="post"
				action="${baseUrl}/edu3/framework/bbstopic/score/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsTopic.resourceid }" />

				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程:</td>
							<td>${bbsTopic.course.courseName }</td>
							<td width="20%">所属知识节点:</td>
							<td>${bbsTopic.syllabus.syllabusName }</td>
						</tr>
						<tr>
							<td width="20%">总帖子数:</td>
							<td>${totalTopicNum }</td>
							<td width="20%" style="color: red; font-weight: bolder;">有效贴数:</td>
							<td style="color: red; font-weight: bolder;">${validTopicNum }</td>
						</tr>
					</table>
					<h2 class="contentTitle">标题: ${bbsTopic.title }</h2>
					<table class="form">
						<thead>
							<tr>
								<td width="20%"><div style="color: #006699;">${bbsTopic.fillinMan }
										(提问人)<br />
										<fmt:formatDate value="${bbsTopic.fillinDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
										<br />
									</div></td>
								<td>
									<div class="questino" style="line-height: 26px;">${bbsTopic.content }</div>
								</td>
								<td style="color: red;" align="center">${ghfn:dictCode2Val("CodeTopicScoreType",bbsTopic.scoreType) }</td>
								<td width="25%"><gh:checkBox
										dictionaryCode="CodeTopicScoreType"
										name="score${bbsTopic.resourceid }" value="1"
										inputType="radio" /></td>
								<td style="color: red;" align="center">${isReply=='Y'?'已回复':'未回复'}</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${replyList }" var="bbsReply" varStatus="vs">
								<tr>
									<td style="line-height: 26px;"><div
											style="color: #006699;">${bbsReply.replyMan }<br />
											<fmt:formatDate value="${bbsReply.replyDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
											<br />
										</div></td>
									<td>
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
									<td width="20%" colspan="3"><c:if
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
					<h2 class="contentTitle">老师回复内容:</h2>
					<table class="form">
						<tr>
							<td class="nostyle"><input type="hidden" name="replyid"
								value="${teacherReply.resourceid }" /> <textarea
									id="topic_replyContent" name="replyContent" rows="5" cols=""
									style="width: 95%; height: 300px; visibility: hidden;"
									class="required">${teacherReply.replyContent }</textarea></td>
						</tr>
						<tr>
							<td><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${teacherReply.attachs}"
									fileExt="zip|rar" /> <font color="green">(单个文件上传大小不能大于10M)</font>
							</td>
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
						<c:choose>
							<c:when test="${not empty topicMap.preresourceid }">
								<li><div class="button">
										<div class="buttonContent">
											<button type="button"
												onclick="nextTopicScore('${topicMap.preresourceid }','${orgUnitID }');">上一个</button>
										</div>
									</div></li>
							</c:when>
							<c:otherwise>
								<li><div class="buttonContent">
										<button type="button" disabled="disabled"
											style="cursor: pointer;">上一个</button>
									</div></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${not empty topicMap.nextresourceid }">
								<li><div class="button">
										<div class="buttonContent">
											<button type="button"
												onclick="nextTopicScore('${topicMap.nextresourceid }','${orgUnitID }');">下一个</button>
										</div>
									</div></li>
							</c:when>
							<c:otherwise>
								<li><div class="buttonContent">
										<button type="button" disabled="disabled"
											style="cursor: pointer;">下一个</button>
									</div></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">	 
 	 window.setTimeout(function (){KE.create('topic_replyContent'); },1000);
 	 function nextTopicScore(nextid,orgUnitID){
 	/*	 var $form = $("#bbsTopicScoreForm");
 		$.ajax({
 			type:'POST',
 			url:$form.attr("action"),
 			data:$form.serializeArray(),
 			dataType:"json",
 			cache: false,
 			success: function (json){ 		*/		
 				var url = "${baseUrl}/edu3/framework/bbstopic/score/list.html?resourceid="+nextid+"&orgUnitID="+orgUnitID;
 				navTab.reload(url,{}, 'RES_METARES_BBS_TOPIC_SCORE');	
 		/*	},
 			
 			error: DWZ.ajaxError
 		});*/
 		
 	 }
 	 
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