<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论文批次</title>
</head>
<body>
	<h2 class="contentTitle">编辑毕业论文批次</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/graduatethesis/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${thesis.resourceid }" />
				<input type="hidden" name="gdresourceid"
					value="${thesis.gradendDate.resourceid }" /> <input type="hidden"
					name="examsubStatus" value="${thesis.examsubStatus }" /> <input
					type="hidden" name="batchType" value="thesis" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">批次名称:</td>
							<td width="30%" colspan="3"><input type="text"
								id="batchName" name="batchName" style="width: 50%"
								value="${thesis.batchName }" class="required" /></td>
						</tr>
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"><gh:selectModel name="yearId"
									bindValue="resourceid" displayValue="yearName"
									style="width:51%"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${thesis.yearInfo.resourceid}" classCss="required" /> <font
								color=red>*</font></td>
							<td width="20%">学期:</td>
							<td width="30%"><gh:select name="term"
									dictionaryCode="CodeTerm" value="${thesis.term}"
									style="width:51%" classCss="required" /> <font color=red>*</font></td>
						</tr>
						<tr>
							<td>预约开始时间:</td>
							<td><input type="text" id="startTime" name="startTime"
								style="width: 50%"
								value="<fmt:formatDate value="${thesis.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:10',maxDate:'#F{$dp.$D(\'endTime\',{d:-1});}'})" /></td>
							<td>预约结束时间:</td>
							<td><input type="text" id="endTime" name="endTime"
								style="width: 50%"
								value="<fmt:formatDate value="${thesis.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'startTime\',{d:1});}',maxDate:'#F{$dp.$D(\'publishDate\',{d:-1});}'})" /></td>
						</tr>
						<tr>
							<td>预约公布时间:</td>
							<td><input type="text" id="publishDate" name="publishDate"
								style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 08:00:00',minDate:'#F{$dp.$D(\'endTime\',{d:1});}',maxDate:'#F{$dp.$D(\'syllabusEndDate\',{d:-1});}'})" /></td>
							<td>提纲初稿结束时间:</td>
							<td><input type="text" id="syllabusEndDate"
								name="syllabusEndDate" style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.syllabusEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'publishDate\',{d:1});}',maxDate:'#F{$dp.$D(\'firstDraftEndDate\',{d:-1});}'})" /></td>
						</tr>
						<tr>
							<td>二稿结束时间:</td>
							<td><input type="text" id="firstDraftEndDate"
								name="firstDraftEndDate" style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.firstDraftEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'syllabusEndDate\',{d:1});}',maxDate:'#F{$dp.$D(\'secondDraftEndDate\',{d:-1});}'})" /></td>
							<td>定稿结束时间:</td>
							<td><input type="text" id="secondDraftEndDate"
								name="secondDraftEndDate" style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.secondDraftEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'firstDraftEndDate\',{d:1});}'})" /></td>
						</tr>
						<tr>
							<td>初评成绩录入开始时间:</td>
							<td><input type="text" id="examinputStartTime"
								name="examinputStartTime" style="width: 50%"
								value="<fmt:formatDate value="${thesis.examinputStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:10',maxDate:'#F{$dp.$D(\'examinputEndTime\',{d:-1});}'})" /></td>
							<td>初评成绩录入结束时间:</td>
							<td><input type="text" id="examinputEndTime"
								name="examinputEndTime" style="width: 50%"
								value="<fmt:formatDate value="${thesis.examinputEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'examinputStartTime\',{d:1});}'})" /></td>
						</tr>
						<tr>
							<td>答辩成绩录入开始时间:</td>
							<td><input type="text" id="oralexaminputStartTime"
								name="oralexaminputStartTime" style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.oralexaminputStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:10',minDate:'#F{$dp.$D(\'examinputStartTime\',{d:1});}',maxDate:'#F{$dp.$D(\'oralexaminputEndTime\',{d:-1});}'})" /></td>
							<td>答辩成绩录入结束时间:</td>
							<td><input type="text" id="oralexaminputEndTime"
								name="oralexaminputEndTime" style="width: 50%"
								value="<fmt:formatDate value="${thesis.gradendDate.oralexaminputEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
								class="required Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:50',minDate:'#F{$dp.$D(\'oralexaminputStartTime\',{d:1});}'})" /></td>
						</tr>
						<tr>
							<td>初评成绩比例：</td>
							<td><input name="writtenScorePer"
								id="thesis_writtenScorePer"
								onblur="genThesisUsuallyScoreRatio()" class="required"
								onKeyUp="value=value.replace(/[^\d]/g,'') "
								onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
								style="width: 20px"
								value="<fmt:formatNumber value="${thesis.writtenScorePer}" pattern="#"></fmt:formatNumber>" />%
							</td>
							<td>答辩成绩比例：</td>
							<td><select id="thesis_usuallyScorePer"
								name="usuallyScorePer" class="required">
									<option value="${100-thesis.writtenScorePer}"><fmt:formatNumber
											value="${100-thesis.writtenScorePer}" pattern="#"></fmt:formatNumber>%
									</option>
							</select></td>
						</tr>
						<tr>
							<td>成绩审核发布时间:</td>
							<td colspan="3"><input type="text"
								id="thesis_graduateAuditDate" name="graduateAuditDate"
								style="width: 20%"
								value="<fmt:formatDate value="${thesis.graduateAuditDate}" pattern="yyyy-MM-dd"/>"
								class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	function genThesisUsuallyScoreRatio(){
		var writtenScorePer = $("#thesis_writtenScorePer").val();
		if(parseInt(writtenScorePer)>100){
			$("#thesis_writtenScorePer").attr("value","");
			$("#thesis_usuallyScorePer").children().remove();
			alertMsg.warn("请输入一个0-100之间的数字!");
			return false;
		}
		var usuallyScorePer = 100-writtenScorePer;
		var appendHTML = "<option value='"+usuallyScorePer.toFixed(2)+"' >"+usuallyScorePer+"%</option>";
		$("#thesis_usuallyScorePer").html(appendHTML);
	}	
	</script>
</body>
</html>