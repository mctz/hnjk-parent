<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次课程</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<input name="examSubId" type="hidden" value="${examSubId }" />
			<table class="table" layouth="25">
				<thead>
					<tr>
						<th width="4%">编号</th>
						<th width="23%">课程名称</th>
						<th width="15%">考试时间</th>
						<th width="6%">考试形式</th>
						<th width="6%">预约考生</th>
						<th width="6%">实考考生</th>
						<th width="6%">负责老师</th>
						<th width="8%">联系电话</th>
						<th width="20%">成绩状态</th>
						<th width="6%">备注</th>
					</tr>
				</thead>
				<tbody id="countExamResultsExamInfoBody">

					<c:forEach items="${list}" var="examInfoVo" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td><a href="javascript:void(0)"
								onclick="viewCourseResultsInfo('${examInfoVo.examInfoResourceId }','${examSubId }')"
								title="${examInfoVo.courseName }">${examInfoVo.courseName }</a></td>
							<td><fmt:formatDate value="${examInfoVo.examStartDate }"
									pattern="yyyy-MM-dd HH:mm" /> -<fmt:formatDate
									value="${examInfoVo.examEndDate }" pattern="HH:mm" /></td>
							<td>${examInfoVo.examType }</td>
							<td>${examInfoVo.orderNumber }</td>
							<td>${examInfoVo.examNumber }</td>
							<td>${examInfoVo.teacherName }</td>
							<td>${examInfoVo.teacherPhone }</td>
							<td>${examInfoVo.checkStatus }</td>
							<td>${examInfoVo.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<%--<gh:page page="${page}" targetType="dialog" goPageUrl="${baseUrl }/edu3/teaching/result/downloadtranscripts-examinfo-list.html" pageType="sys" condition="${condition}"/>		
	--%>
		</div>
	</div>
	<script type="text/javascript">
$(document).ready(function(){
	var msg = "${msg}";
	if(""!=msg){
		alertMsg.warn(msg);
	}
});
function viewCourseResultsInfo(examInfoId,examSubId){
	var url = "${baseUrl}/edu3/teaching/result/course-examresults-view.html?examSub="+examSubId+"&examInfoId="+examInfoId;
	$.pdialog.open(url,"RES_TEACHING_RESULT_COURSE_EXAMRESULTS","单科成绩概况",{width:900, height:600});	
}
</script>
</body>
</html>