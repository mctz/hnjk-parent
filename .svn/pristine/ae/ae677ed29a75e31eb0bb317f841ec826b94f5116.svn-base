<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>指导老师设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑指导老师</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduateMentor/save.html"
				id="_graduateMentorForm" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduate.resourceid }" /> <input type="hidden"
					id="_studentids" name="studentids" value="" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">毕业论文批次:</td>
							<td width="30%"><select name="examSubId" id="examSubId"
								size="1" class="required">
									<option value="">请选择</option>
									<c:forEach items="${examSubs }" var="examsub">
										<option value="${examsub.resourceid }"
											<c:if test="${examsub.resourceid eq  graduate.examSub.resourceid }"> selected</c:if>>${examsub.batchName }</option>
									</c:forEach>
							</select> <font color=red>*</font></td>
							<td width="20%">指导老师:</td>
							<td width="30%"><input type="text" id="guidTeacherName"
								name="guidTeacherName" style="width: 50%"
								value="${graduate.edumanager.cnName }" readonly="readonly"
								class="required" /> <span class="buttonActive"
								style="margin-left: 8px"><div class="buttonContent">
										<button type="button" onclick="chooseTeacher9()">选择导师</button>
									</div></span> <input type="hidden" id="guidTeacherId" name="guidTeacherId"
								value="${graduate.edumanager.resourceid }" /></td>
						</tr>
						<tr>
							<td>学生:</td>
							<td colspan="3"><c:choose>
									<c:when test="${not empty  graduate.resourceid}">
										<div id="displayName5">
											<span class="buttonActive" style="margin-left: 8px"><div
													class="buttonContent">
													<button type="button" onclick="chooseStudent6()">增加学生</button>
												</div></span> <span class="buttonActive" style="margin-left: 8px"><div
													class="buttonContent">
													<button type="button"
														onclick="deleteStu7('${graduate.resourceid }')">删除学生</button>
												</div></span>
										</div>
										<table id="disaplayStuDetails" class="list" width="100%">
											<tr>
												<th width="5%"><input type="checkbox" name="checkall"
													id="check_all_graduateMentor_details"
													onclick="checkboxAll('#check_all_graduateMentor_details','stuid','#graduaeMentorDetailsBody')" /></th>
												<th width="15%">学号</th>
												<th width="15%">姓名</th>
												<th width="25%">学校中心</th>
												<th width="10%">年级</th>
												<th width="20%">专业</th>
												<th width="10%">层次</th>
											</tr>
											<tbody id="graduaeMentorDetailsBody">
												<c:forEach var="d"
													items="${graduate.graduateMentorDetails }" varStatus="vs">
													<tr>
														<td><input type="checkbox" name="stuid"
															value="${d.resourceid}" autocomplete="off" /></td>
														<td>${d.studentInfo.studyNo }</td>
														<td>${d.studentInfo.studentName}</td>
														<td>${d.studentInfo.branchSchool}</td>
														<td>${d.studentInfo.grade }</td>
														<td>${d.studentInfo.major }</td>
														<td>${d.studentInfo.classic}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</c:when>
									<c:otherwise>
										<span class="tips">请先保存表单，再选择学生！</span>
									</c:otherwise>
								</c:choose></td>
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
	//function chooseExamSub7(){ //选择批次
	//	var url ="${baseUrl }/edu3/teaching/graduateNotice/chooseExam.html?type=notice&batchType=thesis";
	//   	$.pdialog.open(url,'_chooseExam','选择论文批次',{mask:true,height:600,width:800});
	//}
		
	function chooseTeacher9(){ //选择导师
		var url ="${baseUrl }/edu3/teaching/graduateNotice/chooseTeacher.html?type=notice&teacherType=ROLE_TEACHER_GRADUATE";
		
    	$.pdialog.open(url,'_chooseTeacher','选择指导老师',{mask:true,height:600,width:800});
	}
	
	function chooseStudent6(){ //选择学生
		if($('#_graduateMentorForm #examSubId').val() == ''){
			alertMsg.warn("请选择论文批次!");
			return false;
		}
		var url ="${baseUrl }/edu3/teaching/graduateMentor/chooseStudent.html?batchId="+$('#_graduateMentorForm #examSubId').val();
    	$.pdialog.open(url,'_chooseStu','选择学生',{mask:true,height:600,width:800});
	}
	
	function deleteStu7(id){		
		alertMsg.confirm("确定要删除这些学生吗？", {	okCall: function(){//执行
			var res = "";
			var k = 0;
			var num  = $("#graduaeMentorDetailsBody input[name='stuid']:checked").size();
			$("#graduaeMentorDetailsBody input[@name='stuid']:checked").each(function(){
                    res+=$(this).val();
                    if(k != num -1 ) res += ",";
                    k++;
                });                
			$.post("${baseUrl}/edu3/framework/teaching/graduateMentor/stu/delete.html", { detailId: res ,gid:id}, function(data){
				DWZ.ajaxDone(data);
				if (data.statusCode == 200){
					if (data.navTabId){
						navTab.reload(data.reloadTabUrl, {}, data.navTabId);
					}	
				}
			},"json");			
			}
		});	
	}
//-->
</script>
</html>