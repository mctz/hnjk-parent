<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统考课程认定</title>
<script type="text/javascript">
$(document).ready(function(){
	appUseConditionQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function appUseConditionQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "${linkageQuerySchoolId}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classic']}";
	var teachingType = "${condition['learningStyle']}";
	var majorId = "${condition['major']}";
	var classesId = "${condition['classes']}";
	var selectIdsJson = "{unitId:'state_examresults_brSchoolId',gradeId:'state_examresults_gradeid',classicId:'state_examresults_classicid',teachingType:'state_examresults_learningStyle',majorId:'state_examresults_majorid',classesId:'state_examresults_classesid'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function appUseConditionQueryUnit() {
	var defaultValue = $("#state_examresults_brSchoolId").val();
	var selectIdsJson = "{gradeId:'state_examresults_gradeid',classicId:'state_examresults_classicid',teachingType:'state_examresults_learningStyle',majorId:'state_examresults_majorid',classesId:'state_examresults_classesid'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function appUseConditionQueryGrade() {
	var defaultValue = $("#state_examresults_brSchoolId").val();
	var gradeId = $("#state_examresults_gradeid").val();
	var selectIdsJson = "{classicId:'state_examresults_classicid',teachingType:'state_examresults_learningStyle',majorId:'state_examresults_majorid',classesId:'state_examresults_classesid'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function appUseConditionQueryClassic() {
	var defaultValue = $("#state_examresults_brSchoolId").val();
	var gradeId = $("#state_examresults_gradeid").val();
	var classicId = $("#state_examresults_classicid").val();
	var selectIdsJson = "{teachingType:'state_examresults_learningStyle',majorId:'state_examresults_majorid',classesId:'state_examresults_classesid'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

// 选择学习形式
function appUseConditionQueryTeachingType() {
	var defaultValue = $("#state_examresults_brSchoolId").val();
	var gradeId = $("#state_examresults_gradeid").val();
	var classicId = $("#state_examresults_classicid").val();
	var teachingTypeId = $("#state_examresults_learningStyle").val();
	var selectIdsJson = "{majorId:'state_examresults_majorid',classesId:'state_examresults_classesid'}";
	cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
}

// 选择专业
function appUseConditionQueryMajor() {
	var defaultValue = $("#state_examresults_brSchoolId").val();
	var gradeId = $("#state_examresults_gradeid").val();
	var classicId = $("#state_examresults_classicid").val();
	var teachingTypeId = $("#state_examresults_learningStyle").val();
	var majorId = $("#state_examresults_majorid").val();
	var selectIdsJson = "{classesId:'state_examresults_classesid'}";
	cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="resetExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/result/statexam/maintain-list.html"
				method="post">
				<input name="courseId" value="${condition['courseId']}"
					type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <c:choose>
								<c:when test="${!isBrschool }">
									<span
										sel-id="state_examresults_brSchoolId" sel-name="branchSchool"
										sel-onchange="appUseConditionQueryUnit()"
										sel-classs="flexselect" sel-style="width: 120px"></span>
								</c:when>
								<c:otherwise>
									<input type="text" value="${unit.unitCode }-${unit.unitName}"
										style="width: 50%" readonly="readonly" />
									<input type="hidden" name="branchSchool"
										id="state_examresults_brSchoolId" value="${unit.resourceid}" />
								</c:otherwise>
							</c:choose></li>
						<li><label>年级：</label>
							<span sel-id="state_examresults_gradeid"
								sel-name="gradeid" sel-onchange="appUseConditionQueryGrade()"
								sel-style="width: 120px"></span>
						</li>
						<li><label>层 次：</label>
							<span sel-id="state_examresults_classicid"
								sel-name="classic" sel-onchange="appUseConditionQueryClassic()"
								sel-style="width: 120px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>学习方式：</label>
							<span
								sel-id="state_examresults_learningStyle" sel-name="learningStyle"
								sel-onchange="appUseConditionQueryTeachingType()"
								dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						<li><label>专 业：</label>
							<span sel-id="state_examresults_majorid"
								sel-name="major" sel-onchange="appUseConditionQueryMajor()"
								sel-classs="flexselect" sel-style="width: 120px"></span>
						</li>
						<li><label>班 级：</label>
							<span sel-id="state_examresults_classesid"
								sel-name="classes" sel-classs="flexselect"
								sel-style="width: 120px"></span> 
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 50%" /></li>
						<li><label>姓名：</label><input type="text" name="stuName"
							value="${condition['stuName']}" style="width: 50%" /></li>
						<li><label>学籍状态：</label> <gh:select name="studentStatus"
								dictionaryCode="CodeStudentStatus" choose="Y"
								value="${condition['studentStatus']}" style="width:53%" /></li>
					</ul>
					<ul class="searchContent">
						<!-- 				<li> -->
						<!-- 					<label>成绩类型：</label> -->
						<%-- 					 <gh:select name="scoreType"  dictionaryCode="StateExamResultsScoreType" choose="Y" value="${condition['scoreType']}" style="width:53%"/>  --%>
						<!-- 				</li> -->
						<li><label>通过时间：</label> <input type="text" id="passtime"
							name="passtime" value="${condition['passtime']}"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})"
							style="width: 50%" /></li>
						<li><label>是否认定：</label> <gh:select name="isIdented"
							dictionaryCode="yesOrNo" choose="Y"
							value="${condition['isIdented']}" style="width:53%" /></li>
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
		<%-- 
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="icon" href="javascript:void(0)" onclick="saveExaminationMaintain();"><span>保存选项</span></a></li>
			<li><a class="icon" href="javascript:void(0)" onclick="delExaminationMaintain();"><span>取消认定</span></a></li>
			<li><a class="icon" href="javascript:void(0)" onclick="downloadExaminationMaintain();"><span>下载成绩列表</span></a></li>
		</ul>
	</div>
	--%>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_RESULT_STATEXAM"
				pageType="maintainList"></gh:resAuth>
			<table class="table" layouth="214" width="100%">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_stateExamResults"
							onclick="checkboxAll('#check_all_stateExamResults','resourceid','#examinationMiantainListBody')" /></th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="3%">性别</th>
						<th width="10%">所属分教点</th>
						<th width="8%">层次</th>
						<th width="6%">年级</th>
						<th width="10%">专业</th>
						<th width="5%">学习方式</th>
						<th width="8%">学籍状态</th>
						<th width="6%">是否认定</th>
						<th width="8%">通过时间</th>
						<th width="5%">修读结果</th>
						<th width="4%">成绩</th>
						<th width="5%">备注</th>
					</tr>
				</thead>
				<tbody id="examinationMiantainListBody">
					<c:forEach items="${objPage.result}" var="stateExamResults"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stateExamResults.resourceid }"
								studentids="${stateExamResults.studentInfo.resourceid }"
								autocomplete="off" /></td>
							<td>${stateExamResults.studentInfo.studyNo }</td>
							<td>${stateExamResults.studentInfo.studentName }</td>
							<td>${ghfn:dictCode2Val('CodeSex',stateExamResults.studentInfo.studentBaseInfo.gender) }</td>
							<td>${stateExamResults.studentInfo.branchSchool.unitName }</td>
							<td>${stateExamResults.studentInfo.classic.classicName }</td>
							<td>${stateExamResults.studentInfo.grade.gradeName }</td>
							<td>${stateExamResults.studentInfo.major.majorName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningStyle',stateExamResults.studentInfo.learningStyle) }</td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stateExamResults.studentInfo.studentStatus) }</td>
							<td><c:choose>
									<c:when test="${stateExamResults.isIdented eq 'Y' }">是</c:when>
									<c:otherwise>否</c:otherwise>
								</c:choose></td>
							<td><fmt:formatDate value="${stateExamResults.passtime}" pattern="yyyy-MM-dd" /></td>
							<%-- <td>${ghfn:dictCode2Val('StateExamResultsScoreType',stateExamResults.scoreType) }</td> --%>
							<td>${ghfn:dictCode2Val('CodeAllowStateExamresultsImportSore',stateExamResults.scoreType) }</td>
							<td>${stateExamResults.score}</td>
							<td>${stateExamResults.memo}</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/statexam/maintain-list.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/statexam/maintain-list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
	<script type="text/javascript">
	
    //保存选项
    function saveExaminationMaintain(){
   		var url = "${baseUrl }/edu3/teaching/result/statexam/maintain-save.html";
    	var stateExamResults = new Array();
    	var studentids = new Array();
    	var cid = "${condition['courseId']}";
		jQuery("#examinationMiantainListBody input[name='resourceid']:checked").each(function(){
			stateExamResults.push(jQuery(this).val());
			studentids.push($(this).attr('studentids'));
		});
		if(stateExamResults.length>0){
			$.ajax({
				type:'POST',
				url:url,
				data:{stateExamResultsIds:stateExamResults.toString(),courseId:cid,studentids:studentids.toString()},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					var statusCode = resultData['statusCode'];
					if(success==true&&statusCode==200){
						navTab.reload($("#resetExamResultsSearchForm").attr("action"),$("#resetExamResultsSearchForm").serializeArray());
					}else{
						alertMsg.warn(msg);
					}
				}
			});
		}else{
			alertMsg.warn("请选择至少一个统考成绩!");
		}
    }
    //取消认定
    function delExaminationMaintain(){
    	var url = "${baseUrl }/edu3/teaching/result/statexam/maintain-del.html";
    	var stateExamResults = new Array();
    	var cid = "${condition['courseId']}";
		jQuery("#examinationMiantainListBody input[name='resourceid']:checked").each(function(){
			stateExamResults.push(jQuery(this).val());
		});
		if(stateExamResults.length>0){
			$.ajax({
				type:'POST',
				url:url,
				data:{stateExamResultsIds:stateExamResults.toString(),courseId:cid},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success:function(resultData){
					var success = resultData['success'];
					var msg     = resultData['msg'];
					if(success==true){
						navTab.reload($("#resetExamResultsSearchForm").attr("action"),$("#resetExamResultsSearchForm").serializeArray());
					}else{
						alertMsg.warn(msg);
					}
				}
			});
		}else{
			alertMsg.warn("请选择至少一个统考成绩!");
		}
    }
    function downloadExaminationMaintain(){
    	var branchSchool = $("#resetExamResultsSearchForm #state_examresults_brSchoolId").val();
    	var gradeid=$("#resetExamResultsSearchForm #state_examresults_gradeid").val();
    	var classic=$("#resetExamResultsSearchForm #state_examresults_classicid").val();
    	var learningStyle = $("#state_examresults_learningStyle").val();
    	var major = $("#resetExamResultsSearchForm #state_examresults_majorid").val();
    	var classes = $("#resetExamResultsSearchForm #state_examresults_classesid").val();
    	var url = "${baseUrl}/edu3/teaching/result/downloadExaminationMaintain.html?branchSchool="+branchSchool+"&gradeid="+gradeid+"&classic="+classic+"&learningStyle="+learningStyle+"&major="+major+"&classes="+classes;
    	alertMsg.confirm("确定按查询条件导出学位外语成绩单？",{
    		okCall:function(){
    			downloadFileByIframe(url,'downloadExaminationMaintainIframe');
    		}
    	});
    	
    }
</script>
</body>
</html>