<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习分布以及完成情况</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/exercise/practice-list.html"
				method="post">
				<c:if test="${empty flag }">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>课程：</label> <gh:courseAutocomplete
									id="activecourseexam_state_CourseId" name="courseId"
									tabindex="1" value="${condition['courseId']}"
									isFilterTeacher="Y" displayType="code" classCss="required" /></li>
							<li><span class="tips"> 请选择您需要查询的课程 </span></li>
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
				</c:if>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="20%">课程</th>
						<th width="30%">知识节点</th>
						<th width="12%">随堂练习数目</th>
						<th width="12%">已经提交随堂练习题目</th>
						<th width="12%">已答对随堂练习题目</th>
						<th width="14%">随堂练习状态</th>
					</tr>
				</thead>
				<tbody>
					<c:set value="0" var="examcounts" />
					<c:forEach items="${activecourseexamDistributeList }" var="stat">
						<c:set value="${examcounts + stat.counts }" var="examcounts" />
						<tr>
							<td>${stat.coursename }</td>
							<td><span style="width: ${stat.nodelevel*20 }px;float:left;">&nbsp;</span>${stat.nodename }</td>
							<td>${stat.counts }</td>
							<td>${stat.mistakecount + stat.correctcount }</td>
							<td>${stat.correctcount }</td>
							<c:choose>
								<c:when
									test="${(stat.mistakecount + stat.correctcount) eq 0 and stat.counts ne 0 }">
									<td style="color: red">未完成</td>
								</c:when>
								<c:when
									test="${(stat.mistakecount + stat.correctcount) lt stat.counts}">
									<td style="color: blue">部分完成</td>
								</c:when>
								<c:otherwise>
									<td style="color: green">完成</td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
					<tr>
						<td>总计</td>
						<td></td>
						<td>${countsAll }</td>
						<td>${submitAll }</td>
						<td>${correctAll }</td>
						<td></td>
					</tr>
					<tr>
						<td>得分</td>
						<td align="center" colspan="5">${originalCourseExamResults }分</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	</script>
</body>
</html>
<script type="text/javascript">
<c:if test="${sessionScope.message ne null}">
	alert("${sessionScope.message}");
	<%request.getSession().removeAttribute("message");%>
</c:if>
</script>