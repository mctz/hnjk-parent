<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学活动环节中的时间设置-列表</title>
<script type="text/javascript">
	//新增
	function activityTimeSettingAdd(){
		navTab.openTab('RES_TEACHING_TEACHING_ACTIVITY_TIME_SETTING_FORM', '${baseUrl}/edu3/teaching/teachingactivitytimeSetting/form.html', '新增活动时间');
	}
	//修改
	function activityTimeSettingEdit(){
		var url = "${baseUrl}/edu3/teaching/teachingactivitytimeSetting/form.html";
		if(isCheckOnlyone('resourceid','#teachingactivitytimeSettingBody')){
			navTab.openTab('RES_TEACHING_TEACHING_ACTIVITY_TIME_SETTING_FORM', url+'?resourceid='+$("#teachingactivitytimeSettingBody input[@name='resourceid']:checked").val(), '编辑活动时间');
		}			
	}
	//删除
	function activityTimeSettingDel(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachingactivitytimeSetting/del.html","#teachingactivitytimeSettingBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="teachingactivitytimeSettingSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingactivitytimeSetting/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel id="teachingactivitytimeSetting_yearInfoId"
								name="yearInfoId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="yearName desc"
								style="width:55%" /></li>
						<li><label>学期：</label>
						<gh:select id="teachingactivitytimeSetting_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								filtrationStr="1,2" style="width:55%" /></li>
						<li><label>业务类型：</label> <gh:select
								id="teachingactivitytimeSetting_mainProcessType"
								name="mainProcessType" value="${condition['mainProcessType']}"
								dictionaryCode="CodeTeachingActivity" style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>开始时间：</label><input type="text"
							id="teachingactivitytimeSetting_startTime" name="startTime"
							value="<fmt:formatDate value="${condition['startTime']}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'teachingactivitytimeSetting_endTime\')}'})"
							width="53%" /></li>
						<li><label>结束时间：</label><input type="text"
							id="teachingactivitytimeSetting_endTime" name="endTime"
							value="<fmt:formatDate value="${condition['endTime']}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							class="Wdate"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'teachingactivitytimeSetting_startTime\')}'})"
							width="53%" /></li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth
				parentCode="RES_TEACHING_TEACHING_ACTIVITY_TIME_SETTING_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_teachingactivitytimesetting"
							onclick="checkboxAll('#check_all_teachingactivitytimesetting','resourceid','#teachingactivitytimeSettingBody')" /></th>
						<th width="15%">年度</th>
						<th width="8%">学期</th>
						<th width="8%">业务类型</th>
						<th width="18%">开始时间</th>
						<th width="18%">结束时间</th>
						<th width="18%">预警时间</th>
						<th width="10%">备注</th>
					</tr>
				</thead>
				<tbody id="teachingactivitytimeSettingBody">
					<c:forEach items="${page.result}" var="setting" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${setting.resourceid }" autocomplete="off" /></td>
							<td>${setting.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',setting.term) }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingActivity',setting.mainProcessType) }</td>
							<td><fmt:formatDate value="${setting.startTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setting.endTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setting.warningTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${setting.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingactivitytimeSetting/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
