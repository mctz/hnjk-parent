<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>座位安排情况汇总</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examSeatAssignedInfoSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/seat/examSeatAssignInfo.html"
				method="post">
				<input type="hidden" id="_examSeatAssignedInfo_chooseExamSubId"
					name="examSubId" value="${condition['examSubId']}" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isBrschool'] eq 'N'}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="examSeatAssignedInfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>考试时间：</label> <select
							id="examSeatAssignedInfo_examTimeSegment" name="examTimeSegment"
							style="width: 55%">
								<option value="">::::::请选择::::::</option>
								<c:forEach items="${timeSegmentList }" var="segment">
									<c:set var="tempSegment"
										value="${segment.STARTTIME }TO${segment.ENDTIME }"></c:set>
									<option value="${segment.STARTTIME }TO${segment.ENDTIME }"
										<c:if test="${tempSegment eq  condition['examTimeSegment']}"> selected="selected"</c:if>>${segment.STARTTIME }至${segment.SHORTENDTIME }</option>
								</c:forEach>
						</select><font color="red">*</font></li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" width="100%" layouth="138">
				<thead>
					<tr>
						<c:choose>
							<c:when test="${condition['isBrschool'] eq 'N'}">

								<th width="10%">序号</th>
								<th width="30%">考点</th>
								<th width="15%">预约人数</th>
								<th width="15%">已安排人数</th>
								<th width="15%">未安排人数</th>
								<th width="15%">剩下座位数</th>

							</c:when>
							<c:when test="${condition['isBrschool'] eq 'Y'}">
								<th width="10%">序号</th>
								<th width="20%">时间</th>
								<th width="30%">课程</th>
								<th width="10%">预约人数</th>
								<th width="10%">已安排人数</th>
								<th width="10%">未安排人数</th>
								<th width="10%">剩下座位数</th>

							</c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody id="examSeatAssignedInfoListBody">
					<c:choose>
						<c:when test="${condition['isBrschool'] eq 'N'}">
							<c:forEach items="${page.result }" var="map" varStatus="vs">
								<tr>
									<td>${vs.index+1 }</td>
									<td>${map.UNITNAME }</td>
									<td><font color="blue"> ${map.ORDERNUM }</font></td>
									<td><font color="green">${map.ASSIGNED }</font></td>
									<td><font color="red">${map.ORDERNUM - map.ASSIGNED }</font></td>
									<td>${map.TOTALSEAT-map.ASSIGNED }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:when test="${condition['isBrschool'] eq 'Y'}">
							<c:forEach items="${page.result }" var="map" varStatus="vs">
								<tr>
									<td>${vs.index+1 }</td>
									<td><fmt:formatDate value="${map.EXAMSTARTTIME }"
											pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
											value="${map.EXAMENDTIME }" pattern="HH:mm" /></td>
									<td>${map.COURSENAME }</td>
									<td><font color="blue">${map.ORDERNUM }</font></td>
									<td><font color="green">${map.ASSIGNED }</font></td>
									<td><font color="red">${map.ORDERNUM - map.ASSIGNED }</font></td>
									<td>${map.TOTALSEAT - map.TOTALASSIGNED }</td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/seat/examSeatAssignInfo.html?examSubId=${condition['examSubId']}&examTimeSegment=${ condition['examTimeSegment'] }&branchSchool=${condition['branchSchool']}"
				pageType="sys" pageNumShown="5" targetType="dialog"
				condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">


</script>
</body>
</html>