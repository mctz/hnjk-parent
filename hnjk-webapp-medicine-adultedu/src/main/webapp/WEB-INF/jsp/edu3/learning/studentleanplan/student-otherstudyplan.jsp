<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的其他学习计划</title>
</head>
<body>
	<div class="page">
		<!-- -------------------------------------------学生学习计划 begin------------------------------------------- -->
		<table id="leanPlanTable" class="list" width="97%">
			<thead>
				<tr>
					<td colspan="10"><strong>学制：</strong>${teachingPlanList['planTitle'].eduYear }
						<strong>最低毕业学分：</strong>${teachingPlanList['planTitle'].minResult }
						<strong>已修总学分：</strong>${currentStudent.finishedCreditHour } <strong>已修必修课总学分：</strong>${currentStudent.finishedNecessCreditHour }
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>已修必修课数：</strong>${teachingPlanList['planTitle'].compulsoryed}
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
			</thead>
			<tbody id="tab">
				<tr>
					<th width="5%">序号</th>
					<th width="9%">课程类别</th>
					<th width="20%">课程名称</th>
					<th width="9%">开课学期</th>
					<th width="8%">学分</th>
					<th width="8%">总学时</th>
					<th width="8%">面授学时</th>
					<th width="8%">考试类别</th>
					<th width="8%">是否主干课</th>
					<th width="8%">是否学位课</th>
					<th width="8%">课程类型</th>
					<th width="9%">成绩</th>
				</tr>
				<c:forEach items="${teachingPlanList['planCourseList']}"
					var="course" varStatus="vs">
					<tr>
						<td>${course.teachingPlan_Course_num }</td>
						<td>${course.teachingPlan_Course_type }</td>
						<td><c:choose>
								<%-- 毕业状态，只给查看，不给操作 --%>
								<c:when test="${currentStudent.studentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt>-1 && course.teachingPlan_Course_teachType_code eq 'networkTeach' &&  course.teachingPlan_Course_type_code ne 'thesis' && course.teachingPlan_Course_teachType_code ne 'facestudy'}">
									<a href="javascript:void(0)"
										onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':''}')">${course.teachingPlan_Course_name }</a>
								</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code eq 'thesis' && course.teachingPlan_Course_teachType_code ne 'netsidestudy'}">
									<a rel="RES_TEACHING_THESIS_MSG" target="navTab" title="个人辅导"
										href="${baseUrl }/edu3/framework/graduateMsg/main.html">${course.teachingPlan_Course_name }</a>
								</c:when>
								<c:otherwise>
				      			${course.teachingPlan_Course_name }
				      		</c:otherwise>
							</c:choose></td>
						<td>${course.teachingPlan_Course_term }</td>
						<td>${course.teachingPlan_Course_creditHour }</td>
						<td>${course.teachingPlan_Course_studyHour}</td>
						<td>${course.teachingPlan_Course_faceStudyHour }</td>
						<td>${course.teachingPlan_Course_examClassType }</td>
						<td>${course.teachingPlan_Course_isMainCourse }</td>
						<td>${course.teachingPlan_Course_isDegreeCourse }</td>
						<td
							<c:if test="${course.teachingPlan_Course_teachType_code eq 'networkTeach' }"> style="color: blue;"</c:if>>${course.teachingPlan_Course_teachType_Name }</td>
						<td id="_student_course_score1"><a href='##'
							class="course_tips"
							title="卷面成绩：${fn:replace(course.teachingPlan_Course_WrittenScore,'-1.0','')}
				      <span style='padding-right:12px'></span>平时成绩：${fn:replace(course.teachingPlan_Course_UsuallyScore,'-1.0','') }"
							class='blue'
							<c:if test="${course.teachingPlan_Course_type_code eq 'thesis' and course.teachingPlan_Course_scoreStr eq '不及格' }">style="color:red;"</c:if>>
								${course.teachingPlan_Course_scoreStr}</a></td>
					</tr>
				</c:forEach>
				<!-- -------------------------------------------学生学习计划 end------------------------------------------- -->

				<!-- -------------------------------------------学生学习计划(计划外课程) begin------------------------------------------- -->
				<c:forEach items="${teachingPlanList['planOutCourseList']}"
					var="course" varStatus="vs">
					<tr>
						<td>${course.teachingPlan_Course_num }</td>
						<td style="color: blue">计划外</td>
						<td><c:choose>
								<c:when test="${currentStudent.studentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
								<c:when
									test="${course.teachingPlan_Course_learnStatusInt>-1 && course.teachingPlan_Course_teachType_code eq 'networkTeach' &&  course.teachingPlan_Course_type_code != 'thesis'}">
									<a href="javascript:void(0)"
										onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':''}')">${course.teachingPlan_Course_name }</a>
								</c:when>
								<c:otherwise>
				      			${course.teachingPlan_Course_name }
				      		</c:otherwise>
							</c:choose></td>
						<td>${course.teachingPlan_Course_term }</td>
						<td>${course.teachingPlan_Course_creditHour }</td>
						<td>${course.teachingPlan_Course_studyHour}</td>
						<td>${course.teachingPlan_Course_faceStudyHour }</td>
						<td>${course.teachingPlan_Course_examClassType }</td>
						<td>${course.teachingPlan_Course_isMainCourse }</td>
						<td>${course.teachingPlan_Course_isDegreeCourse }</td>
						<td
							<c:if test="${course.teachingPlan_Course_teachType_code eq 'networkTeach' }"> style="color: blue;"</c:if>>${course.teachingPlan_Course_teachType_Name }</td>
						<td id="_student_course_score1"><a href='##'
							class="course_tips"
							title="卷面成绩：${course.teachingPlan_Course_WrittenScore}<span style='padding-right:12px'></span>平时成绩：${course.teachingPlan_Course_UsuallyScore }"
							class='blue'> ${course.teachingPlan_Course_scoreStr}</a></td>
					</tr>
				</c:forEach>
				<!-- -------------------------------------------学生学习计划(计划外课程) end------------------------------------------- -->
			</tbody>
			<tfoot>
			</tfoot>
		</table>
	</div>
	<script type="text/javascript">
		var st = new Date();	
		$(document).ready(function(){
			if($('#_student_course_score1')){//学生成绩 tips
				$('.course_tips').tipsy({opacity:1.0,fade:true,html:true,gravity:'w'});
			}			
		});
		
		//预约毕业论文时先确认联系方式
		function thesisChangeTeachingPlanCourseStatus(status,courseId,planId,msg){
			if(status=='thesis'){
				changeTeachingPlanCourseStatus(status,courseId,planId,msg);
			}
		}
		//改变预约状态--计划内课程
		function changeTeachingPlanCourseStatus(status,courseId,planId,msg){	
			if(null==msg || ""==msg ||undefined==msg){
				msg = "确定要执行此操作吗？";
			}
			alertMsg.confirm(msg,{okCall:function(){
				var url = "${basePath}/edu3/framework/setStuTeachingPlanStatus.html?teachingPlanId="+planId+"&teachingPlanCourseId="+courseId+"&teachingPlanCourseStatus="+status;
				$.ajax({
					type:"post",
					url:url,
					dataType:"json",
					success:function(resultDate){	
					 	  
						 if(resultDate['operateError']==true){
					 			var msgList = resultDate['msg'];
								var msg = "";
								for(i=0;i<msgList.length;i++){
									msg+=(i+1)+"、"+msgList[i]+'\n';
								}
								alertMsg.warn(msg);
		
						 }else if(resultDate['operateType']=='orderCourse'){
							 	if(resultDate['orderCourseStatus']==false){
							 		var isNeedChooseCourse  = resultDate['isNeedChooseCourse'];
									var msgList 			= resultDate['msg'];
									var msg     			= "";
									
									for(i=0;i<msgList.length;i++){
										msg +="<strong>"+(i+1)+"、</strong>"+msgList[i]+'</br>';
									}
									//当有考试冲突时弹出确认框供用户选择是否需要调整预约课程
									if(true===isNeedChooseCourse){
										var conFlictCourseChooseURL = "${basePath}/edu3/teaching/courseorder/student-order-manager.html?operateType=orderCourse";
										
										alertMsg.confirm(msg+"</br>是否需要重新选择本次的预约课程?",{okCall:function(){
											navTab.openTab('RES_STUDENT_PLAN', conFlictCourseChooseURL, '调整我的学习计划');
										}});
										return false;
									}
									alertMsg.warn(msg);
								}else{
									
									 alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
									 window.location.href="${baseUrl}/edu3/framework/index.html";
			 
								}
						 }else if(resultDate['operateType']=='orderExam'){
							 	if(resultDate['orderExamStatus']==false){
									var msgList = resultDate['msg'];
									var isNeedChooseCourse  = resultDate['isNeedChooseCourse'];
									var msg = "";
									for(i=0;i<msgList.length;i++){
										msg+=(i+1)+"、"+msgList[i]+'</br>';
									}
									//当有考试冲突时弹出确认框供用户选择是否需要调整预约课程
									if(true===isNeedChooseCourse){
										var conFlictCourseChooseURL = "${basePath}/edu3/teaching/courseorder/student-order-manager.html?operateType=orderExam";
										alertMsg.confirm(msg+"</br>是否需要重新选择本次的考试预约?",{okCall:function(){
											navTab.openTab('RES_STUDENT_PLAN_EXAMORDER_CHOOSE', conFlictCourseChooseURL, '考试预约调整');
										}});
										return false;
									}
									alertMsg.warn(msg);
								}else{
									alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
									window.location.href="${baseUrl}/edu3/framework/index.html";	
								}
						 }else if(resultDate['operateType']=='orderGraduatePaper'){
							 
							 	if(resultDate['orderGraduatePaperStatus']==false){
									var msgList = resultDate['msg'];
									var msg = "";
									for(i=0;i<msgList.length;i++){
										msg+=(i+1)+"、"+msgList[i]+'</br>';
									}
									alertMsg.warn(msg);
								}else{
									alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
									window.location.href="${baseUrl}/edu3/framework/index.html";	
								}
						 }
					}
				});
			}});
		}
	
		//重考先检查是否沿用或重新累积平时分，再执行预约操作
		function changeReExamStudyStatus(isPlanOutCourse,courseId,msg,planIdOrStuId,statusOrType,tcourseId){			
			$.ajax({
		 		url: baseUrl+"/edu3/learning/interactive/course/hasresource/ajax.html",
		 		type: 'POST',
		 		async:false,
		 		dataType: 'json', 	
		 		data:{courseId:courseId,hasResource:'Y',isNeedReExamination:'Y',from:'exam'},//参数设置 	
		 		success: function(json){
		 			if(json.hasResource && json.hasResource=='Y' && json.isRedoCourseExam && json.isRedoCourseExam=='Y'&& json.isHasUsualResults &&json.isHasUsualResults=='Y'){
						var msgTip = "您这学期需要重考，";
						if(json.usualResults){
							msgTip += "上学期的平时成绩为"+json.usualResults+"分。";
						}
						msgTip += "您是否沿用上学期的平时成绩或重新累积平时成绩？";
						alertMsg.confirm2(msgTip,{
							okName: '沿用',
							okCall:function () {
								doSaveUsualResults('N',isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId);						
							},
							cancelName:'重新累积',
							cancelCall:function (){
								doSaveUsualResults('Y',isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId);						
							}
						});	
					} else {
						if(isPlanOutCourse == 'N'){
			 				changeTeachingPlanCourseStatus(statusOrType,tcourseId,planIdOrStuId,msg);
			 			} else {
			 				changePlanOutCourseStatus(courseId,planIdOrStuId,statusOrType,msg);
			 			}
					} 
		 		}
			});		
		}
		function doSaveUsualResults(isRedoCourseExam,isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId){
			$.ajax({
		 		url: baseUrl+"/edu3/learning/interactive/isredocourseexam/save.html",
		 		type: 'POST',
		 		async:false,
		 		dataType: 'json', 	
		 		data:{courseId:courseId,isRedoCourseExam:isRedoCourseExam},
		 		success: function(res){
		 			if(isPlanOutCourse == 'N'){
		 				changeTeachingPlanCourseStatus(statusOrType,tcourseId,planIdOrStuId,msg);
		 			} else {
		 				changePlanOutCourseStatus(courseId,planIdOrStuId,statusOrType,msg);
		 			}						 			
		 		}
			});	
		}
			
		//进入教学站
		function goInteractive(courseId,tCourseId,isNeedReExamination){			
			$.ajax({
		 		url: baseUrl+"/edu3/learning/interactive/course/hasresource/ajax.html",
		 		type: 'POST',
		 		async:false,
		 		dataType: 'json', 	
		 		data:{courseId:courseId,hasResource:'Y',isNeedReExamination:isNeedReExamination,from:'study'},//参数设置 	
		 		success: function(json){
		 			if(json.hasResource && json.hasResource=='N'){	
						 //alertMsg.warn("该课程暂时没有课件资源。<br/>您可以点击右上角反馈按钮进行反馈。");	
						alertMsg.confirm("这门课程没有网上课件资源，请根据教材及其他相关资料在线下学习！",{
							okName: '问题反馈',
							okCall:function () {
								navTab.openTab('RES_STUDENT_FEEDBACK_ADD', "${baseUrl}/edu3/student/feedback/input.html?courseId="+courseId, '新增反馈');
							}
						});	
					} else {
						window.open("${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId,"interactive");
					}
		 		}
			});		
		}
		function redoCourseExam(courseId,isRedoCourseExam){
			$.ajax({
		 		url: baseUrl+"/edu3/learning/interactive/isredocourseexam/save.html",
		 		type: 'POST',
		 		async:false,
		 		dataType: 'json', 	
		 		data:{courseId:courseId,isRedoCourseExam:isRedoCourseExam}, 	
		 		success: function(json){
		 			if(json.statusCode==200){
						window.open("${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId,"interactive");
					}	
		 		}
			});			
		}
	 </script>
</body>
</html>