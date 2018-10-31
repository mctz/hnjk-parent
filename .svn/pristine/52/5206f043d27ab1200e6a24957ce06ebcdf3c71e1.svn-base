<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看考试计划设置</title>
<script type="text/javascript">

</script>
</head>
<body>

	<div class="page">
		<div class="pageContent">

			<div class="tabs">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li class="selected"><a href="#"><span>考试计划设置信息</span></a></li>
							<li><a href="#"><span>考试计划设置课程信息</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;" layoutH="40">
					<div>
						<table class="form">
							<tr>
								<td><strong>考试计划设置名称:</strong></td>
								<td>${examSetting.settingName }</td>
							</tr>
							<tr>
								<td><strong>时间段:</strong></td>
								<td>${ghfn:dictCode2Val('CodeCourseTime',examSetting.timeSegment)}</td>
							</tr>
							<tr>
								<td><strong>开始时间:</strong></td>
								<td>${examSetting.startTime }</td>
							</tr>
							<tr>
								<td><strong>结束时间:</strong></td>
								<td>${examSetting.endTime }</td>
							</tr>
						</table>
					</div>
					<div>
						<table class="list" width="100%">
							<tr>
								<td><strong> 排序号</strong></td>
								<td><strong>考试课程</strong></td>
							</tr>
							<c:forEach items="${examSetting.details}" var="course">
								<tr>
									<td>${course.showOrder }</td>
									<td>${course.courseName }</td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</div>
			</div>

		</div>
	</div>
</body>
