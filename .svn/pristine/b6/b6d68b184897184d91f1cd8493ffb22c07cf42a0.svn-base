<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>
<style type="text/css">
	th,td{text-align: center;}
</style>
</head>
<body>
	<script type="text/javascript">
//打开页面或者点击查询（即加载页面执行）
function examResultsManager2QueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classic']}";
	
	var majorId = "${condition['major']}";
	var classesId = "${condition['classId']}";
	var selectIdsJson = "{unitId:'examResultManage2_brSchoolName',gradeId:'examResultManage2_gradeid',classicId:'examResultManage2_classic',majorId:'examResultManage2_major',classesId:'examResultManage2_classId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
}

// 选择教学点
function examResultsManager2QueryUnit() {
	var defaultValue = $("#examResultManage2_brSchoolName").val();
	var selectIdsJson = "{gradeId:'examResultManage2_gradeid',classicId:'examResultManage2_classic',majorId:'examResultManage2_major',classesId:'examResultManage2_classId'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function examResultsManager2QueryGrade() {
	var defaultValue = $("#examResultManage2_brSchoolName").val();
	var gradeId = $("#examResultManage2_gradeid").val();
	var selectIdsJson = "{classicId:'examResultManage2_classic',majorId:'examResultManage2_major',classesId:'examResultManage2_classId'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function examResultsManager2QueryClassic() {
	var defaultValue = $("#examResultManage2_brSchoolName").val();
	var gradeId = $("#examResultManage2_gradeid").val();
	var classicId = $("#examResultManage2_classic").val();
	var selectIdsJson = "{majorId:'examResultManage2_major',classesId:'examResultManage2_classId'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择专业
function examResultsManager2QueryMajor() {
	var defaultValue = $("#examResultManage2_brSchoolName").val();
	var gradeId = $("#examResultManage2_gradeid").val();
	var classicId = $("#examResultManage2_classic").val();
	
	var majorId = $("#examResultManage2_major").val();
	var selectIdsJson = "{classesId:'examResultManage2_classId'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
}

function submitValidate(obj){
	var teachType = $("#examResultManage2_teachType").val();
	var brSchoolName = $("#examResultManage2_brSchoolName").val();
	var ExamSub = $("#examResultManage2_ExamSub").val();
	if("${schoolCode}"=='10560'){
		brSchoolName = 'Y';
	}
	if(''==teachType || null==teachType||''==brSchoolName||''==ExamSub){
		alertMsg.warn("请在查询前填写必填项。");
		return false;
	}else{
		return navTabSearch(obj);
	}
}
</script>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="examResultsManagerForFaceTeachTypeSearchForm"
				onsubmit="return submitValidate(this);"
				action="${baseUrl}/edu3/teaching/result/list.html" method="post">
				<%-- <input id="examResultsManagerForFaceTeachTypeSearchForm_pageNum"    type="hidden" name="pageNum"    value="${page.pageNum}"/>--%>
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学方式：</label> <gh:select
								id="examResultManage2_teachType" name="teachType"
								value="${condition['teachType'] }" dictionaryCode="teachType"
								style="width:55%;" onchange="changeTeachingType()" /> <font
							color="red">*</font></li>
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学点：</label> <span
								sel-id="examResultManage2_brSchoolName" sel-name="branchSchool"
								sel-onchange="examResultsManager2QueryUnit()"
								sel-classs="flexselect"></span> <font
								color="red">*</font></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="examResultManage2_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>
						<li><label>年级：</label> <span
							sel-id="examResultManage2_gradeid" sel-name="gradeid"
							sel-onchange="examResultsManager2QueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="examResultManage2_classic" sel-name="classic"
							sel-onchange="examResultsManager2QueryClassic()"
							sel-style="width: 100px"></span></li>
					</ul>
					<ul class="searchContent">
						
						<li><label>考试批次：</label> <gh:selectModel
								id="examResultManage2_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="isDeleted='0' and batchType='exam' and examType='N'"
								orderBy="examinputStartTime desc" /> <font color="red">*</font>
						</li>
						<li class="custom-li"><label>专业：</label> <span sel-id="examResultManage2_major"
							sel-name="major" sel-onchange="examResultsManager2QueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>成绩状态：</label>
						<gh:select id="examResultsCheckStatus" name="checkStatus"
								value="${condition['checkStatus'] }"
								dictionaryCode="CodeExamResultCheckStatus" style="width:120px;" />
						</li>
						<li>
							教学形式：
							<gh:select id="examResultsCourseTeachType" name="courseTeachType"
									   value="${condition['courseTeachType'] }"
									   dictionaryCode="CodeCourseTeachType" style="width:120px;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examResultManage2_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:140px;" /></li>
						
						<li class="custom-li"><label>班级：</label> <span
							sel-id="examResultManage2_classId" sel-name="classId"
							sel-classs="flexselect"></span></li>

						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="162" width="100%">
				<thead>
					<tr>
						<%-- <th width="7%" style="text-align: center;vertical-align: middle;">年度</th>--%>
						<th width="3%"><input
							type="checkbox" name="checkall" id="check_all_examResultManager2"
							onclick="checkboxAll('#check_all_examResultManager2','resourceid','#examResultManager2Body')" /></th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="4%">层次</th>
						<th width="13%">班级</th>
						<th width="10%">专业</th>
						<th width="10%">课程</th>
						<th width="7%">任课老师</th>
						<%--<th width="4%">教学方式</th>--%>
						<th width="5%">教学形式</th>
						<th width="4%">应考</th>
						<th width="4%">实考</th>
						<th width="6%">成绩比</th>
						<th width="6%">保/提/发</th>
						<th width="10%">成绩审核</th>
						<%-- <th width="5%" >成绩发布</th>  --%>
						<%--<th width="12%">打印复审成绩</th>--%>
					</tr>
				</thead>
				<tbody id="examResultManager2Body">
					<c:forEach items="${page.result}" var="planCourse" varStatus="vs">
						<tr>
							<td><input
								type="checkbox" name="resourceid"
								value="${planCourse.examInfoId}"
								gradeid="${planCourse.gradeId }"
								classic="${planCourse.classicId}"
								majorid="${planCourse.majorId}"
								classesid="${planCourse.classesid}"
								courseid="${planCourse.courseId}"
								examInfoId="${planCourse.examInfoId }"
								teachType="${planCourse.teachType }" 
								examCourseType="${planCourse.examCourseType }"  autocomplete="off" /></td>
							<c:choose>
								<c:when test="${schoolCode eq '11846' }">
									<td title="${planCourse.unitCode }-${planCourse.unitName }">${planCourse.unitCode }-${planCourse.unitName }</td>
								</c:when>
								<c:otherwise>
									<td title="${planCourse.unitCode }-${planCourse.unitShortName }">${planCourse.unitCode }-${planCourse.unitShortName }</td>
								</c:otherwise>
							</c:choose>
							<td title="${planCourse.gradeName }">${planCourse.gradeName }</td>
							<td title="${planCourse.classicName}">${planCourse.classicName}</td>
							<td title="${planCourse.classesname}">${planCourse.classesname}</td>
							<td title="${planCourse.majorName}">${planCourse.majorName}</td>
							<td title="${planCourse.courseCode}-${planCourse.courseName}">
								<a href="javaScript:void(0)" style="color: blue"
								onclick="showOperateRecord('${planCourse.classesid }','${planCourse.classesname}','${planCourse.courseId}','${planCourse.courseName}','${planCourse.examsubId }')">${planCourse.courseName}</a>
							</td>
							<td title="${planCourse.lecturerName}">${planCourse.lecturerName}</td>
							<%--<td>${ghfn:dictCode2Val('teachType',condition['teachType'])}</td>--%>
							<td title="${planCourse.teachType}">${ghfn:dictCode2Val('CodeCourseTeachType',planCourse.teachType)}</td>
							<td title="应当参加考试的人数，不包括休学和退学学生">${planCourse.headExam}</td>
							<td title="实际参加考试的人数，不包括缺考、缓考">${planCourse.realExam}</td>
							<td title="卷面成绩 : 平时成绩 = ${planCourse.facestudyscoreper } : ${planCourse.facestudyscoreper2 }">
									${planCourse.facestudyscoreper } : ${planCourse.facestudyscoreper2 }</td>
							<td title="保存状态${planCourse.checkStatus_save}人；提交状态${planCourse.checkStatus_submit}人；发布状态${planCourse.checkStatus_publish}人">
								${planCourse.checkStatus_save}/<font color="green">${planCourse.checkStatus_submit}</font>/<font color="blue">${planCourse.checkStatus_publish}</font>
								<%--<c:if test="${planCourse.checkStatus_save ne 0}"><font style="color: blue">保存</font>(${planCourse.checkStatus_save});</c:if>
								<c:if test="${planCourse.checkStatus_submit ne 0}"><font style="color: green">提交</font>(${planCourse.checkStatus_submit});</c:if>
								<c:if test="${planCourse.checkStatus_publish ne 0}"><font style="color: green">发布</font>(${planCourse.checkStatus_publish})</c:if>--%>
							</td>
							<td><a href="javaScript:void(0)" title="个别审核，不包括退学学生"
									onclick="examResultsAuditManagerForFaceTeachType('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType'] }','${planCourse.teachType }','${planCourse.classesid }','single','${planCourse.examInfoId }')">单审</a>&nbsp;&nbsp;
								<a href="javaScript:void(0)" title="全部通过（不包括保存状态的成绩）" style="color: green"
									onclick="examResultsAuditManagerForFaceTeachType('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType'] }','${planCourse.teachType }','${planCourse.classesid }','all','${planCourse.examInfoId }')">通过</a>&nbsp;&nbsp;
								<c:set var="chStatus" value="CheckStatus${statusKey}" scope="page"></c:set> 
								<a href="javaScript:void(0)" title="撤销提交" style="color: red"
									onclick="examResultsAuditManagerCancel2('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType'] }','${planCourse.classesid }','${planCourse.checkStatus_publish}')">撤回</a>
							</td>
							<%--
				            <td style="text-align: center;vertical-align: middle;">
								<a href="javaScript:void(0)"  onclick="publishedExamResultsForFaceTeachType('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType']}')">发布</a>
				            </td>
				            --%>
							<%--<td><a
								href="javaScript:void(0)"
								class="_printExamResultsReviewForFaceCourse" title="打印复审成绩"
								onclick="printExamResultsReviewForFaceCourse('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType']}','${planCourse.classesid }','${planCourse.examInfoId }','printReview')">打印1</a>
								<a href="javaScript:void(0)"
								class="_printExamResultsReviewForFaceCourse" title="打印所有成绩"
								onclick="printExamResultsReviewForFaceCourse('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType']}','${planCourse.classesid }','${planCourse.examInfoId }','printAll')">打印2</a>
								<a href="javaScript:void(0)"
								class="_printExamResultsReviewForFaceCourse" title="导出复审成绩"
								onclick="printExamResultsReviewForFaceCourse('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType']}','${planCourse.classesid }','${planCourse.examInfoId }','export')">导出1</a>
								<a href="javaScript:void(0)"
								class="_printExamResultsReviewForFaceCourse" title="导出所有成绩"
								onclick="printExamResultsReviewForFaceCourse('${condition['branchSchool']}','${planCourse.gradeId}','${planCourse.classicId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${condition['teachType']}','${planCourse.classesid }','${planCourse.examInfoId }','exportAll')">导出2</a>
							</td>--%>
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
		examResultsManager2QueryBegin();
		//$('._printExamResultsReviewForFaceCourse').poshytip({className: 'tip-yellowsimple',showTimeout: 1,alignTo: 'target',	alignX: 'left',	alignY: 'center',	offsetX: 5,allowTipHover: false});
	});

	//成绩审核
	function examResultsAuditManagerForFaceTeachType(brSchool,grade,classic,major,courseId,courseName,pc_teachType,cs_teachType,classesid,operatType,examInfoId){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			if("all"==operatType){
				alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》所有成绩记录审核通过吗？", {
					okCall: function(){
						var url = "${baseUrl}/edu3/teaching/result/examresults-audit-pass.html?operatingType=all&examSubId="+examSubId+"&courseId="+courseId+"&pc_teachType="+pc_teachType;
						url    += "&cs_teachType="+cs_teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic+"&classesid="+classesid+"&examInfoId="+examInfoId;
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
			}else{
				var url = "${baseUrl}/edu3/teaching/result/audit-examresults-list.html?operatingType=single&examSubId="+examSubId+"&branchSchool="+brSchool+"&pc_teachType="+pc_teachType;
				url    += "&gradeid="+grade+"&classic="+classic+"&major="+major+"&courseId="+courseId+"&cs_teachType="+cs_teachType+"&classesid="+classesid+"&examInfoId="+examInfoId;
				navTab.openTab("RES_TEACHING_RESULT_MANAGE_AUDIT_FORFACTCOURSE_LIST",url,courseName+"成绩审核");	
			}
		}
	}
	
	//全部审核
	function examResultsAuditManagerOfAll(){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var brSchool     = "${condition['branchSchool']}";
		var gradeId = "${condition['gradeid']}";
		var classic = "${condition['classic']}";
		var majorId = "${condition['major']}";
		var courseId = "${condition['courseId']}";
		var classesId = "${condition['classId']}";
		var teachType = "${condition['teachType']}";
		var checkStatus = $("#examResultsCheckStatus").val();
		var dataUrl="";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			var url = "${baseUrl}/edu3/teaching/result/examresults-batchAudit.html?examSubId="+examSubId+"&branchSchool="+brSchool+"&teachType="+teachType+"&auditType=pass";
			var resIds = "";
			var gradeIds = "";
			var classics = "";
			var majorIds = "";
			var classesIds = "";
			var courseIds = "";
			var examCourseType = "";
			var k = 0;
			var num = $("#examResultManager2Body input[name='resourceid']:checked").size();
			if(num>0){//按照勾选条件审核
				/* if (num>30) {
					alertMsg.warn('请勾选30条以下的数据进行操作');
					return;
				} */
				$("#examResultManager2Body input[@name='resourceid']:checked").each(function(){
					var checekObj = $(this);
					resIds += checekObj.val();
		    		gradeIds += checekObj.attr("gradeid");
		    		classics += checekObj.attr("classic");
		    		majorIds += checekObj.attr("majorid");
		    		classesIds += checekObj.attr("classesid");
					courseIds += checekObj.attr("courseid");
					examCourseType += checekObj.attr("examCourseType");
			        if(k != num -1 ) {
			        	resIds += ",";
			    		gradeIds += ",";
			    		classics += ",";
			    		majorIds += ",";
			    		classesIds += ",";
						courseIds += ",";
						examCourseType += ",";
			        }
			        k++;
			    });
				url+="&type=checked";
				//url += "&type=checked&resIds="+resIds+"&gradeIds="+gradeIds+"&classics="+classics+"&majorIds="+majorIds+"&classesIds="+classesIds+"&courseIds="+courseIds;
				//dataUrl="&resIds="+resIds+"&courseIds="+courseIds+"&gradeIds="+gradeIds+"&majorIds="+majorIds+"&classics="+classics+"&classesIds="+classesIds;
			}else{//按照查询条件审核
				num = "${page.totalCount}";
				url += "&type=query&gradeid="+gradeId+"&classic="+classic+"&major="+majorId+"&classesid="+classesId+"&courseId="+courseId+"&checkStatus="+checkStatus;
			}
			alertMsg.confirm("确认要将<font color='red'>"+num+"</font>条记录审核通过吗？", {
				okCall: function(){
					$.ajax({
						type:'POST',
						url:url,
						data:{'resIds':resIds,'courseIds':courseIds,'gradeIds':gradeIds,'majorIds':majorIds,'classics':classics,'classesIds':classesIds,'teachTypes':examCourseType},
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
	function printExamResultsReviewForFaceCourse(brSchool,grade,classic,major,courseId,courseName,teachType,classesid,examInfoId,opreatingType){
		var examSubStatus     ="${examSub.examsubStatus}";
		var examSubId         = "${examSub.resourceid}";
		var url               = "${baseUrl}/edu3/teaching/result/examresultsreviewForFaceCourse-print-view.html";
		if("export"==opreatingType||"exportAll"==opreatingType){
			url    =  "${baseUrl}/edu3/teaching/result/examresultsreviewForFaceCourse-print.html";
		}
		url += "?examSubId=${examSub.resourceid}&examInfoId="+examInfoId+"&courseId="+courseId+"&printType="+opreatingType;
		url   			 += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic+"&classesid="+classesid;
		
		if(parseInt(examSubStatus) <= 2&&"printAll"!=opreatingType&&"exportAll"!=opreatingType){
			alertMsg.warn('该考试预约批次未关闭，不允许打印总评成绩！');
		}else{
			if("export"==opreatingType||"exportAll"==opreatingType){
				alertMsg.confirm("确定导出《"+courseName+"》的总评成绩单？", {
					okCall: function(){
						downloadFileByIframe(url,'exportCourseExamResultsReviewForDownloadExportIframe');						
					}
				});
			}else{
				$.pdialog.open(url,"RES_TEACHING_RESULT_REVIEW_FOR_FACECOURSE","打印"+courseName+"总评成绩",{width:800, height:600});	
			}
			
		}
	}
	
	
	//发布成绩
	function publishedExamResultsForFaceTeachType(brSchool,grade,classic,major,courseId,courseName,teachType){

		var examSubId         = "${examSub.resourceid}";
		var examSubStatus     = "${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许发布成绩！');
		}else{
			alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》的成绩发布吗？", {
				okCall: function(){
					var url = "${baseUrl}/edu3/teaching/result/publish-examresults-save.html";
					$.ajax({
						type:'POST',
						url:url,
						data:{examSubId:examSubId,courseId:courseId,branchSchool:brSchool,gradeid:grade,major:major,classic:classic,teachType:teachType},
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
								navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
							}
						}
					});
				}
			});
		}
	}
	//撤销已发布成绩
	function canaelAuditPublishedExamResults(){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var brSchool     = "${condition['branchSchool']}";
		var teachType = "${condition['teachType']}";
		var url = "${baseUrl}/edu3/teaching/result/examresults-batchAudit.html?examSubId="+examSubId+"&branchSchool="+brSchool+"&teachType="+teachType+"&auditType=cancel";
		var resIds = "";
		var gradeIds = "";
		var classics = "";
		var majorIds = "";
		var classesIds = "";
		var courseIds = "";
		var k = 0;
		var num = $("#examResultManager2Body input[name='resourceid']:checked").size();
		if(num<=0){
			alertMsg.confirm("请您至少选择一条记录进行操作！");
			return;
		}
		$("#examResultManager2Body input[@name='resourceid']:checked").each(function(){
			var checekObj = $(this);
			resIds += checekObj.val();
    		gradeIds += checekObj.attr("gradeid");
    		classics += checekObj.attr("classic");
    		majorIds += checekObj.attr("majorid");
    		classesIds += checekObj.attr("classesid");
			courseIds += checekObj.attr("courseid");
	        if(k != num -1 ) {
	        	resIds += ",";
	    		gradeIds += ",";
	    		classics += ",";
	    		majorIds += ",";
	    		classesIds += ",";
				courseIds += ",";
	        }
	        k++;
	    });
		url+="&type=checked";
		alertMsg.confirm("确认要将<font color='red'>"+num+"</font>条记录撤销吗？", {
			okCall: function(){
				$.ajax({
					type:'POST',
					url:url,
					data:{'resIds':resIds,'courseIds':courseIds,'gradeIds':gradeIds,'majorIds':majorIds,'classics':classics,'classesIds':classesIds},
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
						}
						navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
					}
				});
			}
		});
	}
	
	function erm_brschool_Major(){
		var unitId = $("#examResultManage2_brSchoolName").val();
		var majorId = $("#examResultManage2_major").val();
		var url = "${baseUrl}/edu3/teaching/teachinggrade/brschool_Major.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	$("#examResultManage2_major").replaceWith("<select  class=\"flexselect textInput\" id=examResultsManagerForFaceTeachType_major name=\"major\" tabindex=1 style=width:55% >"+data['majorOption']+"</select>");
			    	$("#examResultManage2_major_flexselect").remove();
			    	$("#examResultManage2_major_flexselect_dropdown").remove();
			    	$("#examResultManage2_major").flexselect();
				}
			}
		});
		
	}
	function changeTeachingType(){
		var examResultsManagerForFaceTeachType_teachType = $("#examResultManage2_teachType").val();
		navTab.openTab('RES_TEACHING_RESULT_MANAGE', '${baseUrl}/edu3/teaching/result/list.html?teachType='+examResultsManagerForFaceTeachType_teachType, '成绩审核');  
	}

	/* 撤销成绩 */
	function examResultsAuditManagerCancel2(brSchool,grade,classic,major,courseId,courseName,teachType,classesid,publishNum){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var teachingtype = "${condition['teachType'] }";
		//alert(examSubId);
		//alert(examSubStatus);
		//alert(teachingtype);
		//var status = $("#examResultStatus"+index).val();
		if(publishNum>0){
			alertMsg.warn("成绩已发布，无法撤销提交！");
			//alert(status);
			return false;
		}
		alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》成绩撤销提交吗？", {
			okCall: function(){
				var url = "${baseUrl}/edu3/teaching/result/input-examresults-cancelsubmit2.html?examSubId="+examSubId+"&courseId="+courseId;
				url    += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic+"&classesid="+classesid;
				//alert(url);
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
							navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
						}
					}
				});
			}
		})
	}
	function showOperateRecord(classesid,classesname,courseId,courseName,examsubId){
		var url = "${baseUrl}/edu3/teaching/result/operateRecord.html?classesid="+classesid+"&courseId="+courseId+"&examsubId="+examsubId;
		$.pdialog.open(url,"RES_TEACHING_OPERATE_RECORD",classesname+"\t【"+courseName+"】\t课程操作记录",{width:800, height:600});	
	}
	
</script>
</body>
</html>