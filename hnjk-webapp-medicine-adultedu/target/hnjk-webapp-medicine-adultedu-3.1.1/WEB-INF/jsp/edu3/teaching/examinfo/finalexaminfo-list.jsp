<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期末机考时间安排</title>
<script type="text/javascript">
//新增
function addFinalExamInfo(){
	navTab.openTab('RES_TEACHING_EXAM_FINALEXAM_INPUT', '${baseUrl}/edu3/teaching/examinfo/input.html', '新增期末机考时间');
}

//修改
function modifyFinalExamInfo(){	
	if(isCheckOnlyone('resourceid','#finalexaminfoBody')){
		var url = "${baseUrl}/edu3/teaching/examinfo/input.html?resourceid="+$("#finalexaminfoBody input[@name='resourceid']:checked").val();
		navTab.openTab('RES_TEACHING_EXAM_FINALEXAM_INPUT', url, '编辑期末机考时间');
	}			
}
	
//删除
function removeFinalExamInfo(){	
	pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/examinfo/remove.html","#finalexaminfoBody");
}	
//机考学生名单
function listFinalExamStudent(){
	if(isCheckOnlyone('resourceid','#finalexaminfoBody')){
		var url = "${baseUrl}/edu3/teaching/examinfo/student/list.html?examInfoId="+$("#finalexaminfoBody input[@name='resourceid']:checked").val();
		$.pdialog.open(url, "RES_TEACHING_EXAM_FINALEXAM_STUDENT", "期末机考学生安排", {mask:true,width:1300,height:600});
	}	
}
//导出混合考学生非客观题
function exportFinalExamPaperdetails(exportType){
	if(isCheckOnlyone('resourceid','#finalexaminfoBody')){
		var resObj = $("#finalexaminfoBody input[@name='resourceid']:checked");
		if(resObj.attr('rel')=='Y'){//混合考
			var url = "${baseUrl}/edu3/teaching/finalexam/exampaperdetails/export.html?examInfoId="+resObj.val()+"&exportType="+exportType;
			downloadFileByIframe(url,"finalexaminfoPaperdetailsIframe");
		} else {
			alertMsg.warn("请选择一个混合考场次");
			return false;
		}		
	}	
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examinfo/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<!-- 学院2016修改 -->
						<c:if test="${isorunit ne 'Y' }">
							<li style="width: 360px;"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="finalexaminfo_branchSchoolId" tabindex="1"
									id="finalexaminfo_branchSchoolId" style="width: 240px;"
									defaultValue="${condition['finalexaminfo_branchSchoolId']}" />
							</li>
						</c:if>
						<li><label>考试批次</label> <gh:selectModel
								id="finalexaminfo_examSubId" name='examSubId'
								bindValue='resourceid' displayValue='batchName'
								value="${condition['examSubId']}"
								modelClass='com.hnjk.edu.teaching.model.ExamSub'
								condition="batchType='exam'"
								orderBy='yearInfo.firstYear desc,term desc' style="width:130px;" />
						</li>
					</ul>
					<!-- 学院2016修改 -->
					<ul class="searchContent">
						<li style="width: 360px;"><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="finalexaminfo_courseId" style="width: 240px;"
								value="${condition['courseId'] }" displayType="code" /></li>
						
						<li><label>开始时间：</label> <input type="text"
							name="fillinDateStartStr" style="width: 130px;"
							value="${condition['fillinDateStartStr']}" class="Wdate"
							id="fillinDateStartStrbeginTime"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'fillinDateStartStrendTime\')}'})" />

						</li>
						<li><label>结束时间：</label> <input type="text" style="width: 130px;"
							name="fillinDateEndStr" value="${condition['fillinDateEndStr']}"
							class="Wdate" id="fillinDateStartStrendTime"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'fillinDateStartStrbeginTime\')}'})" />
						</li>
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
			<gh:resAuth parentCode="RES_TEACHING_EXAM_FINALEXAM" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_finalexaminfo"
							onclick="checkboxAll('#check_all_finalexaminfo','resourceid','#finalexaminfoBody')" /></th>
						<th width="10%">考试批次</th>
						<th width="10%">教学站</th>
						<th width="15%">课程</th>
						<th width="15%">场次名称</th>
						<th width="25%">考试时间</th>
						<th width="12%">成卷规则</th>
						<th width="8%">是否混合考</th>
					</tr>
				</thead>
				<tbody id="finalexaminfoBody">
					<c:forEach items="${finalExamInfoPage.result}" var="examInfo"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examInfo.resourceid }" rel="${examInfo.ismixture }"
								autocomplete="off" /></td>
							<td title="${examInfo.examSub.batchName }">${examInfo.examSub.batchName }</td>
							<td title="${examInfo.brSchool.unitName }">${examInfo.brSchool.unitName}</td>
							<td title="${examInfo.course.courseName }">${examInfo.course.courseName }</td>
							<td title="${examInfo.machineExamName }">${examInfo.machineExamName }</td>
							<td>// <fmt:formatDate value="${examInfo.examStartTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
									value="${examInfo.examEndTime }" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td>${examInfo.courseExamRules.courseExamRulesName }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',examInfo.ismixture) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${finalExamInfoPage}"
				goPageUrl="${baseUrl }/edu3/teaching/examinfo/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>