<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷列表</title>
<script type="text/javascript">
	$(function(){
		var qnListSize='${qnListSize}';
		if(typeof qnListSize !== 'undefined' && qnListSize==0){
			$.pdialog.close("stuQuestionnaire");
		}
	})
	function enterEvaluate(questionnaire,studentInfoid,courseName){
		$.pdialog.open('${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/list.html?questionnaireid='+questionnaire+"&studentInfoid="+studentInfoid,'stuQuestionBank',courseName+'问卷调查表',{mask:true,width:800,height:520,close:$.closeDialog});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
		<br>
		<span><h2>提示：为提高成人高等教育课堂教学质量，请同学们填写教学质量评价</h2></span>
		<br>	
		</div>
		<div class="pageContent">
			<table class="table" layouth="10">
				<thead>
					<tr>
						<th width="10%">序号</th>
						<th width="20%">课程名称</th>
						<th width="20%">任课老师</th>
						<th width="15%">年度</th>
						<th width="10%">学期</th>
						<th width="10%">进入评价</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${qnList}" var="qn" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${qn.course.courseName }</td>
							<td>${qn.teacher.cnName}</td>
							<td>${qn.questionnaireBatch.yearInfo.yearName}</td>
							<td>${ghfn:dictCode2Val('CodeTerm',qn.questionnaireBatch.term) }</td>
							<td><a href="javaScript:void(0)" onclick="enterEvaluate('${qn.resourceid}','${studentInfoid }','${qn.course.courseName }')">进入评价</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</div>
	</div>
</body>
</html>