<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习</title>
<style>
.nostyle td {
	height: 4px
}

.list div {
	line-height: 170%;
}

.list p {
	width: 100%
}
</style>
</head>
<body>
	<script type="text/javascript">
function addCourseExamToExercise(){
	if(!isChecked('resourceid',"#exercise_preCourseExamBody")){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	var url = "${baseUrl}/edu3/learning/exercisebatch/exercise/save.html";
	alertMsg.confirm("你确定要把这些随堂练习添加到作业<b>${exerciseBatch.colName}</b>?", {
			okCall: function(){//执行			
				var res = "";
				var k = 0;
				var num  = $("#exercise_preCourseExamBody input[name='resourceid']:checked").size();
				$("#exercise_preCourseExamBody input[@name='resourceid']:checked").each(function(){
	                    res+=$(this).val();
	                    if(k != num -1 ) res += ",";
	                    k++;
	             });
				var colsId = $("#exercise_dialogSearch_colsId").val();
				$.post(url,{resourceid:res,colsId:colsId}, _addExerciseCallBack, "json");
			}
	});	
}
function _addExerciseCallBack(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		if(json.reloadUrl){
			navTab.reload(json.reloadUrl);
		}
		if ("closeCurrent" == json.callbackType) {
			$.pdialog.closeCurrent();
		} else {
			dialogPageBreak();
		}			
	}	
}
</script>
	<c:choose>
		<%-- 客观题 --%>
		<c:when test="${exerciseBatch.colType eq '1'}">
			<div class="page">
				<div class="pageHeader">
					<form onsubmit="return dialogSearch(this);"
						action="${baseUrl }/edu3/learning/exercisebatch/exercise/list.html"
						method="post">
						<div class="searchBar">
							<ul class="searchContent">
								<li><label>知识节点:</label> <input type="hidden"
									id="exercise_dialogSearch_colsId"
									value="${condition['colsId'] }" name="colsId" /> <select
									id="exercise_dialogSearch_syllabusId" name="syllabusId"
									style="width: 125px;">
										<option value="">请选择</option>
										<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
											<option value="${syb.resourceid }"
												<c:if test="${syb.resourceid eq condition['syllabusId']}"> selected </c:if>>
												<c:forEach var="seconds" begin="1"
													end="${syb.syllabusLevel}" step="1">&nbsp;&nbsp;</c:forEach>
												${syb.syllabusName }
											</option>
										</c:forEach>
								</select></li>
								<%-- 
						<li>
							<label>是否发布:</label>	
							<gh:select dictionaryCode="yesOrNo" name="isPublished" id="exercise_courseexam_isPublished" value="${condition['isPublished'] }"/>			
						</li>
						 --%>
							</ul>
							<div class="subBar">
								<ul>
									<li><div class="buttonActive">
											<div class="buttonContent">
												<button type="submit">查 询</button>
											</div>
										</div></li>
								</ul>
							</div>
						</div>
					</form>
				</div>
				<div class="panelBar">
					<ul class="toolBar">
						<li class=""><a onclick="addCourseExamToExercise()" href="#"
							class="icon" title="添加作业题目"><span>添加作业题目</span></a></li>
					</ul>
				</div>
				<table class="list" style="width: 100%;">
					<thead>
						<tr>
							<th width="20%"><input type="checkbox" name="checkall"
								id="check_all_exercise_ativepreview"
								onclick="checkboxAll('#check_all_exercise_ativepreview','resourceid','#exercise_preCourseExamBody')" /></th>
							<th>题目内容</th>
						</tr>
					</thead>
				</table>
				<div class="pageContent" layouth="138">
					<table class="list" style="width: 100%;">
						<thead>
							<tr>
								<th width="20%"></th>
								<th></th>
							</tr>
						</thead>
						<tbody id="exercise_preCourseExamBody">
							<c:forEach items="${activeCourseExamList.result }" var="exam"
								varStatus="vs">
								<c:if
									test="${syllabusExam[exam.syllabus.resourceid] eq exam.resourceid }">
									<tr>
										<td colspan="2">
											<div style="font-weight: bold;">${exam.syllabus.syllabusName }</div>
										</td>
									</tr>
								</c:if>
								<tr>
									<td width="20%"><input type="checkbox" name="resourceid"
										value="${exam.resourceid }" autocomplete="off" /> <br />
									<span style="font-weight: bold;">${exam.showOrder }.
											(${ghfn:dictCode2Val('CodeExamType',exam.courseExam.examType) })</span>
									</td>
									<td>
										<div>${exam.courseExam.question }</div>
										<div tyle="font-weight: bold;">答案：${exam.courseExam.answer }</div>
									</td>
								</tr>
								<tr>
									<td colspan="2">&nbsp;</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<gh:page page="${activeCourseExamList}"
					goPageUrl="${baseUrl}/edu3/learning/exercisebatch/exercise/list.html"
					targetType="dialog" condition="${condition }" pageNumShown="5"
					pageType="sys" />
			</div>
		</c:when>
		<%-- 主观题 --%>
		<c:otherwise>
			<script type="text/javascript">
		$(function() {
			KE.init({//初始化编辑器
		      id : 'exerciseBatch_exerciseContent',     
		      items : [
						'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons',  'link'],

			      afterCreate : function(id) {
						KE.util.focus(id);
					}	      
			});
			KE.init({//初始化编辑器
			      id : 'exerciseBatch_exerciseAnswer',     
			      items : [
							'fontname', 'fontsize', '|','subscript','superscript', 'textcolor', 'bgcolor', 'bold', 'italic', 'underline',
							'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist', '|', 'emoticons',  'link'],

				      afterCreate : function(id) {
							KE.util.focus(id);
						}	      
				});
		});
		</script>
			<h2 class="contentTitle">${exerciseBatch.colName}主观题</h2>
			<div class="page">
				<div class="pageContent">
					<form method="post"
						action="${baseUrl}/edu3/learning/exercisebatch/exercise/save.html"
						class="pageForm"
						onsubmit="return validateCallback(this,_addExerciseCallBack);">
						<input type="hidden" name="colsId"
							value="${exerciseBatch.resourceid }" /> <input type="hidden"
							name="resourceid" value="${exercise.resourceid }">
						<div class="pageFormContent" layoutH="97">
							<table class="form">
								<tr class="nostyle">
									<td width="20%">题目内容：</td>
									<td><textarea rows='10' id="exerciseBatch_exerciseContent"
											name='question' style='width: 98%' class='required'>${exercise.courseExam.question }</textarea></td>
								</tr>
								<tr>
									<td>附件：</td>
									<td><gh:upload hiddenInputName="uploadfileid"
											baseStorePath="users,${storeDir},attachs" fileSize="10485760"
											uploadType="attach" attachList="${exercise.attachs }" />
										<div class="tips" style="width: 80%;">
											(单个文件上传大小不能大于10M)<br />(为方便学生下载附件，请上传Office2003版本的文件;如是Office2007版的文件，请先将其另存为Office2003版的文件再进行上传。)
										</div></td>
								</tr>
								<tr class="nostyle">
									<td>参考答案：</td>
									<td><textarea rows='8' id="exerciseBatch_exerciseAnswer"
											name='answer' style='width: 98%'>${exercise.courseExam.answer }</textarea></td>
								</tr>
								<tr>
									<td>字数要求：</td>
									<td><input type="text" name="limitNum"
										value="${exercise.limitNum }" class="digits" /> <span
										class="tips">0或不填写时表示不限制字数</span></td>
								</tr>
							</table>
						</div>
						<div class="formBar">
							<ul>
								<li><div class="buttonActive">
										<div class="buttonContent">
											<button type="submit">保存</button>
										</div>
									</div></li>
								<li><div class="button">
										<div class="buttonContent">
											<button type="button" class="close"
												onclick="$.pdialog.closeCurrent();">取消</button>
										</div>
									</div></li>
							</ul>
						</div>
					</form>
				</div>
			</div>
			<script type="text/javascript">
		function createExerciseKE(){
			KE.create('exerciseBatch_exerciseAnswer');
			KE.create('exerciseBatch_exerciseContent');			
		}
		setTimeout(createExerciseKE,1000);
		</script>
		</c:otherwise>
	</c:choose>
</body>
</html>