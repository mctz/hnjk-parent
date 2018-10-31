<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约教学计划外课程列表</title>


</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		var errorMsg = "${errorMsg}";
		if(null!=errorMsg && ""!=errorMsg){
			alertMsg.warn(errorMsg);
		}
	})
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="outplancourselist_form"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/outplancourse-list.html?studentId=${condition['studentId'] }"
				method="post">
				<input type="hidden" name="studentId"
					value="${condition['studentId']}" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程名：</label><input type="text" name="courseName"
							value="${condition['courseName']}" /></li>
						<li><label>学分大于：</label><input type="text" name="creditHour"
							value="${condition['creditHour']}" class="number" /></li>
						<li><label>考试形式：</label>
						<%-- <gh:select dictionaryCode="CodeExamMode" name="examMode" value="${condition['examMode']}" />--%>
							<select name="examType">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${condition['examType'] eq '1'}"> selected="selected" </c:if>>笔试</option>
								<option value="2"
									<c:if test="${condition['examType'] eq '2'}"> selected="selected" </c:if>>口试</option>
						</select></li>
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

			<table class="table" layouth="130" width="100%">
				<thead>
					<tr>
						<th width="8%" align="center">序号</th>
						<th width="26%">课程名称</th>
						<th width="14%">课程简介</th>
						<th width="16%" align="center">考试形式</th>
						<th width="8%" align="center">学时</th>
						<th width="8%" align="center">学分</th>
						<th width="20%" align="center">操作</th>
					</tr>
				</thead>

				<tbody id="outPlanCourseListBody">
					<c:forEach items="${objPage.result}" var="course" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td title="${course.courseName }">${course.courseName }</td>
							<td title="${course.chsIntroduction}">${course.chsIntroduction}</td>
							<td>${ghfn:dictCode2Val('CodeExamMode',course.examType) }</td>
							<td>${course.planoutStudyHour }</td>
							<td>${course.planoutCreditHour}</td>
							<td><a href="javaScript:void(0)"
								onclick="outPlanCourseOrder('${studentId}','${course.resourceid}','orderCourse')">预约学习</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/outplancourse-list.html?studentId=${condition['studentId'] }"
				targetType="dialog" pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	//预约计划外课程
	function outPlanCourseOrder(studeinId,courseId,orderType){
		var url = "${baseUrl}/edu3/teaching/courseorder/outplancourse.html";
		var studentId = "${studentId }"
		jQuery.post(url,{studentId:studeinId,orderType:orderType,courseId:courseId},function(returnData){	
		  	var operatingStatus =  returnData['operatingStatus'];
		  	if(operatingStatus==true){
		  		alertMsg.Info("预约成功!");
		  		$.pdialog.reload("${baseUrl}/edu3/teaching/courseorder/outplancourse-list.html?studentId="+studentId,$("#outplancourselist_form").serializeArray());
			}else{
				var msgList  = returnData['msg'];
				var msg 	 = "";
				for(i=0;i<msgList.length;i++){
					msg+=(i+1)+"、"+msgList[i]+'</br>';
				}
				alertMsg.warn(msg);
			}
		},"json");
	}
</script>
</body>
</html>