<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看已发布成绩</title>
</head>
<body>
	<script type="text/javascript">

	//查看成绩
	function showExamResultsList(examInfoId,courseName){
		var url = "${baseUrl}/edu3/teaching/result/view-published-examresultlist.html?examInfoId="+examInfoId;
		navTab.openTab("RES_TEACHING_RESULT_PUBLISH_RESULT_LIST",url,courseName+"成绩查看");	
	}
	//打印、导出总评成绩单
	function printAndExportExamResultsReview(examInfoId,courseName,opreatingType){
		var examSubStatus     ="${examSub.examsubStatus}";
		var url               = "${baseUrl}/edu3/teaching/result/examresultsreview-print-view.html?examResultsStatus=4&examSubId=${examSub.resourceid}&examInfoId="+examInfoId;
		
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许打印、导出总评成绩！');
			return false;
		}else{
			if("exportAll"==opreatingType){
				url    =  "${baseUrl}/edu3/teaching/result/examresultsreview-print.html?examResultsStatus=4&operatingType="+opreatingType+"&examSubId=${examSub.resourceid}&examInfoId="+examInfoId;
				alertMsg.confirm("确定导出 《"+courseName+"》的总评成绩单？", {
					okCall: function(){
						downloadFileByIframe(url,'exportCourseExamResultsReviewForDownloadExportIframe');
					}
				});
			}else{
				$.pdialog.open(url+"&printType="+opreatingType,"RES_TEACHING_RESULT_REVIEW_FOR_BRSCHOOL","打印 "+courseName+"总评成绩",{width:800, height:600});	
			}
			
		}
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="publishedExamResultsCourseSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/view-examresultlist.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考试批次：</label> <gh:selectModel
								id="publishedExamResultsCourseSearchForm_ExamSub"
								name="examSubId" bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}" condition="batchType='exam'"
								orderBy="examinputStartTime desc" /> <font color="red">*</font>
						</li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="publishedExamResultsCourseSearchForm_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:55%" /></li>
						<li><label>考试编号：</label> <input
							id="publishedExamResultsCourseSearchForm_examCourseCode"
							name="examCourseCode" value="${condition['examCourseCode'] }"
							style="width: 55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>教学方式：</label> <gh:select
								name="examInfoCourseType"
								dictionaryCode="CodeExamInfoCourseType" choose="Y"
								value="${condition['examInfoCourseType']}" style="width:55%" />
						</li>
						<li><label>考试方式：</label> <select name="isMachineexam"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isMachineexam'] eq 'Y'}">selected="selected"</c:if>>机考</option>
								<option value="N"
									<c:if test="${condition['isMachineexam'] eq 'N'}">selected="selected"</c:if>>非机考</option>
						</select></li>
						<li><span class="tips">点击课程名查看具体成绩</span></li>
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
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="8%" style="text-align: center; vertical-align: middle;">考试编号</th>
						<th width="44%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">教学方式</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">考试方式</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">预约人数</th>
						<%--  <th width="20%"   style="text-align: center;vertical-align: middle;">成绩状态</th>--%>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">操作</th>
					</tr>
				</thead>
				<tbody id="netWorkStudyExamResultsBody">
					<c:forEach items="${page.result}" var="examInfo" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">${examInfo.examCourseCode}</td>
							<td style="text-align: center; vertical-align: middle;"><a
								href="#"
								onclick="showExamResultsList('${examInfo.examInfoResourceId}','${examInfo.courseName}')">${examInfo.courseName}</a>
							</td>
							<td style="text-align: center; vertical-align: middle;">
								${ghfn:dictCode2Val('CodeExamInfoCourseType',examInfo.examCourseType)}
							</td>
							<td style="text-align: center; vertical-align: middle;"><c:choose>
									<c:when test="${examInfo.isMachineexam eq 'Y' }">机考</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examCourseType)}</c:otherwise>
								</c:choose></td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.orderNumber}</td>
							<%--   <td  style="text-align: center;vertical-align: middle;">
			          		${statMap[examInfo.examInfoResourceId] }    
			            </td>
			           --%>
							<td style="text-align: center; vertical-align: middle;"><a
								href="#"
								onclick="printAndExportExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','printAll')">打印</a>
								<a href="#"
								onclick="printAndExportExamResultsReview('${examInfo.examInfoResourceId}','${examInfo.courseName}','exportAll')">导出</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/view-examresultlist.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
</body>
</html>