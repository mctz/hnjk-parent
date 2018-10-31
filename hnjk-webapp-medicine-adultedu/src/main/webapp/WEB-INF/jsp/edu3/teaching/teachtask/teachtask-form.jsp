<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书</title>
<script type="text/javascript">
		function goSelectTeacher(type,idsN,namesN,title){
			$.pdialog.open("${baseUrl }/edu3/teaching/teachtask/teacher.html?idsN="+idsN+"&namesN="+namesN+"&type="+type,"selector","选择"+title,{mask:true,height:600,width:800});
		}
		function _addTeachTaskDetailTT(){
			var teachTaskId = $('#_teachTaskIdTT').val();
			$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachtask/detail/input.html?from=task&teachTaskId='+teachTaskId,
					'teachTaskDetailTT','新增教学任务',{width:600,height:400});
		}
		function _editTeachTaskDetailTT(){
			var teachTaskId = $('#_teachTaskIdTT').val();
			if(isCheckOnlyone('resid','#_teachTaskDetailBodyTT')){				
				$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachtask/detail/input.html?from=task&teachTaskId='+teachTaskId+"&resourceid="+$("#_teachTaskDetailBodyTT input[@name='resid']:checked").val(),
						'teachTaskDetailTT','编辑教学任务',{width:600,height:400});	
			}				
		}
		function _deleteTeachTaskDetailTT(){
			if(!isChecked('resid','#_teachTaskDetailBodyTT')){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 		}
			alertMsg.confirm("您确定要删除这些记录吗？", {
					okCall: function(){//执行		
						var teachTaskId = $('#_teachTaskIdTT').val();
						var res = "";
						var k = 0;
						var num  = $("#_teachTaskDetailBodyTT input[name='resid']:checked").size();
						$("#_teachTaskDetailBodyTT input[@name='resid']:checked").each(function(){
		                        res+=$(this).val();
		                        if(k != num -1 ) res += ",";
		                        k++;
		                    })
		                
						$.post('${baseUrl}/edu3/framework/teaching/teachtask/detail/delete.html',{resourceid:res,teachTaskId:teachTaskId,from:'task'}, _teachTaskDetailTTNavTabAjaxDone, "json");
					}
			});	
		}
		
		function _teachTaskDetailTTNavTabAjaxDone(json){
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				if ("forward" == json.callbackType) {
					navTab.reload(json.forward);
				}
			}
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">分配任务</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachtask/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs"
						<c:if test="${currentIndex==1 }">currentIndex=${currentIndex }</c:if>>
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>任务书</span></a></li>
									<li class="selected"><a href="#"><span>教学任务安排</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 1 -->
							<div>
								<div class="pageFormContent">
									<input type="hidden" name="resourceid"
										value="${teachTask.resourceid }" /> <input type="hidden"
										name="taskStatus" value="${teachTask.taskStatus }" /> <input
										type="hidden" name="realReturnTime"
										value="<fmt:formatDate value='${teachTask.realReturnTime }' pattern='yyyy-MM-dd'/>" />
									<input type="hidden" name="auditMan"
										value="${teachTask.auditMan }" /> <input type="hidden"
										name="auditManId" value="${teachTask.auditManId }" /> <input
										type="hidden" name="isTemplate"
										value="${teachTask.isTemplate }" /> <input type="hidden"
										name="auditDate"
										value="<fmt:formatDate value='${teachTask.auditDate }' pattern='yyyy-MM-dd'/>" />
									<table class="form">
										<tr>
											<td width="20%">年度:</td>
											<td width="30%"><gh:selectModel name="yearInfoid"
													bindValue="resourceid" displayValue="yearName"
													style="width:60%" disabled="true"
													modelClass="com.hnjk.edu.basedata.model.YearInfo"
													value="${teachTask.yearInfo.resourceid}"
													classCss="required" /></td>
											<td width="20%">学期:</td>
											<td width="30%"><gh:select name="term"
													value="${teachTask.term}" dictionaryCode="CodeTerm"
													style="width:60%" classCss="required" disabled="true" /></td>
										</tr>
										<tr>
											<td width="20%">课程:</td>
											<td width="30%"><gh:selectModel name='courseid'
													bindValue='resourceid' displayValue='courseName'
													style='width:60%'
													modelClass='com.hnjk.edu.basedata.model.Course'
													value="${teachTask.course.resourceid}" disabled="true"
													classCss="required" /></td>
											<td>返回时限:</td>
											<td><input type="text" id="returnTime" name="returnTime"
												value="<fmt:formatDate value='${teachTask.returnTime}' pattern='yyyy-MM-dd'/>"
												style="width: 58%" class="Wdate"
												onFocus="WdatePicker({isShowWeek:true})"></td>
										</tr>
										<tr>
											<td>主讲老师:</td>
											<td colspan="3"><input type="text" name="teacherName"
												value="${teachTask.teacherName }" id="teacherName"
												readonly="readonly" /> <input type="hidden"
												name="teacherId" id="teacherId"
												value="${teachTask.teacherId }" /> <a href="javascript:;"
												class="button"
												onclick="goSelectTeacher('0','teacherId','teacherName','主讲老师');"><span>主讲老师</span></a>
											</td>
										</tr>
										<tr>
											<td>辅导老师:</td>
											<td colspan="3"><input type="text" name="assistantNames"
												value="${teachTask.assistantNames }" id="assistantNames"
												readonly="readonly" style="width: 75%;" /> <input
												type="hidden" name="assistantIds" id="assistantIds"
												value="${teachTask.assistantIds }" /> <a
												href="javascript:;" class="button"
												onclick="goSelectTeacher('2','assistantIds','assistantNames','辅导老师');"><span>辅导老师</span></a>
											</td>
										</tr>
										<tr>
											<td>教学任务建议:</td>
											<td colspan="3"><textarea name="taskAdvise" rows="25"
													cols="" style="width: 90%">${teachTask.taskAdvise }</textarea>
											</td>
										</tr>
										<tr>
											<td>备注:</td>
											<td colspan="3"><textarea name="memo" rows="5" cols=""
													style="width: 90%">${teachTask.memo }</textarea></td>
										</tr>
									</table>
								</div>
							</div>
							<!-- 2 -->
							<div>
								<div class="pageContent">
									<span class="buttonActive"><div class="buttonContent">
											<button type="button" onclick="_addTeachTaskDetailTT()">增加任务</button>
										</div></span> <span class="buttonActive"><div class="buttonContent">
											<button type="button" onclick="_editTeachTaskDetailTT()">编辑任务</button>
										</div></span> <span class="buttonActive"><div class="buttonContent">
											<button type="button" onclick="_deleteTeachTaskDetailTT()">删除任务</button>
										</div></span>
									<table class="form">
										<input type="hidden" id="_teachTaskIdTT"
											value="${teachTask.resourceid }" />
										<tbody id="_teachTaskDetailBodyTT">
											<tr>
												<td style="width: 3%"><input type="checkbox"
													name="checkall" id="check_all__teachTaskDetailTT"
													onclick="checkboxAll('#check_all__teachTaskDetailTT','resid','#_teachTaskDetailBodyTT')" /></td>
												<td style="width: 3%">序号</td>
												<td style="width: 8%">教学活动</td>
												<td style="width: 31%">教学任务内容</td>
												<td style="width: 10%">开始时间</td>
												<td style="width: 10%">结束时间</td>
												<td style="width: 8%">预警时间</td>
												<td style="width: 5%">任务所有者</td>
												<td style="width: 10%">参与者</td>
												<td style="width: 5%">是否可修改</td>
												<td style="width: 5%">状态</td>
											</tr>
											<c:forEach items="${teachTask.teachTaskDetails }" var="c"
												varStatus="vs">
												<tr>
													<td><input type='checkbox' name='resid'
														value='${c.resourceid }' autocomplete="off"></td>
													<td>${c.showOrder }</td>
													<td>${ghfn:dictCode2Val('CodeTaskType',c.taskType) }</td>
													<td><div>${c.taskContent }</div></td>
													<td><fmt:formatDate value="${c.startTime }"
															pattern='yyyy-MM-dd HH:mm:ss' /></td>
													<td><fmt:formatDate value="${c.endTime }"
															pattern='yyyy-MM-dd HH:mm:ss' /></td>
													<td><fmt:formatDate value="${c.warningTime }"
															pattern='yyyy-MM-dd HH:mm:ss' /></td>
													<td>${c.ownerName }</td>
													<td>${c.participantsName }</td>
													<td>${ghfn:dictCode2Val('yesOrNo',c.isAllowModify) }</td>
													<td>${ghfn:dictCode2Val('CodeTeachTaskStatus',c.status) }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>