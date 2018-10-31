<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生批次表单-设置录取查询时间</title>

</head>
<body>
<body>
	<h2 class="contentTitle">设置录取查询时间</h2>
	<div class="page">
		<div class="pageContent">
			<form id="planForm" method="post"
				action="${baseUrl}/edu3/recruit/matriculate/enrollStartTimeAndEndTime-save.html"
				class="pageForm"
				onsubmit="return validateRecruitPlanStartTimeAndEndTimeForm(this);">
				<div layoutH="77">
					<table id="planTable" class="form">
						<input type="hidden" name="recruitPlanId"
							value="${recruitPlan.resourceid }" />
						<tr>
							<td style="width: 12%">招生批次名称:</td>
							<td style="width: 17%" colspan="7">
								${recruitPlan.recruitPlanname }</td>
						</tr>
						<tr>
							<td style="width: 12%">录取查询开放时间:</td>
							<td style="width: 17%"><input type="text"
								name="enrollStartTime" size="40"
								value="${recruitPlan.enrollStartTime }" class="required"
								onFocus="WdatePicker({isShowWeek:true})" /></td>
							<td style="width: 12%">录取查询截止时间:</td>
							<td style="width: 17%"><input type="text"
								name="enrollEndTime" size="40"
								value="${recruitPlan.enrollEndTime }" class="required"
								onFocus="WdatePicker({isShowWeek:true})" /></td>
							<td style="width: 12%">报到开始时间:</td>
							<td style="width: 17%"><input type="text" name="reportTime"
								size="40" value="${recruitPlan.reportTime }" class="required"
								onFocus="WdatePicker({isShowWeek:true})" /></td>
							<td style="width: 12%">报到结束时间:</td>
							<td style="width: 17%"><input type="text" name="endTime"
								size="40" value="${recruitPlan.endTime }" class="required"
								onFocus="WdatePicker({isShowWeek:true})" /></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button id="plansubmit" type="submit">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		//自定义回调方法
		function validateRecruitPlanStartTimeAndEndTimeForm(obj){
			return validateCallback(obj,_startTimeAndEndTimeNavTabAjaxDone);
		}
		//回调处理方法
		function _startTimeAndEndTimeNavTabAjaxDone(json){
			if (json.navTabId){
				navTab.closeCurrentTab();
				navTab.reload(json.reloadTabUrl, $("#matriculateSearchStartTimeAndEndTimeForm").serializeArray(),json.navTabId);
			}		
		}
	</script>
</body>

</html>