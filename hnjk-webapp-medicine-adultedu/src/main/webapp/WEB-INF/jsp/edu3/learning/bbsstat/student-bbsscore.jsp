<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生发帖统计与成绩登记</title>
<script type="text/javascript">
	//保存网络辅导成绩
	function saveBbsResults(){
		$("#bbsResults_save_form input[name^='bbsResults']").removeClass("error");
		var checkedArr = $("#bbsScoreBody input[@name='resourceid']:checked");
		var $form = $("#bbsResults_save_form");
		if(checkedArr.size()==0){
 			alertMsg.warn('请选择一条要保存记录！');
			return false;
	 	}
		var isValidScore = true;
		checkedArr.each(function(){
			var scoreObj = $form.find("input[name='bbsResults"+this.value+"']");
			var score = $.trim(scoreObj.val());
			if(score == "" || !score.isNumber() || score < 0 || score > 100){
				isValidScore = false;
				scoreObj.addClass("error");
				return false;
			}
        });
		if(!isValidScore){
			alertMsg.warn('请输入0-100的分数值！');
			return false;
		} else {
			alertMsg.confirm("您确定要保存这些记录吗？", {
				okCall: function(){//执行			
					$.ajax({
						type:'POST',
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						success: function (json){
							DWZ.ajaxDone(json);
							if (json.statusCode == 200){
								var pageNum = "${bbsScoreList.pageNum}" || "1";
								var pageSize = "${bbsScoreList.pageSize}" || "20";
								navTabPageBreak({pageNum:pageNum,pageSize:pageSize});
							}
						},						
						error: DWZ.ajaxError
					});	
				}
			});
		}						
	}
	//导出
	function exportBbsResults(){
		var yearInfoId = $("#studentBbsScore_yearInfoId").val();
		var term = $("#studentBbsScore_term").val();
		var courseId = $("#studentBbsScore_CourseId").val();
		if(yearInfoId=="" || term=="" || courseId==""){
			alertMsg.warn("请选择年度、学期、课程等条件进行导出");	
		}		
		var url = "${baseUrl}/edu3/learning/bbs/student/score/export.html?"+$("#bbsScoreSearchForm").serialize();
		downloadFileByIframe(url,"bbsResultsExportIframe");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="bbsScoreSearchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/bbs/student/score.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel
								id="studentBbsScore_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" style="width:120px;"
								orderBy="firstYear desc " /> <span style="color: red;">*</span>
						</li>
						<li><label>学期：</label> <gh:select id="studentBbsScore_term"
								name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" style="width:100px;" /> <span
							style="color: red;">*</span></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								id="studentBbsScore_CourseId" name="courseId" tabindex="1"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y" classCss="required" style="width:120px;" />
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="unitId" tabindex="1" id="studentBbsScore_as_unitId"
								displayType="code" defaultValue="${condition['unitId']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo'] }" /></li>
						<li><label>姓名：</label><input type="text" name="studentName"
							value="${condition['studentName'] }" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：请选择年度、学期、课程进行查询</span></li>
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
			<gh:resAuth parentCode="RES_METARES_BBS_STUDENT_STAT_SCORE"
				pageType="list"></gh:resAuth>
			<form action="${baseUrl }/edu3/learning/bbs/student/score/save.html"
				method="post" id="bbsResults_save_form">
				<table class="table" layouth="161">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_bbsScore"
								onclick="checkboxAll('#check_all_bbsScore','resourceid','#bbsScoreBody')" /></th>
							<th width="8%">年度</th>
							<th width="5%">学期</th>
							<th width="8%">课程</th>
							<th width="10%">教学站</th>
							<th width="8%">专业</th>
							<th width="6%">层次</th>
							<th width="10%">学号</th>
							<th width="6%">姓名</th>
							<th width="6%">主题数</th>
							<th width="6%">回复数</th>
							<th width="6%">总发帖数</th>
							<th width="6%">优秀帖数</th>
							<th width="10%">成绩登记</th>
						</tr>
					</thead>
					<tbody id="bbsScoreBody">
						<c:forEach items="${bbsScoreList.result }" var="stat">
							<tr>
								<td><c:if test="${stat.status ne '1' }">
										<input type="checkbox" name="resourceid"
											value="${stat.resourceid }" autocomplete="off" />
									</c:if></td>
								<td title="${stat.yearName }">${stat.yearName }</td>
								<td title="${ghfn:dictCode2Val('CodeTerm',stat.term) }">${ghfn:dictCode2Val('CodeTerm',stat.term) }</td>
								<td title="${stat.courseName }">${stat.courseName }</td>
								<td title="${stat.unitName }">${stat.unitName }</td>
								<td title="${stat.majorname }">${stat.majorname }</td>
								<td title="${stat.classicname }">${stat.classicname }</td>
								<td title="${stat.studyNo }">${stat.studyNo }</td>
								<td>${stat.studentName }</td>
								<td>${stat.topic }</td>
								<td>${stat.reply }</td>
								<td>${stat.topic + stat.reply }</td>
								<td>${stat.isbest }</td>
								<td><c:choose>
										<c:when test="${stat.status eq '1' }">${stat.bbsresults }</c:when>
										<c:otherwise>
											<input type="text" name="bbsResults${stat.resourceid }"
												value="${stat.bbsresults }" style="width: 80%;" />
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${bbsScoreList}"
				goPageUrl="${baseUrl}/edu3/learning/bbs/student/score.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>