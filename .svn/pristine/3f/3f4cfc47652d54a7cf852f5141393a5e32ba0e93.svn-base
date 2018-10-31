<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>

</head>
<body>
	<script type="text/javascript">
//打开页面或者点击查询（即加载页面执行）
function examResultsManagerQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "${branchSchoolName}";
	
	var classicId = "${condition['classic']}";

	var classesId = "${condition['classId']}";
	var selectIdsJson = "{unitId:'examResultManage1_brSchoolName',classicId:'examResultManage1_classic',classesId:'examResultManage1_classid'}";
	cascadeQuery("begin", defaultValue, schoolId, "", classicId,"", "", classesId, selectIdsJson);
}

// 选择教学点
function examResultsManagerQueryUnit() {
	var defaultValue = $("#examResultManage1_brSchoolName").val();
	var selectIdsJson = "{classicId:'examResultManage1_classic',classesId:'examResultManage1_classid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}



// 选择层次
function examResultsManagerQueryClassic() {
	var defaultValue = $("#examResultManage1_brSchoolName").val();
	var classicId = $("#examResultManage1_classic").val();
	var selectIdsJson = "{classesId:'examResultManage1_classid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

function submitValidate(obj){
	var teachType = $("#examResultsManager_teachType").val();
	var brSchoolName = $("#examResultManage1_brSchoolName").val();
	var ExamSub = $("#examResultsManager_ExamSub").val();
	if(''==teachType || null==teachType||''==brSchoolName||''==ExamSub){
		alertMsg.warn("请在查询前填写必填项。");
		return false;
	}else{
		return navTabSearch(obj);
	}
}

</script>
	<div class="page">
		<div class="pageHeader">
			<form id="examResultsManagerSearchForm"
				onsubmit="return submitValidate(this);"
				action="${baseUrl}/edu3/teaching/result/list.html" method="post">
				<input type="hidden" name="pageNum" value="${page.pageNum }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学方式：</label> <gh:select name="teachType"
								id="examResultsManager_teachType"
								value="${condition['teachType'] }" dictionaryCode="teachType"
								style="width:55%;" onchange="changeTeachingType1()" /> <font
							color="red">*</font></li>
						<li><label>考试批次：</label> <gh:selectModel
								id="examResultsManager_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="isDeleted='0' and batchType='exam' and examType='N'"
								orderBy="examinputStartTime desc" /> <font color="red">*</font>
						</li>

						<c:if test="${!isBrschool }">
							<li><label>教学点：</label> <span
								sel-id="examResultManage1_brSchoolName" sel-name="branchSchool"
								sel-onchange="examResultsManagerQueryUnit()"
								sel-classs="flexselect" sel-style="width: 120px"></span> <font
								color="red">*</font></li>
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="examResultManage1_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if>

						<li><label>层次：</label> <span
							sel-id="examResultManage1_classic" sel-name="classic"
							sel-onchange="examResultsManagerQueryClassic()"
							sel-style="width: 120px"></span></li>
					</ul>
					<ul class="searchContent">
						<li><label>考试编号：</label> <input
							id="examResultsManager_examCourseCode" name="examCourseCode"
							value="${condition['examCourseCode'] }" style="width: 55%" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examResultsManager_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:55%" /></li>
						<li id="graduateudit-gradeToMajorToClasses_exam"><label>班级：</label>
							<span sel-id="examResultManage1_classid" sel-name="classId"
							sel-classs="flexselect" sel-style="width: 120px"></span></li>
						<li><label>成绩状态：</label>
						<gh:select id="examResultsCheckStatus" name="checkStatus"
								value="${condition['checkStatus'] }"
								dictionaryCode="CodeExamResultCheckStatus" style="width:55%;" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<c:if test="${condition['teachType'] eq 'networkstudy' }">
								<li><span class="tips">教学站选项用于成绩导出</span></li>
							</c:if>
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
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="161" width="100%">
				<thead>
					<tr>
						<th style="text-align: center; vertical-align: middle;" width="3%"><input
							type="checkbox" name="checkall" id="check_all_examResultManager1"
							onclick="checkboxAll('#check_all_examResultManager1','resourceid','#examResultsManagerBody')" /></th>
						<th width="6%" style="text-align: center; vertical-align: middle;">考试编号</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">班级名称</th>
						<th width="7%" style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">教学方式</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">考试方式</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">预约人数</th>
						<th width="27%" style="text-align: center;">成绩状态</th>
						<th width="10%" style="text-align: center;">成绩审核</th>
						<th width="5%" style="text-align: center;">成绩调试</th>
						<%-- <th width="5%"  style="text-align: center;">成绩发布</th> --%>
						<th width="15%" style="text-align: center;">打印导出成绩</th>
					</tr>
				</thead>
				<tbody id="examResultsManagerBody">
					<c:forEach items="${page.result}" var="examInfo" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;"><input
								type="checkbox" name="resourceid"
								value="${examInfo.examInfoResourceId}" autocomplete="off" /></td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.examCourseCode}</td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.classesname}</td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.courseName}</td>
							<td style="text-align: center; vertical-align: middle;">${ghfn:dictCode2Val('teachType',condition['teachType'])}</td>
							<td style="text-align: center; vertical-align: middle;"><c:choose>
									<c:when test="${examInfo.isMachineexam eq 'Y' }">机考</c:when>
									<c:otherwise>
									${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}
								</c:otherwise>
								</c:choose></td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.orderNumber}</td>
							<c:set var="statusKey"
								value="${examInfo.classesid }${examInfo.examInfoResourceId }"></c:set>
							<td style="text-align: left; vertical-align: middle;">
								${statusMap[statusKey] }</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="examResultsAuditManager('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType'] }','${planCourse.classesid }','single','${planCourse.examInfoId }','${planCourse.teachType }')">个别审核</a>
								| <a href="javaScript:void(0)" style="color: green"
								onclick="examResultsAuditManager('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType'] }','${planCourse.classesid }','all','${planCourse.examInfoId }','${planCourse.teachType }')">全部通过</a>

								<c:set var="chStatus"
									value="CheckStatus${examInfo.examInfoResourceId}" scope="page"></c:set>
								<c:choose>
									<c:when test="${statusMap[chStatus] == '1'}">
										<a href="javaScript:void(0)" style="color: green"
											onclick="examResultsAuditManagerCancel('${examInfo.examInfoResourceId}','${examInfo.courseName}')">撤销提交</a>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose></td>
							<td style="text-align: center; vertical-align: middle;"><c:if
									test="${ghfn:hasAuth('RES_TEACHING_RESULT_MANAGE_ADJUSTMENT')}">
									<a href="javaScript:void(0)"
										onclick="adjustmentExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}')">调试</a>
								</c:if></td>
							<%-- 
			            <td style="text-align: center;vertical-align: middle;">
			            	<c:if test="${ghfn:hasAuth('RES_TEACHING_RESULT_MANAGE_PUBLISHED') }">
			            		<a href="javaScript:void(0)"  onclick="publishedExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}')">发布</a>
			            	</c:if>
			            </td>
			            --%>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)" title="打印复审成绩"
								onclick="printExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.classesid }','printReview')">打印1</a>
								<a href="javaScript:void(0)" title="打印所有成绩"
								onclick="printExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.classesid }','printAll')">打印2</a>
								<a href="javaScript:void(0)" title="导出复审成绩"
								onclick="printExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.classesid }','export')">导出1</a>
								<a href="javaScript:void(0)" title="导出所有成绩"
								onclick="printExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.classesid }','exportAll')">导出2</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">

$(document).ready(function(){
	examResultsManagerQueryBegin();
});
	//成绩审核
	function examResultsAuditManager(brSchool,grade,classic,major,courseId,courseName,teachType,classesid,operatType,examInfoId,examCourseType){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			if("all"==operatType){
				alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》所有成绩记录审核通过吗？", {
					okCall: function(){
						var url = "${baseUrl}/edu3/teaching/result/examresults-audit-pass.html?operatingType=all&examSubId="+examSubId+"&courseId="+courseId;
						url    += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic+"&classesid="+classesid+"&examInfoId="+examInfoId+"&examCourseType="+examCourseType;
						$.ajax({
							type:'POST',
							url:url,
							data:{},
							dataType:"json",
							cache: false,
							error: DWZ.ajaxError,
							success: function(resultData){
								var success  = resultData['success'];
								var msg      = resultData['msg'];
								if(success==false){
									alertMsg.warn(msg);
								}else{
									alertMsg.info(msg);
									navTabPageBreak();
								}
							}
						});
					}
				});
			}else{
				var url = "${baseUrl}/edu3/teaching/result/audit-examresults-list.html?operatingType=single&examSubId="+examSubId+"&branchSchool="+brSchool;
				url    += "&gradeid="+grade+"&classic="+classic+"&major="+major+"&courseId="+courseId+"&teachType="+teachType+"&classesid="+classesid+"&examInfoId="+examInfoId+"&examCourseType="+examCourseType;
				navTab.openTab("RES_TEACHING_RESULT_MANAGE_AUDIT_LIST",url,courseName+"成绩审核");	
			}
		}
	}
	
	//全部审核
	function examResultsAuditManagerOfAll(){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var brSchool     = "${condition['branchSchool']}";
		var examInfo     ="${examInfo.examInfoResourceId}";
		//var checkStatus = $("#examResultsCheckStatus").val();
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			var url = "${baseUrl}/edu3/teaching/result/examresults-batchAudit-pass.html?examSubId="+examSubId;
			var resIds = "";
			var k = 0;
			var num = $("#examResultsManagerBody input[name='resourceid']:checked").size();
			if(num>0){//按照勾选条件审核
				$("#examResultsManagerBody input[@name='resourceid']:checked").each(function(){
					var checekObj = $(this);
					resIds += checekObj.val();
			        if(k != num -1 ) {
			        	resIds += ",";
			        }
			        k++;
			    });
				//url += "&operatingType=checked&gradeIds"+gradeIds+"&classics"+classics+"&majorIds"+majorIds+"&classesIds"+classesIds+"&courseIds"+courseIds;
			}else{//按照查询条件审核
				num = "${page.totalCount}";
				//url += "&gradeId"+gradeId+"&classic"+classic+"&majorId"+majorId+"&classesId"+classesId+"&courseId"+courseId+"&checkStatus"+checkStatus;
				$('#examResultsManagerBody input[name="resourceid"]').each(function(){
					var checekObj = $(this);
					resIds += checekObj.val();
			        if(k != num -1 ) {
			        	resIds += ",";
			        }
			        k++;
			    });
			}
			url += "&operatingType=checked&resIds="+resIds;
			alertMsg.confirm("确认要将《<font color='red'>"+num+"</font>》条记录审核通过吗？", {
				okCall: function(){
					$.ajax({
						type:'POST',
						url:url,
						data:{},
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								alertMsg.info(msg);
								navTabPageBreak();
								//navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
							}
						}
					});
				}
			});
		}
	}
	
	//打印、导出复审后的总评成绩
	function printExamResultsReview(examInfoId,courseName,classesid,opreatingType){
		var examSubStatus     ="${examSub.examsubStatus}";
		var url               = "${baseUrl}/edu3/teaching/result/examresultsreview-print-view.html?examSubId=${examSub.resourceid}&examInfoId="+examInfoId;
		
		if(parseInt(examSubStatus) <= 2&&"printAll"!=opreatingType&&"exportAll"!=opreatingType){
			alertMsg.warn('该考试预约批次未关闭，不允许打印总评成绩！');
			return false;
		}else{
			var branchSchool = $("#examResultsManagerForNetWorkStudyTeachType_brSchoolName").val();
			var branchSchoolName = $("#examResultsManagerForNetWorkStudyTeachType_brSchoolName_flexselect").val();
			url += "&branchSchool="+branchSchool;
			if("export"==opreatingType||"exportAll"==opreatingType){
				url    =  "${baseUrl}/edu3/teaching/result/examresultsreview-print.html?operatingType="+opreatingType+"&examSubId=${examSub.resourceid}&examInfoId="+examInfoId+"&branchSchool="+branchSchool+"&classesid="+classesid;
				
				alertMsg.confirm("export"==opreatingType?"确定导出 "+branchSchoolName+" 《"+courseName+"》的复审成绩单？":"确定导出 "+branchSchoolName+" 《"+courseName+"》的总评成绩单？", {
					okCall: function(){
						downloadFileByIframe(url,'exportCourseExamResultsReviewForDownloadExportIframe');
					}
				});
			}else{
				$.pdialog.open(url+"&printType="+opreatingType+"&classesid="+classesid,"RES_TEACHING_RESULT_REVIEW","打印 "+branchSchoolName+" "+courseName+"总评成绩",{width:800, height:600});	
			}
		}
	}
	
	
	//调试成绩 
	function adjustmentExamResults(examInfoId,courseName){
		//if(isCheckOnlyone('resourceid','#examResultsManagerBody')){
			var examSubId 		= "${condition['examSubId']}";
			var examSubStatus   = jQuery("#examResultsManagerBody input[name='"+examSubId+"']:hidden").val()

			if(parseInt(examSubStatus) <= 2){
				alertMsg.warn('该考试预约批次未关闭，不允许调整成绩！');
			}else{
				var url = "${baseUrl}/edu3/teaching/examinfo/adjust/list.html?examSubId="+examSubId+"&examInfoId="+examInfoId;
				$.pdialog.open(url,"RES_TEACHING_RESULT_ADJUST_LIST",courseName+"成绩调整",{width:800, height:600});	
			}
		//}	
	}
	
	//重置成绩
	function resetExamResultsList(){
		if(isCheckOnlyone('resourceid','#examResultsManagerBody')){
			var examSubId = jQuery("#examResultsManagerBody input[name='resourceid']:checked").val();
			var examSubStatus     = jQuery("#examResultsManagerBody input[name='"+examSubId+"']:hidden").val()
			
			var url = "${baseUrl}/edu3/teaching/result/reset-examresults-list.html?examSubId="+examSubId;
			$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_RESULT_INPUT_RESULT","重置成绩",{width:800, height:600});	
			
		}	
	}

	//导入已审核的成绩
	function importCheckedResult(){
		if(isCheckOnlyone('resourceid','#examResultsManagerBody')){
			var examSubId = jQuery("#examResultsManagerBody input[name='resourceid']:checked").val();
			var examSubStatus     = jQuery("#examResultsManagerBody input[name='"+examSubId+"']:hidden").val()
			if(parseInt(examSubStatus) <= 2){
				alertMsg.warn('该考试预约批次未关闭，不允许导入成绩！');
			}else{
				var url = "${baseUrl}/edu3/teaching/transcripts/upload-checked-transcripts-view.html?examSubId="+examSubId;
				$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_IMPORT_RESULT_CHECKED","导入已审核成绩",{width:900, height:640});	
			}
		}	
	}
	//导出备份的成绩单
	function exPortResult(){
		var examSubStatus     = "${examSub.examsubStatus}";
		var examSubId         =  "${examSub.resourceid}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允操作！');
		}else{
			var url = "${baseUrl}/edu3/teaching/examresults/export-view.html?examSubId="+examSubId;
			$.pdialog.open(url,"RES_TEACHING_RESULT_MANAG_EXPORT_RESULT","导出成绩",{width:800, height:600});	
		}
	}
	//发布成绩
	function publishedExamResults(examInfoId,courseName){

		var examSubStatus     = "${examSub.examsubStatus}";
		var examSubId   	  = "${examSub.resourceid}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许发布成绩！');
		}else{
			alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》的成绩发布吗？", {
				okCall: function(){
					var url = "${baseUrl}/edu3/teaching/result/publish-examresults-save.html";
					$.ajax({
						type:'POST',
						url:url,
						data:{examInfoId:examInfoId,examSubId:examSubId},
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								alertMsg.info(msg);
								navTab.reload($("#examResultsManagerSearchForm").attr("action"), $("#examResultsManagerSearchForm").serializeArray());
							}
						}
					});
				}
			});
		}

	}
	function changeTeachingType1(){
		var examResultsManager_teachType = $("#examResultsManager_teachType").val();
		navTab.openTab('RES_TEACHING_RESULT_MANAGE', '${baseUrl}/edu3/teaching/result/list.html?teachType='+examResultsManager_teachType, '成绩审核');  
	}
	
	/* 撤销成绩 */
	function examResultsAuditManagerCancel(examInfoResourceId, courseName){
		alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》成绩撤销提交吗？", {
			okCall: function(){
				var url = "${baseUrl}/edu3/teaching/result/input-examresults-cancelsubmit.html?examInfoResourceId="+examInfoResourceId + "&courseName=" + encodeURI(courseName);
				$.ajax({
					type:'POST',
					url:url,
					data:{},
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(resultData){
						var success  = resultData['success'];
						var msg      = resultData['msg'];
						if(success==false){
							alertMsg.warn(msg);
						}else{
							alertMsg.info(msg);
							navTab.reload($("#examResultsManagerSearchForm").attr("action"), $("#examResultsManagerSearchForm").serializeArray());
						}
					}
				});
			}
		});
	}
</script>
</body>
</html>