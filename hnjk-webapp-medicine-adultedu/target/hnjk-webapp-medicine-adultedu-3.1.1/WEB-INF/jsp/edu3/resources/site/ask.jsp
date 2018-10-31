<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title><c:choose>
		<c:when test="${askType eq 'ask' }">随堂问答</c:when>
		<c:when test="${askType eq 'faq' }">常见问题</c:when>
		<c:otherwise>反馈意见</c:otherwise>
	</c:choose></title>
<style type="text/css">
.list {
	line-height: 22px;
}

.form {
	padding-bottom: 10px;
}
</style>
<script type="text/javascript">
		function showBbsReply(topicid,type,title){
			var url = baseUrl+"/resource/course/ask/view.html?askType="+type+"&topicid="+topicid;	
			Dialogs.load(url,{id:'resource_course_dialog',draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:800,height:600}).title(title);
		}
	</script>
</head>
<body>
	<div style="padding: 5px;" id="ask_Area_Div">
		<h2 class="title1">
			<c:choose>
				<c:when test="${askType eq 'ask'}">我的提问 > ${syllabus.syllabusName }</c:when>
				<c:when test="${askType eq 'faq'}">常见问题 > ${course.courseName }</c:when>
				<c:otherwise>我的反馈 > ${course.courseName }</c:otherwise>
			</c:choose>
		</h2>
		<c:choose>
			<c:when test="${askType eq 'ask' or askType eq 'faq' }">
				<table width="100%" class="list">
					<thead>
						<tr>
							<th width="5%;"></th>
							<th width="25%;">问题</th>
							<th width="25%;">提问时间</th>
							<th width="12%;">回复数</th>
							<th width="16%;">最后回复人</th>
							<th width="17%;">最后回复日期</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${topicList }" var="bbsTopic" varStatus="vs">
							<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
								<td>${vs.index +1 }</td>
								<td align="left" title="${bbsTopic.title }"><a
									href="javascript:void(0);"
									onclick="showBbsReply('${bbsTopic.resourceid }','${askType }','${bbsTopic.title }')">${bbsTopic.title }</a></td>
								<td
									title="<fmt:formatDate value='${bbsTopic.fillinDate }' pattern='yyyy-MM-dd HH:mm:ss'/>"><fmt:formatDate
										value="${bbsTopic.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td title="${bbsTopic.replyCount }">${bbsTopic.replyCount }</td>
								<td
									title="${bbsTopic.replyCount gt 0 ? bbsTopic.lastReplyMan : ''}">${bbsTopic.replyCount gt 0 ? bbsTopic.lastReplyMan : ''}</td>
								<td
									title="<c:if test='${bbsTopic.replyCount gt 0}'><fmt:formatDate value='${bbsTopic.lastReplyDate }' pattern='yyyy-MM-dd HH:mm:ss'/></c:if>"><c:if
										test="${bbsTopic.replyCount gt 0}">
										<fmt:formatDate value="${bbsTopic.lastReplyDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:when test="${askType eq 'feedback' }">
				<table class="list" width="100%">
					<thead>
						<tr>
							<th width="5%"></th>
							<th width="30%">反馈问题</th>
							<th width="10%">反馈类型</th>
							<th width="10%">反馈学生</th>
							<th width="18%">反馈时间</th>
							<th width="10%">回复人</th>
							<th width="17%">回复日期</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${topicList }" var="bbsTopic" varStatus="vs">
							<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
								<td>${vs.index + 1 }</td>
								<td align="left"><a href="javascript:void(0)"
									onclick="showBbsReply('${bbsTopic.resourceid }','feedback','${bbsTopic.title }');"
									title="${bbsTopic.title }">${bbsTopic.title }</a></td>
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
			</c:when>
		</c:choose>
		<c:if test="${askType eq 'ask' or askType eq 'feedback' }">
			<h2 id="newAsk" class="title1"
				style="margin-top: 50px; text-align: left; font-weight: bold;">${askType eq 'ask' ? '我的新提问':'我的反馈意见' }</h2>
			<form id="ask_form" method="post" style="width: 100%;"
				action="${baseUrl}/resource/course/ask/save.html"
				onsubmit="return false;">
				<input type="hidden" name="askType" value="${askType }" />
				<c:choose>
					<c:when test="${askType eq 'ask' }">
						<input type="hidden" name="syllabusid"
							value="${syllabus.resourceid }" />
						<table width="100%" class="form">
							<tr>
								<td class="formtd" align="left" width="10%"
									style="font-weight: bold;">你的问题:</td>
								<td class="formtd" align="left" width="90%"><input
									type="text" name="title" style="width: 90%" value=""
									maxlength="250" /></td>
							</tr>
							<tr>
								<td class="formtd" align="left" style="font-weight: bold;">内容:</td>
								<td class="formtd" align="left"><textarea
										id="ask_topicContent" name="content" rows="5" cols=""
										style="width: 90%; height: 320px; visibility: hidden;"></textarea></td>
							</tr>
							<tr>
								<td class="formtd" align="left" style="font-weight: bold;">
								</td>
								<td class="formtd" align="left"><a class="button"
									style="margin-left: 10px;" onclick="sendAsk(1,'ask');"><span>发表提问</span></a>
									<a class="button" style="margin-left: 10px;"
									onclick="sendAsk(0,'ask');"><span>清空内容</span></a></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${askType eq 'feedback' }">
						<input type="hidden" name="courseId" value="${course.resourceid }" />
						<table class="form" width="100%">
							<tr>
								<td class="formtd" width="15%" style="font-weight: bold;">标题:</td>
								<td class="formtd" width="85%"><input type="text"
									name="title" value="${course.courseName } - 课件学习的问题反馈"
									class="required" style="width: 60%" /></td>
							</tr>
							<tr>
								<td class="formtd" style="font-weight: bold;">反馈类型</td>
								<td class="formtd"><gh:select
										dictionaryCode="CodeFacebookType" value="4"
										classCss="required" name="facebookType" style="width:125px;" />
									<span style="color: red;">* (如果是课件学习方面的问题，请告知具体课程名称)</span></td>
							</tr>
							<tr>
								<td class="formtd" align="left" style="font-weight: bold;">内容:</td>
								<td class="formtd" align="left"><textarea
										id="ask_topicContent" name="content" rows="5" cols=""
										style="width: 90%; height: 320px; visibility: hidden;"></textarea></td>
							</tr>
							<tr>
								<td class="formtd" align="left" style="font-weight: bold;">
								</td>
								<td class="formtd" align="left"><a class="button"
									style="margin-left: 10px;" onclick="sendAsk(1,'feedback');"><span>提交反馈</span></a>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
			</form>
			<script type="text/javascript">
			function sendAsk(type,askType){
				var $form = $("#ask_form");
				if(type==1){//提交
					if($.trim($("#ask_form input[name='title']").val())==""){
						alert("问题不能为空");
						return false;
					}
					if(askType=='feedback' && $("#ask_form select[name='facebookType']").val()==""){
						alert("反馈类型不能为空");
						return false;
					}
					if(KE.isEmpty("ask_topicContent")){
						alert("内容不能为空");
						return false;
					}					
					$.ajax({
						type:'POST',
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						success: function (json){
							alert(json.message);
							if (json.statusCode == 200){
								var url;
								if(askType=='ask'){
									url = baseUrl+"/resource/course/ask.html?type=ask&syllabusid="+$form.find("input[name='syllabusid']").val()
								} else {
									url = baseUrl+"/resource/course/ask.html?type=feedback&courseId="+$form.find("input[name='courseId']").val()
								}
								var title = askType=="ask"?"随堂问答":(askType=="faq"?"常见问题":"反馈意见");
								Dialogs.load(url,{id:'resource_course_dialog',draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:800,height:600}).title(title);
							} 
						},							
						error: function (xhr, ajaxOptions, thrownError){
							alert("抱歉，系统出错了！<br/>错误代码："+xhr.status+" - "+ajaxOptions+"<br/>请联系系统管理员。");
						}
					});				
				} else if(type==0) {//清空
					$("#ask_form input[name='title']").val("");
					KE.text("ask_topicContent","");
				}
			}
			$(function() {
		  		KE.init({
			      id : 'ask_topicContent',	         
			      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=ask_topicContent&storePath=users,${storeDir},images',	       
			      allowFileManager:true,
			      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
			      rootPath:'users,${storeDir},images',
			      afterCreate : function(id) {
					 //KE.util.focus(id);						
				  }  
		  		});	 
		  		function loadAskEditor(){
		  		     KE.create('ask_topicContent'); 		    
		  	    } 	        
		  	    window.setTimeout(loadAskEditor,1000);
		  	    
		  	    $("#ask_Area_Div").parent().scrollTop($("#newAsk").position ().top);
	    	}); 
		</script>
		</c:if>
	</div>
</body>
</html>