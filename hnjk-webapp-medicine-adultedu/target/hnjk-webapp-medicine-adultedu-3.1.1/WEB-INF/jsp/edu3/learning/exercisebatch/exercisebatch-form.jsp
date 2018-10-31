<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课后作业管理</title>
<style>
.list {
	table-layout: fixed;
	word-wrap: break-word;
}

.list div {
	line-height: 170%;
}

.list p {
	width: 100%;
	height: auto;
}
</style>
<script type="text/javascript">	
		$(document).ready(function(){
			$("select[class*=flexselect]").flexselect();
		});
		//根据作业类型显示表单
		function switchColType(type){
			if(type=='1'){
				$("#exerciseBatchEditForm .objectiveNum").show();
				$("#exerciseBatchEditForm .scoringType").hide();				
				$("#exerciseBatchEditForm_scoringType").removeClass("required");
				$("#exerciseBatchEditForm_objectiveNum").addClass("required digits");
			} else {
				$("#exerciseBatchEditForm .scoringType").show();				
				$("#exerciseBatchEditForm_scoringType").addClass("required");
				$("#exerciseBatchEditForm .objectiveNum").hide();
				$("#exerciseBatchEditForm_objectiveNum").removeClass("required digits");
			}			
		}
		//根据选择列表的值显示后来选项
		function isYesOrNoChange(cls,yn){						
			if(yn=='Y'){
				$("#exerciseBatchEditForm ."+cls).show();
			} else {
				$("#exerciseBatchEditForm ."+cls).hide();
			}
		}	
		//新增
		function addExercise(){		
			var colstatus = $("#exerciseBatchEditForm input[name='status']").val();
			if(colstatus!='0'){	
			 	alertMsg.warn("该次作业已经发布或结束，不能再更改！");
			 	return false;
			 } else {
				 var url = "${baseUrl}/edu3/learning/exercisebatch/exercise/list.html";
				 $.pdialog.open(url+'?colsId='+$("#exerciseBatchEditForm input[name='resourceid']").val(),'RES_METARES_EXERCISE_UNACTIVE_EXERCISE',  '新增作业习题',{width:800,height:600});
			 }			
		}		
		//删除
		function removeExercise(){	
			if(!isChecked('resourceid','#exerciseBatch_exerciseBody')){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 		}
			var colstatus = $("#exerciseBatchEditForm input[name='status']").val();
			var colsId = $("#exerciseBatchEditForm input[name='resourceid']").val();
			if(colstatus!='0'){	
			 	alertMsg.warn("该次作业已经发布或结束，不能再更改！");
			 	return false;
			 } else {
				 pageBarHandle("您确定要删除这些习题吗？","${baseUrl}/edu3/learning/exercisebatch/exercise/remove.html?colsId="+colsId,"#exerciseBatch_exerciseBody");
			 }			
		}			
		
		function goSelectClasses(title){
			var courseId = $('#exerciseBatchForm_CourseId').val()
			var yearInfo = $('#exerciseBatchForm_yearInfoId').val();
			var term = 	   $('#exerciseBatchForm_term').val();
			var onlineClassesId = 	   $('#onlineClassesId').val();
			$.pdialog.open("${baseUrl }/edu3/metares/exercise/unactiveexercise/classes-select.html?courseId="+courseId+"&yearInfo="+yearInfo+"&term="+term+"&onlineClassesId="+onlineClassesId
					,"onlineClasses","选择"+title,{height:300,width:500});
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">${(empty exerciseBatch.resourceid)?'新增':'编辑' }${(exerciseBatch.colType eq '1')?'客观题':'' }作业</h2>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li><a href="#"><span>作业设置</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<!-- 1 -->
						<div>
							<form method="post" id="exerciseBatchEditForm"
								action="${baseUrl}/edu3/metares/exercise/unactiveexercise/save.html"
								class="pageForm" onsubmit="return validateCallback(this);">
								<input type="hidden" name="resourceid"
									value="${exerciseBatch.resourceid }" /> <input type="hidden"
									name="fillinMan" value="${exerciseBatch.fillinMan }" /> <input
									type="hidden" name="fillinManId"
									value="${exerciseBatch.fillinManId }" /> <input type="hidden"
									name="status" value="${exerciseBatch.status }" /> <input
									type="hidden" name="orderCourseNum"
									value="${exerciseBatch.orderCourseNum }" /> <input
									type="hidden" id="exerciseBatchEditForm_isPublished"
									name="isPublished" value="N" />
								<div class="pageFormContent">
									<table class="form">
										<tr>
											<td width="15%">所属课程:</td>
											<td width="35%">${exercisebatchformCourseSelect }</td>
											<td>作业标题:</td>
											<td><input type="text" id="exerciseBatchForm_colName"
												name="colName" style="width: 80%"
												value="${exerciseBatch.colName }" class="required" /></td>
										</tr>
										<tr>
											<td width="15%">年度:</td>
											<td width="35%"><gh:selectModel
													id="exerciseBatchForm_yearInfoId" name="yearInfoId"
													bindValue="resourceid" displayValue="yearName"
													condition="firstYear<=${year}"
													modelClass="com.hnjk.edu.basedata.model.YearInfo"
													value="${exerciseBatch.yearInfo.resourceid}"
													orderBy="firstYear desc" style="width:52%"
													classCss="required" /> <font color="red">*</font></td>
											<td width="15%">学期:</td>
											<td width="35%"><gh:select id="exerciseBatchForm_term"
													name="term" value="${exerciseBatch.term}"
													dictionaryCode="CodeTerm" style="width:52%"
													classCss="required" /><font color="red">*</font></td>
										</tr>
										<tr>
											<c:choose>
												<c:when test="${exerciseBatch.colType eq '1' }">
													<td>客观题题数:</td>
													<td><input type="text"
														id="exerciseBatchEditForm_objectiveNum"
														name="objectiveNum" class="required digits"
														value="${exerciseBatch.objectiveNum }" /></td>
												</c:when>
												<c:otherwise>
													<td>计分形式:</td>
													<td <c:if test="${isBrschool=='N' }"> colspan="3"</c:if>>
														<gh:select id="exerciseBatchEditForm_scoringType"
															name="scoringType" value="${exerciseBatch.scoringType }"
															dictionaryCode="CodeExerciseBatchScoringType"
															style="width:128px;" classCss="required" /> <span
														style="color: red;">*</span> <input type="hidden"
														name="colType" value="2" />
													</td>
													<c:if test="${isBrschool=='Y' }">
														<td>班级：</td>
														<td>
															<%--<input type="text" name="onlineClasses" value="${exerciseBatch.classesNames }" id="onlineClasses" readonly="readonly" class="required"/> --%>
															<textarea name="onlineClasses" id="onlineClasses"
																readonly="readonly" class="required"
																style="width: 400px; height: 80px">${exerciseBatch.classesNames }</textarea>
															<font color="red">*</font> <input type="hidden"
															name="onlineClassesId" id="onlineClassesId"
															value="${exerciseBatch.classesIds }" /> <a
															href="javascript:;" class="button"
															onclick="goSelectClasses('班级');"><span>选择班级</span></a>
														</td>
													</c:if>
												</c:otherwise>
											</c:choose>
										</tr>

										<%-- 
								<tr>
									<td>作业说明:</td>
									<td colspan="3">
										<textarea name="descript" rows="3" cols="" style="width:80%">${exerciseBatch.descript }</textarea>
									</td>
								</tr>
								 --%>
										<tr>
											<td>作业开始日期:</td>
											<%-- <td><input type="text" name="startDate" value="<fmt:formatDate value='${exerciseBatch.startDate}' pattern='yyyy-MM-dd'/>" class="required" id="exerciseBatchForm_startDate" onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'exerciseBatchForm_startDate\')||\'${learningEndTime}\'}',minDate:'${learningStartTime}',onpicked:checkExerciseDate})"></td>--%>
											<td><input type="text" name="startDate"
												value="<fmt:formatDate value='${exerciseBatch.startDate}' pattern='yyyy-MM-dd'/>"
												class="required" id="exerciseBatchForm_startDate"
												onFocus="WdatePicker({isShowWeek:true,maxDate:'${learningEndTime}',minDate:'${learningStartTime}',onpicked:checkExerciseDate})"></td>
											<td>作业截止日期:</td>
											<td>
												<%-- <input type="text" name="endDate" value="<fmt:formatDate value='${exerciseBatch.endDate}' pattern='yyyy-MM-dd'/>" class="required" id="exerciseBatchForm_endDate" onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'exerciseBatchForm_endDate\')||\'${learningStartTime}\'}',maxDate:'${learningEndTime}',onpicked:checkExerciseDate})">--%>
												<input type="text" name="endDate"
												value="<fmt:formatDate value='${exerciseBatch.endDate}' pattern='yyyy-MM-dd'/>"
												class="required" id="exerciseBatchForm_endDate"
												onFocus="WdatePicker({isShowWeek:true,minDate:''${learningStartTime}',maxDate:'${learningEndTime}',onpicked:checkExerciseDate})">
												<%-- 
									<c:choose>
										<c:when test="${exerciseBatch.endDate eq null }">
										<input type="text" name="endDate" value="<fmt:formatDate value='${exerciseBatch.endDate}' pattern='yyyy-MM-dd'/>" class="required" id="exerciseBatchForm_endDate" onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'exerciseBatchForm_endDate\')||\'${learningStartTime}\'}',maxDate:'${learningEndTime}',onpicked:checkExerciseDate})">		
										</c:when>
									<c:otherwise>
										<input id = "exerciseBatchForm_endDate" name = "endDate" value="<fmt:formatDate value='${exerciseBatch.endDate}' pattern='yyyy-MM-dd'/>" readonly="readonly" >
									</c:otherwise>
									</c:choose>
									--%>
											</td>
											<script type="text/javascript">
										function checkExerciseDate(){
											var d1 = $dp.$('exerciseBatchForm_startDate').value;
											var d2 = $dp.$('exerciseBatchForm_endDate').value;
											if(d1!=""){
												document.getElementById('exerciseBatchForm_endDate').onfocus=function(){
													WdatePicker({isShowWeek:true,minDate:d1,maxDate:'${learningEndTime}',onpicked:checkExerciseDate})
												}
											}
											if(d2!=""){
												document.getElementById('exerciseBatchForm_startDate').onfocus=function(){
													WdatePicker({isShowWeek:true,minDate:'${learningStartTime}',maxDate:d2,onpicked:checkExerciseDate})
												}
											}
											if(d1!=""&&d2!=""){
												var date1 = new Date(Date.parse(d1.replace(/-/g, "/")));
												var date2 = new Date(Date.parse(d2.replace(/-/g, "/")));
												var days = (date2-date1)/(24*60*60*1000)+1;
												if(days<25){
													alertMsg.info("您给学生<b>"+days+"</b>天完成作业时间太短，建议至少预留<b>25-30</b>天！");
												} else {
													alertMsg.info("您给学生预留了<b>"+days+"</b>天完成作业");
												}
											}
										}
									</script>
										</tr>
										<tr>
											<td colspan="2"></td>
											<td colspan="2"><span class="tips">建议每次给学生预留至少25-30天完成作业</span></td>
										</tr>
										<%-- 														
								<tr class="scoringType" <c:if test="${exerciseBatch.colType ne '2'}"> style="display: none"</c:if>>
									<td>计分形式:</td>
									<td colspan="3">
										<gh:select id="exerciseBatchEditForm_scoringType" name="scoringType" value="${exerciseBatch.scoringType }" dictionaryCode="CodeExerciseBatchScoringType" style="width:128px;" />
										<span style="color: red;">*</span>
									</td>									
								</tr>
								<tr class="objectiveNum" <c:if test="${exerciseBatch.colType ne '1'}"> style="display: none"</c:if>>
									<td>客观题题数:</td>
									<td colspan="3"><input type="text" id="exerciseBatchEditForm_objectiveNum" name="objectiveNum" <c:if test="${exerciseBatch.colType eq '1'}"> class="required digits"</c:if> value="${exerciseBatch.objectiveNum }"/></td>
								</tr>
								</tr>	 --%>
									</table>
								</div>
								<div class="formBar">
									<%-- 
							<c:if test="${exerciseBatch.colType eq '2' }">
							<span class="tips">点击主观题序号可对题目内容进行修改</span>
							</c:if>
							 --%>
									<ul>
										<c:if
											test="${not empty exerciseBatch.resourceid and exerciseBatch.status eq 0 }">
											<li><div class="buttonActive">
													<div class="buttonContent">
														<button type="button"
															onclick="_publishExerciseBatch('${exerciseBatch.resourceid}');">立即发布作业</button>
													</div>
												</div></li>
											<li><div class="buttonActive">
													<div class="buttonContent">
														<button type="submit"
															onclick="javascript:$('#exerciseBatchEditForm_isPublished').val('Y');">稍后发布作业</button>
													</div>
												</div></li>
										</c:if>
										<c:if test="${exerciseBatch.status eq 0 }">
											<li><div class="buttonActive">
													<div class="buttonContent">
														<button type="submit"
															onclick="javascript:$('#exerciseBatchEditForm_isPublished').val('N');">保存</button>
													</div>
												</div></li>
										</c:if>
										<li><div class="button">
												<div class="buttonContent">
													<button type="button" class="close"
														onclick="navTab.closeCurrentTab();">取消</button>
												</div>
											</div></li>
									</ul>
								</div>
							</form>
							<c:if test="${not empty exerciseBatch.resourceid }">
								<gh:resAuth parentCode="RES_METARES_EXERCISE_UNACTIVE"
									pageType="exlist"></gh:resAuth>
								<table class="list" width="100%">
									<thead>
										<tr>
											<th width="5%"><input type="checkbox" name="checkall"
												id="check_all_exerciseBatch_exercise"
												onclick="checkboxAll('#check_all_exerciseBatch_exercise','resourceid','#exerciseBatch_exerciseBody')" /></th>
											<th width="5%">排序号</th>
											<th>问题与答案</th>
											<c:if test="${exerciseBatch.colType eq '2' }">
												<th width="8%">编辑</th>
											</c:if>
										</tr>
									</thead>
									<tbody id="exerciseBatch_exerciseBody">
										<c:forEach items="${exerciseBatch.exercises }" var="exercise"
											varStatus="vs">
											<tr>
												<td><input type="checkbox" name="resourceid"
													value="${exercise.resourceid }" autocomplete="off" /></td>
												<td><c:choose>
														<c:when test="${exerciseBatch.colType eq '1' }">${exercise.showOrder }</c:when>
														<c:otherwise>
															<a
																href="${baseUrl}/edu3/learning/exercisebatch/exercise/list.html?colsId=${exercise.exerciseBatch.resourceid}&resourceid=${exercise.resourceid}"
																target="dialog"
																rel="RES_METARES_EXERCISE_UNACTIVE_EXERCISE"
																title="编辑主观题" width="800" height="600">
																${exercise.showOrder } </a>
														</c:otherwise>
													</c:choose></td>
												<td>
													<div>${exercise.courseExam.question }
														<c:if
															test="${exerciseBatch.colType eq '2' and exercise.limitNum gt 0 }">
															<span>(${exercise.limitNum }字以内)</span>
														</c:if>
													</div> <c:if test="${not empty exercise.attachs }">
														<div>
															<span style="font-weight: bold;">附件：</span>
															<c:forEach items="${exercise.attachs}" var="attach"
																varStatus="vs">
																<a onclick="downloadAttachFile('${attach.resourceid }')"
																	href="#">${attach.attName }&nbsp;</a>&nbsp;&nbsp;
										</c:forEach>
														</div>
													</c:if>
													<div>
														<span style="font-weight: bold;">参考答案：</span>${exercise.courseExam.answer }</div>
												</td>
												<c:if test="${exerciseBatch.colType eq '2' }">
													<td><a
														href="${baseUrl}/edu3/learning/exercisebatch/exercise/list.html?colsId=${exercise.exerciseBatch.resourceid}&resourceid=${exercise.resourceid}"
														target="dialog"
														rel="RES_METARES_EXERCISE_UNACTIVE_EXERCISE" title="编辑主观题"
														width="800" height="600"> 编辑 </a></td>
												</c:if>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:if>
						</div>

					</div>
					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
//立即发布作业
function _publishExerciseBatch(res){	
	alertMsg.confirm("您确定要发布这次作业吗？", {
		okCall: function(){		
			$.post("${baseUrl}/edu3/metares/exercise/unactiveexercise/status.html?type=1",{resourceid:res}, function (json){
				DWZ.ajaxDone(json);
				if (json.statusCode == 200){
					var form = _getPagerForm(navTab._getPanel('RES_METARES_EXERCISE_UNACTIVE'));
					if (form) {
						navTab._switchTab(navTab._indexTabId('RES_METARES_EXERCISE_UNACTIVE'));
						navTab.reload(form.action, $(form).serializeArray(),'RES_METARES_EXERCISE_UNACTIVE');	
					}
					navTab.closeTab('RES_METARES_EXERCISE_EXERCISEBATCH');
				}
			}, "json");
		}
	});		
}
//附件下载
function downloadAttachFile(attid){
		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
		var elemIF = document.createElement("iframe");  
		elemIF.src = url;  
		elemIF.style.display = "none";  
		document.body.appendChild(elemIF); 
}
</script>
</body>
</html>