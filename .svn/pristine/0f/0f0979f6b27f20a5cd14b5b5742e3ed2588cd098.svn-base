<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生课后作业</title>
<style type="text/css">
#exercise_panelBarDiv .panelBar ul {
	float: right
}
</style>
<script type="text/javascript">	
	//平均分摊
	function avgStudentExercise(){
		if('${exerciseBatch.scoringType}'=='2'){
			alertMsg.warn("该次作业不是全体赋分！");
		} else {
			alertMsg.confirm("请输入您的评分：<input type='text' id='exerciseResultNum' value='0'/>",{
				okCall:function (){
					var exerciseResultNum = $.trim($("#exerciseResultNum").val());
					if(!exerciseResultNum.isNumber()){
						alertMsg.warn("请输入一个数字！");
						return;
					}
					var exerciseBatchId = $('input[name=exerciseBatchId]').val();
					var url = "${baseUrl}/edu3/teaching/exercisecheck/studentexercise/save.html";
					$.post(url,{exerciseBatchId:exerciseBatchId,avgResult:exerciseResultNum},function(json){
						validateFormCallback(json);			
					},'json');	
				}
			});
		}
	}
	function saveStudentExerciseResult(){
		var $form = $("#studentExerciseResultForm");
		if(!isChecked('resourceid',"#studentExerciseResultBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var isValid = true;
		$("#studentExerciseResultBody input[@name='resourceid']:checked").each(function(){
            var r = $.trim($("#result"+$(this).val()).val());
            if(r==""||!r.isNumber()){
            	isValid = false;
            	return;
            }
        });
        if(!isValid){
        	alertMsg.warn("请输入一个合法的成绩数值");
        	return false;
        } else {
        	$.ajax({
    			type:'POST',
    			url:$form.attr("action"),
    			data:$form.serializeArray(),
    			dataType:"json",
    			cache: false,
    			success: function (json){
    				DWZ.ajaxDone(json);
    				if (json.statusCode == 200){
    					navTabPageBreak();
    				}
    			},
    			
    			error: DWZ.ajaxError
    		});    		
        }
        return false;
	}
	
	//下载作业
	function downloadStudentExercise(){		
		if(isCheckOnlyone('resourceid','#studentExerciseResultBody')){
			var url = "${baseUrl}/edu3/teaching/exercisecheck/studentexercise/download.html?resourceid="+$("#studentExerciseResultBody input[@name='resourceid']:checked").val();
			downloadFileByIframe(url,'studentExerciseResultDownloadIframe');
		}	
	}
	//上传经典批改的附件
	function uploadStudentExerciese(){
		if(isCheckOnlyone('resourceid','#studentExerciseResultBody')){
			var url = "${baseUrl}/edu3/teaching/exercisecheck/studentexercise/upload.html?resourceid="+$("#studentExerciseResultBody input[@name='resourceid']:checked").val();
			$.pdialog.open(url, "studentExerciseResultUpload", "上传经典批改作业", {mask:true,width:600,height:400});
		}	
	}
	//撤销作业提交
	function cancelStudentExercise(){		
		if(!isChecked('resourceid',"#studentExerciseResultBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要撤销这些学生的作业提交吗?", {
				okCall: function(){		
					var batchid = $("#checkExerciseBatchId").val();
					var res = "";
					var k = 0;
					var num  = $("#studentExerciseResultBody input[name='resourceid']:checked").size();
					$("#studentExerciseResultBody input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).attr('rel');
	                        if(k != num -1 ) res += ",";
	                        k++;
	                });
	                
					$.post("${baseUrl}/edu3/teaching/studentexercise/cancel.html",{studentId:res,exerciseBatchId:batchid}, navTabAjaxDone, "json");
				}
		});	
		
	}
	
	function setcheckbox(data){
		$("#check"+data+":checkbox").attr("checked", true);
		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exercisecheck/studentexercise/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>学生姓名：</label><input type="text" name="studentName"
							value="${condition['studentName'] }" /> <input type="hidden"
							id="checkExerciseBatchId" name="exerciseBatchId"
							value="${condition['exerciseBatchId'] }" /> <input type="hidden"
							id="avgResult" value="0" /> <input type="hidden" name="courseId"
							value="${condition['courseId'] }" /></li>
						<li><label>学生号：</label><input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
						<li><label>状态：</label> <select name="status">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['status'] eq 1 }">selected="selected"</c:if>>待批改</option>
								<option value="2"
									<c:if test="${condition['status'] eq 2 }">selected="selected"</c:if>>已批改</option>
						</select></li>
					</ul>
					<div class="subBar">
						<ul>
							<c:if test="${condition['status'] ne 0 }">
								<li><span class="tips">点击学生姓名可查看学生作业</span></li>
							</c:if>
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

		<div class="pageContent">
			<c:choose>
				<c:when test="${hasCheckAuthority==true }">
					<c:choose>
						<c:when
							test="${exerciseBatch.colType eq '2' and condition['status'] ne 0 }">
							<div id="exercise_panelBarDiv">
								<gh:resAuth parentCode="RES_METARES_EXERCISE_UNACTIVE"
									pageType="sublist"></gh:resAuth>
							</div>
						</c:when>
						<c:when test="${condition['status'] eq 0 }">
							<gh:resAuth parentCode="RES_METARES_EXERCISE_UNACTIVE"
								pageType="dlist"></gh:resAuth>
						</c:when>
						<c:otherwise>
							<div id="exercise_panelBarDiv">
								<div class="panelBar">
									<ul class="toolBar">
										<li><a onclick="cancelStudentExercise()" href="#"
											class="icon" title="撤销作业提交"><span>撤销作业提交</span></a></li>
									</ul>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<div class='panelBar'></div>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${condition['status'] ne 0 }">
					<form onsubmit="return validateCallback(this);"
						id="studentExerciseResultForm"
						action="${baseUrl }/edu3/teaching/exercisecheck/studentexercise/batchsave.html"
						method="post">
						<table class="table" layouth="138">
							<thead>
								<tr>
									<th width="5%"><input type="checkbox" name="checkall"
										id="check_all_studentExerciseResult"
										onclick="checkboxAll('#check_all_studentExerciseResult','resourceid','#studentExerciseResultBody')" /></th>
									<th width="8%">年度</th>
									<th width="5%">学期</th>
									<th width="8%">课程</th>
									<th width="9%">作业名称</th>
									<th width="8%">截止日期</th>
									<th width="12%">学习中心</th>
									<th width="10%">学号</th>
									<th width="6%">姓名</th>
									<th width="6%">状态</th>
									<th width="8%">成绩</th>
									<%-- 
	               		<th width="6%">是否典型批改</th> 
	               		<th width="6%">是否优秀作业</th> 
	               		--%>
									<th width="15%">评语</th>
								</tr>
							</thead>
							<tbody id="studentExerciseResultBody">
								<c:forEach items="${exerciseListPage.result }" var="exercise"
									varStatus="vs">
									<tr>
										<td><input type="checkbox" name="resourceid"
											id="check${exercise.resourceid }"
											value="${exercise.resourceid }"
											rel="${exercise.studentInfo.resourceid }" autocomplete="off" /></td>
										<td>${exercise.exerciseBatch.yearInfo.yearName }</td>
										<td>${ghfn:dictCode2Val('CodeTerm',exercise.exerciseBatch.term) }</td>
										<td>${exercise.exerciseBatch.course.courseName }</td>
										<td>${exercise.exerciseBatch.colName }</td>
										<td><fmt:formatDate
												value="${exercise.exerciseBatch.endDate }"
												pattern="yyyy-MM-dd" /></td>
										<td>${exercise.studentInfo.branchSchool.unitName }</td>
										<td>${exercise.studentInfo.studyNo }</td>
										<td><a
											href="${baseUrl}/edu3/teaching/exercisecheck/studentexercise/check.html?studentExerciseId=${exercise.resourceid}"
											target="dialog" rel="_${exercise.studentInfo.resourceid}"
											title="${exercise.studentInfo.studentName }·${exercise.exerciseBatch.colName }"
											width="800" height="600">${exercise.studentInfo.studentName }</a></td>
										<td>${ghfn:dictCode2Val('CodeStudentExerciseStatus',exercise.status) }</td>
										<c:choose>
											<c:when test="${exercise.exerciseBatch.colType eq '1' }">
												<td><fmt:formatNumber value='${exercise.result }'
														pattern='###.#' /></td>
												<td>${ghfn:dictCode2Val('yesOrNO',exercise.isTypical) }</td>
												<td>${ghfn:dictCode2Val('yesOrNO',exercise.isExcell) }</td>
												<td>${exercise.teacherAdvise }</td>
											</c:when>
											<c:otherwise>
												<td><input type="text"
													id="result${exercise.resourceid }"
													name="result${exercise.resourceid }"
													onchange='setcheckbox("${exercise.resourceid }")'
													value="<fmt:formatNumber value='${exercise.result }' pattern='###.#' />"
													size="5" /></td>
												<%-- 
				   			<td><gh:select name="isTypical${exercise.resourceid }" dictionaryCode="yesOrNo"    value="${exercise.isTypical }"  onchange='setcheckbox("${exercise.resourceid }")' style="width:50px;"/></td>
				   			<td><gh:select name="isExcell${exercise.resourceid }" dictionaryCode="yesOrNo"  value="${exercise.isExcell }" onchange='setcheckbox("${exercise.resourceid }")' style="width:50px;"/></td>
				   			--%>
												<td><input type="text"
													name="teacherAdvise${exercise.resourceid }"
													value="${exercise.teacherAdvise }"
													onchange='setcheckbox("${exercise.resourceid }")'
													style="width: 95%;" /></td>
											</c:otherwise>
										</c:choose>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
				</c:when>
				<c:otherwise>
					<table class="table" layouth="138">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_studentExerciseResult"
									onclick="checkboxAll('#check_all_studentExerciseResult','resourceid','#studentExerciseResultBody')" /></th>
								<th width="10%">年度</th>
								<th width="5%">学期</th>
								<th width="10%">课程</th>
								<th width="10%">作业名称</th>
								<th width="10%">截止日期</th>
								<th width="10%">学习中心</th>
								<th width="10%">学号</th>
								<th width="10%">姓名</th>
								<th width="10%">移动电话</th>
								<th width="10%">Email</th>
							</tr>
						</thead>
						<tbody id="studentExerciseResultBody">
							<c:forEach items="${exerciseListPage.result }" var="plan"
								varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${plan.studentInfo.sysUser.username }"
										rel="${plan.studentInfo.sysUser.cnName }" autocomplete="off" /></td>
									<td>${exerciseBatch.yearInfo.yearName }</td>
									<td>${ghfn:dictCode2Val('CodeTerm',exerciseBatch.term) }</td>
									<td>${exerciseBatch.course.courseName }</td>
									<td>${exerciseBatch.colName }</td>
									<td><fmt:formatDate value="${exerciseBatch.endDate }"
											pattern="yyyy-MM-dd" /></td>
									<td>${plan.studentInfo.branchSchool.unitName }</td>
									<td>${plan.studentInfo.studyNo }</td>
									<td>${plan.studentInfo.studentName }</td>
									<td>${plan.studentInfo.studentBaseInfo.mobile }</td>
									<td>${plan.studentInfo.studentBaseInfo.email }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<script type="text/javascript"> 
				function remindStudent(){
					if(!isChecked("resourceid","#studentExerciseResultBody")){
			 			alertMsg.warn('请选择一条要操作记录！');
						return false;
				 	}
					alertMsg.confirm("您确定要给这些学生发温馨提示?", {
							okCall: function(){		
								var res = "";
								var name = "";
								var num  = $("#studentExerciseResultBody input[name='resourceid']:checked").size();
								$("#studentExerciseResultBody input[name='resourceid']:checked").each(function(){
				                        res+=$(this).val()+",";
				                        name+=$(this).attr('rel')+",";
				                 });
				                var msgContent = "您好！您的<b>${exerciseBatch.course.courseName}·${exerciseBatch.colName}</b>作业提交截止到<b><fmt:formatDate value='${exerciseBatch.endDate }' pattern='yyyy-MM-dd'/></b>，请尽快交作业。";
								var msgTitle = "${exerciseBatch.course.courseName}·${exerciseBatch.colName}作业提交提醒。";
				                $.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&msgType=tips&userNames='+res+'&cnNames='+encodeURIComponent(name)+'&msgContent='+encodeURIComponent(msgContent)+"&msgTitle="+encodeURIComponent(msgTitle),'selector','温馨提示(作业)',{mask:true,width:750,height:550});
							}
					});	
				}
			</script>
				</c:otherwise>
			</c:choose>

			<gh:page page="${exerciseListPage}"
				goPageUrl="${baseUrl}/edu3/teaching/exercisecheck/studentexercise/list.html"
				beforeForm="studentExerciseResultForm" postBeforeForm="Y"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>