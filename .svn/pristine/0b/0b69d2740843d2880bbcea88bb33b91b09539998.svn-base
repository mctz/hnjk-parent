<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${val eq 'statusonly' ?"学籍状态变更日志":"信息修改日志"}</title>
<style type="text/css">
</style>
<script type="text/javascript">
//导出学生信息维护历史
function exportStudentInfoChangeHistoryExcel(){
	var studentname = $("#studentname").val();
	var certnum   = $("#certnum").val();
	var studyno  = $("#studyno").val();
	var url = "${baseUrl}/edu3/register/studentinfo/exportStudentInfoChangeHistory.html"
		+"?studentname="+studentname
		+"&certnum="+certnum
		+"&studyno="+studyno+"&val=${val}";
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

	<h2 class="contentTitle">${val eq 'statusonly' ?"学籍状态变更日志":"信息修改日志"}</h2>
	<div class="page">
		<div class="pageHeader">
			<form id="statStudentInfoCondition"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/studentinfo/getStudentInfoChangeHistory.html?val=${val}"
				method="post">
				<div id="studentInfoAud" class="searchBar">
					<ul class="searchContent" id="stuStudentStatus">
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
					<c:if test="${val eq 'statusonly'}">
						<ul class="searchContent">
							<li style="width: 300px"><label>年级:</label>
							<gh:selectModel id="gradeid_id" name="gradeid"
									bindValue="resourceid" displayValue="gradeName"
									modelClass="com.hnjk.edu.basedata.model.Grade"
									value="${condition['gradeid']}"
									orderBy="yearInfo.firstYear desc" choose="Y"
									style="width: 110px" /></li>
							<li style="width: 300px"><label>现学籍状态:</label>
							<gh:select name="studentstatus" id="studentstatus_id"
									value="${condition['studentstatus']}"
									dictionaryCode="CodeStudentStatus" style="width:110px" /></li>
						</ul>
					</c:if>

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
				<table class="table" layouth="152">
					<thead>
						<tr>
							<th width="9.7%">学号</th>
							<th width="5.5%">姓名</th>
							<th width="11.3%">身份证号</th>
							<th width="4%">年级</th>
							<c:if test="${val eq 'statusonly'}">
								<th width="6%">专业</th>
								<th width="6%">层次</th>
							</c:if>
							<th width="6%">班级</th>
							<th width="6%">办学单位</th>
							<!--  <th width="7.5%">修改实体</th>-->
							<c:if test="${val ne 'statusonly'}">
								<th width="10%">修改字段</th>
							</c:if>
							<th width="9%">${val eq 'statusonly' ?"改前学籍状态":"改前信息"}</th>
							<th width="9%">${val eq 'statusonly' ?"改后学籍状态":"改后信息"}</th>

							<th width="10%">变更日期</th>
							<th width="7.5%">操作人</th>
							<th width="5%">操作单位</th>
							<th width="5%">操作IP</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach items="${stuInfosChangeHistory.result }" var="history">
							<tr>
								<td>${history.bloodType}</td>
								<td>${history.accountStatus}</td>
								<td>${history.bornAddress}</td>
								<td>${history.email}</td>
								<c:if test="${val eq 'statusonly'}">
									<td>${history.currenAddress}</td>
									<td>${history.degree}</td>
								</c:if>
								<td>${history.diplomaNum}</td>
								<td>${history.eduYear}</td>
								<!-- <td>${history.bornDay}</td> -->
								<c:if test="${val ne 'statusonly'}">
									<td>${history.certNum}</td>
								</c:if>
								<td>${history.certType}</td>
								<td>${history.country}</td>

								<td>${fn:split(history.contactZipcode,' ')[0]}</td>
								<td>${history.classic}</td>
								<td>${history.contactAddress}</td>
								<td>${history.contactPhone}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${stuInfosChangeHistory}"
					goPageUrl="${baseUrl }/edu3/register/studentinfo/getStudentInfoChangeHistory.html"
					condition="${condition }" pageType="sys" />

			</div>
		</div>

	</div>
</body>
</html>