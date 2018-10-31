<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次 - 考试信息</title>
<script type="text/javascript">
	function dialogAjaxDone_examInfo(json){
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			if (json.reloadUrl){
				navTab.reload(json.reloadUrl, {}, json.navTabId);
			}
			$.pdialog.closeCurrent();
		}
	}
	</script>
</head>
<body>
	<h2 class="contentTitle">
		<c:choose>
			<c:when test="${null != examInfo }">${examInfo.examSub.batchName } - ${examInfo.course.courseName } </c:when>
			<c:when test="${null != examSub }">${examSub.batchName }</c:when>
		</c:choose>
	</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/framework/teaching/netcourse/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone_examInfo);">
				<input type="hidden" name="resourceid"
					value="${examInfo.resourceid }" /> <input type="hidden"
					name="examSubId" value="${examSub.resourceid }">
				<div class="pageFormContent" layoutH="130">
					<table class="form">
						<tr>
							<td width="15%">考试开始时间:</td>
							<td width="35%"><input type='text' name='examStartTime'
								value='<fmt:formatDate value="${examInfo.examStartTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 95%' class='required'
								id='examInfo_examStartTime_for_netcoures'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examInfo_examEndTime_for_netcoures\')}'})">
							</td>
							<td width="15%">考试结束时间:</td>
							<td width="35%"><input type='text' name='examEndTime'
								value='<fmt:formatDate value="${examInfo.examEndTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 95%' class='required'
								id='examInfo_examEndTime_for_netcoures'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examInfo_examStartTime_for_netcoures\')}'})">
							</td>
						</tr>
						<tr>
							<c:choose>
								<c:when test="${null != examInfo  }">
									<td width="15%">考试课程编号:</td>
									<td width="35%"><input type='text' name='examCourseCode'
										value='${examInfo.examCourseCode }' style='width: 95%'
										class='required' /></td>
								</c:when>
								<c:otherwise>
									<td width="15%">考试课程:</td>
									<td width="35%"><gh:courseAutocomplete name="course"
											tabindex="1" id="couresId_examInfo_add" value=""
											displayType="code" classCss="required" style='width:95%'></gh:courseAutocomplete>
									</td>
								</c:otherwise>
							</c:choose>
							<td>考试课程类型</td>
							<td><gh:select name="examCourseType"
									value="${examInfo.examCourseType }"
									dictionaryCode="CodeExamInfoCourseType" style="width:95%"
									classCss="required" size="2" /></td>
						</tr>
						<tr>
							<td>课程教学类型</td>
							<td><gh:select name="teachtype"
									value="${examInfo.teachtype }" dictionaryCode="teachType"
									style="width:95%" classCss="required" /></td>
							<td width="15%">备注</td>
							<td width="85%"><textarea rows="3" cols="26" name="memo">${examInfo.memo}</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>