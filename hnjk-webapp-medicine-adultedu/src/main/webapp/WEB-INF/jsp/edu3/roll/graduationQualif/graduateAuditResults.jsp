<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业审核结果</title>
<script type="text/javascript">
function confirmGraduateAudit(){
	alertMsg.confirm("确定将<font color='blue'>审核通过</font>的学生确认<font color='blue'>结业|毕业</font>么？<br>"+
			                     "确认之后，将无法再次查看此次审核结果， <br>"+
			                     "也无法再次导出审核结果。", {
		okCall: function(){
			doGraduateAudit();}
	}); 
}
function doGraduateAudit(){//确定毕业/结业
	var graduateDate = $('#graduateDate_ga').val();
	if(""==graduateDate){
		alertMsg.warn("请您设置<font color='blue'>毕业|结业</font>日期。");
		return false;
	}
	var branchSchool = $("#gAuditResult input[name='branchSchool']").val();
	var major		       = $("#gAuditResult input[name='major']").val();
	var classic			   = $("#gAuditResult input[name='classic']").val();
	var grade			   = $("#gAuditResult input[name='grade']").val();
	var name			   = $("#gAuditResult input[name='name']").val();
	var studyNo	       = $("#gAuditResult input[name='studyNo']").val();
	var studentstatus  = $("#gAuditResult input[name='studentstatus']").val();
	var stus  = $("#gAuditResult #stus").val();
	var isReachGraYear =$("#gAuditResult input[name='isReachGraYear']").val();
	var isPassEnter 		=$("#gAuditResult input[name='isPassEnter']").val();
	var isApplyDelay 		=$("#gAuditResult input[name='isApplyDelay']").val();
	var accoutstatus = $("#gAuditResult input[name='accoutstatus']").val();
	var stuFeeCondition 		=$("#gAuditResult input[name='stuFeeCondition']").val();
	var isSelectAll = $("input[name='isSelectAll'] ").val();
	name=encodeURIComponent(name);
	var postUrl="${baseUrl}/edu3/roll/graduateaudit/confirmGraduateAudit.html?stus="+stus+"&graduateDate="+graduateDate+
		"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&studentstatus="+studentstatus+"&isSelectAll="+isSelectAll
		+"&isReachGraYear="+isReachGraYear+"&isPassEnter="+isPassEnter+"&isApplyDelay="+isApplyDelay+"&accoutstatus="+accoutstatus+"&stuFeeCondition="+stuFeeCondition;
	$.ajax({
		type:"post",
		url:postUrl,
		dataType:"json",
		success:function(data){
			if(data['statusCode']==200){
				alertMsg.correct(data['message']);
			}else{
				alertMsg.warn(data['message']);
			}
			navTab.closeCurrentTab();
		}
	});
	
}
function exportGraduationAudit(){//导出审核结果(按查询条件导出)
	var branchSchool = $("#gAuditResult input[name='branchSchool']").val();
	var major		       = $("#gAuditResult input[name='major']").val();
	var classic			   = $("#gAuditResult input[name='classic']").val();
	var grade			   = $("#gAuditResult input[name='grade']").val();
	var name			   = $("#gAuditResult input[name='name']").val();
	var studyNo	       = $("#gAuditResult input[name='studyNo']").val();
	var studentstatus  = $("#gAuditResult input[name='studentstatus']").val();
	var stus  = $("#gAuditResult input[name='stus']").val();
	var isReachGraYear =$("#gAuditResult input[name='isReachGraYear']").val();
	var isPassEnter 		=$("#gAuditResult input[name='isPassEnter']").val();
	var isApplyDelay 		=$("#gAuditResult input[name='isApplyDelay']").val();
	
	var isSelectAll = $("#gAuditResult input[name='isSelectAll']").val();
	name=encodeURIComponent(name);
	var accoutstatus = $("#gAuditResult input[name='accoutstatus']").val();
	$('#frame_exportGraduateAuditResultList').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportGraduateAuditResultList";
	iframe.src = "${baseUrl }/edu3/schoolroll/graduation/student/exportExcel_new.html?exportType=condition&stus="+stus+"&branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&studentstatus="+studentstatus+"&isSelectAll="+isSelectAll
	+"&isReachGraYear="+isReachGraYear+"&isPassEnter="+isPassEnter+"&isApplyDelay="+isApplyDelay+"&accoutstatus="+accoutstatus;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	alertMsg.confirm("按查询条件导出毕业审核记录吗？",{ 
		okCall:function(){
		document.body.appendChild(iframe);
		}
	});
}

function viewStuInfo2(id){
	var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
	$.pdialog.open(url+'?resourceid='+id+"&type=graduateaudit", 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
}

//查看成绩不及格的课程
function viewExamResultsByFlag(stuid,name){
	//navTab.reload("${baseUrl}/edu3/teaching/result/view-student-examresults-graduate.html?studentId="+stuid);
	navTab.openTab("RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW34234","${baseUrl}/edu3/teaching/result/view-student-examresults-graduate.html?studentId="+stuid,  '查看'+name+'的不及格课程信息', {width: 800, height: 600});
}

// 查看未录入成绩的课程
function viewNoExamResult(studentNo, name){
	navTab.openTab("RES_SCHOOL_SCHOOLROLL_MANAGER_VIEWNOEXAMRESULT","${baseUrl}/edu3/teaching/result/view-student-noExamResultCourse.html?studentNo="+studentNo,  '查看'+name+'的未发布成绩课程信息', {mask: true,width: 800, height: 600});
}


</script>
</head>
<body>

	<h2 class="contentTitle">毕业审核结果</h2>
	<div class="page">
		<div id="gAuditResult">
			<input type="hidden" name="branchSchool" id="branchSchool"
				value="${condition['branchSchool']}" /> <input type="hidden"
				name="major" id="major" value="${condition['major']}" /> <input
				type="hidden" name="classic" id="classic"
				value="${condition['classic']}" /> <input type="hidden" name="grade"
				id="grade" value="${condition['grade']}" /> <input type="hidden"
				name="name" id="name" value="${condition['name']}" /> <input
				type="hidden" name="studyNo" id="studyNo"
				value="${condition['matriculateNoticeNo']}" /> <input type="hidden"
				name="studentstatus" id="studentstatus"
				value="${condition['stuStatus']}" /> <input type="hidden"
				name="stus" id="stus" value="${condition['stus']}" /> <input
				type="hidden" name="isReachGraYear" id="isReachGraYear"
				value="${condition['isReachGraYear']}" /> <input type="hidden"
				name="isPassEnter" id="isPassEnter"
				value="${condition['isPassEnter']}" /> <input type="hidden"
				name="isApplyDelay" id="isApplyDelay"
				value="${condition['isApplyDelay']}" /> <input type="hidden"
				name="isSelectAll" id="isSelectAll"
				value="${condition['isSelectAll']}" /> <input type="hidden"
				name="accoutstatus" id="accoutstatus"
				value="${condition['accoutstatus']}" /> <input type="hidden"
				name="stuFeeCondition" id="stuFeeCondition"
				value="${condition['stuFeeCondition']}" />
		</div>
		<div class="pageHeader">
			<ul>
				<li><label>设置毕业时间:</label><input type="text"
					id="graduateDate_ga" name="graduateDate_ga" class="Wdate"
					value="${condition['graduateDate']}"
					onfocus="WdatePicker({isShowWeek:true })" /></li>
			</ul>
		</div>
		<div class="pageContent">
			<div class="panelBar">
				<ul class="toolBar">
					<li><a class="icon" onclick="confirmGraduateAudit()" href="#"
						title="确定毕业"> <span>确定毕业</span>
					</a></li>
					<li><a class="icon" onclick="exportGraduationAudit()" href="#"
						title="导出审核结果"> <span>导出审核结果</span>
					</a></li>
					<li class="line">line</li>
				</ul>
			</div>
			<div class="pageFormContent" layoutH="97">
				<!-- 审核信息 -->
				<div id="main">
					<table class="table" layouth="168" style="width: 100%;">
						<thead>
							<tr>
								<th width="10%">学号</th>
								<th width="5%">姓名</th>
								<th width="10%">审核状态</th>
								<th width="55%">原因</th>
								<th width="10%">不及格</th>
								<th width="10%">未发布成绩</th>
							</tr>
						</thead>
						<tbody id="auditStusResults">
							<c:forEach items="${auditResults.result}" var="auditResult">
								<tr>
									<td rowspan="2" height="42px"
										style="vertical-align: middle; line-height: 42px;">${auditResult.accountStatus}</td>
									<td rowspan="2" height="42px"
										style="vertical-align: middle; line-height: 42px;">${auditResult.bloodType}</td>
									<td><c:if test="${auditResult.bornAddress=='1'}">[毕业: <font
												color="blue">审核通过 </font>]</c:if> <c:if
											test="${auditResult.bornAddress=='0'}">[毕业 : <font
												color="red">审核不通过</font>]</c:if></td>
									<td title="${fn:replace(auditResult.bornDay, ";", "&#13;&#10;")}">
										[毕业 : ${auditResult.bornDay} ]
									</td>
									<td title="查看该学生不及格的课程" rowspan="2" height="42px"
										style="vertical-align: middle; line-height: 42px;"><c:forEach
											items="${rtlist}" var="rtmap">
											<c:if test="${rtmap.key eq auditResult.accountStatus}">
													${rtmap.value}
												</c:if>
										</c:forEach> <a href="#"
										onclick="viewExamResultsByFlag('${auditResult.studentNo}','${auditResult.bloodType}')"
										style="color: blue;"> 查看详情 </a></td>
									<td title="查看该学生未发布成绩的课程" rowspan="2" height="42px"
										style="vertical-align: middle; line-height: 42px;"><c:forEach
											items="${noExamResultMap}" var="noEntry">
											<c:if test="${noEntry.key eq auditResult.accountStatus}">
													${noEntry.value}
												</c:if>
										</c:forEach> <a href="#"
										onclick="viewNoExamResult('${auditResult.accountStatus}','${auditResult.bloodType}')"
										style="color: blue;"> 查看详情 </a></td>
								</tr>
								<tr>
									<td><c:if test="${auditResult.BH=='1'}">[结业: <font
												color="blue">审核通过 </font>]</c:if> <c:if
											test="${auditResult.BH=='0'}">[结业: <font color="red">审核不通过</font>]</c:if>
									</td>
									<td title="${fn:replace(auditResult.CC,";", "&#13;&#10;")}">
										[结业: ${auditResult.CC} ]
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="panelBar">
					<gh:page page="${auditResults}" targetType="navTab"
						isShowPageSelector="N"
						goPageUrl="${baseUrl}/edu3/roll/graduateaudit/viaGraduate.html"
						pageType="sp_graduate" condition="${condition}" />
				</div>
			</div>
		</div>
	</div>

</body>

</html>