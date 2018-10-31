<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统操作日志管理</title>
<style type="text/css">
	td,th{text-align: center;}
</style>
</head>
<body>
<script type="text/javascript">
	$(document).ready(function(){
		operationLogQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function operationLogQueryBegin() {
		var defaultValue = "${condition['brshSchool']}";
		var schoolId = "";
		var isBrschool = "${isBrschool}";
        if(isBrschool==true || isBrschool=="true"){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['grade']}";
		var classicId = "${condition['classic']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['classes']}";
		var selectIdsJson = "{unitId:'operationLog_brshSchool',gradeId:'menu_grade',classicId:'menu_classic',majorId:'menu_major',classesId:'menu_classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
	}
	function showContent(resourceid){
		var url = "${baseUrl }/edu3/system/userOperationLogs/showContent.html?resourceid="+resourceid;
		$.pdialog.open(url, "operationLogContent", "操作内容", {width: 400, height: 300 });
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/userOperationLogs/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li style="width: 360px;"><label>教学站：</label> <span
								sel-id="operationLog_brshSchool" sel-name="brshSchool"
								sel-onchange="operationLogrollQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="brshSchool"
								value="${condition['brshSchool']}" />
						</c:if> --%>
						<li><label>操作人：</label>
							<input type="text" name="userName" value="${condition['userName'] }" />
						</li>
						<li><label>所属模块：</label>
							<gh:select name="modules"
								dictionaryCode="systemLogsModulesCode"
								value="${condition['modules']}" choose="Y" style="width: 120px" />
						</li>
						<li><label>操作类型：</label>
							<gh:select name="operationType"
								dictionaryCode="systemLogs_operationtype"
								value="${condition['operationType']}" style="width: 100px" />
						</li>
					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>操作内容：</label>
							<input type="text" name="operationContent" value="${condition['operationContent'] }" style="width: 240px;"/>
						</li>
						<li style="width: 300px;"><label>操作时间：</label><input type="text" style="width: 80px;"
							id="recordStartTime" name="recordStartTime" class="Wdate"
							value="${condition['recordStartTime']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 至 <input
							type="text" style="width: 80px;" id="recordEndTime"
							name="recordEndTime" class="Wdate"
							value="${condition['recordEndTime']}"
							onfocus="WdatePicker({isShowWeek:true })" />
						</li>
						<%-- <li><label>开始日期：</label> <input type="text" id="recordStartTime" name="recordStartTime" size="40" style="width:120px" value="<fmt:formatDate value="${condition['recordStartTime']}" pattern="yyyy-MM-dd" />" class="date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/>
						</li>
						<li><label>截止日期：</label> <input type="text" id="recordEndTime" name="recordEndTime" size="40" style="width:120px" value="<fmt:formatDate value="${condition['recordEndTime']}" pattern="yyyy-MM-dd" />" class="date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/>
						</li> --%>
					</ul>
					<ul>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_USEROPERATIONLOGS_LIST" pageType="list"></gh:resAuth>
			<table class="table" layouth="165">
				<thead>
					<tr>
						<th width="10%">客户端IP</th>
						<th width="10%">用户名</th>
						<th width="10%">所属模块</th>
						<th width="10%">操作类型</th>
						<th width="30%">操作内容</th>
						<th width="15%">操作时间</th>
					</tr>
				</thead>
				<tbody id="operationLogsBody">
					<c:forEach items="${operationLogsPage.result }" var="log"
						varStatus="vs">
						<tr>
							<td width="10%">${log.ipaddress }</td>
							<td width="10%">${log.userName }</td>
							<td width="10%">${ghfn:dictCode2Val('systemLogsModulesCode',log.modules)}</td>
							<td width="10%">${ghfn:dictCode2Val('systemLogs_operationtype',log.operationType)}</td>
							<td style="text-align: left;" width="30%" onclick="showContent('${log.resourceid}')">
								 <c:if test="${fn:length(log.operationContent)>50 }">${fn:substring(log.operationContent, 0, 50)}... </c:if>  
                 				 <c:if test="${fn:length(log.operationContent)<=50 }">${log.operationContent} </c:if>  
							</td>
							<td width="15%"><fmt:formatDate value="${log.recordTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div  class="pageContent" style="position: absolute;bottom: 0px;width: 100%">
			<gh:page page="${operationLogsPage}"
				goPageUrl="${baseUrl }/edu3/system/userOperationLogs/list.html"
				pageType="sys" condition="${condition}" /></div>
	</div>
</body>
</html>