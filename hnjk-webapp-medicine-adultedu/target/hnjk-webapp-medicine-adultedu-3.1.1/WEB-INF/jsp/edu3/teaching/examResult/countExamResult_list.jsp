<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩统计</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="countExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/count-examresults-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel id="yearId"
								name="yearId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="yearName desc" value="${condition['yearId']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}" /></li>
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
			<table class="table" layouth="108" width="100%">
				<thead>
					<tr>
						<th width="30%">批次名称</th>
						<th width="10%">年度</th>
						<th width="6%">学期</th>
						<th width="15%">预约截止时间</th>
						<th width="9%">预约状态</th>
						<th width="20%">所属教学站</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody id="countExamResultsBody">
					<c:forEach items="${examSubPage.result}" var="examSub"
						varStatus="vs">
						<tr>
							<td><a href="javascript:void(0)"
								onclick="viewExamResultsCount('${examSub.resourceid }');"
								title="查看成绩概况">${examSub.batchName }</a></td>
							<td>${examSub.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',examSub.term)}</td>
							<td><fmt:formatDate value="${examSub.endTime }"
									pattern="yyyy年MM月dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeExamSubscribeState',examSub.examsubStatus)}
								<input id="${examSub.resourceid }" name="${examSub.resourceid }"
								value="${examSub.examsubStatus }" type="hidden" />
							</td>
							<td>${examSub.brSchool.unitName}</td>
							<td><a href="javascript:void(0)"
								onclick="viewResultsDistributionAll('${examSub.resourceid }')">查看成绩分布概况</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examSubPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/count-examresults-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function viewExamResultsCount(examSubId){
		var url = "${baseUrl}/edu3/teaching/result/count-examresults-view.html?examSubId="+examSubId;
		navTab.openTab('RES_TEACHING_RESULT_COUNT_LIST',url,'查看成绩概况');
	}
	function viewResultsDistributionAll(examSubId){
		var url = "${baseUrl}/edu3/teaching/result/examresults-distribution-all.html?examSubId="+examSubId;
		navTab.openTab('RES_TEACHING_RESULT_DISTRIBUTION_ALL',url,'成绩分布概况');
	}
</script>
</body>
</html>