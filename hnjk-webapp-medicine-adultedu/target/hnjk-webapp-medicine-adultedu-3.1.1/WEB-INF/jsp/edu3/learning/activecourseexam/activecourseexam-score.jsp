<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看学生随堂练习得分累积情况</title>
<style type="text/css">
.active_optionbar {
	float: left;
	border: 1px solid #183152;
	height: 8px;
	width: 70%;
}

.active_optionbar span {
	float: left;
	height: 8px;
	overflow: hidden;
	background-color: #183152;
	background-repeat: repeat-x;
	background-position: 0 100%;
}
</style>
<script type="text/javascript">
// $(document).ready(function(){
// 	$("select[class*=flexselect]").flexselect();
// });

   // 导出学生随堂练习得分累积情况
    function exportScoreInfo() {
    	var studentIds = [];
		
		$("#stuExamScoreBody input[@name='resourceid']:checked").each(function(){
			studentIds.push($(this).val());
		});
		
		var courseId = $("#stuExamScoreSearch #activecourseexam_state_CourseId").val();
		var yearInfoId = $("#stuExamScoreSearch #activecourseexam_score_yearInfo").val();
		var term = $("#stuExamScoreSearch #activecourseexam_score_term").val();
		var studentNo = $("#stuExamScoreSearch #activecourseexam_score_studyNo").val();
		var studentName = $("#stuExamScoreSearch #activecourseexam_score_studentName").val();
		var classesId = $("#stuExamScoreSearch #activecourseexam_score_classid").val();
		
		var param = "studentIds="+studentIds.toString()+"&courseId="+courseId+"&yearInfoId="+yearInfoId
						+"&term="+term+"&studyNo="+studentNo+"&studentName="+studentName+"&classesId="+classesId;
		var url = "${baseUrl}/edu3/metares/exercise/activeexercise/exportStuExamInfo.html?"+param;
		
		downloadFileByIframe(url,'stuExamScore_downloadIframe');
    }

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/score.html"
				method="post">
				<div class="searchBar" id="stuExamScoreSearch">
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> ${activecourseexamscore }</li>
						<li><label>年度：</label> <gh:selectModel
								id="activecourseexam_score_yearInfo" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc "
								style="width:53%" /></li>
						<li><label>学期：</label>
						<gh:select id="activecourseexam_score_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:53%" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="activecourseexam_score_classid"
								tabindex="1" displayType="code"
								defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"
								style="width: 240px;"></gh:classesAutocomplete></li>
						<li><label>学号：</label><input
							id="activecourseexam_score_studyNo" type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
						<li><label>姓名：</label><input
							id="activecourseexam_score_studentName" type="text"
							name="studentName" value="${condition['studentName'] } " "/></li>
						
					</ul>
					<div class="subBar">
						<ul>
							<li>
								<div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_ACTIVECOURSEEXAM_SCORE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_stuExamScore"
							onclick="checkboxAll('#check_all_stuExamScore','resourceid','#stuExamScoreBody')" /></th>
						<th width="10%">年度</th>
						<th width="5%">学期</th>
						<th width="15%">课程</th>
						<th width="18%">班级</th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="18%">提交/总题目数</th>
						<th width="15%">累积得分情况</th>
					</tr>
				</thead>
				<tbody id="stuExamScoreBody">
					<c:forEach items="${activecourseexamScoreList.result }" var="stat">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stat.studentid }" autocomplete="off" /></td>
							<td>${stat.yearname }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',stat.term) }</td>
							<td>${stat.coursename }</td>
							<td>${stat.classesname }</td>
							<td>${stat.studyno }</td>
							<td>${stat.studentname }</td>
							<td style="line-height: 21px; vertical-align: middle;"><span
								style="margin-top: 5px; width: 120px;" class="active_optionbar"
								title="${stat.submitAndTotalNum }"><span
									style="width: <fmt:formatNumber pattern='###.##%' value='${stat.submitAndTotalRate }'/>;"></span></span>&nbsp;${ stat.submitAndTotalNum}(<fmt:formatNumber
									pattern='###.##%' value='${stat.submitAndTotalRate }' />)</td>
							<td style="line-height: 21px; vertical-align: middle;"><span
								style="margin-top: 5px; width: 120px;" class="active_optionbar"
								title="<fmt:formatNumber pattern='###.#' value='${stat.correctper*100 }'/>"><span
									style="width: <fmt:formatNumber pattern='###.##%' value='${stat.correctper }'/>;"></span></span>&nbsp;<fmt:formatNumber
									pattern='###.##%' value='${stat.correctper }' /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${activecourseexamScoreList}"
				goPageUrl="${baseUrl }/edu3/metares/exercise/activeexercise/score.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>