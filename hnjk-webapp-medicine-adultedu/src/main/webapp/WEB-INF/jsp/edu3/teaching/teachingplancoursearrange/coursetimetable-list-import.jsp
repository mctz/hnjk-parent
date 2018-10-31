<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title}</title>
<style type="text/css">
#exameeinfo_messageTbody .success {
	color: green;
}

#exameeinfo_messageTbody .error {
	color: red;
}
</style>
<script type="text/javascript">
	   function _exameeinfoValidateCallback(form){
		   
		   var attachId = $(form).find("[name='attachId']").size();
		   if(attachId<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallbackss(form,function (json){
			   if(json.statusCode == 300 && json.excelErr == "") {
					alertMsg.error(json.message);
				} else if (json.statusCode == 200 && json.excelErr == ""){
					alertMsg.confirm(json.message);
					if (json.reloadUrl){
						navTab.reload(json.reloadUrl, {classesid:json.classesid,plancourseid:json.plancourseid,term:json.term}, json.navTabId);
					    $('#timetableCls').click();       
					}

				}else if(json.statusCode == 300 && json.excelErr != ""){
					alertMsg.confirm(json.message);
					if (json.reloadUrl){
						$('#exameeinfo_messageTbody').text(" Excel行号为："+json.excelErr+"的数据导入失败！");
						navTab.reload(json.reloadUrl, {classesid:json.classesid,plancourseid:json.plancourseid,term:json.term}, json.navTabId);
					}
				}
			});
	   }	  
	  
	   function validateCallbackss(form, callback) {
		   
		   var teachids = ""; //可分配的教师id
		   var teachnames = ""; //可分配的教师名称
		   var roomids = ""; //可分配的教学点
		   var roomnames = ""; //可分配的教学点名称
		   var timeids = "";//可分配的时间段id
		   var timenames = "";//可分配的时间段
		   $('#dialog_timetable_teacherId option').each(function(i,v){//循环可分配的教师
			   if(i > 0){
				   teachids += v.value+",";
				   teachnames += v.text+",";
			   }
		   });
		   $('#dialog_timetable_classroomid option').each(function(i,v){//循环可分配的教学点
			   if(i > 0){
				   roomids += v.value+",";
				   roomnames += v.text+",";
			   }
		   });
		   $('#timetable_timePeriodid option').each(function(i,v){//循环可分配的时间段
			   if(i > 0){
				   timeids += v.value+",";
				   timenames += v.text+",";
			   }
		   });
		   
		   //为了导入时间的严谨性 把这些参数带入后台验证
		   $('#teachids').val(teachids);
		   $('#teachnames').val(teachnames);
		   $('#timeids').val(timeids);
		   $('#timenames').val(timenames);
		   $('#roomids').val(roomids);
		   $('#roomnames').val(roomnames); 
		   
		   
		    var $form = $(form);
		    var url = $form.attr("action");
		    url += "&teachids="+teachids+"&teachnames="+teachnames+"&timeids="+timeids+"&timenames="+timenames+"&roomids="+roomids;
			url +="&roomnames="+roomnames;
			
			$.ajax({
				type:'POST',
				url:url,//$form.attr("action"),
				data:$form.serializeArray(),
				dataType:"json",
				cache: false,
				success: callback || validateFormCallback,
				
				error: DWZ.ajaxError
			});
			return false;
		}
	   
	   /*
	   *选择教师 查看教师id
	   */
	   function seeTeachInfo(){
		   $('#teachid').text($('#dialog_timetable_teacherId').val());
	   }
	   /*
	   *选择上课地点 查看上课地点id
	   */
	   function seeClassRoomId(){
		    $('#classroomid').text($('#a select').val());
	   }
	   /*
	   *选择上课时间 查看上课时间id
	   */
	   function seeTimeId(){
		    $('#timeid').text($('#b select').val());
	   }
	   
	</script>
</head>
<body>
	<h2 class="contentTitle">${title}</h2>
	<div class="page">
		<div class="pageContent">
			<form id="timetableform" method="post"
				action="${baseUrl}/edu3/teaching/teachingplantimetable/import/submit.html?courseids=${planCourse.course.resourceid}"
				class="pageForm"
				onsubmit="return _exameeinfoValidateCallback(this);">
				<input type="hidden" name="isEnrolExam" value="${isEnrolExam }" /> <input
					type="hidden" name="classid" value="${classes.resourceid}" /> <input
					type="hidden" name="plancourseid" value="${planCourse.resourceid}" />
				<input type="hidden" name="term" value="${term}" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">班级:</td>
							<td>${classes.classname }</td>
						</tr>
						<tr>
							<td width="20%">课程:</td>
							<td>${planCourse.course.courseName }</td>
						</tr>
						<tr>
							<td colspan="2">Excel导入可分配资源：</td>
						</tr>
						<tr>
							<td colspan="2"><span style="color: red;">
									请参照下面的资源填写要导入的excel排课数据 </span></td>
						</tr>
						<tr>
							<td width="20%">导入数据里可填写的教师：</td>
							<td><c:choose>
									<c:when test="${edumanagerList eq null}">
							无可分配教师
						</c:when>
									<c:otherwise>
										<select id="dialog_timetable_teacherId" style="width: 80%;"
											class="required" onchange="seeTeachInfo()">
											<option>== 下拉查看可分配的教师 ==</option>
											<c:forEach items="${edumanagerList }" var="t">
												<option value="${t.resourceid }"
													<c:if test="${t.resourceid eq timetable.teacherId }">selected="selected"</c:if>>${t.cnName }</option>
											</c:forEach>
										</select>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td width="20%">导入数据里可填写的上课地点：</td>
							<td id="a"><gh:selectModel id="dialog_timetable_classroomid"
									name="room" bindValue="resourceid" displayValue="classroomName"
									modelClass="com.hnjk.edu.basedata.model.Classroom"
									classCss="required" value="${timetable.classroom.resourceid }"
									condition="building.branchSchool.resourceid='${classes.brSchool.resourceid }'"
									orderBy="building,showOrder,resourceid" style="width:80%;"
									defaultOptionText="== 下拉查看可分配的上课地点 =="
									onchange="seeClassRoomId()" /></td>
						</tr>
						<tr>
							<td width="20%">导入数据里可填写的上课时间段：</td>
							<td id="b"><gh:selectModel id="timetable_timePeriodid"
									name="timeperidodid" bindValue="resourceid"
									displayValue="courseTimeName"
									modelClass="com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod"
									orderBy="brSchool.unitCode,timePeriod,startTime,resourceid"
									value="${timetable.unitTimePeriod.resourceid }"
									style="width:80%;" classCss="required"
									condition="brSchool.resourceid='${classes.brSchool.resourceid }'"
									defaultOptionText="== 下拉查看上课时间段 ==" onchange="seeTimeId()" /></td>
						</tr>

						<tr>
							<td width="20%">上传要导入的文件:</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" baseStorePath="upload" isMulti="false"
									uploadLimit="1" formType="ExameeInfoImport" fileExt="xls" />

								<input value="1" id="teachids" name="teachids" type="hidden">
								<input value="1" id="teachnames" name="teachnames" type="hidden">
								<input value="" id="roomids" name="roomids" type="hidden">
								<input value="" id="roomnames" name="roomnames" type="hidden">
								<input value="" id="timeids" name="timeids" type="hidden">
								<input value="" id="timenames" name="timenames" type="hidden">
							</td>
						</tr>
						<tr>
							<td colspan="2" style="color: red;">
								<table class="list" width="100%">
									<tbody id="exameeinfo_messageTbody"></tbody>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">导入</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button id="timetableCls" type="button" class="close"
										onclick="$.pdialog.closeCurrent();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>