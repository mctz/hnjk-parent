<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论文预约信息</title>
</head>
<body>
	<h2 class="contentTitle">编辑论文预约</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduatePapers/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduate.resourceid }" /> <input type="hidden"
					name="auditManId" value="${graduate.auditManId }" /> <input
					type="hidden" name="auditMan" value="${graduate.auditMan }" /> <input
					type="hidden" name="status" value="${graduate.status }" /> <input
					type="hidden" name="auditTime"
					value="<fmt:formatDate value="${graduate.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">毕业论文批次:</td>
							<td width="30%"><select name="examSubId" id="examSubId2"
								size="1" class="required">
									<option value="">请选择</option>
									<c:forEach items="${examSubs }" var="examsub">
										<option value="${examsub.resourceid }"
											<c:if test="${examsub.resourceid eq  graduate.examSub.resourceid }"> selected</c:if>>${examsub.batchName }</option>
									</c:forEach>
							</select> <font color=red>*</font></td>
							<td width="20%">导师:</td>
							<td width="30%"><input type="text" id="guidTeacherName2"
								name="guidTeacherName" style="width: 50%"
								value="${graduate.teacher.cnName }" readonly="readonly"
								class="required" /> <span class="buttonActive"
								style="margin-left: 8px"><div class="buttonContent">
										<button type="button" onclick="chooseTeacher90()">选择导师</button>
									</div></span> <input type="hidden" id="guidTeacherId2" name="guidTeacherId"
								value="${graduate.teacher.resourceid }" /></td>
						</tr>
						<tr>
							<td>学生姓名:</td>
							<td><input type="text" id="studentName" name="studentName"
								style="width: 50%" value="${graduate.studentInfo.studentName }"
								readonly="readonly" class="required" /> <c:if
									test="${ empty graduate.studentInfo.resourceid }">
									<span class="buttonActive" style="margin-left: 8px"><div
											class="buttonContent">
											<button type="button" onclick="chooseStudent85()">选择学生</button>
										</div></span>
								</c:if> <input type="hidden" id="studentId" name="studentId"
								value="${graduate.studentInfo.resourceid }" /></td>
							<td>学生学号:</td>
							<td><input type="text" id="studentNo4" style="width: 50%"
								value="${graduate.studentInfo.studyNo }" readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td>教学站:</td>
							<td><input type="text" id="stuCenter4" style="width: 50%"
								value="${graduate.studentInfo.branchSchool }"
								readonly="readonly" /></td>
							<td>年级:</td>
							<td><input type="text" id="grade4" style="width: 50%"
								value="${graduate.studentInfo.grade }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>专业:</td>
							<td><input type="text" id="major4" style="width: 50%"
								value="${graduate.studentInfo.major }" readonly="readonly" /></td>
							<td>层次:</td>
							<td><input type="text" id="classic4" style="width: 50%"
								value="${graduate.studentInfo.classic }" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>题目:</td>
							<td><input type="text" id="g_courseName" name="courseName"
								style="width: 50%" value="${graduate.course }"
								readonly="readonly" /> <input type="hidden" id="g_courseId"
								name="courseId" value="${graduate.course.resourceid }" /></td>
							<td>预约时间:</td>
							<td><input type="text" id="orderTime" name="orderTime"
								style="width: 50%"
								value="<fmt:formatDate value="${graduate.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>毕业论文环节:</td>
							<td><gh:select name="currentTache"
									dictionaryCode="CodeCurrentTache"
									value="${graduate.currentTache}" classCss="required"
									style="width:52%" /></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
<!--
	function chooseExamSub78(){ //选择批次
		var url ="${baseUrl }/edu3/teaching/graduateNotice/chooseExam.html?type=paper&batchType=thesis";
	   	$.pdialog.open(url,'chooseExam','选择论文批次',{mask:true,height:500,width:700});
	}
		
	function chooseTeacher90(){ //选择导师
		if($("#examSubId2").val()==""){alertMsg.warn('请选择论文批次！'); return false;};
		var url ="${baseUrl }/edu3/teaching/graduatePapers/chooseTeacher.html?batchId="+$("#examSubId2").val();
    	$.pdialog.open(url,'chooseTeacher','选择导师',{mask:true,height:500,width:700});
	}
	
	function chooseStudent85(){ //选择学生
		var url ="${baseUrl }/edu3/teaching/graduatePapers/chooseStudent.html";
    	$.pdialog.open(url,'chooseStu','选择学生',{mask:true,height:500,width:700});
	}
	
//-->
</script>
</html>