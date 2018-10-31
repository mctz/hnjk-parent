<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约明细</title>


</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var errorMsg = "${errorMsg}";
		if(null!=errorMsg && ""!=errorMsg){
			alertMsg.warn(errorMsg);
		}
	})
	//导出预约明细
	function exportCourseOrderDetails(){
		var url = "${baseUrl}/edu3/teaching/courseorder/courseOrder-details-export.html?"+$("#coursedetails_search_form").serialize();
		downloadFileByIframe(url,'courseOrderDetailsExportIframe');
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="coursedetails_search_form"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/courseOrder-details.html"
				method="post">
				<input type="hidden" name="major" value="${condition['major']}" />
				<input type="hidden" name="grade" value="${condition['grade']}" />
				<input type="hidden" name="status" value="${condition['status']}" />
				<input type="hidden" name="statid" value="${condition['statid']}" />
				<input type="hidden" name="classic" value="${condition['classic']}" />
				<input type="hidden" name="yearInfo"
					value="${condition['yearInfo']}" /> <input type="hidden"
					name="term" value="${condition['term']}" /> <input type="hidden"
					name="courseId" value="${condition['courseId']}" /> <input
					type="hidden" name="flag" value="${condition['flag']}" /> <input
					type="hidden" name="branchSchool"
					value="${condition['branchSchool']}" /> <input type="hidden"
					name="orderExamEndTime" value="${condition['orderExamEndTime']}" />
				<input type="hidden" name="orderExamStartTime"
					value="${condition['orderExamStartTime']}" /> <input
					type="hidden" name="orderCourseEndTime"
					value="${condition['orderCourseEndTime']}" /> <input
					type="hidden" name="orderCourseStartTime"
					value="${condition['orderCourseStartTime']}" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" /></li>
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
		<div class="pageContent" defH="150">
			<div class="panelBar">
				<ul class="toolBar">
					<li><a class="icon" onclick="exportCourseOrderDetails()"
						href="#" title="导出明细列表"> <span>导出明细列表</span>
					</a></li>
				</ul>
			</div>
			<table class="table" layouth="130" width="100%">
				<thead>
					<tr>
						<th width="5%" align="center">序号</th>
						<th width="10%">教学站</th>
						<th width="10%" align="center">姓名</th>
						<th width="10%" align="center">学号</th>
						<th width="10%" align="center">预约课程</th>
						<th width="10%" align="center">课程状态</th>
						<th width="10%" align="center">预约学习时间</th>
						<th width="10%" align="center">预约考试时间</th>
						<th width="10%" align="center">手机</th>
						<th width="10%" align="center">Email</th>
					</tr>
				</thead>

				<tbody id="courseOrderDetailsBody">
					<c:forEach items="${page.result}" var="courseorder" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${courseorder.studentInfo.branchSchool.unitShortName}</td>
							<td title="${courseorder.studentInfo.studentName }">${courseorder.studentInfo.studentName }</td>
							<td>${courseorder.studentInfo.studyNo}</td>
							<td title="${courseorder.courseOrderStat.course.courseName}">${courseorder.courseOrderStat.course.courseName}</td>
							<td>${ghfn:dictCode2Val('CodeCourseOrderStatus',courseorder.status) }</td>
							<td
								title="<fmt:formatDate value="${courseorder.orderCourseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"><fmt:formatDate
									value="${courseorder.orderCourseTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td
								title="<fmt:formatDate value="${courseorder.orderExamTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "><fmt:formatDate
									value="${courseorder.orderExamTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${courseorder.studentInfo.studentBaseInfo.mobile}</td>
							<td>${courseorder.studentInfo.studentBaseInfo.email}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/courseOrder-details.html"
				targetType="dialog" pageType="sys" condition="${condition}" />
		</div>

	</div>
</body>
</html>