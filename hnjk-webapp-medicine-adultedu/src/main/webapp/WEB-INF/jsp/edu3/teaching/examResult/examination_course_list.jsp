<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统考课程认定</title>
</head>
<body>
	<div class="page">
	    <c:choose>
		    <c:when test="${schoolCode eq '11846' }">
				<gh:resAuth parentCode="RES_TEACHING_RESULT_STATEXAM" pageType="list"></gh:resAuth>
			</c:when>
			<c:otherwise>
				<div class="panelBar">
					<ul class="toolBar">
						<li><a class="icon" href="javascript:void(0)"
							onclick="downloadExaminationModel();"><span>下载模板</span></a></li>
						<li><a class="icon" href="javascript:void(0)"
							onclick="uploadExaminationResults();"><span>导入成绩</span></a></li>
					</ul>
				</div>
			</c:otherwise>
	    </c:choose>
		<div class="pageContent">
			<table class="table" layouth="78" width="100%">
				<thead>
					<tr>
						<th width="10%">课程编号</th>
						<th width="20%">课程名称</th>
						<th width="20%">简称</th>
						<th width="10%">负责教师</th>
						<th width="10%">学分</th>
						<th width="10%">学时</th>
						<th width="10%">课程状态</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="examinationCourseListBody">
					<c:forEach items="${objPage.result}" var="course" varStatus="vs">
						<tr>
							<td>${course.courseCode }</td>
							<td>${course.courseName }</td>
							<td>${course.courseShortName}</td>
							<td></td>
							<td></td>
							<td></td>
							<td>${ghfn:dictCode2Val('CodeCourseState',course.status)}</td>
							<%-- <td><a href="javascript:void(0)" onclick="coursesDetermined('${course.resourceid }');" >课程认定</a></td> --%>
							<td><a
								ref="RES_TEACHING_RESULT_STATEXAM_COURSESDETERMINED_LIST"
								target="navTab"
								href="${baseUrl }/edu3/teaching/result/statexam/maintain-list.html?courseId=${course.resourceid }">课程认定</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/statexam/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	//下载模板
	function downloadExaminationModel(){
		window.location.href="${baseUrl }/edu3/teaching/result/statexam/download-file.html"
	}
	//导入成绩
	function uploadExaminationResults(){
		var url = "${baseUrl }/edu3/teaching/result/statexam/upload-file-view.html";
		$.pdialog.open(url,"RES_TEACHING_RESULT_STATEXAM_UPLOAD_VIEW","导入统考成绩",{width:600, height:400});
	}
	//课程认定
	function coursesDetermined(courseId){
		var url = "${baseUrl }/edu3/teaching/result/statexam/maintain-list.html?courseId="+courseId;
		navTab.openTab("RES_TEACHING_RESULT_STATEXAM_COURSESDETERMINED_LIST",url,"统考成绩认定");
	}
	// 学位外语新的处理方式
	function downloadDegreeCourseTemp(){
		var url = "${baseUrl }/edu3/teaching/result/statexam/downloadTemplate.html";
		downloadFileByIframe(url, "downloadDegreeCourseTemplate");
	}
	
	//导入学位外语成绩
	function importDegreeCourseResults(){
		var url = "${baseUrl }/edu3/teaching/result/statexam/importResults-view.html";
		$.pdialog.open(url,"RES_TEACHING_RESULT_STATEXAM_IMPORTRESULTS","导入学位外语成绩",{width:600, height:400});
	}
</script>
</body>
</html>