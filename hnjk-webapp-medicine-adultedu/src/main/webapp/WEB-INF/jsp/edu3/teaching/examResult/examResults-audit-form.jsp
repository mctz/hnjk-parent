<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩更正</title>
<script type="text/javascript">

<c:if test="${islastExam eq 'N'}">
$(document).ready(function(){
	alertMsg.warn("只允许修改最后一次的成绩");
	navTab.closeCurrentTab();
	return ;
});
</c:if>
<c:if test="${examSub.examType ne 'N' }">

$(document).ready(function(){
	 $("#changedExamAbnormity option[value='5']").remove();
});
</c:if>
var facestudyScorePer = "";
var facestudyScorePer2 = ""
$(document).ready(function(){
	//var examinfo = "${examResultAudit.examResults.examInfo}";
	var ismixture = "${examResultAudit.examResults.examInfo.ismixture}";
	var examCourseType = "${examResultAudit.examResults.examInfo.examCourseType}";
	facestudyScorePer = "${examResultAudit.examResults.examInfo.facestudyScorePer}";//面授-卷面
	facestudyScorePer2 = "${examResultAudit.examResults.examInfo.facestudyScorePer2}";//面授-平时
	if(examCourseType=='0'){//网络课程
		facestudyScorePer = examinfo.studyScorePer;
		facestudyScorePer2 = examinfo.netsidestudyScorePer;
	}
	if(ismixture=='Y'){//机考
		facestudyScorePer = examinfo.studyScorePer;
		facestudyScorePer2 = 100-facestudyScorePer;
	}
	$("#facestudyScorePer").html("卷面成绩：平时成绩 = "+facestudyScorePer+"："+facestudyScorePer2);
});
function calculateIntegratedScore(){
	var examinfo = "${examResultAudit.examResults.examInfo}";
	
	var ismixture = examinfo.ismixture;
	var changedWrittenScore = $("#changedWrittenScore").val();
	var changedUsuallyScore = $("#changedUsuallyScore").val();
	
	if(ismixture=='Y'){//机考
		//卷面成绩=机考+笔考
		var writtenMachineScore = $("#changedWrittenMachineScore");//机考成绩
		var writtenHandworkScore = $("#changedWrittenHandworkScore");//笔考成绩
		changedWrittenScore = writtenMachineScore + writtenHandworkScore;
	}
	if(facestudyScorePer=='' || facestudyScorePer2==''
		|| facestudyScorePer==undefined || facestudyScorePer2==undefined){
		alertMsg.warn("请先设置考试信息成绩比例！");
		return false;
	}
	if(changedWrittenScore=='' || changedUsuallyScore==''){
		alertMsg.warn("请先填写更正成绩！");
		return false;
	}
	var integratedScore = (facestudyScorePer*changedWrittenScore + facestudyScorePer2*changedUsuallyScore)/100;
	$("#changedIntegratedScore").val(integratedScore);
}
</script>
</head>
<body>
	<h2 class="contentTitle">编辑成绩</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/examresult/correct/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${examResultAudit.resourceid }" /> <input type="hidden"
					name="examResultsId"
					value="${examResultAudit.examResults.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">考试批次：</td>
							<td width="35%">
								${examResultAudit.examResults.examInfo.examSub.batchName }</td>
							<td width="15%">课程：</td>
							<td width="35%">
								${examResultAudit.examResults.course.courseName }</td>
						</tr>
						<tr>
							<td width="15%">学号：</td>
							<td width="35%">
								${examResultAudit.examResults.studentInfo.studyNo }</td>
							<td width="15%">姓名：</td>
							<td width="35%">
								${examResultAudit.examResults.studentInfo.studentName }</td>
						</tr>
						<tr><td colspan="4"><label id="facestudyScorePer" style="width: 200px;color: red;"></label></td></tr>
						<tr>
							<c:choose>
								<c:when
									test="${examResultAudit.examResults.examInfo.ismixture eq 'Y' }">
									<td>更正前机考成绩：</td>
									<td>${examResultAudit.examResults.writtenMachineScore }</td>
									<td>更正后机考成绩：</td>
									<td><input type="text" id="changedWrittenMachineScore" name="changedWrittenMachineScore"
										value="${examResultAudit.changedWrittenMachineScore }"
										class="number" range="[0,100]" /> </td>
								</c:when>
								<c:otherwise>
									<td>更正前卷面成绩：</td>
									<td>${examResultAudit.examResults.writtenScore }</td>
									<td>更正后卷面成绩：</td>
									<td><input type="text" id="changedWrittenScore" name="changedWrittenScore"
										value="${examResultAudit.changedWrittenScore }" class="number"
										range="[0,100]" /></td>
								</c:otherwise>
							</c:choose>
						</tr>
						<c:if
							test="${examResultAudit.examResults.examInfo.ismixture eq 'Y' }">
							<tr>
								<td>更正前笔考成绩：</td>
								<td>${examResultAudit.examResults.writtenHandworkScore }</td>
								<td>更正后笔考成绩：</td>
								<td><input type="text" id="changedWrittenHandworkScore" name="changedWrittenHandworkScore"
									value="${examResultAudit.changedWrittenHandworkScore }"
									class="number"
									range="[0,${examResultAudit.examResults.examInfo.mixtrueScorePer }]" />
								</td>
							</tr>
						</c:if>
						<tr>
							<td>更正前平时成绩：</td>
							<td>${examResultAudit.examResults.usuallyScore }</td>
							<td>更正后平时成绩：</td>
							<td><input type="text" id="changedUsuallyScore" name="changedUsuallyScore"
								value="${examResultAudit.changedUsuallyScore }" class="number"
								range="[0,100]"
								<c:if test="${examSub.examType ne 'N' }"> readonly="readonly"</c:if> />
								<c:if test="${examSub.examType ne 'N' }">
									<span style="color: red">更正补考成绩无需输入此分数</span>
								</c:if></td>
						</tr>
						<tr>
							<td>更正前综合成绩：</td>
							<td>${examResultAudit.examResults.integratedScore }</td>
							<td>更正后综合成绩：</td>
							<td><input type="text" id="changedIntegratedScore" name="changedIntegratedScore" value="${examResultAudit.changedIntegratedScore }"
								<c:if test="${schoolCode eq '10560' }">readonly="readonly"</c:if> class="number" range="[0,100]" />
								<span class="buttonActive" style="margin-left: 8px;">
									<div class="buttonContent"> <button type="button" onclick="calculateIntegratedScore()">计算综合成绩</button>
									</div>
								</span>
							</td>
						</tr>
						<tr>
							<td>更正前成绩状态：</td>
							<td>
								${ghfn:dictCode2Val('CodeExamAbnormity',examResultAudit.examResults.examAbnormity) }
							</td>
							<td>更正后成绩状态：</td>
							<td><gh:select name="changedExamAbnormity"
									value="${examResultAudit.changedExamAbnormity}"
									dictionaryCode="CodeExamAbnormity" choose="N"
									excludeValue="${noShowCodeStr }" /></td>
						</tr>
						<tr>
							<td>原因：</td>
							<td colspan="3"><textarea rows="3" name="memo" style="width: 50%;" 
								<c:if test="${schoolCode eq '10560' }">required="required"</c:if> >${examResultAudit.memo }</textarea></td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>