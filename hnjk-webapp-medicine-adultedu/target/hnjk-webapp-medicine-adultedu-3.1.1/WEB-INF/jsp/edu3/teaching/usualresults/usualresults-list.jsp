<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生平时成绩管理</title>
<style type="text/css">
th{font-weight: bold;}
td,th{text-align: center;}
</style>
<script type="text/javascript">
	$(function (){
		var msg = "${errorMsg}";
		if(msg!=""){
			alertMsg.warn(msg);
		}
	});
	
	//保存录入成绩
	function saveUsualResults(){	
		$("#usualResultsBody input").each(function (){
			if($(this).hasClass("error")){
				$(this).removeClass("error");
			}
		});
		if(!isChecked('resourceid','#usualResultsBody')){
	 		alertMsg.warn('请选择一条要操作记录！');
			return;
	 	}	
	 	var courseIds = ResultsCourseIds();
	 	$("#usualResultsBody input[name='resourceid']:enabled").each(function (){
			$(this).parent().parent().parent().find("input[name='isDealed']").val(this.checked?'Y':'N');
		});		
		if(isValidResults()){
			$.ajax({
				type:'POST',
				url:$("#usualResultsForm").attr("action"),
				data:$("#usualResultsForm").serializeArray(),
				dataType:"json",
				cache: false,
				success: function (json){
					DWZ.ajaxDone(json);
					if (json.statusCode == 200){
						var pageNum = "${usualResultsList.pageNum}" || "1";
						navTabPageBreak({pageNum:pageNum});
					}
				},						
				error: DWZ.ajaxError
			});			
		} else {
			alertMsg.warn("请填入0-100的数值！");
		}			
	}
	//检查输入的合法性
	function isValidResults(){
		var isValid = true;
		$("#usualResultsBody tr[usualStatus!='1']").each(function (){
			if($(this).find("input[name='resourceid']").get(0).checked){
				$(this).find("input[type='text']").each(function (){
					if(!$(this).val().isNumber()){
						$(this).addClass("error");
						isValid = false;
					} else {
						if($(this).val()<0||$(this).val()>100){
							$(this).addClass("error");
							isValid = false;
						}
					}
				});
			}
		});
		return isValid;
	}
	//课程id
	function ResultsCourseIds(){
		var courseIds = "";
		$("#usualResultsBody tr[usualStatus!='1']").each(function (){
			if($(this).find("input[name='resourceid']").get(0).checked){
				var courseId = $(this).find("input[name='courseId']").val();
				if(courseIds.indexOf(courseId)<0){
					courseIds += courseId +",";
				}
			}
		});
		if(courseIds!=""){
			courseIds = courseIds.substring(0,courseIds.length-1);
		}
		return courseIds;
	}
	//查询条件检查
	function usualresults_navTabSearch(obj){
		if($("#usualresults_courseId").val()==""||$("#usualresults_yearInfo").val()==""||$("#usualresults_term").val()==""){
			var test = $("#usualresults_courseId").val() + $("#usualresults_yearInfo").val() + $("#usualresults_term").val();
			alertMsg.warn('请选择课程、年度和学期！');
			return false;
		}
		return navTabSearch(obj);
	}
	//提交平时成绩
	function submitUsualResults(){	
		pageBarHandle("您确定要提交这些成绩作为平时成绩吗？","${baseUrl}/edu3/teaching/usualresults/submit.html","#usualResultsBody");
	}
	//保存课程平时成绩
	function saveAllUsualResults(){
		var courseId = $("#usualresults_courseId").val();
		var yearInfoId = $("#usualresults_yearInfo").val();
		var term = $("#usualresults_term").val();
		if(courseId==""||yearInfoId==""||term==""){
			alertMsg.warn('请选择课程、年度和学期！');
			return false;
		}
		alertMsg.confirm("你确定要计算并保存整门课程的平时成绩吗?", {
			okCall: function(){
				$.post("${baseUrl}/edu3/teaching/usualresults/all/save.html",{yearInfoId:yearInfoId,term:term,courseId:courseId}, navTabAjaxDone, "json");
			}
		});			
	}
	
	//提交课程平时成绩
	function submitAllUsualResults(){
		var unitId = "${condition['brSchoolid']}";
		var courseId = $("#usualresults_courseId").val();
		var yearInfoId = $("#usualresults_yearInfo").val();
		var term = $("#usualresults_term").val();
		if(courseId==""||yearInfoId==""||term==""){
			alertMsg.warn('请选择课程、年度和学期！');
			return false;
		}
		alertMsg.confirm("你确定要提交整门课程的平时成绩吗?", {
			okCall: function(){	
				$.post("${baseUrl}/edu3/teaching/usualresults/all/submit.html",{unitId:unitId,yearInfoId:yearInfoId,term:term,courseId:courseId}, navTabAjaxDone, "json");
			}
		});		
	}
	
	//导出网上学习成绩
	function exportResult(){
		var brSchoolid = "${condition['brSchoolid']}";
		var examOrderYearInfo = "${condition['examOrderYearInfo']}";
		var examOrderTerm = "${condition['examOrderTerm']}";
		var courseId = "${condition['courseId']}";
		var name = "${condition['name']}";
		var studyNo = "${condition['studyNo']}";
		var usualStatus = "1";

		var url = "${baseUrl }/edu3/teaching/usualresults/download.html?brSchoolid="
				+brSchoolid+"&examOrderYearInfo="+examOrderYearInfo+"&examOrderTerm="
				+examOrderTerm+"&courseId="+courseId+"&name="+name
				+"&studyNo="+studyNo+"&usualStatus="+usualStatus;
		alertMsg.confirm("确定下载查询出来的网上成绩单？", {
			okCall: function(){
				downloadFileByIframe(url,'onlineResultListForDownloadExportIframe');
			}
		});	

	}
	
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
		//alert("${defaultRule}");
	});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return usualresults_navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/usualresults/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>教学站：</label> ${brschools }</li>
						<li><label>年度：</label>
						<gh:selectModel id="usualresults_yearInfo"
								name="examOrderYearInfo" bindValue="resourceid"
								displayValue="yearName" style="width:130px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['examOrderYearInfo']}"
								orderBy="firstYear desc" /></li>
						<li><label>学期：</label>
						<gh:select id="usualresults_term" name="examOrderTerm"
								value="${condition['examOrderTerm']}" dictionaryCode="CodeTerm"
								style="width:130px" /></li>
						<li><label>状态：</label> <select name="usualStatus">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['usualStatus'] eq '-1' }">selected="selected"</c:if>>未保存</option>
								<option value="0"
									<c:if test="${condition['usualStatus'] eq '0' }">selected="selected"</c:if>>已保存</option>
								<option value="1"
									<c:if test="${condition['usualStatus'] eq '1' }">selected="selected"</c:if>>已提交</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>课程：</label> ${usualresultslistCourseSelect }</li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
					</ul>
					<div class="subBar">
						<li class="tips" style="float: left;"><span> <c:if
									test="${currentRule.bbsResultPer gt 0 }">
									<b>网络辅导分</b>比例:<b>${currentRule.bbsResultPer}%</b> &nbsp;</c:if> <c:if
									test="${currentRule.askQuestionResultPer gt 0 }">
									<b>随堂问答分</b>比例:<b>${currentRule.askQuestionResultPer}%</b> &nbsp;</c:if>
								<c:if test="${currentRule.courseExamResultPer gt 0 }">
									<b>随堂练习分</b>比例:<b>${currentRule.courseExamResultPer}%</b> &nbsp;</c:if>
								<c:if test="${currentRule.exerciseResultPer gt 0 }">
									<b>作业练习分比</b>例:<b>${currentRule.exerciseResultPer}%</b> &nbsp;</c:if>
								<c:if test="${currentRule.selftestResultPer gt 0 }">
									<b>同步自测分</b>比例:<b>${currentRule.selftestResultPer}%</b> &nbsp;</c:if>
								<c:if test="${currentRule.otherResultPer gt 0 }">
									<b>实践及其他分</b>比例:<b>${currentRule.otherResultPer}%</b> &nbsp;</c:if> <c:if
									test="${currentRule.faceResultPer gt 0 }">
									<b>面授考勤分</b>比例:<b>${currentRule.faceResultPer}%</b>
								</c:if>
						</span></li>
						<label style="width: 500px;font-size: small;color: green;">网上学习时间：${learningTime.startTime } 至 ${learningTime.endTime }</label>
						<ul>
							<li><span class="tips">提示：查询时请选择课程，年度和学期</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<%-- 
					<li><a class="button" href="${baseUrl }/edu3/teaching/usualresults/list.html?con=advance" target="dialog" rel="RES_TEACHING_ESTAB_USUALRESULTS" title="学生平时成绩查询"><span>高级查询</span></a></li>  --%>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent" id="ususalresultsPageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_USUALRESULTS"
				pageType="list"></gh:resAuth>
			<form id="usualResultsForm" method="post"
				action="${baseUrl}/edu3/teaching/usualresults/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="yearInfoId"
					value="${condition['examOrderYearInfo']}" /> <input type="hidden"
					name="term" value="${condition['examOrderTerm']}" />
				<table class="table" layouth="182">
					<thead>
						<tr>
							<th width="3%"><input type="checkbox" name="checkall"
								id="check_all_usualResults"
								onclick="checkboxAll('#check_all_usualResults','resourceid','#usualResultsBody')" /></th>
							<th width="6%">年度</th>
							<th width="5%">学期</th>
							<th width="8%">课程</th>
							<th width="7%">学号</th>
							<th width="5%">姓名</th>
							
							<th width="8%">随堂问答分</th>
							<th width="8%">随堂练习分</th>
							<th width="7%">作业练习分</th>
							<c:if test="${defaultRule eq 'N'}">
								<th width="8%">网络辅导分</th>
								<th width="7%">同步自测分</th>
								<th width="7%">实践及其他分</th>
								<th width="7%">面授考勤分</th>
							</c:if>
							<th width="7%">平时分总分</th>
							<th width="5%">状态</th>
						</tr>
					</thead>
					<tbody id="usualResultsBody">
						<c:forEach items="${usualResultsList.result}" var="p"
							varStatus="vs">
							<tr usualStatus="${p.status }">
								<td><c:if test="${p.status ne '1' }">
										<input type="checkbox" name="resourceid" value="${p.studentLearnPlanId }" autocomplete="off" />
										<input type="hidden" name="planId" value="${p.studentLearnPlanId }" />
										<input type="hidden" name="isDealed" value="N" /> 
									</c:if></td>
								<td>${p.yearName}</td>
								<td>${ghfn:dictCode2Val('CodeTerm',p.term) }</td>
								<td>${p.courseName}<input type="hidden" name="courseId"
									value="${p.courseId }" /></td>
								<td>${p.studyNo}</td>
								<td>${p.studentName}</td>
								<c:choose>
									<c:when test="${p.status ne '1' }">
										<td><c:choose>
												<c:when test="${empty p.askQuestionResults}">
													<input type="text" name="askQuestionResults"
														value='<fmt:formatNumber pattern="###.#" value="${originalResults[p.studentLearnPlanId].originalAskQuestionResults }"/>'
														size="10" maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="askQuestionResults"
														value="<fmt:formatNumber pattern="###.#" value="${p.askQuestionResults}"/>"
														size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${empty p.courseExamResults}">
													<input type="text" name="courseExamResults"
														value='<fmt:formatNumber pattern="###.#" value="${originalResults[p.studentLearnPlanId].originalCourseExamResults }"/>'
														size="10" maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="courseExamResults"
														value="<fmt:formatNumber pattern="###.#" value="${p.courseExamResults}"/>"
														size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${empty p.exerciseResults}">
													<input type="text" name="exerciseResults"
														value='<fmt:formatNumber pattern="###.#" value="${originalResults[p.studentLearnPlanId].originalExerciseResults }"/>'
														size="10" maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="exerciseResults"
														value="<fmt:formatNumber pattern="###.#" value="${p.exerciseResults}"/>"
														size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<c:if test="${defaultRule eq 'N'}">
										<td><c:choose>
												<c:when test="${empty p.bbsResults}">
													<input type="text" name="bbsResults"
														value='<fmt:formatNumber pattern="###.#" value="${originalResults[p.studentLearnPlanId].originalBbsResults }"/>'
														size="10" maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="bbsResults"
														value="<fmt:formatNumber pattern="###.#" value="${p.bbsResults}"/>"
														size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${empty p.selftestResults}">
													<input type="text" name="selftestResults" value="0"
														size="10" maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="selftestResults"
														value="${p.selftestResults}" size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${empty p.otherResults}">
													<input type="text" name="otherResults" value="0" size="10"
														maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="otherResults"
														value="${p.otherResults}" size="10" maxlength="5" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${empty p.faceResults}">
													<input type="text" name="faceResults" value="0" size="10"
														maxlength="5" />
												</c:when>
												<c:otherwise>
													<input type="text" name="faceResults"
														value="${p.faceResults}" size="10" maxlength="5" />
												</c:otherwise>
											</c:choose>
										</td>
										</c:if>
									</c:when>
									<c:otherwise>
										<td>${p.askQuestionResults}</td>
										<td>${p.courseExamResults}</td>
										<td>${p.exerciseResults}</td>
										<c:if test="${defaultRule eq 'N'}">
											<td>${p.bbsResults}</td>
											<td>${p.selftestResults}</td>
											<td>${p.otherResults}</td>
											<td>${p.faceResults}</td>
										</c:if>
									</c:otherwise>
								</c:choose>
								<td><fmt:formatNumber pattern="###.#"
										value="${p.usualResults}" /></td>
								<td><c:choose>
										<c:when test="${p.status eq '1' }">已提交</c:when>
										<c:when test="${p.status eq '0' }">已保存</c:when>
										<c:otherwise>未保存</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${usualResultsList}"
				goPageUrl="${baseUrl}/edu3/teaching/usualresults/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>