<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程库管理</title>
<script type="text/javascript">
	//新增
	function addCourse(){
		navTab.openTab('navTab', '${baseUrl}/edu3/teaching/teachingcourse/edit.html', '新增课程');
	}
	
	//修改
	function modifyCourse(){
		var url = "${baseUrl}/edu3/teaching/teachingcourse/edit.html";
		if(isCheckOnlyone('resourceid','#tcourseBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#tcourseBody input[@name='resourceid']:checked").val(), '编辑课程');
		}			
	}
		
	//删除
	function deleteCourse(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachingcourse/delete.html","#tcourseBody");
	}
	//停用
	function disenabledCourse(){
		pageBarHandle("您确定要停用这些课程吗？","${baseUrl}/edu3/teaching/teachingcourse/enabled.html?type=disenable","#tcourseBody");
	}
	//启用
	function enabledCourse(){
		pageBarHandle("您确定要启用这些课程吗？","${baseUrl}/edu3/teaching/teachingcourse/enabled.html?type=enable","#tcourseBody");
	}	
	
	//添加教材
	function addOneCourseBook(){
		var url = "${baseUrl}/edu3/teaching/coursebook/input.html";
		if(isCheckOnlyone('resourceid','#tcourseBody')){
			var courseId = $("#tcourseBody input[@name='resourceid']:checked").val();			
			navTab.openTab('_blank', url+'?courseId='+courseId, '新增教材');
		}
	}
	//课程公告管理
	//function listCourseNotice(){
	//	var url = "${baseUrl}/edu3/teaching/coursenotice/list.html";
	//	if(isCheckOnlyone('resourceid','#tcourseBody')){
	//		navTab.openTab('courseNotice', url+'?courseId='+$("#tcourseBody input[@name='resoutceid']:checked").val(), '课程公告管理');
	//	}
	//}
	//导出excel
	function exportTeachingCourseToExcel(){
		//以免每次点击下载都创建一个iFrame，把上次创建的删除
		$('#frame_exportTeachingCourseToExcel').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportTeachingCourseToExcel";
		iframe.src = "${baseUrl }/edu3/teaching/teachingcourse/excel/export.html?act=default";
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
		/*
		var url="${baseUrl}/edu3/teaching/teachingcourse/excel/exportchoice.html?act=default";
		$.pdialog.open(url,'RES_TEACHING_COURSE_TOEXCEL','',{height:100, width:150,mask:true});
		*/
	}
	//复制课程及课件资源
	//function copyCourse(){	
	//	if(isCheckOnlyone('resourceid','#tcourseBody')){
	//		var url = "${baseUrl}/edu3/framework/teachingcourse/copy/select.html?fromCourseId="+$("#tcourseBody input[@name='resourceid']:checked").val();
	//		$.pdialog.open(url,'RES_TEACHING_COURSE_TOEXCEL_SELECT','选择待复制的课程',{height:200, width:350,mask:true});
			//var url = "${baseUrl}/edu3/teaching/teachingcourse/copy.html";
			//var res = $("#tcourseBody input[@name='resourceid']:checked").val();
			//alertMsg.confirm("您确定要复制这门课程的所有课件吗？", {
			//	okCall: function(){
			//		$.post(url,{resourceid:res}, navTabAjaxDone, "json");
			//	}
			//});	
	//	}
	//}
	//添加课程封面
	function addCover(){
		var url = "${baseUrl}/edu3/teaching/teachingcourse/addCover.html";
		if(isCheckOnlyone('resourceid','#tcourseBody')){
			navTab.openTab('navTab', url+'?courseId='+$("#tcourseBody input[@name='resourceid']:checked").val(), '上传课程封面');
		}			
	}
	
	// 计算视频总时长
	function calculateVideoTime() {
		var url = "${baseUrl}/edu3/teaching/materevise/calculateVideoTime.html";
		var courseIds = [];
		$("#tcourseBody input[@name='resourceid']:checked").each(function(){
			courseIds.push($(this).val());
    	});
		
		if(courseIds.length < 1){
    		alertMsg.warn("请选择一条要操作的记录！");	
			return false;
    	}
		
		$.ajax({
			type:'POST',
			url:url,
			data:{courseIds:courseIds.toString()},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data.statusCode == 200){
					alertMsg.correct(data.message);
			    }else{
			    	alertMsg.error(data.message);
				}
			}
		}); 
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachingcourse/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名称：</label><input type="text" name="courseName"
							value="${condition['courseName']}" /></li>
						<li><label>课程编码：</label><input type="text" name="courseCode"
							value="${condition['courseCode']}" /></li>
						<li><label>状态：</label>
						<gh:select name="status" style="width:120px;"
								value="${condition['status']}" dictionaryCode="CodeCourseState" />
						</li>
					</ul>
					<ul class="searchContent">

						<li><label>类型：</label> <select name="courseType2"
							style="width: 120px;">
								<option value="">请选择</option>
								<option value="unitExam"
									<c:if test="${condition['isUniteExam'] eq 'Y'}">selected</c:if>>统考课程</option>
								<option value="degreeUnitExam"
									<c:if test="${condition['isDegreeUnitExam'] eq 'Y'}">selected</c:if>>学位统考课程</option>
								<option value="practice"
									<c:if test="${condition['isPractice'] eq 'Y'}">selected</c:if>>实践课程</option>
						</select></li>
						<li><label>课程性质：</label> <gh:select name="courseType"
								style="width:120px;" value="${condition['courseType']}"
								dictionaryCode="CodeCourseType" size="2" /></li>
						<li><label>有无课件：</label> <gh:select name="hasResource"
								style="width:120px" value="${condition['hasResource']}"
								dictionaryCode="yesOrNo" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_COURSE" pageType="list"></gh:resAuth>
			<table class="table" layouth="158">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tcourse"
							onclick="checkboxAll('#check_all_tcourse','resourceid','#tcourseBody')" /></th>
						<th width="8%">课程编号</th>
						<th width="12%">课程名称</th>
						<th width="12%">简称</th>
						<th width="10%">课程性质</th>
						<th width="10%">考试形式</th>
						<th width="10%">计划外学分</th>
						<th width="10%">课程状态</th>
						<th width="5%">有无课件</th>
					</tr>
				</thead>
				<tbody id="tcourseBody">
					<c:forEach items="${courseListPage.result}" var="course"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${course.resourceid }" autocomplete="off" /></td>
							<td>${course.courseCode }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/teachingcourse/view.html?courseId=${course.resourceid }"
								target="dialog" mask="true" width="700" height="400"
								title="${course.courseName }" style="color: blue">${course.courseName }</a></td>
							<td>${course.courseShortName }</td>
							<td>${ghfn:dictCode2Val('CodeCourseType',course.courseType) }</td>
							<td>${ghfn:dictCode2Val('CodeCourseExamType',course.examType) }</td>
							<td
								<c:if test="${course.courseType eq '22' and course.planoutCreditHour eq 0}">style="color: red;"</c:if>
								<c:if test="${course.courseType eq '22' and course.planoutCreditHour ne 0}">style="color: green;"</c:if>>${course.planoutCreditHour }</td>
							<td>${ghfn:dictCode2Val('CodeCourseState',course.status ) }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',course.hasResource ) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseListPage}"
				goPageUrl="${baseUrl }/edu3/teaching/teachingcourse/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>