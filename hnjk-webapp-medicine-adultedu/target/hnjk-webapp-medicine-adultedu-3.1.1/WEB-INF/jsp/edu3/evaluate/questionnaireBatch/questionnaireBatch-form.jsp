<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教评批次设置</title>
<script type="text/javascript">
 $(function(){
	 var isPublish="${questionnaireBatch.isPublish}";	 
	 if(isPublish=='Y'){		 
		 $("#questionBatchFormYearId").attr("disabled", "disabled");
		 $("#questionBatchFormTerm").attr("disabled", "disabled");
	 }
 })
</script>
</head>
<body>
	<h2 class="contentTitle">编辑批次内容</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/quality/evaluation/questionnaireBatch/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${questionnaireBatch.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%"><strong>年度:</strong></td>
							<td style="width: 38%"><gh:selectModel id="questionBatchFormYearId" 
									name="yearId" bindValue="resourceid" displayValue="yearName"
									style="width:53%"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									orderBy="firstYear desc"
									value="${questionnaireBatch.yearInfo.resourceid}" choose="N" /></td>
							<td style="width: 12%"><strong>学期:</strong></td>
							<td style="width: 38%"><gh:select id="questionBatchFormTerm" name="term" 
									dictionaryCode="CodeTerm" value="${questionnaireBatch.term}" choose="N"
									style="width:53%" /></td>
						</tr>
						<tr>
							<td><strong>面授开始时间:</strong></td>
							<td>
							<input type="text" id="fstartTime" name="faceStartTime"
								class="required" style="width: 50%"
								value='<fmt:formatDate  value="${questionnaireBatch.faceStartTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'fendTime\')}'})" />
<!-- 							<input type="text" -->
<%-- 												value="<fmt:formatDate value='${questionnaireBatch.faceStartTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" --%>
<!-- 												name="faceStartTime" id="fstartTime" class="required" -->
<!-- 												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'fendTime\')}'})" /> -->
							</td>
							<td><strong>面授截止时间:</strong></td>
							<td><input type="text" id="fendTime" name="faceEndTime"
								class="required" style="width: 50%"
								value='<fmt:formatDate value="${questionnaireBatch.faceEndTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'fstartTime\')}'})" />
							</td>

						</tr>
						
						<tr>
							<td><strong>网络开始时间:</strong></td>
							<td><input type="text" id="nstartTime" name="netStartTime"
								class="required" style="width: 50%"
								value='<fmt:formatDate  value="${questionnaireBatch.netStartTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'nendTime\')}'})" />
							</td>
							<td><strong>网络截止时间:</strong></td>
							<td><input type="text" id="nendTime" name="netEndTime"
								class="required" style="width: 50%"
								value='<fmt:formatDate value="${questionnaireBatch.netEndTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
								onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'nstartTime\')}'})" />
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>