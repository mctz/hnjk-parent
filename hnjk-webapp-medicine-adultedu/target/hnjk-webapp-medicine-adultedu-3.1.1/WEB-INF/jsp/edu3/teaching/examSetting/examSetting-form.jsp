<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试计划设置</title>
</head>
<body>
	<h2 class="contentTitle">编辑考试计划设置</h2>
	<div class="page">
		<form method="post"
			action="${baseUrl}/edu3/teaching/exam/plansetting/save.html"
			class="pageForm" onsubmit="return submitCheck(this);">
			<input type="hidden" name="resourceid"
				value="${examSetting.resourceid }" />
			<div class="pageContent">
				<table class="form" width="100%">
					<tr>
						<td width="15%">设置名称:</td>
						<td width="35%"><input type="text" name="settingName"
							value="${examSetting.settingName }" maxlength="30"
							class="required chinese" /></td>
						<td width="15%">时间段:</td>
						<td width="35%"><gh:select name="timeSegment"
								dictionaryCode="CodeCourseTime"
								value="${examSetting.timeSegment}" choose="N" /></td>
					</tr>

					<tr>
						<td width="15%">开始时间:</td>
						<td width="35%"><input type="text" id="examSetting_startTime"
							name="startTime"
							value='<fmt:formatDate value="${examSetting.startTime  }" pattern="HH:mm:ss"/>'
							class="required"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'HH:mm:ss',maxDate:'#F{$dp.$D(\'examSetting_endTime\')}'})" />
						</td>
						<td width="15%">结束时间:</td>
						<td width="35%"><input type="text" id="examSetting_endTime"
							name="endTime"
							value='<fmt:formatDate value="${examSetting.endTime  }" pattern="HH:mm:ss"/>'
							class="required"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'HH:mm:ss',minDate:'#F{$dp.$D(\'examSetting_startTime\')}'})" />
						</td>
					</tr>
					<tr>
						<td colspan="4" style="color: red">使用快捷键Ctrl+F查找课程，选择课程后请点击提交保存当前考试计划！</td>
					</tr>
					<!--<tr>
			<td>课程设置：</td>
			<td colspan="3">
			<select name="courseSelect" id='${examSetting.resourceid }_courseSelect' multiple='multiple' size='10'  style="width:auto;">
				<c:if test="${examSetting.resourceid != null}">
					<c:forEach items="${examSetting.details}" var="selecedCourse">
						<option title="${selecedCourse.courseName }" value="${selecedCourse.courseId.resourceid }" SELECTED>
						${selecedCourse.courseId.courseCode}-${selecedCourse.courseName }</option>
					</c:forEach>
				</c:if>
				<c:forEach items="${courseList }" var="course">
					<option title="${course.courseName }" value="${course.resourceid }" >${course.courseCode}-${course.courseName }</option>
				</c:forEach>
			</select>
			</td>
		</tr>
						
		-->
				</table>
			</div>
			<div class="pageContent" layoutH="160">
				<div
					style="float: left; display: block; width: 48%; margin-bottom: 16px; padding: 5px 0px 0px 0px; line-height: 21px;">
					<div>
						<table id="examSettingCourseListTable" class="list" width="100%">
							<thead>
								<tr>
									<th width="10%"
										onclick="chooseAllCourse('examSettingCourseListBody')">
										<%-- <input type="checkbox" name="checkall" id="examsetting_courseList_check_all" onclick="checkboxAll('#examsetting_courseList_check_all','courseId','#examSettingCourseListBody'),chooseAllCourse('examSettingCourseListBody')"/>--%>
										全选
									</th>
									<th width="90%">未选择的课程</th>
								</tr>
							</thead>
							<tbody id="examSettingCourseListBody">
								<c:forEach items="${courseList }" var="course">
									<c:if test="${ empty examSettingDetails[course.resourceid] }">
										<tr id="${course.resourceid}" style="">
											<td><input type="checkbox" name="courseId"
												value="${course.resourceid}" autocomplete="off"
												title="${course.courseName}"
												onclick="chooseCourse('examSettingCourseListBody','${course.resourceid}')" /></td>
											<td>${course.courseCode}-${course.courseName}</td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div
					style="float: right; display: block; width: 48%; margin-bottom: 16px; padding: 5px 6px 0px 0px; line-height: 21px;">
					<div>
						<table id="examSettingDetailsCourseListTable" class="list"
							width="100%">
							<thead>
								<tr>
									<th width="10%"
										onclick="chooseAllCourse('examSettingDetailsCourseListBody')">

										<%-- <input type="checkbox"  name="checkall" id="examsetting_details_courseList_check_all" onclick="checkboxAll('#examsetting_details_courseList_check_all','selectCourse','#examSettingDetailsCourseListBody'),chooseAllCourse('examSettingDetailsCourseListBody')" checked="checked"/>--%>
										全选
									</th>
									<th width="90%">已选择的课程</th>
								</tr>
							</thead>
							<tbody id="examSettingDetailsCourseListBody">
								<c:if test="${examSetting.resourceid != null}">
									<c:forEach items="${examSetting.details}" var="selecedCourse">
										<tr id="${selecedCourse.courseId.resourceid }"
											style="color: red">
											<td><input type="checkbox" name="selectCourse"
												value="${selecedCourse.courseId.resourceid }"
												autocomplete="off" title="${selecedCourse.courseName }"
												onclick="chooseCourse('examSettingDetailsCourseListBody','${selecedCourse.courseId.resourceid }')"
												checked="checked" /></td>
											<td>${selecedCourse.courseId.courseCode}-${selecedCourse.courseName }</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
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
	<script type="text/javascript">

    $().ready(function(){
    	var examSettingId = "${examSetting.resourceid }";
    	if(null!=examSettingId && ""!=examSettingId){
    		var selectID  = "#"+examSettingId+"_courseSelect"
			$(selectID).multiselect2side({
					selectedPosition: 'right',
					moveOptions: false,
					labelsx: '',
					labeldx: ''
			});
    	}else{
    		$("#_courseSelect").multiselect2side({
				selectedPosition: 'right',
				moveOptions: false,
				labelsx: '',
				labeldx: ''
			});
    	}
	});
    //提交前检查是否选择了课程
   function submitCheck(form){
       if($("#examSettingDetailsCourseListBody input[name='selectCourse']").size()==0){
    	   alertMsg.confirm("未选择考试计划课程，确定要保存当前考试计划吗？",{
    	    	  okCall:function(){
    	    		  validateCallback(form); 
    	    	  } 
    	   });
       }else{
    	   validateCallback(form); 
       }
       return false;
   }
   //选择全部课程 
   function chooseAllCourse(origBodyId){
	   var destBodyId    = origBodyId=="examSettingDetailsCourseListBody"?"examSettingCourseListBody":"examSettingDetailsCourseListBody";
	   var destInputName = origBodyId=="examSettingDetailsCourseListBody"?"courseId":"selectCourse";
	   var origInputName = origBodyId=="examSettingDetailsCourseListBody"?"selectCourse":"courseId";
	   var checked       = origBodyId=="examSettingDetailsCourseListBody"?"":"checked";
	   if(origBodyId=="examSettingDetailsCourseListBody"){
		   $("#"+origBodyId+" tr").css("color","red");
	   }
	   var trs           = $("#"+origBodyId).html();
	   trs               = trs.replaceAll(origBodyId,destBodyId);
	   
	   $(trs).prependTo($("#"+destBodyId));
	   $("#"+destBodyId).find("input[name='"+origInputName+"']").attr("name",destInputName).attr("checked",checked);
	   $("#"+origBodyId+" tr").remove();
	   
   }
   //选择课程
   function chooseCourse(origBodyId,trId){
	   
	   var destBodyId    = origBodyId=="examSettingDetailsCourseListBody"?"examSettingCourseListBody":"examSettingDetailsCourseListBody";
	   var destInputName = origBodyId=="examSettingDetailsCourseListBody"?"courseId":"selectCourse";
	   var origInputName = origBodyId=="examSettingDetailsCourseListBody"?"selectCourse":"courseId";
	   var checked       = origBodyId=="examSettingDetailsCourseListBody"?"":"checked";
	   var style         = origBodyId=="examSettingDetailsCourseListBody"?"style='color: red'":""; 
	   //alert(destTableId+"__"+origTableId+"__"+destInputName+"__"+origInputName);
	   var cls 			 = $("#"+destBodyId+" tr:eq(0)").hasClass("trbg")?"":"trbg";
	   var tr  			 = "<tr id='"+trId+"'"+style+">"+$(" tr[id='"+trId+"']").html()+"</tr>";
	   tr				 = tr.replace(origBodyId,destBodyId);
	   
	   if($("#"+destBodyId+" tr ").size()>0){
		   $(tr).insertBefore($("#"+destBodyId+" tr:eq(0)")).addClass(cls).find("input[name='"+origInputName+"']").attr("name",destInputName).attr("checked",checked);
	   }else{
		   $(tr).appendTo($("#"+destBodyId)).addClass(cls).find("input[name='"+origInputName+"']").attr("name",destInputName).attr("checked",checked);
	   }
	   $("#"+origBodyId+" tr[id='"+trId+"']").remove();
   }
</script>
</body>
