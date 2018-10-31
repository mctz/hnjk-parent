<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>在线作业</title>
<style>
.nostyle td {
	height: 4px
}

.list {
	table-layout: fixed;
	word-wrap: break-word
}

.list td div {
	line-height: 170%;
	font-size: 10pt
}

.list p {
	line-height: 170%;
	font-size: 10pt;
	width: 100%;
	height: auto;
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
		} else {
			msg += "您确定现在就要提交本次作业吗？";
		}
		alertMsg.confirm(msg, {
			okCall: function(){//执行	
				var notAnswerNum = findNotAnswerNum();
				if(type=='submit' && notAnswerNum>0){
					alertMsg.warn("你还有"+notAnswerNum+"题没有做");
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
						alertMsg.warn(limitMsg);
						return false;	
					} else {
						$.ajax({
							type:'POST',
							url:$('#studentExerciseForm').attr("action")+"?type="+type,
							data:$('#studentExerciseForm').serializeArray(),
							dataType:"json",
							cache: false,
							success: validateFormCallback,					
							error: DWZ.ajaxError
						});
					}					
				}			
			}
		});	
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
		if($("textarea[id^='answercontent']").size()>0){
			$("textarea[id^='answercontent']").each(function(){
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
							KE.util.focus(id);
						}	      
				});
			});
		}		
	});
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="studentExerciseForm" method="post"
				action="${baseUrl}/edu3/learning/interactive/exercise/save.html"
				class="pageForm" onsubmit="return false;">
				<input type="hidden" value="${exerciseBatch.resourceid}"
					name="exerciseBatchId" /> <input type="hidden"
					value="${answers['exerciseResult'].resourceid}"
					name="exerciseResultId" />
				<div class="pageFormContent" layoutH="50">
					<h2 class="contentTitle" style="border: 0px solid #FFF;">${exerciseBatch.course.courseName}·${exerciseBatch.colName}</h2>
					<table width="100%" class="list">
						<c:set
							value="${fn:split('_,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
							var="letterList" />
						<c:forEach items="${exerciseBatch.exercises }" var="exercise"
							varStatus="vs">
							<tr class="nostyle">
								<td>
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
											<span style="font-weight: bold;">附件： <c:forEach
													items="${exercise.attachs}" var="attach" varStatus="vs">
													<a onclick="downloadAttachFile('${attach.resourceid }')"
														href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
								</c:forEach></span>
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
																	end="${exercise.courseExam.answerOptionNum }" var="idx">
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
																	end="${exercise.courseExam.answerOptionNum }" var="idx">
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
																<span style="font-weight: bold;">附件：</span>
																<c:forEach
																	items="${answers[exercise.resourceid].attachs }"
																	var="attach" varStatus="vs">
																	<a
																		onclick="downloadAttachFile('${attach.resourceid }')"
																		href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
										</c:forEach>
															</div>
														</c:if>
													</c:when>
													<c:otherwise>
														<gh:upload
															hiddenInputName="uploadfileid${exercise.resourceid }"
															baseStorePath="users,${storeDir},attachs"
															fileSize="10485760" uploadType="attach"
															attachList="${answers[exercise.resourceid].attachs }" />
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</div> <c:if test="${answers['exerciseResult'].status eq 2 }">
										<div class="answer">
											<span style="font-weight: bold;">参考答案：</span>${exercise.courseExam.answer }</div>
									</c:if>
									<div style="both: clear; height: 12px"></div>
								</td>
							</tr>
						</c:forEach>
					</table>
					<c:if test="${isStudent eq 'Y' }">
						<table width="100%" style="background-color: #EBF0F5;">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>
									<div
										style="float: right; margin-left: 10px; margin-right: 50px;">
										<c:if
											test="${exerciseBatch.isEnd eq 'N' and (empty answers['exerciseResult'].status or answers['exerciseResult'].status eq 0)}">
											<a class="button" href="javascript:void(0);"
												id="exerciseSaveButton" onclick="_exerciseSubmit('save')"><span>保存我的答案</span></a>
											<a class="button" href="javascript:void(0);"
												id="exerciseSubmitButton"
												onclick="_exerciseSubmit('submit')"><span>提交本次作业</span></a>
										</c:if>
									</div>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table>
					</c:if>
			</form>
		</div>
	</div>
	<script type="text/javascript">
//附件下载
function downloadAttachFile(attid){
		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
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
</body>
</html>