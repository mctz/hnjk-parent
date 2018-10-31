<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期末机考考场状态列表</title>
<script type="text/javascript">
	jQuery(document).ready(function(){
		$("#finalexamRecruitExamLogs_examSubId").change(function(){
			var url 	  = "${baseUrl}/edu3/teaching/finalexam/machineexamname/list.html";
			var examSubId = $(this).val();
			if(null!=examSubId && ""!=examSubId){
				$.get(url,{examSubId:examSubId},function(myJSON){
					  	 var selectObj="<option value=''>请选择</option>";
					 	 for (var i = 0; i < myJSON.length; i++) {
					 	 	selectObj += '<option value="' + myJSON[i].key + '">' + myJSON[i].value + '</option>';    
							}
					 	 $("#finalexamRecruitExamLogs_machineExamName").html(selectObj);
				},"json");
			}
		});
	});	 		
	//重考
	function reloginRecruitExamLogs(){
		pageBarHandle("您确定要让这些考生重新登录吗？","${baseUrl}/edu3/teaching/finalexam/recruitexamlogs/relogin.html","#finalexam_recruitExamLogsBody");
	}
	//导出期末机考按排考生信息
	function machineExamStudentInfoExport(){
		var examSubId = $("#finalexamRecruitExamLogs_examSubId").val();
		var courseId  = $("#finalexamCourseExamLogs_courseId").val();
		if(examSubId==""){
			alertMsg.warn("请选择考试批次!");
			return false;
		} else {
			var url = "${baseUrl}/edu3/teaching/finalexam/examresult/export.html?"+$("#finalexamRecruitExamLogsForm").serialize();
			downloadFileByIframe(url,"finalexamRecruitExamLogs_Iframe");
		}	
	}
	
	//设置缺考
	function setAbsent(){
		var examSubId = $("#finalexamRecruitExamLogs_examSubId").val();
		var machineExamName = $("#finalexamRecruitExamLogs_machineExamName").val();
		//var isNeedDelay = $("#isNeedDelay").val();
		if(examSubId!=""&& machineExamName!=""){
				alertMsg.confirm("您确定要将该考试批次下的这些考生设置为缺考状态吗？", {
					okCall: function(){//执行			
						 $.ajax({
							   type: "POST",
							   url: "${baseUrl}/edu3/teaching/finalexam/examresult/setabsent.html", //+"&&isNeedDelay="+isNeedDelay,
							   data:{examSubId:examSubId,machineExamName:machineExamName},
							   dataType: "json",	
							   global:false,
							   success: function(json){	
								   if(json.statusCode==200){
									 alertMsg.correct(json.message);	 
								   }else if(json.statusCode==400){
									   alertMsg.warn('没有需要设置缺考的考生！');
								   }else if(json.statusCode==400){
									   alertMsg.warn('考试尚未完结，不允许进行本操作');
								   }
							   }
							}); 
					}
			});	
		}else{
			alertMsg.warn('考试批次和考场批次必须选择！');
		}
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="finalexamRecruitExamLogsForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/finalexam/recruitexamlogs/list.html"
				method="post">
				<input type="hidden" name="exportType" value="2">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${not isBrSchool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									id="finalexamRecruitExamLogs_brSchool" name="branchSchool"
									tabindex="1" defaultValue="${condition['branchSchool'] }"
									style="width:53%" displayType="code"></gh:brSchoolAutocomplete>
							</li>
						</c:if>
						<li><label>考试批次</label> <gh:selectModel
								id="finalexamRecruitExamLogs_examSubId" name='examSubId'
								bindValue='resourceid' displayValue='batchName'
								value="${condition['examSubId']}"
								modelClass='com.hnjk.edu.teaching.model.ExamSub'
								condition="batchType='exam'"
								orderBy='yearInfo.firstYear desc,term desc' style="width:55%" />
							<font color="red">*</font></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								id="finalexamCourseExamLogs_courseId" name="courseId"
								tabindex="1" value="${condition['courseId'] }"
								displayType="code" style="width:53%" /></li>

					</ul>
					<ul class="searchContent">
						<li><label>状态：</label> <select name="status"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['status'] eq -1 }">selected="selected"</c:if>>未登录</option>
								<option value="0"
									<c:if test="${condition['status'] eq 0 }">selected="selected"</c:if>>考试中</option>
								<option value="2"
									<c:if test="${condition['status'] eq 2 }">selected="selected"</c:if>>已交卷</option>
								<option value="1"
									<c:if test="${condition['status'] eq 1 }">selected="selected"</c:if>>退出</option>
								<option value="3"
									<c:if test="${condition['status'] eq 3 }">selected="selected"</c:if>>断线</option>
						</select></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" style="width: 53%" /></li>
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" style="width: 53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>考试场次：</label> <select
							id="finalexamRecruitExamLogs_machineExamName"
							name="machineExamName" style="width: 55%">
								<option value="">请选择</option>
								<c:forEach var="info" items="${machineExamNames }">
									<option value="${info.resourceid }"
										<c:if test="${info.resourceid eq condition['machineExamName'] }">selected="selected"</c:if>>
										<c:choose>
											<c:when test="${ not empty info.machineExamName }">${info.course.courseName}(${info.machineExamName })</c:when>
											<c:otherwise>
										${info.course.courseName}(<fmt:formatDate
													value="${info.examStartTime }"
													pattern="yyyy-MM-dd HH:mm:ss" /> 至 <fmt:formatDate
													value="${info.examEndTime }" pattern="yyyy-MM-dd HH:mm:ss" />)
									</c:otherwise>
										</c:choose>

									</option>
								</c:forEach>
						</select></li>
						<li><label>开始时间：</label> <input type="text"
							id="finalexamRecruitExamLogs_StartTime" name="startTime"
							value="${condition['startTime']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'finalexamRecruitExamLogs_EndTime\')}'})"
							style="width: 53%" /></li>
						<li><label>结束时间：</label> <input type="text"
							id="finalexamRecruitExamLogs_EndTime" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'finalexamRecruitExamLogs_StartTime\')}'})"
							style="width: 53%" /></li>
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
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_RECRUITEXAMLOGS1"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="183">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_finalexam_recruitExamLogs"
							onclick="checkboxAll('#check_all_finalexam_recruitExamLogs','resourceid','#finalexam_recruitExamLogsBody')" /></th>
						<th width="8%">考试批次</th>
						<th width="8%">教学站</th>
						<th width="8%">课程</th>
						<th width="8%">学号</th>
						<th width="6%">姓名</th>
						<th width="8%">应考时间</th>
						<th width="15%">实考时间</th>
						<th width="8%">登录时间</th>
						<th width="8%">退出时间</th>
						<th width="8%">登录IP</th>
						<th width="5%">当前状态</th>
						<th width="5%">最终状态</th>
					</tr>
				</thead>
				<tbody id="finalexam_recruitExamLogsBody">
					<c:forEach items="${recruitExamLogsPage.result}"
						var="recruitExamLog" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${recruitExamLog.resourceid }" autocomplete="off" /></td>
							<td>${recruitExamLog.examInfo.examSub.batchName }</td>
							<td>${recruitExamLog.studentInfo.branchSchool }</td>
							<td
								title="${recruitExamLog.examInfo.course.courseCode }-${recruitExamLog.examInfo.course.courseName }">${recruitExamLog.examInfo.course.courseCode }-${recruitExamLog.examInfo.course.courseName }</td>
							<td title="${recruitExamLog.studentInfo.studyNo }">${recruitExamLog.studentInfo.studyNo }</td>
							<td title="${recruitExamLog.studentInfo.studentName }">${recruitExamLog.studentInfo.studentName }</td>
							<td
								title="<fmt:formatDate value="${recruitExamLog.examInfo.examStartTime }" pattern="yyyy-MM-dd HH:mm:ss"/> - <fmt:formatDate value="${recruitExamLog.examInfo.examEndTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate
									value="${recruitExamLog.examInfo.examStartTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
									value="${recruitExamLog.examInfo.examEndTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${recruitExamLog.startTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
									value="${recruitExamLog.endTime }" pattern="HH:mm:ss" /></td>
							<td><fmt:formatDate value="${recruitExamLog.loginTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${recruitExamLog.logoutTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${recruitExamLog.loginIp }</td>
							<td><c:choose>
									<c:when test="${recruitExamLog.isExceptional eq 'Y' }">
										<span style="color: red;">断线</span>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${recruitExamLog.status eq -1 }">未登录</c:when>
											<c:when test="${recruitExamLog.status eq 0 }">
												<span style="color: green;">考试中</span>
											</c:when>
											<c:when test="${recruitExamLog.status eq 1 }">退出</c:when>
											<c:when test="${recruitExamLog.status eq 2 }">已交卷</c:when>
											<c:otherwise>&nbsp;</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose></td>
							<td>${recruitExamLog.finalStatus }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${recruitExamLogsPage}"
				goPageUrl="${baseUrl }/edu3/teaching/finalexam/recruitexamlogs/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>