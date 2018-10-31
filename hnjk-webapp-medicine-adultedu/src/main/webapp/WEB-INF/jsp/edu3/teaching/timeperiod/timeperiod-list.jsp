<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学站时间管理</title>
<script type="text/javascript">
function addTimePeriod(){
	navTab.openTab('RES_TEACHING_COURSETIMEPERIOD_INPUT', '${baseUrl}/edu3/teaching/timeperiod/input.html', '新增时间设置');
}

//修改
function modifyTimePeriod(){
	var url = "${baseUrl}/edu3/teaching/timeperiod/input.html";
	if(isCheckOnlyone('resourceid','#coursetimeperiodBody')){
		navTab.openTab('RES_TEACHING_COURSETIMEPERIOD_INPUT', url+'?resourceid='+$("#coursetimeperiodBody input[name='resourceid']:checked").val(), '编辑时间设置');
	}			
}
	
//删除
function removeTimePeriod(){	
	pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/timeperiod/remove.html","#coursetimeperiodBody");
}

//删除重复记录
function distinctTimePeriod() {
    $.post("${baseUrl}/edu3/teaching/timeperiod/distinct.html",{}, navTabAjaxDone, "json");
}
</script>
</head>
<body>
	<div class="page">
		<c:if test="${not isBrschool }">
			<div class="pageHeader">
				<form onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/teaching/timeperiod/list.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">

							<li style="width: 360px;"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="brSchoolid" tabindex="1" style="width:240px;"
									id="coursetimeperiod_orgUnitName" displayType="code"
									defaultValue="${condition['brSchoolid']}" /></li>
							<div class="buttonActive" style="float: right;">
								<div class="buttonContent">
									<button type="submit">查 询</button>
								</div>
							</div>
						</ul>
					</div>
				</form>
			</div>
		</c:if>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_COURSETIMEPERIOD"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="${not isBrschool ? '113':'76' }">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_coursetimeperiod"
							onclick="checkboxAll('#check_all_coursetimeperiod','resourceid','#coursetimeperiodBody')" /></th>
						<th width="20%">教学站</th>
						<th width="20%">时间段</th>
						<th width="20%">上课时间名称</th>
						<th width="35%">上课时间</th>
					</tr>
				</thead>
				<tbody id="coursetimeperiodBody">
					<c:forEach items="${timePeriodPage.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off" /></td>
							<td>${t.brSchool.unitName }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTimePeriod',t.timePeriod ) }</td>
							<td>${t.timeName }</td>
							<td><fmt:formatDate value="${t.startTime }" pattern="HH:mm" />-<fmt:formatDate
									value="${t.endTime }" pattern="HH:mm" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${timePeriodPage}"
				goPageUrl="${baseUrl }/edu3/teaching/timeperiod/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html