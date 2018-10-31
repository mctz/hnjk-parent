<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习资源管理</title>
<script type="text/javascript">
	//随堂练习添加编辑入口
	function mgrActiveCourseExam(courseId){	
		var checkurl = "${baseUrl}/edu3/metares/exercise/active/checkSyllabus.html";
		jQuery.post(checkurl,{courseId:courseId},function(isEmptyTree){
			 if(isEmptyTree){	
			 	alertMsg.warn("你还没建立该门课程的知识结构树！");
			 } else {
			 	var url = "${baseUrl}/edu3/metares/exercise/active/addactivecourseexam.html";
				navTab.openTab('navTab', url+'?courseId='+courseId, '随堂练习管理');
			 }
		},"json");	
	}
	$(function (){
		var courseIds = new Array();
		$("#activecourseexam_tcourseBody input[name='courseId']").each(function (){
			courseIds.push($(this).val());
		});
		var url = "${baseUrl}/edu3/metares/exercise/active/getCount.html";
		jQuery.post(url,{courseIds:courseIds.join(',')},function(json){
			$("#activecourseexam_tcourseBody td[name='count']").each(function (){
				$(this).text(json[this.id]);
			});	
		},"json");	
	});	
	//导出习题
	function exportActiveCourseExam(){
		if(isCheckOnlyone('resourceid','#activecourseexam_tcourseBody')){
			var url = "${baseUrl}/edu3/metares/exercise/activeexercise/export.html?courseId="+$("#activecourseexam_tcourseBody input[@name='resourceid']:checked").val();
			window.location.href=url;
		}
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/active/listCourse.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名称：</label><input type="text" name="courseName"
							value="${condition['courseName']}" /></li>
						<li><label>课程编码：</label><input type="text" name="courseCode"
							value="${condition['courseCode']}" /></li>
						<li><label>状态：</label>
						<gh:select name="status" value="${condition['status']}"
								dictionaryCode="CodeCourseState" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">查 询</button>
								</div>
							</div>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_EXERCISE_ACTIVE" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_activecourseexam_tcourse"
							onclick="checkboxAll('#check_all_activecourseexam_tcourse','resourceid','#activecourseexam_tcourseBody')" /></th>
						<th width="15%">课程编号</th>
						<th width="35%">课程名称</th>
						<th width="10%">课程状态</th>
						<th width="10%">总练习题数</th>
						<th width="25%">&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<tbody id="activecourseexam_tcourseBody">
					<c:forEach items="${courseListPage.result}" var="course"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${course.resourceid }" autocomplete="off" /></td>
							<td>${course.courseCode }<input type="hidden"
								name="courseId" value="${course.resourceid }" /></td>
							<td><a
								href="${baseUrl }/edu3/teaching/teachingcourse/view.html?courseId=${course.resourceid }"
								target="dialog" mask="true" width="700" height="400"
								title="${course.courseName }" style="color: blue">${course.courseName }</a></td>
							<td>${ghfn:dictCode2Val('CodeCourseState',course.status ) }</td>
							<td style="text-align: center;" id="${course.resourceid }"
								name="count"></td>
							<td><a href="javascript:;"
								onclick="mgrActiveCourseExam('${course.resourceid }');"
								style="color: blue;">随堂练习管理</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseListPage}"
				goPageUrl="${baseUrl }/edu3/metares/exercise/active/listCourse.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>