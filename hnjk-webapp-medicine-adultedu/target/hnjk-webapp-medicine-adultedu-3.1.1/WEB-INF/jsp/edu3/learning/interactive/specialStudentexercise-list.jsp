<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><c:choose>
		<c:when test="${type eq 'isTypical' }">典型批改</c:when>
		<c:otherwise>优秀作业</c:otherwise>
	</c:choose></title>
<script type="text/javascript">	
	function viewStudentExercise(studentExerciseId,colName,studentName){
		var url = "${baseUrl}/edu3/learning/interactive/studentexercise/view.html?studentExerciseId="+studentExerciseId;
		$.pdialog.open(url, studentExerciseId, studentName+"·"+colName, {width: 800, height: 600});
	}	
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<table class="table" layouth="142">
				<thead>
					<tr>
						<th width="15%">学生号</th>
						<th width="15%">学生姓名</th>
						<th width="15%">作业名称</th>
						<th width="10%">作业类型</th>
						<th width="10%">成绩</th>
						<th width="25%">评语</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${studentExerciseList }" var="exercise"
						varStatus="vs">
						<tr>
							<td>${exercise.studentInfo.studyNo }</td>
							<td>${exercise.studentInfo.studentName }</td>
							<td>${exercise.exerciseBatch.colName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningColType',exercise.exerciseBatch.colType) }</td>
							<td>${exercise.result }</td>
							<td>${exercise.teacherAdvise }</td>
							<td><a href="javascript:;"
								onclick="viewStudentExercise('${exercise.resourceid}','${exercise.exerciseBatch.colName }','${exercise.studentInfo.studentName }');">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>