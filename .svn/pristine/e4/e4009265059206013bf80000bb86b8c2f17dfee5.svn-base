<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生成绩</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${msg}";
		if(""!=msg){
			alertMsg.warn(msg);
		}
		$("#studentExamResults_isPrintNo").change(function(){
			if($(this).val()=='Y'){
				$("#studentExamResults_printNo").attr("readonly","");
			}else{
				$("#studentExamResults_printNo").attr("readonly","readonly");
			}
		});
	});
	
	
	//查看成绩
	function viewExamResultsByFlag(flag){
		var studentId = "${studentInfo.resourceid}";
		navTab.reload("${baseUrl}/edu3/teaching/result/view-student-examresults.html?studentId="+studentId+"&flag="+flag);
	}
	//打印成绩单
	function printPersonalReportCard(flag,printPage){
		
		var studentId = "${studentInfo.resourceid}";
		var printDate = $("#studentExamResults_printDate").val();
		var studyTime = $("#studentExamResults_studyTime").val();
			studyTime = encodeURIComponent(studyTime);
		var terms	  = 	[];
			$('input[name="person_term"]:checked').each(function(){    
	    		terms.push($(this).val());    
	        });
		//打印英文成绩单
		if("en"==flag){
			var url = "${baseUrl }/edu3/teaching/result/personalReportCard-print.html?flag="+flag+"&studentId="+studentId+"&printDate="+printDate;
			if(printDate==""||studyTime==""){
				var msg = "";
				if(printDate==""){
					msg     = "打印时间为空确认要打印吗？";
				}
				if(studyTime==""){
					msg     = "学习时间为空确认要打印吗？";
				}
				alertMsg.confirm(msg, {
	                okCall: function(){
						downloadFileByIframe(url,'studentEnReportCardDownloadExportIframe');
	                }
				});
			}else{
				downloadFileByIframe(url,'studentEnReportCardDownloadExportIframe');
			}
			
		//打印及格成绩单
		}else if("pass"==flag){
			
			var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?flag="+flag+"&studyTime="+studyTime+"&studentId="+studentId+"&printDate="+printDate;
			if(printDate==""||studyTime==""){
				var msg = "";
				if(printDate==""){
					msg     = "打印时间为空确认要打印吗？";
				}
				if(studyTime==""){
					msg     = "学习时间为空确认要打印吗？";
				}
				alertMsg.confirm(msg, {
	                okCall: function(){
	                	$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});	
	                }
				});
			}else{
				$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});	
			}
			
		//打印所有成绩单	
		}else{
			var name	  = $("#ep_name").val();
			var termNames = [];
	    	$('input[name="person_term"]:checked').each(function(){    
	    		termNames.push($(this).attr("title"));    
	        });
			var msg   = name+termNames+"的全部成绩";
			var url       = "${baseUrl}/edu3/teaching/result/personalReportCard-view.html?flag="+flag+"&studentId="+studentId+"&printDate="+printDate+"&terms="+terms+"&printPage="+printPage;
			if(printDate ==""){
				if(printDate==""){
					msg   = "打印时间为空,确认要打印"+msg+"吗？";
				}
				alertMsg.confirm(msg, {
	                okCall: function(){
	                	$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});	
	                }
				});
			}else{
				alertMsg.confirm("确认要打印"+msg+"吗？", {
	                okCall: function(){
	                	$.pdialog.open(url,"RES_TEACHING_PERSONAL_EXAMRESULTS_VIEW","打印预览",{width:800, height:600});	
	                }
				});
			}
		}
		
	}
	
	
	//导出学生成绩
	function exportExamResultsByFlag(flag){
		var studentId = "${studentInfo.resourceid}";
		var printDate = $("#studentExamResults_printDate").val();
		var studyTime = $("#studentExamResults_studyTime").val();
		var name	  = $("#ep_name").val();
			studyTime = encodeURIComponent(studyTime);
		var terms	  = 	[];
		$('input[name="person_term"]:checked').each(function(){    
    		terms.push($(this).val());    
        });
		var url       = "${baseUrl}/edu3/teaching/result/personalExportCard-view.html?flag="+flag+"&studentId="+studentId+"&terms="+terms;
		var termNames = [];
    	$('input[name="person_term"]:checked').each(function(){    
    		termNames.push($(this).attr("title"));    
        });
		
		
		var msg   = "确定要导出"+name+termNames+"的成绩吗？";
		alertMsg.confirm(msg, {
               okCall: function(){
            	   $('#frame_exportPersonalExamResults').remove();
	   				var iframe = document.createElement("iframe");
	   				iframe.id = "frame_exportPersonalExamResults";
	   				iframe.src = url;
	   				iframe.style.display = "none";
	   				//创建完成之后，添加到body中
	   				document.body.appendChild(iframe);
               }
		});

		
	}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<table width="100%">
				<tr height="30px">
					<td style="color: #183152"><strong> 考生号:</strong></td>
					<td>${studentInfo.enrolleeCode }</td>
					<td style="color: #183152"><strong>学号:</strong></td>
					<td>${studentInfo.studyNo }</td>
					<td style="color: #183152"><strong>姓名:</strong></td>
					<td>${studentInfo.studentName }<input id="ep_name"
						type="hidden" value="${studentInfo.studentName }"></td>
				</tr>
				<tr height="30px">
					<td style="color: #183152"><strong>年级: </strong></td>
					<td>${studentInfo.grade.gradeName }</td>
					<td style="color: #183152"><strong>专业:</strong></td>
					<td>${studentInfo.major.majorName}</td>
					<td style="color: #183152"><strong>层次: </strong></td>
					<td>${studentInfo.classic.classicName}</td>
				</tr>
				<tr height="30px">
					<td style="color: #183152"><strong>教学中心:</strong></td>
					<td>${studentInfo.branchSchool.unitName}</td>
					<td style="color: #183152"><strong>学习方式:</strong></td>
					<td>${ghfn:dictCode2Val('CodeLearningStyle',studentInfo.learningStyle) }</td>
					<td style="color: #183152"><strong>入学资格:</strong></td>
					<td>${studentInfo.grade.gradeName }</td>
				</tr>
				<tr height="30px">
					<td style="color: #183152"><strong>学习时间:</strong></td>
					<td><input id="studentExamResults_studyTime" name="studyTime"
						type="text" style="width: 50%; text-align: center;"
						value="${eduYear }">
					<%-- 例:<fmt:formatDate value="${studentInfo.grade.yearInfo.firstMondayOffirstTerm}" pattern="yyyy-MM"/>至--%>
					</td>
					<td style="color: #183152"><strong>打印时间:</strong></td>
					<td><input type="text" id="studentExamResults_printDate"
						name="printDate" style="width: 50%; text-align: center;"
						class="date1" onFocus="WdatePicker({isShowWeek:true})" /></td>
					<td style="color: #183152"><strong>打印编号:</strong></td>
					<td><select name="isPrintNo" id="studentExamResults_isPrintNo">
							<option value="N" selected="selected">否</option>
							<option value="Y">是</option>
					</select></td>
				</tr>
				<tr height="30px">
					<td style="color: #183152"><strong>编号: </strong></td>
					<td><input name="printNo" id="studentExamResults_printNo"
						type="text" readonly="readonly"
						value="${studentInfo.transcriptsNo}"
						style="width: 50%; text-align: center;"></td>
					<c:if test="${!empty persernal_terms}">
						<td><strong>学期</strong></td>
						<td colspan="3">${persernal_terms}</td>
					</c:if>
				</tr>
			</table>
		</div>
		<div class="panelBar">
			<!-- 		<ul class="toolBar"> -->
			<!-- 			<li><a class="icon" onclick="printPersonalReportCard('en')"><span> 打印英文成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="printPersonalReportCard('pass')"><span>打印及格成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="printPersonalReportCard('all')"><span>打印所有成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="viewExamResultsByFlag('pass')"><span>查看通过成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="viewExamResultsByFlag('required')"><span>查看必修课成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="viewExamResultsByFlag('all')"><span>查看所有课成绩</span></a></li> -->
			<!-- 			<li><a class="icon" onclick="exportExamResultsByFlag('all')"><span>导出成绩</span></a></li> -->
			<!-- 		</ul> -->
			<gh:resAuth parentCode="RES_TEACHING_RESULT_SEARCH_A"
				pageType="sublist"></gh:resAuth>
		</div>
		<div class="pageContent" layoutH="188">
			<input id="studentId" name="studentId" type="hidden"
				value="${studentInfo.resourceid}" />
			<table class="list" width="100%">
				<thead>
					<tr>
						<th width="3%" align="center">序号</th>
						<th width="8%">考试批次</th>
						<th width="12%">考试时间</th>
						<th width="10%">考试课程</th>
						<th width="8%">学时</th>
						<th width="8%">课程性质</th>
						<th width="8%">取得学分</th>
						<th width="8%">卷面成绩</th>
						<th width="8%">平时成绩</th>
						<th width="8%">综合成绩</th>
						<th width="8%">成绩状态</th>
						<!-- <th width="8%">选考次数</th> -->
						<th width="8%">考试类型</th>
						<th width="8%" nowrap="nowrap">备注</th>
					</tr>
				</thead>
				<tbody id="examResultsViewBody">
					<c:forEach items="${list}" var="examResults" varStatus="vs">
						<c:choose>
							<c:when test="${fn:length(list) == (vs.index+1)  }">
								<tr style="height: 50px">
									<td colspan="12">
										<table>
											<tr>
												<td style="text-align: right; color: #183152"><strong>合计
														：</strong></td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;取得总学分：</strong>${examResults.totalCredit}</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;取得本专业总学分：</strong>${examResults.totalMajorCredit }</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;必修总学分：</strong>${examResults.requiredCredit}</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;已修必修课：</strong>${examResults.compulsoryed}</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;选修总学分：</strong>${examResults.electiveCredit}</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;参考平均分：</strong>${examResults.avgScore}</td>
												<td style="text-align: left; color: #183152"><strong>&nbsp;最低毕业学分：</strong>${studentInfo.teachingPlan.minResult}</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>${vs.index+1 }</td>
									<td>${examResults.batchName }</td>
									<td><c:choose>
											<c:when test="${not empty examResults.passTime}">
												<fmt:formatDate value="${examResults.passTime}"
													pattern="yyyy-MM-dd" />
											</c:when>
											<c:otherwise>
												<fmt:formatDate value="${examResults.examStartTime}"
													pattern="yyyy-MM-dd" />
												<fmt:formatDate value="${examResults.examStartTime}"
													pattern="HH:mm" />-<fmt:formatDate
													value="${examResults.examEndTime}" pattern="HH:mm" />
											</c:otherwise>
										</c:choose></td>
									<td>${examResults.courseName }</td>
									<td>${examResults.stydyHour }</td>
									<c:choose>
										<c:when test="${not empty examResults.courseType }">
											<td>${examResults.courseType }</td>
										</c:when>
										<c:otherwise>
											<td>${ghfn:dictCode2Val('CodeCourseType',examResults.courseTypeCode)}</td>
										</c:otherwise>
									</c:choose>
									<td>${examResults.inCreditHour}</td>
									<td>${examResults.writtenScore}</td>
									<td>${examResults.usuallyScore}</td>
									<td><c:choose>
											<c:when test="${not empty examResults.examResultsChs}">
												${examResults.examResultsChs}
											</c:when>
											<c:otherwise>
												${examResults.integratedScore}
											</c:otherwise>
										</c:choose>
									</td>
									<c:choose>
										<c:when test="${examResults.checkStatusCode eq '4'}">
											<c:set var="c_l" value="green"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="c_l" value="red"></c:set>
										</c:otherwise>
									</c:choose>
									<td style="color: ${c_l}">${examResults.checkStatus}</td>
									<!-- <td>${examResults.examCount}</td> -->
									<td>${examResults.isdelayexam eq 'Y' ? "缓考" : ghfn:dictCode2Val('ExamResult',examResults.ismakeupexam) }
									</td>
									<td>${examResults.memo}</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>