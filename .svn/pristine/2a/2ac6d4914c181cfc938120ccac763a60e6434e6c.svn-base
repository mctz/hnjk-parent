<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增预约</title>
</head>
<body>
	<div class="page">
		<div class="pageContent" layouth="10">
			<div class="panel">
				<h1>
					<font color="red">${studentInfo.studentBaseInfo.name}</font>教学计划 -
					<font id="teachingPlanName" color='red'>${planTitleVo.teachingPlan_Name }</font>
				</h1>
				<div>
					<table id="leanPlanTable" class="list" width="100%">
						<thead>
							<tr style="color: #183152">
								<td colspan="9"><strong>请选择预约学习年度:</strong> <gh:selectModel
										id="yearInfoSetting" name="yearInfoSetting"
										bindValue="resourceid" displayValue="settingName"
										modelClass="com.hnjk.edu.teaching.model.OrderCourseSetting"
										value="${yearInfoSettingId }" condition="isOpened='Y'" /></td>

							</tr>

							<tr style="color: #183152">
								<td colspan="9"><strong>学制：</strong><font color="red">${planTitleVo.eduYear }</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>最低毕业学分：</strong><font color="red">${planTitleVo.minResult }</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>已修总学分：</strong><font color="red">
										${planTitleVo.totalScore}</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>已修必修课总学分：</strong><font
									color="red">${planTitleVo.compulsoryedScore}</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>已修必修课数：</strong><font color="red">${planTitleVo.compulsoryed }</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>

						</thead>
						<tbody id="tab">
							<!-- 学生学习计划 -->
							<tr style="color: #183152">
								<th width="5%" align="center">编号</th>
								<th width="28%">课程名称</th>
								<th width="8%" align="center">课程性质</th>
								<th width="5%" align="center">学分</th>
								<th width="8%" align="center">建议学期</th>
								<th width="28%" align="center">考试时间</th>
								<th width="10%" align="center">学习状态</th>
								<th width="8%" align="center">预约状态</th>
							</tr>
							<c:forEach items="${teachingPlanVo}" var="courses" varStatus="vs">
								<tr>
									<td>${vs.index+1 }</td>
									<td>${courses.teachingPlan_Course_name }</td>
									<td>${courses.teachingPlan_Course_type }</td>
									<td>${courses.teachingPlan_Course_creditHour}</td>
									<td>${courses.teachingPlan_Course_term }</td>
									<td>${courses.teachingPlan_Course_examStartTime}-${courses.teachingPlan_Course_examEndTime }</td>
									<td><c:choose>
											<c:when
												test="${courses.teachingPlan_Course_teachType_code eq 'facestudy' }">
									不需预约
							 </c:when>
											<c:when
												test="${courses.teachingPlan_Course_learnStatusInt >=1 and courses.teachingPlan_Course_learnStatusInt <3 and courses.isNeedReExamination eq 'Y' }">
												<font color="red">${courses.teachingPlan_Course_learnStatus}</font>
											</c:when>
											<c:when
												test="${courses.teachingPlan_Course_learnStatusInt >=1 and courses.teachingPlan_Course_learnStatusInt <3}">
												<font color="blue">${courses.teachingPlan_Course_learnStatus}</font>
											</c:when>
											<c:when
												test="${courses.teachingPlan_Course_learnStatusInt ==3}">
												<font color="green">${courses.teachingPlan_Course_learnStatus}</font>
											</c:when>
											<c:otherwise>
					      		${courses.teachingPlan_Course_learnStatus}
					      	</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when
												test="${courses.teachingPlan_Course_type_code eq 'thesis'}">

											</c:when>
											<c:when
												test="${courses.teachingPlan_Course_learnStatusInt ==2 && courses.teachingPlan_Course_learnStatus eq '已预约学习'  and courses.teachingPlan_Course_teachType_code eq 'netsidestudy'  }">

											</c:when>
											<c:when
												test="${courses.teachingPlan_Course_type_code ne 'thesis' and courses.teachingPlan_Course_learnStatusInt <=2 and courses.teachingPlan_Course_bookingStatus !='准备考试'}">
												<a href="javascript:void(0)"
													onclick="teachingcoursemakeupon('${studentInfo.resourceid }','${courses.teachingPlan_Id }','${courses.teachingPlan_Course_Id }','${courses.teachingPlan_Course_learnStatusInt }')">${courses.teachingPlan_Course_bookingStatus}</a>
											</c:when>
											<c:otherwise>
						      	${courses.teachingPlan_Course_bookingStatus}
						      </c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
						</tfoot>
					</table>

				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">

	var studentId = "${studentInfo.resourceid}"
	
	jQuery(document).ready(function(){
		var teachingPlanCourseSize = "${fn:length(teachingPlanVo)}"
		if(teachingPlanCourseSize<=0){
			jQuery("#tab").hide();
			alertMsg.confirm("该学员没有关联教学计划！", {
                okCall: function(){
                	$.pdialog.closeCurrent();
                }
			});
		}
	});
	
	//新增课程预约    param:学生ID,教学计划课程ID,课程状态（预约学习、预约考试、已考完）	
	function teachingcoursemakeupon(stuId,teachingPlanId,teachingPlanCourseId,courseStatus){
		var status = "";
		var yearInfoSetting = jQuery("#yearInfoSetting").val();
		if(courseStatus==-1){
			status = 1;
		}else{
			status =courseStatus;
		}
		if(""==yearInfoSetting&&status==1){
		 	$("#yearInfoSetting").focus();
			alertMsg.warn("请选择一个预约年度！");
			return false;
		}
		if(teachingPlanId==""){
			jQuery("#tab").hide();
			alertMsg.warn("该学员没有关联教学计划！"); 
			return false;
		}
		
		var url="${baseUrl}/edu3/teaching/courseorder/makeup-save.html"
		
		jQuery.ajax({
			type:"post",
			data:"sutId="+stuId+
				 "&teachingPlanCourseId="+teachingPlanCourseId+
				 "&teachingPlanId="+teachingPlanId+
				 "&teachingPlanCourseStatus="+status+
				 "&yearInfoSettingId="+yearInfoSetting,
			url:url,
			dataType :"json",
			success:function(resultDate){
			
				if(resultDate['operateError']==true){
			 			var msgList = resultDate['msg'];
						var msg = "";
						for(i=0;i<msgList.length;i++){
							msg+=(i+1)+"、"+msgList[i]+'</br>';
						}
						alertMsg.warn(msg);

				 }else if(resultDate['operateType']=='orderCourse'){
					 	if(resultDate['orderCourseStatus']==false){
							var msgList = resultDate['msg'];
							var msg = "";
							for(i=0;i<msgList.length;i++){
								msg+=(i+1)+"、"+msgList[i]+'</br>';
							}
							alertMsg.warn(msg);
						}else{
							
							 $.pdialog.reload("${baseUrl}/edu3/teaching/courseorder/makeup-form.html?studentId="+stuId+"&yearInfoSettingId="+resultDate['yearInfoSettingId']);
							 alertMsg.correct('预约成功');
						}
				 }else if(resultDate['operateType']=='orderExam'){
					 	
					 	if(resultDate['orderExamStatus']==false){
							var msgList = resultDate['msg'];
							var msg = "";
							for(i=0;i<msgList.length;i++){
								msg+=(i+1)+"、"+msgList[i]+'</br>';
							}
							alertMsg.warn(msg);
						}else{
							 $.pdialog.reload("${baseUrl}/edu3/teaching/courseorder/makeup-form.html?studentId="+stuId+"&yearInfoSettingId="+resultDate['yearInfoSettingId']);
							 alertMsg.correct('预约成功');
						}
				 }
			}
		}); 
	}
</script>
</body>
</html>