<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程公告管理</title>
<script type="text/javascript">
	//新增
	function addCourseNotice(){
		navTab.openTab('RES_TEACHING_COURSENOTICE_INPUT', '${baseUrl}/edu3/teaching/coursenotice/input.html?courseId='+$("#courseNotice_courseId").val(), '新增课程公告');
	}
	
	//修改
	function modifyCourseNotice(){
		var url = "${baseUrl}/edu3/teaching/coursenotice/input.html";
		if(isCheckOnlyone('resourceid','#courseNoticeBody')){
			navTab.openTab('RES_TEACHING_COURSENOTICE_INPUT', url+'?resourceid='+$("#courseNoticeBody input[@name='resourceid']:checked").val(), '编辑课程公告');
		}			
	}
		
	//删除
	function deleteCourseNotice(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/coursenotice/delete.html","#courseNoticeBody");
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="courseNoticeForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/coursenotice/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 360px;"><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="courseNotice_courseId" displayType="code" style="width:240px;"
								isFilterTeacher="Y" value="${condition['courseId']}" /></li>
						<li style="width: 200px;"><label style="width: 50px;">年度：</label>
						<gh:selectModel name="yearInfoId" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc" /></li>
						<li style="width: 200px;"><label style="width: 50px;">学期：</label>
							<gh:select name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" /></li>
					</ul>
					<ul class="searchContent">
						<li style="width: 360px;"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="coursenotice_list_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}" style="width:240px;"></gh:classesAutocomplete></li>
						<li style="width: 360px;"><label>通知标题：</label><input type="text" name="noticeTitle"
							value="${condition['noticeTitle']}" style="width: 240px;"/></li>
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
			<gh:resAuth parentCode="RES_TEACHING_COURSENOTICE" pageType="cnlist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_coursenotice"
							onclick="checkboxAll('#check_all_coursenotice','resourceid','#courseNoticeBody')" /></th>
						<th width="20%">所属课程</th>
						<th width="20%">班级</th>
						<th width="10%">年度</th>
						<th width="5%">学期</th>
						<th width="20%">通知标题</th>
						<th width="6%">填写人</th>
						<th width="20%">填写日期</th>
					</tr>
				</thead>
				<tbody id="courseNoticeBody">
					<c:forEach items="${courseNoticePage.result}" var="courseNotice"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${courseNotice.resourceid }" autocomplete="off" /></td>
							<td>${courseNotice.course.courseName}</td>
							<td>${courseNotice.classes.classname==null?'所有有该门课程的班级':courseNotice.classes.classname}</td>
							<td>${courseNotice.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',courseNotice.term)}</td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/coursenotice/view.html?courseNoticeId=${courseNotice.resourceid }','courseNotice','${courseNotice.noticeTitle }',{width:500,height:350});">${courseNotice.noticeTitle }</a></td>
							<td>${courseNotice.fillinMan }</td>
							<td>${courseNotice.fillinDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseNoticePage}"
				goPageUrl="${baseUrl }/edu3/teaching/coursenotice/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>