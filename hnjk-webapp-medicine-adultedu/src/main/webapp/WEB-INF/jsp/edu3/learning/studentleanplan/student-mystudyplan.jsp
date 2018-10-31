<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的学习计划</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div layoutH="1">
				<%-- 我的学习计划start --%>
				<div>
					<!-- 判断学生是否有教学计划 -->
					<c:choose>
						<c:when test="${noTeachingPlan eq 'Y'}">
							<div style="text-align: center; padding-bottom: 120px">
								<div class="tips">
									<b>您的学籍还未关联教学计划，请联系教务管理人员！</b>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="tabs" currentIndex="0" eventType="click">
								<div class="tabsHeader">
									<div class="tabsHeaderContent">
										<ul>
											<%-- 先输出默认学籍，其余的排在默认学籍后 --%>
											<c:forEach items="${studentInfoList }" var="stu">
												<c:choose>
													<c:when test="${defaultStudentId eq stu.resourceid}">
														<li class="selected"><a href="javascript:void(0)"><span>${stu.teachingPlan.major }-${stu.teachingPlan.classic }-${stu.grade }</span></a></li>
													</c:when>
													<c:otherwise>
														<%-- 
											<li ><a href="javascript:void(0)"><span>${stu.teachingPlan.major }-${stu.teachingPlan.classic }</span></a></li>
											 --%>
														<li><a
															href="${baseUrl }/edu3/student/otherStudyplan.html?studentId=${stu.resourceid}"
															class="j-ajax ajax-once"><span>${stu.teachingPlan.major.majorName }-${stu.teachingPlan.classic.classicName }-${stu.grade }</span></a></li>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</ul>
									</div>
								</div>

								<div class="tabsContent">
									<div>
										<table id="leanPlanTable" class="list" width="97%">
											<thead>
												<tr>
													<td colspan="10"><strong>学制：</strong>${teachingPlanList['planTitle'].eduYear }
														<strong>最低毕业学分：</strong>${teachingPlanList['planTitle'].minResult }
														<strong>已修总学分： <c:forEach
																items="${studentInfoList }" var="stu">
																<c:if test="${defaultStudentId eq stu.resourceid}">
											${teachingPlanList['planTitle'].totalScore}
										</c:if>
															</c:forEach>
													</strong> <strong>已修必修课总学分：</strong> <c:forEach
															items="${studentInfoList }" var="stu">
															<c:if test="${defaultStudentId eq stu.resourceid}">
											${stu.finishedNecessCreditHour }
										</c:if>
														</c:forEach> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>已修必修课数：</strong>${teachingPlanList['planTitle'].compulsoryed}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</td>
												</tr>
												<c:if test="${isDisplayScore eq 'N' }">
													<tr>
														<td colspan="12"><strong><span
																style="color: red;"> (提示：
																	按学校要求，未有缴费记录或未缴费，则不显示成绩。可以在【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况)</span></strong></td>
													</tr>
												</c:if>
												<%--
							    <tr>
							    	<td colspan="10">
							    	<c:set var="learning_process" value="${ teachingPlanList['planTitle'].totalScore /teachingPlanList['planTitle'].minResult }"></c:set>
							    		<c:if test="${learning_process >=1}">
							    			<c:set var="learning_process" value="1"/>													    			
							    	</c:if>													    			
							    	<table width="60%">
							    		<tr>
							    			<td width="20%">学习进度(<font color='green'><fmt:formatNumber type='percent' pattern='###%' value='${learning_process }'/></font>)：</td>
							    			
							    			<td><span class="active_optionbar"><span style="width: <fmt:formatNumber type='percent' pattern='###.##%' value='${learning_process }'/>;"></span></span>
							    			
							    			</td>
							    			
							    		</tr>
							    	</table>											    	
							    	
							    	</td>													    	
							    </tr> --%>
											</thead>
											<tbody id="tab">
												<%-- 
							    <tr>
							      <td colspan="11" style="background:#ff6600;color:#fff;font-weight: bold;">													     
							      <marquee direction=left scrollamount=3 onmouseover="this.stop()" onmouseout="this.start()">
							      	 <!-- 下面的foreach是学籍办提出在主页滚动条提示入学资格未通过的提示 -->
							      	 <c:forEach items="${studentInfoList }" var="stu">
										<c:choose>
											<c:when test="${defaultStudentId eq stu.resourceid and stu.enterAuditStatus=='N'}" >
												<font color="blue"><b>入学资格:不通过，请立即对专科证书验国证。</b></font>
											</c:when>
										</c:choose>
									</c:forEach>
							      	 <c:if test="${ not empty stuOrderTime['orderCourse']}">
								    	 ${stuOrderTime['orderCourse'].settingName } 预约学习时间是：
								    	  <fmt:formatDate value="${stuOrderTime['orderCourse'].startDate }" pattern="yyyy-MM-dd"/> 至
								    	  <fmt:formatDate value="${stuOrderTime['orderCourse'].endDate }"   pattern="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							    	 </c:if> 
							    	 <c:if test="${ not empty stuOrderTime['examSub']}">
								    	 ${stuOrderTime['examSub'].batchName } 预约考试时间是：
								    	 <fmt:formatDate value="${stuOrderTime['examSub'].startTime }" pattern="yyyy-MM-dd"/> 至 
								    	 <fmt:formatDate value="${stuOrderTime['examSub'].endTime }"  pattern="yyyy-MM-dd"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							    	 </c:if>
							    	 <c:if test="${ not empty stuOrderTime['thesisSub'] and fn:indexOf(teachingPlanList['planTitle'].teachingPlan_Name,'专科')<0 }">
								    	 ${stuOrderTime['thesisSub'].batchName } 预约毕业论文时间是：
								    	 <fmt:formatDate value="${stuOrderTime['thesisSub'].startTime }" pattern="yyyy-MM-dd"/> 至 
								    	 <fmt:formatDate value="${stuOrderTime['thesisSub'].endTime }" pattern="yyyy-MM-dd"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							     	 </c:if>
							      </marquee> 
							    </td>
							    </tr> --%>
												<!-- -------------------------------------------学生学习计划------------------------------------------- -->
												<%-- 
							    <tr>
							      <th width="3%">编号</th>
							      <th width="18%">课程名称</th>
							      <th width="8%">课程性质</th>
							      <th width="8%">教学方式</th>
							      <th width="8%">考试形式</th>
							      <th width="7%">学分</th>
							      <th width="8%">建议学期</th>
							      <th width="8%">状态</th>
							      <th width="8%">预约</th>
							      <th width="5%">成绩</th>
							      <th width="19%">考试时间</th>
							    </tr> --%>
												<tr>
													<th width="5%">序号</th>
													<th width="9%">课程类别</th>
													<th width="12%">课程名称</th>
													<th width="9%">开课学期</th>
													<th width="6%">学分</th>
													<th width="6%">总学时</th>
													<th width="6%">面授学时</th>
													<th width="6%">考试类别</th>
													<th width="8%">是否主干课</th>
													<th width="8%">是否学位课</th>
													<th width="8%">课程类型</th>
													<th width="9%">成绩</th>
													<th width="8%">网上学习成绩</th>
												</tr>
												<c:forEach items="${teachingPlanList['planCourseList']}"
													var="course" varStatus="vs">
													<tr>
														<td>${course.teachingPlan_Course_num }</td>
														<td>${course.teachingPlan_Course_type }</td>
														<td><c:choose>
																<%-- 毕业状态，只给查看，不给操作 --%>
																<c:when test="${defaultStudentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
																<%-- 								      		<c:when test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code ne 'thesis' && course.teachingPlan_Course_teachType_code ne 'facestudy'}"> --%>
																<c:when
																	test="${ course.isHasResource eq 'Y' && course.teachingPlan_Course_teachType_code eq 'networkTeach'}">
																	<a href="javascript:void(0)"
																		onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':''}','${course.teachingPlan_Course_term_code }')">${course.teachingPlan_Course_name }</a>
																</c:when>
																<c:when
																	test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code eq 'thesis' && course.teachingPlan_Course_teachType_code ne 'netsidestudy'}">
																	<a rel="RES_TEACHING_THESIS_MSG" target="navTab"
																		title="个人辅导"
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
																${course.teachingPlan_Course_scoreStr}</a> <%--
									      <c:choose>
									      	<c:when test="${not empty course.teachingPlan_Course_scoreStr }">
									      		${course.teachingPlan_Course_scoreStr}
									      	</c:when>
									      	<c:when test="${not empty course.teachingPlan_Course_score }">
									      		${course.teachingPlan_Course_score}
									      	</c:when>
									      </c:choose>
									      --%></td>
														<c:choose>
															<c:when
																test="${course.isHasResource=='Y' && course.teachingPlan_Course_teachType_code eq 'networkTeach'} }">
																<td style="cursor: pointer;"
																	onclick="viewStudentOnlineScore('${course.teachingPlan_Course_Id}','${currentStudent.grade.resourceid }','${currentStudent.branchSchool.resourceid }','${currentStudent.teachingPlan.resourceid }','${currentStudent.resourceid }','${course.course_Id }')"><a
																	href="javascript:void(0)">查看</a></td>
															</c:when>
															<c:otherwise>
																<td></td>
															</c:otherwise>
														</c:choose>
														<%-- 
								      <td id="_student_exam_time">
								      	   <c:choose>
								      	   		<c:when test="${ not empty course.teachingPlan_Course_examStartTime and not empty course.teachingPlan_Course_examEndTime and (course.teachingPlan_Course_examType_code eq '3' or course.teachingPlan_Course_examType_code eq '6')}">
								      	   			<a href="javascript:void(0)" class="_student_exam_time" title="${course.teachingPlan_Course_examStartTime}至${course.teachingPlan_Course_examEndTime }" > 查看考试时间</a>
								      	   		</c:when>
									      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and course.teachingPlan_Course_examEndTime != ''}">
									      			${course.teachingPlan_Course_examStartTime}-${course.teachingPlan_Course_examEndTime }
									      		</c:when>
									      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and empty course.teachingPlan_Course_examEndTime}">
									      			${course.teachingPlan_Course_examStartTime}
									      		</c:when>
									      		<c:otherwise>
									      			&nbsp;
									      		</c:otherwise>	
								     	   </c:choose>
								       </td>
								        --%>
													</tr>
												</c:forEach>
												<!-- -------------------------------------------学生学习计划------------------------------------------- -->
												<!-- -------------------------------------------学生学习计划(计划外课程)--不显示----------------------------------------- -->
												<%-- <c:forEach items="${teachingPlanList['planOutCourseList']}" var="course" varStatus="vs">
								    <tr>
								      <td>${course.teachingPlan_Course_num }</td>
								       <td style="color: blue">计划外</td>
								      <td>
								      	<c:choose>
								      		<c:when test="${defaultStudentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
								      		<c:when test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code != 'thesis'}">
								      			<a href="javascript:void(0)" onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':''}')">${course.teachingPlan_Course_name }</a>
								      		</c:when>
								      		<c:otherwise>
								      			${course.teachingPlan_Course_name }
								      		</c:otherwise>
								      	</c:choose>
								      </td>	
								      <td>${course.teachingPlan_Course_term }</td>	
								      <td>${course.teachingPlan_Course_creditHour }</td>
								      <td>${course.teachingPlan_Course_studyHour}</td>
								      <td>${course.teachingPlan_Course_faceStudyHour }</td>												      
								      <td>${course.teachingPlan_Course_examClassType }</td>
								      <td>${course.teachingPlan_Course_isMainCourse }</td>
								      <td>${course.teachingPlan_Course_isDegreeCourse }</td>
								      		      
								     学习状态 
								      <c:choose>
								      		<c:when test="${course.teachingPlan_Course_learnStatus eq '正在学习' }"><td><font color='green'>${course.teachingPlan_Course_learnStatus}</font></td></c:when>
								      		<c:when test="${course.teachingPlan_Course_learnStatus eq '已预约考试' }"><td><font color='red'>${course.teachingPlan_Course_learnStatus}</font></td></c:when>
								      		<c:otherwise><td>${course.teachingPlan_Course_learnStatus}</td></c:otherwise>
								      </c:choose>
								      学习状态
								      
								      预约状态 
								      <c:choose>  
								      		<c:when test="${defaultStudentStatus eq '16' }"><td > ${course.teachingPlan_Course_bookingStatus}</td></c:when> 
									      	--%>
												<%--预约考试  
									      	<c:when test="${course.teachingPlan_Course_learnStatusInt ==2 && course.teachingPlan_Course_learnStatus eq '已预约学习' }">
									      		<td><a href='javascript:void(0)' style='color:Blue;font-weight:normal;font-size:10pt' onclick="changePlanOutCourseStatus('${course.course_Id}','${defaultStudentId}','orderExam','确定要预约此科目的考试吗？')">${course.teachingPlan_Course_bookingStatus}</a></td>
									      	</c:when>
									      	预约考试 
									      	
									      	重考 
									   		<c:when test="${course.teachingPlan_Course_learnStatusInt ==2 && course.isNeedReExamination eq 'Y' }">
									      		<td><a href='javascript:void(0)' style='color:Blue;font-weight:normal;font-size:10pt' onclick="changeReExamStudyStatus('Y','${course.course_Id}','确定要重考此科目吗？','${defaultStudentId}','orderExam')">${course.teachingPlan_Course_bookingStatus}</a></td>
									      	</c:when>
									      	重考
									  
									      	重修  
									      	<c:when test="${course.teachingPlan_Course_learnStatusInt ==3 &&  course.teachingPlan_Course_bookingStatus ne '成绩录入中'}">															 
									      		<td title="点击重修此科目"> <a href='javascript:void(0)' onclick="changeReExamStudyStatus('Y','${course.course_Id}','确定要重修此科目吗？','${defaultStudentId}','orderExam')"> ${course.teachingPlan_Course_bookingStatus} </a></td>
									      	</c:when>
									        重修  
									      	<c:otherwise>
									      		<td > ${course.teachingPlan_Course_bookingStatus}</td>
									      	</c:otherwise>
								      </c:choose>
								      预约状态											      
								       <td  id="_student_course_score1">														     
								     	 <a href='##' class="course_tips" title="卷面成绩：${course.teachingPlan_Course_WrittenScore}<span style='padding-right:12px'></span>平时成绩：${course.teachingPlan_Course_UsuallyScore }" class='blue'> ${course.teachingPlan_Course_scoreStr}</a>
								     
									      <c:choose>
									      	<c:when test="${not empty course.teachingPlan_Course_scoreStr }">
									      		${course.teachingPlan_Course_scoreStr}
									      	</c:when>
									      	<c:when test="${not empty course.teachingPlan_Course_score }">
									      		${course.teachingPlan_Course_score}
									      	</c:when>
									      </c:choose>
								      </td>
								      <td></td>
								      
								      <td>
								      	   <c:choose>
									      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and course.teachingPlan_Course_examEndTime != ''}">
									      			${course.teachingPlan_Course_examStartTime}-${course.teachingPlan_Course_examEndTime }
									      		</c:when>
									      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and empty course.teachingPlan_Course_examEndTime}">
									      			${course.teachingPlan_Course_examStartTime}
									      		</c:when>
									      		<c:otherwise>
									      			&nbsp;
									      		</c:otherwise>	
								     	   </c:choose>
								       </td>
								    </tr>
								</c:forEach> --%>
												<!-- -------------------------------------------学生学习计划(计划外课程)------------------------------------------- -->
											</tbody>
											<tfoot>
											</tfoot>
										</table>
									</div>
									<c:forEach items="${studentInfoList }" var="stu">
										<div></div>
									</c:forEach>
									<div class="tabsFooter">
										<div class="tabsFooterContent"></div>
									</div>
								</div>
							</div>

						</c:otherwise>
					</c:choose>
				</div>
				<%-- 我的学习计划end --%>
			</div>
		</div>
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
			/*$.post("${baseUrl}/edu3/framework/studentinfo/ajax.html",{userid:'${user.resourceid}'}, function (json){
				if(json.success && json.success=='Y'){
					var msginfo = "<b>${user.cnName}</b>同学,你当前的联系信息如下:<br/>联系电话:"+json.mobile+"<br/>邮箱地址:"+json.email;
					if(json.mobile==""||json.email==""){
						msginfo += "<br/><b>补全联系信息请点击\"修改\"按钮</b>";
					}
					alertMsg.confirm(msginfo,{okName:'确定',okCall:function(){
						changeTeachingPlanCourseStatus(status,courseId,planId,msg);
					},cancelName:'修改',cancelCall:function(){
						$.pdialog.open("${baseUrl}/edu3/framework/user/setting.html", "student_info_setting", "设置", {mask:true});
						return false;
					}});
				} else {
					 changeTeachingPlanCourseStatus(status,courseId,planId,msg);
				}				
			}, "json");*/
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
	function goInteractive(courseId,tCourseId,isNeedReExamination,term){
		//判断当前学期是否为开课学期
		var currentTerm = "${term}";
		var flag = parseInt(term)>parseInt(currentTerm)?true:false;
		if(flag){
			alertMsg.warn("该门课程不是当前学期网络课程，无法进入学习。");
		}else{
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
	
	// 查看某个学生某门课程的网上学习成绩
	function viewStudentOnlineScore(planCourseId,gradeId,schoolId,planId,studentId,courseId) {
		var url = "${baseUrl }/edu3/teaching/usualresults/viewOnlineScore_student.html";
		$.pdialog.open(url+"?planCourseId="+planCourseId+"&gradeId="+gradeId+"&schoolId="+schoolId+"&planId="+planId+"&studentId="+studentId+"&courseId="+courseId,"","查看网上学习成绩",{mask:true,height:400,width:600});
	}
 </script>
</body>
</html>