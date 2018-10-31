<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachtask/listDetail.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfoid" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoid']}" orderBy="firstYear desc" /></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
						<li><label>课程：</label><input type="text" name='courseName'
							value="${condition['courseName']}" /></li>

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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_TASKDETAIL"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tkd"
							onclick="checkboxAll('#check_all_tkd','resourceid','#taskdBody')" /></th>
						<th width="10%">年度</th>
						<th width="10%">学期</th>
						<th width="20%">课程</th>
						<th width="10%">返回时限</th>
						<th width="10%">任务书状态</th>
						<th width="15%">备注</th>
						<th width="10%">&nbsp;</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="taskdBody">
					<c:forEach items="${taskList.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off"
								rel="${t.taskStatus }" /> <input type="hidden"
								id="yearInfo_${t.resourceid }" value="${t.yearInfo.resourceid }" />
								<input type="hidden" id="term_${t.resourceid }"
								value="${t.term }" /> <input type="hidden"
								id="course_${t.resourceid }" value="${t.course.resourceid }" />
							</td>
							<td>${t.yearInfo }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',t.term) }</td>
							<td>${t.course.courseCode }-${t.course.courseName }</td>
							<td><fmt:formatDate value='${t.returnTime}'
									pattern='yyyy-MM-dd' /></td>
							<td>${ghfn:dictCode2Val('CodeTaskStatus',t.taskStatus) }</td>
							<td>${t.memo }</td>
							<td><a height="600" width="800" target="dialog"
								href="${baseUrl }/edu3/teaching/teachtask/view.html?resourceid=${t.resourceid }"
								rel="view_teachtask">查看</a></td>
							<td><a height="600" width="800" target="dialog"
								href="${baseUrl }/edu3/framework/teaching/studentlearnplan/list.html?taskId=${t.resourceid }"
								rel="view_studentlearnplan">查看学生名单</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${taskList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachtask/listDetail.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function modifyTaskDetail(){
		var url = "${baseUrl}/edu3/teaching/teachtask/editDetail.html";
		if(isCheckOnlyone('resourceid','#taskdBody')){
			var taskStatus = $("#taskdBody input[@name='resourceid']:checked").attr("rel");
			if(taskStatus=='2'){
				alertMsg.warn("任务书已经提交给教学办！");
			} else if(taskStatus=='3'){
				alertMsg.warn("任务书已经审核发布！");
			} else {
				navTab.openTab('RES_TEACHING_TASKDETAIL_MODIFY', url+'?resourceid='+$("#taskdBody input[@name='resourceid']:checked").val(), '填写教学任务');
			}			
		}
	}
	function sendBackTask(){
		pageBarHandle("您确定要提交这些任务书吗？","${baseUrl}/edu3/teaching/teachtask/sendBack.html","#taskdBody");
	}
	
	//课程公告管理
	function listCourseNotice(){
		var url = "${baseUrl}/edu3/teaching/coursenotice/list.html";
		if(isCheckOnlyone('resourceid','#taskdBody')){
			var taskId = $("#taskdBody input[@name='resoutceid']:checked").val();
			var yearInfoId = $("#yearInfo_"+taskId).val();
			var term = $("#term_"+taskId).val();
			var courseId = $("#course_"+taskId).val();
			navTab.openTab('courseNotice', url+'?courseId='+courseId+'&yearInfoId='+yearInfoId+'&term='+term, '课程公告管理');
		}
	}
</script>
</body>
</html>