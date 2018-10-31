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
	<h2 class="contentTitle">${examInfo.examSub.batchName }-
		${examInfo.course.courseName }</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/framework/teaching/onlineexam/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,dialogAjaxDone_examInfo);">
				<input type="hidden" name="resourceid"
					value="${examInfo.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">考试开始时间:</td>
							<td width="35%"><input type='text' name='examStartTime'
								value='<fmt:formatDate value="${examInfo.examStartTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 95%' class='required' id='examInfo_examStartTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examInfo_examEndTime\')}'})">
							</td>
							<td width="15%">考试结束时间:</td>
							<td width="35%"><input type='text' name='examEndTime'
								value='<fmt:formatDate value="${examInfo.examEndTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 95%' class='required' id='examInfo_examEndTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examInfo_examStartTime\')}'})">
							</td>
						</tr>
						<tr>
							<td width="15%">成卷规则</td>
							<td width="85%" colspan="3"><select name="courseExamRulesId"
								id="examInfo_courseExamRulesId" style="width: 60%;"
								class="required">
									<option value=''>请选择</option>
									<c:forEach items="${courseExamRules }" var="rule">
										<option value='${rule.resourceid }'
											<c:if test="${rule.resourceid eq examInfo.courseExamRules.resourceid}">selected="selected"</c:if>>${rule.courseExamRulesName }<c:if
												test="${not empty rule.paperSourse }">(题库来源：${ghfn:dictCode2Val('CodeExamform',rule.paperSourse) })</c:if></option>
									</c:forEach>
							</select> <span style="color: red;">*</span></td>

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