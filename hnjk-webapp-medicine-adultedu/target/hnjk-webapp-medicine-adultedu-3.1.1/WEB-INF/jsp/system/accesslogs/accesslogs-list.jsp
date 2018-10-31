<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统访问日志管理</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/accesslogs/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>访问开始时间：</label> <input type="text"
							name="accessStartTime" value="${condition['accessStartTime']}"
							class="Wdate" id="accessStartTime_1"
							onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true,maxDate:'#F{$dp.$D(\'accessEndTime_1\')}'})" />
						</li>
						<li><label>访问结束时间：</label><input type="text"
							name="accessEndTime" value="${condition['accessEndTime']}"
							class="Wdate" id="accessEndTime_1"
							onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true,minDate:'#F{$dp.$D(\'accessStartTime_1\')}'})" />
						</li>
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
			<gh:resAuth parentCode="RES_ACCESSLOGS_LIST" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_accesslogs"
							onclick="checkboxAll('#check_all_accesslogs','resourceid','#accessLogsBody')" /></th>
						<th width="8%">客户端IP</th>
						<th width="8%">访问者账号</th>
						<th width="10%">访问时间</th>
						<th width="8%">访问协议</th>
						<th width="15%">访问资源</th>
						<th width="8%">服务器返回状态</th>
						<th width="8%">访问流量</th>
						<th width="10%">服务器处理时间</th>
						<th width="10%">客户端操作系统</th>
						<th width="10%">客户端浏览器</th>
					</tr>
				</thead>
				<tbody id="accessLogsBody">
					<c:forEach items="${accessLogsPage.result }" var="accesslog"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${accesslog.resourceid }" autocomplete="off" /></td>
							<td>${accesslog.ipaddress }</td>
							<td>${accesslog.username }</td>
							<td><fmt:formatDate value="${accesslog.accessTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${accesslog.protocol }</td>
							<td>${accesslog.url }</td>
							<td>${accesslog.serverStatus }</td>
							<td>${accesslog.netFlow }</td>
							<td><fmt:formatNumber value="${accesslog.runningTime }"
									pattern="####0.000" /></td>
							<td>${ghfn:dictCode2Val('CodeClientOs',accesslog.clientOs)}</td>
							<td>${ghfn:dictCode2Val('CodeClientBrowser',accesslog.clientBrowser)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${accessLogsPage}"
				goPageUrl="${baseUrl}/edu3/system/accesslogs/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
	<script type="text/javascript">
function importAccessLogs(){
	var url = "${baseUrl}/edu3/framework/accesslogs/file/list.html";
	$.pdialog.open(url, "accessLogsFiles", "导入访问日志", {width: 800, height: 600 });
}
</script>
</body>
</html>