<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>学生自荐作业</title>
<gh:loadCom components="fileupload" />
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studySelfExercise"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/studentselftexercise.html?courseId=${course.resourceid}">学生自荐作业</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > 学生自荐作业</div>
			<div class="clear"></div>
			<div id="content">
				<table class="list" width="100%">
					<thead>
						<tr>
							<th width="9%">序号</th>
							<th width="45%">作业内容</th>
							<th width="18%">下载</th>
							<th width="28%">老师点评</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${studentSelfExerciseList }" var="exercise"
							varStatus="vs">
							<tr>
								<td align="center">${vs.index+1 }</td>
								<td>${exercise.descrip }</td>
								<td align="center"><c:if
										test="${not empty exercise.attachs }">
										<a
											onclick="downloadAttachFile('${exercise.attachs[0].resourceid }')"
											href="#">下载</a>
									</c:if></td>
								<td align="center">${exercise.comments }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<c:if test="${isStudent eq 'Y' }">
					<h2 class="title1"
						style="margin-top: 50px; text-align: left; font-weight: bold;">上传我的自荐作业</h2>
					<form id="selfExercixeForm" method="post" style="width: 100%;"
						action="${baseUrl}/resource/course/studentselftexercise/save.html"
						onsubmit="return false;">
						<input type="hidden" name="courseId" value="${course.resourceid }" />
						<table class="list" width="100%">
							<tr>
								<td width="15%" style="font-weight: bold;">作业描述:</td>
								<td width="85%"><textarea name="descrip" rows="3" cols=""
										style="width: 80%;"> </textarea></td>
							</tr>
							<tr>
								<td align="left" style="font-weight: bold;">作业附件:</td>
								<td align="left"><gh:upload hiddenInputName="uploadfileid"
										baseStorePath="users,${storeDir},attachs" pageType="other"
										isMulti="false" uploadLimit="1" uploadType="attach" /> <span
									style="color: green;">*附件大小不能超过30M</span></td>
							</tr>
							<tr>
								<td align="left" style="font-weight: bold;"></td>
								<td align="left">
									<button class="button" style="margin-left: 10px;"
										onclick="sendSelfExercise(this);" flag="0">
										<span>提交作业</span>
									</button>
								</td>
							</tr>
						</table>
					</form>
					<script type="text/javascript">					
					 function sendSelfExercise(obj){
						 var $form = $("#selfExercixeForm");
						 if($.trim($form.find("textarea[name='descrip']").val())==""){
							 alert("作业描述不能为空.");
							 return false;
						 }
						 if($form.find("input[name='uploadfileid']").length==0){
							 alert("作业附件不能为空.");
							 return false;
						 }		
						 $(obj).attr("disabled","disabled");
						 $.ajax({
								type:'POST',
								url:$form.attr("action"),
								data:$form.serializeArray(),
								dataType:"json",
								cache: false,
								success: function (json){
									if(json.message) alert(json.message);
									if (json.statusCode && json.statusCode == 200){
										$form.find("textarea[name='descrip']").val('');
										window.location.reload();
									} else {
										$(obj).attr("disabled","");
									}
								},							
								error: function (xhr, ajaxOptions, thrownError){
									$(obj).attr("disabled","");
								}
							});
					 }					
				</script>
				</c:if>
				<script type="text/javascript">	
				function downloadAttachFile(attid){
					var url = baseUrl+"/edu3/framework/filemanage/${empty user ? 'public/':''}download.html?id="+attid;
					var elemIF = document.createElement("iframe");  
					elemIF.src = url;  
					elemIF.style.display = "none";  
					document.body.appendChild(elemIF); 
				}
			</script>
			</div>
		</div>
		<!--end right-->
	</div>
</body>
</html>