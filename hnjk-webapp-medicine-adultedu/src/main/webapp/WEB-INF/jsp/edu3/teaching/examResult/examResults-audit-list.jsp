<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看已发布成绩</title>
<script type="text/javascript">
$(document).ready(function(){
	publishedExamResultsForAuditQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function publishedExamResultsForAuditQueryBegin() {
	var defaultValue = "${condition['brSchoolId']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool=true){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeId']}";
	var classicId = "${condition['classicId']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorId']}";
	var classesId = "${condition['classId']}";
	var selectIdsJson = "{unitId:'publishedExamResultsForAudit_school',gradeId:'publishedExamResultsForAudit_grade',classicId:'publishedExamResultsForAudit_classic',teachingType:'publishedExamResultsForAudit_teachingType',majorId:'publishedExamResultsForAudit_major',classesId:'examResults_audit_list_classid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function publishedExamResultsForAuditQueryUnit() {
	var defaultValue = $("#publishedExamResultsForAudit_school").val();
	var selectIdsJson = "{gradeId:'publishedExamResultsForAudit_grade',classicId:'publishedExamResultsForAudit_classic',teachingType:'publishedExamResultsForAudit_teachingType',majorId:'publishedExamResultsForAudit_major',classesId:'examResults_audit_list_classid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function publishedExamResultsForAuditQueryGrade() {
	var defaultValue = $("#publishedExamResultsForAudit_school").val();
	var gradeId = $("#publishedExamResultsForAudit_grade").val();
	var selectIdsJson = "{classicId:'publishedExamResultsForAudit_classic',teachingType:'publishedExamResultsForAudit_teachingType',majorId:'publishedExamResultsForAudit_major',classesId:'examResults_audit_list_classid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function publishedExamResultsForAuditQueryClassic() {
	var defaultValue = $("#publishedExamResultsForAudit_school").val();
	var gradeId = $("#publishedExamResultsForAudit_grade").val();
	var classicId = $("#publishedExamResultsForAudit_classic").val();
	var selectIdsJson = "{teachingType:'publishedExamResultsForAudit_teachingType',majorId:'publishedExamResultsForAudit_major',classesId:'examResults_audit_list_classid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function publishedExamResultsForAuditQueryTeachingType() {
	var defaultValue = $("#publishedExamResultsForAudit_school").val();
	var gradeId = $("#publishedExamResultsForAudit_grade").val();
	var classicId = $("#publishedExamResultsForAudit_classic").val();
	var teachingTypeId = $("#publishedExamResultsForAudit_teachingType").val();
	var selectIdsJson = "{majorId:'publishedExamResultsForAudit_major',classesId:'examResults_audit_list_classid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function publishedExamResultsForAuditQueryMajor() {
	var defaultValue = $("#publishedExamResultsForAudit_school").val();
	var gradeId = $("#publishedExamResultsForAudit_grade").val();
	var classicId = $("#publishedExamResultsForAudit_classic").val();
	var teachingTypeId = $("#publishedExamResultsForAudit_teachingType").val();
	var majorId = $("#publishedExamResultsForAudit_major").val();
	var selectIdsJson = "{classesId:'examResults_audit_list_classid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
	function editAuditExamResults(){
		var url = "${baseUrl}/edu3/teaching/examresult/correct/edit.html";
		if(isCheckOnlyone('resourceid','#publishedExamResultsForAuditBody')){
			navTab.openTab('RES_TEACHING_RESULT_CORRECT_EDIT'
					, url+'?resourceid='+$("#publishedExamResultsForAuditBody input[@name='resourceid']:checked").val()
							+'&examSubId='+$("#publishedExamResultsForAudit_ExamSub").val(), '更正成绩');
		}
	}
	//打印成绩审核
	function printAuditExamResults(){
		var url = "${baseUrl}/edu3/teaching/examresult/correct/printView.html";
		var examResultId = new Array();
		jQuery("#publishedExamResultsForAuditBody input[@name='resourceid']:checked").each(function(){
			examResultId.push(jQuery(this).val());
		});
		if(examResultId.length<=0){
			jQuery("#publishedExamResultsForAuditBody input[name='resourceid']").each(function(){
				examResultId.push(jQuery(this).val());
			});
		}
		$.pdialog.open(url+"?resourceids="+examResultId.toString(),"RES_TEACHING_EXAMRESULTS_AUDIT_VIEW","打印预览",{width:800, height:600});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 100px;">
			<form id="publishedExamResultsForAuditSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examresult/correct/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li class="custom-li"><label>教学点：</label> <span
								sel-id="publishedExamResultsForAudit_school"
								sel-name="brSchoolId"
								sel-onchange="publishedExamResultsForAuditQueryUnit()"
								sel-classs="flexselect"></span></li>
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="brSchoolId"
								id="publishedExamResultsForAudit_school"
								value="${condition['brSchoolId']}" />
						</c:if>
						<li><label>年级：</label> <span
							sel-id="publishedExamResultsForAudit_grade" sel-name="gradeId"
							sel-onchange="publishedExamResultsForAuditQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="publishedExamResultsForAudit_classic"
							sel-name="classicId"
							sel-onchange="publishedExamResultsForAuditQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li><label>学习形式：</label> <span
							sel-id="publishedExamResultsForAudit_teachingType"
							sel-name="teachingType"
							sel-onchange="publishedExamResultsForAuditQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 100px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="publishedExamResultsForAudit_major" sel-name="majorId"
							sel-onchange="publishedExamResultsForAuditQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>学号：</label> <input type="text" name="studyNo"
							value="${condition['studyNo'] }" style="width: 125px;"/></li>
						<li><label>姓名：</label> <input type="text" name="studentName"
							value="${condition['studentName'] }" style="width: 130px;"/></li>
						<li><label>考核方式：</label> <gh:select id="examClassType"
								name="examClassType" dictionaryCode="CodeExamClassType"
								value="${condition['examClassType']}"
								filtrationStr="1,2,3,4,5,6" style="width:100px" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="examResults_audit_list_classid" sel-name="classId"
							sel-classs="flexselect"></span></li>
						<li><label>考试批次：</label> <gh:selectModel
								id="publishedExamResultsForAudit_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:130px;"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='exam'"
								orderBy="examinputStartTime desc" /> <span style="color: red">*</span>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="publishedExamResultsForAudit_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:130px;" /></li>
						
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
			<gh:resAuth parentCode="RES_TEACHING_RESULT_CORRECT" pageType="list"></gh:resAuth>
			<table class="table" layouth="185">
				<thead>
					<tr>
						<th width="3%" align="center"><input type="checkbox"
							name="checkall" id="check_all_publishedExamResultsForAudit"
							onclick="checkboxAll('#check_all_publishedExamResultsForAudit','resourceid','#publishedExamResultsForAuditBody')" /></th>
						<th width="8%" align="center">考试批次</th>
						<th width="5%" align="center">年级</th>
						<th width="5%" align="center">层次</th>
						<th width="8%" align="center">专业</th>
						<!-- <th width="12%" align="center">班级名称</th>  -->
						<th width="5%" align="center">考核方式</th>
						<th width="5%" align="center">课程类型</th>
						<th width="10%" align="center">课程名称</th>
						<th width="8%" align="center">学号</th>
						<th width="6%" align="center">姓名</th>
						<th width="5%" align="center">卷面成绩</th>
						<th width="5%" align="center">平时成绩</th>
						<th width="5%" align="center">综合成绩</th>
						<th width="5%" align="center">成绩状态</th>
					</tr>
				</thead>
				<tbody id="publishedExamResultsForAuditBody" align="center">
					<c:forEach items="${examResultsList.result}" var="r" varStatus="vs">
						<tr>
							<td align="center"><input type="checkbox" name="resourceid"
								value="${r.resourceid }" autocomplete="off" /></td>
							<td align="center">${r.batchName }</td>
							<td align="center">${r.gradeName }</td>
							<td align="center">${r.classicName }</td>
							<td align="center">${r.majorName }</td>
							<%-- <td align="center">${r.classesname }</td> --%>
							<td align="center">${ghfn:dictCode2Val('CodeExamClassType',r.examClassType) }</td>
							<td align="center">${ghfn:dictCode2Val('CodeCourseTeachType',r.courseTeachType) }</td>
							<td align="center">${r.courseName }</td>
							<td align="center">${r.studyNo}</td>
							<td align="center">${r.studentName }</td>
							<td align="center"><c:choose>
									<c:when test="${r.isMixTrue eq 'Y' }">
			            			${r.writtenScore }(笔考:${r.writtenHandworkScore})
			            		</c:when>
									<c:otherwise>
			            			${r.writtenScore }
			            		</c:otherwise>
								</c:choose></td>
							<td align="center">${r.usuallyScore }</td>
							<td align="center"><c:choose>
									<c:when test="${r.integratedScore > 101.0 }">
			            				${ghfn:dictCode2Val('CodeScoreChar',r.integratedScore) }
			            		</c:when>
									<c:otherwise>
			            			${r.integratedScore}
			            		</c:otherwise>
								</c:choose></td>
							<td align="center">${ghfn:dictCode2Val('CodeExamAbnormity',r.examAbnormity) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examResultsList}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/correct/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>