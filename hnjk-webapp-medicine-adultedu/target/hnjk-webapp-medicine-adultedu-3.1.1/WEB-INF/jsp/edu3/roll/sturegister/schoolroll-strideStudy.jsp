<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>跨学年复学</title>
<script type="text/javascript">

//选择教学计划_复学异动
function selectTeachPlan_strideStudy(obj){
	var gradeId   = $("#strideStudy_gradeName").val();
	var classicId = $("#strideStudy_classic").val();
	var brSchoolId = $("#strideStudy_schoolCenterid").val(); 
	var teachType  = $("#strideStudy_teachingType").val();
	var majorId    = $("#strideStudy_majorid").val();
	//班和教学计划目前没有关联，所以不用根据所选的班进行调整。
	var html 	  = "<option value=''>请选择...</option>";
	$("#strideStudy_changeguidteachPlanId").html(html);
	if((""==brSchoolId)||(null==brSchoolId) ){
		alertMsg.warn("无法获取办学单位数据.");
		return false;
	}
	if((""==gradeId)||(null==gradeId)){
		alertMsg.warn("无法获取年级数据.");
		return false;
	}
	if(""==teachType){
		alertMsg.warn("请选择教学模式.");
		return false;
	}
	jQuery.ajax({
		data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId+"&majorId="+majorId,
		dataType:"json",
		url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getguidteachplan.html",
		success:function(data){
			if(data.error && data.error!=""){
				$("#strideStudy_changeguidteachPlanId").html(html);
				alertMsg.warn(data.error);
			}else{				
				for(var i=0;i<data.teachPlanList.length;i++){
					html += "<option value='"+data.teachPlanList[i]['RESOURCEID']+"'>"+data.teachPlanList[i]['TEACHPLANNAME']+"</option>";
				}
				$("#strideStudy_changeguidteachPlanId").html(html);
			}
		}
	});
}

</script>
</head>
<body>
	<h2 class="contentTitle">跨学年复学</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/register/studentinfo/strideStudy/save.html"
				class="pageForm" id="strideStudyForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>相关信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="changeTab">
									<!-- 异动前信息 -->
									<input type="hidden" id="changeBeforeLearingStyle"
										name="changeBeforeLearingStyle"
										value="${stuChangeInfo.changeBeforeLearingStyle }" />
									<input type="hidden" id="changeBeforeTeachingGuidePlanId"
										name="changeBeforeTeachingGuidePlanId"
										value="${stuChangeInfo.changeBeforeTeachingGuidePlan.resourceid }" />
									<input type="hidden" id="changeBeforeBrSchoolId"
										name="changeBeforeBrSchoolId"
										value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
									<input type="hidden" id="schoolType" name="schoolType" value="" />
									<input type="hidden" id="studentstatus" name="studentstatus"
										value="" />
									<input type="hidden" id="majorId" name="majorId"
										value="${stuChangeInfo.studentInfo.major.resourceid }" />
									<input type="hidden" id="gradeId" name="gradeId"
										value="${stuChangeInfo.studentInfo.grade.resourceid }" />
									<input type="hidden" id="classicId" name="classicId"
										value="${stuChangeInfo.studentInfo.classic.resourceid }" />
									<input type="hidden" id="classesId" name="classesId"
										value="${stuChangeInfo.studentInfo.classes.resourceid }" />
									<tr>
										<td width="10%">姓名:</td>
										<td width="40%">${studentName}</td>
										<td width="10%">学号:</td>
										<td width="40%" id="stuChangeForStu">${studyNo} <input
											type="hidden" id="strideStudy_stubum"
											name="strideStudy_stubum" value="${studyNo}" />
										</td>
									</tr>
									<tr>
										<td width="10%">原学习形式:</td>
										<td width="40%">${teachingtype}</td>
										<td width="10%">复学学习形式:</td>
										<td width="40%" id="stuChangeForStu">${teachingtype} <input
											type="hidden" id="strideStudy_teachingType"
											name="strideStudy_teachingType" value="${teachingtypeid}" />
										</td>
									</tr>
									<tr>
										<td width="10%">原学院:</td>
										<td width="40%">${schoolCenter}</td>
										<td width="10%">复学学院:</td>
										<td width="40%" id="stuChangeForStu">${schoolCenter} <input
											type="hidden" id="strideStudy_schoolCenterid"
											name="strideStudy_schoolCenterid" value="${schoolCenterid}">
										</td>
									</tr>
									<tr>
										<td width="10%">原年级:</td>
										<td width="40%">${grade}</td>
										<td width="10%">复学年级:</td>
										<td width="40%" id="stuChangeForStu">${gradeName} <input
											type="hidden" id="strideStudy_gradeName"
											name="strideStudy_gradeName" value="${gradeId2}"> <!--<gh:selectModel id="strideStudy_gradeName" name="strideStudy_gradeName" bindValue="resourceid" displayValue="gradeName" modelClass="com.hnjk.edu.basedata.model.Grade" value="${condition['stuGrade']}" onchange="_selectClasses_strideStudy(this)" orderBy="yearInfo.firstYear desc" choose="Y" style="float:none;width: 120px" />-->
										</td>
									</tr>
									<tr>
										<td width="10%">原专业:</td>
										<td width="40%">${major}</td>
										<td width="10%">复学专业:</td>
										<td width="40%" id="stuChangeForStu">${major} <input
											type="hidden" id="strideStudy_majorid"
											name="strideStudy_majorid" value="${majorId}" />
										</td>
									</tr>
									<tr>
										<td width="10%">原班级:</td>
										<td width="40%">${classes}</td>
										<td width="10%">复学班级:</td>
										<td width="40%" id="stuChangeForStu"><select
											name="strideStudy_classesid" id="strideStudy_classesid"
											size="1" class="required" style="float: none; width: 260px;"
											onchange="selectTeachPlan_strideStudy(this)">
												<option value="">请选择</option>
												<c:forEach items="${classesList}" var="cl" varStatus="vs">
													<option value="${cl.RESOURCEID}">${cl.CLASSESNAME}</option>
												</c:forEach>
										</select><font id="strideStudy_changeguidteachPlanId_f" color=red>*</font>
										</td>
									</tr>
									<tr>
										<td width="10%">原层次:</td>
										<td width="40%">${classic}</td>
										<td width="10%">复学层次:</td>
										<td width="40%" id="stuChangeForStu">${classic} <input
											type="hidden" id="strideStudy_classic"
											name="strideStudy_classic" value="${classicid}" />
										</td>
									</tr>
									<tr>
										<td width="10%">复学日期:</td>
										<td width="40%"><input type="text"
											id="strideStudy_backSchoolDate"
											name="pauseStudy_backSchoolDate"
											value="${stuChangeInfo.backSchoolDate}" class="Wdate"
											onfocus="WdatePicker({minDate:'${currentDateAfter}'})" /><font
											id="strideStudy_changeguidteachPlanId_f" color=red>*</font></td>
										<td width="10%">复学后教学计划:</td>
										<td width="40%" id="stuChangeForStu"><select
											name="strideStudy_changeguidteachPlanId"
											id="strideStudy_changeguidteachPlanId" size="1"
											class="required" style="float: none; width: 260px;"><option
													value="">请选择</option></select><font
											id="strideStudy_changeguidteachPlanId_f" color=red>*</font></td>
									</tr>
									<tr>
										<td width="10%">操作员:</td>
										<td width="90%" colspan="3">${opMan} <input type="hidden"
											id="strideStudy_opman" name="strideStudy_opman"
											value="${opMan}" /> <input type="hidden"
											id="strideStudy_opmanid" name="strideStudy_opmanid"
											value="${opManId}" />
										</td>
									</tr>
									<tr>
										<td width="10%">复学原因:</td>
										<td width="90%" colspan="3"><textarea rows="5" cols="70%"
												name="memo"></textarea></td>
									</tr>
									<tr>
										<td width="10%">备注:</td>
										<td width="90%" colspan="3"><textarea rows="5" cols="70%"
												name="reason"></textarea></td>
									</tr>
								</table>
							</div>
						</div>

						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>

				<div class="formBar">
					<ul>
						<c:if test="${ empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">保存</button>
									</div>
								</div></li>
						</c:if>
						<c:if test="${not empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="selfChangeApplyOnceMore()">继续申请异动</button>
									</div>
								</div></li>
						</c:if>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>

			</form>
		</div>
	</div>
</body>
</html>