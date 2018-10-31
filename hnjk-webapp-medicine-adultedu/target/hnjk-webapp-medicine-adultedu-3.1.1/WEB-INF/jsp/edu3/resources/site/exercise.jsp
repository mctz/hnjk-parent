<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业系统</title>
<gh:loadCom components="editor,fileupload" />
<style type="text/css">
.progressBar {
	display: none;
	width: 258px;
	height: 28px;
	position: fixed;
	top: 50%;
	left: 50%;
	margin-left: -74px;
	margin-top: -14px;
	padding: 10px 10px 10px 50px;
	text-align: left;
	line-height: 27px;
	font-weight: bold;
	z-index: 9999;
	border: solid 2px #86a5ad;
	background: #FFF
		url(${baseUrl}/style/default/resources/images/progressBar_m.gif)
		no-repeat 10px 10px;
}
</style>
<script type="text/javascript">
function exercise_exchangeoption(resourceid){
	$("#select_more_show_"+resourceid).show();
	$("#select_more_hide_"+resourceid).hide();
}
function _exerciseSubmit(type){
	var msg = "";
	if(type=='save'){
		msg += "您确定要保存已答答案吗？";
	} else if(type=='test'){//测试
		msg += "您确定要测试作业吗？";
	} else {
		msg += "您确定现在就要提交本次作业吗？";
	}
	if(window.confirm(msg)){
		var notAnswerNum = findNotAnswerNum();
		if((type=='submit' || type=='test') && notAnswerNum>0){
			alert("你还有"+notAnswerNum+"题没有做");
			return false;					
		} else {
			var valid = true;
			var limitMsg = "";
			$("#studentExerciseForm textarea[colType]").each(function(){
				var limitNum = $(this).attr('limitNum');
				if(limitNum!=""&&limitNum!='0'){
					var cNum = KE.count($(this).attr("id"),'text');
					if(cNum>parseInt(limitNum)){
						valid = false;
						limitMsg = "第"+$(this).attr("showOrder")+"题字数超过字数限制"+limitNum;
						return false;
					}
				}						
			});
			if(!valid){
				alert(limitMsg);
				return false;	
			} else {
				$.ajax({
					type:'POST',
					url:$('#studentExerciseForm').attr("action")+"?type="+type,
					data:$('#studentExerciseForm').serializeArray(),
					dataType:"json",
					cache: false,
					beforeSend : function(){progressAction(0);},
					success: function (json){
						progressAction(1);
						if(json.message){
							alert(json.message);
						}
						if(json.statusCode && json.statusCode == 200 && type != "test"){
							window.location.reload();
						} 
					},
					error: function (){	progressAction(1);},
					complete:function(){progressAction(1);}
				});
			}					
		}			
	}
}
function progressAction(flag){
	if(flag==0){
		$("#progressBar").show();
		$("#exerciseSaveButton").attr("disabled","disabled");
		$("#exerciseSubmitButton").attr("disabled","disabled");
	} else {
		$("#progressBar").hide();
		$("#exerciseSaveButton").attr("disabled",false);
		$("#exerciseSubmitButton").attr("disabled",false);
	}
	
}
//未做的题目数
function findNotAnswerNum(){
	var num = 0;
	$("div[id^='answer']").each(function(){
		var answer = "";
		if($(this).attr('rel')=='4'){
			answer = $.trim($("[name='"+this.id+"']").val());
		} else if($(this).attr('rel')=='5'){				
			answer = $.trim(KE.text($("[name='"+this.id+"']").attr("id")));
			if(answer==""){//有附件也算答题
				answer = $(this).find("input[name^='uploadfileid']").size();
				if(answer==0)answer="";
			}
		} else {
			$("input[name='"+this.id+"']:checked").each(function(){
				answer += $(this).val();
			});	
		}	
		if(answer==""){
			num++;
		}
	});
	return num;
}
$(function() {
	$("#progressBar").hide();
	if($("textarea[id^='answercontent']").size()>0){
		$("textarea[id^='answercontent']").each(function(){
			var r = $(this).attr('readonly');
			KE.init({//初始化编辑器
		      id : $(this).attr("id"),     
		      items : [
						'plainpaste','wordpaste','fontname', 'fontsize', 'image','|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist'],
			  imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId='+$(this).attr("id")+'&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
			  allowFileManager:true,
			  fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
			  rootPath:'users,${storeDir},images',         
			      afterCreate : function(id) {
			    		if(r){ //只读
			    	  		KE.toolbar.disable(id, []);
							KE.readonly(id);
							KE.g[id].newTextarea.disabled = true;
			    	  	}
						KE.util.focus(id);
					}	      
			});
		});
	}		
});
</script>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyExercise"></div>
			<div id="left_menu">
				<ul>
					<li><a
						href="${baseUrl }/resource/course/exercisebatch.html?courseId=${course.resourceid}">作业系统</a></li>
					<li><a
						href="${baseUrl }/resource/course/mocktest.html?courseId=${course.resourceid}">在线自测系统</a></li>
				</ul>
				<p>&nbsp;</p>
			</div>
			<!--end menu-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">当前位置：课程学习 > 作业系统 >
				${exerciseBatch.colName }</div>
			<div class="clear"></div>
			<div id="content">
				<!-- 作业习题 -->
				<form id="studentExerciseForm" method="post"
					action="${baseUrl}/resource/course/exercise/save.html"
					class="pageForm" onsubmit="return false;">
					<input type="hidden" value="${exerciseBatch.resourceid}"
						name="exerciseBatchId" /> <input type="hidden"
						value="${answers['exerciseResult'].resourceid}"
						name="exerciseResultId" />
					<div class="pageFormContent">
						<table width="100%" class="form"
							style="table-layout: fixed; word-wrap: break-word; border-collapse: collapse;">
							<c:set
								value="${fn:split('_,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
								var="letterList" />
							<c:forEach items="${exerciseBatch.exercises }" var="exercise"
								varStatus="vs">
								<tr class="${vs.index mod 2 eq 0 ? 'odd' : 'even'}">
									<td class="formtd">
										<div>
											<span style="font-weight: bold;">${exercise.showOrder}.</span>&nbsp;
											${exercise.courseExam.question }&nbsp;
											<c:if
												test="${exerciseBatch.colType eq '2' and exercise.limitNum gt 0 }">
												<span>(${exercise.limitNum }字以内)</span>
											</c:if>
										</div> <c:if
											test="${exerciseBatch.colType eq '2' and not empty exercise.attachs }">
											<div>
												<span style="font-weight: bold;">题目附件：</span>
												<c:forEach items="${exercise.attachs}" var="attach"
													varStatus="vs">
													<a style="color: #3C7FB1;"
														onclick="downloadAttachFile('${attach.resourceid }')"
														href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
									</c:forEach>
											</div>
										</c:if> <input type="hidden" value="${exercise.resourceid}"
										name="exerciseId"
										<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
										<div id="answer${exercise.resourceid }"
											rel="${exercise.courseExam.examType}">
											<c:choose>
												<c:when test="${exerciseBatch.colType eq '1' }">
													<c:if test="${exercise.courseExam.examType ne '6' }">答题：</c:if>
													<c:choose>
														<c:when test="${exercise.courseExam.examType eq '1' }">
															<c:choose>
																<c:when
																	test="${not empty exercise.courseExam.answerOptionNum }">
																	<c:forEach begin="1"
																		end="${exercise.courseExam.answerOptionNum }"
																		var="idx">
																		<input type="radio" value="${letterList[idx] }"
																			name="answer${exercise.resourceid }"
																			<c:if test="${letterList[idx] eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																			<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> ${letterList[idx] }.
														</c:forEach>
																</c:when>
																<c:otherwise>
																	<input type="radio" value="A"
																		name="answer${exercise.resourceid }"
																		<c:if test="${'A' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
														<input type="radio" value="B"
																		name="answer${exercise.resourceid }"
																		<c:if test="${'B' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
														<input type="radio" value="C"
																		name="answer${exercise.resourceid }"
																		<c:if test="${'C' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
														<input type="radio" value="D"
																		name="answer${exercise.resourceid }"
																		<c:if test="${'D' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
													</c:otherwise>
															</c:choose>
														</c:when>
														<c:when test="${exercise.courseExam.examType eq '2' }">
															<c:choose>
																<c:when
																	test="${not empty exercise.courseExam.answerOptionNum }">
																	<c:forEach begin="1"
																		end="${exercise.courseExam.answerOptionNum }"
																		var="idx">
																		<input type="checkbox" value="${letterList[idx] }"
																			name="answer${exercise.resourceid }"
																			<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,letterList[idx]) }">checked="checked"</c:if>
																			<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> ${letterList[idx] }.
													</c:forEach>
																</c:when>
																<c:otherwise>
																	<input type="checkbox" value="A"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'A') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> A.
													<input type="checkbox" value="B"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'B') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> B.
													<input type="checkbox" value="C"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'C') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> C.
													<input type="checkbox" value="D"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'D') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> D.
													<span id="select_more_show_${exercise.resourceid }"
																		<c:if test="${not(fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F'))}">style="display:none"</c:if>>
																		<input type="checkbox" value="E"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
																		E. <input type="checkbox" value="F"
																		name="answer${exercise.resourceid }"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F') }">checked="checked"</c:if>
																		<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>>
																		F.
																	</span>
																	<span id="select_more_hide_${exercise.resourceid }"
																		title="显示更多选项"
																		<c:if test="${fn:containsIgnoreCase(answers[exercise.resourceid].answer,'E') or fn:containsIgnoreCase(answers[exercise.resourceid].answer,'F')}">style="display:none"</c:if>><a
																		href="javascript:void(0)"
																		onclick="exercise_exchangeoption('${exercise.resourceid }')"
																		style="font-weight: bold; color: black; text-decoration: none;">
																			&nbsp;&gt;&gt; </a></span>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:when test="${exercise.courseExam.examType eq '4' }">
															<input type="text"
																value="${answers[exercise.resourceid].answer }"
																name="answer${exercise.resourceid }"
																<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if> />
														</c:when>
														<c:when test="${exercise.courseExam.examType eq '3' }">
															<input type="radio" value="T"
																name="answer${exercise.resourceid }"
																<c:if test="${'T' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 对.
											<input type="radio" value="F"
																name="answer${exercise.resourceid }"
																<c:if test="${'F' eq answers[exercise.resourceid].answer }">checked="checked"</c:if>
																<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">disabled="disabled"</c:if>> 错.
											</c:when>
													</c:choose>
												</c:when>
												<c:otherwise>
													<textarea rows="10" cols="" style="width: 80%;"
														id="answercontent${exercise.resourceid }"
														colType="${exerciseBatch.colType}"
														limitNum="${exercise.limitNum }"
														showOrder="${exercise.showOrder}"
														name="answer${exercise.resourceid }"
														<c:if test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">readonly="readonly" </c:if>>${answers[exercise.resourceid].answer }</textarea>
													<span class="tips">从word复制的文本，请使用编辑器的"从word粘贴"<span
														class="ke-common-icon ke-common-icon-url ke-icon-wordpaste"></span>功能
													</span>
													<c:choose>
														<c:when
															test="${exerciseBatch.isEnd eq 'Y' or not empty answers[exercise.resourceid].status and answers[exercise.resourceid].status ne 0 }">
															<c:if
																test="${not empty answers[exercise.resourceid].attachs }">
																<div>
																	<span style="font-weight: bold;">学生附件：</span>
																	<c:forEach
																		items="${answers[exercise.resourceid].attachs }"
																		var="attach" varStatus="vs">
																		<li style="padding-bottom: 2px"><img
																			style="cursor: pointer; height: 10px"
																			src="${baseUrl }/jscript/jquery.uploadify/images/attach.png">&nbsp;&nbsp;<a
																			style="color: #3C7FB1;"
																			onclick="downloadAttachFile('${attach.resourceid }')"
																			href="#">${attach.attName }&nbsp;</a></li>
																	</c:forEach>
																</div>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if test="${isStudent eq 'Y'}">
																<gh:upload
																	hiddenInputName="uploadfileid${exercise.resourceid }"
																	baseStorePath="users,${storeDir},attachs"
																	fileSize="10485760" pageType="other"
																	uploadType="attach"
																	attachList="${answers[exercise.resourceid].attachs }" />
															</c:if>
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</div> <c:if
											test="${isStudent eq 'Y' and answers['exerciseResult'].status eq 2 }">
											<div class="answer">
												<span style="font-weight: bold;">参考答案：</span>${exercise.courseExam.answer }</div>
										</c:if>
										<div style="both: clear; height: 12px"></div>
									</td>
								</tr>
							</c:forEach>
							<c:choose>
								<c:when
									test="${isStudent eq 'Y' and exerciseBatch.isEnd eq 'N' and (empty answers['exerciseResult'].status or answers['exerciseResult'].status eq 0)}">
									<tr class="even">
										<td class="formtd" width="100%">
											<div
												style="float: right; margin-left: 10px; margin-right: 50px; line-height: 18px;">
												<button class="button" id="exerciseSaveButton"
													onclick="_exerciseSubmit('save')">保存我的答案</button>
												<button class="button" id="exerciseSubmitButton"
													onclick="_exerciseSubmit('submit')">提交本次作业</button>
											</div>
										</td>
									</tr>
								</c:when>
								<c:when
									test="${isStudent ne 'Y' and exerciseBatch.colType eq '1' and exerciseBatch.isEnd eq 'N' }">
									<tr class="even">
										<td class="formtd" width="100%">
											<div
												style="float: right; margin-left: 10px; margin-right: 50px; line-height: 18px;">
												<a class="button" href="javascript:void(0);"
													id="exerciseTestButton" onclick="_exerciseSubmit('test')"><span>
														测 试 </span></a>
											</div>
										</td>
									</tr>
								</c:when>
							</c:choose>
						</table>
				</form>
			</div>
		</div>
		<!--end right-->
		<script type="text/javascript">
	//附件下载
	function downloadAttachFile(attid){
			var url = baseUrl+"/edu3/framework/filemanage/${empty user ? 'public/':''}download.html?id="+attid;
			var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	}
	function studentExerciseAnswerKE(){
		if($("textarea[id^='answercontent']")){
			$("textarea[id^='answercontent']").each(function(){
				KE.create($(this).attr("id"));		
			});
		}		
	}
	setTimeout(studentExerciseAnswerKE,1000);
	</script>
	</div>
	<div id='progressBar' class='progressBar'>正在提交数据，请稍等...</div>
</body>
</html>