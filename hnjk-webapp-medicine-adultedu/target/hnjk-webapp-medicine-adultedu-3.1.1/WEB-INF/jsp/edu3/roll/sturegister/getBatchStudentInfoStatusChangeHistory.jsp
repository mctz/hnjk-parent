<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍状态变更日志</title>
<style type="text/css">
</style>
<script type="text/javascript">
//导出学生信息维护历史
function exportStudentInfoChangeHistoryExcel(){
	var studentname = $("#studentname").val();
	var certnum   = $("#certnum").val();
	var studyno  = $("#studyno").val();
	var url = "${baseUrl}/edu3/register/studentinfo/exportBatchStudentInfoStatusChangeHistory.html"
		+"?studentname="+studentname
		+"&certnum="+certnum
		+"&studyno="+studyno;
	$('#frame_exportStudentInfoChangeHistory').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportStudentInfoChangeHistory";
	iframe.src = url;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
</script>
</head>
<body>

	<h2 class="contentTitle">学籍状态变更日志</h2>
	<div class="page">
		<div class="pageHeader">
			<form id="studentInfoStatusHisForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/getBatchStudentInfoStatusChangeHistory.html"
				method="post">
				<div id="studentInfoStatusHisBody" class="searchBar">
					<ul class="searchContent">
						<li style="width: 300px"><label>姓名:</label> <input
							type="text" name="studentname" id="studentname"
							value="${condition['studentname']}" style="width: 120px" /></li>
						<li style="width: 300px"><label>身份证号:</label> <input
							type="text" name="certnum" id="certnum"
							value="${condition['certnum']}" style="width: 120px" /></li>
						<li style="width: 300px"><label>学号:</label> <input
							type="text" name="studyno" id="studyno"
							value="${condition['studyno']}" style="width: 120px" /></li>
						<li style="width: 300px">修改时间段： <input type="text"
							id="operatedateb_id" style="width: 60px;" name="operatedateb"
							class="Wdate" value="${condition['operatedateb']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 到<input
							type="text" id="operatedatee_id" style="width: 60px;"
							name="operatedatee" class="Wdate"
							value="${condition['operatedatee']}"
							onfocus="WdatePicker({isShowWeek:true })" />
						</li>
					</ul>
					<ul class="searchContent">
						<li style="width: 300px"><label>年级:</label>
						<gh:selectModel id="gradeid_id" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}"
								orderBy="yearInfo.firstYear desc" choose="Y"
								style="width: 120px" /></li>
						<li style="width: 300px"><label>现学籍状态:</label>
						<gh:select name="studentstatus" id="studentstatus_id"
								value="${condition['studentstatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button"
											onclick="exportStudentInfoChangeHistoryExcel()">导出</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>


		<!-- 查询学生信息维护历史记录 -->
		<div id="getStuInfoChange">
			<div class="pageContent">
				<table class="table" layouth="175">
					<thead>
						<tr>
							<th width="11.7%">学号</th>
							<th width="5.5%">姓名</th>
							<th width="12.3%">身份证号</th>
							<th width="4%">年级</th>
							<th width="8%">专业</th>
							<th width="8%">层次</th>
							<th width="6%">班级</th>
							<th width="6%">办学单位</th>
							<th width="7%">改前学籍状态</th>
							<th width="7%">改后学籍状态</th>
							<th width="7%">变更日期</th>
							<th width="7.5%">操作人</th>
							<th width="10%">备注</th>


						</tr>
					</thead>
					<tbody>
						<c:forEach items="${studentInfoStatusHis.result }" var="history">
							<tr>
								<td title="${history['studyno']}">${history['studyno']}</td>
								<td title="${history['name']}">${history['name']}</td>
								<td title="${history['certnum']}">${history['certnum']}</td>
								<td title="${history['gradename']}">${history['gradename']}</td>
								<td title="${history['majorname']}">${history['majorname']}</td>
								<td title="${history['classicname']}">${history['classicname']}</td>
								<td title="${history['classesname']}">${history['classesname']}</td>
								<td title="${history['unitname']}">${history['unitname']}</td>
								<td title="${history['beforestatus']}">${history['beforestatus']}</td>
								<td
									<c:choose><c:when test="${history['afterstatus'] ne '延期'}">title="${history['afterstatus']}"</c:when><c:otherwise>title="延期 延期至 ${history['delaydate']}"</c:otherwise></c:choose>>${history['afterstatus']}<c:if
										test="${history['afterstatus'] eq '延期'}"> 延期至:${history['delaydate']}</c:if></td>
								<td title="${history['auditdate']}">${history['auditdate']}</td>
								<td title="${history['auditman']}">${history['auditman']}</td>
								<td title="${history['memo']}">${history['memo']}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${studentInfoStatusHis}"
					goPageUrl="${baseUrl }/edu3/register/studentinfo/getBatchStudentInfoStatusChangeHistory.html"
					condition="${condition }" pageType="sys" />

			</div>
		</div>

	</div>
</body>
</html>