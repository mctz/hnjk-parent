<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线平时录入成绩</title>
</head>
<body>
	<script type="text/javascript">

	$(document).ready(function(){
		var msg = "${condition['msg']}";
		if(""!=msg){
			$("#usExamResultsInputBody").hide();
			alertMsg.warn(msg);
		}
	});
	
	//保存选择的成绩  这个地方许是要改
	function inputSave(){
		var $form = $("#inputUsExamResultsSaveForm");
		if (!$form.valid()) {
			alertMsg.warn("请录入合法的分数值！");

			return false; 
		}
		alertMsg.confirm("确认要保存成绩记录吗？", {
               okCall: function(){
               	$.ajax({
					type:'POST',
					url:$form.attr("action"),
					data:$form.serializeArray(),
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(json){
						if(json.statusCode == 300){
							alertMsg.warn(json.message);
					    }else{
							var pageNum = "${page.pageNum}";
							if(pageNum==""){
								pageNum = "1";
							}
							navTabPageBreak({pageNum:pageNum});
						}
					}
				});
               }
       });   
	}
	
	
	
</script>
	<div class="page">

		<div class="pageHeader">
			<form id="inputExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/input-examresults-list.html?type=normal"
				method="post">
				<input id="inputExamResultsSearchForm_examSubId" type="hidden"
					name="examSubId" value="${condition['examSubId'] }" /> <input
					id="inputExamResultsSearchForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" /> <input
					id="inputExamResultsSearchForm_isReadOnly" type="hidden"
					name="isReadOnly" value="${condition['isReadOnly'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <c:choose>
								<c:when test="${not empty condition['isBranchSchool']}">
								${condition['branchSchoolName']}
								<input type="hidden" name="branchSchool"
										id="inputExamResults_brSchoolName"
										value="${condition['branchSchool']}" />
								</c:when>
								<c:otherwise>
									<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
										id="inputExamResults_brSchoolName"
										defaultValue="${condition['branchSchool']}" displayType="code"
										style="width:55%" />
								</c:otherwise>
							</c:choose></li>
						<li><label>平时成绩录入状态：</label> <select name="usCheckStatus"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['usCheckStatus'] eq '-1'}"> selected="selected"</c:if>>未录入</option>
								<option value="0"
									<c:if test="${condition['usCheckStatus'] eq '0'}"> selected="selected"</c:if>>保存</option>
								<option value="1"
									<c:if test="${condition['usCheckStatus'] eq '1'}"> selected="selected"</c:if>>提交</option>
						</select></li>
						<li><label>综合成绩录入状态：</label> <select name="checkStatus"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['checkStatus'] eq '-1'}"> selected="selected"</c:if>>未录入</option>
								<option value="0"
									<c:if test="${condition['checkStatus'] eq '0'}"> selected="selected"</c:if>>保存</option>
								<option value="1"
									<c:if test="${condition['checkStatus'] eq '1'}"> selected="selected"</c:if>>提交</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label><input
							type="text" name="stuName" value="${condition['stuName']}"
							style="width: 55%" /></li>
						<li><label>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label><input
							type="text" name="stuNo" value="${condition['stuNo']}"
							style="width: 55%" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>

				</div>
			</form>
		</div>
		<c:if test="${condition['isReadOnly'] ne '1'}">
			<gh:resAuth
				parentCode="RES_TEACHING_RESULT_NETWORKSTUDY_INPUT_WRITTRNSCORELIST1"
				pageType="netWorkStudyUsSub"></gh:resAuth>
		</c:if>
		<div class="pageContent">

			<form id="inputUsExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/input-usexamresults-save.html"
				method="post">
				<input id="type" name="type" type="hidden" value="normal" /> <input
					id="inputUsExamResultsSaveForm_examSubId" type="hidden"
					name="examSubId" value="${condition['examSubId'] }" /> <input
					id="inputUsExamResultsSaveForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" /> <input
					id="inputUsExamResultsSaveForm_isReadOnly" type="hidden"
					name="isReadOnly" value="${condition['isReadOnly'] }" />
				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<c:choose>
								<c:when test="${condition['examInfo'].ismixture eq 'Y' }">
									<th width="15%">课程名称</th>
									<th width="14%">教学站</th>
									<th width="13%">专业</th>
									<th width="10%">学号</th>
									<th width="8%">姓名</th>
									<th width="6%">平时成绩</th>
									<th width="6%">平时成绩录入状态</th>
									<th width="6%">综合成绩录入状态</th>
								</c:when>
								<c:otherwise>
									<th width="15%">课程名称</th>
									<th width="14%">教学站</th>
									<th width="13%">专业</th>
									<th width="10%">学号</th>
									<th width="8%">姓名</th>
									<th width="6%">平时成绩</th>
									<th width="6%">平时成绩录入状态</th>
									<th width="6%">综合成绩录入状态</th>
								</c:otherwise>
							</c:choose>
						</tr>
					</thead>
					<tbody id="usExamResultsInputBody">
						<c:if
							test="${fn:length(page.result)<=0 and '1' ne condition['isReadOnly']}">
							<tr>
								<td colspan="9" style="color: red">所选条件下录入状态为待审核、审核通过或发布。</td>
							</tr>
						</c:if>

						<c:forEach items="${page.result}" var="examResults" varStatus="vs">

							<c:choose>
								<%-- 已提交成绩或者开闭卷且成绩异常不为0的成绩记录不能修改 --%>
								<c:when test="${ examResults.checkStatus>0 }">
									<tr style="background-color: #e4f5ff">
										<td>${examResults.course.courseName }</td>
										<td>${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td>${examResults.studentInfo.major.majorName}</td>
										<td>${examResults.studentInfo.studyNo}</td>
										<td>${examResults.studentInfo.studentName}</td>
										<td>${examResults.usuallyScore}<%/* <input id="usuallyScore_${examResults.resourceid }" onkeyup="_usuallyScoreNext(this,event);" type="text" name="usuallyScore"    align="middle" class="number" style="width:30px"  value="${examResults.usuallyScore}" <c:if test="${examResults.usCheckStatus >0 or examResults.checkStatus>0 }">readonly = "readonly"</c:if>/> */%></td>
										<td style="color: green">${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.usCheckStatus)}</td>
										<td style="color: green">${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td>${examResults.course.courseName }<input type="hidden"
											name="resourceid" value="${examResults.resourceid }" />
										</td>
										<td>${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td>${examResults.studentInfo.major.majorName}</td>
										<td>${examResults.studentInfo.studyNo}</td>
										<td>${examResults.studentInfo.studentName}</td>
										<td><input onkeyup="_usuallyScoreNext(this,event);"
											id="usuallyScore_${examResults.resourceid }" type="text"
											name="usuallyScore" align="middle" class="number"
											style="width: 30px" value="${examResults.usuallyScore}"
											<c:if test="${examResults.usCheckStatus >0 or examResults.checkStatus>0 }">readonly = "readonly"</c:if> /></td>
										<td
											<c:if test="${examResults.usCheckStatus > -1 }"> style="color:blue" </c:if>>
											${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.usCheckStatus)}</td>
										<td
											<c:if test="${examResults.checkStatus > -1 }"> style="color:blue" </c:if>>
											${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
									</tr>
								</c:otherwise>
							</c:choose>

						</c:forEach>

					</tbody>
				</table>
				<!--防止剩下单文本框时，回车就提交了表单  -->
				<div style="display: none;">
					<input type="text" id="empty" />
				</div>
			</form>
			<c:choose>
				<c:when test="${condition['isReadOnly'] ne '1'}">
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/result/input-examresults-list.html?type=normal"
						pageType="sys" beforeForm="inputUsExamResultsSaveForm"
						postBeforeForm="${condition['isAllowInput']}"
						condition="${condition }" />
				</c:when>
				<c:otherwise>
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/result/input-examresults-list.html?type=normal"
						pageType="sys" postBeforeForm="${condition['isAllowInput']}"
						condition="${condition }" />
				</c:otherwise>
			</c:choose>

		</div>
	</div>
	<script type="text/javascript">
function _usuallyScoreNext(obj,event){
	if(event.keyCode == 13) {//按ENTER键
		var ws = $("input[id^='usuallyScore_']");
		var index = ws.index($(obj))+1;
		if(index<ws.length){
			ws.get(index).focus();
		}		
	}
}
</script>
</body>
</html>