<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业论文成绩管理</title>
<script type="text/javascript">
//审核成绩
function auditThesisResults(){
	if($("#thesisresults_Body input[@name='resourceid'][checkStatus='4']:checked").size()>0){
		alertMsg.warn("请不要选择已经审核的成绩");
    	return false;
	}
	pageBarHandle("您确定要让这些毕业论文成绩审核通过吗？","${baseUrl}/edu3/teaching/graduatepapersorder/thesis/audit.html","#thesisresults_Body");
}
function exportThesisResults(){
	if($("#thesis_graduateexamresults_batchId").val()==""){
		alertMsg.warn("请选择一个论文批次");
    	return false;
	}
	var url = "${baseUrl}/edu3/teaching/graduatepapersorder/result/export.html?from=thesis&"+$("#thesisgraduateExamResultSearchForm").serialize();
	downloadFileByIframe(url,"thesisgraduateExamResultSearchIframe");
}
function saveThesisResults(){
	var $form = $("#thesis_graduateExamResult_Form");
	if(!isChecked('resourceid',"#thesisresults_Body")){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	if($("#thesisresults_Body input[@name='resourceid'][checkStatus='4']:checked").size()>0){
		alertMsg.warn("请不要选择已经审核的成绩");
    	return false;
	}
	var isValid = true;
	var isInOralexaminputTime = "${isInOralexaminputTime}";
	var isInExaminputTime = "${isInExaminputTime}";
	$("#thesisresults_Body input[@name='resourceid'][checkStatus!='4']:checked").each(function(){
		var obj1 = $("#thesis_firstScore"+$(this).val());
		var obj2 = $("#thesis_secondScore"+$(this).val());
		if(obj1.size()>0){
			var r1 = $.trim(obj1.val());
			if(r1==""||!r1.isNumber()){
	        	isValid = false;
	        	return false;
	        }
		}
		if(obj2.size()>0){
			var r2 = $.trim(obj2.val());
			if(r2==""||!r2.isNumber()){
	        	isValid = false;
	        	return false;
	        }
		}
    });
    if(!isValid){
    	alertMsg.warn("请输入合法的成绩数值");
    	return false;
    } else {
    	$.ajax({
			type:'POST',
			url:$form.attr("action"),
			data:$form.serializeArray(),
			dataType:"json",
			cache: false,
			success: function (json){
				DWZ.ajaxDone(json);
				if (json.statusCode == 200){
					navTabPageBreak();
				}
			},
			
			error: DWZ.ajaxError
		});    		
    }
    return false;
}
//网成班成绩审核
function auditNetsidestudyThesisResults(){
	if($("#thesisresults_Body input[@name='resourceid'][checkStatus='4']:checked").size()>0){
		alertMsg.warn("请不要选择已经审核的成绩");
    	return false;
	}
	pageBarHandle("您确定要让这些毕业论文成绩审核通过吗？","${baseUrl}/edu3/teaching/graduatepapersorder/thesis/audit.html?teachType=netsidestudy","#thesisresults_Body");
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="thesisgraduateExamResultSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/graduatepapersorder/thesis/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>论文批次：</label> <select
							id="thesis_graduateexamresults_batchId" name="batchId"
							style="width: 50%;">
								<option value="">请选择...</option>
								<c:forEach items="${examSubList}" var="sub">
									<option value="${sub.resourceid }"
										<c:if test="${sub.resourceid eq condition['batchId']}">selected="selected"</c:if>>${sub.batchName }</option>
								</c:forEach>
						</select> <span style="color: red;">*</span></li>
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="thiesis_graduateexamresults_branchSchool"
								defaultValue="${condition['branchSchool']}" displayType="code"
								style="width:120px" /></li>
						<li><label>教学方式：</label> <select name="teachType"
							style="width: 115px;">
								<option value="networkstudy">纯网络</option>
								<option value="netsidestudy"
									<c:if test="${condition['teachType'] eq 'netsidestudy' }">selected="selected"</c:if>>网成班</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel name="grade"
								id="thiesis_graduateexamresults_gradeId" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['grade']}" orderBy="gradeName desc"
								style="width:115px;" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major"
								id="thiesis_graduateexamresults_majorId" bindValue="resourceid"
								displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" orderBy="majorCode"
								style="width:120px;" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic"
								id="thiesis_graduateexamresults_classicId"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>指导老师：</label><input type="text" name="teacherName"
							value="${condition['teacherName']}" style="width: 115px" /></li>
						<li><label>学生姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学生学号：</label><input type="text" name="stuStudyNo"
							value="${condition['stuStudyNo']}" style="width: 115px" /></li>
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
			<c:choose>
				<c:when test="${condition['teachType'] ne 'netsidestudy' }">
					<%-- 纯网络 --%>
					<gh:resAuth parentCode="RES_TEACHING_PRELIMINARY_THESIS"
						pageType="list"></gh:resAuth>
					<form id="thesis_graduateExamResult_Form"
						action="${baseUrl }/edu3/teaching/graduatepapersorder/result/save.html"
						method="post">
						<input type="hidden" name="isInExaminputTime"
							value="${isInExaminputTime }" /> <input type="hidden"
							name="isInOralexaminputTime" value="${isInOralexaminputTime }" />
						<input type="hidden" name="teachType"
							value="${condition['teachType'] }" />
						<table class="table" layouth="184">
							<thead>
								<tr>
									<th width="5%"><input type="checkbox" name="checkall"
										id="check_all_thesisresults"
										onclick="checkboxAll('#check_all_thesisresults','resourceid','#thesisresults_Body')" /></th>
									<th width="8%">论文批次</th>
									<th width="8%">指导老师</th>
									<th width="8%">专业</th>
									<th width="8%">教学站</th>
									<th width="8%">学号</th>
									<th width="8%">姓名</th>
									<th width="8%">层次</th>
									<th width="5%">初评成绩</th>
									<th width="5%">答辩成绩</th>
									<th width="5%">终评成绩</th>
									<th width="8%">审核人</th>
									<th width="8%">审核时间</th>
									<th width="8%">审核状态</th>
								</tr>
							</thead>
							<tbody id="thesisresults_Body">
								<c:forEach items="${ordercList.result}" var="order"
									varStatus="vs">
									<tr>
										<td><c:choose>
												<c:when test="${order.examResults.checkStatus eq '4' }">
													<input type="hidden" value="${order.resourceid }" />
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="resourceid"
														value="${order.resourceid }"
														checkStatus="${order.examResults.checkStatus}"
														autocomplete="off" />
												</c:otherwise>
											</c:choose></td>
										<td>${order.examSub.batchName }</td>
										<td>${order.guidTeacherName }</td>
										<td>${order.studentInfo.major }</td>
										<td>${order.studentInfo.branchSchool }</td>
										<td>${order.studentInfo.studyNo }</td>
										<td>${order.studentInfo.studentName }</td>
										<td>${order.studentInfo.classic }</td>
										<c:choose>
											<c:when test="${order.examResults.checkStatus eq '4' }">
												<td><fmt:formatNumber value='${order.firstScore }'
														pattern='###.#' /></td>
												<td><fmt:formatNumber value='${order.secondScore }'
														pattern='###.#' /></td>
											</c:when>
											<c:otherwise>
												<td><input type="text"
													id="thesis_firstScore${order.resourceid }"
													name="firstScore${order.resourceid }"
													value="<fmt:formatNumber value='${order.firstScore }' pattern='###.#' />"
													size="5" /></td>
												<td><c:choose>
														<c:when test="${empty order.secondScore }">
															<input type="text" id="secondScore${order.resourceid }"
																name="secondScore${order.resourceid }" value="0"
																size="5" />
														</c:when>
														<c:otherwise>
															<input type="text"
																id="thesis_secondScore${order.resourceid }"
																name="secondScore${order.resourceid }"
																value="<fmt:formatNumber value='${order.secondScore }' pattern='###.#' />"
																size="5" />
														</c:otherwise>
													</c:choose></td>
											</c:otherwise>
										</c:choose>
										<td>${ghfn:dictCode2Val('CodeScoreChar',order.examResults.integratedScore) }</td>
										<td>${order.examResults.auditMan }</td>
										<td><fmt:formatDate
												value="${order.examResults.auditDate }"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td><c:choose>
												<c:when test="${order.examResults.checkStatus eq '4' }">通过发布</c:when>
												<c:otherwise>未审核</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
				</c:when>
				<c:otherwise>
					<%-- A+B网成班 --%>
					<gh:resAuth parentCode="RES_TEACHING_PRELIMINARY_THESIS"
						pageType="nlist"></gh:resAuth>
					<input type="hidden" name="teachType"
						value="${condition['teachType'] }" />
					<table class="table" layouth="184">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_thesisresults"
									onclick="checkboxAll('#check_all_thesisresults','resourceid','#thesisresults_Body')" /></th>
								<th width="10%">论文批次</th>
								<th width="10%">教学站</th>
								<th width="10%">专业</th>
								<th width="10%">层次</th>
								<th width="14%">学号</th>
								<th width="10%">姓名</th>
								<th width="7%">终评成绩</th>
								<th width="8%">审核人</th>
								<th width="8%">审核时间</th>
								<th width="8%">审核状态</th>
							</tr>
						</thead>
						<tbody id="thesisresults_Body">
							<c:forEach items="${ordercList.result}" var="vo" varStatus="vs">
								<tr>
									<td><c:choose>
											<c:when test="${vo.checkStatus eq '4' }">
												<input type="hidden" value="${vo.examResultsId }" />
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="resourceid"
													value="${vo.examResultsId }"
													checkStatus="${vo.checkStatus}" autocomplete="off" />
											</c:otherwise>
										</c:choose></td>
									<td>${thesisSub.batchName }</td>
									<td>${vo.branchSchool }</td>
									<td>${vo.majorName }</td>
									<td>${vo.classicName }</td>
									<td>${vo.stuStudyNo }</td>
									<td>${vo.name }</td>
									<td>${ghfn:dictCode2Val('CodeScoreChar',vo.integratedScore) }</td>
									<td>${vo.auditMan }</td>
									<td><fmt:formatDate value="${vo.auditDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><c:choose>
											<c:when test="${vo.checkStatus eq '4' }">通过发布</c:when>
											<c:otherwise>未审核</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>
			<gh:page page="${ordercList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduatepapersorder/thesis/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>