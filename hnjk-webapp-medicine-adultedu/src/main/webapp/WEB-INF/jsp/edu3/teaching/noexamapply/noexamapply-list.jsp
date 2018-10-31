<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>免考申请</title>
<script type="text/javascript">	
$(document).ready(function(){
	noexamapplyQueryBegin();
});
	
//打开页面或者点击查询（即加载页面执行）
function noexamapplyQueryBegin() {
	var defaultValue = "${condition['branchSchool']}";
	var schoolId = "";
	var brshSchool = "${condition['brshSchool']}";
	if(brshSchool=='Y'){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['grade']}";
	var classicId = "${condition['classic']}";
	
	var majorId = "${condition['major']}";
	
	var selectIdsJson = "{unitId:'noexamapply_brSchoolName',gradeId:'gradeId',classicId:'classicId',majorId:'majorId'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, "", selectIdsJson);
}

// 选择教学点
function noexamapplyQueryUnit() {
	var defaultValue = $("#noexamapply_brSchoolName").val();
	var selectIdsJson = "{gradeId:'gradeId',classicId:'classicId',majorId:'majorId'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function noexamapplyQueryGrade() {
	var defaultValue = $("#noexamapply_brSchoolName").val();
	var gradeId = $("#gradeId").val();
	var selectIdsJson = "{classicId:'classicId',majorId:'majorId'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function noexamapplyQueryClassic() {
	var defaultValue = $("#noexamapply_brSchoolName").val();
	var gradeId = $("#gradeId").val();
	var classicId = $("#classicId").val();
	var selectIdsJson = "{majorId:'majorId'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

	//修改
	function ExamApply_edit(){
		var url = "${baseUrl}/edu3/teaching/noexamapply/edit.html";
		if(isCheckOnlyone('resourceid','#neaBody777')){
			navTab.openTab('RES_STUDENT_APPLY_NOEXAM_EDIT', url+'?resourceid='+$("#neaBody777 input[name='resourceid']:checked").val(), '编辑免修免考');
		}		
	}
	//删除
	function ExamApply_del(){	
		var obj = $("#neaBody777 input[@name='resourceid']:checked");

		if(obj.attr("title") == '1'){
			alertMsg.warn('学生的申请已审核通过，不能删除！');
			return false;
		}
		
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/noexamapply/delete.html","#neaBody777");
	}
	//审核通过
	function ExamApply_audit(){
		pageBarHandle("确定要审核通过吗？","${baseUrl}/edu3/teaching/noexamapply/audit.html","#neaBody777");
	}
	//审核不通过状态
	function ExamApply_unAccept(){
		if("${condition['brshSchool'] eq 'Y' }" == "true"){
			pageBarHandle("确定要修改为审核不通过吗？","${baseUrl}/edu3/teaching/noexamapply/unAcceptable.html?isBrschool=1","#neaBody777");
		}else{
			pageBarHandle("确定要修改为审核不通过吗？","${baseUrl}/edu3/teaching/noexamapply/unAcceptable.html?","#neaBody777");
		}
	}
	//取消审核
	function ExamApply_cancel(){
		pageBarHandle("确定要取消审核吗？","${baseUrl}/edu3/teaching/noexamapply/cancel.html","#neaBody777");
	}

	//查看详细的免试申请信息
	function showNoExamApplyDetails(rid){
		var url = "${baseUrl}/edu3/teaching/noexamapply/edit.html?onlyView=1";
			navTab.openTab('RES_STUDENT_APPLY_NOEXAM_VIEWDETAIL', url+'&resourceid='+rid, '查看免考申请信息');	
	}
	//修改详细的免考申请信息
	function editNoExamApplyDetails(rid){
		var url = "${baseUrl}/edu3/teaching/noexamapply/edit.html?";
			navTab.openTab('RES_STUDENT_APPLY_NOEXAM_VIEWDETAIL', url+'&resourceid='+rid, '修改免考申请信息');	
	}
	
	//导入
	function noExamApplyImport(){
		var url = "${baseUrl}/edu3/teaching/noexamapply/import/form.html"
		$.pdialog.open(url,"RES_STUDENT_APPLY_NOEXAM_EXPORT_TEMPLATES","导入免修免考",{width:600, height:400});
	}
	//导出
	function exportNoexam(){
		alertMsg.confirm("确定要导出查询条件下的免修免考记录吗？",{
            okCall: function(){
            	var url = "${baseUrl}/edu3/teaching/noexamapply/list-export.html?"+$("#brSchool_noExamList_search_form").serialize();
        		downloadFileByIframe(url,'noExamAppListExportIframe');
            }
         });
	}
	//免试申请进入等待审核状态(停用)
	function ExamApply_enterAudit(){
		pageBarHandle("确定要修改为等待审核吗？","${baseUrl}/edu3/teaching/noexamapply/audit.html?isBrschool=1","#neaBody777");
	}
	//免试申请取消等待审核状态(停用)
	function ExamApply_enterCancle(){
	    pageBarHandle("确定要取消等待审核吗？","${baseUrl}/edu3/teaching/noexamapply/cancel.html?isBrschool=1","#neaBody777");
	}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="brSchool_noExamList_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/noexamapply/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="noexamapply_brSchoolName" sel-name="branchSchool"
								sel-onchange="noexamapplyQueryUnit()" sel-classs="flexselect"
								></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="noexamapply_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>

						<li><label>年级：</label> <span sel-id="gradeId"
							sel-name="grade" sel-onchange="noexamapplyQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classicId"
							sel-name="classic" sel-onchange="noexamapplyQueryClassic()"
							sel-style="width: 120px"></span></li>

					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="majorId"
							sel-name="major" sel-classs="flexselect" ></span>
						</li>
						<li><label>学号：</label><input type="text" name="stuNo"
							value="${condition['stuNo']}" style="width: 53%" /></li>

						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 53%" /></li>
						<li><label>审核状态：</label> <gh:select
								dictionaryCode="CodeCheckStatus" name="checkStatus"
								value="${condition['checkStatus'] }" filtrationStr="0,1,2"
								style="width:100px;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>申请时间：</label> <input type="text"
							name="appStartTime" value="${condition['appStartTime']}"
							class="Wdate" id="appNoExam_startTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'appNoExam_endTime\')}'})"
							style="width: 80px;" /> 至 <input type="text" name="appEndTime"
							value="${condition['appEndTime']}" class="Wdate"
							id="appNoExam_endTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'appNoExam_startTime\')}'})"
							style="width: 80px;" /></li>
						<li class="custom-li"><label>审核时间：</label> <input type="text"
							name="auditStartTime" value="${condition['auditStartTime']}"
							class="Wdate" id="auditNoExam_startTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'auditExam_endTime\')}'})"
							style="width: 80px;" /> 至 <input type="text" name="auditEndTime"
							value="${condition['auditEndTime']}" class="Wdate"
							id="auditExam_endTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'auditNoExam_startTime\')}'})"
							style="width: 80px;" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_RESULT_NOEXAM" pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_exa"
							onclick="checkboxAll('#check_all_exa','resourceid','#neaBody777')" /></th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="9%">专业</th>
						<th width="6%">层次</th>
						<th width="10%">学号</th>
						<th width="6%">姓名</th>
						<th width="9%">课程名称</th>
						<th width="6%" align="center">申请类型</th>
						<th width="6%" align="center">成绩</th>
						<th width="8%" align="center">申请时间</th>
						<th width="8%" align="center">审核时间</th>
						<th width="6%" align="center">审核状态</th>
						<th width="6%" align="center">查看详情</th>
					</tr>
				</thead>
				<tbody id="neaBody777">
					<c:forEach items="${applyList.result}" var="a" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${a.resourceid }" title="${a.checkStatus }"
								autocomplete="off" /></td>
							<td title="${a.studentInfo.branchSchool }">${a.studentInfo.branchSchool }</td>
							<td>${a.studentInfo.grade }</td>
							<td title="${a.studentInfo.major }">${a.studentInfo.major }</td>
							<td>${a.studentInfo.classic }</td>
							<td>${a.studentInfo.studyNo }</td>
							<td>${a.studentInfo.studentName }</td>
							<td title="${a.course.courseName }">${a.course.courseName }</td>
							<td
								title="${ghfn:dictCode2Val('CodeUnScoreStyle',a.unScore)}_${a.memo }">${ghfn:dictCode2Val('CodeUnScoreStyle',a.unScore)}</td>
							<c:choose>
								<c:when test="${schoolCode eq '10601' and (a.unScore eq '1' or a.unScore eq '2') }">
									<td>合格</td>
								</c:when>
								<c:otherwise>
									<td>${a.scoreForCount}</td>
								</c:otherwise>
							</c:choose>
							<td><fmt:formatDate value="${a.subjectTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${a.checkTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus',a.checkStatus)}</td>
							<td><c:choose>
									<%--待审核 0 审核 1 审核不通过2 --%>
									<c:when test="${a.checkStatus eq '0' }">
										<a href="#"
											onclick="editNoExamApplyDetails('${a.resourceid }')">修改</a>
									</c:when>
									<c:otherwise>
										<a href="#"
											onclick="showNoExamApplyDetails('${a.resourceid }')">查看</a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- <gh:page page="${applyList}"
				goPageUrl="${baseUrl}/edu3/teaching/noexamapply/list.html"
				pageType="sys" condition="${condition}" /> --%>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${applyList}"
				goPageUrl="${baseUrl}/edu3/teaching/noexamapply/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>

</body>
</html>