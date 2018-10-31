<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>提交成绩</title>
</head>
<body>
	<script type="text/javascript">
	//提交成绩
	function examResultsSubmit(){
	
		var examresoultsSubmitResourceid = new Array();
		jQuery("#examResultsSubmitBody input[name='resourceid']:checked").each(function(){
			examresoultsSubmitResourceid.push(jQuery(this).val());
		});
		if(examresoultsSubmitResourceid.length>0){
			
			var $form = $("#submitExamResultsSaveForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要提交所选择的成绩吗", {
                okCall: function(){
                	$.ajax({
						type:'POST',
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								navTab.reload($("#submitExamResultsSearchForm").attr("action"), $("#submitExamResultsSearchForm").serializeArray());
							}
						}
					});
                }
            });   
		}else{
			alertMsg.warn("请勾选要提交的成绩!")
		}
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="submitExamResultsSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/submit-examresults-list.html"
				method="post">
				<input id="examSubId" name="examSubId" type="hidden"
					value="${condition['examSubId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="submitExamResults_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" style="width:55%" /></li>
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
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE"
			pageType="submitList"></gh:resAuth>
		<div class="pageContent">
			<form id="submitExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/submit-examresults-save.html"
				method="post">
				<input id="examSubId" name="examSubId" type="hidden"
					value="${condition['examSubId'] }" />
				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<th width="4%"><input type="checkbox" name="checkall"
								id="check_all_examResults_submit_save"
								onclick="checkboxAll('#check_all_examResults_submit_save','resourceid','#examResultsSubmitBody')" /></th>
							<th width="15%">课程名称</th>
							<th width="7%">教学站</th>
							<th width="10%">专业</th>
							<th width="6%">层次</th>
							<th width="10%">学号</th>
							<th width="6%">姓名</th>
							<th width="6%">选考次数</th>
							<th width="6%">卷面成绩</th>
							<th width="6%">平时成绩</th>
							<th width="6%">综合成绩</th>
							<th width="6%">成绩异常</th>
							<th width="6%">成绩状态</th>
							<th width="6%">审核意见</th>
						</tr>
					</thead>
					<tbody id="examResultsSubmitBody">
						<c:forEach items="${objPage.result}" var="examResults"
							varStatus="vs">
							<tr
								<c:if test="${examResults.checkStatus eq '2' }"> style="color: red" </c:if>>
								<td><input type="checkbox" name="resourceid"
									value="${examResults.resourceid }" autocomplete="off" /></td>
								<td title="${examResults.course.courseName }">${examResults.course.courseName }</td>
								<td
									title="${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }">${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
								<td title="${examResults.studentInfo.major.majorName}">${examResults.studentInfo.major.majorName}</td>
								<td title="${examResults.studentInfo.classic.classicName}">${examResults.studentInfo.classic.classicName}</td>
								<td>${examResults.studentInfo.studyNo}</td>
								<td title="${examResults.studentInfo.studentName}">${examResults.studentInfo.studentName}</td>
								<td>${examResults.examCount}</td>
								<td><c:choose>
										<c:when test="${examResults.checkStatus eq '2' }">
											<input type="text"
												name="writtenScore${examResults.resourceid }" align="middle"
												class="number" style="width: 30px; color: red"
												value="${examResults.writtenScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:when>
										<c:otherwise>
											<input type="text"
												name="writtenScore${examResults.resourceid }" align="middle"
												class="number" style="width: 30px;"
												value="${examResults.writtenScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${examResults.checkStatus eq '2' }">
											<input type="text"
												name="usuallyScore${examResults.resourceid }" align="middle"
												class="number" style="width: 30px; color: red"
												value="${examResults.usuallyScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:when>
										<c:otherwise>
											<input type="text"
												name="usuallyScore${examResults.resourceid }" align="middle"
												class="number" style="width: 30px"
												value="${examResults.usuallyScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${examResults.checkStatus eq '2' }">
											<input type="text"
												name="integratedScore${examResults.resourceid }"
												align="middle" class="number"
												style="width: 30px; color: red"
												value="${examResults.integratedScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:when>
										<c:otherwise>
											<input type="text"
												name="integratedScore${examResults.resourceid }"
												align="middle" class="number" style="width: 30px"
												value="${examResults.integratedScore}"
												<c:if test="${examResults.examAbnormity != null and examResults.examAbnormity ne '0'}">readonly="readonly"</c:if> />
										</c:otherwise>
									</c:choose></td>
								<td><c:choose>
										<c:when test="${examResults.checkStatus eq '2' }">
											<gh:select name="examAbnormity${examResults.resourceid }"
												dictionaryCode="CodeExamAbnormity" choose="N"
												style="color: red" value="${examResults.examAbnormity}"
												disabled="disabled" />
										</c:when>
										<c:otherwise>
											<gh:select name="examAbnormity${examResults.resourceid }"
												dictionaryCode="CodeExamAbnormity" choose="N"
												value="${examResults.examAbnormity}" disabled="disabled" />
										</c:otherwise>
									</c:choose></td>
								<td>
									${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}
								</td>
								<td title="${examResults.checkNotes}">${examResults.checkNotes}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/submit-examresults-list.html"
				pageType="sys" condition="${condition}" />

		</div>
	</div>
</body>
</html>