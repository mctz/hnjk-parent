<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书</title>
<script type="text/javascript">
		function _addTeachTaskDetail(){
			var teachTaskId = $('#_teachTaskId').val();
			$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachtask/detail/input.html?teachTaskId='+teachTaskId,
					'teachTaskDetail','新增教学任务',{width:600,height:400});
		}
		function _editTeachTaskDetail(){
			var teachTaskId = $('#_teachTaskId').val();
			if(isCheckOnlyone('resourceid','#_teachTaskDetailBody')){				
				$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachtask/detail/input.html?teachTaskId='+teachTaskId+"&resourceid="+$("#_teachTaskDetailBody input[@name='resourceid']:checked").val(),
						'teachTaskDetail','编辑教学任务',{width:600,height:400});	
			}				
		}
		function _deleteTeachTaskDetail(){
			if(!isChecked('resourceid','#_teachTaskDetailBody')){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 		}
			if($("#_teachTaskDetailBody input[@name='resourceid'][rel='N']:checked").size()>0){
	 			alertMsg.warn('您只能删除自己增加的教学任务！');
				return false;
	 		}	 		
			alertMsg.confirm("您确定要删除这些记录吗？", {
					okCall: function(){//执行		
						var teachTaskId = $('#_teachTaskId').val();
						var res = "";
						var k = 0;
						var num  = $("#_teachTaskDetailBody input[name='resourceid']:checked").size();
						$("#_teachTaskDetailBody input[@name='resourceid']:checked").each(function(){
		                        res+=$(this).val();
		                        if(k != num -1 ) res += ",";
		                        k++;
		                    })
		                
						$.post('${baseUrl}/edu3/framework/teaching/teachtask/detail/delete.html',{resourceid:res,teachTaskId:teachTaskId}, _teachTaskDetailNavTabAjaxDone, "json");
					}
			});	
		}
		
		function _teachTaskDetailNavTabAjaxDone(json){
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
	<h2 class="contentTitle">教学任务书</h2>
	<div class="page">
		<div class="pageContent">
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
							<table class="form">
								<tr>
									<td width="10%">年度</td>
									<td width="40%">${teachTask.yearInfo}</td>
									<td width="10%">学期</td>
									<td width="40%">${ghfn:dictCode2Val('CodeTerm',teachTask.term) }</td>
								</tr>
								<tr>
									<td>课程</td>
									<td>${teachTask.course.courseName}</td>
									<td>返回时限</td>
									<td><fmt:formatDate value='${teachTask.returnTime}'
											pattern='yyyy-MM-dd' /></td>
								</tr>
								<tr>
									<td>主讲老师</td>
									<td>${teachTask.teacherName }</td>
									<td>辅导老师</td>
									<td>${teachTask.assistantNames }</td>
								</tr>
								<tr>
									<td>备注</td>
									<td colspan="3">${teachTask.memo }</td>
								</tr>
								<tr>
									<td>教学任务建议</td>
									<td colspan="3">${teachTask.taskAdvise }</td>
								</tr>
							</table>
						</div>
						<!-- 2 -->
						<div>
							<div class="pageContent">
								<span class="buttonActive"><div class="buttonContent">
										<button type="button" onclick="_addTeachTaskDetail()">增加任务</button>
									</div></span> <span class="buttonActive"><div class="buttonContent">
										<button type="button" onclick="_editTeachTaskDetail()">编辑任务</button>
									</div></span> <span class="buttonActive"><div class="buttonContent">
										<button type="button" onclick="_deleteTeachTaskDetail()">删除任务</button>
									</div></span>
								<table class="form">
									<input type="hidden" id="_teachTaskId" name="teachTaskId"
										value="${teachTask.resourceid }" />
									<tbody id="_teachTaskDetailBody">
										<tr>
											<td style="width: 3%"><input type="checkbox"
												name="checkall" id="check_all__teachTaskDetail"
												onclick="checkboxAll('#check_all__teachTaskDetail','resourceid','#_teachTaskDetailBody')" /></td>
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
												<td><input type='checkbox' name='resourceid'
													value='${c.resourceid }' rel="${c.isCustomTask }"
													showOrder="${c.showOrder }" autocomplete="off"></td>
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
							<%-- 
								<c:forEach items="${teachTask.teachTaskDetails }" var="c" varStatus="vs">
								<tr> 
									<td><q>${vs.index+1 }</q><input type='checkbox' name='c_id' value='${c.resourceid }'><input type="hidden" name='tk_id' value='${c.resourceid }'></td>
									<td><gh:select name='taskType' choose='N' dictionaryCode='CodeTaskType' classCss='required' style='width:80%' value='${c.taskType }'/><font color='red'>*</font></td>
									<td><textarea rows='5' name='taskContent' style='width:99%' class='required'>${c.taskContent }</textarea></td>
									<td><input type='text' name='beginTime' value='<fmt:formatDate value="${c.startTime }" pattern='yyyy-MM-dd HH:mm:ss'/>' style='width:99%' class='Wdate' class='required' onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
									<td><input type='text' name='endTime' value='<fmt:formatDate value="${c.endTime }" pattern='yyyy-MM-dd HH:mm:ss'/>' style='width:99%' class='Wdate' class='required' onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
									<td>
										<input type='hidden' id='ownerName${vs.index+1 }' name='ownerName' value="${c.ownerName }"/>
										<select name='ownerId' onchange="ownerChange(this,${vs.index+1 })">		
											<option value=''>请选择</option>																			
											<c:forEach items='${ids }' var='i'>
												<option value='${i }' ${fn:contains(c.ownerId,i)?'selected':'' }>${ghfn:ids2Names('user',i) }</option>
											</c:forEach>
										</select>
									</td>
									<td>
										<input type='hidden' id='participantsId${vs.index+1 }' name='participantsId' value="${c.participantsId }"/>
										<input type='hidden' id='participantsName${vs.index+1 }' name='participantsName' value="${c.participantsName }"/>
										<c:forEach items='${ids }' var='i'>
											<input type='checkbox' value='${i }' ${fn:contains(c.participantsId,i)?'checked':'' } onclick="checkTeachers(this,${vs.index+1 })" rel='${ghfn:ids2Names('user',i) }'/>${ghfn:ids2Names('user',i) }
										</c:forEach>
									</td>
									<td><gh:select name='status' dictionaryCode='CodeTeachTaskStatus' style='width:80%' value='${c.status }'/></td>
									<td><input type='text' name='showOrder' value='${c.showOrder }' style='width:90%' /></td>
								</tr>
								</c:forEach>
								--%>
						</div>
					</div>
					<div class="tabsFooter">
						<div class="tabsFooterContent"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">	 
/*
    function addTaskDetail11(){    	
    	var rowNum = jQuery("#td_Tab").get(0).rows.length-1;
    	var html = "<tr><td><q>"+rowNum+"</q><input type='checkbox' name='c_id' value=''><input type='hidden' name='tk_id' value=''></td>"+
    		"<td><gh:select name='taskType' choose='N' dictionaryCode='CodeTaskType' classCss='required' style='width:80%' value=''/><font color='red'>*</font></td>"+
    		"<td><textarea rows='5' name='taskContent' style='width:99%' class='required'></textarea></td>"+
    		"<td><input type='text' name='beginTime' value='' style='width:99%' class='Wdate' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\">"+
			"<td><input type='text' name='endTime' value='' style='width:99%' class='Wdate' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"></td>"+
			"<td><input type='hidden' id='ownerName"+rowNum+"' name='ownerName' value=''/><select name='ownerId' onchange='ownerChange(this,"+rowNum+")'><option value=''>请选择</option><c:forEach items='${ids}' var='i'><option value='${i }'>${ghfn:ids2Names('user',i) }</option></c:forEach></select></td>"+
			"<td><input type='hidden' id='participantsId"+rowNum+"' name='participantsId' value=''/><input type='hidden' id='participantsName"+rowNum+"' name='participantsName' value=''/>"+
			"<c:forEach items='${ids }' var='i'><input type='checkbox' value='${i }' onclick='checkTeachers(this,"+rowNum+")' rel='${ghfn:ids2Names('user',i) }'/>${ghfn:ids2Names('user',i) }</c:forEach></td>"+
			"<td><gh:select name='status' dictionaryCode='CodeTeachTaskStatus' style='width:80%' value=''/></td>"+			
			"<td><input type='text' name='showOrder' value='"+rowNum+"' style='width:90%' /></td></tr>";
		jQuery("#td_Tab").append(html);
    	generateIndex();
    }
    
    function delTaskDetail11(){
    	var ids = new Array();
    	$("#td_Tab input[name='c_id']:checked").each(function(ind){
		  	$(this).parent().parent().remove();
		  	ids.push($(this).val());
		});
		if(ids.length>0){
			var url = "${baseUrl}/edu3/teaching/teachtask/deleteDetail.html";
			$.get(url, {c_id:ids.toString()});
		}
		generateIndex();
    }
        
    function generateIndex(){
    	$("#td_Tab q").each(function(ind){    		
    		$(this).text(ind+1);   		
    		$(this).parent().parent().find("td input[name=participantsName]").attr("id","participantsName"+(ind+1));
    		$(this).parent().parent().find("td input[name=participantsId]").attr("id","participantsId"+(ind+1));
    		$(this).parent().parent().find("input[name=showOrder]").val(ind+1);
    		$(this).parent().parent().find("input[name='ownerName']").attr("id","ownerName"+(ind+1));    	
    	});
    }    
    function checkTeachers(obj,r,name){
    	var tid = $("#participantsId"+r);
    	var nid = $("#participantsName"+r);
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
    function ownerChange(obj,r){
    	$("#ownerName"+r).val($(obj).find("option:selected").text());
    }*/
</script>
</html>