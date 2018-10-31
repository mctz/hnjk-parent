<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约情况统计</title>
</head>
<body>

	<script type="text/javascript">
	//生成
	function generatorTask123(){
			pageBarHandle("您确定要生成这些任务书吗？","${baseUrl}/edu3/teaching/courseorderstat/save.html","#courseOrderStatsBody");
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorderstat/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfoid" bindValue="resourceid"
								displayValue="yearName" style="width:130px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoid']}" orderBy="firstYear desc" />
						</li>
						<li><label>学期：</label>
						<gh:select name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" style="width:50%;" /></li>
						<li><label>课程：</label><input name="courseName"
							value="${condition['courseName'] }" style="width: 55%;" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>是否生成：</label>
						<gh:select name="generatorFlag"
								value="${condition['generatorFlag']}" dictionaryCode="yesOrNo"
								style="width:50%;" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_COURSESTAT"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_courseOrderStats"
							onclick="checkboxAll('#check_all_courseOrderStats','resourceid','#courseOrderStatsBody')" /></th>
						<th width="20%">年度</th>
						<th width="15%">学期</th>
						<th width="10%">课程编号</th>
						<th width="20%">课程</th>
						<th width="15%">预约人数</th>
						<th width="15%">生成状态</th>
					</tr>
				</thead>
				<tbody id="courseOrderStatsBody">
					<c:forEach items="${cosList.result}" var="cos" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${cos.resourceid }" autocomplete="off" /></td>
							<td>${cos.yearInfo }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',cos.term) }</td>
							<td>${cos.course.courseCode }</td>
							<td>${cos.course.courseName }</td>
							<td>${cos.orderNum }</td>
							<td><c:choose>
									<c:when test="${cos.generatorFlag eq 'Y'}">已生成</c:when>
									<c:otherwise>未生成</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${cosList}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorderstat/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>