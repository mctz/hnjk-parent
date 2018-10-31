<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线录入成绩</title>
</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var msg = "${condition['msg']}"
		if(null!=msg&&""!=msg){
			alertMsg.warn(msg);
		}
	});

	//合法性检查
	function isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName){
		if("Y"!=isAllowInputExamResults){
			alertMsg.warn("考试批次未关闭，或者成绩录入未开放，不允许导出或录入成绩单！");
			return false;
		}
		if(isBranchSchool!="Y"){
			alertMsg.warn("你不是教学站教务员，无法录入成绩");
			return false;
		}
		if(teachType=="networkstudy"){		
			alertMsg.warn("课程《"+courseName+"》不是面授课程");
			return false;
		}	
		return true;
	}
	//导出空白成绩单
	function exportFaceStudyExamResultsTranscripts(teachingPlanCourseId,courseName,teachType,classesid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var guidePlanId = "${guidePlan.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			var url = "${baseUrl }/edu3/teaching/transcripts/facestudy/download.html?guidePlanId="+guidePlanId+"&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid;
			alertMsg.confirm("确定下载《"+courseName+"》的成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'faceStudyCourseListForDownloadExportIframe');
				}
			});	
		}		
	}
	//导入成绩单
	function importFaceStudyExamResultsTranscripts(teachingPlanCourseId,courseName,teachType,classesid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			var url = "${baseUrl}/edu3/teaching/transcripts/facestudy/upload-showpage.html?teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid;
			$.pdialog.open(url,"RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_IMPORT",courseName+"成绩导入",{width:900, height:640});		
		}		
	}
	//在线录入
	function inputFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,classesid){
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var guidPlanId = "${guidePlan.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			var url = "${baseUrl}/edu3/teaching/result/facestudy/input-examresults-list.html?guidPlanId="+guidPlanId+"&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&classesId="+classesid;
			navTab.openTab("RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_ONLINE",url,courseName+"成绩录入");	
		}
	}
	//提交成绩
	function submitFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,classesid){	
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var guidePlanId = "${guidePlan.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			var url = "${baseUrl}/edu3/teaching/result/facestudy/input-examresults-submit.html";
			alertMsg.confirm("您确定要提交《"+courseName+"》的成绩到考务办吗？", {
				okCall: function(){
					jQuery.post(url,{teachingPlanCourseId:teachingPlanCourseId,examSubId:examSubId,guidePlanId:guidePlanId,classesId:classesid,teachType:teachType},function(resultData){
						var msg 	   		= resultData['message'];
					  	var statusCode 		= resultData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
					  		var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				}
			});	
		}			
	}
	//查看打印
	function printFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,classesid){	
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var guidePlanId = "${guidePlan.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName)){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print-view.html?gradeid=${guidePlan.grade.resourceid}&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&flag="+teachType+"&classesId="+classesid;
			$.pdialog.open(url, "RES_TEACHING_RESULT_FACESTUDY_INPUT_LIST_PRINT", courseName+"成绩打印", {width:800,height:600});
		}
	}
	//导出面授成绩
	function exportFaceStudyExamResults(teachingPlanCourseId,courseName,teachType,classesid){	
		var isAllowInputExamResults = "${condition['isAllowInputExamResults']}";
		var isBranchSchool = "${condition['isBranchSchool']}";
		var examSubId = "${examSub.resourceid}";
		var guidePlanId = "${guidePlan.resourceid}";
		if(isValideInputResult(isAllowInputExamResults,isBranchSchool,teachType,courseName,classesid)){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print.html?gradeid=${guidePlan.grade.resourceid}&teachingPlanCourseId="+teachingPlanCourseId+"&examSubId="+examSubId+"&flag="+teachType+"&operatingType=export&classesId="+classesid;
			alertMsg.confirm("确定导出《"+courseName+"》的总评成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'exportFaceStudyCourseExamResultsForDownloadExportIframe');
				}
			});		
		}
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="faceStudyExamResultsCourseSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examresults/facestudy-course-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label> <input type="hidden" name="examSubId"
							value="${condition['examSubId']}" /> <input type="hidden"
							name="guidPlanId" value="${condition['guidPlanId']}" /> <select
							id="faceStudyExamResultsCourse_CourseId" name="courseId"
							style="width: 125px;">
								<option value=""></option>
								<c:forEach
									items="${guidePlan.teachingPlan.teachingPlanCourses }" var="c">
									<option value="${c.course.resourceid }"
										<c:if test="${condition['courseId'] eq c.course.resourceid }">selected="selected"</c:if>>${c.course.courseCode }-${c.course.courseName }</option>
								</c:forEach>
						</select> <script type="text/javascript">
					$(document).ready(function(){
						$("#faceStudyExamResultsCourse_CourseId").flexselect();
				    });
					</script></li>
						<!-- 
				<li>
				<label>教学方式：</label>
				<gh:select dictionaryCode="teachType" id="faceStduyExamResults_teachType" name="teachType" value="${condition['teachType'] }"/>
				</li>
				 -->
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="112" width="100%">
				<thead>
					<tr>
						<th></th>
						<th width="16%"
							style="text-align: center; vertical-align: middle;">考试批次</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">授课教师</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">考核方式</th>
						<th width="19%"
							style="text-align: center; vertical-align: middle;">班级</th>
						<!-- 
		            <th width="8%" style="text-align: center;vertical-align: middle;">课程编码</th>
		             -->
						<th width="8%" style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">开课学期</th>
						<!-- 
		            <th width="10%" style="text-align: center;vertical-align: middle;">教学方式</th>
		             -->
						<th width="5%" style="text-align: center; vertical-align: middle;">人数</th>
						<th width="6%" style="text-align: center;">导出成绩</th>
						<th width="6%" style="text-align: center;">导入成绩</th>
						<th width="6%" style="text-align: center;">成绩录入</th>
						<th width="10%" style="text-align: center;">导出与打印总评成绩</th>
					</tr>
				</thead>
				<tbody id="faceStudyExamResultsCourseBody">
					<c:forEach items="${teachingPlanCourseList.result}" var="t"
						varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">
								${examSub.batchName }</td>
							<td style="text-align: center; vertical-align: middle;">
								${t.lecturerName}</td>
							<td style="text-align: center; vertical-align: middle;">
								${ghfn:dictCode2Val('CodeExamClassType',t.examclasstype)}</td>
							<td style="text-align: center; vertical-align: middle;"
								title="${t.classesname}">${t.classesname}</td>
							<!--  <td  style="text-align: center;vertical-align: middle;">${t.course.courseCode}</td>-->
							<td style="text-align: center; vertical-align: middle;">${t.coursename}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('CodeTermType',t.term) }</td>
							<!--  <td  style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('teachType',t.teachType) }</td>-->
							<td style="text-align: center; vertical-align: middle;">${t.inputTotalNum }</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="exportFaceStudyExamResultsTranscripts('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')">导出</a>
							</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="importFaceStudyExamResultsTranscripts('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')">导入</a></td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="inputFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')">录入</a>
								/ <a href="javaScript:void(0)"
								onclick="submitFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')"
								title="提交${examInfo.courseName}的成绩到考务办">提交</a></td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="printFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')">查看打印</a>
								<a href="javaScript:void(0)"
								onclick="exportFaceStudyExamResults('${t.resourceid}','${t.coursename}','${t.teachtype}','${t.classesid }')">导出</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${teachingPlanCourseList}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresults/facestudy-course-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>