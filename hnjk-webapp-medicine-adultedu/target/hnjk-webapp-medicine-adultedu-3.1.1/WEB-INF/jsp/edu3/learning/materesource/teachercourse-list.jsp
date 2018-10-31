<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程资源管理</title>
<script type="text/javascript">
	//课程素材资源添加编辑入口
	function listCourseware(){
		var courseId = $("#metare_courseBody input[@name='resoutceid']:checked").val();	
		var checkurl = "${baseUrl}/edu3/framework/course/syllabus/ajax.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			jQuery.post(checkurl,{courseId:courseId},function(isEmptyTree){
				 if(isEmptyTree){	
				 	alertMsg.warn("你还没建立该门课程的知识结构树！");
				 } else {
				 	var url = "${baseUrl}/edu3/metares/courseware/list.html";
					navTab.openTab('RES_METARES_COURSEWARE_MANAGE_LIST', url+'?courseId='+courseId, '课程素材资源管理');
				 }
			},"json");	
		}	
	}	
	//建立知识结构树
	function addSyllabus(){
		var url = "${baseUrl}/edu3/teaching/teachingcourse/addsyllabus.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('syllabusList', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '建立知识结构树');
		}		
	}
	
	//课程文件列表
	function listTeacherFiles(){
		var url = "${baseUrl}/edu3/metares/teacherfiles/list.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('teacherFilesList', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '课程文件管理');
		}
	}
	
	//课程公告管理
	//function listCourseNotice(){
	//	var url = "${baseUrl}/edu3/teaching/coursenotice/list.html";
	//	if(isCheckOnlyone('resourceid','#metare_courseBody')){
	//		navTab.openTab('courseNotice', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '课程公告管理');
	//	}
	//}
	//课程概况管理
	function listCourseOverview(){
		var url = "${baseUrl}/edu3/teaching/courseoverview/list.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('courseOverview', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '课程概况管理');
		}
	}
	//课程模拟试题
	function listCourseMockTest(){
		var url = "${baseUrl}/edu3/learning/coursemocktest/list.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('courseMockTest', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '模拟试题管理');
		}
	}
	//课程特色栏目
	function listCourseReference(){
		var url = "${baseUrl}/edu3/learning/coursereference/list.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('courseReference', url+'?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '特色栏目');
		}
	}
	//复制课程及课件资源
	function copyCourse(){	
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			var url = "${baseUrl}/edu3/framework/teachingcourse/copy/select.html?fromCourseId="+$("#metare_courseBody input[@name='resourceid']:checked").val();
			$.pdialog.open(url,'RES_TEACHING_COURSE_TOEXCEL_SELECT','选择目标课程',{height:200, width:500,mask:true});
		}
	}
	//教师复习总结录像
	function listMateRevise(){
		var url = "${baseUrl}/edu3/learning/materevise/list.html";
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			navTab.openTab('RES_LEARNING_MATE_REVISE', url+'?reviseCourseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '教师复习总结录像');
		}
	}
	//精品课程栏目
	function listCourseChannel(){
		if(isCheckOnlyone('resourceid','#metare_courseBody')){
			if($("#metare_courseBody input[@name='resoutceid']:checked").attr('rel')!='Y'){
				alertMsg.warn("请选择一门精品课程！");
				return false;
			}
			navTab.openTab('RES_METARES_COURSECHANNEL', '${baseUrl }/edu3/resources/coursechannel/list.html?courseId='+$("#metare_courseBody input[@name='resoutceid']:checked").val(), '精品课程栏目');
		}		
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/courseware/listCourse.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名称：</label><input type="text" name="courseName"
							value="${condition['courseName']}" /></li>
						<li><label>课程编码：</label><input type="text" name="courseCode"
							value="${condition['courseCode']}" /></li>
						<li><label>是否有课件：</label> <gh:select dictionaryCode="yesOrNo"
								id="materesource_hasResource" name="hasResource"
								value="${condition['hasResource']}" style="width:52%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>是否精品课程：</label> <gh:select
								dictionaryCode="yesOrNo" id="materesource_isQualityResource"
								name="isQualityResource"
								value="${condition['isQualityResource']}" style="width:52%;" />
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
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tg1"
							onclick="checkboxAll('#check_all_tg1','resourceid','#metare_courseBody')" /></th>
						<th width="15%">课程编号</th>
						<th width="25%">课程名称</th>
						<th width="10%">考试形式</th>
						<th width="10%">课程状态</th>
						<th width="10%">是否有课件资源</th>
						<th width="10%">是否精品课程</th>
						<th width="15%">课件预览</th>
					</tr>
				</thead>
				<tbody id="metare_courseBody">
					<c:forEach items="${courseListPage.result}" var="course"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${course.resourceid }" rel="${course.isQualityResource }"
								autocomplete="off" /></td>
							<td>${course.courseCode }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/teachingcourse/view.html?courseId=${course.resourceid }"
								target="dialog" mask="true" width="700" height="400"
								title="${course.courseName }" style="color: blue">${course.courseName }</a></td>
							<td>${ghfn:dictCode2Val('CodeExamMode',course.examType) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseState',course.status) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',course.hasResource) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',course.isQualityResource) }</td>
							<td style="text-align: center;"><a href="javascript:;"
								onclick="goInteractive('${course.resourceid }','${course.isQualityResource }')">课件预览</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseListPage}"
				goPageUrl="${baseUrl }/edu3/metares/courseware/listCourse.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function goInteractive(courseId,isQualityResource){
		if(isQualityResource=="Y"){
			window.open("${baseUrl }/resource/course/index.html?courseId="+courseId,"interactive");
		} else {
			window.open("${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId,"interactive");	
		}		
	}
</script>
</body>
</html>