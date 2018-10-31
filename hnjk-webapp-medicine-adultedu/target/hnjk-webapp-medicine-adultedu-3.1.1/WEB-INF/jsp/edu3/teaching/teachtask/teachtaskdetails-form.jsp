<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书 - 任务书明细</title>
</head>
<body>
	<script type="text/javascript">
	function validateTeachTaskDetailCallback(form,dailogAjaxDone111){
		
		return validateCallback(form,dailogAjaxDone111);
	}
	function dailogAjaxDone111(json){
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){				
			if(json.reloadUrl){				
				navTab.reload(json.reloadUrl);
			}	
			if(json.closeCurrent){
				$.pdialog.closeCurrent();
			} else if (json.forward){
				$.pdialog.reload(json.forward);
			}
		}
	}

	</script>
	<h2 class="contentTitle">${teachTask.teacherName }-
		${teachTask.course.courseName } (${teachTask.yearInfo.yearName } -
		${ghfn:dictCode2Val('CodeTerm',teachTask.term) })</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/framework/teaching/teachtask/detail/save.html"
				class="pageForm"
				onsubmit="return validateTeachTaskDetailCallback(this,dailogAjaxDone111);">
				<input type="hidden" name="teachTaskId"
					value="${teachTask.resourceid }" /> <input type="hidden"
					name="resourceid" value="${teachTaskDetail.resourceid }" /> <input
					type="hidden" name="isCustomTask"
					value="${teachTaskDetail.isCustomTask }" /> <input type="hidden"
					name="status" value="${teachTaskDetail.status }" /> <input
					type="hidden" name="isAllowModify"
					value="${teachTaskDetail.isAllowModify }" /> <input type="hidden"
					name="from" value="${from }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<c:choose>
							<c:when
								test="${(from ne 'task') and (teachTaskDetail.isAllowModify eq 'N') }">
								<tr>
									<td>教学活动:</td>
									<td><input type='text' readonly="readonly"
										value="${ghfn:dictCode2Val('CodeTaskType',teachTaskDetail.taskType) }"
										style='width: 90%' /> <input type="hidden" name="taskType"
										value="${teachTaskDetail.taskType }" /></td>
									<td style="color: red;">预警时间:</td>
									<td><input type='text' name='warningTime'
										readonly="readonly"
										value='<fmt:formatDate value="${teachTaskDetail.warningTime}" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' id='teachTaskDetailwarningTime' /></td>
								</tr>
								<tr>
									<td width="15%">开始时间:</td>
									<td width="35%"><input type='text' name='startTime'
										readonly="readonly"
										value='<fmt:formatDate value="${teachTaskDetail.startTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' id='teachTaskDetailstarttime' /></td>
									<td width="15%">结束时间:</td>
									<td width="35%"><input type='text' name='endTime'
										readonly="readonly"
										value='<fmt:formatDate value="${teachTaskDetail.endTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' id='teachTaskDetailendtime' /></td>
								</tr>
								<tr>
									<td>教学任务内容</td>
									<td colspan="3"><textarea rows='5' readonly="readonly"
											name='taskContent' style='width: 98%' class='required'>${teachTaskDetail.taskContent }</textarea></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>教学活动:</td>
									<td><gh:select name='taskType'
											dictionaryCode='CodeTaskType' classCss='required'
											style='width:90%' value='${teachTaskDetail.taskType }' /> <font
										color='red'>*</font></td>
									<td style="color: red;">预警时间:</td>
									<td><input type='text' name='warningTime'
										value='<fmt:formatDate value="${teachTaskDetail.warningTime}" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' id='teachTaskDetailwarningTime'
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'teachTaskDetailstarttime\')||\'%y:%M:%d 00:00:00\'}',maxDate:'#F{$dp.$D(\'teachTaskDetailendtime\')}'})">
									</td>
								</tr>
								<tr>
									<td width="15%">开始时间:</td>
									<td width="35%"><input type='text' name='startTime'
										value='<fmt:formatDate value="${teachTaskDetail.startTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' class='Wdate required'
										id='teachTaskDetailstarttime'
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y:%M:%d 00:00:00',maxDate:'#F{$dp.$D(\'teachTaskDetailendtime\')}'})">
									</td>
									<td width="15%">结束时间:</td>
									<td width="35%"><input type='text' name='endTime'
										value='<fmt:formatDate value="${teachTaskDetail.endTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
										style='width: 95%' class='Wdate required'
										id='teachTaskDetailendtime'
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'teachTaskDetailstarttime\')||\'%y:%M:%d 00:00:00\'}'})">
									</td>
								</tr>
								<tr>
									<td>教学任务内容</td>
									<td colspan="3"><textarea rows='5' name='taskContent'
											style='width: 98%' class='required'>${teachTaskDetail.taskContent }</textarea></td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
							<td>任务所有者:</td>
							<td colspan="3"><input type='hidden' id="teachTaskOwnerName"
								name='ownerName' value="${teachTaskDetail.ownerName }" /> <select
								name='ownerId' onchange="teachTaskOwnerChange(this)"
								class="${(from ne 'task')?'required':''}">
									<option value=''>请选择</option>
									<c:forEach items='${ids }' var='i'>
										<option value='${i }'
											${fn:contains(teachTaskDetail.ownerId,i)?'selected':'' }>${ghfn:ids2Names('user',i) }</option>
									</c:forEach>
							</select> <c:if test="${from ne 'task' }">
									<span style="color: red;"></span>
								</c:if></td>
						</tr>
						<tr>
							<td>参与者:</td>
							<td colspan="3"><input type='hidden'
								id="teachTaskParticipantsId" name='participantsId'
								value="${teachTaskDetail.participantsId }" /> <input
								type='hidden' id='teachTaskParticipantsName'
								name="participantsName"
								value="${teachTaskDetail.participantsName }" /> <c:forEach
									items='${ids }' var='i'>
									<input type='checkbox' value='${i }'
										${fn:contains(teachTaskDetail.participantsId,i)?'checked':'' }
										onclick="checkTeachTaskTeachers(this)"
										rel="${ghfn:ids2Names('user',i) }" />${ghfn:ids2Names('user',i) }
						</c:forEach></td>
						</tr>
						<tr>
							<%-- 
					<td>状态:</td>
					<td><gh:select name='status' dictionaryCode='CodeTeachTaskStatus' value='${teachTaskDetail.status }'/></td>
					 --%>
							<td>排序号:</td>
							<td colspan="3"><input type='text' name='showOrder'
								value='${teachTaskDetail.showOrder }' size="5" class="digits" /></td>
						</tr>
					</table>
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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>

<script type="text/javascript">
function checkTeachTaskTeachers(obj){
	var tid = $("#teachTaskParticipantsId");
	var nid = $("#teachTaskParticipantsName");
	if(obj.checked){
		if(tid.val().indexOf(obj.value)<0){
			tid.val(tid.val()+obj.value+",");
			nid.val(nid.val()+$(obj).attr('rel')+",");
		}
	} else {
		if(tid.val().indexOf(obj.value)>=0){    			
			var ids = tid.val().replace((obj.value+","),"");
			var names = nid.val().replace(($(obj).attr('rel')+","),"");
			tid.val(ids);
			nid.val(names);
		}
	}
}
function teachTaskOwnerChange(obj){
	if($(obj).find("option:selected").val()!=""){
		$("#teachTaskOwnerName").val($(obj).find("option:selected").text());
	}else {
		$("#teachTaskOwnerName").val("");
	}	
}
</script>

</html>