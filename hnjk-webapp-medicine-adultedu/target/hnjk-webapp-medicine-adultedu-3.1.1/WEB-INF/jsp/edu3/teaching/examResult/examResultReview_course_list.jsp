<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>
<script type="text/javascript">
function validateForm(form){
	var examSubId = $("#examResultsReview_ExamSub").val();
	if(examSubId==null || examSubId==''){
		alertMsg.warn("请选择考试批次！");
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
			<form id="examResultsReviewSearchForm"
				onsubmit="return validateForm(this);"
				action="${baseUrl}/edu3/teaching/examresult/review-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="examResultsReview_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="isDeleted=0 and batchType='exam' and examType='N'"
								orderBy="examinputStartTime desc" /> <font color="red">*</font>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examResultsReview_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:55%" /></li>
						<li><label>考试编号：</label> <input
							id="examResultsReview_examCourseCode" name="examCourseCode"
							value="${condition['examCourseCode'] }" style="width: 55%" /></li>
					</ul>
					<div class="subBar">
						<ul>
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
			<table class="table" layouth="112" width="100%">
				<thead>
					<tr>
						<th width="25%"
							style="text-align: center; vertical-align: middle;">年度</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">考试编号</th>
						<th width="35%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">课程类型</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">复审人数</th>
						<th width="10%" style="text-align: center;">操作</th>
					</tr>
				</thead>
				<tbody id="examResultsReviewBody">
					<c:forEach items="${page.result}" var="examInfo" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">
								${examSub.yearInfo.yearName }${ghfn:dictCode2Val('CodeTermType',examSub.term)}
							</td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.examCourseCode}</td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.courseName}</td>
							<td style="text-align: center; vertical-align: middle;"><c:choose>
									<c:when test="${examInfo.examCourseType ==0 }">网络课程</c:when>
									<c:when test="${examInfo.examCourseType ==1 }">面授课程</c:when>
									<c:when test="${examInfo.examCourseType ==2 }">网络+面授课程</c:when>
									<c:when test="${examInfo.examCourseType ==3 }">期末机考</c:when>
								</c:choose></td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.orderNumber}</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="javaScript:void(0)"
								onclick="examResultsReviewList('${examInfo.examInfoResourceId}','${examInfo.courseName}')">复审</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/review-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">
	
	//成绩复审
	function examResultsReviewList(examInfoId,courseName){
		
		var examSubId 		  = "${condition['examSubId']}";
		var examSubStatus     ="${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许复审成绩！');
		}else{
			var url = "${baseUrl}/edu3/teaching/examresult/review-examresults-list.html?examSubId="+examSubId+"&examInfoId="+examInfoId;
			navTab.openTab("RES_TEACHING_RESULT_EXAMRESULTS_REVIEW_EXAMRESULTS_LIST",url,courseName+"成绩复审");	
		}
			
	}
	
</script>
</body>
</html>