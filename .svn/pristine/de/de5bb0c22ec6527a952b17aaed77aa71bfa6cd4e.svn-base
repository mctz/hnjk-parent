<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生打印准考证-列表</title>

</head>
<body>
	<script type="text/javascript">
	//下载准考证
	function examCardDownload(){
		var url ="${baseUrl}/edu3/teaching/exam/examCard/download.html";	
		var studentLearnPlanListSize = "${studentLearnPlanListSize }";
		var assignSeat               = "${assignSeat }";
		var errorMsg                 = "${errorMsg}";
		if(""!=errorMsg){
			alertMsg.warn(errorMsg);
			return false;
		}
		if(studentLearnPlanListSize==0){
			alertMsg.warn("当前考试批次中，您没有需要考试的课程！");
			return false;
		}else{
			if(assignSeat=="false"){
				alertMsg.confirm("你的考试课程未安排座位，确定要打印准考证？", {
					okCall: function(){//执行
						downloadFileByIframe(url,'studentExamCardExportIframe');
					}
				});
			}else{
				downloadFileByIframe(url,'studentExamCardExportIframe');
			}
		}
	}
</script>

	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<font color="red">${student.studentName}：${examSub.batchName }
						考试科目，请在考试时间前下载打印准考证！</font>
				</ul>
			</div>
		</div>
		<div class="pageContent">

			<gh:resAuth parentCode="RES_STUDENT_EXAMCARD_PRINT_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="30%">考试课程</th>
						<th width="10%">考试形式</th>
						<th width="15%">考试时间</th>
						<th width="10%">试室</th>
						<th width="10%">座位号</th>
					</tr>
				</thead>
				<tbody id="studentExamCardPrintListBody">
					<c:forEach items="${studentLearnPlanList }" var="learnPlan"
						varStatus="vs">
						<tr>
							<td>${ vs.index+1 }</td>
							<td>${learnPlan.studentInfo.studentName }</td>
							<td>${learnPlan.studentInfo.studyNo }</td>
							<td>${learnPlan.examInfo.course.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeCourseExamType',learnPlan.examInfo.course.examType)}</td>
							<td><fmt:formatDate
									value="${learnPlan.examInfo.examStartTime }"
									pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
									value="${learnPlan.examInfo.examEndTime }" pattern="HH:mm" /></td>
							<td>${learnPlan.examResults.examroom.examroomName }</td>
							<td>${learnPlan.examResults.examSeatNum }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
