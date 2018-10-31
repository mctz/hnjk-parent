<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间设置列表</title>
<script type="text/javascript">
	//新增
	function addSetTime(){
		navTab.openTab('navTab', '${baseUrl}/edu3/system/settime/edit.html', '新增时间设置');
	}
	
	//修改
	function modifySetTime(){
		var url = "${baseUrl}/edu3/system/settime/edit.html";
		if(isCheckOnlyone('resourceid','#setTimeBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#setTimeBody input[@name='resourceid']:checked").val(), '编辑时间设置');
		}			
	}
		
	//删除
	function deleteSetTime(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/settime/delete.html","#setTimeBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/settime/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>时间类型：</label>
						<gh:select name="businessType" value="${condition[businessType]}"
								dictionaryCode="CodeBusinessType" style="width:52%" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_SETTIME" pageType="list"></gh:resAuth>
			<table id="gradeTab" class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_setTime"
							onclick="checkboxAll('#check_all_setTime','resourceid','#setTimeBody')" /></th>
						<th width="10%">时间类型</th>
						<th width="20%">开始时间</th>
						<th width="20%">预警时间</th>
						<th width="20%">结束时间</th>
						<th width="25%">备注</th>
					</tr>
				</thead>
				<tbody id="setTimeBody">
					<c:forEach items="${settimeList.result}" var="setTime"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${setTime.resourceid }" autocomplete="off" /></td>
							<td>${ghfn:dictCode2Val('CodeBusinessType',setTime.businessType) }</td>
							<td><fmt:formatDate value="${setTime.beginDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setTime.warnDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setTime.endDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${setTime.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${settimeList}"
				goPageUrl="${baseUrl }/edu3/system/settime/list.html" pageType="sys"
				condition="${condition}" />
		</div>
	</div>

</body>
</html>