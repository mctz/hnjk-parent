<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试计划设置</title>
<script type="text/javascript">
	//新增
	function addPlansetting(){
		navTab.openTab('examSettin_AddForm', '${baseUrl}/edu3/teaching/exam/plansetting/input.html', '新增考试计划设置');
	}
	
	//修改
	function modifyPlansetting(){
		var url = "${baseUrl}/edu3/teaching/exam/plansetting/input.html";
		if(isCheckOnlyone('resourceid','#examSettingBody')){
			navTab.openTab('examSettin_Modify', url+'?resourceid='+$("#examSettingBody input[@name='resourceid']:checked").val(), '编辑考试计划设置');
		}			
	}
		
	//删除
	function deletePlansetting(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/exam/plansetting/remove.html","#examSettingBody");
	}
	
	//查看已分配课程
	function viewAllExamsettingDetails(){
		var url = "${baseUrl}/edu3/teaching/exam/plansetting/view-all-course.html";
		$.pdialog.open(url,"RES_TEACHING_EXAM_PLANSETTING_VIEW_ALL_SETTING_COURSE","查看已分配课程", {width:800, height:600});
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exam/plansetting/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试课程:</label> <gh:courseAutocomplete
								name="courseid" tabindex="1" id="courseid"
								value="${condition['courseid'] }" displayType="code" /></li>
						<li><label>开始时间：</label> <input type="text"
							id="settingStartTime" name="startTime"
							value="${condition['startTime'] }"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'HH:mm:ss',maxDate:'#F{$dp.$D(\'settingEndTime\')}'})" />'
						</li>
						<li><label>结束时间：</label> <input type="text"
							id="settingEndTime" name="endTime"
							value="${condition['endTime'] }"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'HH:mm:ss',minDate:'#F{$dp.$D(\'settingStartTime\')}'})" />
						</li>
						<li><label>时间段：</label> <gh:select name="timeSegment"
								dictionaryCode="CodeCourseTime"
								value="${condition['timeSegment']}" choose="Y" /></li>
						<li><label>考试计划设置名称：</label> <input name="settingName"
							type="text" value="${condition['settingName']}" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_EXAM_PLANSETTING"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tp"
							onclick="checkboxAll('#check_all_tp','resourceid','#examSettingBody')" /></th>
						<th width="20%">考试计划设置名称</th>
						<th width="25%">时间段</th>
						<th width="25%">开始时间</th>
						<th width="25%">结束时间</th>
					</tr>
				</thead>
				<tbody id="examSettingBody">
					<c:forEach items="${examSettingList.result}" var="examSetting"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examSetting.resourceid }" autocomplete="off" /></td>
							<td><a
								href="${baseUrl }/edu3/teaching/exam/plansetting/view-course.html?examSettingId=${examSetting.resourceid }"
								target="dialog" title="查看考试课程" mask="true" width="600"
								height="400"> ${examSetting.settingName }</a></td>
							<td>${ghfn:dictCode2Val('CodeCourseTime',examSetting.timeSegment)}</td>
							<td>${examSetting.startTime }</td>
							<td>${examSetting.endTime }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${examSettingList}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/plansetting/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
