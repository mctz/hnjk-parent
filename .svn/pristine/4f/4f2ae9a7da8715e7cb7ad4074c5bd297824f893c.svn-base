<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入学考试考试人数统计</title>
<script type="text/javascript">
function changeRecruitExamStatType(objV){
	$("#recruitExam_statDateLi").toggle(objV=='statDate');
	$("#recruitExam_recruitPlanIdLi").toggle(objV=='recruitPlanId');
}
//导出
function exportRecruitExamStat(){
	var recruitPlanId = $("#recruitExam_recruitPlanId").val();
	if(recruitPlanId==""){
		alertMsg.warn("请选择一个批次");
	} else {
		var url = "${baseUrl }/edu3/recruit/recruitexam/stat/export.html?recruitPlanId="+recruitPlanId;
		downloadFileByIframe(url,"recruitExamStatsIframe");
	}
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitexam/stat.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>统计类型：</label> <select name="statType"
							style="width: 50%;"
							onchange="changeRecruitExamStatType(this.value)">
								<option value="statDate">按日期</option>
								<option value="recruitPlanId"
									<c:if test="${condition['statType'] eq 'recruitPlanId' }">selected="selected"</c:if>>按批次</option>
						</select></li>
						<li id="recruitExam_statDateLi"
							<c:if test="${condition['statType'] ne 'statDate' }">style="display:none"</c:if>>
							<label>日期：</label> <input type="text" name="statDate"
							value="${condition['statDate']}"
							onFocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})" />
						</li>
						<li id="recruitExam_recruitPlanIdLi"
							<c:if test="${condition['statType'] ne 'recruitPlanId' }">style="display:none"</c:if>>
							<label>招生批次：</label> <gh:selectModel
								id="recruitExam_recruitPlanId" name="recruitPlanId"
								bindValue="resourceid" displayValue="recruitPlanname"
								orderBy="yearInfo.firstYear desc"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:50%;" isSubOptionText="Y"
								condition="isPublished='Y'"
								value="${condition['recruitPlanId']}" choose="Y" />
						</li>
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
			<c:choose>
				<c:when test="${condition['statType'] eq 'statDate' }">
					<table class="table" layouth="138">
						<thead>
							<tr>
								<th width="5%"></th>
								<th width="35%"><c:choose>
										<c:when test="${condition['statType'] eq 'statDate'}">考试场次</c:when>
										<c:otherwise>教学站</c:otherwise>
									</c:choose></th>
								<th width="15%">应考人数</th>
								<th width="15%">实考人数</th>
								<th width="15%">缺考人数</th>
								<th width="15%">只考一科人数</th>
							</tr>
						</thead>
						<tbody id="recruitExamStatBody">
							<c:forEach items="${statList}" var="stat" varStatus="vs">
								<tr>
									<td><c:if test="${not vs.last}">${vs.index+1 }</c:if></td>
									<td><c:choose>
											<c:when test="${vs.last}">总计：</c:when>
											<c:otherwise>${stat.statname }</c:otherwise>
										</c:choose></td>
									<td>${stat.allCount }</td>
									<td>${stat.realCount }</td>
									<td>${stat.absentCount }</td>
									<td>${stat.oneCount }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<gh:resAuth parentCode="RES_RECRUIT_RECRUITEXAM_STAT"
						pageType="list"></gh:resAuth>
					<c:if test="${not empty resultMap and fn:length(resultMap)>0 }">
						<div class="tabs">
							<div class="tabsHeader">
								<div class="tabsHeaderContent">
									<ul>
										<c:forEach items="${resultMap }" var="result">
											<li><a href="#"><span>${result.key }</span></a></li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<div class="tabsContent" style="height: 100%;">
								<c:forEach items="${resultMap }" var="result">
									<table class="table" layouth="138">
										<thead>
											<tr>
												<th width="5%"></th>
												<th width="35%"><c:choose>
														<c:when test="${condition['statType'] eq 'statDate'}">考试场次</c:when>
														<c:otherwise>教学站</c:otherwise>
													</c:choose></th>
												<th width="15%">应考人数</th>
												<th width="15%">实考人数</th>
												<th width="15%">缺考人数</th>
												<th width="15%">只考一科人数</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${result.value}" var="stat" varStatus="vs">
												<tr>
													<td><c:if test="${not vs.last}">${vs.index+1 }</c:if></td>
													<td><c:choose>
															<c:when test="${vs.last}">总计：</c:when>
															<c:otherwise>${stat.statname }</c:otherwise>
														</c:choose></td>
													<td>${stat.allCount }</td>
													<td>${stat.realCount }</td>
													<td>${stat.absentCount }</td>
													<td>${stat.oneCount }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:forEach>
							</div>
							<div class="tabsFooter">
								<div class="tabsFooterContent"></div>
							</div>
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>
