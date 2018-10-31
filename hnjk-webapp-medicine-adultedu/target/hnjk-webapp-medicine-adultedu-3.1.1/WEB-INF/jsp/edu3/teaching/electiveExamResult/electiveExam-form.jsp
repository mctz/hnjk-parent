<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>选修课成绩管理</title>
	<script type="text/javascript">
        function calculateIntegratedScore(){
            var examInfo = "${examInfo}";
			if(examInfo == null || examInfo.facestudyScorePer == null || examInfo.facestudyScorePer2 == null){
			    alertMsg.warn("请先设置考试信息成绩比例！");
			    return false;
            }
            var writtenScore = $("#writtenScore").val();
            var usuallyScore = $("#usuallyScore").val();

            if(writtenScore=='' || usuallyScore==''){
                alertMsg.warn("请先填写卷面成绩和平时成绩！");
                return false;
            }
            var integratedScore = (examInfo.facestudyScorePer*writtenScore + examInfo.facestudyScorePer2*changedUsuallyScore)/100;
            $("#changedIntegratedScore").val(integratedScore);
        }
	</script>
</head>
<body>
<h2 class="contentTitle">${(empty examResults.resourceid)?'新增':'编辑' }选修课成绩</h2>
<div class="page">
	<div class="pageContent">
		<form method="post" action="${baseUrl}/edu3/teaching/electiveExamResult/saveExamResult.html" class="pageForm" onsubmit="return validateCallback(this);">
			<input type="hidden" name="resourceid" value="${examResults.resourceid }"/>
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td width="20%">学号：</td>
						<td width="30%">${examResults.studentInfo.studyNo}</td>
						<td width="20%">姓名：</td>
						<td width="30%">${examResults.studentInfo.studentName}</td>
					</tr>
					<tr>
						<td>考试批次：</td>
						<td>${examResults.examSub.batchName}</td>
						<td>课程：</td>
						<td>${examResults.course.courseName}</td>
					</tr>
					<tr>
						<td>卷面成绩：</td>
						<td><input type="text" id="writtenScore" name="writtenScore"
								   value="${examResults.writtenScore }" class="number"
								   range="[0,100]" /></td>
						<td>平时成绩：</td>
						<td><input type="text" id="usuallyScore" name="usuallyScore"
								   value="${examResults.usuallyScore }" class="number"
								   range="[0,100]" /></td>
					</tr>
					<tr>
						<td>总评成绩：</td>
						<td><input type="text" id="integratedScore" name="integratedScore" value="${examResults.integratedScore }"
								  class="number" range="[0,100]" />
							<span class="buttonActive" style="margin-left: 8px;">
								<div class="buttonContent"> <button type="button" onclick="calculateIntegratedScore()">计算综合成绩</button></div>
							</span>
						</td>
						<td>考试异常类型：</td>
						<td><gh:select name="examAbnormity" value="${examResults.examAbnormity}" dictionaryCode="CodeExamAbnormity" choose="N" excludeValue="${noShowCodeStr }" /></td>
					</tr>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent">
						<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
				</ul>
			</div>
		</form>
	</div>
</div>
</body>
</html>