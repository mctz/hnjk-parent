<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习情况统计</title>
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
	 $(document).ready(function(){
		 learningInfoStatisticsBegin();
	 });

	 function learningInfoStatisticsBegin() {
		   var defaultValue = "${condition['branchSchool']}";
		   var schoolId = "${condition['unitId']}";
		   var gradeId = "${condition['gradeId']}";
		   var classicId = "${condition['classicId']}";
		   var teachingType = "${condition['schoolType']}";
		   var majorId = "${condition['majorId']}";
		   var classesId = "${condition['classesId']}";
		   var selectIdsJson = "{unitId:'learningInfoStatistics-brSchoolId',gradeId:'learningInfoStatistics-gradeId',classicId:'learningInfoStatistics-classicId',"
		 							  +"teachingType:'learningInfoStatistics-teachingType',majorId:'learningInfoStatistics-majorId',classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, classesId, selectIdsJson);
		}
		// 选择教学点
		function learningInfoStatisticsUnit() {
		   var defaultValue = $("#learningInfoStatistics-brSchoolId").val();
		   var selectIdsJson = "{gradeId:'learningInfoStatistics-gradeId',classicId:'learningInfoStatistics-classicId',"
				  					  +"teachingType:'learningInfoStatistics-teachingType',majorId:'learningInfoStatistics-majorId',classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
		}
		// 选择年级
		function learningInfoStatisticsGrade() {
		   var defaultValue = $("#learningInfoStatistics-brSchoolId").val();
		   var gradeId = $("#learningInfoStatistics-gradeId").val();
		   var selectIdsJson = "{classicId:'learningInfoStatistics-classicId',teachingType:'learningInfoStatistics-teachingType',majorId:'learningInfoStatistics-majorId',"
			   						  +"classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
		}
		// 选择层次
		function learningInfoStatisticsClassic() {
		   var defaultValue = $("#learningInfoStatistics-brSchoolId").val();
		   var gradeId = $("#learningInfoStatistics-gradeId").val();
		   var classicId = $("#learningInfoStatistics-classicId").val();
		   var selectIdsJson = "{teachingType:'learningInfoStatistics-teachingType',majorId:'learningInfoStatistics-majorId',classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
		}
		// 选择学习形式
		function learningInfoStatisticsTeachingType() {
		   var defaultValue = $("#learningInfoStatistics-brSchoolId").val();
		   var gradeId = $("#learningInfoStatistics-gradeId").val();
		   var classicId = $("#learningInfoStatistics-classicId").val();
		   var teachingTypeId = $("#learningInfoStatistics-teachingType").val();
		   var selectIdsJson = "{majorId:'learningInfoStatistics-majorId',classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("teachingType", defaultValue, "", gradeId, classicId, teachingTypeId, "", "", selectIdsJson);
		}
		//选择专业
		function learningInfoStatisticsMajor(){
		   var defaultValue = $("#learningInfoStatistics-brSchoolId").val();
		   var gradeId = $("#learningInfoStatistics-gradeId").val();
		   var classicId = $("#learningInfoStatistics-classicId").val();
		   var teachingTypeId = $("#learningInfoStatistics-teachingType").val();
		   var majorId = $("#learningInfoStatistics-majorId").val();
		   var selectIdsJson = "{classesId:'learningInfoStatistics-classesId'}";
		   cascadeQuery("classes", defaultValue, "", gradeId, classicId, teachingTypeId, majorId, "", selectIdsJson);
		}
	 
   // 导出学生随堂练习得分累积情况
    function exportLearningInfoStatistics() {
    	var resourceids = [];
		
		$("#learningInfoStatisticsBody input[@name='resourceid']:checked").each(function(){
			resourceids.push($(this).val());
		});
		var param = "resourceids="+resourceids.toString();
		
		if(resourceids.length<1){
			var brSchoolId = $("#learningInfoStatistics-brSchoolId").val();
			var gradeId = $("#learningInfoStatistics-gradeId").val();
			var classicId = $("#learningInfoStatistics-classicId").val();
			var teachingType = $("#learningInfoStatistics-teachingType").val();
			var majorId = $("#learningInfoStatistics-majorId").val();
			var classesId = $("#learningInfoStatistics-classesId").val();
			var courseId = $("#learningInfoStatistics_CourseId").val();
			var yearInfo = $("#learningInfoStatistics_yearInfo").val();
			var term = $("#learningInfoStatistics_term").val();
			var studyNo = $("#learningInfoStatistics_studyNo").val();
			var studentName = $("#learningInfoStatistics_studentName").val();
			param += "&branchSchool="+brSchoolId+"&gradeId="+gradeId+"&classicId="+classicId+"&schoolType="+teachingType
					   +"&majorId="+majorId+"&classesId="+classesId+"&courseId="+courseId+"&yearInfoId="+yearInfo+"&term="+term
					   +"&studyNo="+studyNo+"&studentName="+studentName;
		}
		
		var url = "${baseUrl}/edu3/studySituation/exportLearningInfoStatistics.html?"+param;
		downloadFileByIframe(url,'learningInfoStatistics_downloadIframe');
    }
	
    function checkOption(form){
    	//var courseId = $("#learningInfoStatistics_CourseId").val();
		var yearInfo = $("#learningInfoStatistics_yearInfo").val();
		var term = $("#learningInfoStatistics_term").val();
		
		if(yearInfo==null || yearInfo=="" ||term==null || term==""){
			alertMsg.warn("请输入完整的查询条件");
			return false;
		}else{
			return navTabSearch(form);
		}
    }
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return checkOption(this);"
				action="${baseUrl }/edu3/studySituation/onlineLearningInfo_statistics.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span
							sel-id="learningInfoStatistics-brSchoolId"
							sel-name="branchSchool"
							sel-onchange="learningInfoStatisticsUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span
							sel-id="learningInfoStatistics-gradeId" sel-name="gradeId"
							sel-onchange="learningInfoStatisticsGrade()"
							sel-style="width: 53%"></span></li>
						<li><label>层次：</label> <span
							sel-id="learningInfoStatistics-classicId" sel-name="classicId"
							sel-onchange="slearningInfoStatisticsClassic()"
							sel-style="width: 53%"></span></li>
						<li><label>办学模式：</label> <span
							sel-id="learningInfoStatistics-teachingType"
							sel-name="schoolType"
							sel-onchange="learningInfoStatisticsTeachingType()"
							sel-style="width: 100px;"></span></li>
					</ul>
					<ul class="searchContent">
						
						<li class="custom-li"><label>专业：</label> <span
							sel-id="learningInfoStatistics-majorId" sel-name="majorId"
							sel-onchange="learningInfoStatisticsMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>年度：</label> <gh:selectModel
								id="learningInfoStatistics_yearInfo" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc "
								style="width:53%" /> <span style="color: red;">*</span></li>
						<li><label>学期：</label>
						<gh:select id="learningInfoStatistics_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:53%" /> <span style="color: red;">*</span></li>
						<li><label style="width: 40px;">课程：</label><gh:courseAutocomplete name="courseId"
								value="${condition['courseId']}"
								id="learningInfoStatistics_CourseId" hasResource="Y"
								displayType="code" tabindex="1" style="width: 135px;"></gh:courseAutocomplete></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="learningInfoStatistics-classesId" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li><label>学号：</label><input
							id="learningInfoStatistics_studyNo" type="text" name="studyNo"
							value="${condition['studyNo'] }" style="width: 120px;" /></li>
						<li><label>姓名：</label><input
							id="learningInfoStatistics_studentName" type="text"
							name="studentName" value="${condition['studentName'] } "
							style="width: 120px;" /></li>
						
					</ul>
					<ul class="searchContent">
						
					</ul>
					<div class="subBar">
						<label style="width: 500px;font-size: small;color: green;">网上学习时间：${learningTime.startTime } 至 ${learningTime.endTime }</label>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_LEAR_ONLINELEARNING_STATISTICS"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_learningInfoStatistics"
							onclick="checkboxAll('#check_all_learningInfoStatistics','resourceid','#learningInfoStatisticsBody')" /></th>
						<th width="6%">年度</th>
						<th width="5%">学期</th>
						<th width="7%">课程</th>
						<th width="7%">学号</th>
						<th width="5%">姓名</th>
						<th width="10%">教学站</th>
						<th width="4%">年级</th>
						<th width="8%">专业</th>
						<th width="10%">班级</th>
						<th width="7%">练习提交/总数</th>
						<th width="7%">作业提交/已批改</th>
						<th width="7%">随堂问答</th>
						<th width="7%">练习正确率</th>
						<th width="7%">学习进度</th>
					</tr>
				</thead>
				<tbody id="learningInfoStatisticsBody">
					<c:forEach items="${learningInfoStatisticsList.result }"
						var="statistics">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${statistics.resourceid }" autocomplete="off" /></td>
							<td>${statistics.yearname }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',statistics.term) }</td>
							<td>${statistics.coursename }</td>
							<td>${statistics.studyno }</td>
							<td>${statistics.studentname }</td>
							<td>${statistics.unitname }</td>
							<td>${statistics.gradename }</td>
							<td>${statistics.majorname }</td>
							<td>${statistics.classesname }</td>
							<td>${statistics.submitTotalNum }</td>
							<td>${statistics.submitCheckedNum }</td>
							<td>${statistics.effectiveTotalNum }</td>
							<%-- <td style="line-height: 21px;vertical-align: middle;"><span style="margin-top: 5px;width: 50px;" class="active_optionbar" title="${statistics.effectiveProgress>100?100:statistics.effectiveProgress }"><span style="width: ${(statistics.effectiveProgress>100?100:statistics.effectiveProgress)/2 }px;"></span></span>&nbsp;<fmt:formatNumber pattern='###.##' value='${statistics.effectiveProgress>100?100:statistics.effectiveProgress }'/>%</td> --%>
							<td style="line-height: 21px; vertical-align: middle;"><span
								style="margin-top: 5px; width: 50px;" class="active_optionbar"
								title="${statistics.score }"><span
									style="width: ${statistics.score/2 }px;"></span></span>&nbsp;<fmt:formatNumber
									pattern='###.##' value='${statistics.score }' />%</td>
							<td style="line-height: 21px; vertical-align: middle;"><span
								style="margin-top: 5px; width: 50px;" class="active_optionbar"
								title="${statistics.stuprogress>100?100:statistics.stuprogress}"><span
									style="width: ${(statistics.stuprogress>100?100:statistics.stuprogress)/2 }px;"></span></span>&nbsp;<fmt:formatNumber
									pattern='###.##'
									value='${statistics.stuprogress>100?100:statistics.stuprogress }' />%</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${learningInfoStatisticsList}"
				goPageUrl="${baseUrl }/edu3/studySituation/onlineLearningInfo_statistics.html"
				condition="${condition}" pageType="sys" />
		</div>
	</div>
</body>
</html>