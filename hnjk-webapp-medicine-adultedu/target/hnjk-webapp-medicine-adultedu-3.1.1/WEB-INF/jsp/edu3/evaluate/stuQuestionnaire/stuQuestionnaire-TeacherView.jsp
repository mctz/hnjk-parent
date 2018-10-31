<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷列表</title>
<script type="text/javascript">
// 	$(function(){
// 		var qnListSize='${qnListSize}';
// 		if(qnListSize==0){
// 			$.pdialog.close("stuQuestionnaire");
// 		}
// 	})
	function showStuQuestionnaireList(questionnaireid){
		var url = "${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/stuList.html?questionnaireid="+questionnaireid;
		$.pdialog.open(url,'stuQuestionnaireList','问卷详情表',{width:1000,height:650,zIndex:9999});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
		<br>
		<span style="color:red"><h2>提示：点击有效问卷，可查看具体学生的问卷详情</h2></span>
		<br>
		<span><h2>
		教师：${list[0].cnname} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		账号：${list[0].username } &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		年度：${list[0].yearname} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		学期：${ ghfn:dictCode2Val('CodeTerm',list[0].term)}</h2></span>
		<br>
		
		</div>
		<div class="pageContent">
			<table class="table" layouth="10">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="10%">课程名称</th>
						<th width="15%">班级名称</th>
						<th width="5%">班级人数</th>
						<th width="5%">有效问卷</th>
						<th width="5%">问卷有效率</th>
						<th width="5%">得分</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="qn" varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td>${qn.coursename}</td>
							<td>${qn.classesname}</td>
							<td>${qn.stuCount}</td>
							<td><a href="##" onclick="showStuQuestionnaireList('${qn.qnid}')">${qn.validCount}</a></td>
							<td>${qn.validper}</td>
							<td>${qn.validavg}</td>
							
<%-- 							<td><a href="javaScript:void(0)" onclick="enterEvaluate('${qn.resourceid}','${studentInfoid }','${qn.course.courseName }')">进入评价</a></td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</div>
	</div>
</body>
</html>