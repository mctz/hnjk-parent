<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>缓考审批</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="brSchool_abnormalExamList_search_form"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/abnormalExam/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="abnormalExamList_brSchoolName"
								sel-name="abnormalExamListSchool"
								sel-onchange="abnormalExamQueryUnit()" sel-classs="flexselect"
								></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="abnormalExamListSchool"
								id="abnormalExamList_brSchoolName"
								value="${condition['abnormalExamListSchool']}" />
						</c:if> --%>

						<li><label>年级：</label> <span
							sel-id="brSchool_abnormalExamList_stuGrade"
							sel-name="abnormalExamListGrade"
							sel-onchange="abnormalExamQueryGrade()" sel-style="width: 120px"></span>
						</li>
						<li><label>层次：</label> <span
							sel-id="brSchool_abnormalExamList_classic"
							sel-name="abnormalExamListClassic"
							sel-onchange="abnormalExamQueryClassic()"
							sel-style="width: 120px"></span></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="brSchool_abnormalExamList_major"
							sel-name="abnormalExamListMajor" sel-classs="flexselect"
							></span></li>
						<li><label>学号：</label><input type="text"
							name="abnormalExamListStuNo"
							value="${condition['abnormalExamListStuNo']}" style="width: 120px;" />
						</li>

						<li><label>姓名：</label><input type="text"
							name="abnormalExamListName"
							value="${condition['abnormalExamListName']}" style="width: 120px;" />
						</li>
						<li><label>审核状态：</label> <gh:select
								dictionaryCode="CodeCheckStatus"
								name="abnormalExamListCheckStatus"
								value="${condition['abnormalExamListCheckStatus'] }"
								filtrationStr="0,1,2" style="width:100px;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>申请时间：</label> <input type="text"
							name="abnormalExamListAppStartTime"
							value="${condition['abnormalExamListAppStartTime']}"
							class="Wdate" id="appAbnormalExam_startTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'appAbnormalExam_endTime\')}'})"
							style="width: 80px;" /> 至 <input type="text"
							name="abnormalExamListAppEndTime"
							value="${condition['abnormalExamListAppEndTime']}" class="Wdate"
							id="appAbnormalExam_endTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'appAbnormalExam_startTime\')}'})"
							style="width: 80px;" /></li>
						<li class="custom-li"><label>审核时间：</label> <input type="text"
							name="abnormalExamListAuditStartTime"
							value="${condition['abnormalExamListAuditStartTime']}"
							class="Wdate" id="auditAbnormalExam_startTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'auditAbnormalExam_endTime\')}'})"
							style="width: 80px;" /> 至 <input type="text"
							name="abnormalExamListAuditEndTime"
							value="${condition['abnormalExamListAuditEndTime']}"
							class="Wdate" id="auditAbnormalExam_endTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'auditAbnormalExam_startTime\')}'})"
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
			<gh:resAuth parentCode="RES_TEACHING_ABNORMALEXAM_AUDIT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_abnormalExam"
							onclick="checkboxAll('#check_all_abnormalExam','resourceid','#abnormalExamList')" /></th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="9%">专业</th>
						<th width="5%">层次</th>
						<th width="10%">学号</th>
						<th width="6%">姓名</th>
						<th width="9%">课程名称</th>
						<th width="6%" align="center">申请类型</th>
						<th width="4%" align="center">成绩</th>
						<th width="6%" align="center">申请时间</th>
						<th width="5%" align="center">申请人</th>
						<th width="8%" align="center">审核时间</th>
						<th width="8%" align="center">审核状态</th>
						<th width="4%" align="center">详情</th>
					</tr>
				</thead>
				<tbody id="abnormalExamList">
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
								title="${ghfn:dictCode2Val('CodeAbnormalType',a.abnormalType)}">${ghfn:dictCode2Val('CodeAbnormalType',a.abnormalType)}</td>
							<td>${a.scoreForCount}</td>
							<td><fmt:formatDate value="${a.applyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${a.applyManName }</td>
							<td><fmt:formatDate value="${a.checkDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus',a.checkStatus)}</td>
							<td>
								<%--
			            	<c:choose>待审核 0 审核 1 审核不通过2 
			            		<c:when test="${a.checkStatus eq '0' }">
			            			<a href="#" onclick="editAbnormalExam('${a.resourceid }')">修改</a>
			            		</c:when>
			            		<c:otherwise>
			            			<a href="#" onclick="showAbnormalExamDetails('${a.resourceid }')">查看</a>
			            		</c:otherwise>
			            	</c:choose>
			            --%> <a href="#"
								onclick="showABExamDetails('${a.resourceid }')">查看</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${applyList}"
				goPageUrl="${baseUrl}/edu3/teaching/abnormalExam/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

	<script type="text/javascript">	
$(document).ready(function(){
	abnormalExamQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function abnormalExamQueryBegin() {
	var defaultValue = "${condition['abnormalExamListSchool']}";
	var schoolId = "";
	var brshSchool = "${condition['brshSchool']}"
	if(brshSchool=='Y'){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['abnormalExamListGrade']}";
	var classicId = "${condition['abnormalExamListClassic']}";
	var teachingType = "${condition['name~teachingType']}";
	var majorId = "${condition['abnormalExamListMajor']}";
	var classesId = "${condition['name~classes']}";
	var selectIdsJson = "{unitId:'abnormalExamList_brSchoolName',gradeId:'brSchool_abnormalExamList_stuGrade',classicId:'brSchool_abnormalExamList_classic',majorId:'brSchool_abnormalExamList_major'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

// 选择教学点
function abnormalExamQueryUnit() {
	var defaultValue = $("#abnormalExamList_brSchoolName").val();
	var selectIdsJson = "{gradeId:'brSchool_abnormalExamList_stuGrade',classicId:'brSchool_abnormalExamList_classic',majorId:'brSchool_abnormalExamList_major'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function abnormalExamQueryGrade() {
	var defaultValue = $("#abnormalExamList_brSchoolName").val();
	var gradeId = $("#brSchool_abnormalExamList_stuGrade").val();
	var selectIdsJson = "{classicId:'brSchool_abnormalExamList_classic',majorId:'brSchool_abnormalExamList_major'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function abnormalExamQueryClassic() {
	var defaultValue = $("#abnormalExamList_brSchoolName").val();
	var gradeId = $("#brSchool_abnormalExamList_stuGrade").val();
	var classicId = $("#brSchool_abnormalExamList_classic").val();
	var selectIdsJson = "{majorId:'brSchool_abnormalExamList_major'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}

	//审核通过
	function abnormalExam_past(){
		pageBarHandle("确定要审核通过吗？","${baseUrl}/edu3/teaching/abnormalExam/past.html","#abnormalExamList");
	}
	//审核不通过状态
	function abnormalExam_nopast(){
		if("${condition['brshSchool'] eq 'Y' }" == "true"){
			pageBarHandle("确定要修改为审核不通过吗？","${baseUrl}/edu3/teaching/abnormalExam/noPast.html?isBrschool=1","#abnormalExamList");
		}else{
			pageBarHandle("确定要修改为审核不通过吗？","${baseUrl}/edu3/teaching/abnormalExam/noPast.html?","#abnormalExamList");
		}
	}
	// 撤销审核
	function abnormalExam_revoke(){
		var check = true;
		$("#abnormalExamList input[@name='resourceid']:checked").each(function(){
			if($(this).attr("title") > 0){
				check = false;
			}
		});
        
		if(!check){
			alertMsg.warn('学生的申请已审批过，不能撤销！');
			return false;
		}
		pageBarHandle("确定要撤销审核吗？","${baseUrl}/edu3/teaching/abnormalExam/revoke1.html","#abnormalExamList");
	}

	//查看详细的缓考申请信息
	function showABExamDetails(rid){
		var url = "${baseUrl}/edu3/teaching/abnormalExam/saveOrEdit.html?view=Y";
			navTab.openTab('RES_TEACHING_ABNORMALEXAM_AUDIT_VIEWDETAIL', url+'&resourceid='+rid, '查看缓考申请信息');	
	}
	
	//修改详细的缓考申请信息
	function editAbnormalExamDetails(rid){
		var url = "${baseUrl}/edu3/teaching/abnormalExam/saveOrEdit.html?";
			navTab.openTab('RES_TEACHING_ABNORMALEXAM_AUDIT_VIEWDETAIL', url+'resourceid='+rid, '修改缓考申请信息');	
	}
	
	

</script>
</body>
</html>