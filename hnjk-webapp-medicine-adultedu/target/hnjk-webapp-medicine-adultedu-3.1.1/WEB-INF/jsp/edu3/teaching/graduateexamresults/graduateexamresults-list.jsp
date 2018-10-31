<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业论文预约学生列表</title>
<script type="text/javascript">
$(function (){
	var msg = "${inputTimeTips }";
	if(msg!=""){
		alertMsg.warn(msg);
	}
});
function saveGraduateExamResult(){
	var isInOralexaminputTime = "${isInOralexaminputTime}";
	var isInExaminputTime = "${isInExaminputTime}";
	if(isInExaminputTime=="N" && isInOralexaminputTime=="N"){
		alertMsg.warn('${inputTimeTips }');
		return false;
	}
	var $form = $("#graduateExamResult_Form");
	if(!isChecked('resourceid',"#graduateexamresults_Body")){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	var isValid = true;
	$("#graduateexamresults_Body input[@name='resourceid']:checked").each(function(){
		var obj1 = $("#firstScore"+$(this).val());
		var obj2 = $("#secondScore"+$(this).val());
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
//导出论文成绩
function exportGraduateExamResult(){
	if($("#graduateexamresults_batchId").val()==""){
		alertMsg.warn("请选择一个论文批次");
    	return false;
	}
	var url = "${baseUrl}/edu3/teaching/graduatepapersorder/result/export.html?"+$("#graduateExamResultSearchForm").serialize();
	downloadFileByIframe(url,"graduateExamResultSearchIframe");
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="graduateExamResultSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/graduatepapersorder/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>论文批次：</label> <select
							id="graduateexamresults_batchId" name="batchId"
							style="width: 120px;">
								<option value="">请选择...</option>
								<c:forEach items="${examSubList}" var="sub">
									<option value="${sub.resourceid }"
										<c:if test="${sub.resourceid eq condition['batchId']}">selected="selected"</c:if>>${sub.batchName }</option>
								</c:forEach>
						</select> <span style="color: red;">*</span></li>
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="graduateexamresults_branchSchool"
								defaultValue="${condition['branchSchool']}" displayType="code"
								style="width:120px" /></li>
						<li><label>学生姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学生学号：</label><input type="text" name="stuStudyNo"
							value="${condition['stuStudyNo']}" style="width: 115px" /></li>
					</ul>
					<div class="subBar">
						<c:if test="${not empty inputTimeTips }">
							<span class="tips" style="display: block; float: left;">${inputTimeTips }</span>
						</c:if>
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
			<gh:resAuth parentCode="RES_TEACHING_PRELIMINARY" pageType="list"></gh:resAuth>
			<form id="graduateExamResult_Form"
				action="${baseUrl }/edu3/teaching/graduatepapersorder/result/save.html"
				method="post">
				<input type="hidden" name="isInExaminputTime"
					value="${isInExaminputTime }" /> <input type="hidden"
					name="isInOralexaminputTime" value="${isInOralexaminputTime }" />
				<table class="table" layouth="161">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_graduateexamresults"
								onclick="checkboxAll('#check_all_graduateexamresults','resourceid','#graduateexamresults_Body')" /></th>
							<th width="10%">论文批次</th>
							<th width="10%">学号</th>
							<th width="8%">姓名</th>
							<th width="11%">教学站</th>
							<th width="10%">专业</th>
							<th width="8%">层次</th>
							<th width="8%">年级</th>
							<th width="10%">初评成绩</th>
							<th width="10%">答辩成绩</th>
							<th width="10%">终评成绩</th>
						</tr>
					</thead>
					<tbody id="graduateexamresults_Body">
						<c:forEach items="${ordercList.result}" var="order" varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${order.resourceid }" autocomplete="off" /></td>
								<td>${order.examSub.batchName }</td>
								<td>${order.studentInfo.studyNo }</td>
								<td>${order.studentInfo.studentName }</td>
								<td>${order.studentInfo.branchSchool }</td>
								<td>${order.studentInfo.major }</td>
								<td>${order.studentInfo.classic }</td>
								<td>${order.studentInfo.grade }</td>
								<c:choose>
									<c:when test="${isInExaminputTime eq 'N' }">
										<td><fmt:formatNumber value='${order.firstScore }'
												pattern='###.#' /></td>
									</c:when>
									<c:otherwise>
										<td><input type="text"
											id="firstScore${order.resourceid }"
											name="firstScore${order.resourceid }"
											value="<fmt:formatNumber value='${order.firstScore }' pattern='###.#' />"
											size="5" /></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${isInOralexaminputTime eq 'N' }">
										<td><fmt:formatNumber value='${order.secondScore }'
												pattern='###.#' /></td>
									</c:when>
									<c:otherwise>
										<td><c:choose>
												<c:when test="${empty order.secondScore }">
													<input type="text" id="secondScore${order.resourceid }"
														name="secondScore${order.resourceid }" value="0" size="5" />
												</c:when>
												<c:otherwise>
													<input type="text" id="secondScore${order.resourceid }"
														name="secondScore${order.resourceid }"
														value="<fmt:formatNumber value='${order.secondScore }' pattern='###.#' />"
														size="5" />
												</c:otherwise>
											</c:choose></td>
									</c:otherwise>
								</c:choose>
								<td>${ghfn:dictCode2Val('CodeScoreChar',order.examResults.integratedScore) }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${ordercList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduatepapersorder/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>