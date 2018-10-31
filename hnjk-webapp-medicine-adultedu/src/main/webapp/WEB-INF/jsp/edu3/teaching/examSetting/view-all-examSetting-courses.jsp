<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看所有考试计划设置</title>
<script type="text/javascript">

</script>
</head>
<body>
	<script type="text/javascript">
 	$(document).ready(function(){         
 	    _w_table_rowspan("#examSettingDetailsViewTable",2);  
        _w_table_rowspan("#examSettingDetailsViewTable",1);   
    });
</script>
	<div class="page">
		<div class="pageContent">

			<div class="tabs">
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li class="selected"><a href="#"><span>考试计划设置课程信息</span></a></li>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;" layoutH="40">
					<div>
						<table class="list" width="100%" id="examSettingDetailsViewTable">
							<tr>
								<td><strong> 考试计划设置名</strong></td>
								<td><strong> 时间段</strong></td>
								<td><strong>考试课程</strong></td>
								<td><strong> 排序号</strong></td>


							</tr>
							<c:forEach items="${list}" var="examSettingDetails">
								<tr>
									<td>${examSettingDetails.examSetting.settingName }</td>
									<td>${ghfn:dictCode2Val('CodeCourseTime',examSettingDetails.examSetting.timeSegment)}</td>
									<td>${examSettingDetails.courseName }</td>
									<td>${examSettingDetails.showOrder }</td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</div>
			</div>

		</div>
	</div>
</body>
